package com.tm2batch.event;

public enum ScoreFormatType
{
    NUMERIC_1_TO_5(0,"1-5"),
    NUMERIC_0_TO_100(1,"0-100"),
    NUMERIC_0_TO_3(2,"0-3"),
    NUMERIC_1_TO_10(3,"1-10"),
    OTHER_SCORED(99,"Other, Scored"),
    UNSCORED(100,"Unscored");


    private final int scoreFormatTypeId;

    private String key;


    private ScoreFormatType( int p , String key )
    {
        this.scoreFormatTypeId = p;

        this.key = key;
    }

    public boolean getHasScore()
    {
        return !equals( ScoreFormatType.UNSCORED );
    }

    
    
    public float getMax()
    {
        if( equals( NUMERIC_1_TO_5 ) )
            return 5;
        if( equals( NUMERIC_0_TO_3 ) )
            return 3;
        if( equals(NUMERIC_1_TO_10 ) )
            return 10;
        if( equals( NUMERIC_0_TO_100 ) )
            return 100;
        return 0;
    }
    
    public float getMin()
    {
        if( equals( NUMERIC_1_TO_5 ) || equals( NUMERIC_1_TO_10 ) )
            return 1;
        
        return 0;
    }
    
    
    public boolean getIsScored()
    {
        return !getIsUnscored();
    }

    public boolean getIsUnscored()
    {
        return equals( UNSCORED );
    }


    public int getScoreFormatTypeId()
    {
        return this.scoreFormatTypeId;
    }


    public String getName()
    {
        return key;
    }




    public static ScoreFormatType getType( int typeId )
    {
        return getValue( typeId );
    }



    public String getKey()
    {
        return key;
    }

    public int getScorePrecisionDigits()
    {
        if( equals( NUMERIC_1_TO_5 ) )
            return 1;
        if( equals( NUMERIC_0_TO_3 ) )
            return 2;
        if( equals(NUMERIC_1_TO_10 ) )
            return 1;
        if( equals( NUMERIC_0_TO_100 ) )
            return 0;
        
        return 0;        
    }

    

    public boolean getHasValidNumericScore( float score )
    {
        if( equals( NUMERIC_1_TO_5 ) && score >= 1 && score <= 5 )
            return true;

        if( equals( NUMERIC_0_TO_3 ) && score >= 0 && score <= 3 )
            return true;

        if( equals(NUMERIC_1_TO_10 ) && score >= 1 && score <= 10 )
            return true;
        
        if( equals( NUMERIC_0_TO_100 ) && score >= 0 && score <= 100 )
            return true;

        if( equals( OTHER_SCORED ) )
            return true;

        return false;
    }

    

    public static ScoreFormatType getValue( int id )
    {
        ScoreFormatType[] vals = ScoreFormatType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getScoreFormatTypeId() == id )
                return vals[i];
        }

        return NUMERIC_1_TO_5;
    }

}
