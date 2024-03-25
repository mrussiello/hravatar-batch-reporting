/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.autoreport;

import com.tm2batch.entity.autoreport.BatchReport;

/**
 *
 * @author miker_000
 */
public interface ExecutableReport {
    
    public void setBatchReport( BatchReport br );
    
    public void setOverrideSendFreq( boolean overrideSendFreq );
    
    public void validateBatchReportForExecution() throws Exception;
    
    public int[] executeReport() throws Exception;
    
    public byte[] getBytes();

    public String getFilename();
    
    
}
