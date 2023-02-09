/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.ref;

import com.tm2batch.entity.ref.RcCheck;
import com.tm2batch.entity.ref.RcOrgPrefs;
import com.tm2batch.entity.ref.RcRater;
import com.tm2batch.entity.ref.RcRating;
import com.tm2batch.entity.ref.RcReferral;
import com.tm2batch.entity.ref.RcScript;
import com.tm2batch.entity.report.Report;
import com.tm2batch.entity.user.Org;
import com.tm2batch.entity.user.User;
import com.tm2batch.global.Constants;

import com.tm2batch.global.RuntimeConstants;
import com.tm2batch.service.LogService;
import com.tm2batch.user.UserFacade;
import com.tm2batch.util.I18nUtils;
import com.tm2batch.util.MessageFactory;
import com.tm2batch.util.NVPair;
import com.tm2batch.util.NumberUtils;
import com.tm2batch.util.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author miker_000
 */
public class RcCheckUtils {
    
    private static List<Integer> rcItemIdsForPriorRole;
    
    UserFacade userFacade;
    RcFacade rcFacade;
    RcScriptFacade rcScriptFacade;
    
    
    private static synchronized void init()
    {
        if( rcItemIdsForPriorRole!=null )
            return;
        try
        {
            rcItemIdsForPriorRole = RuntimeConstants.getIntList("RefCheckPriorRoleRcItemIds", ","); 
        }
        catch( Exception e )
        {
            LogService.logIt( e, "RcCheckUtils.init()" );            
        }
    }
    
    
    public void loadRcCheckForScoringOrResults( RcCheck rc) throws Exception
    {
        if( rc==null )
            return;
        
        if( userFacade==null )
            userFacade=UserFacade.getInstance();

        if( rc.getUser()==null )
            rc.setUser( userFacade.getUser( rc.getUserId() ));
        
        //if( rc.getOrg()==null )
        //    rc.setOrg( userFacade.getOrg( rc.getOrgId() ));
        
        if( rc.getAdminUser()==null && rc.getAdminUserId()>0 )
            rc.setAdminUser( userFacade.getUser( rc.getAdminUserId() ));
        
        // rc.setLocale( getLocale() );
        
        if( rcFacade==null )
            rcFacade = RcFacade.getInstance();
        
        //if( rc.getRcOrgPrefs()==null )
        //    rc.setRcOrgPrefs( rcFacade.getRcOrgPrefsForOrgId( rc.getOrgId( )));

        //if( refUserType.getIsCandidate() )
        //{
        //if( rc.getRcScript()==null )
        //{
        if( rcScriptFacade==null )
            rcScriptFacade = RcScriptFacade.getInstance();
        
        if( rc.getRcScript()==null )
            rc.setRcScript( rcScriptFacade.getRcScript( rc.getRcScriptId() ) );
        
        //RcScript rcs = rcScriptFacade.getRcScript( rc.getRcScriptId() );
        //rc.setRcScript( rcs ); // (RcScript)rcs.clone() );
        rcScriptFacade.loadScriptObjects(rc.getRcScript(), true );
        //}
        
            
        // if( rc.getRcRaterList()==null )
        rc.setRcRaterList( rcFacade.getRcRaterList( rc.getRcCheckId() ));                

        for( RcRater r : rc.getRcRaterList() )
        {
            if( r.getUser()==null )
                r.setUser( userFacade.getUser( r.getUserId() ));

            // r.setLocale( getLocale() );                
            r.setRcRaterSourceType( RcRaterSourceType.getForRcRater(rc, r));
            
            r.setRcRatingList( rcFacade.getRcRatingList( rc.getRcCheckId(), r.getRcRaterId() ) );
            setRcRatingsInScript( rc.getRcScript(), r.getRcRatingList(), true );
        }
        
        long candidateRaterId = rc.getCandidateRcRaterId();
        List<Long> rcRaterIdsToSkip = null;        
        if( candidateRaterId>0  && ( rc.getCandidateCanAddRatersB() || rc.getRcRaterList().size()>1 ))
        {
            rcRaterIdsToSkip = new ArrayList<>();
            rcRaterIdsToSkip.add(candidateRaterId);
        }
        for( RcCompetencyWrapper rcw : rc.getRcScript().getRcCompetencyWrapperList() )
        {
            for( RcItemWrapper rciw : rcw.getRcItemWrapperList() )
            {
                if( !rciw.getRcItem().getIsItemScored() )
                    continue;
                rciw.setScoreAvgNoCandidate( rciw.getAverageScore(rcRaterIdsToSkip));
            }
        }
        
        if( rc.getRcCheckType().getIsPrehire() && rc.getRcReferralList()==null )
        {
            if( rcFacade==null )
                rcFacade = RcFacade.getInstance();
            rc.setRcReferralList(rcFacade.getRcReferralList(rc.getRcCheckId()) );
        }
        
        // do this after Raters are fully loaded
        
        // load referrers
        if( rc.getRcCheckType().getIsPrehire() && rc.getRcReferralList()!=null && !rc.getRcReferralList().isEmpty() )
        {
            for( RcReferral rl : rc.getRcReferralList() )
            {
                if( rl.getRcScript()==null )
                {
                    if( rcScriptFacade==null )
                        rcScriptFacade=RcScriptFacade.getInstance();
                    rl.setRcScript( rcScriptFacade.getRcScript( rl.getRcScriptId() ));
                }
                
                if( rl.getUser()==null )
                {
                    if( userFacade==null )
                        userFacade = UserFacade.getInstance();
                    rl.setUser( userFacade.getUser( rl.getUserId() ));
                }
                
                if( rl.getReferrerUser()==null )
                {
                    for( RcRater rtr : rc.getRcRaterList() )
                    {
                        if( rtr.getUserId()==rl.getReferrerUserId() )
                            rl.setReferrerUser(rtr.getUser() );
                    }
                }
                
                // should not happen
                if( rl.getReferrerUser()==null )
                {
                    if( userFacade==null )
                        userFacade = UserFacade.getInstance();
                    rl.setReferrerUser( userFacade.getUser( rl.getReferrerUserId() ));
                }
            }
        }
        
    }
    
    
    public static boolean getBooleanFlagValue( Org org, Report report, String name )
    {
        List<NVPair> pl = org.getReportFlagList( null, report, null );

        // LogService.logIt( "TestEvent.getBooleanFlagValue() " + name + ", Report Flag list is " + pl.size() + ", reportId=" + report.getReportId() );
        
        for( NVPair p : pl )
        {
            if( p.getName().equals( name ) && p.getValue().equals( "1" ) )
                return true;
        }

        return false;        
    }
    
