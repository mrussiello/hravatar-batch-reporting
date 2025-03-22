/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.autoreport;

import com.tm2batch.entity.autoreport.BatchReport;
import java.lang.reflect.Constructor;

/**
 *
 * @author miker_000
 */
public class ExecutableReportFactory {
    
    public static ExecutableReport getExecutableReport( BatchReport br, boolean overrideSendFreq, boolean sampleReport) throws Exception
    {
        BatchReportContentType brct = BatchReportContentType.getValue( br.getBatchReportContentTypeId() );
        
        Class<ExecutableReport> tc = (Class<ExecutableReport>) Class.forName(  brct.getClassName() );

        Constructor ctor = tc.getDeclaredConstructor();
        ExecutableReport er = (ExecutableReport) ctor.newInstance(); 
        er.setBatchReport(br);
        er.setOverrideSendFreq(overrideSendFreq);
        er.setSampleReport(sampleReport);
        return er;
    }
}
