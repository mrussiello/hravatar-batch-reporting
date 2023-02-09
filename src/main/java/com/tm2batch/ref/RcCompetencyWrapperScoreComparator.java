/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.ref;

import java.util.Comparator;

/**
 *
 * @author miker_000
 */
public class RcCompetencyWrapperScoreComparator implements Comparator<RcCompetencyWrapper> {

    boolean highTop = false;
    
    public RcCompetencyWrapperScoreComparator( boolean highTop )
    {
        this.highTop=highTop;
    }
    
    @Override
    public int compare(RcCompetencyWrapper o1, RcCompetencyWrapper o2) {
        
        if( highTop )   
            return ((Float)o2.getScoreAvgNoCandidate()).compareTo( o1.getScoreAvgNoCandidate() );
        else
            return ((Float)o1.getScoreAvgNoCandidate()).compareTo( o2.getScoreAvgNoCandidate() );
    }
    
    
}
