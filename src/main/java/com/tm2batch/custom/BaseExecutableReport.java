/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom;

import com.tm2batch.account.results.TestReportingUtils;
import com.tm2batch.autoreport.AutoReportFacade;
import com.tm2batch.autoreport.BatchReportStatusType;
import com.tm2batch.autoreport.FrequencyType;
import com.tm2batch.email.EmailBlockFacade;
import com.tm2batch.entity.autoreport.BatchReport;
import com.tm2batch.entity.user.User;
import com.tm2batch.global.BatchReportException;
import com.tm2batch.service.EmailUtils;
import com.tm2batch.service.LogService;
import com.tm2batch.user.UserActionFacade;
import com.tm2batch.user.UserActionType;
import com.tm2batch.user.UserFacade;
import com.tm2batch.util.MessageFactory;
import com.tm2batch.util.StringUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author miker_000
 */
public class BaseExecutableReport {
    
    public boolean sampleReport = false;
    public BatchReport batchReport;
    public boolean overrideSendFreq;
    
    public TestReportingUtils testReportingUtils;
    
    public AutoReportFacade autoReportFacade;
    public UserFacade userFacade;
    public EmailBlockFacade emailBlockFacade;
    public UserActionFacade userActionFacade;
    
    public static String EXCEL_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static String PDF_MIME_TYPE = "application/pdf";
    
    public String getFilenameDateStr()
    {
        Calendar cal = new GregorianCalendar();
        return (cal.get( Calendar.MONTH) + 1) + "-" + cal.get( Calendar.DAY_OF_MONTH) + "-" + cal.get( Calendar.YEAR );
    }
    
        
    public void validateBatchReportForExecution() throws Exception
    {
        if( batchReport==null )
            throw new Exception( "BatchReport is null" );
        
        if( batchReport.getUser()==null )
            throw new BatchReportException( batchReport.getBatchReportId(), "BatchReport.user is null" );
        
        if( batchReport.getBatchReportStatusTypeId()!=BatchReportStatusType.ACTIVE.getBatchReportStatusTypeId() )
            throw new BatchReportException( batchReport.getBatchReportId(), "BatchReport.status is inactive" );
                 
        if( batchReport.getScheduleDate()!=null && batchReport.getScheduleDate().before( new Date()) )
            return;
        
        // check freq.
        FrequencyType freqType = FrequencyType.getValue( batchReport.getFrequencyTypeId() );
        
        if( !overrideSendFreq && !batchReport.getSpecialProcessingCheck() && freqType.equals( FrequencyType.NEVER) )
            throw new BatchReportException( batchReport.getBatchReportId(), "BatchReport.Frequency is NEVER" );
        
        if( !overrideSendFreq && !batchReport.getSpecialProcessingCheck() && batchReport.getLastSendDate()!=null && batchReport.getLastSendDate().after( freqType.getCutoffDate() ))
            throw new BatchReportException( batchReport.getBatchReportId(), "BatchReport.Frequency is " + freqType.getName() + ", but last execution date was " + batchReport.getLastSendDate().toString() );
                
    }
        
