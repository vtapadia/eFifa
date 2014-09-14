package com.vtapadia.fifa.service;

import com.vtapadia.fifa.dao.MatchDAO;
import com.vtapadia.fifa.dao.UserDAO;
import com.vtapadia.fifa.domain.Match;
import com.vtapadia.fifa.domain.Prediction;
import com.vtapadia.fifa.dao.PredictionDAO;
import com.vtapadia.fifa.domain.Subscription;
import com.vtapadia.fifa.domain.User;
import com.vtapadia.fifa.resource.MatchStatResource;
import com.vtapadia.fifa.resource.PredictionResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PredictionService {
    Logger log = LoggerFactory.getLogger(PredictionService.class);

    @Autowired
    private PredictionDAO predictionDAO;

    @Autowired
    private MatchDAO matchDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private MatchService matchService;

    public List<PredictionResource> getEditablePredictions(User user) {
        List<Prediction> predictions = predictionDAO.forUpdateForUser(user);
        return convert(predictions, false);
    }

    public List<PredictionResource> getUnEditablePredictions(User user) {
        List<Prediction> predictions = predictionDAO.forViewForUser(user);
        return convert(predictions, false);
    }

    public List<MatchStatResource> getLatestMatchStats() {
        List<Match> matches = matchDAO.getPendingMatches();
        int count=0;
        List<MatchStatResource> matchStatResources = new ArrayList<>();
        for (int i=matches.size(); i>0 && matchStatResources.size()<4;i--) {
            MatchStatResource msr = matchService.convertMatchToStatResource(matches.get(i-1));
            for (Prediction prediction : predictionDAO.getForMatch(matches.get(i-1))) {
                if (prediction.isDraw()) msr.addDraw();
                if (prediction.isTeamAWin()) msr.addTeamAWin();
                if (prediction.isTeamBWin()) msr.addTeamBWin();
            }
            matchStatResources.add(msr);
        }
        return matchStatResources;
    }

    public boolean setDefaultPredictionForFutureMatches(User user) {
        List<Match> matches = matchDAO.getPendingMatches();
        if (matches == null || matches.size() == 0) {
            log.warn("No matches found. Probably too late");
            return false;
        }
        for (Match match : matches) {
            predictionDAO.update(user, match, 0, 0);
        }
        return true;
    }

    public boolean setDefaultPredictionForFutureMatches(String userId) {
        List<Match> matches = matchDAO.getPendingMatches();
        if (matches == null || matches.size() == 0) {
            log.warn("No matches found. Probably too late");
            return false;
        }
        User user = userDAO.getUser(userId);
        for (Match match : matches) {
            predictionDAO.update(user, match, 0, 0);
        }
        if (user.getSubscription() == Subscription.BASIC) {
            log.info("setting user to Full subscription " + userId);
            user.setSubscription(Subscription.FULL);
            userDAO.save(user);
        }
        return true;
    }

    public boolean setDefaultPredictionForFutureMatches() {
        List<Match> matches = matchDAO.getPendingMatches();
        List<User> users = userDAO.getOrderedUsers();
        for (Match match: matches) {
            for (User user: users) {
                predictionDAO.createSafe(user, match);
            }
        }
        return true;
    }

    private List<PredictionResource> convert(List<Prediction> predictions, boolean skipMatch) {
        List<PredictionResource> predictionResources = new ArrayList<>();
        for(Prediction prediction: predictions) {
            PredictionResource pr = convert(prediction, skipMatch);
            predictionResources.add(pr);
        }
        return predictionResources;
    }

    private PredictionResource convert(Prediction p, boolean skipMatch) {
        PredictionResource pr = new PredictionResource();
        pr.setTeamAPrediction(p.getTeamAScore());
        pr.setTeamBPrediction(p.getTeamBScore());
        pr.setId(p.getId());
        pr.setPoints(p.getPoints());
        if (!skipMatch) {
            pr.setMatch(matchService.convertToResource(p.getMatch()));
        }
        return pr;
    }

    public boolean updatePredictions(User user, PredictionResource predictionResource) {
        Prediction prediction = predictionDAO.getById(predictionResource.getId());
        if (prediction.getUser() != user) {
            log.warn("Invalid user trying to update. Something fishy here." + user);
            return false;
        }
        if (prediction.getTeamAScore() != predictionResource.getTeamAPrediction() ||
                prediction.getTeamBScore() != predictionResource.getTeamBPrediction()) {
            prediction.setTeamAScore(predictionResource.getTeamAPrediction());
            prediction.setTeamBScore(predictionResource.getTeamBPrediction());
            predictionDAO.save(prediction);
        }
        return true;
    }
}
