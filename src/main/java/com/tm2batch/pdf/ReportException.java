/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.pdf;


/**
 *
 * @author Mike
 */
public class ReportException extends Exception
{

    // 0 = not permanent. 1=permanent
    private long reportId = 0;
    private String reportLangStr;


    public ReportException( String message, long reportId, String reportLangStr )
    {
        super( message );
        this.reportId = reportId;
        this.reportLangStr=reportLangStr;
    }

    @Override
    public String toString()
    {
        return "ReportException {" + getMessage() +  ", reportId=" + this.reportId + ", reportLanguage=" + reportLangStr + "}";

    }

    public long getReportId() {
        return reportId;
    }

    public String getReportLangStr() {
        return reportLangStr;
    }
}
