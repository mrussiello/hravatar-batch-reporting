/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.score;


import com.tm2batch.entity.event.TestEventResponseRating;
import com.tm2batch.event.TestEventResponseRatingUtils;
import com.tm2batch.service.LogService;
import com.tm2batch.util.I18nUtils;
import com.tm2batch.util.StringUtils;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Mike
 */
public class TextAndTitle implements Serializable, Comparable<TextAndTitle>
{
    private String text;
    private String title;
    private int order = 0;
    private boolean redFlag = false;
    
    
    /**
     * For uploaded user files, this is the uploadedUserFileId
     * For audio samples (without an uploaded user files, IVR tests only) this is the AVItemResponseId
     * For ScoredEssays, this is the simCompetencyId.
     * 
     */
    private long uploadedUserFileId = 0;
    private String rowStyleClass = null;
    private String string1 = null;
    private String string2;

    private int intParam1 = 0;
    private String strParam1;

    private List<TestEventResponseRating> testEventResponseRatingList;
    private Locale locale;


    //public TextAndTitle( String text, String title, long  uploadedUserFileId)
    //{
    //    this.text = text;
    //    this.title = title;
    //    this.uploadedUserFileId = uploadedUserFileId;
    //}

    //public TextAndTitle( String text, String title, boolean rf, long  uploadedUserFileId)
    //{
    //    this.text = text;
    //    this.title = title;
    //    this.redFlag = rf;
    //    this.uploadedUserFileId = uploadedUserFileId;
    //}
    public TextAndTitle( String text, String title )
    {
        this.text = text;
        this.title = title;        
    }

    public TextAndTitle( String text, String title, boolean rf, long  uploadedUserFileId, String string1, String string2 )
    {
        this.text = text;
        this.title = title;
        this.redFlag = rf;
        this.uploadedUserFileId = uploadedUserFileId;
        this.string1 = string1;
        this.string2 = string2;
    }

    @Override
    public String toString() {
        return "TextAndTitle{" + "text=" + text + ", title=" + title + ", order=" + order + ", redFlag=" + redFlag + ", uploadedUserFileId=" + uploadedUserFileId + ", string1=" + string1 + '}';
    }





    @Override
    public int compareTo(TextAndTitle o)
    {
        if( o.getOrder() != 0 && order != 0 )
            return new Integer( order ).compareTo( o.getOrder() );

        if( title != null && o.getTitle() != null )
            return title.compareTo( o.getTitle() );

        return 0;
    }

    public String getTextXhtml()
    {
        return StringUtils.replaceStandardEntities( text );
    }

    public String getTitleXhtml()
    {
        return StringUtils.replaceStandardEntities( title );
    }

    public boolean isValidForReport()
    {
        if( title == null )
            title = "";

        return text != null && !text.isEmpty();
    }

    public boolean getHasString1()
    {
        return string1!=null && !string1.isEmpty();
    }
    
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getRedFlag() {
        return redFlag;
    }

    public void setRedFlag(boolean redFlag) {
        this.redFlag = redFlag;
    }

    public String getFlags()
    {
        String f = "";

        if( redFlag )
            f += "red:1";

        if( uploadedUserFileId>0 )
        {
            if( !f.isEmpty() )
                f +=",";

            f += "uuf:" + uploadedUserFileId;
        }

        return f;
    }

    public void setFlags( String flags )
    {
        if( flags == null || flags.isEmpty() )
            return;

        if( flags.indexOf( "red:1" )>= 0 )
            redFlag = true;
        
        if( flags.indexOf( "uuf:" )>= 0 )
        {
            int idx = flags.indexOf(",", flags.indexOf( "uuf:" )+4 );
            
            String t = flags.substring( flags.indexOf( "uuf:" )+4 , idx>0 ? idx : flags.length() );
            
            if( t.length()> 0 )
            {
                try
                {
                    uploadedUserFileId = Long.parseLong(t);
                }
                catch( NumberFormatException e )
                {
                    LogService.logIt(e, "TextAndTitle.setFlags() unable to parse uploadedUserFileId flags=" + flags );
                }
            }
        }        
    }

    
    
    
    public long getUploadedUserFileId() {
        return uploadedUserFileId;
    }

    public void setUploadedUserFileId(long uploadedUserFileId) {
        this.uploadedUserFileId = uploadedUserFileId;
    }

    public String getRowStyleClass() {
        return rowStyleClass;
    }

    public void setRowStyleClass(String rowStyleClass) {
        this.rowStyleClass = rowStyleClass;
    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    public String getString1Xhtml()
    {
        return StringUtils.replaceStandardEntities( string1 );
    }

    public int getIntParam1() {
        return intParam1;
    }

    public void setIntParam1(int intParam1) {
        this.intParam1 = intParam1;
    }

    public boolean getHasStrParam1()
    {
        return strParam1!=null && !strParam1.trim().isEmpty();
    }
    
    public String getStrParam1() {
        return strParam1;
    }

    public void setStrParam1(String stringParam1) {
        this.strParam1 = stringParam1;
    }

    public List<TestEventResponseRating> getTestEventResponseRatingList() {
        return testEventResponseRatingList;
    }

    public void setTestEventResponseRatingList(List<TestEventResponseRating> testEventResponseRatingList) {
        this.testEventResponseRatingList = testEventResponseRatingList;
    }

    public boolean getHasResponseRatings()
    {
        return testEventResponseRatingList!=null && !testEventResponseRatingList.isEmpty();
    }
    
    public String[] getAverageResponseRatings()
    {
        float[] vals = TestEventResponseRatingUtils.getAverageRating( testEventResponseRatingList );

        return new String[] { I18nUtils.getFormattedNumber(locale, vals[0], 1),I18nUtils.getFormattedNumber(locale, vals[1], 1),I18nUtils.getFormattedNumber(locale, vals[2], 1)};
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    
    public boolean getHasResponseRatingNotes()
    {
        if( !getHasResponseRatings() )
            return false;
        
        for( TestEventResponseRating t : testEventResponseRatingList )
        {
            if( t.getNote()!=null && !t.getNote().isBlank() ) //  t.getHasNote() )
                return true;
        }
        return false;
    }
    
    public int getRatingCols()
    {
        if( !getHasResponseRatings() )
            return 0;
        return TestEventResponseRatingUtils.getRatingCols( testEventResponseRatingList );
    }

    public String getString2() {
        return string2;
    }
    
    

}
