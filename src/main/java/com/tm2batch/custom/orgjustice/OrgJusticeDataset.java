/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tm2batch.custom.orgjustice;

/**
 *
 * @author miker
 */
public class OrgJusticeDataset 
{
    
    float overallAvg;
    float overallCount;
    
    /*
     0 - Clinical supervisor
     1- program leader
     2- interprofessional team member
     3- operations staff
     4- consultant
     5- colleague
     6- patient family
    
    */
    float[] groupAverages;
    float[] groupAveragesCounts;

    
    float[] groupAveragesMale;
    float[] groupAveragesMaleCounts;

    float[] groupAveragesFemale;
    float[] groupAveragesFemaleCounts;

    float[] groupAveragesUrim;
    float[] groupAveragesUrimCounts;

    float[] groupAveragesNonUrim;
    float[] groupAveragesNonUrimCounts;
    
    /*
       0 - Interpersonal Justice
       1 - Informational Justice
       2 - Procedural Justice
       3 - Distributive Justice
    
    */
    float[] dimensionAverages;
    float[] dimensionAveragesCounts;
    
    float[] dimensionAveragesMale;
    float[] dimensionAveragesMaleCounts;
    
    float[] dimensionAveragesFemale;
    float[] dimensionAveragesFemaleCounts;

    float[] dimensionAveragesUrim;
    float[] dimensionAveragesUrimCounts;

    float[] dimensionAveragesNonUrim;
    float[] dimensionAveragesNonUrimCounts;
    
    private synchronized void init()
    {
        if( groupAverages!=null )
            return;
        
        groupAverages = new float[7];
        groupAveragesCounts = new float[7];

        groupAveragesMale = new float[7];
        groupAveragesMaleCounts = new float[7];

        groupAveragesFemale = new float[7];
        groupAveragesFemaleCounts = new float[7];

        groupAveragesUrim = new float[7];
        groupAveragesUrimCounts = new float[7];

        groupAveragesNonUrim = new float[7];
        groupAveragesNonUrimCounts = new float[7];

        dimensionAverages = new float[4];
        dimensionAveragesCounts = new float[4];

        dimensionAveragesMale = new float[4];
        dimensionAveragesMaleCounts = new float[4];

        dimensionAveragesFemale = new float[4];
        dimensionAveragesFemaleCounts = new float[4];

        dimensionAveragesUrim = new float[4];
        dimensionAveragesUrimCounts = new float[4];

        dimensionAveragesNonUrim = new float[4];
        dimensionAveragesNonUrimCounts = new float[4];
    }
    
    
    public void addTestEvent( OrgJusticeTestEvent ote )
    {
        init();
        
        if( ote.getOverall()>0 )
        {
            this.overallAvg+=ote.getOverall();
            this.overallCount++;
        }
        
        for( int i=0;i<7;i++ )
        {
            if( ote.getGroupScores()[i]>0 )
            {
                groupAverages[i] += ote.getGroupScores()[i];
                groupAveragesCounts[i]++;
                
                if( ote.getGenderTypeId()==4 )
                {
                    groupAveragesMale[i] += ote.getGroupScores()[i];
                    groupAveragesMaleCounts[i]++;                    
                }                    
                else if( ote.getGenderTypeId()==5 )
                {
                    groupAveragesFemale[i] += ote.getGroupScores()[i];
                    groupAveragesFemaleCounts[i]++;
                    
                }                    
                if( ote.getUrim()==1 )
                {
                    groupAveragesUrim[i] += ote.getGroupScores()[i];
                    groupAveragesUrimCounts[i]++;                    
                }    
                else
                {
                    groupAveragesNonUrim[i] += ote.getGroupScores()[i];
                    groupAveragesNonUrimCounts[i]++;                    
                }    
            }
        }
        
        for( int i=0;i<4;i++ )
        {
            if( ote.getDimensionScores()[i]>0 )
            {
                dimensionAverages[i] += ote.getGroupScores()[i];
                dimensionAveragesCounts[i]++;
                
                if( ote.getGenderTypeId()==4 )
                {
                    dimensionAveragesMale[i] += ote.getGroupScores()[i];
                    dimensionAveragesMaleCounts[i]++;                    
                }                    
                else if( ote.getGenderTypeId()==5 )
                {
                    dimensionAveragesFemale[i] += ote.getGroupScores()[i];
                    dimensionAveragesFemaleCounts[i]++;
                    
                }                    
                if( ote.getUrim()==1 )
                {
                    dimensionAveragesUrim[i] += ote.getGroupScores()[i];
                    dimensionAveragesUrimCounts[i]++;                    
                }    
                else
                {
                    dimensionAveragesNonUrim[i] += ote.getGroupScores()[i];
                    dimensionAveragesNonUrimCounts[i]++;                    
                }    
            }
        }
    }
    
