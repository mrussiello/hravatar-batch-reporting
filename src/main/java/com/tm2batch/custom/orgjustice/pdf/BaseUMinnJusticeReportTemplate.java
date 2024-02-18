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
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tm2batch.custom.orgjustice.OrgJusticeDataset;
import com.tm2batch.custom.orgjustice.OrgJusticeNorms;
import com.tm2batch.custom.orgjustice.UMinnJusticeDimensionType;
import com.tm2batch.custom.orgjustice.UMinnJusticeGroupType;
import com.tm2batch.custom.orgjustice.UMinnJusticeItemType;
import com.tm2batch.global.STException;
import com.tm2batch.pdf.ITextUtils;
import com.tm2batch.pdf.ReportData;
import com.tm2batch.pdf.ReportTemplate;
import com.tm2batch.pdf.TableBackground;
import com.tm2batch.service.LogService;
import com.tm2batch.util.I18nUtils;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
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
    
    UMinnJusticeReportUtils uminnJusticeReportUtils = null;


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
            c.setPaddingTop(10);
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
            
            String monthYearStr = dfs.getMonths()[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.DAY_OF_MONTH) + ", "  + cal.get( Calendar.YEAR);
                        
            java.util.List<Chunk> cl  = new ArrayList<>();

            cl.add( new Chunk( lmsg( "title" ), headerFontXXLargeMaroon ) );

            cl.add( new Chunk( lmsg( "g.PreparedForC" ), headerFontXLargeMaroon ) );

            cl.add( new Chunk( reportData.getOrgName(), headerFontXLargeMaroon ) );
            
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
            
            t.addCell( new Phrase( reportData.getOrgName(), headerFontXLargeMaroon ) );
            
            t.addCell( new Phrase( monthYearStr, headerFontXLargeMaroon ) );
            
            tableH = t.calculateHeights(); //  + 500;

            tableY = y; //  + 10 - (y - pageHeight/2 - tableH)/2;

            tableW = t.getTotalWidth();

            tableX = (pageWidth - tableW)/2;

            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );

            
            
            // addDirectText( "Assessment", 300, 300, baseFontCalibri, 24, getHraOrangeColor(), false );


            t = new PdfPTable( 1 );
            
            // t.setWidths( new int[] {50,200} );
            
            cl.clear();
            cl.add( new Chunk( lmsg( "url" ), fontXLargeBoldWhite ) );

            // cl.add( new Chunk( lmsg( "twitter" ), fontXLargeBoldWhite ) );
           
            tableWid = ITextUtils.getMaxChunkWidth( cl ) + 10;            
            // tableWid = ITextUtils.getMaxChunkWidth( cl ) + twitterLogo.getScaledWidth() + 10;            

            t.setHorizontalAlignment( Element.ALIGN_CENTER );
            
            // t.setWidths( new float[]{twitterLogo.getScaledWidth(),  ITextUtils.getMaxChunkWidth( cl )+10});
            t.setWidths( new float[]{ITextUtils.getMaxChunkWidth( cl )+20});
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
                             
            t.addCell( new Phrase( lmsg( "url" ), fontXLargeBoldWhite ) );

            tableH = t.calculateHeights(); //  + 500;

            tableY = tableH + 12; //   pageHeight/2 - (pageHeight/2 - tableH)/2;

            tableW = t.getTotalWidth();

            tableX = (pageWidth - tableW)/2;

            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );

            // Add the maroon below
            ITextUtils.addDirectColorRect( pdfWriter, uminnMaroon, 0, 0, pageWidth, tableH + 20, 0, 1, true );

            
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

    
    protected void addDimensionScoresDetailTable(int uminnJusticeDimensionTypeId) throws Exception
    {
        try
        {
            UMinnJusticeDimensionType dimType = UMinnJusticeDimensionType.getValue(uminnJusticeDimensionTypeId );
            
            float y = currentYLevel; // addTitle( currentYLevel, lmsg( "g.PreparationNotes" ), null );

            // First create the table
            PdfPCell c;

            float outerWid = pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN;

            PdfPTable t;
            float tableH; //  + 500;                      
            float tableY; //  usablePageHeight - (usablePageHeight- tableH)/4;
            float tableW;
            float tableX;
            
            // First, add a table
            if( uminnJusticeDimensionTypeId==1 )
            {
                t = new PdfPTable( 1 );
                t.setHorizontalAlignment( Element.ALIGN_CENTER );
                t.setTotalWidth( outerWid );
                t.setLockedWidth( true );
                t.setSplitRows(true);
                setRunDirection( t );

                c = new PdfPCell( new Paragraph( lmsg("bnch.dimension.title"), this.getFontBold() ));
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setColspan(1);
                c.setPadding( 4 );
                c.setPaddingTop(15);
                setRunDirection( c );
                t.addCell(c);
                
                c = new PdfPCell( new Paragraph( lmsg("bnch.dimension.p1"), this.getFont() ));
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setColspan(1);
                c.setPadding( 4 );
                setRunDirection( c );
                t.addCell(c);
                                                    
                tableH = t.calculateHeights(); //  + 500;                      
                // float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;
                tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;
                tableW = t.getTotalWidth();
                tableX = (pageWidth - tableW)/2;
                t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );

                y -= (tableH + 20);
                currentYLevel = y;
            }
                        
            Font dataFont = getFontXSmall();
            
            // 9 columns
            t = new PdfPTable( new float[]{30,14,14,14,14,14} );
            t.setHorizontalAlignment( Element.ALIGN_CENTER );
            t.setTotalWidth( outerWid );
            t.setLockedWidth( true );
            t.setSplitRows(true);
            setRunDirection( t );

            // First ROW
            c = new PdfPCell( new Paragraph( dimType.getName(), this.getFontBold() ));
            c.setBorder( Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "g.All" ), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "g.Female" ), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "g.Male" ), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            

            c = new PdfPCell( new Paragraph( lmsg( "g.Urim" ), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( lmsg( "g.NonUrim" ), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            
            // Next ROW - Your Scores
            c = new PdfPCell( new Paragraph( lmsg("g.YourScore"), dataFont ));
            c.setBorder( Rectangle.BOX );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getDimensionAverages()[uminnJusticeDimensionTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getDimensionAveragesFemale()[uminnJusticeDimensionTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getDimensionAveragesMale()[uminnJusticeDimensionTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getDimensionAveragesUrim()[uminnJusticeDimensionTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getDimensionAveragesNonUrim()[uminnJusticeDimensionTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);


            // Next Row - BENCHMARK SCORES
            c = new PdfPCell( new Paragraph( lmsg("g.Benchmark"), dataFont ));
            c.setBorder( Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, OrgJusticeNorms.dimensionAverages[uminnJusticeDimensionTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, OrgJusticeNorms.dimensionAveragesFemale[uminnJusticeDimensionTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, OrgJusticeNorms.dimensionAveragesMale[uminnJusticeDimensionTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, OrgJusticeNorms.dimensionAveragesUrim[uminnJusticeDimensionTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, OrgJusticeNorms.dimensionAveragesNonUrim[uminnJusticeDimensionTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);


            // Next, get Item ids to use
            // Next Row - Member Items
            c = new PdfPCell( new Paragraph( lmsg("g.MemberItems"), dataFont ));
            c.setBorder( Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(6);
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            UMinnJusticeItemType itemType;
            
            if( uminnJusticeReportUtils==null )
                uminnJusticeReportUtils=new UMinnJusticeReportUtils();
            
            for( int i=0;i<16;i++ )
            {
                if( !dimType.includesItemNumber(i+1) )
                    continue;
                
                itemType = UMinnJusticeItemType.getValue(i+1 );
                
                c = new PdfPCell( new Paragraph( itemType.getUminnJusticeItemTypeId() + ". " + itemType.getName(uminnJusticeReportUtils), dataFont ));
                c.setBorder( Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);

                c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getItemScoreAverages()[i], 1), dataFont ));
                c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
                c.setHorizontalAlignment( Element.ALIGN_CENTER );
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);

                c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getItemScoreAveragesFemale()[i], 1), dataFont ));
                c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
                c.setHorizontalAlignment( Element.ALIGN_CENTER );
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);

                c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getItemScoreAveragesMale()[i], 1), dataFont ));
                c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
                c.setHorizontalAlignment( Element.ALIGN_CENTER );
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);

                c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getItemScoreAveragesUrim()[i], 1), dataFont ));
                c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
                c.setHorizontalAlignment( Element.ALIGN_CENTER );
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);

                c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getItemScoreAveragesNonUrim()[i], 1), dataFont ));
                c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
                c.setHorizontalAlignment( Element.ALIGN_CENTER );
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);                
            }
            
            
            tableH = t.calculateHeights(); //  + 500;                      
            // float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;
            tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;
            tableW = t.getTotalWidth();
            tableX = (pageWidth - tableW)/2;
            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );
            
            y -= (tableH + 20);
            currentYLevel = y;
            
            if( uminnJusticeDimensionTypeId==4 )
            {
                t = new PdfPTable( 1 );
                t.setHorizontalAlignment( Element.ALIGN_CENTER );
                t.setTotalWidth( outerWid );
                t.setLockedWidth( true );
                t.setSplitRows(true);
                setRunDirection( t );


                c = new PdfPCell( new Paragraph( lmsg( "bnch.overall.p2" ), this.getFont() ));
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setColspan(1);
                c.setPadding( 4 );
                setRunDirection( c );
                t.addCell(c);


                tableH = t.calculateHeights(); //  + 500;                      
                // float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;
                tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;
                tableW = t.getTotalWidth();
                tableX = (pageWidth - tableW)/2;
                t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );

                y -= (tableH + 20);            
                currentYLevel = y;
            }
            
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseUMinnJusticeReportTemplate.addDimensionScoresDetailTable()" );
            throw new STException( e );
        }                
    }
    
    protected void addGroupScoresDetailTable( int uminnJusticeGroupTypeId ) throws Exception
    {
        try
        {
            UMinnJusticeGroupType groupType = UMinnJusticeGroupType.getValue(uminnJusticeGroupTypeId );
            
            float y = currentYLevel; // addTitle( currentYLevel, lmsg( "g.PreparationNotes" ), null );

            // First create the table
            PdfPCell c;

            float outerWid = pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN;

            PdfPTable t;
            float tableH; //  + 500;                      
            float tableY; //  usablePageHeight - (usablePageHeight- tableH)/4;
            float tableW;
            float tableX;
            
            // First, add a table
            if( uminnJusticeGroupTypeId==1 )
            {
                t = new PdfPTable( 1 );
                t.setHorizontalAlignment( Element.ALIGN_CENTER );
                t.setTotalWidth( outerWid );
                t.setLockedWidth( true );
                t.setSplitRows(true);
                setRunDirection( t );

                c = new PdfPCell( new Paragraph( lmsg("bnch.group.title"), this.getFontBold() ));
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setColspan(1);
                c.setPadding( 4 );
                c.setPaddingTop(15);
                setRunDirection( c );
                t.addCell(c);
                
                c = new PdfPCell( new Paragraph( lmsg("bnch.group.p1"), this.getFont() ));
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setColspan(1);
                c.setPadding( 4 );
                setRunDirection( c );
                t.addCell(c);
                                                    
                tableH = t.calculateHeights(); //  + 500;                      
                // float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;
                tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;
                tableW = t.getTotalWidth();
                tableX = (pageWidth - tableW)/2;
                t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );

                y -= (tableH + 20);
                currentYLevel = y;
            }

            // First, add a table
            else if( uminnJusticeGroupTypeId!=5 && uminnJusticeGroupTypeId!=7 )
            {
                t = new PdfPTable( 1 );
                t.setHorizontalAlignment( Element.ALIGN_CENTER );
                t.setTotalWidth( outerWid );
                t.setLockedWidth( true );
                t.setSplitRows(true);
                setRunDirection( t );

                c = new PdfPCell( new Paragraph( lmsg("bnch.group.title.2"), this.getFontBold() ));
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setColspan(1);
                c.setPadding( 4 );
                c.setPaddingTop(15);
                setRunDirection( c );
                t.addCell(c);
                                                                    
                tableH = t.calculateHeights(); //  + 500;                      
                // float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;
                tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;
                tableW = t.getTotalWidth();
                tableX = (pageWidth - tableW)/2;
                t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );

                y -= (tableH + 20);
                currentYLevel = y;
            }
            
            
            Font dataFont = getFontXSmall();
            
            // 9 columns
            t = new PdfPTable( new float[]{30,14,14,14,14,14} );
            t.setHorizontalAlignment( Element.ALIGN_CENTER );
            t.setTotalWidth( outerWid );
            t.setLockedWidth( true );
            t.setSplitRows(true);
            setRunDirection( t );

            // First ROW
            c = new PdfPCell( new Paragraph( groupType.getName(), this.getFontBold() ) );
            c.setBorder( Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "g.All" ), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "g.Female" ), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "g.Male" ), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            

            c = new PdfPCell( new Paragraph( lmsg( "g.Urim" ), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( lmsg( "g.NonUrim" ), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            
            // Next ROW - Your Scores
            c = new PdfPCell( new Paragraph( lmsg("g.YourScore"), dataFont ));
            c.setBorder( Rectangle.BOX );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getGroupAverages()[uminnJusticeGroupTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getGroupAveragesFemale()[uminnJusticeGroupTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getGroupAveragesMale()[uminnJusticeGroupTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getGroupAveragesUrim()[uminnJusticeGroupTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getGroupAveragesNonUrim()[uminnJusticeGroupTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);


            // Next Row - BENCHMARK SCORES
            // Next ROW - Your Scores
            c = new PdfPCell( new Paragraph( lmsg("g.Benchmark"), dataFont ));
            c.setBorder( Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, OrgJusticeNorms.groupAverages[uminnJusticeGroupTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, OrgJusticeNorms.groupAveragesFemale[uminnJusticeGroupTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, OrgJusticeNorms.groupAveragesMale[uminnJusticeGroupTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, OrgJusticeNorms.groupAveragesUrim[uminnJusticeGroupTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, OrgJusticeNorms.groupAveragesNonUrim[uminnJusticeGroupTypeId-1], 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg("g.GroupItemScores"), dataFont ));
            c.setBorder( Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(6);
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            
            UMinnJusticeItemType itemType;
            
            if( uminnJusticeReportUtils==null )
                uminnJusticeReportUtils = new UMinnJusticeReportUtils();
            
            // Now for each item.
            for( int ii=0;ii<16;ii++ )
            {
                if( !groupType.includeItemForGroup(ii+1))
                    continue;
                
                itemType = UMinnJusticeItemType.getValue(ii+1);
                
                c = new PdfPCell( new Paragraph( itemType.getUminnJusticeItemTypeId() + ". " + itemType.getName(uminnJusticeReportUtils), dataFont ));
                c.setBorder( Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);

                c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getGroupItemScoreAverages()[uminnJusticeGroupTypeId-1][ii], 1), dataFont ));
                c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
                c.setHorizontalAlignment( Element.ALIGN_CENTER );
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);

                c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getGroupItemScoreAveragesFemale()[uminnJusticeGroupTypeId-1][ii], 1), dataFont ));
                c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
                c.setHorizontalAlignment( Element.ALIGN_CENTER );
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);

                c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getGroupItemScoreAveragesMale()[uminnJusticeGroupTypeId-1][ii], 1), dataFont ));
                c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
                c.setHorizontalAlignment( Element.ALIGN_CENTER );
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);

                c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getGroupItemScoreAveragesUrim()[uminnJusticeGroupTypeId-1][ii], 1), dataFont ));
                c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
                c.setHorizontalAlignment( Element.ALIGN_CENTER );
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);

                c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getGroupItemScoreAveragesNonUrim()[uminnJusticeGroupTypeId-1][ii], 1), dataFont ));
                c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
                c.setHorizontalAlignment( Element.ALIGN_CENTER );
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);                
            }
            
            
            tableH = t.calculateHeights(); //  + 500;                      
            // float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;
            tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;
            tableW = t.getTotalWidth();
            tableX = (pageWidth - tableW)/2;
            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );
            
            y -= (tableH + 20);
            currentYLevel = y;
            
            if( uminnJusticeGroupTypeId!=4 && uminnJusticeGroupTypeId!=6 )
            {
                t = new PdfPTable( 1 );
                t.setHorizontalAlignment( Element.ALIGN_CENTER );
                t.setTotalWidth( outerWid );
                t.setLockedWidth( true );
                t.setSplitRows(true);
                setRunDirection( t );


                c = new PdfPCell( new Paragraph( lmsg( "bnch.overall.p2" ), this.getFont() ));
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setColspan(1);
                c.setPadding( 4 );
                setRunDirection( c );
                t.addCell(c);


                tableH = t.calculateHeights(); //  + 500;                      
                // float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;
                tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;
                tableW = t.getTotalWidth();
                tableX = (pageWidth - tableW)/2;
                t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );

                y -= (tableH + 20);            
                currentYLevel = y;
            }
            
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseUMinnJusticeReportTemplate.addGroupScoresDetailTable()" );
            throw new STException( e );
        }                
    }

    
    protected void addOverallScoresDetailTable() throws Exception
    {
        try
        {
            float y = currentYLevel; // addTitle( currentYLevel, lmsg( "g.PreparationNotes" ), null );

            // First create the table
            PdfPCell c;

            float outerWid = pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN;

            // First, add a table
            PdfPTable t = new PdfPTable( 1 );
            t.setHorizontalAlignment( Element.ALIGN_CENTER );
            t.setTotalWidth( outerWid );
            t.setLockedWidth( true );
            t.setSplitRows(true);
            setRunDirection( t );

            c = new PdfPCell( new Paragraph( lmsg( "bnch.overall.title" ), this.getFontBold() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(1);
            c.setPadding( 4 );
            c.setPaddingTop(15);
            setRunDirection( c );
            t.addCell(c);
                        
            c = new PdfPCell( new Paragraph( lmsg( "bnch.overall.p1" ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(1);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);
                        
            
            float tableH = t.calculateHeights(); //  + 500;                      
            // float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;
            float tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;
            float tableW = t.getTotalWidth();
            float tableX = (pageWidth - tableW)/2;
            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );
            
            y -= (tableH + 20);
            currentYLevel = y;
            

            Font dataFont = getFontXSmall();
            
            // 9 columns
            t = new PdfPTable( new float[]{30,14,14,14,14,14} );
            t.setHorizontalAlignment( Element.ALIGN_CENTER );
            t.setTotalWidth( outerWid );
            t.setLockedWidth( true );
            t.setSplitRows(true);
            setRunDirection( t );

            // First ROW
            c = new PdfPCell( new Paragraph( "", this.getFont() ));
            c.setBorder( Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "g.All" ), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "g.Female" ), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "g.Male" ), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            

            c = new PdfPCell( new Paragraph( lmsg( "g.Urim" ), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( lmsg( "g.NonUrim" ), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            
            // Next ROW - Your Scores
            c = new PdfPCell( new Paragraph( lmsg("g.YourScore"), dataFont ));
            c.setBorder( Rectangle.BOX );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getOverallAvg(), 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getOverallAvgFemale(), 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getOverallAvgMale(), 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getOverallAvgUrim(), 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getOverallAvgNonUrim(), 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);


            // Next Row - BENCHMARK SCORES
            // Next ROW - Your Scores
            c = new PdfPCell( new Paragraph( lmsg("g.Benchmark"), dataFont ));
            c.setBorder( Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, OrgJusticeNorms.overallAvg, 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, OrgJusticeNorms.overallAvgFemale, 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, OrgJusticeNorms.overallAvgMale, 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, OrgJusticeNorms.overallAvgUrim, 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getOverallAvgNonUrim(), 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            tableH = t.calculateHeights(); //  + 500;                      
            // float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;
            tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;
            tableW = t.getTotalWidth();
            tableX = (pageWidth - tableW)/2;
            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );
            
            y -= (tableH + 20);
            currentYLevel = y;
            
            t = new PdfPTable( 1 );
            t.setHorizontalAlignment( Element.ALIGN_CENTER );
            t.setTotalWidth( outerWid );
            t.setLockedWidth( true );
            t.setSplitRows(true);
            setRunDirection( t );

                        
            c = new PdfPCell( new Paragraph( lmsg( "bnch.overall.p2" ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(1);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);
                        
            
            tableH = t.calculateHeights(); //  + 500;                      
            // float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;
            tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;
            tableW = t.getTotalWidth();
            tableX = (pageWidth - tableW)/2;
            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );
            
            y -= (tableH + 20);            
            currentYLevel = y;
            
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseUMinnJusticeReportTemplate.addOverallScoresDetailTable()" );
            throw new STException( e );
        }        
    }
    
    
    protected void addOverallScoresPage() throws Exception
    {
        try
        {
            float y = currentYLevel; // addTitle( currentYLevel, lmsg( "g.PreparationNotes" ), null );

            // First create the table
            PdfPCell c;

            float outerWid = pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN;

            // First, add a table
            PdfPTable t = new PdfPTable( 1 );
            t.setHorizontalAlignment( Element.ALIGN_CENTER );
            t.setTotalWidth( outerWid );
            t.setLockedWidth( true );
            t.setSplitRows(true);
            setRunDirection( t );

            c = new PdfPCell( new Paragraph( lmsg( "ovrs.title" ), this.getFontBold() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(1);
            c.setPadding( 4 );
            c.setPaddingTop(15);
            setRunDirection( c );
            t.addCell(c);
                        
            c = new PdfPCell( new Paragraph( lmsg( "ovrs.p1" ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(1);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);
                        
            
            float tableH = t.calculateHeights(); //  + 500;                      
            // float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;
            float tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;
            float tableW = t.getTotalWidth();
            float tableX = (pageWidth - tableW)/2;
            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );
            
            y -= (tableH + 20);
            currentYLevel = y;

            Font dataFont = getFontXSmall();
            
            // 9 columns
            t = new PdfPTable( new float[]{15f,10.6f,10.6f,10.6f,10.6f,10.6f,10.6f,10.6f,10.6f} );
            t.setHorizontalAlignment( Element.ALIGN_CENTER );
            t.setTotalWidth( outerWid );
            t.setLockedWidth( true );
            t.setSplitRows(true);
            setRunDirection( t );

            // First ROW
            c = new PdfPCell( new Paragraph( "", this.getFont() ));
            c.setBorder( Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "g.Group" ), this.getFont() ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setColspan(8);
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            // Next ROW - Group titles
            c = new PdfPCell( new Paragraph( "", dataFont ));
            c.setBorder( Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( lmsg( "g.AllGroups" ), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setVerticalAlignment( Element.ALIGN_MIDDLE );
            c.setColspan(1);
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            UMinnJusticeGroupType gType;
            UMinnJusticeDimensionType dType;
            
            for( int i=0;i<7;i++ )
            {
                gType = UMinnJusticeGroupType.getValue(i+1);
                c = new PdfPCell( new Paragraph( gType.getName(), dataFont ));
                c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
                c.setHorizontalAlignment( Element.ALIGN_CENTER );
                c.setVerticalAlignment( Element.ALIGN_MIDDLE );
                c.setColspan(1);
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);
            }
            
            // Next ROW - All Dims for Group
            c = new PdfPCell( new Paragraph( lmsg( "g.AllDims" ), dataFont ));
            c.setBorder( Rectangle.BOX );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setColspan(1);
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            // Overall Average
            c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getOverallAvg(), 1), dataFont ));
            c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setColspan(1);
            c.setPadding( 3 );
            setRunDirection( c );
            t.addCell(c);
            
            // Groups scores forall dimensions
            for( int i=0;i<7;i++ )
            {
                c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getGroupAverages()[i], 1), dataFont ));
                c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
                c.setHorizontalAlignment( Element.ALIGN_CENTER );
                c.setVerticalAlignment( Element.ALIGN_MIDDLE );
                c.setColspan(1);
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);
            }
            
            // NEXT ROW Each Dimension
            String val;
            for( int d=0;d<4;d++ )
            {
                dType = UMinnJusticeDimensionType.getValue(d+1);

                c = new PdfPCell( new Paragraph( dType.getName(), dataFont ));
                c.setBorder( Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT );
                c.setHorizontalAlignment( Element.ALIGN_CENTER );
                c.setColspan(1);
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);
                
                c = new PdfPCell( new Paragraph( I18nUtils.getFormattedNumber(Locale.US, dataSet.getDimensionAverages()[d], 1), dataFont ));
                c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
                c.setHorizontalAlignment( Element.ALIGN_CENTER );
                c.setVerticalAlignment( Element.ALIGN_MIDDLE );
                c.setColspan(1);
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);
                                
                for( int g=0;g<7;g++ )
                {
                    // gType = UMinnJusticeGroupType.getValue(g+1);
                    val = dType.includesGroupType(g+1) ? I18nUtils.getFormattedNumber(Locale.US, dataSet.getDimensionGroupAverages()[d][g], 1) : "-";
                    c = new PdfPCell( new Paragraph( val, dataFont ));
                    c.setBorder( Rectangle.BOTTOM | Rectangle.RIGHT );
                    c.setHorizontalAlignment( Element.ALIGN_CENTER );
                    c.setVerticalAlignment( Element.ALIGN_MIDDLE );
                    c.setColspan(1);
                    c.setPadding( 3 );
                    setRunDirection( c );
                    t.addCell(c);
                }
                
            }

            
            tableH = t.calculateHeights(); //  + 500;                      
            // float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;
            tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;
            tableW = t.getTotalWidth();
            tableX = (pageWidth - tableW)/2;
            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );

            y -= (tableH + 20);
            currentYLevel = y;
            
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseUMinnJusticeReportTemplate.addOverallScoresPage()" );
            throw new STException( e );
        }        
        
    }
    
    
    protected void addOverallSummaryPage() throws Exception
    {
        try
        {
            float y = currentYLevel; // addTitle( currentYLevel, lmsg( "g.PreparationNotes" ), null );

            // First create the table
            PdfPCell c;

            float outerWid = pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN;

            // First, add a table
            PdfPTable t = new PdfPTable( 1 );
            t.setHorizontalAlignment( Element.ALIGN_CENTER );
            t.setTotalWidth( outerWid );
            t.setLockedWidth( true );
            t.setSplitRows(true);
            setRunDirection( t );

            c = new PdfPCell( new Paragraph( lmsg( "sum.title" ), this.getFontBold() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(1);
            c.setPadding( 4 );
            c.setPaddingTop(15);
            setRunDirection( c );
            t.addCell(c);

            int participantsTotal = dataSet.getTotalParticipants();
            int participantsUnique = (int) dataSet.getOverallCount();
            int um =  (int) dataSet.getOverallCountMale();
            int uf = (int) dataSet.getOverallCountFemale();
            int uu = (int) dataSet.getOverallCountUrim();
            int unu =  (int) dataSet.getOverallCountNonUrim();
            
            Date startD = dataSet.getDates()[0];
            Date endD = dataSet.getDates()[1];
            
            String startDate = I18nUtils.getFormattedDate(Locale.US, startD, DateFormat.MEDIUM );
            String endDate = I18nUtils.getFormattedDate(Locale.US, endD, DateFormat.MEDIUM );
            
            String[] params = new String[]{startDate, endDate, Integer.toString(participantsTotal - participantsUnique), Integer.toString(participantsTotal),Integer.toString(participantsUnique)};

            c = new PdfPCell( new Paragraph( lmsg( "sum.p0.dates", params ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            
            if( participantsTotal>participantsUnique)
                c = new PdfPCell( new Paragraph( lmsg( "sum.p0.trimmed", params ), this.getFont() ));
            else
                c = new PdfPCell( new Paragraph( lmsg( "sum.p0.all", params ), this.getFont() ));                
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            
            c = new PdfPCell( new Paragraph( lmsg( "sum.p1" ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "str.title" ), this.getFontBold() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(1);
            c.setPadding( 4 );
            c.setPaddingTop(15);
            setRunDirection( c );
            t.addCell(c);
            
            String arg0 = dataSet.getTopDimensionName();
            String arg1 = dataSet.getTopGroupName();
            String[] arg2 = dataSet.getTopDimensionGroupNamePair();
            
            c = new PdfPCell( new Paragraph( lmsg( "str.p1", new String[]{arg0,arg1,arg2[0],arg2[1]} ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "str.p2" ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "opp.title" ), this.getFontBold() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(1);
            c.setPadding( 4 );
            c.setPaddingTop(15);
            setRunDirection( c );
            t.addCell(c);
            
            arg0 = dataSet.getBottomDimensionName();
            arg1 = dataSet.getBottomGroupName();
            arg2 = dataSet.getBottomDimensionGroupNamePair();
            
            c = new PdfPCell( new Paragraph( lmsg( "opp.p1", new String[]{arg0,arg1,arg2[0],arg2[1]} ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "opp.p2" ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            
            c = new PdfPCell( new Paragraph( lmsg( "brk.title" ), this.getFontBold() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(1);
            c.setPadding( 4 );
            c.setPaddingTop(15);
            setRunDirection( c );
            t.addCell(c);
            
            c = new PdfPCell( new Paragraph( lmsg( "brk.p0"), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);
            
            PdfPTable t2 = new PdfPTable( new float[]{50,10});
            t2.setHorizontalAlignment( Element.ALIGN_LEFT );
            t2.setTotalWidth( outerWid*0.3f );
            t2.setLockedWidth( true );
            setRunDirection( t2 );
            
            
            PdfPCell c2 = new PdfPCell( new Phrase( "   " + lmsg( "brk.p0.total") + ":", this.getFont() ));
            c2.setBorder( Rectangle.NO_BORDER );
            c2.setHorizontalAlignment( Element.ALIGN_LEFT );
            c2.setPadding( 2 );
            c2.setPaddingBottom(8);
            setRunDirection( c2 );
            t2.addCell(c2);
            c2 = new PdfPCell( new Phrase( Integer.toString( (int)dataSet.getOverallCount() ), this.getFont() ));
            c2.setBorder( Rectangle.NO_BORDER );
            c2.setHorizontalAlignment( Element.ALIGN_LEFT );
            c2.setPadding( 2 );
            c2.setPaddingBottom(8);
            setRunDirection( c2 );
            t2.addCell(c2);
            
            c2 = new PdfPCell( new Phrase( "   " + lmsg( "brk.p0.female")+ ":", this.getFont() ));
            c2.setBorder( Rectangle.NO_BORDER );
            c2.setHorizontalAlignment( Element.ALIGN_LEFT );
            c2.setPadding( 1 );
            setRunDirection( c2 );
            t2.addCell(c2);            
            c2 = new PdfPCell( new Phrase( Integer.toString( (int)dataSet.getOverallCountFemale() ), this.getFont() ));
            c2.setBorder( Rectangle.NO_BORDER );
            c2.setHorizontalAlignment( Element.ALIGN_LEFT );
            c2.setPadding( 1 );
            setRunDirection( c2 );
            t2.addCell(c2);

            c2 = new PdfPCell( new Phrase( "   " + lmsg( "brk.p0.male")+ ":", this.getFont() ));
            c2.setBorder( Rectangle.NO_BORDER );
            c2.setHorizontalAlignment( Element.ALIGN_LEFT );
            c2.setPadding( 1 );
            c2.setPaddingBottom(8);
            setRunDirection( c2 );
            t2.addCell(c2);            
            c2 = new PdfPCell( new Phrase( Integer.toString( (int)dataSet.getOverallCountMale() ), this.getFont() ));
            c2.setBorder( Rectangle.NO_BORDER );
            c2.setHorizontalAlignment( Element.ALIGN_LEFT );
            c2.setPadding( 1 );
            c2.setPaddingBottom(8);
            setRunDirection( c2 );
            t2.addCell(c2);
            
            c2 = new PdfPCell( new Phrase( "   " + lmsg( "brk.p0.urim")+ ":", this.getFont() ));
            c2.setBorder( Rectangle.NO_BORDER );
            c2.setHorizontalAlignment( Element.ALIGN_LEFT );
            c2.setPadding( 1 );
            setRunDirection( c2 );
            t2.addCell(c2);            
            c2 = new PdfPCell( new Phrase( Integer.toString( (int)dataSet.getOverallCountUrim() ), this.getFont() ));
            c2.setBorder( Rectangle.NO_BORDER );
            c2.setHorizontalAlignment( Element.ALIGN_LEFT );
            c2.setPadding( 1 );
            setRunDirection( c2 );
            t2.addCell(c2);
            
            c2 = new PdfPCell( new Phrase( "   " + lmsg( "brk.p0.nonurim")+ ":", this.getFont() ));
            c2.setBorder( Rectangle.NO_BORDER );
            c2.setHorizontalAlignment( Element.ALIGN_LEFT );
            c2.setPadding( 1 );
            setRunDirection( c2 );
            t2.addCell(c2);            
            c2 = new PdfPCell( new Phrase( Integer.toString( (int)dataSet.getOverallCountNonUrim() ), this.getFont() ));
            c2.setBorder( Rectangle.NO_BORDER );
            c2.setHorizontalAlignment( Element.ALIGN_LEFT );
            c2.setPadding( 1 );
            setRunDirection( c2 );
            t2.addCell(c2);
            
            //StringBuilder ss = new StringBuilder();
            //ss.append( "   " + lmsg( "brk.p0.total", new String[]{Integer.toString((int)dataSet.getOverallCount())}) + "\n\n" );
            //ss.append( "   " + lmsg( "brk.p0.female", new String[]{Integer.toString((int)dataSet.getOverallCountFemale())}) + "\n" );
            //ss.append( "   " + lmsg( "brk.p0.male", new String[]{Integer.toString((int)dataSet.getOverallCountMale())}) + "\n\n" );
            //ss.append( "   " + lmsg( "brk.p0.urim", new String[]{Integer.toString((int)dataSet.getOverallCountUrim())}) + "\n" );
            //ss.append( "   " + lmsg( "brk.p0.nonurim", new String[]{Integer.toString((int)dataSet.getOverallCountNonUrim())}) );
            
            // c = new PdfPCell( new Paragraph( ss.toString(), this.getFont() ));
            c = new PdfPCell( t2);
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);
            
            
            arg0 = dataSet.getFemaleBelowMaleDimensionNames();
            arg1 = dataSet.getFemaleBelowMaleGroupNames();
            
            c = new PdfPCell( new Paragraph( lmsg( "brk.p1", new String[]{arg0,arg1} ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            arg0 = dataSet.getUrimBelowNonDimensionNames();
            arg1 = dataSet.getUrimBelowNonGroupNames();
                        
            c = new PdfPCell( new Paragraph( lmsg( "brk.p2", new String[]{arg0,arg1} ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "brk.p3" ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);


            
            
            float tableH = t.calculateHeights(); //  + 500;                      
            // float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;
            float tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;
            float tableW = t.getTotalWidth();
            float tableX = (pageWidth - tableW)/2;
            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseUMinnJusticeReportTemplate.addOverallSummaryPage()" );
            throw new STException( e );
        }        
        
    }
    
    
    protected void addResourcesSection() throws Exception
    {
        try
        {
            float y = currentYLevel; 

            // First create the table
            PdfPCell c;

            float outerWid = pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN;

            // First, add a table
            PdfPTable t = new PdfPTable( new float[]{5,5,80} );
            t.setHorizontalAlignment( Element.ALIGN_CENTER );
            t.setTotalWidth( outerWid );
            t.setLockedWidth( true );
            t.setSplitRows(true);
            setRunDirection( t );

            c = new PdfPCell( new Paragraph( lmsg( "rsc.title" ), this.getFontBold() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            c.setPaddingTop(15);
            setRunDirection( c );
            t.addCell(c);

            Paragraph par;
            Chunk chk;
            PdfAction pdfa;
            
            for( int i=1;i<=7;i++ )
            {
                c = new PdfPCell( new Paragraph( lmsg( "rsc." + i + ".t" ), this.getFont() ));
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setColspan(3);
                c.setPadding( 4 );
                c.setPaddingBottom(1);
                setRunDirection( c );
                t.addCell(c);

                par = new Paragraph();
                chk = new Chunk( lmsg( "rsc." + i + ".u" ),fontSmallBlueItalic );
                pdfa = PdfAction.gotoRemotePage( lmsg( "rsc." + i + ".u" ) , lmsg("b.Click2Visit"), false, true );                                
                chk.setAction( pdfa );
                par.add( chk );                   
                
                c = new PdfPCell(par);
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setColspan(3);
                c.setPadding( 4 );
                c.setPaddingBottom(10);
                setRunDirection( c );
                t.addCell(c);
            }
            
            
            float tableH = t.calculateHeights(); //  + 500;            
            
            // float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;
            float tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;
            float tableW = t.getTotalWidth();
            float tableX = (pageWidth - tableW)/2;
            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );


        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseUMinnJusticeReportTemplate.addResourcesSection()" );
            throw new STException( e );
        }        
        
    }
    
    
    protected void addIntroductionSection() throws Exception
    {
        try
        {
            float y = currentYLevel; // addTitle( currentYLevel, lmsg( "g.PreparationNotes" ), null );

            // First create the table
            PdfPCell c;

            float outerWid = pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN;

            // First, add a table
            PdfPTable t = new PdfPTable( new float[]{5,5,80} );
            t.setHorizontalAlignment( Element.ALIGN_CENTER );
            t.setTotalWidth( outerWid );
            t.setLockedWidth( true );
            t.setSplitRows(true);
            setRunDirection( t );

            c = new PdfPCell( new Paragraph( lmsg( "intro.title" ), this.getFontBold() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            c.setPaddingTop(15);
            setRunDirection( c );
            t.addCell(c);

            for( int i=1;i<=5;i++ )
            {
                c = new PdfPCell( new Paragraph( lmsg( "intro.p" + i ), this.getFont() ));
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setColspan(3);
                c.setPadding( 4 );
                setRunDirection( c );
                t.addCell(c);
            }
            
            c = new PdfPCell( new Paragraph( lmsg( "ojm.title" ), this.getFontBold() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            c.setPaddingTop(15);
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "ojm.p1"  ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            Phrase phr;
            
            for( int i=1;i<=4; i++ )
            {
                // blank row
                c = new PdfPCell( new Phrase( "", font ));
                c.setBorder( Rectangle.NO_BORDER );
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);
                
                c = new PdfPCell( new Phrase( BULLET, font ));
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_RIGHT );
                c.setVerticalAlignment( Element.ALIGN_MIDDLE);
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);

                phr = new Phrase();
                phr.add( new Chunk( lmsg("ojm.b" + i + ".b"), fontBold ) );
                phr.add( new Chunk( " " + lmsg("ojm.b" + i + ".p"), font ) );
                                
                c = new PdfPCell( phr );
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setVerticalAlignment( Element.ALIGN_MIDDLE);
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);
            }
            
            c = new PdfPCell( new Paragraph( lmsg( "ojm.p2"  ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "ojm.p3"  ), fontBold ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setColspan(3);
            c.setPadding( 4 );
            c.setPaddingTop(15);
            setRunDirection( c );
            t.addCell(c);
            
            // adding image
            c = new PdfPCell( figure1Image );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment(Element.ALIGN_CENTER );
            c.setColspan(3);
            c.setPadding( 0 );
            c.setPaddingTop( 12 );
            c.setPaddingBottom( 12 );
            setRunDirection(c);
            t.addCell( c );            

            c = new PdfPCell( new Paragraph( lmsg( "ojm.p4"  ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "ojm.p5"  ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);
            
            float tableH = t.calculateHeights(); //  + 500;            
            
            // float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;
            float tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;

            float tableW = t.getTotalWidth();

            float tableX = (pageWidth - tableW)/2;

            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );


            this.addNewPage();
            y = this.currentYLevel;
             
            
            t = new PdfPTable( new float[]{5,5,80} );
            t.setHorizontalAlignment( Element.ALIGN_CENTER );
            t.setTotalWidth( outerWid );
            t.setLockedWidth( true );
            t.setSplitRows(true);
            setRunDirection( t );

            c = new PdfPCell( new Paragraph( lmsg( "ojm.p6"  ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "ovw.title" ), this.getFontBold() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            c.setPaddingTop(15);
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "ovw.p1"  ), this.getFont() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);


            c = new PdfPCell( getThreeSegmentBoldPhrase( lmsg("ovw.p2.a"), lmsg("ovw.p2.b"), lmsg("ovw.p2.c") ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            for( int i=1;i<=7;i++ )
            {
                // blank row
                c = new PdfPCell( new Phrase( "", font ));
                c.setBorder( Rectangle.NO_BORDER );
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);
                
                c = new PdfPCell( new Phrase( i + ". ", font ));
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_RIGHT );
                c.setVerticalAlignment( Element.ALIGN_MIDDLE);
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);

                c = new PdfPCell( new Phrase( lmsg("ovw.b." + i), font ) );
                c.setBorder( Rectangle.NO_BORDER );
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setVerticalAlignment( Element.ALIGN_MIDDLE);
                c.setPadding( 3 );
                setRunDirection( c );
                t.addCell(c);                
            }
            
            c = new PdfPCell( new Paragraph( lmsg( "ovw.p3"  ), fontBold ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setColspan(3);
            c.setPadding( 4 );
            c.setPaddingTop(15);
            setRunDirection( c );
            t.addCell(c);
            
            // adding image
            c = new PdfPCell( figure2Image );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment(Element.ALIGN_CENTER );
            c.setColspan(3);
            c.setPadding( 0 );
            c.setPaddingTop( 12 );
            c.setPaddingBottom( 12 );
            setRunDirection(c);
            t.addCell( c );            
            
            c = new PdfPCell( new Paragraph( lmsg( "fp.title" ), this.getFontBold() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            c.setPaddingTop(15);
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( getThreeSegmentBoldPhrase( lmsg("fp.p1.a"), lmsg("fp.p1.b"), lmsg("fp.p1.c") ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( getThreeSegmentBoldPhrase( lmsg("fp.p2.a"), lmsg("fp.p2.b"), lmsg("fp.p2.c") ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( new Paragraph( lmsg( "fp.p3"  ), font ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);
                        

            tableH = t.calculateHeights(); //  + 500;            
            tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;
            tableW = t.getTotalWidth();
            tableX = (pageWidth - tableW)/2;
            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );

            this.addNewPage();
            y = this.currentYLevel;
             
            t = new PdfPTable( new float[]{5,5,80} );
            t.setHorizontalAlignment( Element.ALIGN_CENTER );
            t.setTotalWidth( outerWid );
            t.setLockedWidth( true );
            t.setSplitRows(true);
            setRunDirection( t );

            c = new PdfPCell( new Paragraph( lmsg( "fp.p4"  ), fontBold ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_CENTER );
            c.setColspan(3);
            c.setPadding( 4 );
            c.setPaddingTop(15);
            setRunDirection( c );
            t.addCell(c);
            
            // adding image
            c = new PdfPCell( figure3Image );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment(Element.ALIGN_CENTER );
            c.setColspan(3);
            c.setPadding( 0 );
            c.setPaddingTop( 12 );
            c.setPaddingBottom( 12 );
            setRunDirection(c);
            t.addCell( c );            
            
            c = new PdfPCell( new Paragraph( lmsg( "scr.title" ), this.getFontBold() ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            c.setPaddingTop(15);
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( getThreeSegmentBoldPhrase( lmsg("scr.p1.a"), lmsg("scr.p1.b"), lmsg("scr.p1.c") ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            // adding image
            c = new PdfPCell( scoreFigureImage );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment(Element.ALIGN_CENTER );
            c.setColspan(3);
            c.setPadding( 0 );
            c.setPaddingTop( 12 );
            c.setPaddingBottom( 12 );
            setRunDirection(c);
            t.addCell( c );            

            c = new PdfPCell( getTwoSegmentBoldPhrase( lmsg("scr.p2.a"), lmsg("scr.p2.b") ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( getTwoSegmentBoldPhrase( lmsg("scr.p3.a"), lmsg("scr.p3.b") ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);

            c = new PdfPCell( getTwoSegmentBoldPhrase( lmsg("scr.p4.a"), lmsg("scr.p4.b") ) );
            c.setBorder( Rectangle.NO_BORDER );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setColspan(3);
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);
            
            tableH = t.calculateHeights(); //  + 500;            
            tableY = y; //  usablePageHeight - (usablePageHeight- tableH)/4;
            tableW = t.getTotalWidth();
            tableX = (pageWidth - tableW)/2;
            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseUMinnJusticeReportTemplate.addIntroductionSection()" );
            throw new STException( e );
        }        
    }
    
    
    
    
    
    protected Phrase getTwoSegmentBoldPhrase( String a, String b )
    {
        Phrase p = new Phrase();
        p.add( new Chunk(a, fontBold));
        p.add( new Chunk(" " + b, font));
        return p;
    }

    protected Phrase getThreeSegmentBoldPhrase( String a, String b, String c )
    {
        Phrase p = new Phrase();
        p.add( new Chunk(a + " ", font));
        p.add( new Chunk(b, fontBold));
        p.add( new Chunk(" " + c, font));
        return p;
    }

    protected void addPreparationNotesSection() throws Exception
    {
        try
        {
            List<String> prepNotes = new ArrayList<>();
            
            prepNotes.add( lmsg("prepnotes.1") );
            prepNotes.add( lmsg("prepnotes.2") );
            
            prepNotes.add( lmsg("prepnotes.hrause", new String[]{Integer.toString(reportData.o.getOrgId())}));
            
            if( prepNotes.isEmpty() )
                return;

            //if( currentYLevel <= footerHgt + 200 )            
            //{    addNewPage();
            //     previousYLevel =  currentYLevel;                 
            //}
            
            java.util.List<Chunk> cl  = new ArrayList<>();
            cl.add( new Chunk( lmsg("g.PreparationNotes"), this.fontBold ) );                

            for( String s : prepNotes )
            {
                cl.add( new Chunk( s, this.font ) );                
            }
           
            float tableWid = ITextUtils.getMaxChunkWidth( cl ) + 20;
            if( tableWid > pageWidth-120 )
                tableWid = pageWidth - 120;
            

                        
            float y = currentYLevel; // addTitle( currentYLevel, lmsg( "g.PreparationNotes" ), null );

            // First create the table
            PdfPCell c;

            // First, add a table
            PdfPTable t = new PdfPTable( new float[] { 4,70 } );

            // float outerWid = pageWidth - 2*CT2_MARGIN - 2*CT2_BOX_EXTRAMARGIN;

            t.setHorizontalAlignment( Element.ALIGN_CENTER );
            t.setTotalWidth( tableWid );
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
            
            // float tableY = y - tableH; //   pageHeight/2 - (pageHeight/2 - tableH)/2;
            // float tableY = usablePageHeight - (usablePageHeight- tableH)/4;
            float tableY = usablePageHeight - 50;

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





    
    protected void addTableOfContentsSection() throws Exception
    {
        try
        {
            List<String> contentsItems = new ArrayList<>();
            
            LogService.logIt(  "BaseUMinnJusticeReportTemplate.addTableOfContentsSection() START" );

            for( int i=1;i<=7;i++ )
                contentsItems.add( i + ". " + lmsg("contents." + i) );
            
            float y = currentYLevel; // addTitle( currentYLevel, lmsg( "g.PreparationNotes" ), null );

            // First create the table
            PdfPCell c;

            java.util.List<Chunk> cl  = new ArrayList<>();
            cl.add( new Chunk( lmsg("contents.title"), this.fontBold ) );                

            for( String s : contentsItems )
            {
                cl.add( new Chunk( s, this.font ) );                
            }
           
            float tableWid = ITextUtils.getMaxChunkWidth( cl ) + 50;

            if( tableWid > pageWidth-120 )
                tableWid = pageWidth - 120;
            
            // First create the table
            // PdfPCell c;

            // First, add a table
            PdfPTable t = new PdfPTable( 2 );

            t.setTotalWidth( new float[] { tableWid, 50 } );
            t.setLockedWidth( true );
            setRunDirection(t);

            c = t.getDefaultCell();
            c.setPadding( 5 );
            // c.setPaddingRight( 15 );
            // c.setPaddingBottom( 25 );
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setBorder( Rectangle.NO_BORDER );
            c.setBorderWidth( 0 );
            setRunDirection(c);

            c = new PdfPCell( new Phrase( lmsg( "contents.title" ), fontBold ));
            c.setBorder( Rectangle.NO_BORDER );
            c.setColspan(2);
            c.setHorizontalAlignment( Element.ALIGN_LEFT );
            c.setPadding( 4 );
            setRunDirection( c );
            t.addCell(c);
            
            for( int i=1;i<=8;i++ )
            {
                c = new PdfPCell( new Phrase( i + ". " + lmsg("contents." + i), font ));
                c.setBorder( Rectangle.NO_BORDER );
                c.setColspan(1);
                c.setHorizontalAlignment( Element.ALIGN_LEFT );
                c.setPadding( 4 );
                setRunDirection( c );
                t.addCell(c);

                c = new PdfPCell( new Phrase( lmsg("contents." + i + ".p"), font ));
                c.setBorder( Rectangle.NO_BORDER );
                c.setColspan(1);
                c.setHorizontalAlignment( Element.ALIGN_RIGHT );
                c.setPadding( 4 );
                setRunDirection( c );
                t.addCell(c);
            }
            
            float tableH = t.calculateHeights(); //  + 500;

            // float tableY = y; //  + 10 - (y - pageHeight/2 - tableH)/2;
            // float tableY = usablePageHeight - (usablePageHeight- tableH)/4;

            // float tableY = y; //  + 10 - (y - pageHeight/2 - tableH)/2;
            float tableY = usablePageHeight - 50;
                        
            float tableW = t.getTotalWidth();

            float tableX = (pageWidth - tableW)/2;

            t.writeSelectedRows(0, -1,tableX, tableY, pdfWriter.getDirectContent() );    
            
        }
        catch( Exception e )
        {
            LogService.logIt( e, "BaseUMinnJusticeReportTemplate.addTableOfContentsSection()" );
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
