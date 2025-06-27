/*
 * Created on Dec 12, 2006
 *
 */
package com.tm2batch.global;

import com.tm2batch.service.LogService;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;


public class RuntimeConstants
{
    private static char pathSeparator = ' ';

    private static Map<String, Object> cache = null;

    public static boolean DEBUG = false;

    public static SecretKey sealedObjectSecretKey = null;

    /**
     * Init
     */
    static
    {
        cache = new TreeMap<>();

        cache.put( "pathSeparator" , "/" );
        cache.put( "services/email/mailon", true);

        cache.put( "services/log/logfilepattern", "/work/tm2batch2/log/tm2batch_%g_%u.log");

        cache.put( "propertiesFile", "/work/tm2batch2/zzapplication.conf" );
        cache.put( "secretsFile", "/work/hraconfig/hraglobals-cfmain.conf" );

        cache.put( "filesroot", "/work/tm2batch2/files" );

        cache.put( "httpsONLY", true );

        cache.put( "useAwsMediaServer", true);

        cache.put( "baselogourl", "https://cdn.hravatar.com/web/orgimage/zrWvh1uNWrg-/img_8x1715795136855.png" );
        cache.put( "baseiconurl", "https://cdn.hravatar.com/web/orgimage/zrWvh1uNWrg-/img_21x1717875839610.png" );

        cache.put( "logoDarkTextFilename","hralogoblacktext-blue.png");
        cache.put( "logoWhiteTextFilename","hralogowhitetext-blue.png");
        cache.put( "logoDarkTextSmallFilename","hralogoblacktext-small-blue.png");
        cache.put( "logoWhiteTextSmallFilename","hralogowhitetext-small-blue.png");
        
        cache.put("hraLogoBlackTextFilename","hra-two-color-tagline-logo-trans-800.png");
        cache.put("hraLogoBlackTextPurpleFilename","hra-two-color-tagline-logo-trans-800.png"); 

        cache.put("hraLogoWhiteTextFilename","hra-white-tagline-logo-trans-800.png"); 
        cache.put("hraLogoWhiteTextPurpleFilename","hra-white-tagline-logo-trans-800.png"); 

        cache.put("hraLogoBlackTextSmallFilename","hra-two-color-tagline-logo-trans-420.png"); 
        cache.put("hraLogoBlackTextSmallPurpleFilename","hra-two-color-tagline-logo-trans-420.png"); 

        cache.put("hraLogoWhiteTextSmallFilename","hra-white-tagline-logo-trans-412.png");      
        cache.put("hraLogoWhiteTextSmallPurpleFilename","hra-white-tagline-logo-trans-412.png"); 
        
        cache.put( "hraCoverPageFilename", "https://cfmedia-hravatar-com.s3.us-east-1.amazonaws.com/web/misc/report/cover-bg-2.png" );
        cache.put( "hraCoverIncludedArrowFilename", "https://cfmedia-hravatar-com.s3.us-east-1.amazonaws.com/web/misc/report/cover-blue-arrow-solid.png");
        

        cache.put( "mediaServerProtocol", "https" );
        cache.put( "mediaServerDomain", "cdn.hravatar.com" );
        cache.put( "mediaServerWebapp", "web" );
        
        cache.put( "default-site-name", "HR Avatar" );
        cache.put( "default-site-name-cap", "HR Avatar" );
                
        cache.put( "no-reply-email", "no-reply@hravatar.com" );
        cache.put( "support-email", "support@hravatar.com" );
        cache.put( "system-admin-email", "mike@hravatar.com" );


        cache.put( "baseurl", "https://sim.hravatar.com/td" );
        cache.put( "baseadminuri", "https://www.hravatar.com/ta" );

        cache.put( "testingappprotocol", "https" );
        cache.put( "testingappbasedomain", "test.hravatar.com" );
        cache.put( "testingappcontextroot", "tt" );
        cache.put( "testingapphttpsOK", true);


        cache.put( "tm2batch_rest_api_username", "" );
        cache.put( "tm2batch_rest_api_password", "" );

        cache.put( "systemerrornotifyemails" , "mike@hravatar.com" );

        cache.put( "awsBucket", "cfmedia-hravatar-com" );
        cache.put( "awsBucketLvRecording", "lv-hravatar-com" );
        cache.put( "awsBucketFileUpload", "ful-clicflic-com" );
        cache.put( "awsBucketProctorRecording", "rp-hravatar-com" );

        cache.put( "awsBaseKey", "web/" );
        cache.put( "awsBaseKeyLvRecording", "recordings/" );
        cache.put( "awsBaseKeyLvRecordingTest", "recordingstest/" );
        cache.put( "awsBaseKeyFileUpload", "" );

        cache.put( "awsBaseKeyProctorRecording", "proctorrecordings/" );
        cache.put( "awsBaseKeyProctorRecordingTest", "proctorrecordingstest/" );


        cache.put( "fileTreeRootDirectory" , "/work/sm1/web" );
        cache.put( "localFsFileUploadPath" , "/ful" );


        cache.put( "fileUploadDirCF" , "/cf" );
        cache.put( "fileUploadDirTm2" , "/hra" );
        cache.put( "fileUploadDirLv" , "/lv" );


        cache.put( "applicationSystemId", (int)( 1405 ) );

        cache.put( "stringEncryptorKey",  "" );
        cache.put( "stringEncryptorKeyFileSafe",  "" );

        cache.put( "disableCertificateVerification", true );

        // ////////////////////////////////////////////////////////////////////////////
        // LV Live
        // ////////////////////////////////////////////////////////////////////////////

        cache.put( "AdminSiteBaseUrl", "https://www.hravatar.com/ta" );
        cache.put( "LiveVideoBaseUrl", "https://www.hravatar.com/ti" );

        // ////////////////////////////////////////////////////////////////////////////
        // RC Checks
        // ////////////////////////////////////////////////////////////////////////////

        cache.put( "RefCheckContactPermissionRcItemIds", "178" );
        cache.put( "RefCheckContactRecruitingRcItemIds", "180" );
        cache.put( "RefCheckPriorRoleRcItemIds", "174" );


        // ////////////////////////////////////////////////////////////////////////////
        // Batch Reports
        // ////////////////////////////////////////////////////////////////////////////

        cache.put( "autoReportBatchesOk", true );

        cache.put( "twilio.textingon", true );
        cache.put( "twilio.sid", "" );
        cache.put( "twilio.auhtoken", "" );
        cache.put( "twilio.fromnumber", "+17036353077" );

        cache.put( "twilio.sandboxpin", "7760-3166" );

        cache.put( "twilio.useSandbox", false );
        cache.put( "twilio.sandboxphonenumber", "(415) 599-2671" );

        cache.put( "useTwilioDevelopmentNumber",  false );
        cache.put( "twilioDevelopmentNumber",  "7036353077" );
        cache.put( "twilioDevelopmentNumberFormatted",  "+1 703-635-3077" );


        // ////////////////////////////////////////////////////////////////////////////
        // Org Justice
        // ////////////////////////////////////////////////////////////////////////////

        cache.put( "orgJusticeProductIds",  "6510" );
        cache.put( "orgJusticeReportId",  105 );
        cache.put( "Uminn_OrgJustice_Dev", false );

        // ////////////////////////////////////////////////////////////////////////////
        // DISC
        // ////////////////////////////////////////////////////////////////////////////

        cache.put( "discProductIds",  "7861,8568" );
        cache.put( "discGroupReportId",  122 );

        
        // ////////////////////////////////////////////////////////////////////////////
        // Leader Style
        // ////////////////////////////////////////////////////////////////////////////

        cache.put( "leaderStyleProductIds",  "8141" );
        cache.put( "leaderStyleGroupReportId",  125 );
        
        
        // ////////////////////////////////////////////////////////////////////////////

        // load properties from file. File overlays everything.
        String propertiesFile = (String) cache.get( "propertiesFile" );
        if( propertiesFile != null && !propertiesFile.isBlank() )
            loadProperties( propertiesFile );

        propertiesFile = (String) cache.get( "secretsFile" );
        if( propertiesFile != null && !propertiesFile.isBlank() )
            loadProperties( propertiesFile );
        convertSecretsToSealedObjects();
    }

