package com.tm2batch.entity.profile;


import com.tm2batch.service.LogService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
@Table( name = "profileentry" )
@NamedQueries({
    @NamedQuery ( name="ProfileEntry.findByProfileId", query="SELECT o FROM ProfileEntry AS o WHERE o.profileId=:profileId" )
})
public class ProfileEntry implements Serializable, Comparable<ProfileEntry>
{
    @Transient
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="profileentryid")
    private int profileEntryId;

    @Column(name="profileid")
    private int profileId;

    @Column(name="computemethodtypeid")
    private int computeMethodTypeId;

    @Column(name="profileentrytypeid")
    private int profileEntryTypeId;

    
    /**
     * This is an agg string of ranges and scores. r18,ry28,y48,yg75,g85,r95
     *
     * Will be used in place of score category ranges in Sim Competency.
     */
    @Column(name="scorecategoryrangestr")
    private String scoreCategoryRangeStr;

    /**
     * This is the weight to use for this competency (in place of the SimCompetency Weight) when computing an overall score.
     */
    @Column(name="weight")
    private float weight;



    @Column(name="lowval")
    private float lowVal;

    @Column(name="highval")
    private float highVal;

    @Column(name="mean")
    private float mean;

    @Column(name="stddev")
    private float stdDev;

    @Column(name="name")
    private String name;

    @Column(name="nameenglish")
    private String nameEnglish;    
    
    @Column(name="note")
    private String note;
    
    @Column(name="intparam1")
    private int intParam1;

    @Column(name="intparam2")
    private int intParam2;

    @Column(name="floatparam1")
    private int floatParam1;

    @Column(name="floatparam2")
    private int floatParam2;

    @Column(name="floatparam3")
    private int floatParam3;

    @Column(name="strparam1")
    private String strParam1;

    @Column(name="strparam2")
    private String strParam2;

    
    

    @Override
    public String toString() {
        return "ProfileEntry{" + "profileEntryId=" + profileEntryId + ", profileId=" + profileId + ", name=" + name + '}';
    }


    public boolean getHasHiLowData()
    {
        return lowVal != highVal;
    }

    
    /**
     * scoreCategoryRangeStr is an agg string of ranges and scores. 
     * 
     * Examples - 
     * 
     * Simple 5 colors: r18,ry28,y48,yg75,g85

     * 5 color + Red Cliff: r18,ry28,y48,yg75,g85,r95

     * 5 color + Yellow Cliff: r18,ry28,y48,yg75,g85,y95
      
     * Normal 5 color: r18,y28,g48,y75,r85

     * Will be used in place of score category ranges in Sim Competency.
     */
    public List[] parseColorStr() throws Exception
    {

        List[] out = new List[] {new ArrayList<>(),new ArrayList<>()} ;

        try
        {
            String t = this.scoreCategoryRangeStr;

            if( t == null )
                t = "";

            else
                t = t.trim();

            if( t.isEmpty() )
                return out;

            // LogService.logIt( "CT2GraphicsServlet.doGet() sections=" + t );

            String[] ts = t.split(",");

            // size in pixels
            float score;

            // 1=red, 2=red yellow, 3=yellow, 4=yellow green, 5=green
            int color;

            List<Integer> colors = out[0];
            List<Float> scores = out[1];

            for( String c : ts )
            {
                if( c==null || c.trim().isEmpty() )
                    continue;

                color = 0;
                score=0;

                c = c.trim().toLowerCase();

                if( c.startsWith("ry") )
                {
                    color = 2;
                    score = Float.parseFloat( c.substring(2, c.length() ) );
                }

                else if( c.startsWith("r") )
                {
                    color = 1;
                    score = Float.parseFloat( c.substring(1, c.length() ) );
                }

                else if( c.startsWith("yg") )
                {
                    color = 4;
                    score = Float.parseFloat( c.substring(2, c.length() ) );
                }

                else if( c.startsWith("y") )
                {
                    color = 3;
                    score = Float.parseFloat( c.substring(1, c.length() ) );
                }

                else if( c.startsWith("g") )
                {
                    color = 5;
                    score = Float.parseFloat( c.substring(1, c.length() ) );
                }

                else
                    continue;

                if( color>0 && score>0 )
                {
                    colors.add( color );
                    scores.add(score );
                }
            }
        }

        catch( Exception e )
        {
            LogService.logIt( e, "ProfleEntry.parseColorStr() " + this.toString() + ", scoreCategoryRangeStr=" + scoreCategoryRangeStr );

            throw new Exception( "Unable to parse the scoreCategoryRangeStr=" + scoreCategoryRangeStr + " on profile Entry " + toString() + ", " + e.toString() );
        }

        return out;

    }
    

    @Override
    public int compareTo(ProfileEntry o) {

        if( name != null && !name.isEmpty() && o.getName()!=null && !o.getName().isEmpty() )
            return name.compareTo( o.getName() );

        return new Integer( profileEntryId ).compareTo( o.getProfileEntryId() );
    }

    public int getProfileEntryId() {
        return profileEntryId;
    }

    public void setProfileEntryId(int profileEntryId) {
        this.profileEntryId = profileEntryId;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getComputeMethodTypeId() {
        return computeMethodTypeId;
    }

    public void setComputeMethodTypeId(int computeMethodTypeId) {
        this.computeMethodTypeId = computeMethodTypeId;
    }

    public float getLowVal() {
        return lowVal;
    }

    public void setLowVal(float lowVal) {
        this.lowVal = lowVal;
    }

    public float getHighVal() {
        return highVal;
    }

    public void setHighVal(float highVal) {
        this.highVal = highVal;
    }

    public float getMean() {
        return mean;
    }

    public void setMean(float mean) {
        this.mean = mean;
    }

    public float getStdDev() {
        return stdDev;
    }

    public void setStdDev(float stdDev) {
        this.stdDev = stdDev;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getScoreCategoryRangeStr() {
        return scoreCategoryRangeStr;
    }

    public void setScoreCategoryRangeStr(String scoreCategoryRangeStr) {
        this.scoreCategoryRangeStr = scoreCategoryRangeStr;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getProfileEntryTypeId() {
        return profileEntryTypeId;
    }

    public void setProfileEntryTypeId(int profileEntryTypeId) {
        this.profileEntryTypeId = profileEntryTypeId;
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

    public int getFloatParam1() {
        return floatParam1;
    }

    public void setFloatParam1(int floatParam1) {
        this.floatParam1 = floatParam1;
    }

    public int getFloatParam2() {
        return floatParam2;
    }

    public void setFloatParam2(int floatParam2) {
        this.floatParam2 = floatParam2;
    }

    public int getFloatParam3() {
        return floatParam3;
    }

    public void setFloatParam3(int floatParam3) {
        this.floatParam3 = floatParam3;
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

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }



}
