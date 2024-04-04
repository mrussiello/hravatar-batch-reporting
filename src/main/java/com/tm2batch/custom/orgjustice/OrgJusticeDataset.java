/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tm2batch.custom.orgjustice;

import com.tm2batch.service.LogService;
import java.util.Date;

/**
 *
 * @author miker
 */
public class OrgJusticeDataset 
{
    static boolean DEBUG = true;
    
    int totalParticipants;
    
    Date[] dates;
    
    float overallAvg;
    float overallCount;

    float overallAvgMale;
    float overallCountMale;

    float overallAvgFemale;
    float overallCountFemale;

    float overallAvgOther;
    float overallCountOther;

    
    
    float overallAvgUrim;
    float overallCountUrim;

    float overallAvgNonUrim;
    float overallCountNonUrim;

    
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
    
    float[][] dimensionGroupAverages;
    float[][] dimensionGroupCounts;
    
    
    float[] itemScoreAverages;
    float[] itemScoreCounts;
    
    float[] itemScoreAveragesMale;
    float[] itemScoreCountsMale;
    
    float[] itemScoreAveragesFemale;
    float[] itemScoreCountsFemale;

    float[] itemScoreAveragesOther;
    float[] itemScoreCountsOther;
    
    
    float[] itemScoreAveragesUrim;
    float[] itemScoreCountsUrim;
    
    float[] itemScoreAveragesNonUrim;
    float[] itemScoreCountsNonUrim;

    // group id (0-70, item id (1-16)
    float[][] groupItemScoreAverages;
    float[][] groupItemScoreCounts;
    
    // group id (0-70, item id (1-16)
    float[][] groupItemScoreAveragesMale;
    float[][] groupItemScoreCountsMale;

    // group id (0-70, item id (1-16)
    float[][] groupItemScoreAveragesFemale;
    float[][] groupItemScoreCountsFemale;

    float[][] groupItemScoreAveragesOther;
    float[][] groupItemScoreCountsOther;
    
    // group id (0-70, item id (1-16)
    float[][] groupItemScoreAveragesUrim;
    float[][] groupItemScoreCountsUrim;

    // group id (0-70, item id (1-16)
    float[][] groupItemScoreAveragesNonUrim;
    float[][] groupItemScoreCountsNonUrim;
    
    
    float[] groupAveragesMale;
    float[] groupAveragesMaleCounts;

    float[] groupAveragesFemale;
    float[] groupAveragesFemaleCounts;

    float[] groupAveragesOther;
    float[] groupAveragesOtherCounts;

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

    float[] dimensionAveragesOther;
    float[] dimensionAveragesOtherCounts;

    float[] dimensionAveragesUrim;
    float[] dimensionAveragesUrimCounts;

    float[] dimensionAveragesNonUrim;
    float[] dimensionAveragesNonUrimCounts;
    
