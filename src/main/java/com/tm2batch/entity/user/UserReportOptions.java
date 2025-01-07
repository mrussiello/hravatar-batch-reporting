package com.tm2batch.entity.user;

import com.tm2batch.autoreport.GeneralReportOptions;
import java.util.Date;
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
@Table( name="userreportoptions" )
@NamedQueries({
    @NamedQuery ( name="UserReportOptions.findByUserId", query="SELECT o FROM UserReportOptions AS o WHERE o.userId = :userId" )
})
public class UserReportOptions implements GeneralReportOptions
{
    @Transient
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="userreportoptionsid")
    private long userReportOptionsId;

    @Column(name="userid")
    private long userId;

    @Column(name="battery")
    private int battery=1;

    @Column(name="multiuselink")
    private int multiUseLink=1;

    @Column(name="accountpercentile")
    private int accountPercentile=1;

    @Column(name="altscores")
    private int altScores=1;

    @Column(name="competencies")
    private int competencies=1;

    @Column(name="completed")
    private int completed=1;

    @Column(name="countrypercentile")
    private int countryPercentile=1;

    @Column(name="custom1")
    private int custom1=1;

    @Column(name="custom2")
    private int custom2=1;

    @Column(name="custom3")
    private int custom3=1;

    @Column(name="department")
    private int department=1;

    @Column(name="detailviewurl")
    private int detailViewUrl=1;

    @Column(name="email")
    private int email=1;

    @Column(name="identifier")
    private int identifier=1;

    @Column(name="includecandfbkreptsinpdfdwnld")
    private int includeCandFbkReptsInPdfDwnld=1;

    @Column(name="includefieldsforperfupload")
    private int includeFieldsForPerfUpload=1;

    @Column(name="itemresponses")
    private int itemResponses=1;

    @Column(name="mobile")
    private int mobile=1;

    @Column(name="name")
    private int name=1;

    @Column(name="overallpercentile")
    private int overallPercentile=1;

    @Column(name="overallscore")
    private int overallScore=1;

    @Column(name="performancevalues")
    private int performanceValues=1;

    @Column(name="responseratings")
    private int responseRatings=0;

    @Column(name="sendcandidatefbkreports")
    private int sendCandidateFbkReports=0;

    @Column(name="started")
    private int started=1;

    @Column(name="testadminuser")
    private int testAdminUser=1;

    @Column(name="testcreditsused")
    private int testCreditsUsed=0;

    @Column(name="testeventtype")
    private int testEventType=1;

    @Column(name="testname")
    private int testName=1;

    @Column(name="testtakeridentifier")
    private int testTakerIdentifier=1;

    @Column(name="usernotedates")
    private int userNoteDates=1;

    @Column(name="usernotes")
    private int userNotes=1;

    @Column(name="userstatus")
    private int userStatus=1;

    @Column(name="userdemo")
    private int userDemo;
    
    @Column(name="totalseconds")
    private int totalSeconds=0;
            
    @Column(name="avgrespratings")
    private int avgRespRatings=1;
        
    @Column(name="suspiciousactivity")
    private int suspiciousActivity=0;
        
    @Column(name="plagiarism")
    private int plagiarism=0;
        
    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastupdate")
    private Date lastUpdate;

    @Override
    public String toString() {
        return "UserReportOptions{" + "userId=" + userId + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.userId ^ (this.userId >>> 32));
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
        final UserReportOptions other = (UserReportOptions) obj;
        if (this.userId != other.userId) {
            return false;
        }
        return true;
    }

    
    
    public long getUserReportOptionsId() {
        return userReportOptionsId;
    }

    public void setUserReportOptionsId(long userReportOptionsId) {
        this.userReportOptionsId = userReportOptionsId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public int getMultiUseLink() {
        return multiUseLink;
    }

    public void setMultiUseLink(int multiUseLink) {
        this.multiUseLink = multiUseLink;
    }

    public int getAccountPercentile() {
        return accountPercentile;
    }

    public void setAccountPercentile(int accountPercentile) {
        this.accountPercentile = accountPercentile;
    }

    public int getAltScores() {
        return altScores;
    }

    public void setAltScores(int altScores) {
        this.altScores = altScores;
    }

    public int getCompetencies() {
        return competencies;
    }

    public void setCompetencies(int competencies) {
        this.competencies = competencies;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getCountryPercentile() {
        return countryPercentile;
    }

    public void setCountryPercentile(int countryPercentile) {
        this.countryPercentile = countryPercentile;
    }

    public int getCustom1() {
        return custom1;
    }

    public void setCustom1(int custom1) {
        this.custom1 = custom1;
    }

    public int getCustom2() {
        return custom2;
    }

    public void setCustom2(int custom2) {
        this.custom2 = custom2;
    }

    public int getCustom3() {
        return custom3;
    }

    public void setCustom3(int custom3) {
        this.custom3 = custom3;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public int getDetailViewUrl() {
        return detailViewUrl;
    }

    public void setDetailViewUrl(int detailViewUrl) {
        this.detailViewUrl = detailViewUrl;
    }

    public int getEmail() {
        return email;
    }

    public void setEmail(int email) {
        this.email = email;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public int getIncludeCandFbkReptsInPdfDwnld() {
        return includeCandFbkReptsInPdfDwnld;
    }

    public void setIncludeCandFbkReptsInPdfDwnld(int includeCandFbkReptsInPdfDwnld) {
        this.includeCandFbkReptsInPdfDwnld = includeCandFbkReptsInPdfDwnld;
    }

    public int getIncludeFieldsForPerfUpload() {
        return includeFieldsForPerfUpload;
    }

    public void setIncludeFieldsForPerfUpload(int includeFieldsForPerfUpload) {
        this.includeFieldsForPerfUpload = includeFieldsForPerfUpload;
    }

    public int getItemResponses() {
        return itemResponses;
    }

    public void setItemResponses(int itemResponses) {
        this.itemResponses = itemResponses;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getOverallPercentile() {
        return overallPercentile;
    }

    public void setOverallPercentile(int overallPercentile) {
        this.overallPercentile = overallPercentile;
    }

    public int getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(int overallScore) {
        this.overallScore = overallScore;
    }

    public int getPerformanceValues() {
        return performanceValues;
    }

    public void setPerformanceValues(int performanceValues) {
        this.performanceValues = performanceValues;
    }

    public int getResponseRatings() {
        return responseRatings;
    }

    public void setResponseRatings(int responseRatings) {
        this.responseRatings = responseRatings;
    }

    public int getSendCandidateFbkReports() {
        return sendCandidateFbkReports;
    }

    public void setSendCandidateFbkReports(int sendCandidateFbkReports) {
        this.sendCandidateFbkReports = sendCandidateFbkReports;
    }

    public int getStarted() {
        return started;
    }

    public void setStarted(int started) {
        this.started = started;
    }

    public int getTestAdminUser() {
        return testAdminUser;
    }

    public void setTestAdminUser(int testAdminUser) {
        this.testAdminUser = testAdminUser;
    }

    public int getTestCreditsUsed() {
        return testCreditsUsed;
    }

    public void setTestCreditsUsed(int testCreditsUsed) {
        this.testCreditsUsed = testCreditsUsed;
    }

    public int getTestEventType() {
        return testEventType;
    }

    public void setTestEventType(int testEventType) {
        this.testEventType = testEventType;
    }

    public int getTestName() {
        return testName;
    }

    public void setTestName(int testName) {
        this.testName = testName;
    }

    public int getTestTakerIdentifier() {
        return testTakerIdentifier;
    }

    public void setTestTakerIdentifier(int testTakerIdentifier) {
        this.testTakerIdentifier = testTakerIdentifier;
    }

    public int getUserNoteDates() {
        return userNoteDates;
    }

    public void setUserNoteDates(int userNoteDates) {
        this.userNoteDates = userNoteDates;
    }

    public int getUserNotes() {
        return userNotes;
    }

    public void setUserNotes(int userNotes) {
        this.userNotes = userNotes;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
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


    @Override
    public boolean isBattery() {
        return battery==1;
    }

    @Override
    public boolean isMultiUseLink() {
        return multiUseLink==1;
    }

    @Override
    public boolean isAccountPercentile() {
        return accountPercentile==1;
    }

    @Override
    public boolean isAltScores() {
        return altScores==1;
    }

    @Override
    public boolean isCompetencies() {
        return competencies==1;
    }

    @Override
    public boolean isCompleted() {
        return completed==1;
    }

    @Override
    public boolean isCountryPercentile() {
        return countryPercentile==1;
    }

    @Override
    public boolean isCustom1() {
        return custom1==1;
    }

    @Override
    public boolean isCustom2() {
        return custom2==1;
    }
    @Override
    public boolean isCustom3() {
        return custom3==1;
    }
    
    @Override
    public boolean isDepartment() {
        return department==1;
    }

    @Override
    public boolean isDetailViewUrl() {
        return detailViewUrl==1;
    }

    @Override
    public boolean isEmail() {
        return email==1;
    }

    @Override
    public boolean isIdentifier() {
        return identifier==1;
    }

    @Override
    public boolean isIncludeCandFbkReptsInPdfDwnld() {
        return includeCandFbkReptsInPdfDwnld==1;
    }

    @Override
    public boolean isIncludeFieldsForPerfUpload() {
        return includeFieldsForPerfUpload==1;
    }

    @Override
    public boolean isItemResponses() {
        return itemResponses==1;
    }

    @Override
    public boolean isMobile() {
        return mobile==1;
    }

    @Override
    public boolean isName() {
        return name==1;
    }

    @Override
    public boolean isOverallPercentile() {
        return overallPercentile==1;
    }

    @Override
    public boolean isOverallScore() {
        return overallScore==1;
    }

    @Override
    public boolean isPerformanceValues() {
        return performanceValues==1;
    }

    @Override
    public boolean isResponseRatings() {
        return responseRatings==1;
    }

    @Override
    public boolean isSendCandidateFbkReports() {
        return sendCandidateFbkReports==1;
    }

    @Override
    public boolean isStarted() {
        return started==1;
    }

    @Override
    public boolean isTestAdminUser() {
        return testAdminUser==1;
    }

    @Override
    public boolean isTestCreditsUsed() {
        return testCreditsUsed==1;
    }

    @Override
    public boolean isTestEventType() {
        return testEventType==1;
    }

    @Override
    public boolean isTestName() {
        return testName==1;
    }

    @Override
    public boolean isTestTakerIdentifier() {
        return testTakerIdentifier==1;
    }

    @Override
    public boolean isUserNoteDates() {
        return userNoteDates==1;
    }

    @Override
    public boolean isUserNotes() {
        return userNotes==1;
    }

    @Override
    public boolean isUserStatus() {
        return userStatus==1;
    }

    public int getUserDemo() {
        return userDemo;
    }

    public void setUserDemo(int userDemo) {
        this.userDemo = userDemo;
    }
    
    public boolean isUserDemo() {
        return userDemo==1;
    }

    @Override
    public boolean isUserDemographics()
    {
        return userDemo==1;
    }

    public int getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(int totalSeconds) {
        this.totalSeconds = totalSeconds;
    }
    
    @Override
    public boolean isTotalSeconds() {
        return totalSeconds==1;
    }

    public int getAvgRespRatings() {
        return avgRespRatings;
    }

    public void setAvgRespRatings(int avgRespRatings) {
        this.avgRespRatings = avgRespRatings;
    }

    @Override
    public boolean isAvgRespRatings() {
        return avgRespRatings==1;
    }

    public int getSuspiciousActivity() {
        return suspiciousActivity;
    }

    public void setSuspiciousActivity(int suspiciousActivity) {
        this.suspiciousActivity = suspiciousActivity;
    }

    public int getPlagiarism() {
        return plagiarism;
    }

    public void setPlagiarism(int plagiarism) {
        this.plagiarism = plagiarism;
    }
    
    @Override
    public boolean isSuspiciousActivity() {
        return suspiciousActivity==1;
    }
    
    @Override
    public boolean isPlagiarism() {
        return plagiarism==1;
    }
    
    

}
