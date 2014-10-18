package com.vtapadia.fifa.service;

import com.vtapadia.fifa.dao.LeagueDAO;
import com.vtapadia.fifa.dao.TournamentDAO;
import com.vtapadia.fifa.dao.UserDAO;
import com.vtapadia.fifa.domain.League;
import com.vtapadia.fifa.domain.User;
import com.vtapadia.fifa.resource.LeagueResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeagueService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    LeagueDAO leagueDAO;

    @Autowired
    TournamentDAO tournamentDAO;

    @Autowired
    UserService userService;

    public boolean registerLeague(LeagueResource leagueResource) {
        League league = new League();
        league.name = leagueResource.name;
        league.baseAmount = leagueResource.baseAmount;
        league.tournament = tournamentDAO.getTournament(leagueResource.tournamentName);
        league.leagueOwner = userDAO.getUser(
                leagueResource.leagueOwner.getUser_id());
        return leagueDAO.save(league);
    }

    public List<LeagueResource> getLeagueForLoggedUser() {
        User user = userDAO.getLoggedInUser();
        List<League> leagues = leagueDAO.getLeaguesForOwnerUser(user);
        List<LeagueResource> leagueResourceList = new ArrayList<>();
        for (League league : leagues) {
            leagueResourceList.add(convert(league,false));
        }
        return leagueResourceList;
    }

    public LeagueResource convert(League league, boolean convertUser) {
        LeagueResource leagueResource = new LeagueResource();
        leagueResource.id=league.id;
        leagueResource.name=league.name;
        leagueResource.baseAmount=league.baseAmount;
        if (convertUser) {
            leagueResource.leagueOwner=userService.convert(
                    league.leagueOwner, false);
        }
        return leagueResource;
    }
}
