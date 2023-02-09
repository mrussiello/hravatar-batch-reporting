package com.tm2batch.event;


public enum TestEventResponseRatingType
{
    UPLOADEDUSERFILE(0,"Uploaded User File"),
    AVITEMRESPONSE(1,"AV Item Response"),
    SIMCOMPETENCY(2,"Sim Competency"),
    NONCOMPETENCY(3,"NON Competency");

    private final int testEventResponseRatingTypeId;

    private String key;


    private TestEventResponseRatingType( int p , String key )
    {
        this.testEventResponseRatingTypeId = p;

        this.key = key;
    }


    public int getTestEventResponseRatingTypeId()
    {
        return this.testEventResponseRatingTypeId;
    }

    public boolean getIsUploadedUserFile()
    {
        return equals( UPLOADEDUSERFILE );
    }

    public boolean getIsAvItemResponse()
    {
        return equals( AVITEMRESPONSE );
    }

    public boolean getIsSimCompetency()
    {
        return equals( SIMCOMPETENCY );
    }

    public boolean getIsNonCompetency()
    {
        return equals( NONCOMPETENCY );
    }


    public String getName()
    {
        return key;
    }




    public static TestEventResponseRatingType getType( int typeId )
    {
        return getValue( typeId );
    }



    public String getKey()
    {
        return key;
    }



    public static TestEventResponseRatingType getValue( int id )
    {
        TestEventResponseRatingType[] vals = TestEventResponseRatingType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getTestEventResponseRatingTypeId() == id )
                return vals[i];
        }

        return null;
    }

}
