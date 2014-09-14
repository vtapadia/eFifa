package com.vtapadia.fifa.resource;

import java.util.List;

public class ProgressResource extends BasicResponse {
    private List<MatchResource> matches;
    private List<UserProgressResource> userProgress;

    public ProgressResource() {
        super(true,null);
    }

    public List<MatchResource> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchResource> matches) {
        this.matches = matches;
    }

    public List<UserProgressResource> getUserProgress() {
        return userProgress;
    }

    public void setUserProgress(List<UserProgressResource> userProgress) {
        this.userProgress = userProgress;
    }
}
