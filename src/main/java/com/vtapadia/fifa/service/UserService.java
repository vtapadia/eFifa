package com.vtapadia.fifa.service;

import com.vtapadia.fifa.dao.MatchDAO;
import com.vtapadia.fifa.dao.PredictionDAO;
import com.vtapadia.fifa.dao.TeamDAO;
import com.vtapadia.fifa.dao.UserDAO;
import com.vtapadia.fifa.domain.Match;
import com.vtapadia.fifa.domain.Prediction;
import com.vtapadia.fifa.domain.Subscription;
import com.vtapadia.fifa.domain.Team;
import com.vtapadia.fifa.domain.User;
import com.vtapadia.fifa.resource.GlobalPredictionResource;
import com.vtapadia.fifa.resource.ProgressResource;
import com.vtapadia.fifa.resource.UserProgressResource;
import com.vtapadia.fifa.resource.UserResource;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.vtapadia.fifa.resource.GlobalPredictionResource.*;

@Service
public class UserService {
    Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDao;

    @Autowired
    private TeamDAO teamDAO;

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchDAO matchDAO;

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private PredictionDAO predictionDAO;

    @Autowired
    private StandardPasswordEncoder passwordEncoder;

    private boolean testMode = false;

    public UserResource getLoggedInUser() {
        User user = userDao.getLoggedInUser();
        return convert(user,true);
    }

    public List<UserResource> getLeaders() {
        List<User> users = userDao.getOrderedUsers();
        return convertForLeaders(users);
    }

    public ProgressResource getUsersProgress() {
        List<UserProgressResource> userProgressResources = new ArrayList<>();
        User loggedInUser = userDao.getLoggedInUser();
        List<User> users = userDao.getOrderedUsers();
        List<Match> matches = matchDAO.getAllFinalizedMatches();
        boolean shouldGroupPerDay = matchService.isSecondStageStarted();
        ProgressResource progressResource = new ProgressResource();
        progressResource.setMatches(matchService.convertForProgressResource(matches, shouldGroupPerDay));
        long points = -1, selected=0;
        for (User user :users) {
            if (points == -1 || points != user.getPoints() + user.getGlobalGoalPoints() + user.getGlobalTeamPoints()) {
                points = user.getPoints() + user.getGlobalGoalPoints() + user.getGlobalTeamPoints();
                selected++;
            }
            if (user.getUser_id().equalsIgnoreCase(loggedInUser.getUser_id())
                    || selected<4) {
                List<Prediction> predictions = predictionDAO.getForUserMatches(user, matches);
                UserProgressResource userProgressResource = new UserProgressResource();
                userProgressResource.setPoints(user.getPoints() + user.getGlobalGoalPoints() + user.getGlobalTeamPoints());
                userProgressResource.setUserId(user.getUser_id());
                if (user.getName().indexOf(' ')>0) {
                    userProgressResource.setUserName(user.getName().substring(0, user.getName().indexOf(' ')));
                } else {
                    userProgressResource.setUserName(user.getName());
                }
                setProgression(userProgressResource, predictions, shouldGroupPerDay);
                if (shouldGroupPerDay) {
                    int lastIndex = userProgressResource.getProgression().size() -1;
                    int finalPoints = userProgressResource.getProgression().set(lastIndex,
                            userProgressResource.getProgression().get(lastIndex) +
                                    (int)(user.getGlobalGoalPoints() + user.getGlobalTeamPoints()));
                }
                userProgressResources.add(userProgressResource);
            }
        }
        progressResource.setUserProgress(userProgressResources);
        return progressResource;
    }

