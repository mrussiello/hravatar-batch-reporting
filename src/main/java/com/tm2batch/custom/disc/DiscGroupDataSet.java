/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tm2batch.custom.disc;

import com.tm2batch.entity.purchase.Product;
import com.tm2batch.entity.user.Org;
import com.tm2batch.entity.user.OrgAutoTest;
import com.tm2batch.entity.user.Suborg;
import com.tm2batch.entity.user.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author miker
 */
public class DiscGroupDataSet {
    
    User authUser;
    List<DiscResult> discResultList;
    Org org;
    Suborg suborg;
    OrgAutoTest orgAutoTest;
    Product product;
    String custom1;
    String custom1Name;
    String custom2;
    String custom2Name;
    String custom3;
    String custom3Name;
    String altIdentifier;
    
    Date startDate;
    Date endDate;

    float[] stylePercentages;
    float[] styleCounts;

    float[] twoLevelStylePercentages;
    float[] twoLevelStyleCounts;

    
    
    public void calculateAggregateResults()
    {
        stylePercentages = new float[4];
        styleCounts = new float[4];

        twoLevelStylePercentages = new float[16];
        twoLevelStyleCounts = new float[16];

        
        if( discResultList==null )
            return;
        
        float totalCount = 0;
        
        for( DiscResult dr : discResultList )
        {
            totalCount++;
            
            styleCounts[dr.getDominantTypeIndex()]++; 
            
            twoLevelStyleCounts[dr.getTwoLevelIndex()]++;
        }
        
        if( totalCount>0 )
        {
            for( int i=0;i<4;i++ )
            {
                stylePercentages[i] = 100f*styleCounts[i]/totalCount;
            }

            for( int i=0;i<16;i++ )
            {
                twoLevelStylePercentages[i] = 100f*twoLevelStyleCounts[i]/totalCount;
            }
        }
    }

    public List<DiscResult> getDiscResultListSortedByUser()
    {
        List<DiscResult> out = new ArrayList<>();
        if( discResultList==null )
            return out;
        out.addAll( discResultList );
        Collections.sort( out, new DiscResultUserNameComparator());
        return out;
    }

    
    public List<DiscResult> getDiscResultListForStyleIndex( int styleIndex )
    {
        List<DiscResult> out = new ArrayList<>();
        if( this.discResultList==null )
            return out;
        
        for( DiscResult dr : discResultList )
        {
            if( dr.getDominantTypeIndex()==styleIndex )
                out.add( dr );
        }
        
        Collections.sort( out, new DiscResultUserNameComparator() );        
        return out;
    }
    
    
    public int getTotalCount()
    {
        if( discResultList==null )
            return 0;
        return discResultList.size();
    }
    
    public List<DiscResult> getDiscResultsForUserId( long userId )
    {
        if( discResultList==null )
            discResultList = new ArrayList<>();
        
        List<DiscResult> out = new ArrayList<>();
        
        for( DiscResult dr : discResultList )
        {
            if( dr.getUser()!=null && dr.getUser().getUserId()==userId )
                out.add( dr );
        }
        
        Collections.sort(out);
        return out;        
    }
    
    public void addDiscResult( DiscResult dr )
    {
        if( discResultList==null )
            discResultList = new ArrayList<>();
        
        if( !discResultList.contains( dr ))
            discResultList.add(dr);
    }

    public List<DiscResult> getDiscResultList() {
        return discResultList;
    }

    public void setDiscResultList(List<DiscResult> discResultList) {
        this.discResultList = discResultList;
    }

    @Override
    public String toString() {
        return "DiscGroupDataSet{ org=" + (org==null ? "null" : org.getName() + " (" + org.getOrgId() + ")") + ", suborg=" + (suborg==null ? "null" : suborg.getName() + " (" + suborg.getSuborgId() + ")") + " orgAutoTest=" + (orgAutoTest==null ? "null" : orgAutoTest.getName() + " (" + orgAutoTest.getOrgAutoTestId() + ")") + ", product=" + (product==null ? "null" : product.getName() + " (" + product.getProductId() + ")") + ", discResultList.size=" + (discResultList==null ? "null" : discResultList.size()) + '}';
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public User getAuthUser() {
        return authUser;
    }

    public void setAuthUser(User authUser) {
        this.authUser = authUser;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    public Suborg getSuborg() {
        return suborg;
    }

    public void setSuborg(Suborg suborg) {
        this.suborg = suborg;
    }

    public OrgAutoTest getOrgAutoTest() {
        return orgAutoTest;
    }

    public void setOrgAutoTest(OrgAutoTest orgAutoTest) {
        this.orgAutoTest = orgAutoTest;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public float[] getStylePercentages() {
        return stylePercentages;
    }

    public float[] getTwoLevelStylePercentages() {
        return twoLevelStylePercentages;
    }

    
    public float[] getStyleCounts() {
        return styleCounts;
    }

    
    public float[] getTwoLevelStyleCounts() {
        return twoLevelStyleCounts;
    }
    
    
    public String[] getCustomsArray()
    {
        return new String[]{custom1,custom2,custom3};
    }
    
    public String getCustom1() {
        return custom1;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public String getCustom3() {
        return custom3;
    }

    public void setCustom3(String custom3) {
        this.custom3 = custom3;
    }

    public String getAltIdentifier() {
        return altIdentifier;
    }

    public void setAltIdentifier(String altIdentifier) {
        this.altIdentifier = altIdentifier;
    }

    public String getCustom1Name() {
        return custom1Name;
    }

    public void setCustom1Name(String custom1Name) {
        this.custom1Name = custom1Name;
    }

    public String getCustom2Name() {
        return custom2Name;
    }

    public void setCustom2Name(String custom2Name) {
        this.custom2Name = custom2Name;
    }

    public String getCustom3Name() {
        return custom3Name;
    }

    public void setCustom3Name(String custom3Name) {
        this.custom3Name = custom3Name;
    }
    
    
    
    
}
