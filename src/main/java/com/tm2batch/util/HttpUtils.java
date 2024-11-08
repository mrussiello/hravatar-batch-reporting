/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tm2batch.util;

import com.tm2batch.service.LogService;
import java.net.URI;
import java.net.URL;

/**
 *
 * @author miker
 */
public class HttpUtils {
    
    public static URL getURLFromString( String urlStr )
    {
        if( urlStr==null || urlStr.isBlank() )
            return null;
        
        if( !urlStr.toLowerCase().startsWith("http" ) && !urlStr.toLowerCase().startsWith("file" ))
            LogService.logIt( "HttpUtils.getURLFromString() UrlStr appears to be invalid. trying anyway. " + urlStr );

        try
        {
            return new URI( urlStr ).toURL();
        }
        catch( Exception e )
        {
            LogService.logIt( e, "HttpUtils.getURLFromString() " + urlStr );
            return null;
        }
    }
    
}
