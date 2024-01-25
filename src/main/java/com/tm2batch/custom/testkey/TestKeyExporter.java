/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.testkey;


import com.tm2batch.autoreport.GeneralReportOptions;
import com.tm2batch.entity.event.TestEvent;
import com.tm2batch.entity.event.TestKey;
import com.tm2batch.entity.user.Org;
import com.tm2batch.entity.user.UserAction;
import com.tm2batch.service.LogService;
import com.tm2batch.user.UserFacade;
import com.tm2batch.util.I18nUtils;
import com.tm2batch.util.MessageFactory;
import com.tm2batch.util.NumberUtils;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Mike
 */
public class TestKeyExporter {

    UserFacade userFacade;
    Locale locale;
    
    public byte[] getTestKeyExcelFile( List<TestKey> tkl, Locale locale, TimeZone timezone, GeneralReportOptions excelReportBean, Org org, Date startDate, Date endDate) throws Exception
    {
        byte[] out = null;

        if( locale==null )
            locale=Locale.US;
        this.locale=locale;
        
        try
        {

            Workbook wb = new XSSFWorkbook();

            Font f=wb.createFont();
            CellStyle bold = wb.createCellStyle();
            f.setBold(true);
            bold.setFont(f);

            XSSFCellStyle wrap = (XSSFCellStyle) wb.createCellStyle();
            wrap.setWrapText( true );

            List<Integer> colsToAutosize = new ArrayList<>();
            
            Sheet sheet = wb.createSheet( MessageFactory.getStringMessage(locale, "exrpt.TestKeys" ) );

            int rowNum = 0;
            int cellNum ;
            Row row;
            Cell cell;

            row = sheet.createRow( rowNum );
            rowNum++;

            cell = row.createCell(0);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.TestKeysForC" )  ); 
            cell.setCellStyle(bold);
            sheet.addMergedRegion( new CellRangeAddress( rowNum-1, rowNum-1, 0, 1) );

            cell = row.createCell(2);
            cell.setCellValue( org.getName() );             
            cell.setCellStyle(bold);
            sheet.addMergedRegion( new CellRangeAddress( rowNum-1, rowNum-1, 2, 4) );

            row = sheet.createRow( rowNum );
            rowNum++;

            cell = row.createCell(0);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.DatePrepC" ) );            
            cell.setCellStyle(bold);
            sheet.addMergedRegion( new CellRangeAddress( rowNum-1, rowNum-1, 0, 1) );

            cell = row.createCell(2);
            cell.setCellValue( I18nUtils.getFormattedDate(locale, new Date(), TimeZone.getTimeZone("UTC")) );            
            cell.setCellStyle(bold);
            sheet.addMergedRegion( new CellRangeAddress( rowNum-1, rowNum-1, 2, 4) );

            String sDateStr = I18nUtils.getFormattedDate(locale, startDate, timezone );
            String eDateStr = I18nUtils.getFormattedDate(locale, endDate, timezone );

            row = sheet.createRow( rowNum );
            rowNum++;                
            cell = row.createCell(0);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.DateRange" )+":" );            
            cell.setCellStyle(bold);
            sheet.addMergedRegion( new CellRangeAddress( rowNum-1, rowNum-1, 0, 1) );
            cell = row.createCell(2);                
            cell.setCellValue( sDateStr + " - " + eDateStr );            
            cell.setCellStyle(bold);
            sheet.addMergedRegion( new CellRangeAddress( rowNum-1, rowNum-1, 2, 4) );


            row = sheet.createRow( rowNum );
            rowNum++;
            
            // Next HEADER ROW
            
            cellNum = 0;
            row = sheet.createRow( rowNum );
            rowNum++;
            
            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.Identifier" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;

            cell = row.createCell( cellNum );
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.Department")  );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;


            cell = row.createCell( cellNum );
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.FirstName" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;

            cell = row.createCell( cellNum );
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.LastName" ) );
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
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.TestTakerIdentifier" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;


            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.TestorBattery" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;


            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.Battery" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;

            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.Created" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;

            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.Expires" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;

            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.EmailFirstOpened" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;

            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.LastEmail" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;
                        
            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.LastText" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;
                        
            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.LastAccessed" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;

            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.PercentComplete" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;

            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.TestAdminUserName" ) );
            cell.setCellStyle(bold);
            cellNum++;
            
            cell = row.createCell( cellNum);
            cell.setCellValue( MessageFactory.getStringMessage(locale, "exrpt.StartUrl" ) );
            colsToAutosize.add( cellNum );
            cell.setCellStyle(bold);
            cellNum++;
            
            if( org.getOrgCreditUsageType().getAnyResultCredit() )
            {
                cell = row.createCell( cellNum);
                cell.setCellValue( "Credit Id" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue( "Credit Index" );
                colsToAutosize.add( cellNum );
                cell.setCellStyle(bold);
                cellNum++;
            }
            
                        
            Date firstEmailOpened;

            for( TestKey tk : tkl )
            {
                firstEmailOpened = null;
                
                if( tk.getUserActionList()!=null && !tk.getUserActionList().isEmpty() )
                {
                    for( UserAction ua : tk.getUserActionList() )
                    {
                        if( ua.getEventDate()!=null )
                        {
                            if( firstEmailOpened==null || firstEmailOpened.after( ua.getEventDate() ))
                                firstEmailOpened = ua.getEventDate();
                        }
                    }
                }
                
                cellNum = 0;

                row = sheet.createRow( rowNum );
                rowNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue(tk.getTestKeyId() );
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue(tk.getSuborg()!=null ? tk.getSuborg().getName() : "" );
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue(tk.getUser()!=null ? tk.getUser().getFirstName() : "" );
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue(tk.getUser()!=null ? tk.getUser().getLastName() : "" );
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue(tk.getUser()!=null ? tk.getUser().getEmail() : "" );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue(tk.getUser()!=null ? tk.getUser().getMobilePhone(): "" );
                cellNum++;

                cell = row.createCell( cellNum);
                cell.setCellValue(tk.getUser()!=null ? tk.getUser().getAltIdentifier(): "" );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue(tk.getProduct().getName() );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue(tk.getBattery()!=null ? tk.getBattery().getName(): "" );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue(I18nUtils.getFormattedDateTime(locale, tk.getStartDate(), DateFormat.MEDIUM, DateFormat.LONG, timezone ) );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue(tk.getExpireDate()==null ? "" : I18nUtils.getFormattedDateTime(locale, tk.getExpireDate(), DateFormat.MEDIUM, DateFormat.LONG, timezone ) );
                cellNum++;
                
                
                cell = row.createCell( cellNum);
                cell.setCellValue(firstEmailOpened==null ? "" :  I18nUtils.getFormattedDateTime(locale, firstEmailOpened, DateFormat.MEDIUM, DateFormat.LONG, timezone ) );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue(tk.getLastEmailDate()==null ? "" : I18nUtils.getFormattedDateTime(locale, tk.getLastEmailDate(), DateFormat.MEDIUM, DateFormat.LONG, timezone ) );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue(tk.getLastTextDate()==null ? "" : I18nUtils.getFormattedDateTime(locale, tk.getLastTextDate(), DateFormat.MEDIUM, DateFormat.LONG, timezone ) );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue(tk.getLastAccessDate()==null ? "" : I18nUtils.getFormattedDateTime(locale, tk.getLastAccessDate(), DateFormat.MEDIUM, DateFormat.LONG, timezone ) );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue(getPercentCompleteStrForTestKey(tk ) );
                cellNum++;
                
                cell = row.createCell( cellNum);
                cell.setCellValue(tk.getAuthorizingUser()==null ? "" : tk.getAuthorizingUser().getFullname() );
                cellNum++;
            
                cell = row.createCell( cellNum);
                cell.setCellValue(tk.getStartUrl() );
                cellNum++;  
                
                if( org.getOrgCreditUsageType().getAnyResultCredit() )
                {
                    cell = row.createCell( cellNum);
                    cell.setCellValue(tk.getCreditId() );
                    cellNum++;  

                    cell = row.createCell( cellNum);
                    cell.setCellValue(tk.getCreditIndex() );
                    cellNum++;  
                }
            }
            
            for( Integer n : colsToAutosize )
            {
                sheet.autoSizeColumn(n);
            }

            sheet.createFreezePane(0,4 );            

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
    
    private String getPercentCompleteStrForTestKey( TestKey tk  )
    {
        float pc = estimatePercentCompleteForTestKey( tk  );
        return NumberUtils.getTwoDecimalFormattedAmount(pc);
    }
    
    
    
    private float estimatePercentCompleteForTestKey( TestKey tk  )
    {
        if( tk==null || tk.getTestKeyStatusType().getIsActive() || tk.getTestEventList()==null || tk.getTestEventList().isEmpty() )
            return 0;
        float total = 0;
        float count=0;
        
        
        for( TestEvent te : tk.getTestEventList() )
        {
            count++;
            total += te.getPercentComplete(); 
        }
        
        if( tk.getBattery()!=null && tk.getBattery().getProductIds()!=null && !tk.getBattery().getProductIds().isBlank() )
            count = tk.getBattery().getProductIdList().size();
        
        return count>0 ? total/count : 0;
    }

}
