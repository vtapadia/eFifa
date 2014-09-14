package com.vtapadia.fifa.resource;

public class MatchStatResource extends MatchResource {
    private int teamAWin = 0;
    private int teamBWin = 0;
    private int draw = 0;
    private String teamAColor = "green";
    private String teamBColor = "gold";

    public int getTeamAWin() {
        return teamAWin;
    }

    public void setTeamAWin(int teamAWin) {
        this.teamAWin = teamAWin;
    }

    public int getTeamBWin() {
        return teamBWin;
    }

    public void setTeamBWin(int teamBWin) {
        this.teamBWin = teamBWin;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public void addDraw() {
        this.draw++;
    }

    public void addTeamAWin() {
        this.teamAWin++;
    }

    public void addTeamBWin() {
        this.teamBWin++;
    }
}
