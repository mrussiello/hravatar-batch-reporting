/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.account.results;

import com.tm2batch.custom.result.TestResult;
import java.util.Comparator;

/**
 *
 * @author miker_000
 */
public class TestResultComparator implements Comparator<TestResult>
{

    TestResultSortType testResultSortType;
    
    public TestResultComparator( TestResultSortType trst )
    {
        this.testResultSortType = trst;
    }
    
    
    
    @Override
    public int compare(TestResult o1, TestResult o2) 
    {
        if( testResultSortType.isLastAccess() )
        {
            if( testResultSortType.isDescending() )
                return o2.getLastAccessDate().compareTo(o1.getLastAccessDate() );
            else
                return o1.getLastAccessDate().compareTo(o2.getLastAccessDate() );
        }
        
        if( testResultSortType.isLastname())
        {
            if( testResultSortType.isDescending() )
                return o2.getUser().getLastName().compareTo(o1.getUser().getLastName() );
            else
                return o1.getUser().getLastName().compareTo(o2.getUser().getLastName() );
        }
        
        if( testResultSortType.isScore())
        {
            if( testResultSortType.isDescending() )
                return ((Float)o2.getOverallScore()).compareTo( o1.getOverallScore() ) ;
            else
                return ((Float)o1.getOverallScore()).compareTo( o2.getOverallScore() ) ;
        }
        
        return 0;
    }
    
    
    
}
