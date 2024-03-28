package com.tm2batch.util;


import com.tm2batch.global.STException;
import com.tm2batch.service.LogService;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;



/**
 * This class provides a variety of useful number-oriented utilities
 */
public class NumberUtils
{

    /**
     * Random number generator used by various
     */
    private static Random random = null;


    public static int intFmString( String inStr ) throws STException
    {
    	return intFmString( inStr, true );
    }

    public static int intFmString( String inStr, boolean error ) throws STException
    {
    	try
    	{
    		double d = Double.parseDouble( inStr );

    		return (int) Math.floor( d );
    	}

    	catch( Exception e )
    	{
    		LogService.logIt(e, "NumberUtils.intFmString() " + inStr );

    		if( error )
    			throw new STException( e );
    	}

    	return 0;
    }


    public static boolean isOdd( int num )
    {
    	return num % 2 != 0;
    }

    public static String getPctSuffixStr( Locale locale, float pp, int precision )
    {
        if( 1==1 )
        {
            return I18nUtils.getFormattedNumber( locale , pp, precision ) + getPctSuffix( locale, pp, precision );
        }
        
        float v = (float) roundIt( pp , precision ); //  Float.parseFloat( I18nUtils.getFormattedNumber( locale , pp, precision ) );

        return precision == 0 ? ((int)v) + getPctSuffix( locale, v, precision ) : Float.toString(v);
    }



    public static String getPctSuffix( Locale locale, float pp, int precision )
    {
        if( precision>0 )
            return "";



        String nm = I18nUtils.getFormattedNumber( locale , pp, 0 );

        if( nm.endsWith( "11" ) || nm.endsWith( "12" ) || nm.endsWith( "13" ) )
            return "th";

        int p = (int) Integer.parseInt( nm );

        // zeroeth
        if( p < 1 )
            return "th";

        int m = p%10;

        // LogService.logIt( "BaseCoreTestReportTemplate.getPctSuffix() p=" + p + ", int value=" + ((int)p) + ", Modulus=" + ((int)p)%10 );

        if( m==0 || m>3 )
            return "th";

        else if( m==1 )
            return "st";

        else if( m==2 )
            return "nd";

        else
            return "rd";

    }

    
    
    /**
     * Returns a number rounded to the requested number of decimal places
     */
    public static double roundIt( double theNumber , int decimalPlaces )
    {
        int bump = 1;

        for( int i=0 ; i<decimalPlaces ; i++ )
            bump *= 10;

        theNumber *= bump;

        theNumber = Math.floor( theNumber );

        return theNumber/bump;

    }

    /**
     * Always returns a string matching this pattern: ###,##0.00 where
     * the # signs are optional (will not show up if 0, and the 0 symbols are numbers that always
     * show up.
     *
     * This is set to avoid any effects from rounding inside the decimal format class.
     *
     */
    public static String getTwoDecimalFormattedAmount( double theNumber )
    {
        DecimalFormat decimalFormat = new DecimalFormat( "###,##0.000" );

        String temp = decimalFormat.format( theNumber );

        return temp.substring( 0 , temp.length() - 1 );
    }

    public static String getOneDecimalFormattedAmount( double theNumber )
    {
        DecimalFormat decimalFormat = new DecimalFormat( "###,##0.00" );

        String temp = decimalFormat.format( theNumber );

        return temp.substring( 0 , temp.length() - 1 );
    }



    /**
     * Returns a number rounded to a computed number of decimal places
     */
    public static double roundItNatural( double theNumber )
    {
        int decimalPlaces = 0;

        if( theNumber < 10.0 )
            decimalPlaces = 1;

        if( theNumber < 1.0 )
            decimalPlaces = 2;

        int bump = 1;

        for( int i=0 ; i<decimalPlaces ; i++ )
            bump *= 10;

        theNumber *= bump;

        theNumber = Math.floor( theNumber );

        return theNumber/bump;

    }



    /**
     * returns the desired number of decimal places for a specific number
     */
    public static int getNaturalDecimalPlaces( double theNumber )
    {
        if( theNumber < 0.01 )
            return 4;

        if( theNumber < 0.1 )
            return 3;

        if( theNumber < 1.0 )
            return 2;

        if( theNumber < 10.0 )
            return 1;

        return 0;
    }


    
    public static float calculateStandardDeviation( List<Float> dataList )
    {
        return (float) Math.sqrt( calculateVariance( dataList ) );
    }

    public static float calculateStandardDeviation( List<Float> dataList, float mean )
    {
        return (float) Math.sqrt( calculateVariance( dataList, mean ) );
    }


    public static float calculateVariance( List<Float> dataList )
    {
        if( dataList==null || dataList.isEmpty() )
            return 0;

        float mean = calculateMean( dataList );
        
        return calculateVariance( dataList, mean );
    }

    public static float calculateVariance( List<Float> dataList, float mean )
    {
        if( dataList==null || dataList.isEmpty() )
            return 0;

        // float mean = calculateMean( dataList );

        float sum = 0;

        for( Float f : dataList )
        {
            sum+= Math.pow(f-mean,2);
        }

        // LogService.logIt( "NumUtils.calculateVariance() sum=" + sum + ", n=" + dataList.size() );

        return sum/((float) dataList.size() );
    }
    
    
    public static float calculateMean( List<Float> dataList )
    {
        if( dataList==null || dataList.isEmpty() )
            return 0;

        // Calculate Mean
        float total = 0;

        for( Float f : dataList )
        {
            total += f;
        }

        // LogService.logIt( "NumUtils.calcluateMean() total=" + total + ", n=" + dataList.size() );
        return total/((float) dataList.size() );
    }

    public static float calculateMedian( List<Float> dataList )
    {
        if( dataList==null || dataList.isEmpty() )
            return 0;

        List<Float> tl = new ArrayList<>();
        tl.addAll( dataList );

        Collections.sort( tl );

        // One value
        if( tl.size()==1 )
            return tl.get( 0 );

        // Odd number of values
        else if( tl.size() %2 == 1 )
            return tl.get( ((int)tl.size()/2) );

        // Even number of values
        else
        {
            float v1 = tl.get( ((int)tl.size()/2)-1 );
            float v2 = tl.get( ((int)tl.size()/2) );

            return v1 + (v2-v1)/2;
        }
    }


    
    
    /**
     * 
     * correlation = AVG[  (X-mx)*(Y-my) ]/ (sd1*sd2)
     * 
     * 
     * @param dataList
     * @param dataList2
     * @return 
     */
    public static float calculateCorrelation( List<Float[]> dataList )
    {
        if( dataList==null || dataList.isEmpty()  )
            return 0;
        
        List<Float> dl1 = new ArrayList<>();
        List<Float> dl2 = new ArrayList<>();

        for( Float[] dd : dataList )
        {
            dl1.add( dd[0] );
            dl2.add( dd[1] );
        }
        
        float m1 = calculateMean( dl1 );
        float m2 = calculateMean( dl2 );
        
        float sd1 = calculateStandardDeviation( dl1, m1 );
        float sd2 = calculateStandardDeviation( dl2, m2 );

        if( sd1==0 || sd2==0 )
            return 0;
        
        // List<Float> fl = new ArrayList<>();
        
        double tot = 0;
        
        for( int i=0; i<dl1.size(); i++ )
        {
            tot += (dl1.get(i)-m1)*(dl2.get(i)-m2);
        }
        
        return (float) (tot/((float)(dl1.size()))) /(sd1*sd2);
    }
    

}