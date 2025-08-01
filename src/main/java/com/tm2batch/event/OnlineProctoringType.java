package com.tm2batch.event;



public enum OnlineProctoringType
{
    NONE(0,"opt.OnlineProctorNone"),
    OPTIONAL_NOID(2,"opt.OnlineProctorOptionalNoId"),
    OPTIONAL_ID(3,"opt.OnlineProctorOptionalId"),
    OPTIONAL_NOID_RANDOM(4,"opt.OnlineProctorOptionalNoIdRandom"),
    OPTIONAL_ID_RANDOM(5,"opt.OnlineProctorOptionalIdRandom"),
    REQUIRED_NOID(12,"opt.OnlineProctorRequiredNoId"),
    REQUIRED_ID(13, "opt.OnlineProctorRequiredId"),
    REQUIRED_NOID_RANDOM(14,"opt.OnlineProctorRequiredNoIdRandom"),
    REQUIRED_ID_RANDOM(15, "opt.OnlineProctorRequiredIdRandom"),
    PREMIUM(100, "Premium. Record Video and Suspicious Activity"),
    PREMIUM_AUDIO(101, "Premium. Record Audio and Suspicious Activity"),
    PREMIUM_SUSPICIOUS_IMAGES(102, "Record Suspicious Activity And Images"),
    PREMIUM_VIDNOREC_SUSPICIOUS_IMAGES(103, "Premium. Live Video, Record Images & Suspicious Actions Only"),
    PREMIUM_SUSPICIOUS(110, "Premium. Record Suspicious Activity Only");

    private int onlineProctoringTypeId;

    private String key;

    private OnlineProctoringType( int typeId , String key )
    {
        this.onlineProctoringTypeId = typeId;

        this.key = key;
    }


    public boolean getRequiresAudioFile()
    {
        // disabled - until we have something to do with a separate audio file.
        return equals( PREMIUM_AUDIO );        
    }
    
    public boolean getIsAnyPremium()
    {
        return equals( PREMIUM ) || equals( PREMIUM_AUDIO ) || equals( PREMIUM_SUSPICIOUS );
    }
    
    
    public boolean getIsAnyPremiumWithSuspAct()
    {
        return equals( PREMIUM ) || equals( PREMIUM_SUSPICIOUS_IMAGES ) || equals( PREMIUM_SUSPICIOUS ) || equals( PREMIUM_VIDNOREC_SUSPICIOUS_IMAGES );
    }    
    
    public boolean getIsAnyBasic()
    {
        return onlineProctoringTypeId>0 && onlineProctoringTypeId<100;
    }
    
    
    public boolean getRecordsVideo()
    {
        return equals(PREMIUM);
    }
    
    public boolean getRecordsAudioOnly()
    {
        return equals(PREMIUM_AUDIO);
    }
    
    public boolean getSuspiciousActivityOnly()
    {
        return equals(PREMIUM_SUSPICIOUS);
    }
    
    
    
    public static OnlineProctoringType getValue( int id )
    {
        OnlineProctoringType[] vals = OnlineProctoringType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getOnlineProctoringTypeId() == id )
                return vals[i];
        }

        return NONE;
    }

    public int getOnlineProctoringTypeId()
    {
        return onlineProctoringTypeId;
    }

    public String getKey()
    {
        return key;
    }
}
