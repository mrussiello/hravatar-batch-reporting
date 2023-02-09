package com.tm2batch.purchase;

import com.tm2batch.util.MessageFactory;
import java.util.Locale;


public enum ConsumerProductType
{

    ASSESSMENT_JOBSPECIFIC(0, "cpt.AssessJobSpec"),
    ASSESSMENTBATTERY(1, "cpt.Battery"),
    ASSESSMENT_SKILLS(2, "cpt.AssessSkills"),
    ASSESSMENT_PERSONALITY(3, "cpt.AssessPersonality"),
    ASSESSMENT_COGNITIVE(4, "cpt.AssessCognitive"),
    ASSESSMENT_OTHER(5, "cpt.AssessOther"),
    ASSESSMENT_DEVELOPMENT(6, "cpt.AssessDevel"),
    ASSESSMENT_COMPETENCY(7, "cpt.AssessCompetency"),
    ASSESSMENT_POPMSOFFICE(8, "cpt.AssessPopularMSOfficeSims"),
    ASSESSMENT_VOICE(9, "cpt.AssessVoiceIVR"),
    ASSESSMENT_WHOLEPERSON(10, "cpt.AssessWholePerson"),
    ASSESSMENT_VIDEOINTERVIEW(11, "cpt.AssessVidInterview"),
    ASSESSMENT_VIDEOINTERVIEW_LIVE(12, "cpt.AssessVidInterviewLive"),
    PREVIEW(100, "cpt.JobPreview"),
    TRAINING(200, "cpt.Train" ),
    MISC(1000, "cpt.Misc" ),
    INFO_ARTICLE(1001, "cpt.Article"),
    INFO_BOOK(1002, "cpt.Book"),
    INFO_WHITEPAPER(1003, "cpt.Whitepaper"),
    INFO_BLOG(1004, "cpt.BlogEntry"),
    INFO_NEWS(1005, "cpt.NewsEntry"),
    INFO_WEBPAGE(1006, "cpt.WebPage");

    private final int consumerProductTypeId;

    private String key;

    private ConsumerProductType( int p,
                         String key )
    {
        this.consumerProductTypeId = p;

        this.key = key;
    }

    public int getConsumerProductTypeId()
    {
        return this.consumerProductTypeId;
    }

    public boolean getIsOther()
    {
        return equals( ASSESSMENT_OTHER );
    }
    
    public boolean getIsSkillsKnowledge()
    {
        return equals( ASSESSMENT_SKILLS );
    }

    public boolean getIsWholePerson()
    {
        return equals( ASSESSMENT_WHOLEPERSON );
    }

    public boolean getIsCompetency()
    {
        return equals( ASSESSMENT_COMPETENCY );        
    }
        
    public boolean getIsJobSpecificOrCompetency()
    {
        return getIsCompetency() || getIsJobSpecific();
    }
        
    public boolean getIsJobSpecificOrWholePerson()
    {
        return equals( ASSESSMENT_JOBSPECIFIC ) || equals( ASSESSMENT_WHOLEPERSON );
    }

    public boolean getIsJobSpecific()
    {
        return equals( ASSESSMENT_JOBSPECIFIC );
    }

    public boolean getIsVideoInterview()
    {
        return equals( ASSESSMENT_VIDEOINTERVIEW );
    }
    
    

    public String getName( Locale locale )
    {
        return MessageFactory.getStringMessage( locale, key, null );
    }

    public String getKey()
    {
        return key;
    }

    public String getName()
    {
        return getName( Locale.US );
    }

    public static ConsumerProductType getValue( int id )
    {
        ConsumerProductType[] vals = ConsumerProductType.values();

        for( int i = 0; i < vals.length; i++ )
        {
            if( vals[i].getConsumerProductTypeId() == id )
                return vals[i];
        }

        return null;
    }

}
