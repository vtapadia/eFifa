package com.vtapadia.fifa.resource;

public class PredictionResource extends BasicResponse {
    private long id;
    private MatchResource match;
    private int teamAPrediction;
    private int teamBPrediction;
    private Integer points;

    public PredictionResource() {
        super(true, null);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MatchResource getMatch() {
        return match;
    }

    public void setMatch(MatchResource match) {
        this.match = match;
    }

    public int getTeamAPrediction() {
        return teamAPrediction;
    }

    public void setTeamAPrediction(int teamAPrediction) {
        this.teamAPrediction = teamAPrediction;
    }

    public int getTeamBPrediction() {
        return teamBPrediction;
    }

    public void setTeamBPrediction(int teamBPrediction) {
        this.teamBPrediction = teamBPrediction;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
