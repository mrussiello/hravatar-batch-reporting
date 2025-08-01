package com.tm2batch.entity.event;


import com.tm2batch.service.LogService;
import com.tm2batch.util.JsonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;






@Entity
@Table( name = "botevent" )
@NamedQueries({
    @NamedQuery ( name="BotEvent.findByBotEventId", query="SELECT o FROM BotEvent AS o WHERE o.botEventId=:botEventId" ),
    @NamedQuery ( name="BotEvent.findByTestEventId", query="SELECT o FROM BotEvent AS o WHERE o.testEventId=:testEventId" )
})
public class BotEvent implements Serializable, Comparable<BotEvent>
{
    @Transient
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="boteventid")
    private long botEventId;

    @Column(name="userid")
    private long userId;

    @Column(name="orgid")
    private int orgId;

    @Column(name="testeventid")
    private long testEventId;

    @Column(name="callbackmethod")
    private String callbackMethod;
    
    @Column(name="iframeid")
    private String iframeId;
    
    @Column(name="boteventstatustypeid")
    private int botEventStatusTypeId;

    @Column(name="intnuniqueid")
    private String intnUniqueId;
    
    @Column(name="intnitemseq")
    private int intnItemSeq;

    @Column(name="simletId")
    private long simletId;

    @Column(name="simletversionid")
    private int simletVersionId;

    @Column(name="botinstanceid")
    private int botInstanceId;

    @Column(name="messagecount")
    private int messageCount;

    @Column(name="missedmessagecount")
    private int missedMessageCount;

    @Column(name="usermessagecount")
    private int userMessageCount;

    /**
     * Note - this is raw total points. Used only for determining if session should exit.
     */
    @Column(name="totalpoints")
    private float totalPoints;

    /**
     * Format is ID~Bot Instance Id~Intent Name~User Message Content\n
     * 
     * ID=0  General Comment
     * ID=1  Missed Message (No intent on Lex found)
     * ID=2  Missed Intent (Intent on Lex but not in BotInstance)
     */
    @Column(name="notes")
    private String notes;

    
    
    @Column(name="spellingerrorcount")
    private int spellingErrorCount;

    @Column(name="grammarerrorcount")
    private int grammarErrorCount;

    @Column(name="rapportcount")
    private float rapportCount;
        
    @Column(name="negativeexpressioncount")
    private float negativeExpressionCount;
        

    @Column(name="totalwords")
    private int totalWords;

    @Column(name="spellgrammarerrorrate")
    private float spellGrammarErrorRate;
    
    @Column(name="averageresponsetime")
    private float averageResponseTime;
    
    @Column(name="overallscore")
    private float overallScore;

    @Column(name="rapportscore")
    private float rapportScore;

    @Column(name="responsetimescore")
    private float responseTimeScore;

    @Column(name="spellinggrammarscore")
    private float spellingGrammarScore;

    @Column(name="rapportweight")
    private float rapportWeight;

    @Column(name="responsetimeweight")
    private float responseTimeWeight;

    @Column(name="spellinggrammarweight")
    private float spellingGrammarWeight;
    
    @Column(name="competency1name")
    private String competency1Name;

    @Column(name="competency2name")
    private String competency2Name;

    @Column(name="competency3name")
    private String competency3Name;

    @Column(name="competency4name")
    private String competency4Name;

    @Column(name="competency5name")
    private String competency5Name;

    @Column(name="competency6name")
    private String competency6Name;

    @Column(name="competency1points")
    private float competency1Points;

    @Column(name="competency2points")
    private float competency2Points;
    
    @Column(name="competency3points")
    private float competency3Points;
    
    @Column(name="competency4points")
    private float competency4Points;
    
    @Column(name="competency5points")
    private float competency5Points;
    
    @Column(name="competency6points")
    private float competency6Points;
    
    @Column(name="competency1score")
    private float competency1Score;

    @Column(name="competency2score")
    private float competency2Score;
    
    @Column(name="competency3score")
    private float competency3Score;
    
    @Column(name="competency4score")
    private float competency4Score;
    
    @Column(name="competency5score")
    private float competency5Score;
    
    @Column(name="competency6score")
    private float competency6Score;
    
    @Column(name="competency1weight")
    private float competency1Weight;
    
    @Column(name="competency2weight")
    private float competency2Weight;
    
    @Column(name="competency3weight")
    private float competency3Weight;
    
    @Column(name="competency4weight")
    private float competency4Weight;
    
    @Column(name="competency5weight")
    private float competency5Weight;
    
    @Column(name="competency6weight")
    private float competency6Weight;
    
    
    /**
     * {  boteventid: botEventId,
     *    botinstanceid: initialBotInstanceId,
     *    chatentries: [ { type:       0=initial, 1=user entered, 10=bot return
     *                     content:    the message content used.
     *                     intent:     intent name (bot return only),
     *                     points:     points assigned
     *                     actioncode: actionCodeTypeId
     *                     nextbotinstanceid: nextBotInstanceId (if advance to another bot)
     *                   },
     *                   { .. },
     *                   { .. }, etc for entries.
     *    }
     * }
     */
    @Column(name="respjson")
    private String respJson;
        
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="startdate")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastupdate")
    private Date lastUpdate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="completedate")
    private Date completeDate;
    
    @Transient
    private TestEvent testEvent;

    
    @Override
    public String toString() {
        return "BotEvent{" + "botEventId=" + botEventId + ", userId=" + userId + ", orgId=" + orgId + ", testEventId=" + testEventId + ", botEventStatusTypeId=" + botEventStatusTypeId + ", intnUniqueId=" + intnUniqueId + ", botInstanceId=" + botInstanceId + '}';
    }




    @Override
    public int compareTo( BotEvent b )
    {
        return new Long( b.getBotEventId() ).compareTo( new Long( botEventId ) );
    }

    public List<Object[]> getParsedNotes()
    {
        return parseNotes();
    }
    
    /**
     * Format is ID~Bot Instance Id~Intent Name~User Message Content\n
     * 
     * ID=0  General Comment
     * ID=1  Missed Message (No intent on Lex found)
     * ID=2  Missed Intent (Intent on Lex but not in BotInstance)
     * 
     *  Object[] is 
     *     0 ID Integer
     *     1 BotInstanceId  Integer
     *     2 Intent name String
     *     3 Message String
     *     4 BotEventId Long
     */
    public List<Object[]> parseNotes()
    {
        List<Object[]> out = new ArrayList<>();
        
        if( notes==null )
            return out;
        
        String[] entries = notes.split("[\\r\\n]+");
                
        String[] vals;
        
        Object[] eo;
        
        for( String entry : entries )
        {
            entry = entry.trim();
            
            if( entry.isEmpty() )
                continue;
            
            vals = entry.split("~");
            
            if( vals.length<4 )
            {
                out.add( new Object[]{new Integer(0),new Integer(0),"",entry} );
                continue;
            }
    
            try
            {
                if( vals[0].isEmpty() )
                    vals[0]="0";
                
                if( vals[1].isEmpty() )
                    vals[1]="0";
                
                eo = new Object[] { new Integer(vals[0]), new Integer(vals[1]), vals[2].trim(), vals[3].trim(), new Long(botEventId) };
                out.add( eo );
            }
            catch( Exception e )
            {
                LogService.logIt( e, "BotEvent.parseNotes() entry=" + entry );
            }
           
        }
        
        return out;
    }
    
    
        
    public long getBotEventId() {
        return botEventId;
    }

    public void setBotEventId(long botEventId) {
        this.botEventId = botEventId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public long getTestEventId() {
        return testEventId;
    }

    public void setTestEventId(long testEventId) {
        this.testEventId = testEventId;
    }

    public int getBotEventStatusTypeId() {
        return botEventStatusTypeId;
    }

    public void setBotEventStatusTypeId(int botEventStatusTypeId) {
        this.botEventStatusTypeId = botEventStatusTypeId;
    }

    public String getIntnUniqueId() {
        return intnUniqueId;
    }

    public void setIntnUniqueId(String intnUniqueId) {
        this.intnUniqueId = intnUniqueId;
    }

    public int getIntnItemSeq() {
        return intnItemSeq;
    }

    public void setIntnItemSeq(int intnItemSeq) {
        this.intnItemSeq = intnItemSeq;
    }

    public long getSimletId() {
        return simletId;
    }

    public void setSimletId(long simletId) {
        this.simletId = simletId;
    }

    public int getSimletVersionId() {
        return simletVersionId;
    }

    public void setSimletVersionId(int simletVersionId) {
        this.simletVersionId = simletVersionId;
    }

    public int getBotInstanceId() {
        return botInstanceId;
    }

    public void setBotInstanceId(int botInstanceId) {
        this.botInstanceId = botInstanceId;
    }

    public String getRespJson() {
        return respJson;
    }

    public void setRespJson(String respJson) {
        this.respJson = respJson;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public TestEvent getTestEvent() {
        return testEvent;
    }

    public void setTestEvent(TestEvent testEvent) {
        this.testEvent = testEvent;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public float getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(float totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getCallbackMethod() {
        return callbackMethod;
    }

    public void setCallbackMethod(String callbackMethod) {
        this.callbackMethod = callbackMethod;
    }

    public String getIframeId() {
        return iframeId;
    }

    public void setIframeId(String iframeId) {
        this.iframeId = iframeId;
    }

    public int getMissedMessageCount() {
        return missedMessageCount;
    }

    public void setMissedMessageCount(int missedMessageCount) {
        this.missedMessageCount = missedMessageCount;
    }

    public int getSpellingErrorCount() {
        return spellingErrorCount;
    }

    public void setSpellingErrorCount(int spellingErrorCount) {
        this.spellingErrorCount = spellingErrorCount;
    }

    public int getGrammarErrorCount() {
        return grammarErrorCount;
    }

    public void setGrammarErrorCount(int grammarErrorCount) {
        this.grammarErrorCount = grammarErrorCount;
    }

    public float getRapportCount() {
        return rapportCount;
    }

    public void setRapportCount(float rapportCount) {
        this.rapportCount = rapportCount;
    }

    public float getNegativeExpressionCount() {
        return negativeExpressionCount;
    }

    public void setNegativeExpressionCount(float negativeExpressionCount) {
        this.negativeExpressionCount = negativeExpressionCount;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public float getAverageResponseTime() {
        return averageResponseTime;
    }

    public void setAverageResponseTime(float averageResponseTime) {
        this.averageResponseTime = averageResponseTime;
    }

    public float getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(float overallScore) {
        this.overallScore = overallScore;
    }

    public float getRapportScore() {
        return rapportScore;
    }

    public void setRapportScore(float rapportScore) {
        this.rapportScore = rapportScore;
    }

    public float getResponseTimeScore() {
        return responseTimeScore;
    }

    public void setResponseTimeScore(float responseTimeScore) {
        this.responseTimeScore = responseTimeScore;
    }

    public float getSpellingGrammarScore() {
        return spellingGrammarScore;
    }

    public void setSpellingGrammarScore(float spellingGrammarScore) {
        this.spellingGrammarScore = spellingGrammarScore;
    }

    public float getRapportWeight() {
        return rapportWeight;
    }

    public void setRapportWeight(float rapportWeight) {
        this.rapportWeight = rapportWeight;
    }

    public float getResponseTimeWeight() {
        return responseTimeWeight;
    }

    public void setResponseTimeWeight(float responseTimeWeight) {
        this.responseTimeWeight = responseTimeWeight;
    }

    public float getSpellingGrammarWeight() {
        return spellingGrammarWeight;
    }

    public void setSpellingGrammarWeight(float spellingGrammarWeight) {
        this.spellingGrammarWeight = spellingGrammarWeight;
    }

    public float getCompetency1Points() {
        return competency1Points;
    }

    public void setCompetency1Points(float competency1Points) {
        this.competency1Points = competency1Points;
    }

    public float getCompetency2Points() {
        return competency2Points;
    }

    public void setCompetency2Points(float competency2Points) {
        this.competency2Points = competency2Points;
    }

    public float getCompetency3Points() {
        return competency3Points;
    }

    public void setCompetency3Points(float competency3Points) {
        this.competency3Points = competency3Points;
    }

    public float getCompetency4Points() {
        return competency4Points;
    }

    public void setCompetency4Points(float competency4Points) {
        this.competency4Points = competency4Points;
    }

    public float getCompetency5Points() {
        return competency5Points;
    }

    public void setCompetency5Points(float competency5Points) {
        this.competency5Points = competency5Points;
    }

    public float getCompetency6Points() {
        return competency6Points;
    }

    public void setCompetency6Points(float competency6Points) {
        this.competency6Points = competency6Points;
    }

    public float getCompetency1Score() {
        return competency1Score;
    }

    public void setCompetency1Score(float competency1Score) {
        this.competency1Score = competency1Score;
    }

    public float getCompetency2Score() {
        return competency2Score;
    }

    public void setCompetency2Score(float competency2Score) {
        this.competency2Score = competency2Score;
    }

    public float getCompetency3Score() {
        return competency3Score;
    }

    public void setCompetency3Score(float competency3Score) {
        this.competency3Score = competency3Score;
    }

    public float getCompetency4Score() {
        return competency4Score;
    }

    public void setCompetency4Score(float competency4Score) {
        this.competency4Score = competency4Score;
    }

    public float getCompetency5Score() {
        return competency5Score;
    }

    public void setCompetency5Score(float competency5Score) {
        this.competency5Score = competency5Score;
    }

    public float getCompetency6Score() {
        return competency6Score;
    }

    public void setCompetency6Score(float competency6Score) {
        this.competency6Score = competency6Score;
    }

    public float getCompetency1Weight() {
        return competency1Weight;
    }

    public void setCompetency1Weight(float competency1Weight) {
        this.competency1Weight = competency1Weight;
    }

    public float getCompetency2Weight() {
        return competency2Weight;
    }

    public void setCompetency2Weight(float competency2Weight) {
        this.competency2Weight = competency2Weight;
    }

    public float getCompetency3Weight() {
        return competency3Weight;
    }

    public void setCompetency3Weight(float competency3Weight) {
        this.competency3Weight = competency3Weight;
    }

    public float getCompetency4Weight() {
        return competency4Weight;
    }

    public void setCompetency4Weight(float competency4Weight) {
        this.competency4Weight = competency4Weight;
    }

    public float getCompetency5Weight() {
        return competency5Weight;
    }

    public void setCompetency5Weight(float competency5Weight) {
        this.competency5Weight = competency5Weight;
    }

    public float getCompetency6Weight() {
        return competency6Weight;
    }

    public void setCompetency6Weight(float competency6Weight) {
        this.competency6Weight = competency6Weight;
    }

    public float getSpellGrammarErrorRate() {
        return spellGrammarErrorRate;
    }

    public void setSpellGrammarErrorRate(float spellGrammarErrorRate) {
        this.spellGrammarErrorRate = spellGrammarErrorRate;
    }

    public String getCompetency1Name() {
        return competency1Name;
    }

    public void setCompetency1Name(String competency1Name) {
        this.competency1Name = competency1Name;
    }

    public String getCompetency2Name() {
        return competency2Name;
    }

    public void setCompetency2Name(String competency2Name) {
        this.competency2Name = competency2Name;
    }

    public String getCompetency3Name() {
        return competency3Name;
    }

    public void setCompetency3Name(String competency3Name) {
        this.competency3Name = competency3Name;
    }

    public String getCompetency4Name() {
        return competency4Name;
    }

    public void setCompetency4Name(String competency4Name) {
        this.competency4Name = competency4Name;
    }

    public String getCompetency5Name() {
        return competency5Name;
    }

    public void setCompetency5Name(String competency5Name) {
        this.competency5Name = competency5Name;
    }

    public String getCompetency6Name() {
        return competency6Name;
    }

    public void setCompetency6Name(String competency6Name) {
        this.competency6Name = competency6Name;
    }

    public int getUserMessageCount() {
        return userMessageCount;
    }

    public void setUserMessageCount(int userMessageCount) {
        this.userMessageCount = userMessageCount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }



}
