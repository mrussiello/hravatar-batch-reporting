package com.tm2batch.entity.event;


import com.tm2batch.account.results.CompetencyGroup;
import com.tm2batch.account.results.ItemResponseGroup;
import com.tm2batch.account.results.ReportUtils;
import com.tm2batch.custom.result.TestResult;
import com.tm2batch.entity.battery.Battery;
import com.tm2batch.entity.proctor.RemoteProctorEvent;
import com.tm2batch.entity.profile.Profile;
import com.tm2batch.entity.purchase.Product;
import com.tm2batch.entity.report.Report;
import com.tm2batch.entity.user.Org;
import com.tm2batch.entity.user.OrgAutoTest;
import com.tm2batch.entity.user.Suborg;
import com.tm2batch.entity.user.User;
import com.tm2batch.event.ScoreFormatType;
import com.tm2batch.event.TESScoreComparator;
import com.tm2batch.event.TestEventScoreType;
import com.tm2batch.event.TestEventStatusType;
import com.tm2batch.global.Constants;
import com.tm2batch.global.DisplayOrderComparator;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.ibmcloud.InsightReportTrait;
import com.tm2batch.purchase.ProductType;
import com.tm2batch.service.LogService;
import com.tm2batch.service.EncryptUtils;
import com.tm2batch.sim.SimCompetencyClass;
import com.tm2batch.sim.SimCompetencyVisibilityType;
import com.tm2batch.user.ReleaseCodeType;
import com.tm2batch.util.I18nUtils;
import com.tm2batch.util.NVPair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Date;

import java.util.List;
import java.util.Locale;
import jakarta.persistence.Cacheable;
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



