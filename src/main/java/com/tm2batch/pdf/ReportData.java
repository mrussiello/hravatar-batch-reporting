/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.pdf;

import com.itextpdf.text.pdf.PdfWriter;
import com.tm2batch.entity.purchase.Product;
import com.tm2batch.entity.report.Report;
import com.tm2batch.entity.user.Org;
import com.tm2batch.entity.user.Suborg;
import com.tm2batch.entity.user.User;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.service.LogService;
import com.tm2batch.util.I18nUtils;
import java.awt.ComponentOrientation;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.TimeZone;


/**
 *
 * @author Mike
 */
public class ReportData
{
    public static String logoDarkTextFilename = "hralogoblacktext-blue.png";
    public static String logoWhiteTextFilename = "hralogowhitetext-blue.png";
    public static String logoDarkTextSmallFilename = "hralogoblacktext-small-blue.png";
    public static String logoWhiteTextSmallFilename = "hralogowhitetext-small-blue.png";

    public static String hraLogoBlackTextFilename;
    public static String hraLogoBlackTextPurpleFilename;
    public static String hraLogoWhiteTextFilename;
    public static String hraLogoWhiteTextPurpleFilename;
    public static String hraLogoBlackTextSmallFilename;
    public static String hraLogoBlackTextSmallPurpleFilename;
    public static String hraLogoWhiteTextSmallFilename;
    public static String hraLogoWhiteTextSmallPurpleFilename;
    public static String hraCoverPageFilename;
    public static String hraCoverPageBlueArrowFilename;



    public Report r;

    public  User u;

    public Org o;

    public Suborg s;

    public Product p;

    // public List<NVPair> reportRules;
    public ReportRules reportRules;

    Locale reportLocale;

    public Object[] objArray;


    public ReportData( Report r, User u, Org o, Suborg s, Product p, Object[] objArray)
    {
        this.r = r;
        this.u = u;
        this.o = o;
        this.s = s;
        this.p = p;
        this.objArray = objArray;

        reportRules = new ReportRules( o, s, p, r );

        init();
    }


    public static synchronized void init()
    {
        if( hraLogoBlackTextFilename!=null && !hraLogoBlackTextFilename.isBlank() )
            return;

        logoDarkTextFilename = RuntimeConstants.getStringValue("logoDarkTextFilename");
        logoWhiteTextFilename = RuntimeConstants.getStringValue("logoWhiteTextFilename");
        logoDarkTextSmallFilename = RuntimeConstants.getStringValue("logoDarkTextSmallFilename");
        logoWhiteTextSmallFilename = RuntimeConstants.getStringValue("logoWhiteTextSmallFilename");
        
        hraLogoBlackTextFilename = RuntimeConstants.getStringValue("hraLogoBlackTextFilename");
        hraLogoBlackTextPurpleFilename = RuntimeConstants.getStringValue("hraLogoBlackTextPurpleFilename");

        hraLogoWhiteTextFilename = RuntimeConstants.getStringValue("hraLogoWhiteTextFilename");
        hraLogoWhiteTextPurpleFilename = RuntimeConstants.getStringValue("hraLogoWhiteTextPurpleFilename");

        hraLogoBlackTextSmallFilename = RuntimeConstants.getStringValue("hraLogoBlackTextSmallFilename");
        hraLogoBlackTextSmallPurpleFilename = RuntimeConstants.getStringValue("hraLogoBlackTextSmallPurpleFilename");


        hraLogoWhiteTextSmallFilename = RuntimeConstants.getStringValue("hraLogoWhiteTextSmallFilename");    
        hraLogoWhiteTextSmallPurpleFilename = RuntimeConstants.getStringValue("hraLogoWhiteTextSmallPurpleFilename");
        
        hraCoverPageFilename = RuntimeConstants.getStringValue("hraCoverPageFilename");
        hraCoverPageBlueArrowFilename = RuntimeConstants.getStringValue("hraCoverIncludedArrowFilename");
        
    }


    @Override
    public String toString()
    {

        String out = "ReportData: ";

        if( r!=null )
            out += r.toString();

        return out;
    }


    public URL getLogoDarkTextUrl()
    {
        return getLocalImageUrl( logoDarkTextFilename );
    }

    public URL getLogoDarkTextSmallUrl()
    {
        return getLocalImageUrl( logoDarkTextSmallFilename );
    }


    public URL getLogoWhiteTextSmallUrl()
    {
        return getLocalImageUrl( logoWhiteTextSmallFilename );
    }

    public URL getLogoWhiteTextUrl()
    {
        return getLocalImageUrl( logoWhiteTextFilename );
    }

    public String getReportRuleAsString( String name )
    {
       if( reportRules!=null )
           return reportRules.getReportRuleAsString(name);

       return null;
    }

    public boolean getReportRuleAsBoolean( String name )
    {
       if( reportRules!=null )
           return reportRules.getReportRuleAsBoolean(name);

       return false;
    }

    public int getReportRuleAsInt( String name )
    {
       if( reportRules!=null )
           return reportRules.getReportRuleAsInt(name);

       // LogService.logIt( "ReportData.getReportRuleAsInt(" + name + ") tk: " + (tk==null) + ", sub: " + (this.getSuborg()==null) + ", org: " + (this.getOrg()==null) + ", r2u: " + this.r  );
       return 0;
    }



    public boolean hasUserInfo()
    {
        return  u!=null && u.getUserType().getNamedUserIdUsername();
    }


