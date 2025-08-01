package com.tm2batch.entity.ref;


import com.tm2batch.entity.user.User;
import com.tm2batch.ref.RcCheckUtils;
import com.tm2batch.ref.RcItemFormatType;
import com.tm2batch.ref.RcRatingStatusType;
import com.tm2batch.service.LogService;
import com.tm2batch.util.StringUtils;
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
@Table( name = "rcrating" )
@NamedQueries({
        @NamedQuery( name = "RcRating.findByRcCheckAndRater", query = "SELECT o FROM RcRating AS o WHERE o.rcCheckId=:rcCheckId AND o.rcRaterId=:rcRaterId" ),    
        @NamedQuery( name = "RcRating.findByRcCheckId", query = "SELECT o FROM RcRating AS o WHERE o.rcCheckId=:rcCheckId" )   
})
public class RcRating implements Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="rcratingid")
    private long rcRatingId;

    @Column(name="rcraterid")
    private long rcRaterId;
    
    @Column(name="rccheckid")
    private long rcCheckId;
        
    @Column(name="rcitemid")
    private int rcItemId;

    @Column(name="rcitemformattypeid")
    private int rcItemFormatTypeId;

    @Column(name="selectedresponse")
    private String selectedResponse;

    @Column(name="displayorder")
    private int displayorder;

    @Column(name="rcratingstatustypeid")
    private int rcRatingStatusTypeId;

    @Column(name="score")
    private float score;
    
    @Column(name="uploadeduserfileid")
    private long uploadedUserFileId;
    

    @Column(name="text")
    private String text;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastupdate")
    private Date lastUpdate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="completedate")
    private Date completeDate;
    
    
    
    @Transient
    private User user;
    
    @Transient
    private RcItem rcItem;
    
    @Transient
    private String subtext;
    


    public String getSelectedChoicesText()
    {
        if( rcItem==null || !this.getHasData() || !rcItem.getRcItemFormatType().getHasChoices()  || this.getRcRatingStatusType().getIsSkipped() )
            return "";
        if( this.selectedResponse==null || this.selectedResponse.isBlank() )
            return "";
        StringBuilder sb = new StringBuilder();

        try
        {
            int idx;
            String chc;
            for( String s : selectedResponse.split(","))
            {
                if( s.isBlank() )
                    continue;
                idx=Integer.parseInt(s);
                chc = rcItem.getChoiceForIndex(idx);
                if( chc!=null && !chc.isBlank() )
                {
                    if( sb.length()>0 )
                        sb.append(",\n");
                    
                    sb.append( chc );
                }
            }
        }
        catch(NumberFormatException e)
        {
            LogService.logIt("RcRating.selectedChoicesText() Unable to parse selectedResponse=" + selectedResponse + ", rcRatingId=" + this.rcRatingId + ", rcCheckId=" + rcCheckId );
        }
        return sb.toString();
    }

    //public String getScoreRounded()
    //{
    //    return NumberUtils.getTwoDecimalFormattedAmount( score );
    //}
    
    public float getFinalScore()
    {
        if( rcItem==null || !rcItem.getRcItemFormatType().getIsRating() || rcItem.getIntParam1()!=1 )
            return score;
        
        return RcCheckUtils.invertRatingScore( score );
    }
    
    
    @Override
    public String toString() {
        return "RcRating{" + "rcRatingId=" + rcRatingId + ", rcRaterId=" + rcRaterId + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (int) (this.rcRatingId ^ (this.rcRatingId >>> 32));
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
        final RcRating other = (RcRating) obj;
        if (this.rcRatingId != other.rcRatingId) {
            return false;
        }
        return true;
    }
    
    public boolean getHasNumericScore()
    {
        return this.score>0; 
    }

    public RcItemFormatType getRcItemFormatType()
    {
        return RcItemFormatType.getValue(this.rcItemFormatTypeId);
    }
    
    public RcRatingStatusType getRcRatingStatusType()
    {
        return RcRatingStatusType.getValue(this.rcRatingStatusTypeId);
    }
    
    
    public boolean getHasData()
    {
        return getRcRatingStatusType().getIsComplete();  
    }
    
    public boolean getHasScore()
    {
        return this.score!=0; 
    }
    
    public long getRcRatingId() {
        return rcRatingId;
    }

    public void setRcRatingId(long rcRatingId) {
        this.rcRatingId = rcRatingId;
    }

    public long getRcRaterId() {
        return rcRaterId;
    }

    public void setRcRaterId(long rcRaterId) {
        this.rcRaterId = rcRaterId;
    }

    public long getRcCheckId() {
        return rcCheckId;
    }

    public void setRcCheckId(long rcCheckId) {
        this.rcCheckId = rcCheckId;
    }

    public int getRcItemId() {
        return rcItemId;
    }

    public void setRcItemId(int rcItemId) {
        this.rcItemId = rcItemId;
    }

    public int getDisplayorder() {
        return displayorder;
    }

    public void setDisplayorder(int displayorder) {
        this.displayorder = displayorder;
    }

    public int getRcRatingStatusTypeId() {
        return rcRatingStatusTypeId;
    }

    public void setRcRatingStatusTypeId(int rcRatingStatusTypeId) {
        this.rcRatingStatusTypeId = rcRatingStatusTypeId;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getUploadedUserFileId() {
        return uploadedUserFileId;
    }

    public void setUploadedUserFileId(long uploadedUserFileId) {
        this.uploadedUserFileId = uploadedUserFileId;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public int getRcItemFormatTypeId() {
        return rcItemFormatTypeId;
    }

    public void setRcItemFormatTypeId(int rcItemFormatTypeId) {
        this.rcItemFormatTypeId = rcItemFormatTypeId;
    }

    public String getSelectedResponse() {
        return selectedResponse;
    }

    public void setSelectedResponse(String selectedResponse) {
        this.selectedResponse = selectedResponse;
    }

    public RcItem getRcItem() {
        return rcItem;
    }

    public void setRcItem(RcItem rcItem) {
        this.rcItem = rcItem;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    
}
