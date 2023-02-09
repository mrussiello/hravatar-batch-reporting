/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.profile;

import com.tm2batch.service.LogService;

/**
 *
 * @author miker_000
 */
public class ProfileUtils {
  
    /**
     * Returns array of RGB codes.  
     * 
     * Input string format rRRGGBB,ryRRG,yRRGGBB,ygRRGGBB,gRRGGBB

    *  data[0] = Red color or null   RRGGBB
    *  data[1] = RedYellow or null
    *  data[2] = yellow or null
    *  data[3] = yellow-green or null
    *  data[4] = green or null
    * 
     * @param t
     * @return 
     */
    public static String[] parseBaseColorStr( String t )
    {
        String[] out = new String[5];

        try
        {
            if( t == null )
                t = "";

            else
                t = t.trim();

            if( t.isEmpty() )
                return out;

            String[] ts = t.split(",");

            for( String c : ts )
            {
                if( c==null || c.trim().isEmpty() )
                    continue;

                c = c.trim().toLowerCase();

                if( c.startsWith("ry") )
                    out[1] = c.substring(2, c.length() );

                else if( c.startsWith("r") )
                    out[0] = c.substring(1, c.length() );

                else if( c.startsWith("yg") )
                    out[3] = c.substring(2, c.length() );

                else if( c.startsWith("y") )
                    out[2] = c.substring(1, c.length() );

                else if( c.startsWith("g") )
                    out[4] = c.substring(1, c.length() );
            }
        }

        catch( Exception e )
        {
            LogService.logIt( e, "ProfileUtils.parseBaseColorStr() str=" + t );

            throw e;
        }

        return out;        
    }
    
    
}