    public boolean getUsesNonAscii()
    {
        Locale l = getLocale();

        // Any right to left
        if( I18nUtils.isTextRTL( l ) )
            return true;

        if( I18nUtils.isTextNonAscii(l) )
            return true;

        // Check the product language
        if( p!=null && p.getLangStr()!=null && !p.getLangStr().isEmpty() && I18nUtils.isTextRTL( I18nUtils.getLocaleFromCompositeStr(p.getLangStr() ) ) )
            return true;

        return false;
    }




    public Locale getLocale()
    {
        if( reportLocale!=null )
            return reportLocale;

        if( r!=null && r.getLocaleForReportGen()!=null )
            return r.getLocaleForReportGen();

        else if( r!= null && r.getLocaleStr()!= null && !r.getLocaleStr().isEmpty() )
            return I18nUtils.getLocaleFromCompositeStr( r.getLocaleStr() );

        return Locale.US;
    }


    public Locale getTestContentLocale()
    {
        return p!=null && p.getLangStr()!=null && !p.getLangStr().isBlank() ? I18nUtils.getLocaleFromCompositeStr(p.getLangStr()) : Locale.US;
    }

    public boolean getIsLTR()
    {
        return ComponentOrientation.getOrientation( getLocale() ).isLeftToRight();
    }

    public TimeZone getTimeZone()
    {
        return u==null ? TimeZone.getDefault() : u.getTimeZone();
    }

    public int getTextRunDirection()
    {
        return getIsLTR() ? PdfWriter.RUN_DIRECTION_LTR : PdfWriter.RUN_DIRECTION_RTL;
    }



    public String getBaseImageUrl()
    {
        return RuntimeConstants.getStringValue( "baseurl" ) + "/resources/images/";
    }


    public boolean hasCustLogo()
    {
        return (o!= null && o.getReportLogoUrl()!= null && !o.getReportLogoUrl().isBlank()) || (s!=null && s.getReportLogoUrl()!= null && !s.getReportLogoUrl().isBlank()); //  custLogoFilename != null && !custLogoFilename.isEmpty();
    }


    public URL getCustLogoUrl()
    {
       try
       {
           return hasCustLogo() ? new URI( s!=null && s.getReportLogoUrl()!= null && !s.getReportLogoUrl().isBlank() ? s.getReportLogoUrl() : o.getReportLogoUrl() ).toURL() : null; // com.tm2batch.util.HttpUtils.getURLFromString( baseImageUrl + custLogoFilename );
       }
       catch( Exception e )
       {
           LogService.logIt(e, "ReportData.getCustLogoUrl() " );
           return null;
       }
    }


    public URL getLocalImageUrl( String fn )
    {

       if( fn==null || fn.isBlank() )
           return null;
       
       try
       {
           if( fn.toLowerCase().startsWith("http") )
               return (new URI(fn)).toURL();

           return (new URI(getBaseImageUrl() + "/" + fn)).toURL();
       }

       catch( MalformedURLException | URISyntaxException e )
       {
           LogService.logIt(e, "ReportData.getImageUrl() " );
           return null;
       }
    }


    public URL getHRALogoBlackTextUrl()
    {
        return getLocalImageUrl( hraLogoBlackTextFilename );
    }

    public URL getHRALogoBlackTextUrl( boolean devel )
    {
        return getLocalImageUrl( devel ? hraLogoBlackTextPurpleFilename : hraLogoBlackTextFilename );
    }



    public URL getHRALogoBlackTextSmallUrl()
    {
        return getLocalImageUrl( hraLogoBlackTextSmallFilename );
    }


    public URL getHRALogoBlackTextSmallUrl( boolean devel )
    {
        return getLocalImageUrl(  devel ? hraLogoBlackTextSmallPurpleFilename : hraLogoBlackTextSmallFilename );
    }

    public URL getHRALogoWhiteTextSmallUrl()
    {
        return getLocalImageUrl( hraLogoWhiteTextSmallFilename );
    }

    public URL getHRALogoWhiteTextSmallUrl( boolean devel )
    {
        return getLocalImageUrl( devel ? hraLogoWhiteTextSmallPurpleFilename : hraLogoWhiteTextSmallFilename );
    }


    public URL getHRALogoWhiteTextUrl()
    {
        return getLocalImageUrl( hraLogoWhiteTextFilename );
    }

    public URL getHRALogoWhiteTextUrl( boolean devel )
    {
        return getLocalImageUrl( devel ? hraLogoWhiteTextPurpleFilename : hraLogoWhiteTextFilename );
    }

    public URL getHRACoverPageUrl()
    {
        return getLocalImageUrl( hraCoverPageFilename );
    }

    public URL getHRACoverPageBlueArrowUrl()
    {
        return getLocalImageUrl( hraCoverPageBlueArrowFilename );
    }



    public String getOrgName() {
        return o.getName();
    }

    public String getReportName()
    {
        return r.getTitle();
    }

    public String getUserName()
    {
        return u==null ? "" : u.getFullname();
    }

    public User getUser() {
        return u;
    }


    public Org getOrg() {
        return o;
    }

    public Report getR2Use()
    {
        return getReport();
    }
    
    public Report getReport() {
        return r;
    }

    public Suborg getSuborg() {
        return s;
    }

    public Locale getReportLocale() {
        return reportLocale;
    }

    public void setReportLocale(Locale reportLocale) {
        this.reportLocale = reportLocale;
    }





}
