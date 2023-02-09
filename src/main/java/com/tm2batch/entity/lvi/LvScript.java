package com.tm2batch.entity.lvi;


import com.tm2batch.entity.user.User;
import com.tm2batch.service.EncryptUtils;
import com.tm2batch.service.LogService;
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
@Table( name = "lvscript" )
@NamedQueries({
        @NamedQuery( name = "LvScript.findByLvScriptId", query = "SELECT o FROM LvScript AS o WHERE o.lvScriptId=:lvScriptId" ),
})
public class LvScript implements Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="lvscriptid")
    private int lvScriptId;
    
    @Column(name="orgid")
    private int orgId;

    @Column(name="authoruserid")
    private long authorUserId;

    @Column(name="localestr")
    private String localeStr;

    @Column(name="name")
    private String name;
        
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastupdate")
    private Date lastUpdate;
    
    
    @Transient
    private List<LvScriptQuestionMap> lvScriptQuestionMapList;
    
    @Transient
    private Locale locale;
    
    @Transient
    private User user;
    
    @Transient
    private User recipientUser;
    
    @Transient
    private Date interviewDate;
        
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
    
    @Override
    public String toString() {
        return "LvScript{" + "lvScriptId=" + lvScriptId + ", name=" + name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.lvScriptId ^ (this.lvScriptId >>> 32));
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
        final LvScript other = (LvScript) obj;
        if (this.lvScriptId != other.lvScriptId) {
            return false;
        }
        return true;
    }

    public void sanitizeUserInput()
    {
        //name = StringUtils.sanitizeStringForCSSOnly(name);
    }    
    
    public String getLvScriptIdEncrypted()
    {
        try
        {
            return EncryptUtils.urlSafeEncrypt( lvScriptId );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "LvScript.getLvScriptIdEncrypted() " + toString() );
            return "";
        }        
    }
    
    
    
    
    public int getLvScriptId() {
        return lvScriptId;
    }

    public void setLvScriptId(int lvScriptId) {
        this.lvScriptId = lvScriptId;
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

    public List<LvScriptQuestionMap> getLvScriptQuestionMapList() {
        return lvScriptQuestionMapList;
    }

    public void setLvScriptQuestionMapList(List<LvScriptQuestionMap> lvScriptQuestionMapList) {
        this.lvScriptQuestionMapList = lvScriptQuestionMapList;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getRecipientUser() {
        return recipientUser;
    }

    public void setRecipientUser(User recipientUser) {
        this.recipientUser = recipientUser;
    }

    public Date getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(Date interviewDate) {
        this.interviewDate = interviewDate;
    }


}
