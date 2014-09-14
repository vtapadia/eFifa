package com.vtapadia.fifa.service;

import com.vtapadia.fifa.dao.MatchDAO;
import com.vtapadia.fifa.dao.TeamDAO;
import com.vtapadia.fifa.domain.Match;
import com.vtapadia.fifa.domain.Team;
import com.vtapadia.fifa.resource.MatchResource;
import com.vtapadia.fifa.resource.MatchStatResource;
import com.vtapadia.fifa.resource.TeamResource;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTimeComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MatchService {
    Logger log = LoggerFactory.getLogger(MatchService.class);

    private static final Map<Long, String> SECOND_STAGE_TEAM_A = new HashMap<>();
    private static final Map<Long, String> SECOND_STAGE_TEAM_B = new HashMap<>();
    static {
        SECOND_STAGE_TEAM_A.put(149l, "1A");
        SECOND_STAGE_TEAM_A.put(150l, "1C");
        SECOND_STAGE_TEAM_A.put(151l, "1B");
        SECOND_STAGE_TEAM_A.put(152l, "1D");
        SECOND_STAGE_TEAM_A.put(153l, "1E");
        SECOND_STAGE_TEAM_A.put(154l, "1G");
        SECOND_STAGE_TEAM_A.put(155l, "1F");
        SECOND_STAGE_TEAM_A.put(156l, "1H");
        SECOND_STAGE_TEAM_A.put(157l, "W49");
        SECOND_STAGE_TEAM_A.put(158l, "W53");
        SECOND_STAGE_TEAM_A.put(159l, "W51");
        SECOND_STAGE_TEAM_A.put(160l, "W55");
        SECOND_STAGE_TEAM_A.put(161l, "W57");
        SECOND_STAGE_TEAM_A.put(162l, "W59");
        SECOND_STAGE_TEAM_A.put(163l, "L61");
        SECOND_STAGE_TEAM_A.put(164l, "W61");

        SECOND_STAGE_TEAM_B.put(149l, "2B");
        SECOND_STAGE_TEAM_B.put(150l, "2D");
        SECOND_STAGE_TEAM_B.put(151l, "2A");
        SECOND_STAGE_TEAM_B.put(152l, "2C");
        SECOND_STAGE_TEAM_B.put(153l, "2F");
        SECOND_STAGE_TEAM_B.put(154l, "2H");
        SECOND_STAGE_TEAM_B.put(155l, "2E");
        SECOND_STAGE_TEAM_B.put(156l, "2G");
        SECOND_STAGE_TEAM_B.put(157l, "W50");
        SECOND_STAGE_TEAM_B.put(158l, "W54");
        SECOND_STAGE_TEAM_B.put(159l, "W52");
        SECOND_STAGE_TEAM_B.put(160l, "W56");
        SECOND_STAGE_TEAM_B.put(161l, "W58");
        SECOND_STAGE_TEAM_B.put(162l, "W60");
        SECOND_STAGE_TEAM_B.put(163l, "L62");
        SECOND_STAGE_TEAM_B.put(164l, "W62");
    }

    @Autowired
    private MatchDAO matchDAO;

    @Autowired
    private TeamDAO teamDAO;

    public List<TeamResource> getTeams() {
        return convertToTeamResource(teamDAO.getAllTeams());
    }

    public boolean isSecondStageStarted() {
        List<Match> matches = matchDAO.getSecondStageMatches();
        if (matches.get(0).getMatchDate().before(new Date())) {
            return true;
        }
        return false;
    }

    public boolean canPredictForMatch(MatchResource matchResource) {
        Match match = matchDAO.getMatch(matchResource.getId());
        if (match.getMatchDate().getTime() > System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    public List<MatchResource> getFrozenMatches() {
        List<Match> matches = matchDAO.getAllFrozenMatches();
        return convertToResource(matches);
    }

    public List<MatchResource> getSecondStageMatches() {
        List<Match> matches = matchDAO.getSecondStageMatches();
        return convertToResource(matches);
    }

    public boolean finalizeMatch(MatchResource matchResource) {
        if (!matchResource.isFinalized()) {
            Match match = matchDAO.getMatch(matchResource.getId());
            match.setTeamAScore(matchResource.getTeamAScore());
            match.setTeamBScore(matchResource.getTeamBScore());
            matchDAO.finalizeMatch(match);
            matchResource.setFinalized(true);
        }
        return true;
    }

    public List<MatchResource> convertToResource(List<Match> matches) {
        List<MatchResource> matchResourceList = new ArrayList<>();
        for (Match match : matches) {
            matchResourceList.add(convertToResource(match));
        }
        return matchResourceList;
    }

    public List<MatchResource> convertForProgressResource(List<Match> matches, boolean shouldGroupByDate) {
        List<MatchResource> matchResourceList = new ArrayList<>();
        Date matchDate;
        DateTimeComparator dtc = DateTimeComparator.getDateOnlyInstance();
        MatchResource mr = null;
        for (Match match : matches) {
            if (shouldGroupByDate) {
                matchDate = new Date(match.getMatchDate().getTime() -3600*1000);
                if (mr == null || dtc.compare(mr.getMatchDate(), matchDate) != 0) {
                    mr = new MatchResource();
                    mr.setShortName(DateFormatUtils.format(matchDate, "dd/MMM"));
                    mr.setMatchDate(matchDate);
                    matchResourceList.add(mr);
                }
            } else {
                matchResourceList.add(convertToResource(match));
            }
        }
        return matchResourceList;
    }


    public List<TeamResource> convertToTeamResource(List<Team> teams) {
        List<TeamResource> teamResourceList= new ArrayList<>();
        for (Team team: teams) {
            teamResourceList.add(convertToResource(team));
        }
        return teamResourceList;
    }

    public TeamResource convertToResource(Team team) {
        TeamResource tr = new TeamResource();
        tr.setId(team.getId());
        tr.setName(team.getName());
        tr.setImage("flagsp_" + team.getImage());
        tr.setGroup(team.getGroup());
        tr.setGf(team.getGf());
        tr.setGa(team.getGa());
        tr.setPoints(team.getPoints());
        return tr;
    }

    public MatchResource convertToResource(Match match) {
        MatchResource mr = new MatchResource();
        mr.setId(match.getId());
        if (match.getTeamA() != null) {
            mr.setTeamAName(match.getTeamA().getName());
            mr.setTeamAImage("flagsp_" + match.getTeamA().getImage());
        } else {
            mr.setTeamAName(SECOND_STAGE_TEAM_A.get(match.getId()));
        }
        mr.setTeamAScore(match.getTeamAScore());
        mr.setTeamAPenalty(match.getTeamAPenalty());
        if (match.getTeamB() != null) {
            mr.setTeamBName(match.getTeamB().getName());
            mr.setTeamBImage("flagsp_" + match.getTeamB().getImage());
        } else {
            mr.setTeamBName(SECOND_STAGE_TEAM_B.get(match.getId()));
        }
        if (match.getTeamA() != null && match.getTeamB() != null) {
            mr.setShortName(match.getTeamA().getImage().toUpperCase() + " vs " +
                    match.getTeamB().getImage().toUpperCase());
        }
        mr.setTeamBScore(match.getTeamBScore());
        mr.setTeamBPenalty(match.getTeamBPenalty());
        mr.setMatchDate(match.getMatchDate());
        mr.setMatchType(match.getMatchType());
        if (mr.getTeamAScore() != null && mr.getTeamBScore() != null) {
            mr.setFinalized(true);
        }
        return  mr;
    }

    public MatchResource convertToStatResource(MatchResource matchResource) {
        MatchStatResource msr = new MatchStatResource();
        msr.setId(matchResource.getId());
        msr.setShortName(matchResource.getShortName());
        msr.setTeamAName(matchResource.getTeamAName());
        msr.setTeamAImage(matchResource.getTeamAImage());
        msr.setTeamAScore(matchResource.getTeamAScore());
        msr.setTeamBName(matchResource.getTeamBName());
        msr.setTeamBImage(matchResource.getTeamBImage());
        msr.setTeamBScore(matchResource.getTeamBScore());
        msr.setMatchDate(matchResource.getMatchDate());
        msr.setMatchType(matchResource.getMatchType());
        if (msr.getTeamAScore() != null && msr.getTeamBScore() != null) {
            msr.setFinalized(true);
        }
        return  msr;
    }

    public MatchStatResource convertMatchToStatResource(Match match) {
        MatchStatResource mr = new MatchStatResource();
        mr.setId(match.getId());
        mr.setShortName(match.getTeamA().getImage().toUpperCase() + " vs " +
                match.getTeamB().getImage().toUpperCase());
        mr.setTeamAName(match.getTeamA().getName());
        mr.setTeamAImage("flagsp_" + match.getTeamA().getImage());
        mr.setTeamAScore(match.getTeamAScore());
        mr.setTeamBName(match.getTeamB().getName());
        mr.setTeamBImage("flagsp_" + match.getTeamB().getImage());
        mr.setTeamBScore(match.getTeamBScore());
        mr.setMatchDate(match.getMatchDate());
        mr.setMatchType(match.getMatchType());
        if (mr.getTeamAScore() != null && mr.getTeamBScore() != null) {
            mr.setFinalized(true);
        }
        return  mr;
    }

    public boolean isTournamentEnded() {
        List<Match> matches = matchDAO.getAllNotFinalized();
        return (matches == null || matches.size()==0);
    }

    private static final Map<String, String> TEAM_COLOR = new HashMap<>();
    static {
        TEAM_COLOR.put("arg","powderblue");
        TEAM_COLOR.put("por","red");
        TEAM_COLOR.put("gha","beige");
        TEAM_COLOR.put("usa","crimson");
        TEAM_COLOR.put("ger","color");
        TEAM_COLOR.put("kor","color");
        TEAM_COLOR.put("bel","color");
        TEAM_COLOR.put("alg","color");
        TEAM_COLOR.put("rus","color");
        TEAM_COLOR.put("team","color");
        TEAM_COLOR.put("team","color");
        TEAM_COLOR.put("team","color");
        TEAM_COLOR.put("team","color");
        TEAM_COLOR.put("team","color");
        TEAM_COLOR.put("team","color");
        TEAM_COLOR.put("team","color");
    }
}
