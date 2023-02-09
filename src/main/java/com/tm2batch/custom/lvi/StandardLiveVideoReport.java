/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.lvi;

import com.tm2batch.autoreport.ExecutableReport;
import com.tm2batch.custom.BaseExecutableReport;
import static com.tm2batch.custom.result.StandardResultsReport.DEFAULT_FILENAME_BASE;
import com.tm2batch.entity.lvi.LvCall;
import com.tm2batch.global.BatchReportException;
import com.tm2batch.lvi.LvFacade;
import com.tm2batch.lvi.LvScriptFacade;
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
public class StandardLiveVideoReport extends BaseExecutableReport implements ExecutableReport {
    
    public static String DEFAULT_FILENAME_BASE = "LiveVideoInterviewResults";
    public static String DEFAULT_CONTENT_KEY = "g.StdLiveVideoReport.content";
    public static String DEFAULT_CONTENTSUPP_KEY = "g.StdLiveVideoReport.contentsupp";
    public static String DEFAULT_SUBJECT_KEY = "g.StdLiveVideoReport.subject";
    
    LvFacade lvFacade = null;
    LvScriptFacade lvScriptFacade = null;
    
    byte[] bytes;
    
    public StandardLiveVideoReport()
    {
    }

    @Override
    public String toString() {
        return "StandardLiveVideoReport{" + '}';
    }
    
    
    private void init()
    {
        // testResultUtils = new TestResultUtils();
        
        TimeZone tz = batchReport.getTimeZone();
        Locale loc = batchReport.getLocaleToUseDefaultUS();
        LogService.logIt( "StandardLiveVideoReport.init() using locale=" + loc.toString() + " and TimeZone=" + tz.getID());
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
        
        this.batchReport = batchReport;
        
        int[] out = new int[4];
        
        LogService.logIt( "StandardLiveVideoReport.executeReport() START batchReportId=" + this.batchReport.getBatchReportId() );
        
        try
        {
            validateBatchReportForExecution();

            init();
            
            Date[] dates = batchReport.getDates();
            
            if( lvFacade==null )
                lvFacade = LvFacade.getInstance();
            
            List<LvCall> lvcl = lvFacade.findLvCallList( batchReport.getIntParam1()==1 ? batchReport.getUserId() : 0, 
                                        0, // long userId, 
                                        batchReport.getOrgId(), // int orgId, 
                                        batchReport.getSuborgId(), // int suborgId,
                                        0, // long recipientUserId,
                                        null, // String lastNameKey,
                                        null, // String emailKey,
                                        dates[0],
                                        dates[1],
                                        null, // List<Long> lvCallIdList,
                                        batchReport.getIntParam2()>=0 ? batchReport.getIntParam2() : -1, // int lvCallStatusTypeId,
                                        batchReport.getIntParam3()>=0 ? batchReport.getIntParam3() : 110, // int minLvCallStatusTypeId,
                                        batchReport.getIntParam4()>0 ? batchReport.getIntParam4() : 0, // int lvScriptId,
                                        true  // load 
                                        );
            /*
            List<TestResult> trl = testResultUtils.getTestResultList( batchReport, 
                                               batchReport.getIntParam1()==1, // boolean thisUserIdOnly, 
                                               batchReport.getStrParam1(), // String productNameKeyword,
                                               batchReport.getIntParam2(), // orgAutoTestId
                                               batchReport.getIntParam3(), // int productId, 
                                               batchReport.getIntParam4(), // int productTypeId,
                                               batchReport.getIntParam5(), // int consumerProductTypeId,
                                               batchReport.getIntParam7(), // int batteryId,
                                               batchReport.getStringArray(2), // String[] customsArray,
                                               batchReport.getIntParam6(), // int userCompanyStatusTypeId, 
                                               dates[0], // Date startDate, 
                                               dates[1], // Date endDate, 
                                               batchReport.getIntParam7(), // int testResultSortTypeId,
                                               Constants.DEFAULT_MAX_RESULT_ROWS, // int maxRows,
                                               true // boolean includeBatteryTestEvents 
                                               );
            */
            LogService.logIt( "StandardLiveVideoReport.executeReport() BBB Have " + lvcl.size() + " LvCalls to report. batchReportId=" + this.batchReport.getBatchReportId() );
            
            if( lvcl.size()<=0 )
                return out;
            
            if( batchReport.getOrg()==null )
            {
                if( userFacade==null )
                    userFacade=UserFacade.getInstance();
                batchReport.setOrg( userFacade.getOrg( batchReport.getOrgId() ));
            }
            
            if( batchReport.getOrg().getLvOrgPrefs()==null )
                batchReport.getOrg().setLvOrgPrefs( lvFacade.getLvOrgPrefsForOrgId( batchReport.getOrgId() ));
            
            for( LvCall lvc : lvcl )
            {
                if( lvc.getLvOrgPrefs()==null )
                    lvc.setLvOrgPrefs( batchReport.getOrg().getLvOrgPrefs());
                
                lvc.setLocale( batchReport.getLocaleToUseDefaultUS() );
                
                loadLvCall( lvc );
            }
            
            LvCallExporter lvce = new LvCallExporter();
                        
            int records = lvcl.size();
            
            bytes = lvce.getLvCallExcelFile(lvcl, batchReport.getLocaleToUseDefaultUS(), batchReport.getUser().getTimeZone(), batchReport.getOrg(), dates[0], dates[1] );
                    // getTestResultExcelFile( trl, batchReport.getLocaleToUseDefaultUS(), batchReport.getUser().getTimeZone(), batchReport.getUser().getUserReportOptions(), batchReport.getOrg() );
            
            if( bytes==null )
                throw new BatchReportException( batchReport.getBatchReportId(), "Bytes is null." );

            if( bytes.length==0 )
                throw new BatchReportException( batchReport.getBatchReportId(), "Bytes is empaty." );
            
            LogService.logIt( "StandardLiveVideoReport.executeReport() CCC Have report bytes: length=" + bytes.length + ", batchReportId=" + this.batchReport.getBatchReportId() );
            
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
            LogService.logIt( e, "StandardLiveVideoReport.executeReport() batchReportId=" + batchReport.getBatchReportId() + ", " + batchReport.getTitle() + ", orgId=" + batchReport.getOrgId() );
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
    
    
    private void loadLvCall( LvCall lvc ) throws Exception
    {
        if( userFacade==null )
            userFacade = UserFacade.getInstance();
        
        if( lvc.getRecipientUser()==null )
            lvc.setRecipientUser( userFacade.getUser( lvc.getRecipientUserId() ));

        if( lvc.getInitiatorUser()==null )
            lvc.setInitiatorUser( userFacade.getUser( lvc.getInitiatorUserId() ));

        if( lvc.getMultUser()==null && lvc.getMultUserId()>0 )
            lvc.setMultUser( userFacade.getUser( lvc.getMultUserId()));
        
        if( lvc.getMultUser2()==null && lvc.getMultUserId2()>0 )
            lvc.setMultUser2( userFacade.getUser( lvc.getMultUserId2()));

        if( lvc.getMultUser3()==null && lvc.getMultUserId3()>0 )
            lvc.setMultUser3( userFacade.getUser( lvc.getMultUserId3()));

        if( lvc.getMultUser4()==null && lvc.getMultUserId4()>0 )
            lvc.setMultUser4( userFacade.getUser( lvc.getMultUserId4()));
        
        if( lvc.getUser()==null && lvc.getUserId()>0 )
            lvc.setUser( userFacade.getUser( lvc.getUserId()));
        
        if( lvc.getLvScript()==null )
        {
            if( lvScriptFacade==null )
                lvScriptFacade=LvScriptFacade.getInstance();
            lvc.setLvScript( lvScriptFacade.getLvScript( lvc.getLvScriptId()));
        }
        
        if( lvc.getLvScript().getLvScriptQuestionMapList()==null )
            lvc.getLvScript().setLvScriptQuestionMapList( lvScriptFacade.getLvScriptQuestionMapList( lvc.getLvScriptId(), true ) );
        
        lvc.initCriteriaScores();
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
