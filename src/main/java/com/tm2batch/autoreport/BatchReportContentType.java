package com.tm2batch.autoreport;

import com.tm2batch.util.MessageFactory;
import java.util.Locale;


public enum BatchReportContentType
{
    STD_TEST(0,"Std Test Results","brctn.StdTest", "com.tm2batch.custom.result.StandardResultsReport", true ),
    STD_ACTIVITY(1,"Std Activity","brctn.StdActivity", "com.tm2batch.custom.activity.StandardActivityReport", true ),
    STD_UNFINISHED_TK(2,"Std Unfinished TestKey","brctn.StdUnfinishedTestKey", "com.tm2batch.custom.testkey.StandardUnfinishedTestKeyReport", true ),
    STD_CREDIT_USAGE(3,"Std Credit Usage","brctn.StdCreditUsage", "com.tm2batch.custom.credit.StandardCreditUsageReport", true ),
    STD_LIVEVIDEO(10,"Std Live Video","brctn.StdLvi", "com.tm2batch.custom.lvi.StandardLiveVideoReport", true ),
    STD_REFCHECK(20,"Std Ref Check","brctn.StdRc", "com.tm2batch.custom.ref.StandardRefCheckReport", true ),
    STD_REFCHECK_REFERRALS(30,"Std Ref Check Referrals","brctn.StdRcReferrals", "com.tm2batch.custom.ref.StandardRefCheckReferralReport", true ),
    CUST_UMINNORGJUSTICE(60,"Uminn Org Justice","brctn.UminnOrgJustice", "com.tm2batch.custom.orgjustice.OrgJusticeReport", false ),
    DISC_GROUP(61,"DISC Group Report","brctn.DiscGroup", "com.tm2batch.custom.disc.DiscGroupReport", true );

    private final int batchReportContentTypeId;

    private final boolean standard;
    private final String name;
    private final String className;
    private final String key;

    private BatchReportContentType( int p , String name, String key, String className, boolean standard )
    {
        this.batchReportContentTypeId = p;
        this.name = name;
        this.key=key;
        this.className=className;
        this.standard=standard;
    }
    
    public boolean getIsTest()
    {
        return equals(STD_TEST );
    }

    public boolean getIsLiveVideo()
    {
        return equals(STD_LIVEVIDEO );
    }

    public boolean getIsRefCheck()
    {
        return equals(STD_REFCHECK);
    }

    public int getBatchReportContentTypeId()
    {
        return batchReportContentTypeId;
    }

    public String getClassName() {
        return className;
    }



    public static BatchReportContentType getType( int typeId )
    {
        return getValue( typeId );
    }

    public String getName( Locale loc )
    {
        if( loc==null )
            loc = Locale.US;
        
        return MessageFactory.getStringMessage( loc, key );
    }

    
    public String getName()
    {
        return getName(null);
    }
    
    public boolean getIsCreditUsage()
    {
        return equals(STD_CREDIT_USAGE);
    }

    public boolean getUsesSuborgs()
    {
        return equals(DISC_GROUP);
    }

    public boolean getUsesOrgAutoTest()
    {
        return equals(DISC_GROUP);
    }

    
    
    

    public static BatchReportContentType getValue( int id )
    {
        BatchReportContentType[] vals = BatchReportContentType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getBatchReportContentTypeId() == id )
                return vals[i];
        }

        return STD_TEST;
    }

}
