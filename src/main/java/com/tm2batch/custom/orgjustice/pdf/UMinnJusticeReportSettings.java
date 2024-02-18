/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.orgjustice.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.tm2batch.account.results.ReportUtils;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.pdf.ReportData;
import com.tm2batch.pdf.ReportSettings;
import com.tm2batch.pdf.StandardReportSettings;
import com.tm2batch.service.LogService;
import com.tm2batch.util.MessageFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 *
 * @author Mike
 */
public abstract class UMinnJusticeReportSettings extends StandardReportSettings implements ReportSettings {

    // Map<String,String> customSettingsMap;

    public String logoUrl = RuntimeConstants.getStringValue("baseurl") + "/resources/images/uminn/orgjustice/uminn-logo.png";    
    public String headerLogoUrl = RuntimeConstants.getStringValue("baseurl") + "/resources/images/uminn/orgjustice/uminn-logo-m-trans.png";      
    public String headerLogoWhiteTransUrl = RuntimeConstants.getStringValue("baseurl") + "/resources/images/uminn/orgjustice/uminn-logo-m-trans.png";     
    public String figure1Url = RuntimeConstants.getStringValue("baseurl") + "/resources/images/uminn/orgjustice/orgjustice-figure-1.png";     
    public String figure2Url = RuntimeConstants.getStringValue("baseurl") + "/resources/images/uminn/orgjustice/orgjustice-figure-2.png";     
    public String figure3Url = RuntimeConstants.getStringValue("baseurl") + "/resources/images/uminn/orgjustice/orgjustice-figure-3.png";     
    public String scoreFigureUrl = RuntimeConstants.getStringValue("baseurl") + "/resources/images/uminn/orgjustice/orgjustice-score-figure.png";     
        
    
    // public String pcmUrl = "https://s3.amazonaws.com/cfmedia-hravatar-com/web/orgimage/Q8vQJ8K3q0E-/img_9x1481054844781.png";    

    
    // public String logoUrl = "https://s3.amazonaws.com/cfmedia-hravatar-com/web/orgimage/Q8vQJ8K3q0E-/img_8x1481054844094.png";    
    // public String pcmUrl = "https://s3.amazonaws.com/cfmedia-hravatar-com/web/orgimage/Q8vQJ8K3q0E-/img_9x1481054844781.png";    
    // public String headerLogoUrl = "https://s3.amazonaws.com/cfmedia-hravatar-com/web/orgimage/Q8vQJ8K3q0E-/img_12x1481102616782.png";     
    // public String headerLogoWhiteTransUrl = "https://s3.amazonaws.com/cfmedia-hravatar-com/web/orgimage/Q8vQJ8K3q0E-/img_12x1481102616782.png";    
    // public String twitterLogoUrl = "https://s3.amazonaws.com/cfmedia-hravatar-com/web/orgimage/Q8vQJ8K3q0E-/img_13x1481106795247.jpg";
    public String uminnMaroonColStr = "7a0019";        
    public BaseColor uminnMaroon = new BaseColor( 0x7a, 0x00, 0x19 );    
    public BaseColor uminnMaroonLite = new BaseColor( 0xf5, 0xb7, 0xc3 );  // f5b7c3
    
    Image custLogo;
    // Image twitterLogo;    
    Image headerLogo;
    
    public static Image figure1Image;
    public static Image figure2Image;
    public static Image figure3Image;
    public static Image scoreFigureImage;
    public static Image headerLogoWhiteTrans;

    public BaseFont baseFontBoldArial;
    
    public Font fontXLargeMaroon;
    public Font fontXLargeBoldMaroon;
    public Font fontXXLargeBoldMaroon;
    public Font headerFontXLargeMaroon;
    public Font headerFontLargeMaroon;
    public Font headerFontXXLargeMaroon;
    
    public Font titleHeaderFont;
    public Font titleHeaderFont2;
    
    public Font fontSmallLightItalicMaroon;

    public int XPLFONTSZ = 20;
    
    public ReportData reportData = null;
    
    private UMinnJusticeReportUtils uminnJusticeReportUtils;
    
    protected ReportUtils reportUtils;
    
     
    public abstract void initForSource();
    

