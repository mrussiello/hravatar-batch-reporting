package com.tm2batch.entity.profile;


import com.tm2batch.profile.ProfileStatusType;
import com.tm2batch.profile.ProfileUsageType;
import com.tm2batch.profile.alt.AltScoreCalculator;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@Table( name = "profile" )
@NamedQueries({
    @NamedQuery ( name="Profile.findByOrgId", query="SELECT o FROM Profile AS o WHERE o.orgId=:orgId" )
})
public class Profile implements Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="profileid")
    private int profileId;

    @Column(name="orgid")
    private int orgId;

    @Column(name="applyforallorgs")
    private int applyForAllOrgs;
        
    @Column(name="name")
    private String name;

    @Column(name="profileusagetypeid")
    private int profileUsageTypeId = -1;

    
    
    @Column(name="profilestatustypeid")
    private int profileStatusTypeId;

    @Column(name="overallcomputemethodtypeid")
    private int overallComputeMethodTypeId;

    @Column(name="overalllowval")
    private float overallLowVal;

    @Column(name="overallhighval")
    private float overallHighVal;

    @Column(name="overallmean")
    private float overallMean;

    @Column(name="overallstddev")
    private float overallStdDev;

    @Column(name="intparam1")
    private int intParam1;

    @Column(name="intparam2")
    private int intParam2;

    @Column(name="intparam3")
    private int intParam3;

    @Column(name="intparam4")
    private int intParam4;

    @Column(name="intparam5")
    private int intParam5;

    @Column(name="floatparam1")
    private int floatParam1;

    @Column(name="floatparam2")
    private int floatParam2;

    @Column(name="floatparam3")
    private int floatParam3;

    @Column(name="floatparam4")
    private int floatParam4;

    @Column(name="floatparam5")
    private int floatParam5;

    @Column(name="floatparam6")
    private int floatParam6;

    @Column(name="floatparam7")
    private int floatParam7;

    @Column(name="floatparam8")
    private int floatParam8;

    @Column(name="floatparam9")
    private int floatParam9;

    @Column(name="floatparam10")
    private int floatParam10;

    // AltScore type - Name of Alt Score
    // 
    @Column(name="strparam1")
    private String strParam1;

    @Column(name="strparam2")
    private String strParam2;

    @Column(name="strparam3")
    private String strParam3;

    @Column(name="strparam4")
    private String strParam4;

    @Column(name="strparam5")
    private String strParam5;

    /**
     * For CT3 this is a series of competency names and weights, semicolon delimited.
     */
    @Column(name="textparam1")
    private String textParam1;






    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="computedate")
    private Date computeDate;


    @Transient
    private List<ProfileEntry> profileEntryList;

    
    @Transient
    private AltScoreCalculator altScoreCalculator;
    
    @Transient
    private String rowStyleClass;
    
    
    @Override
    public String toString() {
        return "Profile{" + "profileId=" + profileId + ", orgId=" + orgId + ", name=" + name + ", profileUsageTypeId=" +  profileUsageTypeId + ", profileStatusTypeId=" + profileStatusTypeId + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.profileId;
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
        final Profile other = (Profile) obj;
        if (this.profileId != other.profileId) {
            return false;
        }
        return true;
    }

    
    
    
    public ProfileEntry getLiveProfileEntry( String name, String nameEnglish)
    {
        if( this.profileEntryList==null )
            return null;

        boolean match = false;
        
        
        
        for( ProfileEntry pe : this.profileEntryList )
        {
            //LogService.logIt( "Profile.getLiveProfileEntry() seeking for " + name + ", ne=" + nameEnglish + ", pe.name=" + pe.getName() + ", pe.nameEnglish=" + pe.getNameEnglish() );
            //if( pe.getHasData() )
            //    continue;
            
             match = false;
             
            if( pe.getName().equalsIgnoreCase(name) )
                match =true;
            
            else if( nameEnglish != null && !nameEnglish.isEmpty() && pe.getNameEnglish() != null && pe.getNameEnglish().equalsIgnoreCase(nameEnglish) )
                match = true;
            
            else if( pe.getNameEnglish() != null && pe.getNameEnglish().equalsIgnoreCase(name) )
                match = true;

            //LogService.logIt( "Profile.getLiveProfileEntry() seeking for " + name + ", ne=" + nameEnglish + " MATCH=" + match);
            
            if( !match) 
                continue;
        
            return pe;
        }

        return null;
    }

    public float[] getOverallProfileData()
    {
        if( getOverallLowVal() >= getOverallHighVal() )
            return null;

        float[] out = new float[2];

        out[0] = getOverallLowVal();
        out[1] = getOverallHighVal();
        return out;
    }


    public float[] getProfileEntryData( String name, String nameEnglish)
    {
        float[] out = new float[2];

        ProfileEntry pe = getLiveProfileEntry(name, nameEnglish );

        if( pe == null || !pe.getHasHiLowData() )
            return null;

        out[0] = pe.getLowVal();
        out[1] = pe.getHighVal();
        return out;
    }


    public ProfileUsageType getProfileUsageType()
    {
        return ProfileUsageType.getValue( profileUsageTypeId );
    }
    
    public boolean getUsesProfileEntries()
    {
        return ProfileUsageType.getValue( profileUsageTypeId ).getUsesProfileEntries();
    }

    public ProfileStatusType getProfileStatusType()
    {
        return ProfileStatusType.getValue( profileStatusTypeId );
    }


    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProfileStatusTypeId() {
        return profileStatusTypeId;
    }

    public void setProfileStatusTypeId(int profileStatusTypeId) {
        this.profileStatusTypeId = profileStatusTypeId;
    }

    public List<ProfileEntry> getProfileEntryList() {
        return profileEntryList;
    }

    public void setProfileEntryList(List<ProfileEntry> profileEntryList) {
        this.profileEntryList = profileEntryList;
    }

    public Date getComputeDate() {
        return computeDate;
    }

    public void setComputeDate(Date computeDate) {
        this.computeDate = computeDate;
    }

    public int getOverallComputeMethodTypeId() {
        return overallComputeMethodTypeId;
    }

    public void setOverallComputeMethodTypeId(int overallComputeMethodTypeId) {
        this.overallComputeMethodTypeId = overallComputeMethodTypeId;
    }

    public float getOverallLowVal() {
        return overallLowVal;
    }

    public void setOverallLowVal(float overallLowVal) {
        this.overallLowVal = overallLowVal;
    }

    public float getOverallHighVal() {
        return overallHighVal;
    }

    public void setOverallHighVal(float overallHighVal) {
        this.overallHighVal = overallHighVal;
    }

    public float getOverallMean() {
        return overallMean;
    }

    public void setOverallMean(float overallMean) {
        this.overallMean = overallMean;
    }

    public float getOverallStdDev() {
        return overallStdDev;
    }

    public void setOverallStdDev(float overallStdDev) {
        this.overallStdDev = overallStdDev;
    }

    public int getProfileUsageTypeId() {
        return profileUsageTypeId;
    }

    public void setProfileUsageTypeId(int profileUsageTypeId) {
        this.profileUsageTypeId = profileUsageTypeId;
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

    public int getIntParam4() {
        return intParam4;
    }

    public void setIntParam4(int intParam4) {
        this.intParam4 = intParam4;
    }

    public int getIntParam5() {
        return intParam5;
    }

    public void setIntParam5(int intParam5) {
        this.intParam5 = intParam5;
    }

    public int getFloatParam1() {
        return floatParam1;
    }

    public void setFloatParam1(int floatParam1) {
        this.floatParam1 = floatParam1;
    }

    public int getFloatParam2() {
        return floatParam2;
    }

    public void setFloatParam2(int floatParam2) {
        this.floatParam2 = floatParam2;
    }

    public int getFloatParam3() {
        return floatParam3;
    }

    public void setFloatParam3(int floatParam3) {
        this.floatParam3 = floatParam3;
    }

    public int getFloatParam4() {
        return floatParam4;
    }

    public void setFloatParam4(int floatParam4) {
        this.floatParam4 = floatParam4;
    }

    public int getFloatParam5() {
        return floatParam5;
    }

    public void setFloatParam5(int floatParam5) {
        this.floatParam5 = floatParam5;
    }

    public int getFloatParam6() {
        return floatParam6;
    }

    public void setFloatParam6(int floatParam6) {
        this.floatParam6 = floatParam6;
    }

    public int getFloatParam7() {
        return floatParam7;
    }

    public void setFloatParam7(int floatParam7) {
        this.floatParam7 = floatParam7;
    }

    public int getFloatParam8() {
        return floatParam8;
    }

    public void setFloatParam8(int floatParam8) {
        this.floatParam8 = floatParam8;
    }

    public int getFloatParam9() {
        return floatParam9;
    }

    public void setFloatParam9(int floatParam9) {
        this.floatParam9 = floatParam9;
    }

    public int getFloatParam10() {
        return floatParam10;
    }

    public void setFloatParam10(int floatParam10) {
        this.floatParam10 = floatParam10;
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

    

    public String getTextParam1() {
        return textParam1;
    }

    public void setTextParam1(String textParam1) {
        this.textParam1 = textParam1;
    }

    public String getRowStyleClass() {
        return rowStyleClass;
    }

    public void setRowStyleClass(String rowStyleClass) {
        this.rowStyleClass = rowStyleClass;
    }

    public int getApplyForAllOrgs() {
        return applyForAllOrgs;
    }

    public void setApplyForAllOrgs(int applyForAllOrgs) {
        this.applyForAllOrgs = applyForAllOrgs;
    }

    public AltScoreCalculator getAltScoreCalculator() {
        return altScoreCalculator;
    }

    public void setAltScoreCalculator(AltScoreCalculator altScoreCalculator) {
        this.altScoreCalculator = altScoreCalculator;
    }



}
