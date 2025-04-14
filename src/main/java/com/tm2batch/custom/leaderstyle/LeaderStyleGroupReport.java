/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.leaderstyle;

import com.tm2batch.account.results.TestReportingUtils;
import com.tm2batch.autoreport.AutoReportFacade;
import com.tm2batch.autoreport.ExecutableReport;
import com.tm2batch.custom.BaseExecutableReport;
import static com.tm2batch.custom.BaseExecutableReport.PDF_MIME_TYPE;
import com.tm2batch.entity.event.TestEvent;
import com.tm2batch.entity.purchase.Product;
import com.tm2batch.entity.report.Report;
import com.tm2batch.entity.user.Org;
import com.tm2batch.entity.user.OrgAutoTest;
import com.tm2batch.entity.user.Suborg;
import com.tm2batch.entity.user.User;
import com.tm2batch.event.EventFacade;
import com.tm2batch.event.TestEventScoreType;
import com.tm2batch.global.BatchReportException;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.pdf.ReportData;
import com.tm2batch.pdf.ReportTemplate;
import com.tm2batch.service.LogService;
import com.tm2batch.user.UserFacade;
import com.tm2batch.util.I18nUtils;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author miker_000
 */
public class LeaderStyleGroupReport extends BaseExecutableReport implements ExecutableReport {


    public static String DEFAULT_FILENAME_BASE = "LeadershipStypeGroupLevelReport";
    public static String DEFAULT_CONTENT_KEY = "g.LeaderStyleGroupReport.content";
    public static String DEFAULT_CONTENTSUPP_KEY = "g.LeaderStyleGroupReport.contentsupp";
    public static String DEFAULT_SUBJECT_KEY = "g.LeaderStyleGroupLevelReport.subject";

    byte[] bytes = null;

    LeaderStyleGroupDataSet leaderStyleDataSet;

    EventFacade eventFacade;
    

    public LeaderStyleGroupReport()
    {
    }

    @Override
    public String toString() {
        return "LeaderStyleGroupReport{ dataset=" + (leaderStyleDataSet==null ? "null" : leaderStyleDataSet.toString()) + '}';
    }


