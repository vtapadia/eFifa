package com.vtapadia.fifa.web;

import com.vtapadia.fifa.resource.BasicResponse;
import com.vtapadia.fifa.resource.TeamResource;
import com.vtapadia.fifa.resource.UserResource;
import com.vtapadia.fifa.service.MatchService;
import com.vtapadia.fifa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/main")
public class MainController {
    Logger log = LoggerFactory.getLogger(MainController.class);

    @Autowired
    UserService userService;

    @Autowired
    MatchService matchService;

    @RequestMapping(value="/leaders")
    @ResponseBody
    public List<UserResource> getTopUsers() {
        return userService.getLeaders();
    }

    @RequestMapping(value="/teams")
    @ResponseBody
    public List<TeamResource> getTeams() {
        return matchService.getTeams();
    }

    @RequestMapping(value="/loggedin")
    @ResponseBody
    public BasicResponse isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !StringUtils.isEmpty(authentication.getName())) {
            return new BasicResponse(true,"User is logged in");
        } else {
            return new BasicResponse(false,"No, user is not logged in.");
        }
    }

    @RequestMapping(value="/started")
    @ResponseBody
    public BasicResponse isStarted() {
        if (matchService.isSecondStageStarted()) {
            return new BasicResponse(true, "Second Stage started");
        }
        return new BasicResponse(false,"Not Yet");
    }

    @RequestMapping(value="/ended")
    @ResponseBody
    public BasicResponse isEnded() {
        if (matchService.isTournamentEnded()) {
            return new BasicResponse(true, "Tournament Finished");
        }
        return new BasicResponse(false,"Not Yet");
    }

    @RequestMapping(value="/admin")
    @ResponseBody
    public BasicResponse isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.getName() + " " + authentication.getAuthorities());
        if (authentication != null && authentication.isAuthenticated() && !StringUtils.isEmpty(authentication.getName())) {
            for (GrantedAuthority authority:authentication.getAuthorities()) {
                if (authority.getAuthority().equals("ADMIN")) {
                    return new BasicResponse(true,"User is admin");
                }
            }
        }
        return new BasicResponse(false,"No, user is not Admin.");
    }
}
