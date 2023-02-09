package com.tm2batch.lvi;


import com.tm2batch.util.MessageFactory;
import java.util.Locale;



public enum LiveVideoOverallRatingType
{
    NONE(0,"lvort.none" ),
    RATE_1(1,"lvort.1" ),
    RATE_2(1,"lvort.2" ),
    RATE_3(1,"lvort.3" ),
    RATE_4(1,"lvort.4" ),
    RATE_5(1,"lvort.5" ),
    RATE_6(6,"lvort.6" ),
    RATE_7(7,"lvort.7" ),
    RATE_8(8,"lvort.8" ),
    RATE_9(9,"lvort.9" ),
    RATE_10(10,"lvort.10" );

    private final int liveVideoOverallRatingTypeId;

    private final String key;


    private LiveVideoOverallRatingType( int s , String n )
    {
        this.liveVideoOverallRatingTypeId = s;

        this.key = n;
    }




    public static LiveVideoOverallRatingType getValue( int id )
    {
        LiveVideoOverallRatingType[] vals = LiveVideoOverallRatingType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getLiveVideoOverallRatingTypeId() == id )
                return vals[i];
        }

        return NONE;
    }


    public int getLiveVideoOverallRatingTypeId()
    {
        return liveVideoOverallRatingTypeId;
    }

    public String getName( Locale l )
    {
        if( l==null )
            l = Locale.US;
        
        return MessageFactory.getStringMessage(l, key);
    }

}
