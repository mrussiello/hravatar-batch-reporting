/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tm2batch.custom.leaderstyle;

import com.tm2batch.entity.user.Suborg;
import com.tm2batch.entity.user.User;
import com.tm2batch.util.I18nUtils;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author miker
 */
public class LeaderStyleResult implements Comparable<LeaderStyleResult> {
    
    User user;
    Suborg suborg;
    Map<String,Float> scoreMap;
    Date testDate;
    long testEventId;
    int dominantTypeIndex;

    
    public void calculate()
    {
        if( scoreMap==null )
            return;
        
        dominantTypeIndex = LeaderStyleReportUtils.getTopTraitIndex(scoreMap);
    }
        
    public float[] getScoreArray()
    {
        float[] out = new float[4];
        for( int i=0;i<4;i++ )
            out[i] = getValueForStyleIndex( i );
        return out;
    }
    
    public String[] getScoreStrArray( Locale locale, int digits )
    {
        String[] out = new String[4];
        
        for( int i=0;i<4;i++ )
            out[i] = I18nUtils.getFormattedNumber(locale, getValueForStyleIndex( i ), digits);

        return out;
    }
    
    public boolean getIsValid()
    {
        return !(scoreMap==null || scoreMap.size()<5 || dominantTypeIndex<0);
        
    }
    
    public float getValueForStyleIndex( int styleIndex )
    {
        return getValueForStyleLetter( LeaderStyleReportUtils.LEADER_STYLE_STUBS[styleIndex] );
    }
    
    public float getValueForStyleLetter( String styleLetter )
    {
        if( scoreMap==null || !scoreMap.containsKey( styleLetter ) )
            return 0;
        
        return scoreMap.get( styleLetter );
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.testEventId ^ (this.testEventId >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LeaderStyleResult other = (LeaderStyleResult) obj;
        return this.testEventId == other.testEventId;
    }

    
    
    
    
    @Override
    public int compareTo(LeaderStyleResult o) 
    {
        if( o.dominantTypeIndex!=dominantTypeIndex )
            return Integer.valueOf(dominantTypeIndex).compareTo( o.getDominantTypeIndex());
        
        if( o.getUser()!=null && user!=null && user.getUserId()!=o.getUser().getUserId() )
        {
            return user.compareTo(o.getUser() );
        }
        
        if( testDate!=null && o.getTestDate()!=null )
            return testDate.compareTo(o.getTestDate());
        
        if( testEventId!=o.getTestEventId() )
            return Long.valueOf(testEventId).compareTo(o.getTestEventId());
        
        return 0;
    }

    
    
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Suborg getSuborg() {
        return suborg;
    }

    public void setSuborg(Suborg suborg) {
        this.suborg = suborg;
    }

    public Map<String, Float> getScoreMap() {
        return scoreMap;
    }

    public void setScoreMap(Map<String, Float> scoreMap) {
        this.scoreMap = scoreMap;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public int getDominantTypeIndex() {
        return dominantTypeIndex;
    }

    public void setDominantTypeIndex(int dominantTypeIndex) {
        this.dominantTypeIndex = dominantTypeIndex;
    }

    public long getTestEventId() {
        return testEventId;
    }

    public void setTestEventId(long testEventId) {
        this.testEventId = testEventId;
    }

}
