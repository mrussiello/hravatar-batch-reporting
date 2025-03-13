/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.credit;

import com.tm2batch.entity.user.User;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author miker_000
 */
public class CandidateCreditUse {
    
    User user;
    long userId;
    long creditId;
    int creditIndex;
    Date firstUseDate;
    Date lastUseDate;
    
    public CandidateCreditUse()
    {}

    public CandidateCreditUse( long userId, long creditId, int creditIndex )
    {
        this.userId=userId;
        this.creditId=creditId;
        this.creditIndex=creditIndex;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (int) (this.userId ^ (this.userId >>> 32));
        hash = 71 * hash + (int) (this.creditId ^ (this.creditId >>> 32));
        hash = 71 * hash + this.creditIndex;
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
        final CandidateCreditUse other = (CandidateCreditUse) obj;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.creditId != other.creditId) {
            return false;
        }
        if (this.creditIndex != other.creditIndex) {
            return false;
        }
        return true;
    }
    
    public String getCreditIdIndexStr()
    {
        return creditId + "-" + creditIndex;
    }
    
    public boolean getIsExpired()
    {
        return (new Date()).after( getExpireDate() );
    }
            
    
    public Date getExpireDate()
    {
        if( firstUseDate==null )
            return new Date();
        
        Calendar cal = new GregorianCalendar();
        cal.setTime(firstUseDate);
        cal.add( Calendar.DAY_OF_MONTH, 90 );
        return  cal.getTime();                            
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCreditId() {
        return creditId;
    }

    public void setCreditId(long creditId) {
        this.creditId = creditId;
    }

    public int getCreditIndex() {
        return creditIndex;
    }

    public void setCreditIndex(int creditIndex) {
        this.creditIndex = creditIndex;
    }

    public Date getFirstUseDate() {
        return firstUseDate;
    }

    public void setFirstUseDate(Date firstUseDate) {
        this.firstUseDate = firstUseDate;
    }

    public Date getLastUseDate() {
        return lastUseDate;
    }

    public void setLastUseDate(Date lastUseDate) {
        this.lastUseDate = lastUseDate;
    }
    
    
    
}