    public static float invertRatingScore( float score )
    {
        if( score>=1 && score<=10 )
            return 11f - score;
        
        return score;
    }
    
    
    
    public List<RcCompetencyWrapper> getLowScoringRcCompetencies( RcCheck rc, int maxQuestions )
    {
        return getRcCompetenciesSubList( rc, false, rc.getRcCheckType().getIsPrehire() ? Constants.RC_MAX_LOWRATED_COMP_SCORE : Constants.RC_MAX_LOWRATED_COMP_SCORE_360, maxQuestions );
    }
    
    public List<RcCompetencyWrapper> getHighScoringRcCompetencies( RcCheck rc, int maxQuestions )
    {
        return getRcCompetenciesSubList( rc, true, rc.getRcCheckType().getIsPrehire() ? Constants.RC_MIN_HIGHRATED_COMP_SCORE : Constants.RC_MIN_HIGHRATED_COMP_SCORE_360, maxQuestions );
    }
        
    public List<RcCompetencyWrapper> getRcCompetenciesSubList( RcCheck rc, boolean high, float cutoff, int maxQuestions )
    {
        List<RcCompetencyWrapper> out = new ArrayList<>();
        
        if( !rc.getRcCheckStatusType().getComplete())
            return out;
        
        //if( rc.getRcScript()==null || rc.getRcRaterList()==null )
        //    this.loadRcCheckForScoringOrResults(rc);
        
        for( RcCompetencyWrapper rcw : rc.getRcScript().getRcCompetencyWrapperList() )
        {
            // LogService.logIt( "RcCheckUtils.getRcCompetenciesForInterviewQuestions() competency=" + rcw.getRcCompetency().getName() + ", has interview=" + rcw.getRcCompetency().getHasInterviewQuestion() + ", score=" + rcw.getScoreAvgNoCandidate() );

            // no interview question
            //if( hasInterviewOnly && !rcw.getRcCompetency().getHasInterviewQuestion() )
            //    continue;
            
            // not scored.
            if( rcw.getScoreAvgNoCandidate()<=0 )
                continue;
            
            // above
            if( high && rcw.getScoreAvgNoCandidate()<cutoff )
                continue;
            // below
            if( !high && rcw.getScoreAvgNoCandidate()>cutoff )
                continue;
            
            out.add( rcw );
        }
        
        Collections.sort( out, new RcCompetencyWrapperScoreComparator(high) );
        
        if( maxQuestions>0 && out.size()>maxQuestions )
            return out.subList(0, maxQuestions);
        
        return out;
    }
    
    
    
