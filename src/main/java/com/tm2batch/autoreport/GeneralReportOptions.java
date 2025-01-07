/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.autoreport;

import java.io.Serializable;

/**
 *
 * @author miker_000
 */
public interface GeneralReportOptions extends Serializable {

    boolean isBattery();

    boolean isMultiUseLink();

    // int getReportTypeId();

    boolean isAccountPercentile();

    boolean isAltScores();

    boolean isCompetencies();

    boolean isCompleted();

    boolean isCountryPercentile();

    boolean isCustom1();

    boolean isCustom2();

    boolean isCustom3();

    boolean isDepartment();

    boolean isDetailViewUrl();

    boolean isEmail();

    boolean isIdentifier();

    boolean isIncludeCandFbkReptsInPdfDwnld();

    boolean isIncludeFieldsForPerfUpload();

    boolean isItemResponses();

    boolean isMobile();

    boolean isName();

    boolean isOverallPercentile();

    boolean isOverallScore();

    boolean isPerformanceValues();

    boolean isResponseRatings();

    boolean isSendCandidateFbkReports();

    boolean isStarted();

    boolean isTestAdminUser();

    boolean isTestCreditsUsed();

    boolean isTestEventType();

    boolean isTestName();

    boolean isTestTakerIdentifier();

    boolean isUserNoteDates();

    boolean isUserNotes();

    boolean isUserStatus();
    
    boolean isUserDemographics();
    
    public boolean isTotalSeconds();    
    
    public boolean isAvgRespRatings();
    
    public boolean isSuspiciousActivity();
    
    public boolean isPlagiarism();    
    
}
