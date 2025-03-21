/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.autoreport;

import com.tm2batch.entity.autoreport.BatchReport;
import com.tm2batch.entity.user.UserReportOptions;
import com.tm2batch.global.STException;
import com.tm2batch.service.LogService;
import java.util.Date;
import java.util.List;
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
public class AutoReportFacade
{
    @PersistenceContext
    EntityManager em;

    
    public static AutoReportFacade getInstance()
    {
        try
        {
            return (AutoReportFacade) InitialContext.doLookup( "java:module/AutoReportFacade" );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "AutoReportFacade.getInstance() " );
            return null;
        }
    }

    public BatchReport getBatchReport( int batchReportId ) throws Exception
    {
        try
        {
            return (BatchReport) em.createNamedQuery( "BatchReport.findById" ).setParameter( "batchReportId", batchReportId ).setHint( "jakarta.persistence.cache.retrieveMode", "BYPASS" ).getSingleResult();
        }
        catch( NoResultException e )
        {
            return null;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "CorpFacade.getBatchReport( " + batchReportId + " ) " );
            throw new STException( e );
        }
    }
    
    
    public List<BatchReport> getSpeciallyActiveBatchReportList() throws Exception
    {
        try
        {
            Query q = em.createNamedQuery( "BatchReport.findSpecialActive" ).setParameter( "batchReportStatusTypeId", BatchReportStatusType.ACTIVE.getBatchReportStatusTypeId() ).setHint( "jakarta.persistence.cache.retrieveMode", "BYPASS" );
            return q.getResultList();
        }
        catch( Exception e )
        {
            LogService.logIt( e, "AutoReportFacade.getSpeciallyActiveBatchReportList() " );
            throw new STException( e );
        }        
        
    }
    
    public List<BatchReport> getOneTimeActiveBatchReportList() throws Exception
    {
        try
        {
            Query q = em.createNamedQuery( "BatchReport.findByStatusAndScheduleDate" ).setParameter( "batchReportStatusTypeId", BatchReportStatusType.ACTIVE.getBatchReportStatusTypeId() ).setParameter( "maxScheduleDate", new Date() ).setHint( "jakarta.persistence.cache.retrieveMode", "BYPASS" );
            return q.getResultList();
        }
        catch( Exception e )
        {
            LogService.logIt( e, "AutoReportFacade.getOneTimeActiveBatchReportList() " );
            throw new STException( e );
        }        
    }
    
    public List<BatchReport> getActiveBatchReportList() throws Exception
    {
        try
        {
            Query q = em.createNamedQuery( "BatchReport.findByStatusAndMinFreq" ).setParameter( "batchReportStatusTypeId", BatchReportStatusType.ACTIVE.getBatchReportStatusTypeId() ).setParameter( "minFrequencyTypeId", 1 ).setHint( "jakarta.persistence.cache.retrieveMode", "BYPASS" );
            return q.getResultList();
        }
        catch( Exception e )
        {
            LogService.logIt( e, "AutoReportFacade.getActiveBatchReportList() " );
            throw new STException( e );
        }        
    }


    
    public BatchReport saveBatchReport( BatchReport batchReport ) throws Exception
    {
        //Context envCtx = (Context) new InitialContext().lookup( "java:comp/env" );
        //EntityManager em = (EntityManager) envCtx.lookup( "persistence/tm2" );

        try
        {
            if( batchReport.getUserId() == 0 )
                throw new Exception( "Cannot save BatchReport when userId=0" );

            if( batchReport.getOrgId() == 0 )
                throw new Exception( "Cannot save BatchReport when orgId=0" );

            if( batchReport.getTitle()==null || batchReport.getTitle().isBlank() )
                throw new Exception( "Title is required." );
            
            if( batchReport.getCreateDate() == null )
                batchReport.setCreateDate( new Date() );

            batchReport.setLastUpdate( new Date() );

            if( batchReport.getBatchReportId() > 0 )
                em.merge(batchReport );

            else
                em.persist(batchReport );

            // This causes any exceptions to be thrown here instead of in the EJB transaction.
            // Makes it easier to figure out what went wrong.
            em.flush();
        }

        catch( Exception e )
        {
            LogService.logIt(e, "AutoReportFacade.saveBatchReport() " + batchReport.toString() );
            throw new Exception( "AutoReportFacade.saveBatchReport() " + batchReport.toString() + " " + e.toString() );
        }

        return batchReport;
    }
    
    public UserReportOptions saveUserReportOptions( UserReportOptions uro ) throws Exception
    {
        //Context envCtx = (Context) new InitialContext().lookup( "java:comp/env" );
        //EntityManager em = (EntityManager) envCtx.lookup( "persistence/tm2" );

        try
        {
            if( uro.getUserId() == 0 )
                throw new Exception( "Cannot save BatchReport when userId=0" );

            
            if( uro.getCreateDate() == null )
                uro.setCreateDate( new Date() );

            uro.setLastUpdate( new Date() );

            if( uro.getUserReportOptionsId() > 0 )
                em.merge(uro );

            else
                em.persist(uro );

            // This causes any exceptions to be thrown here instead of in the EJB transaction.
            // Makes it easier to figure out what went wrong.
            em.flush();
        }

        catch( Exception e )
        {
            LogService.logIt(e, "AutoReportFacade.saveUserReportOptions() " + uro.toString() );
            throw new Exception( "AutoReportFacade.saveUserReportOptions() " + uro.toString() + " " + e.toString() );
        }

        return uro;
    }
    
    
    public UserReportOptions getUserReportOptions( long userId ) throws Exception
    {
        try
        {
            Query q = em.createNamedQuery( "UserReportOptions.findByUserId" );
            q.setParameter( "userId", userId );            
            return (UserReportOptions) q.getSingleResult();
        }
        catch( NoResultException e )
        {
            return null;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "AutoReportFacade.getUserReportOptions( " + userId + " ) " );
            throw new STException( e );
        }
    }
    
    
    
}
