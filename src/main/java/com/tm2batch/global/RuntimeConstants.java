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


public class RuntimeConstants
{
    private static char pathSeparator = ' ';

    private static Map<String, Object> cache = null;

    public static boolean DEBUG = false;

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

        cache.put( "httpsONLY", true );
        
        cache.put( "useAwsMediaServer", true);
        
        cache.put( "mediaServerProtocol", "https" );
        cache.put( "mediaServerDomain", "cdn.hravatar.com" );
        cache.put( "mediaServerWebapp", "web" );

        
        cache.put( "baseadminuri", "https://www.hravatar.com/ta" );
        
        
        
        // Note - these are the credentials for S3 Administrator. No other services used by this application.
        //cache.put( "awsAccessKey", "" );
        //cache.put( "awsSecretKey", "" );

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
        
        // ////////////////////////////////////////////////////////////////////////////
        // AWS Creds
        // ////////////////////////////////////////////////////////////////////////////
        
        
        //cache.put( "awsAccessKey", "" );
        //cache.put( "awsSecretKey", "" );
        
        
        
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

        // load properties from file. File overlays everything.
        String propertiesFile = (String) cache.get( "propertiesFile" );
        if( propertiesFile != null && !propertiesFile.isBlank() )
            loadProperties( propertiesFile );

        propertiesFile = (String) cache.get( "secretsFile" );
        if( propertiesFile != null && !propertiesFile.isBlank() )
            loadProperties( propertiesFile );

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

                    if( name != null && name.length() > 0 && strValue != null && strValue.length() > 0 )
                    {
                        currentValue = cache.get( name );

                        //if( currentValue == null )
                        //    cache.put( name, strValue );

                        if( currentValue!=null )
                        {
                            if( currentValue instanceof Integer )
                                cache.put( name, Integer.parseInt( strValue ) );

                            else if( currentValue instanceof Float )
                                cache.put( name, Float.parseFloat(strValue ) );

                            else if( currentValue instanceof Long )
                                cache.put( name, Long.parseLong(strValue ) );

                            else if( currentValue instanceof Boolean )
                                cache.put( name, Boolean.parseBoolean(strValue ) );

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
        return (String) cache.get( theKey );
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
    
    public static int[] getIntArray( String key, String delimiter )
    {
        List<Integer> ll = new ArrayList<>();
        
        int[] out = null;
        
        try
        {
            String s = getStringValue( key );

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
            LogService.logIt( e, "RuntimeConstants.getIntArray() key=" + key + ", delim=" + delimiter );
        }
        
        return out;
    }
    
    public static List<Integer> getIntList( String key, String delimiter ) throws Exception
    {
        List<Integer> idl = new ArrayList<>();
        int[] ial = RuntimeConstants.getIntArray(key, delimiter );
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
