/*
 * Created on Dec 30, 2006
 *
 */
package com.tm2batch.service;

import java.util.Map;
import java.util.Set;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;

import javax.naming.InitialContext;

@Stateless
public class EmailerFacade
{

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

    @Resource( mappedName = "jms/ConnectionFactory" )
    protected ConnectionFactory connectionFactory;

    @Resource( mappedName = "jms/seenthatemailqueue" )
    protected Queue queue;

    public void sendEmail( Map<String, Object> messageInfoMap ) throws Exception
    {
        Connection connection = null;
        Session session = null;
        MessageProducer messageProducer = null;
        MapMessage message = null;

        try
        {
            // JMS connection
            connection = connectionFactory.createConnection();

            session = connection.createSession( false, Session.AUTO_ACKNOWLEDGE );

            messageProducer = session.createProducer( queue );

            message = session.createMapMessage();

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

            messageProducer.send( message );
        }

        catch( JMSException e )
        {
            LogService.logIt( e, "EmailerBean.sendEmail()" );

        }

        finally
        {
            if( connection != null )
            {
                try
                {
                    connection.close();
                }
                catch( JMSException e )
                {}
            } // if

        } // finally
    }
}
