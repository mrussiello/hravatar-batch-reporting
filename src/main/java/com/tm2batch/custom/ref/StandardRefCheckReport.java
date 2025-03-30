/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.ref;

import com.tm2batch.autoreport.AutoReportFacade;
import com.tm2batch.autoreport.ExecutableReport;
import com.tm2batch.custom.BaseExecutableReport;
import com.tm2batch.entity.ref.RcCheck;
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
public class StandardRefCheckReport extends BaseExecutableReport implements ExecutableReport {
    
    public static String DEFAULT_FILENAME_BASE = "ReferenceCheckResults";
    public static String DEFAULT_CONTENT_KEY = "g.StdRefCheckReport.content";
    public static String DEFAULT_CONTENTSUPP_KEY = "g.StdRefCheckReport.contentsupp";
    public static String DEFAULT_SUBJECT_KEY = "g.StdRefCheckReport.subject";
    
    RcFacade rcFacade;
    RcScriptFacade rcScriptFacade;
    RcCheckUtils rcCheckUtils;
    byte[] bytes;
    
    
    
    public StandardRefCheckReport()
    {
    }

    @Override
    public String toString() {
        return "StandardRefCheckReport{" + '}';
    }
    
    
    private void init()
    {
        // testResultUtils = new TestResultUtils();
        
        TimeZone tz = batchReport.getTimeZone();
        Locale loc = batchReport.getLocaleToUseDefaultUS();
        LogService.logIt( "StandardRefCheckReport.init() using locale=" + loc.toString() + " and TimeZone=" + tz.getID());
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
        
        LogService.logIt( "StandardRefCheckReport.executeReport() START batchReportId=" + this.batchReport.getBatchReportId() );
        
        try
        {
            validateBatchReportForExecution();

            init();
            
            Date[] dates = batchReport.getDates();
            
            if( rcFacade==null )
                rcFacade=RcFacade.getInstance();
            
            List<Long> userIdList = getUserIdListFromEmailListStr();
            
            List<RcCheck> rcl = rcFacade.findRcCheckList( batchReport.getIntParam1()==1 ? batchReport.getUserId() : 0, // long adminUserId, 
                                                0, // long userId, 
                                                batchReport.getOrgId(), // int orgId, 
                                                batchReport.getSuborgId(), // int suborgId, 
                                                null, //String lastNameKey,
                                                null, //String emailKey,
                                                userIdList,
                                                dates[0], //Date startDate,
                                                dates[1], //Date endDate,
                                                null, // String candidateAccessCode, 
                                                batchReport.getIntParam2(), // int rcCheckTypeId, 
                                                batchReport.getIntParam3(), // int rcCheckStatusTypeId,
                                                batchReport.getIntParam4(), // int minRcCheckStatusTypeId, 
                                                batchReport.getIntParam5(), // int maxRcCheckStatusTypeId, 
                                                batchReport.getIntParam6(), // int rcScriptId,
                                                0, // int maxRows,
                                                batchReport.getIntParam7() // int sortTypeId
                                            );

            
            LogService.logIt( "StandardRefCheckReport.executeReport() BBB Have " + rcl.size() + " Reference Checks to include. batchReportId=" + this.batchReport.getBatchReportId() );
            
            if( rcl.size()<=0 )
                return out;
            
            if( batchReport.getOrg()==null )
            {
                if( userFacade==null )
                    userFacade=UserFacade.getInstance();
                batchReport.setOrg( userFacade.getOrg( batchReport.getOrgId() ));
            }
            
            if( batchReport.getOrg().getRcOrgPrefs()==null )
                batchReport.getOrg().setRcOrgPrefs( rcFacade.getRcOrgPrefsForOrgId( batchReport.getOrgId() ));
            
            
            for( RcCheck rc : rcl )
            {
                if( rc.getOrg()==null )
                    rc.setOrg( batchReport.getOrg() );
                
                if( rc.getRcOrgPrefs()==null )
                    rc.setRcOrgPrefs( batchReport.getOrg().getRcOrgPrefs() );
                
                rc.setLocale( batchReport.getLocaleToUseDefaultUS() );
                
                loadRcCheck( rc );
            }
                        
            int records = rcl.size();
            
            RcCheckExporter rce = new RcCheckExporter( false );
            
            bytes = rce.getRcCheckExcelFile(rcl, batchReport.getLocaleToUseDefaultUS(), batchReport.getTimeZone(), batchReport.getOrg(), dates[0], dates[1] );
            
       // tre.getTestResultExcelFile( trl, batchReport.getLocaleToUseDefaultUS(), batchReport.getUser().getTimeZone(), batchReport.getUser().getUserReportOptions(), batchReport.getOrg() );
            
            if( bytes==null )
                throw new BatchReportException( batchReport.getBatchReportId(), "Bytes is null." );

            if( bytes.length==0 )
                throw new BatchReportException( batchReport.getBatchReportId(), "Bytes is empty." );
            
            LogService.logIt( "StandardRefCheckReport.executeReport() CCC Have report bytes: length=" + bytes.length + ", batchReportId=" + this.batchReport.getBatchReportId() );
            
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
            LogService.logIt( e, "StandardRefCheckReport.executeReport() batchReportId=" + batchReport.getBatchReportId() + ", " + batchReport.getTitle() + ", orgId=" + batchReport.getOrgId() );
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
        {
            LogService.logIt( "StandardRefCheckReport.validateBatchReportForExecution() BatchReport id=" + batchReport.getBatchReportId() + " does not look back at least one day or one hour or month or year. daysBack=" + batchReport.getDaysBack() + ", hoursBack=" + batchReport.getHoursBack() + ", setting to 1 day back." );
            batchReport.setDaysBack(1);
            if( autoReportFacade==null )
                autoReportFacade=AutoReportFacade.getInstance();
            autoReportFacade.saveBatchReport(batchReport);
        }        
        
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
    
    
    
}
