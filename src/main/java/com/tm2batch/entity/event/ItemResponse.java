package com.tm2batch.entity.event;


import java.io.Serializable;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;


/**
 *
 * @author Mike
 */
@Entity
@Table( name = "itemresponse" )
@NamedQueries( {
        @NamedQuery( name = "ItemResponse.findByTestEventId", query = "SELECT o FROM ItemResponse AS o WHERE o.testEventId=:testEventId ORDER BY o.itemResponseId" ),
} )
public class ItemResponse implements Comparable<ItemResponse>, Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "itemresponseid" )
    private long itemResponseId;

    @Column( name = "testeventid" )
    private long testEventId;

    @Column( name = "surveyeventid" )
    private long surveyEventId;

    @Column( name = "identifier" )
    private String identifier;

    @Column( name = "displayorder" )
    private float displayOrder;

    @Column( name = "simid" )
    private long simId;

    @Column( name = "simnodeseq" )
    private int simNodeSeq;

    @Column( name = "simversionid" )
    private int simVersionId;

    @Column( name = "simletid" )
    private long simletId;

    @Column( name = "simletversionid" )
    private int simletVersionId;

    @Column( name = "simletaid" )
    private long simletAid;

    @Column( name = "simletnodeid" )
    private long simletNodeId;

    @Column( name = "simletnodeseq" )
    private int simletNodeSeq;

    @Column( name = "simletsubnodeseq" )
    private int simletSubnodeSeq;

    @Column( name = "simletitemtypeid" )
    private int simletItemTypeId;

    @Column( name = "simletnodeuniqueid" )
    private String simletNodeUniqueId;

    @Column( name = "radiobuttongroupid" )
    private int radioButtonGroupId;


    @Column( name = "simcompetencyid" )
    private long simCompetencyId;

    @Column( name = "competencyscoreid" )
    private long competencyScoreId;

    @Column( name = "responselevelid" )
    private int responseLevelId;  /* 0=Interaction-level, 1=previous interaction level, 2=Radio button Group Level, 3=Interaction-Item Level */

    @Column( name = "repeatitemsimnodeseq" )
    private int repeatItemSimNodeSeq;    

    @Column( name = "correct" )
    private int correct;

    @Column( name = "correctsubnodeseqids" )
    private String correctSubnodeSeqIds;

    @Column( name = "selectedsubnodeseqids" )
    private String selectedSubnodeSeqIds;

    @Column( name = "selectedsubformattypeids" )
    private String selectedSubFormatTypeIds;

    @Column( name = "subnodeseq" )
    private int subnodeSeq;

    @Column( name = "subnodeformattypeid" )
    private int subnodeFormatTypeId;

    @Column( name = "selectedvalue" )
    private String selectedValue;

    @Column( name = "itemscore" )
    private float itemScore = 0;

    @Column( name = "truescore" )
    private float trueScore = 0;

    @Column( name = "scoreparam1" )
    private float scoreParam1 = 0;

    @Column( name = "scoreparam2" )
    private float scoreParam2 = 0;

    @Column( name = "scoreparam3" )
    private float scoreParam3 = 0;

    @Column( name = "responsetime" )
    private float responseTime = 0;

    // 0=response collected (0=wrong, 1=correct), --1 means not answered, -2 means timed out
    @Column( name = "itemresponsetypeid" )
    private int itemResponseTypeId = 0;

    @Column( name = "metascore1" )
    private float metascore1 = 0;

    @Column( name = "metascore2" )
    private float metascore2 = 0;

    @Column( name = "metascore3" )
    private float metascore3 = 0;

    @Column( name = "metascore4" )
    private float metascore4 = 0;

    @Column( name = "metascore5" )
    private float metascore5 = 0;
    
    @Column( name = "metascore6" )
    private float metascore6 = 0;

    @Column( name = "metascore7" )
    private float metascore7 = 0;

    @Column( name = "metascore8" )
    private float metascore8 = 0;

    

    @Column( name = "itemparadigmtypeid" )
    private int itemParadigmTypeId = 0;


    @Transient
    private float competencyTotalPoints;

    @Transient
    private float competencyTotalCorrect;

    @Transient
    private float competencyMaxCorrect;

    @Transient
    private float competencyMaxPoints;

    @Transient
    private String tempIdentifier;

    @Transient
    private String altIdentifier;

    @Transient
    private boolean skip = false;
    
    public ItemResponse()
    {}

    

    @Override
    public int compareTo(ItemResponse o) {

        return getCompareStr().compareTo(o.getCompareStr());
    }
    
    public String getCompareStr()
    {
        return displayOrder + "_" + (identifier==null ? "" : identifier );
    }

    public long getItemResponseId() {
        return itemResponseId;
    }

    public void setItemResponseId(long itemResponseId) {
        this.itemResponseId = itemResponseId;
    }

    public long getTestEventId() {
        return testEventId;
    }

    public void setTestEventId(long testEventId) {
        this.testEventId = testEventId;
    }

    public long getSurveyEventId() {
        return surveyEventId;
    }

    public void setSurveyEventId(long surveyEventId) {
        this.surveyEventId = surveyEventId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public float getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(float displayOrder) {
        this.displayOrder = displayOrder;
    }

    public long getSimId() {
        return simId;
    }

    public void setSimId(long simId) {
        this.simId = simId;
    }

    public int getSimNodeSeq() {
        return simNodeSeq;
    }

    public void setSimNodeSeq(int simNodeSeq) {
        this.simNodeSeq = simNodeSeq;
    }

    public int getSimVersionId() {
        return simVersionId;
    }

    public void setSimVersionId(int simVersionId) {
        this.simVersionId = simVersionId;
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

    public long getSimletAid() {
        return simletAid;
    }

    public void setSimletAid(long simletAid) {
        this.simletAid = simletAid;
    }

    public long getSimletNodeId() {
        return simletNodeId;
    }

    public void setSimletNodeId(long simletNodeId) {
        this.simletNodeId = simletNodeId;
    }

    public int getSimletNodeSeq() {
        return simletNodeSeq;
    }

    public void setSimletNodeSeq(int simletNodeSeq) {
        this.simletNodeSeq = simletNodeSeq;
    }

    public int getSimletSubnodeSeq() {
        return simletSubnodeSeq;
    }

    public void setSimletSubnodeSeq(int simletSubnodeSeq) {
        this.simletSubnodeSeq = simletSubnodeSeq;
    }

    public int getSimletItemTypeId() {
        return simletItemTypeId;
    }

    public void setSimletItemTypeId(int simletItemTypeId) {
        this.simletItemTypeId = simletItemTypeId;
    }

    public String getSimletNodeUniqueId() {
        return simletNodeUniqueId;
    }

    public void setSimletNodeUniqueId(String simletNodeUniqueId) {
        this.simletNodeUniqueId = simletNodeUniqueId;
    }

    public int getRadioButtonGroupId() {
        return radioButtonGroupId;
    }

    public void setRadioButtonGroupId(int radioButtonGroupId) {
        this.radioButtonGroupId = radioButtonGroupId;
    }

    public long getSimCompetencyId() {
        return simCompetencyId;
    }

    public void setSimCompetencyId(long simCompetencyId) {
        this.simCompetencyId = simCompetencyId;
    }

    public long getCompetencyScoreId() {
        return competencyScoreId;
    }

    public void setCompetencyScoreId(long competencyScoreId) {
        this.competencyScoreId = competencyScoreId;
    }

    public int getResponseLevelId() {
        return responseLevelId;
    }

    public void setResponseLevelId(int responseLevelId) {
        this.responseLevelId = responseLevelId;
    }

    public int getRepeatItemSimNodeSeq() {
        return repeatItemSimNodeSeq;
    }

    public void setRepeatItemSimNodeSeq(int repeatItemSimNodeSeq) {
        this.repeatItemSimNodeSeq = repeatItemSimNodeSeq;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public String getCorrectSubnodeSeqIds() {
        return correctSubnodeSeqIds;
    }

    public void setCorrectSubnodeSeqIds(String correctSubnodeSeqIds) {
        this.correctSubnodeSeqIds = correctSubnodeSeqIds;
    }

    public String getSelectedSubnodeSeqIds() {
        return selectedSubnodeSeqIds;
    }

    public void setSelectedSubnodeSeqIds(String selectedSubnodeSeqIds) {
        this.selectedSubnodeSeqIds = selectedSubnodeSeqIds;
    }

    public String getSelectedSubFormatTypeIds() {
        return selectedSubFormatTypeIds;
    }

    public void setSelectedSubFormatTypeIds(String selectedSubFormatTypeIds) {
        this.selectedSubFormatTypeIds = selectedSubFormatTypeIds;
    }

    public int getSubnodeSeq() {
        return subnodeSeq;
    }

    public void setSubnodeSeq(int subnodeSeq) {
        this.subnodeSeq = subnodeSeq;
    }

    public int getSubnodeFormatTypeId() {
        return subnodeFormatTypeId;
    }

    public void setSubnodeFormatTypeId(int subnodeFormatTypeId) {
        this.subnodeFormatTypeId = subnodeFormatTypeId;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public float getItemScore() {
        return itemScore;
    }

    public void setItemScore(float itemScore) {
        this.itemScore = itemScore;
    }

    public float getTrueScore() {
        return trueScore;
    }

    public void setTrueScore(float trueScore) {
        this.trueScore = trueScore;
    }

    public float getScoreParam1() {
        return scoreParam1;
    }

    public void setScoreParam1(float scoreParam1) {
        this.scoreParam1 = scoreParam1;
    }

    public float getScoreParam2() {
        return scoreParam2;
    }

    public void setScoreParam2(float scoreParam2) {
        this.scoreParam2 = scoreParam2;
    }

    public float getScoreParam3() {
        return scoreParam3;
    }

    public void setScoreParam3(float scoreParam3) {
        this.scoreParam3 = scoreParam3;
    }

    public float getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(float responseTime) {
        this.responseTime = responseTime;
    }

    public int getItemResponseTypeId() {
        return itemResponseTypeId;
    }

    public void setItemResponseTypeId(int itemResponseTypeId) {
        this.itemResponseTypeId = itemResponseTypeId;
    }

    public float getMetascore1() {
        return metascore1;
    }

    public void setMetascore1(float metascore1) {
        this.metascore1 = metascore1;
    }

    public float getMetascore2() {
        return metascore2;
    }

    public void setMetascore2(float metascore2) {
        this.metascore2 = metascore2;
    }

    public float getMetascore3() {
        return metascore3;
    }

    public void setMetascore3(float metascore3) {
        this.metascore3 = metascore3;
    }

    public float getMetascore4() {
        return metascore4;
    }

    public void setMetascore4(float metascore4) {
        this.metascore4 = metascore4;
    }

    public float getMetascore5() {
        return metascore5;
    }

    public void setMetascore5(float metascore5) {
        this.metascore5 = metascore5;
    }

    public float getMetascore6() {
        return metascore6;
    }

    public void setMetascore6(float metascore6) {
        this.metascore6 = metascore6;
    }

    public float getMetascore7() {
        return metascore7;
    }

    public void setMetascore7(float metascore7) {
        this.metascore7 = metascore7;
    }

    public float getMetascore8() {
        return metascore8;
    }

    public void setMetascore8(float metascore8) {
        this.metascore8 = metascore8;
    }

    public int getItemParadigmTypeId() {
        return itemParadigmTypeId;
    }

    public void setItemParadigmTypeId(int itemParadigmTypeId) {
        this.itemParadigmTypeId = itemParadigmTypeId;
    }

    public float getCompetencyTotalPoints() {
        return competencyTotalPoints;
    }

    public void setCompetencyTotalPoints(float competencyTotalPoints) {
        this.competencyTotalPoints = competencyTotalPoints;
    }

    public float getCompetencyTotalCorrect() {
        return competencyTotalCorrect;
    }

    public void setCompetencyTotalCorrect(float competencyTotalCorrect) {
        this.competencyTotalCorrect = competencyTotalCorrect;
    }

    public float getCompetencyMaxCorrect() {
        return competencyMaxCorrect;
    }

    public void setCompetencyMaxCorrect(float competencyMaxCorrect) {
        this.competencyMaxCorrect = competencyMaxCorrect;
    }

    public float getCompetencyMaxPoints() {
        return competencyMaxPoints;
    }

    public void setCompetencyMaxPoints(float competencyMaxPoints) {
        this.competencyMaxPoints = competencyMaxPoints;
    }

    public String getTempIdentifier() {
        return tempIdentifier;
    }

    public void setTempIdentifier(String tempIdentifier) {
        this.tempIdentifier = tempIdentifier;
    }

    public String getAltIdentifier() {
        return altIdentifier;
    }

    public void setAltIdentifier(String altIdentifier) {
        this.altIdentifier = altIdentifier;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }



}
