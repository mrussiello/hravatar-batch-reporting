/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.ref;

import com.tm2batch.autoreport.ExecutableReport;
import com.tm2batch.custom.BaseExecutableReport;
import com.tm2batch.entity.ref.RcReferral;
import com.tm2batch.global.BatchReportException;
import com.tm2batch.ref.RcCheckUtils;
import com.tm2batch.ref.RcFacade;
import com.tm2batch.ref.RcScriptFacade;
import com.tm2batch.service.LogService;
import com.tm2batch.user.UserFacade;
import com.tm2batch.util.I18nUtils;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author miker_000
 */
public class StandardRefCheckReferralReport extends BaseExecutableReport implements ExecutableReport {
    
    public static String DEFAULT_FILENAME_BASE = "ReferenceCheckReferrals";
    public static String DEFAULT_CONTENT_KEY = "g.StdRefCheckReferralsReport.content";
    public static String DEFAULT_CONTENTSUPP_KEY = "g.StdRefCheckReferralsReport.contentsupp";
    public static String DEFAULT_SUBJECT_KEY = "g.StdRefCheckReferralsReport.subject";
    
    RcFacade rcFacade;
    RcScriptFacade rcScriptFacade;
    RcCheckUtils rcCheckUtils;
    byte[] bytes;
    
    
    
    public StandardRefCheckReferralReport()
    {
    }

    @Override
    public String toString() {
        return "StandardRefCheckReferralReport{" + '}';
    }
    
    
    private void init()
    {
        // testResultUtils = new TestResultUtils();
        
        TimeZone tz = batchReport.getTimeZone();
        Locale loc = batchReport.getLocaleToUseDefaultUS();
        LogService.logIt( "StandardRefCheckReferralReport.init() using locale=" + loc.toString() + " and TimeZone=" + tz.getID());
    }


    @Override
    public byte[] getBytes()
    {
        return bytes;
    }
    
    /**
     * data[0] = 0 or 1 for success
     * 
     * @param batchReport
     * @return
     * @throws Exception 
     */
    @Override
    public int[] executeReport() throws Exception {
        
        // this.batchReport = batchReport;
        
        int[] out = new int[4];
        
        LogService.logIt( "StandardRefCheckReferralReport.executeReport() START batchReportId=" + this.batchReport.getBatchReportId() );
        
        try
        {
            validateBatchReportForExecution();

            init();
            
            Date[] dates = batchReport.getDates();
            
            if( rcFacade==null )
                rcFacade=RcFacade.getInstance();
            
            List<RcReferral> rfl = rcFacade.findRcReferralList(  batchReport.getOrgId(), 
                                                batchReport.getIntParam1()==1 ? batchReport.getUserId() : 0,
                                                dates[0], //Date startDate,
                                                dates[1] );
            
            
            LogService.logIt( "StandardRefCheckReferralReport.executeReport() BBB Have " + rfl.size() + " RcReferrals to include. batchReportId=" + this.batchReport.getBatchReportId() );
            
            if( rfl.size()<=0 )
                return out;
            
            if( batchReport.getOrg()==null )
            {
                if( userFacade==null )
                    userFacade=UserFacade.getInstance();
                batchReport.setOrg( userFacade.getOrg( batchReport.getOrgId() ));
            }
            
            if( batchReport.getOrg().getRcOrgPrefs()==null )
                batchReport.getOrg().setRcOrgPrefs( rcFacade.getRcOrgPrefsForOrgId( batchReport.getOrgId() ));
                       
                        
            int records = rfl.size();
            
            RcReferralExporter rce = new RcReferralExporter();
            
            bytes = rce.getRcReferralExcelFile(rfl, batchReport.getLocaleToUseDefaultUS(), batchReport.getTimeZone(), batchReport.getOrg(), dates[0], dates[1], batchReport.getIntParam1()==1 );
            
            if( bytes==null )
                throw new BatchReportException( batchReport.getBatchReportId(), "Bytes is null." );

            if( bytes.length==0 )
                throw new BatchReportException( batchReport.getBatchReportId(), "Bytes is empaty." );
            
            LogService.logIt( "StandardRefCheckReferralReport.executeReport() CCC Have report bytes: length=" + bytes.length + ", batchReportId=" + this.batchReport.getBatchReportId() );
            
            String excelFilename = batchReport.getTitle();
            if( excelFilename==null || excelFilename.isBlank() )
                excelFilename = DEFAULT_FILENAME_BASE;
            
            excelFilename += "-" + getFilenameDateStr() + ".xlsx";
            
            TimeZone tz = batchReport.getTimeZone();
            
            String sDateStr = I18nUtils.getFormattedDate(batchReport.getLocaleToUseDefaultUS(), dates[0], tz );
            String eDateStr = I18nUtils.getFormattedDate(batchReport.getLocaleToUseDefaultUS(), dates[1], tz );
            
            int sentCount = sendReportEmail( EXCEL_MIME_TYPE, excelFilename, DEFAULT_CONTENT_KEY, DEFAULT_CONTENTSUPP_KEY, DEFAULT_SUBJECT_KEY, Integer.toString( records ), sDateStr, eDateStr, bytes );
            // String attachMime, String attachFn, String contentKey, String subjectKey, String parm1, String parm2, byte[] bytes
            
            if( sentCount>0 )
                out[0]=1;
        }
        
        catch( Exception e )
        {
            LogService.logIt( e, "StandardRefCheckReferralReport.executeReport() batchReportId=" + batchReport.getBatchReportId() + ", " + batchReport.getTitle() + ", orgId=" + batchReport.getOrgId() );
        }
        
        return out;
    }
    
    
    @Override
    public void validateBatchReportForExecution() throws Exception
    {
        // check core
        super.validateBatchReportForExecution();
        
        // specific checks
        if( batchReport.getYearsBack()<=0 && batchReport.getMonthsBack()<=0 && batchReport.getDaysBack()<=0 && batchReport.getHoursBack()<=0 )
            throw new BatchReportException( batchReport.getBatchReportId(), "BatchReport does not look back at least one day or one hour. daysBack=" + batchReport.getDaysBack() + ", hoursBack=" + batchReport.getHoursBack() );        
        
        if( batchReport.getUser().getUserReportOptions()==null )
            throw new BatchReportException( batchReport.getBatchReportId(), "BatchReport User (userId=" + batchReport.getUser().getUserId() + ", orgId=" + batchReport.getOrgId() + ") does not have any UserReportOptions. Please create a UserReportOptions object for this User." );
            

    }
    
    @Override
    public String getFilename()
    {
        String excelFilename = batchReport.getTitle();
        if( excelFilename==null || excelFilename.isBlank() )
            excelFilename = DEFAULT_FILENAME_BASE;

        excelFilename += "-" + getFilenameDateStr() + ".xlsx";

        return excelFilename;
    }    
    
    /*
    private void loadRcCheck( RcCheck rc ) throws Exception
    {
        if( rc.getRcScript()==null )
        {
            if( rcScriptFacade==null )
                rcScriptFacade=RcScriptFacade.getInstance();
            rc.setRcScript( rcScriptFacade.getRcScript( rc.getRcScriptId() ) );
        }
        
        if( rcCheckUtils==null )
            rcCheckUtils = new RcCheckUtils();
        
        rcCheckUtils.loadRcCheckForScoringOrResults(rc);
        
    }
    */
    
    
    
}
