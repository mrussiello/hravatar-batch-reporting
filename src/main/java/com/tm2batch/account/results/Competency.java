/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tm2batch.account.results;


import com.tm2batch.entity.event.TestEventScore;
import com.tm2batch.entity.profile.Profile;
import com.tm2batch.event.ScoreCategoryType;
import com.tm2batch.event.ScoreFormatType;
import com.tm2batch.global.Constants;
import static com.tm2batch.profile.ProfileUtils.parseBaseColorStr;
import com.tm2batch.score.TextAndTitle;
import com.tm2batch.service.LogService;
import com.tm2batch.sim.SimCompetencyClass;
import com.tm2batch.util.I18nUtils;
import com.tm2batch.util.STStringTokenizer;
import com.tm2batch.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Mike
 */
public class Competency {

    static int MARKER_WIDTH = 7;
    static float MARKER_LEFT_ADJ = 3.5f;

    int totalPix;
    TestEventScore testEventScore;
    List<ScoreCategoryRange> scoreCategoryRangeList;
    Locale locale;
    String name;
    String scoreName;
    int scoreDigits = -1;
    Profile profile;
    boolean useRawScore = false;
    boolean useScoreTextForNum = false;
    boolean showColorGraph = true;
    String strParam1;
    

    public Competency( TestEventScore tes, Profile profile, int totalPixWid, int scoreDigits, Locale locale, boolean useRawScore, boolean useScoreTextForNum)
    {
        this.testEventScore = tes;
        totalPix = totalPixWid;
        this.locale = locale;
        this.profile = profile;
        this.useRawScore=useRawScore;
        this.scoreDigits = scoreDigits;
        this.useScoreTextForNum = useScoreTextForNum;

        scoreCategoryRangeList = testEventScore.getScoreCatInfoList( totalPixWid );

        // LogService.logIt( "Competency() tes=" + tes.getName() + ", scoreCategoryRangeList=" + scoreCategoryRangeList.size() );
        
        finalizeScoreCatRangeValues();
    }

    public Competency( TestEventScore tes, int totalPixWid, Locale locale, String scoreNm )
    {
        this.testEventScore = tes;
        totalPix = totalPixWid;
        this.locale = locale;
        this.scoreName=scoreNm;

        scoreCategoryRangeList = testEventScore.getScoreCatInfoList(totalPixWid);
        
        finalizeScoreCatRangeValues();
    }

    public boolean getHasScoredChatResponses()
    {
        if( !testEventScore.getSimCompetencyClass().equals( SimCompetencyClass.SCOREDCHAT) )
            return false;
        
        List<ChatResponseSet> pl = getScoredChatResponseSetList();
        
        return pl!=null && !pl.isEmpty();        
    }
    
    
    public List<ChatResponseSet> getScoredChatResponseSetList()
    {
        if( !testEventScore.getSimCompetencyClass().equals( SimCompetencyClass.SCOREDCHAT) )
            return null;
        
        List<TextAndTitle> ttl = testEventScore.getTextBasedResponseList(null, -1, false, false, false, false );
        
        if( ttl==null || ttl.isEmpty() )
            return null;
        
        List<ChatResponseSet> out = new ArrayList<>();
        
        ChatResponseSet crs;
        
        for( TextAndTitle tt : ttl )
        {
            crs = new ChatResponseSet( tt );
            
            if( crs.getHasData() )
                out.add(crs);
        }
        
        return out.isEmpty() ? null : out;
    }
    
    
    public boolean getHasCaveats()
    {
        if( testEventScore.getSimCompetencyClass().equals( SimCompetencyClass.SCOREDDATAENTRY) || testEventScore.getSimCompetencyClass().equals( SimCompetencyClass.SCOREDTYPING ) || testEventScore.getSimCompetencyClass().equals( SimCompetencyClass.SCOREDCHAT ) )
        {
            List<String> cl = getCaveatList();
            
            return cl != null && !cl.isEmpty();            
        }
        
        return false;
    }
    
    public List<String> getCaveats()
    {
        List<String> out = new ArrayList<>();
        
        if( testEventScore.getSimCompetencyClass().equals( SimCompetencyClass.SCOREDDATAENTRY) || testEventScore.getSimCompetencyClass().equals( SimCompetencyClass.SCOREDTYPING ) || testEventScore.getSimCompetencyClass().equals( SimCompetencyClass.SCOREDCHAT ) )
        {
            List<String> cl = getCaveatList();
            
            for( String c : cl )
            {
                if( c!=null && !c.trim().isEmpty() && !c.trim().startsWith( Constants.TOPIC_KEY + "~" )  )
                    out.add( "- " + c);
            }
        }
        
        return out;        
    }
    
    public String getScore4Show( )
    {
        return getScore4Show( false );
    }    

    public String getRawScore4Show( )
    {
        return getScore4Show( true );
    }    
    