    /*
      Returns the number of email addresses sent to.
    */
    public int sendReportEmail( String attachMime, String attachFn, String contentKey, String contentSupKey, String subjectKey, String parm1, String parm2, String parm3, byte[] bytes ) throws Exception
    {
        String toEmails = batchReport.getEmails();
        if( toEmails==null || toEmails.isBlank() )
            toEmails = batchReport.getUser().getEmail();
        
        if( emailBlockFacade==null )
            emailBlockFacade = EmailBlockFacade.getInstance();
        
        if( userFacade==null )
            userFacade = UserFacade.getInstance();
        
        List<User> users = new ArrayList<>();
        User user2;
        String[] ems = toEmails.split(",");
        StringBuilder sb = new StringBuilder();
        int emailsSentTo = 0;
        for( String em : ems )
        {
            em=em.trim();
            if( em.isBlank() )
                continue;
            if( !em.contains("@"))
            {
                LogService.logIt( "BaseExecutableReport.sendReportEmail() AAA.1 Skipping invalid email=" + em  );
                continue;
            }
            if( !EmailUtils.validateEmailNoErrors(em) )
            {
                LogService.logIt( "BaseExecutableReport.sendReportEmail() AAA.2 Skipping invalid email=" + em  );
                continue;
            }
            if( emailBlockFacade.hasEmailBlock(em, true) )
            {
                LogService.logIt( "BaseExecutableReport.sendReportEmail() AAA.3 Email is blocked. Skipping. batchReportId=" + batchReport.getBatchReportId() + ", Email=" + em );
                continue;
            }

            if( sb.length()>0 )
                sb.append(",");
            
            sb.append( em );
            
            emailsSentTo++;
            
            user2 = userFacade.getUserByEmailAndOrgId(em, batchReport.getOrgId() );
            if( user2!=null )
            {
                users.add( user2 );
            }
        }
        
        if( sb.length()<=0 )
        {
            batchReport.setBatchReportStatusTypeId(BatchReportStatusType.DISABLED.getBatchReportStatusTypeId() );
            String n = batchReport.getNote()==null ? "" : batchReport.getNote();
            n += ((new Date()).toString()) + " No valid emails to send to. Disabling report.";
            batchReport.setNote(n);
            AutoReportFacade.getInstance().saveBatchReport(batchReport);
            LogService.logIt( "BaseExecutableReport.sendReportEmail() No valid emails to send to. Disabling report and returning 0. toEmails="  + toEmails + ", batchReport.batchReportId=" + batchReport.getBatchReportId() );
            return 0;
        }
                
        
        if( parm1==null )
            parm1 = "";
        
        if( parm2==null )
            parm2 = "";
        
        if( parm3==null )
            parm3 = "";
        
        String content = batchReport.getMessageContent();
        String subject = batchReport.getMessageSubject();
        
        Locale locale = batchReport.getLocaleToUseDefaultUS();
        String[] params = new String[]{batchReport.getUser().getFullname(), batchReport.getTitle(), parm1, parm2, parm3 }; 
        
        if( content==null || content.isBlank() )
            content = MessageFactory.getStringMessage( locale, contentKey, params );
        
        else
        {
            if( !content.contains("<") || !content.contains( ">" ) )
                content = StringUtils.replaceStandardEntities(content);
            
            content += MessageFactory.getStringMessage( locale, contentSupKey, params );
        }

        if( subject==null || subject.isBlank() )
            subject = MessageFactory.getStringMessage( locale, subjectKey, params );
        
        String contentMime = content.contains("<") && content.contains( ">" ) ? "text/html" : "text/plain";

        LogService.logIt( "BaseExecutableReport.sendReportEmail() BBB.2 Sending to " + emailsSentTo + " users. Emails=" + sb.toString() );
        
        EmailUtils emu = new EmailUtils();     
        emu.sendEmailWithSingleAttachment(subject, content, sb.toString(), contentMime, attachMime, attachFn, bytes);
        
        if( !users.isEmpty() )
            LogService.logIt( "BaseExecutableReport.sendReportEmail() BBB.3 Saving UserActions for " + users.size() + " users." );
        
        for( User u : users )
        {
            if( userActionFacade==null )
                userActionFacade = UserActionFacade.getInstance();            
            userActionFacade.saveMessageAction( batchReport.getUserId(), u, subject, UserActionType.SENT_EMAIL.getUserActionTypeId(), 0, 0, 0, null, null);
        }
        
        return emailsSentTo;        
    }

    public boolean getSampleReport() {
        return sampleReport;
    }

    public void setSampleReport(boolean sampleReport) {
        this.sampleReport = sampleReport;
    }


    
    public void setBatchReport(BatchReport batchReport) {
        this.batchReport = batchReport;
    }

    public void setOverrideSendFreq(boolean overrideSendFreq) {
        this.overrideSendFreq = overrideSendFreq;
    }
    
}
