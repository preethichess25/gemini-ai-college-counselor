package com.vpk.ai.models;


import java.util.List;
import java.util.Map;


public class CollegeRecord {
public String id;
public String name;
public String city;
public String state;
public List<Map<String,Object>> courses; // each course: {"name": "Computer Science", "cutoff": 95}
public boolean hostel_available;
public boolean bus_availability;
public boolean ncc_available;
public boolean nss_available;
public List<String> sports_teams;
public List<String> clubs;
public List<String> other_facilities;
public double placements_percent;
public Map<String,Object> fees_breakup; // e.g., {"avg_per_semester": 75000}
public Map<String,String> contact; // {"email":"...","phone":"..."}
public String description;


// runtime/computed
public double fitScore;


@Override
public String toString() {
String fee = (fees_breakup!=null && fees_breakup.get("avg_per_semester")!=null) ? String.valueOf(fees_breakup.get("avg_per_semester")) : "N/A";
return String.format("%s (%s) — city=%s placements=%.1f%% fees(avg/sem)=%s fit=%.2f",
name, id, city, placements_percent, fee, fitScore);
}
}