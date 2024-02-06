/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.api;

import com.tm2batch.autoreport.AutoReportFacade;
import com.tm2batch.autoreport.AutoReportThread;
import com.tm2batch.entity.autoreport.BatchReport;
import com.tm2batch.service.LogService;
import com.tm2batch.util.JsonUtils;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.enterprise.context.RequestScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;

/**
 * REST Web Service 
 *
 * @author miker_000
 */
@Path("batchreportrequest")
@RequestScoped
public class BatchRequestResource extends BaseBatchRestResource {

    @Context
    private UriInfo context;
    
    // UserFacade userFacade;
    
    AutoReportFacade autoReportFacade;
    

    /**
     * Creates a new instance of GetReviewKeyResource
     */
    public BatchRequestResource() {
        
    }


    @POST
    @Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public String postBatchReportRequest(@Context HttpServletRequest request, String jsonContent) {

        JsonObjectBuilder responseJob = Json.createObjectBuilder();
                  
        int batchReportId = 0;
        String tran = null;
        
        try
        {
            if( jsonContent==null || jsonContent.isBlank() )
                throw new APIException( 101, "Json Content missing.", null );

            jsonContent = jsonContent.trim();
            
            String[] creds = getBasicCreds( request );    
            
            Boolean valid = checkCreds( creds[0],creds[1] );
            
            if( !valid )
                throw new APIException( 003, "Authentication Failure.", null );
                // throw new Exception( "Credentials did not authenticate! authStr=" + authStr + ", credentials=" + credentials );
            
            JsonObject requestJo = JsonUtils.convertJsonStringtoObject(jsonContent);

            if( requestJo==null )
                throw new APIException( 020, "Unable to parse request.", null );
                
            if( !requestJo.containsKey("tran") )
                throw new APIException( 021, "request JSON does not contain tran.", null );
            
            // start the job here
            tran = requestJo.getString("tran");

            if( tran!=null && tran.equals( "runbatchreport") )
            {
                batchReportId = requestJo.getInt("batchreportid");
                
                if( batchReportId<=0 )
                    throw new APIException( 022, "batchReportId is invalid: " + batchReportId, null );
                
                if( autoReportFacade==null )
                    autoReportFacade=AutoReportFacade.getInstance();
                
                BatchReport br = autoReportFacade.getBatchReport(batchReportId);
                
                if( br==null )
                    throw new APIException( 023, "BatchReport not found for batchReportId=" + batchReportId, null );
                
                if( br.getBatchReportStatusTypeId()!=1 )
                    throw new APIException( 024, "BatchReportis not active. batchReportId=" + batchReportId + ", batchReportStatusTypeId=" + br.getBatchReportStatusTypeId(), null );
                    
                LogService.logIt( "BatchRequestResource.postBatchReportRequest() Starting On-Demand Batch report.  batchReportId=" + batchReportId );
                
                AutoReportThread art = new AutoReportThread( br ); 
                art.setOverrideSendFreq(true);
                new Thread(art).start();                
            }
            
            else
                throw new APIException( 030, "Tran not recognized: " + tran, null );
            
            responseJob.add("status", "SUCCESS" );
        }        
        catch( APIException e )
        {
            String msg = "BatchRequestResource.postBatchReportRequest() APIException code=" + e.getCode() + ", msg=" + e.getMessage() + ", msg2=" + e.getMessage2() + ", batchReportId=" + batchReportId + ", tran=" + (tran==null ? "null" : tran) + ", content=" + jsonContent;
            
            LogService.logIt( e, msg );
            
            responseJob.add("status", "ERROR" );
            responseJob.add("errorcode", 100 );
            responseJob.add("errormessage", e.getMessage() );                        
            sendInternalNotificationEmail( "BatchRequestResource.postBatchReportRequest() API Error", msg );
        }        
        catch( Exception e )
        {
            String msg = "BatchRequestResource.postBatchReportRequest() AAA  batchReportId=" + batchReportId + ", tran=" + (tran==null ? "null" : tran) + ", content=" + jsonContent;
            LogService.logIt( e, msg );
            
            responseJob.add("status", "ERROR" );
            responseJob.add("errorcode", 100 );
            responseJob.add("errormessage", e.getMessage() );            
            sendInternalNotificationEmail( "BatchRequestResource.postBatchReportRequest() General Exception", msg );
            // return null;
        }
        
        try
        {
            return JsonUtils.convertJsonObjectToString(responseJob.build() );
        }
        catch( Exception e )
        {
            String msg = "BatchRequestResource.postBatchReportRequest() Error packaging response JO.  batchReportId=" + batchReportId + ", tran=" + (tran==null ? "null" : tran) + " content=" + jsonContent;
            LogService.logIt(e, msg );
            sendInternalNotificationEmail( "BatchRequestResource.postBatchReportRequest() Response Packaging Error", msg );
            return null;
        }        
    }
    
          
    
}
