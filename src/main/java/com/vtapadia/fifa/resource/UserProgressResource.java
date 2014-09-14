package com.vtapadia.fifa.resource;

import java.util.ArrayList;
import java.util.List;

public class UserProgressResource extends BasicResponse {
    private String userId;
    private String userName;
    private long points;
    private List<Integer> progression = new ArrayList<>();

    public UserProgressResource() {
        super(true,null);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public List<Integer> getProgression() {
        return progression;
    }

    public void setProgression(List<Integer> progression) {
        this.progression = progression;
    }

    public void addProgression(Integer progress) {
        this.progression.add(progress);
    }
}
