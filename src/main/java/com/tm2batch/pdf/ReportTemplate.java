/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.pdf;

import java.util.Locale;

/**
 *
 * @author Mike
 */
public interface ReportTemplate
{
    void init( ReportData reportData ) throws Exception;
    
    byte[] generateReport() throws Exception;
    
    void setSampleReport( boolean sampleReport);

    Locale getReportLocale();
    
    void dispose() throws Exception;
    
    boolean getIsReportGenerationPossible();
}