    private synchronized void init()
    {
        if( groupAverages!=null )
            return;

        overallAvg=0;
        overallCount=0;
        
        overallAvgMale=0;
        overallCountMale=0;

        overallAvgFemale=0;
        overallCountFemale=0;

        overallAvgOther=0;
        overallCountOther=0;
        
        overallAvgUrim=0;
        overallCountUrim=0;

        overallAvgNonUrim=0;
        overallCountNonUrim=0;
        
        groupAverages = new float[7];
        groupAveragesCounts = new float[7];

        groupAveragesMale = new float[7];
        groupAveragesMaleCounts = new float[7];

        groupAveragesFemale = new float[7];
        groupAveragesFemaleCounts = new float[7];

        groupAveragesOther = new float[7];
        groupAveragesOtherCounts = new float[7];
        
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

        dimensionAveragesOther = new float[4];
        dimensionAveragesOtherCounts = new float[4];

        dimensionAveragesUrim = new float[4];
        dimensionAveragesUrimCounts = new float[4];

        dimensionAveragesNonUrim = new float[4];
        dimensionAveragesNonUrimCounts = new float[4];
        
        dimensionGroupAverages = new float[4][7];
        dimensionGroupCounts = new float[4][7];
        
        groupItemScoreAverages = new float[7][16];
        groupItemScoreCounts = new float[7][16];;        

         groupItemScoreAveragesMale = new float[7][16];
         groupItemScoreCountsMale = new float[7][16];

         groupItemScoreAveragesFemale = new float[7][16];
         groupItemScoreCountsFemale = new float[7][16];

         groupItemScoreAveragesOther = new float[7][16];
         groupItemScoreCountsOther = new float[7][16];
         
         groupItemScoreAveragesUrim = new float[7][16];
         groupItemScoreCountsUrim = new float[7][16];

         groupItemScoreAveragesNonUrim = new float[7][16];
         groupItemScoreCountsNonUrim = new float[7][16];
         
        itemScoreAverages = new float[16];
        itemScoreCounts = new float[16];

        itemScoreAveragesMale = new float[16];
        itemScoreCountsMale = new float[16];

        itemScoreAveragesFemale = new float[16];
        itemScoreCountsFemale = new float[16];

        itemScoreAveragesOther = new float[16];
        itemScoreCountsOther = new float[16];

        itemScoreAveragesUrim = new float[16];
        itemScoreCountsUrim = new float[16];

        itemScoreAveragesNonUrim = new float[16];
        itemScoreCountsNonUrim = new float[16];
    }
    
    
    public String getFemaleBelowMaleDimensionNames()
    {
        StringBuilder sb = new StringBuilder();
        UMinnJusticeDimensionType type;
        for( int i=0;i<4;i++ )
        {
            if( dimensionAveragesFemale[i]<dimensionAveragesMale[i] )
            {
                type = UMinnJusticeDimensionType.getValue(i+1 );
                if( sb.length()>0 )
                    sb.append( ", " );
                sb.append(type.getName() );
            }
        }        

        if( sb.isEmpty() )
            return "None";
        
        return sb.toString();
    }

    public String getFemaleBelowMaleGroupNames()
    {
        StringBuilder sb = new StringBuilder();
        UMinnJusticeGroupType type;
        for( int i=0;i<7;i++ )
        {
            if( groupAveragesFemale[i]<groupAveragesMale[i] )
            {
                type = UMinnJusticeGroupType.getValue(i+1 );
                if( sb.length()>0 )
                    sb.append( ", " );
                sb.append(type.getName() );
            }
        }        
        if( sb.isEmpty() )
            return "None";
        
        return sb.toString();
    }
    
    public String getUrimBelowNonDimensionNames()
    {
        StringBuilder sb = new StringBuilder();
        UMinnJusticeDimensionType type;
        for( int i=0;i<4;i++ )
        {
            if( dimensionAveragesUrim[i]<dimensionAveragesNonUrim[i] )
            {
                type = UMinnJusticeDimensionType.getValue(i+1 );
                if( sb.length()>0 )
                    sb.append( ", " );
                sb.append(type.getName() );
            }
        }        
        if( sb.isEmpty() )
            return "None";
        
        return sb.toString();
    }

    public String getUrimBelowNonGroupNames()
    {
        StringBuilder sb = new StringBuilder();
        UMinnJusticeGroupType type;
        for( int i=0;i<7;i++ )
        {
            if( this.groupAveragesUrim[i]<this.groupAveragesNonUrim[i] )
            {
                type = UMinnJusticeGroupType.getValue(i+1 );
                if( sb.length()>0 )
                    sb.append( ", " );
                sb.append(type.getName() );
            }
        }        
        if( sb.isEmpty() )
            return "None";
        
        return sb.toString();
    }
    
    
    public String getTopDimensionName()
    {
        int idx = getTopDimensionIndex();
        return UMinnJusticeDimensionType.getValue(idx+1).getName();
    }
    
