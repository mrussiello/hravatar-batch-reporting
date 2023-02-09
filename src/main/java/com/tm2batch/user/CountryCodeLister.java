/*
 * Created on Dec 30, 2006
 *
 */
package com.tm2batch.user;

import com.tm2batch.util.MessageFactory;
import java.util.Locale;



public class CountryCodeLister
{

    public static String getCountryForCode( Locale locale , String countryCode )
    {
        if( countryCode==null )
            return null;

        if( locale==null )
            locale = Locale.US;

        return MessageFactory.getStringMessage( locale, "cntry." + countryCode.toUpperCase() , null );
    }

}
