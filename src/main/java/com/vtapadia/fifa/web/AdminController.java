package com.vtapadia.fifa.web;

import com.vtapadia.fifa.dao.MatchDAO;
import com.vtapadia.fifa.dao.PredictionDAO;
import com.vtapadia.fifa.domain.Subscription;
import com.vtapadia.fifa.dao.UserDAO;
import com.vtapadia.fifa.resource.BasicResponse;
import com.vtapadia.fifa.resource.MatchResource;
import com.vtapadia.fifa.resource.UserResource;
import com.vtapadia.fifa.service.MatchService;
import com.vtapadia.fifa.service.PredictionService;
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

@Controller
@RequestMapping("/admin")
public class AdminController {
    Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    PredictionDAO predictionDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserService userService;

    @Autowired
    PredictionService predictionService;

    @Autowired
    MatchService matchService;

    @Autowired
    MatchDAO matchDAO;

    @RequestMapping(value="/register", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public BasicResponse registerUser(@RequestBody UserResource userResource) {
        log.info("Registering user " + userResource.getName() + " id:" + userResource.getUser_id());
        if (userService.createUser(userResource)) {
            if (userResource.getSubscription() == Subscription.FULL) {
                return new BasicResponse(true,"User created with FULL profile.");
            } else {
                return new BasicResponse(true,"User created with BASIC profile.");
            }
        }
        return new BasicResponse(false, "Unable to create/update, contact Varesh Tapadia(varesh.tapadia@gmail.com).");
    }

    @RequestMapping(value="/finalize/match", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public BasicResponse finalizeMatch(@RequestBody MatchResource matchResource) {
        if (matchService.finalizeMatch(matchResource)) {
            log.info("Match finalized successfuly. " + matchResource.getId());
        } else {
            log.error("Something failed when finalizing match");
        }
        return matchResource;
    }

    @RequestMapping(value = "/set/password", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public BasicResponse setPassword(@RequestBody String userId) {
        log.info("Admin resetting password for user " + userId);
        if (userService.changePassword(userId,userId)) {
            return new BasicResponse(true,"Password Changed Successfully");
        }
        return new BasicResponse(false,"Password Changed failed. Contact Varesh Tapadia");
    }

    @RequestMapping(value = "/set/prediction", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public BasicResponse setPrediction(@RequestBody String userId) {
        log.info("Setting default predictions for user " + userId);
        if (predictionService.setDefaultPredictionForFutureMatches(userId)) {
            return new BasicResponse(true,"Create/Update done");
        } else {
            return new BasicResponse(false,"User created, but default predictions missing.");
        }
    }

    @RequestMapping(value = "/set/futureprediction", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public BasicResponse setPrediction() {
        log.info("Setting default predictions for future matches ");
        if (predictionService.setDefaultPredictionForFutureMatches()) {
            return new BasicResponse(true,"All set to go");
        } else {
            return new BasicResponse(false,"Refer logs, something wrong.");
        }
    }

    @RequestMapping(value = "/finalize/tournament", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public BasicResponse finalizeTournament() {
        log.info("Finalizing tournament");
        if (userService.finalizeTournament()) {
            return new BasicResponse(true,"Done");
        } else {
            return new BasicResponse(false,"Failed, please check logs.");
        }
    }

}
