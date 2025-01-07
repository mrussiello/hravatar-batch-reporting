package com.tm2batch.proctor;


import com.tm2batch.entity.event.TestEvent;
import com.tm2batch.entity.proctor.RemoteProctorEvent;
import com.tm2batch.entity.proctor.SuspiciousActivity;
import com.tm2batch.global.STException;
import com.tm2batch.service.LogService;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;


import jakarta.ejb.Stateless;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import javax.sql.DataSource;

//// @ManagedBean
//@Named
@Stateless
public class ProctorFacade
{
    @PersistenceContext( name = "persistence/tm2", unitName = "tm2" )
    EntityManager em;

    // @PersistenceContext( name = "persistence/tm2mirror", unitName = "tm2mirror" )
    // EntityManager emmirror;

    public static ProctorFacade getInstance()
    {
        try
        {
            return (ProctorFacade) InitialContext.doLookup( "java:module/ProctorFacade" );
        }
        catch( Exception e )
        {
            LogService.logIt( e, "ProctorFacade.getInstance() " );
            return null;
        }
    }
    

    public RemoteProctorEvent getRemoteProctorEventForTestEventId( long testEventId ) throws Exception
    {
        try
        {
            TypedQuery<RemoteProctorEvent> q = em.createNamedQuery( "RemoteProctorEvent.findByTestEventId", RemoteProctorEvent.class );
            q.setParameter( "testEventId", testEventId );            
            q.setHint( "jakarta.persistence.cache.retrieveMode", "BYPASS" );
            return q.getSingleResult();
        }
        catch( NoResultException e )
        {
            return null;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "EventFacade.getRemoteProctorEventForTestEventId( testEventId=" + testEventId + " )" );
            return null;
        }
    }
    
        
    
    
    public List<SuspiciousActivity> getSuspiciousActivityForTestEventId( long testKeyId, long testEventId) throws Exception
    {
        try
        {
            TypedQuery<SuspiciousActivity> q = em.createNamedQuery( "SuspiciousActivity.findByTestEventIdOrTestKeyId", SuspiciousActivity.class );
            q.setParameter( "testEventId", testEventId );
            q.setParameter( "testKeyId", testKeyId );
            //TypedQuery<SuspiciousActivity> q = em.createNamedQuery( "SuspiciousActivity.findByTestEventId", SuspiciousActivity.class );
            //q.setParameter( "testEventId", testEventId );
            return q.getResultList();
        }
        catch( Exception e )
        {
            LogService.logIt(e, "EventFacade.getSuspiciousActivityForTestEventId( testEventId=" + testEventId + " )" );
            return null;
        }
    }
    
    
}
