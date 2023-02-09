package com.tm2batch.util;

import com.tm2batch.global.STException;
import com.tm2batch.service.LogService;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;





/**
 * This class is a set of static utility methods for working with String objects
 */
public class StringUtils
{
    public static String padIntegerToLength( int theInt, int theLength )
    {
    	String s = Integer.toString(theInt);
        StringBuilder sb = new StringBuilder();
        
        while( sb.length()<theLength-s.length() )
            sb.append("0");
        sb.append( s );
        return sb.toString();
    }
        
    
    public static String alphaCharsOnly( String inStr )
    {
        if( inStr == null || inStr.length() == 0 )
            return inStr;

        String outStr = "";

        for( int i=0 ; i<inStr.length() ; i++ )
        {
            if( Character.isLetter( inStr.charAt( i ) ) )
                outStr += inStr.charAt( i );
        }

        return outStr;

    }
    
    public static boolean isValidNameMatch( String n1, String ne1, String n2, String ne2 )
    {
        if( n1==null )
            n1="";
        else
            n1=n1.trim();
        if( n2==null)
            n2="";
        else
            n2=n2.trim();

        if( ne1==null )
            ne1="";
        else
            ne1=ne1.trim();

        if( ne2==null )
            ne2="";
        else
            ne2=ne2.trim();

        if( !n1.isEmpty()  )
        {
            if( n1.equalsIgnoreCase(n2) || n1.equalsIgnoreCase(ne2) )
                return true;
        }

        if( !ne1.isEmpty()  )
        {
            if( ne1.equalsIgnoreCase(n2) || ne1.equalsIgnoreCase(ne2) )
                return true;

        }

        return false;
    }
    

    /**
     * Calculates number of words where empty lines are treated as zero words. A word is a contiguous series of alphanumeric characters.
     *
     * @param s
     * @return
     */
    public static int numWords( String s )
    {
        if( s == null )
            return 0;

        Scanner scnr = new Scanner( s );

         String words[];

         int c =0;

         while( scnr.hasNextLine() )
         {
             words= scnr.nextLine().trim().split(" ");

             for( int i=0;i<words.length;i++ )
             {
                 if( !words[i].trim().isEmpty() )
                      c++;
             }
         }

        return c;
    }
    
    
    public static boolean containsKey( String key , String text )
    {
        if( text==null || text.isEmpty() || key==null || key.isEmpty() )
            return false;
        
        text = UrlEncodingUtils.decodeKeepPlus( text );
        
        return text.contains(key);
    }
    
    
    
    public static List<NVPair> parseNVPairsList( String inStr, String delim )
    {
        List<NVPair> out = new ArrayList<>();

        if( inStr==null || inStr.isEmpty() )
            return out;

        StringTokenizer st = new StringTokenizer( inStr, delim );

        String rule;
        String value;

        // LogService.logIt( "StringUtils.parseNVPairsList()  " + inStr );
        
        while( st.hasMoreTokens() )
        {
            rule = st.nextToken();

            if( !st.hasMoreTokens() )
                break;

            value = st.nextToken();
            
            // LogService.logIt( "StringUtils.parseNVPairsList() rule=" + rule + ", value=" + value );

            if( rule != null && !rule.isEmpty() && value!=null && !value.isEmpty() )
                out.add( new NVPair( rule,value ) );
        }

        
        return out;
    }
    
    public static String doUrlDecode( String inStr, String encoding )
    {
        try
        {
            String o = URLDecoder.decode(inStr, "UTF8" );
            return o;
        } 
        
        catch( IllegalArgumentException e )
        {
            // Must have not been url encoded in teh first place. Just return original.
            return inStr;
        }
        catch( Exception e )
        {
            // unknown problem worth looking into.
            LogService.logIt(e, "StringUtils.doUrlDecode() " + inStr);
            return inStr;
        }
    }
    
    
    
