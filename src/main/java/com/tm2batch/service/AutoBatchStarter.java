/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.service;

import com.tm2batch.global.RuntimeConstants;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 *
 * @author Mike
 */
public class AutoBatchStarter implements Runnable {
    
    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static ScheduledFuture<?> sched = null;
    
    
    @Override
    public void run() {

        // LogService.logIt( "AutoBatchStarter.run() Starting"  );

        try
        {
            LogService.logIt( "TM2Batch - AutoBatchStarter.run() START AutoBatches On=" + RuntimeConstants.getBooleanValue( "autoReportBatchesOk" ) );
            
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/New_York"));
            ZonedDateTime nextRun = now.withHour(6).withMinute(0).withSecond(0);
            
            // if we are already after this time. 
            if(now.compareTo(nextRun) > 0)
                nextRun = nextRun.plusDays(1);

            // Set the delay
            Duration duration = Duration.between(now, nextRun);
            long initalDelay = duration.getSeconds();            
            
            // wait a few minutes to start this.
            Thread.sleep( 120000 );
            
            // LogService.logIt( "TM2Convert - AutoBatchStarter.run() STARTING SETUP  BBBB ");
            final Runnable autoBatchThread = new AutoBatchThread();
    
            // final ScheduledFuture<?> sched = scheduler.scheduleAtFixedRate(autoScoreThread, 30, 180, SECONDS);
            // waits 30 seconds, then runs every 5 minutes.
            sched = scheduler.scheduleAtFixedRate(autoBatchThread, initalDelay, TimeUnit.HOURS.toSeconds(12), SECONDS );
            
            LogService.logIt( "TM2Batch - AutoBatchStarter.run() COMPLETED SETUP CCCC Initial Delay=" + initalDelay + " seconds.");
        }
        catch( Exception ee )
        {
            LogService.logIt(ee, "TM2Batch - AutoBatchStarter.run() Uncaught Exception during autobatch." );
             (new EmailUtils()).sendEmailToAdmin( "TM2Batch AutoBatchStarter.run() Uncaught Exception during autobatch.", "Time: " + (new Date()).toString() + ", Error was: " + ee.toString() );
        }



    }



}
