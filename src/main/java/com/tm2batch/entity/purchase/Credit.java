package com.tm2batch.entity.purchase;




import com.tm2batch.entity.user.Org;
import com.tm2batch.entity.user.User;
import com.tm2batch.purchase.CreditStatusType;
import com.tm2batch.purchase.CreditType;
import com.tm2batch.service.EncryptUtils;
import com.tm2batch.service.LogService;
import com.tm2batch.util.Base64Encoder;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
@Table( name = "credit" )
@NamedQueries( {
    @NamedQuery( name = "Credit.findOverageForOrg", query = "SELECT o FROM Credit AS o WHERE o.orgId=:orgId AND o.creditStatusTypeId=5 AND o.creditTypeId=:creditTypeId AND o.overageCount>0 ORDER BY o.expireDate" ),
    @NamedQuery( name = "Credit.findAvailForOrg", query = "SELECT o FROM Credit AS o WHERE o.orgId=:orgId AND o.creditStatusTypeId IN (1,5) AND o.creditTypeId=:creditTypeId AND o.expireDate>:today AND o.remainingCount>=:quantity ORDER BY o.expireDate" ),
    @NamedQuery( name = "Credit.findForOrderItemId", query = "SELECT o FROM Credit AS o WHERE o.orderItemId=:orderItemId" ),
    @NamedQuery( name = "Credit.findForOrg", query = "SELECT o FROM Credit AS o WHERE o.orgId=:orgId ORDER BY o.createDate DESC" ),
    @NamedQuery( name = "Credit.findForOrgAndStatus", query = "SELECT o FROM Credit AS o WHERE o.orgId=:orgId AND o.creditStatusTypeId=:creditStatusTypeId ORDER BY o.createDate DESC" )
} )
public class Credit implements Serializable, Comparable<Credit>
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "creditid" )
    private long creditId;

    @Column( name = "credittypeid" )
    private int creditTypeId;

    
    @Column( name = "orderid" )
    private long orderId;

    @Column( name = "orderitemid" )
    private long orderItemId;

    @Column( name = "userid" )
    private long userId;

    @Column( name = "orgid" )
    private int orgId;


    @Column( name = "creditstatustypeid" )
    private int creditStatusTypeId;

    @Column( name = "creditsourcetypeid" )
    private int creditSourceTypeId;

    @Column( name = "affiliatedemo" )
    private int affiliateDemo;

    @Column( name = "directpurchaseamount" )
    private float directPurchaseAmount=-1;

    @Column( name = "initialcount" )
    private int initialCount;

    @Column( name = "usedcount" )
    private int usedCount;

    @Column( name = "remainingcount" )
    private int remainingCount;

    @Column( name = "overagecount" )
    private int overageCount;
    
    @Column( name = "appliedoverage" )
    private int appliedOverage;
    
    
    @Column( name = "note" )
    private String note;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="expiredate")
    private Date expireDate;

    @Transient
    private Locale locale;

    @Transient
    private User user;

    @Transient
    private Org org;

    @Transient
    private TimeZone timeZone;
    

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public CreditType getCreditType()
    {
        return CreditType.getValue( creditTypeId );
    }
    
    

    @Override
    public int compareTo( Credit c )
    {
        if( createDate != null && c.getCreateDate() != null )
            return createDate.compareTo( c.getCreateDate() );

        return Long.valueOf(creditId).compareTo(c.getCreditId());
    }

    public void appendNote( String msg )
    {
        if( msg == null || msg.trim().length() == 0 )
            return;

        if( note == null )
            note = "";

        if( note.length() > 0 )
            note += "\n.................\n";

        note += msg;
    }

    
    
    public String getOrderConfirmationCode()
    {
        if( orderId <= 0 )
            return "NONE";

        try
        {
            return Base64Encoder.encodeString( EncryptUtils.urlSafeEncrypt( Long.toString( orderId ) ) );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "getOrderConfirmationCode()" );

            return "ERROR";
        }

    }

    public String getCreditStatusTypeName()
    {
        return getCreditStatusType().getName( locale == null ? Locale.US : locale );
    }


    public CreditStatusType getCreditStatusType()
    {
        return CreditStatusType.getType( creditStatusTypeId );
    }

    @Override
    public String toString()
    {
        return "Credit " + creditId + ", userId=" + userId + ", orderId=" + orderId + ", status=" + creditStatusTypeId + ", initial=" + initialCount + ", used=" + usedCount + ", remaining=" + remainingCount + ", created=" +  ( createDate == null ? "null" : createDate.toString() ) + ", expires=" + ( expireDate == null ? "null" : expireDate.toString() );
    }

    public int getInitialCountPlusAppliedOverage()
    {
        if( getCreditType().getIsResult() )
            return initialCount + appliedOverage;
        return initialCount;
    }
    
    public int getRemainingCountWithOverage()
    {
        if( getCreditType().getIsResult() && overageCount>0 )
            return remainingCount - overageCount;
        
        return remainingCount;
    }
    
    public long getCreditId()
    {
        return creditId;
    }

    public void setCreditId( long creditId )
    {
        this.creditId = creditId;
    }

    public long getOrderId()
    {
        return orderId;
    }

    public void setOrderId( long orderId )
    {
        this.orderId = orderId;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId( long userId )
    {
        this.userId = userId;
    }

    public int getCreditStatusTypeId()
    {
        return creditStatusTypeId;
    }

    public void setCreditStatusTypeId( int creditStatusTypeId )
    {
        this.creditStatusTypeId = creditStatusTypeId;
    }

    public int getInitialCount()
    {
        return initialCount;
    }

    public void setInitialCount( int initialCount )
    {
        this.initialCount = initialCount;
    }

    public int getUsedCount()
    {
        return usedCount;
    }

    public void setUsedCount( int usedCount )
    {
        this.usedCount = usedCount;
    }

    public int getRemainingCount()
    {
        return remainingCount;
    }

    public void setRemainingCount( int remainingCount )
    {
        this.remainingCount = remainingCount;
    }

    public Date getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate( Date createDate )
    {
        this.createDate = createDate;
    }

    public Date getExpireDate()
    {
        return expireDate;
    }

    public void setExpireDate( Date expireDate )
    {
        this.expireDate = expireDate;
    }



    public String getNote()
    {
        return note;
    }



    public void setNote( String note )
    {
        if( note != null && note.trim().length() == 0 )
            note = null;

        this.note = note;
    }



    public long getOrderItemId()
    {
        return orderItemId;
    }



    public void setOrderItemId( long orderItemId )
    {
        this.orderItemId = orderItemId;
    }


    public Locale getLocale()
    {
        return locale;
    }


    public void setLocale( Locale locale )
    {
        this.locale = locale;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getCreditSourceTypeId() {
        return creditSourceTypeId;
    }

    public void setCreditSourceTypeId(int creditSourceTypeId) {
        this.creditSourceTypeId = creditSourceTypeId;
    }

    public int getAffiliateDemo() {
        return affiliateDemo;
    }

    public void setAffiliateDemo(int affiliateDemo) {
        this.affiliateDemo = affiliateDemo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    public float getDirectPurchaseAmount() {
        return directPurchaseAmount;
    }

    public void setDirectPurchaseAmount(float directPurchaseAmount) {
        this.directPurchaseAmount = directPurchaseAmount;
    }

    public int getCreditTypeId() {
        return creditTypeId;
    }

    public void setCreditTypeId(int creditTypeId) {
        this.creditTypeId = creditTypeId;
    }

    public int getOverageCount() {
        return overageCount;
    }

    public void setOverageCount(int overageCount) {
        this.overageCount = overageCount;
    }

    public int getAppliedOverage() {
        return appliedOverage;
    }

    public void setAppliedOverage(int appliedOverage) {
        this.appliedOverage = appliedOverage;
    }



}
