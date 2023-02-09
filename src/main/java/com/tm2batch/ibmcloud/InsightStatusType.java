package com.tm2batch.ibmcloud;

public enum InsightStatusType
{
    NOT_STARTED(0, "Not performed" ),
    COMPLETE(1, "Successful" ),
    REQUESTED(2, "Requested" ),
    NOT_REQUIRED(9, "Not required" ),
    ERROR_TEMPORARY(10, "Unsuccessful, Errored" ),
    ERROR_PERMANENT(11, "Unsuccessful, Too many errors" );

    private final int insightStatusTypeId;

    private String key;


    private InsightStatusType( int p,
                         String key )
    {
        this.insightStatusTypeId = p;
        this.key = key;
    }

    public boolean isNotReadyYet()
    {
        return equals( NOT_STARTED) || equals( REQUESTED ) || equals( ERROR_TEMPORARY );
    }
        
    
    public boolean isCompleteOrPermanentError()
    {
        return equals(COMPLETE) || isPermanentError();                
    }
    
    public boolean isNotRequired()
    {
        return equals(NOT_REQUIRED);           
    }
    
    public boolean isComplete()
    {
        return equals(COMPLETE);                
    }

    public boolean isTempError()
    {
        return equals(ERROR_TEMPORARY);                
    }
    
    
    public boolean isAnyError()
    {
        return equals(ERROR_TEMPORARY) || equals( ERROR_PERMANENT );                
    }
    
    public boolean isPermanentError()
    {
        return equals( ERROR_PERMANENT );                
    }
    
    public boolean isNotStarted()
    {
        return equals(NOT_STARTED);                
    }
    
    public boolean isRequested()
    {
        return equals(REQUESTED);                
    }
    
   
    public String getName()
    {
        return this.key;
    }

    public int getInsightStatusTypeId() {
        return insightStatusTypeId;
    }
    
    public static InsightStatusType getValue( int id )
    {
        InsightStatusType[] vals = InsightStatusType.values();

        for( int i = 0; i < vals.length; i++ )
        {
            if( vals[i].getInsightStatusTypeId() == id )
                return vals[i];
        }

        return NOT_STARTED;
    }
      
}
