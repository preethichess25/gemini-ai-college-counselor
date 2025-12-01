package com.vpk.ai.agents;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.BaseTool;

public class ScholarshipAgent {
    public static LlmAgent create(BaseTool searchTool) {
        return LlmAgent.builder()
                .name("Scholarship_Assistance")
                .description("Extracts scholarship & financial aid info from candidate colleges")
                .instruction("""
                    For each candidate college, collect scholarship entries and return 'scholarships_by_college'.
                    """)
                .tools(java.util.List.of(searchTool))
                .build();
    }
}
