package com.vpk.ai.tools;

import com.google.adk.tools.FunctionTool;
import com.vpk.ai.data.CollegeDataLoader;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * CollegeDatasetTool implemented as a FunctionTool.
 * Handles input safely to avoid deserialization errors from JSON arrays.
 */
public class CollegeDatasetTool {

    private static String RESOURCE_NAME = "colleges.json";

    public static FunctionTool create(String resourceName) {
        RESOURCE_NAME = resourceName == null ? RESOURCE_NAME : resourceName;
        return FunctionTool.create(CollegeDatasetTool.class, "search_colleges");
    }

    public static List<Map<String, Object>> search_colleges(
            String city, String course, Integer maxResults) {

        String cityLower = city != null ? city.trim().toLowerCase() : "";
        String courseKey = course != null ? course.trim() : "";
        int limit = maxResults != null ? maxResults : 25;

        try {
            List<Map<String, Object>> colleges = CollegeDataLoader.load(RESOURCE_NAME);

            return colleges.stream()
                    .filter(c -> cityLower.isEmpty() || cityLower.equals(
                            safeToString(c.get("city")).toLowerCase()))
                    .filter(c -> {
                        if (courseKey.isEmpty()) return true;
                        Object coursesObj = c.get("courses");
                        if (!(coursesObj instanceof Map)) return false;
                        Map<String, Object> courses = (Map<String, Object>) coursesObj;
                        return courses.containsKey(courseKey);
                    })
                    .limit(limit)
                    .map(c -> {
                        Map<String, Object> feesMap = getMap(c.get("fees"));
                        Map<String, Object> placementsMap = getMap(c.get("placements"));
                        Map<String, Object> coursesMap = getMap(c.get("courses"));

                        int tuition = getInt(feesMap, "tuition", 0);
                        int avgPlacement = getInt(placementsMap, "avgPackage", 0);
                        boolean hostelAvailable = getBoolean(c, "hostel_available", false);

                        List<String> coursesMatched = courseKey.isEmpty()
                                ? coursesMap.keySet().stream().map(Object::toString).toList()
                                : List.of(courseKey);

                        Map<String, Object> compact = new HashMap<>();
                        compact.put("id", safeToString(c.getOrDefault("id", "")));
                        compact.put("name", safeToString(c.getOrDefault("name", "")));
                        compact.put("city", safeToString(c.getOrDefault("city", "")));
                        compact.put("tuition", tuition);
                        compact.put("avgPlacement", avgPlacement);
                        compact.put("hostel_available", hostelAvailable);
                        compact.put("coursesMatched", coursesMatched);

                        return compact;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("CollegeDatasetTool.search_colleges error: " + e.getMessage(), e);
        }
    }


    // ----------------------
    // Helper extraction methods
    // ----------------------
    @SuppressWarnings("unchecked")
    private static Map<String, Object> getMap(Object obj) {
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        return Map.of();
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

    private static boolean getBoolean(Map<String, Object> outerMap, String key, boolean defaultVal) {
        if (outerMap == null) return defaultVal;
        Object v = outerMap.get(key);
        if (v instanceof Boolean) return (Boolean) v;
        if (v instanceof String) return Boolean.parseBoolean(((String) v).trim());
        if (v instanceof Number) return ((Number) v).intValue() != 0;
        return defaultVal;
    }

    private static String safeToString(Object o) {
        return o == null ? "" : o.toString();
    }
}
