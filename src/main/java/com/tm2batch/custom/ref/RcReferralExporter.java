/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.ref;

import com.tm2batch.entity.ref.RcReferral;
import com.tm2batch.entity.user.Org;
import com.tm2batch.ref.RcFacade;
import com.tm2batch.ref.RcScriptFacade;

import com.tm2batch.service.LogService;
import com.tm2batch.user.UserFacade;
import com.tm2batch.util.I18nUtils;
import com.tm2batch.util.MessageFactory;
import com.tm2batch.util.StringUtils;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Font;
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
public class RcReferralExporter {

    
    static int MAX_CELL_STR_LEN = 555;
    static int WIDE_COL_WIDTH = 30*256;
    
    UserFacade userFacade;
    RcFacade rcFacade;
    RcScriptFacade rcScriptFacade;
    
    boolean adminUserIdOnly = false;
    
    
    
    public byte[] getRcReferralExcelFile( List<RcReferral> trl, Locale locale, TimeZone timezone, Org org, Date startDate, Date endDate, boolean adminUserIdOnly ) throws Exception
    {
        byte[] out = null;
        try
        {          
            this.adminUserIdOnly=adminUserIdOnly;
            
            for( RcReferral r : trl )
            {
                if( userFacade==null )
                    userFacade=UserFacade.getInstance();
                r.setUser(userFacade.getUser( r.getUserId()));
                r.setReferrerUser(userFacade.getUser(r.getReferrerUserId()));
                
                if( rcFacade==null )
                    rcFacade=RcFacade.getInstance();
                r.setRcScript( rcFacade.getRcScript(r.getRcScriptId()));
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
            
            Sheet sheet = wb.createSheet( MessageFactory.getStringMessage(locale, "exrpt.RcCheckReferrals" ) );

            int rowsToFreeze = 0;
            int rowNum = 0;
            int cellNum = 0;
            Row row;
            Cell cell;

            String nm;

            if( adminUserIdOnly )
            {
                row = sheet.createRow( rowNum );
                rowNum++;
                rowsToFreeze++;

                cell = row.createCell(0);
                cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.FromRcChecksForAdminUserOnly" ) ); 
                cell.setCellStyle(headerCellStyle);

                cell = row.createCell(1);
                cell.setCellValue( I18nUtils.getFormattedDate(locale, new Date(), TimeZone.getTimeZone("UTC")) );            
                cell.setCellStyle(headerCellStyle);
            }
            
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
            rowsToFreeze++;
            
            row = sheet.createRow( rowNum );
            rowNum++;
            rowsToFreeze++;

            
            cellNum = 0;
            CellStyle rowStyle;
            CellStyle wrapCellStyle;


            
            // Next HEADER ROW

            cellNum = 0;
            row = sheet.createRow( rowNum );
            rowNum++;
            rowsToFreeze++;

            cell = row.createCell( cellNum);
            cell.setCellValue( "Id" );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(headerCellStyle);
            cellNum++;

            cell = row.createCell( cellNum);
            cell.setCellValue( "Date" );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(headerCellStyle);
            cellNum++;


            cell = row.createCell( cellNum);
            cell.setCellValue( "First Name" );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(headerCellStyle);
            cellNum++;

            cell = row.createCell( cellNum);
            cell.setCellValue( "Last Name" );
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
            cell.setCellValue( "Source" );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(headerCellStyle);
            cellNum++;

            cell = row.createCell( cellNum);
            cell.setCellValue( "Role" );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(headerCellStyle);
            cellNum++;


            cell = row.createCell( cellNum);
            cell.setCellValue( "Template" );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(headerCellStyle);
            cellNum++;

            cell = row.createCell( cellNum);
            cell.setCellValue( "Status" );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(headerCellStyle);
            cellNum++;

            cell = row.createCell( cellNum);
            cell.setCellValue( "Referrer Notes" );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(headerCellStyle);
            cellNum++;

            cell = row.createCell( cellNum);
            cell.setCellValue( "Notes" );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(headerCellStyle);
            // cellNum++;


            String self = MessageFactory.getStringMessage(locale, "g.Self");
            
            for( RcReferral r : trl )
            {
                tog = !tog;

                cellNum = 0;

                rowStyle = tog ? rowA : rowB;
                wrapCellStyle = tog ? wrapCellStyleA : wrapCellStyleB;

                row = sheet.createRow( rowNum );
                rowNum++;

                cell = row.createCell( cellNum);
                cell.setCellStyle( rowStyle );
                cell.setCellValue( r.getRcReferralId() );
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellStyle( rowStyle );
                cell.setCellValue( I18nUtils.getFormattedDate(locale, r.getCreateDate(), timezone) );
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellStyle( rowStyle );
                cell.setCellValue( r.getUser().getFirstName() );
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellStyle( rowStyle );
                cell.setCellValue( r.getUser().getLastName() );
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellStyle( rowStyle );
                cell.setCellValue( r.getUser().getEmail() );
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellStyle( rowStyle );
                cell.setCellValue(  r.getUser().getMobilePhone()!=null ? r.getUser().getMobilePhone() : "" );
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellStyle( rowStyle );
                cell.setCellValue( r.getReferrerUserId()==r.getUserId() ? self : r.getReferrerUser().getFullname() );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellStyle( rowStyle );
                cell.setCellValue( r.getRoleName() );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellStyle( rowStyle );
                cell.setCellValue( r.getRcScript().getName() );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellStyle( rowStyle );
                cell.setCellValue( r.getRcReferralStatusType().getName(locale) );
                cellNum++;
                
                
                cell = row.createCell( cellNum);
                cell.setCellStyle( rowStyle );
                cell.setCellValue( r.getReferrerNotes()==null || r.getReferrerNotes().isBlank() ? "" : truncateForCell(r.getReferrerNotes()) );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellStyle( rowStyle );
                cell.setCellValue( r.getNotes()==null || r.getNotes().isBlank() ? "" : truncateForCell(r.getNotes()) );
                // cellNum++;                
            }

            
            
            for( Integer n : colsToAutosize )
            {
                sheet.autoSizeColumn(n);
            }
            
            for( Integer n : wideCols )
            {
                sheet.setColumnWidth( n, WIDE_COL_WIDTH );
            }
            
            sheet.createFreezePane(0, rowsToFreeze );            
            ByteArrayOutputStream bais = new ByteArrayOutputStream();
            wb.write(bais);
            return bais.toByteArray();
        }
        catch ( Exception  e )
        {
            LogService.logIt( e, "RcReferralExporter.getRcCheckExcelFile() " );
        }

        return out;
    }
    
    
    
    
    private String truncateForCell( String inStr )
    {
        if( inStr==null || inStr.length()<=MAX_CELL_STR_LEN )
            return inStr;
        
        return StringUtils.truncateStringWithTrailer(inStr, MAX_CELL_STR_LEN, true );
    }
    
    

}
