package com.tm2batch.user;

import com.tm2batch.util.MessageFactory;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


public enum GenderType
{
    UNKNOWN(0, "gent.Unknown"),
    MALE(1, "gent.Male"),
    FEMALE(2, "gent.Female"),
    NONBINARY(5, "gent.NonBinary"),
    // DO NOT USE 9 - used by API to indicate unknown.
    PREFER_NOT_SAY(10, "gent.PreferNotToSay");

    private int genderTypeId;

    private String key;

    private GenderType( int typeId, String k )
    {
        genderTypeId = typeId;
        key = k;
    }

    public int getGenderTypeId()
    {
        return genderTypeId;
    }

    public static GenderType getValue( int id )
    {
        if( id == 1 )
            return MALE;

        if( id == 2 )
            return FEMALE;
        
        if( id == 5 )
            return NONBINARY;
        
        if( id==10 )
            return PREFER_NOT_SAY;

        return UNKNOWN;
    }

    public String getName( Locale locale )
    {
        return MessageFactory.getStringMessage( locale, key , null );
    }



    public String getKey()
    {
        return key;
    }
}