    public String getReportName( RcCheck rc, Report r, Locale locale ) {

        String ttl = "";
        
        if( r!=null && r.getStrParam3()!=null && !r.getStrParam3().isEmpty() )
            ttl = r.getStrParam3();
        
        else if( r!=null && r.getStrParam2()!=null && !r.getStrParam2().isEmpty() )
        {
            String key = r.getStrParam2(); // "g.TestResultsAndInterviewGuide";
        
            ttl = MessageFactory.getStringMessage(locale, key, null );
            
            if( ttl ==null )
                ttl = r.getTitle();
        }
        
        else if( r.getTitle()!=null && !r.getTitle().isEmpty() )
            ttl = r.getTitle();            
        
        else
            ttl = r.getName();
        
        return StringUtils.replaceStr(ttl, "[SCRIPTNAME]" , rc.getRcScript().getName() );
    }
    
    /**
     * /ta/rcpdfdnld.pdf?rc=rcCheckIdEncrypted&r=reportId&l=langCode
     * @param rc
     * @param reportId
     * @param langStr
     * @return 
     */
    public String getPdfDownloadUrl( RcCheck rc, int reportId, String langStr )
    {
        if( rc==null )
            return "";
        
        String u = RuntimeConstants.getStringValue( "RcPdfDownloadUrl" ) + "?rc=" + rc.getRcCheckIdEncrypted();
        if( reportId>0 )
            u += "&r=" + reportId;
        if( langStr!=null && !langStr.isBlank() )
            u += "&l=" + langStr;
        return u;
    }
    
    
    public List<Integer> getReportIdsForRcCheck( RcCheck rc, String langStr ) throws Exception
    {
        List<Integer> out = new ArrayList<>();
        
        if( rc.getReportId()>0 )
        {
            out.add( rc.getReportId() );
            if( rc.getReportId2()>0 )
                out.add( rc.getReportId2() );            
        }
        if( !out.isEmpty() )
            return out;
        
        int reportId = 0;
        int reportId2 = 0;
        if( rcFacade==null )
            rcFacade=RcFacade.getInstance();
        RcOrgPrefs rcop = rcFacade.getRcOrgPrefsForOrgId( rc.getOrgId() );
        if( rcop!=null )
        {
            reportId = rc.getRcCheckType().getIsPrehire() ? rcop.getReportIdPrehire() : rcop.getReportIdEmployee();
            if( reportId>0 )
            {
                reportId2 = rc.getRcCheckType().getIsPrehire() ? 0 : rcop.getReportIdEmployeeFbk();
                out.add( reportId );
                if( reportId2>0 )
                    out.add( reportId2 );
            }
            if( !out.isEmpty() )
                return out;
        }

        if( langStr==null || langStr.isBlank() )
            langStr = rc.getLangCode();
        if( langStr==null || langStr.isBlank() )
            langStr = "en_US";

        Locale locale = I18nUtils.getLocaleFromCompositeStr(langStr);
        String lang = locale.getLanguage();

        // LogService.logIt("RcCheckUtils.getReportIdForRcCheck() langStr=" + langStr + ", lang=" + lang + ", comp=" + ("DefaultRcReportPrehire" + "_" + lang)  );
        reportId = RuntimeConstants.getIntValue( rc.getRcCheckType().getIsPrehire() ? "DefaultRcReportPrehire" + "_" + lang : "DefaultRcReportEmployee" + "_" + lang );        
        if( reportId<=0 )
            reportId = RuntimeConstants.getIntValue( rc.getRcCheckType().getIsPrehire() ? "DefaultRcReportPrehire" : "DefaultRcReportEmployee" ); 
        
        reportId2 = rc.getRcCheckType().getIsPrehire() ? 0 : RuntimeConstants.getIntValue( "DefaultRcReportEmployeeFbk" + "_" + lang );
        if( !rc.getRcCheckType().getIsPrehire() && reportId2<=0 )
            reportId2 = RuntimeConstants.getIntValue( "DefaultRcReportEmployeeFbk" ); 
        
        if( reportId>0 )
        {
            out.add( reportId );
            if( reportId2>0 )
                out.add( reportId2 );            
        }
        return out;
    }