    public int getTopDimensionIndex()
    {
        int idx = 0;
        float max=0;
        for( int i=0;i<4;i++ )
        {
            if( dimensionAverages[i]> max )
            {
                idx = i;
                max=dimensionAverages[i];
            }
        }
        return idx;
    }

    public String getTopGroupName()
    {
        int idx = getTopGroupIndex();
        return UMinnJusticeGroupType.getValue(idx+1).getName();
    }
    
    public int getTopGroupIndex()
    {
        int idx = 0;
        float max=0;
        for( int i=0;i<7;i++ )
        {
            if( groupAverages[i]> max )
            {
                idx = i;
                max=groupAverages[i];
            }
        }
        return idx;
    }

    public String getBottomDimensionName()
    {
        int idx = getBottomDimensionIndex();
        return UMinnJusticeDimensionType.getValue(idx+1).getName();
    }
    
    public int getBottomDimensionIndex()
    {
        int idx = 0;
        float min=999;
        for( int i=0;i<4;i++ )
        {
            if( dimensionAverages[i]<min )
            {
                idx = i;
                min=dimensionAverages[i];
            }
        }
        return idx;
    }

    public String getBottomGroupName()
    {
        int idx = getBottomGroupIndex();
        return UMinnJusticeGroupType.getValue(idx+1).getName();
    }
    
    public int getBottomGroupIndex()
    {
        int idx = 0;
        float min=999;
        for( int i=0;i<7;i++ )
        {
            if( groupAverages[i]<min )
            {
                idx = i;
                min=groupAverages[i];
            }
        }
        return idx;
    }
    

    public String[] getTopDimensionGroupNamePair()
    {
        int[] pair = getTopDimensionGroupIndex();
        
        if( pair==null || pair[0]<0 || pair[1]<0 )
            return new String[]{"None","None"};
        
        return new String[]{UMinnJusticeDimensionType.getValue(pair[0]+1).getName(),UMinnJusticeGroupType.getValue(pair[1]+1).getName()};
    }
    
    // returns 
    // data[0] = dimension index
    // data[1] = group index
    public int[] getTopDimensionGroupIndex()
    {
        UMinnJusticeDimensionType dimType;
        int dimIdx = -1;
        int groupIdx = -1;
        
        float max=0;
        for( int i=0; i<4; i++ )
        {
            dimType = UMinnJusticeDimensionType.getValue(i+1);
            
            for( int g=0; g<7; g++ )
            {
                if( !dimType.includesGroupType(g+1) )
                    continue;
                
                if( dimensionGroupAverages[i][g]>max )
                {
                    dimIdx = i;
                    groupIdx = g;
                    max = dimensionGroupAverages[i][g];
                }
            }
        }
        return new int[] {dimIdx,groupIdx};
    }


    public String[] getBottomDimensionGroupNamePair()
    {
        int[] pair = getBottomDimensionGroupIndex();
        
        if( pair==null || pair[0]<0 || pair[1]<0 )
            return new String[]{"None","None"};
        
        return new String[]{UMinnJusticeDimensionType.getValue(pair[0]+1).getName(),UMinnJusticeGroupType.getValue(pair[1]+1).getName()};
    }
    
    // returns 
    // data[0] = dimension index
    // data[1] = group index
    public int[] getBottomDimensionGroupIndex()
    {
        UMinnJusticeDimensionType dimType;
        int dimIdx = -1;
        int groupIdx = -1;
        
        float min=999;
        for( int i=0; i<4; i++ )
        {
            dimType = UMinnJusticeDimensionType.getValue(i+1);
            
            for( int g=0; g<7; g++ )
            {
                if( !dimType.includesGroupType(g+1) )
                    continue;
                
                if( dimensionGroupAverages[i][g]<min )
                {
                    dimIdx = i;
                    groupIdx = g;
                    min = dimensionGroupAverages[i][g];
                }
            }
        }
        return new int[] {dimIdx,groupIdx};
    }
    
    
    
