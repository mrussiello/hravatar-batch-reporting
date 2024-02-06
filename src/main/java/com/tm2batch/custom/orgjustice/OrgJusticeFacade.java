/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.orgjustice;

import com.tm2batch.event.*;
import com.tm2batch.account.results.TestResultSortType;
import com.tm2batch.global.STException;
import com.tm2batch.service.LogService;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.ejb.Stateless;
import javax.naming.InitialContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import javax.sql.DataSource;

/**
 *
 * @author Mike
 */
@Stateless
public class OrgJusticeFacade
{
    @PersistenceContext
    EntityManager em;

    
    public static OrgJusticeFacade getInstance()
    {
        try
        {
            return (OrgJusticeFacade) InitialContext.doLookup( "java:module/OrgJusticeFacade" );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "OrgJusticeFacade.getInstance() " );
            return null;
        }
    }

    
    
    
    public List<Long> findTestEventIds(  int orgId,
                                            int suborgId,
                                            long authUserId,
                                            List<Integer> productIdList,
                                            Date completedAfter,
                                            Date completedBefore,
                                            int testResultSortTypeId,
                                            int limit,
                                            int offset ) throws Exception
    {
        TestResultSortType testResultSortType = TestResultSortType.getValue( testResultSortTypeId );
        String sqlStr = "SELECT t.testeventid AS 'eid', " + testResultSortType.getSortFieldStr()   + " FROM testevent AS t ";
        String sqlStr2 = "SELECT t.testeventid AS 'eid', " + testResultSortType.getSortFieldStr()   + " FROM testeventarchive AS t ";


        String whereStr = " WHERE t.orgId=" + orgId + " AND t.testeventstatustypeid=" + TestEventStatusType.REPORT_COMPLETE.getTestEventStatusTypeId() + " ";

        List<Long> tl = new ArrayList<>();

        String orderStr = testResultSortType.getTestEventOrderByStr();

        // List<Long> tal = new ArrayList<>();

        if( suborgId > 0 )
        {
            whereStr += " AND t.suborgid=" + suborgId + " ";
        }

        if( authUserId > 0 )
        {
            whereStr += " AND tk.authorizinguserid=" + authUserId + " ";                
        }

        if( productIdList != null && !productIdList.isEmpty() )
        {
            if( productIdList.size()==1 )
                whereStr += " AND t.productId=" + productIdList.get(0) + " ";
                
            else
            {
                StringBuilder pids = new StringBuilder();
                for( Integer pid : productIdList )
                {
                    if( pids.length()>0 )
                        pids.append( "," );
                    pids.append( pid.toString() );
                }
                whereStr += " AND t.productId IN (" + pids.toString() + ") ";
            }
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

        String sql = "(" + sqlStr + whereStr + ") UNION (" + sqlStr2 + whereStr + ") " +
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

            LogService.logIt( "OrgJusticeFacade.findTestEventIds() found " + tl.size() + " records. sql=" + sql );

        }

        catch( Exception e )
        {
            LogService.logIt( e, "OrgJusticeFacade.findTestEventIds() getting ids. orgId=" + orgId + ", sqlStr=" + sql );
            throw new STException( e );
        }

        return tl;
    }

    
    
    
    
}
