package com.tm2batch.battery;

import com.tm2batch.util.MessageFactory;
import java.util.Locale;


public enum BatteryScoreType
{
    NONE( 0, "battscore.none", true ),
    AVERAGE( 1, "battscore.average", true ),
    WEIGHTED_AVERAGE( 2, "battscore.weightedaverage", true ),
    REPORT_ONLY( 3, "battscore.reportonly", true );

    private final int batteryScoreTypeId;

    private String key;

    private final boolean forUsers;

    private BatteryScoreType( int p , String key, boolean forUsers )
    {
        this.batteryScoreTypeId = p;

        this.key = key;

        this.forUsers = forUsers;
    }

    public int getBatteryScoreTypeId()
    {
        return this.batteryScoreTypeId;
    }


    public boolean getHasScore()
    {
        return !equals( NONE ) && !equals(REPORT_ONLY);
    }
    
    public boolean getShowAsResult()
    {
        return !equals( NONE );
    }


    public boolean getIsCustomWeights()
    {
        return equals( WEIGHTED_AVERAGE );
    }



    public String getName( Locale locale )
    {
        return MessageFactory.getStringMessage( locale, key , null );
    }




    public static BatteryScoreType getType( int typeId )
    {
        return getValue( typeId );
    }

    public String getKey()
    {
        return key;
    }

    public static BatteryScoreType getValue( int id )
    {
        BatteryScoreType[] vals = BatteryScoreType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getBatteryScoreTypeId() == id )
                return vals[i];
        }

        return NONE;
    }

}
