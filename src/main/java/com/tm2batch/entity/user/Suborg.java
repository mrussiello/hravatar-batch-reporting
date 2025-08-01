package com.tm2batch.entity.user;

import com.tm2batch.util.NVPair;
import com.tm2batch.util.StringUtils;
import java.io.Serializable;
import java.util.List;
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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;


@Cacheable
@Entity
@Table( name = "suborg" )
@XmlRootElement
@NamedQueries({
@NamedQuery ( name="Suborg.findForOrgid", query="SELECT o FROM Suborg AS o WHERE o.orgId=:orgId" ),
@NamedQuery ( name="Suborg.findActiveForOrgid", query="SELECT o FROM Suborg AS o WHERE o.orgId=:orgId AND o.suborgStatusTypeId=0" )
})
public class Suborg implements Serializable, Comparable<Suborg>, Cloneable
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="suborgid")
    private int suborgId;

    @Column(name="name")
    private String name;

    @Column(name="orgid")
    private int orgId = 0;

    @Column(name="adminuserid")
    private long adminUserId = 0;

    @Column(name="suborgstatustypeid")
    private int suborgStatusTypeId = 0;
    
    @Column(name="defaultmessagetext")
    private String defaultMessageText;

    @Column(name="excludefromnorms")
    private int excludeFromNorms;

    @Column( name = "defaulttesttakerlang" )
    private String defaultTestTakerLang;

    @Column( name = "defaultreportlang" )
    private String defaultReportLang;


    @Column(name="emailresultsto")
    private String emailResultsTo;

    @Column(name="textresultsto")
    private String textResultsTo;

    /**
     * packed string ruleid1|value1|ruleid2|value2;
     */
    @Column(name="reportflags")
    private String reportFlags;


    @Column(name="percentilecountry")
    private String percentileCountry;

    @Column(name="reportlogourl")
    private String reportLogoUrl;
    

    /**
     * Substitutions are [APPLICANT] - name, default to 'Applicant'
     *                   [CANDIDATE] - name, default to Candidate
     *                   [EMPLOYEE] - name, default to Employee
     *                   [COMPANY] - company (Org) name
     *                   [DEPARTMENT] - department (Suborg) name
     *                   [TEST] - product name
     *                   [TESTKEY] - Test Key PIN
     *                   [URL] - full URL to enter assessment
     *
     *
     */
    @Column(name="testkeyemailsubj")
    private String testKeyEmailSubj;

    @Column(name="testkeyemailmsg")
    private String testKeyEmailMsg;

    @Column(name="testkeysmsmsg")
    private String testKeySmsMsg;

    
    @Override
    public Object clone() throws CloneNotSupportedException 
    {
        return (Suborg) super.clone(); 
    }

    
    

    @Override
    public boolean equals( Object o )
    {
        if( o instanceof Suborg )
        {
            Suborg u = (Suborg) o;

            return orgId == u.getOrgId() && u.getSuborgId()==suborgId;
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 71 * hash + (int) (this.suborgId ^ (this.suborgId >>> 32));
        return hash;
    }

    public List<NVPair> getReportFlagList()
    {
        return StringUtils.parseNVPairsList( reportFlags, "|" );        
    }

    @Override
    public String toString() {
        return "Suborg{" + "suborgId=" + suborgId + ", name=" + name  + ", orgId=" + orgId + '}';
    }

    // @Transient
    @Override
    public int compareTo( Suborg b )
    {
        if( b.getOrgId() ==  orgId && b.getSuborgId()==suborgId )
            return 0;

        if( name != null && b.getName() != null )
            return name.compareTo( b.getName() );

        return 0;
    }
    
    
    public int getSuborgId() {
        return suborgId;
    }

    public void setSuborgId(int suborgId) {
        this.suborgId = suborgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public long getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(long adminUserId) {
        this.adminUserId = adminUserId;
    }


    public String getDefaultMessageText() {
        return defaultMessageText;
    }

    public void setDefaultMessageText(String defaultMessageText) {
        this.defaultMessageText = defaultMessageText;
    }

    public int getExcludeFromNorms() {
        return excludeFromNorms;
    }

    public void setExcludeFromNorms(int excludeFromNorms) {
        this.excludeFromNorms = excludeFromNorms;
    }

    public String getDefaultTestTakerLang() {
        return defaultTestTakerLang;
    }

    public void setDefaultTestTakerLang(String defaultTestTakerLang) {
        this.defaultTestTakerLang = defaultTestTakerLang;
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

    public String getDefaultReportLang() {
        return defaultReportLang;
    }

    public void setDefaultReportLang(String defaultReportLang) {
        this.defaultReportLang = defaultReportLang;
    }

    public String getReportFlags() {
        return reportFlags;
    }

    public void setReportFlags(String reportFlags) {
        this.reportFlags = reportFlags;
    }

    public String getPercentileCountry() {
        return percentileCountry;
    }

    public void setPercentileCountry(String percentileCountry) {
        this.percentileCountry = percentileCountry;
    }

    public String getTestKeyEmailSubj() {
        return testKeyEmailSubj;
    }

    public void setTestKeyEmailSubj(String testKeyEmailSubj) {
        this.testKeyEmailSubj = testKeyEmailSubj;
    }

    public String getTestKeyEmailMsg() {
        return testKeyEmailMsg;
    }

    public void setTestKeyEmailMsg(String testKeyEmailMsg) {
        this.testKeyEmailMsg = testKeyEmailMsg;
    }

    public String getTestKeySmsMsg() {
        return testKeySmsMsg;
    }

    public void setTestKeySmsMsg(String testKeySmsMsg) {
        this.testKeySmsMsg = testKeySmsMsg;
    }

    public String getReportLogoUrl() {
        return reportLogoUrl;
    }

    public void setReportLogoUrl(String reportLogoUrl) {
        this.reportLogoUrl = reportLogoUrl;
    }

    public int getSuborgStatusTypeId() {
        return suborgStatusTypeId;
    }

    public void setSuborgStatusTypeId(int suborgStatusTypeId) {
        this.suborgStatusTypeId = suborgStatusTypeId;
    }


}
