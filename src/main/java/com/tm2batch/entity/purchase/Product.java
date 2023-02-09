package com.tm2batch.entity.purchase;


import com.tm2batch.entity.battery.Battery;
import com.tm2batch.purchase.ConsumerProductType;
import com.tm2batch.purchase.ProductType;
import com.tm2batch.util.I18nUtils;
import com.tm2batch.util.NVPair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;


@Entity
@Table( name = "product" )
@NamedQueries({
})
public class Product implements Serializable, Comparable<Product>
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "productid" )
    private int productId;

    @Column( name = "producttypeid" )
    private int productTypeId;

    @Column( name = "productordertypeid" )
    private int productOrderTypeId;

    @Column( name = "productstatustypeid" )
    private int productStatusTypeId;

    @Column( name = "creditusagetypeid" )
    private int creditUsageTypeId;

    @Column( name = "name" )
    private String name;

    @Column( name = "nameenglish" )
    private String nameEnglish;

    @Column( name = "lang" )
    private String langStr = "en_US";

    @Column( name = "price" )
    private float price;


    /**
     * Sim - Name of Sim
     * Findly - name of Findly Test
     * IVR, VOT - Name of Sim
     */
    @Column( name = "strparam1" )
    private String strParam1;


    /**
     *
     * CT3 Sim - Non-Standard Risk Factor String. This is a string with the following format:
     *     CT3RiskFactorTypeId;int1;int2;int3;float1;float2;float3;str1;str2|CT3RiskFactorTypeId;int1;int2;int3;float1;float2;float3;str1;str2|
     *  ...
     * IVR - Specific Item Unique Ids to use in the test, delimited by ;  (Normally not used, this creates a static IVR test).
     * VOT - Unique Id of the first Interation to present. 
     * IFRAME - Default Test Start URL
     *
     */
    @Column( name = "strparam2" )
    private String strParam2;


    /**
     * Findly - Findly Test Code (Test ID)
     * IVR - Specific IvrItemTypeIds, in order, delimited by ; and number of items for each. So, 1;10;3;2;2;5 means 10 items of type 1 followed by 3 items of type 2, followed by 5 items of type2.
     */
    @Column( name = "strparam3" )
    private String strParam3;


    /**
     * Sim - CT6 Short Test Alternatives. A comma delimited list of child (short) CT6 Assessment Product Ids.  These are presented in the catalog as alternatives for this test. 
     * 
     * Findly Category
     * Ivr or VOT - Special ResourceBundle to use. Default is null (will use com.tm2test.test.ivr.resources.IvrMessages)
     */
    @Column( name = "strparam4" )
    private String strParam4;

    /**
     * Sim - CT6 Short Test Alternatives WITH VIDEO INTERVIEW. A comma delimited list of child (short) CT6 Assessment Product Ids that have Video interview.  These are presented in the catalog as alternatives for this test. 
     * 
     * Findly - Test Description
     * PeopleTest - Test Description
     * IVR or VOT - Default Test ADMIN Language. Will set when Test Key is created. Can be over-ridden by settings in API or on Admin Site.
     */
    @Column( name = "strparam5" )
    private String strParam5;

    /**
     * 
     * Findly - LangStr
     * PeopleTest - LangStr
     * IVR or VOT - Test CONTENT Language. This is the language of the content itself. For language tests, this is the language tested as a locale. (en_Us) If not present, the Product.language will be used.
     * 
     * Other and ConsumerProductType=Other:  Type description, like "Emotional Intelligence Test"
     */
    @Column( name = "strparam6" )
    private String strParam6;

    /**
     * Findly Online Category
     * SIM - Recorded Media Language. This is the language of recorded media included in this test. For language tests, this is the language tested as a locale. (en_Us) If not present, the Product.language will be used.
     */
    @Column( name = "strparam7" )
    private String strParam7;

    /**
     * Sim or Battery - Return URL to override Return URL set in the org or the corp (portal). Will not override a return url set by the API.
     * Findly Test Type
     * ADP Data Connector - appId to be used in the login entry URL. This is what tells us what credentials to use for the authorization connection.
     */
    @Column( name = "strparam8" )
    private String strParam8;

    /**
     * Sim or Battery - Email to. This replaces the email to in the test key.
     * Findly Test ReportType
     */
    @Column( name = "strparam9" )
    private String strParam9;

    /**
     * Sim or Battery - Text to. This replaces the Text to in the test key.
     * Findly WS Test Type
     */
    @Column( name = "strparam10" )
    private String strParam10;

    /**
     * Report Flags: packed string ruleid1|value1|ruleid2|value2;
     */
    @Column(name="strparam11")
    private String strParam11;

    
    /**
     * Any Sim - Keywords to add to Mashup
     */
    @Column(name="strparam12")
    private String strParam12;

    
    /*
     * Credit Purchase - number of credits in this package.
     * Battery Type - indicates batteryId
     * Credit: # of Credits,
     * Resource: 0=all, 1=assessment, 2=preview, 3=training, 4=technology, 5=production
     * Subscription: 0=HR Avatar Tests Only. 1=Findly Tests Included.
     * Ivr or VOT - IvrTestTypeId (0=Lang Skills, 1=Screen)
     * Video Interview - VideoInterviewCategoryTypeId
     * Live Video Subscrip - Number of Users in Product
     * IFrame - width of iframe in pixels (default 0=full window)
     *   
     */
    @Column( name = "intparam1" )
    private int intParam1;

    /*
     *
     * Sim - Requires larger screen.
     * Subscription: Number of months for subscription. 0=indefinite.
       Live Video Subscrip - LiveVideoOrgAccessTypeId
     * IFrame - height of iframe in pixels (default 0=full window)
     *  
     *
     */
    @Column( name = "intparam2" )
    private int intParam2;



    /**
     * FINDLY - Tokens used
     * Sim - Best with keyboard devices.
     * IVR / VOT - ignore "AUDIO" tags and use computer gen voice only
     * Subscription - SubscriptionOrgTypeId (1=End User, 2=Recruitment)
     * 
     */
    @Column( name = "intparam3" )
    private int intParam3;

    /**
     * Findly Admin Time - minutes
     * Sim - includes Riasec Sim Competencies
     * IVR or VOT - question count
     * Subscription - SubscriptionVolumeTypeId
     * 
     */
    @Column( name = "intparam4" )
    private int intParam4;

    /**
     * Findly Item Count
     * Sim - For Job Specific, this is the productId for the recommended Whole-Person Sim.
     * Ivr or VOT - Duration (Minutes)
     * Subscription - max Testing Events
     * 
     */
    @Column( name = "intparam5" )
    private int intParam5;


    /**
       Findly - 1=Mobile Ready, 2=Not Mobile Ready
     * Sim or Battery - 
     *      0=no file upload, 
     *      1=Includes general file upload, 
     *      2=recorded video file upload. 
     *      3=recorded audio-only upload, 
     *      4=captured image-only media file upload for proctoring, 
     *      5=captured image only not for proctoring  
     *      6=captured images for proctoring, and audio. 
     *        Typically used as a flag for a NOTE in the email message sent to user.
     * Subscription - Number of Live Video Interview Seats included. 9999 or above means unlimited for entire org.
     */
    @Column( name = "intparam6" )
    private int intParam6;


    /**
     * Findly - FindlyTestTypeId
     * Sim - Calculate Competency Percentiles.
     * Subscription - 0=no proc, 1=include Basic, 2=Include Premium 
     */
    @Column( name = "intparam7" )
    private int intParam7;

    /**
     * Findly - Skills CategoryTypeId
     * Sim - Skills CategoryTypeId
     * Ivr - Skills CategoryTypeId
     * WHOLE = wholePersonCategoryTypeId
     */
    @Column( name = "intparam8" )
    private int intParam8;

    /**
     * Sim - CT3StandardModuleTypeId for Cog Sim if included in sim.
     * WHOLE - CT3StandardModuleTypeId for Cog Sim if included in sim.
     */
    @Column( name = "intparam9" )
    private int intParam9;

    /**
     * Sim - NOT eligible for unlimited accounts.
     */
    @Column( name = "intparam10" )
    private int intParam10;

    
    /*
     * Sim - 0=default
             1=This is a short version of another sim, with no video 
             2=This is a short version of another sim, with video 
    */    
    @Column( name = "intparam11" )
    private int intParam11;

    
    /*
     *  Sim - Legacy Product Id. When an Org is set to show legacy products, if there is a 
              legacy product id it will show the detail for this product rather than the actual 
              product found in the catalog.
    */
    @Column( name = "intparam12" )
    private int intParam12;

    /*
     Sim - 0=Voice Vibes is not required for scoring.
           1=Voice vibes IS required for scoring.
    */
    @Column( name = "intparam13" )
    private int intParam13;

    
    /*
     Sim - 0=No effect
           1=disable any item-level timers (by setting initvalue 8=1
    .
    */    
    @Column( name = "intparam14" )
    private int intParam14;

    /*
     Sim - 0=No effect
           1=disable any IMO-level timers (by setting initvalue 9=1
    .
    */    
    @Column( name = "intparam15" )
    private int intParam15;

    
    /*
     Sim or Battery - RcScriptId to include in the Test Key
    */
    @Column( name = "intparam16" )
    private int intParam16;
    
    
    /*
     Sim or Battery - SuborgId to force Test key and user into (used ONLY for product choice). 
    */
    @Column( name = "intparam17" )
    private int intParam17;
    
    
    @Column( name = "intparam18" )
    private int intParam18;
    
    
    @Column( name = "floatparam1" )
    private float floatParam1;

    @Column( name = "floatparam2" )
    private float floatParam2;



    //@Column( name = "imageuri" )
    //private String imageUri;

    @Column(name="simcontexttypeid")
    private int simContextTypeId = 0;

    @Column(name="othersimcontexttype")
    private String otherSimContextType;

    @Column(name="onetsoc")
    private String onetSoc;

    @Column(name="onetversion")
    private String onetVersion;


    @Column( name = "simdescriptorid" )
    private long simDescriptorId;

    /**
     * Sim - SimId simid simId
     */
    @Column( name = "longparam1" )
    private long longParam1;

    /**
     * Default report Id
     */
    @Column( name = "longparam2" )
    private long longParam2;

    /**
     * Default report Id #2
     */
    @Column( name = "longparam3" )
    private long longParam3;

    /**
     * English Equivalent SimId
     */
    @Column( name = "longparam4" )
    private long longParam4;

    @Column( name = "longparam5" )
    private long longParam5;


    @Column( name = "consumerproducttypeid" )
    private int consumerProductTypeId;

    @Column(name="iconfilename")
    private String iconFilename;

    @Column(name="detailview")
    private String detailView;

    
    @Transient
    Battery battery;
    
    @Override
    public int compareTo(Product p2) {
        
        if( name!=null && !name.isBlank() && p2.getName()!=null )
            return name.compareTo(p2.getName() );
        return ((Integer)productId).compareTo( (Integer)p2.getProductId() );
    }

    
    public String getNameWithEnglishIfNeeded()
    {
        if( getNeedsNameEnglish() )
                return name + " (English: " + nameEnglish + ")";

        return name;
    }

    public boolean getNeedsNameEnglish()
    {
        if( this.langStr!=null && !this.langStr.isBlank() )
        {
            if( I18nUtils.getLocaleFromCompositeStr(langStr).getLanguage().toLowerCase().contains("en") )
                return false;
        }
        
        if( nameEnglish == null || nameEnglish.isEmpty() )
            return false;

        if( name ==null || name.isEmpty() )
            return false;

        if( name.equalsIgnoreCase( nameEnglish ) )
            return false;

        return true;
    }

    
    public ProductType getProductType()
    {
        if( productTypeId <= 0 )
            return null;

        return ProductType.getValue( productTypeId );
    }

    
    
    public ConsumerProductType getConsumerProductType()
    {
        return ConsumerProductType.getValue( this.consumerProductTypeId );
    }

    
    public List<NVPair> getReportFlagList()
    {
        List<NVPair> out = new ArrayList<>();

        if( strParam11==null || strParam11.isEmpty() )
            return out;

        StringTokenizer st = new StringTokenizer( strParam11, "|" );

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
    
    
    
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
        this.productTypeId = productTypeId;
    }

    public int getProductOrderTypeId() {
        return productOrderTypeId;
    }

    public void setProductOrderTypeId(int productOrderTypeId) {
        this.productOrderTypeId = productOrderTypeId;
    }

    public int getProductStatusTypeId() {
        return productStatusTypeId;
    }

    public void setProductStatusTypeId(int productStatusTypeId) {
        this.productStatusTypeId = productStatusTypeId;
    }

    public int getCreditUsageTypeId() {
        return creditUsageTypeId;
    }

    public void setCreditUsageTypeId(int creditUsageTypeId) {
        this.creditUsageTypeId = creditUsageTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }

    public String getLangStr() {
        return langStr;
    }

    public void setLangStr(String langStr) {
        this.langStr = langStr;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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

    public String getStrParam7() {
        return strParam7;
    }

    public void setStrParam7(String strParam7) {
        this.strParam7 = strParam7;
    }

    public String getStrParam8() {
        return strParam8;
    }

    public void setStrParam8(String strParam8) {
        this.strParam8 = strParam8;
    }

    public String getStrParam9() {
        return strParam9;
    }

    public void setStrParam9(String strParam9) {
        this.strParam9 = strParam9;
    }

    public String getStrParam10() {
        return strParam10;
    }

    public void setStrParam10(String strParam10) {
        this.strParam10 = strParam10;
    }

    public String getStrParam11() {
        return strParam11;
    }

    public void setStrParam11(String strParam11) {
        this.strParam11 = strParam11;
    }

    public String getStrParam12() {
        return strParam12;
    }

    public void setStrParam12(String strParam12) {
        this.strParam12 = strParam12;
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

    public int getIntParam16() {
        return intParam16;
    }

    public void setIntParam16(int intParam16) {
        this.intParam16 = intParam16;
    }

    public int getIntParam17() {
        return intParam17;
    }

    public void setIntParam17(int intParam17) {
        this.intParam17 = intParam17;
    }

    public int getIntParam18() {
        return intParam18;
    }

    public void setIntParam18(int intParam18) {
        this.intParam18 = intParam18;
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

    public int getSimContextTypeId() {
        return simContextTypeId;
    }

    public void setSimContextTypeId(int simContextTypeId) {
        this.simContextTypeId = simContextTypeId;
    }

    public String getOtherSimContextType() {
        return otherSimContextType;
    }

    public void setOtherSimContextType(String otherSimContextType) {
        this.otherSimContextType = otherSimContextType;
    }

    public String getOnetSoc() {
        return onetSoc;
    }

    public void setOnetSoc(String onetSoc) {
        this.onetSoc = onetSoc;
    }

    public String getOnetVersion() {
        return onetVersion;
    }

    public void setOnetVersion(String onetVersion) {
        this.onetVersion = onetVersion;
    }

    public long getSimDescriptorId() {
        return simDescriptorId;
    }

    public void setSimDescriptorId(long simDescriptorId) {
        this.simDescriptorId = simDescriptorId;
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

    public long getLongParam3() {
        return longParam3;
    }

    public void setLongParam3(long longParam3) {
        this.longParam3 = longParam3;
    }

    public long getLongParam4() {
        return longParam4;
    }

    public void setLongParam4(long longParam4) {
        this.longParam4 = longParam4;
    }

    public long getLongParam5() {
        return longParam5;
    }

    public void setLongParam5(long longParam5) {
        this.longParam5 = longParam5;
    }

    public int getConsumerProductTypeId() {
        return consumerProductTypeId;
    }

    public void setConsumerProductTypeId(int consumerProductTypeId) {
        this.consumerProductTypeId = consumerProductTypeId;
    }

    public String getIconFilename() {
        return iconFilename;
    }

    public void setIconFilename(String iconFilename) {
        this.iconFilename = iconFilename;
    }

    public String getDetailView() {
        return detailView;
    }

    public void setDetailView(String detailView) {
        this.detailView = detailView;
    }

    public Battery getBattery() {
        return battery;
    }

    public void setBattery(Battery battery) {
        this.battery = battery;
    }



}
