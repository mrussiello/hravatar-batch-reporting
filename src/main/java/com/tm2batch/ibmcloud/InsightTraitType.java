package com.tm2batch.ibmcloud;

    
public enum InsightTraitType
{
    UNKNOWN(0, "Unknown" ),
    PERSONALITY_TOP(1, "Personality" ),
    PERSONALITY_CHILD(2, "Pers Trait"),
    NEED_TOP(11,"Need"),
    VALUES_TOP(21,"Value" );

    private final int insightTraitTypeId;

    private final String key;
    

    private InsightTraitType( int p , String key )
    {
        this.insightTraitTypeId = p;
        this.key = key;
    }

    public int getInsightTraitTypeId() {
        return insightTraitTypeId;
    }

    public boolean getIsNeed()
    {
        return equals( NEED_TOP );
    }
    public boolean getIsValue()
    {
        return equals( VALUES_TOP );
    }

    public boolean getIsPersonality()
    {
        return equals( PERSONALITY_TOP ) || equals( PERSONALITY_CHILD );
    }
    
    public boolean getIsChild()
    {
        return equals( PERSONALITY_CHILD );
    }

    public InsightTraitType getChildType()
    {
        if( equals( PERSONALITY_TOP ) )
            return PERSONALITY_CHILD;
        
        return UNKNOWN;
    }

    
    
    public String getKey()
    {
        return key;
    }
    

    public static InsightTraitType getValue( int id )
    {
        InsightTraitType[] vals = InsightTraitType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getInsightTraitTypeId() == id )
                return vals[i];
        }

        return UNKNOWN;
    }

}
