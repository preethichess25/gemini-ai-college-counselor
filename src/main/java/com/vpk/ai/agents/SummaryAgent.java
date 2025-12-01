package com.vpk.ai.agents;

import com.google.adk.agents.LlmAgent;
import com.google.adk.models.Gemini;
import com.vpk.ai.tools.MCPFileTool;

import java.util.Map;

/**
 * SummaryAgent: creates final human-readable report and optionally writes MCP file.
 */
public class SummaryAgent {

    public static LlmAgent create(Gemini model, MCPFileTool mcp) {
        String instruction = """
            You are SummaryAgent. Read memory: 'user_profile', 'evaluations', 'candidate_summaries', 'search_notes'.
            Produce:
              1) Top 3 recommendations with reasons
              2) Cost comparison summary
              3) Scholarships & documents
              4) Next steps & links
            Save the final plain-text report into 'final_report' memory and return the report as output.
            """;

        var builder = LlmAgent.builder()
                .name("Summary_Assistance")
                .model(model)
                .instruction(instruction)
                .description("Synthesizes final recommendation report.");

        // NOTE: The LLM itself cannot call MCPFileTool directly in this template.
        // After the agent returns a string, your runner code can call mcp.writeReport(...) to persist.
        return builder.build();
    }
}
