/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tm2batch.custom.disc;

import java.util.Comparator;


/**
 *
 * @author miker
 */
public class DiscResultTwoLevelScoreComparator implements Comparator<DiscResult> {

    @Override
    public int compare(DiscResult o1, DiscResult o2) 
    {
        // get TwoLevelIndex
        int twoLevelIndex1 = o1.getTwoLevelIndex();
        int twoLevelIndex2 = o2.getTwoLevelIndex();
        
        if( twoLevelIndex1!=twoLevelIndex2 )
            return Integer.valueOf(twoLevelIndex1).compareTo(twoLevelIndex2);
        
        if( o1.getUser()!=null && o2.getUser()!=null && o1.getUser().getUserId()!=o2.getUser().getUserId() )
            return o1.getUser().compareTo(o2.getUser());

        if( o1.getUser()!=null && o2.getUser()!=null && o1.getUser().getUserId()==o2.getUser().getUserId() )
            return o1.getTestDate().compareTo(o2.getTestDate());
        
        return 0;
    }
    
}
