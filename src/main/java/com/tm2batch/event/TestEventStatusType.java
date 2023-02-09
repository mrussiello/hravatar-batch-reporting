package com.tm2batch.event;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


public enum TestEventStatusType
{
    ACTIVE(0,"Active"),
    STARTED(1,"Started"),
    COMPLETED(100,"Completed"),
    COMPLETED_PENDING_EXTERNAL_SCORES(101,"Pending External Scores"),
    SCORING_STARTED(105,"Scoring Started"),
    SCORED(110,"Scored"),
    REPORT_STARTED(111,"Reports Started"),
    REPORT_COMPLETE(120,"Report Complete"),
    // REPORT_COMPLETE_NONORMS(121,"Report Complete, Exclude From Norms"),
    EXPIRED(201,"Expired"),
    DEACTIVATED(202,"Deactivated"),
    SCORE_ERROR(203,"Scoring Error" ),
    REPORT_ERROR(204,"Report Error" );

    private final int testEventStatusTypeId;

    private String key;


    private TestEventStatusType( int p , String key )
    {
        this.testEventStatusTypeId = p;

        this.key = key;
    }

    public boolean getIsTestScored()
    {
        return (testEventStatusTypeId >= SCORED.getTestEventStatusTypeId() &&
                testEventStatusTypeId <= REPORT_COMPLETE.getTestEventStatusTypeId()) ||
                testEventStatusTypeId==REPORT_ERROR.testEventStatusTypeId;
    }

    public boolean getIsTestCompletedOrHigher()
    {
        return testEventStatusTypeId >= COMPLETED.getTestEventStatusTypeId();
    }
    
    
    public boolean getIsTestCompleted()
    {
        return (testEventStatusTypeId >= COMPLETED.getTestEventStatusTypeId() &&
                testEventStatusTypeId <= REPORT_COMPLETE.getTestEventStatusTypeId()) ||
                testEventStatusTypeId==SCORE_ERROR.testEventStatusTypeId ||
                testEventStatusTypeId==REPORT_ERROR.testEventStatusTypeId;
    }
    public boolean getIsScoreInProgress()
    {
        return equals( COMPLETED ) ||
        equals( COMPLETED_PENDING_EXTERNAL_SCORES ) ||
        equals( SCORING_STARTED ) || equals( SCORED ) ||
        equals( REPORT_STARTED );
    }

    public boolean getIsReportComplete()
    {
        return equals( REPORT_COMPLETE );
    }

    public boolean getIsScoreCompleteOrHigher()
    {
        return equals( SCORED ) || equals( REPORT_STARTED ) || equals( REPORT_COMPLETE ) || equals( REPORT_ERROR );
    }


    public int getTestEventStatusTypeId()
    {
        return this.testEventStatusTypeId;
    }



    public String getName()
    {
        return key;
    }




    public static TestEventStatusType getType( int typeId )
    {
        return getValue( typeId );
    }



    public String getKey()
    {
        return key;
    }



    public static TestEventStatusType getValue( int id )
    {
        TestEventStatusType[] vals = TestEventStatusType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getTestEventStatusTypeId() == id )
                return vals[i];
        }

        return ACTIVE;
    }

}
