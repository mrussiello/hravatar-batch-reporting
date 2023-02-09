package com.tm2batch.ref;

import com.tm2batch.util.MessageFactory;
import java.util.Locale;



/**
 * 
 * @author miker_000
 */
public enum RcCheckStatusType
{
    CREATED(0,"Created", "rccst.created"),
    // CANDIDATE_INPUT(10,"Candidate Input", "rccst.candidateinput"),
    SENT(10,"Sent", "rccst.sent"),
    STARTED(20,"Started", "rccst.started"),
    //PARTIALLY_COMPLETED(100,"Partially Completed", "rccst.partiallycompleted"),
    COMPLETED(101,"Completed", "rccst.completed"),
    //PARTIALLY_SCORED(110,"Partially Scored", "rccst.partiallyscored"),
    //SCORED(111,"Scored", "rccst.scored"),
    EXPIRED(200,"Expired", "rccst.expired"),
    DEACTIVATED(301,"Deactivated", "rccst.deactivated"),
    ARCHIVED(302,"Archived", "rccst.archived" );

    private final int rcCheckStatusTypeId;

    private final String name;
    private String key;


    private RcCheckStatusType( int s , String n, String k )
    {
        this.rcCheckStatusTypeId = s;

        this.name = n;
        this.key = k;
    }
    
    public boolean getCanArchive()
    {
        return equals(COMPLETED) || equals(EXPIRED) || equals(DEACTIVATED);
    }
    
    public boolean getStartedOrHigher()
    {
        return rcCheckStatusTypeId>=STARTED.getRcCheckStatusTypeId();
    }
    
    public boolean getSentOrHigher()
    {
        return rcCheckStatusTypeId>=SENT.getRcCheckStatusTypeId();
    }
    
    public boolean getComplete()
    {
        return equals(COMPLETED);
    }

    

    
    public boolean getCompleteOrHigher()
    {
        return rcCheckStatusTypeId>=COMPLETED.getRcCheckStatusTypeId();
    }
    
    public boolean getIsRaterInputOrHigher()
    {
        return rcCheckStatusTypeId>=STARTED.getRcCheckStatusTypeId();        
    }
    
    //public boolean getIsScoreAvailable()
    //{
    //    return rcCheckStatusTypeId>=PARTIALLY_SCORED.getRcCheckStatusTypeId();
    //}
    public boolean getIsCompleteOrHigher()
    {
        return rcCheckStatusTypeId>=COMPLETED.getRcCheckStatusTypeId();                
    }
    

    public boolean getIsDeactivatedOrHigher()
    {
        return rcCheckStatusTypeId>=DEACTIVATED.getRcCheckStatusTypeId();                
    }

    public boolean getIsExpiredOrHigher()
    {
        return rcCheckStatusTypeId>=EXPIRED.getRcCheckStatusTypeId();                
    }
    
    
    public boolean getIsCandidateInputOrLower()
    {
        return rcCheckStatusTypeId<=SENT.getRcCheckStatusTypeId();        
    }
    
    public boolean getIsCandidateInput()
    {
        return equals( SENT );
    }
    
    public boolean getIsRaterInput()
    {
        return equals( STARTED );
    }
    
    public boolean getCanEdit()
    {
        return rcCheckStatusTypeId<STARTED.getRcCheckStatusTypeId();
    }
    
    public boolean getCanExpire()
    {
        return rcCheckStatusTypeId<COMPLETED.getRcCheckStatusTypeId();
    }
    
    //public boolean getCanAutoComplete()
    //{
    //    return equals( PARTIALLY_COMPLETED );
    //}

    public boolean getExpired()
    {
        return equals( EXPIRED );
    }
    
    public boolean getCanExtend()
    {
        return equals( EXPIRED ) || rcCheckStatusTypeId<=COMPLETED.getRcCheckStatusTypeId();
    }
    
    public boolean getCanReactivate()
    {
        return equals( DEACTIVATED );
    }
    
    
        

    public static RcCheckStatusType getValue( int id )
    {
        RcCheckStatusType[] vals = RcCheckStatusType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getRcCheckStatusTypeId() == id )
                return vals[i];
        }

        return CREATED;
    }


    public int getRcCheckStatusTypeId()
    {
        return rcCheckStatusTypeId;
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
