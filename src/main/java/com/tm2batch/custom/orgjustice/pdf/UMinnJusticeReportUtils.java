/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.orgjustice.pdf;

import com.tm2batch.service.LogService;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author miker_000
 */
public class UMinnJusticeReportUtils {
    
    private static Properties uminnProperties;
    
    
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
            
            // LogService.logIt( "UMinnJusticeReportUtils.loadProperties() Properties files has " + prop.size() + " keys.");
        }
        catch( Exception e )
        {
            LogService.logIt( e, "UMinnJusticeReportUtils.loadProperties() " );
        }
    }
    
}
