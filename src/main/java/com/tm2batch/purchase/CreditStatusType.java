package com.tm2batch.purchase;

import com.tm2batch.util.MessageFactory;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


public enum CreditStatusType
{
    ACTIVE(1,"creditstatustype.active"),
    EXPIRED(2,"creditstatustype.expired"),
    EMPTY(3,"creditstatustype.empty"),
    DISABLED(4,"creditstatustype.disabled"),
    OVERAGE(5,"creditstatustype.overage");

    private final int creditStatusTypeId;

    private String key;

    private CreditStatusType( int p , String key )
    {
        this.creditStatusTypeId = p;

        this.key = key;
    }


    public int getCreditStatusTypeId()
    {
        return this.creditStatusTypeId;
    }


    public boolean getIsActive()
    {
        return equals( ACTIVE );
    }

    public boolean getIsOverage()
    {
        return equals( OVERAGE );
    }


    public static Map<String,Integer> getMap( Locale locale )
    {
        Map<String,Integer> outMap = new TreeMap<>();

        CreditStatusType[] vals = CreditStatusType.values();

        String name;

        for( int i=0 ; i<vals.length ; i++ )
        {
            name = MessageFactory.getStringMessage( locale, vals[i].getKey() , null );

            outMap.put( name , new Integer( vals[i].getCreditStatusTypeId() ) );
        }

        return outMap;
    }



    public String getName( Locale locale )
    {
        return MessageFactory.getStringMessage( locale, key , null );
    }




    public static CreditStatusType getType( int typeId )
    {
        return getValue( typeId );
    }

    public String getKey()
    {
        return key;
    }



    public static CreditStatusType getValue( int id )
    {
        CreditStatusType[] vals = CreditStatusType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getCreditStatusTypeId() == id )
                return vals[i];
        }

        return DISABLED;
    }

}