    /*
    public int getReportIdForRcCheck( RcCheck rc, String langStr ) throws Exception
    {
        if( rc.getReportId()>0 )
            return rc.getReportId();
        
        int reportId = 0;
        if( rcFacade==null )
            rcFacade=RcFacade.getInstance();
        RcOrgPrefs rcop = rcFacade.getRcOrgPrefsForOrgId( rc.getOrgId() );
        if( rcop!=null )
        {
            reportId = rc.getRcCheckType().getIsPrehire() ? rcop.getReportIdPrehire() : rcop.getReportIdEmployee();
            if( reportId>0 )
                return reportId;
        }

        if( langStr==null || langStr.isBlank() )
            langStr = rc.getLangCode();
        if( langStr==null || langStr.isBlank() )
            langStr = "en_US";

        Locale locale = I18nUtils.getLocaleFromCompositeStr(langStr);
        String lang = locale.getLanguage();

        // LogService.logIt("RcCheckUtils.getReportIdForRcCheck() langStr=" + langStr + ", lang=" + lang + ", comp=" + ("DefaultRcReportPrehire" + "_" + lang)  );
        reportId = RuntimeConstants.getIntValue( rc.getRcCheckType().getIsPrehire() ? "DefaultRcReportPrehire" + "_" + lang : "DefaultRcReportEmployee" + "_" + lang );
        if( reportId>0 )
            return reportId;

        return RuntimeConstants.getIntValue( rc.getRcCheckType().getIsPrehire() ? "DefaultRcReportPrehire" : "DefaultRcReportEmployee" );        
    }
    */
    
    
    public void setRcRatingsInScript( RcScript rcScript, List<RcRating> rcrl, boolean forScoringOrReporting) throws Exception
    {
        if( rcrl==null  )
            throw new Exception( "RcRatingList is null" );

        if( rcScript==null )
            throw new Exception( "RcScript is null" );

        if( rcScript.getAllItemWrapperList()==null  )
            throw new Exception( "RcScript.itemWrapperList is null" );

        if( rcrl.isEmpty() )
            return;
        
        rcScript.clearRatings();
        for( RcItemWrapper rciw : rcScript.getAllItemWrapperList() )
        {
            for( RcRating rcr : rcrl )
            {
                if( rcr.getRcItemId()==rciw.getRcItemId() )
                {
                    rcr.setRcItem( rciw.getRcItem() );
                    if( forScoringOrReporting )
                        rciw.addRating(rcr);
                }
            }
        }        
    }
    
    
    
    public float computeRcCheckPercentComplete( RcCheck rc ) throws Exception
    {
        
        if( rcFacade==null )
            rcFacade = RcFacade.getInstance();
        List<RcRater> rcrl = rcFacade.getRcRaterList( rc.getRcCheckId() );
        float ct = 0;
        float total = 0;
        int minRaters = rc.getMinRaters();
        int minSups = rc.getMinSupervisors();
        int rtrs = 0;
        int sups = 0;
        for( RcRater rcr : rcrl )
        {
            rtrs++;            
            if( rcr.getRcRaterRoleType().getIsSupervisorOrManager() )
                sups++;
            
            if( rcr.getRcRaterStatusType().getIsDeactivated() || rcr.getRcRaterStatusType().getIsExpired() || rcr.getRcRaterStatusType().getIsRejected() ) 
                continue;
            
            ct++;
            total += rcr.getPercentComplete();
        }
        
        if( ct==0 )
            return 0;
        
        int missingRtrs = Math.max(minRaters - rtrs,0);
        int missingSups = Math.max(minSups - sups,0);
        
        // The number of missing raters is higher of these. 
        missingRtrs = Math.max(missingRtrs, missingSups);
        
        if( missingRtrs>0 )
            ct += missingRtrs;
        
        // adjust if still need a candidate to complete and there is no candidate rater.
        if( !rc.getCollectCandidateRatingsB() && rc.getNeedsAnyCandidateInput() && !rc.getRcCandidateStatusType().getIsCompletedOrHigher() )
            ct++;
        
        total = total/ct;

        if( rc.getCollectCandidateRatingsB() && ct<=1 )
            total = Math.min( 50f, total );
        
        return (float) NumberUtils.roundIt(total, 0);
    }
    