    private void init()
    {
        testReportingUtils = new TestReportingUtils();

        TimeZone tz = batchReport.getTimeZone();
        Locale loc = batchReport.getLocaleToUseDefaultUS();
        LogService.logIt( "LeaderStyleGroupReport.init() using locale=" + loc.toString() + " and TimeZone=" + tz.getID());
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

        LogService.logIt( "LeaderStyleGroupReport.executeReport() START batchReportId=" + this.batchReport.getBatchReportId() );

        try
        {
            validateBatchReportForExecution();

            init();

            Date[] dates = batchReport.getDates();

            collectDataSet( dates );

            if( leaderStyleDataSet.getTotalCount()<=0 )
            {
                LogService.logIt( "LeaderStyleGroupReport.executeReport() BBB.1 No data found. Returning without sending. batchReportId=" + this.batchReport.getBatchReportId() );
                return out;
            }
            
            int reportId = batchReport.getIntParam1();
            if( reportId<=0 )
                reportId = RuntimeConstants.getIntValue("leaderStyleGroupReportId");

            if( eventFacade==null )
                eventFacade = EventFacade.getInstance();

            Report report = eventFacade.getReport(reportId);

            if( report==null )
                throw new Exception( "Report is null for reportId=" + reportId );

            ReportData reportData = new ReportData( report, batchReport.getUser(), batchReport.getOrg(), batchReport.getSuborg(), null, new Object[]{leaderStyleDataSet} );

            Locale reportLocale = batchReport.getLocaleToUseDefaultUS();

            reportData.setReportLocale( reportLocale );

            String tmpltClassname = report.getImplementationClass();

            if( !report.getReportTemplateType().getIsCustom() )
                tmpltClassname = report.getReportTemplateType().getImplementationClass();

            Class<ReportTemplate> tmpltClass = (Class<ReportTemplate>) Class.forName( tmpltClassname );

            // LogService.logIt( "LeaderStyleGroupReport templateClass=" + tmpltClassname + ", class=" + tmpltClass.toString() + ", dataSetSize= );

            Constructor ctor = tmpltClass.getDeclaredConstructor();
            ReportTemplate rt = (ReportTemplate) ctor.newInstance();
            // ReportTemplate rt = tmpltClass.newInstance();

            if( rt == null )
                throw new Exception( "Could not generate template class instance: " + tmpltClassname );

            rt.init( reportData );
            rt.setSampleReport(sampleReport);

            if( !rt.getIsReportGenerationPossible() )
                throw new Exception( "Report generation not possible." );

            bytes = rt.generateReport();

            rt.dispose();

            if( bytes==null )
                throw new BatchReportException( batchReport.getBatchReportId(), "Bytes is null." );

            if( bytes.length==0 )
                throw new BatchReportException( batchReport.getBatchReportId(), "Bytes is empty." );

            LogService.logIt( "LeaderStyleGroupReport.executeReport() CCC Have report bytes: length=" + bytes.length + ", batchReportId=" + this.batchReport.getBatchReportId() );

            String pdfFilename = batchReport.getTitle();
            if( pdfFilename==null || pdfFilename.isBlank() )
                pdfFilename = DEFAULT_FILENAME_BASE;

            pdfFilename += "-" + getFilenameDateStr() + ".pdf";

            TimeZone tz = batchReport.getTimeZone();

            String sDateStr = I18nUtils.getFormattedDate(batchReport.getLocaleToUseDefaultUS(), dates[0], tz );
            String eDateStr = I18nUtils.getFormattedDate(batchReport.getLocaleToUseDefaultUS(), dates[1], tz );

            int sentCount = sendReportEmail(PDF_MIME_TYPE, pdfFilename, DEFAULT_CONTENT_KEY, DEFAULT_CONTENTSUPP_KEY, DEFAULT_SUBJECT_KEY, Integer.toString((int) this.leaderStyleDataSet.getTotalCount() ), sDateStr, eDateStr, bytes );
            // String attachMime, String attachFn, String contentKey, String subjectKey, String parm1, String parm2, byte[] bytes

            if( sentCount>0 )
                out[0]=1;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "LeaderStyleGroupReport.executeReport() batchReportId=" + batchReport.getBatchReportId() + ", " + batchReport.getTitle() + ", orgId=" + batchReport.getOrgId() );
        }

