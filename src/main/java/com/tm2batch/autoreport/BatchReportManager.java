/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.autoreport;

import com.tm2batch.entity.autoreport.BatchReport;
import com.tm2batch.entity.user.UserReportOptions;
import com.tm2batch.global.STException;
import com.tm2batch.service.EmailUtils;
import com.tm2batch.service.LogService;
import static com.tm2batch.service.LogService.logIt;
import com.tm2batch.service.Tracker;
import com.tm2batch.user.UserFacade;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

/**
 *
 * @author miker_000
 */
public class BatchReportManager 
{
    public static boolean FIRST_BATCH = true;
    public static boolean BATCH_IN_PROGRESS = false;
    public static boolean OK_TO_START_ANY = true;
    public static boolean DEBUG_SCORING = false;
    public static int MAX_FAILURES = 1;
    public static int FAILURES = 0;
    
    // UserFacade userFacade = null;
    AutoReportFacade autoReportFacade = null;
    UserFacade userFacade = null;
    
    ExecutableReport er;
    
    // FileXferUtils fxUtils = null;
    
    

    public static synchronized void init() throws Exception
    {
    }
    
    public int[] doReportBatch()
    {
        int[] count = new int[4];

        try
        {
            LogService.logIt( "BatchReportManager.doReportBatch() START" );
            init();
            
            // If batches have been turned off.
            if( !OK_TO_START_ANY )
                return count;

            if( BATCH_IN_PROGRESS )
            {
                return count;
            }
            
            // raise back up. Will lower if we have too many test events.
            // DiscernFacade.ESSAY_PLAG_CHECK_MAX_OFFSET = 5000;
            

            Tracker.startBatch();

            BATCH_IN_PROGRESS = true;

            count = reportBatch();

            Thread.sleep( 200 );

            BATCH_IN_PROGRESS = false;

            FIRST_BATCH = false;
            
            LogService.logIt( "BatchReportManager.doReportBatch() Completed score batch." );
        }

        catch( STException e )
        {
            // do nothing. Already logged.
            BATCH_IN_PROGRESS = false;
        }

        catch( Exception e )
        {
            BATCH_IN_PROGRESS = false;
            LogService.logIt(e, "BatchReportManager.doReportBatch()" );
            
            (new EmailUtils()).sendEmailToAdmin( "BatchReportManager.doReportBatch() Error during score Batch.", "Time: " + (new Date()).toString() + ", Error was: " + e.toString() );
        }

        return count;
    }
    