@Cacheable(false)
@Entity
@Table( name = "testevent" )
@NamedQueries( {
        @NamedQuery( name = "TestEvent.findByTestKeyId", query = "SELECT o FROM TestEvent AS o WHERE o.testKeyId=:testKeyId" ),
        @NamedQuery( name = "TestEvent.findByTestEventId", query = "SELECT o FROM TestEvent AS o WHERE o.testEventId=:testEventId" )
} )
public class TestEvent implements Serializable, Comparable<TestEvent>, TestResult
{
    @Transient
    private static final long serialVersionUID = 1L;

 
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "testeventid" )
    private long testEventId;

    @Column( name = "testeventstatustypeid" )
    private int testEventStatusTypeId;

    @Column( name = "testkeyid" )
    private long testKeyId;

    @Column( name = "batteryid" )
    private int batteryId;

    @Column( name = "productid" )
    private int productId;

    @Column( name = "pin" )
    private String pin;

    @Column( name = "producttypeid" )
    private int productTypeId;

    @Column( name = "simid" )
    private long simId = 0;

    @Column( name = "simversionid" )
    private int simVersionId = 0;

    @Column(name="reportid")
    private long reportId = 0;

    @Column( name = "creditid" )
    private long creditId;

    @Column( name = "creditsused" )
    private int creditsUsed;

    @Column( name = "orgid" )
    private int orgId;

    @Column( name = "suborgid" )
    private int suborgId;

    @Column( name = "userid" )
    private long userId = 0;

    @Column( name = "corpid" )
    private int corpId;

    @Column( name = "skinid" )
    private int skinId;

    @Column( name = "lang" )
    private String localeStr;

    @Column( name = "langreport" )
    private String localeStrReport;

    @Column( name = "scorecolorschemetypeid" )
    private int scoreColorSchemeTypeId;

    
    @Column( name = "overallscore" )
    private float overallScore;

    @Column( name = "overallpercentile" )
    private float overallPercentile;

    @Column( name = "accountpercentile" )
    private float accountPercentile = -1;

    @Column( name = "countrypercentile" )
    private float countryPercentile = -1;

    @Column( name = "overallpercentilecount" )
    private int overallPercentileCount;

    @Column( name = "accountpercentilecount" )
    private int accountPercentileCount;

    @Column( name = "countrypercentilecount" )
    private int countryPercentileCount;

    @Column(name="percentilecountry")
    private String percentileCountry;


    @Column( name = "excludefmnorms" )
    private int excludeFmNorms;


    @Column( name = "scoreformattypeid" )
    private int scoreFormatTypeId;

    @Column( name = "overallrating" )
    private int overallRating;

    @Column( name = "resultxml" )
    private String resultXml;

    @Column( name = "proctoruserid" )
    private long proctorUserId;

    @Column( name = "percentcomplete" )
    private float percentComplete = 0;

    @Column( name = "releasecode" )
    private int releaseCode;

    @Column(name="extref")
    private String extRef;

    @Column( name = "expertypeid" )
    private int experTypeId;

    @Column( name = "eductypeid" )
    private int educTypeId;

    @Column( name = "traintypeid" )
    private int trainTypeId;

    @Column( name = "useragentid" )
    private int userAgentId;

    @Column(name="useragent")
    private String userAgent;


    @Column( name = "ipaddress" )
    private String ipAddress;

    @Column( name = "ipcountry" )
    private String ipCountry;

    @Column( name = "ipState" )
    private String ipState;

    @Column( name = "ipcity" )
    private String ipCity;

    @Column(name="geographicregionid")
    private int geographicRegionId = 0;

    @Column( name = "thirdpartytesteventid" )
    private String thirdPartyTestEventId;

    @Column( name = "thirdpartytestaccountid" )
    private String thirdPartyTestAccountId;

    @Column(name="errortxt")
    private String errorTxt;

    @Column(name="textstr1")
    private String textStr1;

    @Column(name="totaltesttime")
    private float totalTestTime;



    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="startdate")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastaccessdate")
    private Date lastAccessDate;

    // This is the date of the last time the totalTestTime was updated.
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lasttimeupdate")
    private Date lastTimeUpdate;
    
    @Transient
    private long testEventArchiveId;

    @Transient
    private TestKey testKey;

    @Transient
    private Org org;

    @Transient
    private OrgAutoTest orgAutoTest;

    @Transient
    private Suborg suborg;

    @Transient
    private Product product;

    @Transient
    private Battery battery;

    @Transient
    private User user;

    @Transient
    private String ipCountryName;

    @Transient
    private String percentileCountryName;
    
    @Transient
    private List<TestEventScore> testEventScoreList;

    @Transient
    private List<CompetencyGroup> competencyGroupList;

    @Transient
    private List<TestEventResponseRating> testEventResponseRatingList;

    @Transient
    private List<InsightReportTrait> insightReportTraitList;
    
    @Transient
    private List<ItemResponseGroup> itemResponseGroupList;


    @Transient
    private RemoteProctorEvent remoteProctorEvent;
    
    @Transient
    private Locale locale;
    
    @Transient
    private Report report;

    @Transient
    private Profile reportRangeProfile;
    
    @Transient
    private List<Profile> altScoreProfileList;

    
    @Override
    public boolean getNeedsNameEnglish()
    {
        return product == null ? false : product.getNeedsNameEnglish();
    }
    
    
    @Override
    public String getNameWithEnglishIfNeeded()
    {
        return product == null ? null : product.getNameWithEnglishIfNeeded();
    }

    
    
    //@Transient
    //private int rcCheckCount;    

    @Override
    public String getCustom1()
    {
        return testKey==null ? "" : testKey.getCustom1();
    }

    @Override
    public String getCustom2()
    {
        return testKey==null ? "" : testKey.getCustom2();
    }

    @Override
    public String getCustom3()
    {
        return testKey==null ? "" : testKey.getCustom3();
    }

    
    
    @Override
    public String getAdminUserName()
    {
        if( testKey!=null && testKey.getAuthorizingUser()!=null )
            return testKey.getAuthorizingUser().getFullnameReverse();
        return null;        
    }
    
    @Override
    public float getOverallRawScore() {
        
        if( this.getTestEventScoreList()==null || getTestEventScoreList().isEmpty() || this.getOverallTestEventScore()==null )
            return getOverallScore();

        return getOverallTestEventScore().getOverallRawScoreToShow();        
    }
    
    
    
    @Override
    public boolean getHasOverallScore()
    {
        if( getHideOverallNumeric() )
            return false;
        
        if( product !=null && ProductType.getValue( product.getProductTypeId()).getIsFindly() && this.overallScore<=0 )
            return false;

        if( product !=null && product.getDetailView()!=null && product.getDetailView().equalsIgnoreCase( Constants.INTEREST_SURVEY ) )
            return false;
        
        return true;
    }
    
    
    @Override
    public boolean getHasRating()
    {
        if( report != null && report.getIncludeColorScoresB() )
            return true;

        if( this.overallRating > 0 )
            return true;

        if( getTestEventScoreList() != null && getTestEventScoreList().size()> 0 )
        {
            for( TestEventScore tes : this.getTestEventScoreList() )
            {
                if( tes.getTestEventScoreType().getIsOverall())
                    continue;

                return true;
            }
        }

        return false;
    }
    
    
    @Override
    public boolean getHasOverallPercentile()
    {
        if( getSkipComparison() )
            return false;
        
        if( !getHasOverallScore() )
            return false;

        return overallPercentile >= 0 && (this.overallPercentileCount >= Constants.MIN_PERCENTILE_COUNT || overallPercentileCount==0 );
    }    
    
    @Override
    public String getTestResultId()
    {
        return "TE_" + testEventId;
    }
    
    
    public boolean getHasValidNorms()
    {
        if( getSkipComparison() )
            return false;
        
        return getHasValidOverallNorm() ||
            getHasValidCountryNorm() ||
            getHasValidAccountNorm();
    }

    public boolean getHasValidOverallNorm()
    {
        return overallPercentile>=0 && (overallPercentileCount>=Constants.MIN_PERCENTILE_COUNT || overallPercentileCount==0 );
    }

    @Override
    public boolean getHasValidCountryNorm()
    {
        return countryPercentile>=0 && ( countryPercentileCount>=Constants.MIN_PERCENTILE_COUNT  || countryPercentileCount==0 ) ;
    }

    @Override
    public boolean getHasValidAccountNorm()
    {
        return accountPercentile>=0 && ( accountPercentileCount>=Constants.MIN_PERCENTILE_COUNT  || accountPercentileCount==0 );
    }

    public boolean getHasValidSubcategoryNorms()
    {
        if( this.testEventScoreList==null )
            return false;

        for( TestEventScore tes : this.testEventScoreList )
        {
            if( !tes.getTestEventScoreType().getIsCompetency() && !tes.getTestEventScoreType().getIsTask() )
                continue;

            if( tes.getHasValidNorms() )
                return true;
        }

        return false;
    }

    
    
    @Override
    public String getDirectLinkUrl()
    {
        try
        {
           return RuntimeConstants.getStringValue( "baseadminuri" ) + "/r.xhtml?t=" + getTestEventIdEncrypted() + "&r=0";
        }

        catch( Exception e )
        {
            LogService.logIt( e, "TestEvent.getDirectLinkUrl()" );
        }

        return "";
    }

    
    
    public float getTotalElapsedTime()
    {
        if( this.getStartDate()==null || this.getLastAccessDate()==null )
            return 0;

        long diff = lastAccessDate.getTime() - startDate.getTime();

        if( diff < 0 )
            diff = 0;

        return ((float) diff)/1000;
    }

    
    @Override
    public boolean getHasScore()
    {
        return getScoreFormatType().getHasScore();
    }

    
    
    @Override
    public boolean getAddOverallNumericToExcel()
    {
        return ( getHideOverallNumeric() || getUseScoreText4OverallNumeric()) && getBooleanFlagValue( "addovrnumscrtoexcel" );        
    }    

    @Override
    public boolean getHideOverallNumeric()
    {
        if( getHideOverall() )
            return true;
        
        if( report!=null && !report.getIncludeNumericScoresB() )
            return true;
        
        return getBooleanFlagValue( "ovrnumoff" );
    }
    
    public boolean getHideOverall()
    {
        if( report!=null && !report.getIncludeOverallScoreB() )
            return true;
        
        return getBooleanFlagValue( "ovroff" );
    }

    
    public List<TestEventScore> getReportDocumentTestEventScoreList()
    {
        return getTestEventScoreList( TestEventScoreType.REPORT, false, true );
    }

    
    public List<TestEventScore> getAltScoreTestEventScoreList()
    {
        List<TestEventScore> out = getTestEventScoreList( TestEventScoreType.ALT_OVERALL.getTestEventScoreTypeId() );
        
        Collections.sort( out, new TESScoreComparator() );
        Collections.reverse( out );
        
        int count = 0;
        for( TestEventScore tes : out )
        {
            count++;
            tes.setDisplayOrder( count );
        }
        
        return out;
    }
    
    public List<TestEventScore> getTestEventScoreList( TestEventScoreType test )
    {
        return getTestEventScoreList( test.getTestEventScoreTypeId() );
    }
    
    public List<TestEventScore> getTestEventScoreList( int testEventScoreTypeId )
    {
        if( this.testEventScoreList==null )
            return null;

        List<TestEventScore> tesl = new ArrayList<>();

        for( TestEventScore tes : this.testEventScoreList )
        {
            if( tes.getTestEventScoreTypeId() == testEventScoreTypeId )
                tesl.add( tes );
        }

        return tesl;
    }

    public TestEventScore getOverallTestEventScore()
    {
        if( this.testEventScoreList==null )
            return null;

        List<TestEventScore> tesl = getTestEventScoreList( TestEventScoreType.OVERALL.getTestEventScoreTypeId() );

        return tesl.size()>0 ? tesl.get(0) : null;
    }


    public List<TestEventScore> getTestEventScoreList( TestEventScoreType test, boolean scoredOnly, boolean reportDocumentsOnly )
    {
        List<TestEventScore> out = new ArrayList<>();

        if( testEventScoreList == null )
            return out;

        for( TestEventScore tes : this.testEventScoreList )
        {
            if( tes.getTestEventScoreType().equals( test ) )
            {
                if( test.getIsReport() )
                {
                    if( reportDocumentsOnly && tes.getHasReport() )
                    {
                        out.add( tes );
                    }

                    else if( !reportDocumentsOnly )
                       out.add( tes );
                }

                else if( (!scoredOnly || tes.getHasScore()) )
                    out.add( tes );
            }
        }

        return out;
    }
    
    public List<TestEventScore> scoredCustomTestEventScoreList( int customIndex )
    {
        List<TestEventScore> out = new ArrayList<>();

        if( testEventScoreList == null )
            return out;

        for( TestEventScore tes : testEventScoreList )
        {
            // need scores
            if( !tes.getTestEventScoreType().getIsCompetency() || !tes.getHasScore() || !SimCompetencyVisibilityType.getValue( tes.getHide() ).getShowInReports()  )
                continue;

            if( SimCompetencyClass.getValue( tes.getSimCompetencyClassId() ).isAnyCustom())
            {
                if( customIndex<=0 || 
                    (customIndex==1 && (tes.getSimCompetencyClassId()==SimCompetencyClass.CUSTOM.getSimCompetencyClassId() || tes.getSimCompetencyClassId()==SimCompetencyClass.CUSTOM_COMBO.getSimCompetencyClassId())) ||
                    (customIndex==2 && tes.getSimCompetencyClassId()==SimCompetencyClass.CUSTOM2.getSimCompetencyClassId()) ||
                    (customIndex==3 && tes.getSimCompetencyClassId()==SimCompetencyClass.CUSTOM3.getSimCompetencyClassId()) ||
                    (customIndex==4 && tes.getSimCompetencyClassId()==SimCompetencyClass.CUSTOM4.getSimCompetencyClassId()) ||
                    (customIndex==5 && tes.getSimCompetencyClassId()==SimCompetencyClass.CUSTOM5.getSimCompetencyClassId())  )
                out.add( tes );
            }
        }

        Collections.sort( out, new DisplayOrderComparator() ); //new TESNameComparator() );
        
        return out;
    }
    
    
    
    
    public List<TestEventScore> getScoredAbilityTestEventScoreList()
    {
        List<TestEventScore> out = new ArrayList<>();

        if( testEventScoreList == null )
            return out;

        for( TestEventScore tes : testEventScoreList )
        {
            // need scores
            if( !tes.getTestEventScoreType().getIsCompetency() || !tes.getHasScore() || !SimCompetencyVisibilityType.getValue( tes.getHide() ).getShowInReports()  )
                continue;

            if( SimCompetencyClass.getValue( tes.getSimCompetencyClassId() ).isAbility() )
                out.add( tes );
        }
        
        Collections.sort( out, new DisplayOrderComparator() ); //new TESNameComparator() );

        return out;
    }

    public List<TestEventScore> getScoredAimsTestEventScoreList()
    {
        List<TestEventScore> out = new ArrayList<>();

        if( testEventScoreList == null )
            return out;

        for( TestEventScore tes : testEventScoreList )
        {
            // need scores
            if( !tes.getTestEventScoreType().getIsCompetency() || !tes.getHasScore() || !SimCompetencyVisibilityType.getValue( tes.getHide() ).getShowInReports()  )
                continue;

            if( SimCompetencyClass.getValue( tes.getSimCompetencyClassId() ).isAIMS())
                out.add( tes );
        }

        Collections.sort( out, new DisplayOrderComparator() ); //new TESNameComparator() );

        return out;
    }
    
    public List<TestEventScore> getScoredKsTestEventScoreList()
    {
        List<TestEventScore> out = new ArrayList<>();

        if( testEventScoreList == null )
            return out;

        for( TestEventScore tes : testEventScoreList )
        {
            // need scores
            if( !tes.getTestEventScoreType().getIsCompetency() || !tes.getHasScore() || !SimCompetencyVisibilityType.getValue( tes.getHide() ).getShowInReports()  )
                continue;

            if( SimCompetencyClass.getValue( tes.getSimCompetencyClassId() ).isKS() )
                out.add( tes );
        }

        Collections.sort( out, new DisplayOrderComparator() ); //new TESNameComparator() );
        
        return out;
    }

    public List<TestEventScore> getScoredBiodataTestEventScoreList()
    {
        return getScoredTestEventScoreList( TestEventScoreType.COMPETENCY.getTestEventScoreTypeId() , SimCompetencyClass.SCOREDBIODATA.getSimCompetencyClassId() );
    }

    public List<TestEventScore> getScoredEqTestEventScoreList()
    {
        return getScoredTestEventScoreList( TestEventScoreType.COMPETENCY.getTestEventScoreTypeId(), SimCompetencyClass.EQ.getSimCompetencyClassId() );
    }

    public List<TestEventScore> getScoredAiTestEventScoreList()
    {
        return getScoredTestEventScoreList( TestEventScoreType.COMPETENCY.getTestEventScoreTypeId() , SimCompetencyClass.VOICE_PERFORMANCE_INDEX.getSimCompetencyClassId() );
    }
    
    public List<TestEventScore> getScoredTestEventScoreList( int testEventScoreTypeId, int simCompetencyClassId )
    {
        List<TestEventScore> out = new ArrayList<>();

        if( testEventScoreList == null )
            return out;

        for( TestEventScore tes : testEventScoreList )
        {
            if( tes.getTestEventScoreTypeId() != testEventScoreTypeId )
                continue;

            // need scores
            if( !tes.getHasScore() || !SimCompetencyVisibilityType.getValue( tes.getHide() ).getShowInReports()  )
                continue;

            if( tes.getTestEventScoreType().getIsCompetency() )
            {

                // -1 means pure competency
                if( simCompetencyClassId == -1 && tes.getSimCompetencyClass().getIsPureCompetency() )
                    out.add( tes );

                // -2 means aggregate
                else if( simCompetencyClassId == -2 && tes.getSimCompetencyClass().getIsAggregate() )
                    out.add( tes );

                else if( simCompetencyClassId >= 0 && tes.getSimCompetencyClassId() == simCompetencyClassId )
                    out.add( tes );
            }

            else
                out.add( tes );
        }

        Collections.sort( out, new DisplayOrderComparator() ); //new TESNameComparator() );
        
        return out;
    }
    
    
    public TestEventScore getTestEventScoreForSimCompetencyGroupTypeId( int simCompetencyGroupTypeId )
    {
        if( this.testEventScoreList==null )
            return null;

        for( TestEventScore tes : this.testEventScoreList )
        {
            if( tes.getTestEventScoreType().equals( TestEventScoreType.COMPETENCYGROUP ) && tes.getIntParam1() == simCompetencyGroupTypeId )
                return tes;
        }

        return null;
    }


    public TestEventScore getTestEventScoreForSimCompetencyId( long simCompetencyId )
    {
        if( this.testEventScoreList==null )
            return null;

        for( TestEventScore tes : this.testEventScoreList )
        {
            if( tes.getTestEventScoreType().equals( TestEventScoreType.COMPETENCY ) && tes.getSimCompetencyId() == simCompetencyId )
                return tes;
        }

        return null;
    }

    public String getPercentileOrIpCountryName()
    {
        if( percentileCountryName != null && !percentileCountryName.isEmpty())
            return percentileCountryName;
        
        return ipCountryName;
    }
    
    
    public boolean getIsArchive()
    {
        return testEventArchiveId > 0;
    }

    public TestEventArchive getTestEventArchive()
    {
        TestEventArchive tea = new TestEventArchive();

        tea.setTestEventArchiveId(testEventArchiveId);
        tea.setTestEventId(testEventId);
        tea.setLastAccessDate(lastAccessDate);
        tea.setLocaleStr(localeStr);
        tea.setLocaleStrReport(localeStrReport);
        tea.setCorpId(corpId);
        tea.setSkinId(skinId);
        tea.setOrgId(orgId);
        tea.setSuborgId(suborgId);
        tea.setScoreColorSchemeTypeId( scoreColorSchemeTypeId );
        tea.setOverallRating(overallRating);
        tea.setOverallScore(overallScore);
        tea.setOverallPercentile(overallPercentile);
        tea.setAccountPercentile(accountPercentile);
        tea.setCountryPercentile(countryPercentile);
        tea.setOverallPercentileCount(overallPercentileCount);
        tea.setAccountPercentileCount(accountPercentileCount);
        tea.setCountryPercentileCount(countryPercentileCount);
        tea.setPercentileCountry(percentileCountry);
        tea.setExcludeFmNorms(excludeFmNorms);
        tea.setScoreFormatTypeId(scoreFormatTypeId);
        tea.setPercentComplete(percentComplete);
        tea.setBatteryId(batteryId);
        tea.setProductId(productId);
        tea.setProductTypeId(productTypeId);
        tea.setPin(pin);
        tea.setSimId(simId);
        tea.setSimVersionId(simVersionId);
        tea.setReportId(reportId);
        tea.setCreditId(creditId);
        tea.setCreditsUsed(creditsUsed);
        tea.setResultXml(resultXml);
        tea.setStartDate(startDate);
        tea.setTestEventStatusTypeId(testEventStatusTypeId);
        tea.setTestKeyId(testKeyId);
        tea.setUserId(userId);
        tea.setProctorUserId(proctorUserId);
        tea.setReleaseCode(releaseCode);
        tea.setExtRef(extRef);
        tea.setEducTypeId(educTypeId);
        tea.setExperTypeId(experTypeId);
        tea.setTrainTypeId(trainTypeId);
        tea.setUserAgentId(userAgentId);
        tea.setUserAgent(userAgent);
        tea.setIpAddress(ipAddress);
        tea.setIpCountry(ipCountry);
        tea.setIpState(ipState);
        tea.setIpCity(ipCity);
        tea.setGeographicRegionId(geographicRegionId);
        tea.setTextStr1(textStr1 );
        tea.setTotalTestTime(totalTestTime);
        tea.setThirdPartyTestEventId(thirdPartyTestEventId);
        tea.setThirdPartyTestAccountId(thirdPartyTestAccountId);

        return tea;
    }


    
    public String getOverallScore4Show()
    {        
        if( getUseScoreText4OverallNumeric() )
        {
            String s = ReportUtils.getScoreValueFromStr( getOverallScoreText() );
            
            if( s==null || s.length()>Constants.MAX_STR_LEN_FOR_SCORE_TEXT_AS_NUMSCORE )
                s="";
            return s;
            // return getOverallScoreText();
        }
        
        return I18nUtils.getFormattedNumber( getLocale(), getOverallScore(), getOverallScorePrecisionDigits());
        
        //if( this.getScorePrecisionDigits()==2 )
        //    return NumberUtils.getTwoDecimalFormattedAmount(getOverallScore());
        //if( this.getScorePrecisionDigits()==1 )
        //    return NumberUtils.getOneDecimalFormattedAmount(getOverallScore());
        //return Integer.toString(Math.round(overallScore) );
    }
    
    public boolean getShowItemScores()
    {
        if( getHideItemScores() )
            return false;
        
        return (report != null && report.getIncludeItemScores()==1) || getBooleanFlagValue( "itmscoreson" );
    }
    
    public boolean getSkipComparison()
    {
        if( report!=null && report.getIncludeNorms()!=1 )
            return true;
                
        return getBooleanFlagValue( "skipcomparisonsection" );
    }

    
    public boolean getHideItemScores()
    {
        if( report!=null && report.getIncludeItemScores()!=1 )
            return true;
        
        return getBooleanFlagValue( "itmscoresoff" );
    }
    
    public String getOverallScoreText()
    {
        TestEventScore tes = this.getOverallTestEventScore();

        if( tes != null && tes.getScoreText()!=null && !tes.getScoreText().isEmpty() )
            return tes.getScoreText();

        return "";
    }
            
    @Override
    public int getCompetencyScorePrecisionDigits()
    {
        return this.report!=null && this.report.getIntParam3()>=0 ? report.getIntParam3() : getScorePrecisionDigits();
    }
    
    
    public int getOverallScorePrecisionDigits()
    {
        return this.report!=null && this.report.getIntParam2()>=0 ? report.getIntParam2() : getScorePrecisionDigits();
    }
    
    // @Override
    public int getScorePrecisionDigits()
    {
        return getScoreFormatType().getScorePrecisionDigits();
    }

    public String getOverallRawScore4Show()
    {
        if( this.getTestEventScoreList()==null || getTestEventScoreList().isEmpty() || this.getOverallTestEventScore()==null )
            return getOverallScore4Show();

        if( getUseScoreText4OverallNumeric() )
        {
            String s = ReportUtils.getScoreValueFromStr( getOverallScoreText() );
            
            if( s==null || s.length()>Constants.MAX_STR_LEN_FOR_SCORE_TEXT_AS_NUMSCORE )
                s="";
            return s;
            // return ReportUtils.getScoreValueFromStr( getOverallScoreText() );
        }
                
        int scrDigits = this.report!=null && this.report.getIntParam3()>=0 ? report.getIntParam3() : -1;
        
        return getOverallTestEventScore().getRawScore4Show(getLocale(), scrDigits );        
    }
    
    public boolean getUseScoreText4CompetencyNumeric()
    {
        return getBooleanFlagValue( "cmptyscrtxtasnum" );        
    }    
    
    
    public boolean getUseScoreText4OverallNumeric()
    {
        return getBooleanFlagValue( "ovrscrtxtasnum" );
    }
    
    public ScoreFormatType getScoreFormatType()
    {
        TestEventScore tes = this.getOverallTestEventScore();
        
        if( tes != null )
            return tes.getScoreFormatType();
        
        return ScoreFormatType.getType(this.scoreFormatTypeId);
    }
    
    public boolean getHideIbmInsight()
    {
        if( report!=null && report.getIncludeIbmInsight()!=1 )
            return true;
        
        return getBooleanFlagValue( "getIncludeIbmInsight" );
    }

    public boolean getShowIbmInsightResults()
    {
        if( getHideIbmInsight() )
            return false;
        
        return report != null && report.getIncludeIbmInsight()==1;
    }

    
    public void appendErrorTxt( String t )
    {
        if( t == null )
            return;

        if( errorTxt == null )
            errorTxt = t;

        else
            errorTxt = t + "\n" + errorTxt;

        if( errorTxt != null && errorTxt.length()>1000 )
            errorTxt = errorTxt.substring(0,1000 );
    }
    
    @Override
    public String getName()
    {
        return product == null ? null : product.getName();
    }

    @Override
    public String getNameEnglish()
    {
        return product == null ? null : product.getNameEnglish();
    }

    public ProductType getProductType()
    {
        return ProductType.getValue( productTypeId );
    }

    public ReleaseCodeType getReleaseCodeType()
    {
        return ReleaseCodeType.getValue( releaseCode );
    }

    public TestEventStatusType getTestEventStatusType()
    {
        return TestEventStatusType.getValue( testEventStatusTypeId );
    }

    @Override
    public int compareTo(TestEvent o) {

        if( startDate != null && o.getStartDate() != null )
            return startDate.compareTo( o.getStartDate() );

        return ( (Long) testEventId ).compareTo( o.getTestEventId() );
    }
    

    public String getTestEventIdEncrypted()
    {
        try
        {
            return EncryptUtils.urlSafeEncrypt( testEventId );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "getTestEventIdEncrypted() " + toString() );

            return "";
        }
    }

    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + (int) (this.testEventId ^ (this.testEventId >>> 32));
        return hash;
    }



    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TestEvent other = (TestEvent) obj;
        if (this.testEventId != other.testEventId) {
            return false;
        }
        return true;
    }

    
    public boolean getBooleanFlagValue( String name )
    {
        //if( product == null || product.getDetailView()==null || product.getDetailView().isEmpty() || user==null ) // || user.getOrg()==null )
        //    return false;

        Org o = getOrg();
        if( o==null )
            return false;
        
        List<NVPair> pl = getOrg().getReportFlagList( getSuborg(), report, product );

        // LogService.logIt( "TestEvent.getBooleanFlagValue() " + name + ", Report Flag list is " + pl.size() + ", reportId=" + report.getReportId() );
        
        for( NVPair p : pl )
        {
            if( p.getName().equals( name ) && p.getValue().equals( "1" ) )
                return true;
        }

        return false;        
    }
    
    
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public long getTestEventId() {
        return testEventId;
    }

    public void setTestEventId(long testEventId) {
        this.testEventId = testEventId;
    }

    public long getTestKeyId() {
        return testKeyId;
    }

    public void setTestKeyId(long testKeyId) {
        this.testKeyId = testKeyId;
    }

    public int getTestEventStatusTypeId() {
        return testEventStatusTypeId;
    }

    public void setTestEventStatusTypeId(int testEventStatusTypeId) {
        this.testEventStatusTypeId = testEventStatusTypeId;
    }

    public String getLocaleStr() {
        return localeStr;
    }

    public void setLocaleStr(String localeStr) {
        this.localeStr = localeStr;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TestKey getTestKey() {
        return testKey;
    }

    public void setTestKey(TestKey testKey) {
        this.testKey = testKey;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public float getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(float percentComplete) {
        this.percentComplete = percentComplete;
    }

    public int getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
    }

    public float getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(float overallScore) {
        this.overallScore = overallScore;
    }

    public String getResultXml() {
        return resultXml;
    }

    public void setResultXml(String r) {

        if( r != null && r.trim().length() == 0 )
            r = null;

        this.resultXml = r;
    }

    
    public long getTestEventArchiveId() {
        return testEventArchiveId;
    }

    public void setTestEventArchiveId(long testEventArchiveId) {
        this.testEventArchiveId = testEventArchiveId;
    }

    public List<TestEventScore> getTestEventScoreList() {
        return testEventScoreList;
    }

    public void setTestEventScoreList(List<TestEventScore> testEventScoreList) {
        this.testEventScoreList = testEventScoreList;
    }

    public long getProctorUserId() {
        return proctorUserId;
    }

    public void setProctorUserId(long proctorUserId) {
        this.proctorUserId = proctorUserId;
    }

    public int getCorpId() {
        return corpId;
    }

    public void setCorpId(int corpId) {
        this.corpId = corpId;
    }

    public int getSkinId() {
        return skinId;
    }

    public void setSkinId(int skinId) {
        this.skinId = skinId;
    }


    public int getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(int batteryId) {
        this.batteryId = batteryId;
    }

    public Battery getBattery() {
        return battery;
    }

    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    public long getCreditId() {
        return creditId;
    }

    public void setCreditId(long creditId) {
        this.creditId = creditId;
    }

    public int getReleaseCode() {
        return releaseCode;
    }

    public void setReleaseCode(int releaseCode) {
        this.releaseCode = releaseCode;
    }

    public String getExtRef() {
        return extRef;
    }

    public void setExtRef(String extRef) {
        this.extRef = extRef;
    }

    public long getSimId() {
        return simId;
    }

    public void setSimId(long simId) {
        this.simId = simId;
    }

    public int getSimVersionId() {
        return simVersionId;
    }

    public void setSimVersionId(int simVersionId) {
        this.simVersionId = simVersionId;
    }

    public int getScoreFormatTypeId() {
        return scoreFormatTypeId;
    }

    public void setScoreFormatTypeId(int scoreFormatTypeId) {
        this.scoreFormatTypeId = scoreFormatTypeId;
    }

    public String getErrorTxt() {
        return errorTxt;
    }

    public void setErrorTxt(String errorTxt) {
        this.errorTxt = errorTxt;
    }

    public int getEducTypeId() {
        return educTypeId;
    }

    public void setEducTypeId(int educTypeId) {
        this.educTypeId = educTypeId;
    }

    public int getExperTypeId() {
        return experTypeId;
    }

    public void setExperTypeId(int experTypeId) {
        this.experTypeId = experTypeId;
    }

    public int getTrainTypeId() {
        return trainTypeId;
    }

    public void setTrainTypeId(int trainTypeId) {
        this.trainTypeId = trainTypeId;
    }

    public int getSuborgId()
    {
        return suborgId;
    }

    public void setSuborgId(int suborgId)
    {
        this.suborgId = suborgId;
    }

    public int getCreditsUsed()
    {
        return creditsUsed;
    }

    public void setCreditsUsed(int creditsUsed)
    {
        this.creditsUsed = creditsUsed;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
        this.productTypeId = productTypeId;
    }

    public int getUserAgentId() {
        return userAgentId;
    }

    public void setUserAgentId(int userAgentId) {
        this.userAgentId = userAgentId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpCountry() {
        return ipCountry;
    }

    public void setIpCountry(String ipCountry) {
        this.ipCountry = ipCountry;
    }

    public String getIpState() {
        return ipState;
    }

    public void setIpState(String ipState) {
        this.ipState = ipState;
    }

    public String getIpCity() {
        return ipCity;
    }

    public void setIpCity(String ipCity) {
        this.ipCity = ipCity;
    }

    public int getGeographicRegionId() {
        return geographicRegionId;
    }

    public void setGeographicRegionId(int geographicRegionId) {
        this.geographicRegionId = geographicRegionId;
    }


    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public Suborg getSuborg() {
        return suborg;
    }

    public void setSuborg(Suborg suborg) {
        this.suborg = suborg;
    }

    public float getOverallPercentile() {
        return overallPercentile;
    }

    public void setOverallPercentile(float overallPercentile) {
        this.overallPercentile = overallPercentile;
    }

    public String getTextStr1() {
        return textStr1;
    }

    public void setTextStr1(String textStr1) {
        this.textStr1 = textStr1;
    }

    public float getTotalTestTime() {
        return totalTestTime;
    }

    public void setTotalTestTime(float totalTestTime) {
        this.totalTestTime = totalTestTime;
    }

    public float getAccountPercentile() {
        return accountPercentile;
    }

    public void setAccountPercentile(float accountPercentile) {
        this.accountPercentile = accountPercentile;
    }

    public float getCountryPercentile() {
        return countryPercentile;
    }

    public void setCountryPercentile(float countryPercentile) {
        this.countryPercentile = countryPercentile;
    }

    public int getOverallPercentileCount() {
        return overallPercentileCount;
    }

    public void setOverallPercentileCount(int overallPercentileCount) {
        this.overallPercentileCount = overallPercentileCount;
    }

    public int getAccountPercentileCount() {
        return accountPercentileCount;
    }

    public void setAccountPercentileCount(int accountPercentileCount) {
        this.accountPercentileCount = accountPercentileCount;
    }

    public int getCountryPercentileCount() {
        return countryPercentileCount;
    }

    public void setCountryPercentileCount(int countryPercentileCount) {
        this.countryPercentileCount = countryPercentileCount;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public int getExcludeFmNorms() {
        return excludeFmNorms;
    }

    public void setExcludeFmNorms(int excludeFmNorms) {
        this.excludeFmNorms = excludeFmNorms;
    }

    public boolean getExcludeFmNormsBoolean() {
        return excludeFmNorms==1;
    }

    public void setExcludeFmNormsBoolean(boolean b) {
        this.excludeFmNorms = b ? 1 : 0;
    }

    public String getLocaleStrReport() {
        return localeStrReport;
    }

    public void setLocaleStrReport(String localeStrReport) {
        this.localeStrReport = localeStrReport;
    }


    public String getThirdPartyTestEventId() {
        return thirdPartyTestEventId;
    }

    public void setThirdPartyTestEventId(String thirdPartyTestEventId) {
        this.thirdPartyTestEventId = thirdPartyTestEventId;
    }

    public String getThirdPartyTestAccountId() {
        return thirdPartyTestAccountId;
    }

    public void setThirdPartyTestAccountId(String thirdPartyTestAccountId) {
        this.thirdPartyTestAccountId = thirdPartyTestAccountId;
    }

    public String getPercentileCountry() {
        return percentileCountry;
    }

    public void setPercentileCountry(String percentileCountry) {
        this.percentileCountry = percentileCountry;
    }

    public RemoteProctorEvent getRemoteProctorEvent() {
        return remoteProctorEvent;
    }

    public void setRemoteProctorEvent(RemoteProctorEvent remoteProctorEvent) {
        this.remoteProctorEvent = remoteProctorEvent;
    }

    public Date getLastTimeUpdate() {
        return lastTimeUpdate;
    }

    public void setLastTimeUpdate(Date lastTimeUpdate) {
        this.lastTimeUpdate = lastTimeUpdate;
    }

    public List<TestEventResponseRating> getTestEventResponseRatingList() {
        return testEventResponseRatingList;
    }

    public void setTestEventResponseRatingList(List<TestEventResponseRating> testEventResponseRatingList) {
        this.testEventResponseRatingList = testEventResponseRatingList;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public List<InsightReportTrait> getInsightReportTraitList() {
        return insightReportTraitList;
    }

    public void setInsightReportTraitList(List<InsightReportTrait> insightReportTraitList) {
        this.insightReportTraitList = insightReportTraitList;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public List<ItemResponseGroup> getItemResponseGroupList() {
        return itemResponseGroupList;
    }

    public void setItemResponseGroupList(List<ItemResponseGroup> itemResponseGroupList) {
        this.itemResponseGroupList = itemResponseGroupList;
    }

    public List<CompetencyGroup> getCompetencyGroupList() {
        return competencyGroupList;
    }

    public void setCompetencyGroupList(List<CompetencyGroup> competencyGroupList) {
        this.competencyGroupList = competencyGroupList;
    }

    public Profile getReportRangeProfile() {
        return reportRangeProfile;
    }

    public void setReportRangeProfile(Profile reportRangeProfile) {
        this.reportRangeProfile = reportRangeProfile;
    }

    public String getPercentileCountryName() {
        return percentileCountryName;
    }

    public void setPercentileCountryName(String percentileCountryName) {
        this.percentileCountryName = percentileCountryName;
    }

    public String getIpCountryName() {
        return ipCountryName;
    }

    public void setIpCountryName(String ipCountryName) {
        this.ipCountryName = ipCountryName;
    }

    public List<Profile> getAltScoreProfileList() {
        return altScoreProfileList;
    }

    public void setAltScoreProfileList(List<Profile> altScoreProfileList) {
        this.altScoreProfileList = altScoreProfileList;
    }

    public OrgAutoTest getOrgAutoTest() {
        return orgAutoTest;
    }

    public void setOrgAutoTest(OrgAutoTest orgAutoTest) {
        this.orgAutoTest = orgAutoTest;
    }

    public int getScoreColorSchemeTypeId() {
        return scoreColorSchemeTypeId;
    }

    public void setScoreColorSchemeTypeId(int scoreColorSchemeTypeId) {
        this.scoreColorSchemeTypeId = scoreColorSchemeTypeId;
    }

 


}
