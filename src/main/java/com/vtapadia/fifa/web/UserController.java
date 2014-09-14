package com.vtapadia.fifa.web;

import com.vtapadia.fifa.dao.MatchDAO;
import com.vtapadia.fifa.dao.PredictionDAO;
import com.vtapadia.fifa.dao.UserDAO;
import com.vtapadia.fifa.resource.BasicResponse;
import com.vtapadia.fifa.resource.GlobalPredictionResource;
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
@RequestMapping("/user")
public class UserController {
    Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    PredictionDAO predictionDAO;

    @Autowired
    PredictionService predictionService;

    @Autowired
    UserService userService;

    @Autowired
    MatchService matchService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    MatchDAO matchDAO;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    @ResponseBody
    public UserResource getUserDetails() {
        return userService.getLoggedInUser();
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public BasicResponse changePassword(@RequestBody String newPassword) {
        log.info("User requested change of password");
        if (userService.changePassword(newPassword)) {
            return new BasicResponse(true,"Password Changed Successfully");
        }
        return new BasicResponse(false,"Password Changed failed. Contact Varesh Tapadia");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public BasicResponse update(@RequestBody UserResource userResource) {
        log.info("User requested update");
        if (matchService.isSecondStageStarted()) {
            return new BasicResponse(false,"Failed. Time is finished.");
        }
        if (userService.update(userResource)) {
            return new BasicResponse(true,"User Predictions Updated Successfully");
        }
        return new BasicResponse(false,"Failed. Contact Varesh Tapadia");
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseBody
    public int userCount() {
        log.info("User count called");
        return userService.getRegisteredUserCount();
    }


    @RequestMapping(value = "/globalpredictions", method = RequestMethod.GET)
    @ResponseBody
    public GlobalPredictionResource getGlobalPredictionResource() {
        return userService.getGlobalPredictionResource();
    }
}
