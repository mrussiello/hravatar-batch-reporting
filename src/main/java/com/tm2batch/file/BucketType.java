package com.tm2batch.file;

import com.tm2batch.global.RuntimeConstants;



public enum BucketType
{
    CFMEDIA(1),
    USERUPLOAD(2),
    LVRECORDING(3),
    LVRECORDING_TEST(4),
    PROCTORRECORDING(5),
    PROCTORRECORDING_TEST(6);

    private final int bucketTypeId;


    private BucketType( int p )
    {
        this.bucketTypeId = p;
    }


    public int getBucketTypeId()
    {
        return this.bucketTypeId;
    }




    public static BucketType getValue( int id )
    {
        BucketType[] vals = BucketType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getBucketTypeId() == id )
                return vals[i];
        }

        return null;
    }

    public String getBucket()
    {
        if( equals( CFMEDIA ) )
            return RuntimeConstants.getStringValue( "awsBucket" );

        else if( equals( LVRECORDING )  || equals( LVRECORDING_TEST ) )
            return RuntimeConstants.getStringValue( "awsBucketLvRecording" );

        else if( equals( PROCTORRECORDING ) || equals( PROCTORRECORDING_TEST ) )
            return RuntimeConstants.getStringValue( "awsBucketProctorRecording" );

        return RuntimeConstants.getStringValue( "awsBucketFileUpload" );
    }

    public String getBaseKey()
    {
        if( equals( CFMEDIA ) )
            return RuntimeConstants.getStringValue( "awsBaseKey" );

        else if( equals( LVRECORDING ) )
            return RuntimeConstants.getStringValue( "awsBaseKeyLvRecording" );
        
        else if( equals( LVRECORDING_TEST ) )
            return RuntimeConstants.getStringValue( "awsBaseKeyLvRecordingTest" );
        
        else if( equals( PROCTORRECORDING ) )
            return RuntimeConstants.getStringValue( "awsBaseKeyProctorRecording" );

        else if( equals( PROCTORRECORDING_TEST ) )
            return RuntimeConstants.getStringValue( "awsBaseKeyProctorRecordingTest" );

        return RuntimeConstants.getStringValue( "awsBaseKeyFileUpload" );

    }
    
    
    public int getBucketRegionId()
    {
        if( equals( CFMEDIA ) )
            return RuntimeConstants.getIntValue( "awsBucketRegionId" );

        else if( equals( PROCTORRECORDING ) || equals( PROCTORRECORDING_TEST ) )
            return RuntimeConstants.getIntValue( "awsBucketRegionIdProctorRecording" );

        else if( equals( LVRECORDING )  || equals( LVRECORDING_TEST ) )
            return RuntimeConstants.getIntValue( "awsBucketRegionIdLvRecording" );
        
        return RuntimeConstants.getIntValue( "awsBucketRegionIdFileUpload" );
    }



}
