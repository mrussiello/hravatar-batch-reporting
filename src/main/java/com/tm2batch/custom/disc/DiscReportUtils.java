/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.disc;

import com.itextpdf.text.BaseColor;
import com.tm2batch.entity.event.TestEvent;
import com.tm2batch.entity.event.TestEventScore;
import com.tm2batch.event.TestEventScoreType;
import com.tm2batch.service.LogService;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.imageio.ImageIO;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.style.PieStyler.LabelType;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 *
 * @author miker_000
 */
public class DiscReportUtils {

    public static final String[] DISC_COMPETENCY_NAMES = new String[]{"Dominance", "Influence", "Steadiness", "Compliance"};
    public static final String[] DISC_COMPETENCY_STUBS = new String[]{"d", "i", "s", "c"};
    
    // 0, 1, 2, 3, 
    public static final String[] TWO_LEVEL_DISC_COMPETENCY_STUBS = new String[]{"d", "di", "ds","dc", "id", "i", "is", "ic", "sd", "si", "s", "sc", "cd", "ci", "cs", "c"};

    
    public static Color[] sliceColors = new Color[] 
    {
        new Color(214, 44, 26),   // D #d62c1a
        new Color(229, 124, 30),  // I #e57c1e
        new Color(46, 139, 87),   // S #2e8b57
        new Color(0, 119, 204)    // C #0077cc
    };


    public static BaseColor[] sliceBaseColors = new BaseColor[] 
    {
        new BaseColor(214, 44, 26),   // D #d62c1a
        new BaseColor(229, 124, 30),  // I #e57c1e
        new BaseColor(46, 139, 87),   // S #2e8b57
        new BaseColor(0, 119, 204)    // C #0077cc
    };
    
    private final String bundleName;
    private Properties customProperties;


    public DiscReportUtils( String bundleName)
    {
        this.bundleName=bundleName;
    }



