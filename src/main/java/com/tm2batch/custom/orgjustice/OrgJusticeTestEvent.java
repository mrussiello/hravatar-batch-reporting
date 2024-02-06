/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tm2batch.custom.orgjustice;

import com.tm2batch.entity.event.ItemResponse;
import com.tm2batch.entity.event.TestEvent;
import com.tm2batch.entity.event.TestEventScore;
import com.tm2batch.service.LogService;
import java.util.List;

/**
 *
 * @author miker
 */
public class OrgJusticeTestEvent {
    
    long testEventId;
    float overall;
    
    /*
     0 - Clinical supervisor
     1- program leader
     2- interprofessional team member
     3- operations staff
     4- consultant
     5- colleague
     6- patient family
    
    */
    float[] groupScores;
    
    /*
       0 - Interpersonal Justice
       1 - Informational Justice
       2 - Procedural Justice
       3 - Distributive Justice
    
    */
    float[] dimensionScores;
    
    
    /*
      Item OJDEM-7-gender
    
     0 unknown
     Agrender 1
     non-binay 2
     genderquerr 3
     Male 4
     Female 5
     Prefer not answer 998
     Other 999
    */
    int genderTypeId;
    
    
    /*
      Item 
     
     0 no
     1 yes
    */
    int urim;
    
    
    /*
      Item  OJDEM-1-age
    */
    int age;
    
    
    /*
      Item OJ-Intro-2-LearnerGroup
    */
    int learnerGroup;
    
    /* 
      itme CJDS-I-16-LearnerStatus
    */
    int learnerStatus;
    
    
    public OrgJusticeTestEvent( TestEvent te, List<TestEventScore> tesl, List<ItemResponse> irl )
    {
        this.testEventId=te.getTestEventId();
        
        groupScores = new float[7];
        dimensionScores = new float[4];
        
        UMinnJusticeDimensionType dimType;
        UMinnJusticeGroupType groupType;
        
        for( TestEventScore tes : tesl )
        {
            if( tes.getTestEventScoreType().getIsOverall() )
                overall = tes.getScore();
            
            // competencies are the group scores.
            else if( tes.getTestEventScoreType().getIsCompetency() )
            {
                groupType = UMinnJusticeGroupType.getForName( tes.getName() );
                
                // Demographic survey will do this.
                if( groupType==null )
                    continue;
                
                groupScores[groupType.getIndex()] = tes.getScore();
            }

            // competencies are the dimension scores.
            else if( tes.getTestEventScoreType().getIsCompetencyGroup())
            {
                dimType = UMinnJusticeDimensionType.getForName( tes.getName() );
                
                // Should not happen.
                if( dimType==null )
                {
                    LogService.logIt( "OrgJusticeTestEvent() CompetencyGroup TestEventScore does not match any dimension. tes.testEventScoreId=" + tes.getTestEventScoreId() + ", name=" + tes.getName()+", testEventId=" + testEventId );
                    continue;
                }
                
                dimensionScores[dimType.getIndex()] = tes.getScore();
            }
        }
        
        for( ItemResponse ir : irl )
        {
            if( ir.getIdentifier().equalsIgnoreCase( "OJ-Intro-2-LearnerGroup" ) )
                learnerGroup = (int) ir.getItemScore();

            if( ir.getIdentifier().equalsIgnoreCase( "CJDS-I-16-LearnerStatus" ) )
                learnerStatus = (int) ir.getItemScore();

            if( ir.getIdentifier().equalsIgnoreCase( "OJDEM-1-age" ) && ir.getSelectedValue()!=null && !ir.getSelectedValue().isBlank() )
            {
                try
                {
                    age = Integer.parseInt(ir.getSelectedValue());
                }
                catch( NumberFormatException e )
                {
                    LogService.logIt( "OrgJusticeTestEvent cannot parse age. ir.getSelectedValue()=" + ir.getSelectedValue() + ", testEventId=" + testEventId );
                }
            }

            if( ir.getIdentifier().equalsIgnoreCase( "OJDEM-7-gender" ) )
                genderTypeId = (int) ir.getItemScore();
            
        }
    }
    
    public boolean hasValidData()
    {        
        if( groupScores==null || groupScores.length<7 )
            return false;
        
        if( dimensionScores==null || dimensionScores.length<7 )
            return false;
        for( float i : groupScores )
        {
            if( i<=0 )
                return false;
        }
        for( float i : dimensionScores )
        {
            if( i<=0 )
                return false;
        }
        return true;
    }
    
    public String getDataErrors()
    {
        StringBuilder sb = new StringBuilder();
        if( groupScores==null || groupScores.length<7 )
            sb.append( "GroupScores array invalid: " + (groupScores==null ? "null" : "length=" + groupScores.length ) );

        else
        {
            for( int i=0;i<groupScores.length;i++ )
            {
                if( groupScores[i]<=0 )
                    sb.append( "Score for group index " + i  + " (" + UMinnJusticeGroupType.getValue( i+1 ).getName() + ") is invalid: " + groupScores[i] );
            }            
        }
        
        if( dimensionScores==null || dimensionScores.length<7 )
            sb.append( "DimensionScores array invalid: " + (dimensionScores==null ? "null" : "length=" + dimensionScores.length ) );
        else
        {
            for( int i=0;i<dimensionScores.length;i++ )
            {
                if( dimensionScores[i]<=0 )
                    sb.append( "Score for dimension index " + i  + " (" + UMinnJusticeDimensionType.getValue( i+1 ).getName() + ") is invalid: " + dimensionScores[i] );
            }            
        }
        
        return sb.toString();
    }

    public long getTestEventId() {
        return testEventId;
    }

    public void setTestEventId(long testEventId) {
        this.testEventId = testEventId;
    }

    public float getOverall() {
        return overall;
    }

    public void setOverall(float overall) {
        this.overall = overall;
    }

    public float[] getGroupScores() {
        return groupScores;
    }

    public void setGroupScores(float[] groupScores) {
        this.groupScores = groupScores;
    }

    public float[] getDimensionScores() {
        return dimensionScores;
    }

    public void setDimensionScores(float[] dimensionScores) {
        this.dimensionScores = dimensionScores;
    }

    public int getGenderTypeId() {
        return genderTypeId;
    }

    public void setGenderTypeId(int genderTypeId) {
        this.genderTypeId = genderTypeId;
    }

    public int getUrim() {
        return urim;
    }

    public void setUrim(int urim) {
        this.urim = urim;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getLearnerGroup() {
        return learnerGroup;
    }

    public void setLearnerGroup(int learnerGroup) {
        this.learnerGroup = learnerGroup;
    }

    public int getLearnerStatus() {
        return learnerStatus;
    }

    public void setLearnerStatus(int learnerStatus) {
        this.learnerStatus = learnerStatus;
    }
    
    
    
}
