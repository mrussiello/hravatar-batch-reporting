/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.account.results;


import com.tm2batch.global.Constants;
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
public class ReportUtils {
    
    
    
    public static List<String[]> getParsedTopicScores( List<String> cl, Locale l, int simCompetencyClassId)
    {
        List<String[]> out = new ArrayList<>();
        
        List<String> tl = new ArrayList<>();
        
        for( String c : cl )
        {
            if( c.startsWith( Constants.TOPIC_KEY + "~" ) )
                tl.add( c );            
        }
        
        for( String c : tl )
            out.add(parseTopicCaveatStr(c, l, tl.size()==1, simCompetencyClassId ) );
        
        return out;
    }
    
    /**
     * format of inStr is KEY~S1~S2~S3
     * 
     * Returns
     *   data[0]=Key
     *   data[1]=Topic Name
     *   data[2]=Value
     */
    public static String[] parseTopicCaveatStr( String inStr, Locale locale, boolean isOneLine, int simCompetencyClassId)
    {
        // LogService.logIt( "ReportUtils.parseTopicCaveatStr() inStr=" + inStr );
                    
        String[] out = new String[3];
                
        if( inStr==null || inStr.isEmpty() )
            return out;
        
        String[] ct = inStr.split("~");
        
        int partial = 0;
        
        String stub = "";
        
        if( simCompetencyClassId>=0 )
            stub = SimCompetencyClass.getValue( simCompetencyClassId ).getTopicCorrectStub();
        
        if( ct.length>=4 && ct[0]!=null )
        {
            out[0]=ct[0];
            
            out[1] = ct[1].equals( "NOTOPIC" ) ? MessageFactory.getStringMessage( locale, isOneLine ? "g.CaveatOneTopicName" : "g.CaveatGeneralTopic", null ) : ct[1];

            if( ct.length>4 && ct[4]!=null && !ct[4].isEmpty() ) 
                partial = Integer.parseInt(ct[4]);
            
            // either there is no partially correct items or this is an old score.
            if( partial<=0 ) 
                out[2] = MessageFactory.getStringMessage( locale, "g.CaveatXofYCorrect" + stub, ct ); 
            
            // Has Partials!
            else
                out[2] = MessageFactory.getStringMessage( locale, "g.CaveatXofYCorrectWithPartial" + stub, ct ); 
            
            // out[2] = MessageFactory.getStringMessage( locale, "g.CaveatXofYCorrect", ct ); 
        }
        
        return out;        
    }

    
    public static String getScoreTextFromStr( String scrTxt )
    {
        return getKeyValueFromStr( scrTxt, Constants.SCORETEXTKEY );
    }
    
    public static String getScoreValueFromStr( String scrTxt )
    {
        return getKeyValueFromStr( scrTxt, Constants.SCOREVALUEKEY );
    }
    
    private static boolean getScoreTextStrHasAnyKey( String scrTxt )
    {
        return StringUtils.containsKey( Constants.SCOREVALUEKEY, scrTxt) || StringUtils.containsKey( Constants.SCORETEXTKEY, scrTxt) ;
    }

    
    private static String getKeyValueFromStr( String scrTxt, String key )
    {
        if( scrTxt==null )
            return scrTxt;
        
        scrTxt = scrTxt.trim();
        
        if( scrTxt.isEmpty() )
            return scrTxt;

        // Key not present.
        if( !StringUtils.containsKey(key, scrTxt) )
        {
            // If any other key is present, just return empty.
            if( getScoreTextStrHasAnyKey( scrTxt ) )
                return "";
            
            return scrTxt;
        }
        
        // Score text can be divided between the text to use as the score value and actual score text. 

        // check to see if a key is present.
        String sv = StringUtils.getBracketedArtifactFromString(scrTxt, key); //    IvrStringUtils.getTagValue(scrTxt, key );

        // If key value was present use it.
        if( sv!=null )
            return sv.trim();
        
        // no key value. Use 
        else
            return "";        
    }

    
    
    
}