    /*
      data[0] = BatchReports checked.
      data[1] = BatchReports started.
      data[2] = BatchReports completed.
      data[3] = errors
    */
    private int[] reportBatch() throws Exception
    {        
        int[] out = new int[4];
        
        if( !OK_TO_START_ANY )
            return out;

        
        try
        {
            init();
            
            // first clear all cache
            if( userFacade==null )
                userFacade=UserFacade.getInstance();            
            userFacade.clearSharedCache();
            
            if( autoReportFacade==null )
                autoReportFacade = AutoReportFacade.getInstance();
            
            // get all Batch Reports that have a defined freq and are in active status.
            List<BatchReport> brl = autoReportFacade.getActiveBatchReportList();
            
            // all active batch reports that have a valid send date (before now).
            List<BatchReport> brl2 = autoReportFacade.getOneTimeActiveBatchReportList();

            LogService.logIt("BatchReportManager.reportBatch() AAA.1 Found " + brl.size() + " active BatchReports and " + brl2.size() + " Active One-Time reports to process." );
            
            // get the one times and review them for need to execute.
            for( BatchReport br : brl2 )
            {
                // no schedule date. Should not happen for this group of Batch Reports
                if( br.getScheduleDate()==null )
                {
                    LogService.logIt("BatchReportManager.reportBatch() UERR skipping scheduleDate batchReportId=" + br.getBatchReportId() + " because scheduleDate is null. Something WRONG." );
                    continue;
                }
                
                // send after schedule date. 
                if( br.getLastSendDate()!=null && br.getLastSendDate().after(br.getScheduleDate()) )
                {
                    LogService.logIt("BatchReportManager.reportBatch() removing scheduleDate for batchReportId=" + br.getBatchReportId() + " because lastSendDate (" + br.getLastSendDate().toString() + ") is greater than schedule date (" + br.getScheduleDate().toString() + ")." );
                    br.setScheduleDate(null);
                    autoReportFacade.saveBatchReport(br);
                    continue;
                }
                
                // already in list as a n active.
                if( brl.contains(br) )
                    continue;
                
                brl.add(br);                
            }
            
            out[0] = brl.size();
            
            //ListIterator<BatchReport> iter = brl.listIterator();
            //BatchReport br;
            //while( iter.hasNext() )
            //{
            //    br = iter.next();
            //    
           //     if( !readyForExecution(br) )
           //         iter.remove();
           // }
            
            LogService.logIt("BatchReportManager.reportBatch() AAA.2 Found " + brl.size() + " BatchReports need to be executed." );

            // boolean success;             
            for( BatchReport batchReport : brl )
            {
                
                // LogService.logIt("BatchReportManager.reportBatch() AAA.3 Starting BatchReport " + batchReport.getTitle() + " (" + batchReport.getBatchReportId() + ")" );

                
                if( !readyForExecution(batchReport) )
                    continue;
                
                LogService.logIt("BatchReportManager.reportBatch() AAA.4 BatchReport is Ready for Execution. " + batchReport.getTitle() + " (" + batchReport.getBatchReportId() + ")" );
                
                out[1]++;
                byte[] bytes = executeSingleBatchReport(  batchReport, false );
                
                if( bytes!=null && bytes.length>0 )
                    out[2]++;
                else
                    out[3]++;
            }            
        }
        catch( STException e )
        {
            throw e;
        }
        catch( Exception e )
        {
            LogService.logIt(e, "BatchReportManager.reportBatch() " );
            throw e;
        }
        
        return out;
    }
    
    
    private boolean readyForExecution(BatchReport br) throws Exception
    {
        if( br.getBatchReportStatusTypeId()!=BatchReportStatusType.ACTIVE.getBatchReportStatusTypeId() )
        {
            // LogService.logIt( "BatchReportManager.readyForExecution() Not Ready because status is not ACTIVE. batchReportId=" + br.getBatchReportId() + ", " + br.getTitle() );
            return false;
        }
        
        //if( br.getReportClassName()==null || br.getReportClassName().isBlank() )
        //    return false;
        
        FrequencyType freqType = FrequencyType.getType(br.getFrequencyTypeId());
        
        if( br.getUser()==null )
        {
            if( userFacade==null )
                userFacade=UserFacade.getInstance();
            
            br.setUser( userFacade.getUser( br.getUserId() ));
        }
        
        // if it has an existing schedule date prior to now.
        if( br.getScheduleDate()!=null && br.getScheduleDate().before( new Date()))
            return true;
        
        // only freq based sends here. br.scheduleDate=null
        
        // Not ok to send today based on freq
        if( !freqType.isTodayOkToSend( br.getTimeZone() ) )
        {
            // LogService.logIt( "BatchReportManager.readyForExecution() Not Ready because freq type returned NOT OK to send. TimeZone=" + br.getTimeZoneId() +", batchReportId=" + br.getBatchReportId() + ", " + br.getTitle() );
            return false;
        }

        // Not ok to send today based on freq
        if( !freqType.isThisHourOkToSend( br.getHourToSend() ) )
        {
            // LogService.logIt( "BatchReportManager.readyForExecution() Not Ready because freq type returned NOT OK to send based on set hour to send=" + br.getHourToSend() + ", current date/time=" + (new Date()).toString() + ", batchReportId=" + br.getBatchReportId() + ", " + br.getTitle() );
            return false;
        }

        
        if( freqType.equals( FrequencyType.NEVER) )
        {
            LogService.logIt( "BatchReportManager.readyForExecution() Not Ready because freq type is NEVER, batchReportId=" + br.getBatchReportId() + ", " + br.getTitle() );
            return false;
        }
        
        // Never been sent.
        if( br.getLastSendDate()==null )
            return true;
        
        Date cutoff = freqType.getCutoffDate();
        
        if( br.getLastSendDate().before( cutoff ) )
            return true;
        
        LogService.logIt( "BatchReportManager.readyForExecution() Not Ready because lastSendDate=" + br.getLastSendDate().toString() + " is after cutoff=" + cutoff.toString() + ", batchReportId=" + br.getBatchReportId() + ", " + br.getTitle() );
        return false;        
    }
    

