package com.tm2batch.autoreport;

import com.tm2batch.util.MessageFactory;
import java.util.Locale;


public enum BatchReportContentType
{
    STD_TEST(0,"Std Test Results","brctn.StdTest", "com.tm2batch.custom.result.StandardResultsReport" ),
    STD_ACTIVITY(1,"Std Activity","brctn.StdActivity", "com.tm2batch.custom.activity.StandardActivityReport" ),
    STD_LIVEVIDEO(10,"Std Live Video","brctn.StdLvi", "com.tm2batch.custom.lvi.StandardLiveVideoReport" ),
    STD_REFCHECK(20,"Std Ref Check","brctn.StdRc", "com.tm2batch.custom.ref.StandardRefCheckReport" ),
    STD_REFCHECK_REFERRALS(30,"Std Ref Check Referrals","brctn.StdRcReferrals", "com.tm2batch.custom.ref.StandardRefCheckReferralReport" );

    private final int batchReportContentTypeId;

    private String name;
    private String className;
    private String key;

    private BatchReportContentType( int p , String name, String key, String className )
    {
        this.batchReportContentTypeId = p;
        this.name = name;
        this.key=key;
        this.className=className;
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
