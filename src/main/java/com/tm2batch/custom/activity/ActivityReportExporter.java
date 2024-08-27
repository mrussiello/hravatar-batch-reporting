/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.activity;


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
public class ActivityReportExporter {

    UserFacade userFacade;
    Locale locale;
    
    /*
     * unlimited (Boolean)
     * startdate
     * enddate
     * org
     * suborg - only if suborg only
     * bysuborg  List<SuborgActivitySummary>
     * byproduct  List<ProductActivitySummary>
     * bybattery  List<ProductActivitySummary>
     * byproducttestkey  List<ProductActivitySummary> for Dropoff
     * totaltestkey ProductActivitySummary  for Dropoff
     * rcbysuborg List<RcActivitySummary>
     * lvbysuborg List<LvActivitySummary>
    
    */
    public byte[] getActivityExcelFile( Map<String,Object> dm, Locale locale, TimeZone timezone, GeneralReportOptions excelReportBean, Org org, Date startDate, Date endDate) throws Exception
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
            
            Sheet sheet = wb.createSheet( MessageFactory.getStringMessage(locale, "exrpt.TestResults" ) );

            int rowNum = 0;
            int colNum = 0;
            Row row;
            Cell cell;

            row = sheet.createRow( rowNum );
            rowNum++;

            cell = row.createCell(0);
            cell.setCellValue( lmsg( "exrpt.TestResultsForC" )  ); 
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
            

            
            List<SuborgActivitySummary> bySuborg = (List<SuborgActivitySummary>) dm.get("bysuborg");
            if( bySuborg!=null && !bySuborg.isEmpty() )
            {
                // Title Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                cell = row.createCell(colNum);
                cell.setCellValue( lmsg( "g.DashboardActivityBySuborg" )  ); 
                cell.setCellStyle(bold);

                // Header Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Group" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.UserLogons" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.TestKeysCreated" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.TestStarts" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.TestCompletions" )  ); 
                
                for( SuborgActivitySummary sas : bySuborg )
                {
                    row = sheet.createRow( rowNum );
                    rowNum++;
                    colNum=0;

                    cell = row.createCell(colNum++);
                    cell.setCellValue( sas.getSuborg().getName()  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getUserLogins())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getTestKeysCreated())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getTestKeysStarted())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getTestKeysCompleted())  ); 
                }                

                row = sheet.createRow( rowNum );
                rowNum++;

                if( hasBatteryData(bySuborg) )
                {
                    // Title Row
                    row = sheet.createRow( rowNum );
                    rowNum++;
                    colNum=0;
                    cell = row.createCell(colNum);
                    cell.setCellValue( lmsg( "g.DashboardActivityBySuborgByBattery" )  ); 
                    cell.setCellStyle(bold);

                    // Header Row
                    row = sheet.createRow( rowNum );
                    rowNum++;
                    colNum=0;

                    cell = row.createCell(colNum++);
                    cell.setCellValue( lmsg( "g.Group" )  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( lmsg( "g.BatteryTestKeysCreated" )  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( lmsg( "g.BatteryStarts" )  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( lmsg( "g.BatteryCompletions" )  ); 

                    for( SuborgActivitySummary sas : bySuborg )
                    {
                        row = sheet.createRow( rowNum );
                        rowNum++;
                        colNum=0;

                        cell = row.createCell(colNum++);
                        cell.setCellValue( sas.getSuborg().getName()  ); 

                        cell = row.createCell(colNum++);
                        cell.setCellValue( Integer.toString(sas.getBatteriesCreated())  ); 

                        cell = row.createCell(colNum++);
                        cell.setCellValue( Integer.toString(sas.getBatteriesStarted())  ); 

                        cell = row.createCell(colNum++);
                        cell.setCellValue( Integer.toString(sas.getBatteriesCompleted())  ); 
                    }                

                    row = sheet.createRow( rowNum );
                    rowNum++;
                    colNum=0;
                }
            }

            List<RcActivitySummary> rcBySuborg =  (List<RcActivitySummary>) dm.get("rcbysuborg");
            if( rcBySuborg!=null && !rcBySuborg.isEmpty() )
            {
                // Title Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                cell = row.createCell(colNum);
                cell.setCellValue( lmsg( "g.DashboardActivityBySuborgRc" )  ); 
                cell.setCellStyle(bold);

                // Header Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Group" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.RcDbCreates" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.RcDbCCompletes" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.RcDbCandStarts" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.RcDbCandCompletes" )  ); 
                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.RcDbRtrCreates" )  ); 
                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.RcDbRtrStarts" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.RcDbRtrCompletes" )  ); 
                
                for( RcActivitySummary sas : rcBySuborg )
                {
                    row = sheet.createRow( rowNum );
                    rowNum++;
                    colNum=0;

                    cell = row.createCell(colNum++);
                    cell.setCellValue( sas.getNameToUse()  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getRcChecks())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getRcCheckCompletes())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getCandidateStarts())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getCandidateCompletes())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getRaterCreates())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getRaterStarts())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getRaterCompletes())  ); 
                }                

                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
            }

            List<LvActivitySummary> lvBySuborg =  (List<LvActivitySummary>) dm.get("lvbysuborg");
            if( lvBySuborg!=null && !lvBySuborg.isEmpty() )
            {
                // Title Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                cell = row.createCell(colNum);
                cell.setCellValue( lmsg( "g.DashboardActivityBySuborgLv" )  ); 
                cell.setCellStyle(bold);

                // Header Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Group" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Created" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Started" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Completed" )  ); 
                
                for( LvActivitySummary sas : lvBySuborg )
                {
                    row = sheet.createRow( rowNum );
                    rowNum++;
                    colNum=0;

                    cell = row.createCell(colNum++);
                    cell.setCellValue(  sas.getNameToUse()  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getLvInvitations())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getLvStarts())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getLvCompletes())  ); 
                }                

                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
            }

            if( bySuborg!=null && !bySuborg.isEmpty() )
            {
                // Title Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                cell = row.createCell(colNum);
                cell.setCellValue( lmsg( "g.TestTakerStatusBySuborg" )  ); 
                cell.setCellStyle(bold);

                // Header Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Org" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Status" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Count" )  ); 

                for( SuborgActivitySummary sas : bySuborg )
                {
                    if( sas.getUserCompanyStatusInfo()!=null && !sas.getUserCompanyStatusInfo().isEmpty() )
                    {
                        row = sheet.createRow( rowNum );
                        rowNum++;
                        colNum=0;

                        cell = row.createCell(colNum++);
                        cell.setCellValue( sas.getSuborg().getName()  ); 

                        for( String[] si  : sas.getUserCompanyStatusInfo() )
                        {
                            row = sheet.createRow( rowNum );
                            rowNum++;
                            colNum=0;

                            cell = row.createCell(colNum++);
                            cell.setCellValue( ""  ); 

                            cell = row.createCell(colNum++);
                            cell.setCellValue( si[0]  ); 

                            cell = row.createCell(colNum++);
                            cell.setCellValue( si[1]  );                             
                        }

                        row = sheet.createRow( rowNum );
                        rowNum++;
                        colNum=0;
                    }
                }                

                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
            }
            

            List<ProductActivitySummary> byProd = (List<ProductActivitySummary>) dm.get("byproduct");
            if( byProd!=null && !byProd.isEmpty() )
            {
                // Title Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                cell = row.createCell(colNum);
                cell.setCellValue( lmsg( "g.DashboardActivityByTest" )  ); 
                cell.setCellStyle(bold);

                // Header Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Name" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.TestKeysCreated" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.TestStarts" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.TestCompletions" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.AverageScore" )  ); 
                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.AverageSeconds" )  ); 
                
                
                for( ProductActivitySummary sas : byProd )
                {
                    row = sheet.createRow( rowNum );
                    rowNum++;
                    colNum=0;

                    cell = row.createCell(colNum++);
                    cell.setCellValue( sas.getProduct().getName()  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getTestKeys())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getTestEventStarts())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getCompletions())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Float.toString(sas.getAverageScore())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Float.toString(sas.getAverageSeconds())  ); 
                }                

                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
            }

            List<ProductActivitySummary> byBatt = (List<ProductActivitySummary>) dm.get("bybattery");
            if( byBatt!=null && !byBatt.isEmpty() )
            {
                // Title Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                cell = row.createCell(colNum);
                cell.setCellValue( lmsg( "g.DashboardActivityByBattery" )  ); 
                cell.setCellStyle(bold);

                // Header Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Name" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.TestKeysCreated" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.BatteryStarts" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.BatteryCompletions" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.AverageScoreBatt" )  ); 
                
                
                for( ProductActivitySummary sas : byBatt )
                {
                    row = sheet.createRow( rowNum );
                    rowNum++;
                    colNum=0;

                    cell = row.createCell(colNum++);
                    cell.setCellValue( sas.getProduct().getName()  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getTestKeys())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getTestEventStarts())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getCompletions())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Float.toString(sas.getAverageScore())  ); 

                }                

                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
            }

            Boolean candCredit = (Boolean) dm.get("candidatecredit");
            if( candCredit!=null && candCredit && bySuborg!=null && !bySuborg.isEmpty() )
            {
                // Title Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                cell = row.createCell(colNum);
                cell.setCellValue( lmsg( "g.DashboardActivityBySuborgResultCredits" )  ); 
                cell.setCellStyle(bold);

                // Header Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Group" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.CandidateCreditsAccessed" )  ); 
                
                for( SuborgActivitySummary sas : bySuborg )
                {
                    row = sheet.createRow( rowNum );
                    rowNum++;
                    colNum=0;

                    cell = row.createCell(colNum++);
                    cell.setCellValue( sas.getSuborg().getName()  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getCreditsUsed()) ); 
                }                

                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
            }

            
            // byproducttestkey
            List<ProductActivitySummary> byProdTestKey = (List<ProductActivitySummary>) dm.get("byproducttestkey");
            if( byProdTestKey!=null && !byProdTestKey.isEmpty() )
            {
                // Title Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                cell = row.createCell(colNum);
                cell.setCellValue( lmsg( "g.DashboardDropOffRateInfo" )  ); 
                cell.setCellStyle(bold);

                // Header Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Name" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.DropoffKeysCreated" )  ); 

                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.DropoffAccessed" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( "(%)"  ); 
                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.DropoffStartedTestEvent" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( "(%)"  ); 
                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.DropoffTestCompleted" )  ); 
                
                cell = row.createCell(colNum++);
                cell.setCellValue( "(%)"  ); 
                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.DropoffRate" ) + "%"  ); 
                
                for( ProductActivitySummary sas : byProdTestKey )
                {
                    row = sheet.createRow( rowNum );
                    rowNum++;
                    colNum=0;

                    cell = row.createCell(colNum++);
                    cell.setCellValue( sas.getProduct().getName()  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getTestKeys())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getStarts())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getStartsPct())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getTestEventStarts())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getTestEventStartsPct())  ); 
                    
                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getCompletions()) ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Float.toString(sas.getCompletionsPct()) ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Float.toString(sas.getDropoffRatePct()) );                     
                }                

                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
            }
            
            
            // byproducttestkey
            List<ProductActivitySummary> byProdBatt = (List<ProductActivitySummary>) dm.get("byproductbattery");
            if( byProdBatt!=null && !byProdBatt.isEmpty() )
            {
                // Title Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                cell = row.createCell(colNum);
                cell.setCellValue( lmsg( "g.DashboardDropOffRateInfoBatt" )  ); 
                cell.setCellStyle(bold);

                // Header Row
                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.Name" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.DropoffKeysCreated" )  ); 

                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.DropoffAccessed" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( "(%)"  ); 
                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.DropoffTestCompletedBatt" )  ); 

                cell = row.createCell(colNum++);
                cell.setCellValue( "(%)"  ); 
                                
                cell = row.createCell(colNum++);
                cell.setCellValue( lmsg( "g.DropoffRate" ) + "%"  ); 
                
                for( ProductActivitySummary sas : byProdBatt )
                {
                    row = sheet.createRow( rowNum );
                    rowNum++;
                    colNum=0;

                    cell = row.createCell(colNum++);
                    cell.setCellValue( sas.getProduct().getName()  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getTestKeys())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getStarts())  ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getStartsPct())  ); 
                    
                    cell = row.createCell(colNum++);
                    cell.setCellValue( Integer.toString(sas.getCompletions()) ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Float.toString(sas.getCompletionsPct()) ); 

                    cell = row.createCell(colNum++);
                    cell.setCellValue( Float.toString(sas.getDropoffRatePct()) );                     
                }                

                row = sheet.createRow( rowNum );
                rowNum++;
                colNum=0;
            }
            
            
            for( Integer n : colsToAutosize )
            {
                sheet.autoSizeColumn(n);
            }

            ByteArrayOutputStream bais = new ByteArrayOutputStream();

            wb.write(bais);

            return bais.toByteArray();
        }

        catch ( Exception  e )
        {
            LogService.logIt( e, "ActivityReportExporter.getActivityExcelFile() " );
        }

        return out;
    }
    

    private boolean hasBatteryData( List<SuborgActivitySummary> sasl )
    {
        if( sasl==null || sasl.isEmpty() )
            return false;
        for( SuborgActivitySummary sas : sasl )
        {
            if( sas.getBatteriesCreated()>0 || sas.getBatteriesStarted()>0 || sas.getBatteriesCompleted()>0 )
                return true;
        }
        
        return false;
        
    }
    
    
    private String lmsg( String key )
    {
        return MessageFactory.getStringMessage(locale, key);
    }
    
    

}
