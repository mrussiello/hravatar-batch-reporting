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
public class RcActivitySummary implements Serializable, Comparable<RcActivitySummary> {

    
    Suborg suborg;
    int suborgId;
    
    int rcChecks;
    int rcCheckCompletes;
    int candidateStarts;
    int candidateCompletes;
    int raterCreates;
    int raterStarts;
    int raterCompletes;

    String name;
    
    public RcActivitySummary() {
    }

    @Override
    public int compareTo(RcActivitySummary o) {

        if( suborg==null || o.getSuborg()==null )
            return 0;

        return suborg.compareTo( o.getSuborg() );
    }

    public boolean getHasData()
    {
        return rcChecks>0 || rcCheckCompletes>0 || candidateStarts>0 || candidateCompletes>0 || raterCreates>0 || raterStarts>0 || raterCompletes>0;
    }
    
    public String getNameToUse()
    {
        if( suborg!=null )
            return suborg.getName();
        if( name!=null )
            return name;
        return "";
    }
    
    public void addToRcActivitySummary( RcActivitySummary pas )
    {        
        pas.setRcChecks(rcChecks + pas.getRcChecks() );
        pas.setRcCheckCompletes(rcCheckCompletes + pas.getRcCheckCompletes() );
        pas.setCandidateStarts(candidateStarts + pas.getCandidateStarts() );
        pas.setCandidateCompletes(candidateCompletes + pas.getCandidateCompletes() );
        pas.setRaterCreates(raterCreates + pas.getRaterCreates() );
        pas.setRaterStarts(raterStarts + pas.getCandidateStarts() );
        pas.setRaterCompletes(raterCompletes + pas.getRaterCompletes() );
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

    public int getRcChecks() {
        return rcChecks;
    }

    public void setRcChecks(int rcChecks) {
        this.rcChecks = rcChecks;
    }

    public int getRcCheckCompletes() {
        return rcCheckCompletes;
    }

    public void setRcCheckCompletes(int rcCheckCompletes) {
        this.rcCheckCompletes = rcCheckCompletes;
    }

    public int getCandidateStarts() {
        return candidateStarts;
    }

    public void setCandidateStarts(int candidateStarts) {
        this.candidateStarts = candidateStarts;
    }

    public int getCandidateCompletes() {
        return candidateCompletes;
    }

    public void setCandidateCompletes(int candidateCompletes) {
        this.candidateCompletes = candidateCompletes;
    }

    public int getRaterStarts() {
        return raterStarts;
    }

    public void setRaterStarts(int raterStarts) {
        this.raterStarts = raterStarts;
    }

    public int getRaterCompletes() {
        return raterCompletes;
    }

    public void setRaterCompletes(int raterCompletes) {
        this.raterCompletes = raterCompletes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRaterCreates() {
        return raterCreates;
    }

    public void setRaterCreates(int raterCreates) {
        this.raterCreates = raterCreates;
    }
    


}
