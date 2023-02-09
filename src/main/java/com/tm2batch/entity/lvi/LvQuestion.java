package com.tm2batch.entity.lvi;


import com.tm2batch.entity.user.User;
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
@Table( name = "lvquestion" )
@NamedQueries({
        @NamedQuery( name = "LvQuestion.findByLvQuestionId", query = "SELECT o FROM LvQuestion AS o WHERE o.lvQuestionId=:lvQuestionId" )
})
public class LvQuestion implements Serializable, Comparable<LvQuestion>
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="lvquestionid")
    private long lvQuestionId;
    
    @Column(name="orgid")
    private int orgId;

    @Column(name="authoruserid")
    private long authorUserId;

    @Column(name="localestr")
    private String localeStr;

    @Column(name="name")
    private String name;

    @Column(name="question")
    private String question;
    
    @Column(name="anchorhi")
    private String anchorHi;

    @Column(name="anchormed")
    private String anchorMed;
    
    @Column(name="anchorlow")
    private String anchorLow;
            
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastupdate")
    private Date lastUpdate;
    
    @Transient
    private User user;
    
    @Transient
    private List<LvScript> lvScriptList;
    
    @Transient
    private Locale locale;
    
    
    @Override
    public String toString() {
        return "LvQuestion{" + "lvQuestionId=" + lvQuestionId + ", name=" + name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.lvQuestionId ^ (this.lvQuestionId >>> 32));
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
        final LvQuestion other = (LvQuestion) obj;
        if (this.lvQuestionId != other.lvQuestionId) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(LvQuestion o) {
        
        if( name!=null && o.getName()!=null )
            return name.compareTo( o.getName() );
        
        return (new Long(lvQuestionId)).compareTo( o.getLvQuestionId() );
    }

    public void sanitizeUserInput()
    {
        //name = StringUtils.sanitizeStringForCSSOnly(name);
        //question = StringUtils.sanitizeStringForCSSOnly(question);
        //anchorHi = StringUtils.sanitizeStringForCSSOnly(anchorHi);
        //anchorMed = StringUtils.sanitizeStringForCSSOnly(anchorMed);
        //anchorLow = StringUtils.sanitizeStringForCSSOnly(anchorLow);
    }    
    
    
    public boolean getHasAnchors()
    {
        return getHasAnchorHi() || getHasAnchorMed() || getHasAnchorLow();
    }

    public boolean getHasAnchorHi()
    {
        return anchorHi !=null && !anchorHi.isBlank();
    }
    public boolean getHasAnchorMed()
    {
        return anchorMed !=null && !anchorMed.isBlank();
    }
    public boolean getHasAnchorLow()
    {
        return anchorLow !=null && !anchorLow.isBlank();
    }
    
    public long getLvQuestionId() {
        return lvQuestionId;
    }

    public void setLvQuestionId(long lvQuestionId) {
        this.lvQuestionId = lvQuestionId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public long getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(long authorUserId) {
        this.authorUserId = authorUserId;
    }

    public String getLocaleStr() {
        return localeStr;
    }

    public void setLocaleStr(String localeStr) {
        this.localeStr = localeStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<LvScript> getLvScriptList() {
        return lvScriptList;
    }

    public void setLvScriptList(List<LvScript> lvScriptList) {
        this.lvScriptList = lvScriptList;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }



    public String getAnchorHi() {
        return anchorHi;
    }

    public void setAnchorHi(String anchorHi) {
        this.anchorHi = anchorHi;
    }

    public String getAnchorMed() {
        return anchorMed;
    }

    public void setAnchorMed(String anchorMed) {
        this.anchorMed = anchorMed;
    }

    public String getAnchorLow() {
        return anchorLow;
    }

    public void setAnchorLow(String anchorLow) {
        this.anchorLow = anchorLow;
    }


}
