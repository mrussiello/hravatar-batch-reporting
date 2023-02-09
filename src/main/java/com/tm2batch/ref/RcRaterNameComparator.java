/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.ref;

import com.tm2batch.entity.ref.RcRater;
import java.util.Comparator;

/**
 *
 * @author miker_000
 */
public class RcRaterNameComparator implements Comparator<RcRater> {

    @Override
    public int compare(RcRater o1, RcRater o2) {
        
        if( o1.getUser()==null || o2.getUser()==null)
            return 0;
        
        return o1.getUser().getLastName().compareTo( o2.getUser().getLastName() );
    }
    
    
}
