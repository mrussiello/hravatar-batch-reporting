package com.tm2batch.user;


public enum OrgCreditUsageType
{
    CREDIT(0, "Uses Legacy Credits"),
    RESULT_BASIC_PROC(1, "Pay-Per-Candidate - Basic Online Proctoring "),
    RESULT_NOPROCTORING(2, "Pay-Per-Candidate - No Online Proctoring"),
    RESULT_PREMIUM_PROC(3, "Pay-Per-Candidate - Premium Online Proctoring"),
    UNLIMITED_FULL(10, "Unlimited");

    private int orgCreditUsageTypeId;

    private String key;

    private OrgCreditUsageType( int typeId, String k )
    {
        orgCreditUsageTypeId = typeId;
        key = k;
    }

    
    public boolean getUsesLegacyCredits()
    {
        return equals( CREDIT );
    }

    public boolean getUnlimited()
    {
        return equals( UNLIMITED_FULL );
    }

    public boolean getResultCreditNoProctoring()
    {
        return equals(RESULT_NOPROCTORING);
    }
    
    public boolean getResultCreditProctoring()
    {
        return equals(RESULT_BASIC_PROC);
    }
    
    public boolean getAnyResultCredit()
    {
        return equals(RESULT_BASIC_PROC ) || equals(RESULT_NOPROCTORING ) || equals(RESULT_PREMIUM_PROC);
    }

    public boolean getAnyResultCreditOrUnlimited()
    {
        return getAnyResultCredit() || getUnlimited();
    }
    public int getOrgCreditUsageTypeId()
    {
        return orgCreditUsageTypeId;
    }

    public String getName()
    {
       return key;
    }

    public static OrgCreditUsageType getValue( int id )
    {
        OrgCreditUsageType[] vals = OrgCreditUsageType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getOrgCreditUsageTypeId() == id )
                return vals[i];
        }
        
        return CREDIT;
    }


    public String getKey()
    {
        return key;
    }
}
