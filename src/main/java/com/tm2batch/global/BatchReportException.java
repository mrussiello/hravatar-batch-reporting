/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.global;

/**
 *
 * @author miker_000
 */
public class BatchReportException extends Exception {
    
    int batchReportId;
    
    public BatchReportException( int batchReportId, String message )
    {
        super(message);
        this.batchReportId=batchReportId;
    }

    public BatchReportException( int batchReportId, Exception ee )
    {
        super( ee );
        this.batchReportId=batchReportId;
    }
    
}
