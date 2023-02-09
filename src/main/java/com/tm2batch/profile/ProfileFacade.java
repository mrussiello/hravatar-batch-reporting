/*
 * Created on Jan 1, 2007
 *
 */
package com.tm2batch.profile;


import com.tm2batch.entity.profile.Profile;
import com.tm2batch.entity.profile.ProfileEntry;
import com.tm2batch.entity.profile.ProfileProductMap;
import com.tm2batch.global.STException;
import com.tm2batch.service.LogService;
import com.tm2batch.user.UserFacade;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.List;
import javax.naming.InitialContext;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import javax.sql.DataSource;

//// @ManagedBean
//@Named
@Stateless
public class ProfileFacade
{
    @EJB
    UserFacade userFacade;

    @PersistenceContext
    EntityManager em;




    public static ProfileFacade getInstance()
    {
        try
        {
            return (ProfileFacade) InitialContext.doLookup( "java:module/ProfileFacade" );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "getInstance() " );

            return null;
        }
    }



    public Profile findSingleProfileForTestEvent( int productId, int orgId, int profileUsageTypeId ) throws Exception
    {
        List<Profile> pl = findProfileListForTestEvent(  productId,  orgId,  profileUsageTypeId );

        if( pl.isEmpty() )
            return null;
        
        return pl.get(0);
    }

    
    
    public List<Profile> findProfileListForTestEvent( int productId, int orgId, int profileUsageTypeId ) throws Exception
    {
        List<ProfileProductMap> psml = getProfileProductMapListForProductIdAndOrgId( productId, orgId );

        Profile p;
        
        List<Profile> out = new ArrayList<>();

        for( ProfileProductMap psm : psml )
        {
            p = this.getProfile(psm.getProfileId() );
            
            if( p.getProfileUsageTypeId() != profileUsageTypeId )
                continue;

            if( p.getProfileStatusType().isActive() )
            {
                loadProfile(p);
                out.add(p);
            }
        }
        
        List<Profile> aopl = this.getAllOrgProfileListForProductId(productId);
        
        for( Profile pp : aopl )
        {
            if( out.contains( pp ) )
                continue;
            
            if( pp.getProfileUsageTypeId()!=profileUsageTypeId )
                continue;

            if( !pp.getProfileStatusType().isActive() )
                continue;
            
            loadProfile(pp);
            out.add(pp);
        }
        
        return out;
    }
    
    

    public Profile getProfile( int profileId ) throws Exception
    {
        try
        {
            return em.find( Profile.class, profileId );
        }

        catch( NoResultException e )
        {
            return null;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "getProfile() " );

            throw new STException( e );
        }
    }


    public List<ProfileEntry> getProfileEntryList( int profileId ) throws Exception
    {
        try
        {
            Query q = em.createNamedQuery( "ProfileEntry.findByProfileId" );

            q.setParameter( "profileId", profileId );

            // q.setHint( "jakarta.persistence.cache.retrieveMode", "BYPASS" );

            return q.getResultList();
        }

        catch( Exception e )
        {
            LogService.logIt( e, "getProfileEntryList( profileId=" + profileId + " )" );

            throw new STException( e );
        }
    }

    
    public List<Profile> getAllOrgProfileListForProductId( int productId ) throws Exception
    {
        DataSource pool = (DataSource) new InitialContext().lookup( "jdbc/tm2mirror" );

        if( pool == null )
            throw new Exception( "Can not find Datasource" );

        String sqlStr = "SELECT DISTINCT p.profileid FROM profile AS p INNER JOIN profileproductmap AS ppm ON p.profileid=ppm.profileid WHERE p.applyforallorgs=1 AND ppm.productid=" + productId;
        
        List<Profile> out = new ArrayList<>();
        
        try (Connection con = pool.getConnection(); 
             Statement stmt = con.createStatement() )
        {

             ResultSet rs = stmt.executeQuery( sqlStr );

             while( rs.next() )
             {
                 out.add( getProfile( rs.getInt( 1 ) ) );
             }

             rs.close();
             
             return out;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "ProfileFacade.getAllOrgProfileListForProductId() " + sqlStr );

            throw new STException( e );
        }
    }
    
    
    
    public List<ProfileProductMap> getProfileProductMapListForProductIdAndOrgId( int productId, int orgId ) throws Exception
    {
        try
        {
            Query q = em.createNamedQuery( "ProfileProductMap.findByOrgIdAndProductId" );

            q.setParameter( "productId", productId );

            q.setParameter( "orgId", orgId );

            // q.setHint( "jakarta.persistence.cache.retrieveMode", "BYPASS" );

            return q.getResultList();
        }

        catch( Exception e )
        {
            LogService.logIt( e, "getProfileProductMapListForProductIdAndOrgId( productId=" + productId + ", orgId=" + orgId + " )" );

            throw new STException( e );
        }
    }




    public void loadProfile( Profile p ) throws Exception
    {
        if( p.getUsesProfileEntries() )
            p.setProfileEntryList( getProfileEntryList( p.getProfileId() ));
    }



}
