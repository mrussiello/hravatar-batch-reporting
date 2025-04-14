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
public class LeaderStyleStyleNameValueComparator implements Comparator<LeaderStyleResult> {

    @Override
    public int compare(LeaderStyleResult o1, LeaderStyleResult o2) 
    {
        return Integer.valueOf( o1.getDominantTypeIndex()).compareTo(o2.getDominantTypeIndex());
    }
    
}
