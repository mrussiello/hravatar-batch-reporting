/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.entity.report;


import com.tm2batch.pdf.ReportTemplateType;
import com.tm2batch.util.NVPair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import jakarta.persistence.Basic;
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
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Locale;

/**
 *
 * @author Mike
 */
@Cacheable
@Entity
@Table( name = "report" )
@XmlRootElement
@NamedQueries({
    @NamedQuery ( name="Report.findByReportId", query="SELECT o FROM Report AS o WHERE o.reportId=:reportId" ),
    @NamedQuery ( name="Report.findByOrgIdAndStatus", query="SELECT o FROM Report AS o WHERE o.orgId=:orgId and o.reportStatusTypeId=:reportStatusTypeId" )
})
public class Report implements Serializable, Comparable<Report>
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="reportid")
    private long reportId;

    @Column(name="reportpurposetypeid")
    private int reportPurposeTypeId;

    @Column(name="reportstatustypeid")
    private int reportStatusTypeId;

    @Column(name="reporttemplatetypeid")
    private int reportTemplateTypeId;

    @Column(name="reportavailabilitytypeid")
    private int reportAvailabilityTypeId;
    
    @Column(name="implementationclass")
    private String implementationClass;

    @Column(name="emailformatterclass")
    private String emailFormatterClass;
    
    @Column(name="name")
    private String name;
    
    @Column(name="title")
    private String title;

    @Column(name="userid")
    private long userId;

    @Column(name="orgid")
    private long orgId;

    @Column(name="suborgid")
    private long suborgId;

    @Column(name="emailtesttaker")
    private int emailTestTaker = 0;
    
    
    @Column(name="includeinterview")
    private int includeInterview = 1;

    @Column(name="includenumericscores")
    private int includeNumericScores = 1;

    @Column(name="includecolorscores")
    private int includeColorScores = 1;

    @Column(name="includeoverallscore")
    private int includeOverallScore = 1;

    @Column(name="includeoverviewtext")
    private int includeOverviewText = 1;

    @Column(name="includecompetencyscores")
    private int includeCompetencyScores = 1;

    @Column(name="includecompetencycolorscores")
    private int includeCompetencyColorScores = 1;

    @Column(name="includeibminsight")
    private int includeIbmInsight = 1;

    
    @Column(name="includetaskscores")
    private int includeTaskScores = 1;

    @Column(name="includecompetencydescriptions")
    private int includeCompetencyDescriptions = 1;

    @Column(name="includeeductypedescrip")
    private int includeEducTypeDescrip = 1;

    @Column(name="includetrainingtypedescrip")
    private int includeTrainingTypeDescrip = 1;

    @Column(name="includerelatedexpertypedescrip")
    private int includeRelatedExperTypeDescrip = 1;

    @Column(name="includetaskinfo")
    private int includeTaskInfo = 1;  // 0=no, 1=yes first, 2=yes, after competencies

    @Column(name="maxinterviewquestionspercompetency")
    private int maxInterviewQuestionsPerCompetency = 1;

    @Column(name="includebiodatainfo")
    private int includeBiodataInfo = 1;

    @Column(name="includewritingsampleinfo")
    private int includeWritingSampleInfo = 1;

    @Column(name="includetaskinterestinfo")
    private int includeTaskInterestInfo = 1;

    @Column(name="includetaskexperienceinfo")
    private int includeTaskExperienceInfo = 1;

    @Column(name="includeminqualsinfo")
    private int includeMinQualsInfo = 1;

    @Column(name="includeapplicantdatainfo")
    private int includeApplicantDataInfo = 1;

    @Column(name="includeredflags")
    private int includeRedFlags = 1;

    @Column(name="includeitemscores")
    private int includeItemScores = 0;

    @Column(name="includescoretextinfo")
    private int includeScoreText = 1;

    @Column(name="includenorms")
    private int includeNorms = 1;

    @Column(name="includesubcategorynorms")
    private int includeSubcategoryNorms = 1;

    @Column(name="includesubcategorynumeric")
    private int includeSubcategoryNumeric = 1;

    @Column(name="includesubcategorycategory")
    private int includeSubcategoryCategory = 1;

    /**
     * packed string ruleid1|value1|ruleid2|value2;
     */
    @Column(name="reportflags")
    private String reportFlags;

    @Column(name="nopdfdoc")
    private int noPdfDoc = 0;


    @Column(name="intparam1")
    private int intParam1;

    @Column(name="intparam2")
    private int intParam2 = -1;

    @Column(name="intparam3")
    private int intParam3 = -1;
    
    /**
     * English Equivalent Report Id
     */
    @Column(name="longparam1")
    private long longParam1;

    

    @Column(name="floatparam1")
    private float floatParam1;

    @Column(name="floatparam2")
    private float floatParam2;

    @Column(name="floatparam3")
    private float floatParam3;

    /**
     * CT2 Report - Custom Key for Detail Text on cover page.
     */
    @Column(name="strparam1")
    private String strParam1;

    /**
     * CT2 Report - Custom Key for Report Title
     */
    @Column(name="strparam2")
    private String strParam2;

    /**
     * CT2 Report - Custom Report Title (overrides key and default title for report).
     */
    @Column(name="strparam3")
    private String strParam3;

    @Column(name="strparam4")
    private String strParam4;

    @Column(name="strparam5")
    private String strParam5;

    @Column(name="strparam6")
    private String strParam6;
    
    
    @Column(name="usernote")
    private String userNote;

    @Column(name="localestr")
    private String localeStr;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastupdate")
    private Date lastUpdate;


    @Transient
    private Locale localeForReportGen = null;


    public String getCompetencyColumnName()
    {
        return getReportFlagValueAsString( "competencycolumntitle" );
    }


    // @Transient
    public int compareTo( Report b )
    {
        if( b.getName() != null && name != null )
            return name.compareTo( b.getName() );

        return Long.valueOf(reportId).compareTo(b.getReportId());
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Report other = (Report) obj;
        if (this.reportId != other.reportId) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (int) (this.reportId ^ (this.reportId >>> 32));
        return hash;
    }


    public void sanitizeUserInput()
    {
        //name = StringUtils.sanitizeStringForCSSOnly(name);
        //userNote = StringUtils.sanitizeStringForCSSOnly(userNote);
    }

    public boolean getIncludeAnyScoreInfoIndiv()
    {
        return  getIncludeOverallScore()==1 ||
                getIncludeNumericScores() == 1 ||
                getIncludeCompetencyScores()==1 ||
                getIncludeColorScoresB() ||
                getIncludeApplicantDataInfo()==1 ||
                this.getIncludeTaskInfoB() ||
                this.getIncludeMinQualsInfo()==1 ||
                this.getIncludeInterviewB() ||
                this.getIncludeNorms()==1 ||
                this.getIncludeEducTypeDescripB() ||
                this.getIncludeRelatedExperTypeDescripB() ||
                this.getIncludeTrainingTypeDescripB() ||
                getIncludeInterviewB();
    }

    public boolean getIncludeAnyScoreInfo()
    {
        return  getIncludeAnyScoreInfoIndiv();

    }

    public String getReportFlagValueAsString( String key )
    {
        if( key==null || key.isBlank() || reportFlags==null )
            return null;
        for( NVPair p : getReportFlagList() )
        {
            if( p.getName().equals(key))
                return (String) p.getValue();
        }
        return null;
    }
    
    
    
    public List<NVPair> getReportFlagList()
    {
        List<NVPair> out = new ArrayList<>();

        if( reportFlags==null || reportFlags.isEmpty() )
            return out;

        StringTokenizer st = new StringTokenizer( reportFlags, "|" );

        String rule;
        String value;

        while( st.hasMoreTokens() )
        {
            rule = st.nextToken();

            if( !st.hasMoreTokens() )
                break;

            value = st.nextToken();

            if( rule != null && !rule.isEmpty() && value!=null && !value.isEmpty() )
                out.add( new NVPair( rule,value ) );
        }

        return out;
    }


    public ReportTemplateType getReportTemplateType()
    {
        return ReportTemplateType.getValue( reportTemplateTypeId );
    }


    @Override
    public String toString() {
        return "Report[ id=" + reportId + ", name: "  + name + "]";
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getIncludeColorScores() {
        return includeColorScores;
    }

    public void setIncludeColorScores(int includeColorScores) {
        this.includeColorScores = includeColorScores;
    }

    public int getIncludeCompetencyDescriptions() {
        return includeCompetencyDescriptions;
    }

    public void setIncludeCompetencyDescriptions(int includeCompetencyDescriptions) {
        this.includeCompetencyDescriptions = includeCompetencyDescriptions;
    }

    public int getIncludeCompetencyScores() {
        return includeCompetencyScores;
    }

    public void setIncludeCompetencyScores(int includeCompetencyScores) {
        this.includeCompetencyScores = includeCompetencyScores;
    }

    public int getIncludeEducTypeDescrip() {
        return includeEducTypeDescrip;
    }

    public void setIncludeEducTypeDescrip(int includeEducTypeDescrip) {
        this.includeEducTypeDescrip = includeEducTypeDescrip;
    }

    public int getIncludeInterview() {
        return includeInterview;
    }

    public void setIncludeInterview(int includeInterview) {
        this.includeInterview = includeInterview;
    }

    public int getIncludeNumericScores() {
        return includeNumericScores;
    }

    public void setIncludeNumericScores(int includeNumericScores) {
        this.includeNumericScores = includeNumericScores;
    }

    public int getIncludeOverallScore() {
        return includeOverallScore;
    }

    public void setIncludeOverallScore(int includeOverallScore) {
        this.includeOverallScore = includeOverallScore;
    }

    public int getIncludeRelatedExperTypeDescrip() {
        return includeRelatedExperTypeDescrip;
    }

    public void setIncludeRelatedExperTypeDescrip(int includeRelatedExperTypeDescrip) {
        this.includeRelatedExperTypeDescrip = includeRelatedExperTypeDescrip;
    }

    public int getIncludeTaskInfo() {
        return includeTaskInfo;
    }

    public void setIncludeTaskInfo(int includeTaskInfo) {
        this.includeTaskInfo = includeTaskInfo;
    }

    public int getIncludeTrainingTypeDescrip() {
        return includeTrainingTypeDescrip;
    }

    public void setIncludeTrainingTypeDescrip(int includeTrainingTypeDescrip) {
        this.includeTrainingTypeDescrip = includeTrainingTypeDescrip;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLocaleStr() {
        return localeStr;
    }

    public void setLocaleStr(String localeStr) {
        this.localeStr = localeStr;
    }

    public int getMaxInterviewQuestionsPerCompetency() {
        return maxInterviewQuestionsPerCompetency;
    }

    public void setMaxInterviewQuestionsPerCompetency(int maxInterviewQuestionsPerCompetency) {
        this.maxInterviewQuestionsPerCompetency = maxInterviewQuestionsPerCompetency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public int getReportPurposeTypeId() {
        return reportPurposeTypeId;
    }

    public void setReportPurposeTypeId(int reportPurposeTypeId) {
        this.reportPurposeTypeId = reportPurposeTypeId;
    }

    public int getReportStatusTypeId() {
        return reportStatusTypeId;
    }

    public void setReportStatusTypeId(int reportStatusTypeId) {
        this.reportStatusTypeId = reportStatusTypeId;
    }

    public int getReportTemplateTypeId() {
        return reportTemplateTypeId;
    }

    public void setReportTemplateTypeId(int reportTemplateTypeId) {
        this.reportTemplateTypeId = reportTemplateTypeId;
    }

    public long getSuborgId() {
        return suborgId;
    }

    public void setSuborgId(long suborgId) {
        this.suborgId = suborgId;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getReportAvailabilityTypeId() {
        return reportAvailabilityTypeId;
    }

    public void setReportAvailabilityTypeId(int reportAvailabilityTypeId) {
        this.reportAvailabilityTypeId = reportAvailabilityTypeId;
    }


    public boolean getIncludeInterviewB()
    {
        return includeInterview == 1;
    }

    public void setIncludeInterviewB( boolean b )
    {
        includeInterview = b ? 1 : 0;
    }


    public boolean getIncludeNumericScoresB()
    {
        return includeNumericScores == 1;
    }

    public void setIncludeNumericScoresB( boolean b )
    {
        includeNumericScores = b ? 1 : 0;
    }

    public boolean getIncludeColorScoresB()
    {
        return includeColorScores == 1;
    }

    public void setIncludeColorScoresB( boolean b )
    {
        includeColorScores = b ? 1 : 0;
    }

    public boolean getIncludeOverallScoreB()
    {
        return includeOverallScore == 1;
    }

    public void setIncludeOverallScoreB( boolean b )
    {
        includeOverallScore = b ? 1 : 0;
    }

    public boolean getIncludeCompetencyScoresB()
    {
        return includeCompetencyScores == 1;
    }

    public void setIncludeCompetencyScoresB( boolean b )
    {
        includeCompetencyScores = b ? 1 : 0;
    }

    public boolean getIncludeCompetencyDescriptionsB()
    {
        return includeCompetencyDescriptions == 1;
    }

    public void setIncludeCompetencyDescriptionsB( boolean b )
    {
        includeCompetencyDescriptions = b ? 1 : 0;
    }

    public boolean getIncludeEducTypeDescripB()
    {
        return includeEducTypeDescrip == 1;
    }

    public void setIncludeEducTypeDescripB( boolean b )
    {
        includeEducTypeDescrip = b ? 1 : 0;
    }


    public boolean getIncludeTrainingTypeDescripB()
    {
        return includeTrainingTypeDescrip == 1;
    }

    public void setIncludeTrainingTypeDescripB( boolean b )
    {
        includeTrainingTypeDescrip = b ? 1 : 0;
    }

    public boolean getIncludeRelatedExperTypeDescripB()
    {
        return includeRelatedExperTypeDescrip == 1;
    }

    public void setIncludeRelatedExperTypeDescripB( boolean b )
    {
        includeRelatedExperTypeDescrip = b ? 1 : 0;
    }

    public boolean getIncludeTaskInfoB()
    {
        return includeTaskInfo == 1;
    }

    public void setIncludeTaskInfoB( boolean b )
    {
        includeTaskInfo = b ? 1 : 0;
    }

    public int getIncludeOverviewText() {
        return includeOverviewText;
    }

    public void setIncludeOverviewText(int includeOverviewText) {
        this.includeOverviewText = includeOverviewText;
    }

    public int getIncludeTaskScores() {
        return includeTaskScores;
    }

    public void setIncludeTaskScores(int includeTaskScores) {
        this.includeTaskScores = includeTaskScores;
    }

    public int getIncludeBiodataInfo() {
        return includeBiodataInfo;
    }

    public void setIncludeBiodataInfo(int includeBiodataInfo) {
        this.includeBiodataInfo = includeBiodataInfo;
    }

    public int getIncludeWritingSampleInfo() {
        return includeWritingSampleInfo;
    }

    public void setIncludeWritingSampleInfo(int includeWritingSampleInfo) {
        this.includeWritingSampleInfo = includeWritingSampleInfo;
    }

    public int getIncludeTaskInterestInfo() {
        return includeTaskInterestInfo;
    }

    public void setIncludeTaskInterestInfo(int includeTaskInterestInfo) {
        this.includeTaskInterestInfo = includeTaskInterestInfo;
    }

    public int getIncludeTaskExperienceInfo() {
        return includeTaskExperienceInfo;
    }

    public void setIncludeTaskExperienceInfo(int includeTaskExperienceInfo) {
        this.includeTaskExperienceInfo = includeTaskExperienceInfo;
    }

    public int getIncludeMinQualsInfo() {
        return includeMinQualsInfo;
    }

    public void setIncludeMinQualsInfo(int includeMinQualsInfo) {
        this.includeMinQualsInfo = includeMinQualsInfo;
    }

    public int getIncludeApplicantDataInfo() {
        return includeApplicantDataInfo;
    }

    public void setIncludeApplicantDataInfo(int includeApplicantDataInfo) {
        this.includeApplicantDataInfo = includeApplicantDataInfo;
    }

    public int getIncludeRedFlags() {
        return includeRedFlags;
    }

    public void setIncludeRedFlags(int includeRedFlags) {
        this.includeRedFlags = includeRedFlags;
    }

    public int getIncludeScoreText() {
        return includeScoreText;
    }

    public void setIncludeScoreText(int includeScoreText) {
        this.includeScoreText = includeScoreText;
    }

    public int getIncludeNorms() {
        return includeNorms;
    }

    public void setIncludeNorms(int includeNorms) {
        this.includeNorms = includeNorms;
    }

    public int getIncludeSubcategoryNorms() {
        return includeSubcategoryNorms;
    }

    public void setIncludeSubcategoryNorms(int includeSubcategoryNorms) {
        this.includeSubcategoryNorms = includeSubcategoryNorms;
    }

    public int getIncludeSubcategoryNumeric() {
        return includeSubcategoryNumeric;
    }

    public void setIncludeSubcategoryNumeric(int includeSubcategoryNumeric) {
        this.includeSubcategoryNumeric = includeSubcategoryNumeric;
    }

    public int getIncludeSubcategoryCategory() {
        return includeSubcategoryCategory;
    }

    public void setIncludeSubcategoryCategory(int includeSubcategoryCategory) {
        this.includeSubcategoryCategory = includeSubcategoryCategory;
    }

    public String getReportFlags() {
        return reportFlags;
    }

    public void setReportFlags(String reportFlags) {
        this.reportFlags = reportFlags;
    }

    public int getNoPdfDoc() {
        return noPdfDoc;
    }

    public void setNoPdfDoc(int noPdfDoc) {
        this.noPdfDoc = noPdfDoc;
    }

    public int getIntParam1() {
        return intParam1;
    }

    public void setIntParam1(int intParam1) {
        this.intParam1 = intParam1;
    }

    public int getIntParam2() {
        return intParam2;
    }

    public void setIntParam2(int intParam2) {
        this.intParam2 = intParam2;
    }

    public int getIntParam3() {
        return intParam3;
    }

    public void setIntParam3(int intParam3) {
        this.intParam3 = intParam3;
    }

    public long getLongParam1() {
        return longParam1;
    }

    public void setLongParam1(long longParam1) {
        this.longParam1 = longParam1;
    }

    public float getFloatParam1() {
        return floatParam1;
    }

    public void setFloatParam1(float floatParam1) {
        this.floatParam1 = floatParam1;
    }

    public float getFloatParam2() {
        return floatParam2;
    }

    public void setFloatParam2(float floatParam2) {
        this.floatParam2 = floatParam2;
    }

    public float getFloatParam3() {
        return floatParam3;
    }

    public void setFloatParam3(float floatParam3) {
        this.floatParam3 = floatParam3;
    }

    public String getStrParam1() {
        return strParam1;
    }

    public void setStrParam1(String strParam1) {
        this.strParam1 = strParam1;
    }

    public String getStrParam2() {
        return strParam2;
    }

    public void setStrParam2(String strParam2) {
        this.strParam2 = strParam2;
    }

    public String getStrParam3() {
        return strParam3;
    }

    public void setStrParam3(String strParam3) {
        this.strParam3 = strParam3;
    }

    public String getStrParam4() {
        return strParam4;
    }

    public void setStrParam4(String strParam4) {
        this.strParam4 = strParam4;
    }

    public String getStrParam5() {
        return strParam5;
    }

    public void setStrParam5(String strParam5) {
        this.strParam5 = strParam5;
    }

    public String getStrParam6() {
        return strParam6;
    }

    public void setStrParam6(String strParam6) {
        this.strParam6 = strParam6;
    }

    public int getIncludeItemScores() {
        return includeItemScores;
    }

    public void setIncludeItemScores(int includeItemScores) {
        this.includeItemScores = includeItemScores;
    }

    public int getIncludeCompetencyColorScores() {
        return includeCompetencyColorScores;
    }

    public void setIncludeCompetencyColorScores(int includeCompetencyColorScores) {
        this.includeCompetencyColorScores = includeCompetencyColorScores;
    }

    public int getEmailTestTaker() {
        return emailTestTaker;
    }

    public void setEmailTestTaker(int emailTestTaker) {
        this.emailTestTaker = emailTestTaker;
    }

    public int getIncludeIbmInsight() {
        return includeIbmInsight;
    }

    public void setIncludeIbmInsight(int includeIbmInsight) {
        this.includeIbmInsight = includeIbmInsight;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Locale getLocaleForReportGen() {
        return localeForReportGen;
    }

    public void setLocaleForReportGen(Locale localeForReportGen) {
        this.localeForReportGen = localeForReportGen;
    }

    public String getImplementationClass() {
        return implementationClass;
    }

    public void setImplementationClass(String implementationClass) {
        this.implementationClass = implementationClass;
    }

    public String getEmailFormatterClass() {
        return emailFormatterClass;
    }

    public void setEmailFormatterClass(String emailFormatterClass) {
        this.emailFormatterClass = emailFormatterClass;
    }



}
