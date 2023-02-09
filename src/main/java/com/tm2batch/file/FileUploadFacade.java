/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.file;

import com.tm2batch.entity.file.UploadedUserFile;
import com.tm2batch.global.STException;
import com.tm2batch.service.LogService;
import java.util.Date;
import java.util.List;
import jakarta.ejb.Stateless;
import javax.naming.InitialContext;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/**
 *
 * @author Mike
 */
@Stateless
// @PersistenceContext( name = "persistence/tm2", unitName = "tm2" )
public class FileUploadFacade
{

    @PersistenceContext( name = "persistence/tm2", unitName = "tm2" )
    EntityManager em;

    // private static EntityManagerFactory tm2Factory;

    // private static Random random = null;


    public static FileUploadFacade getInstance()
    {
        try
        {
            return (FileUploadFacade) InitialContext.doLookup( "java:module/FileUploadFacade" );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "FileUploadFacade.getInstance() " );

            return null;
        }
    }


    

    public List<UploadedUserFile> getUploadedUserFileListForTestKeyId( long testKeyId, int uploadedUserFileTypeId, long maxTestEventId) throws Exception
    {
        try
        {
            Query q =  em.createNamedQuery( maxTestEventId<=0 ? "UploadedUserFile.findByTestKeyIdAndUploadedUserFileType" : "UploadedUserFile.findByTestKeyIdAndUploadedUserFileTypeWithMaxTestEventId" , UploadedUserFile.class );

            q.setParameter( "testKeyId", testKeyId );
            q.setParameter("uploadedUserFileTypeId", uploadedUserFileTypeId);
            
            if(maxTestEventId>0)
                q.setParameter("maxTestEventId", maxTestEventId);
            
            q.setHint( "jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS );
            return q.getResultList();
        }

        catch( Exception e )
        {
            LogService.logIt(e, "FileUploadFacade.getUploadedUserFileListForTestKeyId() testKeyId=" + testKeyId );
            throw new STException( e );
        }        
    }
            
    public List<UploadedUserFile> getUploadedUserFileListForTestEventId( long testEventId, int uploadedUserFileTypeId) throws Exception
    {
        try
        {
            Query q =  em.createNamedQuery("UploadedUserFile.findByTestEventIdAndUploadedUserFileType" , UploadedUserFile.class );

            q.setParameter( "testEventId", testEventId );
            q.setParameter("uploadedUserFileTypeId", uploadedUserFileTypeId);
            q.setHint( "jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS );
            return q.getResultList();
        }

        catch( Exception e )
        {
            LogService.logIt(e, "FileUploadFacade.getUploadedUserFileListForTestEventId() testEventId=" + testEventId );
            throw new STException( e );
        }        
    }
    
    
    
    public UploadedUserFile saveUploadedUserFile( UploadedUserFile uuf ) throws Exception
    {
        try
        {
            // 	LogService.logIt( "FileUploadFacade. saving UploadedUserFile "  );
            if( uuf.getTestEventId() == 0 )
                throw new Exception( "testEventId=0" );

            UploadedUserFileType uuft = UploadedUserFileType.getValue( uuf.getUploadedUserFileTypeId() );
            
            if( uuft.getIsResponse() )
            {
                if( uuf.getActId() == 0 )
                    throw new Exception( "actId=0" );

                if( uuf.getNodeSeq() == 0 )
                    throw new Exception( "nodeSeq=0" );

                if( uuf.getSubnodeSeq() == 0 )
                    throw new Exception( "subnodeSeq=0" );
            }
            
            if( uuf.getCreateDate() == null )
                uuf.setCreateDate( new Date() );

            if( uuf.getLastUpload() == null )
                uuf.setLastUpload( new Date() );

            if( uuf.getUploadedUserFileId() > 0 )
                em.merge( uuf );

            else
                em.persist( uuf );

            em.flush();
        }

        catch( Exception e )
        {
            LogService.logIt( e, "FileUploadFacade.saveUploadedUserFile() " + uuf.toString() );
            throw new STException( e );
        }

        return uuf;
    }        
    
}
