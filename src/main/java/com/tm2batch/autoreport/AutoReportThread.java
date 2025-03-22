/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.autoreport;

import com.tm2batch.entity.autoreport.BatchReport;
import com.tm2batch.service.LogService;

/**
 *
 * @author miker_000
 */
public class AutoReportThread implements Runnable {
    
    BatchReport batchReport;
    boolean overrideSendFreq=false;
    boolean sampleReport;
    BatchReportManager brm;
    
    public AutoReportThread( BatchReport batchReport )
    {
        this.batchReport = batchReport;
    }

    public void setOverrideSendFreq(boolean overrideSendFreq) {
        this.overrideSendFreq = overrideSendFreq;
    }
    
    public byte[] runInline() throws Exception
    {
        return doAutoReport();        
    }
    
    
    @Override
    public void run() {

        // LogService.logIt( "AutoReportThread.run() Starting"  );

        try
        {
            doAutoReport();
            
        }
        catch( Exception e )
        {
            LogService.logIt(e, "AutoReportThread.run() Uncaught Exception" );
        }
    }
    
    public byte[] doAutoReport() throws Exception
    {
        brm = new BatchReportManager();
        
        byte[] bytes = brm.executeSingleBatchReport(batchReport, overrideSendFreq, sampleReport );
        
        LogService.logIt( "AutoReportThread.doAutoReport() Process Completed. Success=" + (bytes==null || bytes.length<=0 ? "false" : "true") );        
        
        return bytes;
    }
    
    public String getFilename()
    {
        return brm==null ? null : brm.getFilename();
    }

    public void setSampleReport(boolean sampleReport) {
        this.sampleReport = sampleReport;
    }
    
    
}
