package com.tm2batch.ref;

import com.tm2batch.util.MessageFactory;
import jakarta.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



/**
 * @author miker_000
 */
public enum RcReferralStatusType
{
    NEW(0,"New", "rcrfst.new" ),
    PURSUE(10,"Pursue", "rcrfst.pursue" ),
    SAVE(20,"Save", "rcrfst.save" ),
    ARCHIVED(30,"Archive", "rcrfst.archived" );  

    private final int rcReferralStatusTypeId;

    private final String name;
    private String key;


    private RcReferralStatusType( int s , String n, String k )
    {
        this.rcReferralStatusTypeId = s;

        this.name = n;
        this.key = k;
    }
    
    public boolean getIsPursue()
    {
        return equals( PURSUE );
    }
    
    public static List<SelectItem> getSelectItemList( Locale l )
    {
        if( l==null )
            l = Locale.US;
        
        List<SelectItem> out = new ArrayList<>();

        for( RcReferralStatusType v : RcReferralStatusType.values())
        {
            out.add( new SelectItem( v.getRcReferralStatusTypeId(), v.getName( l ) ) );
        }

        return out;
    }
   
    
    
    public static RcReferralStatusType getValue( int id )
    {
        RcReferralStatusType[] vals = RcReferralStatusType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getRcReferralStatusTypeId() == id )
                return vals[i];
        }

        return NEW;
    }

    public int getRcReferralStatusTypeId()
    {
        return rcReferralStatusTypeId;
    }

    public String getName()
    {
        return name;
    }
    
    public String getName( Locale l )
    {
        if( l==null )
            l=Locale.US;        
        return MessageFactory.getStringMessage(l, this.key );
    }

}
