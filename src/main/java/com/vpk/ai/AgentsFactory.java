package com.vpk.ai;

import com.google.adk.models.Gemini;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;

import com.vpk.ai.agents.ProfileCollectorAgent;
import com.vpk.ai.agents.ResearchAgent;
import com.vpk.ai.agents.FilterAgent;
import com.vpk.ai.agents.RankingAgent;
import com.vpk.ai.agents.SummaryAgent;
import com.vpk.ai.tools.MCPFileTool;

public class AgentsFactory {

    public static LlmAgent profileCollector(Gemini model) {
        return ProfileCollectorAgent.create(model);
    }

    /**
     * Research agent uses:
     * - collegeDatasetTool (required)
     * - googleSearchTool (optional)
     */
    public static LlmAgent researchAgent(
            Gemini model,
            FunctionTool collegeDatasetTool,
            FunctionTool googleSearchTool   // can be null
    ) {
        return ResearchAgent.create(model, collegeDatasetTool, googleSearchTool);
    }

    public static LlmAgent researchAgent(
            Gemini model,
            FunctionTool collegeDatasetTool
    ) {
        // Default: no google tool
        return ResearchAgent.create(model, collegeDatasetTool, null);
    }

    public static LlmAgent filterAgent(Gemini model) {
        return FilterAgent.create(model);
    }

    public static LlmAgent rankingAgent(Gemini model, FunctionTool fitFunction) {
        return RankingAgent.create(model, fitFunction);
    }

    public static LlmAgent summaryAgent(Gemini model, MCPFileTool mcp) {
        return SummaryAgent.create(model, mcp);
    }
}
