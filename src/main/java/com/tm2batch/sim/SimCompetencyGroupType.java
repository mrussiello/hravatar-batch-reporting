package com.tm2batch.sim;



public enum SimCompetencyGroupType
{
    NONE(0,"None", 100),    // When an ONET Soc is selected
    ABILITY(1,"Ability", 10 ),
    PERSONALITY(2,"Personality", 30 ),
    BIODATA(3,"Biodata", 50 ),
    SKILLS(4,"Skills", 20 ),
    EQ( 5, "Emotional Intelligence", 40 ),
    AI( 6, "AI-Derived", 100),
    INTERESTS( 7, "Interests", 110 ),
    CUSTOM( 101, "Custom 1", 1 ),
    CUSTOM2( 102, "Custom 2", 2 ),
    CUSTOM3( 103, "Custom 3", 3 ),
    CUSTOM4( 104, "Custom 4", 4 ),
    CUSTOM5( 105, "Custom 5", 5 );

    private final int simCompetencyGroupTypeId;
    private final String name;
    private final int reportDisplayOrder;
    

    private SimCompetencyGroupType( int s , String n, int rdo )
    {
        this.simCompetencyGroupTypeId = s;
        this.name = n;
        this.reportDisplayOrder=rdo;
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
