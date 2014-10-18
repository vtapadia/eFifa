package com.vtapadia.fifa.dao;

import com.vtapadia.fifa.domain.League;
import com.vtapadia.fifa.domain.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class LeagueDAO extends AbstractDAO {
    Logger log = LoggerFactory.getLogger(LeagueDAO.class);

    public List<League> getLeaguesForOwnerUser(User user) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("leagueOwner", user));
        return criteria.list();
    }

    public boolean save(League league) {
        getEntityManager().persist(league);
        return true;
    }

    @Override
    protected Class getDomainClass() {
        return League.class;
    }
}
