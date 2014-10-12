package com.vtapadia.fifa.web;

import com.vtapadia.fifa.resource.UserResource;
import com.vtapadia.fifa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("/league")
public class LeagueController {
    Logger log = LoggerFactory.getLogger(LeagueController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value="/validateuser", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public UserResource validateUser(@RequestBody String userId) {
        log.info("LeagueController requesting search for user " + userId);
        return userService.getUser(userId);
    }

    @RequestMapping(value="/registeruser", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public UserResource registerUser(@RequestBody UserResource userResource) {
        log.info("LeagueController registering for user " + userResource.getUser_id());
        //TODO Yet to implmenet
        return null;
    }

}
