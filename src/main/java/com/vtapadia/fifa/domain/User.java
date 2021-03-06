package com.vtapadia.fifa.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "ef_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    @Column(name = "user_id")
    private String user_id;
    private String password;
    private String email;
    private long points;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Subscription subscription;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "team_winner", nullable = true)
    private Team teamWinner;
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "team_runner", nullable = true)
    private Team teamRunner;
    @Column(name = "goals", nullable = true)
    private Integer goals;

    @Column(name = "joining_date")
    private Date joiningDate;
    @Column(name = "last_updated")
    private Date lastUpdated;

    @Column(name = "global_team_points")
    private long globalTeamPoints;

    @Column(name = "global_goal_points")
    private long globalGoalPoints;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ef_user_league",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "league_id")
    )
    public Set<League> leagues;

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

    public void addPoints(long points) {
        this.points += points;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Team getTeamWinner() {
        return teamWinner;
    }

    public void setTeamWinner(Team teamWinner) {
        this.teamWinner = teamWinner;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Team getTeamRunner() {
        return teamRunner;
    }

    public void setTeamRunner(Team teamRunner) {
        this.teamRunner = teamRunner;
    }

    public Integer getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
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

    public Set<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(Set<League> leagues) {
        this.leagues = leagues;
    }
}
