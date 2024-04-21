/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.orgjustice.pdf;

import com.tm2batch.custom.orgjustice.OrgJusticeNorms;
import com.tm2batch.custom.orgjustice.UMinnColorType;
import com.tm2batch.service.LogService;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author miker_000
 */
public class UMinnJusticeReportUtils {
    
    private static Properties uminnProperties;

    public static UMinnColorType getScoreColorTypeForOverall( float score )
    {
        return getScoreColorTypeArray( OrgJusticeNorms.overallAvg, score );
    }
    public static UMinnColorType getScoreColorTypeForOverallMale( float score )
    {
        return getScoreColorTypeArray( OrgJusticeNorms.overallAvgMale, score );
    }
    public static UMinnColorType getScoreColorTypeForOverallFemale( float score )
    {
        return getScoreColorTypeArray( OrgJusticeNorms.overallAvgFemale, score );
    }
    public static UMinnColorType getScoreColorTypeForOverallUrim( float score )
    {
        return getScoreColorTypeArray( OrgJusticeNorms.overallAvgUrim, score );
    }
    public static UMinnColorType getScoreColorTypeForOverallNonUrim( float score )
    {
        return getScoreColorTypeArray( OrgJusticeNorms.overallAvgNonUrim, score );
    }

    
    
    public static UMinnColorType getScoreColorTypeForGroup( int groupId, float score )
    {
        return getScoreColorTypeArray( OrgJusticeNorms.groupAverages[groupId-1], score );
    }
    public static UMinnColorType getScoreColorTypeForGroupMale( int groupId, float score )
    {
        return getScoreColorTypeArray( OrgJusticeNorms.groupAveragesMale[groupId-1], score );
    }
    public static UMinnColorType getScoreColorTypeForGroupFemale( int groupId, float score )
    {
        return getScoreColorTypeArray( OrgJusticeNorms.groupAveragesMale[groupId-1], score );
    }
    public static UMinnColorType getScoreColorTypeForGroupUrim( int groupId, float score )
    {
        return getScoreColorTypeArray( OrgJusticeNorms.groupAveragesMale[groupId-1], score );
    }
    public static UMinnColorType getScoreColorTypeForGroupNonUrim( int groupId, float score )
    {
        return getScoreColorTypeArray( OrgJusticeNorms.groupAveragesMale[groupId-1], score );
    }

    public static UMinnColorType getScoreColorTypeForDimension( int dimensionId, float score )
    {
        return getScoreColorTypeArray( OrgJusticeNorms.dimensionAverages[dimensionId-1], score );
    }
    public static UMinnColorType getScoreColorTypeForDimensionMale( int dimensionId, float score )
    {
        return getScoreColorTypeArray( OrgJusticeNorms.dimensionAveragesMale[dimensionId-1], score );
    }
    public static UMinnColorType getScoreColorTypeForDimensionFemale( int dimensionId, float score )
    {
        return getScoreColorTypeArray( OrgJusticeNorms.dimensionAveragesMale[dimensionId-1], score );
    }
    public static UMinnColorType getScoreColorTypeForDimensionUrim( int dimensionId, float score )
    {
        return getScoreColorTypeArray( OrgJusticeNorms.dimensionAveragesMale[dimensionId-1], score );
    }
    public static UMinnColorType getScoreColorTypeFordimensionNonUrim( int dimensionId, float score )
    {
        return getScoreColorTypeArray( OrgJusticeNorms.dimensionAveragesMale[dimensionId-1], score );
    }



    
    
    public static UMinnColorType getScoreColorTypeArray( float norm, float score )
    {
        
        //if( score<0.35f*norm )
        //    return UMinnColorType.RED;
        //if( score<0.7f*norm )
        //    return UMinnColorType.YELLOW;
        if( score<3.0f )
            return UMinnColorType.RED;
        if( score<4.0f )
            return UMinnColorType.YELLOW;
        return UMinnColorType.GREEN;
    }

    
    
    public String getKey( String key )
    {
        if( uminnProperties == null )
            getProperties();
        
        try
        {
            return uminnProperties.getProperty(key, "KEY NOT FOUND" );
        }
        catch( Exception e )
        {
            LogService.logIt( e, "UMinnJusticeReportUtils.getKey() " + key );
            return null;
        }
    }
    
    public Properties getProperties()
    {
        if( uminnProperties== null )
            loadProperties();

        return uminnProperties;
    }
    
    private synchronized void loadProperties()
    {
        try
        {
            Properties prop = new Properties();
            InputStream in = getClass().getResourceAsStream("uminnjustice.properties");
            prop.load(in);
            in.close();
            
            uminnProperties = prop;
            
            LogService.logIt( "UMinnJusticeReportUtils.loadProperties() Properties files has " + prop.size() + " keys.");
        }
        catch( Exception e )
        {
            LogService.logIt( e, "UMinnJusticeReportUtils.loadProperties() " );
        }
    }
    
}
