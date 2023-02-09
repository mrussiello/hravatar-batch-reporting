package com.tm2batch.ref;

import com.tm2batch.entity.ref.RcCompetency;
import com.tm2batch.entity.ref.RcItem;
import com.tm2batch.global.STException;
import com.tm2batch.service.LogService;
import com.tm2batch.entity.ref.RcScript;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.naming.InitialContext;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;



@Stateless
// @PersistenceContext( name = "persistence/tm2", unitName = "tm2" )
public class RcScriptFacade
{
    @PersistenceContext
    EntityManager em;

    // private static EntityManagerFactory tm2Factory;

    // private static final String PERSISTENCE_UNIT_NAME = "tm2";
    // private static EntityManagerFactory tm2Factory;


    public static RcScriptFacade getInstance()
    {
        try
        {
            return (RcScriptFacade) InitialContext.doLookup( "java:module/RcScriptFacade" );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "RcScriptFacade.getInstance() " );
            return null;
        }
    }

    
    public RcScript getRcScript( int rcScriptId ) throws Exception
    {
        try
        {
            return em.find(RcScript.class, rcScriptId );
        }
        catch( NoResultException e )
        {
            return null;
        }
        catch( Exception e )
        {
            LogService.logIt(e, "RcScriptFacade.getRcScript( " + rcScriptId + " ) " );
            throw new STException( e );
        }
    }       
    
    
    
    public RcItem getRcItem( int rcItemId, boolean load) throws Exception
    {
        try
        {
            RcItem r = (RcItem) em.find(RcItem.class, rcItemId );
            
            if( r!=null && r.getRcCompetencyId()>0 )
                r.setRcCompetency( getRcCompetency( r.getRcCompetencyId() ));
            
            return r;
        }
        catch( NoResultException e )
        {
            return null;
        }
        catch( Exception e )
        {
            LogService.logIt(e, "RcScriptFacade.getRcItem( " + rcItemId + " ) " );
            throw new STException( e );
        }
    }       

    
    public List<RcItem> getRcItemList( int rcCompetencyId ) throws Exception
    {
        try
        {
            TypedQuery<RcItem> q = em.createNamedQuery( "RcItem.findByCompetencyId", RcItem.class);
            q.setParameter( "rcCompetencyId", rcCompetencyId );
            return q.getResultList();
        }
        catch( Exception e )
        {
            LogService.logIt(e, "RcScriptFacade.getRcCompetencyList( rcCompetencyId=" + rcCompetencyId + " ) " );
            throw e;
        }        
    }
        
    
    public RcCompetency getRcCompetency( int rcCompetencyId ) throws Exception
    {
        try
        {
            return em.find(RcCompetency.class, rcCompetencyId );
        }
        catch( NoResultException e )
        {
            return null;
        }
        catch( Exception e )
        {
            LogService.logIt(e, "RcScriptFacade.getRcCompetency( " + rcCompetencyId + " ) " );
            throw new STException( e );
        }
    }       
    
    
    public void loadScriptObjects( RcScript s, boolean loadIfNeeded) throws Exception
    {
        s.parseScriptJson();        
        List<RcItem> itms;
        RcItemWrapper w; 
        List<RcItemWrapper> iwl;
        Date lastItemUpdate = null;
        boolean save = false;
        for( RcCompetencyWrapper rcw : s.getRcCompetencyWrapperList() )
        {
            if( rcw.getRcCompetencyId()>0 && ( !loadIfNeeded || rcw.getRcCompetency()==null) )
                rcw.setRcCompetency( getRcCompetency( rcw.getRcCompetencyId() ) );

            itms = getRcItemList( rcw.getRcCompetencyId() );
            iwl = new ArrayList<>();
            
            for( RcItem itm : itms )
            {
                itm.setRcCompetency( rcw.getRcCompetency() );
                
                if( lastItemUpdate==null || lastItemUpdate.before( itm.getLastUpdate() ) )
                    lastItemUpdate = itm.getLastUpdate();
                
                w = rcw.getRcItemWrapper( itm.getRcItemId() );
                if( w ==null )
                {
                    w = new RcItemWrapper();
                    w.setRcItemId( itm.getRcItemId() );                    
                }    
                w.setRcItem(itm);                    
                iwl.add(w);
            }
            if( rcw.getRcItemWrapperList().size()!=iwl.size() )
                save = true;
            Collections.sort(iwl);
            rcw.setRcItemWrapperList(iwl);
        }
        
        // clean the RcComeptencyWrapper list.
        ListIterator<RcCompetencyWrapper> iter = s.getRcCompetencyWrapperList().listIterator();
        RcCompetencyWrapper rcw;
        while( iter.hasNext() )
        {
            rcw = iter.next();
            if( rcw.getRcCompetencyId()>0 && rcw.getRcCompetency()==null )
                iter.remove();
        }
    }
    
    

}