    public GlobalPredictionResource getGlobalPredictionResource() {
        List<User> users = userDao.getOrderedUsers();
        GlobalPredictionResource gpr = new GlobalPredictionResource();
        Map<String, Integer> teamMap = gpr.getTeams();
        Map<String, Integer> goalsMap = gpr.getGoals();
        for (User user :users) {
            Team teamWinner = user.getTeamWinner(), teamRunner = user.getTeamRunner();
            if (teamWinner != null) {
                if (teamMap.containsKey(teamWinner.getName())) {
                    teamMap.put(teamWinner.getName(), teamMap.get(teamWinner.getName()) + 1);
                } else {
                    teamMap.put(teamWinner.getName(), 1);
                }
            }
            if (teamRunner != null) {
                if (teamMap.containsKey(teamRunner.getName())) {
                    teamMap.put(teamRunner.getName(), teamMap.get(teamRunner.getName()) + 1);
                } else {
                    teamMap.put(teamRunner.getName(), 1);
                }
            }
            if (user.getGoals() != null && user.getGoals() > 0) {
                switch (user.getGoals()/10) {
                    case -1: case 0:case 1:case 2:case 3:case 4:case 5:case 6:
                    case 7:case 8:case 9:case 10:case 11:
                        goalsMap.put(GOALS_LESS_THAN_120, goalsMap.get(GOALS_LESS_THAN_120) + 1);
                        break;
                    case 12:
                        goalsMap.put(GOALS_120_to_130, goalsMap.get(GOALS_120_to_130) + 1);
                        break;
                    case 13:
                        goalsMap.put(GOALS_130_to_140, goalsMap.get(GOALS_130_to_140) + 1);
                        break;
                    case 14:
                        goalsMap.put(GOALS_140_to_150, goalsMap.get(GOALS_140_to_150) + 1);
                        break;
                    case 15:
                        goalsMap.put(GOALS_150_to_160, goalsMap.get(GOALS_150_to_160) + 1);
                        break;
                    case 16:
                        goalsMap.put(GOALS_160_to_170, goalsMap.get(GOALS_160_to_170) + 1);
                        break;
                    case 17:
                        goalsMap.put(GOALS_170_to_180, goalsMap.get(GOALS_170_to_180) + 1);
                        break;
                    default:
                        goalsMap.put(GOALS_GREATER_THAN_180, goalsMap.get(GOALS_GREATER_THAN_180) + 1);
                        break;
                }
            }
        }
        List<Match> matches = matchDAO.getAllFinalizedMatches();
        int count = 0;
        for (Match match: matches) {
            count += match.getTeamAScore() + match.getTeamBScore();
        }
        gpr.setCurrentGoalCount(count);
        return gpr;
    }

    private void setProgression(UserProgressResource userProgressResource, List<Prediction> predictions,
                                boolean shouldBeGroupedPerDay) {
        int currentScore = 0;
        LocalDate baseDate = null;
        Date matchDate;
        DateTimeComparator dtc = DateTimeComparator.getDateOnlyInstance();
        if (shouldBeGroupedPerDay) {
            for (Prediction prediction: predictions) {
                matchDate = new Date(prediction.getMatch().getMatchDate().getTime() -3600*1000);
                if (baseDate != null && dtc.compare(matchDate, baseDate.toDate()) == 0) {
                    //Same date
                    currentScore += prediction.getPoints();
                } else {
                    //date changed
                    if (baseDate != null) { //Should not be for the very first match
                        userProgressResource.addProgression(currentScore);
                    }
                    currentScore += prediction.getPoints();
                    baseDate = LocalDate.fromDateFields(matchDate);
                }
            }
            userProgressResource.addProgression(currentScore);
        } else {
            for (Prediction prediction: predictions) {
                currentScore += prediction.getPoints();
                userProgressResource.addProgression(currentScore);
            }
        }
    }

    public boolean createUser(UserResource userResource) {
        User user = new User();
        user.setName(userResource.getName());
        user.setUser_id(userResource.getUser_id());
        user.setPassword(passwordEncoder.encode(userResource.getPassword()));
        user.setEmail(userResource.getEmail());
        user.setSubscription(userResource.getSubscription());
        user.setJoiningDate(new Date());
        user.setPoints(0l);
        userDao.save(user);
        if (user.getSubscription() == Subscription.FULL) {
            predictionService.setDefaultPredictionForFutureMatches(user);
        }
        return true;
    }

    public boolean changePassword(String newPassword) {
        User user = userDao.getLoggedInUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.save(user);
        return true;
    }

