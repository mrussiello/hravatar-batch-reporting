/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.disc.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tm2batch.custom.coretest2.CellBackgroundCellEvent;
import com.tm2batch.custom.coretest2.CoverBlueBarCellEvent;
import com.tm2batch.custom.coretest2.CoverIncludedBarCellEvent;
import com.tm2batch.custom.disc.DiscGroupDataSet;
import com.tm2batch.custom.disc.DiscReportUtils;
import com.tm2batch.custom.disc.DiscResult;
import com.tm2batch.custom.disc.DiscResultTwoLevelScoreComparator;
import com.tm2batch.custom.disc.DiscResultUserNameComparator;
import com.tm2batch.entity.user.Suborg;
import com.tm2batch.entity.user.User;
import com.tm2batch.format.TableBackgroundEvent;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.global.STException;
import com.tm2batch.pdf.ITextUtils;
import com.tm2batch.pdf.ReportData;
import com.tm2batch.pdf.ReportTemplate;
import com.tm2batch.report.SampleReportUtils;
import com.tm2batch.service.LogService;
import com.tm2batch.util.HttpUtils;
import com.tm2batch.util.I18nUtils;
import com.tm2batch.util.MessageFactory;
import com.tm2batch.util.StringUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 *
 * @author Mike
 */
public abstract class BaseDiscGroupReportTemplate extends DiscGroupReportSettings implements ReportTemplate
{
    static String BULLET = "\u2022";
    
    static boolean DEBUG = false;
        
    DiscGroupDataSet discDataSet;
    
    Document document = null;

    ByteArrayOutputStream baos;

    PdfWriter pdfWriter;

    float pageWidth = 0;
    float pageHeight = 0;
    float usablePageHeight = 0;
    String title;

    float headerHgt;
    float footerHgt;

    float PAD = 5;
    float TPAD = 8;

    float lineW = 0.8f;

    float currentYLevel = 0;
    float previousYLevel = 0;

    // int scoreScheme = 0;
    
    DiscReportUtils discReportUtils = null;
    
    public String bundleToUse = null;

    public float footerHeight = 50;
    public float footerBarHeight = 18;
    public int footerImageIndex = 0;
    
    public String groupReportCoverImageUrl = "https://cdn.hravatar.com/web/orgimage/zrWvh1uNWrg-/img_14x1741970464999.png";
    
    public String[] groupReportFooterImageUrls = new String[] { "https://cdn.hravatar.com/web/orgimage/zrWvh1uNWrg-/img_17x1741983088934.png",
                                                       "https://cdn.hravatar.com/web/orgimage/zrWvh1uNWrg-/img_8x1741970464652.png",
                                                       "https://cdn.hravatar.com/web/orgimage/zrWvh1uNWrg-/img_11x1741970464884.png"};
    
    public static String groupReportBarFooterImageUri = "https://s3.amazonaws.com/cfmedia-hravatar-com/web/orgimage/zrWvh1uNWrg-/img_15x1741971514005.png";
    
    // public BaseColor hraBlue = new BaseColor( 0x00, 0x77, 0xcc );
    public static BaseColor babyBlue = new BaseColor( 0xd2, 0xec, 0xff );
    
    public TableBackgroundEvent dataTableEvent;
    public TableBackgroundEvent tableHeaderRowEvent;

    


    @Override
    public abstract byte[] generateReport() throws Exception;


    public synchronized void initFonts() throws Exception
    {
        initSettings( reportData );
        
        String logoUrl = null;

        if( reportData.s!=null && reportData.s.getReportLogoUrl()!=null && !reportData.s.getReportLogoUrl().isBlank() )
            logoUrl = reportData.s.getReportLogoUrl() ;

        if( logoUrl == null && reportData.o.getReportLogoUrl()!=null && !reportData.o.getReportLogoUrl().isBlank() )
            logoUrl = reportData.o.getReportLogoUrl() ;

        if( logoUrl!=null && !logoUrl.isBlank() )
        {
            String lu = logoUrl.toLowerCase();
            if( !lu.contains(".png") && !lu.contains(".gif") && !lu.contains(".jpg") && !lu.contains(".jpeg") )
            {
                LogService.logIt( "BaseDiscGroupReportTemplate.initFonts() AAA.1 LogoURL appears to be invalid format. Ignoring. OrgId=" + reportData.o.getOrgId() + ", logo=" + logoUrl );
                logoUrl=null;
            }
        }

        if( logoUrl!= null && StringUtils.isCurlyBracketed( logoUrl ) )
            logoUrl = RuntimeConstants.getStringValue( "translogoimageurl" );

        try
        {
            custLogo = (logoUrl == null || logoUrl.isBlank()) ? null : ITextUtils.getITextImage( (new URI(logoUrl)).toURL() );  //   getImageInstance(logoUrl, reportData.te.getTestEventId());
        }
        catch( Exception e )
        {
            custLogo = null;

            if( e instanceof IOException && logoUrl!=null && logoUrl.trim().toLowerCase().startsWith("https:"))
            {
                LogService.logIt( "BaseDiscGroupReportTemplate.initFonts() BBB.1 NONFATAL error getting custLogo. Will try http instead of https. logo=" + logoUrl );

                String logo2 = "http:" + logoUrl.trim().substring(6, logoUrl.length());

                try
                {
                    custLogo = ITextUtils.getITextImage( HttpUtils.getURLFromString( logo2 ) );
                    // custLogo = getImageInstance(logo2, reportData.te.getTestEventId()); // ITextUtils.getITextImage( com.tm2score.util.HttpUtils.getURLFromString( logo2 ) );
                }
                catch( IOException ee )
                {
                    custLogo=null;
                    int orgId=reportData==null || reportData.getOrg()==null ? 0 : reportData.getOrg().getOrgId();                    
                    LogService.logIt( "BaseDiscGroupReportTemplate.initFonts() BBB.3 NONFATAL error getting custLogo using http. OrgId=" + orgId + ". Will use null. logo=" + logoUrl + ", logo2=" + logo2 );
                }
                catch( Exception ee )
                {
                    custLogo=null;
                    int orgId=reportData==null || reportData.getOrg()==null ? 0 : reportData.getOrg().getOrgId();
                    LogService.logIt( ee, "BaseDiscGroupReportTemplate.initFonts() BBB.6 NONFATAL error getting custLogo using http. OrgId=" + orgId + ". Will use null. logo=" + logoUrl + ", logo2=" + logo2 );
                }
            }

            else
            {
                LogService.logIt( "BaseDiscGroupReportTemplate.initFonts() CCC.1 NONFATAL error getting custLogo. Will use null. logo= " + logoUrl + ", Exception=" + e.toString() );
            }
        }        
    }

    

    @Override
    public void init( ReportData rd ) throws Exception
    {
        reportData = rd; // new ReportData( rd.getTestKey(), rd.getTestEvent(), rd.getReport(), rd.getUser(), rd.getOrg() );

        discDataSet = (DiscGroupDataSet) rd.objArray[0];
        // ctReportData = new CT2ReportData();

        XXLFONTSZ = 32;
        XLFONTSZ = 18;
        LLFONTSZ = 15;
        LFONTSZ = 14;
        LMFONTSZ = 13;
        FONTSZ = 12;
        SFONTSZ = 11;
        XSFONTSZ = 10;
        XXSFONTSZ = 9;
        
        
        initFonts();
        
        initColors(); 
                        
        document = new Document( PageSize.LETTER );

        baos = new ByteArrayOutputStream();

        pdfWriter = PdfWriter.getInstance(document, baos);

        // Must come before create HeaderFooter
        specialInit();

        DiscHeaderFooter hdr = new DiscHeaderFooter( document, rd.getLocale(), rd.getReportName(), reportData, this, custLogo, groupReportFooterImageUrls );
        
        pdfWriter.setPageEvent(hdr);

        document.open();

        document.setMargins(36, 36, 36, 36 );

        pageWidth = document.getPageSize().getWidth();
        pageHeight = document.getPageSize().getHeight();

        float[] hghts = hdr.getHeaderFooterHeights( pdfWriter );

        headerHgt = hghts[0];
        footerHgt = hghts[1];

        usablePageHeight = pageHeight - headerHgt - footerHgt - 4*PAD;


        // LogService.logIt( "BaseUMinnJusticeReportTemplate.init() pageDims=" + pageWidth + "," + pageHeight + ", margins: " + document.topMargin() + "," + document.rightMargin() + "," + document.bottomMargin() + "," + document.leftMargin() );

        dataTableEvent = new TableBackgroundEvent( BaseColor.LIGHT_GRAY , 0.2f, BaseColor.WHITE );

        tableHeaderRowEvent = new TableBackgroundEvent( null , 0, getTablePageBgColor() );
        
        
    }
    
    public synchronized void specialInit()
    {
        if( discReportUtils==null )
        {
            if( reportData.getReport().getStrParam6() !=null && !reportData.getReport().getStrParam6().isEmpty() )
                bundleToUse = reportData.getReport().getStrParam6();

            if( bundleToUse==null || bundleToUse.isEmpty() )
            {
                Locale loc = reportData.getLocale();
                // String stub = "";
                if( loc.getLanguage().equalsIgnoreCase( "en" ) )
                    bundleToUse = "discreport.properties";
                else
                    bundleToUse = "discreport_" + loc.getLanguage().toLowerCase() + ".properties";
            }

            discReportUtils = new DiscReportUtils( bundleToUse );
        }


        // LogService.logIt( "BaseDiscGroupReportTemplate.specialInit() groupReportCoverImageUrl=" + groupReportCoverImageUrl );

        try
        {
            URL u = reportData.getLocalImageUrl(groupReportCoverImageUrl);
            if( u!=null )
            {
                hraCoverPageImage2 = ITextUtils.getITextImage(u );
            }
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseDiscGroupReportTemplate.specialInit() reportId=" + (reportData!=null && reportData.r!=null ? reportData.r.getReportId() : "null") + ", groupReportCoverImageUrl=" + groupReportCoverImageUrl );
        }
    }
    
    
    @Override
    public Locale getReportLocale()
    {
        if( this.reportData!=null )
            return reportData.getLocale();
        
        return Locale.US;
    }

        
        
