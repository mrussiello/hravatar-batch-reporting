package com.tm2batch.lvi;

import com.tm2batch.entity.lvi.LvCall;
import com.tm2batch.entity.lvi.LvOrgPrefs;
import com.tm2batch.entity.lvi.LvQuestionResponse;
import com.tm2batch.entity.proctor.SuspiciousActivity;
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
// @PersistenceContext( name = "persistence/tm2", unitName = "tm2" )
public class LvFacade
{
    @PersistenceContext
    EntityManager em;

    // private static EntityManagerFactory tm2Factory;

    // private static final String PERSISTENCE_UNIT_NAME = "tm2";
    // private static EntityManagerFactory tm2Factory;


    public static LvFacade getInstance()
    {
        try
        {
            return (LvFacade) InitialContext.doLookup( "java:module/LvFacade" );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "LvFacade.getInstance() " );

            return null;
        }
    }
    
    
    
    
    public LvOrgPrefs getLvOrgPrefsForOrgId( int orgId ) throws Exception
    {
        try
        {
            Query q = em.createNamedQuery( "LvOrgPrefs.findByOrgId", LvOrgPrefs.class );

            q.setParameter( "orgId", orgId );
            
            return (LvOrgPrefs) q.getSingleResult();
        }

        catch( NoResultException e )
        {
            return null;
        }

        catch( Exception e )
        {
            LogService.logIt(e, "LvFacade.getLvOrgPrefs( " + orgId + " ) " );

            throw new STException( e );
        }
    }   
    
    /*
    public LvUserPrefs getLvUserPrefsForUserId( long userId ) throws Exception
    {
        try
        {
            Query q = em.createNamedQuery( "LvUserPrefs.findByUserId", LvUserPrefs.class );

            q.setParameter( "userId", userId );
            
            return (LvUserPrefs) q.getSingleResult();
        }

        catch( NoResultException e )
        {
            return null;
        }

        catch( Exception e )
        {
            LogService.logIt(e, "LvFacade.getLvUserPrefsForUserId( " + userId + " ) " );

            throw new STException( e );
        }
    } 
    */
    
    

    public LvCall getLvCall( long lvCallId, boolean load ) throws Exception
    {
        try
        {
            LvCall t= em.find(LvCall.class, lvCallId );
            
            if( load && t!=null )
            {
                //t.setLvInvitation( getLvInvitation( t.getLvInvitationId(), false, false ) );

                t.setLvQuestionResponseList( getLvQuestionResponseList( t.getLvCallId() ));
            }
            
            return t;
        }

        catch( NoResultException e )
        {
            return null;
        }

        catch( Exception e )
        {
            LogService.logIt(e, "LvFacade.getLvCall( " + lvCallId + " ) " );

            throw new STException( e );
        }
    }       
        
    
    public List<LvCall> findLvCallList( long initiatorUserId, 
                                        long userId, 
                                        int orgId, 
                                        int suborgId,
                                        long recipientUserId,
                                        String lastNameKey,
                                        String emailKey,
                                        List<Long> userIdList,
                                        Date startDate,
                                        Date endDate,
                                        List<Long> lvCallIdList,
                                        int lvCallStatusTypeId,
                                        int minLvCallStatusTypeId,
                                        int lvScriptId,
                                        boolean load ) throws Exception
    {
        
        String sqlStr = "SELECT lvi.lvcallid FROM lvcall AS lvi ";
        
        if( (lastNameKey!=null && !lastNameKey.isBlank())  || (emailKey!=null && !emailKey.isBlank()) )
        {
            sqlStr += " INNER JOIN xuser AS u ON u.userid=lvi.recipientuserid ";
        }

        String whereStr = "";
        
        if( initiatorUserId>0 )
            whereStr += " lvi.initiatoruserid=" + initiatorUserId + " ";
        
        if( userId>0 )
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";
            
            whereStr += " lvi.userid=" + userId + " ";
        }
        
        else if( userIdList!=null && !userIdList.isEmpty() )
        {
            StringBuilder sb = new StringBuilder();
            for( Long uid : userIdList )
            {
                if( !sb.isEmpty() )
                    sb.append(",");
                sb.append( uid.toString());
            }
            if( !sb.isEmpty() )
            {
                whereStr += " AND lvi.userid IN (" + sb.toString() + ") ";
            }
        }        
        
        
        if( orgId>0 )
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";
            
            whereStr += " lvi.orgid=" + orgId + " ";
        }

        if( suborgId>0 )
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";
            
            whereStr += " lvi.suborgid=" + suborgId + " ";
        }

        if( recipientUserId>0 )
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";
            
            whereStr += " lvi.recipientuserid=" + recipientUserId + " ";
        }

        if( lvScriptId > 0 )
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";
            
            whereStr += " lvi.lvscriptid=" + lvScriptId + " ";            
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
        
        if( minLvCallStatusTypeId>0 )
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";
            
            whereStr += " lvi.lvcallstatustypeid>=" + minLvCallStatusTypeId + " ";
        }

        if( lvCallStatusTypeId>=0 )
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";
            
            whereStr += " lvi.lvcallstatustypeid=" + lvCallStatusTypeId + " ";
        }
        
        else
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";
            
            whereStr += " lvi.lvcallstatustypeid<>" + LvCallStatusType.ARCHIVED.getLvCallStatusTypeId() + " ";            
        }
        
        if( startDate != null )
        {
            if( whereStr.length() > 0 )
                whereStr += " AND ";

            java.sql.Timestamp ts = new java.sql.Timestamp( startDate.getTime() );
            whereStr += " lvi.completedate >='" + ts.toString() + "' ";
        }

        if( endDate != null )
        {
            if( whereStr.length() > 0 )
                whereStr += " AND ";

            java.sql.Timestamp ts = new java.sql.Timestamp( endDate.getTime() );
            whereStr += " lvi.completedate <='" + ts.toString() + "' ";
        }
        
        
        if( lvCallIdList!=null && !lvCallIdList.isEmpty() )
        {
            if( !whereStr.isBlank() )
                whereStr += " AND ";

            sqlStr += " lvi.lvcallid IN (";
            
            String idl = "";
            for( Long lvCallId : lvCallIdList )
            {
                if( !idl.isBlank() )
                    idl +=",";
                
                idl += lvCallId;
            }
            
            sqlStr += idl + ") ";
        }

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
            
            ResultSet rs = stmt.executeQuery( sqlStr  );
            
            while( rs.next() )
            {
                idlst.add( rs.getLong(1));
            }
            rs.close();

            LogService.logIt( "LvFacade.findLvCallList() Found " + idlst.size() + ", SQL=" + sqlStr );
            
        }
        catch( Exception e )
        {
            LogService.logIt( e, "LvFacade.findLvCallList() orgId=" + orgId + ", " + sqlStr );
            throw new STException(e);
        }
        
        List<LvCall> out = new ArrayList<>();
        
        for( Long lvid : idlst )
        {
            out.add( getLvCall( lvid, load ) );
        }
        
        return out;
    }   
    
    

    
    public List<LvQuestionResponse> getLvQuestionResponseList( long lvCallId ) throws Exception
    {
        try
        {
            Query q = em.createNamedQuery( "LvQuestionResponse.findByLvCallId", LvQuestionResponse.class );
            
            q.setParameter("lvCallId", lvCallId );
            
            return q.getResultList();
        }

        catch( Exception e )
        {
            LogService.logIt(e, "LvFacade.getLvQuestionResponseList( " + lvCallId + " ) " );
            throw new STException( e );
        }
    }       
    
    public List<SuspiciousActivity> getSuspiciousActivityList( long lvCallId ) throws Exception
    {
        try
        {
            //if( tm2Factory == null ) 
            //    tm2Factory = PersistenceManager.getInstance().getEntityManagerFactory();

            //EntityManager em = tm2Factory.createEntityManager();

            Query q = em.createNamedQuery( "SuspiciousActivity.findByLvCallId", SuspiciousActivity.class );            
            q.setParameter("lvCallId", lvCallId );            
            //q;            
            return q.getResultList();
        }

        catch( Exception e )
        {
            LogService.logIt(e, "LvFacade.getSuspiciousActivityList( " + lvCallId + " ) " );
            throw new STException( e );
        }
    }       
        
}
