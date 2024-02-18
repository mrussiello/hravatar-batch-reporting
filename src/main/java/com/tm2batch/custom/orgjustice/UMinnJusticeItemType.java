package com.tm2batch.custom.orgjustice;

import com.tm2batch.custom.orgjustice.pdf.UMinnJusticeReportUtils;


public enum UMinnJusticeItemType
{
   
    ITEM_1(1),
    ITEM_2(2),
    ITEM_3(3),
    ITEM_4(4),
    ITEM_5(5),
    ITEM_6(6),
    ITEM_7(7),
    ITEM_8(8),
    ITEM_9(9),
    ITEM_10(10),
    ITEM_11(11),
    ITEM_12(12),
    ITEM_13(13),
    ITEM_14(14),
    ITEM_15(15),
    ITEM_16(16);

    private final int uminnJusticeItemTypeId;
        

    private UMinnJusticeItemType( int s  )
    {
        this.uminnJusticeItemTypeId = s;
    }

    public static UMinnJusticeItemType getValue( int id )
    {
        UMinnJusticeItemType[] vals = UMinnJusticeItemType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getUminnJusticeItemTypeId() == id )
                return vals[i];
        }

        return null;
    }
    
    public static UMinnJusticeItemType getForItemUniqueId( String uniqueId )
    {
        int id = OrgJusticeUtils.getItemNumberFromUniqueId(uniqueId);
        if( id>0 && id<=16 )
            return UMinnJusticeItemType.getValue(id);
        return null;
    }
    
    
    public int getUminnJusticeItemTypeId() {
        return uminnJusticeItemTypeId;
    }

    public String getName( UMinnJusticeReportUtils ru ) {
        return ru.getKey( "item." + uminnJusticeItemTypeId );
    }
    

}
