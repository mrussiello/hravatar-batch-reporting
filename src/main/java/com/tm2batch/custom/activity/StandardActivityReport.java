/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.activity;

import com.tm2batch.account.results.TestReportingUtils;
import com.tm2batch.autoreport.ExecutableReport;
import com.tm2batch.custom.BaseExecutableReport;
import com.tm2batch.global.BatchReportException;
import com.tm2batch.service.LogService;
import com.tm2batch.util.I18nUtils;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 *
 * @author miker_000
 */
public class StandardActivityReport extends BaseExecutableReport implements ExecutableReport {
    
    
    public static String DEFAULT_FILENAME_BASE = "ActivityReport";
    public static String DEFAULT_CONTENT_KEY = "g.StdActivityReport.content";
    public static String DEFAULT_CONTENTSUPP_KEY = "g.StdActivityReport.contentsupp";
    public static String DEFAULT_SUBJECT_KEY = "g.StdActivityReport.subject";
    
    byte[] bytes = null;
    
    public StandardActivityReport()
    {
    }

    @Override
    public String toString() {
        return "StandardActivityReport{" + '}';
    }
    
    
    private void init()
    {
        testReportingUtils = new TestReportingUtils();
        
        TimeZone tz = batchReport.getTimeZone();
        Locale loc = batchReport.getLocaleToUseDefaultUS();
        LogService.logIt( "StandardActivityReport.init() using locale=" + loc.toString() + " and TimeZone=" + tz.getID());
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
                
        int[] out = new int[4];
        
        LogService.logIt( "StandardActivityReport.executeReport() START batchReportId=" + this.batchReport.getBatchReportId() );
        
        try
        {
            validateBatchReportForExecution();

            init();
            
            Date[] dates = batchReport.getDates();
            
            DashboardFacade dashboardFacade = DashboardFacade.getInstance();
            
            Map<String, Object> dm = dashboardFacade.getDashboardDataMap( batchReport.getOrg(),
                                                                            batchReport.getSuborgId(),
                                                                            batchReport.getSuborgId()>0,
                                                                            dates[0],
                                                                            dates[1],
                                                                            batchReport.getLocaleToUseDefaultUS() );
            
            
            
            
            ActivityReportExporter are = new ActivityReportExporter();
            
            bytes = are.getActivityExcelFile(dm, batchReport.getLocaleToUseDefaultUS(), batchReport.getUser().getTimeZone(), batchReport.getUser().getUserReportOptions(), batchReport.getOrg(), dates[0], dates[1] );
            
            if( bytes==null )
                throw new BatchReportException( batchReport.getBatchReportId(), "Bytes is null." );

            if( bytes.length==0 )
                throw new BatchReportException( batchReport.getBatchReportId(), "Bytes is empty." );
            
            LogService.logIt( "StandardActivityReport.executeReport() CCC Have report bytes: length=" + bytes.length + ", batchReportId=" + this.batchReport.getBatchReportId() );
            
            String excelFilename = batchReport.getTitle();
            if( excelFilename==null || excelFilename.isBlank() )
                excelFilename = DEFAULT_FILENAME_BASE;
            
            excelFilename += "-" + getFilenameDateStr() + ".xlsx";
            
            TimeZone tz = batchReport.getTimeZone();
            
            String sDateStr = I18nUtils.getFormattedDate(batchReport.getLocaleToUseDefaultUS(), dates[0], tz );
            String eDateStr = I18nUtils.getFormattedDate(batchReport.getLocaleToUseDefaultUS(), dates[1], tz );
            
            int sentCount = sendReportEmail( EXCEL_MIME_TYPE, excelFilename, DEFAULT_CONTENT_KEY, DEFAULT_CONTENTSUPP_KEY, DEFAULT_SUBJECT_KEY, Integer.toString(0), sDateStr, eDateStr, bytes );
            // String attachMime, String attachFn, String contentKey, String subjectKey, String parm1, String parm2, byte[] bytes
            
            if( sentCount>0 )
                out[0]=1;
        }
        
        catch( Exception e )
        {
            LogService.logIt( e, "StandardActivityReport.executeReport() batchReportId=" + batchReport.getBatchReportId() + ", " + batchReport.getTitle() + ", orgId=" + batchReport.getOrgId() );
        }
        
        return out;
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
    
    @Override
    public byte[] getBytes()
    {
        return bytes;
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
    
    
    
}
