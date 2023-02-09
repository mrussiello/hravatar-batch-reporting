package com.tm2batch.autoreport;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;



public enum FrequencyType
{
    NEVER(0,"Never",0,0,0,0 ),
    ANNUAL(1,"Annual",1,0,0,0 ),
    QUARTERLY(5,"Quarterly",0,3,0,0 ),
    MONTHLY(10,"Monthly",0,1,0,0),
    BIWEEKLY(15,"Bi-Weekly",0,0,2,0),
    WEEKLY(20,"Weekly",0,0,1,0),
    DAILY(25,"Daily",0,0,0,1);

    private final int frequencyTypeId;

    private String name;
    private final int years;
    private final int months;
    private final int days;
    private final int weeks;

    private FrequencyType( int p , String key, int y, int m, int w, int d )
    {
        this.frequencyTypeId = p;

        this.name = key;
        this.years=y;
        this.months=m;
        this.weeks=w;
        this.days=d;
    }

    
    public boolean isTodayOkToSend( TimeZone tz)
    {
        if( equals(NEVER) )
            return false;
        
        Calendar cal = new GregorianCalendar( tz );
        int dayOfMonth = cal.get( Calendar.DAY_OF_MONTH );
        int monthOfYear = cal.get(Calendar.MONTH);
        int weekOfYear = cal.get( Calendar.WEEK_OF_YEAR );
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        
        // annuals are only sent on the first day of the year.
        if( equals(ANNUAL) )
            return monthOfYear==0 && dayOfMonth==1;
        
        if( equals(QUARTERLY) )
            return (monthOfYear+1) % 3 == 1;

        if( equals(MONTHLY) )
            return dayOfMonth == 1;
        
        if( equals(BIWEEKLY) )
            return weekOfYear%2 == 0 && dayOfWeek == Calendar.SUNDAY;

        if( equals(WEEKLY) )
            return dayOfWeek == Calendar.SUNDAY;
        
        return true;
    }
    
    public Date getCutoffDate()
    {
        Calendar cal = new GregorianCalendar();
        
        if( equals( NEVER) )
            return cal.getTime();
        
        if( years>0 )
            cal.add( Calendar.YEAR, -1*years );

        if( months>0 )
            cal.add( Calendar.MONTH, -1*months );
        
        if( weeks>0 )
            cal.add( Calendar.WEEK_OF_YEAR, -1*weeks );

        if( days>0 )
            cal.add( Calendar.DAY_OF_MONTH, -1*days );
                
        cal.add( Calendar.HOUR_OF_DAY, 2 );
        
        return cal.getTime();
    }

    public int getFrequencyTypeId()
    {
        return this.frequencyTypeId;
    }



    public static FrequencyType getType( int typeId )
    {
        return getValue( typeId );
    }

    public String getName()
    {
        return name;
    }


    public static FrequencyType getValue( int id )
    {
        FrequencyType[] vals = FrequencyType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getFrequencyTypeId() == id )
                return vals[i];
        }

        return NEVER;
    }

}
