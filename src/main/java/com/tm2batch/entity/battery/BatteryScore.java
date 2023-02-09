package com.tm2batch.entity.battery;

import com.tm2batch.battery.BatteryScoreType;
import com.tm2batch.custom.result.TestResult;
import com.tm2batch.entity.event.TestEvent;
import com.tm2batch.entity.event.TestKey;
import com.tm2batch.entity.profile.Profile;
import com.tm2batch.entity.purchase.Product;
import com.tm2batch.entity.user.OrgAutoTest;
import com.tm2batch.entity.user.Suborg;
import com.tm2batch.entity.user.User;
import com.tm2batch.event.ScoreCategoryType;
import com.tm2batch.event.ScoreFormatType;
import com.tm2batch.global.Constants;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.score.TextAndTitle;
import com.tm2batch.service.EncryptUtils;
import com.tm2batch.service.LogService;
import com.tm2batch.util.NumberUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;


@Entity
@Table( name = "batteryscore" )
@NamedQueries( {
        @NamedQuery( name = "BatteryScore.findByTestKeyId", query = "SELECT o FROM BatteryScore AS o WHERE o.testKeyId=:testKeyId" ),
        @NamedQuery( name = "BatteryScore.findActiveByTestKeyId", query = "SELECT o FROM BatteryScore AS o WHERE o.batteryScoreStatusTypeId=0 AND o.testKeyId=:testKeyId" )
} )
public class BatteryScore implements Serializable, TestResult
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "batteryscoreid" )
    private long batteryScoreId;

    @Column( name = "userid" )
    private long userId;

    @Column( name = "testkeyid" )
    private long testKeyId;

    @Column( name = "orgid" )
    private int orgId;

    @Column( name = "suborgid" )
    private int suborgId;


    @Column( name = "batteryscoretypeid" )
    private int batteryScoreTypeId;

    @Column( name = "batteryscorestatustypeid" )
    private int batteryScoreStatusTypeId;

    @Column( name = "batteryid" )
    private int batteryId;

    @Column(name="scoreformattypeid")
    private int scoreFormatTypeId;

    @Column( name = "score" )
    private float score = 0;

    //@Column( name = "percentile" )
    //private float percentile = -1;

    //@Column( name = "percentilecount" )
    //private int percentileCount = 0;
    
    @Column( name = "accountpercentile" )
    private float accountPercentile = -1;

    @Column( name = "accountpercentilecount" )
    private int accountPercentileCount = 0;

    @Column( name = "countrypercentile" )
    private float countryPercentile = -1;

    @Column( name = "countrypercentilecount" )
    private int countryPercentileCount = 0;

    @Column( name = "excludefmnorms" )
    private int excludeFmNorms = 0;


    @Column( name = "rawscore" )
    private float rawScore;

    @Column( name = "scoretext" )
    private String scoreText;

    @Column( name = "scorecategoryid" )
    private int scoreCategoryId;

    @Column(name="errortxt")
    private String errorTxt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="startdate")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastaccessdate")
    private Date lastAccessDate;
    
    @Transient
    private TestKey testKey;

    @Transient
    private boolean viewDetail;

    @Transient
    private Product product;

    @Transient
    private User user;

    @Transient
    private Suborg suborg;

    @Transient
    private Battery battery;

    @Transient
    private List<TestEvent> testEventList;

    @Transient
    private String overallPercentileStr;

    @Transient
    private String accountPercentileStr;

    @Transient
    private String countryPercentileStr;
    
    @Transient
    private List<Profile> altScoreProfileList;

    @Transient
    private int rcCheckCount;    

    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (int) (this.batteryScoreId ^ (this.batteryScoreId >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BatteryScore other = (BatteryScore) obj;
        if (this.batteryScoreId != other.batteryScoreId) {
            return false;
        }
        return true;
    }    
      
    //@Override
    //public boolean getHasVisualMedia()
    //{
    //    return false;
    //}
    
    
    public String getBatteryScoreIdEncrypted()
    {
        try
        {
            return EncryptUtils.urlSafeEncrypt( batteryScoreId );
        }
        catch( Exception e )
        {
            LogService.logIt(e, "BatteryScore.getBatteryScoreIdEncrypted() batteryScoreId=" + batteryScoreId );
            return "";
        }
    }
    
    public String getOrgIdEncrypted()
    {
        try
        {
            return EncryptUtils.urlSafeEncrypt( orgId );
        }
        catch( Exception e )
        {
            LogService.logIt(e, "BatteryScore.getOrgIdEncrypted() orgId=" + orgId );
            return "";
        }
    }
    
    
    public int getDownloadableReportCount()
    {
        if( testEventList==null || testEventList.isEmpty() )
            return 0;
        int ct = 0;
        
        for( TestEvent te : testEventList )
        {
            if( te.getTestEventScoreList()==null )
                continue;
            ct += te.getReportDocumentTestEventScoreList().size();            
        }
        
        return ct;
    }
    
    //@Override
    //public List<TextAndTitle> getTextAndTitleList1()
    //{
    //    return new ArrayList<>();
    //}
    
    
    //@Override
    public boolean getShowSolidColorIconForOverall()
    {
        return false;
    }

    //@Override
    //public boolean getSkipComparison()
    //{
    //    return false;
    //}
    
    
    @Override
    public String getAdminUserName()
    {
        if( testKey!=null && testKey.getAuthorizingUser()!=null )
            return testKey.getAuthorizingUser().getFullnameReverse();
        return null;
    }
    
    @Override
    public boolean getUseScoreText4OverallNumeric()
    {
        return false;
    }
            
    @Override
    public boolean getHideOverallNumeric()
    {
        return !getBatteryScoreType().getHasScore();
    }
    
    @Override
    public boolean getAddOverallNumericToExcel()
    {
        return false;
    }
    
    //@Override
    //public boolean getHideOverallGraphic()
    //{
    //    return false;
    //}
    
    
    @Override
    public int getOverallScorePrecisionDigits()
    {
        return getScorePrecisionDigits();
    }
    
    @Override
    public int getCompetencyScorePrecisionDigits()
    {
        return getScorePrecisionDigits();
    }
    
    
    @Override
    public String getOverallScore4Show()
    {
        if( this.getScorePrecisionDigits()==2 )
            return NumberUtils.getTwoDecimalFormattedAmount(getOverallScore());
        if( this.getScorePrecisionDigits()==1 )
            return NumberUtils.getOneDecimalFormattedAmount(getOverallScore());
        return Integer.toString(Math.round(getOverallScore()) );
    }

    
    @Override
    public String getOverallRawScore4Show()
    {
        if( this.getScorePrecisionDigits()==2 )
            return NumberUtils.getTwoDecimalFormattedAmount(getRawScore());
        if( this.getScorePrecisionDigits()==1 )
            return NumberUtils.getOneDecimalFormattedAmount(getRawScore());
        return Integer.toString(Math.round(getOverallScore()) );
        
    }
    
    
    @Override
    public int getCreditsUsed()
    {
        if( this.testEventList!=null )
        {
            int c = 0;
            for( TestEvent te : this.testEventList )
                c += te.getCreditsUsed();
            return c;
        }
        return 0;
    }
    
    // @Override
    public int getScorePrecisionDigits()
    {
        return ScoreFormatType.getValue(this.scoreFormatTypeId).getScorePrecisionDigits();
    }

    

    @Override
    public String getDirectLinkUrl()
    {
        try
        {
           return RuntimeConstants.getStringValue( "baseprotocol" ) + "://" + RuntimeConstants.getStringValue( "basedomain" ) + "/ta/r.xhtml?b=" + getTestKeyIdEncrypted() + "&r=0";
        }

        catch( Exception e )
        {
            LogService.logIt( e, "BatteryScore.getDirectLinkUrl()" );
        }
        
        return "";
    }
    
    public String getTestKeyIdEncrypted()
    {
        try
        {
            return EncryptUtils.urlSafeEncrypt( testKeyId );
        }

        catch( Exception e )
        {
            LogService.logIt(e, "BatteryScore.getTestKeyIdEncrypted() " + toString()  );

            return "";
        }
    }
    
    

    @Override
    public boolean getHasOverallScore()
    {
        return this.battery == null || this.battery.getBatteryScoreType().getHasScore();
    }


    //@Override
    //public boolean getUseStars()
    //{
    //    return false;
    //}

    @Override
    public String getTestResultId()
    {
        return "BS_" + batteryScoreId;
    }

    @Override
    public String toString() {
        return "BatteryScore{" + "batteryScoreId=" + batteryScoreId + ", batteruId=" + batteryId + ", testKeyId=" + testKeyId + ", score=" + score + '}';
    }





    //@Override
    //public boolean getHasDetail()
    //{
    //    return true;
    //}


    @Override
    public boolean getHasOverallPercentile()
    {
        return false;
        
        //if( battery != null && !battery.getBatteryScoreType().getHasScore() )
        //    return false;

        //return percentile >= 0 && percentileCount>Constants.MIN_PERCENTILE_COUNT;
    }

    @Override
    public float getOverallPercentile()
    {
        return -1; // accountPercentile;
    }
    
    @Override
     public int getOverallPercentileCount() {
        return 0;
    }
    

    
    
    
    
    //@Override
   // public ScoreCategoryType getOverallScoreCategoryType()
    //{
    //    return ScoreCategoryType.getType( scoreCategoryId );
    //}
    
    //@Override
    //public String getOverallScoreCategoryColorRgb()
    //{
    //    return getOverallScoreCategoryType().getColorRgb();
    //}
    


    //@Override
    //public String getOverallPercentileStr()
    //{
    //    return overallPercentileStr;
    //}

    //@Override
    //public String getAccountPercentileStr()
    //{
    //    return accountPercentileStr;
    //}

    public String getCountryPercentileStr()
    {
        return countryPercentileStr;
    }

    public void setOverallPercentileStr(String overallPercentileStr) {
        this.overallPercentileStr = overallPercentileStr;
    }

    public void setAccountPercentileStr(String accountPercentileStr) {
        this.accountPercentileStr = accountPercentileStr;
    }

    public void setCountryPercentileStr(String countryPercentileStr) {
        this.countryPercentileStr = countryPercentileStr;
    }



    @Override
    public String getName()
    {
        if( battery == null )
            return null;

        return  battery.getName();
    }

    @Override
    public String getNameWithEnglishIfNeeded()
    {
        return battery == null ? null : battery.getNameWithEnglishIfNeeded();
    }

        @Override
    public String getNameEnglish()
    {
        return battery == null ? null : battery.getNameEnglish();
    }

    @Override
    public boolean getNeedsNameEnglish()
    {
        return battery == null ? false : battery.getNeedsNameEnglish();
    }


    @Override
    public float getOverallScore()
    {
        return score;
    }

    @Override
    public float getOverallRawScore()
    {
        return getRawScore();
    }

    @Override
    public boolean getHasRating()
    {
        return false;
    }

    @Override
    public int getOverallRating()
    {
        return 0;
    }

    
    
    @Override
    public boolean getHasValidAccountNorm()
    {
        if( !getHasOverallScore() )
            return false;
        
        return accountPercentile>0 && accountPercentileCount>=Constants.MIN_PERCENTILE_COUNT ;
    }
    
    @Override
    public boolean getHasValidCountryNorm()
    {
        if( !getHasOverallScore() )
            return false;
        
        return countryPercentile>0 && countryPercentileCount>=Constants.MIN_PERCENTILE_COUNT ;
    }
    



    public BatteryScoreType getBatteryScoreType()
    {
        return BatteryScoreType.getType(this.batteryScoreTypeId );
    }


    @Override
    public boolean getHasScore()
    {
        return getBatteryScoreType().getHasScore();
    }





    public void addTestEvent( TestEvent te )
    {
        if( te.getBatteryId() != batteryId || te.getTestKeyId()!=this.getTestKeyId() )
            return;

        if( testEventList == null )
            testEventList = new ArrayList<>();

        for( TestEvent tee : testEventList )
        {
            if( tee.getTestEventId()==te.getTestEventId() )
                return;
        }

        testEventList.add(te);
    }

    public long getBatteryScoreId() {
        return batteryScoreId;
    }

    public void setBatteryScoreId(long batteryScoreId) {
        this.batteryScoreId = batteryScoreId;
    }

    public int getBatteryScoreTypeId() {
        return batteryScoreTypeId;
    }

    public void setBatteryScoreTypeId(int batteryScoreTypeId) {
        this.batteryScoreTypeId = batteryScoreTypeId;
    }

    public float getRawScore() {
        return rawScore;
    }

    public void setRawScore(float rawScore) {
        this.rawScore = rawScore;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public long getTestKeyId() {
        return testKeyId;
    }

    public void setTestKeyId(long testKeyId) {
        this.testKeyId = testKeyId;
    }

    public int getScoreCategoryId() {
        return scoreCategoryId;
    }

    public void setScoreCategoryId(int scoreCategoryId) {
        this.scoreCategoryId = scoreCategoryId;
    }

    public int getScoreFormatTypeId() {
        return scoreFormatTypeId;
    }

    public void setScoreFormatTypeId(int scoreFormatTypeId) {
        this.scoreFormatTypeId = scoreFormatTypeId;
    }

    public String getScoreText() {
        return scoreText;
    }

    public void setScoreText(String s)
    {
        if( s != null && s.isEmpty() )
            s = null;

        this.scoreText = s;
    }

    public String getErrorTxt() {
        return errorTxt;
    }

    public void setErrorTxt(String t) {

        if( t != null && t.isEmpty() )
            t = null;

        this.errorTxt = t;
    }

    public void appendErrorTxt( String t )
    {
        if( t == null )
            return;

        if( errorTxt == null )
            errorTxt = t;

        else if( t != null )
            errorTxt = t + "\n" + errorTxt;

        if( errorTxt != null && errorTxt.length()>1000 )
            errorTxt = errorTxt.substring(0,1000 );
    }

    public Battery getBattery() {
        return battery;
    }

    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    public List<TestEvent> getTestEventList() {
        return testEventList;
    }

    public void setTestEventList(List<TestEvent> testEventList) {
        this.testEventList = testEventList;
    }

    public int getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(int batteryId) {
        this.batteryId = batteryId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getSuborgId() {
        return suborgId;
    }

    public void setSuborgId(int suborgId) {
        this.suborgId = suborgId;
    }

    public boolean getViewDetail() {
        return viewDetail;
    }

    public void setViewDetail(boolean viewDetail) {
        this.viewDetail = viewDetail;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    //public float getPercentile() {
    //    return percentile;
    //}

    //public void setPercentile(float percentile) {
    //    this.percentile = percentile;
    //}

    public int getBatteryScoreStatusTypeId() {
        return batteryScoreStatusTypeId;
    }

    public void setBatteryScoreStatusTypeId(int batteryScoreStatusTypeId) {
        this.batteryScoreStatusTypeId = batteryScoreStatusTypeId;
    }

    @Override
    public float getAccountPercentile()
    {
        return accountPercentile;
    }
    
   
    public void setAccountPercentile(float accountPercentile) {
        this.accountPercentile = accountPercentile;
    }

    @Override
    public float getCountryPercentile() {
        return countryPercentile;
    }

    public void setCountryPercentile(float countryPercentile) {
        this.countryPercentile = countryPercentile;
    }

    public int getExcludeFmNorms() {
        return excludeFmNorms;
    }

    public void setExcludeFmNorms(int excludeFmNorms) {
        this.excludeFmNorms = excludeFmNorms;
    }

    public int getAccountPercentileCount() {
        return accountPercentileCount;
    }

    public void setAccountPercentileCount(int accountPercentileCount) {
        this.accountPercentileCount = accountPercentileCount;
    }

    public Suborg getSuborg() {
        return suborg;
    }

    public void setSuborg(Suborg suborg) {
        this.suborg = suborg;
    }

    public String getCustom1()
    {
        return testKey==null ? "" : testKey.getCustom1();
    }

    public String getCustom2()
    {
        return testKey==null ? "" : testKey.getCustom2();
    }

    public String getCustom3()
    {
        return testKey==null ? "" : testKey.getCustom3();
    }

    public TestKey getTestKey() {
        return testKey;
    }

    public void setTestKey(TestKey testKey) {
        this.testKey = testKey;
    }

    public List<Profile> getAltScoreProfileList() {
        return altScoreProfileList;
    }

    public void setAltScoreProfileList(List<Profile> altScoreProfileList) {
        this.altScoreProfileList = altScoreProfileList;
    }

    public int getCountryPercentileCount() {
        return countryPercentileCount;
    }

    public void setCountryPercentileCount(int countryPercentileCount) {
        this.countryPercentileCount = countryPercentileCount;
    }

    public int getRcCheckCount() {
        return rcCheckCount;
    }

    public void setRcCheckCount(int rcCheckCount) {
        this.rcCheckCount = rcCheckCount;
    }

    public OrgAutoTest getOrgAutoTest() {
        return testKey!=null ? testKey.getOrgAutoTest() : null;
    }


}
