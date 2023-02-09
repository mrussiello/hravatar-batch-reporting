package com.tm2batch.entity.lvi;


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
@Table( name = "lvscriptquestionmap" )
@NamedQueries({
        @NamedQuery( name = "LvScriptQuestionMap.findByLvScriptId", query = "SELECT o FROM LvScriptQuestionMap AS o WHERE o.lvScriptId=:lvScriptId ORDER BY o.displayOrder" ),
        @NamedQuery( name = "LvScriptQuestionMap.findByLvQuestionId", query = "SELECT o FROM LvScriptQuestionMap AS o WHERE o.lvQuestionId=:lvQuestionId ORDER BY o.displayOrder" )     
})
public class LvScriptQuestionMap implements Serializable, Comparable<LvScriptQuestionMap>
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="lvscriptquestionmapid")
    private long lvScriptQuestionMapId;
    
    @Column(name="lvquestionid")
    private long lvQuestionId;

    @Column(name="lvscriptid")
    private int lvScriptId;

    @Column(name="displayorder")
    private int displayOrder;
    
    
    @Transient
    private LvQuestion lvQuestion;
    
    @Transient
    private LvQuestionResponse lvQuestionResponse;
    
    @Transient
    private boolean tempBoolean1;
    

    @Override
    public String toString() {
        return "LvScriptQuestionMap{" + "lvScriptQuestionMapId=" + lvScriptQuestionMapId + ", lvQuestionId=" + lvQuestionId + ", lvScriptId=" + lvScriptId + ", displayOrder=" + displayOrder + '}';
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.lvScriptQuestionMapId ^ (this.lvScriptQuestionMapId >>> 32));
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
        final LvScriptQuestionMap other = (LvScriptQuestionMap) obj;
        if (this.lvScriptQuestionMapId != other.lvScriptQuestionMapId) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(LvScriptQuestionMap o) {
        
        
        return (new Integer(displayOrder)).compareTo( o.getDisplayOrder() );
    }

    public long getLvScriptQuestionMapId() {
        return lvScriptQuestionMapId;
    }

    public void setLvScriptQuestionMapId(long lvScriptQuestionMapId) {
        this.lvScriptQuestionMapId = lvScriptQuestionMapId;
    }

    public long getLvQuestionId() {
        return lvQuestionId;
    }

    public void setLvQuestionId(long lvQuestionId) {
        this.lvQuestionId = lvQuestionId;
    }

    public int getLvScriptId() {
        return lvScriptId;
    }

    public void setLvScriptId(int lvScriptId) {
        this.lvScriptId = lvScriptId;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public LvQuestion getLvQuestion() {
        return lvQuestion;
    }

    public void setLvQuestion(LvQuestion lvQuestion) {
        this.lvQuestion = lvQuestion;
    }

    public boolean isTempBoolean1() {
        return tempBoolean1;
    }

    public void setTempBoolean1(boolean tempBoolean1) {
        this.tempBoolean1 = tempBoolean1;
    }

    public LvQuestionResponse getLvQuestionResponse() {
        return lvQuestionResponse;
    }

    public void setLvQuestionResponse(LvQuestionResponse lvQuestionResponse) {
        this.lvQuestionResponse = lvQuestionResponse;
    }


}
