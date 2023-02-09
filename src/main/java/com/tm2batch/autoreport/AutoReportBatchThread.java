/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.autoreport;

import com.tm2batch.service.LogService;

/**
 *
 * @author miker_000
 */
public class AutoReportBatchThread implements Runnable {
    
    public AutoReportBatchThread()
    {
    }
    
    @Override
    public void run() {

        // LogService.logIt( "AutoReportBatchThread.run() Starting"  );

        try
        {
            doAutoReportBatch();
            
        }
        catch( Exception e )
        {
            LogService.logIt(e, "AutoReportBatchThread.run() Uncaught Exception" );
        }
    }
    
    public void doAutoReportBatch() throws Exception
    {
        BatchReportManager brm = new BatchReportManager();
        
        int[] out = brm.doReportBatch();
        
        LogService.logIt( "AutoReportBatchThread.doAutoReportBatch() Process Completed." );
    }
    
    
}
