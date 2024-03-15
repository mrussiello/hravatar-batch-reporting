package com.tm2batch.custom.orgjustice;

import com.itextpdf.text.BaseColor;


public enum UMinnColorType
{
    

    GREEN(1,"Green", new BaseColor(0x22,0x99,0x54), false ),
    YELLOW(2,"Yellow", new BaseColor(0xf4,0xD0,0x3f), true),
    RED(3,"Red", new BaseColor(0xe7,0x4c,0x3c), false);

    private final int uminnColorTypeTypeId;
    private final String name;    
    private final BaseColor color;
    private final boolean darkFont;
        

    private UMinnColorType( int s , String n, BaseColor color, boolean useDarkFont )
    {
        this.uminnColorTypeTypeId = s;
        this.name = n;
        this.color = color;
        this.darkFont = useDarkFont;
    }

    
    
    public static UMinnColorType getValue( int id )
    {
        UMinnColorType[] vals = UMinnColorType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getUminnColorTypeTypeId() == id )
                return vals[i];
        }

        return GREEN;
    }
    

    public int getUminnColorTypeTypeId() {
        return uminnColorTypeTypeId;
    }

    public String getName() {
        return name;
    }

    public BaseColor getColor() {
        return color;
    }

    public boolean getDarkFont() {
        return darkFont;
    }

    

}
