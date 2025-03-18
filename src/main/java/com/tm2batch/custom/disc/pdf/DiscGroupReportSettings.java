/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.disc.pdf;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tm2batch.pdf.ITextUtils;
import com.tm2batch.pdf.ReportData;
import com.tm2batch.pdf.ReportSettings;
import com.tm2batch.pdf.StandardReportSettings;
import com.tm2batch.service.LogService;
import com.tm2batch.util.I18nUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

/**
 *
 * @author Mike
 */
public abstract class DiscGroupReportSettings extends StandardReportSettings implements ReportSettings {

    //public static Font 
    
    public static int MIN_HEIGHT_FOR_CONTINUED_TEXT = 45;

    public static int MAX_INTERVIEWQS_PER_COMPETENCY = 10;
    public static int MAX_TABLE_CELL_HEIGHT = 5;


    public static float MAX_CUSTLOGO_W_V2 = 110; // 80
    public static float MAX_CUSTLOGO_H_V2 = 60;  // 40
    
    public DiscColors ct2Colors = null;
    public boolean devel = false;
    // public boolean redYellowGreenGraphs=true;
    public String coverDescrip = null;
    
    public Image custLogo = null;
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // NOTE: NONE OF THESE ARE PUBLIC SO THAT OTHER REPORTS CAN OVERRIDE THEM FOR THAT REPORT but still extend from this class.
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public float scoreBoxBorderWidth = 0.8f;    
    public float lightBoxBorderWidth=0.75f;
         
    public boolean rtl = false;
    public boolean usesNonAscii = false;
    
    public static String[] sideTabIconUris = new String[] {
        "https://s3.amazonaws.com/cfmedia-hravatar-com/web/orgimage/zrWvh1uNWrg-/img_3x1742049764524.png",
        "https://s3.amazonaws.com/cfmedia-hravatar-com/web/orgimage/zrWvh1uNWrg-/img_5x1742049764529.png",
        "https://s3.amazonaws.com/cfmedia-hravatar-com/web/orgimage/zrWvh1uNWrg-/img_4x1742049764527.png",
        "https://s3.amazonaws.com/cfmedia-hravatar-com/web/orgimage/zrWvh1uNWrg-/img_2x1742049764524.png"
    };

    

    
    @Override
    public void initSettings(ReportData reportData) throws Exception 
    {
        super.initSettings(reportData);
        
        rtl = I18nUtils.isTextRTL( reportData==null ? Locale.US :  reportData.getLocale() );

        hraLogoBlackText = reportData==null ? null : ITextUtils.getITextImage(reportData.getHRALogoBlackTextUrl( devel ));
        hraLogoWhiteText = reportData==null ? null : ITextUtils.getITextImage(reportData.getHRALogoWhiteTextUrl( devel ));
        hraLogoBlackTextSmall = reportData==null ? null : ITextUtils.getITextImage(reportData.getHRALogoBlackTextSmallUrl( devel ));

        // LogService.logIt( "CT2ReportSettings.initSettings() devel=" + devel + ", url=" + reportData.getHRALogoWhiteTextSmallUrl( devel ) );

        hraLogoWhiteTextSmall = reportData==null ? null : ITextUtils.getITextImage(reportData.getHRALogoWhiteTextSmallUrl( devel ));           
        hraCoverPageImage = reportData==null ? null : ITextUtils.getITextImage(reportData.getHRACoverPageUrl() );                                               
        hraCoverPageBlueArrowImage = reportData==null ? null : ITextUtils.getITextImage(reportData.getHRACoverPageBlueArrowUrl() );
            
        float highresscale = 100 * 72 / 300;

        // float whiteAdj = 0.5f;
        hraLogoBlackText.scalePercent(highresscale);
        hraLogoWhiteText.scalePercent(highresscale);
        hraLogoBlackTextSmall.scalePercent(highresscale);
        hraLogoWhiteTextSmall.scalePercent(highresscale );
    }
    
        

    public boolean isValidForTestEvent()
    {
        return true;
    }
    
    public void initColors()
    {
        // Nothing. 
        if( ct2Colors == null )
            ct2Colors = DiscColors.getCt2Colors( devel );  
                
    }
    
    
    
    
    public boolean isOkToAutoTranslate( Locale testContentLoc, Locale rptLoc )
    {
        if( 1==2 )
            return false;
        
        if( testContentLoc==null || rptLoc==null )
            return false;
        
        if( testContentLoc.getLanguage().equalsIgnoreCase(rptLoc.getLanguage() ) )
            return false;
        
        return isOkToAutoTranslate( testContentLoc ) && isOkToAutoTranslate( rptLoc );
    }
    
    public boolean isOkToAutoTranslate( Locale loc )
    {
        
        if( loc==null )
            return false;
        
        String ln = loc.getLanguage();
        
        if( ln.equalsIgnoreCase("en") )
            return true;
        
        if( ln.equalsIgnoreCase("es") )
            return true;
        
        if( ln.equalsIgnoreCase("de") )
            return true;
        
        if( ln.equalsIgnoreCase("fr") )
            return true;
        
        return false;        
    }  
    
    public boolean getIsRTL()
    {
        return rtl;
    }

    
    
    
    public Image getImageInstance( String thumbUrl, long testEventId )
    {
        try
        {
            // do this to prevent AWS S3 Slow Down Errors.
            Thread.sleep(10);

            return ITextUtils.getITextImage(com.tm2batch.util.HttpUtils.getURLFromString( thumbUrl ) );
        }
        catch( FileNotFoundException e )
        {
            LogService.logIt( "CT2ReportSettings.getImageInstance() XXX.1 ERROR " + e.toString() + ", thumbUrl=" + thumbUrl + ", testEventId=" + testEventId );
            return null;
        }
        catch( IOException | com.itextpdf.text.BadElementException  e )
        {
            LogService.logIt( "CT2ReportSettings.getImageInstance() XXX.2 ERROR " + e.toString() + ", thumbUrl=" + thumbUrl + ", testEventId=" + testEventId );
            return null;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "CT2ReportSettings.getImageInstance() XXX.3 ERROR thumbUrl=" + thumbUrl + ", testEventId=" + testEventId );
            return null;
        }
    }
    
        
    
    
    public void setRunDirection( ReportData reportData, PdfPCell c )
    {
        if( c == null || reportData == null || reportData.getLocale() == null )
            return;

        if( I18nUtils.isTextRTL( reportData.getLocale() ) )
            c.setRunDirection( PdfWriter.RUN_DIRECTION_RTL );
    }

    public void setRunDirection( ReportData reportData, PdfPTable t )
    {
        if( t == null || reportData == null || reportData.getLocale() == null )
            return;

        if( I18nUtils.isTextRTL( reportData.getLocale() ) )
            t.setRunDirection( PdfWriter.RUN_DIRECTION_RTL );
    }
}
