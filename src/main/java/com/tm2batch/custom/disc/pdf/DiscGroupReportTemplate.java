 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.disc.pdf;

import com.tm2batch.global.STException;
import com.tm2batch.pdf.ReportTemplate;
import com.tm2batch.service.LogService;



/**
 * CoreTest Selection Report.
 *
 * @author Mike
 */
public class DiscGroupReportTemplate extends BaseDiscGroupReportTemplate implements ReportTemplate
{    
    public DiscGroupReportTemplate()
    {
        super();
    }


    @Override
    public byte[] generateReport() throws Exception
    {
        
        
        try
        {
            addCoverPage(true);

            addNewPage();
            
            addReportInfoHeader();   
            
            addDiscStylesExplained();
            
            addFooterBar( lmsg( "g.ScoreBreakdown").toUpperCase(), true, fontXLargeBoldWhite );
                        
            addNewPage();
            
            // table of name, Trait-ScoreName, D, I, S, C
            addScoreTable( 0 );
            
            if( currentYLevel > 250 )
                addFooterBar( lmsg( "g.Scores").toUpperCase(), true, fontXLargeBoldWhite );
                        
            addNewPage();
                        
            // table of name, Trait-ScoreName, D, I, S, C
            addScoreTable( 1 );
            
            if( currentYLevel > 250 )
                addFooterBar( lmsg( "g.Scores").toUpperCase(), true, fontXLargeBoldWhite );
                        
            addNewPage();
            
            addDiscEducationSection();

            addFooterBar( lmsg( "g.LearnMore").toUpperCase(), true, fontXLargeBoldWhite );
                
            addNewPage();            
            
            addKeyActionsToTakeSection();
            
            addFooterBar( lmsg( "g.KeyActions").toUpperCase(), false, fontXLargeBoldWhite );
                
            addNewPage();            

            addDiscBuildYourTeamSection();
                        
            addFooterBar( lmsg( "g.Activities").toUpperCase(), true, fontXLargeBoldWhite );
            
            addNewPage();
            
            addHowBuildTeamsWithDiscSection();

            addBlueBar();
            
            addAvoidSterotypingSection();
            
            addFooterBar( lmsg( "g.NextSteps").toUpperCase(), false, fontXLargeBoldWhite );

            addNewPage();
            
                        
            addNotesSection();

            addFooterBar( lmsg( "g.Notes").toUpperCase(), false, fontXLargeBoldWhite );
                        
            addMinimalPrepNotesSection();            
            
            closeDoc();

            return getDocumentBytes();
        }

        catch( STException e )
        {
            throw e;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "UMinnJusticeReportForOrg.DiscGroupReport() " );

            throw new STException( e );
        }
    }

}
