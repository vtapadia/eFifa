package com.vtapadia.fifa.dao;

import com.vtapadia.fifa.domain.Match;
import com.vtapadia.fifa.domain.MatchType;
import com.vtapadia.fifa.domain.Prediction;
import com.vtapadia.fifa.domain.Team;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class MatchDAO extends AbstractDAO<Match> {
    Logger log = LoggerFactory.getLogger(MatchDAO.class);

    @Autowired
    private PredictionDAO predictionDAO;

    public Match getMatch(long matchId) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("id", matchId));
        return (Match) criteria.uniqueResult();
    }

    public List<Match> getPendingMatches() {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.isNotNull("teamA"))
                .add(Restrictions.isNotNull("teamB"))
                .add(Restrictions.gt("matchDate", new Date()))
                .addOrder(Order.desc("matchDate")).addOrder(Order.desc("id"));
        return criteria.list();
    }

    public List<Match> getAllNotFinalized() {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.isNull("teamAScore"))
                .add(Restrictions.isNull("teamBScore"))
                .addOrder(Order.desc("matchDate")).addOrder(Order.desc("id"));
        return criteria.list();
    }

    public List<Match> getAllFinalizedMatches() {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.isNotNull("teamA"))
                .add(Restrictions.isNotNull("teamB"))
                .add(Restrictions.isNotNull("teamAScore"))
                .add(Restrictions.isNotNull("teamBScore"))
                //.add(Restrictions.lt("matchDate", new Date()))
                .addOrder(Order.asc("matchDate")).addOrder(Order.asc("id"));
        return criteria.list();
    }

    public List<Match> getAllFrozenMatches() {
        Query query = getEntityManager().createNativeQuery("select * from ef_match " +
                "where match_date<=current_timestamp order by match_date desc" //future matches
                , // teams are decided
                Match.class);
        return query.getResultList();
    }

    public List<Match> getSecondStageMatches() {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.in("matchType", Arrays.asList(
                MatchType.Final, MatchType.SemiFinal, MatchType.QuarterFinal, MatchType.RoundOf16)))
            .addOrder(Order.asc("id"));
        return criteria.list();
    }

    public void finalizeMatch(Match match) {
        if (match.getTeamAScore() != null && match.getTeamBScore() != null) {
            log.info("persisting match " + match.getTeamA().getName() + "("+match.getTeamAScore()+") - ("+match.getTeamBScore()+")" + match.getTeamB().getName());
            int multiplier = match.getMatchType().getMultiplier();
            int aScore = match.getTeamAScore(), bScore=match.getTeamBScore();
            //Update teams
            if (multiplier == 1) {
                log.info("updating teams scores");
                Team teamA=match.getTeamA(), teamB = match.getTeamB();
                teamA.setGf(teamA.getGf() + aScore); teamB.setGa(teamB.getGa() + aScore);
                teamB.setGf(teamB.getGf() + bScore); teamA.setGa(teamA.getGa() + bScore);
                if (aScore == bScore) {
                    teamA.setPoints(teamA.getPoints() + 1);
                    teamB.setPoints(teamB.getPoints() + 1);
                } else {
                    if (aScore > bScore) {
                        teamA.setPoints(teamA.getPoints() + 3);
                    } else {
                        teamB.setPoints(teamB.getPoints() + 3);
                    }
                }
                getEntityManager().persist(teamA);
                getEntityManager().persist(teamB);
            } else {
                log.warn("update team for next match to be done manually");
            }
            getEntityManager().persist(match);
            List<Prediction> predictions = predictionDAO.getForMatch(match);
            for (Prediction prediction : predictions) {
                int scored = 0;
                if (prediction.getTeamAScore() == aScore && prediction.getTeamBScore() == bScore) {
                    //Exact match == 30 points to be added to user
                    scored = 30;
                } else {
                    if (aScore-bScore == prediction.getTeamAScore()-prediction.getTeamBScore()) {
                        //Goal difference is same
                        scored = 20;
                    } else {
                        if ((aScore>bScore && prediction.getTeamAScore()>prediction.getTeamBScore()) ||
                                (aScore<bScore && prediction.getTeamAScore()<prediction.getTeamBScore())) {
                            //Winner is correctly chosen
                            scored = 10;
                        }
                    }
                }
                prediction.setPoints(scored*multiplier);
                prediction.getUser().setPoints(prediction.getUser().getPoints() + (scored*multiplier));
                log.info("User " +prediction.getUser().getUser_id() + " scored " + (scored*multiplier) + " points.");
                getEntityManager().persist(prediction);
                getEntityManager().persist(prediction.getUser());
            }
            getEntityManager().flush();
            getEntityManager().clear();
        }
    }

    @Override
    protected Class getDomainClass() {
        return Match.class;
    }
}
