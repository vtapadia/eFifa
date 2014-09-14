package com.vtapadia.fifa.resource;

import com.vtapadia.fifa.domain.Subscription;

public class UserResource extends BasicResponse{
    private long id;
    private String name;
    private String user_id;
    private String password;
    private String email;
    private long points;
    private int rank;
    private TeamResource teamWinner;
    private TeamResource teamRunner;
    private Integer goals;
    private Subscription subscription;
    private long globalTeamPoints;
    private long globalGoalPoints;

    public UserResource() {
        super(true, null);
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public TeamResource getTeamWinner() {
        return teamWinner;
    }

    public void setTeamWinner(TeamResource teamWinner) {
        this.teamWinner = teamWinner;
    }

    public TeamResource getTeamRunner() {
        return teamRunner;
    }

    public void setTeamRunner(TeamResource teamRunner) {
        this.teamRunner = teamRunner;
    }

    public Integer getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public long getGlobalTeamPoints() {
        return globalTeamPoints;
    }

    public void setGlobalTeamPoints(long globalTeamPoints) {
        this.globalTeamPoints = globalTeamPoints;
    }

    public long getGlobalGoalPoints() {
        return globalGoalPoints;
    }

    public void setGlobalGoalPoints(long globalGoalPoints) {
        this.globalGoalPoints = globalGoalPoints;
    }
}
