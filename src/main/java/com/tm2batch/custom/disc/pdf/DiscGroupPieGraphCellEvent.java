/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tm2batch.custom.disc.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.tm2batch.custom.disc.DiscReportUtils;
import com.tm2batch.service.LogService;
import com.tm2batch.util.I18nUtils;
import com.tm2batch.util.ImageUtils;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Mike
 */
public class DiscGroupPieGraphCellEvent implements PdfPCellEvent {

    // public static Image summaryCatNumericMarker=null;

    /*
      index 0=D
            1=I
            2=S
            3=C
    */
    // float[] discScoreVals;
    DiscColors ct2Colors;
    int scrDigits = 0;
    Map<String,Object[]> scoreValMap;
    Map<String,Object[]> twoLevelScoreValMap;
    Locale locale;
    BaseFont labelBaseFont;



    public DiscGroupPieGraphCellEvent( Map<String,Object[]> scoreValMap, Map<String,Object[]> twoLevelScoreValMap, int scrDigits, DiscColors ct2Colors, Locale locale, BaseFont labelBaseFont )
    {
        this.scoreValMap=scoreValMap;
        this.twoLevelScoreValMap=twoLevelScoreValMap;
        this.scrDigits=scrDigits;
        this.ct2Colors=ct2Colors;
        this.locale=locale;
        this.labelBaseFont=labelBaseFont;
    }

    @Override
    public void cellLayout( PdfPCell ppc, Rectangle rctngl, PdfContentByte[] pcbs )
    {
        try
        {

            // Get the score
            // float hScale = 72f/300f;
            float wid = 160f; // rctngl.getWidth(); // -20; // -4;
            float hgt = 160f; // rctngl.getHeight(); //  - 20; // -4;

            Rectangle imgRect = new Rectangle( rctngl.getLeft(),rctngl.getBottom(),rctngl.getRight(),rctngl.getTop());

            LogService.logIt( "DiscPieGraphCellEvent.cellLayout() wid=" + wid + ", hgt=" + hgt );

            BufferedImage bufImg = DiscReportUtils.getDiscPieGraphImage(scoreValMap, scrDigits, (int) wid, (int)hgt);

            bufImg = ImageUtils.makeColorTransparent(bufImg, Color.white, 10 );
            
            LogService.logIt( "DiscPieGraphCellEvent.cellLayout() PIE image direct width=" + bufImg.getWidth() + ", hgt=" + bufImg.getHeight() );

            Image img = Image.getInstance(bufImg, null );
            img.setAbsolutePosition( rctngl.getLeft() + 30, rctngl.getBottom()+60 );
            img.scaleToFit(new Rectangle(0,0,160,160));
            // img.scaleToFit(imgRect);

            LogService.logIt( "DiscPieGraphCellEvent.cellLayout() PIE image scaled width=" + img.getWidth() + ", hgt=" + img.getHeight() + ", rect " + imgRect.toString() );

            PdfContentByte pcb = pcbs[ PdfPTable.TEXTCANVAS ];
            pcb.saveState();
            pcb.addImage(img);
            pcb.restoreState();
            
            wid = 220f; // rctngl.getWidth(); // -20; // -4;
            hgt = 220f; // rctngl.getHeight(); //  - 20; // -4;
            
            BufferedImage bufImgD = DiscReportUtils.getDiscDonutGraphImage(twoLevelScoreValMap, 1, (int) wid, (int)hgt);

            bufImgD = ImageUtils.makeColorTransparent(bufImgD, Color.white, 10 );
            
            LogService.logIt( "DiscPieGraphCellEvent.cellLayout() DONUT image direct width=" + bufImgD.getWidth() + ", hgt=" + bufImgD.getHeight() );

            Image imgD = Image.getInstance(bufImgD, null );
            imgD.setAbsolutePosition( rctngl.getLeft(), rctngl.getBottom() + 30 );
            // imgD.scaleToFit(imgRect);
            imgD.scaleToFit(new Rectangle(0,0,220,220));

            LogService.logIt( "DiscPieGraphCellEvent.cellLayout() DONUT scaled.width=" + imgD.getWidth() + ", hgt=" + imgD.getHeight() );

            pcb = pcbs[ PdfPTable.LINECANVAS ];
            pcb.saveState();
            pcb.addImage(imgD);
            pcb.restoreState();
            
            float total = 0;
            
            String stub;
            String labelName;
            Object[] vals;
            float labelValFloat;
            int labelCount;
            String labelStr;
            float theta0 = 0;
            float midVal;
            float x;
            float y;
            float radius = 110 + 5;
            int align;
            
            float xOffset = imgRect.getLeft() + 110;
            float yOffset = imgRect.getBottom()+ 110 + 30;

            
            pcb = pcbs[ PdfPTable.TEXTCANVAS ];
            pcb.saveState();
            
            
            // BaseColor color = font.getColor();
            // float textHeight = labelBaseFont.getDescentPoint("D/S\n55%", StandardReportSettings.LFONTSZ ) - bfont.getAscentPoint(scoreStr, font.getSize() );
            
            pcb.setColorFill( BaseColor.BLACK );
            pcb.beginText();
            pcb.setTextRenderingMode( PdfContentByte.TEXT_RENDER_MODE_FILL );
            pcb.setFontAndSize(labelBaseFont, 10 );
                        
            // Next we calculate and present all of the donut values
            for( int i=0;i<DiscReportUtils.TWO_LEVEL_DISC_COMPETENCY_STUBS.length;i++ )
            {
                // calculate the angle.
                stub = DiscReportUtils.TWO_LEVEL_DISC_COMPETENCY_STUBS[i];
                labelName = DiscReportUtils.getTwoLevelCompetencyName(i);
                vals = twoLevelScoreValMap.get(stub);
                labelValFloat = (Float) vals[1];
                
                if( labelValFloat<=0.1f )
                    continue;
                
                labelCount = (Integer) vals[2];
                labelStr = labelName + " " + I18nUtils.getFormattedNumber(locale, labelValFloat, 1) + "%"; // " (" + labelCount + ")";
                if( labelValFloat<5 )
                    labelStr = labelName;
                
                midVal = total + labelValFloat/2f;
                float theta = 360f*midVal/100f;

                x = radius*((float) Math.sin(Math.toRadians(theta) ));
                y = radius*((float) Math.cos(Math.toRadians(theta) ));

                // LogService.logIt( "DiscPieGraphCellEvent.cellLayout() FFF.1 stub=" + stub + ", val=" + labelValFloat + ", labelStr=" + labelStr + ", total=" + total + ", midVal=" + midVal + ", theta=" + theta + ", x=" + x + ", xOffset=" + xOffset + ", y=" + y + ", yOffset=" + yOffset );
                
                align=Element.ALIGN_CENTER;
                if( theta>5 && theta<175)
                    align=Element.ALIGN_LEFT;
                else if( theta>185 && theta<355)
                    align=Element.ALIGN_RIGHT;
                
                
                pcb.showTextAligned(align, labelStr, x + xOffset, y + yOffset, 0);     
                
                total += labelValFloat;
            }
            
            pcb.endText();
            pcb.restoreState();            
            
        }
        catch( Exception e )
        {
            LogService.logIt( e, "DiscPieGraphCellEvent.cellLayout()  " );
        }
    }

}
