package com.tm2batch.entity.ref;


import java.io.Serializable;
import java.util.Date;
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
@Table( name = "rcorgprefs" )
@NamedQueries({
        @NamedQuery( name = "RcOrgPrefs.findByOrgId", query = "SELECT o FROM RcOrgPrefs AS o WHERE o.orgId=:orgId" )    
})
public class RcOrgPrefs implements Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="rcorgprefsid")
    private int rcOrgPrefsId;

    @Column(name="orgid")
    private int orgId;

    @Column(name="corpid")
    private int corpId;

    @Column(name="reportidprehire")
    private int reportIdPrehire;

    @Column(name="reportidemployee")
    private int reportIdEmployee;

    @Column(name="reportidemployeefbk")
    private int reportIdEmployeeFbk;


    
    @Column(name="remindertypeid")
    private int reminderTypeId;

    @Column(name="showjobtitle")
    private int showJobTitle = 1;

    @Column( name = "showcompanyname" )
    private int showCompanyName = 1;
    
    @Column(name="distributiontypeid")
    private int distributionTypeId;
    
    @Column(name="defaultrcchecktypeid")
    private int defaultRcCheckTypeId;
    
    @Column(name="followupok")
    private int followupOk=1;

    @Column(name="minsupervisors")
    private int minSupervisors = 1;

    @Column(name="candidatecannotaddraters")
    private int candidateCannotAddRaters;

    
    @Column(name="minraters")
    private int minRaters = 2;

    @Column(name="maxraters")
    private int maxRaters = 0;
    
    @Column(name="ratergraceperiod")
    private int raterGracePeriod=10;

    @Column(name="enforceraterlimits")
    private int enforceRaterLimits;

    @Column(name="collectcandidateratings")
    private int collectCandidateRatings = 1;

    
    @Column(name="invitationsubj")
    private String invitationSubj;

    @Column(name="invitationsubjrater")
    private String invitationSubjRater;

    @Column(name="invitation")
    private String invitation;

    @Column(name="invitationrater")
    private String invitationRater;

    @Column(name="otherrolename")
    private String otherRoleName;

    @Column(name="otherrolename2")
    private String otherRoleName2;

    @Column(name="otherrolename3")
    private String otherRoleName3;

    
    
    
     /**
     * 0=no
     * 1=audio
     * 2=video
     */
    @Column(name="audiovideook")
    private int audioVideoOk=0;
   
    @Column(name="defaultrcscriptid")
    private int defaultRcScriptId=0;
   
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastupdate")
    private Date lastUpdate;
   
    
    public String[] getOtherRoleTypeNames()
    {
        return new String[] { this.otherRoleName==null || otherRoleName.isBlank() ? null : otherRoleName, 
                              this.otherRoleName2==null || otherRoleName2.isBlank() ? null : otherRoleName2,
                              this.otherRoleName3==null || otherRoleName3.isBlank() ? null : otherRoleName3 };
    }
    
    @Override
    public String toString() {
        return "RcOrgPrefs{" + "rcOrgPrefsId=" + rcOrgPrefsId + ", orgId=" + orgId + '}';
    }

    public int getRcOrgPrefsId() {
        return rcOrgPrefsId;
    }

    public void setRcOrgPrefsId(int rcOrgPrefsId) {
        this.rcOrgPrefsId = rcOrgPrefsId;
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

    public int getShowJobTitle() {
        return showJobTitle;
    }

    public void setShowJobTitle(int showJobTitle) {
        this.showJobTitle = showJobTitle;
    }

    public boolean getShowJobTitleB() {
        return showJobTitle==1;
    }

    public void setShowJobTitleB(boolean b) {
        this.showJobTitle = b ? 1 : 0;
    }

    public int getShowCompanyName() {
        return showCompanyName;
    }

    public void setShowCompanyName(int showCompanyName) {
        this.showCompanyName = showCompanyName;
    }

    public boolean getShowCompanyNameB() {
        return showCompanyName==1;
    }

    public void setShowCompanyNameB(boolean b) {
        this.showCompanyName = b ? 1 : 0;
    }

    public int getCorpId() {
        return corpId;
    }

    public void setCorpId(int corpId) {
        this.corpId = corpId;
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

    public int getAudioVideoOk() {
        return audioVideoOk;
    }

    public void setAudioVideoOk(int audioVideoOk) {
        this.audioVideoOk = audioVideoOk;
    }

    public int getDistributionTypeId() {
        return distributionTypeId;
    }

    public void setDistributionTypeId(int distributionTypeId) {
        this.distributionTypeId = distributionTypeId;
    }

    public int getDefaultRcScriptId() {
        return defaultRcScriptId;
    }

    public void setDefaultRcScriptId(int defaultRcScriptId) {
        this.defaultRcScriptId = defaultRcScriptId;
    }

    public int getDefaultRcCheckTypeId() {
        return defaultRcCheckTypeId;
    }

    public void setDefaultRcCheckTypeId(int defaultRcCheckTypeId) {
        this.defaultRcCheckTypeId = defaultRcCheckTypeId;
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

    public int getFollowupOk() {
        return followupOk;
    }

    public void setFollowupOk(int followupOk) {
        this.followupOk = followupOk;
    }

    public int getMinSupervisors() {
        return minSupervisors;
    }

    public void setMinSupervisors(int minSupervisors) {
        this.minSupervisors = minSupervisors;
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

    public int getCandidateCannotAddRaters() {
        return candidateCannotAddRaters;
    }

    public void setCandidateCannotAddRaters(int candidateCannotAddRaters) {
        this.candidateCannotAddRaters = candidateCannotAddRaters;
    }
    
    public boolean getCandidateCanAddRatersB() {
        return candidateCannotAddRaters<=0;
    }

    public void setCandidateCanAddRatersB(boolean b) {
        this.candidateCannotAddRaters = b ? 0 : 1;
    }

    public int getReportIdPrehire() {
        return reportIdPrehire;
    }

    public void setReportIdPrehire(int reportIdPrehire) {
        this.reportIdPrehire = reportIdPrehire;
    }

    public int getReportIdEmployee() {
        return reportIdEmployee;
    }

    public void setReportIdEmployee(int reportIdEmployee) {
        this.reportIdEmployee = reportIdEmployee;
    }

    public int getReportIdEmployeeFbk() {
        return reportIdEmployeeFbk;
    }

    public void setReportIdEmployeeFbk(int reportIdEmployeeFbk) {
        this.reportIdEmployeeFbk = reportIdEmployeeFbk;
    }

    public String getInvitationSubj() {
        return invitationSubj;
    }

    public void setInvitationSubj(String invitationSubj) {
        this.invitationSubj = invitationSubj;
    }

    public String getInvitationSubjRater() {
        return invitationSubjRater;
    }

    public void setInvitationSubjRater(String invitationSubjRater) {
        this.invitationSubjRater = invitationSubjRater;
    }

    public String getInvitation() {
        return invitation;
    }

    public void setInvitation(String invitation) {
        this.invitation = invitation;
    }

    public String getInvitationRater() {
        return invitationRater;
    }

    public void setInvitationRater(String invitationRater) {
        this.invitationRater = invitationRater;
    }

    public String getOtherRoleName() {
        return otherRoleName;
    }

    public void setOtherRoleName(String otherRoleName) {
        this.otherRoleName = otherRoleName;
    }

    public String getOtherRoleName2() {
        return otherRoleName2;
    }

    public void setOtherRoleName2(String otherRoleName2) {
        this.otherRoleName2 = otherRoleName2;
    }

    public String getOtherRoleName3() {
        return otherRoleName3;
    }

    public void setOtherRoleName3(String otherRoleName3) {
        this.otherRoleName3 = otherRoleName3;
    }

    public int getRaterGracePeriod() {
        return raterGracePeriod;
    }

    public void setRaterGracePeriod(int raterGracePeriod) {
        this.raterGracePeriod = raterGracePeriod;
    }


    
}
