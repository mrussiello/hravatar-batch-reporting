/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.service;

import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.autoreport.BatchReportManager;
import java.util.Date;

/**
 *
 * @author Mike
 */
public class AutoBatchThread implements Runnable 
{
    static final int MAX_ERRORS = 40;
    
    public static int errorCount = 0;
    
    
    @Override
    public void run() {

        LogService.logIt( "TM2Batch - AutoBatchThread.run() Starting"  );

        try
        {
            if( errorCount > MAX_ERRORS )
            {
                LogService.logIt( "TM2Batch - Did not start conversion batch because errorCount (" + errorCount + ") > max errors (20) AutoBatchThread.run() "  );
                 (new EmailUtils()).sendEmailToAdmin( "TM2Batch - AutoBatchThread.run() Rejected starting AutoBatch becauseerrorCount (" + errorCount + ") > max errors (20). ", "Time: " + (new Date()).toString() );
                return;
            }
                        
            if( RuntimeConstants.getBooleanValue( "autoReportBatchesOk" ) )
            {                
                LogService.logIt( "TM2Batch - AutoBatchThread.doScoreAutoBatch() Starting Report Batch "  );
                
                if( BatchReportManager.BATCH_IN_PROGRESS )
                {
                    LogService.logIt( "TM2Batch - AutoBatchThread.doScoreAutoBatch() A Batch is already in progress. Skipping. " );                          
                } 
                
                else
                {
                    BatchReportManager sm = new BatchReportManager();

                    try
                    {
                        sm.doReportBatch();
                    }

                    catch( Exception e )
                    {
                        errorCount++;                    
                        LogService.logIt(e, "TM2Batch - AutoBatchThread.run() Error during conversion batch. errorCount=" + errorCount );
                         (new EmailUtils()).sendEmailToAdmin( "TM2Batch - Error during Score Batch.", "Time: " + (new Date()).toString() + ", Error was: " + e.toString() + ", errorCount=" + errorCount + ", AutoBatchThread.run() " );
                    }
                }
            }

        }
        catch( Exception ee )
        {
            errorCount++;
            LogService.logIt(ee, "TM2Batch - AutoBatchThread.run() Uncaught Exception during autobatch. errorCount=" + errorCount );
             (new EmailUtils()).sendEmailToAdmin( "TM2Batch - Uncaught Exception during autobatch.", "Time: " + (new Date()).toString() + ", Error was: " + ee.toString() + ", errorCount=" + errorCount + ",  AutoBatchThread.run() " );
        }
    }



}
