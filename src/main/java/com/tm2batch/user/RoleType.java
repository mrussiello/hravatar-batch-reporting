package com.tm2batch.user;

import java.util.Locale;



public enum RoleType
{
    NO_LOGON(0,"ult.noaccount") , // Account - Test Taker, cs request, whitepaper download, no account privileges, generic org, no login, no password
    PERSONAL_USER(10,"ult.personaluser") , // Personal logons allowed only (uses Default Account)
    DISABLED_USER(12,"ult.disabled") , // DISABLED by Corporate
    PORTAL_USER(15,"ult.portaluser") , // Portal and Personal logons allowed only
    ACCOUNT_LEVEL1(20,"ult.acctlevel1") , // Account Viewer - Read Only - results only, can be org or suborg level (org or suborg level access - if suborg is set, then use is restricted to a specific suborg)
    ACCOUNT_LEVEL1A(21,"ult.acctlevel1a") , // Account Viewer - View Incomplete Test Keys Only, can be org or suborg level (org or suborg level access - if suborg is set, then use is restricted to a specific suborg)
    ACCOUNT_LEVEL1B(22,"ult.acctlevel1b") , // Account Viewer - Create Test Keys and View Incomplete Test Keys Only, can be org or suborg level (org or suborg level access - if suborg is set, then use is restricted to a specific suborg)
    ACCOUNT_LEVEL2B(24,"ult.acctlevel2b") , // Account - Results, create pins, email test takers, etc. (org or suborg level access), NO EXCEL REport
    ACCOUNT_LEVEL2(25,"ult.acctlevel2") , // Account User (Basic User) - Level1 plus create pins, email test takers, etc. (org or suborg level access - if suborg is set, then use is restricted to a specific suborg)
    ACCOUNT_LEVEL3(30,"ult.acctlevel3") , // Account Admin - Level2 plus Create/modify sub-orgs, Create/modify account users (full org access)
    CSR(90,"ult.hracsr") , // HR Avatar CSR
    ADMIN(100,"ult.admin");

    private final int roleTypeId;

    private String key;


    private RoleType( int level , String key )
    {
        this.roleTypeId = level;

        this.key = key;
    }

    public boolean getIsAdmin()
    {
        return equals( ADMIN );
    }

    public boolean getIsAccountUser()
    {
        return roleTypeId>=ACCOUNT_LEVEL1.getRoleTypeId();
    }



    public boolean getIsPersonalUser()
    {
        return equals( PERSONAL_USER );
    }


    public int getRoleTypeId()
    {
        return this.roleTypeId;
    }


    public String getName( Locale locale )
    {
        return key;
    }


    /**
     * Returns an enum value for the integer userLevelTypeId provided.
     *
     * @param levelIn
     * @return
     */
    public static RoleType getRoleTypeId( int r )
    {
        return getValue( r );
    }


    public static RoleType getValue( int id )
    {
        RoleType[] vals = RoleType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getRoleTypeId() == id )
                return vals[i];
        }

        return ACCOUNT_LEVEL1;
    }



    public String getKey()
    {
        return key;
    }

}
