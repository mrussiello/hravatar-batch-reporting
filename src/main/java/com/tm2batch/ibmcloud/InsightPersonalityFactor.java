package com.tm2batch.ibmcloud;


    
public enum InsightPersonalityFactor
{
    UNKNOWN(0, "Unknown", "ipf.unknown" ),
    AGREEABLENESS(1, "Agreeableness", "ipf.agree" ),
    CONSCIENTIOUSNESS(2, "Conscientiousness", "ipf.consc" ),
    EMOTIONALRANGE(3, "Emotional Range", "ipf.emot" ),
    EXTRAVERSION(4, "Extraversion" , "ipf.extravert"),
    OPENNESS(5, "Openness", "ipf.open" ),
    
    NEED_CHALLENGE(20, "Challenge", "ipf.challenge" ),
    NEED_CLOSENESS(21, "Closeness", "ipf.closeness" ),
    NEED_CURIOSITY(22, "Curiosity", "ipf.curiosity" ),
    NEED_EXCITEMENT(23, "Excitement", "ipf.excitement" ),
    NEED_HARMONY(24, "Harmony", "ipf.harmony" ),
    NEED_IDEAL(25, "Ideal", "ipf.ideal" ),
    NEED_LIBERTY(26, "Liberty", "ipf.liberty" ),
    NEED_LOVE(27, "Love", "ipf.love" ),
    NEED_PRACTICALITY(28, "Practicality", "ipf.practicality" ),
    NEED_SELFEXPRESSIO(29, "Self-expression", "ipf.selfexpression" ),
    NEED_STABILITY(30, "Stability", "ipf.stability" ),
    NEED_STRUCTURE(31, "Structure", "ipf.structure" ),
    
    VALUE_CONSERVATION(40, "Conservation", "ipf.conservation" ),
    VALUE_OPENNESSCHANGE(41, "Openness to change", "ipf.openness" ),
    VALUE_HEDONISM(42, "Hedonism", "ipf.hedonism" ),
    VALUE_SELFENHANCEMENT(43, "Self-enhancement", "ipf.selfenhance" ),
    VALUE_SELFTRANSCENDANCE(44, "Self-transcendence", "ipf.selftranscend" );

    private final int insightPersonalityFactorId;

    private final String name;
    
    private final String key;
    

    private InsightPersonalityFactor( int p , String n, String k )
    {
        this.insightPersonalityFactorId = p;
        this.name = n;
        this.key = k;
    }

    public int getInsightPersonalityFactorId() {
        return insightPersonalityFactorId;
    }

    public boolean getIsNeed()
    {
        return insightPersonalityFactorId>=20 && insightPersonalityFactorId<=31;
    }

    public boolean getIsValue()
    {
        return insightPersonalityFactorId>=40 && insightPersonalityFactorId<=44;
    }

    public InsightTraitType getInsightTraitType()
    {
        if( getIsNeed() )
            return InsightTraitType.NEED_TOP;
        else if( getIsValue() )
            return InsightTraitType.VALUES_TOP;
        else if( equals( UNKNOWN ) )
            return InsightTraitType.UNKNOWN;
        else 
            return InsightTraitType.PERSONALITY_TOP;
    }
    
        
    
    public String getKey()
    {
        return key;
    }
       

    public static InsightPersonalityFactor getValue( int id )
    {
        InsightPersonalityFactor[] vals = InsightPersonalityFactor.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getInsightPersonalityFactorId() == id )
                return vals[i];
        }

        return UNKNOWN;
    }

}
