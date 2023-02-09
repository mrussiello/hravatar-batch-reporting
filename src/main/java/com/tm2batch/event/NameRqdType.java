package com.tm2batch.event;


public enum NameRqdType
{
    NOT_RQD(0,"rrt.Notrqd"),
    REQUIRED(1,"rrt.Required"),
    REQUIRED_USE_LOGONFORM(2,"rrt.RequiredButUseLoginForm"),
    REQUIRED_USE_USERIDFORM(3,"rrt.RequiredButUseUserIDForm");

    private int nameRequiredTypeId;

    private String key;

    private NameRqdType( int typeId , String key )
    {
        this.nameRequiredTypeId = typeId;

        this.key = key;
    }

    public int getNameRqdTypeId()
    {
        return nameRequiredTypeId;
    }

    public String getKey()
    {
        return key;
    }
}
