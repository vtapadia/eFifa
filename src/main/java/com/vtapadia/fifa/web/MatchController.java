package com.vtapadia.fifa.web;

import com.vtapadia.fifa.resource.MatchResource;
import com.vtapadia.fifa.service.MatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/match")
public class MatchController {
    Logger log = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MatchService matchService;

    @RequestMapping(value="/frozen")
    @ResponseBody
    public List<MatchResource> getFrozenMatches() {
        log.debug("get Frozen matches called");
        return matchService.getFrozenMatches();
    }

    @RequestMapping(value="/secondstage")
    @ResponseBody
    public List<MatchResource> getSecondStageMatches() {
        log.debug("get Second Stag matches called");
        return matchService.getSecondStageMatches();
    }
}
