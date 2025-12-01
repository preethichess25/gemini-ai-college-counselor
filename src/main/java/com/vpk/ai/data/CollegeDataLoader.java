package com.vpk.ai.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class CollegeDataLoader {

    private static List<Map<String,Object>> colleges;

    public static synchronized List<Map<String,Object>> load(String resourceName) {
        if (colleges != null) return colleges;
        try {
            ObjectMapper om = new ObjectMapper();
            InputStream is = CollegeDataLoader.class.getClassLoader().getResourceAsStream(resourceName);
            if (is == null) {
                colleges = om.readValue(new java.io.File(resourceName), new TypeReference<List<Map<String,Object>>>(){});
            } else {
                colleges = om.readValue(is, new TypeReference<List<Map<String,Object>>>(){});
            }
            return colleges;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load colleges: " + e.getMessage(), e);
        }
    }
}
