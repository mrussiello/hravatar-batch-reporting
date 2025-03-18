/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.testkey;

import com.tm2batch.account.results.TestReportingUtils;
import com.tm2batch.autoreport.AutoReportFacade;
import com.tm2batch.autoreport.ExecutableReport;
import com.tm2batch.custom.BaseExecutableReport;
import com.tm2batch.entity.event.TestKey;
import com.tm2batch.event.TestKeyStatusType;
import com.tm2batch.global.BatchReportException;
import com.tm2batch.global.Constants;
import com.tm2batch.service.LogService;
import com.tm2batch.util.I18nUtils;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author miker_000
 */
public class StandardUnfinishedTestKeyReport extends BaseExecutableReport implements ExecutableReport {
    
    
    public static String DEFAULT_FILENAME_BASE = "UnfinishedTestKeys";
    public static String DEFAULT_CONTENT_KEY = "g.StdUnfinishedTestKeyReport.content";
    public static String DEFAULT_CONTENTSUPP_KEY = "g.StdUnfinishedTestKeyReport.contentsupp";
    public static String DEFAULT_SUBJECT_KEY = "g.StdUnfinishedTestKeyReport.subject";
    
    byte[] bytes;
    
    public StandardUnfinishedTestKeyReport()
    {
    }

    @Override
    public String toString() {
        return "StandardUnfinishedTestKeyReport{" + '}';
    }
    
    
    private void init()
    {
        testReportingUtils = new TestReportingUtils();
        
        TimeZone tz = batchReport.getTimeZone();
        Locale loc = batchReport.getLocaleToUseDefaultUS();
        LogService.logIt( "StandardUnfinishedTestKeyReport.init() using locale=" + loc.toString() + " and TimeZone=" + tz.getID());
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
        
        LogService.logIt( "StandardUnfinishedTestKeyReport.executeReport() START batchReportId=" + this.batchReport.getBatchReportId() );
        
        try
        {
            validateBatchReportForExecution();

            init();
            
            Date[] dates = batchReport.getDates();
            
            
            List<TestKey> trl = testReportingUtils.getTestKeyList( batchReport, 
                                               batchReport.getIntParam1()==1, // boolean thisUserIdOnly, 
                                               batchReport.getIntParam2()<=0 ? TestKeyStatusType.STARTED.getTestKeyStatusTypeId() : TestKeyStatusType.ACTIVE.getTestKeyStatusTypeId(),
                                               batchReport.getStrParam1(), // String productNameKeyword,
                                               batchReport.getIntParam3(), // int productId, 
                                               batchReport.getIntParam4(), // int productTypeId,
                                               batchReport.getIntParam5(), // int consumerProductTypeId,
                                               batchReport.getIntParam7(), // int batteryId,
                                               0,
                                               dates[0], // Date startDate, 
                                               dates[1], // Date endDate, 
                                               0, // int testResultSortTypeId,
                                               Constants.DEFAULT_MAX_RESULT_ROWS // int maxRows,
                                               );
            
            LogService.logIt( "StandardUnfinishedTestKeyReport.executeReport() BBB Have " + trl.size() + " Test results. batchReportId=" + this.batchReport.getBatchReportId() );
            
            if( trl.size()<=0 )
                return out;
            
            for( TestKey tk : trl )
            {
                tk.setStartUrl( TestReportingUtils.getTestKeyStartUrl(tk));
            }

            
            TestKeyExporter tre = new TestKeyExporter();
            
            bytes = tre.getTestKeyExcelFile(trl, batchReport.getLocaleToUseDefaultUS(), batchReport.getUser().getTimeZone(), batchReport.getUser().getUserReportOptions(), batchReport.getOrg(), dates[0], dates[1] );
            
            if( bytes==null )
                throw new BatchReportException( batchReport.getBatchReportId(), "Bytes is null." );

            if( bytes.length==0 )
                throw new BatchReportException( batchReport.getBatchReportId(), "Bytes is empty." );
            
            LogService.logIt( "StandardUnfinishedTestKeyReport.executeReport() CCC Have report bytes: length=" + bytes.length + ", batchReportId=" + this.batchReport.getBatchReportId() );
            
            String excelFilename = getFilename();
            
            TimeZone tz = batchReport.getTimeZone();
            
            String sDateStr = I18nUtils.getFormattedDate(batchReport.getLocaleToUseDefaultUS(), dates[0], tz );
            String eDateStr = I18nUtils.getFormattedDate(batchReport.getLocaleToUseDefaultUS(), dates[1], tz );
            
            int sentCount = sendReportEmail( EXCEL_MIME_TYPE, excelFilename, DEFAULT_CONTENT_KEY, DEFAULT_CONTENTSUPP_KEY, DEFAULT_SUBJECT_KEY, Integer.toString( trl.size()), sDateStr, eDateStr, bytes );
            // String attachMime, String attachFn, String contentKey, String subjectKey, String parm1, String parm2, byte[] bytes
            
            if( sentCount>0 )
                out[0]=1;
        }
        
        catch( Exception e )
        {
            LogService.logIt( e, "StandardUnfinishedTestKeyReport.executeReport() batchReportId=" + batchReport.getBatchReportId() + ", " + batchReport.getTitle() + ", orgId=" + batchReport.getOrgId() );
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
    public void validateBatchReportForExecution() throws Exception
    {
        // check core
        super.validateBatchReportForExecution();
        
        // specific checks
        if( batchReport.getYearsBack()<=0 && batchReport.getMonthsBack()<=0 && batchReport.getDaysBack()<=0 && batchReport.getHoursBack()<=0 )
        {
            LogService.logIt( "StandardUnfinishedTestKeyReport.validateBatchReportForExecution() BatchReport id=" + batchReport.getBatchReportId() + " does not look back at least one day or one hour or month or year. daysBack=" + batchReport.getDaysBack() + ", hoursBack=" + batchReport.getHoursBack() + ", setting to 1 day back." );
            batchReport.setDaysBack(1);
            if( autoReportFacade==null )
                autoReportFacade=AutoReportFacade.getInstance();
            autoReportFacade.saveBatchReport(batchReport);
        }        
        
        if( batchReport.getUser().getUserReportOptions()==null )
            throw new BatchReportException( batchReport.getBatchReportId(), "BatchReport User (userId=" + batchReport.getUser().getUserId() + ", orgId=" + batchReport.getOrgId() + ") does not have any UserReportOptions. Please create a UserReportOptions object for this User." );
            

    }
    
    
    
}
