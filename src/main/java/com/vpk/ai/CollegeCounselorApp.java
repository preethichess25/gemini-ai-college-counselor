package com.vpk.ai;

import com.google.adk.models.Gemini;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.BaseAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.FunctionTool;
import com.google.adk.web.AdkWebServer;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.InMemorySessionService;
import com.google.adk.memory.InMemoryMemoryService;

// Optional dev UI (only if google-adk-dev jar is on classpath)
// import com.google.adk.server.AdkWebServer;

import com.vpk.ai.tools.CollegeDatasetTool;
import com.vpk.ai.tools.FitScoreTool;
import com.vpk.ai.tools.MCPFileTool;
import com.vpk.ai.agents.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class CollegeCounselorApp {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Gemini AI College Counselor Demo for ADK 0.4");
        
        Properties props = new Properties();
        try (InputStream is = CollegeCounselorApp.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("Failed to load config.properties from classpath");
            }
            props.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
        
        String apiKey = props.getProperty("gemini.api.key");
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("ERROR: GEMINI_API_KEY is not set in environment variables.");
        }

        // 1) LLM model (adjust constructor if your Gemini class signature differs)

        Gemini model = new Gemini("gemini-2.5-flash", apiKey);

        // 2) Tools

        var collegeDatasetTool = CollegeDatasetTool.create("colleges.json");
        //var fitScoreTool = FitScoreTool.create();
        FunctionTool fitScoreTool = FitScoreTool.create();
        var mcpTool = new MCPFileTool(Path.of("reports"));

        // 3) Create the 5 sub-agents (they return LlmAgent)

        LlmAgent profileCollector = AgentsFactory.profileCollector(model);
        LlmAgent research = AgentsFactory.researchAgent(model, collegeDatasetTool);
        LlmAgent filter = AgentsFactory.filterAgent(model);
        LlmAgent ranking = AgentsFactory.rankingAgent(model, fitScoreTool);
        LlmAgent summary = AgentsFactory.summaryAgent(model, mcpTool);

        // 4) Wrap sub-agents as AgentTool objects (A2A / nested agents)
        AgentTool profileTool = AgentTool.create(profileCollector);
        AgentTool researchTool = AgentTool.create(research);
        AgentTool filterTool = AgentTool.create(filter);
        AgentTool rankingTool = AgentTool.create(ranking);
        AgentTool summaryTool = AgentTool.create(summary);


        // 5) Build a lightweight root orchestrator agent that will call sub-agents (A2A)
        //  This enforces the seq flow from profile -> research -> filter -> rank -> summary

        String rootInstruction = String.join("\n",
        	    "You are the Root Orchestrator Agent for the College Counselor.",
        	    "",
        	    "If the user greets you (hi, hello, hey, vanakkam, etc.) or types something casual, do NOT start the multi-agent pipeline.",
        	    "Instead reply politely:",
        	    "'Hi! I can help you find colleges based on cutoff, course, location, budget, or rank. Tell me any detail and I will help you.'",
        	    "",
        	    "Only when the user shares relevant info such as cutoff marks, preferred course, location, budget, or exam marks, then follow this exact sequence:",
        	    "1) Call the ProfileCollector agent (profileTool).",
        	    "2) Call the Research agent (researchTool).",
        	    "3) Call the Filter agent (filterTool).",
        	    "4) Call the Ranking agent (rankingTool).",
        	    "5) Call the Summary agent (summaryTool)."
        	);

        LlmAgent rootAgent = LlmAgent.builder()
                .name("root_orchestrator")
                .model(model)
                .description("Orchestrates the multi-agent college counseling pipeline using AgentTools.")
                .instruction(rootInstruction)
                // Register sub-agents as tools (AgentTool creates callable agents)
                .tools(List.of(profileTool, researchTool, filterTool, rankingTool, summaryTool))
                .build();

        // -----------------------------
        // 6) Runner: construct with the root agent (InMemoryRunner requires a BaseAgent in ctor)
        // -----------------------------
        BaseAgent rootBase = (BaseAgent) rootAgent;
        InMemoryRunner runner = new InMemoryRunner(rootBase);

        // Sessions & memory (create and supply to UI / runner if needed)
        InMemorySessionService sessionService = new InMemorySessionService();
        InMemoryMemoryService memoryService = new InMemoryMemoryService();

        // 7) Add Web UI (ADK 4.x compatible)

        System.out.println("Starting ADK Dev UI on http://localhost:8000 ...");

        // Optional: change default port
        String port = props.getProperty("adk.web.port", "8000");
        System.setProperty("adk.web.port", port);  

        try {
            AdkWebServer.start(rootAgent); // static start method, no setters needed
        } catch (Exception e) {
            System.err.println("Failed to start ADK Web UI: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("ADK Dev UI running. Use the web interface to interact with the root orchestrator agent.");

        // -----------------------------
        // 8) How to run an example (commented): adapt to the exact runner API you have.
        //    Many ADK runners expose runAsync(userId, sessionId, content) or run(userId, sessionId, content).
        //    Use your IDE to inspect InMemoryRunner methods and call the synchronous/async API shown there.
        // -----------------------------
        /*
        // Example using runAsync (pseudocode: check actual method signature in your JAR)
        com.google.genai.types.Content content = com.google.genai.types.Content.fromParts(
                com.google.genai.types.Part.fromText("Find CSE colleges in Chennai within 6 lakhs budget.")
        );

        // If your InMemoryRunner supports runAsync(userId, sessionId, content):
        runner.runAsync("app", "session-1", content)
              .forEach(event -> {
                  // handle events, finalResponse etc.
                  System.out.println(event);
              });
        */

        System.out.println("Startup complete. Root orchestrator agent loaded. Use Dev UI or runner API to interact.");
    }
}
