package com.tm2batch.entity.event;


import com.tm2batch.entity.user.User;
import com.tm2batch.event.TestEventResponseRatingType;
import java.io.Serializable;
import java.util.Date;

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
@Table( name="testeventresponserating" )
@NamedQueries({
    @NamedQuery ( name="TestEventResponseRating.findForTestEventId", query="SELECT o FROM TestEventResponseRating AS o WHERE o.testEventId = :testEventId ORDER BY o.createDate" )
})
public class TestEventResponseRating implements Serializable, Comparable<TestEventResponseRating>
{
    @Transient
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="testeventresponseratingid")
    private long testEventResponseRatingId;

    /*
     * 0=uploadeduserfile
       1=avitemresponse
       2=sim competency
       3=non-competency
    */
    @Column(name="testeventresponseratingtypeid")
    private int testEventResponseRatingTypeId;
    
    @Column(name="userid")
    private long userId;

    @Column(name="testeventid")
    private long testEventId;
    
    @Column(name="avitemresponseid")
    private long avItemResponseId = -1;
    
    @Column(name="uploadeduserfileid")
    private long uploadedUserFileId = -1;
    

    @Column(name="simcompetencyid")
    private long simCompetencyId = -1;
    
    @Column(name="noncompetencyitemtypeid")
    private int nonCompetencyItemTypeId = -1;
    
    /**
     * Used for sim competencies or non-competency ratings. 1-based sequence id
     */
    @Column(name="sequenceid")
    private int sequenceId = -1;
    
    
    @Column(name="rating")
    private float rating;

    @Column(name="rating2")
    private float rating2;

    @Column(name="rating3")
    private float rating3;

    @Column(name="note")
    private String note;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastupdate")
    private Date lastUpdate;
    
    
    @Transient
    private User user;

    @Override
    public int compareTo(TestEventResponseRating o) {

        if( createDate != null && o.getCreateDate() != null )
            return createDate.compareTo( o.getCreateDate() );

        if( testEventResponseRatingId>0 && o.getTestEventResponseRatingId()>0 )
            return ((Long) testEventResponseRatingId).compareTo( o.getTestEventResponseRatingId() );

        return ((Long) userId).compareTo( o.getUserId() );
    }

    public TestEventResponseRatingType getTestEventResponseRatingType()
    {
        return TestEventResponseRatingType.getValue( testEventResponseRatingTypeId );
    }
    
    public long getTestEventResponseRatingId() {
        return testEventResponseRatingId;
    }

    public void setTestEventResponseRatingId(long testEventResponseRatingId) {
        this.testEventResponseRatingId = testEventResponseRatingId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTestEventId() {
        return testEventId;
    }

    public void setTestEventId(long testEventId) {
        this.testEventId = testEventId;
    }

    public long getAvItemResponseId() {
        return avItemResponseId;
    }

    public void setAvItemResponseId(long avItemResponseId) {
        this.avItemResponseId = avItemResponseId;
    }

    public long getUploadedUserFileId() {
        return uploadedUserFileId;
    }

    public void setUploadedUserFileId(long uploadedUserFileId) {
        this.uploadedUserFileId = uploadedUserFileId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getRating2() {
        return rating2;
    }

    public void setRating2(float rating2) {
        this.rating2 = rating2;
    }

    public float getRating3() {
        return rating3;
    }

    public void setRating3(float rating3) {
        this.rating3 = rating3;
    }

    public String getNote() {
        
        if( note!=null && note.isBlank() )
            note=null;
        
        return note;
    }

    public void setNote(String note) {

        if( note!=null && note.isBlank() )
            note=null;
        
        this.note = note;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getSimCompetencyId() {
        return simCompetencyId;
    }

    public void setSimCompetencyId(long simCompetencyId) {
        this.simCompetencyId = simCompetencyId;
    }

    public int getNonCompetencyItemTypeId() {
        return nonCompetencyItemTypeId;
    }

    public void setNonCompetencyItemTypeId(int nonCompetencyItemTypeId) {
        this.nonCompetencyItemTypeId = nonCompetencyItemTypeId;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public int getTestEventResponseRatingTypeId() {
        return testEventResponseRatingTypeId;
    }

    public void setTestEventResponseRatingTypeId(int testEventResponseRatingTypeId) {
        this.testEventResponseRatingTypeId = testEventResponseRatingTypeId;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }



}
