/*
 * Created on Jan 19, 2007
 *
 */
package com.tm2batch.service;

import com.tm2batch.global.STException;





public class EncryptUtils
{

    public static final String  DEFAULT_ENCRYPTION_KEY   = "MIFavor1teSp0rt1s10nnisOooutsydeInTheSun";


    public static final String  DEFAULT_FILE_ENCRYPTION_KEY   = "JustALittle5923FuN098sWSDFGWETheSDFsddQ";



    public static String urlSafeEncrypt( long i , String key ) throws Exception
    {
        try
        {
            return urlSafeEncrypt( Long.toString( i ) , key );
        }

        catch( Exception e )
        {
            LogService.logIt( e ,  "urlSafeEncrypt( value=" + i + ", key=" + key + " ) "  );

            throw new STException( e );
        }

    }



    public static String urlSafeEncrypt( long i ) throws Exception
    {
        try
        {
            return urlSafeEncrypt( Long.toString( i ) );
        }

        catch( Exception e )
        {
            LogService.logIt( e ,  "urlSafeEncrypt( value=" + i + " ) "  );

            throw new STException( e );
        }

    }




   public static String urlSafeEncrypt( String s ) throws Exception
   {
       try
       {
           return urlSafeEncrypt( s , null );
       }

       catch( Exception e )
       {
           LogService.logIt( e ,  "urlSafeEncrypt( " + s + " ) "  );

           throw new STException( e );
       }

   }


    /**

     * Encodes a string into a url-friendly base64 encoded string

     */
    public static String urlSafeEncrypt( String s ,
                                         String key ) throws Exception
    {
        try
        {
            String newStr = null;

            if( s != null )
                s = s.trim();

            if (s != null) {

                newStr = encryptString(s , key );

                newStr = newStr.replace( '+', '_');

                newStr = newStr.replace( '/', '-');

                newStr = newStr.replace( '=', '*');

                newStr = newStr.replaceAll( ">", "");

                newStr = newStr.replaceAll( "<", "");

                newStr = newStr.replaceAll( "\n", "");

                newStr = newStr.replaceAll( "\r", "");

            }

            return newStr;
        }

        catch( Exception e )
        {
            LogService.logIt( e ,  "urlSafeEncrypt( value=" + s + ", key=" + key + " ) "  );

            throw new STException( e );
        }

    }


    /**
     * Encodes in a way that is filename / filesystem safe
     *
     * @param s
     * @return
     * @throws Exception
     */
    public static String fileSafeEncrypt( String s ) throws Exception
    {
        try
        {
            if( s != null )
                s = s.trim();

            String newStr = urlSafeEncrypt( s , DEFAULT_FILE_ENCRYPTION_KEY );

            newStr = newStr.replace( '*', '-');

            return newStr;
        }

        catch( STException e )
        {
            throw e;
        }

        catch( Exception e )
        {
            LogService.logIt( e ,  "fileSafeEncrypt( value=" + s + " ) "  );

            throw new STException( e );
        }

    }


    /**
     * Encodes in a way that result can be used in a Javascript variable.
     *
     * @param s
     * @return encrypted string
     *
     * @throws Exception
     */
    public static String javascriptSafeEncrypt( String s ) throws Exception
    {
        try
        {
            if( s != null )
                s = s.trim();

            String newStr = urlSafeEncrypt( s , DEFAULT_FILE_ENCRYPTION_KEY );

            newStr = newStr.replace( '*', '_');

            newStr = newStr.replace( '-', '_');

            return newStr;
        }

        catch( STException e )
        {
            throw e;
        }

        catch( Exception e )
        {
            LogService.logIt( e ,  "fileSafeEncrypt( value=" + s + " ) "  );

            throw new STException( e );
        }

    }



    public static String urlSafeDecrypt( String s ) throws Exception
    {

        try
        {
            if( s == null || s.length() == 0 )
                return s;

            return urlSafeDecrypt( s , null );
        }

        catch( Exception e )
        {
            LogService.logIt( e ,  "urlSafeDecrypt( " + s + " ) " + e.toString()  );

            throw new STException( e );
        }
    }



    /**

     * Returns the original string given a url-converted base64 encoded string

     */

    public static String urlSafeDecrypt(String s , String key ) throws Exception
    {

        try
        {
            String newStr = s;

            if (newStr != null)
            {
                newStr = newStr.trim();

                if( newStr.length() > 0 &&
                        newStr.length() % 4 == 3 )
                    newStr += "*";

                newStr = newStr.replaceAll( "%2A", "*" );

                newStr = newStr.replace('_', '+');

                newStr = newStr.replace('-', '/');

                newStr = newStr.replace('*', '=');

                newStr = decryptString(newStr , key );
            }

            return newStr;
        }

        catch( Exception e )
        {
            LogService.logIt( e ,  "urlSafeDecrypt( value=" + s + ", key=" + key + " ) " + e.toString()  );

            throw new STException( e );
        }
    }







    protected static String decryptString( String inStr , String key ) throws Exception
    {
        try
        {
            if( inStr == null )
                return null;

            if( inStr.length() == 0 )
                return inStr;

            //
            // if( encrypter == null )
            //     initEncrypter();

            StringEncrypter encrypter = new StringEncrypter( StringEncrypter.DES_ENCRYPTION_SCHEME , key == null ? DEFAULT_ENCRYPTION_KEY : key );

            return encrypter.decrypt( inStr );
        }

        catch( Exception e )
        {
            LogService.logIt( e ,  "decryptString( " + inStr + " ) " + e.toString()  );

            throw new STException( e );
        }

    }



    protected static String encryptString( String inStr , String key ) throws Exception
    {
        try
        {
            if( inStr == null )
                return null;

            if( inStr.length() == 0 )
                return inStr;

            StringEncrypter encrypter = new StringEncrypter( StringEncrypter.DES_ENCRYPTION_SCHEME , key == null ? DEFAULT_ENCRYPTION_KEY : key  );


            return encrypter.encrypt( inStr );
        }

        catch( Exception e )
        {
            LogService.logIt( e ,  "encryptString( " + inStr + " ) " + e.toString()  );

            throw new STException( e );
        }

    }


    public void LogIt( String message )
    {
        LogService.logIt( message );
    }

}
