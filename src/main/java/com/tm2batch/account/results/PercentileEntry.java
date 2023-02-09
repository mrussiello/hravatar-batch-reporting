/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tm2batch.account.results;


import com.tm2batch.util.MessageFactory;
import com.tm2batch.util.NumberUtils;
import java.util.Locale;

/**
 *
 * @author Mike
 */
public class PercentileEntry {

    int maxBarWid;
    String name;
    float percentile;
    boolean approx=false;
    int count;
    boolean last = false;
    Locale locale;

    public PercentileEntry(String nm, float pct, int cnt, int maxWid, Locale loc )
    {
        if( pct>=0 && pct<1f )
            pct = 1;
        if( pct>99f )
            pct=99f;
        
        this.name = nm;
        this.percentile = pct;
        this.count = cnt;
        this.maxBarWid = maxWid;
        this.locale = loc;
    }

    public int getPctBarPix()
    {
        return Math.round(((float)maxBarWid)*((percentile)/100f));
    }

    public boolean getInnerCountOk()
    {
       return percentile>=30;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPercentileStr()
    {
       return NumberUtils.getPctSuffixStr( locale , percentile, 0 ) + (approx ? " (" + MessageFactory.getStringMessage(locale, "g.PctEstZ" ) + ")" : "" );
    }

    public float getPercentile() {
        return percentile;
    }

    public void setPercentile(float percentile) {
        this.percentile = percentile;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isApprox() {
        return approx;
    }

    public void setApprox(boolean approx) {
        this.approx = approx;
    }



}
