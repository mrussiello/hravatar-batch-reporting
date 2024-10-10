/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.util.I18nUtils;
import com.tm2batch.util.SSLUtils;
import java.util.Locale;

/**
 *
 * @author Mike
 */
public class StandardReportSettings implements ReportSettings
{
    public static float CT2_MARGIN = 20;
    public static float CT2_TEXT_EXTRAMARGIN = 25;
    public static float CT2_BOX_EXTRAMARGIN = 25;
    public static float CT2_BOXHEADER_LEFTPAD = 4;
    
    public static float MAX_CUSTLOGO_W = 80;
    public static float MAX_CUSTLOGO_H = 40;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // NOTE: NONE OF THESE ARE PUBLIC SO THAT OTHER REPORTS CAN OVERRIDE THEM FOR THAT REPORT but still extend from this class.
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // int MIN_COUNT_FOR_PERCENTILE = 10;
    
    public int XXLFONTSZ = 32;
    public int XLFONTSZ = 14;
    public int LLFONTSZ = 13;
    public int LFONTSZ = 12;
    public int LMFONTSZ = 11;
    public int FONTSZ = 10;
    public int SFONTSZ = 9;
    public int XSFONTSZ = 8;
    public int XXSFONTSZ = 7;
    

    public BaseFont baseFont;
    public BaseFont baseFontBold;
    public BaseFont baseFontItalic;
    public BaseFont baseFontBoldItalic;
    public BaseFont headerBaseFont;

    public Font fontXXLarge;
    public Font fontXXLargeWhite;
    public Font fontXXLargeLight;
    public Font fontXXLargeBold;
    public Font fontXXLargeItalic;
    public Font fontXXLargeBoldItalic;

    public Font headerFontXXLarge;
    public Font headerFontXXLargeWhite;

    public Font fontXLarge;
    public Font fontXLargeLight;
    public Font fontXLargeLightBold;
    public Font fontXLargeWhite;
    public Font fontXLargeBold;
    public Font fontXLargeItalic;
    public Font fontXLargeBoldItalic;
    public Font fontXLargeBoldWhite;

    public Font headerFontXLarge;
    public Font headerFontXLargeWhite;

    public Font fontLarge;
    public Font fontLargeWhite;
    public Font fontLargeLight;
    public Font fontLargeLightBold;
    public Font fontLargeBold;
    public Font fontLargeItalic;
    public Font fontLargeBoldItalic;

    public Font headerFontLarge;
    public Font headerFontLargeWhite;

    public Font font;
    public Font fontWhite;
    public Font fontLight;
    public Font fontLightBold;
    public Font fontLightItalic;
    public Font fontBold;
    public Font fontItalic;
    public Font fontBoldItalic;

    public Font fontSmall;
    public Font fontSmallWhite;
    public Font fontSmallLight;
    public Font fontSmallLightBold;
    public Font fontSmallLightItalic;
    public Font fontSmallBold;
    public Font fontSmallItalic;
    public Font fontSmallBoldItalic;
    public Font fontSmallBlueItalic;

    public Font fontXSmall;
    public Font fontXSmallWhite;
    public Font fontXSmallLight;
    public Font fontXSmallBold;
    public Font fontXSmallItalic;
    public Font fontXSmallBoldItalic;

    public Font fontXXSmall;
    public Font fontXXSmallWhite;
    public Font fontXXSmallLight;
    public Font fontXXSmallBold;
    public Font fontXXSmallItalic;
    public Font fontXXSmallBoldItalic;

    public Font fontSectionTitle;

    BaseColor whiteFontColor;  // #ffffff
    BaseColor darkFontColor;   // #282828
    BaseColor lightFontColor;  // #525252


    BaseColor scoreBoxHeaderBgColor;  // #e9e9e9
    BaseColor scoreBoxBgColor;  // #ffffff
    BaseColor scoreBoxBorderColor;  // #525252

    BaseColor headerDarkBgColor;    // #3a3a3a
    BaseColor titlePageBgColor; // #ffffff
    BaseColor pageBgColor;      // #eaeaea
    BaseColor hraBaseReportColor;   // #f1592a
    BaseColor tablePageBgColor; // #ffffff

    Image logoDarkText;
    Image logoDarkTextSmall;
    Image logoWhiteText;
    Image logoWhiteTextSmall;

    public ReportColors reportColors = null;
    
    boolean rtl;
    boolean usesNonAscii;

    // must start as -1
    public int fontTypeId = -1;
    

