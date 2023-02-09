/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.user;

import com.tm2batch.entity.user.User;
import com.tm2batch.entity.user.UserAction;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.service.LogService;
import java.util.Date;
import jakarta.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 *
 * @author Mike
 */
@Stateless
public class UserActionFacade {


    // private static final String PERSISTENCE_UNIT_NAME = "tm2";
    // private static EntityManagerFactory tm2Factory;
    @PersistenceContext( name = "persistence/tm2", unitName = "tm2" )
    EntityManager em;
    

    public static UserActionFacade getInstance()
    {
        try
        {
            return (UserActionFacade) InitialContext.doLookup( "java:module/UserActionFacade" );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "UserActionFacade.getInstance() " );

            return null;
        }
    }


    public UserAction saveMessageAction( long initiatorUserId, User user, String subject, int userActionTypeId, long longParam1, long longParam2, long longParam4, String identifier, String strParam1)
    {
        try
        {
            // LogService.logIt( "UserFacade.saveUserAction() " );
            if( userActionTypeId<=0 )
                throw new Exception( "UserActionTypeId invalid: " + userActionTypeId );

            if( user == null )
                throw new Exception( "User is null" );

            //if( user.getEmail()==null || user.getEmail().isEmpty() || !EmailUtils.validateEmailNoErrors( user.getEmail()))
            //    throw new Exception( "Cannot send invalid email: " + user.toString() );

            // LogService.logIt("UserFacade.saveUserAction() email=" + user.getEmail() + ", phone=" + user.getMobilePhone() );

            UserAction ua = new UserAction();

            ua.setLongParam1(longParam1);
            
            ua.setLongParam2(longParam2);
            
            ua.setLongParam4(longParam4);
            
            ua.setLongParam3(initiatorUserId);

            ua.setStrParam1(strParam1);

            ua.setIdentifier(identifier);

            ua.setCreateDate( new Date());

            ua.setUserId( user.getUserId() );
            ua.setOrgId( user.getOrgId() );

            if( user.getUserId() <= 0 )
            {
                ua.setUserId(RuntimeConstants.getLongValue( "defaultMarketingAccountAnonymousUserId" ));
                ua.setOrgId( RuntimeConstants.getIntValue( "defaultMarketingAccountOrgId" ) );
            }

            ua.setStrParam5(user.getFullname());
            ua.setStrParam4( user.getEmail());
            ua.setStrParam6( user.getMobilePhone() );

            ua.setStrParam3(subject);
            ua.setUserActionTypeId(userActionTypeId);

            saveUserAction( ua );

            return ua;

        }

        catch( Exception e )
        {
            LogService.logIt(e, "UserFacade.saveUserAction() NONFATAL " + ( user == null ? "null" : user.toString()) + ", subject=" + subject + ", userActionTypeId=" + userActionTypeId );

            return null;
            // throw new Exception( "UserActionFacade.saveUserAction() " + userAction.toString() + " " + e.toString() );
        }
    }

    
    
    
    public UserAction saveUserAction( UserAction ua ) throws Exception
    {
        try
        {
            if( ua.getUserId()<=0 )
                throw new Exception( "UserAction.userId is required" );

            if( ua.getCreateDate()==null )
                ua.setCreateDate( new Date() );

            Context envCtx = (Context) new InitialContext().lookup( "java:comp/env" );

            EntityManager em = (EntityManager) envCtx.lookup( "persistence/tm2" );

            if( ua.getUserActionId() > 0 )
                em.merge(ua );

            else
                em.persist(ua );

            // em.flush();
        }

        catch( Exception e )
        {
            LogService.logIt(e, "UserActionFacade.saveUserAction() NONFATAL " + ua.toString() );

            // throw new Exception( "UserActionFacade.saveUserAction() " + userAction.toString() + " " + e.toString() );
        }

        return ua;
    }




}
