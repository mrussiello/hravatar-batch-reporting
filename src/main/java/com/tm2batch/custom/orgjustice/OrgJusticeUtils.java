/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tm2batch.custom.orgjustice;

import com.tm2batch.account.results.TestResultSortType;
import com.tm2batch.entity.event.ItemResponse;
import com.tm2batch.entity.event.TestEvent;
import com.tm2batch.entity.event.TestEventScore;
import com.tm2batch.entity.user.User;
import com.tm2batch.event.EventFacade;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.service.LogService;
import com.tm2batch.user.UserFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author miker
 */
public class OrgJusticeUtils {
    
    EventFacade eventFacade;
    OrgJusticeFacade orgJusticeFacade;
    UserFacade userFacade;
    
    public static List<Integer> ORG_JUSTICE_PRODUCT_IDS;
    
    public static synchronized void init()
    {
        if( ORG_JUSTICE_PRODUCT_IDS!=null )
            return;
        
        try
        {
            ORG_JUSTICE_PRODUCT_IDS = RuntimeConstants.getIntList("orgJusticeProductIds", ",");
        }
        catch( Exception e )
        {
            LogService.logIt( e, "OrgJusticeUtils.init() " );
        }
    }
    
    
    public List<OrgJusticeTestEvent> findOrgJusticeTestEvents( int orgId, Date startDate, Date endDate, List<Integer> forceProductIds) throws Exception
    {
        List<OrgJusticeTestEvent> out = new ArrayList<>();
            
        try
        {
            init();
            
            if( orgId<=0 )
                throw new Exception( "orgId invalid: " + orgId );
            if( startDate==null )
                throw new Exception( "startDate is null" );
            if( endDate==null )
                throw new Exception( "endDate is null" );
                        
            if( orgJusticeFacade==null )
                orgJusticeFacade=OrgJusticeFacade.getInstance();
            
            List<Integer> productIdList = ORG_JUSTICE_PRODUCT_IDS;
            
            if( forceProductIds!=null && !forceProductIds.isEmpty() )
                productIdList = forceProductIds;
            
            List<Long> teidl = orgJusticeFacade.findTestEventIds(orgId, -1, 0, productIdList, startDate, endDate, TestResultSortType.MOST_RECENT_ACCESS_DESC.getTestResultSortTypeId(), 2000, 0);
            LogService.logIt("OrgJusticeUtils.findOrgJusticeTestEvents() found " + teidl.size() + " testeventids for orgId=" + orgId + ", startDate=" + (startDate==null ? "null" : startDate.toString()) + ", endDate=" + (endDate==null ? "null" : endDate.toString()) );
            
            if( teidl.isEmpty() )
                return out;
            
            if( eventFacade==null )
                eventFacade = EventFacade.getInstance();
            
            if( userFacade==null )
                userFacade = UserFacade.getInstance();
            
            TestEvent te;
            List<TestEventScore> tesl;
            List<ItemResponse> irl;
            OrgJusticeTestEvent ojte;
            User u;
            int skipCount = 0;            
            
            for( Long testEventId : teidl )
            {
                te = eventFacade.getTestEvent( testEventId );
                tesl = eventFacade.getTestEventScoresForTestEvent(testEventId, -1 );
                irl = eventFacade.getItemResponsesForTestEvent( testEventId);
                u = userFacade.getUser( te.getUserId() );
                ojte = new OrgJusticeTestEvent( te, tesl, irl );
                
                if( !ojte.hasValidData() )
                {
                    LogService.logIt("OrgJusticeUtils.findOrgJusticeTestEvents() skipping testEventId=" + testEventId + " because it has invalid data. " + ojte.getDataErrors() + ", orgId=" + orgId );
                    skipCount++;
                    continue;
                }                
                out.add( ojte );
            }
            
            LogService.logIt("OrgJusticeUtils.findOrgJusticeTestEvents() COMPLETE orgId=" + orgId + ", valid events found=" + out.size() + ", events skipped=" + skipCount );
        }
        catch( Exception e )
        {
            LogService.logIt(e, "OrgJusticeUtils.findOrgJusticeTestEvents() orgId=" + orgId + ", startDate=" + (startDate==null ? "null" : startDate.toString()) + ", endDate=" + (endDate==null ? "null" : endDate.toString()) );
            throw e;
        }
        
        return out;
        
    }
    
