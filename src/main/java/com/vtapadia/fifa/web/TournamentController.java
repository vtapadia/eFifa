package com.vtapadia.fifa.web;

import com.vtapadia.fifa.service.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/tournament")
public class TournamentController {
    Logger log = LoggerFactory.getLogger(TournamentController.class);

    @Autowired
    TournamentService tournamentService;

    @RequestMapping(method = RequestMethod.GET, value = "/name")
    @ResponseBody
    public String getCurrentTournament() {
        return tournamentService.getCurrentTournament();
    }
}
