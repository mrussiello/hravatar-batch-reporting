package com.tm2batch.entity.ref;


import com.tm2batch.entity.report.Report;
import com.tm2batch.entity.user.Org;
import com.tm2batch.entity.user.User;
import com.tm2batch.entity.user.UserAction;
import com.tm2batch.global.Constants;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.ref.RcCandidateStatusType;
import com.tm2batch.ref.RcCheckStatusType;
import com.tm2batch.ref.RcCheckType;
import com.tm2batch.ref.RcCompetencyWrapper;
import com.tm2batch.ref.RcItemWrapper;
import com.tm2batch.service.EncryptUtils;
import com.tm2batch.service.LogService;
import com.tm2batch.util.MessageFactory;
import com.tm2batch.util.StringUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
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


@Cacheable
@Entity
@Table( name = "rccheck" )
@NamedQueries({
        // @NamedQuery( name = "RcCheck.findByOrgId", query = "SELECT o FROM RcCheck AS o WHERE o.orgId=:orgId" ),
        @NamedQuery( name = "RcCheck.findByRcCheckId", query = "SELECT o FROM RcCheck AS o WHERE o.rcCheckId=:rcCheckId" )
})
public class RcCheck implements Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="rccheckid")
    private long rcCheckId;

    @Column(name="orgid")
    private int orgId;

    @Column(name="suborgid")
    private int suborgId;

    @Column(name="remindertypeid")
    private int reminderTypeId;

    @Column(name="reportid")
    private int reportId;

    @Column(name="reportid2")
    private int reportId2;

    
    /**
     * 0=pre-hire
     * 1=employee
     */
    @Column(name="rcchecktypeid")
    private int rcCheckTypeId;

    @Column(name="rccheckstatustypeid")
    private int rcCheckStatusTypeId;

    @Column(name="rccheckscoringstatustypeid")
    private int rcCheckScoringStatusTypeId;

    @Column(name="emailresultsto")
    private String emailResultsTo;

    @Column(name="textresultsto")
    private String textResultsTo;

    @Column(name="returnurl")
    private String returnUrl;

    @Column(name="distributiontypeid")
    private int distributionTypeId;
    
    @Column(name="adminuserid")
    private long adminUserId;

    @Column(name="userid")
    private long userId;

    @Column(name="candidatecannotaddraters")
    private int candidateCannotAddRaters;


    
    @Column(name="collectcandidateratings")
    private int collectCandidateRatings = 0;

    
    
    @Column(name="candidateaccesscode")
    private String candidateAccessCode;

    @Column(name="candidatestarts")
    private int candidateStarts;
       
    @Column(name="candidateseconds")
    private int candidateSeconds;
    
    @Column(name="minsupervisors")
    private int minSupervisors = 1;

    @Column(name="minraters")
    private int minRaters = 2;

    @Column(name="maxraters")
    private int maxRaters = 0;

    @Column(name="enforceraterlimits")
    private int enforceRaterLimits;

    @Column(name="testkeyid")
    private long testKeyId;
        
    @Column(name="creditid")
    private long creditId;

    @Column(name="creditindex")
    private int creditIndex = 0;
    
    @Column(name="jobtitle")
    private String jobTitle;

    @Column(name="langcode")
    private String langCode;

    @Column(name="overallscore")
    private float overallScore;

    @Column(name="percentcomplete")
    private float percentComplete;
    

    @Column(name="corpid")
    private int corpId;

    @Column(name="rccandidatestatustypeid")
    private int rcCandidateStatusTypeId = 0;

    @Column(name="extref")
    private String extRef;

    @Column(name="rcscriptid")
    private int rcScriptId;
    
    
    @Column(name="ipaddress")
    private String ipAddress;

    @Column(name="useragent")
    private String userAgent;

    @Column(name="textstr1")
    private String textStr1;

    @Column(name="customparameters")
    private String customParameters;

    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="senddate")
    private Date sendDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="firstcandidatesenddate")
    private Date firstCandidateSendDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastcandidatesenddate")
    private Date lastCandidateSendDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="firstcandidatereferencedate")
    private Date firstCandidateReferenceDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastcandidatereferencedate")
    private Date lastCandidateReferenceDate;
    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="candidatestartdate")
    private Date candidateStartDate;
    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="candidatecompletedate")
    private Date candidateCompleteDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="candidatelastupdate")
    private Date candidateLastUpdate;

    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="completedate")
    private Date completeDate;

    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="scoredate")
    private Date scoreDate;

    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastupdate")
    private Date lastUpdate;

    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="expiredate")
    private Date expireDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="candidatereleasedate")
    private Date candidateReleaseDate;
    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastcandidatereminderdate")
    private Date lastCandidateReminderDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastcandidateprogressmsgdate")
    private Date lastCandidateProgressMsgDate;
    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastprogressmsgdate")
    private Date lastProgressMsgDate;
    
    
    @Transient
    private RcOrgPrefs rcOrgPrefs;
    
    @Transient
    private List<RcRater> rcRaterList;

    @Transient
    private List<RcReferral> rcReferralList;
    
    
    @Transient
    private List<RcSuspiciousActivity> rcSuspiciousActivityList;
    
    @Transient
    private User adminUser;
    
    @Transient
    private User user;
    
    @Transient
    private Org org;
    
    @Transient
    private RcScript rcScript;
    
    @Transient
    private Report report;

    @Transient
    private Locale locale;
    
    @Transient
    private TimeZone timeZone;

    @Override
    public String toString() {
        return "RcCheck{" + "rcCheckId=" + rcCheckId + ", orgId=" + orgId + ", userId=" + userId + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (this.rcCheckId ^ (this.rcCheckId >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RcCheck other = (RcCheck) obj;
        if (this.rcCheckId != other.rcCheckId) {
            return false;
        }
        return true;
    }
    
    public boolean getShowCompetencySummary()
    {
        if( rcCheckId<=0 || !getRcCheckStatusType().getStartedOrHigher() || !getHasRatingsToShow() ||rcScript==null || rcScript.getRcCompetencyWrapperList()==null || rcScript.getRcCompetencyWrapperList().isEmpty() )
            return false;
        
        for( RcCompetencyWrapper rcw : rcScript.getRcCompetencyWrapperList() )
        {
            if( rcw.getIsScored() )
                return true;
        }
        return false;
    }
    
    public boolean getIsSelfOnly()
    {
        return getCollectCandidateRatingsB() && rcRaterList!=null && rcRaterList.size()==1 && rcRaterList.get(0).getUserId()==userId;        
    }
    
    
    public int getScoreDigits()
    {
        return report!=null && report.getIntParam2()>=0 ? report.getIntParam2() : Constants.RCCHECK_DEF_SCORE_PRECISION_DIGITS;
    }
        
    public boolean getNeedsAnyCandidateInput()
    {
        return getCandidateCanAddRatersB() || (rcScript==null || rcScript.getHasCandidateInput() ) || collectCandidateRatings>0;
    }

    public long getCandidateRcRaterId()
    {
        if( this.getRcRaterList()==null )
            return 0;
        for( RcRater r : this.rcRaterList )
        {
            if( r.getUserId()==this.userId )
                return r.getRcRaterId();
        }
        return 0;
    }
    
    
    public int[] getRaterRoleTypeCountsCandidate()
    {
        return getRaterRoleTypeCounts( true );
    }
    public int[] getRaterRoleTypeCounts()
    {
        return getRaterRoleTypeCounts( false );
    }
    
    /**
     * data[0]=total
     * data[1]=supervisor/Manager
     * data[2]=peer
     * data[3]=subordinate
     * data[4]=other/unknown
     * 
     * @param candidateOnly
     * @return 
     */
    public int[] getRaterRoleTypeCounts( boolean candidateOnly )
    {
        int[] out = new int[5];
        if( rcRaterList==null )
            return out;
        for( RcRater r : rcRaterList )
        {
            if( candidateOnly && (r.getRcRaterSourceType()==null || r.getRcRaterSourceType().getIsAccountUser()) )
                continue;
            
            if( r.getRcRaterStatusType().getIsDeactivated() )
                continue;
            
            out[0] ++;
            
            if( r.getRcRaterRoleType().getIsSupervisorOrManager() )
                out[1]++;
            if( r.getRcRaterRoleType().getIsPeer() )
                out[2]++;
            if( r.getRcRaterRoleType().getIsSubordinate() )
                out[3]++;
            if( r.getRcRaterRoleType().getIsOther() || r.getRcRaterRoleType().getIsUnknown() )
                out[4]++;
        }
        return out;
    }
    
    
    public List<RcItemWrapper> getItemWrappersWithRatingsList()
    {
        List<RcItemWrapper> out = new ArrayList<>();
        
        if( rcScript==null || rcScript.getAllItemWrapperList()==null || rcScript.getAllItemWrapperList().isEmpty() )
            return out;
        
        for( RcItemWrapper rciw : rcScript.getAllItemWrapperList() )
        {
            if( rciw.getHasRatingData() )
                out.add(rciw);
        }
        return out;        
    }
    
    
    
    public boolean getHasAnyRecordedRatings()
    {
        if( rcRaterList==null )
            return false;
        
        for( RcRater r : rcRaterList )
        {
            if( r.getRcRatingList()!=null && !r.getRcRatingList().isEmpty() )
                return true;
        }
        return false;
    }
    
    public boolean getHasRatingsToShow()
    {
        //LogService.logIt( "RcCheck.getHasRatingsToShow() AAA " );
        if( rcScript==null || rcScript.getAllItemWrapperList()==null || rcScript.getAllItemWrapperList().isEmpty() )
            return false;
        
        //LogService.logIt( "RcCheck.getHasRatingsToShow() BBB " );
        
        for( RcItemWrapper rciw : rcScript.getAllItemWrapperList() )
        {
            //LogService.logIt( "RcCheck.getHasRatingsToShow() CCC rcItemId=" + rciw.getRcItemId() );
            if( rciw.getHasRatingData() )
                return true;
        }
        return false;
    }
    
    public boolean getHasIncompleteRaters()
    {
        if( rcRaterList==null )
            return false;
        for( RcRater rater : rcRaterList )
        {
            if( !rater.getRcRaterStatusType().getIsCompleteOrHigher() )
                return true;
        }
        return false;
    }
    
    
    public RcRater getRcRaterForRcRaterId( long rcRaterId )
    {
        if( rcRaterList==null )
            return null;
        for( RcRater r : rcRaterList )
        {
            if( r.getRcRaterId()==rcRaterId )
                return r;
        }
        return null;
    }
    
    public RcRater getRcRaterForUserId( long userId )
    {
        if( rcRaterList==null )
            return null;
        for( RcRater r : rcRaterList )
        {
            if( r.getUserId()==userId )
                return r;
        }
        return null;
    }
    
    public void setItemsAndRatings() throws Exception
    {
        if( rcScript==null )
        {
            LogService.logIt( "RcCheck.setItemsAndRatings() rcScript is null. Cannot set items and ratings. rcCheckId=" + rcCheckId );
            return;
        }
        
        rcScript.clearRatings();
        RcRating rtg;
        RcCompetencyWrapper rcw;
        for( RcItemWrapper rciw : rcScript.getAllItemWrapperList() )
        {
            if( rciw.getRcItem().getRcCompetency()==null && rcScript!=null )
            {
                rcw = getRcScript().getRcCompetencyWrapper( rciw.getRcItem().getRcCompetencyId() );
                if( rcw!=null )
                    rciw.getRcItem().setRcCompetency( rcw.getRcCompetency() );                    
            }
            
            for( RcRater rcr : rcRaterList )
            {
                rtg = rcr.getRcRating( rciw.getRcItemId() );
                if( rtg!=null && rtg.getHasData() )
                {
                    rtg.setRcItem( rciw.getRcItem() );
                    rtg.setUser( rcr.getUser() );
                    rciw.addRating( rtg );
                }
            }            
        }
    }
    

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
    
    
    
    public String getRcCheckIdEncrypted()
    {
        try
        {
            return EncryptUtils.urlSafeEncrypt( rcCheckId );
        }
        catch( Exception e )
        {
            LogService.logIt(e, "RcCheck.getRcCheckIdEncrypted() " + toString()  );
            return "";
        }
    }
    
    public int getCandidateCompleteSecs()
    {
        if( this.candidateStartDate==null || this.candidateCompleteDate==null )
            return 0;
        
        long secs = (candidateCompleteDate.getTime() - candidateStartDate.getTime())/1000;
        
        return secs>0 ? (int) secs : 0;
    }
    
    
    public long getRcCheckId() {
        return rcCheckId;
    }

    public void setRcCheckId(long rcCheckId) {
        this.rcCheckId = rcCheckId;
    }

    public String getRcCheckStatusTypeName()
    {
        return getRcCheckStatusType().getName(locale);
    }
    
    public String getRcCheckTypeName()
    {
        return getRcCheckType().getName(locale);
    }
    public String getLanguageName()
    {
        return MessageFactory.getStringMessage(locale==null ? Locale.US: locale, langCode==null ? "en_US" : langCode);
    }
    public String getCountryName()
    {
        return MessageFactory.getStringMessage(locale==null ? Locale.US: locale, user==null || user.getCountryCode()==null || user.getCountryCode().isBlank() ? "cntry.US" : "cntry." + user.getCountryCode().toUpperCase() );
    }


    public String getRcCheckStatusTypeFull()
    {
        String s = getRcCheckStatusTypeName();
        if( getRcCheckStatusType().getIsRaterInput() )
            s += " " + MessageFactory.getStringMessage(locale, "g.RCChk.PctComplt", new String[] { Integer.toString((int)percentComplete )} );

        else if( getIsScoreAvailable())
            s += " " + MessageFactory.getStringMessage(locale, "g.RCChk.Scr", new String[] { Integer.toString((int)this.overallScore )} );
        
        return s;
    }
    
    public boolean getIsScoreAvailable()
    {
        return this.completeDate!=null;
    }

    public String getCandidateStartUrl()
    {
        return RuntimeConstants.getStringValue( "RefCheckCandBaseUrl" ) + candidateAccessCode;        
    }
    
    public String getResultsViewUrl()
    {
        return RuntimeConstants.getStringValue( "RefCheckResultsBaseUrl" ) + "?rci=" + getRcCheckIdEncrypted() + "&am=SR";        
    }
    
    public RcCheckStatusType getRcCheckStatusType()
    {
        return RcCheckStatusType.getValue( rcCheckStatusTypeId );
    }

    public RcCandidateStatusType getRcCandidateStatusType()
    {
        return RcCandidateStatusType.getValue(this.rcCandidateStatusTypeId );
    }
    
    public RcCheckType getRcCheckType()
    {
        return RcCheckType.getValue( rcCheckTypeId );
    }
    

    

    
    public String getCandidateInputStr1()
    {
        return getCandidateInputStr( 1 );
    }
    public String getCandidateInputStr2()
    {
        return getCandidateInputStr( 2 );
    }
    public String getCandidateInputStr3()
    {
        return getCandidateInputStr( 3 );
    }
    public String getCandidateInputStr4()
    {
        return getCandidateInputStr( 4 );
    }
    public String getCandidateInputStr5()
    {
        return getCandidateInputStr( 5 );
    }
    
        
    public String getCandidateInputStr( int idx )
    {
        // idx must be between 1 and 5
        if( idx<=0 || idx>5 )
            return null;
        String s = textStr1==null || textStr1.isBlank() ? null : StringUtils.getBracketedArtifactFromString( textStr1, "CANDIDATEINPUTSTR" + idx );
        return s==null ? "" : s;
    }
    
    

    
    public int[] getCandidateDaysHrsMins()
    {
        int[] out = new int[3];
        if( candidateSeconds<=0 )
            return out;
        if( candidateSeconds<60 )
        {
            out[2]=1;
            return out;
        }
        int d = candidateSeconds/(24*60*60);
        out[0] = d;

        int s = candidateSeconds - (d*24*60*60);

        int h = s/(60*60);
        out[1] = h;
        s -= h*60*60;
        
        int m = s/60;
        if( s % 60 >=30 )
            m++;
        out[2] = m;

        return out;        
    }
    
    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getReminderTypeId() {
        return reminderTypeId;
    }

    public void setReminderTypeId(int reminderTypeId) {
        this.reminderTypeId = reminderTypeId;
    }

    public int getRcCheckStatusTypeId() {
        return rcCheckStatusTypeId;
    }

    public void setRcCheckStatusTypeId(int rcCheckStatusTypeId) {
        this.rcCheckStatusTypeId = rcCheckStatusTypeId;
    }

    public long getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(long adminUserId) {
        this.adminUserId = adminUserId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCreditId() {
        return creditId;
    }

    public void setCreditId(long creditId) {
        this.creditId = creditId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public float getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(float overallScore) {
        this.overallScore = overallScore;
    }

    public int getCorpId() {
        return corpId;
    }

    public void setCorpId(int corpId) {
        this.corpId = corpId;
    }

    public String getExtRef() {
        return extRef;
    }

    public void setExtRef(String extRef) {
        this.extRef = extRef;
    }

    public int getRcScriptId() {
        return rcScriptId;
    }

    public void setRcScriptId(int rcScriptId) {
        this.rcScriptId = rcScriptId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getTextStr1() {
        return textStr1;
    }

    public void setTextStr1(String textStr1) {
        this.textStr1 = textStr1;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getRcCheckTypeId() {
        return rcCheckTypeId;
    }

    public void setRcCheckTypeId(int rcCheckTypeId) {
        this.rcCheckTypeId = rcCheckTypeId;
    }

    public int getDistributionTypeId() {
        return distributionTypeId;
    }

    public void setDistributionTypeId(int distributionTypeId) {
        this.distributionTypeId = distributionTypeId;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public List<RcRater> getRcRaterListNoSelf()
    {
        if( rcRaterList==null || rcRaterList.isEmpty() )
            return rcRaterList;
        List<RcRater> out = new ArrayList<>();
        for( RcRater r : rcRaterList )
        {
            if( r.getRcRaterType().getIsCandidateOrEmployee() )
                continue;
            out.add(r);
        }
        return out;
    }
    

    public int[] getRaterCounts()
    {
        int[] out = new int[2];
        if( rcRaterList==null || rcRaterList.isEmpty() )
            return out;
        for( RcRater r : rcRaterList )
        {
            if( r.getRcRaterType().getIsCandidateOrEmployee() )
                continue;
            if( !r.getRcRaterStatusType().getIsNotSent())
                out[0]++;
            if( r.getRcRaterStatusType().getIsComplete())
                out[1]++;
        }
        return out;        
    }
    
    public List<RcRater> getRcRaterList() {
        return rcRaterList;
    }

    public void setRcRaterList(List<RcRater> rcRaterList) {
        this.rcRaterList = rcRaterList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getCandidateCompleteDate() {
        return candidateCompleteDate;
    }

    public void setCandidateCompleteDate(Date candidateCompleteDate) {
        this.candidateCompleteDate = candidateCompleteDate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public RcScript getRcScript() {
        return rcScript;
    }

    public void setRcScript(RcScript rcScript) {
        this.rcScript = rcScript;
    }

    public User getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(User adminUser) {
        this.adminUser = adminUser;
    }

    public List<RcSuspiciousActivity> getRcSuspiciousActivityList() {
        return rcSuspiciousActivityList;
    }

    public void setRcSuspiciousActivityList(List<RcSuspiciousActivity> rcSuspiciousActivityList) {
        this.rcSuspiciousActivityList = rcSuspiciousActivityList;
    }

    public int getSuborgId() {
        return suborgId;
    }

    public void setSuborgId(int suborgId) {
        this.suborgId = suborgId;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public float getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(float percentComplete) {
        this.percentComplete = percentComplete;
    }


    public String getCandidateAccessCode() {
        return candidateAccessCode;
    }

    public void setCandidateAccessCode(String candidateAccessCode) {
        this.candidateAccessCode = candidateAccessCode;
    }

    public String getEmailResultsTo() {
        return emailResultsTo;
    }

    public void setEmailResultsTo(String emailResultsTo) {
        this.emailResultsTo = emailResultsTo;
    }

    public String getTextResultsTo() {
        return textResultsTo;
    }

    public void setTextResultsTo(String textResultsTo) {
        this.textResultsTo = textResultsTo;
    }

    public Date getCandidateStartDate() {
        return candidateStartDate;
    }

    public void setCandidateStartDate(Date candidateStartDate) {
        this.candidateStartDate = candidateStartDate;
    }

    public int getCandidateStarts() {
        return candidateStarts;
    }

    public void setCandidateStarts(int candidateStarts) {
        this.candidateStarts = candidateStarts;
    }

    public int getCandidateSeconds() {
        return candidateSeconds;
    }

    public void setCandidateSeconds(int candidateSeconds) {
        this.candidateSeconds = candidateSeconds;
    }

    public int getMinRaters() {
        return minRaters;
    }

    public void setMinRaters(int minRaters) {
        this.minRaters = minRaters;
    }

    public int getMaxRaters() {
        return maxRaters;
    }

    public void setMaxRaters(int maxRaters) {
        this.maxRaters = maxRaters;
    }

    public int getEnforceRaterLimits() {
        return enforceRaterLimits;
    }

    public void setEnforceRaterLimits(int enforceRaterLimits) {
        this.enforceRaterLimits = enforceRaterLimits;
    }

    public boolean getEnforceRaterLimitsB() {
        return enforceRaterLimits==1;
    }

    public void setEnforceRaterLimitsB(boolean b) {
        this.enforceRaterLimits = b ? 1 : 0;
    }

    public int getRcCandidateStatusTypeId() {
        return rcCandidateStatusTypeId;
    }

    public void setRcCandidateStatusTypeId(int candidateStatusTypeId) {
        this.rcCandidateStatusTypeId = candidateStatusTypeId;
    }

    public Date getCandidateReleaseDate() {
        return candidateReleaseDate;
    }

    public void setCandidateReleaseDate(Date candidateReleaseDate) {
        this.candidateReleaseDate = candidateReleaseDate;
    }

    public Date getCandidateLastUpdate() {
        return candidateLastUpdate;
    }

    public void setCandidateLastUpdate(Date candidateLastUpdate) {
        this.candidateLastUpdate = candidateLastUpdate;
    }


    public int getRcCheckScoringStatusTypeId() {
        return rcCheckScoringStatusTypeId;
    }

    public void setRcCheckScoringStatusTypeId(int rcCheckScoringStatusTypeId) {
        this.rcCheckScoringStatusTypeId = rcCheckScoringStatusTypeId;
    }

    public Date getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(Date scoreDate) {
        this.scoreDate = scoreDate;
    }

    public Date getLastCandidateReminderDate() {
        return lastCandidateReminderDate;
    }

    public void setLastCandidateReminderDate(Date lastCandidateReminderDate) {
        this.lastCandidateReminderDate = lastCandidateReminderDate;
    }

    public int getMinSupervisors() {
        return minSupervisors;
    }

    public void setMinSupervisors(int minSupervisors) {
        this.minSupervisors = minSupervisors;
    }

    public long getTestKeyId() {
        return testKeyId;
    }

    public void setTestKeyId(long testKeyId) {
        this.testKeyId = testKeyId;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public int getCollectCandidateRatings() {
        return collectCandidateRatings;
    }

    public void setCollectCandidateRatings(int collectCandidateRatings) {
        this.collectCandidateRatings = collectCandidateRatings;
    }

    public boolean getCollectCandidateRatingsB() {
        return collectCandidateRatings==1;
    }

    public void setCollectCandidateRatingsB( boolean b) {
        this.collectCandidateRatings = b ? 1 : 0;
    }

    public Date getLastCandidateProgressMsgDate() {
        return lastCandidateProgressMsgDate;
    }

    public void setLastCandidateProgressMsgDate(Date lastCandidateProgressMsgDate) {
        this.lastCandidateProgressMsgDate = lastCandidateProgressMsgDate;
    }

    public Date getLastProgressMsgDate() {
        return lastProgressMsgDate;
    }

    public void setLastProgressMsgDate(Date lastProgressMsgDate) {
        this.lastProgressMsgDate = lastProgressMsgDate;
    }

    public int getCandidateCannotAddRaters() {
        return candidateCannotAddRaters;
    }

    public void setCandidateCannotAddRaters(int candidateCannotAddRaters) {
        this.candidateCannotAddRaters = candidateCannotAddRaters;
    }

    public boolean getCandidateCanAddRatersB() {
        return candidateCannotAddRaters!=1;
    }

    public void setCandidateCanAddRatersB(boolean b) {
        this.candidateCannotAddRaters = b ? 0 : 1;
    }

    public String getCustomParameters() {
        return customParameters;
    }

    public void setCustomParameters(String customParameters) {
        this.customParameters = customParameters;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public int getReportId2() {
        return reportId2;
    }

    public void setReportId2(int reportId2) {
        this.reportId2 = reportId2;
    }


    public RcOrgPrefs getRcOrgPrefs() {
        return rcOrgPrefs;
    }

    public void setRcOrgPrefs(RcOrgPrefs rcOrgPrefs) {
        this.rcOrgPrefs = rcOrgPrefs;
    }

    public Date getFirstCandidateSendDate() {
        return firstCandidateSendDate;
    }

    public void setFirstCandidateSendDate(Date firstCandidateSendDate) {
        this.firstCandidateSendDate = firstCandidateSendDate;
    }

    public Date getFirstCandidateReferenceDate() {
        return firstCandidateReferenceDate;
    }

    public void setFirstCandidateReferenceDate(Date firstCandidateReferenceDate) {
        this.firstCandidateReferenceDate = firstCandidateReferenceDate;
    }

    public Date getLastCandidateReferenceDate() {
        return lastCandidateReferenceDate;
    }

    public void setLastCandidateReferenceDate(Date lastCandidateReferenceDate) {
        this.lastCandidateReferenceDate = lastCandidateReferenceDate;
    }

    public Date getLastCandidateSendDate() {
        return lastCandidateSendDate;
    }

    public void setLastCandidateSendDate(Date lastCandidateSendDate) {
        this.lastCandidateSendDate = lastCandidateSendDate;
    }

    public int getCreditIndex() {
        return creditIndex;
    }

    public void setCreditIndex(int creditIndex) {
        this.creditIndex = creditIndex;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    public List<RcReferral> getRcReferralList() {
        return rcReferralList;
    }

    public void setRcReferralList(List<RcReferral> rcReferralList) {
        this.rcReferralList = rcReferralList;
    }
    
    
}
