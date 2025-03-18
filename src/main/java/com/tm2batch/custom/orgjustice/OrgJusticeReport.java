/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.orgjustice;

import com.tm2batch.autoreport.AutoReportFacade;
import com.tm2batch.autoreport.ExecutableReport;
import com.tm2batch.custom.BaseExecutableReport;
import com.tm2batch.entity.report.Report;
import com.tm2batch.event.EventFacade;
import com.tm2batch.global.BatchReportException;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.pdf.ReportData;
import com.tm2batch.pdf.ReportTemplate;
import com.tm2batch.service.LogService;
import com.tm2batch.user.UserFacade;
import com.tm2batch.util.I18nUtils;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

/**
 *
 * @author miker_000
 */
public class OrgJusticeReport extends BaseExecutableReport implements ExecutableReport {
    
    public static Boolean DEV = null;
    
    public static String DEFAULT_FILENAME_BASE = "OrgJusticeFeedbackReport";
    public static String DEFAULT_CONTENT_KEY = "g.OrgJusticeReport.content";
    public static String DEFAULT_CONTENTSUPP_KEY = "g.OrgJusticeReport.contentsupp";
    public static String DEFAULT_SUBJECT_KEY = "g.OrgJusticeReport.subject";
    
    byte[] bytes;
    
    // OrgJusticeFacade orgJusticeFacade;
    EventFacade eventFacade;
    
    OrgJusticeUtils orgJusticeUtils;
    
    
    public OrgJusticeReport()
    {
    }

    @Override
    public String toString() {
        return "OrgJusticeReport{" + '}';
    }
    
    
    private synchronized void init()
    {
        // testResultUtils = new TestResultUtils();
        
        if( DEV==null )
            DEV = RuntimeConstants.getBooleanValue("Uminn_OrgJustice_Dev" );
        
        TimeZone tz = batchReport.getTimeZone();
        Locale loc = batchReport.getLocaleToUseDefaultUS();
        LogService.logIt( "OrgJusticeReport.init() using locale=" + loc.toString() + " and TimeZone=" + tz.getID());
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
        
        LogService.logIt( "OrgJusticeReport.executeReport() START batchReportId=" + this.batchReport.getBatchReportId() );
        
        try
        {
            validateBatchReportForExecution();

            init();
            
            Date[] dates = batchReport.getDates();
            
            OrgJusticeDataset dataSet = collectDataSet(dates);
            
            LogService.logIt( "OrgJusticeReport.executeReport() BBB.2 Dataset values: orgId=" + batchReport.getOrgId() + ", " + dataSet.toString() );
            
            int reportId = batchReport.getIntParam1();
            if( reportId<=0 )
                reportId = RuntimeConstants.getIntValue("orgJusticeReportId");
            
            if( eventFacade==null )
                eventFacade = EventFacade.getInstance();
            
            Report report = eventFacade.getReport(reportId);
            
            if( report==null )
                throw new Exception( "Report is null for reportId=" + reportId );
                        
            ReportData reportData = new ReportData( report, batchReport.getUser(), batchReport.getOrg(), batchReport.getSuborg(), null, new Object[]{dataSet} );
            
            Locale reportLocale = batchReport.getLocaleToUseDefaultUS();
            
            reportData.setReportLocale( reportLocale );
            
            String tmpltClassname = report.getImplementationClass();

            if( !report.getReportTemplateType().getIsCustom() )
                tmpltClassname = report.getReportTemplateType().getImplementationClass();

            Class<ReportTemplate> tmpltClass = (Class<ReportTemplate>) Class.forName( tmpltClassname );

            // LogService.logIt( "ReportManager templateClass=" + tmpltClassname + ", class=" + tmpltClass.toString() );

            Constructor ctor = tmpltClass.getDeclaredConstructor();
            ReportTemplate rt = (ReportTemplate) ctor.newInstance();
            // ReportTemplate rt = tmpltClass.newInstance();

            if( rt == null )
                throw new Exception( "Could not generate template class instance: " + tmpltClassname );

            rt.init( reportData );

            if( !rt.getIsReportGenerationPossible() )
                throw new Exception( "Report generation not possible." );


            bytes = rt.generateReport();

            rt.dispose();
            
       // tre.getTestResultExcelFile( trl, batchReport.getLocaleToUseDefaultUS(), batchReport.getUser().getTimeZone(), batchReport.getUser().getUserReportOptions(), batchReport.getOrg() );
            
            if( bytes==null )
                throw new BatchReportException( batchReport.getBatchReportId(), "Bytes is null." );

            if( bytes.length==0 )
                throw new BatchReportException( batchReport.getBatchReportId(), "Bytes is empty." );
            
            LogService.logIt( "OrgJusticeReport.executeReport() CCC Have report bytes: length=" + bytes.length + ", batchReportId=" + this.batchReport.getBatchReportId() );
            
            String pdfFilename = batchReport.getTitle();
            if( pdfFilename==null || pdfFilename.isBlank() )
                pdfFilename = DEFAULT_FILENAME_BASE;
            
            pdfFilename += "-" + getFilenameDateStr() + ".pdf";
            
            TimeZone tz = batchReport.getTimeZone();
            
            String sDateStr = I18nUtils.getFormattedDate(batchReport.getLocaleToUseDefaultUS(), dates[0], tz );
            String eDateStr = I18nUtils.getFormattedDate(batchReport.getLocaleToUseDefaultUS(), dates[1], tz );
            
            int sentCount = sendReportEmail(PDF_MIME_TYPE, pdfFilename, DEFAULT_CONTENT_KEY, DEFAULT_CONTENTSUPP_KEY, DEFAULT_SUBJECT_KEY, Integer.toString( (int) dataSet.overallCount ), sDateStr, eDateStr, bytes );
            // String attachMime, String attachFn, String contentKey, String subjectKey, String parm1, String parm2, byte[] bytes
            
            if( sentCount>0 )
                out[0]=1;
        }
        
        catch( Exception e )
        {
            LogService.logIt( e, "OrgJusticeReport.executeReport() batchReportId=" + batchReport.getBatchReportId() + ", " + batchReport.getTitle() + ", orgId=" + batchReport.getOrgId() );
        }
        
        return out;
    }
    
