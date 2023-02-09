package com.tm2batch.user;

import com.tm2batch.util.MessageFactory;
import java.util.Locale;


public enum ReleaseCodeType
{
    NONE(0,"rct.None" ),
    CAPTURED(1,"rct.Captured" ),
    DECLINED(2, "rct.Declined" );

    private int releaseCodeTypeId;

    private String key;

    private ReleaseCodeType( int typeId , String key )
    {
        this.releaseCodeTypeId = typeId;

        this.key = key;
    }

    public static ReleaseCodeType getValue( int id )
    {
        ReleaseCodeType[] vals = ReleaseCodeType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getReleaseCodeTypeId() == id )
                return vals[i];
        }

        return NONE;
    }

    public String getName()
    {
        return getName( Locale.US );
    }



    public String getName( Locale locale )
    {
        return MessageFactory.getStringMessage( locale, key , null );
    }


    public int getReleaseCodeTypeId()
    {
        return releaseCodeTypeId;
    }

    public String getKey()
    {
        return key;
    }

}
