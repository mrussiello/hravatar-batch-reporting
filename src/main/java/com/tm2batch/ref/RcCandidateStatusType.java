package com.tm2batch.ref;

import com.tm2batch.util.MessageFactory;
import java.util.Locale;



/**
 * 
 * @author miker_000
 */
public enum RcCandidateStatusType
{
    CREATED(0,"Created", "rcanst.created"),
    SENT(10,"Sent", "rcanst.sent"),
    STARTED(20,"Started", "rcanst.started"),
    COMPLETED(100,"Completed", "rcanst.completed"),
    REJECTED_REFUSED(182,"Rejected - refused", "rcanst.rejectedrefused"),
    REJECTED_RELEASE(183,"Rejected - release not accepted", "rcanst.rejectedrelease");

    private final int rcCandidateStatusTypeId;

    private final String name;
    private String key;


    private RcCandidateStatusType( int s , String n, String k )
    {
        this.rcCandidateStatusTypeId = s;

        this.name = n;
        this.key = k;
    }
    
    public boolean getIsNotSent()
    {
        return equals(CREATED);
    }

    public boolean getIsComplete()
    {
        return equals(COMPLETED);
    }

    public boolean getIsStartedOrHigher()
    {
        return rcCandidateStatusTypeId>=STARTED.getRcCandidateStatusTypeId();
    }
    
    public boolean getIsCompletedOrHigher()
    {
        return rcCandidateStatusTypeId>=COMPLETED.getRcCandidateStatusTypeId();
    }
    
    
    
    public boolean getIsInProgress()
    {
        return equals(STARTED)|| equals(SENT);
    }
    
    public boolean getCanHaveRatings()
    {
        return rcCandidateStatusTypeId>=STARTED.getRcCandidateStatusTypeId() && rcCandidateStatusTypeId<=REJECTED_REFUSED.getRcCandidateStatusTypeId();
    }
    
    

    public static RcCandidateStatusType getValue( int id )
    {
        RcCandidateStatusType[] vals = RcCandidateStatusType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getRcCandidateStatusTypeId() == id )
                return vals[i];
        }

        return CREATED;
    }


    public int getRcCandidateStatusTypeId()
    {
        return rcCandidateStatusTypeId;
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
