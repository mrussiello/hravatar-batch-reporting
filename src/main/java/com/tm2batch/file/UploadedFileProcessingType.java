package com.tm2batch.file;


public enum UploadedFileProcessingType
{
    NONE(0,"Nothing" ),
    VIEWING_ONLY(10,"Viewing" ),
    ROTATE90(11,"Rotate image 90 degrees Clockwise"),
    ROTATE180(12,"Rotate image 180 degrees Clockwise"),
    ROTATE270(13,"Rotate image 270 degrees Clockwise"),
    LISTENING_ONLY(19,"Audio-Only" ),
    VIEWING_S2T(20,"Viewing and Speech2Text"),
    LISTENINGONLY_S2T(21,"Audio-Only and Speech2Text"),
    VIEWING_S2T_VIBES(30,"Viewing, S2t, and Vibes" ),
    LISTENINGONLY_S2T_VIBES(31,"Audio-Only, S2t, and Vibes" ),
    VIEWING_S2T_VIBES_VIDEO(40,"Viewing,S2t,Vibes, and Video");


    private final int uploadedFileProcessingTypeId;

    private String key;

    private UploadedFileProcessingType( int p , String key )
    {
        this.uploadedFileProcessingTypeId = p;
        this.key = key;
    }

    
    public boolean requiresAvItemResponse()
    {
        return uploadedFileProcessingTypeId >= LISTENING_ONLY.getUploadedFileProcessingTypeId();
    }
    
    public boolean isRotation()
    {
        return equals(ROTATE90) || equals(ROTATE180) || equals(ROTATE270);
    }
    
    public boolean isViewingOnly()
    {
        return equals(VIEWING_ONLY);
    }
    
    public boolean requiresViewingOrListening()
    {
        return uploadedFileProcessingTypeId>=10;
    }
    
    public boolean requiresSpeech2Text()
    {
        return uploadedFileProcessingTypeId>=20;
    }

    public boolean isAudioOnly()
    {
        return equals( LISTENING_ONLY ) || equals(LISTENINGONLY_S2T) || equals( LISTENINGONLY_S2T_VIBES );
    }

    public boolean requiresVibes()
    {
        return uploadedFileProcessingTypeId>=30;
    }

    public boolean requiresVideo()
    {
        return uploadedFileProcessingTypeId>=40;
    }

    public float getRotationRadians()
    {
        if( !isRotation() )
            return 0;
        
        float a = 0;
        
        if( equals(ROTATE90))
            a = 90;
        
        else  if( equals(ROTATE180))
            a = 180;
        
        else if( equals(ROTATE270))
            a = 270;
        
        return (float) (a*Math.PI/180f);
    }



    public int getUploadedFileProcessingTypeId()
    {
        return this.uploadedFileProcessingTypeId;
    }



    public String getName()
    {
        return key;
    }


    public String getKey()
    {
        return key;
    }


    public static UploadedFileProcessingType getValue( int id )
    {
        UploadedFileProcessingType[] vals = UploadedFileProcessingType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getUploadedFileProcessingTypeId() == id )
                return vals[i];
        }

        return NONE;
    }

}
