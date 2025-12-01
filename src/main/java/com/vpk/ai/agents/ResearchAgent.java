package com.vpk.ai.agents;

import com.google.adk.agents.LlmAgent;
import com.google.adk.models.Gemini;
import com.google.adk.tools.FunctionTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ResearchAgent factory that registers provided tools safely.
 * Ensures callers use Map<String, Object> inputs for dataset searches.
 */
public final class ResearchAgent {

    private ResearchAgent() {}

    /**
     * Create an LlmAgent for research.
     *
     * @param model               Gemini model instance
     * @param collegeDatasetTool  FunctionTool that searches colleges (required)
     * @param googleSearchTool    optional FunctionTool for web lookups (may be null)
     * @return configured LlmAgent
     */
    public static LlmAgent create(
            Gemini model,
            FunctionTool collegeDatasetTool,
            FunctionTool googleSearchTool
    ) {
        if (model == null) {
            throw new IllegalArgumentException("model must not be null");
        }

        // Build a list of tools that includes only non-null tools.
        List<Object> tools = new ArrayList<>();
        if (collegeDatasetTool != null) tools.add(collegeDatasetTool);
        if (googleSearchTool != null) tools.add(googleSearchTool);

        String instruction = String.join("\n",
                "You are ResearchAgent.",
                "- Use the dataset tool (first tool) to perform structured searches for colleges.",
                "- Always call dataset tool with a Map<String, Object> input that contains keys: 'city', 'course', and optionally 'maxResults'.",
                "- If additional current web info is required, call the optional google tool (if available).",
                "- Return results as a JSON-friendly structure (List of Maps) and do not return raw arrays or non-JSON types."
        );

        LlmAgent.Builder builder = LlmAgent.builder()
                .name("Research_Assistance")
                .model(model)
                .description("Researches colleges, fees, scholarships using dataset + optional Google Search")
                .instruction(instruction);

        // If tools list is not empty, register them. The .tools(...) method expects a List.
        if (!tools.isEmpty()) {
            builder.tools((List) tools); // cast to raw List to match builder signature if necessary
        }

        return builder.build();
    }

    /**
     * Helper that prepares a safe Map input for the CollegeDatasetTool.
     *
     * Always use this to call the dataset tool to avoid passing arrays or unexpected types.
     */
    public static Map<String, Object> prepareToolInput(String city, String course, Integer maxResults) {
        Map<String, Object> input = new HashMap<>();
        input.put("city", city == null ? "" : city.trim());
        input.put("course", course == null ? "" : course.trim());
        input.put("maxResults", maxResults == null ? 25 : maxResults);
        return input;
    }
}
