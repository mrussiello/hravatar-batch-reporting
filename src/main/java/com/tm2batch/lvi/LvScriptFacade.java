package com.tm2batch.lvi;

import com.tm2batch.entity.lvi.LvQuestion;
import com.tm2batch.entity.lvi.LvQuestionResponse;
import com.tm2batch.entity.lvi.LvScript;
import com.tm2batch.entity.lvi.LvScriptQuestionMap;
import com.tm2batch.global.STException;
import com.tm2batch.service.LogService;
import java.util.List;

import javax.naming.InitialContext;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;



@Stateless
// @PersistenceContext( name = "persistence/tm2", unitName = "tm2" )
public class LvScriptFacade
{
    @PersistenceContext
    EntityManager em;

    // private static final String PERSISTENCE_UNIT_NAME = "tm2";
    // private static EntityManagerFactory tm2Factory;


    public static LvScriptFacade getInstance()
    {
        try
        {
            return (LvScriptFacade) InitialContext.doLookup( "java:module/LvScriptFacade" );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "LvScriptFacade.getInstance() " );

            return null;
        }
    }
    
    

    

    public List<LvScriptQuestionMap> getLvScriptQuestionMapList( int lvScriptId, boolean loadQuestion ) throws Exception
    {
        try
        {
            TypedQuery<LvScriptQuestionMap> q = em.createNamedQuery( "LvScriptQuestionMap.findByLvScriptId", LvScriptQuestionMap.class );

            q.setParameter( "lvScriptId", lvScriptId );
            
            List<LvScriptQuestionMap> out = q.getResultList();      
            
            if( loadQuestion )
            {
                for( LvScriptQuestionMap m : out )
                {
                    m.setLvQuestion( getLvQuestion( m.getLvQuestionId() ) );
                }
            }
            
            return out;
        }

        catch( Exception e )
        {
            LogService.logIt(e, "LvScriptFacade.getLvScriptQuestionMapList( lvScriptId=" + lvScriptId + " ) " );
            throw new STException( e );
        }
    }   

    public LvQuestion getLvQuestion( long lvQuestionId ) throws Exception
    {
        try
        {
            return em.find( LvQuestion.class, lvQuestionId );
        }
        catch( NoResultException e )
        {
            return null;
        }
        catch( Exception e )
        {
            LogService.logIt(e, "LvScriptFacade.getLvQuestion( " + lvQuestionId + " ) " );
            throw new STException( e );
        }
    }       
    
    public LvScript getLvScript( int lvScriptId ) throws Exception
    {
        try
        {
            return em.find( LvScript.class, lvScriptId );
        }

        catch( NoResultException e )
        {
            return null;
        }

        catch( Exception e )
        {
            LogService.logIt(e, "LvScriptFacade.getLvScript( " + lvScriptId + " ) " );
            throw new STException( e );
        }
    }       

    public List<LvScriptQuestionMap> getLvScriptQuestionMapListForQuestionId( long lvQuestionId ) throws Exception
    {
        try
        {
            TypedQuery<LvScriptQuestionMap> q = em.createNamedQuery( "LvScriptQuestionMap.findByLvQuestionId", LvScriptQuestionMap.class );

            q.setParameter( "lvQuestionId", lvQuestionId );            
            return q.getResultList();            
        }

        catch( Exception e )
        {
            LogService.logIt(e, "LvScriptFacade.getLvScriptQuestionMapListForQuestionId( lvScriptId=" + lvQuestionId + " ) " );
            throw new STException( e );
        }
    }       

    
    public List<LvQuestionResponse> getLvQuestionResponseList( long lvCallId ) throws Exception
    {
        try
        {
            TypedQuery<LvQuestionResponse> q = em.createNamedQuery( "LvQuestionResponse.findByLvCallId", LvQuestionResponse.class );

            q.setParameter( "lvCallId", lvCallId );
            
            return q.getResultList();      
        }

        catch( Exception e )
        {
            LogService.logIt(e, "LvScriptFacade.getLvQuestionResponseList( lvCallId=" + lvCallId + " ) " );
            throw new STException( e );
        }
    }   
    
    
    
    public LvQuestionResponse getLvQuestionResponse(long lvCallId, long lvQuestionId ) throws Exception
    {
        try
        {
            return (LvQuestionResponse) em.createNamedQuery( "LvQuestionResponse.findByLvCallIdAndLvQuestionId" ).setParameter("lvCallId", lvCallId ).setParameter("lvQuestionId", lvQuestionId ).getSingleResult();
        }

        catch( NoResultException e )
        {
            return null;
        }

        catch( Exception e )
        {
            LogService.logIt(e, "LvFacade.getLvQuestionResponse( lvCallId=" + lvCallId + "," + lvQuestionId + " ) " );
            throw new STException( e );
        }
    }       
    
    
}
