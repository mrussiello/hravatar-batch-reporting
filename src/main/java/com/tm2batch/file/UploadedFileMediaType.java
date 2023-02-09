package com.tm2batch.file;

import java.util.Map;
import java.util.TreeMap;


public enum UploadedFileMediaType
{
    ALL(0,"All File Types"),
    IMG_ONLY(1,"Image Files Only"),
    AUDIO_ONLY(2,"Audio Files Only"),
    VIDEO_ONLY(3,"Video Files Only"),
    AUDIOVIDEO_ONLY(4,"Video or Audio Files Only"),
    AUDVIDIMG_ONLY(5,"Images, Video, or Audio Files Only"),
    WORD_ONLY(6,"MS Word Files Only"),
    EXCEL_ONLY(7,"MS Excel Files Only"),
    PPT_ONLY(8,"MS PowerPoint Files Only"),
    OFFICE_ONLY(9,"MS Office (Word, Excel, PPT) Files Only");



    private final int uploadedFileMediaTypeId;

    private String key;


    private UploadedFileMediaType( int p , String key )
    {
        this.uploadedFileMediaTypeId = p;

        this.key = key;
    }




    public int getUploadedFileMediaTypeId()
    {
        return this.uploadedFileMediaTypeId;
    }



    public static Map<String,Integer> getMap()
    {
        Map<String,Integer> outMap = new TreeMap<>();

        UploadedFileMediaType[] vals = UploadedFileMediaType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            outMap.put( vals[i].getKey() , new Integer( vals[i].getUploadedFileMediaTypeId() ) );
        }

        return outMap;
    }



    public String getName()
    {
        return key;
    }


    public String getKey()
    {
        return key;
    }



    public static UploadedFileMediaType getValue( int id )
    {
        UploadedFileMediaType[] vals = UploadedFileMediaType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getUploadedFileMediaTypeId() == id )
                return vals[i];
        }

        return ALL;
    }

}