    public float addTitle( float startY, String title, String subtitle ) throws Exception
    {
        try
        {
            //if( !reportData.getIsLTR() )
            //    return addTitleRTL( startY,  title,  subtitle );

            if( startY > 0 )
            {
                float ulY = startY - 16* PAD;

                if( ulY < footerHgt + 3*PAD )
                {
                    document.newPage();

                    startY = 0;
                }
            }

            previousYLevel =  currentYLevel;

            Font fnt =   getHeaderFontXLarge();

            float y = startY>0 ? startY - fnt.getSize() - TPAD :  pageHeight - headerHgt - fnt.getSize() - TPAD;
            // float y = startY>0 ? startY - fnt.getSize() - 2*PAD :  pageHeight - headerHgt - fnt.getSize() - 2*PAD;

            // float y = previousYLevel - 6*PAD - getFont().getSize();

            // Add Title
            ITextUtils.addDirectText( pdfWriter, title, CT2_MARGIN + CT2_TEXT_EXTRAMARGIN, y, reportData.getIsLTR() ? Element.ALIGN_LEFT : Element.ALIGN_RIGHT, fnt, false);

            // No subtitle
            if( subtitle==null || subtitle.isEmpty() )
                return y;

            // Change getFont()
            fnt =  getFontLarge();

            float leading = fnt.getSize();

            float spaceLeft = y - PAD - footerHgt;

            // LogService.logIt( "BaseUMinnJusticeReportTemplate.addAssessmentOverview() y for title=" + y + ", spaceleft=" + spaceLeft );

            float txtW = pageWidth - 2*CT2_MARGIN-2*CT2_TEXT_EXTRAMARGIN;

            float txtHght = ITextUtils.getDirectTextHeight( pdfWriter, subtitle, txtW, reportData.getIsLTR() ? Element.ALIGN_LEFT : Element.ALIGN_RIGHT, leading, fnt);

             y -=  PAD;//  + fnt.getSize(); // + txtHght; //   1.5*PAD + txtHght - 1.1*fnt.getSize();
            // y -=  getHeaderFontXLarge().getSize();//  + fnt.getSize(); // + txtHght; //   1.5*PAD + txtHght - 1.1*fnt.getSize();

            // LogService.logIt( "BaseUMinnJusticeReportTemplate.addAssessmentOverview() y for TEXT BOX=" + y + ", spaceleft=" + spaceLeft + ", txtHght=" + txtHght );

            // float txtW = pageWidth - 2*CT2_MARGIN - 2*CT2_TEXT_EXTRAMARGIN; // - 4*PAD;

            float txtLlx = CT2_MARGIN + CT2_TEXT_EXTRAMARGIN; // + 2*PAD;
            float txtUrx = CT2_MARGIN + CT2_TEXT_EXTRAMARGIN + txtW;

            // if have room, draw it here. Otherwise, need to use columnText. If RTL need to use Column Text anyway.
            if( reportData.getIsLTR() && txtHght <= spaceLeft )
            {
                // y -= txtHght;
                Rectangle rect = new Rectangle( txtLlx, y-txtHght, txtUrx, y  );

                // LogService.logIt( "BaseUMinnJusticeReportTemplate.addAssessmentOverview() rect.left=" + rect.getLeft() + " rect.bottom=" + rect.getBottom()+ ", right=" + rect.getRight() + ", rect.top=" + rect.getTop() + ", " + rect.toString() );

                ITextUtils.addDirectText(  pdfWriter, subtitle, rect, reportData.getIsLTR() ? Element.ALIGN_LEFT : Element.ALIGN_RIGHT, leading, fnt, false );

                // LogService.logIt( "BaseUMinnJusticeReportTemplate.addAssessmentOverview() enough space is avail on page txtHght=" + txtHght + " vs spaceLeft=" + spaceLeft + ", currentYLevelBefore=" + currentYLevel + ", currentLevelAfter=y=" + (y-txtHght) );
                currentYLevel = y - txtHght;

                return currentYLevel;
            }

            else
            {
                LogService.logIt( "BaseUMinnJusticeReportTemplate.addAssessmentOverview() RTL or overview text height is too high at " + txtHght + " vs spaceLeft=" + spaceLeft + ", using columns." );

                // llx,lly,urx,ury
                Rectangle colDims1 = new Rectangle( txtLlx, footerHgt + 2*PAD, txtUrx, footerHgt + spaceLeft );

                // LogService.logIt( "BaseUMinnJusticeReportTemplate.addAssessmentOverview() col1 dims=" + colDims1.getLeft() + "," + colDims1.getBottom() + " - " + colDims1.getRight() + "," + colDims1.getTop() );

                //float c2h = txtHght - spaceLeft;

                Rectangle colDims2 = new Rectangle( txtLlx, footerHgt + 2*PAD, txtUrx, pageHeight - headerHgt - 2*PAD );

                // LogService.logIt( "BaseUMinnJusticeReportTemplate.addAssessmentOverview() col2 dims=" + colDims2.getLeft() + "," + colDims2.getBottom() + " - " + colDims2.getRight() + "," + colDims2.getTop() );

                ColumnText ct = new ColumnText( pdfWriter.getDirectContent() );

                setRunDirection( ct );

                Phrase p = new Phrase( subtitle, fnt );

                // p.setLeading( leading );
                ct.setLeading( leading ); // fnt.getSize() );

                ct.addText( p );

                ct.setSimpleColumn( colDims1.getLeft(), colDims1.getBottom(), colDims1.getRight(), colDims1.getTop() );
                // ct.setSimpleColumn( colDims1 );

                int status = ct.go();

                while( ColumnText.hasMoreText(status) )
                {
                    LogService.logIt( "BaseUMinnJusticeReportTemplate.addAssessmentOverview() adding second column "  );

                    document.newPage();

                    ct.setSimpleColumn( colDims2.getLeft(), colDims2.getBottom(), colDims2.getRight(), colDims2.getTop() );

                    ct.setYLine( colDims2.getTop() );

                    status = ct.go();

                    currentYLevel = ct.getYLine();
                }


                return currentYLevel;
            }
        }

        catch( Exception e )
        {
            LogService.logIt( e, "BaseUMinnJusticeReportTemplate.addTitleAndSubtitle()" );

            throw new STException( e );
        }
    }
    
 
    public void addCoverPage( boolean includeDescriptiveText ) throws Exception
    {
        try
        {

            if( reportData.getReportRuleAsBoolean( "covrdescripoff" ) )
                includeDescriptiveText = false;

            float y = pageHeight - getHraLogoBlackText().getScaledHeight() - 50;  // ( (pageHeight - t.getTotalHeight() )/2 ) - cfLogo.getHeight() )/2 - cfLogo.getHeight();
            // java.util.List<Chunk> cl = new ArrayList<>();

            boolean omitCoverImages = reportData.getReportRuleAsBoolean( "omitcoverimages" );

            boolean coverInfoOk = !reportData.getReportRuleAsBoolean( "omitcoverinfopdf" );

            String reportCompanyName = reportData.getOrgName();
            if( getSampleReport() )
            {
                reportCompanyName = SampleReportUtils.getCompanyName();
                // custLogo = SampleReportUtils.getCustLogoImage();
            }

            //if( reportCompanyName==null || reportCompanyName.isEmpty() )
            //    reportCompanyName = reportData.getOrgName();

            if( StringUtils.isCurlyBracketed( reportCompanyName ) )
                reportCompanyName = "                        ";

            //String reportCompanyAdminName = discDataSet!=null && discDataSet.getAuthUser()!=null ? discDataSet.getAuthUser().getFullname() : null; //   reportData==null ? null : reportData.getReportCompanyAdminName();

            //if( reportData.getTestKey().getAuthUser() != null && (reportCompanyAdminName==null || reportCompanyAdminName.isEmpty())  )
            //    reportCompanyAdminName = reportData.getTestKey().getAuthUser().getFullname();

            //if( StringUtils.isCurlyBracketed( reportCompanyAdminName ) )
            //    reportCompanyAdminName = "                        ";

            //if( reportCompanyAdminName != null && reportCompanyAdminName.contains("AUTOGEN") )
            //    reportCompanyAdminName = null;

            boolean includeCompanyInfo = reportCompanyName!=null && !reportData.getReportRuleAsBoolean( "companyinfooff" ); // ==null || !reportData.getReportRule( "ct3excludepreparedfor" ).equalsIgnoreCase( "1" ) );
            if( !includeCompanyInfo )
            {
                reportCompanyName = "";
                custLogo = null;
            }

            boolean compNameForAdmin = reportData.getReportRuleAsBoolean("compnameforprep") && includeCompanyInfo;

            LogService.logIt( "BaseDiscGroupReportTemplate.addCoverPage()  CCC.1 includeCompanyInfo=" + includeCompanyInfo + ", reportCompanyName=" + reportCompanyName + ", compNameForAdmin=" + compNameForAdmin + ", omitCoverImages=" + omitCoverImages );

            if( compNameForAdmin && !reportData.hasCustLogo() )
                includeCompanyInfo = false;


            boolean includeDates = !reportData.getReportRuleAsBoolean( "hidedatespdf" );

            // boolean includePreparedFor = includeCompanyInfo && reportCompanyAdminName!=null && !reportData.getReportRuleAsBoolean( "ct3excludepreparedfor" ); // ==null || !reportData.getReportRule( "ct3excludepreparedfor" ).equalsIgnoreCase( "1" ) );

            boolean sports = includeCompanyInfo && reportData.getReportRuleAsBoolean( "sportstest" ); // ==null || !reportData.getReportRule( "ct3excludepreparedfor" ).equalsIgnoreCase( "1" ) );

            if( sports || reportData.getReportRuleAsBoolean( "legacycoverpage" ) )
            {
                addCoverPage(includeDescriptiveText);
                return;
            }

            Image hraCover = getHraCoverPageImage();

            // LogService.logIt( "BaseDiscGroupReportTemplate.addCoverPageV2() START page dims=" + pageWidth + "," + pageHeight + ", imageDims=" + hraCover.getWidth() + "," + hraCover.getHeight() );

            hraCover.scalePercent( 100*pageHeight/hraCover.getHeight());

            if( !omitCoverImages )
            {
                ITextUtils.addDirectImage( pdfWriter, hraCover, pageWidth-hraCover.getScaledWidth() + 1, 0, true );

                Image hraCover2 = getHraCoverPageImage2();
                LogService.logIt( "BaseDiscGroupReportTemplate.addCoverPageV2() hraCover2=" + (hraCover2==null ? "null" : "not null") );
                if( hraCover2 !=null )
                {
                    hraCover2.scalePercent(64);
                    ITextUtils.addDirectImage( pdfWriter, hraCover2, pageWidth-hraCover2.getScaledWidth() + 1, 0, true );
                }
            }

            boolean clientLogoInHeader = reportData.getReportRuleAsBoolean( "clientlogopdfhdr" ) && (includeCompanyInfo || compNameForAdmin) && reportData.hasCustLogo();

            if( !clientLogoInHeader && getSampleReport() )
            {
                custLogo = SampleReportUtils.getCustLogoImage();
            }

            if( clientLogoInHeader )
            {
                //float lwid = custLogo.getScaledWidth();
                ITextUtils.addDirectImage( pdfWriter, custLogo, 2*CT2_MARGIN, y, false );
            }
            else if( !reportData.getReportRuleAsBoolean( "hidehralogoinreports" ))
                ITextUtils.addDirectImage( pdfWriter, getHraLogoBlackText(), 2*CT2_MARGIN, y, false );

            // LogService.logIt( "BaseDiscGroupReportTemplate.addCoverPage()  CCC.2 includePreparedFor=" + includePreparedFor + ", clientLogoInHeader=" + clientLogoInHeader  +", reportData.hasCustLogo()=" + reportData.hasCustLogo() + ", custLogo!=null " + (custLogo!=null));

            String reportTitle = reportData.getReportName();

            BaseFont baseTitleFont = baseFontCalibriBold;

            int titleFontHeight = 56;

            //if( 1==2 )
            //    reportTitle = "Test Results and Interview Guilde";

            if( reportTitle.length()>40)
                titleFontHeight = 36;

            else if( reportTitle.length()>32)
                titleFontHeight = 44;

            else if( reportTitle.length()>24)
                titleFontHeight = 48;

            else if( reportTitle.length()>16)
                titleFontHeight = 52;

            Font titleFont = new Font(baseTitleFont, titleFontHeight );
            titleFont.setColor(ct2Colors.hraBlue);
            String reportSubtitle = reportData.getR2Use().getStrParam5()!=null && !reportData.getR2Use().getStrParam5().isBlank() ? reportData.getR2Use().getStrParam5() : null;

            //if( 1==2 )
            //    reportSubtitle="This is a dummy subtitle for report";

            PdfPTable t = new PdfPTable( 2 );

            t.setWidths(reportData.getIsLTR() ?  new float[] {3,9}: new float[] {9,3} );
            t.setTotalWidth( (pageWidth-2*CT2_MARGIN)*0.8f);
            t.setLockedWidth( true );
            setRunDirection(t);

            PdfPCell c = t.getDefaultCell();
            c.setPadding( 0 );
            c.setPaddingRight( 15 );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderWidth( 0 );
            c.setVerticalAlignment(Element.ALIGN_TOP);
            setRunDirection(c);


            c = new PdfPCell( new Phrase( reportTitle, titleFont ) );
            c.setColspan(2);
            c.setBorder(Rectangle.NO_BORDER);
            c.setBorderWidth( 0 );
            c.setVerticalAlignment(Element.ALIGN_TOP);
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPaddingBottom(3);
            setRunDirection(c);
            t.addCell(c);

            if( reportSubtitle !=null && !reportSubtitle.isBlank() )
            {
                Font subtitleFont = new Font(baseTitleFont, 12 );
                subtitleFont.setColor(ct2Colors.hraBlue);

                c = new PdfPCell( new Phrase( reportSubtitle, subtitleFont ) );
                c.setColspan(2);
                c.setBorder(Rectangle.NO_BORDER);
                c.setBorderWidth( 0 );
                c.setVerticalAlignment(Element.ALIGN_TOP);
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setPaddingBottom(3);
                setRunDirection(c);
                t.addCell(c);
            }

            // Blue Bar
            c = new PdfPCell( new Phrase( "\n\n\n\n", font ) );
            c.setColspan(2);
            c.setBorder(Rectangle.NO_BORDER);
            c.setBorderWidth( 0 );
            setRunDirection(c);

            if( !omitCoverImages )
                c.setCellEvent( new CoverBlueBarCellEvent() );
            t.addCell(c);

            // String testTakerTitle = lmsg( "g.PreparedForC" );

            Font cpFont = fontXLarge;

            // Name stuff
            c = t.getDefaultCell();
            c.setPadding( 0 );
            c.setPaddingRight( 15 );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderWidth( 0 );
            c.setVerticalAlignment(Element.ALIGN_TOP);
            setRunDirection(c);

            if( coverInfoOk )
            {
                t.addCell(new Phrase( lmsg( "g.Org" ) + ":" , cpFont ) );
                t.addCell(new Phrase( getSampleReport() ? SampleReportUtils.getCompanyName() : discDataSet.getOrg().getName(), cpFont ) );

                if( discDataSet.getSuborg()!=null )
                {
                    t.addCell(new Phrase( lmsg( "g.Suborg" ) + ":" , cpFont ) );
                    t.addCell(new Phrase( getSampleReport() ? SampleReportUtils.getRandomSuborg() : discDataSet.getSuborg().getName(), cpFont ) );
                }
                
                if( discDataSet.getProduct()!=null )
                {
                    t.addCell(new Phrase( lmsg( "g.Assessment" ) + ":" , cpFont ) );
                    t.addCell(new Phrase( discDataSet.getProduct().getName(), cpFont ) );
                }
                
                if( discDataSet.getOrgAutoTest()!=null )
                {
                    t.addCell(new Phrase( lmsg( "g.OrgAutoTest" ) + ":" , cpFont ) );
                    t.addCell(new Phrase( discDataSet.getOrgAutoTest().getName(), cpFont ) );
                }

                if( discDataSet.getAltIdentifier()!=null && !discDataSet.getAltIdentifier().isBlank() )
                {
                    t.addCell(new Phrase( lmsg( "g.AltIdentifier" ) + ":" , cpFont ) );
                    t.addCell(new Phrase( discDataSet.getAltIdentifier(), cpFont ) );
                }
                
                if( discDataSet.getCustom1()!=null && !discDataSet.getCustom1().isBlank() )
                {
                    String cn = discDataSet.getCustom1Name()!=null && !discDataSet.getCustom1Name().isBlank() ? discDataSet.getCustom1Name() : lmsg( "g.Custom1" );
                    t.addCell(new Phrase( cn + ":" , cpFont ) );
                    t.addCell(new Phrase( discDataSet.getCustom1(), cpFont ) );
                }
                
                if( discDataSet.getCustom2()!=null && !discDataSet.getCustom2().isBlank() )
                {
                    String cn = discDataSet.getCustom2Name()!=null && !discDataSet.getCustom2Name().isBlank() ? discDataSet.getCustom2Name() : lmsg( "g.Custom2" );
                    t.addCell(new Phrase( cn + ":" , cpFont ) );
                    t.addCell(new Phrase( discDataSet.getCustom2(), cpFont ) );
                }
                
                if( discDataSet.getCustom3()!=null && !discDataSet.getCustom3().isBlank() )
                {
                    String cn = discDataSet.getCustom3Name()!=null && !discDataSet.getCustom3Name().isBlank() ? discDataSet.getCustom3Name() : lmsg( "g.Custom3" );
                    t.addCell(new Phrase( cn + ":" , cpFont ) );
                    t.addCell(new Phrase( discDataSet.getCustom3(), cpFont ) );
                }
                
                if( includeDates )
                {
                    t.addCell(new Phrase( lmsg( "g.Dates" ) + ":" , cpFont ) );
                    t.addCell(new Phrase( I18nUtils.getFormattedDate(reportData.getLocale(), discDataSet.getStartDate(), DateFormat.SHORT) + "-" + I18nUtils.getFormattedDate(reportData.getLocale(), discDataSet.getEndDate(), DateFormat.SHORT), cpFont ) );
                }
            }

            float lowerTableAdj = 0;
            float lineHeight = 10;

            if( includeCompanyInfo )
            {
                //if( coverInfoOk || (reportData.hasCustLogo() && custLogo!=null && !clientLogoInHeader) )
                //    t.addCell(new Phrase( coverInfoOk && (devel || sports) ? lmsg( "g.SponsoredByC" ) : "" , cpFont ) );

                t.addCell(new Phrase( "" , cpFont ) );
                
                if( reportData.hasCustLogo() && custLogo!=null && !clientLogoInHeader )
                {
                    float imgSclW=100;
                    float imgSclH = 100;

                    if( custLogo.getWidth() > MAX_CUSTLOGO_W_V2 )
                        imgSclW = 100 * MAX_CUSTLOGO_W_V2/custLogo.getWidth();

                    if( custLogo.getHeight() > MAX_CUSTLOGO_H_V2 )
                        imgSclH = 100 * MAX_CUSTLOGO_H_V2/custLogo.getHeight();

                    imgSclW = Math.min( imgSclW, imgSclH );

                    if( imgSclW < 100 )
                        custLogo.scalePercent( imgSclW );

                    c = new PdfPCell( custLogo );
                    c.setBorder( Rectangle.NO_BORDER );
                    c.setHorizontalAlignment(Element.ALIGN_LEFT );
                    c.setPadding( 0 );
                    c.setPaddingTop( 12 );
                    setRunDirection(c);
                    t.addCell( c );

                    lowerTableAdj += custLogo.getScaledHeight() + 12 - lineHeight;
                }

                else if( coverInfoOk )
                    t.addCell( new Phrase( reportCompanyName, fontXLarge ) );
            }

            float tableH = t.calculateHeights(); //  + 500;
            float tableY = pageHeight-175;

            // float tableW = t.getTotalWidth();

            float tableX = 2*CT2_MARGIN; // (pageWidth - tableW)/2;

            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );

            y = tableY - tableH;

            List<String> whatsContained = new ArrayList<>();

            String includedCustom = reportData.getReportRuleAsString("includedinreportall" );  //
            if( includedCustom!=null && !includedCustom.isBlank() )
            {
                for( String ss : includedCustom.split("~") )
                {
                    if( ss.isBlank() )
                        continue;
                    whatsContained.add( ss.trim() );
                }
            }

            else
            {
                includedCustom = reportData.getReportRuleAsString("includedinreporttop" );  //
                if( includedCustom!=null && !includedCustom.isBlank() )
                {
                    for( String ss : includedCustom.split("~") )
                    {
                        if( ss.isBlank() )
                            continue;
                        whatsContained.add( ss.trim() );
                    }
                }
                
                if( whatsContained.isEmpty() )
                {
                    whatsContained.add( lmsg("g.ResultsOverview") );
                    whatsContained.add( lmsg("g.TmBldActivities") );                    
                }

            }

            includedCustom = reportData.getReportRuleAsString("includedinreportbot" );  //
            if( includedCustom!=null && !includedCustom.isBlank() )
            {
                for( String ss : includedCustom.split("~") )
                {
                    if( ss.isBlank() )
                        continue;
                    whatsContained.add( ss.trim() );
                }
            }

            if( whatsContained.size()>3 )
                lowerTableAdj += (whatsContained.size()-3)*lineHeight;

            int returnCt = 0;

            if( includeDescriptiveText )
            {
                if( coverDescrip!= null &&  !coverDescrip.isEmpty() )
                {}

                else if( reportData.getReport()!=null && reportData.getReport().getTextParam1()!=null && !reportData.getReport().getTextParam1().isBlank() )
                {
                    coverDescrip = reportData.getReport().getTextParam1();                   
                }

                else
                {
                    String coverDetailKey = reportData.getReport()!=null && reportData.getReport().getStrParam1()!=null && !reportData.getReport().getStrParam1().isBlank() ? reportData.getReport().getStrParam1() : "g.CT2CoverDescrip";
                    coverDescrip = lmsg( coverDetailKey, new String[] {} );
                    LogService.logIt(  "BaseDiscGroupReportTemplate.addCoverPage() coverDetailKey=" + coverDetailKey + ", coverDescrip=" + coverDescrip );
                }

                if( coverDescrip!=null )
                {
                    returnCt++;

                    int idx = coverDescrip.indexOf("\n" );
                    while( idx>=0 )
                    {
                        returnCt++;
                        idx = coverDescrip.indexOf("\n" , idx+1);
                    }
                }

                // count \n's in coverDescrip
            }

            Paragraph descripPar = null;

            if( includeDescriptiveText )
            {
                Font f = fontLm;
                Font fb = fontLmBold;

                if( coverDescrip!=null && coverDescrip.length()>=900 )
                {
                    f = fontSmall;
                    fb = fontSmallBold;
                }
                else if( coverDescrip!=null && coverDescrip.length()>=600 )
                {
                    f = font;
                    fb = fontBold;
                }

                if( coverDescrip!=null && coverDescrip.length()>=800 )
                    lowerTableAdj += 2*lineHeight;

                descripPar = new Paragraph();
                Chunk chk = new Chunk( lmsg("g.Cvr2ImportantNote") + ": ", fb );
                descripPar.add(chk);
                chk =  new Chunk( coverDescrip, f );
                descripPar.add(chk);
            }

            boolean addTable = false;
            t = new PdfPTable( 2 );
            t.setWidths( new float[] {0.1f, 4f} );
            t.setTotalWidth( (pageWidth-2*CT2_MARGIN)*( !omitCoverImages && this.getHraCoverPageImage2()!=null ? 0.4f : 0.7f));
            t.setLockedWidth( true );
            setRunDirection(t);


            if( !whatsContained.isEmpty() )
            {
                addTable = true;

                c = new PdfPCell( new Phrase( "\n\n\n" , this.getFontXLargeBold()) );
                c.setBorder( Rectangle.NO_BORDER );
                c.setPadding(0);
                c.setColspan(2);
                if( !omitCoverImages )
                    c.setCellEvent( new CoverIncludedBarCellEvent(lmsg("g.Cvr2WhatsIncluded") , this.getFontXLargeBold()));
                setRunDirection(c);
                t.addCell( c );

                for( String inc : whatsContained )
                {
                    c = new PdfPCell( new Phrase( "\u2022" , fontLm ) );
                    c.setBorder( Rectangle.NO_BORDER );
                    c.setVerticalAlignment(Element.ALIGN_TOP);
                    c.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c.setPadding(0 );
                    c.setPaddingRight(5);
                    setRunDirection(c);
                    t.addCell( c );

                    c = new PdfPCell( new Phrase( inc , fontLm ) );
                    c.setBorder( Rectangle.NO_BORDER );
                    c.setVerticalAlignment(Element.ALIGN_TOP);
                    c.setHorizontalAlignment(Element.ALIGN_LEFT);
                    c.setPadding(1 );
                    c.setPaddingLeft(2);
                    setRunDirection(c);
                    t.addCell( c );
                }
            }

            if( includeDescriptiveText && descripPar!=null )
            {
                addTable = true;

                c = new PdfPCell( descripPar );
                c.setColspan(2);
                c.setBorder( Rectangle.NO_BORDER );
                c.setPaddingTop(whatsContained.isEmpty() ? 40 : 12);
                setRunDirection(c);
                t.addCell( c );
            }

            if( addTable )
            {
                // tableW = t.getTotalWidth();
                tableH = t.calculateHeights(); //  + 500;

                // tableY = y - tableH;
                y -= Math.max( 20, (120 - lowerTableAdj));

                t.writeSelectedRows(0, -1,tableX, y, pdfWriter.getDirectContent() );
            }

            t = new PdfPTable( 1 );

            t.setTotalWidth( new float[] { pageWidth-2*CT2_MARGIN } );
            t.setLockedWidth( true );
            setRunDirection(t);

            c = t.getDefaultCell();
            c.setPadding( 0 );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderWidth( 0 );
            setRunDirection(c);


            c = new PdfPCell( new Phrase( lmsg( "g.ProprietaryAndConfidential" ) , getFont() ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( !omitCoverImages && this.getHraCoverPageImage2()!=null ? Element.ALIGN_LEFT : Element.ALIGN_CENTER );
            if( !omitCoverImages && getHraCoverPageImage2()!=null )
                c.setPaddingLeft( 50 );
            setRunDirection(c);
            t.addCell( c );

            // tableH = t.calculateHeights(); //  + 500;

            tableY = 20;
            float tableW = t.getTotalWidth();
            tableX = (pageWidth - tableW)/2;

            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );


        }

        catch( DocumentException e )
        {
            LogService.logIt( e, "BaseDiscGroupReportTemplate.addCoverPage()" );
        }
    }
    
    
    public void addReportInfoHeader() throws Exception
    {
        try
        {
            previousYLevel =  currentYLevel;

            // Font fnt = getFontXLarge();
            if( reportData.getReportRuleAsBoolean( "ovroff" ) )
                return;

            // boolean hideOverallNumeric = reportData.getReportRuleAsBoolean( "ovrnumoff" );
            // boolean hideOverallGraph = reportData.getReportRuleAsBoolean( "ovrgrphoff" );
            boolean includeDates = !reportData.getReportRuleAsBoolean( "hidedatespdf" );

            float y = addTitleLarge( previousYLevel, lmsg( "g.Overall" ), getFontXXLargeBoldDarkBlue() );

            y -= TPAD;

            int scrDigits = reportData.getReport().getIntParam2() >= 0 ? reportData.getReport().getIntParam2() : 1;

            float[] colRelWids = new float[] { 1f };

            // boolean includeNumScores = !hideOverallNumeric; // reportData.getReport().getIncludeSubcategoryNumeric()==1;
            boolean includeColorGraph = true; // !hideOverallGraph; //  && sft.getSupportsBarGraphic(reportData.getReport()) && reportData.getReport().getIncludeColorScores()==1; // && reportData.getReport().getIncludeCompetencyColorScores()==1;
            //String thirdPartyId = reportData.getThirdPartyTestEventIdentifier();
            // boolean hasThirdPartyId = thirdPartyId!=null && !thirdPartyId.isEmpty();

            String scrTxt = lmsg( "g.GroupScoresBreakdown" ); //getTestEvent().getOverallTestEventScore().getScoreText();

            // First create the table
            PdfPCell c;

            // First, add a table
            PdfPTable touter = new PdfPTable( 1 );

            setRunDirection( touter );
            touter.setTotalWidth( pageWidth ); // - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN );
            touter.setWidths( colRelWids );
            touter.setLockedWidth( true );

            // Create header
            c = new PdfPCell( new Phrase( lmsg( "g.Group"), fontLargeWhite ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setBorderWidth( scoreBoxBorderWidth );
            c.setPadding( 1 );
            c.setPaddingBottom( 5 );
            c.setPaddingLeft( CT2_BOXHEADER_LEFTPAD + CT2_MARGIN + CT2_TEXT_EXTRAMARGIN );
            c.setBackgroundColor( ct2Colors.hraBlue );
            // c.setCellEvent(new CellBackgroundCellEvent(reportData.getIsLTR(), ct2Colors.hraBlue,true, true, true, true) );
            setRunDirection( c );
            touter.addCell(c);

            touter.writeSelectedRows(0, -1,0, y, pdfWriter.getDirectContent() );
            currentYLevel = y - touter.calculateHeights() - 5;

            y = currentYLevel;


            // Inner Table

            touter = new PdfPTable( 2 );
            
            touter.setTotalWidth( (pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN)*0.6f );
            colRelWids = new float[] { 1f, 1f };
            touter.setWidths( colRelWids );
            // touter.setLockedWidth( true );

            c = touter.getDefaultCell();
            c.setPadding( 2 );
            c.setBorder( Rectangle.NO_BORDER );
            setRunDirection( c );

            // Spacer
            c = new PdfPCell( new Phrase( "", fontXSmall ) );
            c.setFixedHeight( 2 );
            c.setColspan(2);
            c.setBorder( Rectangle.LEFT | Rectangle.RIGHT );
            c.setBorderColor( BaseColor.WHITE );
            c.setBorderWidth( scoreBoxBorderWidth );
            setRunDirection( c );
            touter.addCell( c );

            Font cpFont = fontLm;
            
            touter.addCell(new Phrase( lmsg( "g.Org" ) + ":" , cpFont ) );
            touter.addCell(new Phrase( getSampleReport() ? SampleReportUtils.getCompanyName() : discDataSet.getOrg().getName(), cpFont ) );

            if( discDataSet.getSuborg()!=null )
            {
                touter.addCell(new Phrase( lmsg( "g.Suborg" ) + ":" , cpFont ) );
                touter.addCell(new Phrase( getSampleReport() ? SampleReportUtils.getRandomSuborg() : discDataSet.getSuborg().getName(), cpFont ) );
            }

            if( discDataSet.getProduct()!=null )
            {
                touter.addCell(new Phrase( lmsg( "g.Assessment" ) + ":" , cpFont ) );
                touter.addCell(new Phrase( discDataSet.getProduct().getName(), cpFont ) );
            }

            if( discDataSet.getOrgAutoTest()!=null )
            {
                touter.addCell(new Phrase( lmsg( "g.OrgAutoTest" ) + ":" , cpFont ) );
                touter.addCell(new Phrase( discDataSet.getOrgAutoTest().getName(), cpFont ) );
            }

            if( discDataSet.getAltIdentifier()!=null && !discDataSet.getAltIdentifier().isBlank() )
            {
                touter.addCell(new Phrase( lmsg( "g.AltIdentifier" ) + ":" , cpFont ) );
                touter.addCell(new Phrase( discDataSet.getAltIdentifier(), cpFont ) );
            }

            if( discDataSet.getCustom1()!=null && !discDataSet.getCustom1().isBlank() )
            {
                String cn = discDataSet.getCustom1Name()!=null && !discDataSet.getCustom1Name().isBlank() ? discDataSet.getCustom1Name() : lmsg( "g.Custom1" );
                touter.addCell(new Phrase( cn + ":" , cpFont ) );
                touter.addCell(new Phrase( discDataSet.getCustom1(), cpFont ) );
            }

            if( discDataSet.getCustom2()!=null && !discDataSet.getCustom2().isBlank() )
            {
                String cn = discDataSet.getCustom2Name()!=null && !discDataSet.getCustom2Name().isBlank() ? discDataSet.getCustom2Name() : lmsg( "g.Custom2" );
                touter.addCell(new Phrase( cn + ":" , cpFont ) );
                touter.addCell(new Phrase( discDataSet.getCustom2(), cpFont ) );
            }

            if( discDataSet.getCustom3()!=null && !discDataSet.getCustom3().isBlank() )
            {
                String cn = discDataSet.getCustom3Name()!=null && !discDataSet.getCustom3Name().isBlank() ? discDataSet.getCustom3Name() : lmsg( "g.Custom3" );
                touter.addCell(new Phrase( cn + ":" , cpFont ) );
                touter.addCell(new Phrase( discDataSet.getCustom3(), cpFont ) );
            }

            if( includeDates )
            {
                touter.addCell(new Phrase( lmsg( "g.Dates" ) + ":" , cpFont ) );
                touter.addCell(new Phrase( I18nUtils.getFormattedDate(reportData.getLocale(), discDataSet.getStartDate(), DateFormat.SHORT) + "-" + I18nUtils.getFormattedDate(reportData.getLocale(), discDataSet.getEndDate(), DateFormat.SHORT), cpFont ) );
            }

            touter.addCell(new Phrase( lmsg( "g.TotalParticipants" ) + ":" , cpFont ) );
            touter.addCell(new Phrase( Integer.toString(discDataSet.getTotalCount()), cpFont ) );
            

            if( scrTxt != null && !scrTxt.isEmpty())
            {
                c = new PdfPCell( new Phrase( scrTxt, fontXLargeBoldDarkBlue ) );
                c.setColspan(2);
                c.setPadding( 5 );
                c.setBorder(0);
                //c.setPaddingBottom( 6 );
                c.setPaddingLeft( CT2_BOXHEADER_LEFTPAD );
                c.setPaddingRight( CT2_BOXHEADER_LEFTPAD );
                // c.setHorizontalAlignment( Element.ALIGN_CENTER );
                // c.setPaddingLeft( 100 );

                c.setBorderColor( ct2Colors.scoreBoxBorderColor );
                c.setBorderWidth( scoreBoxBorderWidth );
                setRunDirection( c );
                touter.addCell( c );
            }

            touter.writeSelectedRows(0, -1,CT2_MARGIN + CT2_TEXT_EXTRAMARGIN, y, pdfWriter.getDirectContent() );
            currentYLevel = y - touter.calculateHeights();
            previousYLevel = currentYLevel;

            y = previousYLevel - TPAD;

            if( includeColorGraph )
            {
                //      Map of  stubletter :
                //                 obj[0] = Name
                //                 obj[1] = value
                Map<String,Object[]> scoreValMap = getGroupPercentageScoreValMap();

                Map<String,Object[]> twoLevelScoreValMap = getGroupTwoLevelPercentageScoreValMap();
                
                float colWid = (pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN)*0.6f;
                float legendWid = (pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN)*0.5f;

                touter = new PdfPTable( 1 );
                touter.setTotalWidth( colWid );
                touter.setLockedWidth( true );

                c = touter.getDefaultCell();
                c.setPadding( 0 );
                c.setBorder( Rectangle.NO_BORDER );
                setRunDirection( c );

                c = new PdfPCell( new Phrase("") ); // new PdfPCell( summaryCatNumericAxis );
                c.setBorder( Rectangle.NO_BORDER  );
                c.setBackgroundColor(BaseColor.WHITE);
                c.setPadding( 0 );
                c.setFixedHeight(280 );
                c.setCellEvent( new DiscGroupPieGraphCellEvent( scoreValMap, twoLevelScoreValMap, scrDigits, ct2Colors, reportData.getLocale(), fontLarge.getBaseFont() ) );
                touter.addCell(c);

                float newTHeight = touter.calculateHeights();

                touter.writeSelectedRows(0, -1,CT2_MARGIN + CT2_TEXT_EXTRAMARGIN + 40, y, pdfWriter.getDirectContent() );

                PdfPTable legendTable = new PdfPTable(2);
                legendTable.setWidths( new float[] {1f, 10f} );
                legendTable.setTotalWidth( legendWid );
                legendTable.setLockedWidth( true );

                c = legendTable.getDefaultCell();
                c.setPadding( 0 );
                c.setBorder( Rectangle.NO_BORDER );
                setRunDirection( c );

                BaseFont bf = font.getBaseFont();
                Font fnt;
                String styleLetter;
                Object[] data;
                String nm;
                Float val;
                Integer count;
                BaseColor bc;

                for( int i=0; i<4; i++ )
                {
                    styleLetter = DiscReportUtils.getCompetencyStubLetter(i);
                    data = scoreValMap.get( styleLetter );
                    nm = (String) data[0];
                    val = (Float) data[1];
                    count = (Integer) data[2];
                    // LogService.logIt( "BaseDiscGroupReportTemplate.addReportInfoHeader() i=" + i +", letter=" + styleLetter + ", nm=" + nm + ", val=" + val + ", count=" + count );

                    fnt = new Font(bf, this.FONTSZ);
                    bc = DiscReportUtils.sliceBaseColors[i];

                    c = new PdfPCell();
                    c.setBorder( Rectangle.NO_BORDER );
                    c.setHorizontalAlignment( Element.ALIGN_LEFT );
                    c.setVerticalAlignment( Element.ALIGN_TOP );
                    c.setBorderWidth( 0 );
                    c.setPadding( 2 );
                    c.setCellEvent(new DiscGroupLegendCellBackgroundCellEvent(bc) );
                    setRunDirection( c );
                    legendTable.addCell(c);

                    c = new PdfPCell( new Phrase( nm + " (" + count + " " + lmsg("g.Members") + ", " + I18nUtils.getFormattedNumber(reportData.getLocale(), val, 1) + "% " + lmsg("g.ofgroup") + ")", fnt ) );
                    c.setBorder( Rectangle.NO_BORDER );
                    c.setHorizontalAlignment( Element.ALIGN_LEFT );
                    c.setVerticalAlignment( Element.ALIGN_MIDDLE );
                    c.setBorderWidth( 0 );
                    c.setPadding( 6 );
                    setRunDirection( c );
                    legendTable.addCell(c);
                }

                float legendTableHeight = legendTable.calculateHeights();

                legendTable.writeSelectedRows(0, -1,CT2_MARGIN + CT2_TEXT_EXTRAMARGIN + colWid + 5, y - (legendTableHeight<newTHeight ? (newTHeight-legendTableHeight)/2f : 0), pdfWriter.getDirectContent() );

                newTHeight = Math.max( newTHeight,legendTableHeight );

                currentYLevel = y - newTHeight;
                previousYLevel = currentYLevel;

            }

        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseDiscGroupReportTemplate.addReportInfoHeader()" );
            throw e;
        }
    }

    /*
      sortTypeId
         0=By Style
         1=by last name
    
    */
    public void addScoreTable( int sortTypeId ) throws Exception
    {
        try
        {
            previousYLevel =  currentYLevel;


            float y = addTitleLarge( previousYLevel, lmsg( sortTypeId==0 ? "g.ScoresByStyle" : "g.ScoresByName" ), getFontXXLargeBoldDarkBlue() );

            y -= TPAD;

            int scrDigits = reportData.getReport().getIntParam2() >= 0 ? reportData.getReport().getIntParam2() : 1;

            // First, add a table
            PdfPTable t = new PdfPTable( 7 );

            t.setTotalWidth( pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN );
            t.setWidths( new float[]{3,3,1,1,1,1,1.5f} );
            t.setLockedWidth( true );
            setRunDirection( t );


            PdfPCell c = t.getDefaultCell();
            c.setPadding( 0 );
            c.setBorder( Rectangle.NO_BORDER );
            setRunDirection( c );


            int padding = 2;
            int padBot = 4;
            // Create header
            c = new PdfPCell( new Phrase( lmsg( "g.Name"), fontLargeWhite ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderColor( ct2Colors.scoreBoxBorderColor );
            c.setBorderWidth( scoreBoxBorderWidth );
            c.setPadding( padding );
            c.setPaddingBottom(padBot);
            c.setHorizontalAlignment( Element.ALIGN_LEFT);
            c.setCellEvent(new CellBackgroundCellEvent(reportData.getIsLTR(), ct2Colors.hraBlue,true, false, false, false) );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Phrase( lmsg( "g.Style"), fontLargeWhite ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderColor( ct2Colors.scoreBoxBorderColor );
            c.setBorderWidth( scoreBoxBorderWidth );
            c.setPadding( padding );
            c.setPaddingBottom(padBot);
            c.setHorizontalAlignment( Element.ALIGN_CENTER);
            c.setCellEvent(new CellBackgroundCellEvent(reportData.getIsLTR(), ct2Colors.hraBlue,false, false, false, false) );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Phrase( "D", fontLargeWhite ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderColor( ct2Colors.scoreBoxBorderColor );
            c.setBorderWidth( scoreBoxBorderWidth );
            c.setPadding( padding );
            c.setPaddingBottom(padBot);
            c.setHorizontalAlignment( Element.ALIGN_CENTER);
            c.setCellEvent(new CellBackgroundCellEvent(reportData.getIsLTR(), ct2Colors.hraBlue,false, false, false, false) );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Phrase( "I", fontLargeWhite ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderColor( ct2Colors.scoreBoxBorderColor );
            c.setBorderWidth( scoreBoxBorderWidth );
            c.setPadding( padding );
            c.setPaddingBottom(padBot);
            c.setHorizontalAlignment( Element.ALIGN_CENTER);
            c.setCellEvent(new CellBackgroundCellEvent(reportData.getIsLTR(), ct2Colors.hraBlue,false, false, false, false) );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Phrase( "S", fontLargeWhite ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderColor( ct2Colors.scoreBoxBorderColor );
            c.setBorderWidth( scoreBoxBorderWidth );
            c.setPadding( padding );
            c.setPaddingBottom(padBot);
            c.setHorizontalAlignment( Element.ALIGN_CENTER);
            c.setCellEvent(new CellBackgroundCellEvent(reportData.getIsLTR(), ct2Colors.hraBlue,false, false, false, false) );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Phrase( "C", fontLargeWhite ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderColor( ct2Colors.scoreBoxBorderColor );
            c.setBorderWidth( scoreBoxBorderWidth );
            c.setPadding( padding );
            c.setPaddingBottom(padBot);
            c.setHorizontalAlignment( Element.ALIGN_CENTER);
            c.setCellEvent(new CellBackgroundCellEvent(reportData.getIsLTR(), ct2Colors.hraBlue,false, false, false, false) );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Phrase( lmsg( "g.Date"), fontLargeWhite ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderColor( ct2Colors.scoreBoxBorderColor );
            c.setBorderWidth( scoreBoxBorderWidth );
            c.setPadding( padding );
            c.setPaddingBottom(padBot);
            c.setHorizontalAlignment( Element.ALIGN_CENTER);
            c.setCellEvent(new CellBackgroundCellEvent(reportData.getIsLTR(), ct2Colors.hraBlue,false, true, false, false) );
            setRunDirection( c );
            t.addCell(c);
            

            List<DiscResult> drl = new ArrayList<>();
            drl.addAll( discDataSet.getDiscResultList());
            
            if( DEBUG )
            {
                drl.addAll( discDataSet.getDiscResultList());
                drl.addAll( discDataSet.getDiscResultList());
                drl.addAll( discDataSet.getDiscResultList());
                drl.addAll( discDataSet.getDiscResultList());
                
            }
            
            if( getSampleReport() )
            {
                for( DiscResult dr : drl )
                {
                    User uu = (User) dr.getUser().clone(); 
                    uu.setUserId(0);
                    uu.setFirstName(SampleReportUtils.getRandomFirst());
                    uu.setLastName( SampleReportUtils.getRandomLast() );
                    uu.setEmail(SampleReportUtils.getRandomEmail() );                    
                    dr.setUser(uu);
                    if( uu.getSuborg()!=null )
                    {
                        uu.setSuborg( (Suborg)uu.getSuborg().clone() );
                        uu.getSuborg().setSuborgId(0);
                        uu.getSuborg().setName( SampleReportUtils.getRandomSuborg());
                    }
                }
            }
            
            // sort by doubletrait 
            if( sortTypeId==0 )
                Collections.sort( drl, new DiscResultTwoLevelScoreComparator() );
            else
                Collections.sort( drl, new DiscResultUserNameComparator() );
            
            BaseColor bcolor = new BaseColor( 0xea,0xea,0xea);
            float bwid = 0.5f;
            Chunk chk;
            Paragraph par;
            Font cfont=font ;
            // String styleLetter;
            BaseFont bf = fontBold.getBaseFont();
            Font dfont;
            float val;
            
            for( DiscResult dr : drl )
            {
                c = new PdfPCell( new Phrase( dr.getUser().getLastName() + ", " + dr.getUser().getFirstName(), cfont ) );
                c.setBorder( Rectangle.NO_BORDER );
                c.setBorderColor( bcolor );
                c.setBorderWidth( bwid );
                c.setPadding( padding );
                c.setHorizontalAlignment( Element.ALIGN_LEFT);
                setRunDirection( c );
                t.addCell(c);
                
                par = new Paragraph();
                //styleLetter = DiscReportUtils.getCompetencyStubLetter(dr.getDominantTypeIndex());
                dfont = new Font(bf, FONTSZ);
                dfont.setColor( DiscReportUtils.sliceBaseColors[dr.getDominantTypeIndex()] );
                chk = new Chunk( lmsg_spec( DiscReportUtils.getCompetencyStubLetter( dr.getDominantTypeIndex()) + ".name"), dfont);
                par.add( chk );
                
                if( dr.getSecondaryTraitIndex()>=0 )
                {
                    //styleLetter = DiscReportUtils.getCompetencyStubLetter(dr.getSecondaryTraitIndex());
                    dfont = new Font(bf, FONTSZ);
                    dfont.setColor( DiscReportUtils.sliceBaseColors[dr.getSecondaryTraitIndex()] );

                    chk = new Chunk( " " + lmsg_spec( DiscReportUtils.getCompetencyStubLetter( dr.getSecondaryTraitIndex()) + ".name"), dfont);
                    par.add( chk );                    
                }

                c = new PdfPCell( par );
                c.setBorder( Rectangle.NO_BORDER );
                c.setBorderColor( bcolor );
                c.setBorderWidth( bwid );
                c.setPadding( padding );
                c.setHorizontalAlignment( Element.ALIGN_CENTER);
                setRunDirection( c );
                t.addCell(c);

                for( int i=0; i<4; i++ )
                {
                    val = dr.getValueForStyleIndex(i);
                    c = new PdfPCell( new Phrase( I18nUtils.getFormattedNumber(reportData.getLocale(), val, scrDigits), cfont) );
                    c.setBorder( Rectangle.NO_BORDER );
                    c.setBorderColor( bcolor );
                    c.setBorderWidth( bwid );
                    c.setPadding( padding );
                    c.setHorizontalAlignment( Element.ALIGN_CENTER);
                    setRunDirection( c );
                    t.addCell(c);
                }
                
                c = new PdfPCell( new Phrase( I18nUtils.getFormattedDate( reportData.getLocale(), dr.getTestDate(), DateFormat.SHORT), cfont) );
                c.setBorder( Rectangle.NO_BORDER );
                c.setBorderColor( bcolor );
                c.setBorderWidth( bwid );
                c.setPadding( padding );
                c.setHorizontalAlignment( Element.ALIGN_CENTER);
                setRunDirection( c );
                t.addCell(c);                
            }
            
            float thgt = t.calculateHeights();
            if( thgt> pageHeight )
                t.setHeaderRows( 1 );

            //else
            //{
            float ulY = y - PAD;

            float yTemp = addTableToDocument(y, t, false, true );
            
            currentYLevel = ulY - thgt - 5;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseDiscGroupReportTemplate.addReportInfoHeader()" );
            throw e;
        }
    }
    
    public void addDiscEducationSection() throws Exception
    {
        try
        {
            previousYLevel =  currentYLevel;

            String titleKey = "disc.WhatIsDISC";

            String sectionTitle = lmsg_spec( titleKey);

            PdfPTable t = new PdfPTable( 1 );

            t.setTotalWidth( new float[] { pageWidth-2*CT2_MARGIN- 2*CT2_TEXT_EXTRAMARGIN } );
            t.setLockedWidth( true );
            setRunDirection(t);

            Font fnt = new Font(this.baseFontCalibriBold, XXLFONTSZ);
            fnt.setColor( ct2Colors.hraBlue );

            PdfPCell c = new PdfPCell( new Phrase(sectionTitle, fnt) );
            c.setPadding( 6 );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderWidth( 0 );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            setRunDirection(c);
            t.addCell( c );
            float ht = t.calculateHeights(); //  + 500;

            float tw = t.getTotalWidth();
            float tableX = (pageWidth - tw )/2;
            float y = pageHeight - headerHgt - TPAD;

            t.writeSelectedRows(0, -1,tableX, y, pdfWriter.getDirectContent() );

            currentYLevel = y - ht - TPAD;
            y = currentYLevel;


            PdfPTable touter = new PdfPTable( 1 );
            setRunDirection( touter );
            // float importanceWidth = 25;

            touter.setTotalWidth( pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN );
            touter.setWidths( new float[]{1f} );
            touter.setLockedWidth( true );
            // t.setHeaderRows( 1 );

            // Create header
            c = new PdfPCell( new Phrase( sectionTitle, fontLargeWhite ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setBorderWidth( scoreBoxBorderWidth );
            c.setPadding( 1 );
            c.setPaddingBottom( 5 );
            c.setPaddingLeft( CT2_BOXHEADER_LEFTPAD );
            c.setBackgroundColor( ct2Colors.hraBlue );
            c.setCellEvent(new CellBackgroundCellEvent(reportData.getIsLTR(), ct2Colors.hraBlue,true, true, true, true) );
            setRunDirection( c );
            // touter.addCell(c);


            c = touter.getDefaultCell();
            c.setPadding( 0 );
            c.setBorder( Rectangle.NO_BORDER );
            setRunDirection( c );

            Font listHeaderFont = fontLargeBold;
            Font listItemFont = fontLarge;

            c = new PdfPCell( new Phrase( lmsg_spec("disc.what.p1"), listItemFont ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setVerticalAlignment( Element.ALIGN_TOP );
            c.setBorderWidth( 0 );
            c.setPadding( 2 );
            setRunDirection( c );
            touter.addCell(c);


            List<String> itemList = new ArrayList<>();
            itemList.add( lmsg_spec("d.name" ) + " (D)");
            itemList.add( lmsg_spec("i.name" ) + " (I)");
            itemList.add( lmsg_spec("s.name" ) + " (S)");
            itemList.add( lmsg_spec("c.name" ) + " (C)");
            addListItemGroupToTable(touter, null, itemList, listHeaderFont, listItemFont, 0);

            c = new PdfPCell( new Phrase( lmsg_spec("disc.what.p2"), listItemFont ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setVerticalAlignment( Element.ALIGN_TOP );
            c.setBorderWidth( 0 );
            c.setPadding( 2 );
            c.setPaddingBottom(0);
            setRunDirection( c );
            touter.addCell(c);

            c = new PdfPCell( new Phrase( lmsg_spec("disc.what.p3"), listItemFont ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setVerticalAlignment( Element.ALIGN_TOP );
            c.setBorderWidth( 0 );
            c.setPadding( 2 );
            c.setPaddingBottom(0);
            setRunDirection( c );
            touter.addCell(c);

            c = new PdfPCell( new Phrase( lmsg_spec("disc.History.title"), listHeaderFont ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setVerticalAlignment( Element.ALIGN_TOP );
            c.setBorderWidth( 0 );
            c.setPadding( 2 );
            c.setPaddingTop(6);
            setRunDirection( c );
            touter.addCell(c);

            c = new PdfPCell( new Phrase( lmsg_spec("disc.History.1"), listItemFont ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setVerticalAlignment( Element.ALIGN_TOP );
            c.setBorderWidth( 0 );
            c.setPadding( 2 );
            setRunDirection( c );
            touter.addCell(c);

            c = new PdfPCell( new Phrase( lmsg_spec("disc.History.2"), listItemFont ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setVerticalAlignment( Element.ALIGN_TOP );
            c.setBorderWidth( 0 );
            c.setPadding( 2 );
            setRunDirection( c );
            touter.addCell(c);


            c = new PdfPCell( new Phrase( lmsg_spec("disc.HowUsed.title"), listHeaderFont ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setVerticalAlignment( Element.ALIGN_TOP );
            c.setBorderWidth( 0 );
            c.setPadding( 2 );
            c.setPaddingTop(6);
            setRunDirection( c );
            touter.addCell(c);

            c = new PdfPCell( new Phrase( lmsg_spec("disc.HowUsed.1"), listItemFont ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setVerticalAlignment( Element.ALIGN_TOP );
            c.setBorderWidth( 0 );
            c.setPadding( 2 );
            setRunDirection( c );
            touter.addCell(c);

            itemList = new ArrayList<>();
            itemList.add( lmsg_spec("disc.HowUsed.1.a" ));
            itemList.add( lmsg_spec("disc.HowUsed.1.b" ));
            itemList.add( lmsg_spec("disc.HowUsed.1.c" ));
            itemList.add( lmsg_spec("disc.HowUsed.1.d" ));
            addListItemGroupToTable(touter, null, itemList, listHeaderFont, listItemFont, 0);


            c = new PdfPCell( new Phrase( lmsg_spec("disc.ScoringInfo.title"), listHeaderFont ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setVerticalAlignment( Element.ALIGN_TOP );
            c.setBorderWidth( 0 );
            c.setPadding( 2 );
            c.setPaddingTop(6);
            setRunDirection( c );
            touter.addCell(c);

            c = new PdfPCell( new Phrase( lmsg_spec("disc.ScoringInfo.1"), listItemFont ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setVerticalAlignment( Element.ALIGN_TOP );
            c.setBorderWidth( 0 );
            c.setPadding( 2 );
            setRunDirection( c );
            touter.addCell(c);

            Paragraph par = new Paragraph();
            Chunk chk = new Chunk( lmsg_spec("disc.reference.title") + ": ", this.fontSmall );
            par.add(chk );
            chk = new Chunk( lmsg_spec("disc.reference"), this.fontSmallItalic );
            par.add(chk );
            
            c = new PdfPCell( par );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setVerticalAlignment( Element.ALIGN_TOP );
            c.setBorderWidth( 0 );
            c.setPadding( 2 );
            setRunDirection( c );
            touter.addCell(c);
            
            
            float ulY = currentYLevel - 6*PAD;  // 4* PAD;
            float tableHeight = touter.calculateHeights(); //  + 500;
            if( tableHeight > (ulY - footerHgt - 3*PAD) )
            {
                this.addNewPage();
                previousYLevel = currentYLevel;
            }

            //float y = addTitle( previousYLevel, lmsg_spec("disc.LearnMore"), null );
            //y -= TPAD;
            touter.writeSelectedRows(0, -1,CT2_MARGIN + CT2_TEXT_EXTRAMARGIN, y, pdfWriter.getDirectContent() );
            currentYLevel = y - touter.calculateHeights();
            previousYLevel = currentYLevel;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseDiscGroupReportTemplate.addDiscEducationSection()" );
            throw e;
        }
    }
    
    
    
    public void addKeyActionsToTakeSection() throws Exception
    {
        try
        {
            previousYLevel =  currentYLevel;
            BaseFont bf = this.fontXLargeBoldDarkBlue.getBaseFont();
            Font blueFont = new Font(bf, XXLFONTSZ);
            blueFont.setColor( ct2Colors.hraBlue );

            PdfPTable t = new PdfPTable( 1 );
            t.setTotalWidth( new float[] { pageWidth-2*CT2_MARGIN- 2*CT2_TEXT_EXTRAMARGIN } );
            t.setLockedWidth( true );
            setRunDirection(t);

            PdfPCell c = new PdfPCell( new Phrase(lmsg_spec( "disc.keyActionsAfterDisc").toUpperCase() + " ", blueFont) );
            c.setPadding( 6 );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderWidth( 0 );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            setRunDirection(c);
            t.addCell( c );
            float ht = t.calculateHeights(); //  + 500;
            float tw = t.getTotalWidth();
            float tableX = (pageWidth - tw )/2;
            float y = currentYLevel; //  pageHeight - headerHgt - TPAD;
            t.writeSelectedRows(0, -1,tableX, y, pdfWriter.getDirectContent() );
            currentYLevel = y - ht - TPAD;
            y = currentYLevel;

            bf = fontLargeBold.getBaseFont();
            Font listHeaderFont = new Font(bf, LFONTSZ);
            listHeaderFont.setColor( ct2Colors.hraBlue );
            Font listItemFont = fontLarge;


            PdfPTable touter = new PdfPTable( 1 );
            setRunDirection( touter );
            // float importanceWidth = 25;

            touter.setTotalWidth( pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN );
            touter.setWidths( new float[]{1f} );
            touter.setLockedWidth( true );

            String listTitle = lmsg_spec("mgr.actions.1.title");
            List<String> itemList = new ArrayList<>();
            for( int i=1;i<=1; i++ )
            {
                itemList.add( lmsg_spec("mgr.actions.1." + i ) );
            }
            addListItemGroupToTable(touter, listTitle, itemList, listHeaderFont, listItemFont, 0);

            listTitle = lmsg_spec("mgr.actions.2.title");
            itemList = new ArrayList<>();
            for( int i=1;i<=3; i++ )
            {
                itemList.add( lmsg_spec("mgr.actions.2." + i ) );
            }
            addListItemGroupToTable(touter, listTitle, itemList, listHeaderFont, listItemFont, 0);

            listTitle = lmsg_spec("mgr.actions.3.title");
            itemList = new ArrayList<>();
            for( int i=1;i<=1; i++ )
            {
                itemList.add( lmsg_spec("mgr.actions.3." + i ) );
            }
            addListItemGroupToTable(touter, listTitle, itemList, listHeaderFont, listItemFont, 0);

            listTitle = lmsg_spec("mgr.actions.4.title");
            itemList = new ArrayList<>();
            for( int i=1;i<=1; i++ )
            {
                itemList.add( lmsg_spec("mgr.actions.4." + i ) );
            }
            addListItemGroupToTable(touter, listTitle, itemList, listHeaderFont, listItemFont, 0);

            listTitle = lmsg_spec("mgr.actions.5.title");
            itemList = new ArrayList<>();
            for( int i=1;i<=1; i++ )
            {
                itemList.add( lmsg_spec("mgr.actions.5." + i ) );
            }
            addListItemGroupToTable(touter, listTitle, itemList, listHeaderFont, listItemFont, 0);

            touter.writeSelectedRows(0, -1,CT2_MARGIN + CT2_TEXT_EXTRAMARGIN, y, pdfWriter.getDirectContent() );
            currentYLevel = y - touter.calculateHeights();
            previousYLevel = currentYLevel;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseDiscGroupReportTemplate.addKeyActionsToTakeSection()" );
            throw e;
        }

    }
    
    
    public void addDiscBuildYourTeamSection() throws Exception
    {
        try
        {
            previousYLevel =  currentYLevel;
            BaseFont bf = fontXLargeBoldDarkBlue.getBaseFont();
            Font blueFont = new Font(bf, XXLFONTSZ);
            blueFont.setColor( ct2Colors.hraBlue );

            PdfPTable t = new PdfPTable( 1 );
            t.setTotalWidth( new float[] { pageWidth-2*CT2_MARGIN- 2*CT2_TEXT_EXTRAMARGIN } );
            t.setLockedWidth( true );
            setRunDirection(t);

            PdfPCell c = new PdfPCell( new Phrase(lmsg_spec( "mgr.tmbld.title").toUpperCase() + " ", blueFont) );
            c.setPadding( 6 );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderWidth( 0 );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            setRunDirection(c);
            t.addCell( c );
            float ht = t.calculateHeights(); //  + 500;
            float tw = t.getTotalWidth();
            float tableX = (pageWidth - tw )/2;
            float y = currentYLevel; //  pageHeight - headerHgt - TPAD;
            t.writeSelectedRows(0, -1,tableX, y, pdfWriter.getDirectContent() );
            currentYLevel = y - ht - TPAD;
            y = currentYLevel;

            bf = fontLargeBold.getBaseFont();
            Font listHeaderFont = new Font(bf, LFONTSZ);
            listHeaderFont.setColor( ct2Colors.hraBlue );
            Font listItemFont = fontLarge;


            PdfPTable touter = new PdfPTable( 1 );
            setRunDirection( touter );
            // float importanceWidth = 25;

            touter.setTotalWidth( pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN );
            touter.setWidths( new float[]{1f} );
            touter.setLockedWidth( true );


            c = new PdfPCell( new Phrase( lmsg_spec("mgr.tmbld.p1"), listItemFont ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setBorderWidth( 0 );
            c.setPadding( 4 );
            c.setPaddingLeft( CT2_BOXHEADER_LEFTPAD );
            setRunDirection( c );
            touter.addCell(c);


            String listTitle = lmsg_spec("mgr.tmbld.1.title");
            List<String> itemList = new ArrayList<>();
            for( int i=1;i<=1; i++ )
            {
                itemList.add( lmsg_spec("mgr.tmbld.1." + i ) );
            }
            addListItemGroupToTable(touter, listTitle, itemList, listHeaderFont, listItemFont, 0);

            listTitle = lmsg_spec("mgr.tmbld.2.title");
            itemList = new ArrayList<>();
            for( int i=1;i<=1; i++ )
            {
                itemList.add( lmsg_spec("mgr.tmbld.2." + i ) );
            }
            addListItemGroupToTable(touter, listTitle, itemList, listHeaderFont, listItemFont, 0);

            listTitle = lmsg_spec("mgr.tmbld.2.2");
            itemList = new ArrayList<>();
            for( int i=1;i<=5; i++ )
            {
                itemList.add( lmsg_spec("mgr.tmbld.2.2." + i ) );
            }
            addListItemGroupToTable(touter, listTitle, itemList, listItemFont, listItemFont, 20);

            listTitle = lmsg_spec("mgr.tmbld.3.title");
            itemList = new ArrayList<>();
            for( int i=1;i<=1; i++ )
            {
                itemList.add( lmsg_spec("mgr.tmbld.3." + i ) );
            }
            addListItemGroupToTable(touter, listTitle, itemList, listHeaderFont, listItemFont, 0);

            listTitle = lmsg_spec("mgr.tmbld.3.2");
            itemList = new ArrayList<>();
            for( int i=1;i<=8; i++ )
            {
                itemList.add( lmsg_spec("mgr.tmbld.3.2." + i ) );
            }
            addListItemGroupToTable(touter, listTitle, itemList, listItemFont, listItemFont, 20);

            touter.writeSelectedRows(0, -1,CT2_MARGIN + CT2_TEXT_EXTRAMARGIN, y, pdfWriter.getDirectContent() );
            currentYLevel = y - touter.calculateHeights();
            previousYLevel = currentYLevel;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseDiscGroupReportTemplate.addDiscBuildYourTeamSection()" );
            throw e;
        }
    }

    
    
    public void addHowBuildTeamsWithDiscSection() throws Exception
    {
        try
        {
            previousYLevel =  currentYLevel;
            BaseFont bf = this.fontXLargeBoldDarkBlue.getBaseFont();
            Font blueFont = new Font(bf, XXLFONTSZ);
            blueFont.setColor( ct2Colors.hraBlue );

            PdfPTable t = new PdfPTable( 1 );
            t.setTotalWidth( new float[] { pageWidth-2*CT2_MARGIN- 2*CT2_TEXT_EXTRAMARGIN } );
            t.setLockedWidth( true );
            setRunDirection(t);

            PdfPCell c = new PdfPCell( new Phrase(lmsg_spec( "mgr.howbldtm.title").toUpperCase() + " ", blueFont) );
            c.setPadding( 6 );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderWidth( 0 );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            setRunDirection(c);
            t.addCell( c );
            float ht = t.calculateHeights(); //  + 500;
            float tw = t.getTotalWidth();
            float tableX = (pageWidth - tw )/2;
            float y = currentYLevel; //  pageHeight - headerHgt - TPAD;
            t.writeSelectedRows(0, -1,tableX, y, pdfWriter.getDirectContent() );
            currentYLevel = y - ht - TPAD;
            y = currentYLevel;

            bf = fontLargeBold.getBaseFont();
            Font listHeaderFont = new Font(bf, LFONTSZ);
            listHeaderFont.setColor( ct2Colors.hraBlue );
            Font listItemFont = fontLarge;


            PdfPTable touter = new PdfPTable( 1 );
            setRunDirection( touter );
            // float importanceWidth = 25;

            touter.setTotalWidth( pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN );
            touter.setWidths( new float[]{1f} );
            touter.setLockedWidth( true );


            c = new PdfPCell( new Phrase( lmsg_spec("mgr.howbldtm.p1"), listItemFont ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setBorderWidth( 0 );
            c.setPadding( 4 );
            c.setPaddingLeft( CT2_BOXHEADER_LEFTPAD );
            setRunDirection( c );
            touter.addCell(c);

            c = new PdfPCell( new Phrase( lmsg_spec("mgr.howbldtm.p2"), listItemFont ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setBorderWidth( 0 );
            c.setPadding( 4 );
            c.setPaddingLeft( CT2_BOXHEADER_LEFTPAD );
            setRunDirection( c );
            touter.addCell(c);

            touter.writeSelectedRows(0, -1,CT2_MARGIN + CT2_TEXT_EXTRAMARGIN, y, pdfWriter.getDirectContent() );
            currentYLevel = y - touter.calculateHeights();
            previousYLevel = currentYLevel;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseDiscGroupReportTemplate.addHowBuildTeamsWithDiscSection()" );
            throw e;
        }        
    }

    public void addBlueBar() throws Exception
    {
        try
        {
            previousYLevel =  currentYLevel;

            PdfContentByte pcb = pdfWriter.getDirectContent();
            pcb.saveState();

            pcb.setColorStroke( ct2Colors.hraBlue );
            pcb.setColorFill( ct2Colors.hraBlue );
            pcb.setLineWidth(0 );
            float llx = CT2_MARGIN;
            float lly = previousYLevel - 2*TPAD;
            float wid = pageWidth - 2*CT2_MARGIN;
            float hgt = 3;
            pcb.rectangle(llx, lly, wid, hgt);
            pcb.fill();
            pcb.restoreState();

            currentYLevel = currentYLevel - 3*TPAD - hgt;
            previousYLevel = currentYLevel;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseDiscGroupReportTemplate.addBlueBar()" );
            throw e;
        }

    }
    
    
    public void addAvoidSterotypingSection() throws Exception
    {
        try
        {
            previousYLevel =  currentYLevel;
            BaseFont bf = fontXLargeBoldDarkBlue.getBaseFont();
            Font blueFont = new Font(bf, XXLFONTSZ);
            blueFont.setColor( ct2Colors.hraBlue );

            PdfPTable t = new PdfPTable( 1 );
            t.setTotalWidth( new float[] { pageWidth-2*CT2_MARGIN- 2*CT2_TEXT_EXTRAMARGIN } );
            t.setLockedWidth( true );
            setRunDirection(t);

            PdfPCell c = new PdfPCell( new Phrase(lmsg_spec( "mgr.avoids.title").toUpperCase() + " ", blueFont) );
            c.setPadding( 6 );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderWidth( 0 );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            setRunDirection(c);
            t.addCell( c );
            float ht = t.calculateHeights(); //  + 500;
            float tw = t.getTotalWidth();
            float tableX = (pageWidth - tw )/2;
            float y = currentYLevel; //  pageHeight - headerHgt - TPAD;
            t.writeSelectedRows(0, -1,tableX, y, pdfWriter.getDirectContent() );
            currentYLevel = y - ht - TPAD;
            y = currentYLevel;

            bf = fontLargeBold.getBaseFont();
            Font listHeaderFont = new Font(bf, LFONTSZ);
            listHeaderFont.setColor( ct2Colors.hraBlue );
            Font listItemFont = fontLarge;


            PdfPTable touter = new PdfPTable( 1 );
            setRunDirection( touter );
            // float importanceWidth = 25;

            touter.setTotalWidth( pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN );
            touter.setWidths( new float[]{1f} );
            touter.setLockedWidth( true );


            c = new PdfPCell( new Phrase( lmsg_spec("mgr.avoids.p1"), listItemFont ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setBorderWidth( 0 );
            c.setPadding( 4 );
            c.setPaddingLeft( CT2_BOXHEADER_LEFTPAD );
            setRunDirection( c );
            touter.addCell(c);

            touter.writeSelectedRows(0, -1,CT2_MARGIN + CT2_TEXT_EXTRAMARGIN, y, pdfWriter.getDirectContent() );
            currentYLevel = y - touter.calculateHeights();
            previousYLevel = currentYLevel;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseDiscGroupReportTemplate.addAvoidSterotypingSection()" );
            throw e;
        }        
    }
    
    
    
    
    /*
     Map of  stubletter :
              obj[0] = Name
              obj[1] = value
    */
    public Map<String,Object[]> getGroupPercentageScoreValMap()
    {
        Map<String,Object[]> out = new HashMap<>();
        String key;
        
        for( int i=0;i<discDataSet.getStylePercentages().length; i++ )
        {
            //nameUppercase = DiscReportUtils.getCompetencyStubLetter(i).toUpperCase();
            //out.put( DiscReportUtils.getCompetencyStubLetter(i), new Object[]{ nameUppercase, Float.valueOf(discScoreVals[i])} );
            key = DiscReportUtils.getCompetencyStubLetter(i) + ".name";
            out.put( DiscReportUtils.getCompetencyStubLetter(i), new Object[]{ lmsg_spec(key), discDataSet.getStylePercentages()[i], (int)discDataSet.getStyleCounts()[i]} );
        
            if( DEBUG )
            {
                out.put( DiscReportUtils.getCompetencyStubLetter(i), new Object[]{ lmsg_spec(key), (float)100f*(i+1)/10f , (i+1)} );
                // LogService.logIt( "BaseDiscGroupReportTemplate.getGroupPercentageScoreValMap() " + DiscReportUtils.getCompetencyStubLetter(i) + ": " + (float)100f*(i+1)/10f + ", count=" + (i+1));
            }
        
        }
        return out;
    }

    /*
     Map of  stubletter :
              obj[0] = Name
              obj[1] = value
              obj[2] = count
    */
    public Map<String,Object[]> getGroupTwoLevelPercentageScoreValMap()
    {
        Map<String,Object[]> out = new HashMap<>();
        String name;
        // String nameUppercase;
        for( int i=0;i<discDataSet.getTwoLevelStylePercentages().length; i++ )
        {
            //nameUppercase = DiscReportUtils.getCompetencyStubLetter(i).toUpperCase();
            //out.put( DiscReportUtils.getCompetencyStubLetter(i), new Object[]{ nameUppercase, Float.valueOf(discScoreVals[i])} );
            name = DiscReportUtils.getTwoLevelCompetencyName(i);
            out.put(DiscReportUtils.getTwoLevelCompetencyStubLetter(i), new Object[]{ name, discDataSet.getTwoLevelStylePercentages()[i], (int)discDataSet.getTwoLevelStyleCounts()[i]} );

            if( DEBUG )
            {
                float j = 2.5f;
                if( i>=12 )
                    j=10;
                else if( i>=8 )
                    j=7.5f;
                else if( i>=4 )
                    j=5f;
                out.put( DiscReportUtils.getTwoLevelCompetencyStubLetter(i), new Object[]{ name, j , (int)discDataSet.getTwoLevelStyleCounts()[i]} );
                // LogService.logIt( "BaseDiscGroupReportTemplate.getGroupTwoLevelPercentageScoreValMap() " + DiscReportUtils.getTwoLevelCompetencyStubLetter(i) + ": " + j + ", count=" + (1));
            }
            
        }
        return out;
    }
    
    
    
    
    public void addDiscStylesExplained() throws Exception
    {
        try
        {
            previousYLevel =  currentYLevel;

            float y = addTitle( previousYLevel, lmsg_spec( "disc.StylesExplained" ), null );

            y -= TPAD;


            PdfPCell c;
            PdfPTable touter = new PdfPTable( 2 );

            setRunDirection( touter );
            // float importanceWidth = 25;

            touter.setTotalWidth( pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN );
            touter.setWidths( new float[]{1f, 4f } );
            touter.setLockedWidth( true );
            // t.setHeaderRows( 1 );

            c = touter.getDefaultCell();
            c.setPadding( 0 );
            c.setBorder( Rectangle.NO_BORDER );
            setRunDirection( c );

            BaseFont bf = fontBold.getBaseFont();
            Font fnt;
            String styleLetter;

            for( int i=0; i<4; i++ )
            {
                styleLetter = DiscReportUtils.getCompetencyStubLetter(i);
                fnt = new Font(bf, LFONTSZ);
                fnt.setColor( DiscReportUtils.sliceBaseColors[i] );
                c = new PdfPCell( new Phrase( lmsg_spec(  styleLetter + ".namefull"), fnt ) );
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setVerticalAlignment( Element.ALIGN_TOP );
                c.setBorderWidth( 0 );
                c.setPadding( 2 );
                c.setPaddingBottom(12);
                setRunDirection( c );
                touter.addCell(c);

                c = new PdfPCell( new Phrase( lmsg_spec(  styleLetter + ".characteristics"), font ) );
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setVerticalAlignment( Element.ALIGN_TOP );
                c.setBorderWidth( 0 );
                c.setPadding( 2 );
                c.setPaddingBottom(12);
                setRunDirection( c );
                touter.addCell(c);
            }

            touter.writeSelectedRows(0, -1,CT2_MARGIN + CT2_TEXT_EXTRAMARGIN, y, pdfWriter.getDirectContent() );

            currentYLevel = y - touter.calculateHeights();

            previousYLevel = currentYLevel;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseDiscGroupReportTemplate.addDiscFilesExplained()" );
            throw e;
        }
    }
    
    
    public void addHowToWorkWithYSection( int yTraitIndex ) throws Exception
    {
        try
        {
            previousYLevel =  currentYLevel;

            String yTraitLetter = DiscReportUtils.getCompetencyStubLetter(yTraitIndex );
            String yTraitName = lmsg_spec(yTraitLetter+".name");
            String yTraitNameUpper = yTraitName.toUpperCase();

            // String xTraitLetter = DiscReportUtils.getCompetencyStubLetter( topTraitIndexes[0] );

            BaseColor bgColor = DiscReportUtils.sliceBaseColors[yTraitIndex];

            String titleKey = "disc.HowWorkWithX";

            String sectionTitle = lmsg_spec( titleKey, new String[]{yTraitName});

            Font titleFont = fontXLargeBold;
            BaseFont bf = titleFont.getBaseFont();
            titleFont = new Font(bf, XLFONTSZ);
            titleFont.setColor( ct2Colors.hraBlue );


            float y = addTitleLarge( previousYLevel, lmsg_spec("disc.CollaboratingWithHighY", new String[]{yTraitName} ), titleFont );

            y -= TPAD;

            PdfPCell c;
            PdfPTable touter = new PdfPTable( 1 );
            // touter.setWidths(new float[] {0.5f, 10f} );
            setRunDirection( touter );
            // float importanceWidth = 25;

            touter.setTotalWidth( pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN );
            // touter.setWidths( new float[]{1f} );
            touter.setLockedWidth( true );
            // t.setHeaderRows( 1 );

            // Create header
            c = new PdfPCell();
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setBorderWidth( scoreBoxBorderWidth );
            c.setPadding( 1 );
            c.setPaddingBottom( 5 );
            c.setPaddingLeft( CT2_BOXHEADER_LEFTPAD );
            setRunDirection( c );
            // touter.addCell(c);

            c = new PdfPCell( new Phrase( sectionTitle, fontLargeWhite ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setBorderWidth( scoreBoxBorderWidth );
            c.setPadding( 1 );
            c.setPaddingBottom( 5 );
            c.setPaddingLeft( CT2_BOXHEADER_LEFTPAD );
            c.setBackgroundColor( ct2Colors.hraBlue );
            c.setCellEvent(new CellBackgroundCellEvent(reportData.getIsLTR(), ct2Colors.hraBlue,true, true, true, true) );
            setRunDirection( c );
            // touter.addCell(c);


            c = touter.getDefaultCell();
            c.setPadding( 0 );
            c.setBorder( Rectangle.NO_BORDER );
            setRunDirection( c );

            Font listHeaderFont = fontLargeBold;
            Font listItemFont = fontLarge;
            bf = listHeaderFont.getBaseFont();

            listHeaderFont = new Font(bf, LFONTSZ);
            listHeaderFont.setColor( ct2Colors.hraBlue );

            String listTitle = lmsg_spec( "disc.OnATeam");
            List<String> itemList = lmsg_spec_list(yTraitLetter + ".howwork.onteam");
            addListItemGroupToTable(touter, listTitle, itemList, listHeaderFont, listItemFont, 0);

            listTitle = lmsg_spec( "disc.WhenWorkingWithXStyles", new String[]{yTraitName});
            itemList = lmsg_spec_list(yTraitLetter + ".howwork.with");
            addListItemGroupToTable(touter, listTitle, itemList, listHeaderFont, listItemFont, 0);


            for( String xTraitLetter : DiscReportUtils.DISC_COMPETENCY_STUBS )
            {
                if( xTraitLetter.equalsIgnoreCase(yTraitLetter ) )
                    listTitle = lmsg_spec( "disc.HowXWorksWithX", new String[]{xTraitLetter.toUpperCase()} );
                else
                    listTitle = lmsg_spec("disc.HowXWorksWithY", new String[]{xTraitLetter.toUpperCase(), yTraitLetter.toUpperCase()} );


                c = new PdfPCell( new Phrase( listTitle, listHeaderFont ) );
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setVerticalAlignment( Element.ALIGN_TOP );
                c.setBorderWidth( 0 );
                c.setPadding( 2 );
                c.setPaddingTop(6);
                setRunDirection( c );
                touter.addCell(c);
            
                String howWorkTogetherStr = lmsg_spec(xTraitLetter + ".howworkwith." + yTraitLetter + ".1");
                c = new PdfPCell( new Phrase( howWorkTogetherStr, listItemFont ) );
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setVerticalAlignment( Element.ALIGN_TOP );
                c.setBorderWidth( 0 );
                c.setPadding( 2 );
                setRunDirection( c );
                touter.addCell(c);
            }
            
            String imgUri = sideTabIconUris[yTraitIndex];
            URL imgURL = reportData.getLocalImageUrl( imgUri );
            Image iconImage = ITextUtils.getITextImage( imgURL );
            iconImage.scalePercent(30);
            touter.setTableEvent( new DiscCollaboratingTabTableEvent(bgColor,fontXLargeBoldWhite.getBaseFont(),yTraitNameUpper, iconImage) );

            touter.writeSelectedRows(0, -1,CT2_MARGIN + CT2_BOX_EXTRAMARGIN, y, pdfWriter.getDirectContent() );

            currentYLevel = y - touter.calculateHeights();

            currentYLevel -= TPAD;

            previousYLevel = currentYLevel;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseDiscReportTemplate.addHowXShouldWorkWithYSection()" );
            throw e;
        }
    }

    
    
    
    

    @Override
    public void dispose() throws Exception
    {
        if( baos != null )
            baos.close();
    }


    public void closeDoc() throws Exception
    {
        if( document != null && document.isOpen() )
            document.close();

        document = null;
    }


    public byte[] getDocumentBytes() throws Exception
    {
        if( baos == null )
            return null;

        return baos.toByteArray();
    }

    /**
     * Override this method as needed.
     *
     * @return
     */
    @Override
    public boolean getIsReportGenerationPossible()
    {
        return true;
    }


    
    public void setRunDirection( PdfPCell c )
    {
        if( c == null || reportData == null || reportData.getLocale() == null )
            return;

        // if( I18nUtils.isTextRTL( reportData.getLocale() ) )
        c.setRunDirection( reportData.getTextRunDirection() );
    }

    public void setRunDirection( PdfPTable t )
    {
        if( t == null || reportData == null || reportData.getLocale() == null )
            return;

        t.setRunDirection( reportData.getTextRunDirection() );

        //if( I18nUtils.isTextRTL( reportData.getLocale() ) )
        //    t.setRunDirection( PdfWriter.RUN_DIRECTION_RTL );
    }

    public void setRunDirection( ColumnText ct )
    {
        if( ct == null || reportData == null || reportData.getLocale() == null )
            return;

        ct.setRunDirection( reportData.getTextRunDirection() );

        //if( I18nUtils.isTextRTL( reportData.getLocale() ) )
        //    t.setRunDirection( PdfWriter.RUN_DIRECTION_RTL );
    }
    
    
    public void setDiscDataSet(DiscGroupDataSet discDataSet) 
    {
        this.discDataSet = discDataSet;
    }
    
    public void addFooterBar( String title, boolean includeImage, Font fnt ) throws Exception
    {
            // First, add a table
            PdfPTable touter = new PdfPTable( includeImage ? 1 : 2 );

            if( !includeImage )
                touter.setWidths( new float[] {10, 2} );

            setRunDirection( touter );
            touter.setTotalWidth( pageWidth );
            touter.setLockedWidth( true );

            BaseFont bf = fnt.getBaseFont();
            Font blueFont = new Font(bf, fnt.getSize()+4);
            blueFont.setColor( babyBlue);

            // Create header
            PdfPCell c = new PdfPCell( new Phrase( title, blueFont ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setVerticalAlignment(Element.ALIGN_MIDDLE );
            c.setBorderWidth( scoreBoxBorderWidth );
            c.setPadding( includeImage ? 16 : 0 );
            c.setPaddingLeft( CT2_BOXHEADER_LEFTPAD + CT2_MARGIN + CT2_TEXT_EXTRAMARGIN );
            c.setBackgroundColor( ct2Colors.hraBlue );
            // c.setCellEvent(new CellBackgroundCellEvent(reportData.getIsLTR(), ct2Colors.hraBlue,true, true, true, true) );
            setRunDirection( c );
            touter.addCell(c);

            if( !includeImage )
            {
                String footerImageUri = groupReportBarFooterImageUri;
                URL footerImageURL = reportData.getLocalImageUrl( footerImageUri );
                Image footerImage = ITextUtils.getITextImage( footerImageURL );
                // LogService.logIt( "BaseDiscGroupReportTemplate.addFooterBar() footerImageIndex=" + footerImageIndex + ", footerImageUri=" + footerImageUri + ", footerImage=" + (footerImage!=null) );
                if( footerImage !=null )
                {
                    footerImage.scalePercent(21.875f);
                    // footerImage.scalePercent(7);
                    // ITextUtils.addDirectImage( pdfWriter, footerImage, pageWidth-footerImage.getScaledWidth() + 1, 19, false );
                }

                c = new PdfPCell( footerImage );
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_RIGHT );
                c.setBorderWidth( scoreBoxBorderWidth );
                c.setPadding( 0 );
                c.setBackgroundColor( ct2Colors.hraBlue );
                // c.setCellEvent(new CellBackgroundCellEvent(reportData.getIsLTR(), ct2Colors.hraBlue,true, true, true, true) );
                setRunDirection( c );
                touter.addCell(c);
            }

            float y = footerHeight + footerBarHeight;

            touter.writeSelectedRows(0, -1,0, y, pdfWriter.getDirectContent() );
            currentYLevel = y - touter.calculateHeights() - 5;

            // y = currentYLevel;

            if( includeImage )
            {
                footerImageIndex++;
                if( footerImageIndex>2 )
                    footerImageIndex=0;
                String footerImageUri = groupReportFooterImageUrls[footerImageIndex];
                URL footerImageURL = reportData.getLocalImageUrl( footerImageUri );
                Image footerImage = ITextUtils.getITextImage( footerImageURL );
                // LogService.logIt( "BaseDiscGroupReportTemplate.addFooterBar() footerImageIndex=" + footerImageIndex + ", footerImageUri=" + footerImageUri + ", footerImage=" + (footerImage!=null) );
                if( footerImage !=null )
                {
                    footerImage.scalePercent(60);
                    ITextUtils.addDirectImage( pdfWriter, footerImage, pageWidth-footerImage.getScaledWidth() + 1, 0, false );
                }
            }
    }
    
    
    public String lmsg_spec( String key )
    {
        return discReportUtils.getKey(key );
    }

    public String lmsg_spec( String key, String[] prms )
    {
        String msgText = discReportUtils.getKey(key );
        return MessageFactory.substituteParams( reportData.getLocale() , msgText, prms );
    }

    public List<String> lmsg_spec_list( String key )
    {
        List<String> out = new ArrayList<>();
        String val;
        for( int i=1;i<100;i++ )
        {
            val =  discReportUtils.getKey( key + "." + i );

            if( val==null || val.isBlank() || val.startsWith( "KEY NOT FOUND") )
                break;

            out.add(val);
        }

        return out;
    }

    public void addListItemGroupToTable( PdfPTable tbl, String listTitle, List<String> itemList, Font listHeaderFont, Font listItemFont, int extraLeftPadding) throws Exception
    {
        com.itextpdf.text.List cl = new com.itextpdf.text.List( com.itextpdf.text.List.UNORDERED, 12 );
        cl.setListSymbol( "\u2022");
        cl.setIndentationLeft( 10 );
        cl.setSymbolIndent( 10 );

        ListItem li;
        for( String s : itemList )
        {
            li = new ListItem( 11,  s, listItemFont );
            li.setPaddingTop(4);
            li.setExtraParagraphSpace(4);
            cl.add( li );
        }

        PdfPCell c;

        if( listTitle!=null && !listTitle.isBlank() )
        {
            c = new PdfPCell( new Phrase( listTitle, listHeaderFont ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setVerticalAlignment( Element.ALIGN_TOP );
            c.setBorderWidth( 0 );
            c.setPadding( 4 );
            c.setPaddingLeft(4 + extraLeftPadding );
            c.setPaddingBottom(4);
            setRunDirection( c );
            tbl.addCell(c);
        }

        c = new PdfPCell();
        c.addElement( cl );
        c.setBorder( Rectangle.NO_BORDER );
        c.setHorizontalAlignment( Element.ALIGN_LEFT );
        c.setVerticalAlignment( Element.ALIGN_TOP );
        c.setBorderWidth( 0 );
        c.setPaddingLeft(4 + extraLeftPadding );
        c.setPaddingBottom( 4 );
        setRunDirection( c );
        tbl.addCell(c);
    }

    // Standard Locale key
    public String lmsg( String key )
    {
        return MessageFactory.getStringMessage( reportData.getLocale() , key, null );
    }


    // Standard Locale key
    public String lmsg( String key, String[] prms )
    {
        return MessageFactory.getStringMessage( reportData.getLocale() , key, prms );
    }

    
   public float addTitleLarge( float startY, String title, Font fnt ) throws Exception
    {
        try
        {
            if( !reportData.getIsLTR() )
                return addTitleLargeRTL( startY,  title, fnt );

            float tHeight = getTitleHeightLarge( title, fnt );
            if( startY > 0 )
            {
                float ulY = startY - PAD - tHeight;

                if( ulY < footerHgt + 3*PAD )
                {
                    document.newPage();
                    startY = 0;
                    currentYLevel = pageHeight - PAD -  headerHgt;
                }
            }

            previousYLevel =  currentYLevel;

            // Font fnt =   getHeaderFontXXLarge();
            // float leading = fnt.getSize();

            float y = startY>0 ? startY - fnt.getSize() - TPAD :  pageHeight - headerHgt - fnt.getSize() - TPAD;

            // Add Title
            ITextUtils.addDirectText( pdfWriter, title, CT2_MARGIN + CT2_TEXT_EXTRAMARGIN, y, reportData.getIsLTR() ? Element.ALIGN_LEFT : Element.ALIGN_RIGHT, fnt, false);

            return y;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "BaseCT2ReportTemplate.addTitleAndSubtitle()" );
            throw new STException( e );
        }
    }

    public float addTitleLargeRTL( float startY, String title, Font fnt ) throws Exception
    {
        try
        {
            if( startY>0 )
            {
                float ulY = startY - 16* PAD;

                if( ulY < footerHgt + 3*PAD )
                {
                    document.newPage();
                    startY = 0;
                }
            }

            previousYLevel =  currentYLevel;

            // Font fnt =   getHeaderFontXXLarge();

            float y = startY>0 ? startY - fnt.getSize() - TPAD :  pageHeight - headerHgt - fnt.getSize() - TPAD;

            PdfPTable t = new PdfPTable( 1 );

            t.setTotalWidth( new float[] { pageWidth-2*CT2_MARGIN- 2*CT2_TEXT_EXTRAMARGIN } );
            t.setLockedWidth( true );
            setRunDirection(t);

            PdfPCell c = t.getDefaultCell();
            c.setPadding( 0 );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderWidth( 0 );
            setRunDirection(c);

            t.addCell( new Phrase( title , fnt ) );

            float ht = t.calculateHeights(); //  + 500;

            float tw = t.getTotalWidth();

            float tableX = (pageWidth - tw )/2;

            t.writeSelectedRows(0, -1,tableX, y, pdfWriter.getDirectContent() );

            currentYLevel = y - ht;

            return currentYLevel;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "BaseDiscGroupReportTemplate.addTitleLargeRTLLarge()" );
            throw new STException( e );
        }
    }




    public float getTitleHeightLarge( String title, Font fnt ) throws Exception
    {
        try
        {
            // Font fnt =   getHeaderFontXXLarge();
            // Change getFont()
            float leading = fnt.getSize();

            float txtW = pageWidth - 2*CT2_MARGIN-2*CT2_TEXT_EXTRAMARGIN;

            // float y = previousYLevel - 6*PAD - getFont().getSize();
            float h = ITextUtils.getDirectTextHeight( pdfWriter, title, txtW, reportData.getIsLTR() ? Element.ALIGN_LEFT : Element.ALIGN_RIGHT, leading, fnt);

            h += PAD;
            return h;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "BaseDiscGroupReportTemplate.getTitleHeightLarge()" );
            throw new STException( e );
        }
    }

    
    public void addNewPage() throws Exception
    {
        document.newPage();
        currentYLevel = pageHeight - PAD -  headerHgt;
    }


    public void addNotesSection() throws Exception
    {
        if( reportData.getReportRuleAsBoolean( "usernotesoff" ) )
            return;

        addTitle( 0 , lmsg("g.Notes"), lmsg( "g.NotesSubtitle" ) );
    }
    
    protected void addMinimalPrepNotesSection() throws Exception
    {
        // LogService.logIt( "BaseCT2ReportTemplate.addMinimalPrepNotesSection() START " );
        Calendar cal = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
        String dtStr = df.format( cal.getTime() );

        if( reportData.getReportRuleAsBoolean( "hidedatespdf" ) )
            dtStr="***";

        String  note = "HR Avatar Use Only: oid=" + discDataSet.getOrg().getOrgId() + ", rid=" + reportData.getR2Use().getReportId(); // + lmsg( "g.SimIdAndVersion", new String[]{ Long.toString( reportData.getTestEvent().getSimId()) , Integer.toString(reportData.getTestEvent().getSimVersionId() ), Long.toString( reportData.getTestEvent().getTestKeyId()), Long.toString( reportData.getTestEvent().getTestEventId()), Long.toString( reportData.getReport().getReportId() ), Integer.toString( reportData.getTestKey().getProductId() ), dtStr } );

        //if( reportData.getTestEvent().getUserAgent()!=null && !reportData.getTestEvent().getUserAgent().isBlank() )
        //    note += "\nUser-Agent: " + reportData.getTestEvent().getUserAgent();

        previousYLevel =  currentYLevel;

        // First create the table
        PdfPCell c;

        // First, add a table
        PdfPTable t = new PdfPTable( new float[] { 1f } );

        float outerWid = pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN;

        // t.setHorizontalAlignment( Element.ALIGN_CENTER );
        t.setTotalWidth( outerWid );
        t.setLockedWidth( true );
        setRunDirection( t );

        c = new PdfPCell( new Phrase( note, this.font ));
        c.setBorder( Rectangle.NO_BORDER );
        c.setBackgroundColor( BaseColor.WHITE );
        c.setPaddingTop( 12 );
        c.setPaddingLeft(10);
        c.setPaddingRight(5);
        c.setPaddingBottom( 10 );
        setRunDirection( c );
        t.addCell(c);

        currentYLevel = addTableToDocument(currentYLevel, t, false, true );
    }
    
    public float addTableToDocument( float startY, PdfPTable t, boolean onePageIfPossible, boolean addContinuedTextIfNeeded) throws Exception
    {
        try
        {
            float ulY = startY - 2*PAD;  // 4* PAD;

            float tableHeight = t.calculateHeights(); //  + 500;
            float tableHeaderHeight = t.getHeaderHeight();

            int rowCount = t.getRows().size(); //  - t.getHeaderRows() - t.getFooterRows();

            float maxRowHeight=0;

            float[] rowHgts = new float[rowCount];

            for( int i=0; i<rowCount; i++ )
            {
                rowHgts[i]=t.getRowHeight(i);
                maxRowHeight = Math.max( maxRowHeight, rowHgts[i] );
                // LogService.logIt( "BaseCT2ReportTemplate.addTableToDocument() row=" + i + ", rowHeight=" + rowHgts[i] );
            }


            float firstRowHgt = rowHgts.length>t.getHeaderRows() ? rowHgts[t.getHeaderRows()] : 0;

            float heightAvailNewPage = pageHeight - headerHgt - 3*PAD - footerHgt - 3*PAD - tableHeaderHeight;


            // commented out on 4/8/2022 because we DO have some rows bigger than half a page. Still, do not want to splitLate.
            //if( maxRowHeight >= heightAvailNewPage*0.5 )
            //    t.setSplitLate(false);

            // LogService.logIt( "BaseCT2ReportTemplate.addTableToDocument() rows=" + rowCount + ", tableHeight=" + tableHeight + ", tableHeaderHeight=" + tableHeaderHeight + ", maxRowHeight=" + maxRowHeight + ", splitLate=" + t.isSplitLate() );

            if( onePageIfPossible && tableHeight<=(heightAvailNewPage - 3*PAD) && tableHeight > (ulY - footerHgt - 3*PAD) )
            {
                if( ulY >= MIN_HEIGHT_FOR_CONTINUED_TEXT )
                    addContinuedNextPage(ulY, null );

                // LogService.logIt( "BaseCT2ReportTemplate.addTableToDocument() adding new page. "  );
                document.newPage();

                ulY = pageHeight - headerHgt - 3*PAD;
                // currentYLevel = pageHeight - PAD -  headerHgt;
            }

            // If first row doesn't fit on this page
            else if( firstRowHgt > ulY- footerHgt - 3*PAD - tableHeaderHeight ) // ulY < footerHgt + 8*PAD )
            {
                if( ulY >= MIN_HEIGHT_FOR_CONTINUED_TEXT )
                    addContinuedNextPage(ulY, null );

                // LogService.logIt( "BaseCT2ReportTemplate.addTableToDocument() adding new page. "  );
                document.newPage();

                ulY = pageHeight - headerHgt - 3*PAD;
            }

            //else if( 1==1 )
            //{
            //    if( ulY >= MIN_HEIGHT_FOR_CONTINUED_TEXT )
            //        addContinuedNextPage(ulY, null );

                // LogService.logIt( "BaseCT2ReportTemplate.addTableToDocument() adding new page. "  );
            //    document.newPage();
            //    ulY = pageHeight - headerHgt - 3*PAD;
            //}

            //if( maxRowHeight > usablePageHeight )
            //    t.setSplitLate(false);
            float tableXlft = CT2_MARGIN + CT2_BOX_EXTRAMARGIN;
            float tableXrgt = CT2_MARGIN + CT2_BOX_EXTRAMARGIN + t.getTotalWidth();

            Rectangle colDims = new Rectangle( tableXlft, footerHgt + 3*PAD, tableXrgt, ulY );
            // LogService.logIt( "BaseCT2ReportTemplate.addAssessmentOverview() col1 dims=" + colDims1.getLeft() + "," + colDims1.getBottom() + " - " + colDims1.getRight() + "," + colDims1.getTop() );

            float heightNoHeader = tableHeight - tableHeaderHeight;


            Object[] dta = calcTableHghtUsed(colDims.getTop() - colDims.getBottom() - tableHeaderHeight, 0, t.getHeaderRows(), rowCount-t.getFooterRows(), t.isSplitLate(), rowHgts ); //   colDims.getTop() - colDims.getBottom() - headerHeight;
            int nextIndex = (Integer) dta[0];
            float heightUsedNoHeader = (Float) dta[1];
            float residual = (Float) dta[2];

            // LogService.logIt( "BaseCT2ReportTemplate.addTableToDocument() tableHeight=" + t.calculateHeights() + ", headerHeight=" + headerHeight + ", maxRowHeight=" + maxRowHeight + ", heightAvailNewPage=" + heightAvailNewPage + ", initial heightUsedNoHeader=" + heightUsedNoHeader + ", residual=" + residual );


            ColumnText ct = new ColumnText( pdfWriter.getDirectContent() );
            setRunDirection( ct );

            // NOTE - this forces Composite mode (using ColumnText.addElement)
            ct.addElement( t );

            ct.setSimpleColumn( colDims.getLeft(), colDims.getBottom(), colDims.getRight(), colDims.getTop() );
            // ct.setSimpleColumn( colDims1 );


            int status = ct.go();

            // int linesWritten = ct.getLinesWritten();

            // LogService.logIt( "BaseCT2ReportTemplate.addTableToDocument() initial lines written. NO_MORE_COLUMN=" + ColumnText.NO_MORE_COLUMN + ", NO_MORE_TEXT=" + ColumnText.NO_MORE_TEXT  );

            int pages = 0;

            float heightNeededNoHeader = heightNoHeader - heightUsedNoHeader;

            float hgtUsedThisPage;

            // If need to add any pages
            // while( ColumnText.hasMoreText( status ) && heightNeededNoHeader>0 && pages<20 )
            while( ColumnText.hasMoreText( status ) && heightNeededNoHeader >-300 && pages<20 ) // 6-28-2019 - removed the restriction on height as there's something not quite right.
            {
                // Top of writable area
                ulY = pageHeight - headerHgt - 3*PAD;


                dta = calcTableHghtUsed(heightAvailNewPage, residual, nextIndex, rowCount-t.getFooterRows(), t.isSplitLate(), rowHgts ); //   colDims.getTop() - colDims.getBottom() - headerHeight;
                nextIndex = (Integer) dta[0];
                hgtUsedThisPage = (Float) dta[1];
                residual = (Float) dta[2];

                heightUsedNoHeader += hgtUsedThisPage;

                heightNeededNoHeader = heightNoHeader - heightUsedNoHeader;

                // LogService.logIt( "BaseCT2ReportTemplate.addTableToDocument() AFTER adding next page. hgtUsedThisPage=" + hgtUsedThisPage +  ", Total HeightNeededNoHeader=" + heightNeededNoHeader + ", Total HeightUsedNoHeader=" + heightUsedNoHeader + ", pages=" + pages );

                colDims = new Rectangle( tableXlft, ulY - heightAvailNewPage , tableXrgt, ulY );

                document.newPage();

                ct.setSimpleColumn( colDims.getLeft(), colDims.getBottom(), colDims.getRight(), colDims.getTop() );

                ct.setYLine( colDims.getTop() );

                status = ct.go();

                // linesWritten += ct.getLinesWritten();

                // LogService.logIt( "BaseCT2ReportTemplate.addTableToDocument() status=" + status + ", ColumnText.hasMoreText( status )=" + ColumnText.hasMoreText( status ) );

                pages++;
            }

            return ct.getYLine();
        }

        catch( ExceptionConverter e  )
        {
            Exception ee = e.getException();

            if( ee!=null && ( ee instanceof com.itextpdf.text.pdf.BadPdfFormatException || ee instanceof com.itextpdf.text.pdf.PdfException ) )
                LogService.logIt( "BaseCT2ReportTemplate.addTableToDocument() (ExceptionConverter) " + ee.toString() );
            else
                LogService.logIt( e, "BaseCT2ReportTemplate.addTableToDocument() (ExceptionConverter) " + ee.toString() );

            return startY;
        }
        catch( com.itextpdf.text.pdf.BadPdfFormatException e  )
        {
            LogService.logIt( "BaseCT2ReportTemplate.addTableToDocument() (BadPdfFormat) " + e.toString() );
            return startY;
        }
        catch( com.itextpdf.text.pdf.PdfException e )
        {
            LogService.logIt( "BaseCT2ReportTemplate.addTableToDocument() (Pdf) " + e.toString() );
            return startY;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseCT2ReportTemplate.addTableToDocument() " );
            return startY;
        }
    }
    
    public float addContinuedNextPage( float startY, String text ) throws Exception
    {
        try
        {
            //LogService.logIt( "BaseCt2ReportTemplate.addContinuedNextPage() AAA startY=" + startY );
            if( startY <= MIN_HEIGHT_FOR_CONTINUED_TEXT ) //12*PAD )
                return startY;

            if( text==null || text.isBlank() )
                text = lmsg( "g.ContdNxtPg");

            previousYLevel =  currentYLevel;

            PdfPTable t = new PdfPTable( 1 );
            t.setTotalWidth( new float[] { pageWidth-2*CT2_MARGIN } );
            t.setLockedWidth( true );
            setRunDirection(t);

            PdfPCell c = new PdfPCell( new Phrase(text, getFont()) );
            c.setPadding( TPAD );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderWidth( 0 );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            setRunDirection(c);
            t.addCell( c );

            float tableH = t.calculateHeights();
            float tableW = t.getTotalWidth();

            float tableX = (pageWidth - tableW)/2;

            float y = (startY - TPAD )*0.75f;

            if( y < tableH + 20 )
            {
                // table will not fit.
                if( startY < tableH + 15 )
                    return startY;

                y = startY - 10;
            }

            //LogService.logIt( "BaseCt2ReportTemplate.addContinuedNextPage() BBB startY=" + startY + ", tableH=" + tableH +", y=" + y );

            //if( y + tableH + TPAD >= startY )
            //{
                //LogService.logIt( "BaseCt2ReportTemplate.addContinuedNextPage() CCC returning because y + tableH + TPAD=" + (y + tableH + TPAD) + " is greater than startY=" + startY );
            //    return startY;
            //}

            t.writeSelectedRows(0, -1,tableX, y, pdfWriter.getDirectContent() );

            return y;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseCT2ReportTemplate.addContinuedNextPage()" );
            throw new STException( e );
        }
    }

    


    /**
     * Returns
     *    Next Index -- if three is a residual, it's the index of the residual, else it's the next index
     *    Amount of height used
     *    Residual height unused from split cell
     *
     * @param maxRoom
     * @param startIndex
     * @param maxIndex
     * @param isSplitLate
     * @param rowHgts
     * @return
     */
    public Object[] calcTableHghtUsed( float maxRoom, float prevResidual, int startIndex, int maxIndex, boolean isSplitLate, float[] rowHgts )
    {
        // LogService.logIt( "BaseCoreTestReportTemplt.calcTableHghtUsed( maxRoom=" + maxRoom + ", prevResidual=" + prevResidual + ", startIndex=" + startIndex + ", maxIndex=" + maxIndex + ", isSplitLate=" + isSplitLate + ", " + ")");

        Object[] dta = new Object[] {startIndex, Float.valueOf(0), Float.valueOf(0)};

        if( rowHgts.length<=startIndex )
            return dta;

        float hgt = 0;
        float resid = 0;

        if( prevResidual>0 )
        {
            // Bigger than max
            if( prevResidual>= maxRoom )
            {
                dta[1] = Float.valueOf(maxRoom);
                dta[2] = Float.valueOf(prevResidual -  maxRoom);

                if( prevResidual== maxRoom)
                {
                    dta[0]=startIndex+1;
                    dta[2] = Float.valueOf(0);
                }

                return dta;
            }

            hgt = prevResidual;
            maxRoom -= prevResidual;
            startIndex++;
        }

        for( int i=startIndex; i<rowHgts.length && i<=maxIndex; i++ )
        {
            if( rowHgts[i] + hgt == maxRoom )
            {
                dta[0]=i+1;
                dta[1] = Float.valueOf(hgt);
                return dta;
            }

            if( rowHgts[i] + hgt > maxRoom )
            {
                if( i==startIndex || !isSplitLate )
                {
                    // LogService.logIt( "BaseCoreTestReportTemplt.calcTableHghtUsed() AAA i=" + i + ", hgt=" + hgt );

                    resid = rowHgts[i] - (maxRoom-hgt);
                    dta[2] = resid;
                    hgt = maxRoom;

                    // LogService.logIt( "BaseCoreTestReportTemplt.calcTableHghtUsed() BBB hgt=" + hgt + ", resid=" + resid );
                }

                dta[0] = i;
                dta[1] = hgt;
                return dta;
            }

            hgt += rowHgts[i];
        }

        dta[0] = maxIndex+1;
        dta[1] = hgt;
        return dta;
    }

    
    
    
    
}
