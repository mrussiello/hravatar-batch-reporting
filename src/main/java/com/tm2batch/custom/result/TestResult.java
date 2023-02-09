/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.result;


import com.tm2batch.entity.battery.Battery;
import com.tm2batch.entity.event.TestKey;
import com.tm2batch.entity.profile.Profile;
import com.tm2batch.entity.purchase.Product;
import com.tm2batch.entity.user.OrgAutoTest;
import com.tm2batch.entity.user.Suborg;
import com.tm2batch.entity.user.User;
import com.tm2batch.event.ScoreCategoryType;
import com.tm2batch.score.TextAndTitle;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Mike
 */
public interface TestResult
{
    public long getTestKeyId();

    public void setTestKey( TestKey testKey );

    public User getUser();

    public Product getProduct();

    public String getName();

    public String getNameEnglish();

    public boolean getNeedsNameEnglish();

    public String getNameWithEnglishIfNeeded();

    public float getOverallScore();

    public float getOverallRawScore();
    
    public int getCreditsUsed();
    
    public String getCustom1();

    public String getCustom2();

    public String getCustom3();
    
    public String getAdminUserName();

    public boolean getHasOverallScore();
    
    // public boolean getShowSolidColorIconForOverall();
    
    // public boolean getForceShowScaledScore();
    
    // public boolean getHasOverallRawScore();

    // public boolean getHasScoreColorGraph();

    // public String getScoreColorGraphUrl();

    public boolean getHasRating();

    public int getOverallRating();

    public float getOverallPercentile();
    public int getOverallPercentileCount();
    public boolean getHasOverallPercentile();

    
    public float getAccountPercentile();
    public float getCountryPercentile();

    public boolean getHasValidAccountNorm();
    public boolean getHasValidCountryNorm();
    
    //public String getAccountPercentileStr();
    
    //public String getOverallPercentileStr();

    //public boolean getHasDetail();

    // public ScoreCategoryType getOverallScoreCategoryType();

    // public String getOverallScoreCategoryColorRgb();

    public Date getStartDate();

    public Date getLastAccessDate();

    //public boolean getViewDetail();
    //public void setViewDetail( boolean b );

    //public boolean getHasIndivDetail();

    //public boolean getHasIndivPercentile();

    //public boolean getHasIndivRating();

    public boolean getHasScore();

    // public boolean getUseStars();

    public String getTestResultId();

    public long getUserId();

    public void setUser( User u );

    public void setProduct( Product p );

    public String getDirectLinkUrl();

    public Suborg getSuborg();

    public TestKey getTestKey();

    public OrgAutoTest getOrgAutoTest();
    
    public Battery getBattery();
    
    public List<Profile> getAltScoreProfileList();

    public int getOverallScorePrecisionDigits();
        
    public int getCompetencyScorePrecisionDigits();
        
     // public int getScorePrecisionDigits();

    public String getOverallScore4Show();
    
    public String getOverallRawScore4Show();
    
    // List<TextAndTitle> getTextAndTitleList1();
    
    // boolean getSkipComparison();
    
    boolean getHideOverallNumeric();
    
    boolean getUseScoreText4OverallNumeric();
    
    // boolean getHideOverallGraphic();
    
    boolean getAddOverallNumericToExcel();
    
    //boolean getHasVisualMedia();
    
    //int getRcCheckCount(); 
    
    //void setRcCheckCount(int c);
}