    public void addTestEvent( OrgJusticeTestEvent ote )
    {
        init();
        
        if( DEBUG )
            LogService.logIt( "OrgJusticeDataset.addTestEvent() adding " + ote.toString() );
        
        if( ote.getOverall()>0 )
        {
            this.overallAvg+=ote.getOverall();
            this.overallCount++;
            
            if( ote.getGenderTypeId()==4 )
            {
                overallAvgMale += ote.getOverall();
                overallCountMale++;
            }
            else if( ote.getGenderTypeId()==5 )
            {
                overallAvgFemale += ote.getOverall();
                overallCountFemale++;
            }
            else
            {
                overallAvgOther += ote.getOverall();
                overallCountOther++;
            }
            
            if( ote.getUrim()==1 )
            {
                overallAvgUrim += ote.getOverall();
                overallCountUrim++;
            }
            else
            {
                overallAvgNonUrim += ote.getOverall();
                overallCountNonUrim++;
            }
        }

        // for each group
        for( int i=0;i<7;i++ )
        {
            // has group i
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
                else
                {
                    groupAveragesOther[i] += ote.getGroupScores()[i];
                    groupAveragesOtherCounts[i]++;
                    
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
                
                for( int ii=0;ii<16;ii++ )
                {
                    if( ote.getGroupItemScores()[i][ii]>0 )
                    {
                        groupItemScoreAverages[i][ii] += ote.getGroupItemScores()[i][ii];
                        groupItemScoreCounts[i][ii]++; 
                        
                        itemScoreAverages[ii] += ote.getGroupItemScores()[i][ii];
                        itemScoreCounts[ii]++;
                        
                        if( ote.getGenderTypeId()==4 )
                        {
                            groupItemScoreAveragesMale[i][ii] += ote.getGroupItemScores()[i][ii];
                            groupItemScoreCountsMale[i][ii]++;                    

                            itemScoreAveragesMale[ii] += ote.getGroupItemScores()[i][ii];
                            itemScoreCountsMale[ii]++;

                        }                    
                        else if( ote.getGenderTypeId()==5 )
                        {
                            groupItemScoreAveragesFemale[i][ii] += ote.getGroupItemScores()[i][ii];
                            groupItemScoreCountsFemale[i][ii]++;         
                            
                            itemScoreAveragesFemale[ii] += ote.getGroupItemScores()[i][ii];
                            itemScoreCountsFemale[ii]++;
                            
                        }                    
                        else
                        {
                            groupItemScoreAveragesOther[i][ii] += ote.getGroupItemScores()[i][ii];
                            groupItemScoreCountsOther[i][ii]++;         
                            
                            itemScoreAveragesOther[ii] += ote.getGroupItemScores()[i][ii];
                            itemScoreCountsOther[ii]++;                           
                        }                    

                        if( ote.getUrim()==1 )
                        {
                            groupItemScoreAveragesUrim[i][ii] += ote.getGroupItemScores()[i][ii];
                            groupItemScoreCountsUrim[i][ii]++;   
                            
                            itemScoreAveragesUrim[ii] += ote.getGroupItemScores()[i][ii];
                            itemScoreCountsUrim[ii]++;
                        }    
                        else
                        {
                            groupItemScoreAveragesNonUrim[i][ii] += ote.getGroupItemScores()[i][ii];
                            groupItemScoreCountsNonUrim[i][ii]++;                    

                            itemScoreAveragesNonUrim[ii] += ote.getGroupItemScores()[i][ii];
                            itemScoreCountsNonUrim[ii]++;
                        } 
                    }
                }                
            }
        }
        
        for( int i=0;i<4;i++ )
        {
            if( ote.getDimensionScores()[i]>0 )
            {
                dimensionAverages[i] += ote.getDimensionScores()[i];
                dimensionAveragesCounts[i]++;
                
                if( ote.getGenderTypeId()==4 )
                {
                    dimensionAveragesMale[i] += ote.getDimensionScores()[i];
                    dimensionAveragesMaleCounts[i]++;                    
                }                    
                else if( ote.getGenderTypeId()==5 )
                {
                    dimensionAveragesFemale[i] += ote.getDimensionScores()[i];
                    dimensionAveragesFemaleCounts[i]++;                    
                }                    
                else
                {
                    dimensionAveragesOther[i] += ote.getDimensionScores()[i];
                    dimensionAveragesOtherCounts[i]++;                    
                }                    
                if( ote.getUrim()==1 )
                {
                    dimensionAveragesUrim[i] += ote.getDimensionScores()[i];
                    dimensionAveragesUrimCounts[i]++;                    
                }    
                else
                {
                    dimensionAveragesNonUrim[i] += ote.getDimensionScores()[i];
                    dimensionAveragesNonUrimCounts[i]++;                    
                } 
                
                // for each group for this dimension
                for( int gg=0;gg<7;gg++ )
                {
                    if( ote.pairScores[i][gg]>0 )
                    {
                        dimensionGroupAverages[i][gg] += ote.pairScores[i][gg];
                        dimensionGroupCounts[i][gg]++;
                    }
                                        
                }
            }
        }
    }
    
