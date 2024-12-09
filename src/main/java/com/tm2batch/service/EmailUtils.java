package com.tm2batch.service;

import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.global.STException;
import java.util.HashMap;
import java.util.Map;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

import org.apache.commons.validator.routines.EmailValidator;

//import org.apache.commons.validator.routines.EmailValidator;

public class EmailUtils
{
    public static final String HTML_HEADER = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n" +
                                             "<html><head><meta content=\"text/html;charset=UTF-8\" http-equiv=\"Content-Type\"><title></title></head><body bgcolor=\"#ffffff\" text=\"#000000\">\n";

    public static final String HTML_FOOTER = "</body></html>";


    public static String CONTENT = "content";
    public static String TO = "to";
    public static String FROM = "from";
    public static String CC = "cc";
    public static String BCC = "bcc";
    public static String SUBJECT = "subject";
    public static String MIME_TYPE = "mime";
    public static String ATTACH_BYTES = "attach_bytes_";
    public static String ATTACH_MIME = "attach_mime_";
    public static String ATTACH_FN = "attach_name_";
    public static String OVERRIDE_BLOCK = "overrideblock";
    public static String OVERRIDE_FULLBLOCK = "overrideblockFULL";    
    public static String[] VALID_SUPPORT_ADDRESSES = { "support" , "help" , "payments" , "info" , "sales", "scoring" };


    //@EJB
    private EmailerFacade emailerFacade;

    
    public void sendEmailWithSingleAttachment( String subject, String content, String toEmailsCommaDelim, String mimeType, String attachMimeType, String attachFilename, byte[] attachBytes  )
    {
        try
        {
            if( attachBytes==null || attachBytes.length<=0)
                throw new Exception( "Attachment bytes is missing." );

            if( attachMimeType == null || attachMimeType.isEmpty() )
                throw new Exception( "Attachment MimeType is missing." );
            
            if( attachFilename == null || attachFilename.isEmpty() )
                throw new Exception( "Attachment Filename is missing." );
            
            
            if( content == null || content.isEmpty() )
                throw new Exception( "Content is missing." );
            
            if( subject==null || subject.isEmpty() )
                subject = "HR Avatar Internal Message ";
            
            Map<String, Object> emailMap = new HashMap<>();

            emailMap.put( SUBJECT, subject );
            emailMap.put( CONTENT, content );            
            emailMap.put( MIME_TYPE, mimeType==null || mimeType.isBlank() ? "text/plain" : mimeType );

            StringBuilder sb; //  = new StringBuilder();

            emailMap.put( TO, toEmailsCommaDelim  );

            sb = new StringBuilder();
            sb.append( RuntimeConstants.getStringValue("no-reply-email") );
            emailMap.put( FROM, sb.toString() );
            emailMap.put( OVERRIDE_BLOCK, "true" );

            emailMap.put( ATTACH_MIME + "0", attachMimeType );
            emailMap.put( ATTACH_FN + "0", attachFilename );
            emailMap.put( ATTACH_BYTES + "0", attachBytes );
            
            LogService.logIt("EmailUtils.sendEmailWithSingleAttachment() content=" + content + ", bytes attached.length=" + attachBytes.length );
            
            // EmailerFacade  emailerFacade = EmailerFacade.getInstance();

            sendEmail( emailMap );
        }

        catch( Exception e )
        {
            LogService.logIt(e, "EmailUtils.sendEmailWithSingleAttachment() " );
        }

    }
    
    

    public void sendEmailToAdmin( String subj, String msg )
    {
        sendEmailToAdmin(  subj,  msg, null );
    }
    
    public void sendEmailToAdmin( String subj, String msg, String emailAddr )
    {
        try
        {
                // prepare to send
                Map<String, Object> emailMap = new HashMap<>();

                emailMap.put( EmailUtils.MIME_TYPE , "text/plain" );
                emailMap.put( EmailUtils.SUBJECT, subj );
                emailMap.put( EmailUtils.CONTENT, msg );
                emailMap.put( EmailUtils.TO, emailAddr==null || emailAddr.isEmpty() ? RuntimeConstants.getStringValue("system-admin-email")  : emailAddr );

                // emailMap.put( EmailUtils.FROM, Constants.SUPPORT_EMAIL + "|" + MessageFactory.getStringMessage( locale , "g.SupportEmailKey", null ) );
                emailMap.put( EmailUtils.FROM, RuntimeConstants.getStringValue("no-reply-email") );

                sendEmail( emailMap );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "EmailUtils.sendEmailToAdmin(" + subj + ", msg=" + msg + " )" );
        }
    }


    /**
     * Sends an email message fia a JMS queue.
     *
     * @param messageInfoMap must have the following values: <pre>
     *
     *
     *      to          List<InternetAddress>
     *      cc          List<InternetAddress>
     *      subject     String
     *      from        InternetAddress
     *      mime        mime type. Defaults to text/plain supports text/html
     *      content     String, content of the message
     *      attachments ordered params starting at 0
     *         attach_bytes_0 - bytes    byte[]
     *         attach_mime_0 - mime     String
     *         attach_name_0 - filename String
     *
     *
     *
     * </pre>
     */
    public void sendEmail( Map<String,Object> messageInfoMap ) throws Exception
    {
        try
        {
            if( emailerFacade == null )
                emailerFacade = EmailerFacade.getInstance();

            emailerFacade.sendEmail( messageInfoMap );
        }

        catch( Exception e )
        {
            LogService.logIt( e , "EmailUtils.sendEmail()" );

            throw new STException( e );
        }
    }



    public static boolean validateEmail( InternetAddress iAddr ) throws Exception
    {
        try
        {
            if( 1==1 )
                return EmailValidator.getInstance().isValid( iAddr.getAddress() );
            //LogService.logIt( "EmailUtils.validateEmail() email=" + iAddr.getAddress() );
            
            else 
               iAddr.validate();
        }

        catch( AddressException e )
        {
            LogService.logIt( "Email found invalid: " + ( iAddr==null ? "null" : iAddr.getAddress()) );

            String[] params = new String[2];

            params[0] = iAddr.getAddress();

            params[1] = e.getMessage();

            throw new STException( "g.InvalidEmailAddress" , params );
        }

        catch( Exception e )
        {
            LogService.logIt( e , "EmailUtils.validateEmail() " + ( iAddr == null ? "address is null" : iAddr.getAddress() ) );

            return false;
        }

        return true;
    }



    public static boolean validateEmailNoErrors( String email )
    {
        try
        {
            return validateEmail( new InternetAddress( email ) );
        }

        catch( Exception e )
        {
            return false;
        }
    }



    public static boolean validateEmail( String email ) throws Exception
    {
        try
        {
            return validateEmail( new InternetAddress( email ) );
        }

        catch( AddressException e )
        {
            String[] params = new String[2];
            params[0] = email;
            params[1] = e.getMessage();
            throw new STException( "g.InvalidEmailAddress" , params );
        }
    }





}
