package com.tm2batch.voicevibes;


public enum VoiceVibesStatusType
{
    // Starts here
    NOT_SET(0,"Not set"),
    
    // intermediate steps
    NOT_POSTED(9,"Required but not yet posted"),
    POSTED(10,"Audio posted and analysis requested"),
    ANALYSIS_COMPLETE(11,"Analysis Completed and stored."),
    
    // stops heer
    NOT_SENT_TOO_SHORT(94,"Not Sent Because Too Short"),
    POST_ERROR(95,"Posting Error Received"),
    ERROR(96,"Processing Error Received"),
    DELETE_ERROR(97,"Deletion Error Received"),
    DELETED(98,"Deleted"),
    NOT_REQUIRED(99,"Not required"),
    POST_ERROR_PERMANENT(100,"Permanent Error");

    private final int voiceVibesStatusTypeId;

    private String key;

    private VoiceVibesStatusType( int p , String key )
    {
        this.voiceVibesStatusTypeId = p;

        this.key = key;
    }


    public int getVoiceVibesStatusTypeId()
    {
        return this.voiceVibesStatusTypeId;
    }



    public static VoiceVibesStatusType getType( int typeId )
    {
        return getValue( typeId );
    }

    public boolean isAnalysisComplete()
    {
        return equals(ANALYSIS_COMPLETE);
    }
    
    public boolean hasValidResult()
    {
        return equals(ANALYSIS_COMPLETE) || equals(DELETED) || equals(DELETE_ERROR);
    }
    
    public boolean isNotRequiredOrCompleteOrPermanentError()
    {
        return equals(ANALYSIS_COMPLETE) || equals(DELETED) || equals(DELETE_ERROR) ||equals(NOT_REQUIRED)  || equals(ERROR)  || equals(POST_ERROR_PERMANENT) || equals(NOT_SENT_TOO_SHORT);
        
    }
    
    public boolean isAnalysisCompleteOrError()
    {
        return equals(ANALYSIS_COMPLETE) || equals(ERROR)  || equals(POST_ERROR)  || equals(POST_ERROR_PERMANENT) || equals(NOT_SENT_TOO_SHORT);
    }
    
    public String getKey()
    {
        return key;
    }

    public boolean isPostError()
    {
        return equals(POST_ERROR );
    }
    
    
    public boolean isAnyError()
    {
        return equals(ERROR) || equals(POST_ERROR) || equals(POST_ERROR_PERMANENT ) || equals(NOT_SENT_TOO_SHORT);
    }
    
    public boolean isPermanentPostError()
    {
        return equals(POST_ERROR_PERMANENT ) || equals(NOT_SENT_TOO_SHORT);
    }

    
    public boolean isPostedOrHigher()
    {
        return equals( POSTED )|| equals( DELETED ) || equals( ANALYSIS_COMPLETE );
    }

    public boolean isPending()
    {
        return equals( POSTED );
    }

    
 
    
    public static VoiceVibesStatusType getValue( int id )
    {
        VoiceVibesStatusType[] vals = VoiceVibesStatusType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getVoiceVibesStatusTypeId() == id )
                return vals[i];
        }

        return NOT_SET;
    }

}