    @Override
    public void initSettings( ReportData reportData ) throws Exception
    {
        if( RuntimeConstants.getBooleanValue( "disableCertificateVerification" ) )
            SSLUtils.disableSslVerification();
        
        initColors(); 
        
        rtl = I18nUtils.isTextRTL( reportData==null ? Locale.US :  reportData.getLocale() );

        usesNonAscii = rtl || ( reportData==null ? false : reportData.getUsesNonAscii() );
        
        int rdFontTypeId = reportData==null ? -1 : reportData.getReportRuleAsInt("reportfonttypeid");
        if( rdFontTypeId>0 )
            fontTypeId = rdFontTypeId;
        
        if( baseFont == null )
        {
            String filesRoot = RuntimeConstants.getStringValue( "filesroot" ) + "/fonts/";

            if( fontTypeId<0 )
                fontTypeId = getFontTypeIdForLocale( reportData.getLocale() );

            // LogService.logIt( "Ct2ReportSettings.initSettings() Locale=" + reportData.getLocale().toString() + ", fontTypeId=" + fontTypeId );
            
            if( fontTypeId==0 && !usesNonAscii )
            {
                baseFont = BaseFont.createFont(filesRoot + "calibri.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED); // BaseFont.createFont( BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED );
                baseFontBold = BaseFont.createFont(filesRoot + "calibrib.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
                baseFontItalic = BaseFont.createFont(filesRoot + "calibrii.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
                baseFontBoldItalic = BaseFont.createFont(filesRoot + "calibriz.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);

                headerBaseFont = BaseFont.createFont(filesRoot + "calibrib.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
            }

            else if( fontTypeId == 1 || (fontTypeId==0 && usesNonAscii) )
            {
                // Mike R 9/8/2016 - Changed for this to Embedded Fonts.
                baseFont = BaseFont.createFont(filesRoot + "arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); // BaseFont.createFont( BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED );
                baseFontBold = BaseFont.createFont(filesRoot + "arialbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                baseFontItalic = BaseFont.createFont(filesRoot + "ariali.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                baseFontBoldItalic = BaseFont.createFont(filesRoot + "arialbi.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                headerBaseFont = BaseFont.createFont(filesRoot + "arialbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            }

            // Chinese
            else if( fontTypeId == 2 )
            {
                baseFont = BaseFont.createFont(filesRoot + "msyh.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); // BaseFont.createFont( BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED );
                baseFontBold = BaseFont.createFont(filesRoot + "msyhbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                baseFontItalic = BaseFont.createFont(filesRoot + "msyh.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                baseFontBoldItalic = BaseFont.createFont(filesRoot + "msyhbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

                headerBaseFont = BaseFont.createFont(filesRoot + "msyh.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            }

            // Japanese
            else if( fontTypeId == 3 )
            {
                baseFont = BaseFont.createFont(filesRoot + "KozMinPro-Regular.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); // BaseFont.createFont( BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED );
                baseFontBold = BaseFont.createFont(filesRoot + "KozMinPro-Bold.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                baseFontItalic = BaseFont.createFont(filesRoot + "KozMinPro-Regular.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                baseFontBoldItalic = BaseFont.createFont(filesRoot + "KozMinPro-Bold.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                headerBaseFont = BaseFont.createFont(filesRoot + "KozMinPro-Regular.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            }

            else if( fontTypeId==4 )
            {
                baseFont = BaseFont.createFont(filesRoot + "times.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED); // BaseFont.createFont( BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED );
                baseFontBold = BaseFont.createFont(filesRoot + "timesbd.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
                baseFontItalic = BaseFont.createFont(filesRoot + "timesi.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
                baseFontBoldItalic = BaseFont.createFont(filesRoot + "timesbi.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
                headerBaseFont = BaseFont.createFont(filesRoot + "timesbd.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
            }

            // cyrillic
            else if( fontTypeId==5 )
            {
                baseFont = BaseFont.createFont(filesRoot + "clearsans.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED); // BaseFont.createFont( BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED );
                baseFontBold = BaseFont.createFont(filesRoot + "clearsans-b.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
                baseFontItalic = BaseFont.createFont(filesRoot + "clearsans-i.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
                baseFontBoldItalic = BaseFont.createFont(filesRoot + "clearsans-bi.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
                headerBaseFont = BaseFont.createFont(filesRoot + "clearsans-b.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
            }


            //baseFont = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED );
            //baseFontBold = baseFont; 
            //baseFontItalic = baseFont; 
            //baseFontBoldItalic = baseFont; 
            //headerBaseFont = BaseFont.createFont( filesRoot + "BNKGOTHM.TTF", BaseFont.WINANSI, BaseFont.EMBEDDED );

            fontXXLarge = new Font( baseFont, XXLFONTSZ );
            fontXXLargeWhite = new Font( baseFont, XXLFONTSZ );
            fontXXLargeLight = new Font( baseFont, XXLFONTSZ );
            fontXXLargeBold = new Font( baseFontBold, XXLFONTSZ, Font.BOLD );
            fontXXLargeItalic = new Font( baseFontItalic, XXLFONTSZ, Font.ITALIC );
            fontXXLargeBoldItalic = new Font( baseFontBoldItalic, XXLFONTSZ, Font.BOLDITALIC );

            fontXLarge = new Font( baseFont, XLFONTSZ );
            fontXLargeWhite = new Font( baseFont, XLFONTSZ );
            fontXLargeLight = new Font( baseFont, XLFONTSZ );
            fontXLargeLightBold = new Font( baseFontBold, XLFONTSZ );
            fontXLargeBold = new Font( baseFontBold, XLFONTSZ, Font.BOLD );
            fontXLargeItalic = new Font( baseFontItalic, XLFONTSZ, Font.ITALIC );
            fontXLargeBoldItalic = new Font( baseFontBoldItalic, XLFONTSZ, Font.BOLDITALIC );
            fontXLargeBoldWhite = new Font( baseFontBold, XLFONTSZ, Font.BOLDITALIC );

            fontLarge = new Font( baseFont, LFONTSZ );
            fontLargeWhite = new Font( baseFont, LFONTSZ );
            fontLargeLight = new Font( baseFont, LFONTSZ );
            fontLargeLightBold = new Font( baseFontBold, LFONTSZ );
            fontLargeBold = new Font( baseFontBold, LFONTSZ, Font.BOLD );
            fontLargeItalic = new Font( baseFontItalic, LFONTSZ, Font.ITALIC );
            fontLargeBoldItalic = new Font( baseFontBoldItalic, LFONTSZ, Font.BOLDITALIC );

            fontSectionTitle = fontLargeLight;

            font = new Font( baseFont, FONTSZ );
            fontWhite = new Font( baseFont, FONTSZ );
            fontLight = new Font( baseFont, FONTSZ );
            fontLightBold = new Font( baseFontBold, FONTSZ );
            fontLightItalic = new Font( baseFontItalic, FONTSZ );
            fontBold = new Font( baseFontBold, FONTSZ, Font.BOLD );
            fontItalic = new Font( baseFontItalic, FONTSZ, Font.ITALIC );
            fontBoldItalic = new Font( baseFontBoldItalic, FONTSZ, Font.BOLDITALIC );

            fontSmall = new Font( baseFont, SFONTSZ );
            fontSmallWhite = new Font( baseFont, SFONTSZ );
            fontSmallLight = new Font( baseFont, SFONTSZ );
            fontSmallLightBold = new Font( baseFontBold, XSFONTSZ );
            fontSmallLightItalic = new Font( baseFontItalic, XSFONTSZ );
            fontSmallBold = new Font( baseFontBold, SFONTSZ, Font.BOLD );
            fontSmallItalic = new Font( baseFontItalic, SFONTSZ, Font.ITALIC );
            fontSmallBlueItalic =  new Font( baseFontItalic, SFONTSZ, Font.ITALIC );
            fontSmallBoldItalic = new Font( baseFontBoldItalic, SFONTSZ, Font.BOLDITALIC );

            fontXSmall = new Font( baseFont, XSFONTSZ );
            fontXSmallWhite = new Font( baseFont, XSFONTSZ );
            fontXSmallLight = new Font( baseFont, XSFONTSZ );
            fontXSmallBold = new Font( baseFontBold, XSFONTSZ, Font.BOLD );
            fontXSmallItalic = new Font( baseFontItalic, XSFONTSZ, Font.ITALIC );
            fontXSmallBoldItalic = new Font( baseFontBoldItalic, XSFONTSZ, Font.BOLDITALIC );

            fontXXSmall = new Font( baseFont, XXSFONTSZ );
            fontXXSmallWhite = new Font( baseFont, XXSFONTSZ );
            fontXXSmallLight = new Font( baseFont, XXSFONTSZ );
            fontXXSmallBold = new Font( baseFontBold, XXSFONTSZ, Font.BOLD );
            fontXXSmallItalic = new Font( baseFontItalic, XXSFONTSZ, Font.ITALIC );
            fontXXSmallBoldItalic = new Font( baseFontBoldItalic, XXSFONTSZ, Font.BOLDITALIC );

            headerFontXXLarge = new Font( headerBaseFont, XXLFONTSZ );
            headerFontXXLargeWhite = new Font( headerBaseFont, XXLFONTSZ );
            headerFontXLarge = new Font( headerBaseFont, XLFONTSZ );
            headerFontXLargeWhite = new Font( headerBaseFont, XLFONTSZ );
            headerFontLarge = new Font( headerBaseFont, LFONTSZ );
            headerFontLargeWhite = new Font( headerBaseFont, LFONTSZ );

            whiteFontColor = new BaseColor( 255,255,255 );  // #ffffff
            darkFontColor = new BaseColor( 40,40,40 );   // #282828
            lightFontColor = new BaseColor( 82,82,82 );  // #525252

            scoreBoxHeaderBgColor = new BaseColor( 233,233,233 );  // #e9e9e9
            scoreBoxBgColor = new BaseColor( 255,255,255 );  // #ffffff
            scoreBoxBorderColor = new BaseColor( 82,82,82 );  // #525252

            headerDarkBgColor =  new BaseColor(33, 150, 243); // new BaseColor(39,178,231); // #27b2e7 // new BaseColor( 58,58,58 );    // #3a3a3a
            titlePageBgColor = new BaseColor( 255,255,255 ); // #ffffff
            pageBgColor = new BaseColor( 234,234,234 );      // #eaeaea
            hraBaseReportColor =  new BaseColor(33, 150, 243); // = new BaseColor( 39,178,231 ); // #27b2e7   //   new BaseColor( 241,90,41 );   // #f1592a
            tablePageBgColor = new BaseColor( 0xf9, 0xf9, 0xf9 );
            
            BaseColor baseFontColor = darkFontColor;

            fontXXLarge.setColor( baseFontColor  );
            fontXXLargeWhite.setColor( whiteFontColor  );
            fontXXLargeLight.setColor( lightFontColor  );
            fontXXLargeBold.setColor( baseFontColor  );
            fontXXLargeItalic.setColor( baseFontColor  );
            fontXXLargeBoldItalic.setColor( baseFontColor  );

            fontXLarge.setColor( baseFontColor  );
            fontXLargeWhite.setColor( whiteFontColor  );
            fontXLargeLight.setColor( lightFontColor  );
            fontXLargeLightBold.setColor( lightFontColor  );
            fontXLargeBold.setColor( baseFontColor  );
            fontXLargeItalic.setColor( baseFontColor  );
            fontXLargeBoldItalic.setColor( baseFontColor  );
            fontXLargeBoldWhite.setColor( whiteFontColor );

            fontLarge.setColor( baseFontColor  );
            fontLargeWhite.setColor( whiteFontColor  );
            fontLargeLight.setColor( lightFontColor  );
            fontLargeLightBold.setColor( lightFontColor  );
            fontLargeBold.setColor( baseFontColor  );
            fontLargeItalic.setColor( baseFontColor  );
            fontLargeBoldItalic.setColor( baseFontColor  );

            font.setColor( baseFontColor  );
            fontWhite.setColor( whiteFontColor  );
            fontLight.setColor( lightFontColor  );
            fontLightBold.setColor( lightFontColor  );
            fontLightItalic.setColor( lightFontColor  );
            fontBold.setColor( baseFontColor  );
            fontItalic.setColor( baseFontColor  );
            fontBoldItalic.setColor( baseFontColor  );

            fontSmall.setColor( baseFontColor  );
            fontSmallWhite.setColor( whiteFontColor  );
            fontSmallLight.setColor( lightFontColor  );
            fontSmallLightBold.setColor( lightFontColor  );
            fontSmallLightItalic.setColor( lightFontColor  );
            fontSmallBold.setColor( baseFontColor  );
            fontSmallItalic.setColor( baseFontColor  );
            fontSmallBlueItalic.setColor( BaseColor.BLUE );
            fontSmallBoldItalic.setColor( baseFontColor  );

            fontXSmall.setColor( baseFontColor  );
            fontXSmallWhite.setColor( whiteFontColor  );
            fontXSmallLight.setColor( lightFontColor  );
            fontXSmallBold.setColor( baseFontColor  );
            fontXSmallItalic.setColor( baseFontColor  );
            fontXSmallBoldItalic.setColor( baseFontColor  );

            fontXXSmall.setColor( baseFontColor  );
            fontXXSmallWhite.setColor( whiteFontColor  );
            fontXXSmallLight.setColor( lightFontColor  );
            fontXXSmallBold.setColor( baseFontColor  );
            fontXXSmallItalic.setColor( baseFontColor  );
            fontXXSmallBoldItalic.setColor( baseFontColor  );


            headerFontXXLarge.setColor( baseFontColor  );
            headerFontXLarge.setColor( baseFontColor  );
            headerFontLarge.setColor( baseFontColor  );
            headerFontXXLargeWhite.setColor( whiteFontColor  );
            headerFontXLargeWhite.setColor( whiteFontColor  );
            headerFontLargeWhite.setColor( whiteFontColor  );
            
            scoreBoxHeaderBgColor = new BaseColor( 233,233,233 );  // #e9e9e9
            scoreBoxBgColor = new BaseColor( 255,255,255 );  // #ffffff
            scoreBoxBorderColor = new BaseColor( 82,82,82 );  // #525252

            logoDarkText = Image.getInstance( reportData.getLogoDarkTextUrl() );
            logoWhiteText = Image.getInstance( reportData.getLogoWhiteTextUrl() );
            logoDarkTextSmall= Image.getInstance( reportData.getLogoDarkTextSmallUrl() );
            logoWhiteTextSmall= Image.getInstance( reportData.getLogoWhiteTextSmallUrl() );

            float highresscale = 100*72/300;

            logoDarkText.scalePercent(highresscale );
            logoWhiteText.scalePercent( highresscale);
            logoDarkTextSmall.scalePercent( highresscale);
            logoWhiteTextSmall.scalePercent( highresscale );
        }
    }

    public void initColors()
    {
        // Nothing. 
        if( reportColors == null )
            reportColors = ReportColors.getCt2Colors( false );
    }
    
    

    @Override
    public int getFontTypeIdForLocale( Locale locale )
    {
        if( locale == null )
            return 0;

        String lang = locale.getLanguage().toLowerCase();

        if( lang.equals( "en" )|| lang.equals( "fr") || lang.equals( "de") || lang.equals( "it") || lang.equals( "pt" ) )
            return 0;


        else if( lang.equals( "he" ) || lang.equals( "ar")  || lang.equals( "es")  )
            return 1;

        else if( lang.equals( "zh" )   )
            return 2;

        else if( lang.equals( "ja" )   )
            return 3;

        else if( lang.equals( "ru")   )
            return 5;
        
        return 0;
    }

    
    
    @Override
    public int getXXLFONTSZ() {
        return XXLFONTSZ;
    }

    @Override
    public void setXXLFONTSZ(int XXLFONTSZ) {
        this.XXLFONTSZ = XXLFONTSZ;
    }

    @Override
    public int getXLFONTSZ() {
        return XLFONTSZ;
    }

    @Override
    public void setXLFONTSZ(int XLFONTSZ) {
        this.XLFONTSZ = XLFONTSZ;
    }

    @Override
    public int getLFONTSZ() {
        return LFONTSZ;
    }

    @Override
    public void setLFONTSZ(int LFONTSZ) {
        this.LFONTSZ = LFONTSZ;
    }

    @Override
    public int getFONTSZ() {
        return FONTSZ;
    }

    @Override
    public void setFONTSZ(int FONTSZ) {
        this.FONTSZ = FONTSZ;
    }

    @Override
    public int getSFONTSZ() {
        return SFONTSZ;
    }

    @Override
    public void setSFONTSZ(int SFONTSZ) {
        this.SFONTSZ = SFONTSZ;
    }

    @Override
    public int getXSFONTSZ() {
        return XSFONTSZ;
    }

    @Override
    public void setXSFONTSZ(int XSFONTSZ) {
        this.XSFONTSZ = XSFONTSZ;
    }

    @Override
    public int getXXSFONTSZ() {
        return XXSFONTSZ;
    }

    @Override
    public void setXXSFONTSZ(int XXSFONTSZ) {
        this.XXSFONTSZ = XXSFONTSZ;
    }

    @Override
    public BaseFont getBaseFont() {
        return baseFont;
    }

    @Override
    public void setBaseFont(BaseFont baseFont) {
        this.baseFont = baseFont;
    }



    @Override
    public BaseFont getBaseFontBold() {
        return baseFontBold;
    }

    @Override
    public void setBaseFontBold(BaseFont baseFontBold) {
        this.baseFontBold = baseFontBold;
    }

    @Override
    public BaseFont getBaseFontItalic() {
        return baseFontItalic;
    }

    @Override
    public void setBaseFontItalic(BaseFont baseFontItalic) {
        this.baseFontItalic = baseFontItalic;
    }

    @Override
    public BaseFont getBaseFontBoldItalic() {
        return baseFontBoldItalic;
    }

    @Override
    public void setBaseFontBoldItalic(BaseFont baseFontBoldItalic) {
        this.baseFontBoldItalic = baseFontBoldItalic;
    }

    @Override
    public BaseFont getHeaderBaseFont() {
        return headerBaseFont;
    }

    @Override
    public void setHeaderBaseFont(BaseFont headerBaseFont) {
        this.headerBaseFont = headerBaseFont;
    }

    @Override
    public Font getFontXXLarge() {
        return fontXXLarge;
    }

    @Override
    public void setFontXXLarge(Font fontXXLarge) {
        this.fontXXLarge = fontXXLarge;
    }

    @Override
    public Font getFontXXLargeWhite() {
        return fontXXLargeWhite;
    }

    @Override
    public void setFontXXLargeWhite(Font fontXXLargeWhite) {
        this.fontXXLargeWhite = fontXXLargeWhite;
    }

    @Override
    public Font getFontXXLargeLight() {
        return fontXXLargeLight;
    }

    @Override
    public void setFontXXLargeLight(Font fontXXLargeLight) {
        this.fontXXLargeLight = fontXXLargeLight;
    }

    @Override
    public Font getFontXXLargeBold() {
        return fontXXLargeBold;
    }

    @Override
    public void setFontXXLargeBold(Font fontXXLargeBold) {
        this.fontXXLargeBold = fontXXLargeBold;
    }

    @Override
    public Font getFontXXLargeItalic() {
        return fontXXLargeItalic;
    }

    @Override
    public void setFontXXLargeItalic(Font fontXXLargeItalic) {
        this.fontXXLargeItalic = fontXXLargeItalic;
    }

    @Override
    public Font getFontXXLargeBoldItalic() {
        return fontXXLargeBoldItalic;
    }

    @Override
    public void setFontXXLargeBoldItalic(Font fontXXLargeBoldItalic) {
        this.fontXXLargeBoldItalic = fontXXLargeBoldItalic;
    }

    @Override
    public Font getHeaderFontXXLarge() {
        return headerFontXXLarge;
    }

    @Override
    public void setHeaderFontXXLarge(Font headerFontXXLarge) {
        this.headerFontXXLarge = headerFontXXLarge;
    }

    @Override
    public Font getHeaderFontXXLargeWhite() {
        return headerFontXXLargeWhite;
    }

    @Override
    public void setHeaderFontXXLargeWhite(Font headerFontXXLargeWhite) {
        this.headerFontXXLargeWhite = headerFontXXLargeWhite;
    }

    @Override
    public Font getFontXLarge() {
        return fontXLarge;
    }

    @Override
    public void setFontXLarge(Font fontXLarge) {
        this.fontXLarge = fontXLarge;
    }

    @Override
    public Font getFontXLargeLight() {
        return fontXLargeLight;
    }

    @Override
    public void setFontXLargeLight(Font fontXLargeLight) {
        this.fontXLargeLight = fontXLargeLight;
    }

    @Override
    public Font getFontXLargeLightBold() {
        return fontXLargeLightBold;
    }

    @Override
    public void setFontXLargeLightBold(Font fontXLargeLightBold) {
        this.fontXLargeLightBold = fontXLargeLightBold;
    }

    @Override
    public Font getFontXLargeWhite() {
        return fontXLargeWhite;
    }

    @Override
    public void setFontXLargeWhite(Font fontXLargeWhite) {
        this.fontXLargeWhite = fontXLargeWhite;
    }

    @Override
    public Font getFontXLargeBold() {
        return fontXLargeBold;
    }

    @Override
    public void setFontXLargeBold(Font fontXLargeBold) {
        this.fontXLargeBold = fontXLargeBold;
    }

    @Override
    public Font getFontXLargeItalic() {
        return fontXLargeItalic;
    }

    @Override
    public void setFontXLargeItalic(Font fontXLargeItalic) {
        this.fontXLargeItalic = fontXLargeItalic;
    }

    @Override
    public Font getFontXLargeBoldItalic() {
        return fontXLargeBoldItalic;
    }

    @Override
    public void setFontXLargeBoldItalic(Font fontXLargeBoldItalic) {
        this.fontXLargeBoldItalic = fontXLargeBoldItalic;
    }

    @Override
    public Font getHeaderFontXLarge() {
        return headerFontXLarge;
    }

    @Override
    public void setHeaderFontXLarge(Font headerFontXLarge) {
        this.headerFontXLarge = headerFontXLarge;
    }

    @Override
    public Font getHeaderFontXLargeWhite() {
        return headerFontXLargeWhite;
    }

    @Override
    public void setHeaderFontXLargeWhite(Font headerFontXLargeWhite) {
        this.headerFontXLargeWhite = headerFontXLargeWhite;
    }

    @Override
    public Font getFontLarge() {
        return fontLarge;
    }

    @Override
    public void setFontLarge(Font fontLarge) {
        this.fontLarge = fontLarge;
    }

    @Override
    public Font getFontLargeWhite() {
        return fontLargeWhite;
    }

    @Override
    public void setFontLargeWhite(Font fontLargeWhite) {
        this.fontLargeWhite = fontLargeWhite;
    }

    @Override
    public Font getFontLargeLight() {
        return fontLargeLight;
    }

    @Override
    public void setFontLargeLight(Font fontLargeLight) {
        this.fontLargeLight = fontLargeLight;
    }

    @Override
    public Font getFontLargeLightBold() {
        return fontLargeLightBold;
    }

    @Override
    public void setFontLargeLightBold(Font fontLargeLightBold) {
        this.fontLargeLightBold = fontLargeLightBold;
    }

    @Override
    public Font getFontLargeBold() {
        return fontLargeBold;
    }

    @Override
    public void setFontLargeBold(Font fontLargeBold) {
        this.fontLargeBold = fontLargeBold;
    }

    @Override
    public Font getFontLargeItalic() {
        return fontLargeItalic;
    }

    @Override
    public void setFontLargeItalic(Font fontLargeItalic) {
        this.fontLargeItalic = fontLargeItalic;
    }

    @Override
    public Font getFontLargeBoldItalic() {
        return fontLargeBoldItalic;
    }

    @Override
    public void setFontLargeBoldItalic(Font fontLargeBoldItalic) {
        this.fontLargeBoldItalic = fontLargeBoldItalic;
    }

    @Override
    public Font getHeaderFontLarge() {
        return headerFontLarge;
    }

    @Override
    public void setHeaderFontLarge(Font headerFontLarge) {
        this.headerFontLarge = headerFontLarge;
    }

    @Override
    public Font getHeaderFontLargeWhite() {
        return headerFontLargeWhite;
    }

    @Override
    public void setHeaderFontLargeWhite(Font headerFontLargeWhite) {
        this.headerFontLargeWhite = headerFontLargeWhite;
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public Font getFontWhite() {
        return fontWhite;
    }

    @Override
    public void setFontWhite(Font fontWhite) {
        this.fontWhite = fontWhite;
    }

    @Override
    public Font getFontLight() {
        return fontLight;
    }

    @Override
    public void setFontLight(Font fontLight) {
        this.fontLight = fontLight;
    }

    @Override
    public Font getFontLightBold() {
        return fontLightBold;
    }

    @Override
    public void setFontLightBold(Font fontLightBold) {
        this.fontLightBold = fontLightBold;
    }

    @Override
    public Font getFontLightItalic() {
        return fontLightItalic;
    }

    @Override
    public void setFontLightItalic(Font fontLightItalic) {
        this.fontLightItalic = fontLightItalic;
    }

    @Override
    public Font getFontBold() {
        return fontBold;
    }

    @Override
    public void setFontBold(Font fontBold) {
        this.fontBold = fontBold;
    }

    @Override
    public Font getFontItalic() {
        return fontItalic;
    }

    @Override
    public void setFontItalic(Font fontItalic) {
        this.fontItalic = fontItalic;
    }

    @Override
    public Font getFontBoldItalic() {
        return fontBoldItalic;
    }

    @Override
    public void setFontBoldItalic(Font fontBoldItalic) {
        this.fontBoldItalic = fontBoldItalic;
    }

    @Override
    public Font getFontSmall() {
        return fontSmall;
    }

    @Override
    public void setFontSmall(Font fontSmall) {
        this.fontSmall = fontSmall;
    }

    @Override
    public Font getFontSmallWhite() {
        return fontSmallWhite;
    }

    @Override
    public void setFontSmallWhite(Font fontSmallWhite) {
        this.fontSmallWhite = fontSmallWhite;
    }

    @Override
    public Font getFontSmallLight() {
        return fontSmallLight;
    }

    @Override
    public void setFontSmallLight(Font fontSmallLight) {
        this.fontSmallLight = fontSmallLight;
    }

    @Override
    public Font getFontSmallLightBold() {
        return fontSmallLightBold;
    }

    @Override
    public void setFontSmallLightBold(Font fontSmallLightBold) {
        this.fontSmallLightBold = fontSmallLightBold;
    }

    @Override
    public Font getFontSmallLightItalic() {
        return fontSmallLightItalic;
    }

    @Override
    public void setFontSmallLightItalic(Font fontSmallLightItalic) {
        this.fontSmallLightItalic = fontSmallLightItalic;
    }

    @Override
    public Font getFontSmallBold() {
        return fontSmallBold;
    }

    @Override
    public void setFontSmallBold(Font fontSmallBold) {
        this.fontSmallBold = fontSmallBold;
    }

    @Override
    public Font getFontSmallItalic() {
        return fontSmallItalic;
    }

    @Override
    public void setFontSmallItalic(Font fontSmallItalic) {
        this.fontSmallItalic = fontSmallItalic;
    }

    @Override
    public Font getFontSmallBoldItalic() {
        return fontSmallBoldItalic;
    }

    @Override
    public void setFontSmallBoldItalic(Font fontSmallBoldItalic) {
        this.fontSmallBoldItalic = fontSmallBoldItalic;
    }

    @Override
    public Font getFontXSmall() {
        return fontXSmall;
    }

    @Override
    public void setFontXSmall(Font fontXSmall) {
        this.fontXSmall = fontXSmall;
    }

    @Override
    public Font getFontXSmallWhite() {
        return fontXSmallWhite;
    }

    @Override
    public void setFontXSmallWhite(Font fontXSmallWhite) {
        this.fontXSmallWhite = fontXSmallWhite;
    }

    @Override
    public Font getFontXSmallLight() {
        return fontXSmallLight;
    }

    @Override
    public void setFontXSmallLight(Font fontXSmallLight) {
        this.fontXSmallLight = fontXSmallLight;
    }

    @Override
    public Font getFontXSmallBold() {
        return fontXSmallBold;
    }

    @Override
    public void setFontXSmallBold(Font fontXSmallBold) {
        this.fontXSmallBold = fontXSmallBold;
    }

    @Override
    public Font getFontXSmallItalic() {
        return fontXSmallItalic;
    }

    @Override
    public void setFontXSmallItalic(Font fontXSmallItalic) {
        this.fontXSmallItalic = fontXSmallItalic;
    }

    @Override
    public Font getFontXSmallBoldItalic() {
        return fontXSmallBoldItalic;
    }

    @Override
    public void setFontXSmallBoldItalic(Font fontXSmallBoldItalic) {
        this.fontXSmallBoldItalic = fontXSmallBoldItalic;
    }

    @Override
    public Font getFontXXSmall() {
        return fontXXSmall;
    }

    @Override
    public void setFontXXSmall(Font fontXXSmall) {
        this.fontXXSmall = fontXXSmall;
    }

    @Override
    public Font getFontXXSmallWhite() {
        return fontXXSmallWhite;
    }

    @Override
    public void setFontXXSmallWhite(Font fontXXSmallWhite) {
        this.fontXXSmallWhite = fontXXSmallWhite;
    }

    @Override
    public Font getFontXXSmallLight() {
        return fontXXSmallLight;
    }

    @Override
    public void setFontXXSmallLight(Font fontXXSmallLight) {
        this.fontXXSmallLight = fontXXSmallLight;
    }

    @Override
    public Font getFontXXSmallBold() {
        return fontXXSmallBold;
    }

    @Override
    public void setFontXXSmallBold(Font fontXXSmallBold) {
        this.fontXXSmallBold = fontXXSmallBold;
    }

    @Override
    public Font getFontXXSmallItalic() {
        return fontXXSmallItalic;
    }

    @Override
    public void setFontXXSmallItalic(Font fontXXSmallItalic) {
        this.fontXXSmallItalic = fontXXSmallItalic;
    }

    @Override
    public Font getFontXXSmallBoldItalic() {
        return fontXXSmallBoldItalic;
    }

    @Override
    public void setFontXXSmallBoldItalic(Font fontXXSmallBoldItalic) {
        this.fontXXSmallBoldItalic = fontXXSmallBoldItalic;
    }

    @Override
    public Font getFontSectionTitle() {
        return fontSectionTitle;
    }

    @Override
    public void setFontSectionTitle(Font fontSectionTitle) {
        this.fontSectionTitle = fontSectionTitle;
    }

    @Override
    public BaseColor getWhiteFontColor() {
        return whiteFontColor;
    }

    @Override
    public void setWhiteFontColor(BaseColor whiteFontColor) {
        this.whiteFontColor = whiteFontColor;
    }

    @Override
    public BaseColor getDarkFontColor() {
        return darkFontColor;
    }

    @Override
    public void setDarkFontColor(BaseColor darkFontColor) {
        this.darkFontColor = darkFontColor;
    }

    @Override
    public BaseColor getLightFontColor() {
        return lightFontColor;
    }

    @Override
    public void setLightFontColor(BaseColor lightFontColor) {
        this.lightFontColor = lightFontColor;
    }




    @Override
    public BaseColor getHeaderDarkBgColor() {
        return headerDarkBgColor;
    }

    @Override
    public void setHeaderDarkBgColor(BaseColor headerBgColor) {
        this.headerDarkBgColor = headerBgColor;
    }

    @Override
    public BaseColor getTitlePageBgColor() {
        return titlePageBgColor;
    }

    @Override
    public void setTitlePageBgColor(BaseColor titlePageBgColor) {
        this.titlePageBgColor = titlePageBgColor;
    }

    @Override
    public BaseColor getPageBgColor() {
        return pageBgColor;
    }

    @Override
    public void setPageBgColor(BaseColor pageBgColor) {
        this.pageBgColor = pageBgColor;
    }

    @Override
    public BaseColor getHraBaseReportColor() {
        return hraBaseReportColor;
    }

    @Override
    public void setHraBaseReportColor(BaseColor hraOrangeColor) {
        this.hraBaseReportColor = hraOrangeColor;
    }

    @Override
    public BaseColor getTablePageBgColor() {
        return tablePageBgColor;
    }

    @Override
    public void setTablePageBgColor(BaseColor tablePageBgColor) {
        this.tablePageBgColor = tablePageBgColor;
    }

    @Override
    public Image getLogoWhiteText() {
        return logoWhiteText;
    }

    @Override
    public void setLogoWhiteText(Image img) {
        this.logoWhiteText = img;
    }

    @Override
    public Image getLogoWhiteTextSmall() {
        return logoWhiteTextSmall;
    }

    @Override
    public void setLogoWhiteTextSmall(Image hraLogoWhiteTextSmall) {
        this.logoWhiteTextSmall = hraLogoWhiteTextSmall;
    }

    @Override
    public Image getLogoDarkText() {
        return logoDarkText;
    }

    @Override
    public void setLogoDarkText(Image img) {
        this.logoDarkText = img;
    }

    @Override
    public Image getLogoDarkTextSmall() {
        return logoDarkTextSmall;
    }

    @Override
    public void setLogoDarkTextSmall(Image hraLogoDarkTextSmall) {
        this.logoDarkTextSmall = hraLogoDarkTextSmall;
    }
    
    
    @Override
    public BaseColor getScoreBoxHeaderBgColor() {
        return scoreBoxHeaderBgColor;
    }

    @Override
    public void setScoreBoxHeaderBgColor(BaseColor scoreBoxHeaderBgColor) {
        this.scoreBoxHeaderBgColor = scoreBoxHeaderBgColor;
    }

    @Override
    public BaseColor getScoreBoxBgColor() {
        return scoreBoxBgColor;
    }

    @Override
    public void setScoreBoxBgColor(BaseColor scoreBoxBgColor) {
        this.scoreBoxBgColor = scoreBoxBgColor;
    }

    @Override
    public BaseColor getScoreBoxBorderColor() {
        return scoreBoxBorderColor;
    }

    @Override
    public void setScoreBoxBorderColor(BaseColor scoreBoxBorderColor) {
        this.scoreBoxBorderColor = scoreBoxBorderColor;
    }

}
