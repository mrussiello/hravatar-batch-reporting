package com.tm2batch.entity.lvi;


import com.tm2batch.util.StringUtils;
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


@Entity
@Table( name = "lvquestionresponse" )
@NamedQueries({
        @NamedQuery( name = "LvQuestionResponse.findByLvCallId", query = "SELECT o FROM LvQuestionResponse AS o WHERE o.lvCallId=:lvCallId" ),
        @NamedQuery( name = "LvQuestionResponse.findByLvCallIdAndLvQuestionId", query = "SELECT o FROM LvQuestionResponse AS o WHERE o.lvCallId=:lvCallId AND o.lvQuestionId=:lvQuestionId" )
})
public class LvQuestionResponse implements Serializable, Comparable<LvQuestionResponse>
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="lvquestionresponseid")
    private long lvQuestionResponseId;
    
    @Column(name="displayorder")
    private int displayOrder;

    @Column(name="lvquestionid")
    private long lvQuestionId;

    @Column(name="lvcallid")
    private long lvCallId;

    @Column(name="responsetext")
    private String responseText;

    @Column(name="scoretext")
    private String scoreText;

    @Column(name="scoretext2")
    private String scoreText2;

    @Column(name="scoretext3")
    private String scoreText3;

    @Column(name="scoretext4")
    private String scoreText4;

    
    @Column(name="score1")
    private float score1;

    @Column(name="score2")
    private float score2;

    @Column(name="score3")
    private float score3;

    @Column(name="score4")
    private float score4;
    
    
    @Transient
    private LvQuestion lvQuestion;
    
    
    @Override
    public int compareTo(LvQuestionResponse o) {
        
        if( lvCallId != o.getLvCallId() )
            return Long.compare(lvCallId, o.getLvCallId());

        return Integer.compare(displayOrder, o.getDisplayOrder());        
    }

    @Override
    public String toString() {
        return "LvQuestionResponse{" + "lvQuestionResponseId=" + lvQuestionResponseId + ", lvQuestionId=" + lvQuestionId + ", lvCallId=" + lvCallId + '}';
    }

    
    public void sanitizeUserInput()
    {
        //responseText = StringUtils.sanitizeForSqlQuery(responseText);
        //scoreText = StringUtils.sanitizeForSqlQuery(scoreText);
        //scoreText2 = StringUtils.sanitizeForSqlQuery(scoreText2);
        //scoreText3 = StringUtils.sanitizeForSqlQuery(scoreText3);
        //scoreText4 = StringUtils.sanitizeForSqlQuery(scoreText4);
    }
    
    
    public boolean getHasData()
    {
        return score1>0 || score2>0 || score3>0 || score4>0 || 
                (scoreText!=null && !scoreText.isBlank()) || 
                (scoreText2!=null && !scoreText2.isBlank()) || 
                (scoreText3!=null && !scoreText3.isBlank()) || 
                (scoreText4!=null && !scoreText4.isBlank()) || 
                (responseText!=null && !responseText.isBlank() );
    }    
    
    public String getScoreTextXhtml()
    {
        return StringUtils.replaceStandardEntities( scoreText );        
    }
    public String getScoreText2Xhtml()
    {
        return StringUtils.replaceStandardEntities( scoreText2 );        
    }
    public String getScoreText3Xhtml()
    {
        return StringUtils.replaceStandardEntities( scoreText3 );        
    }
    public String getScoreText4Xhtml()
    {
        return StringUtils.replaceStandardEntities( scoreText4 );        
    }
    

    public boolean getHasData( int userIdCode )
    {
        if( userIdCode==1 )
            return score1>0 || (scoreText!=null && !scoreText.isBlank());

        if( userIdCode==2 )
            return score2>0 || (scoreText2!=null && !scoreText2.isBlank());

        if( userIdCode==3 )
            return score3>0 || (scoreText3!=null && !scoreText3.isBlank());

        if( userIdCode==4 )
            return score4>0 || (scoreText4!=null && !scoreText4.isBlank());

         return false;       
    }    

    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (int) (this.lvQuestionResponseId ^ (this.lvQuestionResponseId >>> 32));
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
        final LvQuestionResponse other = (LvQuestionResponse) obj;
        if (this.lvQuestionResponseId != other.lvQuestionResponseId) {
            return false;
        }
        return true;
    }
    
    

    public long getLvQuestionResponseId() {
        return lvQuestionResponseId;
    }

    public void setLvQuestionResponseId(long lvQuestionResponseId) {
        this.lvQuestionResponseId = lvQuestionResponseId;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public long getLvQuestionId() {
        return lvQuestionId;
    }

    public void setLvQuestionId(long lvQuestionId) {
        this.lvQuestionId = lvQuestionId;
    }

    public long getLvCallId() {
        return lvCallId;
    }

    public void setLvCallId(long lvCallId) {
        this.lvCallId = lvCallId;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public String getScoreText() {
        return scoreText;
    }

    public void setScoreText(String scoreText) {
        this.scoreText = scoreText;
    }

    public float getScore1() {
        return score1;
    }

    public void setScore1(float score1) {
        this.score1 = score1;
    }

    public float getScore2() {
        return score2;
    }

    public void setScore2(float score2) {
        this.score2 = score2;
    }

    public float getScore3() {
        return score3;
    }

    public void setScore3(float score3) {
        this.score3 = score3;
    }

    public LvQuestion getLvQuestion() {
        return lvQuestion;
    }

    public void setLvQuestion(LvQuestion lvQuestion) {
        this.lvQuestion = lvQuestion;
    }

    public float getScore4() {
        return score4;
    }

    public void setScore4(float score4) {
        this.score4 = score4;
    }

    public String getScoreText2() {
        return scoreText2;
    }

    public void setScoreText2(String scoreText2) {
        if( scoreText2!=null && scoreText2.length()>1499 )
            scoreText2 = scoreText2.substring(0, 1499 );
        
        this.scoreText2 = scoreText2;
    }

    public String getScoreText3() {
        return scoreText3;
    }

    public void setScoreText3(String scoreText3) {
        if( scoreText2!=null && scoreText2.length()>1499 )
            scoreText2 = scoreText2.substring(0, 1499 );
        this.scoreText3 = scoreText3;
    }

    public String getScoreText4() {
        return scoreText4;
    }

    public void setScoreText4(String scoreText4) {
        if( scoreText4!=null && scoreText4.length()>1499 )
            scoreText4 = scoreText4.substring(0, 1499 );
        this.scoreText4 = scoreText4;
    }
    

}
