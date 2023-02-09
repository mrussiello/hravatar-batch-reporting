package com.tm2batch.ref;

import com.tm2batch.util.MessageFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import jakarta.faces.model.SelectItem;



/**
 * @author miker_000
 */
public enum RcItemFormatType
{
    NONE(0,"Not Set", "rcift.none" ),
    RATING(1,"Rating and/Or Comments", "rcift.rating" ),  // has comments and ratings. This is the standard question type.
    RADIO(2,"Radios and Comments", "rcift.radiospluscomments" ),  
    BUTTON(3,"Buttons Only - No Comments", "rcift.button" ),  
    MULTIPLE_CHECKBOX(4,"Multiple Checkboxes - No Comments", "rcift.multicheckbox" );  
    
    private final int rcItemFormatTypeId;

    private final String name;
    private String key;


    private RcItemFormatType( int s , String n, String k )
    {
        this.rcItemFormatTypeId = s;

        this.name = n;
        this.key = k;
    }
    
    public boolean getIsRating()
    {
        return equals(RATING);
    }
    public boolean getIsRadio()
    {
        return equals(RADIO);
    }
    

    public static RcItemFormatType getValue( int id )
    {
        RcItemFormatType[] vals = RcItemFormatType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getRcItemFormatTypeId() == id )
                return vals[i];
        }

        return NONE;
    }

    public boolean getHasInfoText()
    {
        return true;        
    }

    public boolean getHasQuestion()
    {
        return true;        
    }

    public boolean getHasCommentsOptional()
    {
        return equals(RATING) || equals(RADIO) || equals(MULTIPLE_CHECKBOX) ;        
    }

    public boolean getHasRatingOptional()
    {
        return equals(RATING);        
    }

    public boolean getHasAnchors()
    {
        return equals(RATING);        
    }

    public boolean getHasChoices()
    {
        return equals(RADIO) || equals(BUTTON) || equals(MULTIPLE_CHECKBOX);        
    }

    public boolean getHasChoicePoints()
    {
        return equals(RADIO) || equals(BUTTON) || equals(MULTIPLE_CHECKBOX);        
    }

    
    
    public int getRcItemFormatTypeId()
    {
        return rcItemFormatTypeId;
    }

    public String getName()
    {
        return name;
    }

    public String getName( Locale l )
    {
        if( l==null )
            l = Locale.US;
        
        return MessageFactory.getStringMessage(l, key );
    }
    
}
