/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.leaderstyle;

import com.itextpdf.text.BaseColor;
import com.tm2batch.custom.disc.DiscReportUtils;
import com.tm2batch.entity.event.TestEvent;
import com.tm2batch.entity.event.TestEventScore;
import com.tm2batch.event.EventFacade;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.imageio.ImageIO;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler;

/**
 *
 * @author miker_000
 */
public class LeaderStyleReportUtils {

    public static final String[] LEADER_STYLE_NAMES = new String[]{"Authoritarian","Democratic","Laissez-Faire", "Transactional","Transformational"};
    public static final String[] LEADER_STYLE_STUBS = new String[]{"auth","demo",  "lais", "transact", "transform"};

    public static Color[] sliceColors = new Color[]
    {
        new Color(214, 44, 26),   // Auth #d62c1a
        new Color(229, 124, 30),  // Dem #e57c1e
        new Color(46, 139, 87),   // Laissez #2e8b57
        new Color(0, 119, 204),    // Transact #0077cc
        new Color(72, 0, 243),    // Transform #4800f3
        //new Color(224, 224, 224)    // Transform #0077cc
    };


    public static BaseColor[] sliceBaseColors = new BaseColor[]
    {
        new BaseColor(214, 44, 26),   // Auth #d62c1a
        new BaseColor(229, 124, 30),  // Dem #e57c1e
        new BaseColor(46, 139, 87),   // Laissez #2e8b57
        new BaseColor(0, 119, 204),    // Transact #0077cc
        new BaseColor(72, 0, 243),    // Transform #4800f3
        //new BaseColor(224, 224, 224),    // Transact #0077cc
    };



    private final String bundleName;
    private Properties customProperties;


    public LeaderStyleReportUtils( String bundleName)
    {
        this.bundleName=bundleName;
    }




    public static String getCompetencyStub( int index )
    {
        return LEADER_STYLE_STUBS[index];
    }


    public static int getIndexForStub(String stub )
    {
        for( int i=0; i<LEADER_STYLE_STUBS.length; i++ )
        {
            if( LEADER_STYLE_STUBS[i].equalsIgnoreCase(stub))
                return i;
        }
        return -1;
    }

    public static int getTopTraitIndex( Map<String,Float> scoreMap )
    {
        float[] vals = new float[5];
        for( int i=0;i<LEADER_STYLE_STUBS.length;i++ )
        {
            vals[i] = scoreMap.get( LEADER_STYLE_STUBS[i] );
        }
        return getTopTraitIndex( vals );
    }


    public static int getTopTraitIndex( float[] leaderStyleScoreVals )
    {
        int out = -1;
        float highVal=-1;
        int idx=0;

        if( leaderStyleScoreVals==null || leaderStyleScoreVals.length<5 )
            return out;

        for( int i=0; i<5; i++ )
        {
            if(leaderStyleScoreVals[i]<=0)
                continue;

            if( leaderStyleScoreVals[i]>highVal )
            {
                highVal=leaderStyleScoreVals[i];
                idx=i;
            }

            else if( leaderStyleScoreVals[i]==highVal )
            {
                int indexToUse = getTieBreakerIndex( i, idx );
                LogService.logIt( "LeaderStyleReportUtils.getTopTraitIndexes() TOP value Tie Breaker: i=" + i + ", existing idx=" + idx +", tieBreaker index=" + indexToUse );
                idx = indexToUse;
            }

        }
        out=idx;

        return out;
    }

    public static int getTieBreakerIndex( int idx1, int idx2 )
    {
        // same - should not happen
        if( idx1==idx2 )
            return idx1;

        // Authoritarian beats all
        if( idx1==0 || idx2==0 )
            return 0;

        // Transformational beats all except dominance.
        if( idx1==4 || idx2==4 )
            return 1;

        // Transactional beats all except dominance.
        if( idx1==3 || idx2==3 )
            return 1;

        return 0;

    }

    public static Map<String,Float> getScoreMap( TestEvent te ) throws Exception
    {
        Map<String,Float> out = new HashMap<>();

        float[] vals = getLeaderStyleScoreVals( te );

        for( int i=0;i<LEADER_STYLE_STUBS.length; i++ )
        {
            out.put( LEADER_STYLE_STUBS[i], vals[i]);
        }
        return out;
    }