    public synchronized void finalizeAverages()
    {
        init();
        
        overallAvg = overallCount>0 ? overallAvg/overallCount : 0;
        overallAvgMale = overallCountMale>0 ? overallAvgMale/overallCountMale : 0;
        overallAvgFemale = overallCountFemale>0 ? overallAvgFemale/overallCountFemale : 0;
        overallAvgOther = overallCountOther>0 ? overallAvgOther/overallCountOther : 0;
        overallAvgUrim = overallCountUrim>0 ? overallAvgUrim/overallCountUrim : 0;
        overallAvgNonUrim = overallCountNonUrim>0 ? overallAvgNonUrim/overallCountNonUrim : 0;
        
        // group
        for( int i=0;i<7;i++ )
        {
            groupAverages[i] = groupAveragesCounts[i]>0 ? groupAverages[i]/groupAveragesCounts[i] : 0;
            groupAveragesMale[i] = groupAveragesMaleCounts[i]>0 ? groupAveragesMale[i]/groupAveragesMaleCounts[i] : 0;
            groupAveragesFemale[i] = groupAveragesFemaleCounts[i]>0 ? groupAveragesFemale[i]/groupAveragesFemaleCounts[i] : 0;
            groupAveragesOther[i] = groupAveragesOtherCounts[i]>0 ? groupAveragesOther[i]/groupAveragesOtherCounts[i] : 0;
            groupAveragesUrim[i] = groupAveragesUrimCounts[i]>0 ? groupAveragesUrim[i]/groupAveragesUrimCounts[i] : 0;
            groupAveragesNonUrim[i] = groupAveragesNonUrimCounts[i]>0 ? groupAveragesNonUrim[i]/groupAveragesNonUrimCounts[i] : 0;
            
            // item
            for( int ii=0;ii<16;ii++ )
            {
                groupItemScoreAverages[i][ii] = groupItemScoreCounts[i][ii]>0 ? groupItemScoreAverages[i][ii]/groupItemScoreCounts[i][ii] : 0;
                groupItemScoreAveragesMale[i][ii] = groupItemScoreCountsMale[i][ii]>0 ? groupItemScoreAveragesMale[i][ii]/groupItemScoreCountsMale[i][ii] : 0;
                groupItemScoreAveragesFemale[i][ii] = groupItemScoreCountsFemale[i][ii]>0 ? groupItemScoreAveragesFemale[i][ii]/groupItemScoreCountsFemale[i][ii] : 0;
                groupItemScoreAveragesOther[i][ii] = groupItemScoreCountsOther[i][ii]>0 ? groupItemScoreAveragesOther[i][ii]/groupItemScoreCountsOther[i][ii] : 0;
                groupItemScoreAveragesUrim[i][ii] = groupItemScoreCountsUrim[i][ii]>0 ? groupItemScoreAveragesUrim[i][ii]/groupItemScoreCountsUrim[i][ii] : 0;
                groupItemScoreAveragesNonUrim[i][ii] = groupItemScoreCountsNonUrim[i][ii]>0 ? groupItemScoreAveragesNonUrim[i][ii]/groupItemScoreCountsNonUrim[i][ii] : 0;
            }            
        }

        for( int i=0;i<4;i++ )
        {
            dimensionAverages[i] = dimensionAveragesCounts[i]>0 ? dimensionAverages[i]/dimensionAveragesCounts[i] : 0;
            dimensionAveragesMale[i] = dimensionAveragesMaleCounts[i]>0 ? dimensionAveragesMale[i]/dimensionAveragesMaleCounts[i] : 0;
            dimensionAveragesFemale[i] = dimensionAveragesFemaleCounts[i]>0 ? dimensionAveragesFemale[i]/dimensionAveragesFemaleCounts[i] : 0;
            dimensionAveragesOther[i] = dimensionAveragesOtherCounts[i]>0 ? dimensionAveragesOther[i]/dimensionAveragesOtherCounts[i] : 0;
            dimensionAveragesUrim[i] = dimensionAveragesUrimCounts[i]>0 ? dimensionAveragesUrim[i]/dimensionAveragesUrimCounts[i] : 0;
            dimensionAveragesNonUrim[i] = dimensionAveragesNonUrimCounts[i]>0 ? dimensionAveragesNonUrim[i]/dimensionAveragesNonUrimCounts[i] : 0;
            
            // for each group for this dimension
            for( int gg=0;gg<7;gg++ )
            {
                dimensionGroupAverages[i][gg] = dimensionGroupCounts[i][gg]>0 ? dimensionGroupAverages[i][gg]/dimensionGroupCounts[i][gg] : 0;
            }            
        }

        // item
        for( int i=0; i<16; i++ )
        {
            itemScoreAverages[i] = itemScoreCounts[i]>0 ? itemScoreAverages[i]/itemScoreCounts[i] : 0;
            itemScoreAveragesMale[i] = itemScoreCountsMale[i]>0 ? itemScoreAveragesMale[i]/itemScoreCountsMale[i] : 0;
            itemScoreAveragesFemale[i] = itemScoreCountsFemale[i]>0 ? itemScoreAveragesFemale[i]/itemScoreCountsFemale[i] : 0;
            itemScoreAveragesOther[i] = itemScoreCountsOther[i]>0 ? itemScoreAveragesOther[i]/itemScoreCountsOther[i] : 0;
            itemScoreAveragesUrim[i] = itemScoreCountsUrim[i]>0 ? itemScoreAveragesUrim[i]/itemScoreCountsUrim[i] : 0;
            itemScoreAveragesNonUrim[i] = itemScoreCountsNonUrim[i]>0 ? itemScoreAveragesNonUrim[i]/itemScoreCountsNonUrim[i] : 0;
            
        }

    }
    
