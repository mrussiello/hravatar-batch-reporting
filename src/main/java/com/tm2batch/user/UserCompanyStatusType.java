package com.tm2batch.user;

import com.tm2batch.util.MessageFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import jakarta.faces.model.SelectItem;


public enum UserCompanyStatusType
{
    UNKNOWN(0,"usercompanytype.unknown"),
    APPLICANT(5,"usercompanytype.candidateapplicant"),
    POST_SCREENING(10,"usercompanytype.candidatepostscreening"),
    PREINTERVIEW(15,"usercompanytype.candidatepreinterview"),
    POSTINTERVIEW(20,"usercompanytype.candidatepostinterview"),
    OFFER(25,"usercompanytype.candidateoffer"),
    ACCEPTED(30,"usercompanytype.candidateaccept"),
    REJECTED(50,"usercompanytype.candidaterejected"),
    APP_TERMINATED(51,"usercompanytype.candiateterminated"),
    EMPLOYEE(100,"usercompanytype.employee"),
    TERMINATED(200,"usercompanytype.employeeterminated");

    private int userCompanyStatusTypeId;

    private String key;

    private UserCompanyStatusType( int typeId , String key )
    {
        this.userCompanyStatusTypeId = typeId;

        this.key = key;
    }

    public int getUserCompanyStatusTypeId()
    {
        return userCompanyStatusTypeId;
    }


    public static UserCompanyStatusType getForName( Locale loc, String name )
    {
        if( loc==null )
            loc = Locale.ENGLISH;
        
        if( name==null || name.isBlank() )
            return null;
        
        name=name.trim();
        
        String n;

        for( UserCompanyStatusType u : UserCompanyStatusType.values() )
        {
            n = MessageFactory.getStringMessage( loc, u.getKey() , null ).trim();
            if( n.equalsIgnoreCase(name) )
                return u;
        }
        
        return null;
    }

    public static List<SelectItem> getSelectItemList( Locale locale )
    {
        List<SelectItem>  out = new ArrayList<>();

        UserCompanyStatusType[] vals = UserCompanyStatusType.values();

        String name;

        for( int i=0 ; i<vals.length ; i++ )
        {
            name = MessageFactory.getStringMessage( locale, vals[i].getKey() , null );

            out.add( new SelectItem( vals[i].getUserCompanyStatusTypeId(), name ) );
        }

        return out;
    }


    public String getName( Locale locale )
    {
        return MessageFactory.getStringMessage( locale, getKey() , null );
    }

    public static UserCompanyStatusType getValue( int id )
    {
        UserCompanyStatusType[] vals = UserCompanyStatusType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getUserCompanyStatusTypeId() == id )
                return vals[i];
        }

        return UNKNOWN;
    }

    public String getKey()
    {
        return key;
    }
}
