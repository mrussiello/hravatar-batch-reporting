/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.user;

import com.tm2batch.entity.user.LogonHistory;
import com.tm2batch.entity.user.Org;
import com.tm2batch.entity.user.Suborg;
import com.tm2batch.entity.user.User;
import com.tm2batch.entity.user.UserAction;
import com.tm2batch.entity.user.UserNote;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.global.STException;
import com.tm2batch.service.LogService;
import com.tm2batch.util.StringUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import javax.sql.DataSource;

/**
 *
 * @author Mike
 */
@Stateless
// @PersistenceContext( name = "persistence/tm2", unitName = "tm2" )
public class UserFacade
{
    // private static final String PERSISTENCE_UNIT_NAME = "tm2";
    // private static EntityManagerFactory tm2Factory;
    @PersistenceContext( name = "persistence/tm2", unitName = "tm2" )
    EntityManager em;




    public static UserFacade getInstance()
    {
        try
        {
            return (UserFacade) InitialContext.doLookup( "java:module/UserFacade" );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "getInstance() " );

            return null;
        }
    }

    public void clearSharedCache()
    {
        try
        {
            //if( tm2Factory == null )
            //    tm2Factory = PersistenceManager.getInstance().getEntityManagerFactory();

            //EntityManager em = tm2Factory.createEntityManager();

            em.getEntityManagerFactory().getCache().evictAll();
        }

        catch( Exception e )
        {
            LogService.logIt( e, "UserFacade.clearSharedCache() " );
        }
    }


    public List<Suborg> getSuborgList( long orgId, boolean activeOnly ) throws Exception
    {
        try
        {
            // if( tm2Factory == null )
                // tm2Factory = PersistenceManager.getInstance().getEntityManagerFactory();

            // EntityManager em = tm2Factory.createEntityManager();

            TypedQuery<Suborg> q = em.createNamedQuery( activeOnly ?  "Suborg.findActiveForOrgid" : "Suborg.findForOrgid", Suborg.class );

            q.setParameter( "orgId", orgId );

            return (List<Suborg>) q.getResultList();
        }

        catch( Exception e )
        {
            LogService.logIt( e, "UserFacade.getSuborgList() " );

            return new ArrayList<>();
        }
    }
    
    

    public Date getLastLogonDate( long userId, long logonHistoryId ) throws Exception
    {
        String sqlStr = "SELECT MAX(logondate) FROM logonhistory WHERE userid=" + userId + ( logonHistoryId>0 ? " AND logonhistoryid<>" + logonHistoryId : "" ) + " AND systemId=" + RuntimeConstants.getIntValue( "applicationSystemId" );

        DataSource pool = (DataSource) new InitialContext().lookup( "jdbc/tm2" );

        if( pool == null )
            throw new Exception( "Can not find Datasource" );

        try (Connection con = pool.getConnection();
             Statement stmt = con.createStatement() )
        {
            ResultSet rs = stmt.executeQuery( sqlStr );

            Date d = null;

            Timestamp ts;

            if( rs.next() )
            {
                ts = rs.getTimestamp(1);

                if( ts != null )
                    d = new Date( ts.getTime() );
                else
                    d = null;
            }

            rs.close();

            // LogService.logIt( "UserFacade.getLastLogonDate() " + d.toString() );
            return d;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "UserFacade.getLastLogonDate() " + sqlStr );

            throw new STException( e );
        }
    }
    
    
    
    public LogonHistory addLogonHistory( User user, int logonTypeId, String userAgent, String ipAddress) throws Exception
    {
        Context envCtx = (Context) new InitialContext().lookup( "java:comp/env" );

        EntityManager em = (EntityManager) envCtx.lookup( "persistence/tm2" );


        // EntityTransaction utx = em.getTransaction();

        try
        {
            LogonHistory lh = new LogonHistory();

            lh.setUserId( user.getUserId() );

            lh.setLogonDate( new Date() );

            lh.setLogonTypeId( logonTypeId );

            lh.setLogonHistoryId( 0 );

            lh.setOrgId( user.getOrgId() );

            lh.setSuborgId( user.getSuborgId() );

            lh.setSystemId( RuntimeConstants.getIntValue( "applicationSystemId" ) );
            
            lh.setUserAgent( userAgent );
            
            lh.setIpAddress( ipAddress );

            // utx.begin();

            em.persist(lh );

            // This causes any exceptions to be thrown here instead of in the EJB transaction.
            // Makes it easier to figure out what went wrong.

            em.flush();

            // utx.commit();

            return lh;
        }

        catch( Exception e )
        {

            // if( utx.isActive() )
                // utx.rollback();

            LogService.logIt(e, "addLogonHistory() " + ( user == null ? "User is null" : user.toString() ) );

            throw new Exception( "addLogonHistory() " + ( user == null ? "User is null" : user.toString() ) + " " + e.toString() );
        }

    }

    
    public void addUserLogout( long logonHistoryId, int logoffTypeId ) throws Exception
    {

        try
        {
            LogonHistory logonHistory = em.find( LogonHistory.class, new Long( logonHistoryId ) );

            if( logonHistory != null )
            {
                logonHistory.setLogoffDate( new Date() );

                logonHistory.setLogoffTypeId( logoffTypeId );

                // utx.begin();

                try
                {
	                em.merge( logonHistory );

	                em.flush();

	                // utx.commit();
                }

                catch( Exception e )
                {
                    // if( utx.isActive() )
	                // utx.rollback();

                    throw e;
                }
            }
        }

        catch( Exception e )
        {
            LogService.logIt( e, "addUserLogout( logoutHistoryId=" + logonHistoryId + "  ) " );
        }
    }

    
    

    /**
     * Returns null if none found.
     */
    public User getUserByUsername( String username ) throws Exception
    {
        try
        {
            //if( tm2Factory == null )
            //    tm2Factory = PersistenceManager.getInstance().getEntityManagerFactory();

           // EntityManager em = tm2Factory.createEntityManager();

            TypedQuery<User> q = em.createNamedQuery( "User.findByUsername", User.class );

            q.setParameter( "uname", username );

            return q.getSingleResult();
        }

        catch( NoResultException e )
        {
            return null;
        }
    }

    
    public List<UserNote> getUserNotes( long userId ) throws Exception
    {
        try
        {
            if( userId <= 0 )
                return new ArrayList<>();

            Query q = em.createNamedQuery( "UserNote.findForUser" );
            q.setParameter( "userId", userId );
            // q.setHint( "jakarta.persistence.cache.retrieveMode", "BYPASS" );
            List<UserNote> unl = q.getResultList();
            for( UserNote un : unl )
                un.setAuthorUser( this.getUser( un.getAuthorUserId() ));

            return unl;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "getUserNotes1( userId=" + userId  + " ) " );
            throw new STException( e );
        }
    }
    


    public User saveUser( User user ) throws Exception
    {
        Context envCtx = (Context) new InitialContext().lookup( "java:comp/env" );

        EntityManager em = (EntityManager) envCtx.lookup( "persistence/tm2" );

        try
        {
            if( user.getUserId() > 0 )
            {
                em.merge( user );
            }

            else
            {
                throw new Exception("This application cannot create new users." );
                //em.detach( user );
                //em.persist( user );
            }

            // em.flush();

            // utx.commit();
        }

        catch( Exception e )
        {
            LogService.logIt( e, "UserFacade.saveUser() " + user.toString() );

            throw new STException( e );
        }

        return user;
    }
    
    

    public boolean checkPassword( long userId, String password ) throws Exception
    {
        DataSource pool = (DataSource) new InitialContext().lookup( "jdbc/tm2" );

        if( pool == null )
            throw new Exception( "Can not find Datasource" );

        try (Connection con = pool.getConnection() )
        {
            if( password == null || password.length() < 1 )
                return false;

            // password is invalid
            if( !password.equals( StringUtils.sanitizeForSqlQuery( password ) ) )
                return false;

            if( userId <= 0 )
                return false;

            con.setTransactionIsolation( Connection.TRANSACTION_READ_UNCOMMITTED );

            password = StringUtils.sanitizeForSqlQuery( password );

            PreparedStatement ps = con.prepareStatement( "SELECT username FROM xuser WHERE xpass=MD5( ? ) AND userid=?" );

            ps.setString( 1, password );

            ps.setLong( 2, userId );

            ResultSet rs = ps.executeQuery();

            boolean recordFound = false;

            if( rs.next() )
                recordFound = true;

            rs.close();

            ps.close();

            return recordFound;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "UserFacade.checkPassword() userId=" + userId );

            throw new STException( e );
        }
    }

    
    public Suborg getSuborg( int suborgId ) throws Exception
    {
        try
        {
            if( suborgId <= 0 )
                return null;

            return em.find( Suborg.class,  suborgId );
        }

        catch( NoResultException e )
        {
            return null;
        }
    }
    

    
    public Org getOrg( int orgId ) throws Exception
    {
        try
        {
            if( orgId <= 0 )
                return null;

            return em.find( Org.class,  orgId );
        }

        catch( NoResultException e )
        {
            return null;
        }
    }
    
    

    /**
     * Returns null if either this username (or email) is not found or the password is invalid.
     *
     */
    public User getUserByLogonInfo( String username, String password ) throws Exception
    {
        try
        {

            // both fields are required.
            if( username == null || username.length() == 0 || password == null || password.length() == 0 )
                return null;

            // first look for username
            User user = getUserByUsername( username );

            // not found?
            if( user == null )
                return null;

            if( !checkPassword( user.getUserId(), password ) )
                return null;

            // found!
            return user;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "getUserByLogonInfo( " + username + ", password=" + password + " ) " );

            return null;
        }
    }

    
    /**
     * Returns null if none found.
     */
    public User getUserByEmailAndOrgId( String email, int orgId ) throws Exception
    {
        try
        {
            if( email == null || email.length() == 0 )
                return null;

            // if( tm2Factory == null )
                // tm2Factory = PersistenceManager.getInstance().getEntityManagerFactory();

            // EntityManager em = tm2Factory.createEntityManager();

            Query q = em.createNamedQuery( "User.findByEmailAndOrgId" );

            q.setParameter( "uemail", email );
            q.setParameter( "orgId", orgId );

            return (User) q.getSingleResult();
        }

        catch( NoResultException e )
        {
            return null;
        }
    }
    
    
    public User getUser( long userId ) throws Exception
    {
        try
        {
            //if( tm2Factory == null )
            //    tm2Factory = PersistenceManager.getInstance().getEntityManagerFactory();

            //EntityManager em = tm2Factory.createEntityManager();

            return em.find(User.class, userId );
        }

        catch( NoResultException e )
        {
            return null;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "getUser( " + userId + " ) " );

            throw new STException( e );
        }
    }
    
    public List<UserAction> getUserActionEmails( long longParam1, long longParam4, long userId, int userActionTypeId) throws Exception
    {
        try
        {
            if( longParam1 <= 0 && longParam4 <= 0 && userId <= 0 )
                throw new Exception( "longParam1 or longParam4 or userId are all invalid. Need at least one of them." );

            TypedQuery<UserAction> q = null;
            
            if( longParam1>0 && longParam4>0 && userId>0 )
                q = em.createNamedQuery("UserAction.findByLongParam1And4AndUserActionType" , UserAction.class );
            else if( longParam1>0 && userId>0 )
                q = em.createNamedQuery("UserAction.findByLongParam1UserIdAndUserActionType" , UserAction.class );
            else if( longParam4>0 && userId>0 )
                q = em.createNamedQuery("UserAction.findByLongParam4UserIdAndUserActionType" , UserAction.class );
            else if( longParam1>0 )
                q = em.createNamedQuery("UserAction.findByLongParam1AndUserActionType" , UserAction.class );
            else if( longParam4>0 )
                q = em.createNamedQuery("UserAction.findByLongParam4AndUserActionType" , UserAction.class );
            else
                q = em.createNamedQuery("UserAction.findByUserIdAndUserActionType" , UserAction.class );

            q.setParameter( "userActionTypeId", userActionTypeId );

            if( longParam1>0 && longParam4>0 && userId>0 )
            {
                q.setParameter( "longParam1", longParam1 );
                q.setParameter( "longParam4", longParam4 );
                q.setParameter( "userId", userId );                
            }
            
            else if( longParam1 > 0  && userId>0 )
            {
                q.setParameter( "longParam1", longParam1 );
                q.setParameter( "userId", userId );                
            }

            else if( longParam4 > 0  && userId>0 )
            {
                q.setParameter( "longParam4", longParam4 );
                q.setParameter( "userId", userId );                
            }

            else if( longParam1 > 0 )
                q.setParameter( "longParam1", longParam1 );

            else if( longParam4 > 0 )
                q.setParameter( "longParam4", longParam4 );

            else if( userId > 0 )
                q.setParameter( "userId", userId );

            return (List<UserAction>) q.getResultList();
        }

        catch( Exception e )
        {
            LogService.logIt(e, "UserFacade.getUserActionEmails() longParam1=" + longParam1 + ", userId=" + userId );

            return new ArrayList<>();
        }

    }
    
    

}