    @Override
    public String toString()
    {
        init();
        StringBuilder sb = new StringBuilder();
        sb.append( "OrgJusticeDataSet overall: " + overallAvg + " (" + overallCount + ")\n" );
        sb.append( "\nGroups:\n");
        for( int i=0;i<7;i++ )
            sb.append( UMinnJusticeGroupType.getValue(i+1).getName() + " overall avg: "+ groupAverages[i] + " (cnt=" + groupAveragesCounts[i] + ")\n" );            
        sb.append( "\nGroups Male: ");
        for( int i=0;i<7;i++ )
            sb.append( UMinnJusticeGroupType.getValue(i+1).getName() + " male avg: "+ groupAveragesMale[i] + " (cnt=" + groupAveragesMaleCounts[i] + ")\n" );            
        sb.append( "\nGroups Female:\n");
        for( int i=0;i<7;i++ )
            sb.append( UMinnJusticeGroupType.getValue(i+1).getName() + " female avg: "+ groupAveragesFemale[i] + " (cnt=" + groupAveragesFemaleCounts[i] + ")\n" );            
        sb.append( "\nGroups Urim:\n");
        for( int i=0;i<7;i++ )
            sb.append( UMinnJusticeGroupType.getValue(i+1).getName() + " urim avg: "+ groupAveragesUrim[i] + " (cnt=" + groupAveragesUrimCounts[i] + ")\n" );            
        sb.append( "\nGroups Non-Urim:\n");
        for( int i=0;i<7;i++ )
            sb.append( UMinnJusticeGroupType.getValue(i+1).getName() + " non-urim avg: "+ groupAveragesNonUrim[i] + " (cnt=" + groupAveragesNonUrimCounts[i] + ")\n" );            

        sb.append( "\nDimensions:\n");
        for( int i=0;i<4;i++ )
            sb.append( UMinnJusticeDimensionType.getValue(i+1).getName() + " overall avg: "+ dimensionAverages[i] + " (cnt=" + dimensionAveragesCounts[i] + ")\n" );            
        sb.append( "\nDimensions Male:\n");
        for( int i=0;i<4;i++ )
            sb.append( UMinnJusticeDimensionType.getValue(i+1).getName() + " male avg: "+ dimensionAveragesMale[i] + " (cnt=" + dimensionAveragesMaleCounts[i] + ")\n" );            
        sb.append( "\nDimensions Female:\n");
        for( int i=0;i<4;i++ )
            sb.append( UMinnJusticeDimensionType.getValue(i+1).getName() + " female avg: "+ dimensionAveragesFemale[i] + " (cnt=" + dimensionAveragesFemaleCounts[i] + ")\n" );            
        sb.append( "\nDimensions Urim:\n");
        for( int i=0;i<4;i++ )
            sb.append( UMinnJusticeDimensionType.getValue(i+1).getName() + " urim avg: "+ dimensionAveragesUrim[i] + " (" + dimensionAveragesUrimCounts[i] + ")\n" );            
        sb.append( "\nDimensions Non-Urim:\n");
        for( int i=0;i<4;i++ )
            sb.append( UMinnJusticeDimensionType.getValue(i+1).getName() + " non-urim avg: "+ dimensionAveragesNonUrim[i] + " (" + dimensionAveragesNonUrimCounts[i] + ")\n" );            

        return sb.toString();        
    }
    
