package com.vtapadia.fifa.web;

import com.vtapadia.fifa.domain.Prediction;
import com.vtapadia.fifa.dao.PredictionDAO;
import com.vtapadia.fifa.domain.User;
import com.vtapadia.fifa.dao.UserDAO;
import com.vtapadia.fifa.resource.BasicResponse;
import com.vtapadia.fifa.resource.MatchStatResource;
import com.vtapadia.fifa.resource.PredictionResource;
import com.vtapadia.fifa.service.MatchService;
import com.vtapadia.fifa.service.PredictionService;
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
@RequestMapping("/predictions")
public class PredictionController {
    Logger log = LoggerFactory.getLogger(PredictionController.class);

    @Autowired
    PredictionDAO predictionDAO;

    @Autowired
    PredictionService predictionService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    MatchService matchService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Prediction> getAllForUser() {
        User user = userDAO.getLoggedInUser();
        return predictionDAO.getForUser(user);
    }

    @RequestMapping(value = "/forupdate", method = RequestMethod.GET)
    @ResponseBody
    public List<PredictionResource> getForUpdate() {
        User user = userDAO.getLoggedInUser();
        log.info(user.getUser_id() + " viewed prediction board");
        return predictionService.getEditablePredictions(user);
    }

    @RequestMapping(value = "/forview", method = RequestMethod.GET)
    @ResponseBody
    public List<PredictionResource> getForView() {
        User user = userDAO.getLoggedInUser();
        return predictionService.getUnEditablePredictions(user);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public BasicResponse doUpdate(@RequestBody PredictionResource predictionResource) {
        User user = userDAO.getLoggedInUser();
        if (!matchService.canPredictForMatch(predictionResource.getMatch())) {
            return new BasicResponse(false, "Match Deadline over. Sorry.. !!");
        }
        if (predictionService.updatePredictions(user, predictionResource)) {
            log.info(user.getUser_id() + " updated for match " + predictionResource.getMatch().getId() + ". A " + predictionResource.getTeamAPrediction() + ":" + predictionResource.getTeamBPrediction() + " B");
            return new BasicResponse(true,"Prediction between "+predictionResource.getMatch().getTeamAName() + " and " + predictionResource.getMatch().getTeamBName() + " updated.");
        }
        return new BasicResponse(false, "Unable to update, contact Varesh Tapadia(varesh.tapadia@gmail.com).");
    }

    @RequestMapping(value = "/update/all", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public BasicResponse doUpdateAll(@RequestBody List<PredictionResource> predictionResources) {
        User user = userDAO.getLoggedInUser();
        List<PredictionResource> current = predictionService.getEditablePredictions(user);
        int count = 0, missed = 0;
        for (PredictionResource predictionResource : predictionResources) {
            if (isUpdated(predictionResource, current)) {
                if (!matchService.canPredictForMatch(predictionResource.getMatch())) {
                    missed++;
                } else {
                    if (predictionService.updatePredictions(user, predictionResource)) {
                        log.info(user.getUser_id() + " updated for match " + predictionResource.getMatch().getId() + ". A " + predictionResource.getTeamAPrediction() + ":" + predictionResource.getTeamBPrediction() + " B");
                        count++;
                    } else {
                        missed++;
                    }
                }
            }
        }
        if (missed>0) {
            return new BasicResponse(false,"Update to " + count + " matches done. However "+ missed+ " were not updated. Kindly refresh.");
        }
        return new BasicResponse(true,"Update to " + count + " matches predictions successfully done.");
    }

    @RequestMapping(value = "/stats", method = RequestMethod.GET)
    @ResponseBody
    public List<MatchStatResource> getPredictionStats() {
        return predictionService.getLatestMatchStats();
    }

    private boolean isUpdated(PredictionResource changed, List<PredictionResource> current) {
        for (PredictionResource pr : current) {
            if (pr.getId() == changed.getId()) {
                if (pr.getTeamAPrediction() != changed.getTeamAPrediction() ||
                    pr.getTeamBPrediction() != changed.getTeamBPrediction()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}
