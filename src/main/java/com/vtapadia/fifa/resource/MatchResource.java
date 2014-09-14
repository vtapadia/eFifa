package com.vtapadia.fifa.resource;

import java.util.Date;

public class MatchResource extends BasicResponse {
    private long id;
    private String shortName;
    private String teamAName;
    private String teamBName;
    private String teamAImage;
    private String teamBImage;
    private Integer teamAScore;
    private Integer teamBScore;
    private Integer teamAPenalty;
    private Integer teamBPenalty;
    private Date matchDate;
    private String matchType;
    private boolean finalized;

    public MatchResource() {
        super(true, null);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getTeamAName() {
        return teamAName;
    }

    public void setTeamAName(String teamAName) {
        this.teamAName = teamAName;
    }

    public String getTeamBName() {
        return teamBName;
    }

    public void setTeamBName(String teamBName) {
        this.teamBName = teamBName;
    }

    public String getTeamAImage() {
        return teamAImage;
    }

    public void setTeamAImage(String teamAImage) {
        this.teamAImage = teamAImage;
    }

    public String getTeamBImage() {
        return teamBImage;
    }

    public void setTeamBImage(String teamBImage) {
        this.teamBImage = teamBImage;
    }

    public Integer getTeamAScore() {
        return teamAScore;
    }

    public void setTeamAScore(Integer teamAScore) {
        this.teamAScore = teamAScore;
    }

    public Integer getTeamBScore() {
        return teamBScore;
    }

    public void setTeamBScore(Integer teamBScore) {
        this.teamBScore = teamBScore;
    }

    public Integer getTeamAPenalty() {
        return teamAPenalty;
    }

    public void setTeamAPenalty(Integer teamAPenalty) {
        this.teamAPenalty = teamAPenalty;
    }

    public Integer getTeamBPenalty() {
        return teamBPenalty;
    }

    public void setTeamBPenalty(Integer teamBPenalty) {
        this.teamBPenalty = teamBPenalty;
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }
}