    public void reset()
    {
        overallAvg=0;
        overallCount=0;
        
        overallAvgMale=0;
        overallCountMale=0;

        overallAvgFemale=0;
        overallCountFemale=0;

        overallAvgUrim=0;
        overallCountUrim=0;

        overallAvgNonUrim=0;
        overallCountNonUrim=0;
        
        totalParticipants=0;
        
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
        
        dimensionGroupAverages = null;
        dimensionGroupCounts = null;
        
        groupItemScoreAverages = null;
        groupItemScoreCounts = null;

        itemScoreAverages = null;
        itemScoreCounts = null;

        itemScoreAveragesMale = null;
        itemScoreCountsMale = null;

        itemScoreAveragesFemale = null;
        itemScoreCountsFemale = null;

        itemScoreAveragesUrim = null;
        itemScoreCountsUrim = null;

        itemScoreAveragesNonUrim = null;
        itemScoreCountsNonUrim = null;
        
        
    }

    public float getOverallAvg() {
        return overallAvg;
    }

    public float[] getGroupAverages() {
        return groupAverages;
    }

    public float[] getDimensionAverages() {
        return dimensionAverages;
    }

    public float[][] getDimensionGroupAverages() {
        return dimensionGroupAverages;
    }

    public float getOverallAvgMale() {
        return overallAvgMale;
    }

