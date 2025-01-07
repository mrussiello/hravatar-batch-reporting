package com.tm2batch.sim;



public enum SimCompetencyClass
{
    NONCOGNITIVE(0,"Personality/ Non-Cog"),
    ABILITY(1,"Ability"),
    CORESKILL(2,"Core Skill"),
    KNOWLEDGE(3,"Knowledge"),
    EQ(4,"Emotional Intelligence"),
    CUSTOM(21,"Custom 1"),
    CUSTOM2(22,"Custom 2"),
    CUSTOM3(23,"Custom 3"),
    CUSTOM4(24,"Custom 4"),
    CUSTOM5(25,"Custom 5"),
    SCOREDTASK(100,"Scored Task"),
    SCOREDINTEREST(120,"Scored Interest"),
    SCOREDEXPERIENCE(130,"Scored Experience"),
    SCOREDBIODATA(140,"Scored Biodata"),
    SCOREDTYPING(141,"Scored Typing"),
    SCOREDDATAENTRY(146,"Scored Data Entry"),
    SCOREDESSAY(142,"Scored Essay"),
    SCOREDAUDIO(143,"Scored Audio" ),
    SCOREDAVUPLOAD(144,"Scored Audio/Video Upload" ),
    SCOREDIMAGEUPLOAD(145,"Scored Image Upload" ),
    SCOREDCHAT(147,"Scored Chat" ),
    ABILITY_COMBO(150,"Ability Combination"),
    SKILL_COMBO(151,"Skill Combination"),
    NONCOG_COMBO(152,"Non-Cognitive Combination" ),
    VOICE_PERFORMANCE_INDEX(153,"Voice Performance Index"),
    INTERESTS_COMBO(154,"Interests Combination" ),
    CUSTOM_COMBO(155,"Custom Combination"),
    AGGREGATEABILITY(200,"AggregateAbility"),
    AGGREGATESKILL(201,"AggregateSkill"),
    AGGREGATEKNOWLEDGE(202,"AggregateKnowledge"),
    UNSCORED(300,"Unscored");

    private final int simCompetencyClassId;

    private final String name;

    private SimCompetencyClass( int s , String n )
    {
        this.simCompetencyClassId = s;

        this.name = n;
    }


    public boolean getSupportsUploadedAvMedia()
    {
        return equals( SCOREDAUDIO ) || equals( SCOREDAVUPLOAD );
    }
    
    public boolean getSupportsPercentiles()
    {
        return !equals( NONCOGNITIVE ) && !equals( NONCOG_COMBO ) && !equals( SCOREDBIODATA ) && !equals( EQ );
    }

    
    public String getTopicCorrectStub()
    {
        if( equals( SCOREDCHAT ) )
            return "NoCorrect";
        
        return "";
    }
    
    
    
    public boolean isKSA()
    {
        return  equals( ABILITY ) || equals( CORESKILL ) ||equals( KNOWLEDGE ) ||
                equals( AGGREGATESKILL ) || equals( AGGREGATEKNOWLEDGE ) || equals( AGGREGATEABILITY ) ||
                equals( SCOREDTYPING ) || equals( SCOREDDATAENTRY ) || equals( SCOREDESSAY )  || equals( SCOREDAUDIO ) || equals( SCOREDCHAT ) ||
                equals( ABILITY_COMBO ) || equals(SKILL_COMBO) || equals( SCOREDAVUPLOAD ) ;
    }

    public boolean isKS()
    {
        return  equals( CORESKILL ) ||equals( KNOWLEDGE ) ||
                equals( AGGREGATESKILL ) || equals( AGGREGATEKNOWLEDGE ) || 
                equals( SCOREDTYPING ) || equals( SCOREDDATAENTRY ) || equals( SCOREDESSAY )|| equals( SCOREDAUDIO ) || equals( SCOREDCHAT ) || 
                equals(SKILL_COMBO) || equals( SCOREDAVUPLOAD );
    }

    public boolean isSkill()
    {
        return  equals( CORESKILL ) ||
                equals( AGGREGATESKILL );
    }
    

    public boolean isScoredEssay()
    {
        return  equals( SCOREDESSAY );
    }
        
    public boolean isScoredAudio()
    {
        return  equals( SCOREDAUDIO );
    }
    
    public boolean isScoredAvUpload()
    {
        return equals( SCOREDAVUPLOAD );
    }
    
    public boolean isScoredImageUpload()
    {
        return equals( SCOREDIMAGEUPLOAD );
    }
    
    public boolean isAnyCustom()
    {
        return equals( CUSTOM ) ||equals( CUSTOM2 ) || equals( CUSTOM3 ) || equals( CUSTOM4 ) || equals( CUSTOM5 ) || equals( CUSTOM_COMBO );
    }
            
    public boolean isAbility()
    {
        return  equals( ABILITY ) || equals( AGGREGATEABILITY ) || equals( ABILITY_COMBO );
    }


    public boolean isAIMS()
    {
        return  equals( NONCOGNITIVE ) || equals( NONCOG_COMBO );
    }

    public boolean isEQ()
    {
        return  equals( EQ ) ;
    }

    public boolean isBiodata()
    {
        return  equals( SCOREDBIODATA );
    }
    
    public boolean isAIDerived()
    {
        return equals( VOICE_PERFORMANCE_INDEX );
    }



    public boolean getUsesKnowledgeInterpretation()
    {
        return equals( CORESKILL ) ||equals( KNOWLEDGE ) ||  getIsAggregate();
    }

    public boolean getIsPureCompetency()
    {
        return simCompetencyClassId<100 || simCompetencyClassId==141 || simCompetencyClassId==142 || simCompetencyClassId==153 || getIsCombo();
    }


    public boolean getIsTask()
    {
        return simCompetencyClassId>=100 && simCompetencyClassId<200;
    }

    public boolean getIsAggregateOrTask()
    {
        return getIsTask() || getIsAggregate();
    }

    public boolean getIsAggregate()
    {
        return simCompetencyClassId>=200 && simCompetencyClassId < 300;
    }



    public boolean getIsCombo()
    {
        return equals( ABILITY_COMBO ) || equals(SKILL_COMBO) || equals(NONCOG_COMBO);        
    }


    public static SimCompetencyClass getValue( int id )
    {
        SimCompetencyClass[] vals = SimCompetencyClass.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getSimCompetencyClassId() == id )
                return vals[i];
        }

        return NONCOGNITIVE;
    }


    public int getSimCompetencyClassId()
    {
        return simCompetencyClassId;
    }

    public String getName()
    {
        return name;
    }

}
