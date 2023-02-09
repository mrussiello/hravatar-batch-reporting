package com.tm2batch.account.results;


public enum TestResultSortType
{
    MOST_RECENT_ACCESS_DESC(0,"trst.MostRecentDesc"),
    MOST_RECENT_ACCESS(1,"trst.MostRecent"),
    LASTNAME(2,"trst.Lastname"),
    LASTNAME_DESC(3,"trst.LastnameDesc"),
    SCORE_DESC(4,"trst.ScoreDesc"),
    SCORE(5,"trst.Score");

    private final int testResultSortTypeId;

    private String key;


    private TestResultSortType( int p , String key )
    {
        this.testResultSortTypeId = p;

        this.key = key;
    }


    public boolean needsUserTable()
    {
        return equals( LASTNAME ) || equals( LASTNAME_DESC );
    }

    public boolean isLastname()
    {
        return equals( LASTNAME ) || equals( LASTNAME_DESC );
    }

    public boolean isScore()
    {
        return equals( SCORE_DESC ) || equals( SCORE );
    }

    public boolean isLastAccess()
    {
        return equals( MOST_RECENT_ACCESS_DESC ) || equals( MOST_RECENT_ACCESS );
    }

    public boolean isDescending()
    {
        return equals( MOST_RECENT_ACCESS_DESC ) || equals( LASTNAME_DESC ) || equals( SCORE_DESC );
    }


    public String getSortFieldStr()
    {
        if( needsUserTable() )
            return " u.lastname AS ulastname ";

        if( isLastAccess() )
            return " t.lastaccessdate AS tlastaccessdate ";

        // if( isLastAccess() )
         return " t.overallscore AS toverallscore ";


    }

    public String getTestEventOrderByStr()
    {
        String out = " ORDER BY ";

        if( isLastAccess() )
            out +=  " tlastaccessdate ";

        else if( isLastname() )
            out +=  " ulastname ";

        else
            out +=  " toverallscore ";

        if( isDescending() )
            out += " DESC ";

        return out;

    }


    public int getTestResultSortTypeId()
    {
        return testResultSortTypeId;
    }





    public static TestResultSortType getType( int typeId )
    {
        return getValue( typeId );
    }


    public String getKey()
    {
        return key;
    }



    public static TestResultSortType getValue( int id )
    {
        TestResultSortType[] vals = TestResultSortType.values();

        for( int i=0 ; i<vals.length ; i++ )
        {
            if( vals[i].getTestResultSortTypeId() == id )
                return vals[i];
        }

        return MOST_RECENT_ACCESS_DESC;
    }

}
