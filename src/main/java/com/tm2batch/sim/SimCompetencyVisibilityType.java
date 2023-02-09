package com.tm2batch.sim;

import java.util.ArrayList;
import java.util.List;
import jakarta.faces.model.SelectItem;



public enum SimCompetencyVisibilityType
{
    SHOW_IF_HAVE_RESPONSES(0,"Score and Show in reports if item responses present (default)"),
    HIDE_IN_REPORTS(1,"Score but Hide in Reports"),
    SHOW_IN_ALL_REPORTS(2,"Score and Show in reports even if item responses NOT present"),
    HIDE_IN_REPORTS_INCLUDEEXCEL(3,"Score and Show only in Excel Download"),
    HIDE_IN_REPORTS_INCLUDEITEMSRESPONSES(4,"Score but Hide in Reports Except Item Responses");

    private final int simCompetencyVisibilityTypeId;

    private final String name;


    private SimCompetencyVisibilityType( int s , String n )
    {
        this.simCompetencyVisibilityTypeId = s;

        this.name = n;
    }

    public boolean getHideItemScores()
    {
        return equals( HIDE_IN_REPORTS ) || equals( HIDE_IN_REPORTS_INCLUDEEXCEL );
    }
    
    public boolean getHideItemScoresExcel()
    {
        return equals( HIDE_IN_REPORTS );
    }
    
    public boolean getShowInExcel()
    {
        return equals(SHOW_IF_HAVE_RESPONSES ) || equals(SHOW_IN_ALL_REPORTS) || equals( HIDE_IN_REPORTS_INCLUDEEXCEL ) || equals( HIDE_IN_REPORTS_INCLUDEITEMSRESPONSES );
    }

    public boolean getShowItemResponsesOnly()
    {
        return equals( HIDE_IN_REPORTS_INCLUDEITEMSRESPONSES );
    }
    
    public boolean getShowInReports()
    {
        return equals(SHOW_IF_HAVE_RESPONSES ) || equals(SHOW_IN_ALL_REPORTS);
    }
    
    
    public static List<SelectItem> getSelectItemList()
    {
        List<SelectItem> outMap = new ArrayList<>();

        SimCompetencyVisibilityType[] vals = SimCompetencyVisibilityType.values();

        //String name;

        for( int i=0 ; i<vals.length ; i++ )
        {
            outMap.add( new SelectItem( vals[i].simCompetencyVisibilityTypeId, vals[i].name ) );
        }

        return outMap;
    }
    
   

    public static SimCompetencyVisibilityType getValue( int id )
    {
        SimCompetencyVisibilityType[] vals = SimCompetencyVisibilityType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getSimCompetencyVisibilityTypeId() == id )
                return vals[i];
        }

        return SHOW_IF_HAVE_RESPONSES;
    }


    public int getSimCompetencyVisibilityTypeId()
    {
        return simCompetencyVisibilityTypeId;
    }

    public String getName()
    {
        return name;
    }

}