    private OrgJusticeDataset collectDataSet(Date[] dates) throws Exception
    {
        if( batchReport.getOrg()==null )
        {
            if( userFacade==null )
                userFacade=UserFacade.getInstance();
            batchReport.setOrg( userFacade.getOrg( batchReport.getOrgId() ));
        }

        if( orgJusticeUtils==null )
            orgJusticeUtils=new OrgJusticeUtils();

        List<Integer> forceProductIds = null;

        if( batchReport.getStrParam1()!=null && !batchReport.getStrParam1().isBlank() )
            forceProductIds = RuntimeConstants.getIntListForString( batchReport.getStrParam1(), ",");

        List<OrgJusticeTestEvent> tel = orgJusticeUtils.findOrgJusticeTestEvents(batchReport.getOrgId(),  dates[0],  dates[1], forceProductIds );

        LogService.logIt( "OrgJusticeReport.executeReport() BBB.1 found " + tel.size() + " testEvents for orgId=" + batchReport.getOrgId() );

        if( DEV && tel.isEmpty() )
        {
            tel.addAll( OrgJusticeUtils.getDummyData() );
            LogService.logIt( "OrgJusticeReport.executeReport() BBB.2 Added dummy data. tel.size=" + tel.size() );
        }

        OrgJusticeDataset dataSet = new OrgJusticeDataset();
        dataSet.setDates(dates);
        dataSet.setTotalParticipants( tel.size() );

        Set<Long> uids = new HashSet<>();
        
        // calculate means        
        for( OrgJusticeTestEvent ote : tel )
        {
            // only use most recent for a user.
            if( uids.contains(ote.getUserId() ) )
                continue;
            
            dataSet.addTestEvent(ote);
            uids.add( ote.getUserId() );
        }
        dataSet.finalizeAverages();
        
        return dataSet;
        
    }
    
    
    @Override
    public void validateBatchReportForExecution() throws Exception
    {
        // check core
        super.validateBatchReportForExecution();
        
        // specific checks
        if( batchReport.getYearsBack()<=0 && batchReport.getMonthsBack()<=0 && batchReport.getDaysBack()<=0 && batchReport.getHoursBack()<=0 )
        {
            LogService.logIt( "OrgJusticeReport.validateBatchReportForExecution() BatchReport id=" + batchReport.getBatchReportId() + " does not look back at least one day or one hour or month or year. daysBack=" + batchReport.getDaysBack() + ", hoursBack=" + batchReport.getHoursBack() + ", setting to 1 month back." );
            batchReport.setMonthsBack(1);
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
        String pdfFilename = batchReport.getTitle();
        if( pdfFilename==null || pdfFilename.isBlank() )
            pdfFilename = DEFAULT_FILENAME_BASE;

        pdfFilename += "-" + getFilenameDateStr() + ".pdf";

        return pdfFilename;
    }    
    
    
    
    
}