    public String getScore4Show( boolean raw )
    {
        if( useScoreTextForNum )
        {
            if( testEventScore==null )
                return "";
            
            String s = ReportUtils.getScoreValueFromStr( testEventScore.getScoreText() );
            
            if( s==null || s.length()>Constants.MAX_STR_LEN_FOR_SCORE_TEXT_AS_NUMSCORE )
                s = "";
            return s;
            // return testEventScore!=null ? ReportUtils.getScoreValueFromStr( testEventScore.getScoreText() ) : "";
        }
        
        int scrDigits = scoreDigits>=0 ? scoreDigits : ScoreFormatType.getValue( this.testEventScore.getScoreFormatTypeId() ).getScorePrecisionDigits();
        
        return I18nUtils.getFormattedNumber( locale, raw ? testEventScore.getRawScore() : testEventScore.getScore(), scrDigits);        
    }
    
    
    public String getScoreCategoryColorRgb()
    {
        if( profile==null || profile.getStrParam3()==null || profile.getStrParam3().trim().isEmpty() )
        {
            return testEventScore.getScoreCategoryType().getColorRgb();
        }
        
        String[] cols = parseBaseColorStr( profile.getStrParam3() );
        
        return testEventScore.getScoreCategoryType().getColorRgb( cols );        
    }
    
            

    public boolean getHasTopicScores()
    {
        // LogService.logIt( "Competency.getHasTopicScores() AAA " + testEventScore.getName() );
        
        if( testEventScore.getSimCompetencyClass().equals( SimCompetencyClass.KNOWLEDGE) || testEventScore.getSimCompetencyClass().equals( SimCompetencyClass.CORESKILL ) || testEventScore.getSimCompetencyClass().equals( SimCompetencyClass.SCOREDCHAT ) )
        {
            List<String> cl = getCaveatList();
            
            // LogService.logIt( "Competency.getHasTopicScores() BBB " + testEventScore.getName() + ", cl=" + cl.size() );
        
            if( cl == null || cl.isEmpty() )
                return false;
            
            for( String c : cl )
            {
                // LogService.logIt( "Competency.getHasTopicScores() CCC " + testEventScore.getName() + ", c=" + c );
                if( c.startsWith( Constants.TOPIC_KEY + "~" ) )
                    return true;
            }
        }
        
        return false;
    }
    
    public List<String[]> getTopicScores()
    {
        if( testEventScore.getSimCompetencyClass().equals( SimCompetencyClass.KNOWLEDGE) || testEventScore.getSimCompetencyClass().equals( SimCompetencyClass.CORESKILL ) || testEventScore.getSimCompetencyClass().equals( SimCompetencyClass.SCOREDCHAT ) )
        {
            List<String> cl = getCaveatList();
            
            return ReportUtils.getParsedTopicScores(cl, locale, testEventScore.getSimCompetencyClassId() );
        }
        
        return new ArrayList<>();        
    }
    
    
    public List<String> getCaveatList()
    {
        List<String> out = new ArrayList<>();

        String tpm1 = testEventScore.getTextParam1();
        
        if( tpm1 == null || tpm1.isEmpty() )
            return out;

        String cl = StringUtils.getBracketedArtifactFromString( tpm1 , Constants.CAVEATS );

        if( cl == null || cl.isEmpty() )
            return out;

        STStringTokenizer st = new STStringTokenizer( cl , Constants.DELIMITER );

        String t;

        while( st.hasMoreTokens() )
        {
            t = st.nextToken();

            t = t.trim();

            if( t.isEmpty() )
                continue;

            out.add( t );
        }

        return out;
    }

    public boolean getShowColorGraph() {
        return showColorGraph;
    }

