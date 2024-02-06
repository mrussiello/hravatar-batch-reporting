/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.api;

import com.tm2batch.global.Constants;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.service.EmailUtils;
import com.tm2batch.service.LogService;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author miker_000
 */
public class BaseBatchRestResource {
    
    protected boolean checkCreds( String username, String password ) 
    {
        try
        {
            // LogService.logIt( "BaseBuilderRest.checkCreds() un=" + username + ", pw=" + password );
            if( username==null || username.isBlank())
                return false;
            if( password==null || password.isBlank() )
                return false;

            username=username.trim();
            password=password.trim();
            
            if( !username.equalsIgnoreCase( RuntimeConstants.getStringValue("tm2batch_rest_api_username").trim()))
                return false;
            
            if( !password.equals(  RuntimeConstants.getStringValue("tm2batch_rest_api_password").trim() ) )
                return false;
                        
            return true;
        }
        
        catch( Exception e )
        {
            LogService.logIt( e, "BaseBatchRestResource.checkCreds() " );
            
            return false;
        }
    }
    
    protected String[] getBasicCreds( HttpServletRequest request ) throws Exception
    {
        String authStr = request.getHeader("Authorization");

        if( authStr == null || authStr.isEmpty() )
            throw new Exception( "No authorization credentials found in request." );

        if( authStr.toLowerCase().indexOf( "basic ") >=0 )
            authStr = authStr.substring(authStr.toLowerCase().indexOf( "basic ") + 6, authStr.length() );

        String credentials = new String(Base64.getDecoder().decode(authStr), Charset.forName("UTF-8"));

        if( credentials.isEmpty() )
            throw new APIException( 001, "Authentication Failure.", null );
            // throw new Exception( "Could not convert authorization credentials. authStr=" + authStr );

        if( credentials.indexOf(":")<0 )
            throw new APIException( 002, "Authentication Failure.", null );
            // throw new Exception( "Could not parse authorization credentials. authStr=" + authStr + ", credentials=" + credentials );

        // credentials = username:password
        return credentials.split( ":" , 2 );    
    }
    
    
    protected static void sendInternalNotificationEmail( String subject, String content ) 
    {
        try
        {

            EmailUtils emailUtils = new EmailUtils();

            if( content == null || content.isEmpty() )
                throw new Exception( "Content is missing." );
            
            if( subject==null || subject.isEmpty() )
                subject = "HR Avatar Twilio REST Integration Error Message";
            
            Map<String, Object> emailMap = new HashMap<>();

            emailMap.put( EmailUtils.SUBJECT, subject );

            emailMap.put( EmailUtils.CONTENT, content );

            StringBuilder sb; //  = new StringBuilder();

            emailMap.put( EmailUtils.TO, RuntimeConstants.getStringValue( "systemerrornotifyemails" )  );

            sb = new StringBuilder();

            sb.append( Constants.SUPPORT_EMAIL );

            emailMap.put( EmailUtils.FROM, sb.toString() );

            // emailMap.put( EmailConstants.OVERRIDE_BLOCK, "true" );

            LogService.logIt("BaseBatchRestResource.sendInternalNotificationEmail() content=" + content );

            emailUtils.sendEmail( emailMap );
        }

        catch( Exception e )
        {
            LogService.logIt(e, "BaseBatchRestResource.sendInternalNotificationEmail() SUBJ: " + subject + ", MSG: " + content );
        }

    }
    
    
    
    
    
    /*
    private String getIdValueValue( String name, List<com.tm2builder.reconstruct.xml.IdValue> idValueList )
    {
        // LogService.logIt( "GetReviewKeyResource.getIdValueValue() name=" + name + ", listSize=" + idValueList.size() );
        if( name==null || name.isEmpty() )
            return null;

        for( com.tm2builder.reconstruct.xml.IdValue idValue : idValueList )
        {
            // LogService.logIt( "GetReviewKeyResource.getIdValueValue() TARGET name=" + name + ", curname=" + idValue.getName() + ", value=" + idValue.getValue() );
            if( idValue.getName()!=null && idValue.getName().equals(name) )
                return idValue.getValue();
        }

        return null;
    }
    */

    
    
}
