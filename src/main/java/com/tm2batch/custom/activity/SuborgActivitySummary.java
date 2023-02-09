/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.activity;

import com.tm2batch.entity.user.Suborg;
import com.tm2batch.user.UserCompanyStatusType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Mike
 */
public class SuborgActivitySummary implements Serializable {

    Suborg suborg;
    int testKeysCreated;
    int testKeysStarted;
    int testKeysCompleted;
    int batteriesCreated;
    int batteriesStarted;
    int batteriesCompleted;
    int creditsUsed;
    int userLogins;
    boolean unlimited;

    int testEventsStarted;
    int testEventsCompleted;

    Locale locale;

    // Map of userCompanyStatusId, count
    Map<Integer,Integer> userCompanyStatusMap;


    //public SuborgActivitySummary()
    //{}

    public SuborgActivitySummary( Suborg s )
    {
        this.suborg=s;
    }

    public boolean getHasData()
    {
        return testKeysCreated>0 || testKeysStarted>0 || testKeysCompleted>0 || batteriesCreated>0 || batteriesStarted>0 || batteriesCompleted>0 || creditsUsed!=0 || userLogins>0 || testEventsStarted>0 || testEventsCompleted>0;
    }

    public List<String[]> getUserCompanyStatusInfo()
    {
        if( locale == null )
            locale = Locale.US;

        List<String[]> out = new ArrayList<>();

        if( userCompanyStatusMap == null )
            return out;

        String[] d;

        Integer val;

        for( Integer key : userCompanyStatusMap.keySet() )
        {
            val = userCompanyStatusMap.get(key);

            d = new String[]{UserCompanyStatusType.getValue( key ).getName( locale ), val.toString()};

            out.add( d );
        }

        return out;
    }


    public int getTestKeysCreated() {
        return testKeysCreated;
    }

    public void setTestKeysCreated(int testKeysCreated) {
        this.testKeysCreated = testKeysCreated;
    }

    public int getTestKeysStarted() {
        return testKeysStarted;
    }

    public void setTestKeysStarted(int testKeysStarted) {
        this.testKeysStarted = testKeysStarted;
    }

    public int getTestKeysCompleted() {
        return testKeysCompleted;
    }

    public void setTestKeysCompleted(int testKeysCompleted) {
        this.testKeysCompleted = testKeysCompleted;
    }

    public int getCreditsUsed() {
        return creditsUsed;
    }

    public void setCreditsUsed(int creditsUsed) {
        this.creditsUsed = creditsUsed;
    }

    public int getUserLogins() {
        return userLogins;
    }

    public void setUserLogins(int userLogins) {
        this.userLogins = userLogins;
    }

    public boolean isUnlimited() {
        return unlimited;
    }

    public void setUnlimited(boolean unlimited) {
        this.unlimited = unlimited;
    }

    public int getTestEventsStarted() {
        return testEventsStarted;
    }

    public void setTestEventsStarted(int testEventsStarted) {
        this.testEventsStarted = testEventsStarted;
    }

    public int getTestEventsCompleted() {
        return testEventsCompleted;
    }

    public void setTestEventsCompleted(int testEventsCompleted) {
        this.testEventsCompleted = testEventsCompleted;
    }

    public Suborg getSuborg() {
        return suborg;
    }

    public void setSuborg(Suborg suborg) {
        this.suborg = suborg;
    }

    public Map<Integer, Integer> getUserCompanyStatusMap() {
        return userCompanyStatusMap;
    }

    public void setUserCompanyStatusMap(Map<Integer, Integer> userCompanyStatusMap) {
        this.userCompanyStatusMap = userCompanyStatusMap;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getBatteriesStarted() {
        return batteriesStarted;
    }

    public void setBatteriesStarted(int batteriesStarted) {
        this.batteriesStarted = batteriesStarted;
    }

    public int getBatteriesCompleted() {
        return batteriesCompleted;
    }

    public void setBatteriesCompleted(int batteriesCompleted) {
        this.batteriesCompleted = batteriesCompleted;
    }

    public int getBatteriesCreated() {
        return batteriesCreated;
    }

    public void setBatteriesCreated(int batteriesCreated) {
        this.batteriesCreated = batteriesCreated;
    }



}
