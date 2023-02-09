/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.entity.proctor;

import java.io.Serializable;
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

/**
 *
 * @author Mike
 */
@Entity
@Table( name = "remoteproctorevent" )
@NamedQueries( {
    @NamedQuery( name = "RemoteProctorEvent.findByTestEventId", query = "SELECT o FROM RemoteProctorEvent AS o WHERE o.testEventId=:testEventId" )
} )
public class RemoteProctorEvent implements Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "remoteproctoreventid" )
    private long remoteProctorEventId;

    @Column( name = "testkeyid" )
    private long testKeyId;

    @Column( name = "testeventid" )
    private long testEventId;

    @Column( name = "orgid" )
    private int orgId;

    @Column( name = "testserverid" )
    private String testServerId;
    
    @Column( name = "onlineproctoringtypeid" )
    private int onlineProctoringTypeId;
    
    @Column( name = "suspiciousactivitythresholdtypeid" )
    private int suspiciousActivityThresholdTypeId;
    
    /*
     0 = Event Not Completed
     10 = Event completed. 
     20 = Post Media Processing and Conversion Completed
     100 = Analysis Completed
     
    */
    @Column( name = "remoteproctoreventstatustypeid" )
    private int remoteProctorEventStatusTypeId;
    
    @Column( name = "mediatypeid" )
    private int mediaTypeId;
    
    /*
     0 = Not started
     10 = Ready for Post Media Processing
     20 = Post Media Processing Complete
     30 = Conversions Completed    
     100 = All recordings ready     
    */
    @Column( name = "recordingstatustypeid" )
    private int recordingStatusTypeId;

    
    /**
     * Number of uploaded user files.
     */
    @Column( name = "uploadedfilecount" )
    private int uploadedFileCount;
    

    /**
     * 
     */
    @Column( name = "totaldurationsecs" )
    private int totalDurationSecs;
    
    
    /*
     0 = Normal. Minimal or no issues detected.
     10 = Low Risk Issues Detected
     20 = Medium Risk Issues Detected
     30 = High Risk Issues Detected
    */
    @Column( name = "analysisresulttypeid" )
    private int analysisResultTypeId;

    
    @Column( name = "ovserverbaseurl" )
    private String ovServerBaseUrl;
    
    
    
    /**
     * Packed String 
     * 0=suspended, 1=unsuspended
     * 
     * Date (Long MS);0 or 1;Date (Long MS);0 or 1; ..
     */
    @Column( name = "suspensionhistory" )
    private String suspensionHistory;

        
    @Column( name = "unsuspendedsuspiciousactivitycount" )
    private int unsuspendedSuspiciousActivityCount;

    @Column( name = "idfacematchpercent" )
    private float idFaceMatchPercent=-1;

    @Column( name = "idfacematchconfidence" )
    private float idFaceMatchConfidence;

    /**
     * Total number of thumbs found.
     */
    @Column( name = "thumbsprocessed" )
    private int thumbsProcessed;
    
    /**
     * Thumbs that had a face.
     */
    @Column( name = "thumbspassed" )
    private int thumbsPassed;
    
    /**
     * thumb pairs that had same face
     */
    @Column( name = "thumbpairspassed" )
    private int thumbPairsPassed;
    
    /**
     * thumb pairs that had different faces
     */
    @Column( name = "thumbpairsfailed" )
    private int thumbPairsFailed;
    
    /**
     * thumbs that had more than one face
     */
    @Column( name = "multifacethumbs" )
    private int multiFaceThumbs;
        
    /**
     * this is a score based on thumb comparison.
     * 100=same face, consistent.
     * 50 = too many failed images.
     * <50 mult faces found.
     */
    @Column( name = "thumbscore" )
    private float thumbScore;

    /**
     * combination of multi-face, thumbscore, and id score
     * 
     */
    @Column( name = "overallproctorscore" )
    private float overallProctorScore  = -1;

    
    
    
    
    @Column( name = "note" )
    private String note;
    

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="eventcompletedate")
    private Date eventCompleteDate;

    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;

    
    
    @Override
    public String toString() {
        return "RemoteProctorEvent{" + "remoteProctorEventId=" + remoteProctorEventId + ", testEventId=" + testEventId + ", onlineProctoringTypeId=" + onlineProctoringTypeId + ", remoteProctorEventStatusTypeId=" + remoteProctorEventStatusTypeId + ", recordingStatusTypeId=" + recordingStatusTypeId + ", uploadedFileCount=" + uploadedFileCount + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (this.remoteProctorEventId ^ (this.remoteProctorEventId >>> 32));
        hash = 53 * hash + (int) (this.testEventId ^ (this.testEventId >>> 32));
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
        final RemoteProctorEvent other = (RemoteProctorEvent) obj;
        if (this.remoteProctorEventId != other.remoteProctorEventId) {
            return false;
        }
        if (this.testEventId != other.testEventId) {
            return false;
        }
        return true;
    }
                
    
    public long getRemoteProctorEventId() {
        return remoteProctorEventId;
    }

    public void setRemoteProctorEventId(long remoteProctorEventId) {
        this.remoteProctorEventId = remoteProctorEventId;
    }

    public long getTestKeyId() {
        return testKeyId;
    }

    public void setTestKeyId(long testKeyId) {
        this.testKeyId = testKeyId;
    }

    public long getTestEventId() {
        return testEventId;
    }

    public void setTestEventId(long testEventId) {
        this.testEventId = testEventId;
    }

    public int getOnlineProctoringTypeId() {
        return onlineProctoringTypeId;
    }

    public void setOnlineProctoringTypeId(int onlineProctoringTypeId) {
        this.onlineProctoringTypeId = onlineProctoringTypeId;
    }

    public int getSuspiciousActivityThresholdTypeId() {
        return suspiciousActivityThresholdTypeId;
    }

    public void setSuspiciousActivityThresholdTypeId(int suspiciousActivityThresholdTypeId) {
        this.suspiciousActivityThresholdTypeId = suspiciousActivityThresholdTypeId;
    }

    public int getRemoteProctorEventStatusTypeId() {
        return remoteProctorEventStatusTypeId;
    }

    public void setRemoteProctorEventStatusTypeId(int remoteProctorEventStatusTypeId) {
        this.remoteProctorEventStatusTypeId = remoteProctorEventStatusTypeId;
    }

    public int getMediaTypeId() {
        return mediaTypeId;
    }

    public void setMediaTypeId(int mediaTypeId) {
        this.mediaTypeId = mediaTypeId;
    }

    public int getRecordingStatusTypeId() {
        return recordingStatusTypeId;
    }

    public void setRecordingStatusTypeId(int recordingStatusTypeId) {
        this.recordingStatusTypeId = recordingStatusTypeId;
    }

    public int getUploadedFileCount() {
        return uploadedFileCount;
    }

    public void setUploadedFileCount(int uploadedFileCount) {
        this.uploadedFileCount = uploadedFileCount;
    }

    public int getAnalysisResultTypeId() {
        return analysisResultTypeId;
    }

    public void setAnalysisResultTypeId(int analysisResultTypeId) {
        this.analysisResultTypeId = analysisResultTypeId;
    }

    public String getSuspensionHistory() {
        return suspensionHistory;
    }

    public void setSuspensionHistory(String suspensionHistory) {
        this.suspensionHistory = suspensionHistory;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEventCompleteDate() {
        return eventCompleteDate;
    }

    public void setEventCompleteDate(Date eventCompleteDate) {
        this.eventCompleteDate = eventCompleteDate;
    }
    
    public int getTotalDurationSecs() {
        return totalDurationSecs;
    }

    public void setTotalDurationSecs(int totalDurationSecs) {
        this.totalDurationSecs = totalDurationSecs;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getOvServerBaseUrl() {
        return ovServerBaseUrl;
    }

    public void setOvServerBaseUrl(String ovServerBaseUrl) {
        this.ovServerBaseUrl = ovServerBaseUrl;
    }

    public int getUnsuspendedSuspiciousActivityCount() {
        return unsuspendedSuspiciousActivityCount;
    }

    public void setUnsuspendedSuspiciousActivityCount(int unsuspendedSuspiciousActivityCount) {
        this.unsuspendedSuspiciousActivityCount = unsuspendedSuspiciousActivityCount;
    }

    public int getThumbsProcessed() {
        return thumbsProcessed;
    }

    public void setThumbsProcessed(int thumbsProcessed) {
        this.thumbsProcessed = thumbsProcessed;
    }

    public int getThumbsPassed() {
        return thumbsPassed;
    }

    public void setThumbsPassed(int thumbsPassed) {
        this.thumbsPassed = thumbsPassed;
    }

    public int getThumbPairsPassed() {
        return thumbPairsPassed;
    }

    public void setThumbPairsPassed(int thumbPairsPassed) {
        this.thumbPairsPassed = thumbPairsPassed;
    }

    public int getThumbPairsFailed() {
        return thumbPairsFailed;
    }

    public void setThumbPairsFailed(int thumbPairsFailed) {
        this.thumbPairsFailed = thumbPairsFailed;
    }

    public int getMultiFaceThumbs() {
        return multiFaceThumbs;
    }

    public void setMultiFaceThumbs(int multiFaceThumbs) {
        this.multiFaceThumbs = multiFaceThumbs;
    }

    public String getTestServerId() {
        return testServerId;
    }

    public void setTestServerId(String testServerId) {
        this.testServerId = testServerId;
    }

    public float getIdFaceMatchPercent() {
        return idFaceMatchPercent;
    }

    public void setIdFaceMatchPercent(float idFaceMatchPercent) {
        this.idFaceMatchPercent = idFaceMatchPercent;
    }

 

    public float getIdFaceMatchConfidence() {
        return idFaceMatchConfidence;
    }

    public void setIdFaceMatchConfidence(float idFaceMatchConfidence) {
        this.idFaceMatchConfidence = idFaceMatchConfidence;
    }

    
    
    
}
