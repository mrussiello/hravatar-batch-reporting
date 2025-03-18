/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.event;

import com.tm2batch.account.results.TestResultSortType;
import com.tm2batch.entity.battery.Battery;
import com.tm2batch.entity.battery.BatteryScore;
import com.tm2batch.entity.event.ItemResponse;
import com.tm2batch.entity.event.TestEvent;
import com.tm2batch.entity.event.TestEventArchive;
import com.tm2batch.entity.event.TestEventScore;
import com.tm2batch.entity.event.TestKey;
import com.tm2batch.entity.event.TestKeyArchive;
import com.tm2batch.entity.purchase.Product;
import com.tm2batch.entity.report.Report;
import com.tm2batch.entity.user.OrgAutoTest;
import com.tm2batch.global.Constants;
import com.tm2batch.global.STException;
import com.tm2batch.service.LogService;
import com.tm2batch.util.StringUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import jakarta.ejb.Stateless;
import javax.naming.InitialContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import javax.sql.DataSource;

/**
 *
 * @author Mike
 */
@Stateless
public class EventFacade
{
    @PersistenceContext
    EntityManager em;

    
    public static EventFacade getInstance()
    {
        try
        {
            return (EventFacade) InitialContext.doLookup( "java:module/EventFacade" );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "EventFacade.getInstance() " );
            return null;
        }
    }

    public Product getProduct( int productId ) throws Exception
    {
        try
        {
            if( productId <= 0 )
                throw new Exception( "productId is invalid " + productId );

            // else it's a system type (0 or 1)
            return em.find( Product.class, (Integer)( productId ) );
        }
        catch( NoResultException e )
        {
            return null;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "EventFacade.getProduct( " + productId + " )" );
            throw new STException( e );
        }
    }    
    
    public Battery getBattery( int batteryId ) throws Exception
    {
        try
        {
            if( batteryId <= 0 )
                throw new Exception( "productId is invalid " + batteryId );

            return em.find( Battery.class, (Integer)( batteryId ) );
        }

        catch( NoResultException e )
        {
            return null;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "EventFacade.getBattery( " + batteryId + " )" );
            throw new STException( e );
        }
    }   
    
    public OrgAutoTest getOrgAutoTest( int orgAutoTestId ) throws Exception
    {
        try
        {
            if( orgAutoTestId <= 0 )
                throw new Exception( "orgAutoTestId is invalid " + orgAutoTestId );

            // else it's a system type (0 or 1)
            return em.find( OrgAutoTest.class, (Integer)( orgAutoTestId ) );
        }
        catch( NoResultException e )
        {
            return null;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "EventFacade.getOrgAutoTest( " + orgAutoTestId + " )" );
            throw new STException( e );
        }
    }    
    
    public List<TestEvent> getTestEventsForTestKey( long testKeyId) throws Exception
    {
        try
        {
            // if( tm2Factory == null )
                // tm2Factory = PersistenceManager.getInstance().getEntityManagerFactory();

            // EntityManager em = tm2Factory.createEntityManager();

            Query q = em.createNamedQuery( "TestEventArchive.findByTestKeyId",  TestEventArchive.class );

            q.setParameter( "testKeyId", testKeyId );

            List<TestEvent> tel = new ArrayList<>();

            List<TestEventArchive> tkal = q.getResultList();

            for( TestEventArchive tka : tkal )
            {
                tel.add( tka.getTestEvent() );
            }

            q = em.createNamedQuery( "TestEvent.findByTestKeyId" );

            q.setParameter( "testKeyId", testKeyId );

            tel.addAll( q.getResultList() );

            Collections.sort( tel );

            return tel;
        }
        catch( Exception e )
        {
            LogService.logIt(e, "EventFacade.getTestEventsForTestKey( " + testKeyId + " ) " );
            throw new STException( e );
        }
    }    
    
    public TestEvent getTestEvent( long testEventId ) throws Exception
    {
        try
        {
            TestEvent te =  em.find( TestEvent.class, (Long)( testEventId ) );

            if( te != null )
                return te;

            TestEventArchive tea = getTestEventArchiveForTestEventId( testEventId );

            return tea != null ? tea.getTestEvent() : null;
        }

        catch( NoResultException e )
        {
            TestEventArchive tea = getTestEventArchiveForTestEventId( testEventId );

            return tea != null ? tea.getTestEvent() : null;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "EventFacade.getTestEvent( " + testEventId + " ) " );

            throw new STException( e );
        }
    }
    
    public TestEventArchive getTestEventArchiveForTestEventId( long testEventId ) throws Exception
    {
        try
        {
            Query q = em.createNamedQuery( "TestEventArchive.findByTestEventId" );

            q.setParameter( "testEventId", testEventId );

            // q.setHint( "jakarta.persistence.cache.retrieveMode", "BYPASS" );

            return (TestEventArchive) q.getSingleResult();
        }

        catch( NoResultException e )
        {
            return null;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "EventFacade.getTestEventArchiveForTestEventId( " + testEventId + " ) " );
            throw new STException( e );
        }
    }
    
    
    public TestKey getTestKey( long testKeyId, boolean refresh ) throws Exception
    {
        try
        {
            // if( tm2Factory == null )
                // tm2Factory = PersistenceManager.getInstance().getEntityManagerFactory();

            // EntityManager em = tm2Factory.createEntityManager();

            if( refresh )
            {
                TestKey tk = (TestKey) em.createNamedQuery( "TestKey.findByTestKeyId" ).setHint( "jakarta.persistence.cache.retrieveMode", "BYPASS" ).setParameter( "testKeyId", testKeyId ).getSingleResult();

                if( tk!=null )
                    return tk;

                TestKeyArchive tka = getTestKeyArchiveForTestKeyId( testKeyId );
                return tka != null ? tka.getTestKey() : null;
            }

            TestKey tk =  em.find(TestKey.class, testKeyId);

            if( tk!= null )
                return tk;

            TestKeyArchive tka = getTestKeyArchiveForTestKeyId( testKeyId );

            if( tka != null )
                return tka.getTestKey();

            return null;
        }

        catch( NoResultException e )
        {
            TestKeyArchive tka = getTestKeyArchiveForTestKeyId( testKeyId );

            return tka != null ? tka.getTestKey() : null;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "EventFacade.getTestKey( " + testKeyId + ", " + refresh + " ) " );

            throw new STException( e );
        }
    }
    
    public Report getReport( long reportId ) throws Exception
    {
        try
        {
            if( reportId <= 0 )
                throw new Exception( "reportId is invalid " + reportId );

            return em.find(Report.class, reportId);
        }

        catch( NoResultException e )
        {
            return null;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "getReport( " + reportId + " )" );
            throw new STException( e );
        }
    }
    


    public TestKeyArchive getTestKeyArchiveForTestKeyId( long testKeyId ) throws Exception
    {
        try
        {
            // if( tm2Factory == null )
                // tm2Factory = PersistenceManager.getInstance().getEntityManagerFactory();

            // EntityManager em = tm2Factory.createEntityManager();

            Query q = em.createNamedQuery( "TestKeyArchive.findByTestKeyId" );

            q.setParameter( "testKeyId", testKeyId );

            return (TestKeyArchive) q.getSingleResult();

        }

        catch( NoResultException e )
        {
            return null;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "EventFacade.getTestKeyArchiveForTestKeyId( " + testKeyId + " ) " );

            throw new STException( e );
        }
    }
    
    public BatteryScore findActiveBatteryScoreForTestKeyId( long testKeyId ) throws Exception
    {
        try
        {
            return (BatteryScore) em.createNamedQuery( "BatteryScore.findActiveByTestKeyId" ).setParameter( "testKeyId", testKeyId ).getSingleResult();
        }

        catch( NoResultException e )
        {
            return null;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "EventFacade.findBatteryScoreForTestKeyId( " + testKeyId + " ) " );
            throw new STException( e );
        }

    }
    
    public List<ItemResponse> getItemResponsesForTestEvent( long testEventId ) throws Exception
    {
        try
        {
            if( testEventId<=0 )
                return new ArrayList<>();

            Query q = em.createNamedQuery( "ItemResponse.findByTestEventId" );

            q.setParameter( "testEventId", testEventId );

            List<ItemResponse> out = (List<ItemResponse>) q.getResultList();

            Collections.sort(out);

            return out;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "EventFacade.getItemResponsesForTestEvent( testEventId=" + testEventId + " ) " );

            throw new STException( e );
        }
    }
    
    
    
    public List<TestEventScore> getTestEventScoresForTestEvent( long testEventId, int testEventScoreTypeId ) throws Exception
    {
        try
        {
            // if( tm2Factory == null )
                // tm2Factory = PersistenceManager.getInstance().getEntityManagerFactory();

            // EntityManager em = tm2Factory.createEntityManager();

            Query q = em.createNamedQuery( testEventScoreTypeId<0 ? "TestEventScore.findByTestEventId" : "TestEventScore.findByTestEventIdAndTestEventScoreTypeId" );

            q.setParameter( "testEventId", testEventId );
            
            if( testEventScoreTypeId>=0 )
                q.setParameter( "testEventScoreTypeId", testEventScoreTypeId );
                
            //if( refresh )
            //    q.setHint( "jakarta.persistence.cache.retrieveMode", "BYPASS" );

            List<TestEventScore> sl = (List<TestEventScore>) q.getResultList();

            Collections.sort( sl );

            return sl;
        }

        catch( Exception e )
        {
            LogService.logIt(e, "EventFacade.getTestEventScoresForTestEvent( " + testEventId + " ) " );

            throw new STException( e );
        }

    }
    
    public List<TestKey> findTestKeys(  int orgId,
                                        int suborgId,
                                        long authUserId,
                                        int maxTestKeyStatusTypeId,
                                        String productNameKeyword, // productNameKeyword,
                                        int productId, // testResultBean.getProductId(),
                                        int productTypeId, // testResultBean.getProductTypeId(),
                                        int consumerProductTypeId, // testResultBean.getConsumerProductTypeId(),
                                        int batteryId, // testResultBean.getBatteryId(),
                                        int orgAutoTestId, // testResultBean.getProductId(),
                                        Date startDate, // testResultBean.getCompletedAfter(),
                                        Date endDate, // testResultBean.getCompletedBefore(),
                                        int sortTypeId, // testResultBean.getTestResultSortTypeId(),
                                        int maxRows  ) throws Exception
    {
        if( maxRows<=0 )
            maxRows = Constants.DEFAULT_MAX_RESULT_ROWS;
        
        String sqlStr = "SELECT t.testkeyid AS 'tkid' FROM testkey AS t ";
        String sqlStr2 = "SELECT t.testkeyid AS 'tkid' FROM testkeyarchive AS t ";

        String joinStr = (productNameKeyword!=null && !productNameKeyword.isBlank()) || consumerProductTypeId>=0 ? " INNER JOIN product AS p ON p.productid=t.productid " : "";
                        
        //String joinStrTk = "";
        //String joinStrTk2 = "";
        
        String whereStr = " WHERE t.orgId=" + orgId + " AND t.statustypeid<=" + maxTestKeyStatusTypeId + " ";

        List<Long> tl = new ArrayList<>();

        if( suborgId > 0 )
        {
            whereStr += " AND t.suborgid=" + suborgId + " ";
        }

        if( authUserId > 0 )
        {
            whereStr += " AND t.authorizinguserid=" + authUserId + " ";                
        }

        if( productNameKeyword != null && !productNameKeyword.isBlank() )
        {
            productNameKeyword = StringUtils.sanitizeForSqlQuery( productNameKeyword );

            whereStr += " AND p.name LIKE '%" + productNameKeyword + "%' ";
        }

        if( productTypeId > 0 )
        {
            whereStr += " AND t.producttypeid=" + productTypeId;
        }

        if( consumerProductTypeId >= 0 )
        {
            whereStr += " AND p.consumerproducttypeid=" + consumerProductTypeId;
        }

        if( batteryId > 0 )
        {
            whereStr += " AND t.batteryid=" + batteryId;
        }

        else if( productId > 0 )
        {
            whereStr += " AND t.productid=" + productId;
        }

        else if( orgAutoTestId > 0 )
        {
            whereStr += " AND t.orgautotestid=" + orgAutoTestId;
        }


        // java.sql.Date sDate;

        if( startDate != null )
        {
            // sDate = new java.sql.Date( completedAfter.getTime() );
            java.sql.Timestamp ts = new java.sql.Timestamp( startDate.getTime() );
            whereStr += " AND t.startdate >='" + ts.toString() + "' ";
        }

        if( endDate != null )
        {
            java.sql.Timestamp ts = new java.sql.Timestamp( endDate.getTime() );
            // sDate = new java.sql.Date( completedBefore.getTime() );

            whereStr += " AND t.startdate<='" + ts.toString() + "' ";
        }

        String sql = "(" + sqlStr + joinStr + whereStr + ") UNION (" + sqlStr2 + joinStr + whereStr + ") " +
                " ORDER BY tkid " +
                " LIMIT " + maxRows;
                
        DataSource pool = (DataSource) new InitialContext().lookup( "jdbc/tm2mirror" );

        if( pool == null )
            throw new Exception( "Can not find Datasource" );

        try( Connection con = pool.getConnection();
             Statement stmt = con.createStatement() )
        {
            con.setTransactionIsolation( Connection.TRANSACTION_READ_UNCOMMITTED );

            ResultSet rs = stmt.executeQuery( sql );

            while( rs.next() )
            {
                tl.add( rs.getLong(1) );
            }

            rs.close();

            LogService.logIt( "EventFacade.findTestKeys() found " + tl.size() + " records. sql=" + sql );

        }

        catch( Exception e )
        {
            LogService.logIt( e, "EventFacade.findTestEvents() getting ids. orgId=" + orgId + ", sqlStr=" + sql );

            throw new STException( e );
        }

        List<TestKey> out = new ArrayList<>();
        for( Long l : tl )
        {
            out.add( getTestKey(l, true) );
        }

        return out;
        
    }
    
    
    public List<TestEvent> findTestEvents(  int orgId,
                                            int suborgId,
                                            long authUserId,
                                            String productNameKeyword,
                                            String lastNameStart,
                                            String emailKeyword,
                                            int orgAutoTestId,
                                            int productId,
                                            String productIdStr,
                                            int productTypeId,
                                            int consumerProductTypeId,
                                            int batteryId,
                                            long userId,
                                            String extRef,
                                            String lastName,
                                            String altIdentifier,
                                            String pin,
                                            String langStr,
                                            String[] customs,
                                            Date completedAfter,
                                            Date completedBefore,
                                            int testResultSortTypeId,
                                            int userCompanyStatusTypeId,
                                            int limit,
                                            int offset ) throws Exception
    {
        String sql = "";

        TestResultSortType testResultSortType = TestResultSortType.getValue( testResultSortTypeId );

        boolean ndsUsr = (altIdentifier != null && !altIdentifier.isEmpty()) || 
                (lastName != null && !lastName.isEmpty()) || 
                (lastNameStart != null && !lastNameStart.isEmpty() ) || 
                (emailKeyword != null && !emailKeyword.isEmpty() ) || 
                testResultSortType.needsUserTable() || 
                userCompanyStatusTypeId>=0 ;


        String sqlStr = "SELECT t.testeventid AS 'eid', " + testResultSortType.getSortFieldStr()   + " FROM testevent AS t ";
        String sqlStr2 = "SELECT t.testeventid AS 'eid', " + testResultSortType.getSortFieldStr()   + " FROM testeventarchive AS t ";


        String joinStr = ( ndsUsr ? " INNER JOIN xuser AS u ON u.userid=t.userid " : "" ) +
                         ( ( productNameKeyword != null && !productNameKeyword.isEmpty() ) || consumerProductTypeId>=0 ? " INNER JOIN product AS p ON p.productid=t.productid " : "" );
                        
        String joinStrTk = "";
        String joinStrTk2 = "";
        
        boolean hasCustoms = false;
        
        if( customs!=null )
        {
            for( String cc : customs )
            {
                if( cc!=null && !cc.isEmpty() )
                    hasCustoms=true;
            }
        }
        
        if( orgAutoTestId>0 || hasCustoms || authUserId>0  )
        {
            joinStrTk = " INNER JOIN testkey AS tk ON tk.testkeyid=t.testkeyid ";
            joinStrTk2 = " INNER JOIN testkeyarchive AS tk ON tk.testkeyid=t.testkeyid ";
        }

        String whereStr = " WHERE t.orgId=" + orgId + " AND t.testeventstatustypeid=" + TestEventStatusType.REPORT_COMPLETE.getTestEventStatusTypeId() + " ";

        List<Long> tl = new ArrayList<>();

        String orderStr = testResultSortType.getTestEventOrderByStr();

        // List<Long> tal = new ArrayList<>();

        if( suborgId > 0 )
        {
            whereStr += " AND t.suborgid=" + suborgId + " ";
        }

        if( orgAutoTestId > 0 )
        {
            whereStr += " AND tk.orgautotestid=" + orgAutoTestId + " ";
        }

        if( authUserId > 0 )
        {
            whereStr += " AND tk.authorizinguserid=" + authUserId + " ";                
        }

        if( productNameKeyword != null && !productNameKeyword.isEmpty() )
        {
            productNameKeyword = StringUtils.sanitizeForSqlQuery( productNameKeyword );

            whereStr += " AND p.name LIKE '%" + productNameKeyword + "%' ";
        }

        if( langStr != null && !langStr.isEmpty() )
        {
            langStr = StringUtils.sanitizeForSqlQuery( langStr );

            whereStr += " AND t.lang='" + langStr + "' ";
        }

        if( lastName != null && !lastName.isEmpty() )
        {
            lastName = StringUtils.sanitizeForSqlQuery( lastName );

            whereStr += " AND u.lastname='" + lastName + "' ";
        }

        else if( lastNameStart != null && !lastNameStart.isEmpty() )
        {
            lastNameStart = StringUtils.sanitizeForSqlQuery( lastNameStart );

            whereStr += " AND u.lastname LIKE '" + lastNameStart + "%' ";
        }

        if( emailKeyword != null && !emailKeyword.isEmpty() )
        {
            emailKeyword = StringUtils.sanitizeForSqlQuery( emailKeyword );

            whereStr += " AND u.email LIKE '%" + emailKeyword + "%' ";
        }

        if( altIdentifier != null && !altIdentifier.isEmpty() )
        {
            altIdentifier = StringUtils.sanitizeForSqlQuery( altIdentifier );

            whereStr += " AND u.altidentifier='" + altIdentifier + "' ";
        }

        if( hasCustoms )
        {
            String cc = customs[0];                
            if( cc!=null && !cc.isEmpty() )
                whereStr += " AND tk.custom1='" + cc + "' ";
            cc = customs[1];                
            if( cc!=null && !cc.isEmpty() )
                whereStr += " AND tk.custom2='" + cc + "' ";
            cc = customs[2];                
            if( cc!=null && !cc.isEmpty() )
                whereStr += " AND tk.custom3='" + cc + "' ";
        }

        //if( productId > 0 )
        //{
        //    whereStr += " AND t.productid=" + productId;
        //}

        if( productTypeId>0 )
        {
            whereStr += " AND t.producttypeid=" + productTypeId;
        }

        if( consumerProductTypeId>=0 )
        {
            whereStr += " AND p.consumerproducttypeid=" + consumerProductTypeId;
        }

        if( batteryId>0 )
        {
            whereStr += " AND t.batteryid=" + batteryId;
        }

        if( productId>0 )
        {
            whereStr += " AND t.productid=" + productId;
        }
        
        else if( productIdStr!=null && !productIdStr.isBlank() )
        {
            whereStr += " AND t.productid IN (" + productIdStr + ")";
        }

        if( userId > 0 )
        {
            whereStr += " AND t.userid=" + userId;
        }




        if( userCompanyStatusTypeId >=0 )
            whereStr += " AND u.usercompanystatustypeid=" + userCompanyStatusTypeId + " ";

        if( extRef != null && !extRef.isEmpty() )
        {
            extRef = StringUtils.sanitizeForSqlQuery( extRef );

            whereStr += " AND t.extref='" + extRef + "' ";
        }

        if( pin != null && !pin.isEmpty() )
        {
            pin = StringUtils.sanitizeForSqlQuery( pin );

            whereStr += " AND t.pin='" + pin + "' ";
        }

        // java.sql.Date sDate;

        if( completedAfter != null )
        {
            // sDate = new java.sql.Date( completedAfter.getTime() );
            java.sql.Timestamp ts = new java.sql.Timestamp( completedAfter.getTime() );

            whereStr += " AND t.lastaccessdate >='" + ts.toString() + "' ";
        }

        if( completedBefore != null )
        {
            java.sql.Timestamp ts = new java.sql.Timestamp( completedBefore.getTime() );
            // sDate = new java.sql.Date( completedBefore.getTime() );

            whereStr += " AND t.lastaccessdate <='" + ts.toString() + "' ";
        }

        sql = "(" + sqlStr + joinStr + joinStrTk + whereStr + ") UNION (" + sqlStr2 + joinStr + joinStrTk2 + whereStr + ") " +
                ( orderStr != null && !orderStr.isEmpty() ? orderStr : "" ) +
                (limit>0 ? " LIMIT " + limit : "") + 
                (offset>0 ? " OFFSET " + offset : "");
                
        DataSource pool = (DataSource) new InitialContext().lookup( "jdbc/tm2mirror" );

        if( pool == null )
            throw new Exception( "Can not find Datasource" );

        try( Connection con = pool.getConnection();
             Statement stmt = con.createStatement() )
        {
            con.setTransactionIsolation( Connection.TRANSACTION_READ_UNCOMMITTED );

            ResultSet rs = stmt.executeQuery( sql );

            while( rs.next() )
            {
                tl.add( rs.getLong(1) );
            }

            rs.close();

            LogService.logIt( "EventFacade.findTestEvents() found " + tl.size() + " records. sql=" + sql );

        }

        catch( Exception e )
        {
            LogService.logIt( e, "EventFacade.findTestEvents() getting ids. orgId=" + orgId + ", sqlStr=" + sql );

            throw new STException( e );
        }

        List<TestEvent> out = new ArrayList<>();

        for( Long l : tl )
        {
            out.add( getTestEvent(l) );
        }

        // Collections.sort( out, new TestEventReverseDateComparator() );

        return out;
    }

    
    
    
    
}
