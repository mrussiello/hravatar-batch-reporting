package com.tm2batch.ref;

import com.tm2batch.entity.ref.RcCheck;
import com.tm2batch.entity.ref.RcRater;
import com.tm2batch.util.MessageFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import jakarta.faces.model.SelectItem;



/**
 * @author miker_000
 */
public enum RcRaterSourceType
{
    CANDIDATE(0,"Candidate or Employee", "rcrtst.candidate" ),
    ACCT_USER(1,"Account User", "rcrtst.accountuser" );  

    private final int rcRaterSourceTypeId;

    private final String name;
    private String key;


    private RcRaterSourceType( int s , String n, String k )
    {
        this.rcRaterSourceTypeId = s;

        this.name = n;
        this.key = k;
    }
    
    public static RcRaterSourceType getForRcRater( RcCheck rc, RcRater rcRater )
    {
        if( rcRater.getSourceUserId()==rc.getUserId() )
            return CANDIDATE;
        return ACCT_USER;
    }
    
    public boolean getIsCandidateOrEmployee()
    {
        return equals( CANDIDATE );
    }
    
    public boolean getIsAccountUser()
    {
        return equals( ACCT_USER );
    }
    

    public static RcRaterSourceType getValue( int id )
    {
        RcRaterSourceType[] vals = RcRaterSourceType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getRcRaterSourceTypeId() == id )
                return vals[i];
        }

        return CANDIDATE;
    }


    public int getRcRaterSourceTypeId()
    {
        return rcRaterSourceTypeId;
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
