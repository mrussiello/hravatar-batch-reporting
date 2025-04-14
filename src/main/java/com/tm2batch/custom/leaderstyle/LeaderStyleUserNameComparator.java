/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tm2batch.custom.leaderstyle;

import java.util.Comparator;


/**
 *
 * @author miker
 */
public class LeaderStyleUserNameComparator implements Comparator<LeaderStyleResult> {

    @Override
    public int compare(LeaderStyleResult o1, LeaderStyleResult o2) 
    {
        if( o1.getUser()!=null && o2.getUser()!=null )
            return o1.getUser().compareTo(o2.getUser());
        
        return 0;
    }
    
}