    public void setShowColorGraph(boolean showColorGraph) {
        this.showColorGraph = showColorGraph;
    }
    
    
    /*
    public String getColorGraphUrl()
    {
        StringBuilder sb = new StringBuilder();

        try
        {
            ScoreCategoryType sct = testEventScore.getScoreCategoryType();

            ScoreFormatType sft = testEventScore.getScoreFormatType();
            
            StringBuilder imgUrl = new StringBuilder();

            int ptrPos = getPointerLeft();

            String pflPrms = "";
            String custClrs = "";

            // if( !testEventScore.getTestEventScoreType().equals( TestEventScoreType.OVERALL) )
            if( scoreCategoryRangeList!=null && !scoreCategoryRangeList.isEmpty() )
            {
                for( ScoreCategoryRange scr : scoreCategoryRangeList )
                {
                    if( imgUrl.length()>0 )
                        imgUrl.append( "," );

                    imgUrl.append( getRangeColor( scr.getRangeColor() ) + scr.getAdjustedRangePix(testEventScore.getScoreFormatType()) );
                }
            }

            else
                pflPrms += "&tw=" + ( Constants.CT2_COLORGRAPHWID );

            if( testEventScore.getProfileBoundaries() != null )
            {
                ScoreCategoryRange scr = new ScoreCategoryRange( sct.getScoreCategoryTypeId(), testEventScore.getProfileBoundaries()[0], testEventScore.getProfileBoundaries()[1], Constants.CT2_COLORGRAPHWID );

                // pflPrms += "&prl=" + tes.getProfileBoundaries()[0] + "&prh=" + tes.getProfileBoundaries()[1];
                pflPrms += "&prl=" + scr.getMinPix( testEventScore.getScoreFormatType() ) + "&prh=" + ( scr.getMinPix( testEventScore.getScoreFormatType() ) + scr.getAdjustedRangePix( testEventScore.getScoreFormatType() ) );
            }
            
            if( profile!=null && profile.getStrParam3()!=null && !profile.getStrParam3().isEmpty() )
                custClrs = "&cs=" + profile.getStrParam3().trim();

            if( testEventScore.getScoreFormatTypeId() != ScoreFormatType.NUMERIC_0_TO_100.getScoreFormatTypeId() )
                pflPrms += "&sft=" + testEventScore.getScoreFormatTypeId();
            
            sb.append( "/ta/ct2scorechart/" + testEventScore.getTestEventScoreIdEncrypted()  + ".png?ss=" + imgUrl.toString() + "&p=" + ptrPos + pflPrms + custClrs );

        }

        catch( Exception e )
        {
            LogService.logIt( "Competency.getColorGraphUrl() " + ( testEventScore == null ? "TestEventScore is null" : testEventScore.toString() ) );
        }

        return sb.toString();
    }
    */

    /**
     * Used to adjust all range pix values to equal total pix by adjusting the last one.
     */
    private void finalizeScoreCatRangeValues()
    {
        if( scoreCategoryRangeList.isEmpty() )
            return;

        int pix = 0;

        for( ScoreCategoryRange scr : scoreCategoryRangeList )
        {
            pix += scr.getRangePix( testEventScore.getScoreFormatType() );
        }

        int adj = totalPix - pix;

        ScoreCategoryRange last = scoreCategoryRangeList.get( scoreCategoryRangeList.size()-1 );

        last.setAdjRange(adj);
    }

    public int getPointerLeft()
    {
        ScoreFormatType sft = testEventScore.getScoreFormatType();
        
        float scr = useRawScore ? testEventScore.getOverallRawScoreToShow() : testEventScore.getScore();
        
        if( totalPix <= 0 || testEventScore==null || scr<=sft.getMin() )
            return 0 - Math.round(MARKER_LEFT_ADJ);

        if( scr>=sft.getMax() )
            return totalPix - Math.round(MARKER_LEFT_ADJ);

        int specAdj = scr<=0 ? 1 : 0;

        return Math.round(((float)totalPix)*((scr-sft.getMin())/(sft.getMax()-sft.getMin()))) - Math.round(MARKER_LEFT_ADJ) + specAdj;
    }

    public String getScoreCategoryStarsFilename()
    {
        if( testEventScore == null )
            return null;

        ScoreCategoryType sct = testEventScore.getScoreCategoryType();

        if( sct.getIsRed() )
            return "report_detail_stars_1.png";

        else if( sct.getIsRedYellow() )
            return "report_detail_stars_2.png";

        else if( sct.getIsYellow() )
            return "report_detail_stars_3.png";

        else if( sct.getIsYellowGreen() )
            return "report_detail_stars_4.png";

        else if( sct.getIsGreen() )
            return "report_detail_stars_5.png";

        else
            return "report_detail_stars_0.png";


    }

    /**
     * USed to get color code for image generator.
     * @param color
     * @return
     */
    private String getRangeColor( String color )
    {
        if( color.equals("#ff8f8f") )
            return "r";
        else if( color.equals("#ffe78f") )
            return "ry";
        else if( color.equals("#ffff8f") )
            return "y";
        else if( color.equals("#eaff8f") )
            return "yg";
        else if( color.equals("#a1ff8f") )
            return "g";

        // Yellow
        return "y";
    }




    public TestEventScore getTestEventScore() {
        return testEventScore;
    }

    public void setTestEventScore(TestEventScore testEventScore) {
        this.testEventScore = testEventScore;
    }

    public List<ScoreCategoryRange> getScoreCategoryRangeList() {
        return scoreCategoryRangeList;
    }

    public void setScoreCategoryRangeList(List<ScoreCategoryRange> scoreCategoryRangeList) {
        this.scoreCategoryRangeList = scoreCategoryRangeList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScoreName() {
        return scoreName;
    }

    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }

    public int getScoreDigits() {
        return scoreDigits;
    }

    public void setScoreDigits(int scoreDigits) {
        this.scoreDigits = scoreDigits;
    }

    public boolean isUseRawScore() {
        return useRawScore;
    }

    public String getStrParam1() {
        return strParam1;
    }

    public void setStrParam1(String strParam1) {
        this.strParam1 = strParam1;
    }



}
