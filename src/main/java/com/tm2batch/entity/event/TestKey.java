package com.tm2batch.entity.event;


import com.tm2batch.entity.user.User;
import com.tm2batch.entity.user.Org;
import com.tm2batch.event.TestKeyStatusType;
import com.tm2batch.event.OnlineProctoringType;
import com.tm2batch.event.TestKeyAdminType;
import com.tm2batch.service.LogService;
import com.tm2batch.service.EncryptUtils;
import com.tm2batch.entity.battery.Battery;
import com.tm2batch.entity.purchase.Product;
import com.tm2batch.entity.user.OrgAutoTest;
import com.tm2batch.entity.user.Suborg;
import com.tm2batch.entity.user.UserAction;
import com.tm2batch.purchase.ProductType;
import com.tm2batch.user.ReleaseCodeType;
import com.tm2batch.util.JsonUtils;


import java.io.Serializable;

import java.util.Date;

import java.util.Objects;
import jakarta.json.JsonObject;
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
import java.util.List;


@Cacheable(false)
@Entity
@Table( name = "testkey" )
@NamedQueries( {
        @NamedQuery( name = "TestKey.findByTestKeyId", query = "SELECT o FROM TestKey AS o WHERE o.testKeyId=:testKeyId" ),

} )
public class TestKey implements Serializable, Comparable<TestKey>
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "testkeyid" )
    private long testKeyId;

    @Column( name = "pin" )
    private String pin;

    @Column( name = "statustypeid" )
    private int testKeyStatusTypeId;

    @Column( name = "testkeyauthtypeid" )
    private int testKeyAuthTypeId;

    @Column( name = "testkeyproctortypeid" )
    private int testKeyProctorTypeId;


    @Column(name="testkeysourcetypeid")
    private int testKeySourceTypeId;

    @Column( name = "orgautotestid" )
    private int orgAutoTestId;

    @Column(name="apitypeid")
    private int apiTypeId;

    @Column( name = "mediadeliverymodetypeid" )
    private int mediaDeliveryModeTypeId = 0;

    @Column(name="frccountry")
    private String frcCountry;


    @Column( name = "orderid" )
    private long orderId;

    @Column( name = "orderitemid" )
    private long orderItemId;

    @Column( name = "creditid" )
    private long creditId;

    @Column( name = "creditindex" )
    private int creditIndex;
    
    @Column( name = "batteryid" )
    private int batteryId;

    @Column( name = "productid" )
    private int productId;

    @Column( name = "producttypeid" )
    private int productTypeId;

    @Column( name = "orgid" )
    private int orgId;

    @Column( name = "suborgid" )
    private int suborgId;

    @Column( name = "skinid" )
    private int skinId;

    @Column( name = "corpid" )
    private int corpId;

    @Column( name = "userid" )
    private long userId = 0;

    @Column( name = "authorizinguserid" )
    private long authorizingUserId = 0;

    @Column( name = "lang" )
    private String localeStr;

    @Column( name = "langreport" )
    private String localeStrReport;

    @Column( name = "namerqd" )
    private int nameRqd=-1;

    @Column( name = "demorqd" )
    private int demoRqd=-1;

    @Column( name= "releaserqd" )
    private int releaseRqd=-1;

    @Column( name = "releasecode" )
    private int releaseCode;

    @Column(name="firstdistcomplete")
    private int firstDistComplete;


    @Column(name="extref")
    private String extRef;

    @Column(name="emailresultsto")
    private String emailResultsTo;

    @Column(name="textresultsto")
    private String textResultsTo;

    @Column(name="returnurl")
    private String returnUrl;

    @Column(name="errorreturnurl")
    private String errorReturnUrl;

    @Transient
    private String pinsave;

    @Column(name="errortxt")
    private String errorTxt;

    //@Column( name = "reportid" )
    //private long reportId;

    @Column(name="emailcandidateok")
    private int emailCandidateOk;

    @Column(name="emaillogomessageok")
    private int emailLogoMessageOk;

    @Column(name="emailonettasklistok")
    private int emailOnetTaskListOk;

    /**
     * Used to indicate that the system should offer an optional interest survey. 
     * Once test key is started, used to indicate if the survey is NA or is accepted or declined.
     * 
     * 0=Do not offer
     * 1=Offer
     * 
     * 10=Offered but not accepted.
     * 11=Offered and accepted.
     * 
     */
    @Column(name="emailactivitylistok")
    private int emailActivityListOk;

    @Column(name="emailoverallscoresok")
    private int emailOverallScoresOk;

    @Column(name="emailcompetencyscoresok")
    private int emailCompetencyScoresOk;

    @Column(name="emailtaskscoresok")
    private int emailTaskScoresOk;

    @Column(name="emailaltscoresok")
    private int emailAltScoresOk;

    @Column(name="customemailmessagetext")
    private String customEmailMessageText;

    @Column(name="tempnameemail")
    private String tempNameEmail;

    @Column(name="testkeyadmintypeid")
    private int testKeyAdminTypeId;

    @Column(name="resultposturl")
    private String resultPostUrl;

    @Column(name="resultposttypeid")
    private int resultPostTypeId;

    @Column(name="requestaccessible")
    private int requestAccessible=-1;

    @Column(name="customparameters")
    private String customParameters;

    @Column(name="custom1")
    private String custom1;

    @Column(name="custom2")
    private String custom2;

    @Column(name="custom3")
    private String custom3;

    @Column(name="reminderdays")
    private int reminderDays;

    @Column(name="cumseconds")
    private int cumSeconds;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="startdate")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="expiredate")
    private Date expireDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="firstaccessdate")
    private Date firstAccessDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastaccessdate")
    private Date lastAccessDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastemaildate")
    private Date lastEmailDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lasttextdate")
    private Date lastTextDate;

    
    @Transient
    private long testKeyArchiveId = 0;

    @Transient
    private User user;

    @Transient
    private User authorizingUser;

    
    @Transient
    private Org org;

    @Transient
    private Suborg suborg;
    
    @Transient
    private OrgAutoTest orgAutoTest;
    
    @Transient
    private Product product;

    @Transient
    private Battery battery;

    @Transient
    private TestKeyArchive testKeyArchive;
    
    @Transient
    private List<TestEvent> testEventList;

    @Transient
    private List<UserAction> userActionList;
    
    @Transient
    private String startUrl;

    public boolean getIsArchive()
    {
        return testKeyArchiveId>0;
    }

    public void sanitizeUserInput()
    {
    }


    public TestKeyAdminType getTestKeyAdminType()
    {
        return TestKeyAdminType.getValue( testKeyAdminTypeId );
    }

    public TestKeyArchive getTestKeyArchive()
    {
        TestKeyArchive tka = testKeyArchive == null ? new TestKeyArchive() : testKeyArchive;

        tka.setTestKeyArchiveId(testKeyArchiveId);
        tka.setAuthorizingUserId( authorizingUserId);
        tka.setCorpId(corpId);
        tka.setExpireDate(expireDate);
        tka.setFirstAccessDate(firstAccessDate);
        tka.setLastAccessDate(lastAccessDate);
        tka.setLocaleStr(localeStr);
        tka.setLocaleStrReport(localeStrReport);
        tka.setOrderId(orderId);
        tka.setOrderItemId(orderItemId);
        tka.setCreditId(creditId);
        tka.setCreditIndex(creditIndex);
        tka.setOrgId(orgId);
        tka.setBatteryId(batteryId);
        // tka.setReportId(reportId);
        tka.setProductId(productId);
        tka.setProductTypeId(productTypeId);
        tka.setSkinId(skinId);
        tka.setStartDate(startDate);
        tka.setSuborgId(suborgId);
        tka.setTestKeyId(testKeyId);
        tka.setOrgAutoTestId(orgAutoTestId);
        tka.setTestKeyStatusTypeId(testKeyStatusTypeId);
        tka.setTestKeyAuthTypeId(testKeyAuthTypeId);
        tka.setTestKeyProctorTypeId(testKeyProctorTypeId);
        tka.setTestKeyAdminTypeId(testKeyAdminTypeId);
        tka.setTestKeySourceTypeId(testKeySourceTypeId);
        tka.setApiTypeId(apiTypeId);
        tka.setMediaDeliveryModeTypeId(mediaDeliveryModeTypeId);
        tka.setUserId(userId);
        tka.setNameRqd(nameRqd);
        tka.setDemoRqd(demoRqd);
        tka.setReleaseRqd(releaseRqd);
        tka.setReleaseCode(releaseCode);
        tka.setExtRef(extRef);
        tka.setFirstDistComplete(firstDistComplete);
        tka.setErrorTxt(errorTxt);
        tka.setEmailResultsTo(emailResultsTo);
        tka.setTextResultsTo(textResultsTo);
        tka.setTempNameEmail(tempNameEmail);
        tka.setReturnUrl(returnUrl);
        tka.setErrorReturnUrl(errorReturnUrl);
        tka.setEmailCandidateOk(emailCandidateOk);
        tka.setEmailLogoMessageOk(emailLogoMessageOk);
        tka.setEmailActivityListOk(emailActivityListOk);
        tka.setEmailOnetTaskListOk(emailOnetTaskListOk);
        tka.setEmailOverallScoresOk(emailOverallScoresOk);
        tka.setEmailCompetencyScoresOk(emailCompetencyScoresOk);
        tka.setEmailTaskScoresOk(emailTaskScoresOk);
        tka.setEmailAltScoresOk(emailAltScoresOk);
        tka.setCustomEmailMessageText(customEmailMessageText);
        tka.setFrcCountry(frcCountry);

        tka.setRequestAccessible( requestAccessible );
        tka.setResultPostUrl( resultPostUrl );
        tka.setResultPostTypeId( resultPostTypeId );
        tka.setCustomParameters(customParameters);
        tka.setCustom1( custom1 );
        tka.setCustom2( custom2 );
        tka.setCustom3( custom3 );
        tka.setReminderDays( reminderDays );
        tka.setCumSeconds( cumSeconds );

        tka.setLastEmailDate(lastEmailDate);
        tka.setLastTextDate(lastTextDate);

        if( pin!=null && !pin.isEmpty() && (tka.getPinsave()==null || tka.getPinsave().isEmpty() ) )
            tka.setPinsave(pin);

        else if( pinsave != null && !pinsave.isEmpty() )
            tka.setPinsave(pinsave);

        testKeyArchive = tka;

        return tka;
    }

    public void setTestKeyArchive( TestKeyArchive tka )
    {
        this.testKeyArchive = tka;
    }

    public int getFeedbackReportOk()
    {
        return getIntCustomParameterValue( "fbkkreportok" );
    }



    public boolean getFeedbackReportOkB()
    {
        return getIntCustomParameterValue( "fbkkreportok" )==1;
    }

    
    public boolean getHideMediaInReports()
    {
        String s = getCustomParameterValue( "hidemediainreports" );
        
        return s!=null && s.equalsIgnoreCase( "1" );        
    }

    public int getProctoringIdCaptureTypeId()
    {
        return getIntCustomParameterValue( "idcaptype" );
    }
    
    public int getOnlineProctoringTypeId()
    {
        // LogService.logIt( "TestKey.getOnlineProctoringTypeId() val=" + getIntCustomParameterValue( "onlineproctortype" ) + ", str=" + getCustomParameterValue( "onlineproctortype" ) + ", params=" + customParameters );
        return getIntCustomParameterValue( "onlineproctortype" );
    }
    
    public OnlineProctoringType getOnlineProctoringType()
    {
        return OnlineProctoringType.getValue( this.getOnlineProctoringTypeId() );
    }
    
    public int getSuspiciousActivityThresholdTypeId()
    {
        return getIntCustomParameterValue( "suspacttype" );
    }

    
    public int getOnlineProctoringCreditsCharged()
    {
        return getIntCustomParameterValue( "onlineproctorcredits" );        
    }
    
    public int getAnonymityType()
    {
        return getIntCustomParameterValue( "anonimitytype" );        
    }
    
    
    public int getIntCustomParameterValue( String nm )
    {
        String s = getCustomParameterValue( nm );
        
        if( s == null )
            return 0;
        
        return Integer.parseInt(s);
    }
    
    
    
    
    public String getCustomParameterValue( String name )
    {
        if( customParameters==null || customParameters.isEmpty() )
            return null;

        JsonObject jo = JsonUtils.getJsonObject( customParameters );

        return jo.getString( name, null );
    }

        
    public ProductType getProductType()
    {
        if( productTypeId <= 0 )
            return null;

        return ProductType.getValue( productTypeId );
    }

    public TestKeyStatusType getTestKeyStatusType()
    {
        return TestKeyStatusType.getValue( this.testKeyStatusTypeId );
    }

    public ReleaseCodeType getReleaseCodeType()
    {
        return ReleaseCodeType.getValue( releaseCode );
    }

    @Override
    public int compareTo(TestKey o)
    {
        if( pin != null && o.getPin() != null )
            return pin.compareTo( o.getPin() );

        return Long.valueOf(testKeyId).compareTo(o.getTestKeyId());
    }


    public String getTestKeyIdEncrypted()
    {
        try
        {
            return EncryptUtils.urlSafeEncrypt( testKeyId );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "getTestKeyIdEncrypted() " + toString() );

            return "";
        }
    }



    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TestKey other = (TestKey) obj;
        if (this.testKeyId != other.testKeyId) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.pin);
        return hash;
    }

    public String toString() {
        return "TestKey{" + "testKeyId=" + testKeyId + ", pin=" + pin + ", productId=" + productId + ", orgId=" + orgId + '}';
    }

    public String getPinToUse()
    {
        if( getPin()!=null && !getPin().isBlank() )
            return getPin();
        
        if( pinsave!=null && !pinsave.isBlank() )
            return pinsave;
        
        if( testKeyArchive!=null )
            return testKeyArchive.getPinsave();
        
        return getPin();
    }
    
    



    public int getCorpId() {
        return corpId;
    }

    public void setCorpId(int corpId) {
        this.corpId = corpId;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getLocaleStr() {
        
        if( localeStr==null )
            return "";
        
        return localeStr;
    }

    public void setLocaleStr(String localeStr) {
        this.localeStr = localeStr;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;

        if( org != null && org.getOrgId() != orgId )
            org = null;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;

        if( product != null && product.getProductId() != productId )
            product = null;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
        this.productTypeId = productTypeId;
    }

    public int getSkinId() {
        return skinId;
    }

    public void setSkinId(int skinId) {
        this.skinId = skinId;
    }

    public int getTestKeyStatusTypeId() {
        return testKeyStatusTypeId;
    }
    
    public void setTestKeyStatusTypeId(int statusTypeId) {
        this.testKeyStatusTypeId = statusTypeId;
    }

    public long getTestKeyId() {
        return testKeyId;
    }

    public void setTestKeyId(long testKeyId) {
        this.testKeyId = testKeyId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;

        if( user != null && user.getUserId() != userId )
            user = null;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getSubOrgId() {
        return suborgId;
    }

    public void setSubOrgId(int subOrgId) {
        this.suborgId = subOrgId;
    }

    public long getTestKeyArchiveId() {
        return testKeyArchiveId;
    }

    public void setTestKeyArchiveId(long testKeyArchiveId) {
        this.testKeyArchiveId = testKeyArchiveId;
    }

    public int getTestKeyAuthTypeId() {
        return testKeyAuthTypeId;
    }

    public void setTestKeyAuthTypeId(int testKeyAuthTypeId) {
        this.testKeyAuthTypeId = testKeyAuthTypeId;
    }

    public int getTestKeyProctorTypeId() {
        return testKeyProctorTypeId;
    }

    public void setTestKeyProctorTypeId(int testKeyProctorTypeId) {
        this.testKeyProctorTypeId = testKeyProctorTypeId;
    }

    public long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(int batteryId) {
        this.batteryId = batteryId;
    }

    public int getSuborgId() {
        return suborgId;
    }

    public void setSuborgId(int suborgId) {
        this.suborgId = suborgId;
    }

    public Battery getBattery() {
        return battery;
    }

    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    public int getDemoRqd() {
        return demoRqd;
    }

    public void setDemoRqd(int demoRqd) {
        this.demoRqd = demoRqd;
    }

    public int getNameRqd() {
        return nameRqd;
    }

    public void setNameRqd(int nameRqd) {
        this.nameRqd = nameRqd;
    }

    public boolean getNameRqdBoolean()
    {
        return nameRqd == 1;
    }

    public void setNameRqd(boolean b) {
        this.nameRqd = b ? 1 : 0;
    }

    public int getReleaseRqd() {
        return releaseRqd;
    }

    public void setReleaseRqd(int releaseRqd) {
        this.releaseRqd = releaseRqd;
    }

    public String getExtRef() {
        return extRef;
    }

    public void setExtRef(String extRef) {
        this.extRef = extRef;
    }

    public int getReleaseCode() {
        return releaseCode;
    }

    public void setReleaseCode(int releaseCode) {
        this.releaseCode = releaseCode;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    //public long getReportId() {
    //    return reportId;
    //}

    //public void setReportId(long reportId) {
    //    this.reportId = reportId;
    //}

    public String getErrorTxt() {
        return errorTxt;
    }

    public void setErrorTxt(String errorTxt) {
        this.errorTxt = errorTxt;
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

    public long getAuthorizingUserId() {
        return authorizingUserId;
    }

    public void setAuthorizingUserId(long authorizingUserId) {
        this.authorizingUserId = authorizingUserId;
    }

    public int getEmailCandidateOk() {
        return emailCandidateOk;
    }

    public void setEmailCandidateOk(int emailCandidateOk) {
        this.emailCandidateOk = emailCandidateOk;
    }

    public boolean getEmailCandidateOkB() {
        return emailCandidateOk == 1;
    }

    public void setEmailCandidateOkB(boolean b) {
        this.emailCandidateOk = b ? 1 : 0;
    }


    public int getEmailLogoMessageOk() {
        return emailLogoMessageOk;
    }

    public void setEmailLogoMessageOk(int emailLogoMessageOk) {
        this.emailLogoMessageOk = emailLogoMessageOk;
    }

    public boolean getEmailLogoMessageOkB() {
        return emailLogoMessageOk == 1;
    }

    public void setEmailLogoMessageOkB(boolean b) {
        this.emailLogoMessageOk = b ? 1 : 0;
    }

    public int getEmailOnetTaskListOk() {
        return emailOnetTaskListOk;
    }

    public void setEmailOnetTaskListOk(int emailOnetTaskListOk) {
        this.emailOnetTaskListOk = emailOnetTaskListOk;
    }

    public boolean getEmailOnetTaskListOkB() {
        return emailOnetTaskListOk == 1;
    }

    public void setEmailOnetTaskListOkB(boolean b) {
        this.emailOnetTaskListOk = b ? 1 : 0;
    }

    public int getEmailActivityListOk() {
        return emailActivityListOk;
    }

    public void setEmailActivityListOk(int emailActivityListOk) {
        this.emailActivityListOk = emailActivityListOk;
    }

    public boolean getEmailActivityListOkB() {
        return emailActivityListOk == 1;
    }

    public void setEmailActivityListOkB(boolean b) {
        this.emailActivityListOk = b ? 1 : 0;
    }

    public String getCustomEmailMessageText() {
        return customEmailMessageText;
    }

    public void setCustomEmailMessageText(String customEmailMessageText) {
        this.customEmailMessageText = customEmailMessageText;
    }

    public Date getFirstAccessDate() {
        return firstAccessDate;
    }

    public void setFirstAccessDate(Date firstAccessDate) {
        this.firstAccessDate = firstAccessDate;
    }


    public String getTempNameEmail() {
        return tempNameEmail;
    }

    public void setTempNameEmail(String tempNameEmail) {
        this.tempNameEmail = tempNameEmail;
    }

    public int getTestKeyAdminTypeId() {
        return testKeyAdminTypeId;
    }

    public void setTestKeyAdminTypeId(int testKeyAdminTypeId) {
        this.testKeyAdminTypeId = testKeyAdminTypeId;
    }

    public int getEmailOverallScoresOk() {
        return emailOverallScoresOk;
    }

    public void setEmailOverallScoresOk(int emailOverallScoresOk) {
        this.emailOverallScoresOk = emailOverallScoresOk;
    }

    public int getEmailCompetencyScoresOk() {
        return emailCompetencyScoresOk;
    }

    public void setEmailCompetencyScoresOk(int emailCompetencyScoresOk) {
        this.emailCompetencyScoresOk = emailCompetencyScoresOk;
    }

    public int getEmailTaskScoresOk() {
        return emailTaskScoresOk;
    }

    public void setEmailTaskScoresOk(int emailTaskScoresOk) {
        this.emailTaskScoresOk = emailTaskScoresOk;
    }

    public int getEmailAltScoresOk() {
        return emailAltScoresOk;
    }

    public void setEmailAltScoresOk(int emailAltScoresOk) {
        this.emailAltScoresOk = emailAltScoresOk;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public int getFirstDistComplete() {
        return firstDistComplete;
    }

    public void setFirstDistComplete(int firstdistcomplete) {
        this.firstDistComplete = firstdistcomplete;
    }

    public String getResultPostUrl() {
        return resultPostUrl;
    }

    public void setResultPostUrl(String resultPostUrl) {
        this.resultPostUrl = resultPostUrl;
    }

    public int getResultPostTypeId() {
        return resultPostTypeId;
    }

    public void setResultPostTypeId(int resultPostTypeId) {
        this.resultPostTypeId = resultPostTypeId;
    }

    public Date getLastEmailDate() {
        return lastEmailDate;
    }

    public void setLastEmailDate(Date lastEmailDate) {
        this.lastEmailDate = lastEmailDate;
    }

    public Date getLastTextDate() {
        return lastTextDate;
    }

    public void setLastTextDate(Date lastTextDate) {
        this.lastTextDate = lastTextDate;
    }

    public String getLocaleStrReport() {
        return localeStrReport;
    }

    public void setLocaleStrReport(String localeStrReport) {
        this.localeStrReport = localeStrReport;
    }

    public int getRequestAccessible() {
        return requestAccessible;
    }

    public void setRequestAccessible(int requestAccessible) {
        this.requestAccessible = requestAccessible;
    }
    public String getCustomParameters() {
        
        // LogService.logIt( "TestKey.getCustomParameters() " + customParameters );
        return customParameters;
    }

    public void setCustomParameters(String cp) {
        // LogService.logIt( "TestKey.setCustomParameters() " + cp );
        this.customParameters = cp;
    }

    public int getTestKeySourceTypeId() {
        return testKeySourceTypeId;
    }

    public void setTestKeySourceTypeId(int testKeySourceTypeId) {
        this.testKeySourceTypeId = testKeySourceTypeId;
    }

    public String getCustom1() {
        return custom1;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public String getCustom3() {
        return custom3;
    }

    public void setCustom3(String custom3) {
        this.custom3 = custom3;
    }

    public int getMediaDeliveryModeTypeId() {
        return mediaDeliveryModeTypeId;
    }

    public void setMediaDeliveryModeTypeId(int mediaDeliveryModeTypeId) {
        this.mediaDeliveryModeTypeId = mediaDeliveryModeTypeId;
    }

    public String getErrorReturnUrl() {
        return errorReturnUrl;
    }

    public void setErrorReturnUrl(String errorReturnUrl) {
        this.errorReturnUrl = errorReturnUrl;
    }

    public String getPinsave() {
        return pinsave;
    }

    public void setPinsave(String pinsave) {
        this.pinsave = pinsave;
    }

    public int getApiTypeId() {
        return apiTypeId;
    }

    public void setApiTypeId(int apiTypeId) {
        this.apiTypeId = apiTypeId;
    }

    public int getOrgAutoTestId() {
        return orgAutoTestId;
    }

    public void setOrgAutoTestId(int orgAutoTestId) {
        this.orgAutoTestId = orgAutoTestId;
    }

    public int getReminderDays() {
        return reminderDays;
    }

    public void setReminderDays(int reminderDays) {
        this.reminderDays = reminderDays;
    }

    public String getFrcCountry() {
        return frcCountry;
    }

    public void setFrcCountry(String frcCountry) {
        this.frcCountry = frcCountry;
    }

    public long getCreditId() {
        return creditId;
    }

    public void setCreditId(long creditId) {
        this.creditId = creditId;
    }

    public int getCumSeconds() {
        return cumSeconds;
    }

    public void setCumSeconds(int cumSeconds) {
        this.cumSeconds = cumSeconds;
    }

    public int getCreditIndex() {
        return creditIndex;
    }

    public void setCreditIndex(int creditIndex) {
        this.creditIndex = creditIndex;
    }

    public User getAuthorizingUser() {
        return authorizingUser;
    }

    public void setAuthorizingUser(User authorizingUser) {
        this.authorizingUser = authorizingUser;
    }

    public OrgAutoTest getOrgAutoTest() {
        return orgAutoTest;
    }

    public void setOrgAutoTest(OrgAutoTest orgAutoTest) {
        this.orgAutoTest = orgAutoTest;
    }

    public Suborg getSuborg() {
        return suborg;
    }

    public void setSuborg(Suborg suborg) {
        this.suborg = suborg;
    }

    public List<TestEvent> getTestEventList() {
        return testEventList;
    }

    public void setTestEventList(List<TestEvent> testEventList) {
        this.testEventList = testEventList;
    }

    public String getStartUrl() {
        return startUrl;
    }

    public void setStartUrl(String startUrl) {
        this.startUrl = startUrl;
    }

    public List<UserAction> getUserActionList() {
        return userActionList;
    }

    public void setUserActionList(List<UserAction> userActionList) {
        this.userActionList = userActionList;
    }



}
