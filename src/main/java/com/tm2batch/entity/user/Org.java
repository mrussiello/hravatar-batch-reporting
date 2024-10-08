package com.tm2batch.entity.user;

import com.tm2batch.entity.lvi.LvOrgPrefs;
import com.tm2batch.entity.purchase.Product;
import com.tm2batch.entity.ref.RcOrgPrefs;
import com.tm2batch.entity.report.Report;
import com.tm2batch.user.OrgCreditUsageType;
import com.tm2batch.user.UserAnonymityType;
import com.tm2batch.util.NVPair;
import com.tm2batch.util.StringUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;


@Cacheable
@Entity
@Table( name = "org" )
@XmlRootElement
public class Org implements Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="orgid")
    private int orgId;

    @Column(name="name")
    private String name;

    @Column( name = "orgcreditusagetypeid" )
    private int orgCreditUsageTypeId;
    
    @Column(name="adminuserid")
    private long adminUserId = 0;

    @Column( name = "affiliateid" )
    private String affiliateId;

    @Column(name="orgstatustypeid")
    private int orgStatusTypeId = 0;


    @Column( name = "defaulttesttakerlang" )
    private String defaultTestTakerLang;

    @Column( name = "defaultreportlang" )
    private String defaultReportLang;


    @Column(name="showoverallrawscore")
    private int showOverallRawScore;
    
    @Column(name="customfieldname1")
    private String customFieldName1;

    @Column(name="customfieldname2")
    private String customFieldName2;

    @Column(name="customfieldname3")
    private String customFieldName3;
    
    @Column(name="performancenames")
    private String performanceNames;    
    
    
    @Column(name="ratingnames")
    private String ratingNames;    
    
    @Column(name="reportlogourl")
    private String reportLogoUrl;

    @Column( name = "sharedemo" )
    private int shareDemo = 0;
                
    
    /*
     0 = none
     1 = both
     2= Live video interviewing only , no testing
     3= testing only, no live video interviewing    
    */
    @Column( name = "aiok" )
    private int aiOk;
    
    @Column( name = "useranonymitytypeid" )
    private int userAnonymityTypeId;

    
        
    /**
     * 
     * packed string ruleid1|value1|ruleid2|value2;
     */
    @Column(name="reportflags")
    private String reportFlags;


    @Transient
    private LvOrgPrefs lvOrgPrefs;

    @Transient
    private RcOrgPrefs rcOrgPrefs;

    @Transient
    private String[] ratingNamesArray;
    
    
    @Override
    public boolean equals( Object o )
    {
        if( o instanceof Org )
        {
            Org u = (Org) o;

            return orgId == u.getOrgId();
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 71 * hash + (int) (this.orgId ^ (this.orgId >>> 32));
        return hash;
    }

    public List<NVPair> getReportFlagList(Suborg suborg, Report report, Product product)
    {
        List<NVPair> out = new ArrayList<>();
        
        List<NVPair> l2;
              
        // Start with Report - lowest level
        if( report != null )
            out = report.getReportFlagList();

        // Product is the next level.
        if( product!=null )
        {
            l2 = product.getReportFlagList();
            
            // LogService.logIt( "Org.getReportFlagList()  Product id=" + product.getProductId() + "  has " + l2.size() + " report flags." );

            for( NVPair pr : out )
            {
                if( !hasNvPair( pr, l2 ) )
                {
                    l2.add( pr );
                }
            }

            out=l2;            
        }        
        
        
        
            // Next level is Org
        if( reportFlags !=null && !reportFlags.isEmpty() )
        {
            l2 = getReportFlagList();
            
            for( NVPair pr : out )
            {
                // Only add from out if not in l2
                if( !hasNvPair( pr, l2 ) )
                {
                    l2.add( pr );
                }
            }
            
            out=l2;
        }

        // Highest level is Suborg list
        if( suborg != null )
        {
            l2 = suborg.getReportFlagList();
            
            for( NVPair pr : out )
            {
                if( !hasNvPair( pr, l2 ) )
                {
                    l2.add( pr );
                }
            }

            out=l2;
        }

        
        return out;
    }
    
    
    public String getRatingName1()
    {
        return getRatingName(1);
    }

    public String getRatingName2()
    {
        return getRatingName(2);
    }

    public String getRatingName3()
    {
        return getRatingName(3);
    }

    public String getRatingName4()
    {
        return getRatingName(4);
    }

    public String getRatingName5()
    {
        return getRatingName(5);
    }

    public String getRatingName6()
    {
        return getRatingName(6);
    }

    public String getRatingName7()
    {
        return getRatingName(7);
    }

    public String getRatingName8()
    {
        return getRatingName(8);
    }

    public String getRatingName9()
    {
        return getRatingName(9);
    }

    public String getRatingName10()
    {
        return getRatingName(10);
    }

    public int getRatingNameCount()
    {
        int ct = 0;
        for( String s : getRatingNamesArray() )
        {
            if( s!=null && !s.isBlank() )
                ct++;
            else
                break;
        }
        return ct;
    }
    
    public String getRatingName(int idx)
    {
        return getRatingNamesArray()[idx-1];
    }
    
    public String[] getRatingNamesArray()
    {
        if( ratingNamesArray!=null )
            return ratingNamesArray;
        
        if( ratingNames == null || ratingNames.isBlank() )
        {
            ratingNamesArray = new String[10];
            return ratingNamesArray;
        }

        List<String> out = new ArrayList<>();

        for( String s : ratingNames.split( ";" ) )
        {
            if( s==null )
                continue;

            s = s.trim();

            out.add( s );
        }
        
        while( out.size()<10 )
        {
            out.add("");
        }

	String[] sa = new String[out.size()];
        out.toArray(sa);
        ratingNamesArray=sa;
        return sa;
    }
    
    
    
    public OrgCreditUsageType getOrgCreditUsageType()    
    {
        return OrgCreditUsageType.getValue( orgCreditUsageTypeId );        
    }
    
    public List<NVPair> getReportFlagList(Suborg suborg )
    {
        List<NVPair> out = new ArrayList<>();
        
        List<NVPair> l2;
              
        
        // LogService.logIt( "Org.getReportFlagList() AAA.1 out has " + out.size() + " report flags." );        
        
            // Next level is Org
        if( reportFlags !=null && !reportFlags.isEmpty() )
        {
            l2 = getReportFlagList();
            
            for( NVPair pr : out )
            {
                // Only add from out if not in l2
                if( !hasNvPair( pr, l2 ) )
                {
                    l2.add( pr );
                }
            }
            
            out=l2;
        }

        // LogService.logIt( "Org.getReportFlagList() AAA.2 out has " + out.size() + " report flags." );        
        
        // Highest level is Suborg list
        if( suborg != null )
        {
            l2 = suborg.getReportFlagList();
            
            for( NVPair pr : out )
            {
                if( !hasNvPair( pr, l2 ) )
                {
                    l2.add( pr );
                }
            }

            out=l2;
        }

        return out;
    }

    
    public UserAnonymityType getUserAnonymityType()
    {
        return UserAnonymityType.getValue( userAnonymityTypeId );
    }
    
    
    
    public boolean hasNvPair( NVPair nvp, List<NVPair> pl  )
    {
        if( nvp == null )
            return false;
        
        for( NVPair pr : pl )
        {
            if( pr.getName()!=null && pr.getName().equals( nvp.getName() ) )
                return true;
        }
        
        return false;
    }
    

    
    public List<NVPair> getReportFlagList()
    {
        return StringUtils.parseNVPairsList( reportFlags, "|" );          
    }
    
        
    
    public List<String> getPerformanceNamesList()
    {
        List<String> out = new ArrayList<>();

        if( this.performanceNames == null || performanceNames.isEmpty() )
            return out;


        for( String s : performanceNames.split( ";" ) )
        {
            if( s==null )
                continue;

            s = s.trim();

            out.add( s );
        }

        return out;
    }
    
    public String getPerformanceName1()
    {
        return getPerformanceName(1);
    }

    public String getPerformanceName2()
    {
        return getPerformanceName(2);
    }

    public String getPerformanceName3()
    {
        return getPerformanceName(3);
    }

    
    public String getPerformanceName( int idx  )
    {
        List<String> sl = getPerformanceNamesList();
        
        if( sl.size()>=idx )
        {
            String s = sl.get(idx-1);
            
            return s == null ? "": s.trim();
        }
        
        return "";
    }
    
    
    
    
    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(long adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getAffiliateId() {
        return affiliateId;
    }

    public void setAffiliateId(String affiliateId) {
        this.affiliateId = affiliateId;
    }

    public int getOrgStatusTypeId() {
        return orgStatusTypeId;
    }

    public void setOrgStatusTypeId(int orgStatusTypeId) {
        this.orgStatusTypeId = orgStatusTypeId;
    }

    public String getDefaultTestTakerLang() {
        return defaultTestTakerLang;
    }

    public void setDefaultTestTakerLang(String defaultTestTakerLang) {
        this.defaultTestTakerLang = defaultTestTakerLang;
    }

    public String getDefaultReportLang() {
        return defaultReportLang;
    }

    public void setDefaultReportLang(String defaultReportLang) {
        this.defaultReportLang = defaultReportLang;
    }

    public int getShowOverallRawScore() {
        return showOverallRawScore;
    }

    public void setShowOverallRawScore(int showOverallRawScore) {
        this.showOverallRawScore = showOverallRawScore;
    }


    public String getReportFlags() {
        return reportFlags;
    }

    public void setReportFlags(String reportFlags) {
        this.reportFlags = reportFlags;
    }

    public int getAiOk() {
        return aiOk;
    }

    public void setAiOk(int aiOk) {
        this.aiOk = aiOk;
    }

    public boolean getLvAiOk() {
        return aiOk==1 || aiOk==2;
    }

    public String getCustomFieldName1() {
        return customFieldName1;
    }

    public void setCustomFieldName1(String customFieldName1) {
        this.customFieldName1 = customFieldName1;
    }

    public String getCustomFieldName2() {
        return customFieldName2;
    }

    public void setCustomFieldName2(String customFieldName2) {
        this.customFieldName2 = customFieldName2;
    }

    public String getCustomFieldName3() {
        return customFieldName3;
    }

    public void setCustomFieldName3(String customFieldName3) {
        this.customFieldName3 = customFieldName3;
    }

    public String getPerformanceNames() {
        return performanceNames;
    }

    public void setPerformanceNames(String performanceNames) {
        this.performanceNames = performanceNames;
    }

    public int getUserAnonymityTypeId() {
        return userAnonymityTypeId;
    }

    public void setUserAnonymityTypeId(int userAnonymityTypeId) {
        this.userAnonymityTypeId = userAnonymityTypeId;
    }

    public LvOrgPrefs getLvOrgPrefs() {
        return lvOrgPrefs;
    }

    public void setLvOrgPrefs(LvOrgPrefs lvOrgPrefs) {
        this.lvOrgPrefs = lvOrgPrefs;
    }

    public RcOrgPrefs getRcOrgPrefs() {
        return rcOrgPrefs;
    }

    public void setRcOrgPrefs(RcOrgPrefs rcOrgPrefs) {
        this.rcOrgPrefs = rcOrgPrefs;
    }

    public int getOrgCreditUsageTypeId() {
        return orgCreditUsageTypeId;
    }

    public void setOrgCreditUsageTypeId(int orgCreditUsageTypeId) {
        this.orgCreditUsageTypeId = orgCreditUsageTypeId;
    }

    public String getReportLogoUrl() {
        return reportLogoUrl;
    }

    public void setReportLogoUrl(String reportLogoUrl) {
        this.reportLogoUrl = reportLogoUrl;
    }

    public int getShareDemo() {
        return shareDemo;
    }

    public void setShareDemo(int shareDemo) {
        this.shareDemo = shareDemo;
    }
    
    public boolean getShareDemoB() {
        return shareDemo==1;
    }

    
}
