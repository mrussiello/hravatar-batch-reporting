/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.util;


import com.tm2batch.service.LogService;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 *
 * @author miker_000
 */
public class UrlEncodingUtils {
    
    public static String encode( String inStr )
    {
        if( inStr==null || inStr.isEmpty() )
            return inStr;
        
        try
        {
            return URLEncoder.encode(inStr, "UTF8");
        }
        catch( IllegalArgumentException e )
        {
            LogService.logIt( "UrlEncodingUtils.encode() " + e.toString() + ", inStr=" + inStr );
            return inStr;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "UrlEncodingUtils.encode() inStr=" + inStr );
            return inStr;
        }
    }
    
    public static String decodeKeepPlus( String inStr )
    {
        return decodeKeepPlus(  inStr, "UTF8" );
    }
    

    public static String decodeKeepPlus( String inStr, String enc )
    {
        if( inStr==null || inStr.trim().isEmpty() )
            return inStr;
        
        try
        {
            String p = inStr.replaceAll( "\\+" , "%2B");
            
            return URLDecoder.decode(p, enc );
        }
        catch( IllegalArgumentException e )
        {
            // This can happen when using score as num.
            if( inStr!=null && inStr.contains("%" ) )
                return inStr;
            
            LogService.logIt( "UrlEncodingUtils.decode() " + e.toString() + ", inStr=" + inStr );
            return inStr;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "UrlEncodingUtils.decode() inStr=" + inStr );
            return inStr;
        }
    }
    

    
}
