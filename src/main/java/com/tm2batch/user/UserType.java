package com.tm2batch.user;


public enum UserType
{
    NAMED(0,"usertype.named"),
    ANONYMOUS(1,"usertype.anonymous"),
    USERID(3,"usertype.userid"),
    USERNAME(4,"usertype.usernamepwd"),
    PSEUDONYMIZED(10,"usertype.pseudonymized");

    private int userTypeId;

    private String key;

    private UserType( int typeId , String key )
    {
        this.userTypeId = typeId;

        this.key = key;
    }

    
    
    public boolean getNamedUserIdUsername()
    {
        return getNamed() || getUserId() || getUsername();
    }
    
    public boolean getNotNamed()
    {
        return !equals( NAMED );
    }
    
    public boolean getNamed()
    {
        return equals( NAMED );
    }

    public boolean getUserId()
    {
        return equals( USERID );
    }

    public boolean getUsername()
    {
        return equals( USERNAME );
    }

    public boolean getPseudo()
    {
        return equals( PSEUDONYMIZED );
    }

    public boolean getNamedAnonPseudo()
    {
        return equals( NAMED ) || equals( ANONYMOUS ) || equals( PSEUDONYMIZED );
    }
    
    public boolean getAnonymous()
    {
        return equals( ANONYMOUS );
    }
    
    
    public int getUserTypeId()
    {
        return userTypeId;
    }

    /*
    public static UserType getForUserAnonymityType( UserAnonymityType userAnonymityType )
    {
        if( userAnonymityType.getHasName() )
            return NAMED;
        if( userAnonymityType.getHasUserId() )
            return USERID;
        if( userAnonymityType.getHasUsername() )
            return USERNAME;
        if( userAnonymityType.getAnonymous())
            return ANONYMOUS;
        if( userAnonymityType.getPseudo())
            return PSEUDONYMIZED;
        return NAMED;
    }
    */
    
    public static UserType getValue( int id )
    {
        UserType[] vals = UserType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getUserTypeId() == id )
                return vals[i];
        }

        return NAMED;
    }

    public String getKey()
    {
        return key;
    }
}
