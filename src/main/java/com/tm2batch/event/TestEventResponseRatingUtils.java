/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.event;


import com.tm2batch.entity.event.TestEvent;
import com.tm2batch.entity.event.TestEventResponseRating;
import com.tm2batch.score.TextAndTitle;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author miker_000
 */
public class TestEventResponseRatingUtils 
{
    private static final String UNKNOWN_FILE_STUB = "MiscUploadedFile";
    private static final String UNKNOWN_WRITING_STUB = "MiscText";
    private static final String NONCOMP_FILEUPLOAD_STUB = "UploadedFile";
    private static final String NONCOMP_SPEAKINGSAMPLE_STUB = "SpeakingSample";
    private static final String NONCOMP_AVUPLOAD_STUB = "AudioVideo";
    private static final String SCORED_AVUPLOAD_STUB = "ScoredAudioVideo";
    private static final String WRITING_SAMPLE_STUB = "WritingSample";
    private static final String SCORED_ESSAY_STUB = "ScoredEssay";
    
    
    
    
    
    private static TextAndTitle findTextAndTitleForSequenceId( List<TextAndTitle> ttl, int sequenceId )
    {
        for( TextAndTitle tt : ttl )
        {
            if( tt.getIntParam1()==sequenceId )
                return tt;
        }
        
        return null;        
    }
    
    private static TextAndTitle findTextAndTitleForUploadedUserFileId( List<TextAndTitle> ttl, long uploadedUserFileId )
    {
        for( TextAndTitle tt : ttl )
        {
            if( tt.getUploadedUserFileId()==uploadedUserFileId )
                return tt;
        }
        return null;
    }
    
    
   public static TestEventResponseRating getMatchingTestEventResponseRating( TestEvent te, 
                                                                             TestEventResponseRating terr ) throws Exception
   {
       if( te==null || terr==null || te.getTestEventResponseRatingList()==null || te.getTestEventId()!=terr.getTestEventId() )
           return null;
       
       return getTestEventResponseRating(   te, 
                                            terr.getUserId(), 
                                            terr.getTestEventResponseRatingTypeId(),
                                            terr.getUploadedUserFileId(),
                                            terr.getAvItemResponseId(),
                                            terr.getSimCompetencyId(),
                                            terr.getNonCompetencyItemTypeId(),
                                            terr.getSequenceId() );
   }

    
   public static TestEventResponseRating getTestEventResponseRating(    TestEvent te, 
                                                                        long userId, 
                                                                        int testEventResponseRatingTypeId,
                                                                        long uploadedUserFileId,
                                                                        long avItemResponseId,
                                                                        long simCompetencyId,
                                                                        int nonCompetencyItemTypeId,
                                                                        int sequenceId ) throws Exception
   {
       if( userId<=0 )
           throw new Exception( "TestEventResponseRatingUtils.getTestEventResponseRating() userId is required." );

       if( uploadedUserFileId<=0 && avItemResponseId<=0 && simCompetencyId<=0 && nonCompetencyItemTypeId<0 )
           throw new Exception( "TestEventResponseRatingUtils.getTestEventResponseRating() at least one identifier is required." );
           
       if( (simCompetencyId>0 || nonCompetencyItemTypeId>=0) && sequenceId<=0)
           throw new Exception( "TestEventResponseRatingUtils.getTestEventResponseRating() sequenceId is required when simCompetencyId or nonCompetencyItemTypeId are specified." );
       
       if( te.getTestEventResponseRatingList()==null || te.getTestEventResponseRatingList().isEmpty() )
           return null;
       
       List<TestEventResponseRating> tel = getTestEventResponseRatings( te.getTestEventResponseRatingList(),
                                                                      te.getTestEventId(),
                                                                      userId,
                                                                      testEventResponseRatingTypeId,
                                                                      uploadedUserFileId,
                                                                      avItemResponseId,
                                                                      simCompetencyId,
                                                                      nonCompetencyItemTypeId,
                                                                      sequenceId
                                                                    );
       return tel.isEmpty() ? null : tel.get(0);
   }
   
   public static List<TestEventResponseRating> getTestEventResponseRatings( List<TestEventResponseRating> inList,
                                                                     long testEventId,
                                                                     long userId,
                                                                     int testEventResponseRatingTypeId,
                                                                     long uploadedUserFileId,
                                                                     long avItemResponseId,
                                                                     long simCompetencyId,
                                                                     int nonCompetencyItemTypeId,
                                                                     int sequenceId
                                                                    )
   {
       List<TestEventResponseRating> out = new ArrayList<>();
       
       if( inList==null )
           return out;
       
       for( TestEventResponseRating te : inList )
       {
           if( testEventId>0 && te.getTestEventId()!=testEventId )
               continue;
           
           if( testEventResponseRatingTypeId>=0 && te.getTestEventResponseRatingTypeId()!=testEventResponseRatingTypeId )
               continue;
           
           if( uploadedUserFileId>0 && te.getUploadedUserFileId()!=uploadedUserFileId )
               continue;
           
           if( avItemResponseId>0 && te.getAvItemResponseId()!=avItemResponseId )
               continue;
           
           if( simCompetencyId>0 && te.getSimCompetencyId()!=simCompetencyId )
               continue;
           
           if( nonCompetencyItemTypeId>=0 && te.getNonCompetencyItemTypeId()!=nonCompetencyItemTypeId )
               continue;
           
           if( sequenceId>=0 && te.getSequenceId()!=sequenceId )
               continue;
           
           if( userId>0 && te.getUserId()!=userId )
               continue;
           
           out.add(te);
       }
       
       return out;
   }
   
   
   public static int getRatingCols(List<TestEventResponseRating> inList )
   {
       float[] vs = getAverageRating( inList );
       
       if( vs[2]>0 )
           return 3;
       if( vs[1]>0 )
           return 2;
       if( vs[0]>0 )
           return 1;
       return 0;
   }
   
   public static float[] getAverageRating( List<TestEventResponseRating> inList )
   {
       
       float ct = 0;
       float[] totals = new float[3]; // {-1,-1,-1};
       if( inList==null || inList.isEmpty() )
           return totals;
       
       for( TestEventResponseRating te : inList )
       {
           ct++;
           totals[0] += te.getRating();
           totals[1] += te.getRating2();
           totals[2] += te.getRating3();
       }
       
       if( ct>0 )
       {
           for( int i=0;i<3;i++ )
           {
               totals[i] /= ct;
           }
       }
       
       return totals;
       
   }
   
}
