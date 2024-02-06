/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.orgjustice.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tm2batch.custom.orgjustice.OrgJusticeDataset;
import com.tm2batch.entity.purchase.Product;
import com.tm2batch.global.STException;
import com.tm2batch.pdf.ITextUtils;
import com.tm2batch.pdf.ReportData;
import com.tm2batch.pdf.ReportTemplate;
import com.tm2batch.pdf.TableBackground;
import com.tm2batch.service.LogService;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


/**
 *
 * @author Mike
 */
public abstract class BaseUMinnJusticeReportTemplate extends UMinnJusticeReportSettings implements ReportTemplate
{
    static String BULLET = "\u2022";

    Document document = null;

    ByteArrayOutputStream baos;

    PdfWriter pdfWriter;

    float pageWidth = 0;
    float pageHeight = 0;
    float usablePageHeight = 0;

    String title;

    float headerHgt;
    float footerHgt;

    float lastY = 0;

    TableBackground dataTableEvent;
    TableBackground tableHeaderRowEvent;

    // ReportUtils reportUtils;

    float PAD = 5;
    float TPAD = 8;

    // float bxX;
    // float bxWid;
    //float barGrphWid;
    //float barGrphX;
    float lineW = 0.8f;


    float currentYLevel = 0;
    float previousYLevel = 0;

    OrgJusticeDataset dataSet;
    
    int scoreScheme = 0;


    @Override
    public abstract byte[] generateReport() throws Exception;


    public synchronized void initFonts() throws Exception
    {
        initSettings( reportData );
        
        initExtra( reportData );
        
    }

    

