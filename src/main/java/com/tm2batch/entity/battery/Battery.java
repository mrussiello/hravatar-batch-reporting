package com.tm2batch.entity.battery;


import com.tm2batch.battery.BatteryScoreType;
import java.io.Serializable;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;


@Entity
@Table( name = "battery" )
@NamedQueries({
})
public class Battery implements Serializable, Comparable<Battery>
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "batteryid" )
    private int batteryId;

    @Column( name = "batterytypeid" )
    private int batteryTypeId;
    
    @Column( name = "name" )
    private String name;

    @Column( name = "nameenglish" )
    private String nameEnglish;

    @Column( name = "description" )
    private String description;

    @Column( name = "batterystatustypeid" )
    private int batteryStatusTypeId;


    @Column( name = "rcscriptid" )
    private int rcScriptId;

    
    @Column( name = "orgid" )
    private int orgId;

    @Column( name = "suborgid" )
    private int suborgId;

    @Column( name = "productids" )
    private String productIds;

    @Column( name = "weights" )
    private String weights;

    @Column( name = "lang" )
    private String localeStr;

    @Column( name = "batteryscoretypeid" )
    private int batteryScoreTypeId;

    @Column( name = "textparam1" )
    private String textParam1;

    @Column( name = "strparam1" )
    private String strParam1;

    @Column( name = "strparam2" )
    private String strParam2;

    /**
     * Product Choice String
     * This is a string used to present a choice to the test taker to choose from one of several products. The format is choiceTypeId;productid;productid;productid etc. 
     *                  where choiceTypeId:
     *                      0 = List Product LANGUAGES
     *                      1 = list Products by NAME
     *                  productIds must be either a Battery or Sim type.
     * 
     */
    @Column( name = "strparam3" )
    private String strParam3;

    /**
     * Product Choice 
     * 
     * This is custom text to present to the user when the choice is to be made. 
     * 
     */
    @Column( name = "strparam4" )
    private String strParam4;
    
    


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.batteryId;
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
        final Battery other = (Battery) obj;
        if (this.batteryId != other.batteryId) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Battery o) {

        if( name != null && !name.isEmpty() && o.getName() != null && !o.getName().isEmpty()  )
            return name.compareTo( o.getName() );

        return new Integer( batteryId ).compareTo( (Integer)( o.getBatteryId() ) );
    }


    public String getNameWithEnglishIfNeeded()
    {
        if( getNeedsNameEnglish() )
                return name + " (English: " + nameEnglish + ")";

        return name;
    }

    
    public BatteryScoreType getBatteryScoreType()
    {
        return BatteryScoreType.getValue(batteryScoreTypeId);
    }
    
    public boolean getNeedsNameEnglish()
    {
        if( nameEnglish == null || nameEnglish.isEmpty() )
            return false;

        if( name ==null || name.isEmpty() )
            return false;

        if( name.equalsIgnoreCase( nameEnglish ) )
            return false;

        return true;
    }

    public int getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(int batteryId) {
        this.batteryId = batteryId;
    }

    public int getBatteryTypeId() {
        return batteryTypeId;
    }

    public void setBatteryTypeId(int batteryTypeId) {
        this.batteryTypeId = batteryTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBatteryStatusTypeId() {
        return batteryStatusTypeId;
    }

    public void setBatteryStatusTypeId(int batteryStatusTypeId) {
        this.batteryStatusTypeId = batteryStatusTypeId;
    }

    public int getRcScriptId() {
        return rcScriptId;
    }

    public void setRcScriptId(int rcScriptId) {
        this.rcScriptId = rcScriptId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getSuborgId() {
        return suborgId;
    }

    public void setSuborgId(int suborgId) {
        this.suborgId = suborgId;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getWeights() {
        return weights;
    }

    public void setWeights(String weights) {
        this.weights = weights;
    }

    public String getLocaleStr() {
        return localeStr;
    }

    public void setLocaleStr(String localeStr) {
        this.localeStr = localeStr;
    }

    public int getBatteryScoreTypeId() {
        return batteryScoreTypeId;
    }

    public void setBatteryScoreTypeId(int batteryScoreTypeId) {
        this.batteryScoreTypeId = batteryScoreTypeId;
    }

    public String getTextParam1() {
        return textParam1;
    }

    public void setTextParam1(String textParam1) {
        this.textParam1 = textParam1;
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

    public String getStrParam3() {
        return strParam3;
    }

    public void setStrParam3(String strParam3) {
        this.strParam3 = strParam3;
    }

    public String getStrParam4() {
        return strParam4;
    }

    public void setStrParam4(String strParam4) {
        this.strParam4 = strParam4;
    }



}
