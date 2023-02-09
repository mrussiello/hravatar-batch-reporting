package com.tm2batch.lvi;

import com.tm2batch.util.MessageFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import jakarta.faces.model.SelectItem;



public enum LvCallStatusType
{
    CREATED(0,"Created", "lvcst.created" ),
    DEACTIVATED(40,"Deactivated", "lvcst.deactivated" ),
    STARTED(50,"Started", "lvcst.started" ),
    COMPLETED(100,"Completed", "lvcst.completed" ), 
    COMPLETED_POSTPROC_STARTED(101,"Completed. Post Proc Started", "lvcst.completedpostprocstarted" ),
    COMPLETED_POSTPROC_COMPLETED(102,"Completed Post Proc Completed", "lvcst.completedfilescopied" ),
    // SCORING_STARTED(109,"Scoring Started", "lvcst.scorestarted" ),
    SCORED(110,"Scored", "lvcst.scored" ),
    ARCHIVED(200,"Archived", "lvcst.archived" );

    private final int lvCallStatusTypeId;
    private final String name;
    private String key;


    private LvCallStatusType( int s , String n, String k )
    {
        this.lvCallStatusTypeId = s;

        this.name = n;
        this.key = k;
    }


    public boolean getCanDeactivate()
    {
        return lvCallStatusTypeId<100;
    }
    
    public boolean getCompletedOrLower()
    {
        return lvCallStatusTypeId<=102;
    }
    
    public boolean getCompletedOrHigher()
    {
        return lvCallStatusTypeId>=100;
    }
    
    public boolean getScoredOrHigher()
    {
        return lvCallStatusTypeId>=110;
    }
        
    public static LvCallStatusType getValue( int id )
    {
        LvCallStatusType[] vals = LvCallStatusType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getLvCallStatusTypeId() == id )
                return vals[i];
        }

        return CREATED;
    }

    
    

    public int getLvCallStatusTypeId()
    {
        return lvCallStatusTypeId;
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
