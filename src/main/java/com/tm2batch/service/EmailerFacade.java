/*
 * Created on Dec 30, 2006
 *
 */
package com.tm2batch.service;

import jakarta.annotation.PreDestroy;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.JMSProducer;
import jakarta.jms.MapMessage;
import jakarta.jms.Queue;
import jakarta.jms.Session;

import javax.naming.InitialContext;

@Stateless
public class EmailerFacade
{
    @Resource(mappedName = "jms/ConnectionFactory")
    protected ConnectionFactory connectionFactory;

    @Resource(mappedName = "jms/seenthatemailqueue")
    protected Queue queue;

    static JMSContext context = null;
    JMSProducer  messageProducer = null;

    public EmailerFacade( ConnectionFactory cf, Queue q )
    {
        this.connectionFactory=cf;
        this.queue=q;
    }

    public EmailerFacade()
    {}


    public static EmailerFacade getInstance()
    {
        try
        {
            return (EmailerFacade) InitialContext.doLookup( "java:module/EmailerFacade" );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "getInstance() " );

            return null;
        }
    }


    public void sendEmail( Map<String, Object> messageInfoMap ) throws Exception
    {
        MapMessage message = null;

        try
        {
            if( context==null )
                context = connectionFactory.createContext();

            if( messageProducer==null )
                messageProducer = context.createProducer();

            message = context.createMapMessage();

            // now copy all keys to MapMessage

            Set<String> keys = messageInfoMap.keySet();

            Object obj;

            for( String key : keys )
            {
                // LogService.logIt( "Setting object: " + key + ", " + messageInfoMap.get( key ) );

                obj = messageInfoMap.get( key );

                if( obj instanceof String )
                    message.setString( key, (String) obj );

                if( obj instanceof byte[] )
                    message.setBytes( key, (byte[]) obj );
            }

            messageProducer.send(queue,message);
        }

        catch( Exception e )
        {
            LogService.logIt( e, "EmailerBean.sendEmail()" );

        }

    }
    
}
