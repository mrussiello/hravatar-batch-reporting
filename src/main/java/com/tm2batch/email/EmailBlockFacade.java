/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.email;

import com.tm2batch.entity.email.EmailBlock;
import com.tm2batch.global.STException;
import com.tm2batch.service.LogService;
import jakarta.ejb.Stateless;
import javax.naming.InitialContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/**
 *
 * @author Mike
 */
@Stateless
// @PersistenceContext( name = "persistence/tm2", unitName = "tm2" )
public class EmailBlockFacade
{
    // private static final String PERSISTENCE_UNIT_NAME = "tm2";
    // private static EntityManagerFactory tm2Factory;
    @PersistenceContext( name = "persistence/tm2", unitName = "tm2" )
    EntityManager em;


    public static EmailBlockFacade getInstance()
    {
        try
        {
            return (EmailBlockFacade) InitialContext.doLookup( "java:module/EmailBlockFacade" );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "EmailBlockFacade.getInstance() " );

            return null;
        }
    }



    public boolean hasEmailBlock( String email, boolean fullBlock) throws Exception
    {
        try
        {
            if( email==null || email.trim().isEmpty() )
                return false;
            
            Query q = em.createNamedQuery( fullBlock ? "EmailBlock.findFullBlockForEmail" : "EmailBlock.findForEmail" );

            q.setParameter( "email", email );

            // q.setHint( "jakarta.persistence.cache.retrieveMode", "BYPASS" );

            EmailBlock emailBlock = (EmailBlock) q.getSingleResult();

            return emailBlock != null;
        }

        catch( NoResultException e )
        {
            return false;
        }

        catch( Exception e )
        {
            LogService.logIt(e, "hasEmailBlock() email=" + email );

            throw new STException( e );
        }

    }

}
