package com.tm2batch.file;

public enum ConversionStatusType
{
    NA(0,"conversionstatustype.na"),
    NOT_STARTED(1,"conversionstatustype.notstarted"),
    MARKED_FOR_CANCELLATION(9,"conversionstatustype.markedcanceled"),
    FAILED(10,"conversionstatustype.failed"),
    CANCELED(11,"conversionstatustype.canceled"),
    STARTED(100,"conversionstatustype.started"),
    SENT_TO_AMAZON(110,"conversionstatustype.senttoamazon"),
    RETURNED_FROM_AMAZON(120,"conversionstatustype.returnedfromamazon"),
    ERROR_FROM_AMAZON(130,"conversionstatustype.errorfromamazon"),
    PHASE1COMPLETE(200,"conversionstatustype.phase1complete"),
    PHASE2COMPLETE(300,"conversionstatustype.phase2complete"),
    PHASE3COMPLETE(400,"conversionstatustype.phase3complete"),
    PHASE4COMPLETE(500,"conversionstatustype.phase4complete"),
    NEEDSMEDIAOBJECTUPDATE(600,"conversionstatustype.needsmediaobjectupdate"),
    COMPLETE(1000,"conversionstatustype.complete"),
    POSTFAIL(1001,"conversionstatustype.failed_updated"),
    POSTCANCEL(1002,"conversionstatustype.canceled_updated"),
    COMPLETE_ORIENTATIONSET(1003,"conversionstatustype.completeoritentationset");


    private final int conversionStatusTypeId;

    private String key;


    private ConversionStatusType( int p , String key )
    {
        this.conversionStatusTypeId = p;

        this.key = key;
    }

    public boolean getIsCompleteOrError()
    {
        return getIsCompleteOrNotRequired() || getIsError();
    }
    
    
    public boolean getIsComplete()
    {
        return equals( COMPLETE ) || equals( COMPLETE_ORIENTATIONSET );
    }
    
    public boolean getIsError()
    {
        return equals( FAILED ) || equals( POSTFAIL );
    }
    
    public boolean getIsSourceFileNeeded()
    {
        return needsInitialConversion() || readyForSpeech2TextConversion() || readyForVideoProcConversion();
    }
    
    public boolean readyForAvItemResponse()
    {
        return equals( PHASE3COMPLETE );
    }
    
    public boolean readyForSpeech2TextConversion()
    {
        return equals( PHASE1COMPLETE );
    }
    
    public boolean readyForVideoProcConversion()
    {
        return equals( PHASE2COMPLETE );
    }
    
    
    public boolean needsInitialConversion()
    {
        return equals( NOT_STARTED ) || equals( STARTED );
    }
    
    public boolean getIsCompleteOrNotRequired()
    {
        return equals( NA ) || conversionStatusTypeId >= 1000;
    }
    



    public int getConversionStatusTypeId()
    {
        return this.conversionStatusTypeId;
    }



    public static ConversionStatusType getType( int typeId )
    {
        return getValue( typeId );
    }


    public String getKey()
    {
        return key;
    }



    public static ConversionStatusType getValue( int id )
    {
        ConversionStatusType[] vals = ConversionStatusType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getConversionStatusTypeId() == id )
                return vals[i];
        }

        return NOT_STARTED;
    }

}
