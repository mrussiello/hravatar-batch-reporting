package com.tm2batch.entity.lvi;


import com.tm2batch.lvi.LiveVideoCriteriaScoreType;
import com.tm2batch.util.MessageFactory;
import java.io.Serializable;
import java.util.Date;
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
@Table( name = "lvorgprefs" )
@NamedQueries({
        @NamedQuery( name = "LvOrgPrefs.findByOrgId", query = "SELECT o FROM LvOrgPrefs AS o WHERE o.orgId=:orgId" )    
})
public class LvOrgPrefs implements Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="lvorgprefsid")
    private int lvOrgPrefsId;

    @Column(name="orgid")
    private int orgId;

    @Column(name="maxusers")
    private int maxUsers = 0;

    @Column( name = "defaultcorpid" )
    private int defaultCorpId = 0;
    
    @Column(name="remindertypeid")
    private int reminderTypeId = 1;

    @Column(name="notifyonchanges")
    private int notifyOnChanges = 1;

    @Column(name="defaultlvscriptid")
    private int defaultLvScriptId = 0;
    
    @Column(name="defaultlowbandwidthtypeid")
    private int defaultLowBandwidthTypeId = 0;
    
    @Column(name="defaultdurationmins")
    private int defaultDurationMins = 15;

    @Column(name="doublebookingok")
    private int doubleBookingOk = 1;
    
    @Column(name="hiderelease")
    private int hideRelease = 0;
    
    @Column(name="releasehtml")
    private String releaseHtml;
    
    @Column(name="availabilityschedulingon")
    private int availabilitySchedulingOn = 0;
    
    @Column(name="unacceptedexpiredays")
    private int unacceptedExpireDays = 2;

    @Column(name="criterianame1")
    private String criteriaName1;

    @Column(name="criterianame2")
    private String criteriaName2;

    @Column(name="criterianame3")
    private String criteriaName3;

    @Column(name="criteriascoretypeid1")
    private int criteriaScoreTypeId1;

    @Column(name="criteriascoretypeid2")
    private int criteriaScoreTypeId2;

    @Column(name="criteriascoretypeid3")
    private int criteriaScoreTypeId3;

    @Column(name="includetranscript")
    private int includeTranscript = 0;
    
    @Column(name="includevoiceanalysis")
    private int includeVoiceAnalysis = 0;
    
    @Column(name="includepersonality")
    private int includePersonality = 0;
    
    @Column(name="allowscreenshare")
    private int allowScreenShare=1;
    
    @Column(name="allowfileupload")
    private int allowFileUpload=1;

    /**
     * 0=none
     * 1=record suspicious activity
     */
    @Column(name="proctortypeid")
    private int proctorTypeId = 0;
    

    @Column(name="recipientauthenticationtypeid")
    private int recipientAuthenticationTypeId;

    @Column(name="userauthenticationtypeid")
    private int userAuthenticationTypeId;
   
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="accessexpiredate")
    private Date accessExpireDate;
        
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastupdate")
    private Date lastUpdate;
    
    @Transient
    private int currentUserCount = 0;
    
    
    
    @Override
    public String toString() {
        return "LvOrgPrefs{" + "lvOrgPrefsId=" + lvOrgPrefsId + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.lvOrgPrefsId ^ (this.lvOrgPrefsId >>> 32));
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
        final LvOrgPrefs other = (LvOrgPrefs) obj;
        if (this.lvOrgPrefsId != other.lvOrgPrefsId) {
            return false;
        }
        return true;
    }
    
    public String getAllowScreenShareName()
    {
        return MessageFactory.getStringMessage(Locale.US, this.allowScreenShare==0 ? "g.No" : "g.Yes" );
    }

    public String getAllowFileUploadName()
    {
        return MessageFactory.getStringMessage(Locale.US, this.allowFileUpload==0 ? "g.No" : "g.Yes" );
    }
    
    public String getIncludePersonalityName()
    {
        return MessageFactory.getStringMessage(Locale.US, this.includePersonality==0 ? "g.No" : "g.Yes" );
    }
    
    public String getIncludeTranscriptName()
    {
        return MessageFactory.getStringMessage(Locale.US, this.includeTranscript==0 ? "g.No" : "g.Yes" );
    }
    
    
    public String getDefaultLowBandwidthTypeName()
    {
        return MessageFactory.getStringMessage(Locale.US, this.defaultLowBandwidthTypeId==0 ? "g.No" : "g.Yes" );        
    }
    
    public String getIncludeVoiceAnalysisName()
    {
        return MessageFactory.getStringMessage(Locale.US, this.includeVoiceAnalysis==0 ? "g.No" : "g.Yes" );
    }

    public String getDoubleBookingOkName()
    {
        return MessageFactory.getStringMessage(Locale.US, this.doubleBookingOk==0 ? "g.No" : "g.Yes" );
    }

    public String getAvailabilitySchedulingOnName()
    {
        return MessageFactory.getStringMessage(Locale.US, this.availabilitySchedulingOn==0 ? "g.No" : "g.Yes" );
    }
    
    
    public LiveVideoCriteriaScoreType getCriteriaScoreType1()
    {
        return LiveVideoCriteriaScoreType.getValue( this.criteriaScoreTypeId1 );
    }
    public LiveVideoCriteriaScoreType getCriteriaScoreType2()
    {
        return LiveVideoCriteriaScoreType.getValue( this.criteriaScoreTypeId2 );
    }
    public LiveVideoCriteriaScoreType getCriteriaScoreType3()
    {
        return LiveVideoCriteriaScoreType.getValue( this.criteriaScoreTypeId3 );
    }

    public String getNotifyOnChangesName()
    {
        return MessageFactory.getStringMessage( Locale.US, this.notifyOnChanges==0 ? "g.No" : "g.Yes" );
    }
            

    
    
    public boolean getHasCriteria1()
    {
        return this.criteriaName1!=null && !this.criteriaName1.isBlank();
    }

    public boolean getHasCriteria2()
    {
        return this.criteriaName2!=null && !this.criteriaName2.isBlank();
    }

    public boolean getHasCriteria3()
    {
        return this.criteriaName3!=null && !this.criteriaName3.isBlank();
    }
    
    
    public int getLvOrgPrefsId() {
        return lvOrgPrefsId;
    }

    public void setLvOrgPrefsId(int lvOrgPrefsId) {
        this.lvOrgPrefsId = lvOrgPrefsId;
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

    public int getDefaultDurationMins() {
        return defaultDurationMins;
    }

    public void setDefaultDurationMins(int defaultDurationMins) {
        this.defaultDurationMins = defaultDurationMins;
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

    public String getCriteriaName1() {
        return criteriaName1;
    }

    public void setCriteriaName1(String criteriaName1) {
        this.criteriaName1 = criteriaName1;
    }

    public String getCriteriaName2() {
        return criteriaName2;
    }

    public void setCriteriaName2(String criteriaName2) {
        this.criteriaName2 = criteriaName2;
    }

    public String getCriteriaName3() {
        return criteriaName3;
    }

    public void setCriteriaName3(String criteriaName3) {
        this.criteriaName3 = criteriaName3;
    }

    public int getCriteriaScoreTypeId1() {
        return criteriaScoreTypeId1;
    }

    public void setCriteriaScoreTypeId1(int criteriaScoreTypeId1) {
        this.criteriaScoreTypeId1 = criteriaScoreTypeId1;
    }

    public int getCriteriaScoreTypeId2() {
        return criteriaScoreTypeId2;
    }

    public void setCriteriaScoreTypeId2(int criteriaScoreTypeId2) {
        this.criteriaScoreTypeId2 = criteriaScoreTypeId2;
    }

    public int getCriteriaScoreTypeId3() {
        return criteriaScoreTypeId3;
    }

    public void setCriteriaScoreTypeId3(int criteriaScoreTypeId3) {
        this.criteriaScoreTypeId3 = criteriaScoreTypeId3;
    }

    public int getUnacceptedExpireDays() {
        return unacceptedExpireDays;
    }

    public void setUnacceptedExpireDays(int unacceptedExpireDays) {
        this.unacceptedExpireDays = unacceptedExpireDays;
    }

    public Date getAccessExpireDate() {
        return accessExpireDate;
    }

    public void setAccessExpireDate(Date accessExpireDate) {
        this.accessExpireDate = accessExpireDate;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public int getIncludeTranscript() {
        return includeTranscript;
    }

    public void setIncludeTranscript(int includeTranscript) {
        this.includeTranscript = includeTranscript;
    }

    public int getIncludeVoiceAnalysis() {
        return includeVoiceAnalysis;
    }

    public void setIncludeVoiceAnalysis(int includeVoiceAnalysis) {
        this.includeVoiceAnalysis = includeVoiceAnalysis;
    }

    public int getIncludePersonality() {
        return includePersonality;
    }

    public void setIncludePersonality(int includePersonality) {
        this.includePersonality = includePersonality;
    }

    public boolean getIncludeTranscriptBoolean() {
        return includeTranscript==1;
    }

    public void setIncludeTranscriptBoolean(boolean b) {
        this.includeTranscript = b ? 1 : 0;
    }

    public boolean getIncludeVoiceAnalysisBoolean() {
        return includeVoiceAnalysis==1;
    }

    public void setIncludeVoiceAnalysisBoolean(boolean b) {
        this.includeVoiceAnalysis =  b ? 1 : 0;
    }

    public boolean getIncludePersonalityBoolean() {
        return includePersonality==1;
    }

    public void setIncludePersonalityBoolean(boolean b) {
        this.includePersonality =  b ? 1 : 0;
    }

    public int getCurrentUserCount() {
        return currentUserCount;
    }

    public void setCurrentUserCount(int currentUserCount) {
        this.currentUserCount = currentUserCount;
    }

    public int getRecipientAuthenticationTypeId() {
        return recipientAuthenticationTypeId;
    }

    public void setRecipientAuthenticationTypeId(int recipientAuthenticationTypeId) {
        this.recipientAuthenticationTypeId = recipientAuthenticationTypeId;
    }

    public int getUserAuthenticationTypeId() {
        return userAuthenticationTypeId;
    }

    public void setUserAuthenticationTypeId(int userAuthenticationTypeId) {
        this.userAuthenticationTypeId = userAuthenticationTypeId;
    }

    public int getDefaultCorpId() {
        return defaultCorpId;
    }

    public void setDefaultCorpId(int defaultCorpId) {
        this.defaultCorpId = defaultCorpId;
    }

    public int getDefaultLvScriptId() {
        return defaultLvScriptId;
    }

    public void setDefaultLvScriptId(int defaultLvScriptid) {
        this.defaultLvScriptId = defaultLvScriptid;
    }

    public int getNotifyOnChanges() {
        return notifyOnChanges;
    }

    public void setNotifyOnChanges(int notifyOnChanges) {
        this.notifyOnChanges = notifyOnChanges;
    }

    public boolean getNotifyOnChangesBoolean() {
        return notifyOnChanges==1;
    }

    public void setNotifyOnChangesBoolean(boolean b) {
        this.notifyOnChanges = b ? 1 : 0;
    }

    public int getDoubleBookingOk() {
        return doubleBookingOk;
    }

    public void setDoubleBookingOk(int doubleBookingOk) {
        this.doubleBookingOk = doubleBookingOk;
    }

    public int getHideRelease() {
        return hideRelease;
    }

    public void setHideRelease(int hideRelease) {
        this.hideRelease = hideRelease;
    }

    public boolean getHideReleaseBoolean() {
        return hideRelease==1;
    }

    public void setHideReleaseBoolean(boolean b ) {
        this.hideRelease = b ? 1 : 0;
    }
    
    
    public String getReleaseHtml() {
        return releaseHtml;
    }

    public void setReleaseHtml(String releaseHtml) {
        this.releaseHtml = releaseHtml;
    }

    public int getAvailabilitySchedulingOn() {
        return availabilitySchedulingOn;
    }

    public void setAvailabilitySchedulingOn(int availabilitySchedulingOn) {
        this.availabilitySchedulingOn = availabilitySchedulingOn;
    }

    public boolean getAvailabilitySchedulingOnBoolean() {
        return availabilitySchedulingOn==1;
    }

    public void setAvailabilitySchedulingOnBoolean(boolean b) {
        this.availabilitySchedulingOn = b ? 1 : 0;
    }
    
    public boolean getDoubleBookingOkBoolean() {
        return doubleBookingOk==1;
    }

    public void setDoubleBookingOkBoolean(boolean b) {
        this.doubleBookingOk = b ? 1 : 0;
    }

    public int getAllowScreenShare() {
        return allowScreenShare;
    }

    public void setAllowScreenShare(int allowScreenShare) {
        this.allowScreenShare = allowScreenShare;
    }

    public int getAllowFileUpload() {
        return allowFileUpload;
    }

    public void setAllowFileUpload(int allowFileUpload) {
        this.allowFileUpload = allowFileUpload;
    }

    public boolean getAllowScreenShareBoolean() {
        return allowScreenShare==1;
    }

    public void setAllowScreenShareBoolean(boolean b) {
        this.allowScreenShare = b ? 1 : 0;
    }

    public boolean getAllowFileUploadBoolean() {
        return allowFileUpload==1;
    }

    public void setAllowFileUploadBoolean(boolean b) {
        this.allowFileUpload = b ? 1 : 0;
    }

    public int getDefaultLowBandwidthTypeId() {
        return defaultLowBandwidthTypeId;
    }

    public void setDefaultLowBandwidthTypeId(int defaultLowBandwidthTypeId) {
        this.defaultLowBandwidthTypeId = defaultLowBandwidthTypeId;
    }

    public boolean getDefaultLowBandwidthTypeIdBoolean() {
        return defaultLowBandwidthTypeId==1;
    }

    public void setDefaultLowBandwidthTypeIdBoolean(boolean b) {
        this.defaultLowBandwidthTypeId = b ? 1 : 0;
    }

    public int getProctorTypeId() {
        return proctorTypeId;
    }

    public void setProctorTypeId(int proctorTypeId) {
        this.proctorTypeId = proctorTypeId;
    }

    public boolean getProctorTypeIdBoolean() {
        return proctorTypeId==1;
    }

    public void setProctorTypeIdBoolean(boolean b) {
        this.proctorTypeId = b ? 1 : 0;
    }        
}
