package com.tm2batch.event;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


public enum TestEventScoreType
{
    OVERALL(0,"Overall"),
    COMPETENCY(1,"Competency"),
    TASK(2,"Task"),
    REPORT(3,"Report"),
    KNOWLEDGE(4,"Knowledge"),
    SKILLS(5,"Skills"),
    ABILITIES(6,"Abilities"),
    LEVEL_SCORES(7,"Level Scores"),
    COMPETENCYGROUP(8,"Competency Group"),
    ALT_OVERALL( 9 ,"Alt Overall Score" );

    private final int testEventScoreTypeId;

    private String key;


    private TestEventScoreType( int p , String key )
    {
        this.testEventScoreTypeId = p;

        this.key = key;
    }


    public boolean getIsReport()
    {
        return equals( REPORT );
    }


    public boolean getIsOverall()
    {
        return equals( OVERALL );
    }

    public boolean getIsCompetency()
    {
        return equals( COMPETENCY );
    }

    public boolean getIsTask()
    {
        return equals( TASK );
    }

    
    public int getTestEventScoreTypeId()
    {
        return this.testEventScoreTypeId;
    }



    public static Map<String,Integer> getMap( Locale locale )
    {
        Map<String,Integer> outMap = new TreeMap();

        TestEventScoreType[] vals = TestEventScoreType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            outMap.put( vals[i].getKey() , new Integer( vals[i].getTestEventScoreTypeId() ) );
        }

        return outMap;
    }

    public String getName()
    {
        return key;
    }




    public static TestEventScoreType getType( int typeId )
    {
        return getValue( typeId );
    }



    public String getKey()
    {
        return key;
    }



    public static TestEventScoreType getValue( int id )
    {
        TestEventScoreType[] vals = TestEventScoreType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getTestEventScoreTypeId() == id )
                return vals[i];
        }

        return OVERALL;
    }

}