    public void setOverallAvgMale(float overallAvgMale) {
        this.overallAvgMale = overallAvgMale;
    }

    public float getOverallAvgFemale() {
        return overallAvgFemale;
    }

    public float getOverallAvgOther() {
        return overallAvgOther;
    }

    public void setOverallAvgFemale(float overallAvgFemale) {
        this.overallAvgFemale = overallAvgFemale;
    }

    public float getOverallAvgUrim() {
        return overallAvgUrim;
    }

    public void setOverallAvgUrim(float overallAvgUrim) {
        this.overallAvgUrim = overallAvgUrim;
    }

    public float getOverallAvgNonUrim() {
        return overallAvgNonUrim;
    }

    public void setOverallAvgNonUrim(float overallAvgNonUrim) {
        this.overallAvgNonUrim = overallAvgNonUrim;
    }

    public float[] getGroupAveragesMale() {
        return groupAveragesMale;
    }

    public float[] getGroupAveragesFemale() {
        return groupAveragesFemale;
    }

    public float[] getGroupAveragesOther() {
        return groupAveragesOther;
    }

    public float[] getGroupAveragesUrim() {
        return groupAveragesUrim;
    }

    public float[] getGroupAveragesNonUrim() {
        return groupAveragesNonUrim;
    }

    public float[] getDimensionAveragesMale() {
        return dimensionAveragesMale;
    }

    public float[] getDimensionAveragesFemale() {
        return dimensionAveragesFemale;
    }

    public float[] getDimensionAveragesOther() {
        return dimensionAveragesOther;
    }

    public float[] getDimensionAveragesUrim() {
        return dimensionAveragesUrim;
    }

    public float[] getDimensionAveragesNonUrim() {
        return dimensionAveragesNonUrim;
    }

    public float[][] getGroupItemScoreAverages() {
        return groupItemScoreAverages;
    }

    public float[][] getGroupItemScoreAveragesMale() {
        return groupItemScoreAveragesMale;
    }

    public float[][] getGroupItemScoreAveragesOther() {
        return groupItemScoreAveragesOther;
    }

    public float[][] getGroupItemScoreAveragesFemale() {
        return groupItemScoreAveragesFemale;
    }

    public float[][] getGroupItemScoreAveragesUrim() {
        return groupItemScoreAveragesUrim;
    }

    public float[][] getGroupItemScoreAveragesNonUrim() {
        return groupItemScoreAveragesNonUrim;
    }

    public float[] getItemScoreAverages() {
        return itemScoreAverages;
    }

    public float[] getItemScoreAveragesMale() {
        return itemScoreAveragesMale;
    }

    public float[] getItemScoreAveragesFemale() {
        return itemScoreAveragesFemale;
    }

    public float[] getItemScoreAveragesOther() {
        return itemScoreAveragesOther;
    }

    public float[] getItemScoreAveragesUrim() {
        return itemScoreAveragesUrim;
    }

    public float[] getItemScoreAveragesNonUrim() {
        return itemScoreAveragesNonUrim;
    }

    public int getTotalParticipants() {
        return totalParticipants;
    }

    public void setTotalParticipants(int totalParticipants) {
        this.totalParticipants = totalParticipants;
    }

    public Date[] getDates() {
        return dates;
    }

    public void setDates(Date[] dates) {
        this.dates = dates;
    }

    public float getOverallCount() {
        return overallCount;
    }

    public float getOverallCountMale() {
        return overallCountMale;
    }

    public float getOverallCountFemale() {
        return overallCountFemale;
    }
    public float getOverallCountOther() {
        return overallCountOther;
    }


    public float getOverallCountUrim() {
        return overallCountUrim;
    }

    public float getOverallCountNonUrim() {
        return overallCountNonUrim;
    }

    
    
    
}