    public byte[] executeSingleBatchReport( BatchReport br, boolean overrideSendFreq ) throws Exception
    {        
        if( !OK_TO_START_ANY )
            return null;
        
        try
        {
            init();  
            
            if( userFacade==null )
                userFacade=UserFacade.getInstance();
            
            br.setUser( userFacade.getUser( br.getUserId() ));

            if( autoReportFacade==null )
                autoReportFacade=AutoReportFacade.getInstance();
            
            if( br.getUser()==null )
                throw new Exception( "User not found for userId=" + br.getUserId() + ", batchReportId=" + br.getBatchReportId() );

            if( br.getUser().getOrgId()!=br.getOrgId() )
                throw new Exception( "BatchReport.User.orgId does not match OrgId in BatchReport. batchReportId=" + br.getBatchReportId() );

            if( !br.getUser().getRoleType().getIsAccountUser() )
            {
                br.setBatchReportStatusTypeId(BatchReportStatusType.DISABLED.getBatchReportStatusTypeId() );
                String n = br.getNote()==null ? "" : br.getNote();
                n += ((new Date()).toString()) + " User must be a valid Account user. Batch Report has been disabled.";
                br.setNote(n);                
                autoReportFacade.saveBatchReport(br);
                LogService.logIt( "BatchReportManager.executeSingleBatchReport() batchReportId=" + br.getBatchReportId() + ", Originating user must be a valid account user for this org. Batch Report has been disabled. batchReport.userId=" + br.getUserId() );
                return null;
            }
            
            if( !br.getUser().getUserType().getNamed() )
            {
                br.setBatchReportStatusTypeId(BatchReportStatusType.DISABLED.getBatchReportStatusTypeId() );
                String n = br.getNote()==null ? "" : br.getNote();
                n += ((new Date()).toString()) + " User must be a valid Account user. Batch Report has been disabled.";
                br.setNote(n);                
                autoReportFacade.saveBatchReport(br);
                LogService.logIt( "BatchReportManager.executeSingleBatchReport() batchReportId=" + br.getBatchReportId() + ", User must be Named. Batch Report has been disabled. batchReport.userId=" + br.getUserId() );
                return null;
            }
            
            br.getUser().setUserReportOptions( autoReportFacade.getUserReportOptions( br.getUserId() ));

            if( br.getUser().getUserReportOptions()==null )
            {
                UserReportOptions uro = new UserReportOptions();
                uro.setUserId( br.getUserId() );
                autoReportFacade.saveUserReportOptions(uro);
                br.getUser().setUserReportOptions(uro);
            }
            
            br.setOrg( userFacade.getOrg( br.getOrgId() ));

            if( br.getOrg()==null )
                throw new Exception( "Cannot find a org with orgId=" + br.getOrgId() + ", batchReportId=" + br.getBatchReportId() );

            if( br.getSuborgId()>0 )
            {
                br.setSuborg( userFacade.getSuborg( br.getSuborgId() ));

                if( br.getSuborg()==null )
                    throw new Exception( "Cannot find a suborg with SuborgId=" + br.getSuborgId() + ", batchReportId=" + br.getBatchReportId() );

                if( br.getSuborg()!=null && br.getSuborg().getOrgId()!=br.getOrgId() )
                    throw new Exception( "BatchReport.suborgId references orgId=" + br.getSuborg().getOrgId() + ", but batchReport references orgId=" + br.getOrgId() + ", batchReportId=" + br.getBatchReportId());
            }            
            
            Tracker.addBatchReportStarted();
            
            er = ExecutableReportFactory.getExecutableReport( br, overrideSendFreq );
                
            LogService.logIt("BatchReportManager.executeSingleBatchReport() AAA.1 Have ExecutableReport. " + er.toString() + ", Starting. " );

            // Check that there is nothing stopping execution.
            er.validateBatchReportForExecution();
            
            // out[0]==1 for success
            int[] out = er.executeReport();

            LogService.logIt("BatchReportManager.executeSingleBatchReport() AAA.1 Completed BatchReport " + br.getTitle() + " (" + br.getBatchReportId() + ") success=" + (out[0]==1) );
            
            // if successful.
            if( out[0]==1 )
            {
                br.setLastSendDate(new Date());
                
                if( br.getScheduleDate()!=null && br.getScheduleDate().before(new Date()))
                {
                    LogService.logIt("BatchReportManager.executeSingleBatchReport() removing scheduleDate for batchReportId=" + br.getBatchReportId() + " because this run qualifies as a one-time." );
                    br.setScheduleDate(null);                   
                }
                
                autoReportFacade.saveBatchReport(br);
                
                Tracker.addBatchReportComplete();
            }
            
            return out[0]==1 ? er.getBytes() : null;
        }
        catch( STException e )
        {
            Tracker.addError();
            FAILURES++;
            
            if( FAILURES>= MAX_FAILURES )
                throw e;
            
            return null;
        }
        catch( Exception e )
        {
            FAILURES++;
            LogService.logIt(e, "BatchReportManager.executeSingleBatchReport() batchReportId=" + br.getBatchReportId() + ", Failures=" + FAILURES );
            
            String msg = "Error Messge is: " + e.getMessage() + "\n\nbatchReportId=" + br.getBatchReportId() + "\n";
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter( sw );
            e.printStackTrace( pw );
            logIt( msg + "\n" + sw.toString() );
            
            sendEmailToAdmin( "BatchReport Failure " + br.getTitle() + " (" + br.getBatchReportId() + ")", msg );

            if( FAILURES>=MAX_FAILURES )
                throw e;
            
            return null;
        }
    }

    
    public String getFilename()
    {
        return er==null ? null : er.getFilename();
    }

    private void sendEmailToAdmin( String subj, String content )
    {
        try
        {
            EmailUtils emu = new EmailUtils();
            emu.sendEmailToAdmin(subj, content );
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BatchReportManager.sendEmailToAdmin() ");
        }
    }
    
    
}
