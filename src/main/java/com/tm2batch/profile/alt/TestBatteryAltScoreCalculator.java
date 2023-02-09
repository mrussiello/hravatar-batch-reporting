/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.profile.alt;


import com.tm2batch.entity.battery.Battery;
import com.tm2batch.entity.battery.BatteryScore;
import com.tm2batch.entity.event.TestEvent;
import com.tm2batch.entity.event.TestEventScore;
import com.tm2batch.entity.profile.Profile;
import com.tm2batch.entity.profile.ProfileEntry;
import com.tm2batch.event.TestEventScoreType;
import com.tm2batch.profile.ProfileUsageType;
import com.tm2batch.service.LogService;
import java.util.List;

/**
 *
 * @author miker_000
 */
public class TestBatteryAltScoreCalculator extends BaseAltScoreCalculator implements AltScoreCalculator
{
    BatteryScore batteryScore;
    
    Battery battery;
    
    List<TestEvent> testEventList;
    
    Profile profile;
    
    public TestBatteryAltScoreCalculator( BatteryScore batteryScore, Battery battery, List<TestEvent> tel, Profile p ) throws Exception
    {
        this.batteryScore = batteryScore;
        this.battery = battery;
        this.testEventList = tel;
        this.profile = p;
        
        if( !p.getProfileUsageType().equals( ProfileUsageType.ALTERNATE_BATTERY_OVERALL_WEIGHTS ) && !p.getProfileUsageType().equals( ProfileUsageType.ALTERNATE_BATTERY_COMPETENCY_WEIGHTS ))
            throw new Exception( "Profile has the wrong ProfileUsageType: " + p.toString()  );
    }
    
    
    @Override
    public float getScore()
    {
        float totalScore = 0;
        float totalWeight = 0;
        
        ProfileEntry pe;

        for( TestEvent testEvent : testEventList )
        {
            if( profile.getProfileUsageType().equals( ProfileUsageType.ALTERNATE_BATTERY_OVERALL_WEIGHTS ) )
            {
                LogService.logIt( "Seeking Profile Entry for Overall Weights battery: " + testEvent.getName() );
                pe = profile.getLiveProfileEntry( testEvent.getName(), testEvent.getNameEnglish() );
                
                if( pe == null )
                    continue;

                LogService.logIt( "Yes - Have Profile Entry for Overall Weights battery: " + testEvent.getName() );
                
                totalScore += testEvent.getOverallScore()* pe.getWeight();

                totalWeight += pe.getWeight();                
            }
            
            else
            {

                for( TestEventScore tes : testEvent.getTestEventScoreList( TestEventScoreType.COMPETENCY ) )
                {
                    pe = profile.getLiveProfileEntry( tes.getName(), tes.getNameEnglish() );

                    if( pe == null )
                        continue;

                    totalScore += tes.getScore() * pe.getWeight();

                    totalWeight += pe.getWeight();
                }
            }        
        }

        if( totalWeight <= 0 )
            return -1;

        totalScore = Math.round( totalScore / totalWeight );
        
        return totalScore;
    }
    
    
    @Override
    protected boolean getHasWeights()
    {
        ProfileEntry pe;
        
        for( TestEvent testEvent : testEventList )
        {
            if( profile.getProfileUsageType().equals( ProfileUsageType.ALTERNATE_BATTERY_OVERALL_WEIGHTS ) )
            {
                pe = profile.getLiveProfileEntry( testEvent.getName(), testEvent.getNameEnglish() );
                
                // LogService.logIt( "TestBatteryAltScoreCalculator.getHasWeights() lookling for " + testEvent.getName() + " in ProfileEntries " + ( pe==null ? "Not Found" : "Found weight=" + pe.getWeight()  ) );
                if( pe == null )
                    continue;

                if( pe.getWeight()>0 )
                    return true;
            }
            
            else
            {

                for( TestEventScore tes : testEvent.getTestEventScoreList( TestEventScoreType.COMPETENCY ) )
                {
                    pe = profile.getLiveProfileEntry( tes.getName(), tes.getNameEnglish() );

                    if( pe == null )
                        continue;

                    if( pe.getWeight()>0 )
                        return true;
                }
            }        
        }
        
        return false;
    }
    
    
    
}