    public void initExtra(ReportData reportData) throws Exception 
    {        
        initForSource();
        
        String filesRoot = RuntimeConstants.getStringValue("filesroot") + "/fonts/";        
        baseFontBoldArial = BaseFont.createFont(filesRoot + "arialbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        
        titleHeaderFont = new Font(baseFont, 22);
        titleHeaderFont.setColor(BaseColor.WHITE);

        titleHeaderFont2 = new Font(baseFontBoldArial, 20);
        titleHeaderFont2.setColor(BaseColor.WHITE);

        fontXLargeMaroon = new Font(baseFont, XPLFONTSZ);
        fontXLargeBoldMaroon = new Font(baseFontBold, XPLFONTSZ, Font.NORMAL);
        fontXXLargeBoldMaroon = new Font(baseFontBold, XXLFONTSZ, Font.NORMAL);

        headerFontLargeMaroon = new Font(headerBaseFont, LFONTSZ);
        headerFontXLargeMaroon = new Font(headerBaseFont, XPLFONTSZ);
        headerFontXXLargeMaroon = new Font(headerBaseFont, XXLFONTSZ);

        fontSmallLightItalicMaroon = new Font(baseFontItalic, XSFONTSZ);

        fontXLargeMaroon.setColor(uminnMaroon);
        fontXLargeBoldMaroon.setColor(uminnMaroon);



        headerFontLargeMaroon.setColor(uminnMaroon);
        headerFontXLargeMaroon.setColor(uminnMaroon);
        headerFontXXLargeMaroon.setColor(uminnMaroon);
        fontSmallLightItalicMaroon.setColor(uminnMaroon);
         
        reportColors.pageBgColor = uminnMaroon;
        reportColors.hraBaseReportColor = uminnMaroon;
        reportColors.headerDarkBgColor = uminnMaroon;
        reportColors.scoreBoxShadeBgColor = uminnMaroonLite;
        reportColors.barGraphCoreShade1 = new BaseColor( 0x91, 0x21, 0x38 ); // f68d2f // new BaseColor( 0xf6, 0x8d, 0x2f ); // f68d2f
        reportColors.barGraphCoreShade2 = new BaseColor( 0xff, 0x3b, 0x62 );      // ff3b62               

        try
        {
            if( logoUrl != null && !logoUrl.isBlank() )
                custLogo = Image.getInstance( new URL( logoUrl ) );

            if( figure1Url != null && !figure1Url.isBlank() )
                figure1Image = Image.getInstance( new URL( figure1Url ) );

            if( figure2Url != null && !figure2Url.isBlank() )
                figure2Image = Image.getInstance( new URL( figure2Url ) );

            if( figure3Url != null && !figure3Url.isBlank() )
                figure3Image = Image.getInstance( new URL( figure3Url ) );

            if( scoreFigureUrl != null && !scoreFigureUrl.isBlank() )
                scoreFigureImage = Image.getInstance( new URL( scoreFigureUrl ) );

            if( headerLogoUrl != null && !headerLogoUrl.isBlank() )
                headerLogo = Image.getInstance( new URL( headerLogoUrl ) );

            if( headerLogoWhiteTransUrl!=null && !headerLogoWhiteTransUrl.isBlank() )
                headerLogoWhiteTrans = Image.getInstance( new URL( headerLogoWhiteTransUrl ) );

            //if( twitterLogo==null )
            //    twitterLogo = Image.getInstance( new URL( twitterLogoUrl ) );

            // LogService.logIt( "UMinnJusticeReportSettings.initExtra() AAA headerLogoWhiteTrans.scaledHeight " +  headerLogoWhiteTrans.getScaledHeight() );

            // twitterLogo.scalePercent(70 );
            custLogo.scalePercent( 80);
            figure1Image.scalePercent( 66 );
            figure2Image.scalePercent( 66 );
            figure3Image.scalePercent( 66 );
            scoreFigureImage.scalePercent( 66 );
            
            headerLogo.scalePercent( 46 );
            headerLogoWhiteTrans.scalePercent( 38 );

            // LogService.logIt( "UMinnJusticeReportSettings.initExtra() BBB headerLogoWhiteTrans.scaledHeight " +  headerLogoWhiteTrans.getScaledHeight() );                
        }
        catch( Exception e )
        {
            LogService.logIt( e, "UMinnJusticeReportSettings.initFonts() getting images. "  );
        }

        // LogService.logIt( "UMinnJusticeReportSettings.initExtra() headerLogoWhiteTrans is " + (headerLogoWhiteTrans==null ? "null" : "not null")  );

        setLogoWhiteTextSmall(headerLogoWhiteTrans);
    }

    
    protected void initData() throws Exception
    {
        try
        {
            if( reportUtils == null )
                reportUtils = new ReportUtils();   
        }
        
        catch( Exception e )
        {
            LogService.logIt( e, "UMinnJusticeReportSettings.initData() " );
        }
    }
    
    
    
    public URL getLocalImageUrl(String baseUrl, String fn) {
        try {
            return new URL(baseUrl + "/" + fn);
        } catch (MalformedURLException e) {
            LogService.logIt(e, "getLocalImageUrl() ");
            return null;
        }
    }

    @Override
    public int getFontTypeIdForLocale( Locale locale )
    {
        return 4;
    }
    

    @Override
    public BaseColor getHeaderDarkBgColor() {
        return this.uminnMaroon;
    }
    
    
    public void initMessages()
    {
        if( this.uminnJusticeReportUtils==null )
            uminnJusticeReportUtils = new UMinnJusticeReportUtils();
    }


    public String lmsg( String key )
    {
        initMessages();
        
        return uminnJusticeReportUtils.getKey(key );
    }

    public String lmsg( String key, String[] prms )
    {
        initMessages();
        
        String msgText = uminnJusticeReportUtils.getKey(key );
        
        return MessageFactory.substituteParams(Locale.US , msgText, prms );
    }
    
    



}
