package com.vpk.ai.tools;

/**
 * Input for CollegeDatasetTool
 */
public class CollegeSearchInput {
    public String city = "";
    public String course = "";
    public Integer maxResults = 25;

    // Default constructor needed for Jackson
    public CollegeSearchInput() {}

    public CollegeSearchInput(String city, String course, Integer maxResults) {
        this.city = city != null ? city : "";
        this.course = course != null ? course : "";
        this.maxResults = maxResults != null ? maxResults : 25;
    }
}