        return out;
    }

    
    private void collectDataSet( Date[] dates ) throws Exception
    {
        try
        {
            leaderStyleDataSet = new LeaderStyleGroupDataSet();
            if( dates == null || dates.length<2 || dates[0]==null )
                throw new Exception( "Dates invalid. " + (dates==null ? "null" : "length=" + dates.length + ", dates[0]=" + (dates[0]==null ? "null" : "not null") ));

            leaderStyleDataSet.setStartDate(dates[0]);
            leaderStyleDataSet.setEndDate(dates[1]);
            
            List<Long> userIdList = getUserIdListFromEmailListStr();
            
            if( userFacade==null )
                userFacade=UserFacade.getInstance();

            Org o = userFacade.getOrg( batchReport.getOrgId());
            leaderStyleDataSet.setOrg(o);
            
            // suborgId
            if( batchReport.getIntParam4()>0 )
            {
                Suborg suborg = userFacade.getSuborg( batchReport.getIntParam4());
                if( suborg==null )
                    throw new Exception( "Suborg not found for suborgId=" + batchReport.getIntParam4() );
                if( suborg.getOrgId()!=o.getOrgId() )
                    throw new Exception( "Suborg for suborgId=" + suborg.getSuborgId() + " is for OrgId=" + suborg.getOrgId() + " not " + o.getOrgId() );
                leaderStyleDataSet.setSuborg(suborg);
            }
                
            // use authUserId only
            if( batchReport.getIntParam1()==1 )
            {
                User authUser = userFacade.getUser(batchReport.getUserId());
                leaderStyleDataSet.setAuthUser(authUser);
            }
            
            boolean hasCustoms = false;
            
            if( batchReport.getStrParam1()!=null && !batchReport.getStrParam1().isBlank() )
            {
                leaderStyleDataSet.setCustom1(batchReport.getStrParam1() );
                leaderStyleDataSet.setCustom1Name( o.getCustomFieldName1());
                hasCustoms = true;
            }
            
            if( batchReport.getStrParam2()!=null && !batchReport.getStrParam2().isBlank() )
            {
                leaderStyleDataSet.setCustom2(batchReport.getStrParam2() );
                leaderStyleDataSet.setCustom2Name( o.getCustomFieldName2());
                hasCustoms = true;
            }
            
            if( batchReport.getStrParam3()!=null && !batchReport.getStrParam3().isBlank() )
            {
                leaderStyleDataSet.setCustom3(batchReport.getStrParam3() );
                leaderStyleDataSet.setCustom3Name( o.getCustomFieldName3());
                hasCustoms = true;
            }

            if( batchReport.getStrParam4()!=null && !batchReport.getStrParam4().isBlank() )
                leaderStyleDataSet.setAltIdentifier( batchReport.getStrParam4() );
            
            
            // orgAutoTestId
            if( batchReport.getIntParam2()>0 )
            {
                if( eventFacade==null )
                    eventFacade=EventFacade.getInstance();
                OrgAutoTest oat = eventFacade.getOrgAutoTest( batchReport.getIntParam2());
                if( oat==null )
                    throw new Exception( "OrgAutoTest not found for orgAutoTestId=" + batchReport.getIntParam2() );
                if( oat.getOrgId()!=o.getOrgId() )
                    throw new Exception( "OrgAutoTest for orgAutoTestId=" + oat.getOrgAutoTestId() + " is for OrgId=" + oat.getOrgId() + " not " + o.getOrgId() );
                leaderStyleDataSet.setOrgAutoTest(oat);
            }

            // productId
            if( batchReport.getIntParam3()>0 )
            {
                if( eventFacade==null )
                    eventFacade=EventFacade.getInstance();
                Product p = eventFacade.getProduct( batchReport.getIntParam3());
                if( p==null )
                    throw new Exception( "Product not found for productId=" + batchReport.getIntParam3() );
                leaderStyleDataSet.setProduct(p);
            }
            
            if( eventFacade==null )
                eventFacade=EventFacade.getInstance();
            
            List<TestEvent> tel = eventFacade.findTestEvents(leaderStyleDataSet.getOrg().getOrgId(), 
                                                                  leaderStyleDataSet.getSuborg()!=null ? leaderStyleDataSet.getSuborg().getSuborgId() : 0, 
                                                                  leaderStyleDataSet.getAuthUser()!=null ? leaderStyleDataSet.getAuthUser().getUserId() : 0, 
                                                                  null, 
                                                                  null, 
                                                                  null, 
                                                                  leaderStyleDataSet.getOrgAutoTest()!=null ? leaderStyleDataSet.getOrgAutoTest().getOrgAutoTestId() : 0, 
                                                                  leaderStyleDataSet.getProduct()!=null ? leaderStyleDataSet.getProduct().getProductId() : 0,
                                                                  leaderStyleDataSet.getProduct()!=null ? null : RuntimeConstants.getStringValue("leaderStyleProductIds"), 
                                                                  0, 
                                                                  -1, 
                                                                  0, 
                                                                  0, 
                                                                  null, 
                                                                  null, 
                                                                  leaderStyleDataSet.getAltIdentifier(), 
                                                                  null, 
                                                                  null, 
                                                                  hasCustoms ? leaderStyleDataSet.getCustomsArray() : null, 
                                                                  userIdList,
                                                                  leaderStyleDataSet.getStartDate(), 
                                                                  leaderStyleDataSet.getEndDate(),
                                                                  0, 
                                                                  -1, 
                                                                  1000, 
                                                                  0);
            
            LogService.logIt( "LeaderStyleGroupReport.collectDataSet() CCC.1 Found " + tel.size() + " test events." );
            
            LeaderStyleResult lsr;
            for( TestEvent te : tel )
            {
                if( !te.getTestEventStatusType().getIsTestScored() )
                {
                    LogService.logIt( "LeaderStyleGroupReport.collectDataSet() DDD.1 Skipping test event because status incorrect. testEventStatusTypeId=" + te.getTestEventStatusTypeId() + ", testEventId=" + te.getTestEventId() );
                    continue;
                }
                
                lsr = new LeaderStyleResult();
                lsr.setTestDate( te.getLastAccessDate());
                lsr.setTestEventId(te.getTestEventId());
                lsr.setUser( userFacade.getUser( te.getUserId()));
                if( te.getSuborgId()>0 )
                    lsr.setSuborg( userFacade.getSuborg( te.getSuborgId() ));
                te.setTestEventScoreList( eventFacade.getTestEventScoresForTestEvent( te.getTestEventId(), TestEventScoreType.COMPETENCY.getTestEventScoreTypeId()) );
                lsr.setScoreMap( LeaderStyleReportUtils.getScoreMap(te));
                lsr.calculate();
                
                if( !lsr.getIsValid() )
                {
                    LogService.logIt( "LeaderStyleGroupReport.collectDataSet() DDD.4 Skipping test event because Results appear invalid. testEventStatusTypeId=" + te.getTestEventStatusTypeId() + ", testEventId=" + te.getTestEventId() );
                    continue;
                }                    
                
                leaderStyleDataSet.addLeaderStyleResult(lsr);                
            }
            
            leaderStyleDataSet.calculateAggregateResults();
            
            LogService.logIt( "LeaderStyleGroupReport.collectDataSet() EEE.1 Valid LeaderStyleResults Found " + leaderStyleDataSet.getTotalCount() + ", Overall Style Percentages: Auth:"
                    + leaderStyleDataSet.getStylePercentages()[0] + "% (count=" + leaderStyleDataSet.getStyleCounts()[0] + "), Demo: " 
                    + leaderStyleDataSet.getStylePercentages()[1] + "% (count=" + leaderStyleDataSet.getStyleCounts()[1] + "), Laissez: "
                    + leaderStyleDataSet.getStylePercentages()[2] + "% (count=" + leaderStyleDataSet.getStyleCounts()[2] + "), Transact: "
                    + leaderStyleDataSet.getStylePercentages()[3] + "% (count=" + leaderStyleDataSet.getStyleCounts()[3] + "), Transform: "
                    + leaderStyleDataSet.getStylePercentages()[4] + "% (count=" + leaderStyleDataSet.getStyleCounts()[4] + ")");            
        }
        catch( Exception e )
        {
            LogService.logIt(e, "LeaderStyleGroupReport.collectDataSet() " + toString() );
            throw e;
        }
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
        {
            LogService.logIt( "LeaderStyleGroupReport.validateBatchReportForExecution() BatchReport id=" + batchReport.getBatchReportId() + " does not look back at least one day or one hour or month or year. daysBack=" + batchReport.getDaysBack() + ", hoursBack=" + batchReport.getHoursBack() + ", setting to 1 month back." );
            batchReport.setMonthsBack(1);
            if( autoReportFacade==null )
                autoReportFacade=AutoReportFacade.getInstance();
            autoReportFacade.saveBatchReport(batchReport);
        }        
        
        if( batchReport.getUser().getUserReportOptions()==null )
            throw new BatchReportException( batchReport.getBatchReportId(), "BatchReport User (userId=" + batchReport.getUser().getUserId() + ", orgId=" + batchReport.getOrgId() + ") does not have any UserReportOptions. Please create a UserReportOptions object for this User." );
    }
    



}
