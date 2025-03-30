package com.tm2batch.account.results;


import com.tm2batch.battery.BatteryScoreStatusType;
import com.tm2batch.custom.result.TestResult;
import com.tm2batch.entity.autoreport.BatchReport;
import com.tm2batch.entity.battery.BatteryScore;
import com.tm2batch.entity.event.TestEvent;
import com.tm2batch.entity.event.TestKey;
import com.tm2batch.entity.report.Report;
import com.tm2batch.entity.user.Suborg;
import com.tm2batch.event.EventFacade;
import com.tm2batch.global.Constants;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.global.STException;
import com.tm2batch.profile.ProfileFacade;
import com.tm2batch.profile.ProfileUsageType;
import com.tm2batch.score.TextAndTitle;
import com.tm2batch.service.LogService;
import com.tm2batch.user.CountryCodeLister;
import com.tm2batch.user.UserActionType;
import com.tm2batch.user.UserFacade;
import com.tm2batch.util.MessageFactory;
import com.tm2batch.util.NumberUtils;
import com.tm2batch.util.STStringTokenizer;
import com.tm2batch.util.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class TestReportingUtils
{
    EventFacade eventFacade;
    UserFacade userFacade;
    ProfileFacade profileFacade;



    public List<TestKey> getTestKeyList( BatchReport batchReport, 
                                               boolean thisUserIdOnly, 
                                               int maxTestKeyStatusTypeId,
                                               String productNameKeyword,
                                               int productId, 
                                               int productTypeId,
                                               int consumerProductTypeId,
                                               int batteryId,
                                               int orgAutoTestId,
                                               List<Long> userIdList,
                                               Date startDate, 
                                               Date endDate, 
                                               int sortTypeId,
                                               int maxRows ) throws Exception
    {
        List<TestKey> out = new ArrayList<>();
        try
        {
            List<BatteryScore> bsl = new ArrayList<>();

            // testResultBean.resetForRecentOnly( userBean.getUser() );

            if( eventFacade == null )
                eventFacade = EventFacade.getInstance();

            List<TestKey> tel = eventFacade.findTestKeys(   batchReport.getOrgId(),
                                                              batchReport.getSuborgId(),
                                                              thisUserIdOnly ? batchReport.getUserId() : 0,
                                                              maxTestKeyStatusTypeId,
                                                              productNameKeyword, // productNameKeyword,
                                                              productId, // testResultBean.getProductId(),
                                                              productTypeId, // testResultBean.getProductTypeId(),
                                                              consumerProductTypeId, // testResultBean.getConsumerProductTypeId(),
                                                              batteryId, // testResultBean.getBatteryId(),
                                                              orgAutoTestId,
                                                              userIdList,
                                                              startDate, // testResultBean.getCompletedAfter(),
                                                              endDate, // testResultBean.getCompletedBefore(),
                                                              sortTypeId, // testResultBean.getTestResultSortTypeId(),
                                                              maxRows  );
            
            
            for( TestKey tk : tel )
            {
                loadTestKeyObjects(tk, true );
            }

            return tel;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "TestReportingUtils.getTestKeyList() " );
        }
        return out;
    }


    
    
    public List<TestResult> getTestResultList( BatchReport batchReport, 
                                               boolean thisUserIdOnly, 
                                               String productNameKeyword,
                                               int orgAutoTestId,
                                               int productId, 
                                               int productTypeId,
                                               int consumerProductTypeId,
                                               int batteryId,
                                               String[] customsArray,
                                               int userCompanyStatusTypeId, 
                                               List<Long> userIdList,
                                               Date startDate, 
                                               Date endDate, 
                                               int testResultSortTypeId,
                                               int maxRows,
                                               boolean includeBatteryTestEvents ) throws Exception
    {
        List<TestResult> out = new ArrayList<>();
        try
        {
            List<BatteryScore> bsl = new ArrayList<>();

            // testResultBean.resetForRecentOnly( userBean.getUser() );

            if( eventFacade == null )
                eventFacade = EventFacade.getInstance();

            List<TestEvent> tel = eventFacade.findTestEvents(   batchReport.getOrgId(),
                                                                batchReport.getSuborgId(),
                                                                thisUserIdOnly ? batchReport.getUserId() : 0,
                                                                productNameKeyword, // productNameKeyword,
                                                                null, // testResultBean.getLastNameStart(),
                                                                null, // testResultBean.getEmailKeyword(),
                                                                orgAutoTestId, // testResultBean.getOrgAutoTestId(),
                                                                productId, // testResultBean.getProductId(),
                                                                null,
                                                                productTypeId, // testResultBean.getProductTypeId(),
                                                                consumerProductTypeId, // testResultBean.getConsumerProductTypeId(),
                                                                batteryId, // testResultBean.getBatteryId(),
                                                                0, // testResultBean.getCandidateUser()==null ? 0 : testResultBean.getCandidateUser().getUserId(), // userId
                                                                null, // testResultBean.getExtRef(),
                                                                null, // testResultBean.getLastName(),
                                                                null, // testResultBean.getAlternateIdentifier(),
                                                                null, // testResultBean.getPin(),
                                                                null, // testResultBean.getLangStr(),
                                                                customsArray, // testResultBean.getCustoms(),
                                                                userIdList,
                                                                startDate, // testResultBean.getCompletedAfter(),
                                                                endDate, // testResultBean.getCompletedBefore(),
                                                                testResultSortTypeId, // testResultBean.getTestResultSortTypeId(),
                                                                userCompanyStatusTypeId, // testResultBean.getUserCompanyStatusTypeId(),
                                                                maxRows,
                                                                0  );
            
            
            for( TestEvent te : tel )
            {
                loadTestEventObjects( te, bsl, batchReport.getLocaleToUseDefaultUS(), false );
            }

            if( !includeBatteryTestEvents )
            {
                LogService.logIt( "TestReportingUtils.processViewTestResults() Removing BatteryEvents.");
                removeBatteryTestEvents( tel );
            }

            List<TestResult> ter = new ArrayList<>();
            ter.addAll( bsl );
            ter.addAll( tel );

            // Next we need to sort the TestResultList.
            Collections.sort( ter, new TestResultComparator( TestResultSortType.getValue( testResultSortTypeId )) );

            return ter;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "TestReportingUtils.getTestResultList() " );
        }
        return out;
    }
    
    
    public void loadTestKeyObjects( TestKey tk, boolean forTestKeyReport) throws Exception
    {
        try
        {

            if( tk.getUser()==null && tk.getUserId()>0 )
            {
                if( userFacade == null ) 
                    userFacade = UserFacade.getInstance();
                tk.setUser( userFacade.getUser( tk.getUserId()));
            }

            if( forTestKeyReport && tk.getAuthorizingUser()==null && tk.getAuthorizingUserId()>0 )
            {
                if( userFacade == null ) 
                    userFacade = UserFacade.getInstance();
                tk.setAuthorizingUser( userFacade.getUser( tk.getAuthorizingUserId()));
            }
            
            if( tk.getOrg()==null && tk.getOrgId()>0 )
            {
                if( userFacade == null ) 
                    userFacade = UserFacade.getInstance();
                tk.setOrg( userFacade.getOrg( tk.getOrgId()));
            }

            if( tk.getSuborg()==null && tk.getSuborgId()>0 )
            {
                if( userFacade == null ) 
                    userFacade = UserFacade.getInstance();
                tk.setSuborg( userFacade.getSuborg( tk.getSuborgId()));
            }

            if( forTestKeyReport && tk.getBattery()==null && tk.getBatteryId()>0 )
            {
                if( eventFacade == null ) 
                    eventFacade = EventFacade.getInstance();
                tk.setBattery( eventFacade.getBattery( tk.getBatteryId()));
            }
            
            if( tk.getProduct()==null && tk.getProductId()>0 )
            {
                if( eventFacade == null ) 
                    eventFacade = EventFacade.getInstance();
                tk.setProduct( eventFacade.getProduct( tk.getProductId() ));
            }

            if( forTestKeyReport && tk.getTestEventList()==null )
            {
                if( eventFacade == null ) 
                    eventFacade = EventFacade.getInstance();
                tk.setTestEventList(eventFacade.getTestEventsForTestKey( tk.getTestKeyId() ) );                
            }
            
            if( forTestKeyReport )
            {
                for( TestEvent te : tk.getTestEventList() )
                {
                    if( te.getProduct()==null )
                        te.setProduct( eventFacade.getProduct( te.getProductId() ));
                }
            }

            if( forTestKeyReport && tk.getUserActionList()==null )
            {
                if( userFacade == null ) 
                    userFacade = UserFacade.getInstance();

                tk.setUserActionList(userFacade.getUserActionEmails(tk.getTestKeyId(), 0, 0, UserActionType.SENT_EMAIL.getUserActionTypeId() ) );
                // tk.getUserActionList().addAll( userFacade.getUserActionEmails(tk.getTestKeyId(), 0, 0, UserActionType.SENT_TEXT.getUserActionTypeId() ) );                
            }
            
        }
        catch( Exception e )
        {
            LogService.logIt(e, "TestReportingUtils.loadTestKeyObjects() testKeyId=" + (tk==null ? "null" : tk.getTestKeyId() ) );
            throw new STException( e );
        }
        
    }
    
    
    public static String getTestKeyStartUrl( TestKey tk )
    {
        if( tk==null || tk.getTestKeyStatusType().getIsCompleteOrHigher() || tk.getPinToUse()==null || tk.getPinToUse().isBlank() )
            return null;
        
        if( tk.getStartUrl()!=null && !tk.getStartUrl().isBlank() )
            return tk.getStartUrl();
        
        String protocol = RuntimeConstants.getStringValue( "testingappprotocol" );

        if( !RuntimeConstants.getBooleanValue("testingapphttpsOK") )
            protocol = "http";
            
        String url = protocol + "://" + RuntimeConstants.getStringValue( "testingappbasedomain" ) + "/" + RuntimeConstants.getStringValue( "testingappcontextroot" ) + "/tk/" + tk.getProductId() + "/" + tk.getOrgId() + "/" + tk.getPinToUse();

        tk.setStartUrl( url );
            
        return url;
        
    }
    
    public void loadTestEventObjects( TestEvent te, List<BatteryScore> batteryScoreList, Locale locale, boolean forDetail ) throws Exception
    {
        try
        {
            te.setLocale( locale );

            if( te.getUser()==null && te.getUserId()>0 )
            {
                if( userFacade == null ) 
                    userFacade = UserFacade.getInstance();
                te.setUser( userFacade.getUser( te.getUserId()));
            }
            
            if( te.getOrg()==null && te.getOrgId()>0 )
            {
                if( userFacade == null ) 
                    userFacade = UserFacade.getInstance();
                te.setOrg( userFacade.getOrg( te.getOrgId()));
            }

            if( te.getSuborg()==null && te.getSuborgId()>0 )
            {
                if( userFacade == null ) 
                    userFacade = UserFacade.getInstance();
                te.setSuborg( userFacade.getSuborg( te.getSuborgId()));
            }

            if( te.getReport()==null )
            {
                if( te.getReportId()>0 )
                {
                    if( eventFacade == null ) eventFacade = EventFacade.getInstance();
                    te.setReport( eventFacade.getReport( te.getReportId()));
                }

                // cant find report, use default. Need this object.
                if( te.getReport() == null )
                    te.setReport( new Report() );
            }

            if( te.getProduct()==null && te.getProductId()>0 )
            {
                if( eventFacade == null ) 
                    eventFacade = EventFacade.getInstance();
                te.setProduct( eventFacade.getProduct( te.getProductId() ));
            }

            if( te.getTestEventScoreList()==null )
            {
                if( eventFacade == null ) 
                    eventFacade = EventFacade.getInstance();
                te.setTestEventScoreList(eventFacade.getTestEventScoresForTestEvent(te.getTestEventId(), -1 ) );                
            }
            
            if( te.getProduct().getDetailView().equalsIgnoreCase( Constants.CORETEST2 ) || 
                    te.getProduct().getDetailView().equalsIgnoreCase( Constants.IVRTEST1 ) || 
                    te.getProduct().getDetailView().equalsIgnoreCase( Constants.AVTEST1 ) || 
                    te.getProduct().getDetailView().equalsIgnoreCase( Constants.HRAPH_TEAMLEAD )|| 
                    te.getProduct().getDetailView().equalsIgnoreCase( Constants.HRAPH_BSPL ) ||
                    te.getProduct().getDetailView().equalsIgnoreCase( Constants.HRAPH_BSP_ITSS ) ||
                    te.getProduct().getDetailView().equalsIgnoreCase( Constants.HRAPH_METROBANK_MOSS ) ||
                    te.getProduct().getDetailView().equalsIgnoreCase( Constants.HRAPH_FAST_CORE ) )
            {
                if( profileFacade==null )
                    profileFacade=ProfileFacade.getInstance();
                
                te.setReportRangeProfile(profileFacade.findSingleProfileForTestEvent(te.getProductId() , te.getOrgId(), ProfileUsageType.REPORT_RANGES.getProfileUsageTypeId() ) );                
                // te.setReportRangeProfile(ProfileUtils.getLiveProfileForProductIdAndOrgId( te.getProductId() , te.getOrgId(), ProfileUsageType.REPORT_RANGES.getProfileUsageTypeId() ) );
            }


            // get the battery score list straight
            if( batteryScoreList != null && te.getBatteryId()>0 && te.getProduct()!=null )
            {
                BatteryScore bs = null;

                for( BatteryScore bss : batteryScoreList )
                {
                    if( bss.getTestKeyId()==te.getTestKeyId() )
                    {
                        bs = bss;
                        break;
                    }
                }

                if( bs == null )
                {
                    if( eventFacade == null )
                        eventFacade = EventFacade.getInstance();

                    bs = eventFacade.findActiveBatteryScoreForTestKeyId( te.getTestKeyId() );

                    if( bs != null &&  bs.getBatteryScoreStatusTypeId()==BatteryScoreStatusType.DEACTIVATED.getBatteryScoreStatusTypeId() )
                        bs = null;

                    if( bs != null )
                    {
                        batteryScoreList.add( bs );
                        bs.setBattery( eventFacade.getBattery( te.getBatteryId() ) );

                        bs.setUser(te.getUser() );

                        TestKey tk = eventFacade.getTestKey( bs.getTestKeyId() , true );

                        if( tk != null )
                        {
                            bs.setProduct( eventFacade.getProduct( tk.getProductId() ));
                        }
                    }
                }

                if( bs != null )
                {
                    bs.addTestEvent( te );

                    // if( bs.getOverallPercentile()>= 0 )
                    //     bs.setOverallPercentileStr( NumberUtils.getPctSuffixStr( locale , bs.getOverallPercentile(), 0 ) );

                    if( bs.getAccountPercentile()>= 0 )
                        bs.setAccountPercentileStr( NumberUtils.getPctSuffixStr( locale , bs.getAccountPercentile(), 0 ) );

                    if( bs.getCountryPercentile()>= 0 )
                        bs.setCountryPercentileStr( NumberUtils.getPctSuffixStr( locale , bs.getCountryPercentile(), 0 ) );

                    if( bs.getUser()!=null && ( bs.getUser().getCountryName()==null || bs.getUser().getCountryName().isEmpty() ) )
                    {
                        
                        if( bs.getUser().getIpCountry()!=null )
                            bs.getUser().setCountryName( CountryCodeLister.getCountryForCode( locale , bs.getUser().getIpCountry() ) );

                        //else if( bs.getUser().getCountryCode()!=null )
                        //    bs.getUser().setCountryName( CountryCodeLister.getCountryForCode( locale , bs.getUser().getCountryCode() ) );                        
                    }
                    
                    if( bs.getSuborg()==null && bs.getSuborgId()>0 )
                    {
                        Suborg s = te.getSuborg(); //  userBean.getUser().getOrg().getSuborg( bs.getSuborgId() );

                        if( s== null )
                        {
                            if( userFacade == null ) 
                                userFacade = UserFacade.getInstance();
                            s = userFacade.getSuborg( bs.getSuborgId() );
                        }
                        bs.setSuborg(s);
                    }
                }
            }

            /*
            if( te.getSuborg()==null && te.getSuborgId()>0 )
            {
                Suborg s = userBean.getUser().getOrg().getSuborg( te.getSuborgId() );

                te.setSuborg(s);
            }
            */

            if( forDetail || (te.getReportRangeProfile()!=null && te.getReportRangeProfile().getStrParam2()!=null && !te.getReportRangeProfile().getStrParam2().isEmpty() ) )
            {
                if( te.getTestEventScoreList()==null ) // || te.getTestEventScoreList().size()==1 )
                {
                    if( eventFacade == null ) eventFacade = EventFacade.getInstance();
                    te.setTestEventScoreList(eventFacade.getTestEventScoresForTestEvent(te.getTestEventId(), -1 ) );
                }
            }

            if( te.getIpCountry() != null && !te.getIpCountry().isEmpty() )
                te.setIpCountryName( MessageFactory.getStringMessage( locale , "cntry." + te.getIpCountry(), null ) );

            else
                te.setIpCountryName( MessageFactory.getStringMessage( locale , "cntry." + te.getUser().getCountryCode(), null ) );

            if( te.getPercentileCountry() != null && !te.getPercentileCountry().isEmpty() )
                te.setPercentileCountryName( MessageFactory.getStringMessage( locale , "cntry." + te.getPercentileCountry(), null ) );

            else
                te.setPercentileCountryName( null );            
        }
        catch( Exception e )
        {
            LogService.logIt( e, "TestReportingUtils.loadTestEventObjects() " + (te==null ? "null" : te.toString() ) );
            throw new STException( e );
        }
    }
    
    
    
    
    
    
    
    public static void removeBatteryTestEvents( List<TestEvent> tel )
    {
        ListIterator<TestEvent> li = tel.listIterator();
        TestEvent te;

        while( li.hasNext() )
        {
            te = li.next();

            if( te.getBatteryId()>0 )
                li.remove();
        }
    }    
    
    public static String getValueFromTextParam( String key, String t )
    {
        String o = StringUtils.getBracketedArtifactFromString( t , key );

        if( o != null && !o.isEmpty() )
            return o;

        if( t.indexOf( "[" ) > 0 )
            return t.substring( 0 , t.indexOf( "[" ) );

        return t;
    }



    
    public static List<TextAndTitle> getTextBasedResponseList( String inStr )
    {
        return getTextBasedResponseList(inStr, null, false, false, false );
    }
    
    public static List<TextAndTitle> getTextBasedResponseList( String inStr, String title, boolean includeFileUploads, boolean fileUploadsOnly, boolean setOrderInInt1)
    {
        List<TextAndTitle> out = new ArrayList<>();

        if( inStr==null || inStr.isEmpty() )
            return out;

        int ct = 0;
        
        try
        {
            int i;

            String ky;

            boolean sty = true;

            String t = inStr;

            if( title != null && !title.isEmpty() )
            {
                ky =  ";;;" + title + ";;;" + Constants.DELIMITER;

                i = inStr.indexOf(";;;" + title + ";;;" + Constants.DELIMITER );

                if( i < 0 )
                    return out;

                t = inStr.substring( i + ky.length() );
            }

            if( t.length()==0 )
                return out;

            if( t.indexOf( ";;;" ) >= 0 )
                t = t.substring(0, t.indexOf( ";;;" ) );

            if( t.length()==0 )
                return out;

            STStringTokenizer st = new STStringTokenizer( t, Constants.DELIMITER );

            String q,a,r;

            boolean upld;

            long uufid;
            String string1;
            String string2;

            TextAndTitle ttl;
            
            // boolean hasString1 = st.countTokens() % 4 == 0;
            boolean hasString1 = ( st.countDelims() + 1) % 4 == 0;
                                    
            while( st.hasMoreTokens() )
            {
                q = st.nextToken();

                a = st.hasMoreTokens()? st.nextToken() : null;

                upld =  a != null && a.startsWith( "UPLOAD:" );

                r = st.hasMoreTokens()? st.nextToken() : null;

                if( upld && !includeFileUploads )
                    continue;

                //if( !upld && fileUploadsOnly )
                //    continue;

               // LogService.logIt( "TestReportingUtils.getTextBasedResponseList() q=" + q + ", a=" + a + ", r=" + r + ", upld=" + upld + ", hasString1="  +hasString1  );

                uufid = 0;

                if( a != null )
                {
                    try
                    {
                        a = StringUtils.doUrlDecode(a, "UTF8");
                    }
                    catch( Exception e )
                    {
                        LogService.logIt( "TestReportingUtils.getTextBasedResponseList() URLDecoding AAA error. NON-FATAL  error=" + e.toString() + ", decoding: a=" + a );
                    }
                }

                if( q != null )
                {
                    try
                    {
                        q = StringUtils.doUrlDecode(q, "UTF8");
                    }
                    catch( Exception e )
                    {
                        LogService.logIt( "TestReportingUtils.getTextBasedResponseList() URLDecoding BBB NON-FATAL error=" + e.toString() + ", decoding: q=" + q );
                    }
                }


                if( r != null && r.indexOf( "uuf:" )>=0 )
                {
                    uufid = Long.parseLong( r.substring( r.indexOf( "uuf:" )+4, r.length() ) );

                    // LogService.logIt( "TestReportingUtils.getTextBasedResponseList() parsed uufid=" + uufid + " from " + r );
                }

                //if( uufid>0 && !includeFileUploads )
                //    continue;

                if( !upld && uufid<=0 && fileUploadsOnly )
                    continue;
                
                string1 = hasString1 && st.hasMoreTokens() ? st.nextToken() : null;
                string2 = null;
                
                if( string1!=null && !string1.isEmpty() )
                {
                    try
                    {
                        string1 = StringUtils.doUrlDecode(string1, "UTF8");
                        
                        if( string1.contains("~") )
                        {
                            String[] ds = string1.split("~");
                            string1 = ds.length>0 ? ds[0] : "";
                            if( ds.length>1 )
                                string2 = ds[1];
                        }
                        //LogService.logIt( "TestReportingUtils.getTextBasedResponseList() parsed string1=" + string1 );
                    }
                    catch( Exception e )
                    {
                        LogService.logIt( "TestReportingUtils.getTextBasedResponseList() URLDecoding DDD string1 error. NON-FATAL  error=" + e.toString() + ", decoding: string1=" + string1 );
                    } 
                }

                if( q != null && ((a != null && a.trim().length()>0) || uufid>0 ))
                {
                    ttl = new TextAndTitle( a, q, r!=null && r.indexOf( "red:1")>=0, uufid, string1, string2 );

                    //.logIt( "TestReportingUtils.getTextBasedResponseList() Craeated TTL " + ttl.getString1() + " hasStr1=" + ttl.getHasString1() );
                    
                    ttl.setRowStyleClass( "rowstyle" + (sty ? "a" : "b" ) );
                    sty = !sty;

                    if( setOrderInInt1 )
                    {
                        ct++;
                        ttl.setIntParam1(ct);
                    }
                    out.add( ttl );
                }
            }
        }

        catch( Exception e )
        {
            LogService.logIt( e, "TestReportingUtils.getTextBasedResponseList() " );
        }

        return out;
    }
    
    

}
