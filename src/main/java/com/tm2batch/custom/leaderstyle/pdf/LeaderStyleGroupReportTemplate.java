 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.leaderstyle.pdf;

import com.tm2batch.global.STException;
import com.tm2batch.pdf.ReportTemplate;
import com.tm2batch.service.LogService;



/**
 * CoreTest Selection Report.
 *
 * @author Mike
 */
public class LeaderStyleGroupReportTemplate extends BaseLeaderStyleGroupReportTemplate implements ReportTemplate
{    
    public LeaderStyleGroupReportTemplate()
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
            
            //addDiscStylesExplained();
            
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
            
            addInterpretationSection();

            if( currentYLevel > 250 )
                addFooterBar( lmsg( "g.Interpretation").toUpperCase(), true, fontXLargeBoldWhite );
            
            addNewPage();
            
            addPurposeOfAssessment();
                        
            addFooterBar( lmsg( "g.Purpose").toUpperCase(), true, fontXLargeBoldWhite );
                        
            
            addNewPage();
            
            addLeaderStyleStylesExplained();

            //if( currentYLevel > 250 )
            //    addFooterBar( lmsg_spec( "ls.StylesExpl").toUpperCase(), true, fontXLargeBoldWhite );

            //addNewPage();
                        
            addCitationsSection();
                        
            addMinimalPrepNotesSection();            
            
            addNotesSection();

            addFooterBar( lmsg( "g.Notes").toUpperCase(), false, fontXLargeBoldWhite );
                        
            closeDoc();

            return getDocumentBytes();
        }

        catch( STException e )
        {
            throw e;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "LeaderStyleGroupReportTemplate.generateReport() " );
            throw new STException( e );
        }
    }

}
