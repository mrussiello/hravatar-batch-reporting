package com.tm2batch.entity.autoreport;

import com.tm2batch.autoreport.BatchReportContentType;
import com.tm2batch.entity.user.Org;
import com.tm2batch.entity.user.Suborg;
import com.tm2batch.entity.user.User;
import com.tm2batch.service.LogService;
import com.tm2batch.util.I18nUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
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
@Table( name="batchreport" )
@NamedQueries({
    
    @NamedQuery ( name="BatchReport.findById", query="SELECT o FROM BatchReport AS o WHERE o.batchReportId = :batchReportId" ),
    @NamedQuery ( name="BatchReport.findByStatusAndScheduleDate", query="SELECT o FROM BatchReport AS o WHERE o.batchReportStatusTypeId=:batchReportStatusTypeId AND o.scheduleDate IS NOT NULL AND o.scheduleDate<=:maxScheduleDate" ),
    @NamedQuery ( name="BatchReport.findByStatusAndMinFreq", query="SELECT o FROM BatchReport AS o WHERE o.batchReportStatusTypeId=:batchReportStatusTypeId AND o.frequencyTypeId>=:minFrequencyTypeId" ),
    @NamedQuery ( name="BatchReport.findSpecialActive", query="SELECT o FROM BatchReport AS o WHERE o.batchReportStatusTypeId=:batchReportStatusTypeId AND o.batchReportContentTypeId=3 AND o.intParam2=1" )
})
public class BatchReport implements Serializable, Comparable<BatchReport>
{
    @Transient
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="batchreportid")
    private int batchReportId;

    @Column(name="orgid")
    private int orgId;
    
    @Column(name="suborgid")
    private int suborgId;
    
    @Column(name="userid")
    private long userId;

    @Column(name="title")
    private String title;

    @Column(name="emails")
    private String emails;

    @Column(name="langcode")
    private String langCode;
    
    @Column(name="timezoneid")
    private  String timeZoneId; // = "US/Eastern";

    @Column(name="yearsback")
    private int yearsBack;
    
    @Column(name="wholetimeunitsonly")
    private int wholeTimeUnitsOnly;    

    @Column(name="weeksback")
    private int weeksBack;

    @Column(name="monthsback")
    private int monthsBack;

    @Column(name="daysback")
    private int daysBack;

    @Column(name="hoursback")
    private int hoursBack;
    
    /**
     * 0 - 23 - this is in GMT - will run every hour. Default is 6 am EST
     */
    @Column(name="hourtosend")
    private int hourToSend=10;
    
    
    
    
    /**
     * StandardResultReport - this userId as authorizing user only
     * StandardUnfinishedTestKeyReport - this userId as authorizing user only
     * StandardLiveVideoReport - this userId as initiating user only
     * StandardRefCheckReport - this userId as admin user only
     * UminnOrgJustice - force reportId
     * DiscGroupReport - this userId as authorizing user only
     */
    @Column(name="intparam1")
    private int intParam1;
    
    /**
     * StandardResultReport - OrgAutoTestId
     * StandardLiveVideoReport - lvCallStatusTypeId (-1 for all)
     * StandardRefCheckReport - RcCheckTypeId (-1 for all)
     * DiscGroupReport - OrgAutoTestId
     * CreditUsageReport - send Daily MF when credits are low or empty.
     */
    @Column(name="intparam2")
    private int intParam2;
    
    /**
     * StandardResultReport - ProductId
     * StandardLiveVideoReport - Min lvCallStatusTypeId (-1 for Completed)
     * StandardRefCheckReport - RcCheckStatusTypeId (-1 for all)
     * DiscGroupReport - ProductId
     */
    @Column(name="intparam3")
    private int intParam3;
    
    /**
     * StandardResultReport - ProductTypeId
     * StandardLiveVideoReport - LvScriptId
     * StandardRefCheckReport - MIN RcCheckStatusTypeId (-1 for all)
     * DiscGroupReport - SuborgId
     * 
     */
    @Column(name="intparam4")
    private int intParam4;
    
    /**
     * StandardResultReport - ConsumerProductTypeId
     * StandardRefCheckReport - MAX RcCheckStatusTypeId (-1 for all)
     * 
     */
    @Column(name="intparam5")
    private int intParam5;
    
    /**
     * StandardResultReport - userCompanyStatusTypeId
     * StandardRefCheckReport - RcScriptId
     * 
     */
    @Column(name="intparam6")
    private int intParam6;
    
    /**
     * StandardResultReport - batteryId
     * StandardRefCheckReport - SortTypeId (0=default,1=last name, 2=status)
     * 
     */
    @Column(name="intparam7")
    private int intParam7;
    
    /**
     * StandardResultReport - TestResultSortTypeId
     * 
     */
    @Column(name="intparam8")
    private int intParam8;
    
    /**
     * 
     */
    @Column(name="intparam9")
    private int intParam9;
    
    /**
     * 
     */
    @Column(name="intparam10")
    private int intParam10;
    
    /**
     * 
     */
    @Column(name="intparam11")
    private int intParam11;
    
    /**
     * 
     */
    @Column(name="intparam12")
    private int intParam12;
    
    /**
     * 
     */
    @Column(name="intparam13")
    private int intParam13;
    
    /**
     * 
     */
    @Column(name="intparam14")
    private int intParam14;
    
    /**
     * 
     */
    @Column(name="intparam15")
    private int intParam15;
    
    @Column(name="longparam1")
    private long longParam1;
    
    @Column(name="longparam2")
    private long longParam2;
    
    @Column(name="floatparam1")
    private float floatParam1;
    
    @Column(name="floatparam2")
    private float floatParam2;
    
    @Column(name="floatparam3")
    private float floatParam3;
    

    
    
    /**
     * StandardResultReport - product name keyword
     * StandardUnfinishedTestKeyReport - product name keyword
     * UminnOrgJustice - force productIds Str
     * DiscGroupReport - Custom Field 1
     * 
     */
    @Column(name="strparam1")
    private String strParam1;
    
    /**
     * StandardResultReport - Custom fields in order 1, 2, 2 separated by commas
     * DiscGroupReport - Custom Field 2
     * 
     */
    @Column(name="strparam2")
    private String strParam2;

    /*
     * DiscGroupReport - Custom Field 3    
    */
    @Column(name="strparam3")
    private String strParam3;

    /*
     Alt Identifier Value
    */
    @Column(name="strparam4")
    private String strParam4;
    
    @Column(name="textparam1")
    private String textParam1;

    @Column(name="messagesubject")
    private String messageSubject;
        
    @Column(name="messagecontent")
    private String messageContent;
        
    
    //@Column(name="reportclassname")
    //private String reportClassName;
        
    @Column(name="batchreporttypeid")
    private int batchReportTypeId;

    @Column(name="batchreportcontenttypeid")
    private int batchReportContentTypeId;

    @Column(name="batchreportstatustypeid")
    private int batchReportStatusTypeId=1;

    @Column(name="frequencytypeid")
    private int frequencyTypeId=0;

    
    @Column(name="note")
    private String note;

    
        
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastupdate")
    private Date lastUpdate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastsenddate")
    private Date lastSendDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="scheduledate")
    private Date scheduleDate;
    
    
    @Transient
    private User user;
    
    @Transient
    private Org org;
    
    @Transient
    private Suborg suborg;
    
    @Transient
    private boolean specialProcessingCheck;
    
    
    @Override
    public String toString() {
        return "BatchReport{" + "batchReportId=" + batchReportId + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.batchReportId ^ (this.batchReportId >>> 32));
        return hash;
    }

    public TimeZone getTimeZone()
    {
        if( timeZoneId != null && !timeZoneId.isEmpty() )
            return TimeZone.getTimeZone( timeZoneId );

        if( user!=null )
            return user.getTimeZone();
                
        return TimeZone.getDefault();
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
        final BatchReport other = (BatchReport) obj;
        if (this.batchReportId != other.batchReportId) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(BatchReport o1) {
        
        if( title!=null && !title.isBlank() && o1.getTitle()!=null )
            return title.compareTo(o1.getTitle() );
        
        return ((Integer)batchReportId).compareTo( (Integer)o1.getBatchReportId());
    }

    public String[] getStringArray( int idx )
    {
        String raw = null;
        switch (idx) {
            case 1:
                raw=strParam1;
                break;
            case 2:
                raw=strParam2;
                break;
            case 3:
                raw=strParam3;
                break;
            case 4:
                raw=strParam4;
                break;
            default:
                break;
        }
        
        if( raw==null || raw.isBlank() )
            return null;
        
        return raw.trim().split(",");        
    }
    
    public BatchReportContentType getBatchReportContentType()
    {
        return BatchReportContentType.getValue(this.batchReportContentTypeId);
    }
    
    /*
     
    */
    public Date[] getDates()
    {
        boolean roundDateTimes = getBatchReportContentType().getRoundDateTimes();
        
        
        TimeZone tz = getTimeZone();
        // ZoneId zoneId = tz.toZoneId();
                
        Calendar cal = new GregorianCalendar();
        TimeZone ctz = cal.getTimeZone();
        ZoneId czoneId = ctz.toZoneId();
        
        if( yearsBack!=0 )
        {
            cal.add( Calendar.YEAR, -1*yearsBack );
            if( roundDateTimes && wholeTimeUnitsOnly==1 && monthsBack<=0 && weeksBack<=0 && daysBack<=0 && hoursBack<=0 )
            {
                cal.set( Calendar.MONTH, 0 );
                cal.set( Calendar.DAY_OF_MONTH, 1);
                cal.set( Calendar.HOUR_OF_DAY, 0);
                cal.set( Calendar.MINUTE, 0);
                cal.set( Calendar.SECOND, 0);
            }
        }

        if( monthsBack!=0 )
        {
            cal.add( Calendar.MONTH, -1*monthsBack );
            if( roundDateTimes && wholeTimeUnitsOnly==1 && weeksBack<=0 && daysBack<=0 && hoursBack<=0)
            {
                cal.set( Calendar.DAY_OF_MONTH, 1);
                cal.set( Calendar.HOUR_OF_DAY, 0);
                cal.set( Calendar.MINUTE, 0);
                cal.set( Calendar.SECOND, 0);
            }
        }

        if( weeksBack!=0 )
        {
            cal.add( Calendar.WEEK_OF_YEAR, -1*weeksBack );
            if( roundDateTimes && wholeTimeUnitsOnly==1 && daysBack<=0 && hoursBack<=0)
            {
                cal.set( Calendar.HOUR_OF_DAY, 0);
                cal.set( Calendar.MINUTE, 0);
                cal.set( Calendar.SECOND, 0);
            }
        }

        if( daysBack!=0 )
        {
            cal.add( Calendar.DAY_OF_YEAR, -1*daysBack );
            if( roundDateTimes && wholeTimeUnitsOnly==1  && hoursBack<=0)
            {
                cal.set( Calendar.HOUR_OF_DAY, 0);
                cal.set( Calendar.MINUTE, 0);
                cal.set( Calendar.SECOND, 0);
            }
        }
        
        if( hoursBack!=0 )
        {
            cal.add( Calendar.HOUR, -1*hoursBack );
            if( roundDateTimes &&  wholeTimeUnitsOnly==1 )
            {
                cal.set( Calendar.MINUTE, 0);
                cal.set( Calendar.SECOND, 0);
            }
        }
        
        // this is the local date time in the desired time zone.
        LocalDateTime localDateTime = LocalDateTime.ofInstant(cal.toInstant(), czoneId);

        Date sDate = Date.from( localDateTime.atZone(czoneId).toInstant() );
        //int offset = tz.getOffset(sDate.getTime());
        
        Date eDate = new Date();        

        if(  roundDateTimes && wholeTimeUnitsOnly==1 )
        {
            cal = new GregorianCalendar();
    
            if( yearsBack>0 && monthsBack<=0 && weeksBack<=0 && daysBack<=0 && hoursBack<=0 )
            {
                LogService.logIt( "BatchReport.getDates() yearsBack=" + yearsBack + ", daysBack=" + daysBack );
                cal.set( Calendar.MONTH, 0 );
                cal.set( Calendar.DAY_OF_MONTH, 1);
                cal.set( Calendar.HOUR_OF_DAY, 0);
                cal.set( Calendar.MINUTE, 0);
                cal.set( Calendar.SECOND, 0);

            }
            
            if( monthsBack>0 && weeksBack<=0 && daysBack<=0 && hoursBack<=0 )
            {
                cal.set( Calendar.DAY_OF_MONTH, 1);
                cal.set( Calendar.HOUR_OF_DAY, 0);
                cal.set( Calendar.MINUTE, 0);
                cal.set( Calendar.SECOND, 0);                
            }
            
            if( daysBack>0 && hoursBack<=0 )
            {
                // cal.set( Calendar.HOUR_OF_DAY, 0);
                cal.set( Calendar.MINUTE, 0);
                cal.set( Calendar.SECOND, 0);                
            }

            if( hoursBack>0 )
            {
                cal.set( Calendar.MINUTE, 0);
                cal.set( Calendar.SECOND, 0);                
            }
            
            localDateTime = LocalDateTime.ofInstant(cal.toInstant(), czoneId);

            eDate = Date.from( localDateTime.atZone(czoneId).toInstant() );        
        }
        
        return new Date[]{sDate,eDate};
    }
    
    public boolean matchesReport( BatchReport br )
    {
        return br.getUserId()==userId && br.getBatchReportTypeId()==this.batchReportTypeId;
    }
    
    
    public Locale getLocaleToUseDefaultUS()    
    {
        if( user!=null && user.getLangStr()!=null && !user.getLangStr().isBlank() )
            return I18nUtils.getLocaleFromCompositeStr(user.getLangStr());
        if( langCode!=null && !langCode.isBlank() )
            return I18nUtils.getLocaleFromCompositeStr(langCode);
        return Locale.US;
    }
    
    public int getBatchReportId() {
        return batchReportId;
    }

    public void setBatchReportId(int batchReportId) {
        this.batchReportId = batchReportId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public int getBatchReportTypeId() {
        return batchReportTypeId;
    }

    public void setBatchReportTypeId(int batchReportTypeId) {
        this.batchReportTypeId = batchReportTypeId;
    }

    public int getBatchReportStatusTypeId() {
        return batchReportStatusTypeId;
    }

    public void setBatchReportStatusTypeId(int batchReportStatusTypeId) {
        this.batchReportStatusTypeId = batchReportStatusTypeId;
    }

    public int getFrequencyTypeId() {
        return frequencyTypeId;
    }

    public void setFrequencyTypeId(int frequencyTypeId) {
        this.frequencyTypeId = frequencyTypeId;
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

    public int getDaysBack() {
        return daysBack;
    }

    public void setDaysBack(int daysBack) {
        this.daysBack = daysBack;
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

    public Date getLastSendDate() {
        return lastSendDate;
    }

    public void setLastSendDate(Date lastSendDate) {
        this.lastSendDate = lastSendDate;
    }



    public int getIntParam5() {
        return intParam5;
    }

    public void setIntParam5(int intParam5) {
        this.intParam5 = intParam5;
    }

    public String getMessageSubject() {
        return messageSubject;
    }

    public void setMessageSubject(String messageSubject) {
        this.messageSubject = messageSubject;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getSuborgId() {
        return suborgId;
    }

    public void setSuborgId(int suborgId) {
        this.suborgId = suborgId;
    }

    public int getIntParam6() {
        return intParam6;
    }

    public void setIntParam6(int intParam6) {
        this.intParam6 = intParam6;
    }

    public int getIntParam7() {
        return intParam7;
    }

    public void setIntParam7(int intParam7) {
        this.intParam7 = intParam7;
    }

    public int getIntParam8() {
        return intParam8;
    }

    public void setIntParam8(int intParam8) {
        this.intParam8 = intParam8;
    }

    public int getIntParam9() {
        return intParam9;
    }

    public void setIntParam9(int intParam9) {
        this.intParam9 = intParam9;
    }

    public int getIntParam10() {
        return intParam10;
    }

    public void setIntParam10(int intParam10) {
        this.intParam10 = intParam10;
    }

    public int getIntParam11() {
        return intParam11;
    }

    public void setIntParam11(int intParam11) {
        this.intParam11 = intParam11;
    }

    public int getIntParam12() {
        return intParam12;
    }

    public void setIntParam12(int intParam12) {
        this.intParam12 = intParam12;
    }

    public int getIntParam13() {
        return intParam13;
    }

    public void setIntParam13(int intParam13) {
        this.intParam13 = intParam13;
    }

    public int getIntParam14() {
        return intParam14;
    }

    public void setIntParam14(int intParam14) {
        this.intParam14 = intParam14;
    }

    public int getIntParam15() {
        return intParam15;
    }

    public void setIntParam15(int intParam15) {
        this.intParam15 = intParam15;
    }

    public long getLongParam1() {
        return longParam1;
    }

    public void setLongParam1(long longParam1) {
        this.longParam1 = longParam1;
    }

    public long getLongParam2() {
        return longParam2;
    }

    public void setLongParam2(long longParam2) {
        this.longParam2 = longParam2;
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

    public String getTextParam1() {
        return textParam1;
    }

    public void setTextParam1(String textParam1) {
        this.textParam1 = textParam1;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
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

    public int getHoursBack() {
        return hoursBack;
    }

    public void setHoursBack(int hoursBack) {
        this.hoursBack = hoursBack;
    }

    public int getMonthsBack() {
        return monthsBack;
    }

    public void setMonthsBack(int monthsBack) {
        this.monthsBack = monthsBack;
    }

    public int getYearsBack() {
        return yearsBack;
    }

    public void setYearsBack(int yearsBack) {
        this.yearsBack = yearsBack;
    }

    public int getWholeTimeUnitsOnly() {
        return wholeTimeUnitsOnly;
    }

    public void setWholeTimeUnitsOnly(int wholeTimeUnitsOnly) {
        this.wholeTimeUnitsOnly = wholeTimeUnitsOnly;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public int getBatchReportContentTypeId() {
        return batchReportContentTypeId;
    }

    public void setBatchReportContentTypeId(int batchReportContentTypeId) {
        this.batchReportContentTypeId = batchReportContentTypeId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public int getWeeksBack() {
        return weeksBack;
    }

    public void setWeeksBack(int weeksBack) {
        this.weeksBack = weeksBack;
    }

    public int getHourToSend() {
        return hourToSend;
    }

    public void setHourToSend(int hourToSend) {
        this.hourToSend = hourToSend;
    }

    public boolean getSpecialProcessingCheck() {
        return specialProcessingCheck;
    }

    public void setSpecialProcessingCheck(boolean specialProcessingCheck) {
        this.specialProcessingCheck = specialProcessingCheck;
    }

    
    

}
