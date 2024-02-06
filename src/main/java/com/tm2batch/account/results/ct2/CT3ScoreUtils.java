/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.account.results.ct2;


import com.tm2batch.account.results.Competency;
import com.tm2batch.account.results.CompetencyGroup;
import com.tm2batch.account.results.ItemResponseGroup;
import com.tm2batch.account.results.PercentileEntry;
import com.tm2batch.entity.event.TestEvent;
import com.tm2batch.entity.event.TestEventScore;
import com.tm2batch.event.ScoreFormatType;
import com.tm2batch.event.TestEventScoreType;
import com.tm2batch.global.Constants;
import com.tm2batch.ibmcloud.InsightReportTrait;
import com.tm2batch.score.TextAndTitle;
import com.tm2batch.service.LogService;
import com.tm2batch.sim.SimCompetencyClass;
import com.tm2batch.sim.SimCompetencyGroupType;
import com.tm2batch.sim.SimCompetencyVisibilityType;
import com.tm2batch.simlet.CompetencyScoreType;
import com.tm2batch.util.MessageFactory;
import com.tm2batch.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Mike
 */
public class CT3ScoreUtils {

    public static List<String> getCt3RiskFactorsList( TestEvent te )
    {
        List<String> out = new ArrayList<>();

        if( te == null )
            return out;

        TestEventScore tes = te.getOverallTestEventScore();

        if( tes == null || tes.getTextbasedResponses()==null || tes.getTextbasedResponses().isEmpty() )
            return out;

        List<TextAndTitle> ttl = tes.getTextBasedResponseList(CT3Constants.CT3RISKFACTORS, -1, false, false, false, false );

        ttl.addAll(tes.getTextBasedResponseList(CT3Constants.STD_RISKFACTORS, -1, false, false, false, false ) );

        String rftxt;

        for( TextAndTitle tt : ttl )
        {
            rftxt = tt.getText();

            if( rftxt.indexOf( "[FACET]" ) >= 0 )
                rftxt = rftxt.substring(0, rftxt.indexOf( "[FACET]" ) );

            out.add( rftxt );
        }

        return out;

    }
    
    public static void setIbmInsightGroupData( TestEvent te, Locale locale ) throws Exception
    {
        try
        {
            if( te==null || !te.getShowIbmInsightResults() )
                return;
            
            TestEventScore tes = te.getOverallTestEventScore();
            
            List<TextAndTitle> ttl = tes.getTextBasedResponseList(Constants.IBMINSIGHT, -1, false, false, false, false );
            
            if( ttl==null || ttl.isEmpty() )
                return;
            
            List<InsightReportTrait> irtl = new ArrayList<>();
            
            InsightReportTrait irt;
            
            for( TextAndTitle tt : ttl )
            {
                irt = new InsightReportTrait( tt );
                
                if( !irt.getIsValid() )
                    continue;
                
                irtl.add(irt);
            }
            
            if( irtl.isEmpty() )
                return;
            
            te.setInsightReportTraitList(irtl);
        }
        catch( Exception e )
        {
            LogService.logIt( e, "CT3ScoreUtils.setItemRsetIbmInsightGroupDataesponseGroupData() testEventId=" + te.getTestEventId() );
        }
    }

    public static void setItemResponseGroupData( TestEvent te, Locale locale, boolean forExcel) throws Exception
    {
        if( te.getItemResponseGroupList()!=null || !te.getShowItemScores()  )
            return;        
            
        te.setItemResponseGroupList(computeItemResponseGroupData( te, locale, forExcel));
    }    

    public static List<ItemResponseGroup> computeItemResponseGroupData( TestEvent te, Locale locale, boolean forExcel) throws Exception
    {
        if( !te.getShowItemScores()  )
            return null;
        
        List<ItemResponseGroup> irgl = new ArrayList<>();
        
        if( locale==null )
            locale = Locale.US;

        try
        {

            List<TestEventScore> tesl = te.getTestEventScoreList( TestEventScoreType.COMPETENCY );

            te.setItemResponseGroupList(irgl);
            
            if( !tesl.isEmpty() )
            {
                String cp;
                for( TestEventScore tes : tesl )
                {
                    if( forExcel && SimCompetencyVisibilityType.getValue( tes.getHide()).getHideItemScoresExcel() )
                        continue;

                    if( !forExcel && SimCompetencyVisibilityType.getValue( tes.getHide()).getHideItemScores() )
                        continue;
                    
                    if( tes.getIntParam2()<=0 )
                        continue;
                    if( tes.getTextParam1()==null || tes.getTextParam1().isEmpty() )
                        continue;
                    cp = StringUtils.getBracketedArtifactFromString(tes.getTextParam1(), Constants.ITEMSCOREINFO );
                    
                    if( cp==null || cp.trim().isEmpty() )
                        continue;
                    
                    irgl.add( new ItemResponseGroup( tes, locale ) );
                }
            }
            
            // te.setItemResponseGroupList(irgl);
        }
        catch( Exception e )
        {
            LogService.logIt(e, "CT3ScoreUtils.setItemResponseGroupData() NONFATAL RETURNING NULL testEventId=" + te.getTestEventId() );
            return null;
        }
        
        return irgl;        
    }    
    
    
            
