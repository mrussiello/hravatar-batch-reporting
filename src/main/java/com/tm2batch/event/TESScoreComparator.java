/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.event;


import com.tm2batch.entity.event.TestEventScore;
import java.util.Comparator;

/**
 *
 * @author Mike
 */
public class TESScoreComparator implements Comparator<TestEventScore>
{
    @Override
    public int compare(TestEventScore a, TestEventScore b)
    {
        return ((Float) a.getScore() ).compareTo( b.getScore() );
    }

}
