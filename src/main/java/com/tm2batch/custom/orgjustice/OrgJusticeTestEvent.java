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
    long userId;
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
    
    
    // [dimension][group]
    float[][] pairScores;
    


    // [group][item index]
    float[][] groupItemScores; 

    
    /*
      Item OJDEM-7-gender
    
    ITEM:
     0 unknown
     Agrender 1
     non-binay 2
     genderquerr 3
     Male 4
     Female 5
     Prefer not answer 998
     Other 999
    
    // However, we are only using
      Male 4
      Female 5
      Other  999
      All OrgJusticeTestEvent records should be set there.
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

    // Used to create dummy data
    public OrgJusticeTestEvent()
    {                
    }
    
    
    public OrgJusticeTestEvent( TestEvent te, List<TestEventScore> tesl, List<ItemResponse> irl )
    {
        this.testEventId=te.getTestEventId();
        this.userId=te.getUserId();
        
        groupScores = new float[7];
        dimensionScores = new float[4];
        
        pairScores = new float[4][7];
        
        groupItemScores = new float[7][16];
        
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

            // competencies are the dimension scores. Additionally these have the competency-group scores.
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
                
                if( tes.getScore2()>0 )
                    pairScores[dimType.getIndex()][0] = tes.getScore2();
                if( tes.getScore3()>0 )
                    pairScores[dimType.getIndex()][1] = tes.getScore3();
                if( tes.getScore4()>0 )
                    pairScores[dimType.getIndex()][2] = tes.getScore4();
                if( tes.getScore5()>0 )
                    pairScores[dimType.getIndex()][3] = tes.getScore5();
                if( tes.getScore6()>0 )
                    pairScores[dimType.getIndex()][4] = tes.getScore6();
                if( tes.getScore7()>0 )
                    pairScores[dimType.getIndex()][5] = tes.getScore7();
                if( tes.getScore8()>0 )
                    pairScores[dimType.getIndex()][6] = tes.getScore8();
            }
        }
        
        //boolean asianPac = false;
        int points = 0;
        
        UMinnJusticeItemType itemType;
        
        
        for( ItemResponse ir : irl )
        {
            groupType = UMinnJusticeGroupType.getForItemUniqueId(ir.getSimletNodeUniqueId());
            
            // save group scores.
            if( groupType!=null )
            {
                itemType = UMinnJusticeItemType.getForItemUniqueId( ir.getSimletNodeUniqueId() );
                
                if( itemType!=null )
                {
                    groupItemScores[groupType.getUminnJusticeGroupTypeId()-1][itemType.getUminnJusticeItemTypeId()-1] = ir.getItemScore();
                }
                else
                    LogService.logIt( "OrgJusticeTestEvent() Cannot identify the itemType for item which has Group Type=" + groupType.getName() + ", itemResponseId=" + ir.getItemResponseId() + ", testEventId=" + ir.getTestEventId() );
            }
            

            // URIM poor
            if( ir.getSimletNodeUniqueId()!=null && ir.getSimletNodeUniqueId().equalsIgnoreCase( "OJDEM-11-childhood-income" ) )
            {
                points = (int) ir.getItemScore();
                if( points==5 || points==6 )
                    urim=1;
                if( urim==0 )
                {
                    String sel = ir.getSelectedValue();
                    sel = sel==null ? "" : sel;
                    if( sel.contains( "$30,000 to $49,999") || 
                        sel.contains( "$29,999 or less") )
                        urim=1;                    
                }
            }

            // URIM Immigrant
            if( urim==0 &&  ir.getSimletNodeUniqueId()!=null && ir.getSimletNodeUniqueId().equalsIgnoreCase( "OJDEM-14-firstgen" ) )
            {
                // 1=yes 0=no 99=Prefer not to answer
                points = (int) ir.getItemScore();
                if( points>=1 && points<99 )
                    urim=1;                
                else
                {
                    String sel = ir.getSelectedValue();
                    sel = sel==null ? "" : sel;
                    if( sel.contains( "Yes") )
                        urim=1;                    
                }

            }
            
            // URIM Ethnicity
            if( urim==0 && ir.getSimletNodeUniqueId()!=null && ir.getSimletNodeUniqueId().equalsIgnoreCase( "OJDEM-2-ethnicity" ) )
            {
                //points = (int) ir.getItemScore();
                //if( points==1 || points==3 || points==5 )
                //{
                //    urim=1;
                //}
                
                //else
                //{
                    String sel = ir.getSelectedValue();
                    sel = sel==null ? "" : sel;
                    if( sel.contains( "Black or African American") || 
                        sel.contains( "Hispanic or Latino/a")  ||
                        sel.contains( "American Indian or Alaska Native")  )
                        urim=1;
                //}
            }

            // URIM Hispanic Category
            /*
            if( urim==0 && ir.getSimletNodeUniqueId()!=null && ir.getSimletNodeUniqueId().equalsIgnoreCase( "OJDEM-5-hispanic-category" ) )
            {
                points = (int) ir.getItemScore();
                if( points==8 )
                {
                    urim=1;
                }
                
                else
                {
                    String sel = ir.getSelectedValue();
                    sel = sel==null ? "" : sel;
                    if( sel.contains( "Puerto Rican")  )
                        urim=1;
                }
            }
            */
            
            // URIM Asian Category
            if(  urim==0 && ir.getSimletNodeUniqueId()!=null && ir.getSimletNodeUniqueId().equalsIgnoreCase( "OJDEM-3-asian-cat" ) )
            {
                //points = (int) ir.getItemScore();
                //if( points==1 || points==2 || points==3 || points==5 || points==6 || points==8 || points==11 || points==12 || points==13 || points==15 || points==16 || points==17 || points==18 )
                //    urim=1;
                //else
                //{
                    String sel = ir.getSelectedValue();
                    sel = sel==null ? "" : sel;
                    if( sel.contains( "Brunese" ) ||
                        sel.contains( "Burmese" ) ||
                        sel.contains( "Cambodian" ) ||
                        sel.contains( "Timor-Leste" ) ||
                        sel.contains( "Hmong" ) ||
                        sel.contains( "Indonesian" ) ||
                        sel.contains( "Laotian" ) ||
                        sel.contains( "Malaysian" ) ||
                        sel.contains( "Filipino" ) ||
                        sel.contains( "Singaporean" ) ||
                        sel.contains( "Thai" ) ||
                        sel.contains( "Vietnamese" ) )
                        urim=1;
                //}
            }
            

            // learner group
            if( ir.getSimletNodeUniqueId()!=null && ir.getSimletNodeUniqueId().equalsIgnoreCase( "OJ-Intro-2-LearnerGroup" ) )
                learnerGroup = (int) ir.getItemScore();

            else if( ir.getSimletNodeUniqueId()!=null && ir.getSimletNodeUniqueId().equalsIgnoreCase( "CJDS-I-16-LearnerStatus" ) )
                learnerStatus = (int) ir.getItemScore();

            // Age
            if( ir.getSimletNodeUniqueId()!=null && ir.getSimletNodeUniqueId().equalsIgnoreCase( "OJDEM-1-age" ) && ir.getSelectedValue()!=null && !ir.getSelectedValue().isBlank() )
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

            // gender
            if( ir.getSimletNodeUniqueId()!=null && ir.getSimletNodeUniqueId().equalsIgnoreCase( "OJDEM-7-gender" ) )
            {                
                //genderTypeId = (int) ir.getItemScore();
                //if( genderTypeId!=4 && genderTypeId!=5 )
                //{
                    String selValue = ir.getSelectedValue();
                    selValue = selValue==null ? "" : selValue;
                    
                    selValue=selValue.trim();
                    if( selValue.equalsIgnoreCase("Woman"))
                    {
                        //if( selValue.contains("Man"))
                        //    genderTypeId=999;
                        //else
                        genderTypeId = 5;
                    }
                    
                    else if( selValue.equalsIgnoreCase("Man"))
                        genderTypeId = 4;  
                    
                    else
                        genderTypeId=999;
                  //}
            }
            
        }
    }
    
    public boolean isMale()
    {
        return this.genderTypeId==4;
    }

    public boolean isFemale()
    {
        return this.genderTypeId==5;
    }
    
    public boolean isUrim()
    {
        return this.urim==1;
    }
    
    public boolean hasValidData()
    {        
        if( groupScores==null || groupScores.length<7 )
        {
            return false;
        }
        
        if( dimensionScores==null || dimensionScores.length<4 )
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
            sb.append( "GroupScores array invalid: " + (groupScores==null ? "null" : "length=" + groupScores.length ) + "\n" );

        else
        {
            for( int i=0;i<groupScores.length;i++ )
            {
                if( groupScores[i]<=0 )
                    sb.append( "Score for group index " + i  + " (" + UMinnJusticeGroupType.getValue( i+1 ).getName() + ") is invalid: " + groupScores[i] + "\n" );
            }            
        }
        
        if( dimensionScores==null || dimensionScores.length<4 )
            sb.append( "DimensionScores array invalid: " + (dimensionScores==null ? "null" : "length=" + dimensionScores.length ) + "\n" );
        else
        {
            for( int i=0;i<dimensionScores.length;i++ )
            {
                if( dimensionScores[i]<=0 )
                    sb.append( "Score for dimension index " + i  + " (" + UMinnJusticeDimensionType.getValue( i+1 ).getName() + ") is invalid: " + dimensionScores[i] + "\n" );
            }            
        }
        
        return sb.toString();
    }

    @Override
    public String toString() {
        
        StringBuilder grp = new StringBuilder();
        for( float s : this.groupScores )
        {
            if( grp.length()>0 )
                grp.append(",");
            grp.append( Float.toString(s));
        }

        StringBuilder dim = new StringBuilder();
        for( float s : this.dimensionScores )
        {
            if( dim.length()>0 )
                dim.append(",");
            dim.append( Float.toString(s));
        }

        
        return "OrgJusticeTestEvent{" + "testEventId=" + testEventId + ", overall=" + overall + ", genderTypeId=" + genderTypeId + ", urim=" + urim + ", age=" + age + ", learnerGroup=" + learnerGroup + ", learnerStatus=" + learnerStatus + " groups: " + grp + ", dimensions=" + dim + '}';
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

    public float[][] getGroupItemScores() {
        return groupItemScores;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    
    
}
