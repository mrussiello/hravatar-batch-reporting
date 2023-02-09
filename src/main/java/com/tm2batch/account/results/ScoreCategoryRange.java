/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tm2batch.account.results;

import com.tm2batch.event.ScoreCategoryType;
import com.tm2batch.event.ScoreFormatType;
import com.tm2batch.service.LogService;




/**
 *
 * @author Mike
 */
public class ScoreCategoryRange
{
    int totalPix;
    int scoreCategoryTypeId=0;
    float min=0;
    float max=0;

    int adjRange = 0;

    ScoreCategoryType scoreCategoryType;

    public ScoreCategoryRange( int scoreCategoryTypeId, float min, float max, int totalPix )
    {
        this.scoreCategoryTypeId = scoreCategoryTypeId;
        this.min = min;
        this.max = max;
        this.totalPix = totalPix;

        scoreCategoryType = ScoreCategoryType.getValue( scoreCategoryTypeId );
    }

    public ScoreCategoryRange( String info, int totalPix )
    {
        this.totalPix = totalPix;

        if( info == null || info.isEmpty() )
            return;

        try
        {
            String[] sl = info.split( ";" );

            scoreCategoryTypeId = Integer.parseInt( sl[0] );
            scoreCategoryType = ScoreCategoryType.getValue( scoreCategoryTypeId );
            min = Float.parseFloat( sl[1] );
            max = Float.parseFloat( sl[2] );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "ScoreCategoryRange() Parsing " + info );
        }

        if( min<0 )
            min=0;
        if( max>100 )
            max=100;
    }



    /*
            blue = new BaseColor(0xb8,0xe1,0xe7);
            green = new BaseColor(0xa1,0xff,0x8f);
            yellowgreen = new BaseColor(0xea,0xff,0x8f);
            yellow = new BaseColor(0xff,0xff,0x8f);
            redyellow = new BaseColor(0xff,0xe7,0x8f);
            red = new BaseColor(0xff,0x8f,0x8f);
    */
    public String getRangeColor()
    {
        if( scoreCategoryType.getIsRed() )
            return "#ff8f8f";
        else if( scoreCategoryType.getIsRedYellow() )
            return "#ffe78f";
        else if( scoreCategoryType.getIsYellow() )
            return "#ffff8f";
        else if( scoreCategoryType.getIsYellowGreen() )
            return "#eaff8f";

        // Green
        return "#a1ff8f";
    }

    public int getRangePix( ScoreFormatType sft )
    {

        if( totalPix <= 0 || min>=max )
            return 0;

        return   Math.round(  ((float)totalPix)*((max-min)/(sft.getMax()-sft.getMin()))  );
    }

    public int getAdjustedRangePix( ScoreFormatType sft )
    {
        int r = getRangePix(sft) + adjRange;

        if( r<0 )
            r=0;

        return r;
    }

    public int getMinPix( ScoreFormatType sft )
    {

        if( totalPix <= 0 || min<=0 )
            return 0;

        if( scoreCategoryType==null )
            scoreCategoryType = ScoreCategoryType.getValue( scoreCategoryTypeId );
        
        return Math.round(((float)totalPix)*((min - sft.getMin())/(sft.getMax()-sft.getMin())));
    }


    public boolean getIsValid()
    {
        return scoreCategoryTypeId > 0 && min<max;
    }

    public String getPackedString()
    {
        return scoreCategoryTypeId + ";" + min + ";" + max;
    }

    @Override
    public String toString() {
        return "ScoreCategoryRange{" + "scoreCategoryTypeId=" + scoreCategoryTypeId + ", min=" + min + ", max=" + max + '}';
    }

    public int getScoreCategoryTypeId() {
        return scoreCategoryTypeId;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public int getAdjRange() {
        return adjRange;
    }

    public void setAdjRange(int adjRange) {
        this.adjRange = adjRange;
    }




}
