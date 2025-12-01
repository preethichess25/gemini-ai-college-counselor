package com.vpk.ai.models;


import java.util.List;


public class UserProfile {
public String preferredCity; // null or empty -> any
public List<String> preferredCourses; // e.g., ["Computer Science"]
public double expectedCutoff; // student's score / percentage
public double maxAffordPerSemester; // student's budget per semester
public boolean needsHostel;
public boolean needsBus;
public boolean wantsNcc;
public boolean wantsNss;
public List<String> interests; // optional
}