package com.tm2batch.ref;

import com.tm2batch.entity.ref.RcCheck;
import com.tm2batch.entity.ref.RcOrgPrefs;
import com.tm2batch.entity.ref.RcRater;
import com.tm2batch.entity.ref.RcRating;
import com.tm2batch.entity.ref.RcReferral;
import com.tm2batch.entity.ref.RcSuspiciousActivity;
import com.tm2batch.global.Constants;
import com.tm2batch.global.STException;
import com.tm2batch.service.LogService;
import com.tm2batch.util.StringUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import javax.sql.DataSource;



@Stateless
public class RcFacade
{
    @PersistenceContext
    EntityManager em;

    public static RcFacade getInstance()
    {
        try
        {
            return (RcFacade) InitialContext.doLookup( "java:module/RcFacade" );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "RcFacade.getInstance() " );
            return null;
        }
    }
    
    
    public RcOrgPrefs getRcOrgPrefsForOrgId( int orgId ) throws Exception
    {
        try
        {
            Query q = em.createNamedQuery( "RcOrgPrefs.findByOrgId", RcOrgPrefs.class );
            q.setParameter( "orgId", orgId );            
            return (RcOrgPrefs) q.getSingleResult();
        }
        catch( NoResultException e )
        {
            return null;
        }
        catch( Exception e )
        {
            LogService.logIt(e, "RcFacade.getRcOrgPrefs( " + orgId + " ) " );
            throw new STException( e );
        }
    }   
    
    
    
    public List<RcCheck> findRcCheckList(   long adminUserId, 
                                            long userId, 
                                            int orgId, 
                                            int suborgId, 
                                            String lastNameKey,
                                            String emailKey,
                                            Date startDate,
                                            Date endDate,
                                            String candidateAccessCode, 
                                            int rcCheckTypeId, 
                                            int rcCheckStatusTypeId,
                                            int minRcCheckStatusTypeId, 
                                            int maxRcCheckStatusTypeId, 
                                            int rcScriptId,
                                            int maxRows,
                                            int sortTypeId ) throws Exception
    {
        if( rcCheckStatusTypeId>=0 )
        {
            minRcCheckStatusTypeId = -1;
            maxRcCheckStatusTypeId = -1;
        }
        
        String sqlStr = "SELECT rc.rccheckid FROM rccheck AS rc ";
        
        if( (lastNameKey!=null && !lastNameKey.isBlank())  || (emailKey!=null && !emailKey.isBlank()) || sortTypeId==1 )
            sqlStr += " INNER JOIN xuser AS u ON u.userid=rc.userid ";

        String whereStr = "";
        
        if( adminUserId>0 )
            whereStr += " rc.adminuserid=" + adminUserId + " ";
        
        if( userId>0 )
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";            
            whereStr += " rc.userid=" + userId + " ";
        }
        
        if( candidateAccessCode!=null && !candidateAccessCode.isBlank() )
        {
            candidateAccessCode = StringUtils.sanitizeForSqlQuery(candidateAccessCode);
            if( !whereStr.isBlank() )
                whereStr += " AND ";            
            whereStr += " rc.candidateaccesscode='" + candidateAccessCode + "' ";            
        }
        
        if( orgId>0 )
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";            
            whereStr += " rc.orgid=" + orgId + " ";
        }

        if( suborgId>0 )
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";            
            whereStr += " rc.suborgid=" + suborgId + " ";
        }
        
        
        if( rcScriptId > 0 )
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";
            
            whereStr += " rc.rcscriptid=" + rcScriptId + " ";            
        }
        
        if( lastNameKey!=null && !lastNameKey.isBlank() )
        {
            lastNameKey = StringUtils.sanitizeForSqlQuery( lastNameKey );
            lastNameKey = lastNameKey.trim();
            
            if( !whereStr.isBlank() )
                whereStr += " AND ";
            whereStr += " u.lastname LIKE '%" + lastNameKey + "%' ";
        }

        if( emailKey!=null && !emailKey.isBlank() )
        {
            emailKey = StringUtils.sanitizeForSqlQuery( emailKey );
            emailKey = emailKey.trim();
            
            if( !whereStr.isBlank() )
                whereStr += " AND ";
            whereStr += " u.email LIKE '%" + emailKey + "%' ";
        }
        
        if( rcCheckStatusTypeId>=0 )
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";            
            whereStr += " rc.rccheckstatustypeid=" + rcCheckStatusTypeId + " ";            
        }
        
        if( maxRcCheckStatusTypeId>=0 )
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";            
            whereStr += " rc.rccheckstatustypeid<=" + maxRcCheckStatusTypeId + " ";
        }
        
        else
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";            
            whereStr += " rc.rccheckstatustypeid<" + RcCheckStatusType.ARCHIVED.getRcCheckStatusTypeId() + " ";
        }
        
        if( minRcCheckStatusTypeId>=0 )
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";            
            whereStr += " rc.rccheckstatustypeid>=" + minRcCheckStatusTypeId + " ";
        }

        if( !whereStr.isBlank() )
            whereStr += " AND ";            
        whereStr += " rc.rccheckstatustypeid<302 "; // + RcCheckStatusType.ARCHIVED.getRcCheckStatusTypeId() + " ";            

        if( rcCheckTypeId>=0 )
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";            
            whereStr += " rc.rcchecktypeid=" + rcCheckTypeId + " ";
        }
        
        if( startDate != null )
        {
            if( whereStr.length() > 0 )
                whereStr += " AND ";

            java.sql.Timestamp ts = new java.sql.Timestamp( startDate.getTime() );
            
            whereStr += " rc.createdate IS NOT NULL AND rc.createdate >='" + ts.toString() + "' ";
            // whereStr += " rc.createdate >='" + new java.sql.Date( startDate.getTime() ).toString() + "' ";
        }

        if( endDate != null )
        {
            if( whereStr.length() > 0 )
                whereStr += " AND ";

            java.sql.Timestamp ts = new java.sql.Timestamp( endDate.getTime() );
            whereStr += " (rc.createdate IS NULL OR rc.createdate <='" + ts.toString() + "') ";
            //whereStr += " rc.createdate <='" + new java.sql.Date( endDate.getTime() ).toString() + "' ";
        }
        
        String sortStr = " ORDER BY ";
        
        if( sortTypeId==1 )
            sortStr += " u.lastname ";
        else if( sortTypeId == 2 )
            sortStr += " rc.rccheckstatustypeid ";
        else 
            sortStr += " rc.lastupdate DESC ";
        
        

        DataSource pool = (DataSource) new InitialContext().lookup( "jdbc/tm2mirror" );

        if( pool == null )
            throw new Exception( "Can not find Datasource" );

        
        List<Long> idlst = new ArrayList<>();
        try (Connection con = pool.getConnection();
             Statement stmt = con.createStatement() )
        {
            if( whereStr.isBlank() )
                throw new Exception( "No Search Criteria found.");
        
            sqlStr += " WHERE " + whereStr;
            
            if( maxRows<=0 )
                maxRows = Constants.DEFAULT_MAX_RESULT_ROWS;
            
            sqlStr +=  sortStr + " LIMIT " + maxRows;
            
            // LogService.logIt( "RCFacade.findRcCheckList() orgId=" + orgId + ", sqlStr=" + sqlStr );
        
            ResultSet rs = stmt.executeQuery( sqlStr );
            
            while( rs.next() )
            {
                idlst.add( rs.getLong(1));
            }
            rs.close();
        }
        catch( Exception e )
        {
            LogService.logIt( e, "LvFacade.findRcCheckList() orgId=" + orgId + ", " + sqlStr );
            throw new STException(e);
        }
        
        List<RcCheck> out = new ArrayList<>();
        
        for( Long lvid : idlst )
        {
            out.add( getRcCheck( lvid, true ) );
        }
        
        return out;
    }
    
    
    
    public RcCheck getRcCheck( long rcCheckId, boolean refresh ) throws Exception
    {
        try
        {
            if( refresh )
                return (RcCheck) em.createNamedQuery( "RcCheck.findByRcCheckId" ).setParameter("rcCheckId", rcCheckId ).getSingleResult();

            return em.find(RcCheck.class, rcCheckId );
        }

        catch( NoResultException e )
        {
            return null;
        }

        catch( Exception e )
        {
            LogService.logIt(e, "RcFacade.getRcCheck( " + rcCheckId + ", " + refresh + " ) " );
            throw new STException( e );
        }
    }       
    
    
    
    public List<RcSuspiciousActivity> getRcSuspiciousActivityList( long rcCheckId ) throws Exception
    {
        try
        {
            return em.createNamedQuery( "RcSuspiciousActivity.findByRcCheckId" ).setParameter("rcCheckId", rcCheckId ).getResultList();
        }
        catch( Exception e )
        {
            LogService.logIt(e, "RcFacade.getRcSuspiciousActivityList( " + rcCheckId + " ) " );
            throw new STException( e );
        }        
    }
    
    public List<RcReferral> getRcReferralList( long rcCheckId ) throws Exception
    {
        try
        {
            return em.createNamedQuery( "RcReferral.findByRcCheckId" ).setHint( "jakarta.persistence.cache.retrieveMode", "BYPASS" ).setParameter("rcCheckId", rcCheckId ).getResultList();
        }
        catch( Exception e )
        {
            LogService.logIt(e, "RcFacade.getRcReferralList( " + rcCheckId + " ) " );
            throw new STException( e );
        }        
    }
    
    
    
    public List<RcRater> getRcRaterList( long rcCheckId ) throws Exception
    {
        try
        {
            return em.createNamedQuery( "RcRater.findByRcCheckId" ).setParameter("rcCheckId", rcCheckId ).getResultList();
        }
        catch( Exception e )
        {
            LogService.logIt(e, "RcFacade.getRcRaterList( " + rcCheckId + " ) " );
            throw new STException( e );
        }        
    }
    
    
    public List<RcRating> getRcRatingList( long rcCheckId, long rcRaterId ) throws Exception
    {
        try
        {
            return em.createNamedQuery( "RcRating.findByRcCheckAndRater" ).setParameter("rcCheckId", rcCheckId ).setParameter("rcRaterId", rcRaterId ).getResultList();
        }
        catch( Exception e )
        {
            LogService.logIt(e, "RcFacade.getRcRatingList( rcCheckId=" + rcCheckId + ", rcRaterId=" + rcRaterId + " ) " );
            throw new STException( e );
        }        
    }
    
    
    
}
