package com.tm2batch.user;



public enum AccountAccessLevelType
{
    ORG(0,"aat.org") ,
    SUBORG(1,"aat.suborg");

    private final int accountAccessLevelTypeId;

    private String key;


    private AccountAccessLevelType( int level , String key )
    {
        this.accountAccessLevelTypeId = level;

        this.key = key;
    }


    public boolean getIsSuborgOnly()
    {
        return equals( SUBORG );
    }

    public int getAccountAccessLevelTypeId()
    {
        return this.accountAccessLevelTypeId;
    }


    public String getName()
    {
        return key;
    }


    public static AccountAccessLevelType getValue( int id )
    {
        AccountAccessLevelType[] vals = AccountAccessLevelType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getAccountAccessLevelTypeId() == id )
                return vals[i];
        }

        return ORG;
    }



    public String getKey()
    {
        return key;
    }

}
