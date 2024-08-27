package com.tm2batch.user;
import com.tm2batch.util.MessageFactory;

import java.util.Locale;


public enum EthnicCategoryType
{
    HISPANIC(1,"ecat.hispanic"),
    NOTHISPANIC(2, "ecat.nothispanic" );

    private int ethnicCategoryTypeId;

    private String key;

    private EthnicCategoryType( int typeId , String key )
    {
        this.ethnicCategoryTypeId = typeId;

        this.key = key;
    }


    public static EthnicCategoryType getValue( int id )
    {
        EthnicCategoryType[] vals = EthnicCategoryType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getEthnicCategoryTypeId() == id )
                return vals[i];
        }
        
        return null;
    }
    
    

    public String getName( Locale locale )
    {
        if( locale==null )
            locale= Locale.US;
        
        return MessageFactory.getStringMessage( locale, key , null );
    }

    public int getEthnicCategoryTypeId()
    {
        return ethnicCategoryTypeId;
    }

    public String getKey()
    {
        return key;
    }
}