    public synchronized void finalizeAverages()
    {
        init();
        
        overallAvg = overallCount>0 ? overallAvg/overallCount : 0;
        
        for( int i=0;i<7;i++ )
        {
            groupAverages[i] = groupAveragesCounts[i]>0 ? groupAverages[i]/groupAveragesCounts[i] : 0;
            groupAveragesMale[i] = groupAveragesMaleCounts[i]>0 ? groupAveragesMale[i]/groupAveragesMaleCounts[i] : 0;
            groupAveragesFemale[i] = groupAveragesFemaleCounts[i]>0 ? groupAveragesFemale[i]/groupAveragesFemaleCounts[i] : 0;
            groupAveragesUrim[i] = groupAveragesUrimCounts[i]>0 ? groupAveragesUrim[i]/groupAveragesUrimCounts[i] : 0;
            groupAveragesNonUrim[i] = groupAveragesNonUrimCounts[i]>0 ? groupAveragesNonUrim[i]/groupAveragesNonUrimCounts[i] : 0;
        }

        for( int i=0;i<4;i++ )
        {
            dimensionAverages[i] = dimensionAveragesCounts[i]>0 ? dimensionAverages[i]/dimensionAveragesCounts[i] : 0;
            dimensionAveragesMale[i] = dimensionAveragesMaleCounts[i]>0 ? dimensionAveragesMale[i]/dimensionAveragesMaleCounts[i] : 0;
            dimensionAveragesFemale[i] = dimensionAveragesFemaleCounts[i]>0 ? dimensionAveragesFemale[i]/dimensionAveragesFemaleCounts[i] : 0;
            dimensionAveragesUrim[i] = dimensionAveragesUrimCounts[i]>0 ? dimensionAveragesUrim[i]/dimensionAveragesUrimCounts[i] : 0;
            dimensionAveragesNonUrim[i] = dimensionAveragesNonUrimCounts[i]>0 ? dimensionAveragesNonUrim[i]/dimensionAveragesNonUrimCounts[i] : 0;
        }        
    }
    
    public String toString()
    {
        init();
        StringBuilder sb = new StringBuilder();
        sb.append( "OrgJusticeDataSet overall: " + overallAvg + " (" + overallCount + ")" );
        sb.append( "\nGroups: ");
        for( int i=0;i<7;i++ )
            sb.append( groupAverages + " (" + groupAveragesCounts[i] + ")\n" );            
        sb.append( "\nGroups Male: ");
        for( int i=0;i<7;i++ )
            sb.append( groupAveragesMale + " (" + groupAveragesMaleCounts[i] + ")\n" );            
        sb.append( "\nGroups Female: ");
        for( int i=0;i<7;i++ )
            sb.append( groupAveragesFemale + " (" + groupAveragesFemaleCounts[i] + ")\n" );            
        sb.append( "\nGroups Urim: ");
        for( int i=0;i<7;i++ )
            sb.append( groupAveragesUrim + " (" + groupAveragesUrimCounts[i] + ")\n" );            
        sb.append( "\nGroups Non-Urim: ");
        for( int i=0;i<7;i++ )
            sb.append( groupAveragesNonUrim + " (" + groupAveragesNonUrimCounts[i] + ")\n" );            

        sb.append( "\nDimensions: ");
        for( int i=0;i<4;i++ )
            sb.append( dimensionAverages + " (" + dimensionAveragesCounts[i] + ")\n" );            
        sb.append( "\nDimensions Male: ");
        for( int i=0;i<4;i++ )
            sb.append( dimensionAveragesMale + " (" + dimensionAveragesMaleCounts[i] + ")\n" );            
        sb.append( "\nDimensions Female: ");
        for( int i=0;i<4;i++ )
            sb.append( dimensionAveragesFemale + " (" + dimensionAveragesFemaleCounts[i] + ")\n" );            
        sb.append( "\nDimensions Urim: ");
        for( int i=0;i<4;i++ )
            sb.append( dimensionAveragesUrim + " (" + dimensionAveragesUrimCounts[i] + ")\n" );            
        sb.append( "\nDimensions Non-Urim: ");
        for( int i=0;i<4;i++ )
            sb.append( dimensionAveragesNonUrim + " (" + dimensionAveragesNonUrimCounts[i] + ")\n" );            

        return sb.toString();        
    }
    
    public void reset()
    {
        groupAverages = null;
        groupAveragesCounts = null;

        groupAveragesMale = null;
        groupAveragesMaleCounts = null;

        groupAveragesFemale = null;
        groupAveragesFemaleCounts = null;

        groupAveragesUrim = null;
        groupAveragesUrimCounts = null;

        groupAveragesNonUrim = null;
        groupAveragesNonUrimCounts = null;

        dimensionAverages = null;
        dimensionAveragesCounts = null;

        dimensionAveragesMale = null;
        dimensionAveragesMaleCounts = null;

        dimensionAveragesFemale = null;
        dimensionAveragesFemaleCounts = null;

        dimensionAveragesUrim = null;
        dimensionAveragesUrimCounts = null;

        dimensionAveragesNonUrim = null;
        dimensionAveragesNonUrimCounts = null;
        
    }
    
}
