/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.service;

import com.tm2batch.global.RuntimeConstants;
import java.util.Date;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AutoBatchService implements ServletContextListener {
        
    public static boolean STARTED = false;
    
    @Override
    public void contextInitialized(ServletContextEvent evt) 
    {  
        try
        {            
            LogService.logIt( "TM2Batch - AutoBatchService.contextInitialized() STARTING SETUP  AAAA ");

            if( !STARTED && RuntimeConstants.getBooleanValue( "autoReportBatchesOk" ) )
            {
                (new Thread(new AutoBatchStarter())).start();
                STARTED = true;
            }                      


            LogService.logIt( "TM2Batch - AutoBatchService.contextInitialized() COMPLETED SETUP  BBBB STARTED=" + STARTED );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "TM2Batch - AutoBatchService.contextInitialized() ");
            (new EmailUtils()).sendEmailToAdmin( "TM2Batch AutoBatchService.doReportAutoBatch() Error during Score Batch.", "Time: " + (new Date()).toString() + ", Error was: " + e.toString() );            
        }
    }
  
  
    @Override
    public void contextDestroyed(ServletContextEvent evt) 
    {
        try
        {
          STARTED = false;
          
          LogService.logIt( "TM2Batch - AutoBatchService.contextDestroyed() Stopping AutoBatchStarter." );

          if( AutoBatchStarter.sched != null )
              AutoBatchStarter.sched.cancel(false);

          if( AutoBatchStarter.scheduler != null )
              AutoBatchStarter.scheduler.shutdownNow();
          
        }      
        catch( Exception e )
        {
            LogService.logIt(e, "TM2Batch - AutoBatchService.contextDestroyed() Stopping AutoBatchStarter." );
        }
    } 
    
    
}
