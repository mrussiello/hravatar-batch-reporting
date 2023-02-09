package com.tm2batch.user;


import com.tm2batch.entity.event.TestKey;
import com.tm2batch.event.NameRqdType;
import com.tm2batch.util.MessageFactory;
import java.util.Locale;



public enum UserAnonymityType
{
    NAME_EMAIL(0,"Default","uayt.Default" ),                
    LOGON_PWD(1,"Logon Pwd","uayt.LogonPwd" ),                
    USERID(3,"User ID","uayt.UserId" ),
    ANONYMOUS(98,"Anonymous","uayt.Anon" ),
    PSEUDO(99,"Anonymous","uayt.Pseudo" );      


    private final int userAnonymityTypeId;

    private final String name;
    private final String key;
    

    public boolean getNeedsFirstNameFld()
    {
        return equals(NAME_EMAIL) || equals(LOGON_PWD);
    }
    
    public boolean getNeedsLastNameFld()
    {
        return equals(NAME_EMAIL);
    }
    
    public boolean getNeedsEmailFld()
    {
        return equals(NAME_EMAIL) || equals(LOGON_PWD) || getHasUserId();
    }
    
    public boolean getNeedsMobileFld()
    {
        return equals(NAME_EMAIL);
    }
    
    
    
    public boolean getHasName()
    {
        return equals( NAME_EMAIL );
    }
    
    public boolean getHasUserId()
    {
        return equals( USERID );
    }

    public boolean getHasUsername()
    {
        return equals( LOGON_PWD );
    }

    public boolean getOkForOrgAutoTest()
    {
        return !equals( LOGON_PWD );
    }
    
    public void adjustNameRqdType( TestKey tk )
    {
        if( equals( LOGON_PWD ) )
            tk.setNameRqd( NameRqdType.REQUIRED_USE_LOGONFORM.getNameRqdTypeId() );
        
        else if( getHasUserId() )
            tk.setNameRqd( NameRqdType.REQUIRED_USE_USERIDFORM.getNameRqdTypeId() );

        else if( getAnonymous() || getPseudo() )
            tk.setNameRqd( NameRqdType.NOT_RQD.getNameRqdTypeId() );

    }

    private UserAnonymityType( int s , String n, String k )
    {
        this.userAnonymityTypeId = s;
        this.name = n;
        this.key=k;
    }
    
    
    public boolean getAnonymous()
    {
        return equals( ANONYMOUS );
    }
    
    public boolean getPseudo()
    {
        return equals( PSEUDO );
    }
    
    
    public static UserAnonymityType getValue( int id )
    {
        UserAnonymityType[] vals = UserAnonymityType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getUserAnonymityTypeId() == id )
                return vals[i];
        }

        return NAME_EMAIL;
    }

    

    public int getUserAnonymityTypeId()
    {
        return userAnonymityTypeId;
    }

    public String getName()
    {
        return getName(null);
    }
    
    public String getName(Locale l)
    {
        return MessageFactory.getStringMessage(l==null ? Locale.US : l, key, null );
    }
    
    


}
