/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.result;


import com.tm2batch.account.results.ItemResponseGroup;
import com.tm2batch.account.results.ct2.CT3ScoreUtils;
import com.tm2batch.autoreport.GeneralReportOptions;
import com.tm2batch.entity.battery.BatteryScore;
import com.tm2batch.entity.event.TestEvent;
import com.tm2batch.entity.event.TestEventResponseRating;
import com.tm2batch.entity.event.TestEventScore;
import com.tm2batch.entity.profile.Profile;
import com.tm2batch.entity.user.Org;
import com.tm2batch.entity.user.User;
import com.tm2batch.entity.user.UserNote;
import com.tm2batch.event.EventFacade;
import com.tm2batch.event.TestEventResponseRatingFacade;
import com.tm2batch.event.TestEventResponseRatingUtils;
import com.tm2batch.global.Constants;
import com.tm2batch.proctor.ProctorFacade;
import com.tm2batch.proctor.SuspiciousActivityType;
import com.tm2batch.score.TextAndTitle;
import com.tm2batch.service.LogService;
import com.tm2batch.sim.SimCompetencyVisibilityType;
import com.tm2batch.user.EthnicCategoryType;
import com.tm2batch.user.GenderType;
import com.tm2batch.user.RacialCategoryType;
import com.tm2batch.user.UserCompanyStatusType;
import com.tm2batch.user.UserFacade;
import com.tm2batch.user.UserType;
import com.tm2batch.util.I18nUtils;
import com.tm2batch.util.MessageFactory;
import com.tm2batch.util.NVPair;
import com.tm2batch.util.StringUtils;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Mike
 */
public class TestResultExporter {

    UserFacade userFacade;
    TestEventResponseRatingFacade terrFacade;
    ProctorFacade proctorFacade;

    Locale locale;

