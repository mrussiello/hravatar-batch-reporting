/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.activity;

import com.tm2batch.entity.user.Suborg;
import java.io.Serializable;

/**
 *
 * @author Mike
 */
public class LvActivitySummary implements Serializable, Comparable<LvActivitySummary> {

    
    Suborg suborg;
    int suborgId;
    
    int lvInvitations;
    int lvStarts;
    int lvCompletes;
    
    String name;

    public LvActivitySummary() {
    }

    @Override
    public int compareTo(LvActivitySummary o) {

        if( suborg==null || o.getSuborg()==null )
            return 0;

        return suborg.compareTo( o.getSuborg() );
    }

    public boolean getHasData()
    {
        return lvInvitations>0 || lvCompletes>0 || lvStarts>0;
    }
    
    public String getNameToUse()
    {
        if( suborg!=null )
            return suborg.getName();
        if( name!=null )
            return name;
        return "";
    }
    
    
    public void addToLvActivitySummary( LvActivitySummary pas )
    {        
        pas.setLvInvitations(lvInvitations + pas.getLvInvitations() );
        pas.setLvCompletes(lvCompletes + pas.getLvCompletes() );
    }

    public Suborg getSuborg() {
        return suborg;
    }

    public void setSuborg(Suborg suborg) {
        this.suborg = suborg;
    }

    public int getSuborgId() {
        return suborgId;
    }

    public void setSuborgId(int suborgId) {
        this.suborgId = suborgId;
    }

    public int getLvInvitations() {
        return lvInvitations;
    }

    public void setLvInvitations(int lvInvitations) {
        this.lvInvitations = lvInvitations;
    }

    public int getLvCompletes() {
        return lvCompletes;
    }

    public void setLvCompletes(int lvCompletes) {
        this.lvCompletes = lvCompletes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLvStarts() {
        return lvStarts;
    }

    public void setLvStarts(int lvStarts) {
        this.lvStarts = lvStarts;
    }


}
