/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.autoreport;

import com.tm2batch.entity.autoreport.BatchReport;
import com.tm2batch.faces.FacesUtils;
import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.global.STException;
import com.tm2batch.service.AutoBatchService;
import static com.tm2batch.service.AutoBatchService.STARTED;
import com.tm2batch.service.AutoBatchStarter;
import com.tm2batch.service.LogService;
import com.tm2batch.service.Tracker;
import com.tm2batch.user.UserBean;
import com.tm2batch.user.UserFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 *
 * @author Mike
 */
@Named
@RequestScoped
public class AdminAutoReportUtils extends FacesUtils implements Serializable {

    // static String[] NO_AUTO_EMAIL_AFFILIATE_IDS = new String[] {"dragnet", "hraph", "midot", "hrabenelux", "psiservices", "maran", "pantesting", "hrasia", "pantestingproduction", "icims", "manpowergroup", "avilar", "asiap"};
    
    @Inject
    UserBean userBean;
    
    @Inject
    AdminAutoReportBean adminAutoReportBean;

    AutoReportFacade autoReportFacade;
    
    UserFacade userFacade;
    


    public static AdminAutoReportUtils getInstance()
    {
        FacesContext fc = FacesContext.getCurrentInstance();

        return (AdminAutoReportUtils) fc.getApplication().getELResolver().getValue( fc.getELContext(), null, "adminAutoReportUtils" );
    }


    public String processClearDmbsCache()
    {
        try
        {
            if( !userBean.getUserLoggedOnAsAdmin())
                throw new STException( "g.YouAreNotAuthorizedForAction" );
                    
            if( userFacade==null )
                userFacade=UserFacade.getInstance();

            userFacade.clearSharedCache();
            
            setStringInfoMessage( "DBMS Cache Cleared." );            
            return "StayInSamePlace";            
        }
        catch( STException e )
        {
            setMessage( e );
        }
        catch( Exception e )
        {
            LogService.logIt( e, "AdminAutoEmailUtils.processEditBatchReport() " );
            setMessage( e );
        }
        
        return null;                        
    }

    public String processStopAutoScores()
    {
        try
        {
            if( !userBean.getUserLoggedOnAsAdmin())
                throw new STException( "g.YouAreNotAuthorizedForAction" );
                    
            RuntimeConstants.setValue( "autoReportBatchesOk", false );
            
            setStringInfoMessage( "Automatic Report Batches are OFF" );     
            
            if( AutoBatchService.STARTED )
            {
                if( AutoBatchStarter.sched != null )
                    AutoBatchStarter.sched.cancel(false);

                if( AutoBatchStarter.scheduler != null )
                    AutoBatchStarter.scheduler.shutdownNow();
          
                AutoBatchService.STARTED = false;                
                setStringInfoMessage( "AutoBatchStarter Stopped" );            
            }
            
            return "StayInSamePlace";            
        }
        catch( STException e )
        {
            setMessage( e );
        }
        catch( Exception e )
        {
            LogService.logIt( e, "AdminAutoEmailUtils.processStopAutoScores() " );
            setMessage( e );
        }
        
        return null;                        
    }

    public String processStartAutoScores()
    {
        try
        {
            if( !userBean.getUserLoggedOnAsAdmin())
                throw new STException( "g.YouAreNotAuthorizedForAction" );
                    
            RuntimeConstants.setValue( "autoReportBatchesOk", true );
            
            setStringInfoMessage( "Automatic Report Batches are ON" );            
            
            if( !AutoBatchService.STARTED )
            {
                (new Thread(new AutoBatchStarter())).start();
                AutoBatchService.STARTED = true;                
                setStringInfoMessage( "AutoBatchStarter Started" );            
            }
            return "StayInSamePlace";            
        }
        catch( STException e )
        {
            setMessage( e );
        }
        catch( Exception e )
        {
            LogService.logIt( e, "AdminAutoEmailUtils.processStartAutoScores() " );
            setMessage( e );
        }
        
        return null;                        
    }
    
    public String processStartBatchNow()
    {
        try
        {
            if( !userBean.getUserLoggedOnAsAdmin())
                throw new STException( "g.YouAreNotAuthorizedForAction" );
                    
            if( BatchReportManager.BATCH_IN_PROGRESS )
            {
                this.setStringErrorMessage( "A Report Batch is currently in progress. Please wait." );
                return "StayInSamePlace";
            }
            
            AutoReportBatchThread art = new AutoReportBatchThread();            
            (new Thread(art)).start();            
                        
            setStringInfoMessage( "A Report Batch has been started via Thread. Check logs for info." );            
            return "StayInSamePlace";            
        }
        catch( STException e )
        {
            setMessage( e );
        }
        catch( Exception e )
        {
            LogService.logIt( e, "AdminAutoEmailUtils.processStartBatchNow() " );
            setMessage( e );
        }
        
        return null;                        
        
    }
    
    
    public String processExecuteBatchReport()
    {
        LogService.logIt("AutoReportUtils.processExecuteBatchReport() START" );
        
        int batchReportId = adminAutoReportBean.getBatchReportId();
            
        try
        {
            if( !userBean.getUserLoggedOnAsAdmin())
                throw new STException( "g.YouAreNotAuthorizedForAction" );
                 
            if( batchReportId<=0 )
            {
                setStringErrorMessage( "Batch ReportId is required");
                return "StayInSamePlace";
            }
            
            if( BatchReportManager.BATCH_IN_PROGRESS )
            {
                setStringErrorMessage( "A Report Batch is currently in progress. Please wait." );
                return "StayInSamePlace";
            }
            
            adminAutoReportBean.setReportBytes(null);
            adminAutoReportBean.setReportName(null);
            if( autoReportFacade==null )
                autoReportFacade=AutoReportFacade.getInstance();
            
            BatchReport br = autoReportFacade.getBatchReport( batchReportId );
            
            if( br == null )
            {
                setStringErrorMessage( "Batch Report not found for batchReportId=" + batchReportId );
                return "StayInSamePlace";               
            }
            
            if( br.getBatchReportStatusTypeId()!=1 )
            {
                setStringErrorMessage( "Batch Report is not in Active Status. batchReportId=" + batchReportId );
                return "StayInSamePlace";           
            }
                        
            AutoReportThread art = new AutoReportThread( br ); 
            art.setOverrideSendFreq(true);
            byte[] bytes = art.runInline();

            if( bytes!=null && bytes.length>0 )
            {
                adminAutoReportBean.setReportBytes(bytes);
                
                String reportName = art.getFilename();                
                if( reportName==null )
                    reportName= "Report-" + "-" + br.getBatchReportId() + "-" + br.getBatchReportContentTypeId() + "-" + (new Date()).getTime() + ".xlsx";
                adminAutoReportBean.setReportName(reportName);                
                setStringInfoMessage( "Batch Report run successfully. Check logs for info. batchReportId=" + batchReportId + " " + br.getTitle() + ", reportName=" + reportName + ", bytes=" + (bytes==null ? "null" : bytes.length) );                                 
            }
            else
                setStringErrorMessage( "Batch Report failed. Check logs for info. batchReportId=" + batchReportId + " " + br.getTitle() + ", bytes=" + (bytes==null ? "null" : bytes.length) );                                 
        }
        catch( STException e )
        {
            setMessage( e );
        }
        catch( Exception e )
        {
            LogService.logIt( e, "AdminAutoEmailUtils.processExecuteBatchReport() batchReportId=" + batchReportId );
            setMessage( e );
        }        
        
        return "StayInSamePlace";   
    }
    

    public List<String[]> getStatusList()
    {
        return Tracker.getStatusList();        
    }
    
    
}