    public byte[] getTestResultExcelFile( List<TestResult> trl, Locale locale, TimeZone timezone, GeneralReportOptions excelReportBean, Org org, Date startDate, Date endDate) throws Exception
    {
        byte[] out = null;

        if( locale==null )
            locale=Locale.US;
        this.locale=locale;

        try
        {

            // Indicates this is for performance upload. Sets Certain fields.
            boolean forPerfUpload = excelReportBean.isIncludeFieldsForPerfUpload();

            boolean addOverallNumScore2 = false;

            Set<String> tnames = new TreeSet<>();

            Set<String> cNames = new TreeSet<>();

            Set<Integer> suspActTypeIds = new TreeSet<>();

            Set<String> terrCategoryNames = new TreeSet<>();
            Map<String,String> avgRespRatingMap;
            String respRatingVal;

            Map<Integer,Integer> susActTypeIdMap;


            TestEvent te;

            EventFacade eventFacade = null;

            for( TestResult tr : trl )
            {
                tnames.add( tr.getName() );

                if( tr.getAddOverallNumericToExcel() )
                    addOverallNumScore2=true;
            }

            boolean hasMultTests = tnames.size()>1;

            for( TestResult tr : trl )
            {
                if( tr instanceof TestEvent )
                {
                    te = (TestEvent) tr;

                    if( te.getTestEventScoreList()==null )
                    {
                        if( eventFacade == null )
                            eventFacade = EventFacade.getInstance();

                        te.setTestEventScoreList(eventFacade.getTestEventScoresForTestEvent(te.getTestEventId(), -1 ));
                    }

                    for( TestEventScore tes : te.getTestEventScoreList() )
                    {
                        if( !tes.getTestEventScoreType().getIsCompetency() )
                            continue;

                        if( !SimCompetencyVisibilityType.getValue( tes.getHide() ).getShowInExcel() )
                            continue;

                        if( SimCompetencyVisibilityType.getValue( tes.getHide() ).getShowItemResponsesOnly())
                            continue;

                        if( tes.getSimCompetencyClass().isScoredImageUpload() )
                            cNames.add(( hasMultTests ? "(" + tr.getName() + ") " : ""  ) + MessageFactory.getStringMessage(locale, "g.ImgCapRiskLevel" ) );

                        else
                            cNames.add( ( hasMultTests ? "(" + tr.getName() + ") " : ""  ) + tes.getName() );
                    }

                    if( te.getProduct()==null )
                    {
                        if( eventFacade == null )
                            eventFacade = EventFacade.getInstance();
                        te.setProduct( eventFacade.getProduct(te.getProductId()));
                    }

                    if( excelReportBean.isAvgRespRatings() )
                    {
                        if( te.getTestEventResponseRatingList()==null )
                        {
                            if( terrFacade==null )
                                terrFacade=TestEventResponseRatingFacade.getInstance();
                            te.setTestEventResponseRatingList( terrFacade.getTestEventResponseRatingsForTestEventId(te.getTestEventId()));
                        }
                        if( te.getTestEventResponseRatingList()!=null && !te.getTestEventResponseRatingList().isEmpty() )
                        {
                            for( TestEventResponseRating ter : te.getTestEventResponseRatingList() )
                            {
                                TestEventResponseRatingUtils.setTestEventResponseRatingNames(org, te.getProduct(), te.getTestEventScoreList(), locale, te.getTestEventResponseRatingList());

                                if( ter.getRatingNameList()!=null )
                                    terrCategoryNames.addAll( ter.getRatingNameList() );
                            }
                        }
                    }

                    if( excelReportBean.isSuspiciousActivity() && tr.getTestKey()!=null && tr.getTestKey().getOnlineProctoringType().getIsAnyPremium() )
                    {
                        if( te.getRemoteProctorEvent()==null )
                        {
                            if( proctorFacade==null )
                                proctorFacade=ProctorFacade.getInstance();
                            te.setRemoteProctorEvent( proctorFacade.getRemoteProctorEventForTestEventId(te.getTestEventId()));
                        }

                        if( tr.getTestKey().getOnlineProctoringType().getIsAnyPremiumWithSuspAct() && te.getRemoteProctorEvent()!=null && te.getRemoteProctorEvent().getSuspiciousActivityList()==null )
                        {
                            if( proctorFacade==null )
                                proctorFacade=ProctorFacade.getInstance();
                            te.getRemoteProctorEvent().setSuspiciousActivityList( proctorFacade.getSuspiciousActivityForTestEventId(te.getTestKeyId(), te.getTestEventId()));
                        }

                        if( te.getRemoteProctorEvent()!=null && te.getRemoteProctorEvent().getSuspiciousActivityList()!=null )
                        {
                            susActTypeIdMap=te.getRemoteProctorEvent().getSuspciousActivityCountByType();
                            for( Integer suspActTypeId : susActTypeIdMap.keySet() )
                            {
                                if( !suspActTypeIds.contains(suspActTypeId) )
                                    suspActTypeIds.add(suspActTypeId);
                            }
                        }
                    }

                }
            }

            String langKey;

            List<String> cNameList = new ArrayList<>();

            cNameList.addAll( cNames );

            Collections.sort( cNameList );


            boolean orgUsesRawOverallScore = org.getShowOverallRawScore()==1;



            List<TestEventScore> altTesl;
            int maxAlts = 0;
            SuspiciousActivityType sat;
            Map<Integer,Integer> saMap;
            int saCount;
            

            if( !forPerfUpload && excelReportBean.isAltScores() )
            {
                for( TestResult tr : trl )
                {
                    if( tr.getAltScoreProfileList()!=null && !tr.getAltScoreProfileList().isEmpty() )
                    {
                        if( tr.getAltScoreProfileList().size() > maxAlts )
                            maxAlts = tr.getAltScoreProfileList().size();
                    }

                    else if( tr instanceof TestEvent )
                    {
                        altTesl = ((TestEvent)tr).getAltScoreTestEventScoreList();

                        if( altTesl!=null && !altTesl.isEmpty() && altTesl.size()>maxAlts )
                            maxAlts = altTesl.size();
                    }
                }
            }

            Set<String> itemNames = null;

            if( !forPerfUpload && excelReportBean.isItemResponses() )
                itemNames = getAllItemNameSet( trl );

            UserFacade uf = null;

            boolean hasPerfData1 = forPerfUpload ? true : false;
            boolean hasPerfData2 = hasPerfData1;
            boolean hasPerfData3 = hasPerfData1;

            if( !forPerfUpload )
            {
                for( TestResult tr : trl )
                {
                    if( tr.getUser()==null || tr.getUser().getUserId()<=0 )
                    {
                        if( uf == null )
                            uf = UserFacade.getInstance();

                        tr.setUser( uf.getUser( tr.getUserId() ) );
                    }

                    if( tr.getUser().getPerform1()!=0 )
                        hasPerfData1 = true;

                    if( tr.getUser().getPerform2()!=0 )
                        hasPerfData2 = true;

                    if( tr.getUser().getPerform3()!=0 )
                        hasPerfData3 = true;

                    if(hasPerfData1 && hasPerfData2 && hasPerfData3 )
                        break;
                }
            }

            Workbook wb = new XSSFWorkbook();


            Font f=wb.createFont();
            CellStyle bold = wb.createCellStyle();
            f.setBold(true);
            bold.setFont(f);

            XSSFCellStyle wrap = (XSSFCellStyle) wb.createCellStyle();
            wrap.setWrapText( true );

            List<Integer> colsToAutosize = new ArrayList<>();

            Sheet sheet = wb.createSheet( MessageFactory.getStringMessage(locale, "exrpt.TestResults" ) );

            int rowNum = 0;
            int cellNum = 0;
            Row row;
            Cell cell;

            String nm;

            if( !forPerfUpload )
            {
                row = sheet.createRow( rowNum );
                rowNum++;

                cell = row.createCell(0);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.TestResultsForC" )  );
                cell.setCellStyle(bold);

                cell = row.createCell(1);
                cell.setCellValue( org.getName() );
                cell.setCellStyle(bold);


                row = sheet.createRow( rowNum );
                rowNum++;

                cell = row.createCell(0);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.DatePrepC" ) );
                cell.setCellStyle(bold);

                cell = row.createCell(1);
                cell.setCellValue( I18nUtils.getFormattedDate(locale, new Date(), TimeZone.getTimeZone("UTC")) );
                cell.setCellStyle(bold);

                String sDateStr = I18nUtils.getFormattedDate(locale, startDate, timezone );
                String eDateStr = I18nUtils.getFormattedDate(locale, endDate, timezone );

                row = sheet.createRow( rowNum );
                rowNum++;
                cell = row.createCell(0);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.DateRange" )+":" );
                cell.setCellStyle(bold);
                cell = row.createCell(1);
                cell.setCellValue( sDateStr + " - " + eDateStr );
                cell.setCellStyle(bold);


                row = sheet.createRow( rowNum );
                rowNum++;

                if( hasMultTests )
                {
                    String testName;
                    cellNum = 0;

                    row = sheet.createRow( rowNum );
                    rowNum++;

                    int count = 0;

                    if( excelReportBean.isIdentifier() )
                        count++;

                    if( excelReportBean.isDepartment() )
                        count++;

                    if( excelReportBean.isCustom1() && org != null && org.getCustomFieldName1()!=null && !org.getCustomFieldName1().isEmpty() )
                        count++;

                    if( excelReportBean.isCustom2() && org != null && org.getCustomFieldName2()!=null && !org.getCustomFieldName2().isEmpty() )
                        count++;

                    if( excelReportBean.isCustom3() && org != null && org.getCustomFieldName3()!=null && !org.getCustomFieldName3().isEmpty() )
                        count++;

                    if( excelReportBean.isName())
                        count+=2;

                    if( excelReportBean.isEmail())
                        count++;

                    if( excelReportBean.isMobile())
                        count++;

                    if( !forPerfUpload && excelReportBean.isUserDemographics() )
                        count+=5;

                    if( excelReportBean.isUserStatus())
                        count++;

                    if( excelReportBean.isTestAdminUser() )
                        count++;

                    if( excelReportBean.isUserNotes())
                        count++;

                    if( excelReportBean.isTestCreditsUsed())
                        count++;

                    // Online views
                    count++;

                    if( excelReportBean.isTestTakerIdentifier())
                        count++;


                    if( excelReportBean.isTestName())
                        count++;

                    if( excelReportBean.isTestEventType())
                        count++;

                    if( excelReportBean.isBattery() )
                        count++;

                    if( excelReportBean.isMultiUseLink() )
                        count++;

                    if( excelReportBean.isStarted())
                        count++;

                    if( excelReportBean.isCompleted())
                        count++;

                    if( excelReportBean.isTotalSeconds())
                        count++;

                    if( excelReportBean.isOverallScore())
                        count++;

                    if( addOverallNumScore2 )
                        count++;

                    if( excelReportBean.isOverallPercentile())
                        count++;

                    if( excelReportBean.isAccountPercentile())
                        count++;

                    if( excelReportBean.isCountryPercentile())
                        count++;

                    if( excelReportBean.isDetailViewUrl())
                        count++;

                    if( !forPerfUpload && excelReportBean.isPlagiarism() )
                        count++;

                    if( !forPerfUpload && excelReportBean.isSuspiciousActivity() && !suspActTypeIds.isEmpty() )
                        count += (suspActTypeIds.size() + 1);

                    // First Header Row.
                    for( int i=0;i<count;i++ )
                    {
                        cell = row.createCell( cellNum);
                        cell.setCellValue( "" );
                        colsToAutosize.add( cellNum );
                        cell.setCellStyle(bold);
                        cellNum++;
                    }

                    if( excelReportBean.isCompetencies())
                    {
                        for( String cname : cNameList )
                        {
                            testName = cname;

                            if( cname.startsWith("(") && cname.indexOf(") ")>0 )
                                testName = cname.substring(1, cname.indexOf(") ") );

                            cell = row.createCell( cellNum);
                            cell.setCellValue( testName );
                            colsToAutosize.add( cellNum );
                            cell.setCellStyle(bold);
                            cellNum++;
                        }
                    }

                    if( excelReportBean.isAvgRespRatings() && !terrCategoryNames.isEmpty() )
                    {
                        for( String cname : terrCategoryNames )
                        {
                            cell = row.createCell( cellNum);
                            cell.setCellValue( "" );
                            colsToAutosize.add( cellNum );
                            cell.setCellStyle(bold);
                            cellNum++;
                        }
                    }

                }
            }

            // Next HEADER ROW
            String compName;

            cellNum = 0;
            row = sheet.createRow( rowNum );
            rowNum++;

            if( forPerfUpload || excelReportBean.isIdentifier() )
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( forPerfUpload ? "TestResultIdentifier" :  MessageFactory.getStringMessage(locale, "exrpt.Identifier" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( forPerfUpload || excelReportBean.isDepartment() )
            {
                cell = row.createCell( cellNum );
                cell.setCellValue( forPerfUpload ? "Department" :  MessageFactory.getStringMessage(locale, "exrpt.Department")  );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( forPerfUpload || ( excelReportBean.isCustom1() && org != null && org.getCustomFieldName1()!=null && !org.getCustomFieldName1().isEmpty() ) )
            {
                cell = row.createCell( cellNum );
                cell.setCellValue( forPerfUpload ? "Custom1" : org.getCustomFieldName1() );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( forPerfUpload || ( excelReportBean.isCustom2() && org != null && org.getCustomFieldName2()!=null && !org.getCustomFieldName2().isEmpty() ) )
            {
                cell = row.createCell( cellNum );
                cell.setCellValue( forPerfUpload ? "Custom2" : org.getCustomFieldName2() );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( forPerfUpload || ( excelReportBean.isCustom3() && org != null && org.getCustomFieldName3()!=null && !org.getCustomFieldName3().isEmpty() ) )
            {
                cell = row.createCell( cellNum );
                cell.setCellValue( forPerfUpload ? "Custom3" : org.getCustomFieldName3() );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( forPerfUpload || excelReportBean.isName())
            {
                cell = row.createCell( cellNum );
                langKey = "exrpt.LastName";

                if( org.getUserAnonymityType().getHasUsername() )
                    langKey = "exrpt.Username";
                else if( org.getUserAnonymityType().getHasUserId() )
                    langKey = "exrpt.UserId";

                cell.setCellValue( forPerfUpload ? "LastName" : MessageFactory.getStringMessage(locale, langKey ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;

                langKey = "exrpt.FirstName";
                if( org.getUserAnonymityType().getHasUsername() )
                    langKey = "exrpt.Password";

                cell = row.createCell( cellNum );
                cell.setCellValue( forPerfUpload ? "FirstName" : MessageFactory.getStringMessage(locale, langKey ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( forPerfUpload || excelReportBean.isEmail())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( forPerfUpload ? "Email" : MessageFactory.getStringMessage(locale, "exrpt.Email" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( forPerfUpload || excelReportBean.isMobile())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( forPerfUpload ? "Phone" : MessageFactory.getStringMessage(locale, "exrpt.Mobile" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( !forPerfUpload && excelReportBean.isUserDemographics())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.Age" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;


                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.Gender" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.Nationality" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.Race" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.Ethnicity" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }


            if( forPerfUpload || excelReportBean.isUserStatus())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( forPerfUpload ? "CandidateStatus" :  MessageFactory.getStringMessage(locale, "exrpt.UserStatus" )  );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( !forPerfUpload && excelReportBean.isTestAdminUser())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.TestAdminUserName" ) );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( !forPerfUpload && excelReportBean.isUserNotes())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.UserNotes" ) );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( !forPerfUpload && excelReportBean.isTestCreditsUsed())
            {
                //cell = row.createCell( cellNum);
                //cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.CreditsUsed" ) );
                //colsToAutosize.add( cellNum );
                //cell.setCellStyle(bold);
                //cellNum++;

                cell = row.createCell( cellNum);
                if( org.getOrgCreditUsageType().getUsesLegacyCredits() || org.getOrgCreditUsageType().getUnlimited() )
                    cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.CreditsUsed" ) );
                else
                    cell.setCellValue( "Credit Id" );

                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;

                if( org.getOrgCreditUsageType().getAnyResultCredit() )
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( "Credit Index" );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(bold);
                    cellNum++;
                }


            }

            if( !forPerfUpload)
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "g.ResultViews" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }



            if( forPerfUpload || excelReportBean.isTestTakerIdentifier())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( forPerfUpload ? "AltIdentifier" :  MessageFactory.getStringMessage(locale, "exrpt.TestTakerIdentifier" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }


            if( forPerfUpload || excelReportBean.isTestName())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( forPerfUpload ? "TestName" :  MessageFactory.getStringMessage(locale, "exrpt.TestorBattery" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( !forPerfUpload && excelReportBean.isTestEventType())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.Type" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( !forPerfUpload && excelReportBean.isBattery())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.Battery" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( !forPerfUpload && excelReportBean.isMultiUseLink())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.MultiUseLink" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( !forPerfUpload && excelReportBean.isStarted())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.Started" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( forPerfUpload || excelReportBean.isCompleted())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( forPerfUpload ? "Completed" : MessageFactory.getStringMessage(locale, "exrpt.Completed" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( !forPerfUpload && excelReportBean.isTotalSeconds())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.TotalSeconds" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( forPerfUpload || excelReportBean.isOverallScore())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( forPerfUpload ? "Score" : MessageFactory.getStringMessage(locale, "exrpt.OverallScore" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( !forPerfUpload && addOverallNumScore2 )
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.OverallScoreNumeric" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( !forPerfUpload && excelReportBean.isOverallPercentile())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.OverallPercentile" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( !forPerfUpload && excelReportBean.isAccountPercentile())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.AccountPercentile" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( !forPerfUpload && excelReportBean.isCountryPercentile())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.CountryPercentile" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }


            if( !forPerfUpload && excelReportBean.isDetailViewUrl())
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.DetailViewURL" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( !forPerfUpload && excelReportBean.isPlagiarism() )
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.PlagCnt" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            if( !forPerfUpload && excelReportBean.isSuspiciousActivity() && !suspActTypeIds.isEmpty()  )
            {
                for( Integer satypeid : suspActTypeIds )
                {
                    sat = SuspiciousActivityType.getValue(satypeid);
                    cell = row.createCell( cellNum);
                    cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.SAX", new String[]{ sat.getName(locale)} ) );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(bold);
                    cellNum++;
                }

                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.SAALL" ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }


            if( !forPerfUpload && excelReportBean.isCompetencies())
            {
                for( String cname : cNameList )
                {
                    compName = cname;

                    if( hasMultTests && cname.startsWith("(") && cname.indexOf(") ")>0 )
                        compName = cname.substring(cname.indexOf(") ")+2, cname.length() );

                    cell = row.createCell( cellNum);
                    cell.setCellValue( compName );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(bold);
                    cellNum++;
                }
            }

            if( !forPerfUpload && excelReportBean.isAvgRespRatings() && !terrCategoryNames.isEmpty() )
            {
                for( String cname : terrCategoryNames )
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( "Avg. " + cname );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(bold);
                    cellNum++;
                }
            }


            if( forPerfUpload || excelReportBean.isPerformanceValues() )
            {
                if( forPerfUpload || hasPerfData1 )
                {
                    cell = row.createCell( cellNum );
                    cell.setCellValue( forPerfUpload ? "Performance1" : getOrgPerformName( locale, 1, org.getPerformanceName1() )  );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(bold);
                    cellNum++;
                }

                if( forPerfUpload || hasPerfData2 )
                {
                    cell = row.createCell( cellNum );
                    cell.setCellValue( forPerfUpload ? "Performance2" : getOrgPerformName( locale, 2, org.getPerformanceName2() )  );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(bold);
                    cellNum++;
                }

                if( forPerfUpload || hasPerfData3 )
                {
                    cell = row.createCell( cellNum );
                    cell.setCellValue( forPerfUpload ? "Performance3" : getOrgPerformName( locale, 3, org.getPerformanceName3() )  );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(bold);
                    cellNum++;
                }

                if( !forPerfUpload && (hasPerfData1 || hasPerfData2 || hasPerfData3) )
                {
                    cell = row.createCell( cellNum );
                    cell.setCellValue( MessageFactory.getStringMessage(locale, "g.PerformanceDate", null ) );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(bold);
                    cellNum++;
                }


            }

            altTesl = null;

            if( !forPerfUpload && excelReportBean.isAltScores() )
            {
                for( int m=1; m<= maxAlts; m++ )
                {
                    cell = row.createCell( cellNum );
                    cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.AlternateScoreNameX", new String[]{ Integer.toString(m) } )  );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(bold);
                    cellNum++;

                    cell = row.createCell( cellNum );
                    cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.AlternateScoreX", new String[]{ Integer.toString(m) } )  );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(bold);
                    cellNum++;
                }
            }

            if( !forPerfUpload && excelReportBean.isItemResponses() && itemNames!=null && !itemNames.isEmpty() )
            {
                for( String inm : itemNames )
                {
                    cell = row.createCell( cellNum );
                    cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.QX", new String[]{ inm }) );
                    cell.setCellStyle(bold);
                    cellNum++;
                }
            }



            UserType ut;

            EventFacade ef = null;
            //PurchaseFacade pf = null;

            //BatteryFacade bf = null;

            boolean isBattery = false;
            TestEventScore tes;
            int scoreDigits = 0;
            float overall;
            String itemValue;
            String temp;
            String rsk;
            int saCt;

            for( TestResult tr : trl )
            {
                cellNum = 0;

                row = sheet.createRow( rowNum );
                rowNum++;

                scoreDigits = tr.getOverallScorePrecisionDigits();

                isBattery =  tr instanceof TestEvent ? false : true;

                te = isBattery ? null : (TestEvent) tr;

                if( tr.getUser()==null || tr.getUser().getUserId()<=0 )
                {
                    if( uf == null )
                        uf = UserFacade.getInstance();

                    tr.setUser( uf.getUser( tr.getUserId() ) );
                }

                if( tr.getTestKey()==null )
                {
                    if( ef == null )
                        ef = EventFacade.getInstance();
                    tr.setTestKey( ef.getTestKey( tr.getTestKeyId(), true ) );
                }

                if( isBattery && ((BatteryScore)tr).getBattery()==null )
                {
                    if( ef == null )
                        ef = EventFacade.getInstance();
                    ((BatteryScore)tr).setBattery( ef.getBattery( ((BatteryScore)tr).getBatteryId() ) );
                }

                if( !isBattery && tr.getTestKey()!=null && tr.getTestKey().getBatteryId()>0 && te.getBattery()==null )
                {
                    if( ef == null )
                        ef = EventFacade.getInstance();
                    te.getTestKey().setBattery( ef.getBattery( tr.getTestKey().getBatteryId() ) );
                }


                if( !isBattery && tr.getProduct()==null )
                {
                    if( ef == null )
                        ef = EventFacade.getInstance();
                    tr.setProduct( ef.getProduct( te.getProductId() ) );
                }

                ut = UserType.getValue( tr.getUser().getUserTypeId() );

                if( forPerfUpload || excelReportBean.isIdentifier())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( tr.getTestResultId() );
                    cellNum++;
                }

                if( forPerfUpload || excelReportBean.isDepartment())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( tr.getSuborg()!=null ? tr.getSuborg().getName() : "" );
                    cellNum++;
                }

                if( forPerfUpload || ( excelReportBean.isCustom1() && org != null && org.getCustomFieldName1()!=null && !org.getCustomFieldName1().isEmpty()) )
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( tr.getCustom1()!=null ? tr.getCustom1() : "" );
                    cellNum++;
                }


                if( forPerfUpload || ( excelReportBean.isCustom2() && org != null && org.getCustomFieldName2()!=null && !org.getCustomFieldName2().isEmpty() ) )
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( tr.getCustom2()!=null ? tr.getCustom2() : "" );
                    cellNum++;
                }

                if( forPerfUpload || ( excelReportBean.isCustom3() && org != null && org.getCustomFieldName3()!=null && !org.getCustomFieldName3().isEmpty() ) )
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( tr.getCustom3()!=null ? tr.getCustom3() : "" );
                    cellNum++;
                }

                if( forPerfUpload || excelReportBean.isName())
                {
                    temp = tr.getUser().getLastName();

                    if( ut.getAnonymous() )
                        temp = "Anonymous";
                    else if ( ut.getPseudo() )
                        temp = "Pseudo";
                    else if ( ut.getUsername() || ut.getUserId() )
                        temp = tr.getUser().getEmail();


                    cell = row.createCell( cellNum);
                    cell.setCellValue(temp );
                    cellNum++;

                    temp = tr.getUser().getFirstName();

                    if( ut.getAnonymous() )
                        temp = "Anonymous";
                    else if ( ut.getPseudo() )
                        temp = "Pseudo";
                    else if ( ut.getUserId() )
                        temp = "";

                    cell = row.createCell( cellNum);
                    cell.setCellValue( temp );
                    cellNum++;
                }

                if( forPerfUpload || excelReportBean.isEmail())
                {
                    temp = tr.getUser().getEmail();
                    if( ut.getAnonymous() )
                        temp = "Anonymous";
                    else if ( ut.getPseudo() )
                        temp = "Pseudo";
                    else if ( ut.getUserId() || ut.getUsername() )
                        temp = tr.getUser().getEmail();

                    cell = row.createCell( cellNum);
                    cell.setCellValue( temp );
                    cellNum++;
                }

                if( forPerfUpload || excelReportBean.isMobile())
                {
                    temp = tr.getUser().getMobilePhone();
                    if( ut.getAnonymous() )
                        temp = "Anonymous";
                    else if ( ut.getPseudo() )
                        temp = "Pseudo";
                    else if ( ut.getUserId() || ut.getUsername() )
                        temp = "";

                    cell = row.createCell( cellNum);
                    cell.setCellValue( temp );
                    cellNum++;
                }

                if( !forPerfUpload && excelReportBean.isUserDemographics())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( tr.getUser().getBirthYear() );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(bold);
                    cellNum++;


                    cell = row.createCell( cellNum);
                    cell.setCellValue( this.getGenderName(tr.getUser().getGenderTypeId(),locale) );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(bold);
                    cellNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellValue( tr.getUser().getNationality() );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(bold);
                    cellNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellValue( getRacialCategoriesStr(tr.getUser().getRacialCategories(),locale) );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(bold);
                    cellNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellValue( this.getEthnicName(tr.getUser().getEthnicCategoryId(),locale) );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(bold);
                    cellNum++;
                }


                if( forPerfUpload || excelReportBean.isUserStatus())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( !ut.getNamed() ? "" : getUserCompanyStatus( forPerfUpload ? Locale.US : locale, tr.getUser() ) );
                    cellNum++;
                }

                if( !forPerfUpload && excelReportBean.isTestAdminUser())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( tr.getAdminUserName() );
                    cellNum++;
                }

                //Notes
                if( !forPerfUpload && excelReportBean.isUserNotes())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellStyle(wrap);
                    cell.setCellValue( getUserNoteStr( locale, tr.getUser(), excelReportBean.isUserNoteDates() ) );
                    cellNum++;
                }

                if( !forPerfUpload && excelReportBean.isTestCreditsUsed())
                {
                    //cell = row.createCell( cellNum);
                    //cell.setCellStyle(wrap);
                    //cell.setCellValue( Integer.toString( tr.getCreditsUsed() ) );
                    //cellNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellStyle(wrap);
                    if( org.getOrgCreditUsageType().getUsesLegacyCredits() || org.getOrgCreditUsageType().getUnlimited() )
                        cell.setCellValue( Integer.toString( tr.getCreditsUsed() ) );
                    else
                        cell.setCellValue(  tr.getTestKey()==null ? "" : Long.toString( tr.getTestKey().getCreditId() ) );
                    cellNum++;

                    if( org.getOrgCreditUsageType().getAnyResultCredit() )
                    {
                        cell = row.createCell( cellNum);
                        cell.setCellStyle(wrap);
                        cell.setCellValue( tr.getTestKey()==null ? "" : Integer.toString( tr.getTestKey().getCreditIndex() ) );
                        cellNum++;
                    }


                }

                if( !forPerfUpload )
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( te==null ? "" : Integer.toString(te.getSkinId()) );
                    cellNum++;
                }

                // Test Taker Identifier
                if( forPerfUpload || excelReportBean.isTestTakerIdentifier())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( tr.getUser().getAltIdentifier() == null ? "" : tr.getUser().getAltIdentifier() );
                    cellNum++;
                }


                if( forPerfUpload || excelReportBean.isTestName())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( isBattery ? ((BatteryScore)tr).getBattery().getName() : tr.getProduct().getName() );
                    cellNum++;
                }

                if( !forPerfUpload && excelReportBean.isTestEventType())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( isBattery ? "Battery" : "Test" );
                    cellNum++;
                }

                if( !forPerfUpload && excelReportBean.isBattery())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( tr.getBattery()==null ? "" : tr.getBattery().getName() );
                    cellNum++;
                }


                if( !forPerfUpload && excelReportBean.isMultiUseLink())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( tr.getOrgAutoTest()==null ? "" : tr.getOrgAutoTest().getName() );
                    cellNum++;
                }

                if( !forPerfUpload && excelReportBean.isStarted())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue(  I18nUtils.getFormattedDateTime(locale, tr.getStartDate(), DateFormat.LONG, DateFormat.LONG, timezone )  );
                    cellNum++;
                }

                if( forPerfUpload || excelReportBean.isCompleted())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( I18nUtils.getFormattedDateTime(locale, tr.getLastAccessDate(), DateFormat.LONG, DateFormat.LONG, timezone ) );
                    cellNum++;
                }