    public static String replaceStandardEntities( String inStr )
    {
        if( inStr == null )
            return "";

        // String s = inStr;

        inStr = inStr.replaceAll( "&" , "&amp;" );

        inStr = inStr.replaceAll( "  " , " &#160;" );

        inStr = inStr.replaceAll( "<" , "&lt;" );

        inStr = inStr.replaceAll( ">" , "&gt;" );

        inStr = inStr.replaceAll( "\"" , "&quot;" );

        inStr = inStr.replaceAll( "`" , "'" );

        inStr = StringUtils.replaceStr(inStr, "\\\'", "'");
        // inStr = inStr.replaceAll( "\'" , "'" );


        // put at end!
        if( inStr.indexOf( "\n" ) >= 0 )
            inStr = replaceStr( inStr , "\n" , "<br />" );

        else
            inStr = replaceStr( inStr , "\r" , "<br />" );

        //if( inStr.indexOf("'") >=0 )
        //    LogService.logIt( "StringUtils.replaceStdEntities() " + s + " ->> " + inStr );

        return inStr;
    }

    
    public static String sanitizeForSqlQuery( String inStr )
    {
        if( inStr == null || inStr.length() == 0 )
            return inStr;

        return escapeChar( inStr, '\'' , '\\' ); //replaceUnescapedChar( inStr , '\'' ,"\\\'" ); // inStr.replaceAll( "\'" , "\\\'" );
    }
    


    public static String truncateStringWithTrailer( String inStr , int maxLength, boolean lastWhitespace )
    {
        if( inStr == null )
            return "";

        if( inStr.length() <= maxLength )
            return inStr;

        if( maxLength < 4 )
            return inStr.substring( 0 , maxLength -1 );

        if( lastWhitespace )
            return truncateString( inStr , maxLength ) + "...";

        else
            return inStr.substring( 0 , maxLength -1 ) + "...";
    }



    /**
     * Returns a truncated String that is truncated at the latest whitespace prior to index.
     */
    public static String truncateString( String inStr , int index )
    {
    	if( inStr == null || inStr.length() < index )
    		return inStr;

        // get most previous whiteSpace index
        int pwi = getPreviousWhitespaceIndex( inStr , index );

        if( pwi > inStr.length() - 1 )
        	pwi = inStr.length() - 1;

        // if found a whitespace character
        if( pwi > 0 )
            return inStr.substring( 0 , pwi );

        // hard truncate
        return inStr.substring( 0 , index );
    }



    /**
     * Returns the index of the next occurance of a whitespace character within the provided string, of
     * of the String length if there is none.
     */
    public static int getNextWhitespaceIndex( String inStr , int index )
    {
        // if no length to string, return 0
        if( inStr.length() == 0 )
            return 0;

        // if already at length, return length
        if( index >= inStr.length() )
            return inStr.length();


        // get first char
        char ch = inStr.charAt( index );

        while( !Character.isWhitespace( ch )  )
        {
            index++;

            if( index == inStr.length() )
                return index;

            // get next ch
            ch = inStr.charAt( index );

        }  // while

        return index;
    }



    /**
     * Returns the index of the most recent previous occurance of a whitespace character within the provided string, or 0
     * if there is none.
     */
    public static int getPreviousWhitespaceIndex( String inStr , int index )
    {

        // if no length to string, return 0
        if( inStr == null || inStr.length() == 0 )
            return 0;

        // if already at length, return length
        if( index >= inStr.length() )
            return inStr.length() - 1;

        // get first char
        char ch = inStr.charAt( index );

        while( !Character.isWhitespace( ch ) && index > 0 )
        {
            index--;

            if( index == 0 )
                return 0;

            // get next ch
            ch = inStr.charAt( index );

        }  // while

        return index;
    }






    public static String replaceChar( String inStr, char out, String in )
    {
      if ( ( inStr == null ) || ( inStr.length() == 0 ) )
        return ( "" );

      StringBuffer outStr = new StringBuffer( "" );

      for ( int i = 0; i < inStr.length(); i++ )
      {
        if ( inStr.charAt( i ) == out )
          outStr.append( in );
        else
          outStr.append( inStr.charAt( i ) );
      }

      return ( outStr.toString() );
    }

    public static String escapeChar( String inStr, char charToEscape , char escapeChar )
    {
      if ( ( inStr == null ) || ( inStr.length() == 0 ) )
        return ( "" );

      StringBuffer outStr = new StringBuffer( "" );

      for ( int i = 0; i < inStr.length(); i++ )
      {
        if ( inStr.charAt( i ) == charToEscape )
        {
            if( i == 0 || inStr.charAt( i-1 ) != escapeChar )
                outStr.append( escapeChar );

            outStr.append( charToEscape );
        }
        else
          outStr.append( inStr.charAt( i ) );
      }

      return ( outStr.toString() );
    }


