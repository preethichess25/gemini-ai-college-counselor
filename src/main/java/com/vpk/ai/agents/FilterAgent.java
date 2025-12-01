package com.vpk.ai.agents;

import com.google.adk.agents.LlmAgent;
import com.google.adk.models.Gemini;

/**
 * FilterAgent: deterministic filtering and context compaction
 */
public class FilterAgent {

    public static LlmAgent create(Gemini model) {
        String instruction = """
            You are FilterAgent. Read 'candidate_colleges' and 'user_profile'. Apply deterministic rules:
              - Remove colleges with tuition > 1.3 * budget unless scholarships exist.
              - Prefer colleges with placementPercent > 50 when focus=='placement'.
            Compact each college to a summary (max 100 chars) and save 'candidate_summaries'.
            Return 'FILTER_DONE'.
            """;

        return LlmAgent.builder()
                .name("Filtering_Assistant")
                .model(model)
                .instruction(instruction)
                .description("Filters and compacts shortlisted colleges.")
                .build();
    }
}
