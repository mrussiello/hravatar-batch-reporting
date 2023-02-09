package com.tm2batch.lvi;

import com.tm2batch.util.MessageFactory;
import java.util.Locale;



public enum LiveVideoCriteriaScoreType
{
    INTEGER_1_10(0,"lvcsrt.default1.10" ),
    TEXT(100,"lvcsrt.text" );

    private final int liveVideoCriteriaScoreTypeId;

    private final String key;


    private LiveVideoCriteriaScoreType( int s , String n )
    {
        this.liveVideoCriteriaScoreTypeId = s;

        this.key = n;
    }

    
    public boolean getIsNum10()
    {
        return equals( INTEGER_1_10 );
    }

    public boolean getIsText()
    {
        return equals( TEXT );
    }
    




    public static LiveVideoCriteriaScoreType getValue( int id )
    {
        LiveVideoCriteriaScoreType[] vals = LiveVideoCriteriaScoreType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getLiveVideoCriteriaScoreTypeId() == id )
                return vals[i];
        }

        return INTEGER_1_10;
    }


    public int getLiveVideoCriteriaScoreTypeId()
    {
        return liveVideoCriteriaScoreTypeId;
    }

    public String getName( Locale l )
    {
        if( l==null )
            l = Locale.US;
        
        return MessageFactory.getStringMessage(l, key);
    }

}
