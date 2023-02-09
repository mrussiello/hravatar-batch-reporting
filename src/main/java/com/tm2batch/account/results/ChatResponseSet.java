/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.account.results;


import com.tm2batch.score.TextAndTitle;
import com.tm2batch.util.NVPair;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author miker_000
 */
public class ChatResponseSet {
    
    TextAndTitle tt;
    
    
    public ChatResponseSet( TextAndTitle tt )
    {
        this.tt = tt;
    }
    
    public String getTitle()
    {
        return tt.getTitle();
    }
    
    public boolean getHasData()
    {
        return !getNvPairList().isEmpty();
    }
    
    public List<NVPair> getNvPairList()
    {
        List<NVPair> out = new ArrayList<>();
        
        if( tt.getText()==null || tt.getText().trim().isEmpty() )
            return out;
            
        // LogService.logIt( "ChatResponseSet.getNvPairList() tt.getText()=" + tt.getText() );
        String[] pairs = tt.getText().trim().split( "\\|" );
                
        // LogService.logIt( "ChatResponseSet.getNvPairList() BB pairs.length=" + pairs.length );
        
        if( pairs.length < 2 )
            return out;
                
        String typeStr;
        String content;
                
        for( int idx=0; idx<pairs.length-1; idx+=2 )
        {
            typeStr = pairs[idx].trim();
            content = pairs[idx+1].trim();

            // LogService.logIt( "ChatResponseSet.getNvPairList() CC pair=" + typeStr + ", " + content );
        
            if( typeStr.isEmpty() || content.isEmpty() )
                continue;

            out.add( new NVPair( typeStr, content ) );
        }
        
        return out;
    }
}
