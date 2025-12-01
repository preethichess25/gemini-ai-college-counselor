package com.vpk.ai.agents;

import com.google.adk.agents.LlmAgent;
import com.google.adk.models.Gemini;
import com.google.adk.tools.FunctionTool;

/**
 * RankingAgent: calls FitScoreTool (FunctionTool) for each candidate and sorts evaluations.
 */
public class RankingAgent {

    public static LlmAgent create(Gemini model, FunctionTool fitScoreFunction) {
        String instruction = """
            You are RankingAgent. For each college in 'candidate_summaries', call the function tool 'calculateFit' with:
            {marksPercent, entranceRank, budgetLakhs, collegeCourseInfo}.
            Collect the tool's response (fitScore & explain) and produce 'evaluations' sorted by fitScore desc.
            Save 'evaluations' to memory.
            """;

        var builder = LlmAgent.builder()
                .name("Ranking_Assistant")
                .model(model)
                .instruction(instruction)
                .description("Scores and ranks candidate colleges.");

        if (fitScoreFunction != null) builder.tools(java.util.List.of(fitScoreFunction));
        return builder.build();
    }
}
