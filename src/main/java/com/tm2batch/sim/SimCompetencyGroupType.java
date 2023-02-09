package com.tm2batch.sim;



public enum SimCompetencyGroupType
{
    NONE(0,"None"),    // When an ONET Soc is selected
    ABILITY(1,"Ability"),
    PERSONALITY(2,"Personality"),
    BIODATA(3,"Biodata"),
    SKILLS(4,"Skills"),
    EQ( 5, "Emotional Intelligence"),
    AI( 6, "AI-Derived"),
    CUSTOM( 100, "Custom" );



    private final int simCompetencyGroupTypeId;

    private final String name;


    private SimCompetencyGroupType( int s , String n )
    {
        this.simCompetencyGroupTypeId = s;

        this.name = n;
    }


    public static SimCompetencyGroupType getValue( int id )
    {
        SimCompetencyGroupType[] vals = SimCompetencyGroupType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getSimCompetencyGroupTypeId() == id )
                return vals[i];
        }

        return NONE;
    }


    public int getSimCompetencyGroupTypeId()
    {
        return simCompetencyGroupTypeId;
    }


    public String getName()
    {
        return name;
    }

}
