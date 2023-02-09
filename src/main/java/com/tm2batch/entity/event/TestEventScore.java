package com.tm2batch.entity.event;

import com.tm2batch.account.results.PercentileEntry;
import com.tm2batch.account.results.ScoreCategoryRange;
import com.tm2batch.account.results.TestResultUtils;
import com.tm2batch.event.ScoreCategoryType;
import com.tm2batch.event.ScoreFormatType;
import com.tm2batch.event.TestEventResponseRatingType;
import com.tm2batch.event.TestEventResponseRatingUtils;
import com.tm2batch.event.TestEventScoreStatusType;
import com.tm2batch.event.TestEventScoreType;
import com.tm2batch.file.FileContentType;
import com.tm2batch.global.Constants;
import com.tm2batch.global.DisplayOrderObject;
import com.tm2batch.score.TextAndTitle;
import com.tm2batch.service.EncryptUtils;
import com.tm2batch.service.LogService;
import com.tm2batch.sim.SimCompetencyClass;
import com.tm2batch.util.I18nUtils;
import com.tm2batch.util.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import jakarta.persistence.Cacheable;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;


@Cacheable(false)
@Entity
@Table( name = "testeventscore" )
@NamedQueries( {
        @NamedQuery( name = "TestEventScore.findByTestEventId", query = "SELECT o FROM TestEventScore AS o WHERE o.testEventId=:testEventId" ),
        @NamedQuery( name = "TestEventScore.findByTestEventIdAndTestEventScoreTypeId", query = "SELECT o FROM TestEventScore AS o WHERE o.testEventId=:testEventId AND o.testEventScoreTypeId=:testEventScoreTypeId" )
} )
public class TestEventScore implements Serializable, Comparable<TestEventScore>, Cloneable, DisplayOrderObject
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "testeventscoreid" )
    private long testEventScoreId;

    @Column( name = "testeventid" )
    private long testEventId;

    @Column( name = "testeventscorestatustypeid" )
    private int testEventScoreStatusTypeId;


    // overall or subsocre
    @Column( name = "testeventscoretypeid" )
    private int testEventScoreTypeId;

    @Column( name = "hide" )
    private int hide;

    @Column( name = "hidenumericscore" )
    private int hideNumericScore;

    // non-numeric, numeric 0-5, 0-100, other
    @Column( name = "scoreformattypeid" )
    private int scoreFormatTypeId;

    @Column( name = "scorecategoryid" )
    private int scoreCategoryId;

    @Column( name = "name" )
    private String name;

    @Column( name = "nameenglish" )
    private String nameEnglish;


    /**
     * CompetencyGroup type - SimCompetencyGroupTypeId
     * 
     */
    @Column( name = "intparam1" )
    private int intParam1 = 0;

    /**
     * Competency Type -  IncludeItemScoresTypeId
     */
    @Column( name = "intparam2" )
    private int intParam2 = 0;

    @Column( name = "longparam1" )
    private long longParam1 = 0;

    @Column( name = "longparam2" )
    private long longParam2 = 0;

    @Column( name = "strparam1" )
    private String strParam1;

    @Column( name = "strparam2" )
    private String strParam2;


    @Column( name = "displayorder" )
    private int displayOrder;

    @Column( name = "score" )
    private float score = 0;

    @Column( name = "score2" )
    private float score2;

    @Column( name = "score3" )
    private float score3;

    @Column( name = "score4" )
    private float score4;

    @Column( name = "score5" )
    private float score5;
    
    @Column( name = "score6" )
    private float score6;
    

    @Column( name = "percentile" )
    private float percentile;

    @Column( name = "accountpercentile" )
    private float accountPercentile = -1;

    @Column( name = "countrypercentile" )
    private float countryPercentile = -1;

    @Column( name = "overallpercentilecount" )
    private int overallPercentileCount;

    @Column( name = "accountpercentilecount" )
    private int accountPercentileCount;

    @Column( name = "countrypercentilecount" )
    private int countryPercentileCount;

    @Column(name="percentilecountry")
    private String percentileCountry;

    
    @Column( name = "simcompetencyclassid" )
    private int simCompetencyClassId = 0;

    @Column( name = "simcompetencyid" )
    private long simCompetencyId = 0;

    @Column( name = "totalscorableitems" )
    private float totalScorableItems = 0;

    @Column( name = "simletid" )
    private long simletId = 0;

    @Column( name = "simletversionid" )
    private int simletVersionId = 0;

    @Column( name = "simletcompetencyid" )
    private long simletCompetencyId = 0;



    /**
     * this is the total value (total points or total correct
     * used in calculating the score.
     * Across all simlets or aggregate or task competencies only.
     */
    @Column( name = "totalused" )
    private float totalUsed = 0;

    /**
     * This is the max value (max possible points or max correct responses)
     * used in calculating the score.
     * Across all simlets or aggregate or task competencies only.
     */
    @Column( name = "maxvalueused" )
    private float maxValueUsed = 0;

    /**
     * This is the fraction correct or fraction of total points used (if appropriate)
     * Across all simlets or aggregate or task competencies only.
     */
    @Column( name = "fractionused" )
    private float fractionUsed = 0;

    @Column( name = "totalcorrect" )
    private float totalCorrect = 0;

    @Column( name = "totalpoints" )
    private float totalPoints = 0;

    @Column( name = "maxtotalcorrect" )
    private float maxTotalCorrect = 0;

    @Column( name = "maxtotalpoints" )
    private float maxTotalPoints = 0;




    @Column( name = "scoretext" )
    private String scoreText;

    @Column( name = "textbasedresponses" )
    private String textbasedResponses;

    @Column( name = "interviewquestions" )
    private String InterviewQuestions;

    @Column( name = "textparam1" )
    private String textParam1;

    @Column( name = "weight" )
    private float weight;


    @Column( name = "rawscore" )
    private float rawScore;

    @Column( name = "reportid" )
    private long reportId;

    @Column( name = "reportbytes" )
    private byte[] reportBytes;

    /**
     * This is mean used if a normalized scale was used for calculation.
     * Across all simlets or aggregate or task competencies only.
     */
    @Column( name = "mean" )
    private float mean = 0;

    /**
     * This is std deviation used if a normalized scale was used for calculation.
     * Across all simlets or aggregate or task competencies only.
     */
    @Column( name = "stddeviation" )
    private float stdDeviation = 0;
    
    
    @Column( name = "scoretypeidused" )
    private int scoreTypeIdUsed = 0;
    
    /**
     * For Competency Types, this is the scorePresentationTypeId
     * 
     */
    @Column( name = "reportfilecontenttypeid" )
    private int reportFileContentTypeId;

    @Column( name = "reportfilename" )
    private String reportFilename;

    @Column(name="errortxt")
    private String errorTxt;


    @Transient
    private TestEvent testEvent;
    
    @Transient
    private float[] tempFloatParams;

    @Transient
    private float[] profileBoundaries;

    @Transient
    private List<PercentileEntry> percentileEntryList;



    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TestEventScore other = (TestEventScore) obj;
        if (this.testEventScoreId != other.testEventScoreId) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.testEventScoreId ^ (this.testEventScoreId >>> 32));
        return hash;
    }

    @Override
    public int compareTo(TestEventScore o) {

        if( displayOrder != o.getDisplayOrder() )
            return ((Integer) displayOrder ).compareTo( o.getDisplayOrder() );

        if( name != null && !name.isEmpty() && o.getName() != null && !o.getName().isEmpty() )
            return name.compareTo( o.getName() );

        return ((Long)testEventScoreId ).compareTo( o.getTestEventScoreId() );
    }

    @Override
    public String toString() {
        return "TestEventScore{" + "testEventScoreId=" + testEventScoreId + ", testEventId=" + testEventId + ", testEventScoreStatusTypeId=" + testEventScoreStatusTypeId + ", testEventScoreTypeId=" + testEventScoreTypeId + ", name=" + name + ", nameEnglish=" + nameEnglish + '}';
    }

    public boolean getHasValidNorms()
    {
        return getHasValidOverallNorm() ||
            getHasValidCountryNorm() ||
            getHasValidAccountNorm();
    }
    
    public boolean getHasValidOverallNorm()
    {
        return percentile>=0 && ( overallPercentileCount>=Constants.MIN_PERCENTILE_COUNT || overallPercentileCount==0 ) ;
    }

    public boolean getHasValidCountryNorm()
    {
        return countryPercentile>=0 && ( countryPercentileCount>=Constants.MIN_PERCENTILE_COUNT || countryPercentileCount==0 ) ;
    }

    public boolean getHasValidAccountNorm()
    {
        return accountPercentile>=0 && ( accountPercentileCount>=Constants.MIN_PERCENTILE_COUNT || accountPercentileCount==0 ) ;
    }
    

    public String getRawScore4Show(Locale loc, int reportScrDigits)
    {
        return getScoreValue4Show( loc, getOverallRawScoreToShow(), reportScrDigits );
    }
    
    public String getScoreValue4Show( Locale loc, float scr, int reportScrDigits  )
    {
        int scrDigits = reportScrDigits>=0 ? reportScrDigits : ScoreFormatType.getValue( scoreFormatTypeId ).getScorePrecisionDigits();
        
        return I18nUtils.getFormattedNumber( loc, scr, scrDigits);
    }
    
    public float getOverallRawScoreToShow()
    {
        String tpVal = StringUtils.getBracketedArtifactFromString( textParam1, Constants.OVERRIDESHOWRAWSCOREKEY );
        
        if( tpVal!=null && !tpVal.isEmpty() )
        {
            try
            {
                return Float.parseFloat( tpVal );
            }
            catch( NumberFormatException e )
            {
                LogService.logIt( e, "TestEventScore.getOverallRawScoreToShow() Unable to parse detected value=" + tpVal + ", returning recorded raw score=" + this.getRawScore() + ", " + toString() );
            }
        }
        
        return getRawScore();
    }
    
    public List<ScoreCategoryRange> getScoreCatInfoList( int totalRangeWid )
    {
        List<ScoreCategoryRange> out = new ArrayList<>();

        if( textParam1 == null || textParam1.isEmpty() )
            return out;

        String cl = StringUtils.getBracketedArtifactFromString( textParam1 , Constants.CATEGORYINFO );

        if( cl == null || cl.isEmpty() )
            return out;

        String[] sl = cl.split( "~" );

        ScoreCategoryRange scr;

        // LogService.logIt( "TestEventScore.getScoreCateInfoList() TES=" + this.getName() + ", " + this.getScore() + ", " + scr.toString() );
        
        for( String s : sl )
        {
            s = s.trim();

            if( s.isEmpty() )
                continue;

            scr =  new ScoreCategoryRange( s, totalRangeWid );

            // LogService.logIt( "TestEventScore.getScoreCateInfoList() tes=" + this.getName() + ", " + this.getScore() + ", " + scr.toString() );
            
            if( scr.getIsValid() )
                out.add( scr );
        }

        return out;
    }
    
    public ScoreCategoryType getScoreCategoryType()
    {
        return ScoreCategoryType.getValue( scoreCategoryId );
    }
    
    
    
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
    
    
    public boolean getHasScore()
    {
        return ScoreFormatType.getValue( this.scoreFormatTypeId ).getHasValidNumericScore( score );
    }

    
    
    public SimCompetencyClass getSimCompetencyClass()
    {
        return SimCompetencyClass.getValue( this.simCompetencyClassId);
    }
    
    public boolean getHasReport()
    {
        return reportBytes != null && reportBytes.length > 0;
    }

    public boolean getNameEnglishIsDifferent()
    {
        if( nameEnglish==null || nameEnglish.isEmpty() )
            return false;
        return !nameEnglish.equalsIgnoreCase( name );
    }

    public FileContentType getReportFileContentType()
    {
        if( reportFileContentTypeId <= 0 )
            reportFileContentTypeId = FileContentType.DOCUMENT_PDF.getFileContentTypeId();

        return FileContentType.getValue( reportFileContentTypeId );
    }

    public StreamedContent getReportFileForDownload()
    {
        if( !getHasReport() )
            return null;

        if( reportFileContentTypeId <= 0 )
            reportFileContentTypeId = FileContentType.DOCUMENT_PDF.getFileContentTypeId();

        FileContentType fct = getReportFileContentType();

        if( reportFilename == null || reportFilename.trim().isEmpty() )
        {
            String fn1 = name == null ? "report" : name.replaceAll("[^a-zA-Z0-9]", "_");

            reportFilename = fn1 + "." + fct.getBaseExtension();
        }

        ByteArrayInputStream bais = new ByteArrayInputStream( reportBytes );

        return DefaultStreamedContent.builder().contentType(fct.getBaseContentType()).name(reportFilename ).stream( () -> bais ).build(); 
        // return new DefaultStreamedContent(bais, fct.getBaseContentType(), reportFilename );
    }

    public List<TextAndTitle> getTextBasedResponseList( String title, int nonCompetencyItemTypeId, boolean includeFileUploads, boolean fileUploadsOnly, boolean setOrderInInt1, boolean addTestEventResponseRatings)
    {
        List<TextAndTitle> ttl = TestResultUtils.getTextBasedResponseList(textbasedResponses, title, includeFileUploads, fileUploadsOnly, setOrderInInt1 );
        
        // LogService.logIt( "TestEventScore.getTextBasedResponseList() returned " + ttl.size() + ", title=" + title + ", tbr=" + textbasedResponses );
        if( addTestEventResponseRatings && nonCompetencyItemTypeId>=0 && testEvent!=null )
        {
            for( TextAndTitle t : ttl )
            {
                t.setLocale( testEvent.getLocale() );
                
                // Essays that have a sim competency assigned should be caught here. 
                if( simCompetencyId>0 )
                    t.setTestEventResponseRatingList( TestEventResponseRatingUtils.getTestEventResponseRatings( testEvent.getTestEventResponseRatingList(), 0, 0, TestEventResponseRatingType.SIMCOMPETENCY.getTestEventResponseRatingTypeId(), 0, 0, simCompetencyId, nonCompetencyItemTypeId, t.getIntParam1() ));
                
                // Unscored uploads and essays with no sim competency id assigned should be caught here.
                else
                    t.setTestEventResponseRatingList( TestEventResponseRatingUtils.getTestEventResponseRatings( testEvent.getTestEventResponseRatingList(), 0, 0, TestEventResponseRatingType.NONCOMPETENCY.getTestEventResponseRatingTypeId(), 0, 0, 0, nonCompetencyItemTypeId, t.getIntParam1() ));
            }
        }
        
        else if( addTestEventResponseRatings &&  testEvent!=null )
        {
            for( TextAndTitle t : ttl )
            {
                t.setLocale( testEvent.getLocale() );
                
                if( t.getUploadedUserFileId()<=0 && simCompetencyId>0 )
                    t.setTestEventResponseRatingList( TestEventResponseRatingUtils.getTestEventResponseRatings( testEvent.getTestEventResponseRatingList(), 0, 0, TestEventResponseRatingType.SIMCOMPETENCY.getTestEventResponseRatingTypeId(), 0, 0, simCompetencyId, -1, t.getIntParam1() ));
                                
                // This means it's an IVR Audio Sample
                else
                    t.setTestEventResponseRatingList( TestEventResponseRatingUtils.getTestEventResponseRatings( testEvent.getTestEventResponseRatingList(), 0, 0, TestEventResponseRatingType.AVITEMRESPONSE.getTestEventResponseRatingTypeId(), 0, t.getUploadedUserFileId(), 0, -1, t.getIntParam1() ));
                    
            }
        }
        
        return ttl;
    }
    
    public String getTestEventScoreIdEncrypted()
    {
        return encryptStr( this.testEventScoreId );
    }

    public String getTestEventIdEncrypted()
    {
        return encryptStr( this.testEventId );
    }

    private String encryptStr( long id )
    {
        try
        {
            return EncryptUtils.urlSafeEncrypt( Long.toString(id) );
        }
        catch( Exception e )
        {
            LogService.logIt( e, "TestEventScore.enryptStr() " + toString() );

            return "";
        }
    }

    
    public boolean getHasValidOverallZScoreNorm()
    {
        return getOverallZScorePercentileValid()==1f;
    }
    
    public float getOverallZScorePercentile() {
        return totalUsed;
    }

    public void setOverallZScorePercentile(float f) {
        this.totalUsed = f;
    }

    public float getOverallZScorePercentileValid() {
        return fractionUsed;
    }

    public void setOverallZScorePercentileValid(float f) {
        this.fractionUsed = f;
    }
    
    

    public ScoreFormatType getScoreFormatType()
    {
        return ScoreFormatType.getValue( this.scoreFormatTypeId );
    }

    public TestEventScoreType getTestEventScoreType()
    {
        return TestEventScoreType.getValue( testEventScoreTypeId );
    }

    public TestEventScoreStatusType getTestEventScoreStatusType()
    {
        return TestEventScoreStatusType.getValue( testEventScoreStatusTypeId );
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRawScore() {
        return rawScore;
    }

    public void setRawScore(float rawScore) {
        this.rawScore = rawScore;
    }

    public byte[] getReportBytes() {
        return reportBytes;
    }

    public void setReportBytes(byte[] reportBytes) {
        this.reportBytes = reportBytes;
    }

    public int getReportFileContentTypeId() {
        return reportFileContentTypeId;
    }

    public void setReportFileContentTypeId(int reportFileContentTypeId) {
        this.reportFileContentTypeId = reportFileContentTypeId;
    }

    public String getReportFilename() {
        return reportFilename;
    }

    public void setReportFilename(String reportFilename) {
        this.reportFilename = reportFilename;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getScoreText() {
        return scoreText;
    }

    public void setScoreText(String scoreText) {
        this.scoreText = scoreText;
    }

    public TestEvent getTestEvent() {
        return testEvent;
    }

    public void setTestEvent(TestEvent testEvent) {
        this.testEvent = testEvent;
    }

    public long getTestEventId() {
        return testEventId;
    }

    public void setTestEventId(long testEventId) {
        this.testEventId = testEventId;
    }

    public long getTestEventScoreId() {
        return testEventScoreId;
    }

    public void setTestEventScoreId(long testEventScoreId) {
        this.testEventScoreId = testEventScoreId;
    }

    public int getTestEventScoreTypeId() {
        return testEventScoreTypeId;
    }

    public void setTestEventScoreTypeId(int testEventScoreTypeId) {
        this.testEventScoreTypeId = testEventScoreTypeId;
    }

    public int getScoreCategoryId() {
        return scoreCategoryId;
    }

    public void setScoreCategoryId(int scoreCategoryId) {
        this.scoreCategoryId = scoreCategoryId;
    }

    public int getScoreFormatTypeId() {
        return scoreFormatTypeId;
    }

    public void setScoreFormatTypeId(int scoreFormatTypeId) {
        this.scoreFormatTypeId = scoreFormatTypeId;
    }

    public String getInterviewQuestions() {
        return InterviewQuestions;
    }

    public void setInterviewQuestions(String InterviewQuestions) {
        this.InterviewQuestions = InterviewQuestions;
    }

    public int getHide() {
        return hide;
    }

    public void setHide(int hide) {
        this.hide = hide;
    }

    public String getTextbasedResponses() {
        return textbasedResponses;
    }

    public void setTextbasedResponses(String textbasedResponses) {
        this.textbasedResponses = textbasedResponses;
    }

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public int getTestEventScoreStatusTypeId() {
        return testEventScoreStatusTypeId;
    }

    public void setTestEventScoreStatusTypeId(int testEventScoreStatusTypeId) {
        this.testEventScoreStatusTypeId = testEventScoreStatusTypeId;
    }

    public String getErrorTxt() {
        return errorTxt;
    }

    public void setErrorTxt(String errorTxt) {
        this.errorTxt = errorTxt;
    }

    public float getPercentile() {
        return percentile;
    }

    public void setPercentile(float percentile) {
        this.percentile = percentile;
    }

    public float getAccountPercentile() {
        return accountPercentile;
    }

    public void setAccountPercentile(float accountPercentile) {
        this.accountPercentile = accountPercentile;
    }

    public float getCountryPercentile() {
        return countryPercentile;
    }

    public void setCountryPercentile(float countryPercentile) {
        this.countryPercentile = countryPercentile;
    }

    public String getTextParam1() {
        return textParam1;
    }

    public void setTextParam1(String textParam1) {
        this.textParam1 = textParam1;
    }

    public int getOverallPercentileCount() {
        return overallPercentileCount;
    }

    public void setOverallPercentileCount(int overallPercentileCount) {
        this.overallPercentileCount = overallPercentileCount;
    }

    public int getAccountPercentileCount() {
        return accountPercentileCount;
    }

    public void setAccountPercentileCount(int accountPercentileCount) {
        this.accountPercentileCount = accountPercentileCount;
    }

    public int getCountryPercentileCount() {
        return countryPercentileCount;
    }

    public void setCountryPercentileCount(int countryPercentileCount) {
        this.countryPercentileCount = countryPercentileCount;
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

    public float getScore4() {
        return score4;
    }

    public void setScore4(float score4) {
        this.score4 = score4;
    }

    public float getScore5() {
        return score5;
    }

    public void setScore5(float score5) {
        this.score5 = score5;
    }

    public int getSimCompetencyClassId() {
        return simCompetencyClassId;
    }

    public void setSimCompetencyClassId(int simCompetencyClassId) {
        this.simCompetencyClassId = simCompetencyClassId;
    }

    public long getSimCompetencyId() {
        return simCompetencyId;
    }

    public void setSimCompetencyId(long simCompetencyId) {
        this.simCompetencyId = simCompetencyId;
    }

    public float getTotalUsed() {
        return totalUsed;
    }

    public void setTotalUsed(float totalUsed) {
        this.totalUsed = totalUsed;
    }

    public float getMaxValueUsed() {
        return maxValueUsed;
    }

    public void setMaxValueUsed(float maxValueUsed) {
        this.maxValueUsed = maxValueUsed;
    }

    public float getFractionUsed() {
        return fractionUsed;
    }

    public void setFractionUsed(float fractionUsed) {
        this.fractionUsed = fractionUsed;
    }

    public float getTotalCorrect() {
        return totalCorrect;
    }

    public void setTotalCorrect(float totalCorrect) {
        this.totalCorrect = totalCorrect;
    }

    public float getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(float totalPoints) {
        this.totalPoints = totalPoints;
    }

    public float getMaxTotalCorrect() {
        return maxTotalCorrect;
    }

    public void setMaxTotalCorrect(float maxTotalCorrect) {
        this.maxTotalCorrect = maxTotalCorrect;
    }

    public float getMaxTotalPoints() {
        return maxTotalPoints;
    }

    public void setMaxTotalPoints(float maxTotalPoints) {
        this.maxTotalPoints = maxTotalPoints;
    }

    public float getTotalScorableItems() {
        return totalScorableItems;
    }

    public void setTotalScorableItems(float totalScorableItems) {
        this.totalScorableItems = totalScorableItems;
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

    public long getSimletCompetencyId() {
        return simletCompetencyId;
    }

    public void setSimletCompetencyId(long simletComeptencyId) {
        this.simletCompetencyId = simletComeptencyId;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish(String nameenglish) {
        this.nameEnglish = nameenglish;
    }

    public int getHideNumericScore() {
        return hideNumericScore;
    }

    public void setHideNumericScore(int hideNumericScore) {
        this.hideNumericScore = hideNumericScore;
    }

    public int getIntParam1() {
        return intParam1;
    }

    public void setIntParam1(int intParam1) {
        this.intParam1 = intParam1;
    }

    public int getIntParam2() {
        return intParam2;
    }

    public void setIntParam2(int intParam2) {
        this.intParam2 = intParam2;
    }

    public long getLongParam1() {
        return longParam1;
    }

    public void setLongParam1(long longParam1) {
        this.longParam1 = longParam1;
    }

    public long getLongParam2() {
        return longParam2;
    }

    public void setLongParam2(long longParam2) {
        this.longParam2 = longParam2;
    }

    public String getPercentileCountry() {
        return percentileCountry;
    }

    public void setPercentileCountry(String percentileCountry) {
        this.percentileCountry = percentileCountry;
    }

    public String getStrParam1() {
        return strParam1;
    }

    public void setStrParam1(String strParam1) {
        this.strParam1 = strParam1;
    }

    public String getStrParam2() {
        return strParam2;
    }

    public void setStrParam2(String strParam2) {
        this.strParam2 = strParam2;
    }

    public float[] getTempFloatParams() {
        
        if( tempFloatParams==null )
            tempFloatParams = new float[15];
        
        return tempFloatParams;
    }

    public void setTempFloatParams(float[] t) {

        this.tempFloatParams = t;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getScore6() {
        return score6;
    }

    public void setScore6(float score6) {
        this.score6 = score6;
    }

    public float[] getProfileBoundaries() {
        return profileBoundaries;
    }

    public void setProfileBoundaries(float[] profileBoundaries) {
        this.profileBoundaries = profileBoundaries;
    }

    public List<PercentileEntry> getPercentileEntryList() {
        return percentileEntryList;
    }

    public void setPercentileEntryList(List<PercentileEntry> percentileEntryList) {
        this.percentileEntryList = percentileEntryList;
    }

    public float getMean() {
        return mean;
    }

    public void setMean(float mean) {
        this.mean = mean;
    }

    public float getStdDeviation() {
        return stdDeviation;
    }

    public void setStdDeviation(float stdDeviation) {
        this.stdDeviation = stdDeviation;
    }

    public int getScoreTypeIdUsed() {
        return scoreTypeIdUsed;
    }

    public void setScoreTypeIdUsed(int scoreTypeIdUsed) {
        this.scoreTypeIdUsed = scoreTypeIdUsed;
    }

    


}
