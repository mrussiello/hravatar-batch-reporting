package com.tm2batch.entity.user;


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
@Table( name="usernote" )
@NamedQueries({
    @NamedQuery ( name="UserNote.findForUser", query="SELECT o FROM UserNote AS o WHERE o.userId = :userId ORDER BY o.createDate" )
})
public class UserNote implements Serializable, Comparable<UserNote>
{
    @Transient
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="usernoteid")
    private long userNoteId;

    
    @Column(name="userid")
    private long userId;

    @Column(name="authoruserid")
    private long authorUserId;

    @Column(name="note")
    private String note;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;

    @Transient
    private User authorUser;

    @Override
    public int compareTo(UserNote o) {

        if( createDate != null && o.getCreateDate() != null )
            return createDate.compareTo( o.getCreateDate() );

        if( userNoteId>0 && o.getUserNoteId()>0 )
            return ((Long)userNoteId).compareTo( o.getUserNoteId() );

        return ((Long)userId).compareTo( o.getUserId() );
    }

    @Override
    public String toString() {
        return "UserNote{" + "userNoteId=" + userNoteId + ", userId=" + userId + ", authorUserId=" + authorUserId + ", createDate=" + createDate + ", Note=" + note + '}';
    }



    public long getUserNoteId() {
        return userNoteId;
    }

    public void setUserNoteId(long userNoteId) {
        this.userNoteId = userNoteId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(long authorUserId) {
        this.authorUserId = authorUserId;
    }

    public String getNote() {
        return note;
    }

    public String getNoteXhtml() {
        return StringUtils.replaceStandardEntities(note);
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

    public User getAuthorUser() {
        return authorUser;
    }

    public void setAuthorUser(User authorUser) {
        this.authorUser = authorUser;
    }


}
