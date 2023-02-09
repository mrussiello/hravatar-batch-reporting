package com.tm2batch.lvi;

public enum SpeechTextStatusType
{
    NOT_STARTED(0, "Not performed" ),
    COMPLETE(1, "Successful" ),
    REQUESTED(2, "Requested" ),
    NOT_REQUIRED(9, "Not required" ),
    ERROR_TEMPORARY(10, "Unsuccessful, Errored" ),
    ERROR_PERMANENT(11, "Unsuccessful, Too many errors" );

    private final int speechTextStatusTypeId;

    private String key;


    private SpeechTextStatusType( int p,
                         String key )
    {
        this.speechTextStatusTypeId = p;
        this.key = key;
    }

    public boolean isNotReadyYet()
    {
        return equals( NOT_STARTED ) || equals( REQUESTED ) || equals( ERROR_TEMPORARY );
    }
        
    public boolean isPermanentError()
    {
        return equals( ERROR_PERMANENT );                
    }
       
    public String getName()
    {
        return this.key;
    }

    public int getSpeechTextStatusTypeId() {
        return speechTextStatusTypeId;
    }
        
    public static SpeechTextStatusType getValue( int id )
    {
        SpeechTextStatusType[] vals = SpeechTextStatusType.values();

        for( int i = 0; i < vals.length; i++ )
        {
            if( vals[i].getSpeechTextStatusTypeId() == id )
                return vals[i];
        }

        return NOT_STARTED;
    }
      
}
