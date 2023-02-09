package com.tm2batch.ref;

import com.tm2batch.util.MessageFactory;
import java.util.Locale;



/**
 * 
 * @author miker_000
 */
public enum RcSuspiciousActivityType
{
    UNKNOWN(0,"Unknown", "rcsat.unknown"),
    SAME_IP_UA(10,"Same IP Address and User Agent", "rcsat.sameplat"),
    SAME_IP(11,"Same IP Address", "rcsat.sameip"),
    RATERS_SAME_IP(12,"Different Raters, Same IP Address", "rcsat.raterssameip");

    private final int rcSuspiciousActivityTypeId;

    private final String name;
    private String key;


    private RcSuspiciousActivityType( int s , String n, String k )
    {
        this.rcSuspiciousActivityTypeId = s;
        this.name = n;
        this.key = k;
    }
    
    
    public boolean getIsRaterRater()
    {
        return equals( RATERS_SAME_IP );
    }

    public static RcSuspiciousActivityType getValue( int id )
    {
        RcSuspiciousActivityType[] vals = RcSuspiciousActivityType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getRcSuspiciousActivityTypeId() == id )
                return vals[i];
        }

        return UNKNOWN;
    }


    public int getRcSuspiciousActivityTypeId()
    {
        return rcSuspiciousActivityTypeId;
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