    public static void setCompetencyGroupData( TestEvent te, Locale locale ) throws Exception
    {
        List<CompetencyGroup> cgl = new ArrayList<>();
        CompetencyGroup cg;
        List<Competency> cl;
        Competency comp;
        
        int scoreDigits;
        
        String title;
        
        

        List<TestEventScore> ctesl = te.getScoredAbilityTestEventScoreList();
        if( !ctesl.isEmpty() )
        {
            cl = new ArrayList<>();
            for( TestEventScore tes : ctesl )
            {
                scoreDigits = te.getReport()!=null && te.getReport().getIntParam3()>=0 ? te.getReport().getIntParam3() : ScoreFormatType.getValue( tes.getScoreFormatTypeId() ).getScorePrecisionDigits();
                        
                cl.add(new Competency( tes, te.getReportRangeProfile(), Constants.CT2_COLORGRAPHWID, scoreDigits, locale, false, te.getUseScoreText4CompetencyNumeric() ) );
            }
            
            title = te.getReport()!=null ? te.getReport().getReportFlagValueAsString( "competencygrouptitle" + SimCompetencyGroupType.ABILITY.getSimCompetencyGroupTypeId() ) : null;
            if( title==null )
                title = MessageFactory.getStringMessage(locale, "g.CogAbilities" , null);

            cg = new CompetencyGroup( title, cl, SimCompetencyClass.ABILITY.getSupportsPercentiles() );
            cgl.add( cg );
        }

        ctesl = te.getScoredKsTestEventScoreList();
        if( !ctesl.isEmpty() )
        {
            cl = new ArrayList<>();
            for( TestEventScore tes : ctesl )
            {
                scoreDigits = te.getReport()!=null && te.getReport().getIntParam3()>=0 ? te.getReport().getIntParam3() : ScoreFormatType.getValue( tes.getScoreFormatTypeId() ).getScorePrecisionDigits();
                cl.add(new Competency( tes, te.getReportRangeProfile(), Constants.CT2_COLORGRAPHWID, scoreDigits, locale, false, te.getUseScoreText4CompetencyNumeric() ) );
            }
            
            title = te.getReport()!=null ? te.getReport().getReportFlagValueAsString( "competencygrouptitle" + SimCompetencyGroupType.SKILLS.getSimCompetencyGroupTypeId() ) : null;
            if( title==null )
                title = MessageFactory.getStringMessage(locale, "g.KSs" , null);

            cg = new CompetencyGroup( title, cl, SimCompetencyClass.KNOWLEDGE.getSupportsPercentiles() );
            cgl.add( cg );
        }

        ctesl = te.getScoredAimsTestEventScoreList();
        if( !ctesl.isEmpty() )
        {
            cl = new ArrayList<>();
            for( TestEventScore tes : ctesl )
            {
                scoreDigits = te.getReport()!=null && te.getReport().getIntParam3()>=0 ? te.getReport().getIntParam3() : ScoreFormatType.getValue( tes.getScoreFormatTypeId() ).getScorePrecisionDigits();
                cl.add(new Competency( tes, te.getReportRangeProfile(), Constants.CT2_COLORGRAPHWID, scoreDigits, locale, false, te.getUseScoreText4CompetencyNumeric() ) );
            }
            
            title = te.getReport()!=null ? te.getReport().getReportFlagValueAsString( "competencygrouptitle" + SimCompetencyGroupType.PERSONALITY.getSimCompetencyGroupTypeId() ) : null;
            if( title==null )
                title = MessageFactory.getStringMessage(locale, "g.AIMs" , null);

            cg = new CompetencyGroup( title, cl, SimCompetencyClass.NONCOGNITIVE.getSupportsPercentiles() );
            cgl.add( cg );
        }

        ctesl = te.getScoredEqTestEventScoreList();
        if( !ctesl.isEmpty() )
        {
            cl = new ArrayList<>();
            for( TestEventScore tes : ctesl )
            {
                scoreDigits = te.getReport()!=null && te.getReport().getIntParam3()>=0 ? te.getReport().getIntParam3() : ScoreFormatType.getValue( tes.getScoreFormatTypeId() ).getScorePrecisionDigits();
                cl.add(new Competency( tes, te.getReportRangeProfile(), Constants.CT2_COLORGRAPHWID, scoreDigits, locale, false, te.getUseScoreText4CompetencyNumeric() ) );
            }

            title = te.getReport()!=null ? te.getReport().getReportFlagValueAsString( "competencygrouptitle" + SimCompetencyGroupType.EQ.getSimCompetencyGroupTypeId() ) : null;
            if( title==null )
                title = MessageFactory.getStringMessage(locale, "g.EQs" , null);

            cg = new CompetencyGroup( title, cl, SimCompetencyClass.EQ.getSupportsPercentiles() );
            cgl.add( cg );
        }
        
        
        ctesl = te.getScoredBiodataTestEventScoreList();
        if( !ctesl.isEmpty() )
        {
            cl = new ArrayList<>();
            boolean scrdSurvey = te.getBooleanFlagValue( "biodataisscoredsurvey" );
            
            for( TestEventScore tes : ctesl )
            {
                scoreDigits = te.getReport()!=null && te.getReport().getIntParam3()>=0 ? te.getReport().getIntParam3() : ScoreFormatType.getValue( tes.getScoreFormatTypeId() ).getScorePrecisionDigits();
                
                comp = new Competency( tes, te.getReportRangeProfile(), Constants.CT2_COLORGRAPHWID, scoreDigits, locale, false, te.getUseScoreText4CompetencyNumeric() ) ;                
                comp.setShowColorGraph( showColorGraphForTes( te, tes ) );                
                cl.add( comp );
            }
            
            String key = scrdSurvey ? "g.ScoredSurvey" : "g.BiographicaData";
            
            title = te.getReport()!=null ? te.getReport().getReportFlagValueAsString( "competencygrouptitle" + SimCompetencyGroupType.BIODATA.getSimCompetencyGroupTypeId() ) : null;
            if( title==null )
                title = MessageFactory.getStringMessage(locale, key , null);

            cg = new CompetencyGroup( title, cl, SimCompetencyClass.SCOREDBIODATA.getSupportsPercentiles() );
            cgl.add( cg );
        }
        
        ctesl = te.getScoredAiTestEventScoreList();
        if( !ctesl.isEmpty() )
        {
            cl = new ArrayList<>();
            for( TestEventScore tes : ctesl )
            {
                scoreDigits = te.getReport()!=null && te.getReport().getIntParam3()>=0 ? te.getReport().getIntParam3() : ScoreFormatType.getValue( tes.getScoreFormatTypeId() ).getScorePrecisionDigits();
                cl.add(new Competency( tes, te.getReportRangeProfile(), Constants.CT2_COLORGRAPHWID, scoreDigits, locale, false, te.getUseScoreText4CompetencyNumeric() ) );
            }
            cg = new CompetencyGroup( MessageFactory.getStringMessage(locale, "g.AIs" , null), cl, SimCompetencyClass.VOICE_PERFORMANCE_INDEX.getSupportsPercentiles() );
            cgl.add( cg );
        }
        

        te.setCompetencyGroupList(cgl);

        TestEventScore otes = te.getOverallTestEventScore();

        if( otes != null && (otes.getHasValidNorms() || otes.getHasValidOverallZScoreNorm()))
        {
            List<PercentileEntry> pl = new ArrayList<>();

            if( otes.getHasValidOverallNorm() )
                pl.add( new PercentileEntry( MessageFactory.getStringMessage( locale , "g.AllTestTakers" , null), otes.getPercentile(), otes.getOverallPercentileCount(), Constants.CT2_PERCENTILEBARWIDTH, locale ) );
            else if( otes.getHasValidOverallZScoreNorm() )
            {
                PercentileEntry pe = new PercentileEntry( MessageFactory.getStringMessage( locale , "g.AllTestTakersApprox" , null), otes.getOverallZScorePercentile(), 0, Constants.CT2_PERCENTILEBARWIDTH, locale );
                pe.setApprox(true);
                pl.add( pe );// new PercentileEntry( MessageFactory.getStringMessage( locale , "g.AllTestTakersApprox" , null), otes.getOverallZScorePercentile(), 0, Constants.CT2_PERCENTILEBARWIDTH, locale ) );
            }
            
            if( otes.getHasValidCountryNorm() )
                pl.add( new PercentileEntry( te.getPercentileOrIpCountryName(), otes.getCountryPercentile(), otes.getCountryPercentileCount(), Constants.CT2_PERCENTILEBARWIDTH, locale ) );

            if( otes.getHasValidAccountNorm() )
                pl.add( new PercentileEntry( te.getOrg().getName(), otes.getAccountPercentile(), otes.getAccountPercentileCount(), Constants.CT2_PERCENTILEBARWIDTH, locale ) );

            if( pl.size()>0 )
                pl.get(pl.size()-1).setLast(true);

            otes.setPercentileEntryList(pl);
        }

    }

    
    public static boolean showColorGraphForTes( TestEvent te, TestEventScore tes )
    {
        SimCompetencyClass scc = tes.getSimCompetencyClass();
        
        //if( scc.isUnscored() )
        //    return false;
                
        if( !scc.isBiodata()|| !te.getBooleanFlagValue("biodataisscoredsurvey" ) )
            return true;
        
        //need competency type - percent of total is ok.
        CompetencyScoreType cst = CompetencyScoreType.getValue( tes.getScoreTypeIdUsed() );
        
        // cold be scroed as percent of total. This is OK for graph. 
        if( cst.isPercentOfTotal() )
            return true;
        
        // Could be normed - would have a STD. This is ok. 
        if( tes.getStdDeviation()>0 )
            return true;
        
        return false;        
    }
    
    
    
}
