/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.pdf;

import com.tm2batch.entity.purchase.Product;
import com.tm2batch.entity.report.Report;
import com.tm2batch.entity.user.Org;
import com.tm2batch.entity.user.Suborg;
import com.tm2batch.service.LogService;
import com.tm2batch.util.StringUtils;
import java.util.Map;

/**
 *
 * @author miker_000
 */
public class ReportRules {
    
    
    public Map<String,String> reportRules;
    //public List<NVPair> reportRules = null;
    
    public ReportRules( Org org, Suborg suborg, Product product, Report report)
    {
        reportRules = StringUtils.getReportFlagMap(org, suborg, report, product);
        //if( org!=null )
        //    reportRules=org.getReportFlagList( suborg, report, product);
    }
        
    
    public String getReportRuleAsString( String name )
    {
        if( name == null || name.isEmpty() || reportRules == null || reportRules.isEmpty() )
            return null;

        return reportRules.get(name);
    }
    
    
    public int getReportRuleAsInt( String name )
    {
        //if(1==1)
        //    return null;
        String sv = getReportRuleAsString( name );

        if( sv == null || sv.trim().isEmpty() )
            return 0;

        try
        {
            int v = Integer.parseInt( sv );
            return v;
        }

        catch( NumberFormatException e )
        {
            LogService.logIt( e, "ReportData.getReportRuleAsInt() " + name + ", value=" + sv );
        }

        return 0;
    }

    public boolean getReportRuleAsBoolean( String name )
    {
       return getReportRuleAsInt( name ) == 1;
    }    
}
