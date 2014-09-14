package com.vtapadia.fifa.resource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class GlobalPredictionResource extends BasicResponse {
    private Map<String, Integer> goals = new LinkedHashMap();
    private Map<String, Integer> teams = new TreeMap<>();

    private Integer currentGoalCount = 0;

    //TODO Should be improved.
    public static final String GOALS_LESS_THAN_120 = "<120";
    public static final String GOALS_120_to_130 = "120 - 130";
    public static final String GOALS_130_to_140 = "130 - 140";
    public static final String GOALS_140_to_150 = "140 - 150";
    public static final String GOALS_150_to_160 = "150 - 160";
    public static final String GOALS_160_to_170 = "160 - 170";
    public static final String GOALS_170_to_180 = "170 - 180";
    public static final String GOALS_GREATER_THAN_180 = ">180";

    public GlobalPredictionResource() {
        super(true,null);
        goals.put(GOALS_LESS_THAN_120, 0);
        goals.put(GOALS_120_to_130, 0);
        goals.put(GOALS_130_to_140, 0);
        goals.put(GOALS_140_to_150, 0);
        goals.put(GOALS_150_to_160, 0);
        goals.put(GOALS_160_to_170, 0);
        goals.put(GOALS_170_to_180, 0);
        goals.put(GOALS_GREATER_THAN_180, 0);
    }

    public Map<String, Integer> getGoals() {
        return goals;
    }

    public void setGoals(Map<String, Integer> goals) {
        this.goals = goals;
    }

    public Map<String, Integer> getTeams() {
        return teams;
    }

    public void setTeams(Map<String, Integer> teams) {
        this.teams = teams;
    }

    public Integer getCurrentGoalCount() {
        return currentGoalCount;
    }

    public void setCurrentGoalCount(Integer currentGoalCount) {
        this.currentGoalCount = currentGoalCount;
    }
}