    @Override
    public void init( ReportData rd ) throws Exception
    {
        reportData = rd; // new ReportData( rd.getTestKey(), rd.getTestEvent(), rd.getReport(), rd.getUser(), rd.getOrg() );

        dataSet = (OrgJusticeDataset) rd.objArray[0];
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
        
        initData();
        
        /*
         scorescheme=1 = development report
        */
        scoreScheme = reportData.getReportRuleAsInt("scorescheme");
        
        document = new Document( PageSize.LETTER );

        baos = new ByteArrayOutputStream();

        pdfWriter = PdfWriter.getInstance(document, baos);

        UMinnJusticeHeaderFooter hdr = new UMinnJusticeHeaderFooter( document, rd.getLocale(), lmsg("title"), reportData, this );

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

        dataTableEvent = new TableBackground( BaseColor.LIGHT_GRAY , 0.2f, BaseColor.WHITE );

        tableHeaderRowEvent = new TableBackground( null , 0, getTablePageBgColor() );
        
        
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
    
 
   public float addBoldRegPair( float startY, String boldTxt, String regTxt ) throws Exception
    {
        try
        {
            //if( !reportData.getIsLTR() )
            //    return addTitleRTL( startY,  title,  subtitle );

            if( startY > 0 )
            {
                float ulY = startY - 6* PAD;

                if( ulY < footerHgt + 3*PAD )
                {
                    document.newPage();

                    startY = 0;
                }
            }

            previousYLevel =  currentYLevel;

            Font fnt =   getFontLargeBold();

            float y = startY>0 ? startY - fnt.getSize() - TPAD :  pageHeight - headerHgt - fnt.getSize() - TPAD;
            // float y = startY>0 ? startY - fnt.getSize() - 2*PAD :  pageHeight - headerHgt - fnt.getSize() - 2*PAD;

            // float y = previousYLevel - 6*PAD - getFont().getSize();

            // Add Title
            ITextUtils.addDirectText( pdfWriter, boldTxt, CT2_MARGIN + CT2_TEXT_EXTRAMARGIN, y, reportData.getIsLTR() ? Element.ALIGN_LEFT : Element.ALIGN_RIGHT, fnt, false);

            // No subtitle
            if( regTxt==null || regTxt.isEmpty() )
                return y;

            // Change getFont()
            fnt =  getFontLarge();

            float leading = fnt.getSize();

            float spaceLeft = y - PAD - footerHgt;

            // LogService.logIt( "BaseUMinnJusticeReportTemplate.addAssessmentOverview() y for title=" + y + ", spaceleft=" + spaceLeft );

            float txtW = pageWidth - 2*CT2_MARGIN-2*CT2_TEXT_EXTRAMARGIN;

            float txtHght = ITextUtils.getDirectTextHeight( pdfWriter, regTxt, txtW, reportData.getIsLTR() ? Element.ALIGN_LEFT : Element.ALIGN_RIGHT, leading, fnt);

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

                ITextUtils.addDirectText(  pdfWriter, regTxt, rect, reportData.getIsLTR() ? Element.ALIGN_LEFT : Element.ALIGN_RIGHT, leading, fnt, false );

                // LogService.logIt( "BaseUMinnJusticeReportTemplate.addAssessmentOverview() enough space is avail on page txtHght=" + txtHght + " vs spaceLeft=" + spaceLeft + ", currentYLevelBefore=" + currentYLevel + ", currentLevelAfter=y=" + (y-txtHght) );
                currentYLevel = y - txtHght;

                return currentYLevel;
            }

            else
            {
                LogService.logIt( "BaseUMinnJusticeReportTemplate.addBoldRegPair() RTL or overview text height is too high at " + txtHght + " vs spaceLeft=" + spaceLeft + ", using columns." );

                // llx,lly,urx,ury
                Rectangle colDims1 = new Rectangle( txtLlx, footerHgt + 2*PAD, txtUrx, footerHgt + spaceLeft );

                // LogService.logIt( "BaseUMinnJusticeReportTemplate.addAssessmentOverview() col1 dims=" + colDims1.getLeft() + "," + colDims1.getBottom() + " - " + colDims1.getRight() + "," + colDims1.getTop() );

                //float c2h = txtHght - spaceLeft;

                Rectangle colDims2 = new Rectangle( txtLlx, footerHgt + 2*PAD, txtUrx, pageHeight - headerHgt - 2*PAD );

                // LogService.logIt( "BaseUMinnJusticeReportTemplate.addAssessmentOverview() col2 dims=" + colDims2.getLeft() + "," + colDims2.getBottom() + " - " + colDims2.getRight() + "," + colDims2.getTop() );

                ColumnText ct = new ColumnText( pdfWriter.getDirectContent() );

                setRunDirection( ct );

                Phrase p = new Phrase( regTxt, fnt );

                // p.setLeading( leading );
                ct.setLeading( leading ); // fnt.getSize() );

                ct.addText( p );

                ct.setSimpleColumn( colDims1.getLeft(), colDims1.getBottom(), colDims1.getRight(), colDims1.getTop() );
                // ct.setSimpleColumn( colDims1 );

                int status = ct.go();

                while( ColumnText.hasMoreText(status) )
                {
                    LogService.logIt( "BaseUMinnJusticeReportTemplate.addBoldRegPair() adding second column "  );

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
            LogService.logIt( e, "BaseUMinnJusticeReportTemplate.addBoldRegPair()" );

            throw new STException( e );
        }
    }
     
    
    
    public void addCoverPage() throws Exception
    {
        try
        {
            //ITextUtils.addDirectColorRect( pdfWriter, uminnMaroon, 0, pageHeight-100, pageWidth, 100, 0, 1, true );            
            
            float y = pageHeight; // = pageHeight - headerLogo.getScaledHeight() - 10;  // ( (pageHeight - t.getTotalHeight() )/2 ) - cfLogo.getHeight() )/2 - cfLogo.getHeight();
            
            //java.util.List<Chunk> cl = new ArrayList<>();

            //cl.add( new Chunk( lmsg( "g.Univ" ), titleHeaderFont) );

            //cl.add( new Chunk( lmsg( "g.driven" ), titleHeaderFont2 ) );
           
            float tableWid = pageWidth; // ITextUtils.getMaxChunkWidth( cl ) + 20 + headerLogo.getScaledWidth();

            //if( tableWid > pageWidth-120 )
            //    tableWid = pageWidth - 120;            
            
            // First create the table
            PdfPCell c;

            // First, add a table
            PdfPTable t = new PdfPTable( 4 );  
            t.setWidths(new float[] { 20,20,70,20 } );
            t.setTotalWidth( tableWid );
            t.setLockedWidth( true );
            t.setHorizontalAlignment( Element.ALIGN_CENTER );
            setRunDirection(t);
            
            c = new PdfPCell( new Phrase("") );
            c.setBorder( Rectangle.NO_BORDER );
            c.setPadding( 5 );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setBackgroundColor(uminnMaroon);
            setRunDirection( c );
            t.addCell(c);            

            c = new PdfPCell( headerLogo );
            c.setBorder( Rectangle.NO_BORDER );
            c.setPadding( 5 );
            c.setHorizontalAlignment( Element.ALIGN_RIGHT );
            c.setBackgroundColor(uminnMaroon);
            setRunDirection( c );
            t.addCell(c);            

            Phrase ph = new Phrase( lmsg( "g.Univ" ), titleHeaderFont);
            
            ph.add( new Chunk( "\n" + lmsg( "g.driven" ), titleHeaderFont2));
            
            c = new PdfPCell( ph );
            c.setBorder( Rectangle.NO_BORDER );
            c.setPadding( 5 );            
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setBackgroundColor(uminnMaroon);
            setRunDirection( c );
            t.addCell(c);            

            c = new PdfPCell( new Phrase( "", titleHeaderFont2) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setPadding( 5 );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setBackgroundColor(uminnMaroon);
            setRunDirection( c );
            t.addCell(c);            
            
            int pad = 4;
            
            float tableH = t.calculateHeights(); //  + 500;

            float tableY = pageHeight; // -tableH; //  + 10 - (y - pageHeight/2 - tableH)/2;

            float tableW = t.getTotalWidth();

            float tableX = 0; // (pageWidth - tableW)/2;

            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );            

            // ITextUtils.addDirectColorRect( pdfWriter, uminnMaroon, 0, pageHeight-tableH-2*pad, pageWidth, tableH+2*pad, 0, 1, true );            
            
            // ITextUtils.addDirectImage( pdfWriter, headerLogo, ( pageWidth-headerLogo.getScaledWidth())/2 , y, false );

            y = pageHeight-tableH-2*pad - 150;
            
            Date testDate = new Date();
            
            Calendar cal = new GregorianCalendar();
            
            cal.setTime(testDate);
            
            DateFormatSymbols dfs = new DateFormatSymbols();
            
            String monthYearStr = dfs.getMonths()[cal.get(Calendar.MONTH)] + ", "  + cal.get( Calendar.YEAR);
                        
            java.util.List<Chunk> cl  = new ArrayList<>();

            cl.add( new Chunk( lmsg( "title" ), headerFontXXLargeMaroon ) );

            cl.add( new Chunk( lmsg( "g.PreparedForC" ), headerFontXLargeMaroon ) );

            cl.add( new Chunk( reportData.getUserName(), headerFontXLargeMaroon ) );
            
            cl.add( new Chunk( monthYearStr, headerFontXLargeMaroon ) );
           
            tableWid = ITextUtils.getMaxChunkWidth( cl ) + 20;

            if( tableWid > pageWidth-120 )
                tableWid = pageWidth - 120;
            
            // First create the table
            // PdfPCell c;

            // First, add a table
            t = new PdfPTable( 1 );

            t.setTotalWidth( new float[] { tableWid } );
            t.setLockedWidth( true );
            setRunDirection(t);

            c = t.getDefaultCell();
            c.setPadding( 5 );
            // c.setPaddingRight( 15 );
            // c.setPaddingBottom( 25 );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderWidth( 0 );
            setRunDirection(c);

            
            t.addCell( new Phrase( lmsg( "title" ), headerFontXXLargeMaroon ) );
            
            t.addCell( new Phrase( "\n\n\n\n\n" + lmsg( "g.PreparedForC" ), headerFontXLargeMaroon ) );
            
            t.addCell( new Phrase( "\n" + reportData.getUserName(), headerFontXLargeMaroon ) );
            
            t.addCell( new Phrase( "\n" + monthYearStr, headerFontXLargeMaroon ) );
            
            tableH = t.calculateHeights(); //  + 500;

            tableY = y; //  + 10 - (y - pageHeight/2 - tableH)/2;

            tableW = t.getTotalWidth();

            tableX = (pageWidth - tableW)/2;

            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );

            
            
            // addDirectText( "Assessment", 300, 300, baseFontCalibri, 24, getHraOrangeColor(), false );


            t = new PdfPTable( 2 );
            
            // t.setWidths( new int[] {50,200} );
            
            cl.clear();

            cl.add( new Chunk( lmsg( "url" ), fontXLargeBoldWhite ) );

            cl.add( new Chunk( lmsg( "twitter" ), fontXLargeBoldWhite ) );
           
            tableWid = ITextUtils.getMaxChunkWidth( cl ) + twitterLogo.getScaledWidth() + 10;            

            t.setHorizontalAlignment( Element.ALIGN_CENTER );
            
            t.setWidths( new float[]{twitterLogo.getScaledWidth(),  ITextUtils.getMaxChunkWidth( cl )+10});
            t.setTotalWidth( tableWid );
            t.setLockedWidth( true );
            setRunDirection(t);

            c = t.getDefaultCell();
            c.setPadding( 2 );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderWidth( 0 );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            setRunDirection(c);
                             
            t.addCell( new Phrase( "", fontXLargeBoldWhite ) );
            t.addCell( new Phrase( lmsg( "url" ), fontXLargeBoldWhite ) );

            
            t.addCell( twitterLogo );
            t.addCell( new Phrase( lmsg( "twitter" ), fontXLargeBoldWhite ) );

            tableH = t.calculateHeights(); //  + 500;

            tableY = tableH + 12; //   pageHeight/2 - (pageHeight/2 - tableH)/2;

            tableW = t.getTotalWidth();

            tableX = (pageWidth - tableW)/2;

            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );

            // Add the maroon below
            ITextUtils.addDirectColorRect( pdfWriter, getHraBaseReportColor(), 0, 0, pageWidth, tableH + 20, 0, 1, true );

            
        }

        catch( DocumentException e )
        {
            LogService.logIt( e, "BaseUMinnJusticeReportTemplate.addCoverPage()" );
        }
    }

    
    public void addNewPage() throws Exception
    {
        document.newPage();
        this.currentYLevel = pageHeight - PAD -  headerHgt;
    }

    
    
    
    protected void addPreparationNotesSection() throws Exception
    {
        try
        {
            List<String> prepNotes = new ArrayList<>();
            
            // LogService.logIt(  "BaseUMinnJusticeReportTemplate.addPreparationNotesSection() START" );

            //if( reportData.getReport().getIncludeNorms()>0 && hasComparisonData() )
             //    prepNotes.add( 0, lmsg( "g.CT3ComparisonVsOverallNote" ) );



            //if( !devel )
            //    prepNotes.add( 0, lmsg( "g.CT3RptCaveat" ) );
            //else
            //    prepNotes.add( 0, lmsg( "g.CT3RptCaveatDevel" ) );

            Product p = reportData.p;

            Calendar cal = new GregorianCalendar();            
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm z");            
            String dtStr = df.format( cal.getTime() );
            
            if( prepNotes.isEmpty() )
                return;

            if( currentYLevel <= footerHgt + 200 )            
            {    addNewPage();
                 previousYLevel =  currentYLevel;                 
            }

            
            
            float y = currentYLevel; // addTitle( currentYLevel, lmsg( "g.PreparationNotes" ), null );

            // First create the table
            PdfPCell c;

            // First, add a table
            PdfPTable t = new PdfPTable( new float[] { 4,70 } );

            float outerWid = pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN;

            // t.setHorizontalAlignment( Element.ALIGN_CENTER );
            t.setTotalWidth( outerWid );
            t.setLockedWidth( true );
            setRunDirection( t );

            Font tcFont = this.getFont();
            Font bFont = this.getFontXLargeBold();


            c = new PdfPCell( new Phrase( lmsg( "g.PreparationNotes" ), this.getFontBold() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setColspan(2);
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            //c.setBackgroundColor( BaseColor.WHITE );
            //c.setBorderColor( ct2Colors.scoreBoxBorderColor );
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);
            
            for( String ct : prepNotes )
            {
                if( ct.isEmpty() )
                    continue;

                c = new PdfPCell( new Phrase( BULLET, bFont ));
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_RIGHT );
                c.setVerticalAlignment( Element.ALIGN_MIDDLE);
                //c.setBackgroundColor( BaseColor.WHITE );
                //c.setBorderColor( ct2Colors.scoreBoxBorderColor );
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);
                c = new PdfPCell( new Phrase( ct, tcFont ) );
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setVerticalAlignment( Element.ALIGN_MIDDLE);
                //c.setBackgroundColor( BaseColor.WHITE );
                //c.setBorderColor( ct2Colors.scoreBoxBorderColor );
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);
            }
            

            float tableH = t.calculateHeights(); //  + 500;            
            
            float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;

            float tableW = t.getTotalWidth();

            float tableX = (pageWidth - tableW)/2;

            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "BaseUMinnJusticeReportTemplate.addPreparationNotesSection()" );

            throw new STException( e );
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
    
    


}