    public boolean changePassword(String userId, String newPassword) {
        User user = userDao.getUser(userId);
        if (user == null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.save(user);
        return true;
    }

    public boolean update(UserResource userResource) {
        User user = userDao.getLoggedInUser();
        user.setGoals(userResource.getGoals());
        if (userResource.getTeamWinner() != null && userResource.getTeamWinner().getName().length()!=0) {
            user.setTeamWinner(teamDAO.getTeamByName(userResource.getTeamWinner().getName()));
        } else {
            user.setTeamWinner(null);
        }
        if (userResource.getTeamRunner() != null && userResource.getTeamRunner().getName().length()!=0) {
            user.setTeamRunner(teamDAO.getTeamByName(userResource.getTeamRunner().getName()));
        } else {
            user.setTeamRunner(null);
        }
        userDao.save(user);
        return true;
    }

    public boolean finalizeTournament() {
        List<Match> unfinishedMatches = matchDAO.getAllNotFinalized();
        if (testMode || unfinishedMatches == null || unfinishedMatches.size()==0) {
            List<Match> matches = matchDAO.getAllFinalizedMatches();
            int totalGoals = 0;
            for (Match match: matches) {
                totalGoals += match.getTeamAScore() + match.getTeamBScore();
            }
            Match finalMatch = matches.get(matches.size()-1); // The last match is the Final Match
            Team winner = getTeamWinner(finalMatch);
            Team loser = getTeamRunner(finalMatch, winner);
            List<User> users = userDao.getOrderedUsers();
            updateForGoals(users, totalGoals);
            log.info("match for finals " + finalMatch.getTeamA().getName() + " between " + finalMatch.getTeamB().getName());
            updateForTeams(users, winner, loser);
            if (!testMode) {
                userDao.save(users);
            }
        } else {
            log.error("matches not yet finalized, Tournament can not be finalized.");
            return false;
        }
        return true;
    }

    private void updateForTeams(List<User> users, Team winner, Team loser) {
        for (User user : users) {
            if (user.getTeamWinner() != null && user.getTeamRunner() != null) {
                int globalPoints = getGlobalTeamPoints(user, winner, loser);
                if (!testMode) {
                    user.setGlobalTeamPoints(globalPoints);
                }
                log.info("User " + user.getName() + " got " + globalPoints + "team points");
            } else {
                //log.info("User " + user.getName() + " has not set global teams. 0 points");
            }
        }
    }

    private int getGlobalTeamPoints(User user, Team winner, Team loser) {
        if (user.getTeamWinner().equals(winner) && user.getTeamRunner().equals(loser)) {
            return 150;
        }
        if (user.getTeamWinner().equals(loser) && user.getTeamRunner().equals(winner)) {
            return 100;
        }
        if (user.getTeamWinner().equals(winner)) {
            return 75;
        }
        if (user.getTeamRunner().equals(loser)) {
            return 60;
        }
        if (user.getTeamWinner().equals(loser) || user.getTeamRunner().equals(winner)) {
            return 50;
        }
        return 0;
    }

    private Team getTeamWinner(Match match) {
        if (match.getTeamAScore() == match.getTeamBScore()) {
            //Tie
            if (match.getTeamAPenalty() > match.getTeamBPenalty()) {
                return match.getTeamA();
            } else {
                return match.getTeamB();
            }
        } else {
            if (match.getTeamAScore() > match.getTeamBScore()) {
                return match.getTeamA();
            } else {
                return  match.getTeamB();
            }
        }
    }
    private Team getTeamRunner(Match match, Team winner) {
        if (match.getTeamA().equals(winner)) {
            return match.getTeamB();
        } else {
            return match.getTeamA();
        }
    }


    private void updateForGoals(List<User> users, int totalGoals) {
        int counter = 0;
        int min = totalGoals, max = totalGoals;
        boolean someOneGotPoints = false;
        do {
            for (User user : users) {
                if (user.getGoals() == null) {
                    continue;
                }
                if (user.getGoals() == min || user.getGoals() == max) {
                    someOneGotPoints = true;
                    if (!testMode) {
                        user.setGlobalGoalPoints(getGoalPoints(counter));
                    }
                    log.info("user " + user.getName() + " getting goals points: " + getGoalPoints(counter));
                }
            }
            if (someOneGotPoints) {
                someOneGotPoints = false;
                counter++;
            }
            max++;min--;//moving one side around
        } while (counter != 2);

    }

    private int getGoalPoints(int counter) {
        if (counter == 0) {
            return 100;
        } else {
            return 60;
        }
    }

    private List<UserResource> convertForLeaders(List<User> users) {
        List<UserResource> userResources = new ArrayList<>();
        int rank=0;//TODO handle when the tournament has no leaders
        long points = -1;
        UserResource ur;
        for (User user : users) {
            ur = convert(user, false);
            if (points != (user.getPoints() + user.getGlobalTeamPoints() + user.getGlobalGoalPoints())) {
                points = user.getPoints() + user.getGlobalTeamPoints() + user.getGlobalGoalPoints();
                rank++;
            }
            ur.setRank(rank);
            userResources.add(ur);
        }
        return userResources;
    }

    public UserResource convert(User user, boolean shouldSetRank) {
        UserResource ur = new UserResource();
        ur.setId(user.getId());
        ur.setName(user.getName());
        ur.setEmail(user.getEmail());
        ur.setPoints(user.getPoints() + user.getGlobalTeamPoints() + user.getGlobalGoalPoints());
        ur.setGlobalTeamPoints(user.getGlobalTeamPoints());
        ur.setGlobalGoalPoints(user.getGlobalGoalPoints());
        if (shouldSetRank) {
            ur.setRank(getRank(user.getPoints() + user.getGlobalGoalPoints() + user.getGlobalTeamPoints()));
        }
        ur.setTeamWinner(user.getTeamWinner() == null ? null : matchService.convertToResource(user.getTeamWinner()));
        ur.setTeamRunner(user.getTeamRunner() == null ? null : matchService.convertToResource(user.getTeamRunner()));
        ur.setGoals(user.getGoals());
        ur.setSubscription(user.getSubscription());
        return ur;
    }

    private int getRank(long points) {
        List<BigInteger> pointsList = userDao.getDistinctPoints();
        if (pointsList.size() ==1 && pointsList.get(0).longValue()==0) {
            return 0;
        } else {
            return pointsList.indexOf(new BigDecimal(points)) + 1;
        }
    }

    public int getRegisteredUserCount() {
        return userDao.getRegisteredUserCount();
    }
}
