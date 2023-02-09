package com.tm2batch.entity.ref;


import com.tm2batch.entity.user.User;
import com.tm2batch.entity.user.UserAction;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.ref.RcContactMethodType;
import com.tm2batch.ref.RcContactPermissionType;
import com.tm2batch.ref.RcRaterRoleType;
import com.tm2batch.ref.RcRaterSourceType;
import com.tm2batch.ref.RcRaterStatusType;
import com.tm2batch.ref.RcRaterType;
import com.tm2batch.service.EncryptUtils;
import com.tm2batch.service.LogService;
import com.tm2batch.util.StringUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
@Table( name = "rcrater" )
@NamedQueries({
        @NamedQuery( name = "RcRater.findByRcCheckId", query = "SELECT o FROM RcRater AS o WHERE o.rcCheckId=:rcCheckId" )   
})
public class RcRater implements Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="rcraterid")
    private long rcRaterId;

    @Column(name="rccheckid")
    private long rcCheckId;

    @Column(name="orgid")
    private int orgId;
    
    @Column(name="userid")
    private long userId;

    @Column(name="sourceuserid")
    private long sourceUserId;

    @Column(name="rcratertypeid")
    private int rcRaterTypeId;

    @Column(name="rcraterstatustypeid")
    private int rcRaterStatusTypeId;

    @Column(name="rcraterroletypeid")
    private int rcRaterRoleTypeId;
    
    @Column(name="candidateroleresp")
    private String candidateRoleResp;
    
    @Column(name="companyname")
    private String companyName;
    
    
    
    @Column(name="rateraccesscode")
    private String raterAccessCode;
    
    
    @Column(name="percentcomplete")
    private float percentComplete;

    
    @Column(name="overallscore")
    private float overallScore;

    @Column(name="contactpermissiontypeid")
    private int contactPermissionTypeId;

    @Column(name="recruitingpermissiontypeid")
    private int recruitingPermissionTypeId;
    
    @Column(name="contactmethodtypeid")
    private int contactMethodTypeId;
    
    @Column(name="raterstarts")
    private int raterStarts;

    @Column(name="raterseconds")
    private int raterSeconds;

    

    @Column(name="ipaddress")
    private String ipAddress;

    @Column(name="useragent")
    private String userAgent;

    @Column(name="note")
    private String note;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="senddate")
    private Date sendDate;
        
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="startdate")
    private Date startDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="releasedate")
    private Date releaseDate;
    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastupdate")
    private Date lastUpdate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="completedate")
    private Date completeDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastprogressmsgdate")
    private Date lastProgressMsgDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastreminderdate")
    private Date lastReminderDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="observationstartdate")
    private Date observationStartDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="observationenddate")
    private Date observationEndDate;

    @Transient
    private RcOrgPrefs rcOrgPrefs;
    
    
    @Transient
    private List<RcRating> rcRatingList;
    
    @Transient
    private User user;
    
    @Transient
    private User sourceUser;
    
    @Transient
    private List<UserAction> userActionList;
    
    @Transient
    private Locale locale;
    
    
    @Transient
    private RcRaterSourceType rcRaterSourceType;
    
    @Override
    public String toString() {
        return "RcRater{" + "rcRaterId=" + rcRaterId + ", rcCheckId=" + rcCheckId + ", userId=" + userId + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.rcRaterId ^ (this.rcRaterId >>> 32));
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
        final RcRater other = (RcRater) obj;
        if (this.rcRaterId != other.rcRaterId) {
            return false;
        }
        return true;
    }
    
    
    
    public String getRaterStartUrl()
    {
        return RuntimeConstants.getStringValue( "RefCheckRaterBaseUrl" ) + raterAccessCode;
    }
    
    public String getRcRaterIdEncrypted()
    {
        try
        {
            return EncryptUtils.urlSafeEncrypt( rcRaterId );
        }
        catch( Exception e )
        {
            LogService.logIt(e, "RcRater.getRcRaterIdEncrypted() " + toString()  );
            return "";
        }
    }
       
    
    public boolean getIsCandidateOrEmployee()
    {
        return getRcRaterType().getIsCandidateOrEmployee();
    }

    
    public int getQuestionsComplete()
    {
        if( rcRatingList==null )
            return 0;

        int c = 0;
        for( RcRating r : rcRatingList )
        {
            if( r.getRcRatingStatusType().getIsComplete() )
                c++;
        }
        return c;
    }

    public boolean getHasAnyRatingData()
    {
        if( rcRatingList==null )
            return false;

        for( RcRating r : rcRatingList )
        {
            if( r.getRcRatingStatusType().getIsComplete() )
                return true;
        }
        return false;
    }

    
    
    public RcRating getRcRating( int rcItemId )
    {
        if( rcItemId<=0 || this.rcRatingList==null || this.rcRatingList.isEmpty() )
            return null;
        
        for( RcRating r : rcRatingList )
        {
            if( r.getRcItemId()==rcItemId )
                return r;
        }
        return null;
    }
    
    
    public int[] getRaterDaysHrsMins()
    {
        int[] out = new int[3];
        if( raterSeconds<=0 )
            return out;
        if( raterSeconds<60 )
        {
            out[2]=1;
            return out;
        }
        int d = raterSeconds/(24*60*60);
        out[0] = d;

        int s = raterSeconds - (d*24*60*60);

        int h = s/(60*60);
        out[1] = h;
        s -= h*60*60;
        
        int m = s/60;
        if( s % 60 >=30 )
            m++;
        out[2] = m;

        return out;        
    }
    
    public boolean getHasNote()
    {
        return note!=null && !note.isBlank();
    }
    
    public RcRaterType getRcRaterType()
    {
        return RcRaterType.getValue( this.rcRaterTypeId );
    }

    public RcRaterRoleType getRcRaterRoleType()
    {
        return RcRaterRoleType.getValue( this.rcRaterRoleTypeId );
    }

    public RcRaterSourceType getRcRaterSourceType() {
        return rcRaterSourceType;
    }

    public void setRcRaterSourceType(RcRaterSourceType rcRaterSourceType) {
        this.rcRaterSourceType = rcRaterSourceType;
    }
    
    public String getRcRaterRoleTypeName()
    {
        return getRcRaterRoleType().getName(locale, rcOrgPrefs==null ? null : rcOrgPrefs.getOtherRoleTypeNames() );
    }
    
    public String getRcRaterStatusTypeName()
    {
        return getRcRaterStatusType().getName(locale);
    }
    
    public RcRaterStatusType getRcRaterStatusType()
    {
        return RcRaterStatusType.getValue( rcRaterStatusTypeId );
    }

    public RcContactPermissionType getRcContactPermissionType()
    {
        return RcContactPermissionType.getValue( contactPermissionTypeId );
    }

    public RcContactMethodType getRcContactMethodType()
    {
        return RcContactMethodType.getValue( contactMethodTypeId );
    }

    
    public long getRcRaterId() {
        return rcRaterId;
    }

    public void setRcRaterId(long rcRaterId) {
        this.rcRaterId = rcRaterId;
    }

    public long getRcCheckId() {
        return rcCheckId;
    }

    public void setRcCheckId(long rcCheckId) {
        this.rcCheckId = rcCheckId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getRcRaterStatusTypeId() {
        return rcRaterStatusTypeId;
    }

    public void setRcRaterStatusTypeId(int rcRaterStatusTypeId) {
        this.rcRaterStatusTypeId = rcRaterStatusTypeId;
    }

    public float getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(float overallScore) {
        this.overallScore = overallScore;
    }

    public int getContactMethodTypeId() {
        return contactMethodTypeId;
    }

    public void setContactMethodTypeId(int contactMethodTypeId) {
        this.contactMethodTypeId = contactMethodTypeId;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getContactPermissionTypeId() {
        return contactPermissionTypeId;
    }

    public void setContactPermissionTypeId(int contactPermissionTypeId) {
        this.contactPermissionTypeId = contactPermissionTypeId;
    }

    public int getPercentCompleteRounded()
    {
        if( percentComplete<=0 )
            return 0;
        if( percentComplete>=100 )
            return 100;
        
        return Math.round( percentComplete );        
    }
    public float getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(float percentComplete) {
        this.percentComplete = percentComplete;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public List<RcRating> getRcRatingList() {
        return rcRatingList;
    }

    public void setRcRatingList(List<RcRating> rcRatingList) {
        this.rcRatingList = rcRatingList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserAction> getUserActionList() {
        return userActionList;
    }

    public void setUserActionList(List<UserAction> userActionList) {
        this.userActionList = userActionList;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getRaterAccessCode() {
        return raterAccessCode;
    }

    public void setRaterAccessCode(String raterAccessCode) {
        this.raterAccessCode = raterAccessCode;
    }

    public int getRaterStarts() {
        return raterStarts;
    }

    public void setRaterStarts(int raterStarts) {
        this.raterStarts = raterStarts;
    }

    public int getRaterSeconds() {
        return raterSeconds;
    }

    public void setRaterSeconds(int raterSeconds) {
        this.raterSeconds = raterSeconds;
    }

    public long getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(long sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getRcRaterRoleTypeId() {
        return rcRaterRoleTypeId;
    }

    public void setRcRaterRoleTypeId(int rcRaterRoleTypeId) {
        this.rcRaterRoleTypeId = rcRaterRoleTypeId;
    }


    public String getCandidateRoleResp() {
        return candidateRoleResp;
    }

    public void setCandidateRoleResp(String candidateRoleResp) {
        this.candidateRoleResp = candidateRoleResp;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getLastProgressMsgDate() {
        return lastProgressMsgDate;
    }

    public void setLastProgressMsgDate(Date lastProgressMsgDate) {
        this.lastProgressMsgDate = lastProgressMsgDate;
    }

    public Date getLastReminderDate() {
        return lastReminderDate;
    }

    public void setLastReminderDate(Date lastReminderDate) {
        this.lastReminderDate = lastReminderDate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getObservationStartDate() {
        return observationStartDate;
    }

    public void setObservationStartDate(Date observationStartDate) {
        this.observationStartDate = observationStartDate;
    }

    public Date getObservationEndDate() {
        return observationEndDate;
    }

    public void setObservationEndDate(Date observationEndDate) {
        this.observationEndDate = observationEndDate;
    }

    public int getRecruitingPermissionTypeId() {
        return recruitingPermissionTypeId;
    }

    public void setRecruitingPermissionTypeId(int recruitingPermissionTypeId) {
        this.recruitingPermissionTypeId = recruitingPermissionTypeId;
    }

    public int getRcRaterTypeId() {
        return rcRaterTypeId;
    }

    public void setRcRaterTypeId(int rcRaterTypeId) {
        this.rcRaterTypeId = rcRaterTypeId;
    }

    public RcOrgPrefs getRcOrgPrefs() {
        return rcOrgPrefs;
    }

    public void setRcOrgPrefs(RcOrgPrefs rcOrgPrefs) {
        this.rcOrgPrefs = rcOrgPrefs;
    }

    public User getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(User sourceUser) {
        this.sourceUser = sourceUser;
    }

    
}
