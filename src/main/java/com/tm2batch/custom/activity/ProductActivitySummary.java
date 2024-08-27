/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.activity;

import com.tm2batch.entity.purchase.Product;
import java.io.Serializable;

/**
 *
 * @author Mike
 */
public class ProductActivitySummary implements Serializable, Comparable<ProductActivitySummary> {

    Product product;

    int testKeys;
    int starts;
    int testEventStarts;
    int completions;
    float averageScore;
    int creditsUsed;
    float averageSeconds;
    

    public ProductActivitySummary(Product product) {
        this.product = product;
    }

    @Override
    public int compareTo(ProductActivitySummary o) {

        if( product==null || o.getProduct()==null )
            return 0;

        return product.compareTo( o.getProduct() );
    }

    public boolean getHasData()
    {
        return testKeys>0 || starts>0 || testEventStarts>0 || completions>0 || averageScore>0 || creditsUsed!=0;
    }
    
    public void addToProductActivitySummary( ProductActivitySummary pas )
    {        
        pas.setTestKeys(testKeys + pas.getTestKeys() );
        pas.setStarts(starts + pas.getStarts() );
        pas.setCompletions(completions + pas.getCompletions() );
        pas.setTestEventStarts(testEventStarts + pas.getTestEventStarts() );
        pas.setCreditsUsed( creditsUsed + pas.getCreditsUsed() );
    }
    

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getStarts() {
        return starts;
    }

    public int getStartsPct() {
        return testKeys<=0 ? 0 : Math.round( 100f*((float)starts)/((float)testKeys));
    }

    public void setStarts(int starts) {
        this.starts = starts;
    }

    public int getCompletions() {
        return completions;
    }

    public int getCompletionsPct() {
        return testKeys<=0 ? 0 : Math.round( 100f*((float)completions)/((float)testKeys));
    }

    public void setCompletions(int completions) {
        this.completions = completions;
    }

    public float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(float averageScore) {
        this.averageScore = averageScore;
    }

    public int getCreditsUsed() {
        return creditsUsed;
    }

    public void setCreditsUsed(int creditsUsed) {
        this.creditsUsed = creditsUsed;
    }

    public int getTestKeys() {
        return testKeys;
    }

    public void setTestKeys(int testKeys) {
        this.testKeys = testKeys;
    }

    public int getTestEventStarts() {
        return testEventStarts;
    }

    public int getTestEventStartsPct() {
        return testKeys<=0 ? 0 : Math.round( 100f*((float)testEventStarts)/((float)testKeys));
    }


    public void setTestEventStarts(int testEventStarts) {
        this.testEventStarts = testEventStarts;
    }

    public int getDropoffRatePctBatt()
    {
        return starts<=0 ? 0 : Math.round( 100f*((float)completions)/((float)starts));
        
    }

    
    public int getDropoffRatePct()
    {
        return testEventStarts<=0 ? 0 : Math.round( 100f*((float)completions)/((float)testEventStarts));
        
    }

    public float getAverageSeconds() {
        return averageSeconds;
    }

    public void setAverageSeconds(float averageSeconds) {
        this.averageSeconds = averageSeconds;
    }



}
