/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.lvi;

import com.tm2batch.account.results.*;
import com.tm2batch.account.results.ct2.CT3ScoreUtils;
import com.tm2batch.custom.result.TestResult;
import com.tm2batch.entity.event.TestEvent;
import com.tm2batch.entity.lvi.LvCall;
import com.tm2batch.entity.lvi.LvQuestion;
import com.tm2batch.entity.lvi.LvQuestionResponse;
import com.tm2batch.entity.lvi.LvScript;
import com.tm2batch.entity.user.Org;
import com.tm2batch.entity.user.User;
import com.tm2batch.entity.user.UserNote;

import com.tm2batch.lvi.LiveVideoOverallRatingType;
import com.tm2batch.lvi.LvScriptFacade;
import com.tm2batch.score.TextAndTitle;
import com.tm2batch.service.LogService;
import com.tm2batch.user.UserFacade;
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
public class LvCallExporter {

    UserFacade userFacade = null;
    LvScriptFacade lvScriptFacade = null;
    
    public byte[] getLvCallExcelFile( List<LvCall> lvcl, Locale locale, TimeZone timezone, Org org, Date startDate, Date endDate) throws Exception
    {
        byte[] out = null;

        try
        {
            // List of titles
            //Set<String> cnames = new TreeSet<>();

            // set of all LvQuestions
            Set<LvQuestion> qset = new TreeSet<>();
            

            for( LvCall lvc : lvcl )
            {
                //if( lvc.getTitle() !=null && !lvc.getTitle().isBlank() )
                //    cnames.add(lvc.getTitle() );
                
                if( lvc.getLvQuestionResponseList()!=null )
                {
                    if( lvScriptFacade==null )
                        lvScriptFacade=LvScriptFacade.getInstance();
                    
                    for( LvQuestionResponse lvqr : lvc.getLvQuestionResponseList() )
                    {
                        qset.add( lvScriptFacade.getLvQuestion( lvqr.getLvQuestionId() ) );
                    }
                }
            }
            
            
            String ratingName1 = org.getLvOrgPrefs()!=null && org.getLvOrgPrefs().getHasCriteria1() ? org.getLvOrgPrefs().getCriteriaName1() : MessageFactory.getStringMessage(locale, "g.RatingX", new String[]{"1"} );
            String ratingName2 = org.getLvOrgPrefs()!=null && org.getLvOrgPrefs().getHasCriteria2() ? org.getLvOrgPrefs().getCriteriaName2() : MessageFactory.getStringMessage(locale, "g.RatingX", new String[]{"2"} );
            String ratingName3 = org.getLvOrgPrefs()!=null && org.getLvOrgPrefs().getHasCriteria3() ? org.getLvOrgPrefs().getCriteriaName3() : MessageFactory.getStringMessage(locale, "g.RatingX", new String[]{"3"} );
            
            List<LvQuestion> questionList = new ArrayList<>();
            questionList.addAll(qset);
            Collections.sort(questionList);
            
            String langKey;
                                     
            Workbook wb = new XSSFWorkbook();


            Font f=wb.createFont();
            CellStyle bold = wb.createCellStyle();
            f.setBold(true);
            bold.setFont(f);

            XSSFCellStyle wrap = (XSSFCellStyle) wb.createCellStyle();
            wrap.setWrapText( true );

            List<Integer> colsToAutosize = new ArrayList<>();
            
            Sheet sheet = wb.createSheet( MessageFactory.getStringMessage(locale, "exrptlvc.LvcResults" ) );

            int rowNum = 0;
            int cellNum = 0;
            Row row;
            Cell cell;

            String nm;

            row = sheet.createRow( rowNum );
            rowNum++;

            cell = row.createCell(0);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrptlvc.LvcResultsForC" )  ); 
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
            cell.setCellValue(MessageFactory.getStringMessage(locale, "exrpt.DateRange" )+":" );            
            cell.setCellStyle(bold);
            cell = row.createCell(1);                
            cell.setCellValue( sDateStr + " - " + eDateStr );            
            cell.setCellStyle(bold);


            
            // Next HEADER ROW
            
            cellNum = 0;
            row = sheet.createRow( rowNum );
            rowNum++;

            cell = row.createCell( cellNum );
            cell.setCellValue( "ID" );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;
            
            cell = row.createCell( cellNum );
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrptlvc.Title")  );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;
            
            cell = row.createCell( cellNum );
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.LastName" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;

            cell = row.createCell( cellNum );
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.FirstName" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;
            
            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.Email" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;
                        
            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.Mobile" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;
           
            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "g.Date" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;
            
            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "g.Duration" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;
            
            
            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrptlvc.Initiator" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;

            //cell = row.createCell( cellNum);
            //cell.setCellValue( MessageFactory.getStringMessage(locale, "exrptlvc.Interviewer" ) );
            //cell.setCellStyle(bold);
            //cellNum++;

            for( int i=1;i<=4;i++ )
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrptlvc.InterviewerAddlX", new String[]{Integer.toString(i)} ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }

            for( int i=1;i<=4;i++ )
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrptlvc.OvrRatingX", new String[]{Integer.toString(i)} ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }
            
                   
            for( int i=1;i<=4;i++ )
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrptlvc.OvrCommentsX", new String[]{Integer.toString(i)} ) );                
                cell.setCellStyle(bold);
                cellNum++;
            }

            for( int i=1;i<=4;i++ )
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrptlvc.CriteriaXScoreY", new String[]{ ratingName1, Integer.toString(i)} ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }            

            for( int i=1;i<=4;i++ )
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrptlvc.CriteriaXScoreY", new String[]{ ratingName2, Integer.toString(i)} ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }            

            for( int i=1;i<=4;i++ )
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrptlvc.CriteriaXScoreY", new String[]{ ratingName3, Integer.toString(i)} ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }            
            
            
            for( int i=1;i<=4;i++ )
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrptlvc.AvgScriptX", new String[]{Integer.toString(i)} ) );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }
            
            
            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "g.LVScript" ) );
            cell.setCellStyle(bold);
            colsToAutosize.add( cellNum );
            cellNum++;
            
            for( LvQuestion lvq : questionList )
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( lvq.getName() );
                // colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;                

                for( int i=1;i<=4;i++ )
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( MessageFactory.getStringMessage(locale, "exrptlvc.QuestionScoreX", new String[]{Integer.toString(i)} ) );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(bold);
                    cellNum++;
                }

                for( int i=1;i<=4;i++ )
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( MessageFactory.getStringMessage(locale, "exrptlvc.QuestionCommentsX", new String[]{Integer.toString(i)} ) );
                    cell.setCellStyle(bold);
                    cellNum++;
                }
                
            }
            
            
            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.UserNotes" ) );
            cell.setCellStyle(bold);
            cellNum++;

            int scoreDigits = 2;
            String scoreVal;
            
            
            // OK that's the header row.
            User u;
            LiveVideoOverallRatingType ort;
            LvScript script;
            LvQuestionResponse lvqr;
            
            for( LvCall lvc : lvcl )
            {
                cellNum = 0;

                row = sheet.createRow( rowNum );
                rowNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( lvc.getLvCallId() );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue( lvc.getTitle()==null ? "" : lvc.getTitle() );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue( lvc.getRecipientUser().getLastName() );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue( lvc.getRecipientUser().getFirstName() );
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( lvc.getRecipientUser().getEmail() );
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( lvc.getRecipientUser().getMobilePhone());
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue(  I18nUtils.getFormattedDateTime(locale, lvc.getCompleteDate(), DateFormat.LONG, DateFormat.LONG, timezone )  );
                cellNum++;                
                
                cell = row.createCell( cellNum);
                cell.setCellValue(  lvc.getDurationStr()  );
                cellNum++;   
                
                cell = row.createCell( cellNum);
                cell.setCellValue( lvc.getInitiatorUser().getFullnameReverse() );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue( lvc.getUser().getFullnameReverse() );
                cellNum++;
                
                u=lvc.getMultUser();                
                cell = row.createCell( cellNum);
                cell.setCellValue( u==null ? "" : u.getFullnameReverse() );
                cellNum++;

                u=lvc.getMultUser2();                
                cell = row.createCell( cellNum);
                cell.setCellValue( u==null ? "" : u.getFullnameReverse() );
                cellNum++;

                u=lvc.getMultUser3();                
                cell = row.createCell( cellNum);
                cell.setCellValue( u==null ? "" : u.getFullnameReverse() );
                cellNum++;
                
                ort = LiveVideoOverallRatingType.getValue( lvc.getOverallRatingTypeId() );
                cell = row.createCell(cellNum);
                cell.setCellValue( ort.getName(locale) );
                cellNum++;

                ort = LiveVideoOverallRatingType.getValue( lvc.getOverallRatingTypeId2() );
                cell = row.createCell(cellNum);
                cell.setCellValue( ort.getName(locale) );
                cellNum++;

                ort = LiveVideoOverallRatingType.getValue( lvc.getOverallRatingTypeId3() );
                cell = row.createCell(cellNum);
                cell.setCellValue( ort.getName(locale) );
                cellNum++;

                ort = LiveVideoOverallRatingType.getValue( lvc.getOverallRatingTypeId4() );
                cell = row.createCell(cellNum);
                cell.setCellValue( ort.getName(locale) );
                cellNum++;
                
                cell = row.createCell(cellNum);
                cell.setCellStyle(wrap);
                cell.setCellValue( lvc.getHistory()==null ? "" : lvc.getHistory() );
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellStyle(wrap);
                cell.setCellValue( lvc.getHistory2()==null ? "" : lvc.getHistory2() );
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellStyle(wrap);
                cell.setCellValue( lvc.getHistory3()==null ? "" : lvc.getHistory3() );
                cellNum++;
                
                cell = row.createCell(cellNum);
                cell.setCellStyle(wrap);
                cell.setCellValue( lvc.getHistory4()==null ? "" : lvc.getHistory4() );
                cellNum++;
                
                for( int i=0;i<4;i++ )
                {
                    scoreVal = getCriteriaScoreValue(locale, lvc.getCriteriaScore1()[i]);
                    cell = row.createCell(cellNum);
                    cell.setCellValue( scoreVal );
                    cellNum++;                    
                }

                for( int i=0;i<4;i++ )
                {
                    scoreVal = getCriteriaScoreValue(locale, lvc.getCriteriaScore2()[i]);
                    cell = row.createCell(cellNum);
                    cell.setCellValue( scoreVal );
                    cellNum++;                    
                }

                for( int i=0;i<4;i++ )
                {
                    scoreVal = getCriteriaScoreValue(locale, lvc.getCriteriaScore3()[i]);
                    cell = row.createCell(cellNum);
                    cell.setCellValue( scoreVal );
                    cellNum++;                    
                }
                
                cell = row.createCell(cellNum);
                cell.setCellValue( I18nUtils.getFormattedNumber(locale, lvc.getOverallScriptScore(), scoreDigits) );
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue( I18nUtils.getFormattedNumber(locale, lvc.getOverallScriptScore2(), scoreDigits) );
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue( I18nUtils.getFormattedNumber(locale, lvc.getOverallScriptScore3(), scoreDigits) );
                cellNum++;
                
                cell = row.createCell(cellNum);
                cell.setCellValue( I18nUtils.getFormattedNumber(locale, lvc.getOverallScriptScore4(), scoreDigits) );
                cellNum++;
                
                script = lvc.getLvScript();
                cell = row.createCell(cellNum);
                cell.setCellValue( script==null ? "" : script.getName() );
                cellNum++;
                
                for( LvQuestion lvq : questionList )
                {
                    lvqr = getLvQuestionResponse( lvc.getLvQuestionResponseList(), lvq );
                    
                    cell = row.createCell(cellNum);
                    cell.setCellValue( lvqr==null ? "" : StringUtils.truncateStringWithTrailer( lvq.getQuestion(), 120, true) );
                    cell.setCellStyle(wrap);
                    cellNum++;

                    cell = row.createCell(cellNum);
                    cell.setCellValue( lvqr==null ? "" : I18nUtils.getFormattedNumber(locale, lvqr.getScore1(), scoreDigits) );
                    cellNum++;
                    cell = row.createCell(cellNum);
                    cell.setCellValue( lvqr==null ? "" : I18nUtils.getFormattedNumber(locale, lvqr.getScore2(), scoreDigits) );
                    cellNum++;
                    cell = row.createCell(cellNum);
                    cell.setCellValue( lvqr==null ? "" : I18nUtils.getFormattedNumber(locale, lvqr.getScore3(), scoreDigits) );
                    cellNum++;
                    cell = row.createCell(cellNum);
                    cell.setCellValue( lvqr==null ? "" : I18nUtils.getFormattedNumber(locale, lvqr.getScore4(), scoreDigits) );
                    cellNum++;

                    cell = row.createCell(cellNum);
                    cell.setCellStyle(wrap);
                    cell.setCellValue( lvqr==null ? "" : lvqr.getScoreText() );
                    cellNum++;
                    cell = row.createCell(cellNum);
                    cell.setCellStyle(wrap);
                    cell.setCellValue( lvqr==null ? "" : lvqr.getScoreText2() );
                    cellNum++;
                    cell = row.createCell(cellNum);
                    cell.setCellStyle(wrap);
                    cell.setCellValue( lvqr==null ? "" : lvqr.getScoreText3() );
                    cellNum++;
                    cell = row.createCell(cellNum);
                    cell.setCellStyle(wrap);
                    cell.setCellValue( lvqr==null ? "" : lvqr.getScoreText4() );
                    cellNum++;
                    
                } 
                
                cell = row.createCell( cellNum);
                cell.setCellStyle(wrap);
                cell.setCellValue( getUserNoteStr( locale, lvc.getRecipientUser(), true ) );
                cellNum++;
            }


            
            for( Integer n : colsToAutosize )
            {
                sheet.autoSizeColumn(n);
            }

            sheet.createFreezePane(0, 3 );            

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
    
    
    private String getCriteriaScoreValue( Locale locale, String scoreVal )
    {
        try
        {
            LiveVideoOverallRatingType  ort = LiveVideoOverallRatingType.getValue( scoreVal==null ? 0 :Integer.parseInt(scoreVal));
            return ort.getName(locale);
            
        }
        catch( NumberFormatException e )
        {
            return scoreVal;
        }
    }
    
    private LvQuestionResponse getLvQuestionResponse( List<LvQuestionResponse> lvqrl, LvQuestion lvq )
    {
        if( lvqrl==null )
            return null;
        
        for( LvQuestionResponse r : lvqrl )
        {
            if( r.getLvQuestionId()==lvq.getLvQuestionId() )
                return r;
        }
        return null;
    }
    

    private String getUserNoteStr( Locale locale, User user, boolean includeNamesDates )
    {

        StringBuilder sb = new StringBuilder();

        try
        {

            UserFacade userFacade = null;

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
    
    
    
    

}
