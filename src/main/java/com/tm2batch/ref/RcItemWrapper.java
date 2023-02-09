/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.ref;

//import com.tm2batch.account.results.ref.RcHistogram;
import com.tm2batch.entity.ref.RcItem;
import com.tm2batch.entity.ref.RcRating;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author miker_000
 */
public class RcItemWrapper implements Comparable<RcItemWrapper>, Cloneable {
    
    int rcItemId;
    float weight;
    // int displayOrder;
    float scoreAvgNoCandidate;
    //RcHistogram histogram;
    boolean showHist = true;
    
    RcCompetencyWrapper tempRcCompetencyWrapper;    
    
    RcItem rcItem;
    
    List<RcRating> rcRatingList;
    
    
    public RcItemWrapper()
    {}
    
    //public RcItemWrapper( int displayOrder, int rcItemId, float weight )
    //{
    //    this.displayOrder = displayOrder;
    //    this.rcItemId = rcItemId;
    //    this.weight = weight;
    //}

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.rcItemId;
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
        final RcItemWrapper other = (RcItemWrapper) obj;
        if (this.rcItemId != other.rcItemId) {
            return false;
        }
        return true;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }    

    @Override
    public String toString() {
        return "RcItemWrapper{" + "rcItemId=" + rcItemId + ", weight=" + weight + ", displayOrder=" + getDisplayOrder() + '}';
    }

    public void clearRatings()
    {
        if( rcRatingList==null )
            return;
        rcRatingList.clear();
    }
    

    
    public void addRating( RcRating rtg )
    {
        if( rcRatingList==null )
            rcRatingList = new ArrayList<>();
        
        rcRatingList.add( rtg );
    }
    
    public boolean getHasRatingData()
    {
        //LogService.logIt( "RcItemWrapper.getHasRatingData() rcItemId=" + rcItemId + ", rcRatingList=" + (rcRatingList==null ? "null" : rcRatingList.size() ) );
        return rcRatingList!=null && !rcRatingList.isEmpty();
    }
    
    public boolean getIsScored()
    {
        return rcItem!=null && rcItem.getIncludeNumRatingB();
    }
    
    public float getAverageScore(List<Long> rcRaterIdsToSkip)
    {
        if( rcRatingList==null)
            return 0;
        
        if( rcItem==null || !rcItem.getIncludeNumRatingB() )
            return 0;
        
        float t = 0;
        float c = 0;
        for( RcRating r : rcRatingList )
        {
            if( !r.getHasScore() )
                continue;
            
            if( rcRaterIdsToSkip!=null && rcRaterIdsToSkip.contains( r.getRcRaterId() ) )
                continue;
            
            t+=r.getFinalScore();
            c++;
        }
        if( c<=0 )
            return 0;
        return t/c;
    }
    
    
    public float getAverageScore()
    {
        if( rcRatingList==null)
            return 0;
        
        if( rcItem==null || !rcItem.getIncludeNumRatingB() )
            return 0;
        
        float t = 0;
        float c = 0;
        for( RcRating r : rcRatingList )
        {
            if( !r.getHasScore() )
                continue;
            
            t+=r.getFinalScore();
            c++;
        }
        if( c<=0 )
            return 0;
        return t/c;
    }
    
    
    public RcRating getRcRating( long rcRaterId )
    {
        if( rcRatingList==null )
            return null;
        for( RcRating r : rcRatingList )
        {
            if( r.getRcRaterId()==rcRaterId )
                return r;
       }
       return null;        
    }
    
    

    @Override
    public int compareTo(RcItemWrapper o) 
    {        
        return ((Integer)getDisplayOrder()).compareTo( o.getDisplayOrder() );           
    }

    public int getRcItemId() {
        return rcItemId;
    }

    public void setRcItemId(int rcItemId) {
        this.rcItemId = rcItemId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getDisplayOrder() {
        return rcItem==null ? 0 : rcItem.getDisplayOrder();
    }

    //public void setDisplayOrder(int displayOrder) {
    //    this.displayOrder = displayOrder;
    //}

    public RcItem getRcItem() {
        return rcItem;
    }

    public void setRcItem(RcItem rcItem) {
        this.rcItem = rcItem;
    }

    public List<RcRating> getRcRatingList() {
        return rcRatingList;
    }

    public void setRcRatingList(List<RcRating> rcRatingList) {
        this.rcRatingList = rcRatingList;
    }

    public float getScoreAvgNoCandidate() {
        return scoreAvgNoCandidate;
    }

    public void setScoreAvgNoCandidate(float scoreAvgNoCandidate) {
        this.scoreAvgNoCandidate = scoreAvgNoCandidate;
    }

    public RcCompetencyWrapper getTempRcCompetencyWrapper() {
        return tempRcCompetencyWrapper;
    }

    public void setTempRcCompetencyWrapper(RcCompetencyWrapper tempRcCompetencyWrapper) {
        this.tempRcCompetencyWrapper = tempRcCompetencyWrapper;
    }

    public boolean getShowHist() {
        return showHist;
    }

    public void setShowHist(boolean showHist) {
        this.showHist = showHist;
    }
        
    

    
}