    public static BufferedImage getDiscPieGraphImage( Map<String,Object[]> scoreValMap, int scrDigits, int wid, int hgt ) throws Exception
    {
        try
        {
            LogService.logIt( "DiscReportUtils.getDiscPieGraphImage2() wid=" + wid + ", hgt=" + hgt  );
            
            PieChart chart = new PieChartBuilder().width(wid).height(hgt).theme(ChartTheme.GGPlot2).build();

            Color[] colors = reverseColorArray(sliceColors);
            
            // Customize Chart
            // chart.getStyler().setSeriesColors(colors);
            chart.getStyler().setChartPadding(0);
            chart.getStyler().setCircular(true);
            chart.getStyler().setLegendVisible(false);
            Font font = new Font("Arial", Font.PLAIN, 10);
            chart.getStyler().setLegendFont(font);            
            Font font2 = new Font("Arial", Font.BOLD, 10);
            chart.getStyler().setLabelsFont(font2);
            chart.getStyler().setLabelsFontColor( Color.BLACK );
            //chart.getStyler().setLegendLayout(LegendLayout.Horizontal );
            //chart.getStyler().setLegendPosition( LegendPosition.OutsideS);
            // chart.getStyler().setLegendPadding(30);
            chart.getStyler().setLabelsDistance(0.5);  // 0.98f
            chart.getStyler().setChartBackgroundColor(Color.white);
            chart.getStyler().setPlotBackgroundColor( Color.white );
            // chart.getStyler().setPlotBorderVisible(true);
            // chart.getStyler().setPlotBorderColor(Color.red);
            // chart.getStyler()
            chart.getStyler().setLabelType( LabelType.Name);
            chart.getStyler().setLabelsVisible(true);
            chart.getStyler().setStartAngleInDegrees(0);
            chart.getStyler().setForceAllLabelsVisible(true);
            chart.getStyler().setAntiAlias( true );
            chart.getStyler().setAnnotationTextPanelPadding( 0 );            
            chart.getStyler().setDefaultSeriesRenderStyle(PieSeriesRenderStyle.Pie);
            
            // chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
            // chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
    
            float scrAdj;
            String name;
            LogService.logIt( "DiscReportUtils.getDiscPieGraphImage() BBB.1 DiscReportUtils.DISC_COMPETENCY_STUBS[0]=" + DiscReportUtils.DISC_COMPETENCY_STUBS[0] + ", DiscReportUtils.DISC_COMPETENCY_STUBS[1]=" + DiscReportUtils.DISC_COMPETENCY_STUBS[1]  );
            String[] cols = DiscReportUtils.DISC_COMPETENCY_STUBS; //  new String[]{"d", "i", "s", "c" };            
            List<String> colsRev = new ArrayList<>();
            for( int i=0;i<4;i++ )
            {
                colsRev.add( cols[i]);
            }
            // List<String> colsRev = Arrays.asList(cols);
            Collections.reverse(colsRev);  
            
            LogService.logIt( "DiscReportUtils.getDiscPieGraphImage() BBB.2 DiscReportUtils.DISC_COMPETENCY_STUBS[0]=" + DiscReportUtils.DISC_COMPETENCY_STUBS[0] + ", DiscReportUtils.DISC_COMPETENCY_STUBS[1]=" + DiscReportUtils.DISC_COMPETENCY_STUBS[1]  );

            
            int colorIndex = -1;
            List<Color> colorList = new ArrayList<>();
            
            for( String s : colsRev )
            {
                name = s.toUpperCase(); // = (String) scoreValMap.get(s)[0];
                scrAdj = (Float) scoreValMap.get(s)[1];
                
                colorIndex++;
                
                // skip non-scores
                if( scrAdj<=0.1f )
                    continue;
                
                colorList.add( colors[colorIndex]);
                BigDecimal bd = new BigDecimal(Float.toString(scrAdj));
                bd = bd.setScale(scrDigits, RoundingMode.DOWN); // Truncate to 2 decimal places
                float trimmed = bd.floatValue(); // trimmed = 3.14
                // dataset.setValue(name + "(" + trimmed + ")", trimmed);
                // chart.addSeries(name + " (" + trimmed + ")", trimmed);
                chart.addSeries(name, trimmed);
            }
            
            colors = colorList.toArray(Color[]::new);
            chart.getStyler().setSeriesColors(colors);

            
            Path tempFile = Files.createTempFile("discpie-", ".png" );
            Path pathFilename = tempFile.toAbsolutePath();
            LogService.logIt( "DiscReportUtils.getDiscPieGraphImage2() writing NIO tempFile to " +  pathFilename.toString());
            
            BitmapEncoder.saveBitmapWithDPI(chart, pathFilename.toString(), BitmapFormat.PNG, 300);
            
            File imageFile = new File(pathFilename.toString()); 
            
            // Read the image into a BufferedImage object
            BufferedImage bi = ImageIO.read(imageFile);    
            
            imageFile.delete();
            
            return bi;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "DiscReportUtils.getDiscPieGraphImage() " );
            throw e;
        }
    }
    
    public static Color[] reverseColorArray(Color[] arr )
    {
        Color[] out = new Color[arr.length];
        for( int i=0;i<out.length;i++ )
            out[i] = arr[arr.length-1-i];
        return out;
    }
    
    public static BaseColor[] reverseBaseColorArray(BaseColor[] arr )
    {
        BaseColor[] out = new BaseColor[arr.length];
        for( int i=0;i<out.length;i++ )
            out[i] = arr[arr.length-1-i];
        return out;
    }
    

    

    public static BufferedImage getDiscDonutGraphImage( Map<String,Object[]> scoreValMap, int scrDigits, int wid, int hgt ) throws Exception
    {
        try
        {
            LogService.logIt( "DiscReportUtils.getDiscDonutGraphImage() wid=" + wid + ", hgt=" + hgt  );
            
            PieChart chart = new PieChartBuilder().width(wid).height(hgt).theme(ChartTheme.GGPlot2).build();

            Color[] colors = new Color[16];
            for( int i=0;i<4;i++ )
            {
                for( int j=0;j<4;j++ )
                {
                    colors[i*4 + j]=sliceColors[i];                    
                }
            }
            
            colors = reverseColorArray(colors);
            
            // Customize Chart
            chart.getStyler().setChartPadding(0);
            chart.getStyler().setCircular(true);
            chart.getStyler().setLegendVisible(false);
            Font font = new Font("Arial", Font.PLAIN, 10);
            chart.getStyler().setLegendFont(font);            
            Font font2 = new Font("Arial", Font.BOLD, 10);
            chart.getStyler().setLabelsFont(font2);
            chart.getStyler().setLabelsFontColor( Color.MAGENTA );
            // chart.getStyler().setLabelsDistance(30);
            //chart.getStyler().setLegendLayout(LegendLayout.Horizontal );
            //chart.getStyler().setLegendPosition( LegendPosition.OutsideS);
            // chart.getStyler().setLegendPadding(30);
            // chart.getStyler().setLabelsDistance(0.5);  // 0.98f
            chart.getStyler().setChartBackgroundColor(Color.white);
            chart.getStyler().setPlotBackgroundColor( Color.white );
            // chart.getStyler().setPlotBorderVisible(true);
            // chart.getStyler().setPlotBorderColor(Color.red);
            // chart.getStyler()
            chart.getStyler().setLabelType( LabelType.Name);
            chart.getStyler().setLabelsVisible(false);
            chart.getStyler().setStartAngleInDegrees(0);
            chart.getStyler().setForceAllLabelsVisible(false);
            chart.getStyler().setAntiAlias( true );
            //chart.getStyler().setAnnotationTextPanelPadding( 10 );  
            //chart.getStyler().setAnnotationTextFontColor(Color.orange);
            //chart.getStyler().setAnnotationTextFont( font2 );
            chart.getStyler().setDefaultSeriesRenderStyle(PieSeriesRenderStyle.Donut);
            
            // chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
            // chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
    
            float scrAdj;
            String name;
            String[] cols = DiscReportUtils.TWO_LEVEL_DISC_COMPETENCY_STUBS; // new String[]{"d", "i", "s", "c" };
            // List<String> colsRev = Arrays.asList(cols);
            List<String> colsRev = new ArrayList<>();
            int colorIndex = -1;
            List<Color> colorList = new ArrayList<>();
            
            for( int i=0;i<16;i++ )
            {
                colsRev.add( cols[i] );
            }
            Collections.reverse(colsRev);  
            
            
            for( String s : colsRev )
            {
                name = s.toUpperCase(); // = (String) scoreValMap.get(s)[0];
                scrAdj = (Float) scoreValMap.get(s)[1];
                
                colorIndex++;
                
                // skip non-scores
                if( scrAdj<=0.1f )
                    continue;
                
                colorList.add( colors[colorIndex]);
                
                BigDecimal bd = new BigDecimal(Float.toString(scrAdj));
                bd = bd.setScale(scrDigits, RoundingMode.DOWN); // Truncate to 2 decimal places
                float trimmed = bd.floatValue(); // trimmed = 3.14
                // dataset.setValue(name + "(" + trimmed + ")", trimmed);
                // chart.addSeries(name + " (" + trimmed + ")", trimmed);
                chart.addSeries(name, trimmed);
            }
            
            colors = colorList.toArray(Color[]::new);
            chart.getStyler().setSeriesColors(colors );
                        
            Path tempFile = Files.createTempFile("discpie-", ".png" );
            Path pathFilename = tempFile.toAbsolutePath();
            LogService.logIt( "DiscReportUtils.getDiscPieGraphImage2() writing NIO tempFile to " +  pathFilename.toString());
            
            BitmapEncoder.saveBitmapWithDPI(chart, pathFilename.toString(), BitmapFormat.PNG, 300);
            
            File imageFile = new File(pathFilename.toString()); 
            
            // Read the image into a BufferedImage object
            BufferedImage bi = ImageIO.read(imageFile);    
            
            imageFile.delete();
            
            return bi;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "DiscReportUtils.getDiscPieGraphImage2() " );
            throw e;
        }
    }
    
    

    public static String getTwoLevelCompetencyName(int index)
    {
        String s = getTwoLevelCompetencyStubLetter( index ).toUpperCase();
        if( s.length()<=1)
            return s;
        return s.charAt(0) + "/" + s.charAt(1);
    }


    public static String getCompetencyStub( int[] topTraitIndexes )
    {
        if( topTraitIndexes[1]<0 )
            return getCompetencyStubLetter( topTraitIndexes[0] );
        
        return getCompetencyStubLetter( topTraitIndexes[0] ) + getCompetencyStubLetter( topTraitIndexes[1] );
    }


    public static String getTwoLevelCompetencyStubLetter( int index )
    {
        return TWO_LEVEL_DISC_COMPETENCY_STUBS[index];
    }

    
    public static String getCompetencyStubLetter( int index )
    {
        return DISC_COMPETENCY_STUBS[index];
    }

    public static int[] getTopTraitIndexes( Map<String,Float> scoreMap )
    {
        float[] vals = new float[4];
        for( int i=0;i<4;i++ )
        {
            vals[i] = scoreMap.get( DISC_COMPETENCY_STUBS[i] );
        }
        return getTopTraitIndexes( vals );
    }
    
    
    /**
     * Returns
     *    data[0] = high value INDEX (0,1,2, or 3).      *
     *    data[1] = second highest value INDEX IF:
     *                the 2nd highest value is>=70 or
     *                the highest value is>=40 AND 2nd highest value is >=highest value minus 25
     *                otherwise -1.
     *
     * @param discScoreVals
     * @return
     */
    public static int[] getTopTraitIndexes( float[] discScoreVals )
    {
        int[] out = new int[] {-1,-1};

        float highVal=-1;
        int idx=-1;

        if( discScoreVals==null || discScoreVals.length<4 )
            return out;

        for( int i=0; i<4; i++ )
        {
            if(discScoreVals[i]<=0 )
                continue;
            
            if( discScoreVals[i]>highVal )
            {
                highVal=discScoreVals[i];
                idx=i;
            }
            
            else if( discScoreVals[i]==highVal )
            {
                int indexToUse = getTieBreakerIndex( i, idx );
                LogService.logIt( "DiscReeportUtils.getTopTraitIndexes() TOP value Tie Breaker: i=" + i + ", existing idx=" + idx +", tieBreaker index=" + indexToUse );
                idx = indexToUse;
            }
            
        }
        out[0]=idx;

        // get second highest value.
        int idx2=-1;
        float highVal2=-1;
        for( int i=0; i<4; i++ )
        {
            // already used
            if( i==idx )
                continue;

            if( discScoreVals[i]>highVal2 )
            {
                highVal2=discScoreVals[i];
                idx2=i;
            }

            else if( discScoreVals[i]==highVal2 )
            {
                int indexToUse = getTieBreakerIndex( i, idx2 );
                LogService.logIt( "DiscReportUtils.getTopTraitIndexes() Secondary value Tie Breaker: i=" + i + ", existing idx=" + idx2 +", tieBreaker index=" + indexToUse );
                idx2 = indexToUse;
            }
        }

        
        if( highVal>0 && ((highVal-highVal2)/highVal)<0.2f )
            out[1]=idx2;
        else
            out[1]=-1;

            /*
        if( highVal2>=75f )
            out[1]=idx2;
        else if( highVal>=40 && highVal2>=highVal-25 )
            out[1]=idx2;
        else
            out[1]=-1;
        */

        return out;
    }
    
    public static int getTieBreakerIndex( int idx1, int idx2 )
    {
        // same - should not happen
        if( idx1==idx2 )
            return idx1;
        
        // Dominance beats all
        if( idx1==0 || idx2==0 )
            return 0;

        // influence beats all except dominance.
        if( idx1==1 || idx2==1 )
            return 1;
        
        // at this point must have 1 steadiness and 1 compliance.
        
        // compliance always beats steadiness
        return 3;
        
    }
    
    public static Map<String,Float> getScoreMap( TestEvent te ) throws Exception
    {
        Map<String,Float> out = new HashMap<>();
        
        float[] vals = getDiscScoreVals( te );
        
        for( int i=0;i<DISC_COMPETENCY_STUBS.length; i++ )
        {
            out.put( DISC_COMPETENCY_STUBS[i], vals[i]);
        }
        return out;
    }

    public static float[] getDiscScoreVals( TestEvent te ) throws Exception
    {
        float[] out = new float[4];

        // TESTING ONLY
        if( 1==2 )
        {
            // out = new float[]{50,15,40,25};
            out = new float[]{35,50,50,5};
            return out;
        }

        if( te==null )
        {
            LogService.logIt( "DiscReportUtils.getDiscScoreVals() testEvent is NULL!" );
            return out;
        }

        if( te.getTestEventScoreList()==null )
        {
            throw new Exception( "DiscReportUtils.getDiscScoreVals is null. testEventId=" + te.getTestEventId() );
        }
        
        String nameEnglish;
        for( int i=0; i<4; i++ )
        {
            nameEnglish = DISC_COMPETENCY_NAMES[i].toLowerCase();

            for( TestEventScore tes : te.getTestEventScoreList( TestEventScoreType.COMPETENCY.getTestEventScoreTypeId()) )
            {
                // LogService.logIt( "DiscReportUtils.getDiscScoreVals() testing " + tes.getName() + ", " + tes.getNameEnglish() + ", against " + nameEnglish );
                if( (tes.getName()!=null && tes.getName().toLowerCase().equals(nameEnglish )) ||
                    (tes.getNameEnglish()!=null && tes.getNameEnglish().toLowerCase().equals(nameEnglish )) )
                {
                    out[i] = tes.getScore();
                    break;
                }
            }
        }

        return out;
    }

    public String getKey( String key )
    {
        if( customProperties==null )
            getProperties();

        try
        {
            if( customProperties==null )
            {
                LogService.logIt( "DiscReportUtils.getKey() customProperties is null. Cannot load. Returning null. key=" + key );
                return null;
            }

            String s = customProperties.getProperty( key, "KEY NOT FOUND" );

            if( s.startsWith( "KEY NOT FOUND") )
                s += " (" + key + ")";

            return s;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "DiscReportUtils.getKey() " + key );
            return null;
        }
    }

    public synchronized Properties getProperties()
    {
        if( customProperties== null )
            loadProperties();
        return customProperties;
    }

    private synchronized void loadProperties()
    {
        try
        {
            Properties prop = new Properties();
            InputStream in = getClass().getResourceAsStream( bundleName );

            if( in!=null )
            {
                prop.load(in);
                in.close();
            }
            else
                LogService.logIt( "DiscReportUtils.loadProperties() BBB.1 Unable to load properties for Bundle=" + bundleName );

            customProperties = prop;
            LogService.logIt( "DiscReportUtils.loadProperties() " + bundleName + ", Properties files has " + customProperties.size() + " keys. " );
        }
        catch( Exception e )
        {
            LogService.logIt( e, "DiscReportUtils.loadProperties() " );
        }
    }

}
