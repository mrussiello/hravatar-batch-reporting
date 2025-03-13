/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.credit;


import com.tm2batch.entity.purchase.Credit;
import com.tm2batch.entity.user.Org;
import com.tm2batch.event.EventFacade;
import com.tm2batch.global.STException;
import com.tm2batch.purchase.CreditSourceType;
import com.tm2batch.purchase.CreditType;
import com.tm2batch.service.LogService;
import com.tm2batch.user.UserFacade;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import jakarta.ejb.Stateless;
import javax.naming.InitialContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import javax.sql.DataSource;

/**
 *
 * @author Mike
 */
@Stateless
public class CreditUsageFacade {

    @PersistenceContext
    EntityManager em;

    public static CreditUsageFacade getInstance()
    {
        try
        {
            return (CreditUsageFacade) InitialContext.doLookup( "java:module/CreditUsageFacade" );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "CreditUsageFacade.getInstance() " );

            return null;
        }
    }


    /**
     *
     * returns:
     *
     * startdate
     * enddate
     * org
     * creditsremaining Int
     * creditslast30 Int 
     * creditslast180 Int
     * creditslast365 Int
     * addedlast30 Int
     * addedlast180 Int
     * creditsperweek String
     * creditsperweekfloat float
     * weeksremaining String    
     * creditactivity List<CandidateCreditUse>
     * creditlist     List<Credit>

     * @return
     * @throws Exception
     */
    public Map<String, Object> getCreditUsageDataMap( Org org,
                                                    Date startDate,
                                                    Date endDate,
                                                    Locale locale,
                                                    TimeZone tz) throws Exception
    {
        Map<String, Object> out = new HashMap<>();

        out.put( "startdate", startDate );
        out.put( "enddate", endDate );

        boolean isUnlimited = org.getOrgCreditUsageType().getUnlimited();
        out.put( "unlimited", isUnlimited );

        java.sql.Timestamp sDate = new java.sql.Timestamp( startDate.getTime() );
        java.sql.Timestamp eDate = new java.sql.Timestamp( endDate.getTime() );

        int orgId = org.getOrgId();

        UserFacade userFacade = UserFacade.getInstance();

        out.put( "org", org );

        CreditType creditType = CreditType.RESULT;

        // need to be sure all suborgs are present.
        List<Credit> cl = getCreditList(org.getOrgId(), creditType.getCreditTypeId());
        Collections.sort( cl, new CreditExpirationComparator() );        
        out.put("creditlist", cl);
                
        int[] creditInfo = getCreditInfo( org.getOrgId(), tz, creditType.getCreditTypeId() );
        out.put( "creditsremaining",  creditInfo[0] );
        out.put( "creditslast30", creditInfo[1] );
        out.put( "addedlast30",  creditInfo[2] );
        out.put( "creditslast180",  creditInfo[3] );
        out.put( "addedlast180",   creditInfo[4] );
        out.put( "creditslast365",  creditInfo[5] );
        
        List<CandidateCreditUse> dml = getRecentCandCreditAssignsInfo(org.getOrgId(), 500, 0 );
        if( dml!=null && !dml.isEmpty() )
        {
            if( userFacade==null )
                userFacade=UserFacade.getInstance();
            CandidateCreditUse cco;
            for( Object dmo : dml )
            {
                cco = (CandidateCreditUse) dmo;
                cco.setUser( userFacade.getUser(cco.getUserId()));
                cco.setFirstUseDate( getResultCreditStartDate( org.getOrgId(), cco.getUserId(), cco.getCreditId(), cco.getCreditIndex()));
            }
        }
        out.put( "creditactivity", dml );
        
        float avgCreditsPerWeekFloat = getAvgCreditsPerWeekFloat(out);
        out.put("creditsperweekfloat", avgCreditsPerWeekFloat);
        
        String avgCreditsPerWeek = getAvgCreditsPerWeek(out);        
        out.put("creditsperweek", avgCreditsPerWeek);
        
        String weeksLeft = getCreditWeeksRemaining(out );
        out.put( "weeksremaining", weeksLeft );
        
        LogService.logIt( "CreditUsageFacade.getCreditUsageDataMap() avgCreditsPerWeekFloat=" + avgCreditsPerWeekFloat + ", avgCreditsPerWeek=" + avgCreditsPerWeek + ", weeksLeft=" + weeksLeft );
        
        return out;
    }


    public float getAvgCreditsPerWeekFloat(Map<String, Object> dm)
    {
        float month = 0;
        float semi=0;
        float year=0;
        
        float days;
        float usage;
        
        int usedLast30 = (Integer) dm.get("creditslast30");
        int usedLast180 = (Integer) dm.get("creditslast180");
        int usedLast365 = (Integer) dm.get("creditslast365");
        
        if( usedLast30>0)
        {
            days=30;
            usage=usedLast30; 
            month = usage/days;
        }
        if( usedLast180>0 )
        {
            days=180;
            usage=usedLast180;   
            semi=usage/days;
        }
        if( usedLast365>0 )
        {
            days=365;
            usage=usedLast365;
            year = usage/days;            
        }
        
        float perDay = 0;
        if( month>0 )
            perDay = month;
        if( semi>0 && semi>perDay )
            perDay=semi;
        if( year>0 && year>perDay )
            perDay = year;

        if( perDay<=0 )
            return 0;
        
        float cupw = 7f*perDay;
       
        if( cupw<1 && cupw>0.1 )
            return 1;
        
        return cupw;
    }

    
    public String getAvgCreditsPerWeek(Map<String, Object> dm)
    {
        float cupw = getAvgCreditsPerWeekFloat(dm);

        if( cupw<=0 )
            return "0";
        
        if( cupw<1 && cupw>0.1 )
            return "1";
        
        return String.format("%.1f", cupw);//   (int)Math.round( cupw);
    }

    public String getCreditWeeksRemaining(Map<String, Object> dm)
    {
        int creditsRemaining = (Integer) dm.get("creditsremaining");
        
        float cpwf = (Float) dm.get("creditsperweekfloat" );
        
        if( creditsRemaining<=0 || cpwf<=0 )
            return "0";
        
        float weeks = creditsRemaining/cpwf;        
        return String.format("%.1f", weeks);    
    }
    
    
    
    
    public Date getResultCreditStartDate( int orgId, long userId, long creditId, int creditIndex ) throws Exception
    {
        if( creditId<=0 ) //  || creditIndex<=0 )
            return null;
        
        String sqlStr = 
            "(SELECT lastaccessdate AS 'lad' from testkeyarchive WHERE orgid=" + orgId + " AND userid=" + userId + " AND lastaccessdate IS NOT NULL AND creditid=" + creditId + " AND creditindex=" + creditIndex + " ) " + 
            " UNION ALL " + 
            "(SELECT lastaccessdate AS 'lad' from testkey WHERE orgid=" + orgId + " AND userid=" + userId + " AND lastaccessdate IS NOT NULL AND creditid=" + creditId + " AND creditindex=" + creditIndex + " ) " + 
            " UNION ALL " + 
            "(SELECT candidatestartdate AS 'lad' from rccheck WHERE orgid=" + orgId + " AND userid=" + userId + " AND candidatestartdate IS NOT NULL AND creditid=" + creditId + " AND creditindex=" + creditIndex + " ) " + 
            " UNION ALL " + 
            "(SELECT startdate AS 'lad' from lvcall WHERE orgid=" + orgId + " AND recipientuserid=" + userId + " AND startdate IS NOT NULL AND creditid=" + creditId + " AND creditindex=" + creditIndex + " ) ";

        DataSource pool = (DataSource) new InitialContext().lookup( "jdbc/tm2mirror" );
        if( pool == null )
            throw new Exception( "Can not find Datasource tm2mirror" );
        
        try( Connection con = pool.getConnection();
             Statement stmt = con.createStatement() )
        {
            ResultSet rs = stmt.executeQuery(sqlStr);
            Timestamp firstDate = null;
            Timestamp curDate;
            while( rs.next() )
            {
                curDate = rs.getTimestamp(1);
                
                if( curDate==null )
                    LogService.logIt(  "CreditUsageFacade.getResultCreditStartDate() Timestamp is null! Skipping. " + sqlStr );
                
                else if( firstDate==null || firstDate.after(curDate) )
                    firstDate=curDate;                    
            }
            rs.close();
            
            return firstDate;
        }
        catch( Exception e )
        {
            LogService.logIt(e, "CreditUsageFacade.getResultCreditStartDate() P2 orgId=" + orgId + ", creditId=" + creditId + ", creditIndex=" + creditIndex + ", sqlStr=" + sqlStr );
            throw new STException( e );
        }        
    }
    
    
    /*
      returns list of 
          mapentry.userid=userId
          mapentry.firstusedate=date assigned
          mapentry.lastusedate=date assigned
          mapentry.creditid
          mapentry.creditindex
    */
    public List<CandidateCreditUse> getRecentCandCreditAssignsInfo( int orgId, int maxRows, int offset ) throws Exception
    {
        if( maxRows<=0 )
            maxRows = 100;
        
        if( offset>0 )
            maxRows = maxRows*offset;
        
        List<CandidateCreditUse> out = new ArrayList<>();
        
        Calendar cal = new GregorianCalendar();
        cal.add( Calendar.MONTH, -12 );
        java.sql.Date minDate = new java.sql.Date( cal.getTime().getTime() );
        
        String sqlStr = "SELECT * FROM (" + 
                        "SELECT tk.creditid, tk.creditindex,tk.userid,tk.lastaccessdate as adate FROM testkey tk WHERE tk.orgid=" + orgId + " AND tk.creditid>0 AND tk.lastaccessdate IS NOT NULL AND tk.lastaccessdate>='" + minDate + "' " + 
                        "UNION ALL " + 
                        "SELECT tk.creditid, tk.creditindex,tk.userid,tk.lastaccessdate as adate FROM testkeyarchive tk WHERE tk.orgid=" + orgId + " AND tk.creditid>0 AND tk.lastaccessdate IS NOT NULL AND tk.lastaccessdate>='" + minDate + "' " + 
                        "UNION ALL " + 
                        "SELECT rc.creditid, rc.creditindex,rc.userid,rc.createdate as adate FROM rccheck rc WHERE rc.orgid=" + orgId + " AND rc.creditid>0 AND rc.createdate IS NOT NULL AND rc.createdate>='" + minDate + "' " + 
                        "UNION ALL " + 
                        "SELECT rc.creditid, rc.creditindex,rc.recipientuserid,rc.startdate as adate FROM lvcall rc WHERE rc.orgid=" + orgId + " AND rc.creditid>0 AND rc.startdate IS NOT NULL AND rc.startdate>='" + minDate + "' " + 
                        ") a ORDER BY adate DESC LIMIT " + maxRows; 
                
        // LogService.logIt( "AccountFacade.getRecentCandCreditAssignsInfo() AAA " + sqlStr );
        
        DataSource pool = (DataSource) new InitialContext().lookup( "jdbc/tm2mirror" );
        if( pool == null )
            throw new Exception( "Can not find Datasource" );

        // int ct = 0;
        CandidateCreditUse cco;
        long creditId;
        int creditIndex;
        long userId;
        Date lastUseDate;
        
        try (Connection con = pool.getConnection();
             Statement stmt = con.createStatement() )
        {        
            ResultSet rs = stmt.executeQuery(sqlStr);
            while( rs.next() )
            {
                creditId = rs.getInt(1);
                creditIndex = rs.getInt(2);
                userId=rs.getLong(3);
                lastUseDate=rs.getTimestamp(4); // rs.getDate(4);
                
                if( lastUseDate==null )
                    continue;
                
                cco = new CandidateCreditUse( userId,creditId,creditIndex );
                cco.setLastUseDate(lastUseDate);
                if( out.contains(cco) )
                    continue;
                out.add(cco);
                if( out.size()>=maxRows )
                    break;
            }   
            rs.close();
        }
        catch( Exception e )
        {
            LogService.logIt( e, "CreditUsageFacade.getRecentCandCreditAssignsInfo() " + sqlStr );
            throw new STException( e );
        }

        if( offset>0 )
        {
            if( out.size()<offset )
                return new ArrayList<>();
            
            return out.subList(offset, out.size() );
        }
        
        return out;
    }
    
    
    


    /**
     *
     * int[0] = total remaining
     * int[1] = used last 30
     * int[2] = added last 30
     * int[3] = used last 180
     * int[4] = added last 180
     * int[5] = used last 365
     * int[6] = purchasedEver (no free trial)
     *
     *
     * @param orgId
     * @return
     * @throws Exception
     */
    public int[] getCreditInfo( int orgId, TimeZone tz, int creditTypeId ) throws Exception
    {
        Connection con = null;
        Statement stmt = null;
        String sqlStr = null;
        int[] out = new int[7];

        try
        {
            DataSource pool = (DataSource) new InitialContext().lookup( "jdbc/tm2mirror" );

            if( pool == null )
                throw new Exception( "Can not find Datasource" );

            con = pool.getConnection();

            con.setTransactionIsolation( Connection.TRANSACTION_READ_UNCOMMITTED );

            Calendar cal = new GregorianCalendar(); // tz);
            
            java.sql.Date today = new java.sql.Date( cal.getTime().getTime() );

            // Go back 30
            cal.add( Calendar.DAY_OF_MONTH, -30 );

            java.sql.Date back30 = new java.sql.Date( cal.getTime().getTime() );

            // Go back 180
            cal.add( Calendar.DAY_OF_MONTH, -150 );

            java.sql.Date back180 = new java.sql.Date( cal.getTime().getTime() );

            // Go back 1 year
            cal = new GregorianCalendar(); // tz);
            
            cal.add( Calendar.YEAR, -1 );

            java.sql.Date back365 = new java.sql.Date( cal.getTime().getTime() );

            stmt = con.createStatement();
            
            // get total credits available.
            out[0] = getCreditBalance( orgId, creditTypeId, tz);

            // Next get credits added 30
            sqlStr = "SELECT SUM( initialCount ) FROM credit WHERE orgId=" + orgId + " AND credittypeid=" + creditTypeId + " AND createDate>='" + back30.toString() + "' ";

            ResultSet rs = stmt.executeQuery(sqlStr);

            if( rs.next() )
                out[2] = rs.getInt( 1 );

            rs.close();

            // Next get credits added 180
            sqlStr = "SELECT SUM( initialCount ) FROM credit WHERE orgId=" + orgId + " AND credittypeid=" + creditTypeId + " AND createDate>='" + back180.toString() + "' ";

            rs = stmt.executeQuery(sqlStr);

            if( rs.next() )
                out[4] = rs.getInt( 1 );

            rs.close();  
            
            Set<String> unqSet = new HashSet<>();
            
            // Next get credits used 30
            if( creditTypeId==CreditType.RESULT.getCreditTypeId() )
            {
                unqSet.clear();
                sqlStr =    "SELECT DISTINCT creditid,creditindex FROM testkey WHERE orgId=" + orgId + " AND creditid>0 and creditindex>0 AND startDate>='" + back30.toString() + "' " +
                            "UNION ALL " +
                            "SELECT DISTINCT creditid,creditindex FROM testkeyarchive WHERE orgId=" + orgId + " AND creditid>0 and creditindex>0 AND startDate>='" + back30.toString() + "' " +
                            "UNION ALL " +
                            "SELECT DISTINCT creditid,creditindex FROM rccheck WHERE orgId=" + orgId + " AND creditid>0 and creditindex>0 AND createdate>='" + back30.toString() + "' " +
                            "UNION ALL " +
                            "SELECT DISTINCT creditid,creditindex FROM lvcall WHERE orgId=" + orgId + " AND creditid>0 and creditindex>0 AND startdate>='" + back30.toString() + "' ";
                        
                        //"SELECT COUNT(1) FROM testkey WHERE orgId=" + orgId + " AND creditid>0 AND startDate>='" + back30.toString() + "' " +
                         //"UNION ALL " +
                         //"SELECT COUNT(1) FROM testkeyarchive WHERE orgId=" + orgId + " AND creditid>0 AND startDate>='" + back30.toString() + "' ";
                    rs = stmt.executeQuery(sqlStr);
                    while( rs.next() )
                        unqSet.add( rs.getInt(1) + "-" + rs.getInt(2) );
                    
                    out[1] = unqSet.size();
                    rs.close();
            }
            else
            {
                sqlStr = "SELECT SUM( creditsused ) FROM testevent WHERE orgId=" + orgId + " AND startDate>='" + back30.toString() + "' " +
                         "UNION ALL " +
                         "SELECT SUM( creditsused ) FROM testeventarchive WHERE orgId=" + orgId + " AND startDate>='" + back30.toString() + "' ";
                
                // LogService.logIt( "AccountFacade.getCreditInfo() " + sqlStr );
                rs = stmt.executeQuery(sqlStr);
                while( rs.next() )
                    out[1] += rs.getInt( 1 );
                rs.close();
            }
            
            // Next get credits used 180
            if( creditTypeId==CreditType.RESULT.getCreditTypeId() )
            {
                unqSet.clear();
                sqlStr =    "SELECT DISTINCT creditid,creditindex FROM testkey WHERE orgId=" + orgId + " AND creditid>0 and creditindex>0 AND startDate>='" + back180.toString() + "' " +
                            "UNION ALL " +
                            "SELECT DISTINCT creditid,creditindex FROM testkeyarchive WHERE orgId=" + orgId + " AND creditid>0 and creditindex>0 AND startDate>='" + back180.toString() + "' " +
                            "UNION ALL " +
                            "SELECT DISTINCT creditid,creditindex FROM rccheck WHERE orgId=" + orgId + " AND creditid>0 and creditindex>0 AND createdate>='" + back180.toString() + "' " +
                            "UNION ALL " +
                            "SELECT DISTINCT creditid,creditindex FROM lvcall WHERE orgId=" + orgId + " AND creditid>0 and creditindex>0 AND startdate>='" + back180.toString() + "' ";

                rs = stmt.executeQuery(sqlStr);                
                while( rs.next() )
                    unqSet.add( rs.getInt(1) + "-" + rs.getInt(2) );

                out[3] = unqSet.size();
                rs.close();

                //    sqlStr = "SELECT COUNT(1) FROM testkey WHERE orgId=" + orgId + " AND creditid>0 AND startDate>='" + back180.toString() + "' " +
                //         "UNION ALL " +
                //         "SELECT COUNT(1) FROM testkeyarchive WHERE orgId=" + orgId + " AND creditid>0 AND startDate>='" + back180.toString() + "' ";
            }
            else
            {
                sqlStr = "SELECT SUM( creditsused ) FROM testevent WHERE orgId=" + orgId + " AND startDate>='" + back180.toString() + "' " +
                     "UNION ALL " +
                     "SELECT SUM( creditsused ) FROM testeventarchive WHERE orgId=" + orgId + " AND startDate>='" + back180.toString() + "' ";
            
                rs = stmt.executeQuery(sqlStr);
                while( rs.next() ) 
                    out[3] += rs.getInt(1);
                rs.close();
            }
                    
            // Next get credits used 365
            if( creditTypeId==CreditType.RESULT.getCreditTypeId() )
            {    
                unqSet.clear();
                sqlStr =    "SELECT DISTINCT creditid,creditindex FROM testkey WHERE orgId=" + orgId + " AND creditid>0 and creditindex>0 AND startDate>='" + back365.toString() + "' " +
                            "UNION ALL " +
                            "SELECT DISTINCT creditid,creditindex FROM testkeyarchive WHERE orgId=" + orgId + " AND creditid>0 and creditindex>0 AND startDate>='" + back365.toString() + "' " +
                            "UNION ALL " +
                            "SELECT DISTINCT creditid,creditindex FROM rccheck WHERE orgId=" + orgId + " AND creditid>0 and creditindex>0 AND createdate>='" + back365.toString() + "' " +
                            "UNION ALL " +
                            "SELECT DISTINCT creditid,creditindex FROM lvcall WHERE orgId=" + orgId + " AND creditid>0 and creditindex>0 AND startdate>='" + back365.toString() + "' ";

                rs = stmt.executeQuery(sqlStr);                
                while( rs.next() )
                    unqSet.add( rs.getInt(1) + "-" + rs.getInt(2) );

                out[5] = unqSet.size();
                rs.close();
            }
            else
            {    
                sqlStr = "SELECT SUM( creditsused ) FROM testevent WHERE orgId=" + orgId + " AND startDate>='" + back365.toString() + "' " +
                         "UNION ALL " +
                         "SELECT SUM( creditsused ) FROM testeventarchive WHERE orgId=" + orgId + " AND startDate>='" + back365.toString() + "' ";

                rs = stmt.executeQuery(sqlStr);
                while( rs.next() )
                    out[5] += rs.getInt(1);
                rs.close();  
            }

            
            // get credits EVER not free trial.
            sqlStr = "SELECT SUM( initialCount ) FROM credit WHERE orgId=" + orgId + " AND credittypeid=" + creditTypeId + " AND creditsourcetypeid<>" + CreditSourceType.FREE_PROMO.getCreditSourceTypeId() + " ";

            rs = stmt.executeQuery(sqlStr);

            if( rs.next() )
                out[6] = rs.getInt( 1 );
            rs.close();  
            
            stmt.close();

            return out;
        }

        catch( Exception e )
        {
            LogService.logIt(e, "CreditUsageFacade.getCreditInfo() orgId=" + orgId + ", " + sqlStr );

            throw new STException( e );
        }

        finally
        {
            try
            {
                if( stmt != null )
                    stmt.close();
                if( con != null )
                    con.close();
            }
            catch( SQLException e )
            {}
        }
    }
    
    
    public int getCreditBalance(int orgId, int creditTypeId, TimeZone tz) throws Exception
    {
        Connection con = null;

        String sqlStr = null;

        int out = 0;

        DataSource pool = (DataSource) new InitialContext().lookup( "jdbc/tm2mirror" );

        if( pool == null )
            throw new Exception( "Can not find Datasource" );

        try
        {
            con = pool.getConnection();

            // con.setTransactionIsolation( Connection.TRANSACTION_READ_UNCOMMITTED );

            Calendar cal = new GregorianCalendar(); // tz);

            java.sql.Date today = new java.sql.Date( cal.getTime().getTime() );

            // get total credits available.
            sqlStr = "SELECT SUM( remainingcount ) FROM credit WHERE orgId=" + orgId + " AND creditStatusTypeId=1 AND credittypeid=" + creditTypeId + " AND expireDate>'" + today.toString() + "' AND remainingCount>0";

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlStr);

            if( rs.next() )
                out = rs.getInt( 1 );
            rs.close();

            int overageCount = 0;
            // Deduct overage if there is any.
            if( creditTypeId==CreditType.RESULT.getCreditTypeId() )
            {
                int ovrg=0;
                sqlStr = "SELECT SUM( overagecount ) FROM credit WHERE orgId=" + orgId + " AND creditstatustypeid=5 AND credittypeid=" + creditTypeId + " AND overagecount>0";
                rs = stmt.executeQuery(sqlStr);
                if( rs.next() )
                {
                    ovrg = rs.getInt(1);
                    overageCount += ovrg;
                    out = out - ovrg;
                }
                rs.close();                
            }            

            return out;
        }
        catch( Exception e )
        {
            LogService.logIt(e, "CreditUsageFacade.getCreditBalance() orgId=" + orgId + ", creditTypeId=" + creditTypeId + ", sql=" + sqlStr );
            throw new STException( e );
        }
        finally
        {
            try
            {
                if( con != null )
                    con.close();
            }
            catch( SQLException e )
            {}
        }
    }

    
    
    
    public List<Credit> getCreditList( int orgId, int creditTypeId) throws Exception
    {
        try
        {
            if( orgId <= 0 )
                return null;

            // if( tm2Factory == null ) // tm2Factory = PersistenceMan
            Query q = em.createNamedQuery( "Credit.findAvailForOrg" );
            q.setHint( "jakarta.persistence.cache.retrieveMode", "BYPASS" );
            q.setParameter( "orgId", orgId );
            q.setParameter( "creditTypeId", creditTypeId );
            q.setParameter( "today", new Date() );
            q.setParameter( "quantity", 0 );

            return q.getResultList();
        }
        catch( Exception e )
        {
            LogService.logIt(e, "getCreditList( " + orgId + " ) " );
            throw new STException( e );
        }
    }



}
