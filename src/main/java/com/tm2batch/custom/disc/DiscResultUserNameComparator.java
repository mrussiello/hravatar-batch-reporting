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
public class DiscResultUserNameComparator implements Comparator<DiscResult> {

    @Override
    public int compare(DiscResult o1, DiscResult o2) 
    {
        if( o1.getUser()!=null && o2.getUser()!=null )
            return o1.getUser().compareTo(o2.getUser());
        
        return 0;
    }
    
}
