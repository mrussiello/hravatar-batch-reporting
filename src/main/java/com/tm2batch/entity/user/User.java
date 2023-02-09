package com.tm2batch.entity.user;

import com.tm2batch.service.EmailUtils;
import com.tm2batch.user.RoleType;
import com.tm2batch.user.UserType;
import java.io.Serializable;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

@Cacheable
@Entity
@Table( name = "xuser" )
@XmlRootElement
@NamedQueries({
    @NamedQuery ( name="User.findByEmailAndOrgId", query="SELECT o FROM User AS o WHERE o.email = :uemail AND o.orgId=:orgId" ),
    @NamedQuery ( name="User.findByUsername", query="SELECT o FROM User AS o  WHERE o.username = :uname" )
})
public class User implements Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="userid")
    private long userId;

    @Column(name="usertypeid")
    private int userTypeId;
    
    @Column(name="firstname")
    private String firstName;

    @Column(name="lastname")
    private String lastName;

    @Column(name="email")
    private String email;

    @Column(name="username")
    private String username;

    @Column(name="altidentifier")
    private String altIdentifier;

    @Column( name = "altidentifiername" )
    private String altIdentifierName;

    @Column(name="accountaccessleveltypeid")
    private int accountAccessLevelTypeId = 0;

    
    @Column(name="roleid")
    private int roleId = 0;
    
    @Column(name="orgid")
    private int orgId = 0;

    @Column(name="suborgid")
    private int suborgId = 0;

    @Column(name="language")
    private String langStr = null;

    @Column(name="timezoneid")
    private  String timeZoneId; // = "US/Eastern";
    
    @Column(name="countrycode")
    private String countryCode;

    @Column(name="ipcountry")
    private String ipCountry;

    
    /**
     * NOTE - this is now used as the Mobile phone number
     */
    @Column(name="phoneprefix")
    private String phonePrefix;
    
    
    @Column(name="userstatustypeid")
    private int userStatusTypeId = 0;

    @Column(name="usercompanystatustypeid")
    private int userCompanyStatusTypeId;
    
    @Column( name = "resetpwd" )
    private int resetPwd;

    
    @Column(name="perform1")
    private float perform1;

    @Column(name="perform2")
    private float perform2;

    @Column(name="perform3")
    private float perform3;

    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="performdate")
    private Date performDate;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lockoutdate")
    private Date lockoutDate;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="passwordstartdate")
    private Date passwordStartDate;

    @Transient
    private Org org;

    @Transient
    private Suborg suborg;

    @Transient
    private String countryName;

    @Transient
    private List<UserNote> userNoteList;
    
    @Transient
    private UserReportOptions userReportOptions;

    
    
    public RoleType getRoleType()
    {
        return RoleType.getValue( roleId );
    }




    @Override
    public boolean equals( Object o )
    {
        if( o instanceof User )
        {
            User u = (User) o;

            return userId == u.getUserId();
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (int) (this.userId ^ (this.userId >>> 32));
        return hash;
    }

    public UserType getUserType()
    {
        return UserType.getValue(userTypeId);
    }
    
    public boolean getHasMobilePhone()
    {
        return phonePrefix !=null && !phonePrefix.isEmpty();
    }

    public String getMobilePhone()
    {
        return phonePrefix;
    }

    public TimeZone getTimeZone()
    {
        if( timeZoneId != null && !timeZoneId.isEmpty() )
            return TimeZone.getTimeZone( timeZoneId );

        return TimeZone.getDefault();
    }


    public boolean getHasValidEmail()
    {
        return email!=null && !email.isBlank() && EmailUtils.validateEmailNoErrors(email);
    }
    
    
    
    public String getFullnameReverse()
    {
        String n = lastName;

        if( n == null )
            n = "";

        if( firstName != null && firstName.length() > 0 )
        {
            if( n.length() > 0 )
                n += ", ";

            n += firstName;
        }

        return n;        
    }
    
    
    // @Transient
    public String getFullname()
    {
        String fullName = firstName;

        if( fullName == null )
            fullName = "";

        if( lastName != null && lastName.length() > 0 )
        {
            if( fullName.length() > 0 )
                fullName += " ";

            fullName += lastName;
        }

        return fullName;
    }
    
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getUserStatusTypeId() {
        return userStatusTypeId;
    }

    public void setUserStatusTypeId(int userStatusTypeId) {
        this.userStatusTypeId = userStatusTypeId;
    }

    public Date getLockoutDate() {
        return lockoutDate;
    }

    public void setLockoutDate(Date lockoutDate) {
        this.lockoutDate = lockoutDate;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getSuborgId() {
        return suborgId;
    }

    public void setSuborgId(int suborgId) {
        this.suborgId = suborgId;
    }

    public Date getPasswordStartDate() {
        return passwordStartDate;
    }

    public void setPasswordStartDate(Date passwordStartDate) {
        this.passwordStartDate = passwordStartDate;
    }

    public int getResetPwd() {
        return resetPwd;
    }

    public void setResetPwd(int resetPwd) {
        this.resetPwd = resetPwd;
    }

    public String getLangStr() {
        return langStr;
    }

    public void setLangStr(String langStr) {
        this.langStr = langStr;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public String getPhonePrefix() {
        return phonePrefix;
    }

    public void setPhonePrefix(String phonePrefix) {
        this.phonePrefix = phonePrefix;
    }

    public float getPerform1() {
        return perform1;
    }

    public void setPerform1(float perform1) {
        this.perform1 = perform1;
    }

    public float getPerform2() {
        return perform2;
    }

    public void setPerform2(float perform2) {
        this.perform2 = perform2;
    }

    public float getPerform3() {
        return perform3;
    }

    public void setPerform3(float perform3) {
        this.perform3 = perform3;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getAltIdentifier() {
        return altIdentifier;
    }

    public void setAltIdentifier(String altIdentifier) {
        this.altIdentifier = altIdentifier;
    }

    public String getAltIdentifierName() {
        return altIdentifierName;
    }

    public void setAltIdentifierName(String altIdentifierName) {
        this.altIdentifierName = altIdentifierName;
    }

    public Date getPerformDate() {
        return performDate;
    }

    public void setPerformDate(Date performDate) {
        this.performDate = performDate;
    }

    public List<UserNote> getUserNoteList() {
        return userNoteList;
    }

    public void setUserNoteList(List<UserNote> userNoteList) {
        this.userNoteList = userNoteList;
    }

    public int getUserCompanyStatusTypeId() {
        return userCompanyStatusTypeId;
    }

    public void setUserCompanyStatusTypeId(int userCompanyStatusTypeId) {
        this.userCompanyStatusTypeId = userCompanyStatusTypeId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    public Suborg getSuborg() {
        return suborg;
    }

    public void setSuborg(Suborg suborg) {
        this.suborg = suborg;
    }

    public String getIpCountry() {
        return ipCountry;
    }

    public void setIpCountry(String ipCountry) {
        this.ipCountry = ipCountry;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public UserReportOptions getUserReportOptions() {
        return userReportOptions;
    }

    public void setUserReportOptions(UserReportOptions userReportOptions) {
        this.userReportOptions = userReportOptions;
    }

    public int getAccountAccessLevelTypeId() {
        return accountAccessLevelTypeId;
    }

    public void setAccountAccessLevelTypeId(int accountAccessLevelTypeId) {
        this.accountAccessLevelTypeId = accountAccessLevelTypeId;
    }



}
