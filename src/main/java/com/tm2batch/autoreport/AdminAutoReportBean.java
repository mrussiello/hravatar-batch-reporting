/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.autoreport;

import com.tm2batch.entity.autoreport.BatchReport;
import com.tm2batch.service.LogService;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.io.Serializable;
import java.util.List;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Mike
 */
@Named
@SessionScoped
public class AdminAutoReportBean implements Serializable {


    BatchReport batchReport;
    int batchReportId;
    List<BatchReport> batchReportList;
    int frequencyTypeId;
    
    byte[] reportBytes = null;
    String reportName = null;
    

    public static AdminAutoReportBean getInstance()
    {
        FacesContext fc = FacesContext.getCurrentInstance();

        if( fc==null )
            return null;
        
        return (AdminAutoReportBean) fc.getApplication().getELResolver().getValue( fc.getELContext(), null, "adminAutoReportBean" );
    }

    // @Inject
    // private Conversation conversation;   
    public StreamedContent getExcelFileForDownload() {

        try
        {

            if( reportBytes == null || reportBytes.length==0)
                return null;
            
            InputStream bais = new ByteArrayInputStream( reportBytes );

            // StreamedContent file = new DefaultStreamedContent(bais, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "testresults_" + orgNameTrunc + "_" + ds + ".xlsx");

            StreamedContent file = DefaultStreamedContent.builder().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").name(reportName).stream(() -> bais).build();
            
            return file;
        }

        catch ( Exception e )
        {
            LogService.logIt( e, "AdminAutoReportBean.getExcelFileForDownload() size of reportBytes=" + (reportBytes==null ? "null" :reportBytes.length ) );
        }

        return null;
    }
    
    
    
    
        
    public void clearBean()
    {
        batchReport=null;
        reportBytes=null;
        reportName=null;
    }

    public BatchReport getBatchReport() {
        return batchReport;
    }

    public void setBatchReport(BatchReport batchReport) {
        this.batchReport = batchReport;
    }

    public int getFrequencyTypeId() {
        return frequencyTypeId;
    }

    public void setFrequencyTypeId(int frequencyTypeId) {
        this.frequencyTypeId = frequencyTypeId;
    }

    public List<BatchReport> getBatchReportList() {
        return batchReportList;
    }

    public void setBatchReportList(List<BatchReport> batchReportList) {
        this.batchReportList = batchReportList;
    }

    public int getBatchReportId() {
        return batchReportId;
    }

    public void setBatchReportId(int batchReportId) {
        this.batchReportId = batchReportId;
    }

    public byte[] getReportBytes() {
        return reportBytes;
    }

    public void setReportBytes(byte[] reportBytes) {
        this.reportBytes = reportBytes;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }
    

}
