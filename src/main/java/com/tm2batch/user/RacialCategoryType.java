package com.tm2batch.user;
import com.tm2batch.service.LogService;
import com.tm2batch.util.MessageFactory;

import java.util.Locale;


public enum RacialCategoryType
{
    WHITE(1,"rcat.white"),
    BLACK(2, "rcat.black" ),
    ASIAN(3, "rcat.asian" ),
    AMERINDIAN(4, "rcat.amerindian" ),
    PACIFIC(5, "rcat.pacific" ),
    OTHER(6, "rcat.other" );

    private int racialCategoryTypeId;

    private String key;

    private RacialCategoryType( int typeId , String key )
    {
        this.racialCategoryTypeId = typeId;

        this.key = key;
    }

    public String getName( Locale locale )
    {
        if( locale==null )
            locale= Locale.US;
        return MessageFactory.getStringMessage( locale, key , null );
    }

    public int getRacialCategoryTypeId()
    {
        return racialCategoryTypeId;
    }
    
    public static String getRacialCategoryStr( String types, Locale locale )
    {
        String out = "";
        
        if( types==null || types.isBlank() )
            return out;
        
        if( locale==null )
            locale= Locale.US;
        int t;
        RacialCategoryType rct;
        
        for( String s : types.split(",") )
        {
            try
            {
                if( s.isBlank() )
                    continue;
                t = Integer.parseInt(s.trim());
                rct = RacialCategoryType.getValue(t);
                if( rct!=null )
                {
                    if( !out.isBlank() )
                        out += ", ";
                    out += rct.getName(locale);
                }
            }
            catch(NumberFormatException e )
            {
                LogService.logIt(e,"RacialCategoryType.getRacialCategoryStr() " + types );
            }
        }
        
        return out;
    }
    
    public static RacialCategoryType getValue( int id )
    {
        RacialCategoryType[] vals = RacialCategoryType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getRacialCategoryTypeId() == id )
                return vals[i];
        }
        
        return null;
    }
    

    public String getKey()
    {
        return key;
    }
}
