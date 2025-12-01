package com.vpk.ai.tools;

import com.google.adk.tools.FunctionTool;

import java.util.Map;

/**
 * FitScoreTool as a FunctionTool handler without InvocationContext.
 */
public class FitScoreTool {

    /**
     * Create the FunctionTool instance.
     */
    public static FunctionTool create() {
        return FunctionTool.create(FitScoreTool.class, "calculate_fit");
    }

    /**
     * Expected payload:
     * {
     *   "marksPercent": Number,
     *   "entranceRank": Number,
     *   "budgetLakhs": Number,
     *   "collegeCourseInfo": Map<String,Object>
     * }
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> calculate_fit(Map<String, Object> payload) {
        int marks = getInt(payload, "marksPercent", 0);
        int rank = getInt(payload, "entranceRank", 0);
        int budget = getInt(payload, "budgetLakhs", 0);

        Map<String, Object> courseInfo = (Map<String, Object>) payload.get("collegeCourseInfo");
        int score = Math.max(0, Math.min(100, marks));

        // Add rank-based score
        if (rank > 0) {
            if (rank <= 1000) score += 15;
            else if (rank <= 5000) score += 8;
            else if (rank <= 15000) score += 4;
        }

        // Consider tuition and placement
        if (courseInfo != null) {
            int tuition = getInt(courseInfo, "annual_tuition", 0);
            int budgetInRs = budget * 100_000;
            if (tuition > 0 && tuition > budgetInRs) score -= 20;

            int placementPercent = getInt(courseInfo, "avg_placement_percent", 0);
            score += placementPercent / 10;
        }

        // Clamp score 0-100
        score = Math.max(0, Math.min(100, score));

        return Map.of(
                "fitScore", score,
                "explain", "heuristic(marks+rank+budget+placement)"
        );
    }

    private static int getInt(Map<String, Object> m, String key, int defaultVal) {
        if (m == null) return defaultVal;
        Object v = m.get(key);
        if (v instanceof Number) return ((Number) v).intValue();
        if (v instanceof String) {
            try { return Integer.parseInt(((String) v).trim()); } catch (Exception ignored) {}
        }
        return defaultVal;
    }
}
