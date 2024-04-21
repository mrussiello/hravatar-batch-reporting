package com.tm2batch.custom.orgjustice;


public enum UMinnJusticeGroupType
{
   
    CLINICAL_SUPERVISORS(1,"Clinical Supervisors", "OJCS", new int[]{1,2,3,4}),
    PROGRAM_LEADERS(2,"Program Leaders", "OJPL", new int[]{1,2,3,4}),
    INTERPROF(3,"Interprofessional Team Members", "OJIPTM", new int[]{1,2,3}),
    OPERATIONS(4,"Operations Staff", "OJOS", new int[]{1}),
    CONSULTANTS(5,"Consultants", "OJCON", new int[]{1,2,3}),
    COLLEAGUES(6,"Colleagues", "OJCOL", new int[]{1,2,3,4}),
    PATIENTS(7,"Patients and Families", "OJPAT", new int[]{1});

    private final int uminnJusticeGroupTypeId;
    private final String name;    
    private final String idStub;
    private final int[] justiceTypeIds;
        

    private UMinnJusticeGroupType( int s , String n, String idStub, int[] justiceTypeIds )
    {
        this.uminnJusticeGroupTypeId = s;
        this.name = n;
        this.idStub = idStub;
        this.justiceTypeIds=justiceTypeIds;
    }

    public static UMinnJusticeGroupType getValue( int id )
    {
        UMinnJusticeGroupType[] vals = UMinnJusticeGroupType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getUminnJusticeGroupTypeId() == id )
                return vals[i];
        }

        return null;
    }
    
    public boolean includeItemForGroup( int itemTypeId )
    {
        if( itemTypeId==1 || itemTypeId==3 )
            return true;
            
        if( equals(PROGRAM_LEADERS ) )
            return true;

        if( equals(CLINICAL_SUPERVISORS ) )
            return itemTypeId!=13 && itemTypeId!=15;
        
        if( equals(INTERPROF ) )
            return itemTypeId<=12 && itemTypeId!=4;

        if( equals(CONSULTANTS ) )
            return itemTypeId<=12;
        
        if( equals(COLLEAGUES) )
            return itemTypeId<=12 || itemTypeId==16;

        if( equals(PATIENTS ) )
            return false;
        
        if( equals(OPERATIONS) )
            return false;
        
        return false;
    }
    
    public static UMinnJusticeGroupType getForItemUniqueId( String uniqueId )
    {
        for( UMinnJusticeGroupType t : UMinnJusticeGroupType.values() )
        {
            if( uniqueId.startsWith(t.idStub) )
                return t;
        }
        return null;
    }
    
    public static UMinnJusticeGroupType getForName( String nm )
    {
        for( UMinnJusticeGroupType t : UMinnJusticeGroupType.values() )
        {
            if( t.getName().equalsIgnoreCase(nm) )
                return t;
        }
        return null;
    }
    
    public int getIndex()
    {
        return this.uminnJusticeGroupTypeId - 1;
    }
    
    public int getUminnJusticeGroupTypeId() {
        return uminnJusticeGroupTypeId;
    }

    public String getName() {
        return name;
    }

    public String getIdStub() {
        return idStub;
    }

    public int[] getJusticeTypeIds() {
        return justiceTypeIds;
    }

    

}
