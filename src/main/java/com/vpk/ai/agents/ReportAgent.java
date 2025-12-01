package com.vpk.ai.agents;

import com.google.adk.agents.LlmAgent;

public class ReportAgent {

    public static LlmAgent create() {
        return LlmAgent.builder()
                .name("Reporting_Assistant")
                .description("Generates the final student-friendly report & next steps.")
                .instruction("""
                    Merge user_profile, candidate_colleges, scholarships_by_college, evaluations, cost_estimates.
                    Output:
                      - Top 3 recommendations
                      - For each: quick reasons, fit score, cost summary
                      - Scholarships & actions (documents & deadlines)
                      - Next steps (apply, counseling dates, documents)
                    Return plain text for the user.
                    """)
                .build();
    }
}
