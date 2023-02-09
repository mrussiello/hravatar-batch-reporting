package com.tm2batch.service;

import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.autoreport.BatchReportManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tracker
{
    public static Date startDate = null;

    private static int logonCount = 0;
    private static int logoutCount = 0;
    private static int errorCount = 0;
    private static int batchCount = 0;    
    private static Date lastBatch = null;
    
    private static int batchReportsStarted = 0;
    private static int batchReportsCompleted = 0;
            
    private static int emailMessageSent = 0;
    private static int textMessageSent = 0;
    
        
    public static void addBatchReportStarted()
    {
        batchReportsStarted++;
    }
    public static void addBatchReportComplete()
    {
        batchReportsCompleted++;
    }

            
    public static void addEmailMessageSent()
    {
        emailMessageSent++;
    }
    
    
    public static void addTextMessageSent()
    {
        textMessageSent++;
    }

    
       
    
    public static void startBatch()
    {
        lastBatch = new Date();
        batchCount++;
    }    
   

    public static void addError()
    {
        errorCount++;
    }


    public static void addLogon()
    {
        logonCount++;
    }

    public static void addLogout()
    {
        logoutCount++;
    }




    public static List<String[]> getStatusList()
    {
        List<String[]> ot = new ArrayList<>();

        ot.add( new String[] {"SYSTEM: Auto Report Batches", RuntimeConstants.getBooleanValue( "autoReportBatchesOk" ) ? "ON" : "OFF" } );

        ot.add( new String[] {"SYSTEM: Report Batch In Progress", BatchReportManager.BATCH_IN_PROGRESS ? "YES" : "NO" } );
        ot.add( new String[] {"SYSTEM: Batch Report Errors", Integer.toString( errorCount ) } );
        ot.add( new String[] {"SYSTEM: Current Date", (new Date()).toString() } );
        ot.add( new String[] {"SYSTEM: Last Batch", lastBatch==null ? "None" : (lastBatch).toString() } );

        ot.add( new String[] {"BATCH REPORTS: Batch Reports Started", Integer.toString( batchReportsStarted )} );
        ot.add( new String[] {"BATCH REPORTS: Batch Reports Completed", Integer.toString( batchReportsCompleted )} );

        ot.add( new String[] {"MESSAGES: Emails Sent", Integer.toString( emailMessageSent ) } );
        ot.add( new String[] {"MESSAGES: Test Messages Sent", Integer.toString( textMessageSent ) } );
        
        ot.add( new String[] {"USER: Logons", Integer.toString( logonCount ) } );
        ot.add( new String[] {"USER: Logouts", Integer.toString( logoutCount ) } );

        return ot;
    }



}
