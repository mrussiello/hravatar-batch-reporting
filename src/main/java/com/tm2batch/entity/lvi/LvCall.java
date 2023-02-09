package com.tm2batch.entity.lvi;


import com.tm2batch.entity.proctor.SuspiciousActivity;
import com.tm2batch.entity.user.User;
import com.tm2batch.global.Constants;
import com.tm2batch.ibmcloud.InsightReportTrait;

import com.tm2batch.lvi.LiveVideoOverallRatingType;
import com.tm2batch.lvi.LvCallStatusType;
import com.tm2batch.lvi.SpeechTextStatusType;

import com.tm2batch.service.EncryptUtils;
import com.tm2batch.service.LogService;
import com.tm2batch.util.STStringTokenizer;
import com.tm2batch.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
@Table( name = "lvcall" )
@NamedQueries({
        @NamedQuery( name = "LvCall.findByLvCallId", query = "SELECT o FROM LvCall AS o WHERE o.lvCallId=:lvCallId" ),
        @NamedQuery( name = "LvCall.findByLvInvitationId", query = "SELECT o FROM LvCall AS o WHERE o.lvInvitationId=:lvInvitationId" )     
})
public class LvCall implements Serializable, Comparable<LvCall>, Cloneable
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="lvcallid")
    private long lvCallId;

    @Column(name="lvinvitationid")
    private long lvInvitationId;

    @Column(name="lvcallstatustypeid")
    private int lvCallStatusTypeId;
    
    @Column(name="orgid")
    private int orgId;

    @Column(name="suborgid")
    private int suborgId;

    @Column(name="initiatoruserid")
    private long initiatorUserId;
            
    @Column(name="userid")
    private long userId;

    @Column(name="recipientuserid")
    private long recipientUserId;

    @Column(name="multuserid")
    private long multUserId;
    
    @Column(name="multuserid2")
    private long multUserId2;

    @Column(name="multuserid3")
    private long multUserId3;

    @Column(name="multuserid4")
    private long multUserId4;

    @Column(name="recipientimagefilename")
    private String recipientImageFilename;
    
    @Column(name="productid")
    private int productId;

    @Column(name="title")
    private String title;
        
    
    @Column(name="lvscriptid")
    private int lvScriptId;
    
    @Column(name="connecthistory")
    private String connectHistory;
    
    @Column(name="duration")
    private int duration;
    
    @Column(name="overallratingtypeid")
    private int overallRatingTypeId;

    @Column(name="overallratingtypeid2")
    private int overallRatingTypeId2;

    @Column(name="overallratingtypeid3")
    private int overallRatingTypeId3;

    @Column(name="overallratingtypeid4")
    private int overallRatingTypeId4;

    @Column(name="overallscriptscore")
    private float overallScriptScore;

    @Column(name="overallscriptscore2")
    private float overallScriptScore2;

    @Column(name="overallscriptscore3")
    private float overallScriptScore3;

    @Column(name="overallscriptscore4")
    private float overallScriptScore4;

    @Column( name = "specialprocresultstr" )
    private String specialProcResultStr;
    
    @Column( name = "voicevibesoverallscore" )
    private float voiceVibesOverallScore;
    
    @Column( name = "voicevibesoverallscorehra" )
    private float voiceVibesOverallScoreHra;
    
    @Column( name = "ibminsightresultid" )
    private long ibmInsightResultId;

    @Column( name = "ibminsightstatustypeid" )
    private int ibmInsightStatusTypeId;
    
    @Column( name = "voicevibesstatustypeid" )
    private int voiceVibesStatusTypeId;
    
    @Column( name = "speechtextstatustypeid" )
    private int speechTextStatusTypeId;
    
    
    // format is textDELIMITERtextDELIMITERtextDELIMITER etc
    @Column(name="criteriascorestr")
    private String criteriaScoreStr;

    @Column(name="creditscharged")
    private int creditsCharged = 0;
    
    @Column(name="creditid")
    private long creditId = 0;

    @Column(name="creditindex")
    private int creditIndex = 0;
    
    @Column(name="includetranscript")
    private int includeTranscript;

    @Column(name="includerecordingtypeid")
    private int includeRecordingTypeId = 0;
    
    @Column(name="lvpostprocessingstatustypeid")
    private int lvPostProcessingStatusTypeId;

    
    /**
     * This is userid, date(long) of first log in delimited by comma
     */
    @Column(name="multipleintervieweruserids")
    private String multipleInterviewerUserIds;

    
    @Column(name="history")
    private String history;

    @Column(name="history2")
    private String history2;

    @Column(name="history3")
    private String history3;

    @Column(name="history4")
    private String history4;

    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="startdate")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="enddate")
    private Date endDate;
        
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="completedate")
    private Date completeDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastupdate")
    private Date lastUpdate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="laststatusupdate")
    private Date lastStatusUpdate;
    
    @Transient
    private User user;

    @Transient
    private User recipientUser;

    @Transient
    private User initiatorUser;

    @Transient
    private User multUser;
    
    @Transient
    private User multUser2;
    
    @Transient
    private User multUser3;
    
    @Transient
    private User multUser4;
        
    @Transient
    private String[] criteriaScore1;

    @Transient
    private String[] criteriaScore2;

    @Transient
    private String[] criteriaScore3;
    
    @Transient
    private Locale locale;
    
    @Transient
    private LvScript lvScript;
    
    @Transient
    private List<LvQuestionResponse> lvQuestionResponseList;

    @Transient
    private List<SuspiciousActivity> suspiciousActivityList;
    
    @Transient
    private LvOrgPrefs lvOrgPrefs;
    
    
    @Transient
    Date creditExpireDate;
    
    
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
    
    @Override
    public String toString() {
        return "LvCall{" + "lvCallId=" + lvCallId + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.lvCallId ^ (this.lvCallId >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LvCall other = (LvCall) obj;
        if (this.lvCallId != other.lvCallId) {
            return false;
        }
        return true;
    }

    public void sanitizeUserInput()
    {
        //history = StringUtils.sanitizeForSqlQuery(history);
        //history2 = StringUtils.sanitizeForSqlQuery(history2);
        //history3 = StringUtils.sanitizeForSqlQuery(history3);
        //history4 = StringUtils.sanitizeForSqlQuery(history4);
    }
    

    public boolean getHasRecipientImage()
    {
        return this.recipientImageFilename!=null && !recipientImageFilename.isBlank();
    }

    
    public String getRecipientImagePathFilename()
    {
        if( this.recipientImageFilename==null || this.recipientImageFilename.isBlank() )
            return null;
        
        return "/" + orgId + "/" + lvCallId + "/" + recipientImageFilename;
    }
    
    public String getHistoryXhtml()
    {
        return StringUtils.replaceStandardEntities(history);
    }

    public String getHistory2Xhtml()
    {
        return StringUtils.replaceStandardEntities(history2);
    }

    public String getHistory3Xhtml()
    {
        return StringUtils.replaceStandardEntities(history3);
    }

    public String getHistory4Xhtml()
    {
        return StringUtils.replaceStandardEntities(history4);
    }
    
    @Override
    public int compareTo(LvCall o) 
    {
        if( this.completeDate !=null && o.getCompleteDate()!=null )
            return completeDate.compareTo( o.getCompleteDate() );
        
        if( this.lastUpdate !=null && o.getLastUpdate()!=null )
            return lastUpdate.compareTo( o.getLastUpdate() );
        
        return ((Long)lvCallId).compareTo( o.getLvCallId() );
    }

    
    public String getOverallRatingName()
    {
        return LiveVideoOverallRatingType.getValue( overallRatingTypeId ).getName(locale);
    }

    public String getOverallRatingName2()
    {
        return LiveVideoOverallRatingType.getValue( overallRatingTypeId2 ).getName(locale);
    }

    public String getOverallRatingName3()
    {
        return LiveVideoOverallRatingType.getValue( overallRatingTypeId3 ).getName(locale);
    }

    public String getOverallRatingName4()
    {
        return LiveVideoOverallRatingType.getValue( overallRatingTypeId4 ).getName(locale);
    }
    
    
    public String getDurationStr()
    {
        int h=0;
        int m=0;
        int s=0;
        
        int secs = duration;        
        if( secs<=0 && startDate!=null && completeDate!=null )
        {
            long diff = completeDate.getTime() - startDate.getTime();        
            secs = diff>0 ? (int) diff /(1000) : 0;
        }
        
        //if( secs>10000 )
        //    secs = 0;
        
        float tsecs = secs;
        
        h = (int) (tsecs/(60f*60f));
        if( h>0 )
            tsecs -= 60*60*h;
    
        m = (int) (tsecs/60f);        
        if( m>0 )
            tsecs -= 60*m;
    
        s = (int) tsecs;
        
        // LogService.logIt( "LvCall.getDurationStr() duration=" + duration + ", secs=" + secs + ", h=" + h + ", mins=" + m + ", s=" + s );
        
        String o = "";
        
        if( h>0 )
            o += (h>=10 ? h : "0" + h) + ":";
        
        o += (m>=10 ? m : "0" + m) + ":" + (s>=10 ? s : "0" + s );
        
        if( h>0 )
            o += " hh:mm:ss";
        else
            o += " mm:ss";

        return o;
    }
    
    public String getLvInvitationIdEncrypted()
    {
        try
        {
            return EncryptUtils.urlSafeEncrypt( lvInvitationId );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "getLvInvitationIdEncrypted() " + toString() );
            return "";
        }        
    }
    
    public String getLvCallIdEncrypted()
    {
        try
        {
            return EncryptUtils.urlSafeEncrypt( lvCallId );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "getLvCallIdEncrypted() " + toString() );
            return "";
        }        
    }
    
    
    public String getLvScriptName()
    {
        if( this.lvScript==null )
            return "";
        
        return lvScript.getName();
    }
    
    public synchronized void initCriteriaScores()
    {
        if( criteriaScore1!=null || lvOrgPrefs==null )
            return;
        
        criteriaScore1=new String[4];
        criteriaScore2=new String[4];
        criteriaScore3=new String[4];
        
        int useridx;
        int idx;
        String text;
        
        if( criteriaScoreStr!=null && !criteriaScoreStr.isBlank() )
        {
            STStringTokenizer st = new STStringTokenizer( criteriaScoreStr, Constants.DELIMITER );
            
            List<String> sl = new ArrayList<>();
            while( st.hasMoreTokens() )
            {
                sl.add(st.nextToken() );
            }
            
            for( int i=0;i<sl.size()-2; i+=3 )
            {
                useridx = Integer.parseInt(sl.get(i) );
                idx = Integer.parseInt( sl.get(i+1));
                text = sl.get(i+2);

                if( idx==1 )
                    criteriaScore1[useridx]=text;
                else if( idx==2 )
                    criteriaScore2[useridx]=text;
                else if( idx==3 )
                    criteriaScore3[useridx]=text;
            }            
        }
        
        if( lvOrgPrefs.getHasCriteria1() && lvOrgPrefs.getCriteriaScoreType1().getIsNum10() )
        {
            for( int i=0;i<criteriaScore1.length;i++ )
            {
                if( criteriaScore1[i]==null || criteriaScore1[i].isBlank() )
                    criteriaScore1[i]="0";
            }
        }

        if( lvOrgPrefs.getHasCriteria2() && lvOrgPrefs.getCriteriaScoreType2().getIsNum10() )
        {
            for( int i=0;i<criteriaScore2.length;i++ )
            {
                if( criteriaScore2[i]==null || criteriaScore2[i].isBlank() )
                    criteriaScore2[i]="0";
            }
        }

        if( lvOrgPrefs.getHasCriteria3() && lvOrgPrefs.getCriteriaScoreType3().getIsNum10() )
        {
            for( int i=0;i<criteriaScore3.length;i++ )
            {
                if( criteriaScore3[i]==null || criteriaScore3[i].isBlank() )
                    criteriaScore3[i]="0";
            }
        }        
        
    }

    

    public boolean getHasPermanentPostProcError()
    {
        return this.getLvCallStatusType().getScoredOrHigher() && this.getLvPostProcessingStatusTypeId()>=200;
    }

 
    public SpeechTextStatusType getSpeechTextStatusType()
    {
        return SpeechTextStatusType.getValue( this.speechTextStatusTypeId );
    }
    public boolean getTranscriptNotReadyYet()
    {
        return getSpeechTextStatusType().isNotReadyYet() && this.includeTranscript>0;
    }
    
 
    
    public boolean getHasTranscriptError()
    {
        return getSpeechTextStatusType().isPermanentError() && this.includeTranscript>0;
    }
    public boolean getHasTranscript()
    {
        return getTranscript() !=null && this.includeTranscript>0;
    }

    public boolean getHasTranscriptEnglish()
    {
        return getTranscriptEnglish() !=null && this.includeTranscript>0;
    }
    
    
    public String getTranscriptXhtml()
    {
        return getStrXhtml(  getTranscript() );
    }
    
    public String getTranscriptEnglishXhtml()
    {
        return getStrXhtml(  getTranscriptEnglish() );
    }
    
    private String getStrXhtml( String t )
    {
        return  t==null || t.isBlank() ? "" : StringUtils.replaceStandardEntities(t);
    }
    
    public String getTranscript()
    {
        return getProcStrForKey( Constants.LVC_TRANSCRIPT );
    }
    
    public String getTranscriptEnglish()
    {
        return getProcStrForKey( Constants.LVC_TRANSCRIPT_ENGLISH );
    }
        
    

    public String getRecordingStatusStr()
    {
        return getProcStrForKey( Constants.LVC_RECORDINGSTATUS );
    }
    
    public String getProcStrForKey( String key )
    {
        if( this.specialProcResultStr==null || this.specialProcResultStr.isBlank() )
            return null;
        
        return StringUtils.getBracketedArtifactFromString(specialProcResultStr, key);
    }

    
    
    
    public List<Object[]> parseConnectHistory()
    {
        return parseConnectHistory(0);
    }
    
    
    public List<Object[]> parseConnectHistory( long userId )
    {
        List<Object[]> out = new ArrayList<>();

        // LogService.logIt( "LvCall.parseConnectHistory() connectHistory="  + connectHistory );
        
        if( connectHistory==null || connectHistory.isBlank() )
            return out;
        
        try
        {
            long uid;
            Date start;
            Date end;
            int typeId;
            
            String[] vals = connectHistory.split(";");
            // LogService.logIt( "LvCall.parseConnectHistory() vals.length="  + vals.length );
            for( int i=0;i<vals.length-3; i+=4)
            {
                uid=Long.parseLong(vals[i]);
                
                if( userId>0 && userId!=uid )
                    continue;
                
                start = new Date( Long.parseLong( vals[i+1] ) );
                end = vals[i+2]==null || vals[i+2].isBlank() || vals[i+2].startsWith("S_") ? null : new Date( Long.parseLong( vals[i+2] ) );  
                typeId = vals[i+3]==null || vals[i+3].isBlank() ? (int) 0 : Integer.parseInt( vals[i+3] );  

                // LogService.logIt( "LvCall.parseConnectHistory() Adding "  + uid + " , " + start.toString() + ", " + typeId );
                out.add( new Object[]{uid, start, end, typeId} );
            }
        }
        catch( Exception e )
        {
            LogService.logIt( e, "LvCall.parseConnectHistory() " + connectHistory );
        }
        return out;
    }
        
    
    
    public String getLvCallStatusTypeName()
    {
        return getLvCallStatusType().getName( locale==null ? Locale.US : locale );
    }
    
    
    public LvCallStatusType getLvCallStatusType()
    {
        return LvCallStatusType.getValue( this.lvCallStatusTypeId );
    }
    
    public long getLvCallId() {
        return lvCallId;
    }

    public void setLvCallId(long lvCallId) {
        this.lvCallId = lvCallId;
    }

    public long getLvInvitationId() {
        return lvInvitationId;
    }

    public void setLvInvitationId(long lvInvitationId) {
        this.lvInvitationId = lvInvitationId;
    }

    public int getLvCallStatusTypeId() {
        return lvCallStatusTypeId;
    }

    public void setLvCallStatusTypeId(int s) {
        
        if( s!=this.lvCallStatusTypeId )
            this.lastStatusUpdate=new Date();
        
        this.lvCallStatusTypeId = s;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public long getRecipientUserId() {
        return recipientUserId;
    }

    public void setRecipientUserId(long recipientUserId) {
        this.recipientUserId = recipientUserId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCriteriaScoreStr() {
        return criteriaScoreStr;
    }

    public void setCriteriaScoreStr(String criteriaScoreStr) {
        this.criteriaScoreStr = criteriaScoreStr;
    }

    public String[] getCriteriaScore1() {
        return criteriaScore1;
    }

    public void setCriteriaScore1(String[] criteriaScore1) {
        this.criteriaScore1 = criteriaScore1;
    }

    public String[] getCriteriaScore2() {
        return criteriaScore2;
    }

    public void setCriteriaScore2(String[] criteriaScore2) {
        this.criteriaScore2 = criteriaScore2;
    }

    public String[] getCriteriaScore3() {
        return criteriaScore3;
    }

    public void setCriteriaScore3(String[] criteriaScore3) {
        this.criteriaScore3 = criteriaScore3;
    }


    public int getCreditsCharged() {
        return creditsCharged;
    }

    public void setCreditsCharged(int creditsCharged) {
        this.creditsCharged = creditsCharged;
    }

    public long getCreditId() {
        return creditId;
    }

    public void setCreditId(long creditId) {
        this.creditId = creditId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getRecipientUser() {
        return recipientUser;
    }

    public void setRecipientUser(User recipientUser) {
        this.recipientUser = recipientUser;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getSuborgId() {
        return suborgId;
    }

    public void setSuborgId(int suborgId) {
        this.suborgId = suborgId;
    }

    public Date getLastStatusUpdate() {
        return lastStatusUpdate;
    }

    public void setLastStatusUpdate(Date lastStatusUpdate) {
        this.lastStatusUpdate = lastStatusUpdate;
    }

    public int getLvScriptId() {
        return lvScriptId;
    }

    public void setLvScriptId(int lvScriptId) {
        this.lvScriptId = lvScriptId;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public LvScript getLvScript() {
        return lvScript;
    }

    public void setLvScript(LvScript lvScript) {
        this.lvScript = lvScript;
    }

    public List<LvQuestionResponse> getLvQuestionResponseList() {
        return lvQuestionResponseList;
    }

    public void setLvQuestionResponseList(List<LvQuestionResponse> lvQuestionResponseList) {
        this.lvQuestionResponseList = lvQuestionResponseList;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getInitiatorUserId() {
        return initiatorUserId;
    }

    public void setInitiatorUserId(long initiatorUserId) {
        this.initiatorUserId = initiatorUserId;
    }

    public User getInitiatorUser() {
        return initiatorUser;
    }

    public void setInitiatorUser(User initiatorUser) {
        this.initiatorUser = initiatorUser;
    }

    public String getMultipleInterviewerUserIds() {
        return multipleInterviewerUserIds;
    }

    public void setMultipleInterviewerUserIds(String multipleInterviewerUserIds) {
        this.multipleInterviewerUserIds = multipleInterviewerUserIds;
    }

    public String getConnectHistory() {
        return connectHistory;
    }

    public void setConnectHistory(String connectHistory) {
        this.connectHistory = connectHistory;
    }

    public int getOverallRatingTypeId() {
        return overallRatingTypeId;
    }

    public void setOverallRatingTypeId(int overallRatingTypeId) {
        this.overallRatingTypeId = overallRatingTypeId;
    }

    public float getOverallScriptScore() {
        return overallScriptScore;
    }

    public void setOverallScriptScore(float overallScriptScore) {
        this.overallScriptScore = overallScriptScore;
    }

    public long getMultUserId() {
        return multUserId;
    }

    public void setMultUserId(long multUserId) {
        this.multUserId = multUserId;
    }

    public long getMultUserId2() {
        return multUserId2;
    }

    public void setMultUserId2(long multUserId2) {
        this.multUserId2 = multUserId2;
    }

    public long getMultUserId3() {
        return multUserId3;
    }

    public void setMultUserId3(long multUserId3) {
        this.multUserId3 = multUserId3;
    }

    public long getMultUserId4() {
        return multUserId4;
    }

    public void setMultUserId4(long multUserId4) {
        this.multUserId4 = multUserId4;
    }

    public float getOverallScriptScore2() {
        return overallScriptScore2;
    }

    public void setOverallScriptScore2(float overallScriptScore2) {
        this.overallScriptScore2 = overallScriptScore2;
    }

    public float getOverallScriptScore3() {
        return overallScriptScore3;
    }

    public void setOverallScriptScore3(float overallScriptScore3) {
        this.overallScriptScore3 = overallScriptScore3;
    }

    public float getOverallScriptScore4() {
        return overallScriptScore4;
    }

    public void setOverallScriptScore4(float overallScriptScore4) {
        this.overallScriptScore4 = overallScriptScore4;
    }

    public User getMultUser() {
        return multUser;
    }

    public void setMultUser(User multUser) {
        this.multUser = multUser;
    }

    public User getMultUser2() {
        return multUser2;
    }

    public void setMultUser2(User multUser2) {
        this.multUser2 = multUser2;
    }

    public User getMultUser3() {
        return multUser3;
    }

    public void setMultUser3(User multUser3) {
        this.multUser3 = multUser3;
    }

    public User getMultUser4() {
        return multUser4;
    }

    public void setMultUser4(User multUser4) {
        this.multUser4 = multUser4;
    }


    public int getOverallRatingTypeId2() {
        return overallRatingTypeId2;
    }

    public void setOverallRatingTypeId2(int overallRatingTypeId2) {
        this.overallRatingTypeId2 = overallRatingTypeId2;
    }

    public int getOverallRatingTypeId3() {
        return overallRatingTypeId3;
    }

    public void setOverallRatingTypeId3(int overallRatingTypeId3) {
        this.overallRatingTypeId3 = overallRatingTypeId3;
    }

    public int getOverallRatingTypeId4() {
        return overallRatingTypeId4;
    }

    public void setOverallRatingTypeId4(int overallRatingTypeId4) {
        this.overallRatingTypeId4 = overallRatingTypeId4;
    }

    public String getHistory2() {
        return history2;
    }

    public void setHistory2(String history2) {
        
        if( history2!=null && history2.length()>1499 )
            history2 = history2.substring(0,1499 );
        
        this.history2 = history2;
    }

    public String getHistory3() {
        return history3;
    }

    public void setHistory3(String history3) {
        if( history3!=null && history3.length()>1499 )
            history3 = history3.substring(0,1499 );
        this.history3 = history3;
    }

    public String getHistory4() {
        return history4;
    }

    public void setHistory4(String history4) {
        if( history4!=null && history4.length()>1499 )
            history4 = history4.substring(0,1499 );
        this.history4 = history4;
    }

    public LvOrgPrefs getLvOrgPrefs() {
        return lvOrgPrefs;
    }

    public void setLvOrgPrefs(LvOrgPrefs lvOrgPrefs) {
        this.lvOrgPrefs = lvOrgPrefs;
    }

    public String getRecipientImageFilename() {
        return recipientImageFilename;
    }

    public void setRecipientImageFilename(String recipientImageFilename) {
        this.recipientImageFilename = recipientImageFilename;
    }

    public String getSpecialProcResultStr() {
        return specialProcResultStr;
    }

    public void setSpecialProcResultStr(String specialProcResultStr) {
        this.specialProcResultStr = specialProcResultStr;
    }

    public float getVoiceVibesOverallScore() {
        return voiceVibesOverallScore;
    }

    public void setVoiceVibesOverallScore(float voiceVibesOverallScore) {
        this.voiceVibesOverallScore = voiceVibesOverallScore;
    }

    public float getVoiceVibesOverallScoreHra() {
        return voiceVibesOverallScoreHra;
    }

    public void setVoiceVibesOverallScoreHra(float voiceVibesOverallScoreHra) {
        this.voiceVibesOverallScoreHra = voiceVibesOverallScoreHra;
    }

    public long getIbmInsightResultId() {
        return ibmInsightResultId;
    }

    public void setIbmInsightResultId(long ibmInsightResultId) {
        this.ibmInsightResultId = ibmInsightResultId;
    }

    public int getIbmInsightStatusTypeId() {
        return ibmInsightStatusTypeId;
    }

    public void setIbmInsightStatusTypeId(int ibmInsightStatusTypeId) {
        this.ibmInsightStatusTypeId = ibmInsightStatusTypeId;
    }


    public int getVoiceVibesStatusTypeId() {
        return voiceVibesStatusTypeId;
    }

    public void setVoiceVibesStatusTypeId(int voiceVibesStatusTypeId) {
        this.voiceVibesStatusTypeId = voiceVibesStatusTypeId;
    }

    public int getSpeechTextStatusTypeId() {
        return speechTextStatusTypeId;
    }

    public void setSpeechTextStatusTypeId(int speechTextStatusTypeId) {
        this.speechTextStatusTypeId = speechTextStatusTypeId;
    }

    public int getIncludeTranscript() {
        return includeTranscript;
    }

    public void setIncludeTranscript(int includeTranscript) {
        this.includeTranscript = includeTranscript;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIncludeRecordingTypeId() {
        return includeRecordingTypeId;
    }

    public void setIncludeRecordingTypeId(int includeRecordingTypeId) {
        this.includeRecordingTypeId = includeRecordingTypeId;
    }

    public List<SuspiciousActivity> getSuspiciousActivityList() {
        return suspiciousActivityList;
    }

    public void setSuspiciousActivityList(List<SuspiciousActivity> suspiciousActivityList) {
        this.suspiciousActivityList = suspiciousActivityList;
    }

    public int getCreditIndex() {
        return creditIndex;
    }

    public void setCreditIndex(int creditIndex) {
        this.creditIndex = creditIndex;
    }

    public int getLvPostProcessingStatusTypeId() {
        return lvPostProcessingStatusTypeId;
    }

    public void setLvPostProcessingStatusTypeId(int lvPostProcessingStatusTypeId) {
        this.lvPostProcessingStatusTypeId = lvPostProcessingStatusTypeId;
    }

    public Date getCreditExpireDate() {
        return creditExpireDate;
    }

    public void setCreditExpireDate(Date creditExpireDate) {
        this.creditExpireDate = creditExpireDate;
    }

    
}