    public float computeRcCheckOverallScore( RcCheck rc ) throws Exception
    {
        //if( rc.getPercentComplete()<100 )
        //    return 0;
        
        if( rcFacade==null )
            rcFacade=RcFacade.getInstance();
        List<RcRater> rcrl = rcFacade.getRcRaterList( rc.getRcCheckId() );
        float ct = 0;
        float total = 0;
        for( RcRater rcr : rcrl )
        {
            if( rcr.getRcRaterType().getIsCandidateOrEmployee() && ( rc.getCandidateCanAddRatersB() || rcrl.size()>1 ) )
                continue;
            
            if( !rcr.getRcRaterStatusType().getIsComplete() ) 
                continue;
            ct++;
            total += rcr.getOverallScore();
        }
        if( ct>0 )
            total = total/ct;

        return (float) NumberUtils.roundIt(total, 2);
    }
    
    public static String performMessageSubstitutions( String inStr, RcCheck rc, User raterUser, Org org, Locale locale, TimeZone timeZone, String url )
    {
        if( inStr==null || inStr.isBlank() )
            return inStr;
        String s = StringUtils.replaceStr(inStr, "[CANDIDATENAME]" , rc.getUser().getFullname() );
        if( raterUser!=null  )
            s = StringUtils.replaceStr( s, "[RATERNAME]", raterUser.getFullname() );
        s = StringUtils.replaceStr( s, "[TYPE]", rc.getRcCheckType().getName(locale) );
        s = StringUtils.replaceStr( s, "[COMPANY]", org.getName() );        
        s = StringUtils.replaceStr( s, "[TEMPLATE]", rc.getRcScript().getName() );        
        s = StringUtils.replaceStr( s, "[EXPIRE]", I18nUtils.getFormattedDateTime(locale, rc.getExpireDate(), timeZone) );        
        s = StringUtils.replaceStr( s, "[URL]", url );
        return s;
    }
    
    
    
    public static String performSubstitutions( String inStr, RcCheck rc, Org org, Locale locale )
    {
        if( inStr==null || inStr.isBlank() )
            return inStr;
        String s = StringUtils.replaceStr(inStr, "[CANDIDATENAME]" , rc.getUser().getFullname() );
        s = StringUtils.replaceStr( s, "[REFCHECKTYPENAME]", rc.getRcCheckType().getName(locale) );
        s = StringUtils.replaceStr( s, "[CURRENTCOMPANY]", org.getName() );        
        return s;
    }
    
    public static void correctRcRaterListForReporting( RcCheck rc )
    {
        if( rc.getRcRaterList()==null || !rc.getRcCheckType().getIsEmployee() || !rc.getCollectCandidateRatingsB() )
            return;
        
        List<RcRater> rlst = new ArrayList<>();
        for( RcRater rater : rc.getRcRaterList() )
        {
            if( !rater.getRcRaterType().getIsCandidateOrEmployee() )
                continue;
            rlst.add(rater);
        }
        for( RcRater rater : rc.getRcRaterList() )
        {
            if( rater.getRcRaterType().getIsCandidateOrEmployee() )
                continue;
            rlst.add(rater);
        }
        rc.setRcRaterList(rlst);
    }

    
    public static void addCandidateRoleRespToRaterRoleRespItemResponses( RcCheck rc, Locale loc ) throws Exception
    {
        // No raters, or not prehire
        if( rc.getRcRaterList()==null )
            return;
        
        init();
        
        // List<Integer> itemIds = RuntimeConstants.getIntList( "RefCheckPriorRoleRcItemIds", "," );
        RcRater rater;
        for( RcItemWrapper rciw : rc.getItemWrappersWithRatingsList() )            
        {
            // Not a "Prior Role Item"
            if( !rcItemIdsForPriorRole.contains( (int) rciw.getRcItemId() ) )
                    continue;
            
            for( RcRating rcr : rciw.getRcRatingList() )
            {
                // skip ratings from candidate
                if( rcr.getRcRaterId()==rc.getCandidateRcRaterId() )
                    continue;
                
                // get rater for this rating.
                rater = rc.getRcRaterForRcRaterId( rcr.getRcRaterId() );
                if( rater==null || 
                    rater.getCandidateRoleResp()==null || 
                    rater.getCandidateRoleResp().isBlank() || 
                    !rater.getRcRaterSourceType().getIsCandidateOrEmployee() )
                    continue;
                
                // set subtext.
                rcr.setSubtext( MessageFactory.getStringMessage( loc, rc.getRcCheckType().getIsPrehire() ? "g.RCFmCandidate" : "g.RCFmEmployee" , new String[]{rater.getCandidateRoleResp()}));
            }
        }        
    }
    
        
}
