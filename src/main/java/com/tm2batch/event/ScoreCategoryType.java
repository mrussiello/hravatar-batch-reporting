package com.tm2batch.event;

import com.tm2batch.sim.SimCompetencyClass;



public enum ScoreCategoryType
{
    UNRATED(0,"Unrated", "g.ScoreCatInterpUnknown", "#ffffff"),
    RED(1,"Red", "g.ScoreCatInterpRed", "#ff0000"),
    REDYELLOW(3,"Red-Yellow", "g.ScoreCatInterpRedYellow", "#f17523"),
    YELLOW(5,"Yellow", "g.ScoreCatInterpYellow", "#fcee21"),
    YELLOWGREEN(8,"Yellow-Green", "g.ScoreCatInterpYellowGreen", "#8cc63f" ),
    GREEN(11,"Green", "g.ScoreCatInterpGreen", "#69a220" );


    private final int scoreCategoryTypeId;

    private String key;

    private String interpretationKey;
    private String colorRgb;

    private ScoreCategoryType( int p , String key, String interpKey, String rgb )
    {
        this.scoreCategoryTypeId = p;

        this.key = key;

        this.interpretationKey = interpKey;
        this.colorRgb = rgb;
    }


    public String getInterpretationKey( int simCompetencyClassTypeId ) {

        if( SimCompetencyClass.getValue( simCompetencyClassTypeId ).getUsesKnowledgeInterpretation() )
            return getInterpretationKeyKnowledge();

        return getInterpretationKey();
    }


    public String getInterpretationKey() {
        return interpretationKey;
    }

    public String getInterpretationKeyKnowledge() {
        return interpretationKey + "Knldg";
    }


    public boolean getIsRed()
    {
        return equals( RED );
    }

    public boolean getIsRedYellow()
    {
        return equals( REDYELLOW );
    }

    public boolean getIsGreen()
    {
        return equals( GREEN );
    }

    public boolean getIsYellow()
    {
        return equals( YELLOW );
    }

    public boolean getIsYellowGreen()
    {
        return equals( YELLOWGREEN );
    }

    public boolean getHasCategory()
    {
        return getScoreCategoryTypeId()>0;
    }


    public int getScoreCategoryTypeId()
    {
        return this.scoreCategoryTypeId;
    }


    public String getName()
    {
        return key;
    }

    
    
    public String getColorRgb( String[] cols )
    {
        if( cols == null || cols.length==0 )
            return getColorRgb(); 
        
        if( equals( RED ) && cols.length>=1 && cols[0]!=null && !cols[0].isEmpty() ) 
            return "#" + cols[0];
        if( equals( REDYELLOW ) && cols.length>=2 && cols[1]!=null && !cols[1].isEmpty() ) 
            return "#" + cols[1];
        if( equals( YELLOW ) && cols.length>=3 && cols[2]!=null && !cols[2].isEmpty() ) 
            return "#" + cols[2];
        if( equals( YELLOWGREEN ) && cols.length>=4 && cols[3]!=null && !cols[3].isEmpty() ) 
            return "#" + cols[3];
        if( equals( GREEN ) && cols.length>=5 && cols[4]!=null && !cols[4].isEmpty() ) 
            return "#" + cols[4];
        
        return getColorRgb();
    }
    
    
    
    public String getColorRgb()
    {
        return colorRgb;
    }



    public static ScoreCategoryType getType( int typeId )
    {
        return getValue( typeId );
    }



    public String getKey()
    {
        return key;
    }


    public static ScoreCategoryType getValue( int id )
    {
        ScoreCategoryType[] vals = ScoreCategoryType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getScoreCategoryTypeId() == id )
                return vals[i];
        }

        return UNRATED;
    }

}
