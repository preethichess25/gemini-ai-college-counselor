package com.vpk.ai.agents;

import com.google.adk.agents.LlmAgent;
import com.google.adk.models.Gemini;

public class ProfileCollectorAgent extends LlmAgent {

    private ProfileCollectorAgent(Builder builder) {
        super(builder);
    }

    public static ProfileCollectorAgent create(Gemini model) {
        Builder b = LlmAgent.builder()
                .name("Student_Profile_Assistant")
                .model(model)
                .description("Collects student profile information for college counseling")
                .instruction(
                    "Hi! I can help you find colleges in your preferred location based on your cutoff marks.\n" +
                    "Before we start, please tell me your percentage in your last qualifying exam (e.g., 12th grade or equivalent).")
                .disallowTransferToParent(false)
                .disallowTransferToPeers(false);
        return new ProfileCollectorAgent(b);
    }
}
