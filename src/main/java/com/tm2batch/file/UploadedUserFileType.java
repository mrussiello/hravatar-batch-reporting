package com.tm2batch.file;


public enum UploadedUserFileType
{
    RESPONSE(0,"User Response"),
    REMOTE_PROCTORING(100,"Remote Proctoring Audio/Video (with Image Thumbs)"),
    REMOTE_PROCTORING_IMAGES(101,"Remote Proctoring Image Thumbs (Only)"),
    REMOTE_PROCTORING_ID(102,"Remote Proctoring IDs (Only)"),
    REF_CHECK_IMAGES(201,"Reference Check Candidate Image Thumbs (Only)"),
    REF_CHECK_ID(202,"Reference Check Candidate IDs (Only)"),
    REF_CHECK_RATER(211,"Reference Check Rater Images (Only)"),
    REF_CHECK_RATER_ID(212,"Reference Check Rater IDs (Only)"),
    REF_CHECK_RATER_COMMENT(220,"Reference Check Rater Comment" ),
    CT5(230,"CT5 media" ),
    CT5_GENERAL(231,"CT5 General Uploaded File (for download inside item)" );



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
