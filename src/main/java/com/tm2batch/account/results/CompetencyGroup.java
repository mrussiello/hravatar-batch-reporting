/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tm2batch.account.results;

import java.util.List;

/**
 *
 * @author Mike
 */
public class CompetencyGroup
{
    String name;
    String scoreName;
    List<Competency> competencyList;
    boolean showPercentile = true;
    float score;

    public CompetencyGroup( String name, List<Competency> cl, boolean showPercentile )
    {
        this.name = name;
        this.competencyList = cl;
        this.showPercentile = showPercentile;
    }


    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Competency> getCompetencyList() {
        return competencyList;
    }

    public void setCompetencyList(List<Competency> competencyList) {
        this.competencyList = competencyList;
    }



    public boolean isShowPercentile() {
        return showPercentile;
    }

    public void setShowPercentile(boolean showPercentile) {
        this.showPercentile = showPercentile;
    }

    public String getScoreName() {
        return scoreName;
    }

    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }




}
