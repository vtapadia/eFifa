package com.vtapadia.fifa.web;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.vtapadia.fifa.dao.UserDAO;
import com.vtapadia.fifa.domain.League;
import com.vtapadia.fifa.domain.User;
import com.vtapadia.fifa.resource.ProgressResource;
import com.vtapadia.fifa.resource.UserResource;
import com.vtapadia.fifa.service.MatchService;
import com.vtapadia.fifa.service.PredictionService;
import com.vtapadia.fifa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/leader")
public class LeaderController {
    Logger log = LoggerFactory.getLogger(LeaderController.class);

    @Autowired
    UserService userService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    PredictionService predictionService;

    @Autowired
    MatchService matchService;

    @RequestMapping(value="/leaders")
    @ResponseBody
    public List<UserResource> getTopUsers(
            @RequestParam(value = "filterSilent", required = false, defaultValue = "false") boolean filterSilent,
            @RequestParam(value = "leagueId", required = false) Long leagueId) {
        User user = userDAO.getLoggedInUser();
        if (leagueId == null) {
            for (League league : user.getLeagues()) {
                leagueId = league.id;
            }
        }
        List<UserResource> leaders = userService.getLeaders(leagueId);
        if (filterSilent) {
            List<UserResource> filteredLeaders =
                    FluentIterable.from(leaders).filter(new Predicate<UserResource>() {
                        @Override
                        public boolean apply(UserResource userResource) {
                            return userResource.getTeamWinner() != null;
                        }
                    }).toList();
            return filteredLeaders;
        } else {
            log.info(userDAO.getLoggedInUser().getUser_id() + " viewed leader board");
        }
        return leaders;
    }

    @RequestMapping(value="/progress")
    @ResponseBody
    public ProgressResource getUsersProgress() {
        return userService.getUsersProgress();
    }


}
