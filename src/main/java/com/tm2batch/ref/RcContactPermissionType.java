package com.tm2batch.ref;

import com.tm2batch.util.MessageFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import jakarta.faces.model.SelectItem;



/**
 * @author miker_000
 */
public enum RcContactPermissionType
{
    UNKNOWN(0,"None", "rcpt.notanserwed" ),
    YES(1,"Yes", "rcpt.yes" ),  
    NO(2,"None", "rcpt.no" );  
  

    private final int rcContactPermissionTypeId;

    private final String name;
    private String key;


    private RcContactPermissionType( int s , String n, String k )
    {
        this.rcContactPermissionTypeId = s;
        this.name = n;
        this.key = k;
    }
    
     
    public static RcContactPermissionType getValue( int id )
    {
        RcContactPermissionType[] vals = RcContactPermissionType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getRcContactPermissionTypeId() == id )
                return vals[i];
        }

        return UNKNOWN;
    }


    public int getRcContactPermissionTypeId()
    {
        return rcContactPermissionTypeId;
    }

    public String getName()
    {
        return name;
    }

    public String getName( Locale l )
    {
        if( l==null )
            l = Locale.US;
        
        return MessageFactory.getStringMessage(l, key );
    }
    
}
