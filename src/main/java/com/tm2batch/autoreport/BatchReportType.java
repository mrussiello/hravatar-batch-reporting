package com.tm2batch.autoreport;

import java.util.ArrayList;
import java.util.List;
import jakarta.faces.model.SelectItem;


public enum BatchReportType
{
    EMAIL_FILE(0,"File Email");

    private final int batchReportTypeId;

    private String name;

    private BatchReportType( int p , String key )
    {
        this.batchReportTypeId = p;

        this.name = key;
    }


    public int getBatchReportTypeId()
    {
        return this.batchReportTypeId;
    }



    public static BatchReportType getType( int typeId )
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
        
        for( BatchReportType val : BatchReportType.values() )
        {
            out.add( new SelectItem( val.batchReportTypeId, val.name ) );
        }
        
        return out;
    }
    

    public static BatchReportType getValue( int id )
    {
        BatchReportType[] vals = BatchReportType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getBatchReportTypeId() == id )
                return vals[i];
        }

        return EMAIL_FILE;
    }

}
