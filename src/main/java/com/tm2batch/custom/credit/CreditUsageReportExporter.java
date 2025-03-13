/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.credit;


import com.tm2batch.custom.activity.*;
import com.tm2batch.autoreport.GeneralReportOptions;
import com.tm2batch.entity.user.Org;
import com.tm2batch.service.LogService;
import com.tm2batch.user.UserFacade;
import com.tm2batch.util.I18nUtils;
import com.tm2batch.util.MessageFactory;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
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
public class CreditUsageReportExporter {

    UserFacade userFacade;
    Locale locale;
    
    /*
     * startdate
     * enddate
     * org
     * creditsremaining Int
     * creditslast30 Int 
     * creditslast180 Int
     * creditslast365 Int
     * creditsperweek String
     * weeksremaining String    
     * creditactivity List<CandidateCreditUse>
    
    */
    public byte[] getCreditUsageExcelFile( Map<String,Object> dm, Locale locale, TimeZone timezone, GeneralReportOptions excelReportBean, Org org, Date startDate, Date endDate) throws Exception
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
            for( int i=0; i<6; i++ )
            {    
                colsToAutosize.add(i);            
            }
            
            Sheet sheet = wb.createSheet( MessageFactory.getStringMessage(locale, "curpt.CreditUsage" ) );

            int rowNum = 0;
            int colNum;
            Row row;
            Cell cell;

            row = sheet.createRow( rowNum );
            rowNum++;

            cell = row.createCell(0);
            cell.setCellValue( lmsg( "curpt.CreditUsageReportForC" )  ); 
            cell.setCellStyle(bold);

            cell = row.createCell(1);
            cell.setCellValue( org.getName() );            
            cell.setCellStyle(bold);

            // Title stuff
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
            
            
            row = sheet.createRow( rowNum );
            rowNum++;            
            colNum=0;
            cell = row.createCell(colNum++);
            cell.setCellValue( lmsg( "curpt.TotalRemC" )  ); 
            cell = row.createCell(colNum);
            cell.setCellValue( Integer.toString( (Integer)dm.get("creditsremaining"))); 

            row = sheet.createRow( rowNum );
            rowNum++;            
            colNum=0;
            cell = row.createCell(colNum++);
            cell.setCellValue( lmsg( "curpt.Used30C" )  ); 
            cell = row.createCell(colNum);
            cell.setCellValue( Integer.toString( (Integer)dm.get("creditslast30"))); 

            row = sheet.createRow( rowNum );
            rowNum++;            
            colNum=0;
            cell = row.createCell(colNum++);
            cell.setCellValue( lmsg( "curpt.Used180C" )  ); 
            cell = row.createCell(colNum);
            cell.setCellValue( Integer.toString( (Integer)dm.get("creditslast180"))); 

            row = sheet.createRow( rowNum );
            rowNum++;            
            colNum=0;
            cell = row.createCell(colNum++);
            cell.setCellValue( lmsg( "curpt.Used365C" )  ); 
            cell = row.createCell(colNum);
            cell.setCellValue( Integer.toString( (Integer)dm.get("creditslast365"))); 
            
            sheet.createRow( rowNum );
            rowNum++;            

            row = sheet.createRow( rowNum );
            rowNum++;            
            colNum=0;
            cell = row.createCell(colNum++);
            cell.setCellValue( lmsg( "curpt.AvgPerWeekC" )  ); 
            cell = row.createCell(colNum);
            cell.setCellValue( (String)dm.get("creditsperweek")); 

            row = sheet.createRow( rowNum );
            rowNum++;            
            colNum=0;
            cell = row.createCell(colNum++);
            cell.setCellValue( lmsg( "curpt.WeeksRemainingC" )  ); 
            cell = row.createCell(colNum);
            cell.setCellValue( (String)dm.get("weeksremaining")); 

            List<CandidateCreditUse> usagelist = (List<CandidateCreditUse>) dm.get("creditactivity");
            if( usagelist!=null && !usagelist.isEmpty() )
            {
                row = sheet.createRow( rowNum );
                rowNum++;            

                // Title Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                cell = row.createCell(colNum);
                cell.setCellValue( lmsg( "curpt.CreditActivity" )  ); 
                cell.setCellStyle(bold);

                row = sheet.createRow( rowNum );
                rowNum++;            
                
                // Header Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Name" )  ); 
                cell.setCellStyle(bold);

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Email" )  ); 
                cell.setCellStyle(bold);

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Phone" )  ); 
                cell.setCellStyle(bold);

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Department" )  ); 
                cell.setCellStyle(bold);

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.CreditId" )  ); 
                cell.setCellStyle(bold);

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.FirstUsed" )  ); 
                cell.setCellStyle(bold);

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.LastUsed" )  ); 
                cell.setCellStyle(bold);

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.UsageEndDate" )  ); 
                cell.setCellStyle(bold);
                
                for( CandidateCreditUse ccu : usagelist )
                {
                    row = sheet.createRow( rowNum );
                    rowNum++;
                    colNum=0;

                    if( ccu.getUser()==null )
                    {
                        if( userFacade==null )
                            userFacade=UserFacade.getInstance();
                        ccu.setUser(userFacade.getUser(ccu.getUserId()));
                    }
                    
                    if( ccu.getUser().getSuborgId()>0 && ccu.getUser().getSuborg()==null )
                    {
                        if( userFacade==null )
                            userFacade=UserFacade.getInstance();
                        ccu.getUser().setSuborg(userFacade.getSuborg(ccu.getUser().getSuborgId()));
                    }
                    
                    cell = row.createCell(colNum++);
                    cell.setCellValue( ccu.getUser().getFullname()  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( ccu.getUser().getEmail()  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( ccu.getUser().getMobilePhone()==null ? "" : ccu.getUser().getMobilePhone()  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( ccu.getUser().getSuborg()==null ? "" : ccu.getUser().getSuborg().getName()  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( ccu.getCreditIdIndexStr()  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue(  I18nUtils.getFormattedDate(locale, ccu.getFirstUseDate(), TimeZone.getTimeZone("UTC")) ); 
                    
                    cell = row.createCell(colNum++);
                    cell.setCellValue(  I18nUtils.getFormattedDate(locale, ccu.getLastUseDate(), TimeZone.getTimeZone("UTC")) ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue(  I18nUtils.getFormattedDate(locale, ccu.getExpireDate(), TimeZone.getTimeZone("UTC")) ); 
                }
            }
            
            
            for( int n=0;n<7;n++ )
            {
                sheet.autoSizeColumn(n);
            }

            ByteArrayOutputStream bais = new ByteArrayOutputStream();

            wb.write(bais);

            return bais.toByteArray();
        }

        catch ( Exception  e )
        {
            LogService.logIt( e, "CreditUsageReportExporter.getCreditUsageExcelFile() " );
        }

        return out;
    }
    
    
    
    private String lmsg( String key )
    {
        return MessageFactory.getStringMessage(locale, key);
    }
    
    

}
