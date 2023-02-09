package com.tm2batch.autoreport;

import java.util.ArrayList;
import java.util.List;
import jakarta.faces.model.SelectItem;


public enum BatchReportStatusType
{
    DISABLED(0,"Disabled"),
    ACTIVE(1,"Active");

    private final int batchReportStatusTypeId;

    private String name;

    private BatchReportStatusType( int p , String key )
    {
        this.batchReportStatusTypeId = p;

        this.name = key;
    }


    public int getBatchReportStatusTypeId()
    {
        return this.batchReportStatusTypeId;
    }



    public static BatchReportStatusType getType( int typeId )
    {
        return getValue( typeId );
    }

    public String getName()
    {
        return name;
    }


    public static List<SelectItem> getSelectItemList()
    {
        List<SelectItem> out = new ArrayList<>();
        
        for( BatchReportStatusType val : BatchReportStatusType.values() )
        {
            out.add( new SelectItem( val.batchReportStatusTypeId, val.name ) );
        }
        
        return out;
    }
    

    public static BatchReportStatusType getValue( int id )
    {
        BatchReportStatusType[] vals = BatchReportStatusType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getBatchReportStatusTypeId() == id )
                return vals[i];
        }

        return DISABLED;
    }

}