    private static synchronized void convertSecretsToSealedObjects()
    {
        if( sealedObjectSecretKey!=null )
            return;

        try
        {
            sealedObjectSecretKey = KeyGenerator.getInstance("DES").generateKey();
            Cipher ecipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, sealedObjectSecretKey );

            substituteStringWithSealedObject( "secretsFile", ecipher );
            substituteStringWithSealedObject( "stringEncryptorKey", ecipher );
            substituteStringWithSealedObject( "stringEncryptorKeyFileSafe", ecipher );
            substituteStringWithSealedObject( "twilio.auhtoken", ecipher );
        }
        catch( Exception e )
        {
            LogService.logIt( e, "RuntimeConstants.convertSecretsToSealedObjects()" );
        }
    }

    private static String getStringValueFromSealedObject( String cacheKey, SealedObject so )
    {
        if( cacheKey==null || cacheKey.isBlank() )
            return null;

        try
        {
            if( so==null )
            {
                Object o = cache.get(cacheKey);
                if( o ==null )
                    return null;
                else if( o instanceof String )
                    return (String)o;
                else if( o instanceof SealedObject )
                    so = (SealedObject)o;
                else
                    throw new Exception( "Cache value for key=" + cacheKey + " is not a String or SealedObject: " + o.getClass().getName() );
            }
            Cipher dcipher = Cipher.getInstance("DES");
            dcipher.init(Cipher.DECRYPT_MODE, sealedObjectSecretKey);
            return (String) so.getObject(dcipher);
        }
        catch( Exception e )
        {
            LogService.logIt("RuntimeConstants.getStringValueFromSealedObject() NONFATAL " + e.toString() + ", cacheKey=" + cacheKey );
        }
        return null;
    }

    private static void substituteStringWithSealedObject( String cacheKey, Cipher cipher )
    {
        try
        {
            if( cacheKey==null || cacheKey.isBlank() )
            {
                LogService.logIt( "RuntimeConstants.substituteStringWithSealedObject() cacheKey is invalid (null or empty). Skipping." );
                return;
            }

            Object o = cache.get(cacheKey );
            if( o==null )
                throw new Exception( "no entry found for CacheKey " + cacheKey );

            if( !(o instanceof String) )
                throw new Exception( "Value for CacheKey " + cacheKey + " is not a String. Class="  + (o.getClass().getName()) );

            SealedObject so = new SealedObject((String)o, cipher);
            cache.put( cacheKey, so );

        }
        catch( Exception e )
        {
            LogService.logIt("RuntimeConstants.substituteStringWithSealedObject() NONFATAL " + e.toString() + ", cacheKey=" + cacheKey );
        }
    }

    private static void loadProperties( String propertiesFile )
    {
        try
        {
            if( propertiesFile != null && !propertiesFile.isBlank() )
            {
                Properties props = new Properties();

                try
                {
                    props.load( new FileInputStream( propertiesFile ) );
                }

                catch( Exception e )
                {
                    System.out.println( "ERROR Loading RuntimeConstants: " + e.toString() );
                }

                Enumeration propertyNames = props.propertyNames();

                String name = null;

                String strValue = null;

                Object currentValue = null;

                while( propertyNames.hasMoreElements() )
                {
                    name = (String) propertyNames.nextElement();

                    strValue = props.getProperty( name );

                    if( strValue!=null )
                        strValue=strValue.trim();

                    if( name != null && name.length() > 0 && strValue != null && strValue.length() > 0 )
                    {
                        currentValue = cache.get( name );

                        //if( currentValue == null )
                        //    cache.put( name, strValue );

                        if( currentValue!=null )
                        {
                            if( currentValue instanceof Integer )
                                cache.put(name, Integer.valueOf( strValue ) );

                            else if( currentValue instanceof Float )
                                cache.put(name, Float.valueOf(strValue ) );

                            else if( currentValue instanceof Long )
                                cache.put(name, Long.valueOf(strValue ) );

                            else if( currentValue instanceof Boolean )
                                cache.put(name, Boolean.valueOf(strValue ) );

                            else
                                cache.put( name, strValue );
                        }

                        // logIt( "Revised property from file: " + name + " : " + strValue );
                    }
                }
                LogService.logIt( "RuntimeConstants.loadProperties() Updated " + props.size() + " keys from " + propertiesFile );
            }
        }
        catch( Exception e )
        {
            LogService.logIt( e, "RuntimeConstants.loadProperties() reading properties file=" + propertiesFile );
        }
    }


    public static String dumpAllValues()
    {
        StringBuilder sb = new StringBuilder( "RuntimeConstants:\n" );

        for( String name : cache.keySet() )
        {
            sb.append( name + "=" + ( cache.get( name ) ).toString() + "\n" );
        }

        try
        {
            LogService.init();
        }

        catch( Exception e )
        {
            System.out.println( "RuntimeConstants.dumpAllValues() " + e.toString() );
        }

        logIt( sb.toString() );

        return sb.toString();
    }

    public static boolean getHttpsOnly()
    {
        return RuntimeConstants.getBooleanValue("httpsONLY");
    }



    /**
     * Gets a value from the environment. Returns null if not found.
     */
    public static Object getValue( String theKey )
    {

        return cache.get( theKey );
    }

    public static void setValue( String theKey, Object theValue )
    {
        cache.put( theKey, theValue );
    }

    /**
     * Gets a value from the environment. Returns null if not found.
     */
    public static String getStringValue( String theKey )
    {
        Object o = cache.get( theKey );

        if( o==null )
            return null;
        if( o instanceof SealedObject )
            return RuntimeConstants.getStringValueFromSealedObject(theKey, (SealedObject)o);

        return (String)o;
    }


    /**
     * Gets a value from the environment. Returns null if not found.
     */
    public static Boolean getBooleanValue( String theKey )
    {
        return (Boolean) cache.get( theKey );
    }

    public static Integer getIntValue( String theKey )
    {
        return (Integer) cache.get( theKey );
    }

    public static Long getLongValue( String theKey )
    {
        return (Long) cache.get( theKey );
    }

    /**
     * logs messages
     */
    private static void logIt( String message )
    {
        LogService.getLogger().fine( message );
    }

    public static int[] getIntArrayForString(String s, String delimiter )
    {
        List<Integer> ll = new ArrayList<>();

        int[] out = null;

        try
        {
            if( s==null || s.isEmpty() )
                return new int[0];

            String[] tks = s.split( delimiter );

            for( String t : tks )
            {
                if( t!=null && !t.trim().isEmpty() )
                    ll.add( Integer.parseInt(t) );
            }

            out = new int[ll.size()];

            for( int i=0; i<ll.size(); i++ )
            {
                out[i] = ll.get(i).intValue();
            }
        }
        catch( NumberFormatException e )
        {
            LogService.logIt( e, "RuntimeConstants.getIntArrayForString() s=" + s + ", delim=" + delimiter );
        }

        return out;
    }

    public static int[] getIntArray( String key, String delimiter )
    {
        return getIntArrayForString( getStringValue( key ),  delimiter );
    }

    public static List<Integer> getIntList( String key, String delimiter ) throws Exception
    {
        List<Integer> idl = new ArrayList<>();
        int[] ial = RuntimeConstants.getIntArray(key, delimiter );
        for( int i : ial )
            idl.add( i );
        return idl;
    }

    public static List<Integer> getIntListForString( String s, String delimiter ) throws Exception
    {
        List<Integer> idl = new ArrayList<>();
        int[] ial = RuntimeConstants.getIntArrayForString(s, delimiter );
        for( int i : ial )
            idl.add( i );
        return idl;
    }


    public static char getPathSeparator()
    {

        if( pathSeparator == ' ' )
        {

            Object obj = cache.get( "pathSeparator" );

            String tempStr = null;

            if( obj != null )
            {
                tempStr = (String) obj;

                pathSeparator = tempStr.charAt( 0 );

                if( pathSeparator == 'x' )
                    pathSeparator = '\\';
            }

            else
                pathSeparator = '\\';
        }

        // return value
        return pathSeparator;
    }



}
