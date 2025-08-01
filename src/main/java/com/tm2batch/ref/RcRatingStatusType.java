package com.tm2batch.ref;

import com.tm2batch.util.MessageFactory;
import java.util.Locale;



/**
 * 
 * @author miker_000
 */
public enum RcRatingStatusType
{
    INCOMPLETE(0,"Incomplete", "rcrrst.incomplete"),
    COMPLETE(100,"Complete", "rcrrst.complete"),
    SKIPPED(101,"Skipped", "rcrrst.skipped");

    private final int rcRatingStatusTypeId;

    private final String name;
    private String key;


    private RcRatingStatusType( int s , String n, String k )
    {
        this.rcRatingStatusTypeId = s;

        this.name = n;
        this.key = k;
    }
    
    
    public boolean getIsComplete()
    {
        return equals(COMPLETE);        
    }
    
    public boolean getIsSkipped()
    {
        return equals(SKIPPED);        
    }
    
    public boolean getIsIncomplete()
    {
        return equals(INCOMPLETE);        
    }
    

    public static RcRatingStatusType getValue( int id )
    {
        RcRatingStatusType[] vals = RcRatingStatusType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getRcRatingStatusTypeId() == id )
                return vals[i];
        }

        return INCOMPLETE;
    }


    public int getRcRatingStatusTypeId()
    {
        return rcRatingStatusTypeId;
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
