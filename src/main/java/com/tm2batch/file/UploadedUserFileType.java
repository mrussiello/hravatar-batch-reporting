package com.tm2batch.file;


public enum UploadedUserFileType
{
    RESPONSE(0,"User Response"),
    REMOTE_PROCTORING(100,"Remote Proctoring"),
    REMOTE_PROCTORING_IMAGES(101,"Remote Proctoring Images"),
    REMOTE_PROCTORING_ID(102,"Remote Proctoring ID Capture");



    private final int uploadedUserFileTypeId;

    private String key;


    private UploadedUserFileType( int p , String key )
    {
        this.uploadedUserFileTypeId = p;

        this.key = key;
    }

    

    public boolean getIsResponse()
    {
        return equals( RESPONSE );
    }

    public boolean getIsAnyPremiumRemoteProctoring()
    {
        return equals( REMOTE_PROCTORING ) ||  equals( REMOTE_PROCTORING_IMAGES ) || equals( REMOTE_PROCTORING_ID );
    }

    public boolean getIsRemoteProctoring()
    {
        return equals( REMOTE_PROCTORING );
    }

    public boolean getIsRemoteProctoringImage()
    {
        return equals( REMOTE_PROCTORING_IMAGES );
    }

    public boolean getIsRemoteProctoringId()
    {
        return equals( REMOTE_PROCTORING_ID );
    }



    public int getUploadedUserFileTypeId()
    {
        return this.uploadedUserFileTypeId;
    }




    public String getName()
    {
        return key;
    }



    public static UploadedUserFileType getValue( int id )
    {
        UploadedUserFileType[] vals = UploadedUserFileType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getUploadedUserFileTypeId() == id )
                return vals[i];
        }

        return RESPONSE;
    }

}
