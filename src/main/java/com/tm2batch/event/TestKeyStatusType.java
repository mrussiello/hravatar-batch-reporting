package com.tm2batch.event;




public enum TestKeyStatusType
{
    ACTIVE(0,"Never Accessed"),
    STARTED(1,"Started"),
    STOPPED_PROCTOR(90,"Stopped By Proctor"),
    COMPLETED(100,"Completed"),
    COMPLETED_PENDING_EXTERNAL(101,"Completed - Pending External Scoring"),
    SCORING_STARTED(105,"Scoring Started"),
    SCORED(110,"Scored"),
    REPORTS_STARTED(111,"Reports Started"),
    REPORTS_COMPLETE(120,"Reports Complete"),
    DISTRIBUTION_STARTED(129,"Distribution Started"),
    DISTRIBUTION_COMPLETE(130,"Distribution Complete"),
    DISTRIBUTION_ERROR(131,"Distribution Error" ),
    EXPIRED(201,"Expired"),
    DEACTIVATED(202,"Deactivated"),
    SCORE_ERROR(203,"Scoring Error" ),
    REPORT_ERROR(204,"Report Error" );

    private final int testKeyStatusTypeId;

    private String key;

    public boolean getIsActive()
    {
        return equals( ACTIVE );
    }

    public boolean getIsCompleteOrHigher()
    {
        return testKeyStatusTypeId >= COMPLETED.getTestKeyStatusTypeId();
    }
    

    public String getName()
    {
        return key;
    }
    
    public String getKey()
    {
        return key;
    }

    private TestKeyStatusType( int p , String key )
    {
        this.testKeyStatusTypeId = p;

        this.key = key;
    }


    public boolean getIsProctorStop()
    {
        return equals(STOPPED_PROCTOR);
    }
    
    public boolean getIsCompletedOrHigher()
    {
        return testKeyStatusTypeId>= COMPLETED.testKeyStatusTypeId;
    }

    public boolean getIsReportsComplete()
    {
        return testKeyStatusTypeId>= REPORTS_COMPLETE.testKeyStatusTypeId;
    }

    public boolean getCanBeDeactivated()
    {
        return !getIsExpired() && !getIsFinishedByUser() && !getIsDeactivated();
    }
    
    public boolean getInStartedStatus()
    {
        return equals( STARTED );
    }
    
    public boolean getIsStartedOrHigher()
    {
        return this.testKeyStatusTypeId>=1;
    }
    
    public boolean getStarted()
    {
        return testKeyStatusTypeId>0;
    }

    public boolean getIsFinishedByUser()
    {
        return testKeyStatusTypeId>=COMPLETED.getTestKeyStatusTypeId() && testKeyStatusTypeId<DISTRIBUTION_ERROR.getTestKeyStatusTypeId();
    }

    public boolean getIsDistributionError()
    {
        return testKeyStatusTypeId==DISTRIBUTION_ERROR.getTestKeyStatusTypeId();
    }

    public boolean getIsReportError()
    {
        return testKeyStatusTypeId==REPORT_ERROR.getTestKeyStatusTypeId();
    }

    public boolean getIsScoreErrorError()
    {
        return testKeyStatusTypeId==SCORE_ERROR.getTestKeyStatusTypeId();
    }

    public boolean getIsExpired()
    {
        return testKeyStatusTypeId==EXPIRED.getTestKeyStatusTypeId();
    }

    public boolean getIsDeactivated()
    {
        return testKeyStatusTypeId==DEACTIVATED.getTestKeyStatusTypeId();
    }
    

    public int getTestKeyStatusTypeId()
    {
        return this.testKeyStatusTypeId;
    }


    public static TestKeyStatusType getValue( int id )
    {
        TestKeyStatusType[] vals = TestKeyStatusType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getTestKeyStatusTypeId() == id )
                return vals[i];
        }

        return ACTIVE;
    }

}
