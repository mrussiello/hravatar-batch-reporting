/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.ref;

import com.tm2batch.entity.ref.RcCheck;
import com.tm2batch.entity.ref.RcCompetency;
import com.tm2batch.entity.ref.RcItem;
import com.tm2batch.entity.ref.RcRater;
import com.tm2batch.entity.ref.RcRating;
import com.tm2batch.entity.ref.RcReferral;
import com.tm2batch.entity.ref.RcScript;
import com.tm2batch.entity.ref.RcSuspiciousActivity;
import com.tm2batch.entity.user.Org;
import com.tm2batch.global.Constants;
import com.tm2batch.ref.RcCompetencyWrapper;
import com.tm2batch.ref.RcFacade;
import com.tm2batch.ref.RcItemFormatType;
import com.tm2batch.ref.RcItemWrapper;
import com.tm2batch.ref.RcRaterSourceType;
import com.tm2batch.ref.RcScriptFacade;

import com.tm2batch.service.LogService;
import com.tm2batch.user.UserFacade;
import com.tm2batch.util.I18nUtils;
import com.tm2batch.util.MessageFactory;
import com.tm2batch.util.StringUtils;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Mike
 */
public class RcCheckExporter {

    boolean referralsOnly = false;
    
    
    
    static int MAX_CELL_STR_LEN = 555;
    static int WIDE_COL_WIDTH = 30*256;
    
    UserFacade userFacade;
    RcFacade rcFacade;
    RcScriptFacade rcScriptFacade;
    
