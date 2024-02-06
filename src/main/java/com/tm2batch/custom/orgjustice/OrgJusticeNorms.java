/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tm2batch.custom.orgjustice;

/**
 *
 * @author miker
 */
public class OrgJusticeNorms {
    
    static final float overallAvg = 4.30f;
    
    /*
     0 - Clinical supervisor
     1- program leader
     2- interprofessional team member
     3- operations staff
     4- consultant
     5- colleague
     6- patient family  
    */
    static final float[] groupAverages = new float[]{4.26f,4.42f,4.09f,4.79f,4.26f,4.47f,4.03f};

    
    static final float[] groupAveragesMale = new float[]{4.44f,4.54f,4.32f,4.82f,4.44f,4.58f,4.08f};

    static final float[] groupAveragesFemale = new float[]{4.22f,4.35f,3.98f,4.78f,4.17f,4.47f,4.07f};

    static final float[] groupAveragesUrim = new float[]{3.82f,4.15f,3.78f,4.75f,4.03f,4.27f,3.85f};

    static final float[] groupAveragesNonUrim = new float[]{4.43f,4.51f,4.21f,4.82f,4.33f,4.54f,4.10f};
    
    /*
       0 - Interpersonal Justice
       1 - Informational Justice
       2 - Procedural Justice
       3 - Distributive Justice
    */
    static final float[] dimensionAverages = new float[]{4.33f,4.25f,4.40f,4.22f};
    
    static final float[] dimensionAveragesMale = new float[]{4.47f,4.41f,4.55f,4.42f};
    
    static final float[] dimensionAveragesFemale = new float[]{4.43f,4.19f,4.34f,4.13f};

    static final float[] dimensionAveragesUrim = new float[]{4.13f,3.96f,4.01f,4.02f};

    static final float[] dimensionAveragesNonUrim = new float[]{4.43f,4.36f,4.50f,4.32f};
}