    public static String removeChar( String inStr, char out )
    {
      StringBuffer outStr = new StringBuffer();

      for ( int i = 0;i < inStr.length();i++ )
      {
        if ( inStr.charAt( i ) != out )
          outStr.append( inStr.charAt( i ) );
      }

      return outStr.toString();
    }



    public static String replaceStr( String inStr , String oldPiece , String newPiece  )
    {

        if( inStr == null || inStr.length() == 0 )
            return "";

        if( oldPiece == null || oldPiece.length() == 0 )
            return inStr;

        if( newPiece == null )
            newPiece = "";

        StringBuilder outStr = new StringBuilder();

        int index = inStr.indexOf( oldPiece , 0 );

        if( index < 0 )
            return inStr;

        int lastIndex = 0;

        while( index >= 0 )
        {
            if( index > 0 )
                outStr.append( inStr.substring( lastIndex , index ) );

            outStr.append( newPiece );

            lastIndex =  index + oldPiece.length();

            if( lastIndex >= inStr.length() )
                break;

            index = inStr.indexOf( oldPiece , lastIndex );
        }

        // attach tail
        if( lastIndex < inStr.length() )
            outStr.append( inStr.substring( lastIndex , inStr.length() ) );

        return outStr.toString();
    }


    public static  String replaceStr(   String inStr ,
                                        String findStr ,
                                        String replaceStr ,
                                        boolean ignoreCase) throws Exception
    {
        try
        {
            if( inStr == null || inStr.length() == 0 )
                return inStr;

            if( findStr == null || findStr.length() == 0 )
                return inStr;

            if( replaceStr == null )
                replaceStr = "";

            if( !ignoreCase )
                return inStr.replaceAll( findStr , replaceStr );

            // work on upper case
            findStr = findStr.toUpperCase();

            String tempInStr = inStr.toUpperCase();

            int index = tempInStr.indexOf( findStr );

            int startIndex = 0;

            String outStr = "";

            while( index >= 0 && index < tempInStr.length() )
            {
                outStr += inStr.substring( startIndex , index );

                outStr += replaceStr;

                index += findStr.length();

                startIndex = index;

                if( index < inStr.length() )
                    index = tempInStr.indexOf( findStr , index );
            }

            if( startIndex < inStr.length() )
                outStr += inStr.substring( startIndex , inStr.length() );

            return outStr;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "replaceStr( inStr=" + inStr + ", findStr=" + findStr + ", replaceStr=" + replaceStr + ", replaceStr=" + replaceStr + " ) " + e.toString() );

            throw new STException( e );
        }
    }

    
    public static String addBracketedArtifactToStr( String inStr, String name, String value )
    {
        if( inStr==null )
            inStr = "";
        
        String t = inStr.trim();

        if( name == null || name.isBlank() )
            return null;

        name = name.trim();

        if( name.startsWith("[" ) )
            name = name.substring(1, name.length() );

        if( name.endsWith( "]") )
            name = name.substring(0,name.length()-1);

        if(name.isEmpty() )
            return null;

        // Is key already tehre?
        int idx = t.indexOf( "[" + name + "]" );

        // if there, is there a following key
        int idx2 = idx>=0 ? t.indexOf(  "[" , idx+2 + name.length() ) : -1;
        
        // no following key
        if( idx2 < 0 )
            idx2 = t.length();

        // key doesn't exist yet, so add to end.
        if( idx<0 )
        {
            if( value==null || value.isBlank() )
                return t;
            
            return t + "[" + name + "]" + value;
        }

        // remove key
        String ta = idx>0 ? t.substring( 0, idx ) : "";
        String tb = idx2<t.length() ? t.substring(idx2, t.length()) : "";
        
        if( value==null || value.isBlank() )
            return ta + tb;
            
        return ta + tb + "[" + name + "]" + value;        
    }



    
    public static String getBracketedArtifactFromString( String inStr, String name )
    {
        String t = inStr;

        if( name == null || t == null || t.isEmpty() )
            return null;

        name = name.trim();

        if( name.startsWith("[" ) )
            name = name.substring(1, name.length() );

        if( name.endsWith( "]") )
            name = name.substring(0,name.length()-1);

        if(name.isEmpty() )
            return null;

        int idx = t.indexOf( "[" + name + "]" );

        if( idx <0 )
            return null;

        int idx2 = t.indexOf(  "[" , idx+2 + name.length() );

        if( idx2 < 0 )
            idx2 = t.length();

        return t.substring( idx + 2 + name.length() , idx2 ).trim();
    }

    
    
}