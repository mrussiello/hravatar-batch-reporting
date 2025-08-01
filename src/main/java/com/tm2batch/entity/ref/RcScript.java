package com.tm2batch.entity.ref;


import com.tm2batch.ref.RcCheckType;
import com.tm2batch.ref.RcCompetencyWrapper;
import com.tm2batch.ref.RcImportanceType;
import com.tm2batch.ref.RcItemWrapper;
import com.tm2batch.service.LogService;
import com.tm2batch.util.I18nUtils;
import com.tm2batch.util.JsonUtils;
import com.tm2batch.util.MessageFactory;
import com.tm2batch.util.StringUtils;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.persistence.Cacheable;
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


@Cacheable
@Entity
@Table( name = "rcscript" )
@NamedQueries({
})
public class RcScript implements Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="rcscriptid")
    private int rcScriptId;

    @Column(name="orgid")
    private int orgId;
    
    @Column(name="suborgid")
    private int suborgId;
    
    @Column(name="authoruserid")
    private long authorUserId;
                
    @Column(name="orgaccesstypeid")
    private int orgAccessTypeId;

    /**
     * 0=pre-hire
     * 1=employee
     */
    @Column(name="rcchecktypeid")
    private int rcCheckTypeId = RcCheckType.PREHIRE.getRcCheckTypeId();

    
    @Column(name="rcscriptstatustypeid")
    private int rcScriptStatusTypeId;

    @Column(name="allcommentsrequired")
    private int allCommentsRequired;

    
    @Column(name="standardtemplate")
    private int standardTemplate;
    
    @Column(name="description")
    private String description;
    
    
    @Column(name="langcode")
    private String langCode;

    /**
     * 0=no
     * 1=audio
     * 2=video
     */
    @Column(name="audiovideook")
    private int audioVideoOk;

    
    @Column(name="usediscreteratings")
    private int useDiscreteRatings;
    
    
    /**
     * This is the soc that was used to calculate the weights, if any. 
     */
    @Column(name="onetsoccode")
    private String onetSocCode;

    
    /**
     * {
     *     rcscriptid:rcScriptId,
     *     competencies: [
     *                      {   
     *                          rccompetencyid: rcCompetencyId,
     *                          onetelementid: onet elementid,  (since a competency can have multiple, store the actual one here)
     *                          displayorder: display order,
                                onetimportance: onet importance value,
                                userimportancetypeid: user importance typeid,
     *                          items: [
     *                                     { 
     *                                         rcitemid: rcItemId,
     *                                         weight: weight for item
                                               displayorder: display order
     *                                     }
     *                                 ]
     *                      }
     *                   ],
     *      specialinstructionscandidate:special instructions
     *      specialinstructionsraters:special instructions
     *     
     * }     */
    @Column(name="scriptjson")
    private String scriptJson;

    
    @Column(name="userorder")
    private int userOrder;

    
    @Column(name="name")
    private String name;
    
    @Column(name="nameenglish")
    private String nameEnglish;
        
    @Column(name="note")
    private String note;
    
    @Column(name="candidatestr1title")
    private String candidateStr1Title;
    
    @Column(name="candidatestr2title")
    private String candidateStr2Title;
    
    @Column(name="candidatestr3title")
    private String candidateStr3Title;
    
    @Column(name="candidatestr4title")
    private String candidateStr4Title;
    
    @Column(name="candidatestr5title")
    private String candidateStr5Title;
    
    @Column(name="candidatestr1question")
    private String candidateStr1Question;
    
    @Column(name="candidatestr2question")
    private String candidateStr2Question;
    
    @Column(name="candidatestr3question")
    private String candidateStr3Question;
    
    @Column(name="candidatestr4question")
    private String candidateStr4Question;
    
    @Column(name="candidatestr5question")
    private String candidateStr5Question;

    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date createDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastupdate")
    private Date lastUpdate;
    
    
    @Transient
    private List<RcCompetencyWrapper> rcCompetencyWrapperList;
    
    @Transient
    private Locale locale;
    
    @Transient
    private String strParam1;
    
    @Transient
    private String strParam2;
    
    @Transient
    private String specialInstructionsCandidate;
    
    @Transient
    private String specialInstructionsRaters;
    

    @Override
    public String toString() {
        return "RcScript{" + "rcScriptId=" + rcScriptId + ", orgId=" + orgId + ", name=" + name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.rcScriptId;
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
        final RcScript other = (RcScript) obj;
        if (this.rcScriptId != other.rcScriptId) {
            return false;
        }
        return true;
    }

    
    public RcCompetencyWrapper getRcCompetencyWrapper(int rcCompetencyId )
    {
        if( rcCompetencyWrapperList==null )
            return null;
        for( RcCompetencyWrapper rcw : rcCompetencyWrapperList )
        {
            if( rcw.getRcCompetencyId()==rcCompetencyId )
                return rcw;
        }
        return null;
    }

    public boolean getIsNonEnglish()
    {
        if( langCode==null )
            return false;
        return !langCode.startsWith("en");
        
    }
    
    public String getRcScriptLanguageName()
    {
        if( locale==null )
            locale = Locale.US;
        
        return MessageFactory.getStringMessage( locale, "localeStr." + locale.toString(), null );
    }
    
    public Locale getRcScriptLocale()
    {
        if( langCode==null )
            langCode="en_US";
        return I18nUtils.getLocaleFromCompositeStr(langCode );
    }
    
    public String getRcCheckTypeName()
    {
        if( locale==null )
            locale = Locale.US;
        
        return this.getRcCheckType().getName(locale);
    }
    
    public RcCheckType getRcCheckType()
    {
        return RcCheckType.getValue(this.rcCheckTypeId);
    }
    
    public int getScoredCompetencyCount()
    {
        if( this.rcCompetencyWrapperList==null )
            return 0;
        int ct = 0;
        for( RcCompetencyWrapper w : this.rcCompetencyWrapperList )
        {
            if( w.getIsScored() && w.getScoreAvgNoCandidate()>0 )
                ct++;
        }
        return ct;
        
    }
    
    public boolean getHasRcCompetency( int rcCompetencyId )
    {
        if( rcCompetencyWrapperList==null )
            return false;
        for( RcCompetencyWrapper rcw : rcCompetencyWrapperList )
        {
            if( rcw.getRcCompetencyId()==rcCompetencyId )
                return true;
        }
        return false;
    }
    
    public RcItemWrapper getRcItemWrapper( int rcItemId )
    {
        if( rcCompetencyWrapperList==null )
            return null;
        
        for( RcCompetencyWrapper rcw : rcCompetencyWrapperList )
        {
            if( rcw.getRcItemWrapperList()==null )
                continue;
            for( RcItemWrapper rciw : rcw.getRcItemWrapperList() )
            {
                if( rciw.getRcItemId()==rcItemId )
                    return rciw;
            }
        }
        return null;
    }

    public List<RcItemWrapper> getItemWrappersWithRatingsAndHeadersList()
    {
        List<RcItemWrapper> out = new ArrayList<>();        
        if( rcCompetencyWrapperList==null )
            return out;        
        for( RcCompetencyWrapper rcw : rcCompetencyWrapperList )
        {
            if( rcw.getRcItemWrapperList()==null )
                continue;
            out.addAll( rcw.getRcItemWrapperListWithHeadersAndRatings() );
        }        
        return out;        
    }

    
    /*
    public List<RcItemWrapper> getItemWrappersWithRatingsList()
    {
        List<RcItemWrapper> out = new ArrayList<>();        
        for( RcItemWrapper rcw : getAllItemWrapperList() )
        {
            if( !rcw.getHasRatingData() )
                continue;
            out.add( rcw );
        }
        return out;        
    }
    */
    
    public List<RcCompetencyWrapper> getRcCompetencyWrappersForSummaryList()
    {
        List<RcCompetencyWrapper> out = new ArrayList<>();
        
        if( rcCompetencyWrapperList==null )
            return out;
        
        for( RcCompetencyWrapper rcw : this.rcCompetencyWrapperList )
        {
            if( rcw.getIsScored() )
                out.add( rcw );
        }
        
        return out;
    }
    

    public int getActiveCompetencyCount()
    {
        return computeCounts( true )[0];
    }

    public int getCompetencyCount()
    {
        return computeCounts( false )[0];
    }
    
    
    public int getActiveQuestionCount()
    {
        return computeCounts( true )[1];
    }

    public int getQuestionCount()
    {
        return computeCounts( false )[1];
    }
    
    /*
     returns int[]
       data[0]=competencies
       data[1]=items
    */
    private int[] computeCounts( boolean activeOnly )
    {
        int[] out = new int[2];
        if( rcCompetencyWrapperList==null )
            return out;
        //int c = 0;
        for( RcCompetencyWrapper rcw : rcCompetencyWrapperList )
        {
            if( rcw.getRcCompetency()==null )
                continue;
            
            if( activeOnly && !rcw.getRcCompetency().getRcCompetencyStatusType().getIsActive() )
                continue;
            
            // count competency
            out[0]++;
            
            if( rcw.getRcItemWrapperList()==null )
                continue;
            out[1] += rcw.getRcItemWrapperList().size();
        }
        
        return out;
    }

    
    public void clearRatings()
    {
        for( RcItemWrapper rciw : getAllItemWrapperList() )
        {
            rciw.clearRatings();
        }
    }
    
    
    
    public List<RcItemWrapper> getAllItemWrapperList()
    {
        List<RcItemWrapper> out = new ArrayList<>();        
        if( rcCompetencyWrapperList==null )
            return out;        
        for( RcCompetencyWrapper rcw : rcCompetencyWrapperList )
        {
            if( rcw.getRcItemWrapperList()==null )
                continue;
            out.addAll( rcw.getRcItemWrapperList() );
        }        
        return out;
    }
    
    public List<RcCompetency> getRcCompetencyList()
    {
        List<RcCompetency> o = new ArrayList<>();        
        if( rcCompetencyWrapperList==null )
            return o;
        
        for( RcCompetencyWrapper i : rcCompetencyWrapperList )
        {
            if( i.getRcCompetency()==null )
                continue;
            if( o.contains( i.getRcCompetency() ) )
                continue;
            o.add( i.getRcCompetency() );
        }
        Collections.sort( o );
        return o;
    }
    
    public void setItemWeights()
    {
        if( rcCompetencyWrapperList!=null )
        {
            float wt;
            float wtPerItem;
            RcImportanceType rcit;
            for( RcCompetencyWrapper rcw : rcCompetencyWrapperList )
            {
                rcit = rcw.getUserRcImportanceType();
                
                if( rcit.equals( RcImportanceType.USE_ONET ) )
                    wt = rcw.getOnetImportance();
                else
                    wt = rcit.getImportance();
                if( rcw.getRcItemWrapperList()!=null )
                {
                    wtPerItem = rcw.getRcItemWrapperList().isEmpty() ? 0 : wt/((float)rcw.getRcItemWrapperList().size());
                    
                    for( RcItemWrapper rciw : rcw.getRcItemWrapperList() )
                    {
                        rciw.setWeight( wtPerItem);
                    }                        
                }                    
            }
        }        
    }
    
    
    public boolean getHasCandidateInput()
    {
        if( getHasCandidateInput1() )
            return true;
        if( getHasCandidateInput2() )
            return true;
        if( getHasCandidateInput3() )
            return true;
        if( getHasCandidateInput4() )
            return true;
        if( getHasCandidateInput5() )
            return true;        
        return false;
    }
    
    public String getCandidateStr1TitleOrQ()
    {
        if( candidateStr1Title!=null && !candidateStr1Title.isBlank() )
            return candidateStr1Title;        
        return candidateStr1Question;
    }
    
    public String getCandidateStr2TitleOrQ()
    {
        if( candidateStr2Title!=null && !candidateStr2Title.isBlank() )
            return candidateStr2Title;        
        return candidateStr2Question;
    }
    
    public String getCandidateStr3TitleOrQ()
    {
        if( candidateStr3Title!=null && !candidateStr3Title.isBlank() )
            return candidateStr3Title;        
        return candidateStr3Question;
    }
    
    public String getCandidateStr4TitleOrQ()
    {
        if( candidateStr4Title!=null && !candidateStr4Title.isBlank() )
            return candidateStr4Title;        
        return candidateStr4Question;
    }
    
    public String getCandidateStr5TitleOrQ()
    {
        if( candidateStr5Title!=null && !candidateStr5Title.isBlank() )
            return candidateStr5Title;        
        return candidateStr5Question;
    }
    
    public boolean getHasCandidateInput1()
    {
        return candidateStr1Question!=null && !candidateStr1Question.isBlank();
    }
    public boolean getHasCandidateInput2()
    {
        return candidateStr2Question!=null && !candidateStr2Question.isBlank();
    }
    public boolean getHasCandidateInput3()
    {
        return candidateStr3Question!=null && !candidateStr3Question.isBlank();
    }
    public boolean getHasCandidateInput4()
    {
        return candidateStr4Question!=null && !candidateStr4Question.isBlank();
    }
    public boolean getHasCandidateInput5()
    {
        return candidateStr5Question!=null && !candidateStr5Question.isBlank();
    }
    
    
    public synchronized void parseScriptJson()
    {
       rcCompetencyWrapperList = new ArrayList<>();
       
       if( scriptJson==null || scriptJson.isBlank() )
           return;
       
       try
       {
            JsonReader r = Json.createReader( new StringReader(scriptJson) );
            JsonObject top = r.readObject();
            RcCompetencyWrapper compWrap;
            RcItemWrapper itemWrap;
            JsonArray itemArray;
            
            if( top.containsKey("competencies") && !top.isNull("competencies") )
            {
                JsonArray ja = top.getJsonArray("competencies" );
                
                for( JsonObject comp : ja.getValuesAs(JsonObject.class) )
                {
                    compWrap = new RcCompetencyWrapper();
                    rcCompetencyWrapperList.add(compWrap);

                    if( comp.containsKey( "rccompetencyid" ) )
                        compWrap.setRcCompetencyId( comp.getInt("rccompetencyid"));
                    else
                        throw new Exception("No competency id in json." );
                    
                    if( comp.containsKey( "displayorder" ) )
                        compWrap.setDisplayOrder( comp.getInt("displayorder"));
                    if( comp.containsKey("onetelementid") && !comp.isNull( "onetelementid" ) )
                        compWrap.setOnetElementId( comp.getString("onetelementid"));
                    if( comp.containsKey("onetimportance") )
                        compWrap.setOnetImportance(  (float) comp.getJsonNumber("onetimportance").doubleValue() );
                    if( comp.containsKey( "userimportancetypeid" ) )
                        compWrap.setUserImportanceTypeId(comp.getInt("userimportancetypeid"));
                    
                    if( comp.containsKey( "items" ) )
                    {
                        itemArray = comp.getJsonArray( "items" );
                        for( JsonObject itemObj : itemArray.getValuesAs( JsonObject.class ) )
                        {
                            itemWrap = new RcItemWrapper();
                            // itemWrap.setDisplayOrder( itemObj.containsKey("displayorder") ? itemObj.getInt("displayorder") : 0 );
                            
                            if( itemObj.containsKey("rcitemid") )
                                itemWrap.setRcItemId( itemObj.getInt("rcitemid") );
                            else
                                throw new Exception( "Item doesn't have any rcItemId." );
                            if( itemObj.containsKey("weight") )
                                itemWrap.setWeight( (float) itemObj.getJsonNumber("weight").doubleValue()  );
                            
                            compWrap.addItemWrapper(itemWrap, -1);
                        }
                    }
                }
            } 
            if( top.containsKey( "specialinstructionscandidate") && !top.isNull( "specialinstructionscandidate" ) )
                specialInstructionsCandidate = top.getString( "specialinstructionscandidate" );
            
            if( top.containsKey( "specialinstructionsraters") && !top.isNull( "specialinstructionsraters" ) )
                specialInstructionsRaters = top.getString( "specialinstructionsraters" );
            
       }
       catch( Exception e )
       {
           LogService.logIt( e, "RcScript.parseScriptJson() scriptJson=" + scriptJson );
       }
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

    public long getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(long authorUserId) {
        this.authorUserId = authorUserId;
    }

    public int getOrgAccessTypeId() {
        return orgAccessTypeId;
    }

    public void setOrgAccessTypeId(int orgAccessTypeId) {
        this.orgAccessTypeId = orgAccessTypeId;
    }

    public int getRcScriptStatusTypeId() {
        return rcScriptStatusTypeId;
    }

    public void setRcScriptStatusTypeId(int rcScriptStatusTypeId) {
        this.rcScriptStatusTypeId = rcScriptStatusTypeId;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCandidateStr1Title() {
        return candidateStr1Title;
    }

    public void setCandidateStr1Title(String candidateStr1Title) {
        this.candidateStr1Title = candidateStr1Title;
    }

    public String getCandidateStr2Title() {
        return candidateStr2Title;
    }

    public void setCandidateStr2Title(String candidateStr2Title) {
        this.candidateStr2Title = candidateStr2Title;
    }

    public String getCandidateStr3Title() {
        return candidateStr3Title;
    }

    public void setCandidateStr3Title(String candidateStr3Title) {
        this.candidateStr3Title = candidateStr3Title;
    }

    public String getCandidateStr4Title() {
        return candidateStr4Title;
    }

    public void setCandidateStr4Title(String candidateStr4Title) {
        this.candidateStr4Title = candidateStr4Title;
    }

    public String getCandidateStr5Title() {
        return candidateStr5Title;
    }

    public void setCandidateStr5Title(String candidateStr5Title) {
        this.candidateStr5Title = candidateStr5Title;
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


    public String getOnetSocCode() {
        return onetSocCode;
    }

    public void setOnetSocCode(String onetSocCode) {
        this.onetSocCode = onetSocCode;
    }



    public int getSuborgId() {
        return suborgId;
    }

    public void setSuborgId(int suborgId) {
        this.suborgId = suborgId;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public List<RcCompetencyWrapper> getRcCompetencyWrapperList() {
        return rcCompetencyWrapperList;
    }

    public void setRcCompetencyWrapperList(List<RcCompetencyWrapper> crl) {
        this.rcCompetencyWrapperList = crl;
    }

    public String getScriptJson() {
        return scriptJson;
    }

    public void setScriptJson(String scriptJson) {
        this.scriptJson = scriptJson;
    }

    public int getAudioVideoOk() {
        return audioVideoOk;
    }

    public void setAudioVideoOk(int audioVideoOk) {
        this.audioVideoOk = audioVideoOk;
    }

    public String getSpecialInstructionsCandidate() {
        return specialInstructionsCandidate;
    }

    public void setSpecialInstructionsCandidate(String specialInstructionsCandidate) {
        this.specialInstructionsCandidate = specialInstructionsCandidate;
    }

    public String getSpecialInstructionsRaters() {
        return specialInstructionsRaters;
    }

    public void setSpecialInstructionsRaters(String specialInstructionsRaters) {
        this.specialInstructionsRaters = specialInstructionsRaters;
    }

    public int getRcCheckTypeId() {
        return rcCheckTypeId;
    }

    public void setRcCheckTypeId(int rcCheckTypeId) {
        this.rcCheckTypeId = rcCheckTypeId;
    }

    public String getCandidateStr1Question() {
        return candidateStr1Question;
    }

    public void setCandidateStr1Question(String candidateStr1Question) {
        this.candidateStr1Question = candidateStr1Question;
    }

    public String getCandidateStr2Question() {
        return candidateStr2Question;
    }

    public void setCandidateStr2Question(String candidateStr2Question) {
        this.candidateStr2Question = candidateStr2Question;
    }

    public String getCandidateStr3Question() {
        return candidateStr3Question;
    }

    public void setCandidateStr3Question(String candidateStr3Question) {
        this.candidateStr3Question = candidateStr3Question;
    }

    public String getCandidateStr4Question() {
        return candidateStr4Question;
    }

    public void setCandidateStr4Question(String candidateStr4Question) {
        this.candidateStr4Question = candidateStr4Question;
    }

    public String getCandidateStr5Question() {
        return candidateStr5Question;
    }

    public void setCandidateStr5Question(String candidateStr5Question) {
        this.candidateStr5Question = candidateStr5Question;
    }

    public int getAllCommentsRequired() {
        return allCommentsRequired;
    }

    public void setAllCommentsRequired(int allCommentsRequired) {
        this.allCommentsRequired = allCommentsRequired;
    }

    public boolean getAllCommentsRequiredB() {
        return allCommentsRequired==1;
    }

    public void setAllCommentsRequiredB(boolean b) {
        this.allCommentsRequired = b ? 1 : 0;
    }

    public int getStandardTemplate() {
        return standardTemplate;
    }

    public void setStandardTemplate(int standardTemplate) {
        this.standardTemplate = standardTemplate;
    }

    public boolean getStandardTemplateB() {
        return standardTemplate==1;
    }

    public void setStandardTemplateB(boolean b) {
        this.standardTemplate = b ? 1 : 0;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionXhtml()
    {
        return StringUtils.replaceStandardEntities( description );
    }

    public int getUseDiscreteRatings() {
        return useDiscreteRatings;
    }

    public void setUseDiscreteRatings(int useDiscreteRatings) {
        this.useDiscreteRatings = useDiscreteRatings;
    }

    public boolean getUseDiscreteRatingsB() {
        return useDiscreteRatings == 1;
    }

    public void setUseDiscreteRatingsB(boolean b) {
        this.useDiscreteRatings = b ? 1 : 0;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }

    public int getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(int userOrder) {
        this.userOrder = userOrder;
    }
    
}
