package com.tm2batch.global;

public class Constants
{
    public final static int ON = 1;

    public final static int YES = 1;

    public final static int OFF = 0;

    public final static int NO = 0;

    public final static int MAX_ROWS = 1000;
    
    public final static String DEFAULT_RESOURCE_BUNDLE = "com.tm2batch.resources.TM2Messages";
   
    public final static String DELIMITER = "@#*@";
    
    public static String INTEREST_SURVEY = "interestsurvey";
    public static String CORETEST2 = "coretest2";
    public static String IVRTEST1 = "ivrtest1";
    public static String AVTEST1 = "avtest1";
    public static String IFRAMETEST = "iframetest";
    public static String HRAPH_TEAMLEAD = "hraph_tmldr";
    public static String HRAPH_BSPL = "hraph_bsp1";
    public static String HRAPH_BSP_ITSS = "hraph_bsp2";
    public static String HRAPH_METROBANK_MOSS = "hraph_mb_moss";
    public static String HRAPH_FAST_CORE = "hraph_fastlog_core";
    

    public final static String LVC_TRANSCRIPT = "LVCTRANSCRIPT";
    public final static String LVC_TRANSCRIPT_ENGLISH = "LVCTRANSCRIPTENGLISH";
    public final static String LVC_VIBES = "LVCVIBES";
    public final static String LVC_INSIGHT = "LVCINSIGHT";
    public final static String LVC_RECORDINGSTATUS = "LVCRECORDINGSTATUS";
    

    public static String DESCRIPTION = "DESCRIPTION";
    public static String CAVEATS = "CAVEATS";
    public static String TOPIC_KEY = "TOPIC";
    public static String IBMINSIGHT = "IBMINSIGHT";
    public static String IBMLOWWORDSERROR = "IBMLOWWORDSERROR";
        
    public static int MAX_STR_LEN_FOR_SCORE_TEXT_AS_NUMSCORE = 140;  
    
    public static int MIN_PERCENTILE_COUNT = 25;
    public static int CT2_PERCENTILEBARWIDTH = 578;
    
    public static String ITEMSCOREINFO = "ITEMSCOREINFO";

    public static String COMPETENCYSPECTRUMKEY = "SPECTRUMINFO";
    
    public static String OVERRIDESHOWRAWSCOREKEY = "OVERRIDESHOWRAWSCOREKEY";
    
    public static String SCOREVALUEKEY = "SCOREVALUE";
    public static String SCORETEXTKEY = "SCORETEXT";
    
    public static String CATEGORYINFO = "CATEGINFO";
    
    
    // NEEDED FOR Post processing
    //public final static String CANDIDATE_RECORDING_FILENAME = "candidate-recording";
    //public static final int MAX_MINS_FOR_NO_DATA = 10;
   // public static final int MIN_POSTPROC_WAIT_TIME_MINUTES = 10;
    // public static final int MAX_OV_WAIT_MINS = 30;  // Completed and ready to copy and no status update since.
    
    
    public static float IMAGE_CAPTURE_HIGH_RISK_CUTOFF = 33.33f;
    public static float IMAGE_CAPTURE_MED_RISK_CUTOFF = 75f;
    
    
    
    // NEEDED FOR INSIGHT
    //public final static String DELIMITER = "@#*@";
    
    public static int CT2_COLORGRAPHWID_EML = 220;
    public static int CT2_COLORGRAPHWID = 220;
    public static int CT2_COLORGRAPHWID_SUMMARY = 180;    
    
    public static int MARKER_WIDTH = 7;
    public static float MARKER_LEFT_ADJ = 3.5f;
    public static String[] SCORE_GRAPH_COLS = new String[] {"r","ry","y","yg", "g" };
     public static String IBMINSIGHT_SCORE_GRAPH_COLORS = "rFeFeFe,ryE1F0Fd,yBFE0FE,yg94CCFE,g62B4FE";
   
    
    // NEEDED FOR VIBES
    
    public static int IVR_COLORGRAPHWID = 200;
    public static int IVR_COLORGRAPHHGT = 11;

    public static int VIBES_WAIT_TIME_MINS = 6;
    
    
    public static int RCCHECK_DEF_SCORE_PRECISION_DIGITS = 1;
    
    public static final int DEFAULT_HILOWCOMPETENCIES = 3;

    public static final float RC_MAX_LOWRATED_COMP_SCORE = 4.5f;
    public static final float RC_MIN_HIGHRATED_COMP_SCORE = 7.5f;

    public static final float RC_MAX_LOWRATED_COMP_SCORE_360 = 10f;
    public static final float RC_MIN_HIGHRATED_COMP_SCORE_360 = 1f;
    
    
    public final static int DEFAULT_MAX_ROWS_ANALYTICS = 2000;

    
    ////////////////////////////////////////////////////////
    
    public static final String SERVER_START_LOG_MARKER = "*************************************** TM2Batch SERVER START ******************************************";

    public static String SUPPORT_EMAIL = "support@hravatar.com";

    public static String SYSTEM_ADMIN_EMAIL = "mike@hravatar.com";

    public static String SUPPORT_EMAIL_NOREPLY = "no-reply@hravatar.com";

    public static int MAX_FAILED_LOGON_ATTEMPTS  = 5;
    
    public static int LOGON_LOCKOUT_MINUTES = 30;
    
    public static int MAX_PASSWORD_AGE_MONTHS = 12;
    
    // public static int MAX_BATCH_SIZE = 200;
    
    public static int IDLE_SESSION_TIMEOUT_MINS = 20;    
    
    public static int DEFAULT_MAX_RESULT_ROWS = 2000;
    

}
