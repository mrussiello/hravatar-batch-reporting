/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.ibmcloud;


import com.tm2batch.global.Constants;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.score.TextAndTitle;
import com.tm2batch.service.LogService;
import com.tm2batch.util.StringUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author miker_000
 */
public class InsightReportTrait implements Serializable 
{
    
    public static int MARKER_WIDTH = 7;
    public static float MARKER_LEFT_ADJ = 3.5f;
    
    
    String name;
    String scoreStr;
    float score;
    String descrip;
    InsightPersonalityFactor factor;
    InsightTraitType type;
    List<String> facets;
    boolean valid = false;
    
    public InsightReportTrait( TextAndTitle tt )
    {
        if( tt==null )
            return;
        
        name = tt.getTitle();
        
        if( name!=null && name.equals( Constants.IBMLOWWORDSERROR ) )
        {
            String txt = tt.getText();            
            if( txt.indexOf(';')>=0 )
                txt = txt.substring(0, txt.indexOf(';') );
            scoreStr = txt;
            valid=true;
            return;
        }
        
        String[] vals = tt.getText().split(";");        
        if( vals.length>0 )
        {
            int ipt = Integer.parseInt(vals[0]);
            factor = InsightPersonalityFactor.getValue(ipt);
            type = factor.getInsightTraitType();            
        }
                
        if( vals.length>1 )
            scoreStr = vals[1].trim();
        
        if( scoreStr!=null&& !scoreStr.isEmpty() )
        {
            score = Float.parseFloat( scoreStr );
            valid=true;
        }
        
        if( vals.length>2 )
            descrip = vals[2].trim();
        
        facets = new ArrayList<>();
        String t;
        
        if( vals.length>3 )
        {
            for( int i=3; i<vals.length; i++ )
            {
                t = vals[i].trim();
                if( !t.isEmpty() )
                    facets.add( t );
            }
        }        
    }
    
    
        
    
    public String getNameXhtml()
    {
        return StringUtils.replaceStandardEntities( name );
    }
    
    public String getDescripXhtml()
    {
        return StringUtils.replaceStandardEntities( descrip );
    }
    
    public List<String> getFacetsXhtml() 
    {
        List<String> out = new ArrayList<>();
        
        if( facets==null )
            return out;
        
        for( String f : facets )
        {
            out.add( StringUtils.replaceStandardEntities( f ) );
        }
        
        return out;
    }
    
    
    
    
    

    public boolean getIsValid()
    {
        return valid;
    }
    
    public String getName() {
        return name;
    }

    public String getScoreStr() {
        return scoreStr;
    }

    public float getScore() {
        return score;
    }

    public String getDescrip() {
        return descrip;
    }

    public List<String> getFacets() {
        return facets;
    }

    public InsightPersonalityFactor getFactor() {
        return factor;
    }

    public InsightTraitType getType() {
        return type;
    }
    
    
    
}
