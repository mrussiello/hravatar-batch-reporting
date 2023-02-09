/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.ref;

import com.tm2batch.entity.ref.RcCompetency;
import com.tm2batch.entity.ref.RcRating;
import com.tm2batch.util.I18nUtils;

import com.tm2batch.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

/**
 *
 * @author miker_000
 */
public class RcCompetencyWrapper implements Cloneable, Comparable<RcCompetencyWrapper> {
    
    String onetElementId;
    int rcCompetencyId;
    int displayOrder; 
    int tempDispOrder;
    float onetImportance;
    int userImportanceTypeId;
    float scoreCandidate;
    
    List<RcItemWrapper> rcItemWrapperList;
    
    RcCompetency rcCompetency;
    Locale locale;
    
    public RcCompetencyWrapper()
    {
    }

    
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.rcCompetencyId;
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
        final RcCompetencyWrapper other = (RcCompetencyWrapper) obj;
        if (this.rcCompetencyId != other.rcCompetencyId) {
            return false;
        }
        return true;
    }
    
    
    public boolean getHasDescription()
    {
        return this.rcCompetency!=null && rcCompetency.getDescription()!=null && !rcCompetency.getDescription().isBlank();
    }
    
    public String getDescriptionXhtml()
    {
        if( !getHasDescription() )
            return "";
        
        return StringUtils.replaceStandardEntities( rcCompetency.getDescription() );
    }
        
    public boolean getIsScored()
    {
        if( rcItemWrapperList==null )
            return false;
        for( RcItemWrapper rciw : rcItemWrapperList )
        {
            if( rciw.getIsScored() )
                return true;
        }
        return false;
        
    }
    
    public boolean getHasRatingInfoToShow()
    {
        if( rcItemWrapperList==null )
            return false;
        for( RcItemWrapper rciw : rcItemWrapperList )
        {
            if( rciw.getHasRatingData() )
                return true;
        }
        return false;
    }
    
    public float getAverageScore(List<Long> rcRaterIdsToSkip)
    {
        if( rcItemWrapperList==null )
            return 0;
        float ct = 0;
        float t = 0;
        float s;
        for( RcItemWrapper rciw : rcItemWrapperList )
        {
            s = rciw.getAverageScore(rcRaterIdsToSkip);
            if( s<=0 )
                continue;
            ct++;
            t+=s;
        }
        return ct<=0 ? 0 : t/ct;
    }
    
    
    
    
    public int getScriptBoxDisplayHeight()
    {
        if( rcItemWrapperList==null || rcItemWrapperList.isEmpty() )
            return 120;
        
        return 120 + (rcItemWrapperList.size()-1)*40;
    }
    
    public boolean getHasAnyWeight()
    {
        return rcCompetency!=null && rcCompetency.getRcCompetencyType().getUsesAnyWeight();        
    }
    
    public boolean getHasOnetWeight()
    {
        return rcCompetency!=null && rcCompetency.getUsesOnet() && onetImportance>0;
    }
    
    public String getOnetWeightFormatted()
    {
        if( !getHasOnetWeight() )
            return "";
        if( locale==null )
            locale=Locale.US;
        
        RcImportanceType rit = RcImportanceType.getForOnetWeight(onetImportance);        
        return ( rit!=null ? rit.getName(locale) + " (" : "" ) + I18nUtils.getFormattedNumber(locale, onetImportance, 2 ) + ( rit!=null ? ")" : "" );
    }

        
    public float getScoreAvgNoCandidate() {
        
        //if( 1==1 )
        //    return 4.3234f;
        
        
        if( rcItemWrapperList==null )
            return 0;
        float ct = 0;
        float tot = 0;
        for( RcItemWrapper rciw : rcItemWrapperList)
        {
            if( rciw.getScoreAvgNoCandidate()<=0 )
                continue;
            ct++;
            tot += rciw.getScoreAvgNoCandidate();
        }
        return ct>0 ? tot/ct : 0;
    }

    public float getScoreCandidate( long candidateRaterId ) 
    {
        if( rcItemWrapperList==null || candidateRaterId<=0 )
            return 0;        
        for( RcItemWrapper rciw : rcItemWrapperList)
        {
            if( rciw.getRcRatingList()==null )
                continue;
            
            for( RcRating rating : rciw.getRcRatingList() )
            {
                if( rating.getRcRaterId()==candidateRaterId )
                    return rating.getFinalScore();
            }
        }
        return 0;
    }
    
    
    
    public float getScore()
    {
        if( rcItemWrapperList==null )
            return 0;
        
        float t = 0;  
        float twt = 0;
        for( RcItemWrapper rciw : rcItemWrapperList )
        {
            if( !rciw.getHasRatingData() )
                continue;
            for( RcRating r : rciw.getRcRatingList() )
            {
                if( !r.getHasScore() )
                    continue;
                t += r.getFinalScore()*rciw.getWeight();
                twt += rciw.getWeight();
            }
        }
        
        return twt>0 ? t/twt : 0;
    }
        
    
    public RcImportanceType getUserRcImportanceType()
    {
        return RcImportanceType.getValue( userImportanceTypeId );
    }
    
    public String getRcCompetencyTypeName()
    {
        if( rcCompetency==null )
            return "";
        
        return rcCompetency.getRcCompetencyType().getName( locale );
    }
    
    @Override
    public String toString() {
        return "RcCompetencyWrapper{" + "elementId=" + onetElementId + '}';
    }

    @Override
    public int compareTo(RcCompetencyWrapper o) 
    {        
        return ((Integer)displayOrder).compareTo( o.getDisplayOrder() );           
    }
        
    public boolean getHasMultipleScoredItems()
    {
        if( rcItemWrapperList==null )
            return false;
        if( rcItemWrapperList.size()<=1 )
            return false;
                
        int scoredCt = 0;
        for( RcItemWrapper w : rcItemWrapperList )
        {            
            // not scored? Don't count it.
            if( w.getIsScored()  )
                scoredCt++;
        }
        
        // only one scored competency.
        return scoredCt>1;   
    }
    
    public List<RcItemWrapper> getRcItemWrapperListWithHeadersAndRatings()
    {
        List<RcItemWrapper> out = new ArrayList<>();
        
        if( rcItemWrapperList==null )
            return out;
        
        //Only one Item wrapper.
        if( rcItemWrapperList.size()<=1 )
        {
            out.addAll( rcItemWrapperList );
            return out;
        }
                
        //RcHistogram h = null;
        int scoredCt = 0;
        for( RcItemWrapper w : rcItemWrapperList )
        {
            // just to be sure.
            w.setTempRcCompetencyWrapper(null);
            
            //if( h==null && w.getHistogram()!=null )
            //    h = w.getHistogram();
            
            // no ratings. Skip
            if( !w.getHasRatingData() )
                continue;
            
            out.add( w );
            
            // not scored? Don't count it.
            if( w.getIsScored() && w.getScoreAvgNoCandidate()>0 )
                scoredCt++;
        }
        
        // only one scored competency. Include histogram where it is. 
        if( scoredCt<=1 )
            return out;        
        
        //if( h!=null && !h.isItemLevel() )
        //{
        //    for( RcItemWrapper w : rcItemWrapperList )
        //    {
        //        w.setShowHist( false );
                //w.setHistogram(null);
        //    }            
        //}
        
        RcItemWrapper rw = new RcItemWrapper();
        //rw.setHistogram(h);
        rw.setTempRcCompetencyWrapper(this);
        out.add(0, rw);
        return out;
    }
        
    public RcItemWrapper getRcItemWrapper( int rcItemId )
    {
        if( rcItemWrapperList==null )
            return null;
        
        for( RcItemWrapper w : rcItemWrapperList )
        {
            if( w.getRcItemId()==rcItemId )
                return w;
        }
        return null;
    }
    
    public synchronized void moveRcItemWrapper( RcItemWrapper rciw, boolean up) throws Exception
    {
        if( rcItemWrapperList==null )
            return;
        
        int idx = -1;
        RcItemWrapper w;
        for( int i=0; i<rcItemWrapperList.size(); i++ )
        {
            w = rcItemWrapperList.get(i);
            if( w.getRcItemId()==rciw.getRcItemId() )
            {
                idx = i;
                break;     
            }            
        }
        if( up && idx<0 )
            throw new Exception( "Index is already 0. Cannot move any higher in list.");
        if( up && idx==0 )
            return;
        if( !up && idx>=rcItemWrapperList.size()-1 )
            throw new Exception( "Index is already at bottom. Cannot move any lower in list.");
        if( !up && idx==rcItemWrapperList.size()-1 )
            return;
        
        rcItemWrapperList.remove(idx);
        
        if( up )
            rcItemWrapperList.add(idx-1, rciw);
        else
            rcItemWrapperList.add(idx+1, rciw);
                    
        for( int i=0; i<rcItemWrapperList.size(); i++ )
        {
            w = rcItemWrapperList.get(i);
            w.getRcItem().setDisplayOrder( i+1 );
            // w.setDisplayOrder( i+1 );
        }
    }
    
    public synchronized void removeRcItemWrapper( RcItemWrapper rciw )
    {
        if( rcItemWrapperList==null )
            return;
        
        ListIterator<RcItemWrapper> iter = rcItemWrapperList.listIterator();
        RcItemWrapper w;
        int dispOrder = 1;
        while( iter.hasNext() )
        {
            w = iter.next();
            if( w.getRcItemId()==rciw.getRcItemId() )
            {
                iter.remove();
                continue;
            }
            w.getRcItem().setDisplayOrder(dispOrder);
            dispOrder++;
        }
    }
    
    public synchronized void addItemWrapper( RcItemWrapper iw, int atIndex)
    {
        if( rcItemWrapperList==null )
            rcItemWrapperList=new ArrayList<>();
        
        if( rcItemWrapperList.contains(iw) )
            return;
        
        if( atIndex<0 )
            rcItemWrapperList.add(iw);
        else
            rcItemWrapperList.add(atIndex, iw);
    }
    

    public RcCompetency getRcCompetency() {
        return rcCompetency;
    }

    public void setRcCompetency(RcCompetency competency) {
        this.rcCompetency = competency;
    }

    public String getOnetElementId() {
        return onetElementId;
    }

    public void setOnetElementId(String onetElementId) {
        this.onetElementId = onetElementId;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getRcCompetencyId() {
        return rcCompetencyId;
    }

    public void setRcCompetencyId(int rcCompetencyId) {
        this.rcCompetencyId = rcCompetencyId;
    }

    public List<RcItemWrapper> getRcItemWrapperList() {        
        if( rcItemWrapperList==null )
            rcItemWrapperList=new ArrayList<>();
        
        return rcItemWrapperList;
    }

    public void setRcItemWrapperList(List<RcItemWrapper> rcItemWrapperList) {
        this.rcItemWrapperList = rcItemWrapperList;
    }

    public float getOnetImportance() {
        return onetImportance;
    }

    public void setOnetImportance(float onetImportance) {
        this.onetImportance = onetImportance;
    }

    public int getUserImportanceTypeId() {
        return userImportanceTypeId;
    }

    public void setUserImportanceTypeId(int userImportanceTypeId) {
        this.userImportanceTypeId = userImportanceTypeId;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getTempDispOrder() {
        return tempDispOrder;
    }

    public void setTempDispOrder(int tempDispOrder) {
        this.tempDispOrder = tempDispOrder;
    }

    public float getScoreCandidate() {
        
        //if( 1==1 )
        //    return 3.845f;
        
        return scoreCandidate;
    }

    public void setScoreCandidate(float scoreCandidate) {
        this.scoreCandidate = scoreCandidate;
    }
    
}
