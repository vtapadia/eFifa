package com.vtapadia.fifa.dao;

import com.vtapadia.fifa.domain.Match;
import com.vtapadia.fifa.domain.Prediction;
import com.vtapadia.fifa.domain.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class PredictionDAO extends AbstractDAO {
    Logger log = LoggerFactory.getLogger(PredictionDAO.class);

    @PersistenceContext
    private EntityManager entityManager;

    public Prediction getById(long id) {
        Query query = entityManager.createNativeQuery("select * from ef_prediction where id=?1", Prediction.class);
        query.setParameter(1,id);
        Prediction prediction = (Prediction) query.getSingleResult();
        return prediction;
    }

    public List<Prediction> getForUser(User user) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("user", user));
        List<Prediction> predictions = criteria.list();
        return predictions;
    }

    public List<Prediction> forUpdateForUser(User user) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("user", user));
        criteria.createAlias("match", "match");
        criteria.add(Restrictions.gt("match.matchDate", new Date()));
        criteria.addOrder(Order.asc("match.matchDate"));
        List<Prediction> predictions = criteria.list();
        return predictions;
    }

    public List<Prediction> forViewForUser(User user) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("user", user));
        criteria.createAlias("match", "match");
        Date date = new Date();
        criteria.add(
                Restrictions.or(
                        Restrictions.lt("match.matchDate", date),
                        Restrictions.eq("match.matchDate", date)
                ));
        criteria.addOrder(Order.desc("match.matchDate"));
        List<Prediction> predictions = criteria.list();
        return predictions;
    }

    public List<Prediction> getForMatch(Match match) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("match", match));
        List<Prediction> predictions = criteria.list();
        return predictions;
    }

    public List<Prediction> getForUserMatches(User user, List<Match> matches) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("user", user));
        criteria.createAlias("match", "match");
        criteria.add(Restrictions.in("match", matches));
        criteria.addOrder(Order.asc("match.matchDate")).addOrder(Order.asc("match.id"));
        List<Prediction> predictions = criteria.list();
        return predictions;
    }

    public boolean createSafe(User user, Match match) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("user", user));
        criteria.add(Restrictions.eq("match", match));

        List<Prediction> predictions = criteria.list();
        Prediction prediction = null;
        if (predictions.size() == 0) {
            prediction = new Prediction();
            prediction.setCreated(new Date());
            prediction.setUser(user);
            prediction.setMatch(match);
            prediction.setTeamAScore(0);
            prediction.setTeamBScore(0);
            entityManager.persist(prediction);
        } else {
            log.debug("Ignoring for default prediction " + user.getUser_id() + " match " + match.getId());
        }
        return true;
    }

    public Prediction update(User user, Match match, int teamAScore, int teamBScore) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("user", user));
        criteria.add(Restrictions.eq("match", match));

        List<Prediction> predictions = criteria.list();
        Prediction prediction = null;
        if (predictions.size() == 0) {
            prediction = new Prediction();
            prediction.setCreated(new Date());
            prediction.setUser(user);
            prediction.setMatch(match);
            prediction.setTeamAScore(teamAScore);
            prediction.setTeamBScore(teamBScore);
        } else {
            prediction = predictions.get(0);
            prediction.setTeamAScore(teamAScore);
            prediction.setTeamBScore(teamBScore);
        }
        entityManager.persist(prediction);
        return prediction;
    }

    public void save(Prediction prediction) {
        entityManager.persist(prediction);
    }


    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class getDomainClass() {
        return Prediction.class;
    }
}
