package com.vtapadia.fifa.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="ef_match")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tournament", nullable = true)
    private Tournament tournament;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_a", nullable = true)
    private Team teamA;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_b", nullable = true)
    private Team teamB;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_winner", nullable = true)
    private Team teamWinner;

    @Column(name = "team_a_score", nullable = true)
    private Integer teamAScore;
    @Column(name = "team_b_score", nullable = true)
    private Integer teamBScore;
    @Column(name = "team_a_penalty", nullable = true)
    private Integer teamAPenalty;
    @Column(name = "team_b_penalty", nullable = true)
    private Integer teamBPenalty;
    @Column(name = "match_date")
    private Date matchDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_type")
    private MatchType matchType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
    }

    public Team getTeamWinner() {
        return teamWinner;
    }

    public void setTeamWinner(Team teamWinner) {
        this.teamWinner = teamWinner;
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

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }
}
