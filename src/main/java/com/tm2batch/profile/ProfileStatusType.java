package com.tm2batch.profile;


public enum ProfileStatusType
{
    INACTIVE(0, "Inactive"),
    ACTIVE(1, "Active");

    private int profieStatusTypeId;

    private String key;

    private ProfileStatusType( int typeId, String k )
    {
        profieStatusTypeId = typeId;
        key = k;
    }


    public int getProfileStatusTypeId()
    {
        return profieStatusTypeId;
    }

    public static ProfileStatusType getValue( int id )
    {
        for( ProfileStatusType val : ProfileStatusType.values() )
        {
            if( id == val.getProfileStatusTypeId())
                return val;
        }

        return INACTIVE;
    }

    public boolean isActive()
    {
        return equals( ACTIVE );
    }


    public String getKey()
    {
        return key;
    }
}
