/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.activity;


import com.tm2batch.entity.battery.Battery;
import com.tm2batch.entity.purchase.Product;
import com.tm2batch.entity.user.Org;
import com.tm2batch.entity.user.Suborg;
import com.tm2batch.event.TestKeyStatusType;
import com.tm2batch.global.Constants;
import com.tm2batch.global.STException;
import com.tm2batch.service.LogService;
import com.tm2batch.user.UserFacade;
import com.tm2batch.util.MessageFactory;
import com.tm2batch.util.StringUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import jakarta.ejb.Stateless;
import javax.naming.InitialContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import javax.sql.DataSource;

/**
 *
 * @author Mike
 */
@Stateless
public class DashboardFacade {

    @PersistenceContext
    EntityManager em;

    public static DashboardFacade getInstance()
    {
        try
        {
            return (DashboardFacade) InitialContext.doLookup( "java:module/DashboardFacade" );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "DashboardFacade.getInstance() " );

            return null;
        }
    }


    /**
     *
     * returns:
     *
     * unlimited (Boolean)
     * startdate
     * enddate
     * org
     * suborg - only if suborg only
     * bysuborg  List<SuborgActivitySummary>
     * byproduct  List<ProductActivitySummary>
     * bybattery  List<ProductActivitySummary>
     * byproducttestkey  List<ProductActivitySummary> for Dropoff
     * totaltestkey ProductActivitySummary  for Dropoff
     * rcbysuborg List<RcActivitySummary>
     * lvbysuborg List<LvActivitySummary>
     *
     * @param orgId
     * @param suborgId
     * @param suborgOnly
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public Map<String, Object> getDashboardDataMap( Org org,
                                                    int suborgId,
                                                    boolean suborgOnly,
                                                    Date startDate,
                                                    Date endDate,
                                                    Locale locale ) throws Exception
    {
        Map<String, Object> out = new HashMap<>();

        out.put( "startdate", startDate );
        out.put( "enddate", endDate );

        boolean isUnlimited = org.getOrgCreditUsageType().getUnlimited();
        out.put( "unlimited", isUnlimited );

        java.sql.Date sDate = new java.sql.Date( startDate.getTime() );
        java.sql.Date eDate = new java.sql.Date( endDate.getTime() );

        int orgId = org.getOrgId();

        UserFacade userFacade = UserFacade.getInstance();

        Suborg suborg = suborgId>0 ? userFacade.getSuborg(suborgId) : null;

        out.put( "org", org );
        out.put( "suborg", suborg );

        String suborgStr = suborgOnly ? " AND suborgid=" + suborgId + " " : " ";
        String suborgStrTk = suborgOnly ? " AND tk.suborgid=" + suborgId + " " : " ";
        String suborgStrBs = suborgOnly ? " AND bs.suborgid=" + suborgId + " " : " ";

        boolean isResultCredit = org.getOrgCreditUsageType().getAnyResultCredit();
        out.put( "candidatecredit", isResultCredit );

        DataSource pool = (DataSource) new InitialContext().lookup( "jdbc/tm2mirror" );

        if( pool == null )
            throw new Exception( "Can not find Datasource" );

        Map<Integer,Integer> logonsByOrg = new HashMap<>();
        int tempInt = 0;
        int tempInt2 = 0;
        int tempInt3 = 0;
        float tempFloat = 0;
        Float tempFloatVal;

        Integer tempInteger;
        Integer tempInteger2;
        int totalLogons = 0;

        Map<Integer,Suborg> suborgMap = new TreeMap<>();

        Suborg noSuborg = new Suborg();
        noSuborg.setName( MessageFactory.getStringMessage(locale, "g.NoSuborgAssigned", null) );

        Suborg total = new Suborg();
        total.setSuborgId(-1);
        total.setName( MessageFactory.getStringMessage(locale, "g.FullOrganization", null) );

        


        // suborgMap.put( 0, full );

        List<Suborg> suborgs = userFacade.getSuborgList(orgId, false);

        Set<Integer> suborgIdSet = new HashSet<>();

        // No suborg assigned
        if( !suborgOnly )
            suborgs.add(0, noSuborg);

        // all suborgs and full org
        if( !suborgOnly )
            suborgs.add(total);

        for( Suborg s : suborgs )
        {
            if( suborg != null && s.getSuborgId() != suborgId )
                continue;

            suborgMap.put( s.getSuborgId(), s );

            suborgIdSet.add( s.getSuborgId() );
        }

        Set<Integer> productIdSet = new HashSet<>();

        Map<Integer,Product> productMap = new TreeMap<>();

        List<SuborgActivitySummary> sasl = new ArrayList<>();
        out.put( "bysuborg", sasl );

        List<ProductActivitySummary> pasl = new ArrayList<>();
        out.put( "byproduct", pasl );

        List<ProductActivitySummary> basl = new ArrayList<>();
        out.put( "bybattery", basl );

        List<ProductActivitySummary> pasltk = new ArrayList<>();
        out.put( "byproducttestkey", pasltk );

        List<ProductActivitySummary> paslBatt = new ArrayList<>();
        out.put("byproductbattery", paslBatt );

        List<RcActivitySummary> rasl = new ArrayList<>();
        out.put("rcbysuborg", rasl );

        List<LvActivitySummary> lasl = new ArrayList<>();
        out.put("lvbysuborg", lasl );
        
        Product p = new Product();
        p.setName( MessageFactory.getStringMessage(locale, "g.AllTests", null));

        Product pb = new Product();
        pb.setName( MessageFactory.getStringMessage(locale, "g.AllBatteries", null));

        ProductActivitySummary totalPasTk = new ProductActivitySummary(p);
        out.put( "totaltestkey", totalPasTk );

        ProductActivitySummary totalPasBatt = new ProductActivitySummary(pb);
        out.put( "totaltestbattery", totalPasBatt );

        // this is added at the end of the rasl
        RcActivitySummary totalRasl = new RcActivitySummary();
        totalRasl.setName( MessageFactory.getStringMessage( locale, "g.All"  ));
        out.put( "totalrc", totalRasl );

        // this is added at the end of the lasl
        LvActivitySummary totalLasl = new LvActivitySummary();
        totalLasl.setName( MessageFactory.getStringMessage( locale, "g.All"  ));
        out.put( "totallv", totalLasl );
        
        
        SuborgActivitySummary sa;

        for( Suborg s : suborgs )
        {
            if( suborg != null && s.getSuborgId() != suborgId )
                continue;

            sa = new SuborgActivitySummary( s );
            sasl.add( sa );
        }

        Map<Integer,Integer> testKeysCreatedMap = new HashMap<>();
        int totalTestKeysCreated = 0;

        Map<Integer,Integer> testKeysStartedMap = new HashMap<>();
        int totalTestKeysStarted = 0;

        Map<Integer,Integer> batteriesCreatedMap = new HashMap<>();
        int totalBatteriesCreated = 0;

        Map<Integer,Integer> batteriesStartedMap = new HashMap<>();
        int totalBatteriesStarted = 0;
        

        Map<Integer,Integer> testKeysFinishedMap = new HashMap<>();
        int totalTestKeysFinished = 0;

        Map<Integer,Integer> batteriesFinishedMap = new HashMap<>();
        int totalBatteriesFinished = 0;
        
        
        Map<Integer,Integer> creditsUsedMap = new HashMap<>();
        int totalCreditsUsed = 0;

        Map<Integer,Integer> productIdStartMap = new HashMap<>();

        Map<Integer,Integer> productIdCompletionMap = new HashMap<>();

        Map<Integer,Integer> productIdCreditsUsedMap = new HashMap<>();

        Map<Integer,Integer> productIdTestKeysCreatedMap = new HashMap<>();

        // Map<Integer,Float> productIdWeightedSumMap = new HashMap<>();
        Map<Integer,Integer> productIdTotalScoredMap = new HashMap<>();
        Map<Integer,Float> productIdAverageMap = new HashMap<>();

        Map<Integer,Map<Integer,Integer>> suborgUserCompanyStatusMap = new HashMap<>();

        // Map<Integer,Map<Integer,Integer>> productUserCompanyStatusMap = new HashMap<>();

        Map<Integer,Integer> userCompanyStatusMap;

        String sqlStr = "SELECT suborgid, count(1) FROM logonhistory WHERE orgid=" + orgId + suborgStr  + " AND logondate>='" + sDate.toString() + "' AND logondate<='" + eDate.toString() + "' GROUP BY suborgid ";

        try (Connection con = pool.getConnection(); Statement stmt = con.createStatement() )
        {
             con.setTransactionIsolation( Connection.TRANSACTION_READ_UNCOMMITTED );

             ResultSet rs = stmt.executeQuery( sqlStr );

             while( rs.next() )
             {
                 tempInt2 = rs.getInt(1);

                 if( !suborgIdSet.contains( tempInt2 ) )
                     continue;

                 tempInt = rs.getInt( 2 ) ;
                 totalLogons += tempInt;
                 logonsByOrg.put( tempInt2, tempInt );
             }

            rs.close();

            // LogService.logIt( "DashboardFacade.getDataMap() " + sqlStr );

            RcActivitySummary ras;
            
            // Next, RcChecks created by suborg
            sqlStr = " SELECT suborgid, COUNT(1) FROM rccheck WHERE orgid=" + orgId + " " +  suborgStr + " AND createDate>='" + sDate.toString() + "' AND createDate<='" + eDate.toString() + "' GROUP BY suborgid ";
            rs = stmt.executeQuery( sqlStr );
            while( rs.next() )
            {
                // suborgId
                tempInt2 = rs.getInt( 1 );

                if( suborgOnly && tempInt2 != suborgId )
                    continue;

                 if( !suborgIdSet.contains( tempInt2 ) )
                     continue;
                 
                ras = null;
                for( RcActivitySummary r : rasl )
                {
                    if( r.getSuborgId()==tempInt2 )
                    {
                        ras = r;
                        break;
                    }
                }
                if( ras == null )
                {
                    ras = new RcActivitySummary();
                    ras.setSuborgId( tempInt2 );
                    rasl.add(ras);
                }
                
                 // count started
                tempInt3 = rs.getInt( 2 );
                ras.setRcChecks( ras.getRcChecks() + tempInt3 );                
            }
            rs.close();

            // Next, RcCheck completes by suborg
            sqlStr = " SELECT suborgid, COUNT(1) FROM rccheck WHERE orgid=" + orgId + " " +  suborgStr + " AND completedate>='" + sDate.toString() + "' AND completedate<='" + eDate.toString() + "' GROUP BY suborgid ";
            rs = stmt.executeQuery( sqlStr );
            while( rs.next() )
            {
                // suborgId
                tempInt2 = rs.getInt( 1 );

                if( suborgOnly && tempInt2 != suborgId )
                    continue;

                 if( !suborgIdSet.contains( tempInt2 ) )
                     continue;
                 
                ras = null;
                for( RcActivitySummary r : rasl )
                {
                    if( r.getSuborgId()==tempInt2 )
                    {
                        ras = r;
                        break;
                    }
                }
                if( ras == null )
                {
                    ras = new RcActivitySummary();
                    ras.setSuborgId( tempInt2 );
                    rasl.add(ras);
                }
                
                 // count started
                tempInt3 = rs.getInt( 2 );
                ras.setRcCheckCompletes(ras.getRcCheckCompletes() + tempInt3 );                
            }
            rs.close();

            // Next, Rc Candidate Starts created by suborg
            sqlStr = " SELECT suborgid, COUNT(1) FROM rccheck WHERE orgid=" + orgId + " " +  suborgStr + " AND rccandidatestatustypeid>=20 AND candidatelastupdate>='" + sDate.toString() + "' AND candidatelastupdate<='" + eDate.toString() + "' GROUP BY suborgid ";
            rs = stmt.executeQuery( sqlStr );
            while( rs.next() )
            {
                // suborgId
                tempInt2 = rs.getInt( 1 );

                if( suborgOnly && tempInt2 != suborgId )
                    continue;

                 if( !suborgIdSet.contains( tempInt2 ) )
                     continue;
                 
                ras = null;
                for( RcActivitySummary r : rasl )
                {
                    if( r.getSuborgId()==tempInt2 )
                    {
                        ras = r;
                        break;
                    }
                }
                if( ras == null )
                {
                    ras = new RcActivitySummary();
                    ras.setSuborgId( tempInt2 );
                    rasl.add(ras);
                }
                
                 // count started
                tempInt3 = rs.getInt( 2 );
                ras.setCandidateStarts(ras.getCandidateStarts() + tempInt3 );                
            }
            rs.close();            
            
            
            // Next, Rc Candidate Completes created by suborg
            sqlStr = " SELECT suborgid, COUNT(1) FROM rccheck WHERE orgid=" + orgId + " " +  suborgStr + " AND rccandidatestatustypeid=100 AND candidatecompletedate>='" + sDate.toString() + "' AND candidatecompletedate<='" + eDate.toString() + "' GROUP BY suborgid ";
            rs = stmt.executeQuery( sqlStr );
            while( rs.next() )
            {
                // suborgId
                tempInt2 = rs.getInt( 1 );

                if( suborgOnly && tempInt2 != suborgId )
                    continue;

                 if( !suborgIdSet.contains( tempInt2 ) )
                     continue;
                 
                ras = null;
                for( RcActivitySummary r : rasl )
                {
                    if( r.getSuborgId()==tempInt2 )
                    {
                        ras = r;
                        break;
                    }
                }
                if( ras == null )
                {
                    ras = new RcActivitySummary();
                    ras.setSuborgId( tempInt2 );
                    rasl.add(ras);
                }
                
                 // count started
                tempInt3 = rs.getInt( 2 );
                ras.setCandidateCompletes(ras.getCandidateCompletes() + tempInt3 );                
            }
            rs.close();

            // Next, Rc Raters Created  by suborg
            sqlStr = " SELECT tk.suborgid, COUNT(1) FROM rcrater rr INNER JOIN rccheck tk on rr.rccheckid=tk.rccheckid  WHERE tk.orgid=" + orgId + " " +  suborgStrTk + " AND rr.createdate>='" + sDate.toString() + "' AND rr.createdate<='" + eDate.toString() + "' GROUP BY tk.suborgid ";
            rs = stmt.executeQuery( sqlStr );
            while( rs.next() )
            {
                // suborgId
                tempInt2 = rs.getInt( 1 );

                if( suborgOnly && tempInt2 != suborgId )
                    continue;

                 if( !suborgIdSet.contains( tempInt2 ) )
                     continue;
                 
                ras = null;
                for( RcActivitySummary r : rasl )
                {
                    if( r.getSuborgId()==tempInt2 )
                    {
                        ras = r;
                        break;
                    }
                }
                if( ras == null )
                {
                    ras = new RcActivitySummary();
                    ras.setSuborgId( tempInt2 );
                    rasl.add(ras);
                }
                
                 // count started
                tempInt3 = rs.getInt( 2 );
                ras.setRaterCreates(ras.getRaterCreates() + tempInt3 );                
            }
            rs.close();            


            // Next, Rc Rater Starts  by suborg
            sqlStr = " SELECT tk.suborgid, COUNT(1) FROM rcrater rr INNER JOIN rccheck tk on rr.rccheckid=tk.rccheckid  WHERE tk.orgid=" + orgId + " " +  suborgStrTk + " AND rr.rcraterstatustypeid>=20 AND rr.startdate IS NOT NULL AND rr.startdate>='" + sDate.toString() + "' AND rr.startdate<='" + eDate.toString() + "' GROUP BY tk.suborgid ";
            rs = stmt.executeQuery( sqlStr );
            while( rs.next() )
            {
                // suborgId
                tempInt2 = rs.getInt( 1 );

                if( suborgOnly && tempInt2 != suborgId )
                    continue;

                 if( !suborgIdSet.contains( tempInt2 ) )
                     continue;
                 
                ras = null;
                for( RcActivitySummary r : rasl )
                {
                    if( r.getSuborgId()==tempInt2 )
                    {
                        ras = r;
                        break;
                    }
                }
                if( ras == null )
                {
                    ras = new RcActivitySummary();
                    ras.setSuborgId( tempInt2 );
                    rasl.add(ras);
                }
                
                 // count started
                tempInt3 = rs.getInt( 2 );
                ras.setRaterStarts(ras.getRaterStarts() + tempInt3 );                
            }
            rs.close();            

            // Next, Rc Rater Completes  by suborg
            sqlStr = " SELECT tk.suborgid, COUNT(1) FROM rcrater rr INNER JOIN rccheck tk on rr.rccheckid=tk.rccheckid  WHERE tk.orgid=" + orgId + " " +  suborgStrTk + " AND rr.rcraterstatustypeid=100 AND rr.lastupdate IS NOT NULL AND rr.lastupdate>='" + sDate.toString() + "' AND rr.lastupdate<='" + eDate.toString() + "' GROUP BY tk.suborgid ";
            rs = stmt.executeQuery( sqlStr );
            while( rs.next() )
            {
                // suborgId
                tempInt2 = rs.getInt( 1 );

                if( suborgOnly && tempInt2 != suborgId )
                    continue;

                 if( !suborgIdSet.contains( tempInt2 ) )
                     continue;
                 
                ras = null;
                for( RcActivitySummary r : rasl )
                {
                    if( r.getSuborgId()==tempInt2 )
                    {
                        ras = r;
                        break;
                    }
                }
                if( ras == null )
                {
                    ras = new RcActivitySummary();
                    ras.setSuborgId( tempInt2 );
                    rasl.add(ras);
                }
                
                 // count started
                tempInt3 = rs.getInt( 2 );
                ras.setRaterCompletes(ras.getRaterCompletes() + tempInt3 );                
            }
            rs.close();   
            
            // Finish the Rc activity.
            boolean hasData = false;
            ListIterator<RcActivitySummary> riter = rasl.listIterator();
            while( riter.hasNext() )
            {
                ras = riter.next();
                if( !ras.getHasData() )
                    riter.remove();
            }
            for( RcActivitySummary ra : rasl )
            {
                if( ra.getHasData() )
                    hasData=true;
                ra.addToRcActivitySummary(totalRasl);
                if( ra.getSuborgId()==0 )
                    ra.setName( MessageFactory.getStringMessage(locale, "g.None" ) );
                else
                    ra.setSuborg( suborgMap.get( ra.getSuborgId()));
            }
            if( hasData )
            {
                Collections.sort(rasl);
                rasl.add( totalRasl );
            }
            else
                rasl.clear();
                        
            LvActivitySummary las;
            
            // Next, LvInvitations created by suborg
            sqlStr = " SELECT suborgid, COUNT(1) FROM lvinvitation WHERE orgid=" + orgId + " " +  suborgStr + " AND createDate>='" + sDate.toString() + "' AND createDate<='" + eDate.toString() + "' GROUP BY suborgid ";
            rs = stmt.executeQuery( sqlStr );
            while( rs.next() )
            {
                // suborgId
                tempInt2 = rs.getInt( 1 );

                if( suborgOnly && tempInt2 != suborgId )
                    continue;

                 if( !suborgIdSet.contains( tempInt2 ) )
                     continue;
                 
                las = null;
                for( LvActivitySummary r : lasl )
                {
                    if( r.getSuborgId()==tempInt2 )
                    {
                        las = r;
                        break;
                    }
                }
                if( las == null )
                {
                    las = new LvActivitySummary();
                    las.setSuborgId( tempInt2 );
                    lasl.add(las);
                }
                
                 // count started
                tempInt3 = rs.getInt( 2 );
                las.setLvInvitations(las.getLvInvitations() + tempInt3 );                
            }
            rs.close();

            // Next, LvCalls started by suborg
            sqlStr = " SELECT suborgid, COUNT(1) FROM lvcall WHERE orgid=" + orgId + " " +  suborgStr + " AND lvcallstatustypeid>=50 AND startdate IS NOT NULL AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY suborgid ";
            rs = stmt.executeQuery( sqlStr );
            while( rs.next() )
            {
                // suborgId
                tempInt2 = rs.getInt( 1 );

                if( suborgOnly && tempInt2 != suborgId )
                    continue;

                 if( !suborgIdSet.contains( tempInt2 ) )
                     continue;
                 
                las = null;
                for( LvActivitySummary r : lasl )
                {
                    if( r.getSuborgId()==tempInt2 )
                    {
                        las = r;
                        break;
                    }
                }
                if( las == null )
                {
                    las = new LvActivitySummary();
                    las.setSuborgId( tempInt2 );
                    lasl.add(las);
                }
                
                 // count started
                tempInt3 = rs.getInt( 2 );
                las.setLvStarts(las.getLvStarts() + tempInt3 );                
            }
            rs.close();

            
            // Next, LvCalls completed by suborg
            sqlStr = " SELECT suborgid, COUNT(1) FROM lvcall WHERE orgid=" + orgId + " " +  suborgStr + " AND lvcallstatustypeid>=100 AND completedate IS NOT NULL AND completedate>='" + sDate.toString() + "' AND completedate<='" + eDate.toString() + "' GROUP BY suborgid ";
            rs = stmt.executeQuery( sqlStr );
            while( rs.next() )
            {
                // suborgId
                tempInt2 = rs.getInt( 1 );

                if( suborgOnly && tempInt2 != suborgId )
                    continue;

                 if( !suborgIdSet.contains( tempInt2 ) )
                     continue;
                 
                las = null;
                for( LvActivitySummary r : lasl )
                {
                    if( r.getSuborgId()==tempInt2 )
                    {
                        las = r;
                        break;
                    }
                }
                if( las == null )
                {
                    las = new LvActivitySummary();
                    las.setSuborgId( tempInt2 );
                    lasl.add(las);
                }
                
                 // count started
                tempInt3 = rs.getInt( 2 );
                las.setLvCompletes(las.getLvCompletes() + tempInt3 );                
            }
            rs.close();

            // Finish the Lv activity.
            ListIterator<LvActivitySummary> liter = lasl.listIterator();
            while( liter.hasNext() )
            {
                las = liter.next();
                if( !las.getHasData() )
                    liter.remove();
            }
            
            hasData = false;
            for( LvActivitySummary ra : lasl )
            {
                if( ra.getHasData() )
                    hasData = true;
                ra.addToLvActivitySummary(totalLasl);
                if( ra.getSuborgId()==0 )
                    ra.setName( MessageFactory.getStringMessage(locale, "g.None" ) );
                else
                    ra.setSuborg(suborgMap.get( ra.getSuborgId()));
            }
            if( hasData )
            {
                Collections.sort(lasl);
                lasl.add( totalLasl );
            }
            else
                lasl.clear();
            
            
            // Next, test keys created, by suborg.
            sqlStr = " SELECT suborgid, batteryid, COUNT(1) FROM testkey WHERE orgid=" + orgId + " " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY suborgid,batteryid " +
                    " UNION ALL " +
                    " SELECT suborgid, batteryid, COUNT(1) FROM testkeyarchive WHERE orgid=" + orgId + " " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY suborgid,batteryid ";

            rs = stmt.executeQuery( sqlStr );
            while( rs.next() )
            {
                tempInt2 = rs.getInt( 1 );

                if( suborgOnly && tempInt2 != suborgId )
                    continue;

                 if( !suborgIdSet.contains( tempInt2 ) )
                     continue;
                 
                tempInt3 = rs.getInt( 2 );
                 
                tempInt = rs.getInt( 3 ) ;
                tempInteger = testKeysCreatedMap.get( tempInt2 );
                totalTestKeysCreated += tempInt;
                
                if( tempInt3>0 )
                    totalBatteriesCreated += tempInt;

                if( tempInteger == null )
                    tempInteger = 0;

                testKeysCreatedMap.put( tempInt2, tempInt + tempInteger );

                if( tempInt3>0 )
                {
                    tempInteger = batteriesCreatedMap.get( tempInt2 );
                    if( tempInteger == null )
                        tempInteger = 0;
                    batteriesCreatedMap.put( tempInt2, tempInt + tempInteger );
                }                    
            }
            rs.close();

            // Next, test events completed, by suborg.
            sqlStr = "SELECT suborgid, COUNT(1) FROM testevent WHERE orgid=" + orgId + " AND testeventstatustypeid>1 AND testeventstatustypeid NOT IN (201,202) " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY suborgid " +
                    " UNION ALL " +
                    " SELECT suborgid, COUNT(1) FROM testeventarchive WHERE orgid=" + orgId + " AND testeventstatustypeid>1 AND testeventstatustypeid NOT IN (201,202) " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY suborgid ";

            rs = stmt.executeQuery( sqlStr );

            while( rs.next() )
            {
                tempInt2 = rs.getInt( 1 );

                if( suborgOnly && tempInt2 != suborgId )
                    continue;

                 if( !suborgIdSet.contains( tempInt2 ) )
                     continue;

                // tempInt3 = rs.getInt( 2 );
                 
                tempInt = rs.getInt( 2 ) ;
                tempInteger = testKeysFinishedMap.get( tempInt2 );
                totalTestKeysFinished += tempInt;

                //if( tempInt3>0 )
                //    totalBatteriesFinished += tempInt;
                
                if( tempInteger == null )
                    tempInteger = 0;

                testKeysFinishedMap.put( tempInt2, tempInt + tempInteger );
                
                //if( tempInt3>0 )
                //{
                 //   tempInteger = batteriesFinishedMap.get( tempInt2 );
                //    if( tempInteger == null )
                //        tempInteger = 0;
                //    batteriesFinishedMap.put( tempInt2, tempInt + tempInteger );
                //}                                    
            }

            rs.close();

            // Test Keys Started
            sqlStr = "SELECT suborgid, COUNT(1) FROM testevent WHERE orgid=" + orgId + " AND testeventstatustypeid>=1 " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY suborgid " +
                    " UNION ALL " +
                    " SELECT suborgid, COUNT(1) FROM testeventarchive WHERE orgid=" + orgId + " AND testeventstatustypeid>=1 " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY suborgid ";

            rs = stmt.executeQuery( sqlStr );

            while( rs.next() )
            {
                tempInt2 = rs.getInt( 1 );

                if( suborgOnly && tempInt2 != suborgId )
                    continue;

                 if( !suborgIdSet.contains( tempInt2 ) )
                     continue;
                
                // tempInt3 = rs.getInt( 2 );
                tempInt = rs.getInt( 2 ) ;
                tempInteger = testKeysStartedMap.get( tempInt2 );
                totalTestKeysStarted += tempInt;

                //if( tempInt3>0 )
                //    totalBatteriesStarted += tempInt;
                
                
                if( tempInteger == null )
                    tempInteger = 0;

                testKeysStartedMap.put( tempInt2, tempInt + tempInteger );

                //if( tempInt3>0 )
                //{
                //    tempInteger = batteriesStartedMap.get( tempInt2 );
                //    if( tempInteger == null )
                //        tempInteger = 0;
                //    batteriesStartedMap.put( tempInt2, tempInt + tempInteger );
                //}                                    
            }

            rs.close();

            /*
            for( Integer sid : testKeysFinishedMap.keySet() )
            {
                tempInteger = testKeysFinishedMap.get( sid );

                tempInteger2 = testKeysStartedMap.get( sid );

                if( tempInteger2 == null )
                    tempInteger2 = new Integer(0);

                testKeysStartedMap.put( sid, tempInteger + tempInteger2 );

                // totalTestKeysStarted += tempInteger;
            }
            */


            // User status Types
            sqlStr = " SELECT tk.suborgid, u.usercompanystatustypeid, COUNT(1) FROM testevent AS tk INNER JOIN xuser AS u ON tk.userid=u.userid WHERE tk.orgid=" + orgId + " AND tk.testeventstatustypeid >1 AND tk.testeventstatustypeid<>201 " +  suborgStrTk + " AND tk.startdate>='" + sDate.toString() + "' AND tk.startdate<='" + eDate.toString() + "' GROUP BY tk.suborgid, u.usercompanystatustypeid " +
                    " UNION ALL " +
                    " SELECT tk.suborgid, u.usercompanystatustypeid, COUNT(1) FROM testeventarchive AS tk INNER JOIN xuser AS u ON tk.userid=u.userid WHERE tk.orgid=" + orgId + " AND tk.testeventstatustypeid >1 AND tk.testeventstatustypeid<>201 " +  suborgStrTk + " AND tk.startdate>='" + sDate.toString() + "' AND tk.startdate<='" + eDate.toString() + "' GROUP BY tk.suborgid, u.usercompanystatustypeid ";

            rs = stmt.executeQuery( sqlStr );

            while( rs.next() )
            {
                // suborgid
                tempInt2 = rs.getInt( 1 );

                 if( !suborgIdSet.contains( tempInt2 ) )
                     continue;

                if( suborgOnly && tempInt2 != suborgId )
                    continue;

                // usercompanystatustype
                tempInt = rs.getInt( 2 );

                // count
                tempInt3 = rs.getInt( 3 );

                userCompanyStatusMap = suborgUserCompanyStatusMap.get( tempInt2 );

                if( userCompanyStatusMap == null )
                {
                    userCompanyStatusMap = new HashMap<>();
                    suborgUserCompanyStatusMap.put( tempInt2, userCompanyStatusMap );
                }

                tempInteger = userCompanyStatusMap.get( tempInt );

                if( tempInteger == null )
                    tempInteger =0;

                userCompanyStatusMap.put( tempInt, tempInt3 + tempInteger );
            }

            rs.close();



            // CREDITS USED
            if( !isUnlimited && !isResultCredit )
            {
                sqlStr = "SELECT suborgid, SUM(creditsused) FROM testevent WHERE orgid=" + orgId + " AND testeventstatustypeid>=0 " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY suborgid " +
                        " UNION ALL " +
                        " SELECT suborgid, SUM(creditsused) FROM testeventarchive WHERE orgid=" + orgId + " AND testeventstatustypeid>=0 " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY suborgid ";

                rs = stmt.executeQuery( sqlStr );

                while( rs.next() )
                {
                    tempInt2 = rs.getInt( 1 );

                    if( suborgOnly && tempInt2 != suborgId )
                        continue;

                    if( !suborgIdSet.contains( tempInt2 ) )
                        continue;

                    tempInt = rs.getInt( 2 ) ;
                    tempInteger = creditsUsedMap.get( tempInt2 );
                    totalCreditsUsed += tempInt;

                    if( tempInteger == null )
                        tempInteger = 0;

                    creditsUsedMap.put( tempInt2, tempInt + tempInteger );
                }

                rs.close();

                // LogService.logIt(  "DashboardFacade.getDataMap() " + sqlStr );
            }

            if( isResultCredit )
            {
                Map<Integer,Set<String>> smp = new HashMap<>();
                
                // set of creditid-creditindex
                Set<String> cm;
                String creditStr;  // creditid-creditindex
                
                sqlStr = "SELECT suborgid, creditid,creditindex FROM testkey WHERE orgid=" + orgId + " AND creditid>0 " +  suborgStr + " AND lastaccessdate IS NOT NULL AND lastaccessdate>='" + sDate.toString() + "' AND lastaccessdate<='" + eDate.toString() + "' "  +
                        " UNION ALL " +
                        "SELECT suborgid, creditid,creditindex FROM testkeyarchive WHERE orgid=" + orgId + " AND creditid>0 " +  suborgStr + " AND lastaccessdate IS NOT NULL AND lastaccessdate>='" + sDate.toString() + "' AND lastaccessdate<='" + eDate.toString() + "' " + 
                        " UNION ALL " +
                        "SELECT suborgid, creditid,creditindex FROM lvcall WHERE orgid=" + orgId + " AND creditid>0 " +  suborgStr + " AND startdate IS NOT NULL AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' " + 
                        " UNION ALL " +
                        "SELECT suborgid, creditid,creditindex FROM rccheck WHERE orgid=" + orgId + " AND creditid>0 " +  suborgStr + " AND lastupdate IS NOT NULL AND lastupdate>='" + sDate.toString() + "' AND lastupdate<='" + eDate.toString() + "' ";

                rs = stmt.executeQuery( sqlStr );

                while( rs.next() )
                {
                    tempInt2 = rs.getInt( 1 );

                    if( suborgOnly && tempInt2 != suborgId )
                        continue;

                    if( !suborgIdSet.contains( tempInt2 ) )
                        continue;

                    tempInt = rs.getInt( 2 ) ;
                    tempInt3 = rs.getInt(3);
                    
                    creditStr = tempInt + "-" + tempInt3;
                    
                    cm = smp.get(tempInt2);
                    if( cm==null )
                    {
                        cm = new HashSet<>();
                        smp.put(tempInt2, cm);
                    }
                    cm.add(creditStr);
                }
                rs.close();
                
                totalCreditsUsed=0;
                for( Integer subid : smp.keySet() )
                {
                    cm = smp.get( subid );
                    tempInteger = creditsUsedMap.get( subid );
                    if( tempInteger == null )
                        tempInteger = 0;
                    totalCreditsUsed += cm.size();
                    creditsUsedMap.put( subid, cm.size() + tempInteger );
                }                
                // LogService.logIt(  "DashboardFacade.getDataMap() " + sqlStr );
            }
            
            
            // product id started
            sqlStr = "SELECT productid, COUNT(1) FROM testevent WHERE orgid=" + orgId + " AND testeventstatustypeid>=1 " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY productid " +
                    " UNION ALL " +
                    " SELECT productid, COUNT(1) FROM testeventarchive WHERE orgid=" + orgId + " AND testeventstatustypeid>=1 " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY productid ";

            rs = stmt.executeQuery( sqlStr );

            while( rs.next() )
            {
                tempInt2 = rs.getInt( 1 );

                tempInt = rs.getInt( 2 ) ;
                //tempInt3 = rs.getInt( 3 );
                tempInteger = productIdStartMap.get( tempInt2 );

                if( tempInteger == null )
                    tempInteger = 0;

                productIdStartMap.put( tempInt2, tempInt + tempInteger );
                productIdSet.add( tempInt2 );
            }

            rs.close();

            // LogService.logIt(  "DashboardFacade.getDataMap() " + sqlStr );

            if( !isUnlimited && !isResultCredit )
            {
                // product id credits used (legacy)
                sqlStr = "SELECT productid, SUM(creditsused) FROM testevent WHERE orgid=" + orgId + " AND testeventstatustypeid>=0 " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY productid " +
                        " UNION ALL " +
                        " SELECT productid, SUM(creditsused) FROM testeventarchive WHERE orgid=" + orgId + " AND testeventstatustypeid>=0 " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY productid ";

                rs = stmt.executeQuery( sqlStr );

                while( rs.next() )
                {
                    tempInt2 = rs.getInt( 1 );

                    // tempInt = rs.getInt( 2 ) ;
                    tempInt3 = rs.getInt( 2 );

                    tempInteger = productIdCreditsUsedMap.get( tempInt2 );

                    if( tempInteger == null )
                        tempInteger = 0;

                    productIdCreditsUsedMap.put( tempInt2, tempInt3 + tempInteger );

                    productIdSet.add( tempInt2 );
                }

                rs.close();
                // LogService.logIt(  "DashboardFacade.getDataMap() " + sqlStr );
            }

             // Next, product id test keys created.
            sqlStr = " SELECT productid, COUNT(1) FROM testkey WHERE orgid=" + orgId + " " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY productid " +
                    " UNION ALL " +
                    " SELECT productid, COUNT(1) FROM testkeyarchive WHERE orgid=" + orgId + " " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY productid ";

            rs = stmt.executeQuery( sqlStr );

            while( rs.next() )
            {
                tempInt2 = rs.getInt( 1 );

                tempInt = rs.getInt( 2 ) ;
                tempInteger = productIdTestKeysCreatedMap.get( tempInt2 );

                if( tempInteger == null )
                    tempInteger = 0;

                // Product Id is the key. Created is the value
                productIdTestKeysCreatedMap.put( tempInt2, tempInt + tempInteger );
            }

            rs.close();


            
            // product id completed and scored - test event completions and average score
            sqlStr = "SELECT productid, COUNT(1), AVG(overallscore) FROM testevent WHERE orgid=" + orgId + " AND testeventstatustypeid >=110 AND testeventstatustypeid NOT IN (203,204) " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY productid " +
                    " UNION ALL " +
                    " SELECT productid, COUNT(1), AVG(overallscore) FROM testeventarchive WHERE orgid=" + orgId + " AND testeventstatustypeid >=110  AND testeventstatustypeid NOT IN (203,204) " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY productid ";

            rs = stmt.executeQuery( sqlStr );

            //int total = 0;
            //float weighted = 0;
            float avg;

            while( rs.next() )
            {
                tempInt2 = rs.getInt( 1 );
                tempFloatVal = productIdAverageMap.get( tempInt2 );

                if( tempFloatVal == null )
                    tempFloatVal = (0f);

                tempInt = rs.getInt( 2 );

                tempFloat = rs.getFloat( 3 ) ;

                productIdAverageMap.put( tempInt2, tempInt * tempFloat + tempFloatVal );

                tempInteger = productIdTotalScoredMap.get( tempInt2 );

                if( tempInteger == null )
                    tempInteger =0;

                productIdTotalScoredMap.put( tempInt2, tempInteger + tempInt );

                tempInteger = productIdCompletionMap.get( tempInt2 );

                if( tempInteger == null )
                    tempInteger = 0;
                productIdCompletionMap.put( tempInt2, tempInt + tempInteger );                
                
                productIdSet.add( tempInt2 );
            }

            rs.close();

             // product id - batteries started.
            sqlStr = " SELECT productid, COUNT(1) FROM testkey WHERE orgid=" + orgId + " AND statustypeid>=1 AND batteryid>0 " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY productid " +
                    " UNION ALL " +
                    " SELECT productid, COUNT(1) FROM testkeyarchive WHERE orgid=" + orgId + " AND statustypeid>=1 AND batteryid>0 " +  suborgStr + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "' GROUP BY productid ";

            rs = stmt.executeQuery( sqlStr );

            while( rs.next() )
            {
                // product id
                tempInt2 = rs.getInt( 1 );

                // start dount
                tempInt = rs.getInt( 2 ) ;
                //tempInt3 = rs.getInt( 3 );
                tempInteger = productIdStartMap.get( tempInt2 );

                if( tempInteger == null )
                    tempInteger = 0;

                productIdStartMap.put( tempInt2, tempInt + tempInteger );

                productIdSet.add( tempInt2 );                
            }

            rs.close();
            
            
            // product id - Battery Product completion and score
            sqlStr = "SELECT tk.productid, COUNT(1) FROM testkey tk WHERE tk.orgid=" + orgId + " AND tk.batteryid>0 AND tk.statustypeid >=100 AND tk.statustypeid<=131 " +  suborgStrTk + " AND tk.startdate>='" + sDate.toString() + "' AND tk.startdate<='" + eDate.toString() + "' GROUP BY tk.productid " + 
                    " UNION ALL " +
                    "SELECT tk.productid, COUNT(1) FROM testkeyarchive tk WHERE tk.orgid=" + orgId + " AND tk.batteryid>0 AND tk.statustypeid >=100 AND tk.statustypeid<=131 " +  suborgStrTk + " AND tk.startdate>='" + sDate.toString() + "' AND tk.startdate<='" + eDate.toString() + "' GROUP BY tk.productid ";            
            
            rs = stmt.executeQuery( sqlStr );

            while( rs.next() )
            {
                // product id
                tempInt2 = rs.getInt( 1 );
                
                tempFloatVal = productIdAverageMap.get( tempInt2 );

                if( tempFloatVal == null )
                    tempFloatVal = (0f);

                // count
                tempInt = rs.getInt( 2 );

                // average
                tempFloat = getAverageBatteryScoreForProductId( tempInt2, orgId, suborgStrBs, sDate, eDate );

                productIdAverageMap.put( tempInt2, tempInt * tempFloat + tempFloatVal );

                tempInteger = productIdTotalScoredMap.get( tempInt2 );

                if( tempInteger == null )
                    tempInteger =0;

                productIdTotalScoredMap.put( tempInt2, tempInteger + tempInt );
                
                tempInteger = productIdCompletionMap.get( tempInt2 );

                if( tempInteger == null )
                    tempInteger = 0;

                productIdCompletionMap.put( tempInt2, tempInt + tempInteger );
                productIdSet.add( tempInt2 );                
            }

            rs.close();
            
            
            // all products - Average Score
            for( Integer productId : productIdTotalScoredMap.keySet() )
            {
                if( productIdAverageMap.get( productId )==null  )
                {
                    LogService.logIt( "DashboardFacade.productIdAverageMap cannot find productId=" + productId + " in productIdAverageMap" );
                    continue;
                }

                avg = productIdAverageMap.get( productId ) / ((float) (productIdTotalScoredMap.get( productId)) );

                productIdAverageMap.put( productId, avg);
            }

            StringBuilder pids = new StringBuilder();

            for( Integer pid : productIdSet)
            {
                if( pids.length()>0 )
                    pids.append( "," );

                pids.append( pid );
            }

            if( !productIdSet.isEmpty() )
            {
                sqlStr = "SELECT productid, name FROM product WHERE productid IN (" + pids.toString() +  ") ORDER BY name ";

                rs = stmt.executeQuery( sqlStr );

                while( rs.next() )
                {
                    tempInt = rs.getInt(1);
                    productMap.put( tempInt, getProduct( tempInt ) );
                }

                rs.close();
            }

            ProductActivitySummary pas;

            for( Product pp : productMap.values() )
            {
                if( pp.getProductType().getIsAnyBattery() )
                    pp.setBattery( getBattery( pp.getIntParam1() ) );
                pas = new ProductActivitySummary( pp );
                
                if( pp.getProductType().getIsAnyBattery() )
                    basl.add(pas);
                else
                    pasl.add(pas);
            }

            Collections.sort( pasl );
            Collections.sort( basl );
            
            // Set<Long> tkids = new HashSet<>();
            StringBuilder tkidstr;
            
            int authorized;
            int accessed;
            int startTest;
            int completeTest;
            // int completeBattery;
            
            // Next work on dropoff
            for( Integer pid : productIdTestKeysCreatedMap.keySet() )
            {
                p = getProduct(pid);
                
                if( p==null ) // || p.getProductType().getIsAnyBattery() )
                    continue;
                
                // authorized
                authorized = 0;
                accessed = 0;
                startTest = 0;
                completeTest = 0;
                // completeBattery = 0;
                
                
                // Get the test keys
                // tkids.clear();
                
                tkidstr  = new StringBuilder();
                
                // test keys authorized
                sqlStr = "( SELECT testkeyid FROM testkey WHERE orgid=" + orgId + " " +  suborgStr + " AND productid=" + pid + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "') " + 
                        " UNION ALL " +
                        "( SELECT testkeyid FROM testkeyarchive WHERE orgid=" + orgId + " " +  suborgStr + " AND productid=" + pid + " AND startdate>='" + sDate.toString() + "' AND startdate<='" + eDate.toString() + "') LIMIT 1000 ";

                // get authorized
                rs = stmt.executeQuery( sqlStr );
                while( rs.next() )
                {
                    authorized++;
                    if( tkidstr.length()>0 )
                        tkidstr.append( "," );
                    tkidstr.append(rs.getLong(1));
                    
                    if( authorized>1000 )
                        break;
                }
                rs.close();
                
                // Next, test keys accessed count
                sqlStr = " SELECT COUNT(1) FROM testkey WHERE testkeyid IN (" + tkidstr.toString() + ") AND statustypeid>=1 " + 
                        " UNION ALL " +
                        " SELECT COUNT(1)  FROM testkeyarchive WHERE testkeyid IN (" + tkidstr.toString() + ") AND statustypeid>=1 ";
                
                
                rs = stmt.executeQuery( sqlStr );
                while( rs.next() )
                {
                    accessed += rs.getInt(1);
                    
                }
                rs.close();

                // batteries
                if( p.getProductType().getIsAnyBattery() )
                {          
                    // starts = accesses for batteries
                    startTest = accessed;                    

                    // Next, get the batteries completed count
                    sqlStr = " SELECT COUNT(1) FROM testkey WHERE testkeyid IN (" + tkidstr.toString() + ") AND statustypeid>=100 AND statustypeid<=131 "  + 
                            " UNION ALL " +
                            " SELECT COUNT(1)  FROM testkeyarchive WHERE testkeyid IN (" + tkidstr.toString() + ") AND statustypeid>=100 AND statustypeid<=131 ";

                    rs = stmt.executeQuery( sqlStr );
                    while( rs.next() )
                    {
                        completeTest += rs.getInt(1);
                    }
                    rs.close();
                    
                }
                
                // test events
                else 
                {
                    // Next, get the started count
                    sqlStr = " SELECT COUNT(1) FROM testevent WHERE testkeyid IN (" + tkidstr.toString() + ") AND testeventstatustypeid>=1 "  + 
                            " UNION ALL " +
                            " SELECT COUNT(1)  FROM testeventarchive WHERE testkeyid IN (" + tkidstr.toString() + ") AND testeventstatustypeid>=1 ";

                    rs = stmt.executeQuery( sqlStr );
                    while( rs.next() )
                    {
                        startTest += rs.getInt(1);
                    }
                    rs.close();

                    // Next, get the completed count
                    sqlStr = " SELECT COUNT(1) FROM testevent WHERE testkeyid IN (" + tkidstr.toString() + ") AND testeventstatustypeid>=100 AND percentcomplete>=100 "  + 
                            " UNION ALL " +
                            " SELECT COUNT(1)  FROM testeventarchive WHERE testkeyid IN (" + tkidstr.toString() + ") AND testeventstatustypeid>=100 AND percentcomplete>=100 ";

                    rs = stmt.executeQuery( sqlStr );
                    while( rs.next() )
                    {
                        completeTest += rs.getInt(1);
                    }
                    rs.close();
                }
                
                
                pas = new ProductActivitySummary( p );
                pas.setTestKeys(authorized);
                pas.setStarts(accessed);
                pas.setTestEventStarts(startTest);
                pas.setCompletions(completeTest);
                
                if( p.getProductType().getIsAnyBattery() )
                {
                    paslBatt.add(pas);
                    pas.addToProductActivitySummary(totalPasBatt);
                }
                else
                {
                    pasltk.add(pas);
                    pas.addToProductActivitySummary(totalPasTk);
                }
            }

        }
        catch( Exception e )
        {
            LogService.logIt( e, "DashboardFacade.getDataMap() " + sqlStr );
            throw new STException( e );
        }
        
        // Add to bottom of this list.
        pasltk.add( totalPasTk );
        paslBatt.add( totalPasBatt );
        

        boolean hasData = false;
        
        for( SuborgActivitySummary sas : sasl )
        {
            // The full org + all suborgs
            if( sas.getSuborg().getSuborgId()< 0 )
            {
                sas.setUserLogins( totalLogons );
                sas.setTestKeysCreated( totalTestKeysCreated );
                sas.setTestKeysStarted( totalTestKeysStarted );
                sas.setTestKeysCompleted( totalTestKeysFinished );
                sas.setBatteriesCreated( totalBatteriesCreated );
                sas.setBatteriesStarted( totalBatteriesStarted );
                sas.setBatteriesCompleted( totalBatteriesFinished );
                sas.setCreditsUsed( totalCreditsUsed );

                //userCompanyStatusMap = suborgUserCompanyStatusMap.get( sas.getSuborg().getSuborgId() );

                //if( userCompanyStatusMap == null )
                userCompanyStatusMap = new HashMap<>();

                // For each suborg, add sum
                for( Map<Integer,Integer> ucsm : suborgUserCompanyStatusMap.values() )
                {
                    for( Integer ucsid : ucsm.keySet() )
                    {
                        // current value
                        tempInteger = userCompanyStatusMap.get(ucsid);

                        if( tempInteger==null )
                            tempInteger = 0;

                        userCompanyStatusMap.put(ucsid, tempInteger + ucsm.get(ucsid ) );
                    }
                }

                sas.setUserCompanyStatusMap(userCompanyStatusMap);

            }

            else
            {
                tempInteger = logonsByOrg.get( sas.getSuborg().getSuborgId() );
                if( tempInteger != null )
                    sas.setUserLogins( tempInteger );

                tempInteger = testKeysCreatedMap.get( sas.getSuborg().getSuborgId() );
                if( tempInteger != null )
                    sas.setTestKeysCreated(tempInteger );

                tempInteger = testKeysStartedMap.get( sas.getSuborg().getSuborgId() );
                if( tempInteger != null )
                    sas.setTestKeysStarted(tempInteger );

                tempInteger = testKeysFinishedMap.get( sas.getSuborg().getSuborgId() );
                if( tempInteger != null )
                    sas.setTestKeysCompleted(tempInteger );

                tempInteger = batteriesCreatedMap.get( sas.getSuborg().getSuborgId() );
                if( tempInteger != null )
                    sas.setBatteriesCreated(tempInteger );

                tempInteger = batteriesStartedMap.get( sas.getSuborg().getSuborgId() );
                if( tempInteger != null )
                    sas.setBatteriesStarted(tempInteger );

                tempInteger = batteriesFinishedMap.get( sas.getSuborg().getSuborgId() );
                if( tempInteger != null )
                    sas.setBatteriesCompleted(tempInteger );
                                
                tempInteger = creditsUsedMap.get( sas.getSuborg().getSuborgId() );
                if( tempInteger != null )
                    sas.setCreditsUsed(tempInteger );

                userCompanyStatusMap = suborgUserCompanyStatusMap.get( sas.getSuborg().getSuborgId() );

                sas.setUserCompanyStatusMap(userCompanyStatusMap);
            }
        }

        ListIterator<SuborgActivitySummary> li = sasl.listIterator();
        while( li.hasNext() )
        {
            sa = li.next();

            if( sa.getSuborg().getSuborgId()==0 )
            {
                if( !sa.getHasData() )
                    li.remove();
                //if( sa.getTestKeysCreated()<=0 &&
                //    sa.getTestKeysCompleted()<=0 &&
                //    sa.getTestEventsStarted()<=0 &&
                //    sa.getBatteriesCreated()<=0 &&
                //    sa.getBatteriesStarted()<=0 &&   
                //    sa.getBatteriesCompleted()<=0 &&
                //    sa.getCreditsUsed() <= 0 )
                //    li.remove();
            }
        }

        for( ProductActivitySummary pas : pasl )
        {
            tempInteger = productIdTestKeysCreatedMap.get( pas.getProduct().getProductId() );
            if( tempInteger != null )
                pas.setTestKeys(tempInteger );

            tempInteger = productIdStartMap.get( pas.getProduct().getProductId() );
            if( tempInteger != null )
                pas.setStarts(tempInteger );

            tempInteger = productIdCompletionMap.get( pas.getProduct().getProductId() );
            if( tempInteger != null )
                pas.setCompletions(tempInteger );

            tempFloatVal = productIdAverageMap.get( pas.getProduct().getProductId() );
            if( tempFloatVal != null )
                pas.setAverageScore(tempFloatVal );

            tempInteger = productIdCreditsUsedMap.get( pas.getProduct().getProductId() );
            if( tempInteger != null )
                pas.setCreditsUsed( tempInteger );
        }

        for( ProductActivitySummary pas : basl )
        {
            tempInteger = productIdTestKeysCreatedMap.get( pas.getProduct().getProductId() );
            if( tempInteger != null )
                pas.setTestKeys(tempInteger );

            tempInteger = productIdStartMap.get( pas.getProduct().getProductId() );
            if( tempInteger != null )
                pas.setStarts(tempInteger );

            tempInteger = productIdCompletionMap.get( pas.getProduct().getProductId() );
            if( tempInteger != null )
                pas.setCompletions(tempInteger );

            tempFloatVal = productIdAverageMap.get( pas.getProduct().getProductId() );
            if( tempFloatVal != null )
                pas.setAverageScore(tempFloatVal );

            tempInteger = productIdCreditsUsedMap.get( pas.getProduct().getProductId() );
            if( tempInteger != null )
                pas.setCreditsUsed( tempInteger );
        }
        
        ListIterator<ProductActivitySummary> iter = basl.listIterator();
        while( iter.hasNext() )
        {
            if( !iter.next().getHasData() )
                iter.remove();
        }
        
        iter = paslBatt.listIterator();
        while( iter.hasNext() )
        {
            if( !iter.next().getHasData() )
                iter.remove();
        }
        
        
        return out;

    }


    
    private float getAverageBatteryScoreForProductId( int productId, int orgId, String suborgStrBs, java.sql.Date sDate, java.sql.Date eDate ) throws Exception
    {
        DataSource pool = (DataSource) new InitialContext().lookup( "jdbc/tm2mirror" );

        if( pool == null )
            throw new Exception( "Can not find Datasource" );

        String sqlStr = "SELECT AVG(bs.score) FROM batteryscore bs INNER JOIN testkeyarchive tk ON tk.testkeyid=bs.testkeyid WHERE tk.orgid=" + orgId + " AND tk.productid=" + productId  + " AND tk.statustypeid >=110 AND tk.statustypeid<=131 " +  suborgStrBs + " AND tk.startdate>='" + sDate.toString() + "' AND tk.startdate<='" + eDate.toString() + "' ";

        try (Connection con = pool.getConnection(); Statement stmt = con.createStatement() )
        {
            con.setTransactionIsolation( Connection.TRANSACTION_READ_UNCOMMITTED );

            ResultSet rs = stmt.executeQuery( sqlStr );
            if( rs.next() )
            {
                return rs.getFloat(1);
            }
            rs.close();
        }
        catch( Exception e )
        {
            LogService.logIt( e, "DashboardFacade.getAverageBatteryScoreForProductId() " + sqlStr );
            throw new STException( e );
        }
        
        return 0;
        
    }



        /**
     *
     * returns:
     *
     * product product
     * startdate
     * enddate
     * org
     * suborg - only if suborg only
     * 
     * total  = total # of Test Keys
     * notstarted = not started
     * processstarted = # Test keys in started status
     * teststarted = # test keys that have started one TestEvent (processcomplete>0)
     * suspended = # Test keys that are suspended  one TestEvent (processcomplete>0)
     * expired = # Test keys that are expired 
     * completed = test keys completed

     */
    public Map<String, Object> getDashboard2DataMap( Product product, 
                                                     Org org,
                                                     int suborgId,
                                                     Date startDate,
                                                     Date endDate,
                                                     Locale locale ) throws Exception
    {
        Map<String, Object> out = new HashMap<>();

        out.put( "startdate", startDate );
        out.put( "enddate", endDate );

        java.sql.Timestamp sTs = new java.sql.Timestamp( startDate.getTime() );
        java.sql.Timestamp eTs = new java.sql.Timestamp( endDate.getTime() );
        //java.sql.Date sDate = new java.sql.Date( startDate.getTime() );
        //java.sql.Date eDate = new java.sql.Date( endDate.getTime() );

        int orgId = org.getOrgId();

        UserFacade userFacade = UserFacade.getInstance();

        Suborg suborg = suborgId>0 ? userFacade.getSuborg(suborgId) : null;

        out.put( "product", product );
        out.put( "org", org );
        out.put( "suborg", suborg );

        DataSource pool = (DataSource) new InitialContext().lookup( "jdbc/tm2mirror" );
        if( pool == null )
            throw new Exception( "Can not find Datasource" );

        String wStr = " WHERE tk.orgid=" + orgId + " AND tk.productid=" + product.getProductId();
        if( suborgId>=0 )
            wStr += " AND tk.suborgid=" + suborgId + " ";
        wStr += " AND tk.startdate>='" + sTs.toString() + "' AND tk.startdate<='" + eTs.toString() + "' ";
        
        String wStr2 = " AND tk.statustypeid NOT IN (" + TestKeyStatusType.STARTED.getTestKeyStatusTypeId() + "," + TestKeyStatusType.STOPPED_PROCTOR.getTestKeyStatusTypeId() + "," + TestKeyStatusType.EXPIRED.getTestKeyStatusTypeId() + "," + TestKeyStatusType.DEACTIVATED.getTestKeyStatusTypeId() + ") GROUP BY tk.statustypeid ";
        
        String sStr = "SELECT tk.statustypeid, COUNT(1) AS 'ct' FROM testkey tk ";
        String sStr2 = "SELECT tk.statustypeid, COUNT(1) AS 'ct' FROM testkeyarchive tk ";
                
        String sqlStr = "(" + sStr + wStr + wStr2 + ") UNION ALL (" + sStr2 + wStr + wStr2 + ") ";
 
        int count;
        int total = 0;
        int processStarted = 0;
        int testStarted = 0;
        int notStarted = 0;
        int expired = 0;
        int suspended = 0;
        int completed = 0;
        int incompletetestnotstarted=0;
        int incompleteteststarted=0;
        TestKeyStatusType tkst;
        
        Set<Long> tkSet = new HashSet<>();
        
        try (Connection con = pool.getConnection(); Statement stmt = con.createStatement() )
        {
            int statusTypeId;
            con.setTransactionIsolation( Connection.TRANSACTION_READ_UNCOMMITTED );

            LogService.logIt( "DashboardFacade.getDashboard2DataMap() AAA " + sqlStr );
            ResultSet rs = stmt.executeQuery( sqlStr );
            while( rs.next() )
            {
                statusTypeId=rs.getInt(1);
                count=rs.getInt(2);
                if( count<=0 )
                    continue;
                
                tkst = TestKeyStatusType.getValue(statusTypeId);                 
                
                total+= count;
                 
                if( tkst.getIsActive() )
                {
                    notStarted += count;
                    continue;
                }
                
                processStarted += count;
                
                if( tkst.getIsFinishedByUser() )
                {
                    testStarted += count;
                    completed += count;
                }                     
            }
            rs.close();
            
            sStr = "SELECT tk.testkeyid,tk.statustypeid FROM testkey tk "; // testevent te INNER JOIN ON te.testkeyid=tk.testkeyid ";
            sStr2 = "SELECT tk.testkeyid,tk.statustypeid FROM testkeyarchive tk "; // INNER JOIN testkeyarchive tk ON te.testkeyid=tk.testkeyid ";

            wStr = " WHERE tk.orgid=" + orgId + " AND tk.productid=" + product.getProductId();
            if( suborgId>=0 )
                wStr += " AND tk.suborgid=" + suborgId + " ";
            wStr += " AND tk.startdate>='" + sTs.toString() + "' AND tk.startdate<='" + eTs.toString() + "' " + 
                    " AND  tk.statustypeid IN (" + TestKeyStatusType.STARTED.getTestKeyStatusTypeId() + "," + TestKeyStatusType.STOPPED_PROCTOR.getTestKeyStatusTypeId() + "," + TestKeyStatusType.EXPIRED.getTestKeyStatusTypeId() + ") " + 
                    " ";                    

            sqlStr = "(" + sStr + wStr + ") UNION ALL (" + sStr2 + wStr + ") ";

            LogService.logIt( "DashboardFacade.getDashboard2DataMap() BBB " + sqlStr );
            
            long testKeyId;
            rs = stmt.executeQuery( sqlStr );
            while( rs.next() )
            {
                testKeyId=rs.getLong(1);
                tkst = TestKeyStatusType.getValue(rs.getInt(2));                 
                
                total++;
                
                // the test questions were started
                if( getStartedTestEvent(testKeyId) )
                {
                    testStarted++;                    
                    processStarted++; 
                    incompleteteststarted++;
                }

                // test not started, no other status.
                else if( tkst.getStarted() )
                {
                    processStarted++;
                    incompletetestnotstarted++;
                }
                
                // proctor stop because never started
                else if( tkst.getIsProctorStop() )
                {
                    processStarted++;
                    incompletetestnotstarted++;
                }
                
                // must be expired but never started
                else if( tkst.getIsExpired() )
                {
                    notStarted++;
                }

                // must count separately because test can be started or not started.
                if( tkst.getIsProctorStop() )
                {
                    suspended++;
                }                
                
                // must count separately because test can be started or not started.
                if( tkst.getIsExpired() )
                    expired++;
            }
            rs.close();
            
            
            out.put( "total", total);            
            out.put( "notstarted", notStarted );
            out.put( "processstarted", processStarted);
            out.put( "teststarted", testStarted);
            out.put( "completed", completed);
            out.put( "suspended", suspended);
            out.put( "expired", expired);
            out.put( "incompletetestnotstarted", incompletetestnotstarted);
            out.put( "incompleteteststarted", incompleteteststarted);

        }
        catch( Exception e )
        {
            LogService.logIt( e, "DashboardFacade.getDashboard2DataMap() " + sqlStr );
            throw e;
        }
        
        return out;
    }


    /**
     *
     * returns:
     *
     * product product
     * startdate
     * enddate
     * org
     * suborg - only if suborg only
     * 
     * total  = total # of Test Keys
     * notstarted = not started
     * processstarted = # Test keys in started status
     * teststarted = # test keys that have started one TestEvent (processcomplete>0)
     * suspended = # Test keys that are suspended  one TestEvent (processcomplete>0)
     * expired = # Test keys that are expired 
     * completed = test keys completed

     *
    public Map<String, Object> getDashboard2DataMap( Product product, 
                                                    Org org,
                                                    int suborgId,
                                                    Date startDate,
                                                    Date endDate,
                                                    Locale locale ) throws Exception
    {
        Map<String, Object> out = new HashMap<>();

        out.put( "startdate", startDate );
        out.put( "enddate", endDate );

        java.sql.Date sDate = new java.sql.Date( startDate.getTime() );
        java.sql.Date eDate = new java.sql.Date( endDate.getTime() );

        int orgId = org.getOrgId();

        UserFacade userFacade = UserFacade.getInstance();

        Suborg suborg = suborgId>0 ? userFacade.getSuborg(suborgId) : null;

        out.put( "org", org );
        out.put( "suborg", suborg );

        DataSource pool = (DataSource) new InitialContext().lookup( "jdbc/tm2mirror" );
        if( pool == null )
            throw new Exception( "Can not find Datasource" );

        String sStr = "SELECT tk.testkeyid, tk.statustypeid FROM testkey tk ";
        String sStr2 = "SELECT tk.testkeyid, tk.statustypeid FROM testkeyarchive tk ";
        
        String wStr = " WHERE tk.orgid=" + orgId + " AND tk.productid=" + product.getProductId();
        if( suborgId>0 )
            wStr += " AND tk.suborgid=" + suborgId + " ";
        wStr += " AND tk.startdate>='" + sDate.toString() + "' AND tk.startdate<='" + eDate.toString() + "' ";
        
        String sqlStr = "(" + sStr + wStr + ") UNION ALL (" + sStr2 + wStr + ") ";
 
        int total = 0;
        int processStarted = 0;
        int testStarted = 0;
        int notStarted = 0;
        int expired = 0;
        int suspended = 0;
        int completed = 0;
        TestKeyStatusType tkst;
        
        Set<Long> tkSet = new HashSet<>();
        
        try (Connection con = pool.getConnection(); Statement stmt = con.createStatement() )
        {
            long testKeyId;
            con.setTransactionIsolation( Connection.TRANSACTION_READ_UNCOMMITTED );

            LogService.logIt( "DashboardFacade.getDashboard2DataMap() AAA " + sqlStr );
            ResultSet rs = stmt.executeQuery( sqlStr );
            while( rs.next() )
            {
                testKeyId=rs.getLong(1);
                if( tkSet.contains(testKeyId ) )
                     continue;
                tkSet.add(testKeyId);

                tkst = TestKeyStatusType.getValue(rs.getInt(2));                 
                if( tkst.getIsDeactivated() )
                     continue;

                total++;
                 
                if( tkst.getIsActive() )
                {
                    notStarted++;
                    continue;
                }
                processStarted++;
                
                if( tkst.getIsFinishedByUser() )
                {
                    testStarted++;
                    completed++;
                }                     
                // need to know if started.
                else if( getStartedTestEvent(testKeyId) )
                    testStarted++;
                
                if( tkst.getIsExpired() )
                    expired++;
                
                else if( tkst.getIsProctorStop())
                    suspended++;
            }
            rs.close();
            
            out.put( "total", total);
            
            out.put( "notstarted", notStarted );
            out.put( "processstarted", processStarted);
            out.put( "teststarted", testStarted);

            out.put( "suspended", suspended);
            out.put( "expired", expired);
            out.put( "completed", completed);
        }
        catch( Exception e )
        {
            LogService.logIt( e, "DashboardFacade.getDashboard2DataMap() " + sqlStr );
            throw e;
        }
        
        return out;
    }
    */
    
    private boolean getStartedTestEvent(long testKeyId) throws Exception
    {
        DataSource pool = (DataSource) new InitialContext().lookup( "jdbc/tm2mirror" );
        if( pool == null )
            throw new Exception( "Can not find Datasource" );

        
        String sqlStr = "SELECT te.testeventid FROM testevent te WHERE testkeyid=" + testKeyId + " AND te.percentcomplete>0 AND te.testeventstatustypeid>0 ";
 
        try (Connection con = pool.getConnection(); Statement stmt = con.createStatement() )
        {
            con.setTransactionIsolation( Connection.TRANSACTION_READ_UNCOMMITTED );

            ResultSet rs = stmt.executeQuery( sqlStr );
            if( rs.next() )
            {
                if( rs.getLong(1)>0)
                    return true;
            }
            rs.close();
            
            sqlStr = "SELECT te.testeventid FROM testeventarchive te WHERE testkeyid=" + testKeyId + " AND te.percentcomplete>0 AND te.testeventstatustypeid>0 ";
            
            rs = stmt.executeQuery( sqlStr );
            if( rs.next() )
            {
                if( rs.getLong(1)>0)
                    return true;
            }
            rs.close();       
        }
        catch( Exception e )
        {
            LogService.logIt( e, "DashboardFacade.getDashboard2DataMap() " + sqlStr );
            throw e;
        }
        return false;
    }











    
    
    
    public Set<Long> getAnalyticsTestEventIds(    Org org,
                                                  int suborgId,
                                                  List<Product> productList,
                                                  String competencyName,
                                                  List<Integer> userCompanyStatusTypeIdList,
                                                  Date startDate,
                                                  Date endDate,
                                                  int maxRows ) throws Exception
    {
        Set<Long> out = new HashSet<>();
        
        java.sql.Date sDate = new java.sql.Date( startDate.getTime() );
        java.sql.Date eDate = new java.sql.Date( endDate.getTime() );

        int orgId = org.getOrgId();

        UserFacade userFacade = UserFacade.getInstance();
        
        // get testeventid list
        String sqlStr = "SELECT te.testeventid FROM testeventarchive AS te ";
        
        if( userCompanyStatusTypeIdList!=null && !userCompanyStatusTypeIdList.isEmpty() )
            sqlStr += " INNER JOIN xuser AS u ON u.userid=te.userid ";
        
        if( competencyName!=null && !competencyName.isBlank() )
            sqlStr += " INNER JOIN testeventscore AS tes ON tes.testeventid=te.testeventid ";
        
        String whereStr = " WHERE te.orgid=" + orgId + " ";
        
        if( suborgId>0 )
            whereStr += " AND te.suborgid=" + suborgId + " ";
        
        whereStr += " AND te.testeventstatustypeid=120 AND te.lastaccessdate>='" + sDate.toString() + "' AND te.lastaccessdate<='" + eDate.toString() + "' ";

        if( productList!=null && !productList.isEmpty() )
        {
            whereStr += " AND te.productid IN (";            
            String pl = "";            
            for( Product p : productList )
            {
                if( !pl.isBlank() )
                    pl += ",";
                
                pl += p.getProductId() + "";
            }
            
            whereStr += pl + ") ";
        }
        
        if( userCompanyStatusTypeIdList!=null && !userCompanyStatusTypeIdList.isEmpty() )
        {
            whereStr += " AND u.usercompanystatustypeid IN (";
            
            String pl = "";            
            for( Integer p : userCompanyStatusTypeIdList )
            {
                if( !pl.isBlank() )
                    pl += ",";
                
                pl += p.toString();
            }
            whereStr += pl + ") ";            
        }
        
        if( competencyName!=null && !competencyName.isBlank() )
        {
            competencyName = StringUtils.sanitizeForSqlQuery( competencyName );
            whereStr += " AND (tes.name='" + competencyName + "' OR tes.nameenglish='" + competencyName + "') ";
        }
        
        sqlStr += whereStr;
        
        sqlStr += " ORDER BY te.testeventid DESC ";
        
        sqlStr += " LIMIT " + (maxRows>0 ? maxRows : Constants.DEFAULT_MAX_ROWS_ANALYTICS );

        DataSource pool = (DataSource) new InitialContext().lookup( "jdbc/tm2mirror" );

        if( pool == null )
            throw new Exception( "Can not find Datasource" );

        try (Connection con = pool.getConnection(); Statement stmt = con.createStatement() )
        {
             con.setTransactionIsolation( Connection.TRANSACTION_READ_UNCOMMITTED );

             ResultSet rs = stmt.executeQuery( sqlStr );
             
             while( rs.next() )
                 out.add( rs.getLong(1));
           
             rs.close();
             
             LogService.logIt( "DashboardFacade.getAnalyticsTestEventIds() Found " + out.size() + " Test Event Ids " + sqlStr );             
        }
        
        catch( Exception e )
        {
           LogService.logIt( "DashboardFacade.getAnalyticsTestEventIds() " + sqlStr );
           throw new STException( e );
        }
        
        return out;
    }
    
    
    







    public Product getProduct( int productId ) throws Exception
    {
        try
        {
            if( productId <= 0 )
                throw new Exception( "productId is invalid " + productId );
            return em.find( Product.class, (Integer)( productId ) );
        }
        catch( NoResultException e )
        {
            return null;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "DashboardFacade.getProduct( " + productId + " )" );
            throw new STException( e );
        }
    }

    public Battery getBattery( int batteryId ) throws Exception
    {
        try
        {
            if( batteryId <= 0 )
                throw new Exception( "batteryId is invalid " + batteryId );
            return em.find( Battery.class, (Integer)( batteryId ) );
        }
        catch( NoResultException e )
        {
            return null;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "DashboardFacade.getBattery( " + batteryId + " )" );
            throw new STException( e );
        }
    }
    




}