    public static float[] getLeaderStyleScoreVals( TestEvent te )
    {
        // dauth, dem, lais, trasact, transform
        float[] out = new float[5];

        // TESTING ONLY
        if( 1==2 )
        {
            // out = new float[]{50,15,40,25};
            out = new float[]{35,50,50,5,8};
            return out;
        }

        if( te==null )
        {
            LogService.logIt( "LeaderStyleReportUtils.getLeaderStyleScoreVals() testEvent is NULL!" );
            return out;
        }

        if( te.getTestEventScoreList()==null )
        {
            try
            {
                te.setTestEventScoreList(EventFacade.getInstance().getTestEventScoresForTestEvent( te.getTestEventId(), -1));
            }
            catch( Exception e )
            {
                LogService.logIt( e, "LeaderStyleReportUtils.getDiscScoreVals() testEventId=" + te.getTestEventId() );
            }
        }
        String nameEnglish;
        for( int i=0; i<LEADER_STYLE_NAMES.length; i++ )
        {
            nameEnglish = LEADER_STYLE_NAMES[i].toLowerCase();

            for( TestEventScore tes : te.getTestEventScoreList( TestEventScoreType.COMPETENCY.getTestEventScoreTypeId()) )
            {
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

    public String getStyleName(String stub )
    {
        return getKey( stub + ".name" );
    }

    public String getKey( String key )
    {
        if( customProperties==null )
            getProperties();

        try
        {
            if( customProperties==null )
            {
                LogService.logIt( "LeaderStyleReportUtils.getKey() customProperties is null. Cannot load. Returning null. key=" + key );
                return null;
            }

            String s = customProperties.getProperty( key, "KEY NOT FOUND" );

            if( s.startsWith( "KEY NOT FOUND") )
                s += " (" + key + ")";

            return s;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "LeaderStyleReportUtils.getKey() " + key );
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
                LogService.logIt( "LeaderStyleReportUtils.loadProperties() BBB.1 Unable to load properties for Bundle=" + bundleName );

            customProperties = prop;
            // LogService.logIt( "LeaderStyleReportUtils.loadProperties() " + bundleName + ", Properties files has " + customProperties.size() + " keys. " );
        }
        catch( Exception e )
        {
            LogService.logIt( e, "LeaderStyleReportUtils.loadProperties() " );
        }
    }


    public static BufferedImage getLeaderStyleGroupPieGraphImage( Map<String,Object[]> scoreValMap, int scrDigits, int wid, int hgt ) throws Exception
    {
        try
        {
            LogService.logIt( "LeaderStyleReportUtils.getLeaderStyleGroupPieGraphImage() wid=" + wid + ", hgt=" + hgt  );

            PieChart chart = new PieChartBuilder().width(wid).height(hgt).theme(Styler.ChartTheme.GGPlot2).build();

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
            chart.getStyler().setLabelType( PieStyler.LabelType.Name);
            chart.getStyler().setLabelsVisible(false);
            chart.getStyler().setStartAngleInDegrees(0);
            chart.getStyler().setForceAllLabelsVisible(true);
            chart.getStyler().setAntiAlias( true );
            chart.getStyler().setAnnotationTextPanelPadding( 0 );
            chart.getStyler().setDefaultSeriesRenderStyle(PieSeries.PieSeriesRenderStyle.Pie);

            // chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
            // chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);

            float scrAdj;
            String name;
            LogService.logIt( "LeaderStyleReportUtils.getLeaderStyleGroupPieGraphImage() BBB.1 DiscReportUtils.LEADER_STYLE_STUBS[0]=" + LeaderStyleReportUtils.LEADER_STYLE_STUBS[0] + ", LeaderStyleReportUtils.LEADER_STYLE_STUBS[1]=" + LeaderStyleReportUtils.LEADER_STYLE_STUBS[1]  );
            String[] cols = LeaderStyleReportUtils.LEADER_STYLE_STUBS; 
            List<String> colsRev = new ArrayList<>();
            for( int i=0;i<5;i++ )
            {
                colsRev.add( cols[i]);
            }
            // List<String> colsRev = Arrays.asList(cols);
            Collections.reverse(colsRev);

            LogService.logIt( "LeaderStyleReportUtils.getLeaderStyleGroupPieGraphImage() BBB.2 DiscReportUtils.DISC_COMPETENCY_STUBS[0]=" + LeaderStyleReportUtils.LEADER_STYLE_STUBS[0] + ", LeaderStyleReportUtils.LEADER_STYLE_STUBS[1]=" + LeaderStyleReportUtils.LEADER_STYLE_STUBS[1]  );


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
            LogService.logIt( "LeaderStyleReportUtils.getLeaderStyleGroupPieGraphImage() writing NIO tempFile to " +  pathFilename.toString());

            BitmapEncoder.saveBitmapWithDPI(chart, pathFilename.toString(), BitmapEncoder.BitmapFormat.PNG, 300);

            File imageFile = new File(pathFilename.toString());

            // Read the image into a BufferedImage object
            BufferedImage bi = ImageIO.read(imageFile);

            imageFile.delete();

            return bi;
        }
        catch( Exception e )
        {
            LogService.logIt( e, "LeaderStyleReportUtils.getLeaderStyleGroupPieGraphImage() " );
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



}
