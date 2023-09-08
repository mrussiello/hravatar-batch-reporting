/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.account.results;


import com.tm2batch.entity.event.TestEventScore;
import com.tm2batch.global.Constants;
import com.tm2batch.score.TextAndTitle;
import com.tm2batch.sim.IncludeItemScoresType;
import com.tm2batch.sim.SimCompetencyClass;
import com.tm2batch.util.MessageFactory;
import com.tm2batch.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author miker_000
 */
public class ItemResponseGroup {
    
    TestEventScore tes;
    Locale loc;
    List<TextAndTitle> textAndTitleList;
    IncludeItemScoresType iist;
    String scoreKeyInfo;
    
    public ItemResponseGroup( TestEventScore t, Locale l ) throws Exception
    {
        this.tes = t;
        this.loc = l;
        
        if( tes==null )
            throw new Exception( "ItemResponseGroup() TestEventScore is null!" );
            
        if( !tes.getTestEventScoreType().getIsCompetency() )
            throw new Exception( "ItemResponseGroup() TestEventScore is not a competency type. " + tes.toString() );
        
        if( loc==null )
            loc = Locale.US;
        iist = IncludeItemScoresType.getValue( tes.getIntParam2() );
                
        if( iist.isNone() )
            throw new Exception( "ItemResponseGroup() TestEventScore.intParam2 IncludeItemScoresType is invalid for an ItemResponseGroup. " + tes.toString() );
        
        if( tes.getTextParam1()==null || tes.getTextParam1().isEmpty() )
            throw new Exception( "ItemResponseGroup() TestEventScore contains no item response data. " + tes.toString() );
        
        this.scoreKeyInfo = iist.getScoreKeyInfo( loc, SimCompetencyClass.getValue( tes.getSimCompetencyClassId() ) );
    }
    
    public String getNameWithInfo()
    {        
        return MessageFactory.getStringMessage(loc, "g.ItemScoresCompetencyTitle", new String[]{tes.getName(), iist.getName4Reports(loc) } );
    }
    
    
    public List<TextAndTitle> getTextTitleList()
    {
        if( textAndTitleList !=null )
            return textAndTitleList;
        
        
        textAndTitleList = new ArrayList<>();
        
        if( tes==null || tes.getTextParam1()==null || tes.getTextParam1().isEmpty() )
            return textAndTitleList;
        
        String cp = StringUtils.getBracketedArtifactFromString( tes.getTextParam1(), Constants.ITEMSCOREINFO );
        
        if( cp==null || cp.isEmpty() )
            return textAndTitleList;
        
        textAndTitleList = TestReportingUtils.getTextBasedResponseList( cp );
        
        String itemRespInfo;
        
        for( TextAndTitle tt : textAndTitleList )
        {
            itemRespInfo = tt.getText();

            if( itemRespInfo==null )
                itemRespInfo="";

            if( itemRespInfo.equalsIgnoreCase("Correct"))
                itemRespInfo = lmsg( "g.Correct" );

            else if( itemRespInfo.equalsIgnoreCase("Incorrect"))
                itemRespInfo = lmsg( "g.Incorrect" );

            else if( itemRespInfo.equalsIgnoreCase("Partial"))
                itemRespInfo = lmsg( "g.PartiallyCorrect" );
            
            tt.setText( itemRespInfo );
        }
        
        return textAndTitleList;
    }
    
    private String lmsg( String key )
    {
        if( loc==null )
            loc = Locale.US;
        
        return MessageFactory.getStringMessage(loc, key, null );
    }

    public String getScoreKeyInfo() {
        return scoreKeyInfo;
    }

    public void setScoreKeyInfo(String scoreKeyInfo) {
        this.scoreKeyInfo = scoreKeyInfo;
    }
    
    
    
}