                if( !forPerfUpload && excelReportBean.isTotalSeconds())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( te==null ? "" : I18nUtils.getFormattedNumber(locale, te.getTotalTestTime(), 1) );
                    cellNum++;
                }


                if( forPerfUpload || excelReportBean.isOverallScore())
                {
                    overall = tr.getOverallScore();

                    cell = row.createCell( cellNum);
                    // cell.setCellValue( tr.getHasScore() ? new Integer( Math.round( tr.getOverallScore() )).toString() : "NA" );

                    if( isBattery )
                        cell.setCellValue( tr.getHasScore() ? I18nUtils.getFormattedNumber(locale, overall, scoreDigits) : "NA" );

                    else
                    {
                        if( orgUsesRawOverallScore &&
                                tr.getProduct()!=null &&
                                ( tr.getProduct().getConsumerProductType().getIsJobSpecificOrCompetency() ) // && !tr.getForceShowScaledScore()
                          )
                        {
                            overall = tr.getOverallRawScore();
                            cell.setCellValue( tr.getHasScore() ? I18nUtils.getFormattedNumber(locale, overall, scoreDigits) : "NA" );
                        }

                        else if( te !=null )
                            cell.setCellValue( tr.getHasScore() ? te.getOverallScore4Show() : "NA" );
                        else
                            cell.setCellValue( tr.getHasScore() ? I18nUtils.getFormattedNumber(locale, overall, scoreDigits) : "NA" );

                    }
                    cellNum++;
                }

                if( !forPerfUpload && addOverallNumScore2 )
                {
                    cell = row.createCell( cellNum);

                    if( tr.getAddOverallNumericToExcel() )
                    {
                         cell.setCellValue( tr.getHasScore() ? I18nUtils.getFormattedNumber(locale, tr.getOverallScore(), scoreDigits) : "NA" );
                    }

                    cellNum++;
                }

                if( !forPerfUpload && excelReportBean.isOverallPercentile())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( tr.getHasOverallPercentile()? Integer.toString(Math.round(tr.getOverallPercentile())) : "NA" );
                    cellNum++;
                }

                if( !forPerfUpload && excelReportBean.isAccountPercentile())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( tr.getHasValidAccountNorm()? Integer.toString(Math.round(tr.getAccountPercentile())) : "NA" );
                    cellNum++;
                }

                if( excelReportBean.isCountryPercentile())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( tr.getHasValidCountryNorm()? Integer.toString(Math.round(tr.getCountryPercentile())) : "NA" );
                    cellNum++;
                }

                if( !forPerfUpload && excelReportBean.isDetailViewUrl())
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( tr.getDirectLinkUrl() );
                    cellNum++;
                }
                
                if( !forPerfUpload && excelReportBean.isPlagiarism() )
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( getPlagiarismCount(te) );
                    cellNum++;
                }

                if( !forPerfUpload && excelReportBean.isSuspiciousActivity() && !suspActTypeIds.isEmpty()  )
                {
                    saCt=0;
                    saMap = te==null || te.getRemoteProctorEvent()==null ? null : te.getRemoteProctorEvent().getSuspciousActivityCountByType();
                    for( Integer satypeid : suspActTypeIds )
                    {
                        sat = SuspiciousActivityType.getValue(satypeid);
                        saCount = 0;

                        if( saMap!=null && saMap.get(satypeid)!=null )
                            saCount = saMap.get(satypeid);

                        cell = row.createCell( cellNum);
                        cell.setCellValue( saCount );
                        colsToAutosize.add( cellNum );
                        cellNum++;

                        saCt += saCount;
                    }

                    cell = row.createCell( cellNum);
                    cell.setCellValue( saCt );
                    colsToAutosize.add( cellNum );
                    cellNum++;
                }
                

                if( !forPerfUpload && excelReportBean.isCompetencies())
                {
                    scoreDigits = tr.getCompetencyScorePrecisionDigits();
                    for( String cname : cNameList )
                    {
                        tes = getTestEventScoreForCompetencyName(cname, hasMultTests, locale, tr );

                        cell = row.createCell( cellNum);

                        if( tes!=null && tes.getSimCompetencyClass().isScoredImageUpload() )
                        {

                            if( tes.getScore()<0 )
                                rsk = "";

                            else if( tes.getScore() <= Constants.IMAGE_CAPTURE_HIGH_RISK_CUTOFF )
                                rsk = MessageFactory.getStringMessage(locale, "g.HighRisk" );

                            else if( tes.getScore() <= Constants.IMAGE_CAPTURE_MED_RISK_CUTOFF )
                                rsk = MessageFactory.getStringMessage(locale, "g.MediumRisk" );

                            else
                                rsk = MessageFactory.getStringMessage(locale, "g.LowRisk" );

                            cell.setCellValue( rsk );
                        }

                        else
                            cell.setCellValue( tes == null ? "" :  I18nUtils.getFormattedNumber(locale, tes.getScore(), scoreDigits) );
                        cellNum++;
                    }
                }

                if( !forPerfUpload && excelReportBean.isAvgRespRatings()&& !terrCategoryNames.isEmpty() )
                {
                    avgRespRatingMap = te==null ? null : TestEventResponseRatingUtils.getOverallAverageRatingMap(te.getTestEventResponseRatingList(), locale);

                    for( String cname : terrCategoryNames )
                    {
                        respRatingVal = avgRespRatingMap==null ? null : avgRespRatingMap.get(cname);

                        cell = row.createCell( cellNum);
                        cell.setCellValue( respRatingVal==null ? "" : respRatingVal );
                        cellNum++;
                    }
                }

                if( forPerfUpload || (excelReportBean.isPerformanceValues() && hasPerfData1) )
                {
                    cell = row.createCell( cellNum );
                    cell.setCellValue( Float.toString( tr.getUser().getPerform1() ) );
                    cellNum++;
                }

                if( forPerfUpload || (excelReportBean.isPerformanceValues() && hasPerfData2) )
                {
                    cell = row.createCell( cellNum );
                    cell.setCellValue( Float.toString( tr.getUser().getPerform2() ) );
                    cellNum++;
                }

                if( forPerfUpload || (excelReportBean.isPerformanceValues() && hasPerfData3) )
                {
                    cell = row.createCell( cellNum );
                    cell.setCellValue( Float.toString( tr.getUser().getPerform3() ) );
                    cellNum++;
                }

                if( !forPerfUpload && excelReportBean.isPerformanceValues() && ( hasPerfData1 || hasPerfData2 || hasPerfData3) )
                {
                    cell = row.createCell( cellNum );
                    cell.setCellValue( tr.getUser().getPerformDate()==null ? "" : I18nUtils.getFormattedDateTime(locale, tr.getUser().getPerformDate(), DateFormat.LONG, DateFormat.LONG, timezone ) );
                    cellNum++;
                }

                if( !forPerfUpload && excelReportBean.isAltScores() )
                {
                    if( tr.getAltScoreProfileList()!=null && !tr.getAltScoreProfileList().isEmpty() )
                    {
                        scoreDigits = tr.getOverallScorePrecisionDigits();

                        for( Profile profile : tr.getAltScoreProfileList() )
                        {
                            cell = row.createCell( cellNum );
                            cell.setCellValue(  profile.getStrParam1() );
                            cellNum++;

                            cell = row.createCell( cellNum );

                            cell.setCellValue( I18nUtils.getFormattedNumber(locale, profile.getAltScoreCalculator().getScore(), scoreDigits)  );
                            // cell.setCellValue( "" + Math.round( profile.getAltScoreCalculator().getScore() )  );
                            cellNum++;
                        }

                        if( tr.getAltScoreProfileList().size()<maxAlts )
                        {
                            for( int k=0;k<maxAlts-tr.getAltScoreProfileList().size();k++ )
                            {
                                cell = row.createCell( cellNum );
                                cell.setCellValue("");
                                cellNum++;

                                cell = row.createCell( cellNum );
                                cell.setCellValue("");
                                cellNum++;

                            }
                        }
                    }

                    else if( tr instanceof TestEvent )
                    {
                        altTesl = ((TestEvent)tr).getAltScoreTestEventScoreList();

                        scoreDigits = tr.getOverallScorePrecisionDigits();

                        for( TestEventScore tesx : altTesl )
                        {
                            cell = row.createCell( cellNum );
                            cell.setCellValue(  tesx.getName() );
                            cellNum++;

                            cell = row.createCell( cellNum );
                            cell.setCellValue( I18nUtils.getFormattedNumber(locale, tesx.getScore(), scoreDigits)  );
                            cellNum++;
                        }

                        if( altTesl.size()<maxAlts )
                        {
                            for( int k=0;k<maxAlts-altTesl.size();k++ )
                            {
                                cell = row.createCell( cellNum );
                                cell.setCellValue("");
                                cellNum++;

                                cell = row.createCell( cellNum );
                                cell.setCellValue("");
                                cellNum++;

                            }
                        }

                    }



                }

                if( !forPerfUpload && excelReportBean.isItemResponses() && itemNames!=null && !itemNames.isEmpty() )
                {
                    List<ItemResponseGroup> irgl = (tr instanceof TestEvent) ? CT3ScoreUtils.computeItemResponseGroupData((TestEvent)tr, te.getLocale(), true ) : null;

                    for( String inm : itemNames )
                    {
                        itemValue = getItemValue(tr, inm, irgl );
                        cell = row.createCell( cellNum );
                        cell.setCellValue(itemValue);
                        cellNum++;
                    }
                }
            }

            for( Integer n : colsToAutosize )
            {
                sheet.autoSizeColumn(n);
            }

            sheet.createFreezePane(0,hasMultTests ? 5 : 4 );

            ByteArrayOutputStream bais = new ByteArrayOutputStream();

            wb.write(bais);

            return bais.toByteArray();
        }

        catch ( Exception  e )
        {
            LogService.logIt( e, "TestResultExporter.getTestResultExcelFile() " );
        }

        return out;
    }


    private String getUserNoteStr( Locale locale, User user, boolean includeNamesDates )
    {

        StringBuilder sb = new StringBuilder();

        try
        {

            if( user.getUserNoteList()==null )
            {
                if( userFacade == null )
                    userFacade = UserFacade.getInstance();

                user.setUserNoteList( userFacade.getUserNotes( user.getUserId() ));
            }

            for( UserNote un : user.getUserNoteList() )
            {
                if( un.getAuthorUser()==null )
                {
                    if( userFacade == null )
                        userFacade = UserFacade.getInstance();

                    un.setAuthorUser( userFacade.getUser( un.getAuthorUserId() ) );
                }

                if( sb.length()>0 )
                    sb.append( "\n" );

                if( un.getAuthorUser()==null )
                    un.setAuthorUser( new User() );

                if( includeNamesDates )
                {
                    sb.append( I18nUtils.getFormattedDateTime( locale , un.getCreateDate(), DateFormat.LONG,DateFormat.LONG, un.getAuthorUser().getTimeZone() ) + " " );

                    sb.append("(by " + un.getAuthorUser().getLastName() + ") " );
                }

                sb.append( un.getNote() );
            }
        }

        catch( Exception e )
        {
            LogService.logIt( e, "TestResultExporter.getUserNoteStr() userId=" + user.getUserId()  );
        }

        return sb.toString();
    }


    private String lmsg( String key )
    {
        return MessageFactory.getStringMessage(locale, key);
    }

    private static String getUserCompanyStatus( Locale locale, User user )
    {
        try
        {

            return UserCompanyStatusType.getValue( user.getUserCompanyStatusTypeId() ).getName(locale);

        }
        catch( Exception e )
        {
            LogService.logIt( e, "TestResultExporter.getUserCompanyStatus() " + (user==null ? "user is null" : user.toString() ));
            return "";
        }

    }


    private static TestEventScore getTestEventScoreForCompetencyName( String cname, boolean hasMultTests, Locale locale, TestResult tr)
    {
        if( tr instanceof TestEvent )
        {
            TestEvent te = (TestEvent) tr;

            if( te.getTestEventScoreList()==null )
                return null;

            String n2 = cname; // ( hasMultTests ? "(" + tr.getName() + ") " : "" ) + cname;

            // int idx;

            // Clean out test name.
            if( hasMultTests )
            {
                if( !cname.contains("(" + tr.getName() + ") " ) )
                    return null;

                // LogService.logIt( "TestResultExporter.getTestEventScoreForCompetencyName() adjusting name. cname=" + " " + cname + ", tr.getName()=" + tr.getName() );
                n2 = cname.substring(((new String( "(" + tr.getName() + ") " ) ) ).length(), cname.length() );
            }

            // Look for exact match.
            for( TestEventScore tes : te.getTestEventScoreList() )
            {
                if( !SimCompetencyVisibilityType.getValue( tes.getHide() ).getShowInExcel() )
                    continue;

                if( SimCompetencyVisibilityType.getValue( tes.getHide() ).getShowItemResponsesOnly())
                    continue;

                // if( tes.getName().equalsIgnoreCase(cname))
                if( StringUtils.isValidNameMatch( n2,n2, tes.getName(),tes.getNameEnglish() ) )
                    return tes;
            }

            // No match found. But n2 is for Image Capture Proctoring. Find TES for that.
            if( n2.contains( MessageFactory.getStringMessage(locale, "g.ImgCapRiskLevel" )) )
            {
                for( TestEventScore tes : te.getTestEventScoreList() )
                {
                    if( tes.getTestEventScoreType().getIsCompetency() && tes.getSimCompetencyClass().isScoredImageUpload() )
                        return tes;
                }
            }
        }

        return null;
    }

    public static String getOrgPerformName( Locale locale, int idx, String orgName )
    {
        if( orgName==null || orgName.isEmpty() )
            return  MessageFactory.getStringMessage( locale, "g.TestTakerPerfNameDefaultX", new String[] {Integer.toString(idx)} );

        return orgName;
    }

    public static Set<String> getAllItemNameSet( List<TestResult> trl ) throws Exception
    {
        Set<String> out = new TreeSet<>();

        TestEvent te;
        List<TextAndTitle> ttl;

        for( TestResult tr : trl )
        {
            if( tr instanceof TestEvent )
            {
                te = (TestEvent) tr;

                ttl = getAllItemResponseTextAndTitleList( te );

                for( TextAndTitle tt : ttl )
                {
                    out.add( tt.getTitle() );
                }
            }
        }

        return out;
    }

    public static List<TextAndTitle> getAllItemResponseTextAndTitleList( TestEvent te ) throws Exception
    {
        List<TextAndTitle> out = new ArrayList<>();

        List<ItemResponseGroup> irgl = CT3ScoreUtils.computeItemResponseGroupData( te, te.getLocale(), true );

        if( irgl==null || irgl.isEmpty() )
            return out;

        List<TextAndTitle> ttl;

        for( ItemResponseGroup irg : irgl )
        {
            ttl = irg.getTextTitleList();
            if( ttl==null )
                continue;
            out.addAll(ttl);
        }

        return out;
    }

    private static String getItemValue( TestResult tr, String itemName, List<ItemResponseGroup> irgl)
    {
        if( itemName==null || itemName.isEmpty() )
            return "";

        if( tr instanceof TestEvent )
        {
            TestEvent te = (TestEvent) tr;

            // if( te.getItemResponseGroupList()==null )
            if( irgl==null )
                return "";

            for( ItemResponseGroup irg : irgl )
            {
                for( TextAndTitle tt : irg.getTextTitleList() )
                {
                    if( tt.getTitle()!=null && tt.getTitle().equals( itemName ) )
                    {
                        String txt = tt.getText();
                        return txt==null ? "" : tt.getText();
                    }
                }
            }
        }

        return "";
    }


    public boolean getBooleanFlagValue( String name, Org org )
    {
        //if( product == null || product.getDetailView()==null || product.getDetailView().isEmpty() || user==null ) // || user.getOrg()==null )
        //    return false;

        List<NVPair> pl = org.getReportFlagList();

        // LogService.logIt( "TestEvent.getBooleanFlagValue() " + name + ", Report Flag list is " + pl.size() + ", reportId=" + report.getReportId() );

        for( NVPair p : pl )
        {
            if( p.getName().equals( name ) && p.getValue().equals( "1" ) )
                return true;
        }

        return false;
    }


    private String getGenderName( int typ, Locale locale )
    {
        GenderType gt = GenderType.getValue(typ);
        if( gt==null || gt.equals(GenderType.UNKNOWN) )
            return "";
        return gt.getName(locale);
    }

    private String getEthnicName( int typ, Locale locale )
    {
        EthnicCategoryType gt = EthnicCategoryType.getValue(typ);
        if( gt==null  )
            return "";
        return gt.getName(locale);
    }

    private String getRacialCategoriesStr( String cats, Locale locale )
    {
        return RacialCategoryType.getRacialCategoryStr(cats, locale);
    }

    private Integer getPlagiarismCount( TestEvent te )
    {
        if( te==null || te.getTestEventScoreList()==null )
            return 0;

        int ct = 0;

        for( TestEventScore tes : te.getTestEventScoreList() )
        {
            if( tes.getSimCompetencyClass().isScoredEssay() && tes.getScore8()>0 )
                ct += ((int)tes.getScore8());
        }

        return ct;
    }


    
    

}