    public static List<OrgJusticeTestEvent> getDummyData()
    {
        List<OrgJusticeTestEvent> out = new ArrayList<>();
        
        OrgJusticeTestEvent ojte;
        
        for( int i=0; i<20; i++ )
        {
            ojte = new OrgJusticeTestEvent();            
            ojte.testEventId = getRandomInt( 1, 1000 );            
            ojte.userId = i+293;            
            ojte.overall = getRandomFloat( 1f, 5f );            
            ojte.genderTypeId = getRandomBoolean() ? 4 : 5;
            ojte.urim  = getRandomBoolean() ? 1 : 0;
            ojte.age = getRandomInt( 18, 28 );
            ojte.learnerGroup = getRandomInt( 1, 13 );
            ojte.learnerStatus = getRandomInt( 1, 15 );
            ojte.dimensionScores = new float[4];
            for( int j=0;j<4;j++ )
                ojte.dimensionScores[j] = getRandomFloat( 1f, 5f );             
            ojte.groupScores = new float[7];
            for( int j=0;j<7;j++ )
                ojte.groupScores[j] = getRandomFloat( 1f, 5f ); 
            ojte.pairScores = new float[4][7];
            
            UMinnJusticeDimensionType dimType;
            for( int d=0;d<4;d++ )
            {
                dimType = UMinnJusticeDimensionType.getValue(d+1);
                for( int g=0;g<7;g++ )
                {
                    if( !dimType.includesGroupType(g+1) )
                        continue;
                    
                    ojte.pairScores[d][g] = getRandomFloat( 1f, 5f );  
                }
            }

            UMinnJusticeGroupType groupType;

            // UMinnJusticeItemType itemType;
            
            ojte.groupItemScores = new float[7][16];
            
            for( int g=0;g<7;g++ )
            {
                groupType = UMinnJusticeGroupType.getValue(g+1 );
                
                for( int ii=0;ii<16;ii++ )
                {
                    // itemType = UMinnJusticeItemType.getValue(ii+1);
                    
                    if( !groupType.includeItemForGroup( ii+1 ) )
                        continue;
                
                    ojte.groupItemScores[g][ii]=getRandomFloat( 1f, 5f );  
                }
            }
            
            
            LogService.logIt( "Added Dummy Data " + ojte.toString());
            out.add( ojte );
        }
        
        return out;
    }
    
   public static int getItemNumberFromUniqueId( String u )
   {        
        if( u==null || u.isBlank() || !u.contains("-") )
        {
           LogService.logIt( "OrgJusticeUtils.getItemNumberFromUniqueId() ScorableResponse.intnObj.uniqueId is invalid: u=" + u );
           return 0;
        }
        String ss = u.substring( u.lastIndexOf('-')+1, u.length() ).trim();
        if( ss.isBlank() )
        {
           LogService.logIt( "OrgJusticeUtils.getItemNumberFromUniqueId() uniqueId is invalid: u=" + u + ", ss=" + ss );
           return 0;
        }
        try
        {
            return Integer.parseInt(ss);
        }
        catch( NumberFormatException e )
        {
           LogService.logIt( "OrgJusticeUtils.getItemNumberFromUniqueId() " + e.toString() + ", ScorableResponse.intnObj.uniqueId is invalid: u=" + u + ", ss=" + ss );
           return 0;
        }
   }
    
    
    private static boolean getRandomBoolean()
    {
        return Math.random() >= 0.5d;
    }

    private static float getRandomFloat( float min, float max )
    {
        return (float) (Math.random()*(max-min)) + min;
    }

    private static int getRandomInt( int min, int max )
    {
        return (int) (Math.random()*(max-min)) + min;
    }
    
    
    
}
