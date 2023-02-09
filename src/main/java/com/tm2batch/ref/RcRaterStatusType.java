package com.tm2batch.ref;

import com.tm2batch.util.MessageFactory;
import java.util.Locale;



/**
 * 
 * @author miker_000
 */
public enum RcRaterStatusType
{
    CREATED(0,"Created", "rcrst.created"),
    SENT(10,"Sent", "rcrst.sent"),
    STARTED(20,"Started", "rcrst.started"),
    COMPLETED(100,"Completed", "rcrst.completed"),
    REJECTED_CANDIDATE_NOT_KNOWN(180,"Rejected - not known by rater", "rcrst.rejectednotknown"),
    REJECTED_UNFAMILIAR_WITH_PERFORMANCE(181,"Rejected - unfamiliar", "rcrst.rejectedcannotrate"),
    REJECTED_REFUSED(182,"Rejected - refused", "rcrst.rejectedrefused"),
    REJECTED_RELEASE(183,"Rejected - release not accepted", "rcrst.rejectedrelease"),
    EXPIRED(201,"Expired", "rcrst.expired"),
    DEACTIVATED(202,"Deactivated", "rcrst.deactivated");

    private final int rcRaterStatusTypeId;

    private final String name;
    private String key;


    private RcRaterStatusType( int s , String n, String k )
    {
        this.rcRaterStatusTypeId = s;

        this.name = n;
        this.key = k;
    }
    
    
    public boolean getCanDeactivate()
    {
        return rcRaterStatusTypeId<=COMPLETED.rcRaterStatusTypeId;        
    }
    
    public boolean getCanEdit()
    {
        return !getIsStartedOrHigher();
    }

    public boolean getCanSend()
    {
        return !getIsCompleteOrHigher();
    }

    public boolean getIsDeactivated()
    {
        return equals(DEACTIVATED);
    }

    public boolean getCanReactivate()
    {
        return equals(DEACTIVATED) || getIsRejected();
    }
    
    public boolean getIsExpired()
    {
        return equals( EXPIRED );
    }

    
    public boolean getIsRejected()
    {
        return equals(REJECTED_CANDIDATE_NOT_KNOWN) || equals(REJECTED_UNFAMILIAR_WITH_PERFORMANCE) || equals(REJECTED_REFUSED) || equals(REJECTED_RELEASE);
    }
        
    public boolean getIsNotSent()
    {
        return equals(CREATED);
    }

    public boolean getIsComplete()
    {
        return equals(COMPLETED);
    }
    
    public boolean getIsStarted()
    {
        return equals(STARTED);
    }
    
    public boolean getIsCompleteOrHigher()
    {
        return rcRaterStatusTypeId>=COMPLETED.getRcRaterStatusTypeId();
    }

    public boolean getIsStartedOrHigher()
    {
        return rcRaterStatusTypeId>=STARTED.getRcRaterStatusTypeId();
    }
    
    
    public boolean getIsInProgress()
    {
        return equals(STARTED)|| equals(SENT);
    }
    
    public boolean getCanHaveRatings()
    {
        return rcRaterStatusTypeId>=STARTED.getRcRaterStatusTypeId() && rcRaterStatusTypeId<=REJECTED_REFUSED.getRcRaterStatusTypeId();
    }
        

    public static RcRaterStatusType getValue( int id )
    {
        RcRaterStatusType[] vals = RcRaterStatusType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getRcRaterStatusTypeId() == id )
                return vals[i];
        }

        return CREATED;
    }


    public int getRcRaterStatusTypeId()
    {
        return rcRaterStatusTypeId;
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