    public RcCheckExporter( boolean referralsOnly )
    {
        this.referralsOnly = referralsOnly;
    }
    
    
    public byte[] getRcCheckExcelFile( List<RcCheck> trl, Locale locale, TimeZone timezone, Org org, Date startDate, Date endDate) throws Exception
    {
        byte[] out = null;
        try
        {                       
            Set<RcItem> items = new HashSet<>();
            Set<RcCompetency> competencies = new HashSet<>();
            Set<RcScript> rcScripts = new HashSet<>();
            for( RcCheck rc : trl )
            {
                loadRcCheck( rc );
                
                rcScripts.add( rc.getRcScript() );
            }
            
            if( !referralsOnly )
            {
                for( RcScript s : rcScripts )
                {
                    for( RcCompetencyWrapper rcw : s.getRcCompetencyWrapperList() )
                    {
                        if( rcw.getHasMultipleScoredItems() )
                            competencies.add( rcw.getRcCompetency());
                    }

                    for( RcItemWrapper rciw : s.getAllItemWrapperList() )
                    {
                        items.add( rciw.getRcItem() );
                    }
                }
            }
                 
            Workbook wb = new XSSFWorkbook();
            Font f=wb.createFont();
            CellStyle headerCellStyle = wb.createCellStyle();
            f.setBold(true);
            headerCellStyle.setFont(f);
            headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerCellStyle.setFillPattern( FillPatternType.SOLID_FOREGROUND ); 
            
            
            CellStyle wrapCellStyleA = wb.createCellStyle();
            wrapCellStyleA.setWrapText(true);
            wrapCellStyleA.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            wrapCellStyleA.setFillPattern( FillPatternType.SOLID_FOREGROUND ); 
            wrapCellStyleA.setVerticalAlignment( VerticalAlignment.TOP );
            CellStyle wrapCellStyleB = wb.createCellStyle();
            wrapCellStyleB.setWrapText(true);
            wrapCellStyleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            wrapCellStyleB.setFillPattern( FillPatternType.SOLID_FOREGROUND ); 
            wrapCellStyleB.setVerticalAlignment( VerticalAlignment.TOP );
            
            CellStyle rowA = wb.createCellStyle();
            CellStyle rowB = wb.createCellStyle();
            
            
            rowA.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            rowA.setFillPattern( FillPatternType.SOLID_FOREGROUND ); 
            rowA.setVerticalAlignment( VerticalAlignment.TOP );
            
            rowB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            rowB.setFillPattern( FillPatternType.SOLID_FOREGROUND ); 
            rowB.setVerticalAlignment( VerticalAlignment.TOP );
            
            Font hlink_font = wb.createFont();
            hlink_font.setUnderline(Font.U_SINGLE);
            hlink_font.setColor(IndexedColors.BLUE.getIndex());


            CellStyle hlinkA = wb.createCellStyle();
            hlinkA.setFont(hlink_font);            
            hlinkA.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            hlinkA.setFillPattern( FillPatternType.SOLID_FOREGROUND ); 
            hlinkA.setVerticalAlignment( VerticalAlignment.TOP );
            
            CellStyle hlinkB = wb.createCellStyle();
            hlinkB.setFont(hlink_font);            
            hlinkB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            hlinkB.setFillPattern( FillPatternType.SOLID_FOREGROUND ); 
            hlinkB.setVerticalAlignment( VerticalAlignment.TOP );
            

            
            boolean tog = false;

            XSSFCellStyle wrap = (XSSFCellStyle) wb.createCellStyle();
            wrap.setWrapText( true );

            List<Integer> colsToAutosize = new ArrayList<>();
            List<Integer> wideCols = new ArrayList<>();
            
            Sheet sheet = wb.createSheet( MessageFactory.getStringMessage(locale, referralsOnly ? "exrpt.RcCheckReferrals" : "exrpt.RcChecks" ) );

            int rowNum = 0;
            int cellNum = 0;
            Row row;
            Cell cell;

            String nm;

            row = sheet.createRow( rowNum );
            rowNum++;

            cell = row.createCell(0);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.RcChecksForC" )  ); 
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(1);
            cell.setCellValue( org.getName() );            
            cell.setCellStyle(headerCellStyle);

            row = sheet.createRow( rowNum );
            rowNum++;

            cell = row.createCell(0);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.DatePrepC" ) );            
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(1);
            cell.setCellValue( I18nUtils.getFormattedDate(locale, new Date(), TimeZone.getTimeZone("UTC")) );            
            cell.setCellStyle(headerCellStyle);

            String sDateStr = I18nUtils.getFormattedDate(locale, startDate, timezone );
            String eDateStr = I18nUtils.getFormattedDate(locale, endDate, timezone );

            row = sheet.createRow( rowNum );
            rowNum++;                
            cell = row.createCell(0);
            cell.setCellValue(MessageFactory.getStringMessage(locale, "exrpt.DateRange" )+":" );            
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(1);                
            cell.setCellValue( sDateStr + " - " + eDateStr );            
            cell.setCellStyle(headerCellStyle);
            
            
            row = sheet.createRow( rowNum );
            rowNum++;

            cellNum = 0;
            CellStyle rowStyle;
            CellStyle wrapCellStyle;


            
            if( !referralsOnly )
            {
                // Next HEADER ROW

                cellNum = 0;
                row = sheet.createRow( rowNum );
                rowNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Id" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Status" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Job Title" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Organization" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;


                cell = row.createCell( cellNum);
                cell.setCellValue( "Candidate First" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Candidte Last" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Candidate Email" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Candidate Phone" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Candidate Status" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Overall Score" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Overall Pct Complete" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Rater Id" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Rater Status" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Rater First" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Rater Last" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Rater Email" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Rater Phone" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Rater Start" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Rater Last" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Rater Complete" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Contact OK" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Recruit OK" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Rater Overall Score" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Rater Pct Complete" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Template" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Template Id" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                if( org.getOrgCreditUsageType().getAnyResultCredit() )
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( "Credit Id" );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(headerCellStyle);
                    cellNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellValue( "Credit Index" );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(headerCellStyle);
                    cellNum++;
                }            
                
                for( RcCompetency rcc : competencies )
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue( rcc.getName() + " (overall)" );
                    wideCols.add( cellNum );
                    // colsToAutosize.add( cellNum );
                    cell.setCellStyle(headerCellStyle);
                    cellNum++;                
                }

                for( RcItem itm : items )
                {
                    //cell = row.createCell( cellNum);
                    //cell.setCellValue( "Q " + itm.getRcItemId() ) );
                    //colsToAutosize.add( cellNum );
                    //cell.setCellStyle(bold);
                    //cellNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellValue( "Competency (Q" + itm.getRcItemId() + ")" );
                    wideCols.add( cellNum );
                    // colsToAutosize.add( cellNum );
                    cell.setCellStyle(headerCellStyle);
                    cellNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellValue( "Question (Q" + itm.getRcItemId() + ")" );
                    wideCols.add( cellNum );
                    // colsToAutosize.add( cellNum );
                    cell.setCellStyle(headerCellStyle);
                    cellNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellValue( "Avg Score (Q" + itm.getRcItemId() + ")" );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(headerCellStyle);
                    cellNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellValue( "Score (Q" + itm.getRcItemId() + ")" );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(headerCellStyle);
                    cellNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellValue( "Weight (Q" + itm.getRcItemId() + ")" );
                    wideCols.add( cellNum );
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(headerCellStyle);
                    cellNum++;



                    cell = row.createCell( cellNum);
                    cell.setCellValue( "Selection (Q" + itm.getRcItemId() + ")" );
                    wideCols.add( cellNum );
                    // colsToAutosize.add( cellNum );
                    cell.setCellStyle(headerCellStyle);
                    cellNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellValue( "Comments (Q" + itm.getRcItemId() + ")" );
                    wideCols.add( cellNum );
                    // colsToAutosize.add( cellNum );
                    cell.setCellStyle(headerCellStyle);
                    cellNum++;                
                }

                cell = row.createCell( cellNum);
                cell.setCellValue( "Results URL" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;


                long candidateRaterId;
                List<Long> rcRaterIdsToSkip;        

                int scoreDigits = Constants.RCCHECK_DEF_SCORE_PRECISION_DIGITS;     
                RcRating rating;
                String itemAvgScore;
                String scoreStr;
                String responseStr;
                String weightStr;
                Hyperlink link;      

                CreationHelper createHelper = wb.getCreationHelper();

                for( RcCheck rc : trl )
                {
                    if( rc.getRcRaterList()==null )
                        rc.setRcRaterList(new ArrayList<>());

                    if( rc.getRcRaterList().isEmpty() )
                    {
                        RcRater rtrx = new RcRater();
                        rtrx.setRcRatingList( new ArrayList<>() );
                        rc.getRcRaterList().add( rtrx );
                    }

                    scoreDigits = rc.getScoreDigits();

                    candidateRaterId = rc.getCandidateRcRaterId();
                    rcRaterIdsToSkip = null;        
                    if( candidateRaterId>0 )
                    {
                        rcRaterIdsToSkip = new ArrayList<>();
                        rcRaterIdsToSkip.add(candidateRaterId);
                    }

                    tog = !tog;

                    for( RcRater rtr : rc.getRcRaterList() )
                    {
                        cellNum = 0;

                        rowStyle = tog ? rowA : rowB;
                        wrapCellStyle = tog ? wrapCellStyleA : wrapCellStyleB;

                        row = sheet.createRow( rowNum );
                        rowNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rc.getRcCheckId() );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rc.getRcCheckStatusType().getName(locale) );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rc.getJobTitle() );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rc.getUser().getSuborg()!=null ? rc.getUser().getSuborg().getName() : "" );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rc.getUser().getFirstName() );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rc.getUser().getLastName() );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rc.getUser().getEmail() );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rc.getUser().getMobilePhone());
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rc.getRcCandidateStatusType().getName(locale) );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( I18nUtils.getFormattedNumber(locale, rc.getOverallScore(),scoreDigits) );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( I18nUtils.getFormattedNumber(locale, rc.getPercentComplete(),scoreDigits) );
                        cellNum++;


                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rtr.getRcRaterId()>0 ? rtr.getRcRaterId()+"" : "" );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rtr.getRcRaterId()>0 ? rtr.getRcRaterStatusType().getName(locale) : "" );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rtr.getRcRaterId()>0 ? rtr.getUser().getFirstName() : "" );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rtr.getRcRaterId()>0 ? rtr.getUser().getLastName() : "" );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rtr.getRcRaterId()>0 ? rtr.getUser().getEmail() : "" );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rtr.getRcRaterId()>0 ? rtr.getUser().getMobilePhone(): "" );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rtr.getRcRaterId()>0 && rtr.getStartDate()!=null ? I18nUtils.getFormattedDate(locale, rtr.getStartDate(), timezone): "" );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rtr.getRcRaterId()>0 && rtr.getStartDate()!=null && rtr.getLastUpdate()!=null ? I18nUtils.getFormattedDate(locale, rtr.getLastUpdate(), timezone): "" );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rtr.getRcRaterId()>0 && rtr.getCompleteDate()!=null ? I18nUtils.getFormattedDate(locale, rtr.getCompleteDate(), timezone): "" );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rtr.getRcRaterId()>0 && rtr.getContactPermissionTypeId()==1 ? "Yes" : "" );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rtr.getRcRaterId()>0 && rtr.getRecruitingPermissionTypeId()==1 ? "Yes" : "" );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rtr.getRcRaterId()>0 && rtr.getCompleteDate()!=null ? I18nUtils.getFormattedNumber(locale, rtr.getOverallScore(),scoreDigits) : "" );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rtr.getRcRaterId()>0 && rtr.getStartDate()!=null ? I18nUtils.getFormattedNumber(locale, rtr.getPercentComplete(),scoreDigits) : "" );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rc.getRcScript().getName() );
                        cellNum++;

                        cell = row.createCell( cellNum);
                        cell.setCellStyle( rowStyle );
                        cell.setCellValue( rc.getRcScript().getRcScriptId());
                        cellNum++;

                        if( org.getOrgCreditUsageType().getAnyResultCredit() )
                        {
                            cell = row.createCell( cellNum);
                            cell.setCellStyle( rowStyle );
                            cell.setCellValue( rc.getCreditId() );
                            cellNum++;                        

                            cell = row.createCell( cellNum);
                            cell.setCellStyle( rowStyle );
                            cell.setCellValue( rc.getCreditIndex() );
                            cellNum++;                        
                        }                    
                        
                        for( RcCompetency rcc : competencies )
                        {
                            cell = row.createCell( cellNum);
                            cell.setCellStyle( rowStyle );
                            cell.setCellValue( I18nUtils.getFormattedNumber(locale, getCompetencyAvgScoreNoCand( rc, rcc),scoreDigits) );
                            cellNum++;
                        }

                        for( RcItem item : items )
                        {
                            rating = rtr.getRcRating( item.getRcItemId() );

                            itemAvgScore = getItemAvgScoreStr( rc, item, locale, scoreDigits, rcRaterIdsToSkip );
                            scoreStr = getScoreStr( item, rating, locale, scoreDigits );
                            responseStr = getResponseStr( item, rating, locale );
                            weightStr = getWeightStr( rc, item, rating, locale, scoreDigits );

                            //cell = row.createCell( cellNum);
                            //cell.setCellValue( item.getRcItemId() );
                            //cellNum++;

                            cell = row.createCell( cellNum);
                            cell.setCellValue( item.getRcCompetency()==null ? "" : truncateForCell( item.getRcCompetency().getName() ) );
                            cell.setCellStyle(wrapCellStyle );                        
                            cellNum++;

                            cell = row.createCell( cellNum);
                            cell.setCellValue( item.getQuestion()==null ? "" : truncateForCell( item.getQuestion() ) );
                            cell.setCellStyle(wrapCellStyle );
                            cellNum++;

                            cell = row.createCell( cellNum);
                            cell.setCellValue( itemAvgScore );
                            cell.setCellStyle( rowStyle );
                            cellNum++;

                            cell = row.createCell( cellNum);
                            cell.setCellValue( weightStr );
                            cell.setCellStyle( rowStyle );
                            cellNum++;

                            cell = row.createCell( cellNum);
                            cell.setCellValue( scoreStr );
                            cell.setCellStyle( rowStyle );
                            cellNum++;

                            cell = row.createCell( cellNum);
                            cell.setCellValue( responseStr );
                            cell.setCellStyle(wrapCellStyle );
                            cellNum++;

                            cell = row.createCell( cellNum);
                            cell.setCellValue(rating==null || rating.getText()==null ? "" : truncateForCell( rating.getText() ) );
                            cell.setCellStyle(wrapCellStyle );
                            cellNum++;                    
                        }

                        cell = row.createCell( cellNum);
                        cell.setCellValue( rtr.getRcRaterId()>0 && rtr.getHasAnyRatingData() ? rc.getResultsViewUrl() : "" );                    
                        if( rtr.getRcRaterId()>0 && rtr.getHasAnyRatingData() )
                        {
                            link = createHelper.createHyperlink(HyperlinkType.URL);
                            link.setAddress(rc.getResultsViewUrl());
                            cell.setHyperlink(link);
                        }
                        cell.setCellStyle( tog ? hlinkA : hlinkB );                    
                        cellNum++;                                    
                    }


                }
            }

            List<RcReferral> fullRcReferralList = new ArrayList<>();
            
            for( RcCheck rc : trl )
            {
                if( rc.getRcCheckType().getIsPrehire() )
                    fullRcReferralList.addAll( rc.getRcReferralList() );
            }
            
            if( referralsOnly || !fullRcReferralList.isEmpty() )
            {
                //if( !referralsOnly )
                //{
                    cellNum = 0;
                    sheet.createRow( rowNum );
                    rowNum++;

                    row = sheet.createRow( rowNum );
                    rowNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellValue( "Reference Check Referrals (" + fullRcReferralList.size() + ")");
                    colsToAutosize.add( cellNum );
                    cell.setCellStyle(headerCellStyle);
                    cellNum++;
                //}
            }
                
            if( !fullRcReferralList.isEmpty() )
            {
                Collections.sort(fullRcReferralList);
                
                cellNum = 0;
                row = sheet.createRow( rowNum );
                rowNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Last Name" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "First Name" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue( "Email" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Phone" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue( "Template" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue( "Referred By" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue( "Date" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(headerCellStyle);
                cellNum++;     
                
                for( RcReferral rfl : fullRcReferralList )
                {
                    cellNum = 0;
                    row = sheet.createRow( rowNum );
                    rowNum++;

                    rowStyle = rowB; //  tog ? rowA : rowB;
                    
                    cell = row.createCell( cellNum);
                    cell.setCellValue( rfl.getUser()==null ? "" : rfl.getUser().getLastName() );
                    cell.setCellStyle(rowStyle);
                    cellNum++;
                    
                    cell = row.createCell( cellNum);
                    cell.setCellValue( rfl.getUser()==null ? "" : rfl.getUser().getFirstName() );
                    cell.setCellStyle(rowStyle);
                    cellNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellValue( rfl.getUser()==null || !rfl.getUser().getHasValidEmail() ? "" : rfl.getUser().getEmail() );
                    cell.setCellStyle(rowStyle);
                    cellNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellValue( rfl.getUser()==null || !rfl.getUser().getHasMobilePhone() ? "" : rfl.getUser().getMobilePhone());
                    cell.setCellStyle(rowStyle);
                    cellNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellValue( rfl.getRcScript()==null ? "" : rfl.getRcScript().getName() );
                    cell.setCellStyle(rowStyle);
                    cellNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellValue( rfl.getReferrerUser()==null ? "" : rfl.getReferrerUser().getLastName() );
                    cell.setCellStyle(rowStyle);
                    cellNum++;

                    cell = row.createCell( cellNum);
                    cell.setCellValue( I18nUtils.getFormattedDate(locale, rfl.getCreateDate(), timezone) );
                    cell.setCellStyle(rowStyle);
                    cellNum++;
                    
                    tog = !tog;                    
                }
            }

            
            
            
            for( Integer n : colsToAutosize )
            {
                sheet.autoSizeColumn(n);
            }
            
            for( Integer n : wideCols )
            {
                sheet.setColumnWidth( n, WIDE_COL_WIDTH );
            }
            
            sheet.createFreezePane(0, referralsOnly ? 6 : 4 );            
            ByteArrayOutputStream bais = new ByteArrayOutputStream();
            wb.write(bais);
            return bais.toByteArray();
        }
        catch ( Exception  e )
        {
            LogService.logIt( e, "RcCheckExporter.getRcCheckExcelFile() " );
        }

        return out;
    }
    
    
    
    private String getWeightStr( RcCheck rc, RcItem item, RcRating rating, Locale locale, int scoreDigits )
    {
        if( item==null || rating==null || rating.getRcRatingStatusType().getIsIncomplete() || rating.getRcRatingStatusType().getIsSkipped() || !rating.getHasScore() )
            return "";
        
        RcItemWrapper rciw = rc.getRcScript().getRcItemWrapper(item.getRcItemId() );
        if( rciw==null )
            return "";
        return I18nUtils.getFormattedNumber(locale, rciw.getWeight(), scoreDigits );
    }
    
    
    private String getScoreStr( RcItem item, RcRating rating, Locale locale, int scoreDigits )
    {
        if( item==null || rating==null || rating.getRcRatingStatusType().getIsIncomplete() || rating.getRcRatingStatusType().getIsSkipped() || !rating.getHasScore() )
            return "";
        
        if( rating.getScore()<=0 )
            return "";
        
        return I18nUtils.getFormattedNumber(locale, rating.getFinalScore(), scoreDigits );
    }
    
    private String getResponseStr( RcItem item, RcRating rating, Locale locale )
    {
        if( item==null || rating==null || rating.getRcRatingStatusType().getIsIncomplete() )
            return "";
        
        if( rating.getRcRatingStatusType().getIsSkipped() )
            return MessageFactory.getStringMessage(locale, "rcrrst.skipped" );
        
        if( !item.getRcItemFormatType().getHasChoices() )
            return "";
        
        String r = rating.getSelectedChoicesText();
        
        if( item.getRcItemFormatType().equals( RcItemFormatType.MULTIPLE_CHECKBOX) )
            r = StringUtils.replaceStr(r, "\n", " " );
        
        return r;
    }
    
    
    private float getCompetencyAvgScoreNoCand( RcCheck rc, RcCompetency rcc )
    {
        if( rc.getRcScript()==null )
            return 0;
        
        RcCompetencyWrapper rcw = rc.getRcScript().getRcCompetencyWrapper( rcc.getRcCompetencyId() );
        if( rcw==null )
            return 0;
        return rcw.getScoreAvgNoCandidate();
    }

    private String getItemAvgScoreStr( RcCheck rc, RcItem item, Locale locale, int scoreDigits, List<Long> rcRaterIdsToSkip )
    {        
        if( rc.getRcScript()==null || (!item.getRcItemFormatType().getHasChoicePoints() && !item.getRcItemFormatType().getIsRating()) )
            return "";
        
        RcItemWrapper rciw = rc.getRcScript().getRcItemWrapper( item.getRcItemId() );
        if( rciw==null )
            return "";
                
        float avg = rciw.getAverageScore(rcRaterIdsToSkip);  
        
        return avg>0 ? I18nUtils.getFormattedNumber(locale, avg, scoreDigits ) : "";
    }
    
    
    private String truncateForCell( String inStr )
    {
        if( inStr==null || inStr.length()<=MAX_CELL_STR_LEN )
            return inStr;
        
        return StringUtils.truncateStringWithTrailer(inStr, MAX_CELL_STR_LEN, true );
    }
    
    
    private void loadRcCheck( RcCheck rc ) throws Exception
    {
        if( rc==null )
            return;

        if( rc.getUser()==null )
        {
            if( userFacade==null )
                userFacade = UserFacade.getInstance();
            rc.setUser( userFacade.getUser( rc.getUserId() ) );
        }
        
        if( rc.getAdminUser()==null  )
        {
            if( userFacade==null )
                userFacade = UserFacade.getInstance();
            rc.setAdminUser( userFacade.getUser( rc.getAdminUserId() ) );                
        }

        // always replace the script and items.
        if( rc.getRcScript()==null )
        {
            if( rcScriptFacade == null )
                rcScriptFacade = RcScriptFacade.getInstance();
            rc.setRcScript( rcScriptFacade.getRcScript( rc.getRcScriptId() ));            
        }
                
        if( rcFacade==null )
            rcFacade = RcFacade.getInstance();
        
        rc.setRcRaterList( rcFacade.getRcRaterList( rc.getRcCheckId() ) );        
        for( RcRater r : rc.getRcRaterList() )
            loadRcRater(r, rc );
        
        
        if( rc.getRcCheckType().getIsPrehire() && rc.getRcReferralList()==null )
        {
            if( rcFacade==null )
                rcFacade = RcFacade.getInstance();
            rc.setRcReferralList(rcFacade.getRcReferralList(rc.getRcCheckId()) );
        }
        
        // do this after Raters are fully loaded
        
        // load referrers
        if( rc.getRcCheckType().getIsPrehire() && rc.getRcReferralList()!=null && !rc.getRcReferralList().isEmpty() )
        {
            for( RcReferral rl : rc.getRcReferralList() )
            {
                if( rl.getRcScript()==null )
                {
                    if( rcScriptFacade==null )
                        rcScriptFacade=RcScriptFacade.getInstance();
                    rl.setRcScript( rcScriptFacade.getRcScript( rl.getRcScriptId() ));
                }
                
                if( rl.getUser()==null )
                {
                    if( userFacade==null )
                        userFacade = UserFacade.getInstance();
                    rl.setUser( userFacade.getUser( rl.getUserId() ));
                }
                
                if( rl.getReferrerUser()==null )
                {
                    for( RcRater rtr : rc.getRcRaterList() )
                    {
                        if( rtr.getUserId()==rl.getReferrerUserId() )
                            rl.setReferrerUser(rtr.getUser() );
                    }
                }
                
                // should not happen
                if( rl.getReferrerUser()==null )
                {
                    if( userFacade==null )
                        userFacade = UserFacade.getInstance();
                    rl.setReferrerUser( userFacade.getUser( rl.getReferrerUserId() ));
                }
            }
        }
        
        
        if( referralsOnly )
            return;
                
        // always replace the script and items.
        if( rc.getRcScript()!=null )
        {
            if( rcScriptFacade == null )
                rcScriptFacade = RcScriptFacade.getInstance();            
            rcScriptFacade.loadScriptObjects(rc.getRcScript(), true );
            rc.setItemsAndRatings();
        }
                
        if( rc.getRcSuspiciousActivityList()==null )
        {
            if( rcFacade==null )
                rcFacade = RcFacade.getInstance();
            rc.setRcSuspiciousActivityList( rcFacade.getRcSuspiciousActivityList(rc.getRcCheckId()) );
        }
        
        RcSuspiciousActivity raterRaterSa = null;
        
        for( RcSuspiciousActivity sa : rc.getRcSuspiciousActivityList() )
        {
            if( sa.getRcSuspiciousActivityType().getIsRaterRater() )
                raterRaterSa = sa;
            
            if( sa.getUser()==null )
            {
                if( sa.getRcRaterId()<=0 )
                    sa.setUser(rc.getUser());
                else
                {
                    for( RcRater r : rc.getRcRaterList() )
                    {
                        if( r.getRcRaterId()==sa.getRcRaterId() )
                        {
                            sa.setUser( r.getUser() );
                            break;
                        }
                    }
                }
            }
        }
        
        if( raterRaterSa!=null )
        {
            StringBuilder sb = new StringBuilder();
            RcRater rcr;
            for( Long rid : raterRaterSa.getUserIdSet() )
            {
                rcr = null;
                for( RcRater r : rc.getRcRaterList() )
                {
                    if( r.getRcRaterId()==rid )
                    {
                        rcr=r;
                        break;
                    }
                }
                if( rcr!=null )
                {
                    if( sb.length()>0 )
                        sb.append( ", ");
                    sb.append( rcr.getUser().getFullname() );
                }
            }
        }
    }
    
    

    
    private void loadRcRater( RcRater rtr, RcCheck rcCheck) throws Exception
    {
        if( rtr==null )
            return;
        rtr.setRcRaterSourceType( RcRaterSourceType.getForRcRater(rcCheck, rtr));
        
        if( rtr.getUser()==null )
        {
            if( userFacade==null )
                userFacade = UserFacade.getInstance();
            rtr.setUser(userFacade.getUser(rtr.getUserId() ) );
        }

        if( referralsOnly )
            return;
                        
        // always replace the ratings.
        if( rcFacade==null )
            rcFacade = RcFacade.getInstance();        
        rtr.setRcRatingList(rcFacade.getRcRatingList(rtr.getRcCheckId(), rtr.getRcRaterId() ));        
    }    
    
    

}
