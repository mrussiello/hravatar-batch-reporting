/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.entity.file;

import com.tm2batch.file.ConversionStatusType;
import com.tm2batch.file.FileContentType;
import com.tm2batch.file.UploadedFileProcessingType;
import com.tm2batch.file.UploadedUserFileStatusType;
import com.tm2batch.file.UploadedUserFileType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
@Table( name = "uploadeduserfile" )
@NamedQueries( {    
    @NamedQuery( name = "UploadedUserFile.findByTestEventIdAndUploadedUserFileType", query = "SELECT o FROM UploadedUserFile AS o WHERE o.testEventId=:testEventId AND o.uploadedUserFileTypeId=:uploadedUserFileTypeId ORDER BY o.uploadedUserFileId" ),
    @NamedQuery( name = "UploadedUserFile.findByTestKeyIdAndUploadedUserFileType", query = "SELECT o FROM UploadedUserFile AS o WHERE o.testKeyId=:testKeyId AND o.uploadedUserFileTypeId=:uploadedUserFileTypeId ORDER BY o.uploadedUserFileId" ),
    @NamedQuery( name = "UploadedUserFile.findByTestKeyIdAndUploadedUserFileTypeWithMaxTestEventId", query = "SELECT o FROM UploadedUserFile AS o WHERE o.testKeyId=:testKeyId AND o.uploadedUserFileTypeId=:uploadedUserFileTypeId AND o.testEventId<=:maxTestEventId ORDER BY o.uploadedUserFileId" )
} )
public class UploadedUserFile implements Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "uploadeduserfileid" )
    private long uploadedUserFileId;

    @Column( name = "uploadeduserfilestatustypeid" )
    private int uploadedUserFileStatusTypeId;

    @Column( name = "uploadeduserfiletypeid" )
    private int uploadedUserFileTypeId;
    
    @Column( name = "testkeyid" )
    private long testKeyId;
    
    @Column( name = "testeventid" )
    private long testEventId;

    @Column( name = "actid" )
    private long actId;

    @Column( name = "nodeseq" )
    private int nodeSeq;

    @Column( name = "subnodeseq" )
    private int subnodeSeq;

    @Column( name = "orientation" )
    private int orientation;
    
    @Column( name = "fileprocessingtypeid" )
    private int fileProcessingTypeId;

    @Column( name = "avitemresponseid" )
    private long avItemResponseId;
    
    @Column( name = "avitemtypeid" )
    private int avItemTypeId;

    
    @Column( name = "initialfilesize" )
    private int initialFileSize;

    @Column( name = "initialfilename" )
    private String initialFilename;

    @Column( name = "initialfilecontenttypeid" )
    private int initialFileContentTypeId;

    
    /**
     * 0=uploaded
     * 10=deleted
     */
    @Column( name = "initialfilestatustypeid" )
    private int initialFileStatusTypeId;

    @Column( name = "initialmime" )
    private String initialMime;

    @Column( name = "originalsavedfilename" )
    private String originalSavedFilename;

    
    
    @Column( name = "filename" )
    private String filename;

    @Column( name = "mime" )
    private String mime;

    @Column( name = "audiofilename" )
    private String audioFilename;

    @Column( name = "audiofilecontenttypeid" )
    private int audioFileContentTypeId;
           
    @Column( name = "audiosize" )
    private int audioSize;

    @Column( name = "filecontenttypeid" )
    private int fileContentTypeId;

    @Column( name = "thumbfilename" )
    private String thumbFilename;    
    
    @Column( name = "thumbwidth" )
    private int thumbWidth;    
    
    @Column( name = "thumbheight" )
    private int thumbHeight;    

    @Column( name = "maxthumbindex" )
    private int maxThumbIndex;    
    
    @Column( name = "failedthumbindices" )
    private String failedThumbIndices;    

    @Column( name = "pretestthumbindices" )
    private String preTestThumbIndices;    
        
    @Column( name = "conversionstatustypeid" )
    private int conversionStatusTypeId;

    @Column( name = "r1" )
    private int r1;

    @Column( name = "r2" )
    private long r2;

    @Column( name = "simid" )
    private long simId;

    @Column( name = "simversionid" )
    private int simVersionId;
    
    @Column( name = "productid" )
    private int productId;

    /*
     name~value~name~value etc.
    */
    @Column( name = "processingparams" )
    private String processingParams;

    @Column( name = "errorcount" )
    private int errorCount;
    
    /*
     name~name~name etc.
    */
    @Column( name = "filestodelete" )
    private String filesToDelete;

    /*
     
    */
    @Column( name = "needsfiledelete" )
    private int needsFileDelete;
    

    @Column( name = "width" )
    private int width;

    @Column( name = "height" )
    private int height;

    @Column( name = "rotation" )
    private int rotation;

    
    @Column( name = "duration" )
    private float duration;

    @Column( name = "note" )
    private String note;

    /**
     * For images, this is a type indicator
     *    0=unknown
     *    1=face but face not detected. 
     *    2=face but low confidence.
     *    3=face but multi faces.
     * 
     *    20=face detected. 
     * 
     *    
     */
    @Column( name = "meta1" )
    private float meta1;

    /**
     * For images with faces, this is confidence
     *    
     */
    @Column( name = "meta2" )
    private float meta2;

    /**
     * For images with faces, this is pitch
     * For images with multiple faces, this is the number of faces
     *    
     */
    @Column( name = "meta3" )
    private float meta3;

    /**
     * For images with faces, this is yaw
     *    
     */
    @Column( name = "meta4" )
    private float meta4;
    

    @Column( name = "meta5" )
    private float meta5;

    
    

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastupload")
    private Date lastUpload;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="conversionstatusdate")
    private Date conversionStatusDate;

        
    //@Transient
    //private Object[] faceDetails;
    
    @Transient
    private List<Integer> validIndexList;
    
    @Transient
    private Map<Integer,Integer> failedIndexMap;
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UploadedUserFile other = (UploadedUserFile) obj;
        if (this.uploadedUserFileId != other.uploadedUserFileId) {
            return false;
        }
        if (this.actId != other.actId) {
            return false;
        }
        return true;
    }

    
    public FileContentType getFileContentType()
    {
        return FileContentType.getValue( this.fileContentTypeId );
    }
    
    public UploadedUserFileStatusType getUploadedUserFileStatusType()
    {
        return UploadedUserFileStatusType.getValue( this.uploadedUserFileStatusTypeId );
    }

    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (this.uploadedUserFileId ^ (this.uploadedUserFileId >>> 32));
        hash = 89 * hash + (int) (this.actId ^ (this.actId >>> 32));
        return hash;
    }


    @Override
    public String toString() {
        return "UploadedUserFile {" + "uploadedUserFileId=" + uploadedUserFileId + ", testEventId=" + testEventId + ", actId=" + actId + ", nodeSeq=" + nodeSeq + ", subnodeSeq=" + subnodeSeq + ", initialFileSize=" + initialFileSize + ", initialFilename=" + initialFilename + ", initialFileContentTypeId=" + initialFileContentTypeId + ", initialMime=" + initialMime + ", filename=" + filename + ", mime=" + mime + ", fileContentTypeId=" + fileContentTypeId + ", conversionStatusTypeId=" + conversionStatusTypeId + ", r1=" + r1 + ", r2=" + r2 + '}';
    }

    public boolean isClicFlic()
    {
        return false;
    }

    public boolean hasFailedIndex( int idx )
    {
        if( failedIndexMap==null )
            initFailedIndexMap();

        return failedIndexMap.containsKey( idx );
    }

    public int getFailedIndexErrorTypeId( int idx )
    {
        if( failedIndexMap==null )
            return -1;

        Integer v = failedIndexMap.get( idx );
        return v==null ? -1 : v;
    }
    

    public void addFailedIndex( int idx, int proctorImageErrorTypeId )
    {
        if( failedIndexMap==null )
            initFailedIndexMap();
        
        if( idx>0 )
            failedIndexMap.put( idx, proctorImageErrorTypeId );
    }
    
    public synchronized void initFailedIndexMap()
    {
        if( failedIndexMap==null )
            failedIndexMap = new TreeMap<>();
        
        if( failedThumbIndices==null || failedThumbIndices.isBlank() )
            return;
        
        int errorTypeId;
        int idx;
        
        for( String s : failedThumbIndices.split(",") )
        {
            if( s.isBlank() )
                continue;
            
            errorTypeId=0;
            if( s.indexOf(":")>0 )
            {
                idx = Integer.parseInt( s.substring(0, s.indexOf(":")));
                errorTypeId = Integer.parseInt( s.substring(s.indexOf(":")+1,s.length()));
            }    
            else
                idx = Integer.parseInt( s );
            
            failedIndexMap.put( idx, errorTypeId );
        }
    }

    public void saveFailedIndexMap()
    {
        if( failedIndexMap==null || failedIndexMap.isEmpty() )
        {
            failedThumbIndices=null;
            return;
        }
        
        Integer val;
        StringBuilder sb = new StringBuilder();
        for( Integer i : failedIndexMap.keySet() )
        {
            val = failedIndexMap.get(i);
            if( val==null )
                val = 0;
            if( sb.length()>0 )
                sb.append(",");
            sb.append( i.toString() + ":" + val.toString() );
        }
        failedThumbIndices = sb.length()>0 ? sb.toString() : null;
    }

    
    public UploadedUserFileType getUploadedUserFileType()
    {
        return UploadedUserFileType.getValue( this.uploadedUserFileTypeId );
    }
    
    public UploadedFileProcessingType getFileProcessingType()
    {
        return UploadedFileProcessingType.getValue( fileProcessingTypeId );
    }
    
    
    public ConversionStatusType getConversionStatusType(){
        return ConversionStatusType.getValue( conversionStatusTypeId );
    }
    

    public String getDirectory()
    {
        if( getUploadedUserFileType().getIsResponse() )
            return "/" + r1 + "/" + r2 + "/" + actId + "/" + testEventId;

        else if( getUploadedUserFileType().getIsAnyPremiumRemoteProctoring() )
            return "/" + r1 + "/" + r2;   
        
        return "/" + r1 + "/" + r2 + "/" + actId + "/" + testEventId;
    }


    public long getUploadedUserFileId() {
        return uploadedUserFileId;
    }

    public void setUploadedUserFileId(long uploadedUserFileId) {
        this.uploadedUserFileId = uploadedUserFileId;
    }


    public long getActId() {
        return actId;
    }

    public void setActId(long actId) {
        this.actId = actId;
    }

    public int getNodeSeq() {
        return nodeSeq;
    }

    public void setNodeSeq(int nodeSeq) {
        this.nodeSeq = nodeSeq;
    }

    public int getSubnodeSeq() {
        return subnodeSeq;
    }

    public void setSubnodeSeq(int subnodeSeq) {
        this.subnodeSeq = subnodeSeq;
    }

    public int getInitialFileSize() {
        return initialFileSize;
    }

    public void setInitialFileSize(int initialFileSize) {
        this.initialFileSize = initialFileSize;
    }

    public String getInitialFilename() {
        return initialFilename;
    }

    public void setInitialFilename(String initialFilename) {
        this.initialFilename = initialFilename;
    }

    public int getInitialFileContentTypeId() {
        return initialFileContentTypeId;
    }

    public void setInitialFileContentTypeId(int initialFileContentTypeId) {
        this.initialFileContentTypeId = initialFileContentTypeId;
    }

    public String getInitialMime() {
        return initialMime;
    }

    public void setInitialMime(String initialMime) {
        this.initialMime = initialMime;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public int getFileContentTypeId() {
        return fileContentTypeId;
    }

    public void setFileContentTypeId(int fileContentTypeId) {
        this.fileContentTypeId = fileContentTypeId;
    }

    public int getConversionStatusTypeId() {
        return conversionStatusTypeId;
    }

    public void setConversionStatusTypeId(int s) {
        
        if( s!=conversionStatusTypeId || conversionStatusDate==null )
            conversionStatusDate=new Date();
        
        this.conversionStatusTypeId = s;
    }

    public int getR1() {
        return r1;
    }

    public void setR1(int r1) {
        this.r1 = r1;
    }

    public long getR2() {
        return r2;
    }

    public void setR2(long r2) {
        this.r2 = r2;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpload() {
        return lastUpload;
    }

    public void setLastUpload(Date lastUpload) {
        this.lastUpload = lastUpload;
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAudioFilename() {
        return audioFilename;
    }

    public void setAudioFilename(String audioFilename) {
        this.audioFilename = audioFilename;
    }

    public int getAudioFileContentTypeId() {
        return audioFileContentTypeId;
    }

    public void setAudioFileContentTypeId(int audioFileContentTypeId) {
        this.audioFileContentTypeId = audioFileContentTypeId;
    }

    public int getFileProcessingTypeId() {
        return fileProcessingTypeId;
    }

    public void setFileProcessingTypeId(int fileProcessingTypeId) {
        this.fileProcessingTypeId = fileProcessingTypeId;
    }

    public long getAvItemResponseId() {
        return avItemResponseId;
    }

    public void setAvItemResponseId(long avItemResponseId) {
        this.avItemResponseId = avItemResponseId;
    }

    public int getSimVersionId() {
        return simVersionId;
    }

    public void setSimVersionId(int simVersionId) {
        this.simVersionId = simVersionId;
    }

    public int getInitialFileStatusTypeId() {
        return initialFileStatusTypeId;
    }

    public void setInitialFileStatusTypeId(int initialFileStatusTypeId) {
        this.initialFileStatusTypeId = initialFileStatusTypeId;
    }

    public String getThumbFilename() {
        return thumbFilename;
    }

    public void setThumbFilename(String thumbFilename) {
        this.thumbFilename = thumbFilename;
    }

    public int getThumbWidth() {
        return thumbWidth;
    }

    public void setThumbWidth(int thumbWidth) {
        this.thumbWidth = thumbWidth;
    }

    public int getThumbHeight() {
        return thumbHeight;
    }

    public void setThumbHeight(int thumbHeight) {
        this.thumbHeight = thumbHeight;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getOriginalSavedFilename() {
        return originalSavedFilename;
    }

    public void setOriginalSavedFilename(String originalSavedFilename) {
        this.originalSavedFilename = originalSavedFilename;
    }

    public String getProcessingParams() {
        return processingParams;
    }

    public void setProcessingParams(String processingParams) {
        this.processingParams = processingParams;
    }

    public String getFilesToDelete() {
        return filesToDelete;
    }

    public void setFilesToDelete(String filesToDelete) {
        this.filesToDelete = filesToDelete;
    }

    public int getNeedsFileDelete() {
        return needsFileDelete;
    }

    public void setNeedsFileDelete(int needsFileDelete) {
        this.needsFileDelete = needsFileDelete;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getUploadedUserFileStatusTypeId() {
        return uploadedUserFileStatusTypeId;
    }

    public void setUploadedUserFileStatusTypeId(int uploadedUserFileStatusTypeId) {
        this.uploadedUserFileStatusTypeId = uploadedUserFileStatusTypeId;
    }

    public int getAvItemTypeId() {
        return avItemTypeId;
    }

    public void setAvItemTypeId(int avItemTypeId) {
        this.avItemTypeId = avItemTypeId;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public Date getConversionStatusDate() {
        return conversionStatusDate;
    }

    public void setConversionStatusDate(Date conversionStatusDate) {
        this.conversionStatusDate = conversionStatusDate;
    }

    public int getAudioSize() {
        return audioSize;
    }

    public void setAudioSize(int audioSize) {
        this.audioSize = audioSize;
    }

    public int getUploadedUserFileTypeId() {
        return uploadedUserFileTypeId;
    }

    public void setUploadedUserFileTypeId(int uploadedUserFileTypeId) {
        this.uploadedUserFileTypeId = uploadedUserFileTypeId;
    }

    public int getMaxThumbIndex() {
        return maxThumbIndex;
    }

    public void setMaxThumbIndex(int maxThumbIndex) {
        this.maxThumbIndex = maxThumbIndex;
    }

    public List<Integer> getValidIndexList() {
        return validIndexList;
    }

    public void setValidIndexList(List<Integer> validIndexList) {
        this.validIndexList = validIndexList;
    }

    public long getTestKeyId() {
        return testKeyId;
    }

    public void setTestKeyId(long testKeyId) {
        this.testKeyId = testKeyId;
    }

    public float getMeta1() {
        return meta1;
    }

    public void setMeta1(float meta1) {
        this.meta1 = meta1;
    }

    public float getMeta2() {
        return meta2;
    }

    public void setMeta2(float meta2) {
        this.meta2 = meta2;
    }

    public float getMeta3() {
        return meta3;
    }

    public void setMeta3(float meta3) {
        this.meta3 = meta3;
    }

    public float getMeta4() {
        return meta4;
    }

    public void setMeta4(float meta4) {
        this.meta4 = meta4;
    }

    public float getMeta5() {
        return meta5;
    }

    public void setMeta5(float meta5) {
        this.meta5 = meta5;
    }


    public String getFailedThumbIndices() {
        return failedThumbIndices;
    }

    public void setFailedThumbIndices(String failedThumbIndices) {
        this.failedThumbIndices = failedThumbIndices;
    }

    public String getPreTestThumbIndices() {
        return preTestThumbIndices;
    }

    public void setPreTestThumbIndices(String preTestThumbIndices) {
        this.preTestThumbIndices = preTestThumbIndices;
    }

    public Map<Integer, Integer> getFailedIndexMap() {
        return failedIndexMap;
    }

    public void setFailedIndexMap(Map<Integer, Integer> failedIndexMap) {
        this.failedIndexMap = failedIndexMap;
    }
    
}
