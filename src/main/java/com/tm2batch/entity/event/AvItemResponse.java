package com.tm2batch.entity.event;


import com.tm2batch.av.AvItemAudioStatusType;
import com.tm2batch.av.AvItemEssayStatusType;
import com.tm2batch.av.AvItemScoringStatusType;
import com.tm2batch.av.AvItemSpeechTextStatusType;
import com.tm2batch.av.AvItemType;
import com.tm2batch.voicevibes.VoiceVibesStatusType;
import java.io.Serializable;
import java.util.Date;
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


/**
 *
 * @author Mike
 */
@Entity
@Table( name = "avitemresponse" )
@NamedQueries( {
        @NamedQuery( name = "AvItemResponse.findByTestEventId", query = "SELECT o FROM AvItemResponse AS o WHERE o.testEventId=:testEventId ORDER BY o.avItemResponseId" )
} )
public class AvItemResponse implements Comparable<AvItemResponse>, Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "avitemresponseid" )
    private long avItemResponseId;
    
    @Column( name = "testeventid" )
    private long testEventId;

    @Column( name = "simid" )
    private long simId;

    @Column( name = "simversionid" )
    private int simVersionId;

    @Column( name = "itemseq" )
    private int itemSeq;

    @Column( name = "itemsubseq" )
    private int itemSubSeq;

    
    @Column( name = "itemuniqueid" )
    private String itemUniqueId;

    @Column( name = "avItemTypeId" )
    private int avItemTypeId;
    
    @Column( name = "ivrTranTypeId" )
    private int ivrTranTypeId;
    
    @Column( name = "scoringstatustypeid" )
    private int scoringStatusTypeId;
    
    @Column( name = "speechtextsimilarity" )
    private float speechTextSimilarity;
    
    @Column( name = "uploadeduserfileid" )
    private long uploadedUserFileId;
    
    
    @Column( name = "unscoredessayid" )
    private int unscoredEssayId;    
    
    @Column( name = "essaystatustypeid" )
    private int essayStatusTypeId;
           
    @Column( name = "essaymachinescore" )
    private float essayMachineScore;
    
    @Column( name = "essayconfidence" )
    private float essayConfidence;
    
    @Column( name = "essayplagiarized" )
    private int essayPlagiarized;
    
    @Column( name = "langcode" )
    private String langCode;
    
    
    @Column( name = "similarity" )
    private float similarity;
    
    @Column( name = "confidence" )
    private float confidence;
    
    @Column( name = "rawscore" )
    private float rawScore;
    
    @Column( name = "score" )
    private float score;
    
    @Column( name = "scorestr" )
    private String scoreStr;

    @Column( name = "assignedpoints" )
    private float assignedPoints;
        
    @Column( name = "playcount" )
    private int playCount;
    
    @Column( name = "displayorder" )
    private int displayOrder;
    
    @Column( name = "savelocalaudio" )
    private int saveLocalAudio;
    
    @Column( name = "audiostatustypeid" )
    private int audioStatusTypeId;

    @Column( name = "avintnelementtypeid" )
    private String avIntnElementTypeId;

    @Column( name = "dtmf" )
    private String dtmf;

    @Column( name = "selectedsubnodeseq" )
    private int selectedSubnodeSeq;
    
    @Column( name = "duration" )
    private float duration;

    /**
     * Packed string 
     * tran1a,tran1b,tran1c;confidence1;tran2a,tran2b|confidence2; ... 
     */
    @Column( name = "speechtext" )
    private String speechText;

    @Column( name = "speechtextconfidence" )
    private float speechTextConfidence;

    @Column( name = "speechtextstatustypeid" )
    private int speechTextStatusTypeId;
    
    @Column( name = "speechtextthirdpartyid" )
    private String speechTextThirdPartyId;

    @Column( name = "speechtexterrorcount" )
    private int speechTextErrorCount;
    
    @Column( name = "audiouri" )
    private String audioUri;

    @Column( name = "audiothirdpartyid" )
    private String audioThirdPartyId;

    @Column( name = "extrathirdpartyaudioids" )
    private String extraThirdPartyAudioIds;
    
    @Column( name = "voicevibesstatustypeid" )
    private int voiceVibesStatusTypeId;
    
    @Column( name = "voicevibesresponsestr" )
    private String voiceVibesResponseStr;
    
    @Column( name = "voicevibesid" )
    private String voiceVibesId;
    
    @Column( name = "voicevibesaccounttypeid" )
    private int voiceVibesAccountTypeId;
    
    @Column( name = "voicevibesoverallscore" )
    private float voiceVibesOverallScore;
    
    @Column( name = "voicevibesoverallscorehra" )
    private float voiceVibesOverallScoreHra;
    
    @Column( name = "voicevibesposterrorcount" )
    private int voiceVibesPostErrorCount;
    
    
    @Column( name = "googlestoragename" )
    private String googleStorageName;

    
    
    @Column( name = "speechtextenglish" )
    private String speechTextEnglish;
    
    @Column( name = "notes" )
    private String notes;
    
    @Column( name = "audiobytes" )
    private byte[] audioBytes;

    @Column( name = "audiosize" )
    private int audioSize;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="speechtextrequestdate")
    private Date speechTextRequestDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="voicevibesrequestdate")
    private Date voiceVibesRequestDate;
    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;
    
        
    
    
    public AvItemResponse()
    {}


    @Override
    public int compareTo(AvItemResponse o) {

        if( this.itemUniqueId!=null && !this.itemUniqueId.isEmpty() && o.getItemUniqueId()!=null )
            return itemUniqueId.compareTo(o.getItemUniqueId() );

        return 0;
    }
    
    public AvItemScoringStatusType getScoringStatusType()
    {
        return AvItemScoringStatusType.getValue( this.scoringStatusTypeId );
    }
    
    public AvItemAudioStatusType getIvrItemAudioStatusType()
    {
        return AvItemAudioStatusType.getValue( this.audioStatusTypeId );
    }
    
    public AvItemSpeechTextStatusType getSpeechTextStatusType()
    {
        return AvItemSpeechTextStatusType.getValue( this.speechTextStatusTypeId );
    }
    
    public AvItemType getAvItemType()
    {
        return AvItemType.getValue(this.avItemTypeId );
    }

    public VoiceVibesStatusType getVoiceVibesStatusType()
    {
        return VoiceVibesStatusType.getValue( this.voiceVibesStatusTypeId );
    }

    
    public AvItemScoringStatusType getIvrItemScoringStatusType()
    {
        return AvItemScoringStatusType.getValue( this.scoringStatusTypeId );
    }
    
    public AvItemSpeechTextStatusType getIvrItemSpeechTextStatusType()
    {
        return AvItemSpeechTextStatusType.getValue( this.speechTextStatusTypeId );
    }
    
    public boolean requiresSpeechToText()
    {
        return getAvItemType().requiresRecordVoice(); //  && (speechText==null );
    }

    public boolean isSpeechToTextComplete()
    {
        return getIvrItemSpeechTextStatusType().isComplete();
    }
    
    
    public boolean containsPendingAudio()
    {
        return getIvrItemAudioStatusType().isPending();
    }
    
    
    public boolean isSpeechToTextCompleteOrPermanentError()
    {
        return getIvrItemSpeechTextStatusType().isComplete() || 
               getIvrItemSpeechTextStatusType().isNotRequired() || 
               getIvrItemSpeechTextStatusType().isPermanentError();
    }
    
    
    public AvItemAudioStatusType getAudioStatusType()
    {
        return AvItemAudioStatusType.getValue( this.audioStatusTypeId );
    }
    
    public AvItemEssayStatusType getEssayStatusType()
    {
        return AvItemEssayStatusType.getValue( this.essayStatusTypeId );
    }
    
        
    
    @Override
    public String toString() {
        return "AvItemResponse{" + "avItemResponseId=" + avItemResponseId + ", testEventId=" + testEventId + ", itemSeq=" + itemSeq + ", itemUniqueId=" + itemUniqueId + ", avItemTypeId=" + avItemTypeId + ", scoringStatusTypeId=" + scoringStatusTypeId + ", audioStatusTypeId=" + audioStatusTypeId + ", avIntnElementTypeId=" + avIntnElementTypeId + ", dtmf=" + dtmf + ", selectedSubnodeSeq=" + selectedSubnodeSeq + ", duration=" + duration + ", speechTextConfidence=" + speechTextConfidence + '}';
    }
    
    

    public long getAvItemResponseId() {
        return avItemResponseId;
    }

    public void setAvItemResponseId(long i) {
        this.avItemResponseId = i;
    }

    public long getTestEventId() {
        return testEventId;
    }

    public void setTestEventId(long testEventId) {
        this.testEventId = testEventId;
    }

    public long getSimId() {
        return simId;
    }

    public void setSimId(long simId) {
        this.simId = simId;
    }

    public int getItemSeq() {
        return itemSeq;
    }

    public void setItemSeq(int itemSeq) {
        this.itemSeq = itemSeq;
    }

    public String getAvIntnElementTypeId() {
        return avIntnElementTypeId;
    }

    public void setAvIntnElementTypeId(String t) {
        this.avIntnElementTypeId = t;
    }

    public String getItemUniqueId() {
        return itemUniqueId;
    }

    public void setItemUniqueId(String itemUniqueId) {
        this.itemUniqueId = itemUniqueId;
    }

    public int getIvrTranTypeId() {
        return ivrTranTypeId;
    }

    public void setIvrTranTypeId(int ivrTranTypeId) {
        this.ivrTranTypeId = ivrTranTypeId;
    }

    public int getScoringStatusTypeId() {
        return scoringStatusTypeId;
    }

    public void setScoringStatusTypeId(int scoringStatusTypeId) {
        this.scoringStatusTypeId = scoringStatusTypeId;
    }

    public int getAudioStatusTypeId() {
        return audioStatusTypeId;
    }

    public void setAudioStatusTypeId(int audioStatusTypeId) {
        this.audioStatusTypeId = audioStatusTypeId;
    }

    public String getDtmf() {
        return dtmf;
    }

    public void setDtmf(String dtmf) {
        this.dtmf = dtmf;
    }

    public int getSelectedSubnodeSeq() {
        return selectedSubnodeSeq;
    }

    public void setSelectedSubnodeSeq(int selectedSubnodeSeq) {
        this.selectedSubnodeSeq = selectedSubnodeSeq;
    }

    public String getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(String audioUri) {
        this.audioUri = audioUri;
    }

    public String getAudioThirdPartyId() {
        return audioThirdPartyId;
    }

    public void setAudioThirdPartyId(String audioThirdPartyId) {
        this.audioThirdPartyId = audioThirdPartyId;
    }


    public int getAvItemTypeId() {
        return avItemTypeId;
    }

    public void setAvItemTypeId(int i) {
        this.avItemTypeId = i;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getSpeechText() {
        return speechText;
    }

    public void setSpeechText(String speechText) {
        this.speechText = speechText;
    }

    public String getSpeechTextEnglish() {
        return speechTextEnglish;
    }

    public void setSpeechTextEnglish(String speechTextEnglish) {
        this.speechTextEnglish = speechTextEnglish;
    }

    public int getSimVersionId() {
        return simVersionId;
    }

    public void setSimVersionId(int simVersionId) {
        this.simVersionId = simVersionId;
    }

    public byte[] getAudioBytes() {
        return audioBytes;
    }

    public void setAudioBytes(byte[] audioBytes) {
        this.audioBytes = audioBytes;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getAudioSize() {
        return audioSize;
    }

    public void setAudioSize(int audioSize) {
        this.audioSize = audioSize;
    }

    public float getSpeechTextConfidence() {
        return speechTextConfidence;
    }

    public void setSpeechTextConfidence(float speechTextConfidence) {
        this.speechTextConfidence = speechTextConfidence;
    }

    public float getSpeechTextSimilarity() {
        return speechTextSimilarity;
    }

    public void setSpeechTextSimilarity(float speechTextSimilarity) {
        this.speechTextSimilarity = speechTextSimilarity;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getExtraThirdPartyAudioIds() {
        return extraThirdPartyAudioIds;
    }

    public void setExtraThirdPartyAudioIds(String extraThirdPartyAudioIds) {
        this.extraThirdPartyAudioIds = extraThirdPartyAudioIds;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public int getSpeechTextStatusTypeId() {
        return speechTextStatusTypeId;
    }

    public void setSpeechTextStatusTypeId(int speechTextStatusTypeId) {
        this.speechTextStatusTypeId = speechTextStatusTypeId;
    }

    public int getSpeechTextErrorCount() {
        return speechTextErrorCount;
    }

    public void setSpeechTextErrorCount(int speechTextErrorCount) {
        this.speechTextErrorCount = speechTextErrorCount;
    }

    public float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public float getRawScore() {
        return rawScore;
    }

    public void setRawScore(float rawScore) {
        this.rawScore = rawScore;
    }

    public String getScoreStr() {
        return scoreStr;
    }

    public void setScoreStr(String scoreStr) {
        this.scoreStr = scoreStr;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getVoiceVibesResponseStr() {
        return voiceVibesResponseStr;
    }

    public void setVoiceVibesResponseStr(String voiceVibesResponseStr) {
        this.voiceVibesResponseStr = voiceVibesResponseStr;
    }

    public String getVoiceVibesId() {
        return voiceVibesId;
    }

    public void setVoiceVibesId(String voiceVibesId) {
        this.voiceVibesId = voiceVibesId;
    }

    public Date getVoiceVibesRequestDate() {
        return voiceVibesRequestDate;
    }

    public void setVoiceVibesRequestDate(Date voiceVibesRequestDate) {
        this.voiceVibesRequestDate = voiceVibesRequestDate;
    }

    public String getGoogleStorageName() {
        return googleStorageName;
    }

    public void setGoogleStorageName(String googleStorageName) {
        this.googleStorageName = googleStorageName;
    }

    public int getVoiceVibesStatusTypeId() {
        return voiceVibesStatusTypeId;
    }

    public void setVoiceVibesStatusTypeId(int voiceVibesStatusTypeId) {
        this.voiceVibesStatusTypeId = voiceVibesStatusTypeId;
    }

    public int getVoiceVibesAccountTypeId() {
        return voiceVibesAccountTypeId;
    }

    public void setVoiceVibesAccountTypeId(int voiceVibesAccountTypeId) {
        this.voiceVibesAccountTypeId = voiceVibesAccountTypeId;
    }

    public String getSpeechTextThirdPartyId() {
        return speechTextThirdPartyId;
    }

    public void setSpeechTextThirdPartyId(String speechTextThirdPartyId) {
        this.speechTextThirdPartyId = speechTextThirdPartyId;
    }

    public Date getSpeechTextRequestDate() {
        return speechTextRequestDate;
    }

    public void setSpeechTextRequestDate(Date speechTextRequestDate) {
        this.speechTextRequestDate = speechTextRequestDate;
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

    public int getVoiceVibesPostErrorCount() {
        return voiceVibesPostErrorCount;
    }

    public void setVoiceVibesPostErrorCount(int voiceVibesPostErrorCount) {
        this.voiceVibesPostErrorCount = voiceVibesPostErrorCount;
    }

    public int getSaveLocalAudio() {
        return saveLocalAudio;
    }

    public void setSaveLocalAudio(int saveLocalAudio) {
        this.saveLocalAudio = saveLocalAudio;
    }

    public float getAssignedPoints() {
        return assignedPoints;
    }

    public void setAssignedPoints(float assignedPoints) {
        this.assignedPoints = assignedPoints;
    }

    public int getItemSubSeq() {
        return itemSubSeq;
    }

    public void setItemSubSeq(int itemSubSeq) {
        this.itemSubSeq = itemSubSeq;
    }

    public int getUnscoredEssayId() {
        return unscoredEssayId;
    }

    public void setUnscoredEssayId(int unscoredEssayId) {
        this.unscoredEssayId = unscoredEssayId;
    }

    public int getEssayStatusTypeId() {
        return essayStatusTypeId;
    }

    public void setEssayStatusTypeId(int essayScoreStatusTypeId) {
        this.essayStatusTypeId = essayScoreStatusTypeId;
    }

    public long getUploadedUserFileId() {
        return uploadedUserFileId;
    }

    public void setUploadedUserFileId(long uploadedUserFileId) {
        this.uploadedUserFileId = uploadedUserFileId;
    }

    public float getEssayMachineScore() {
        return essayMachineScore;
    }

    public void setEssayMachineScore(float essayMachineScore) {
        this.essayMachineScore = essayMachineScore;
    }

    public float getEssayConfidence() {
        return essayConfidence;
    }

    public void setEssayConfidence(float essayConfidence) {
        this.essayConfidence = essayConfidence;
    }

    public int getEssayPlagiarized() {
        return essayPlagiarized;
    }

    public void setEssayPlagiarized(int essayPlagiarized) {
        this.essayPlagiarized = essayPlagiarized;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

}
