package com.vtapadia.fifa.dao;

import com.vtapadia.fifa.domain.Status;
import com.vtapadia.fifa.domain.Tournament;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class TournamentDAO extends AbstractDAO {
    @Autowired
    EntityManager entityManager;

    public Tournament getCurrentTournament() {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        List<Tournament> list = criteria.list();
        if (list != null && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    public Tournament getTournament(String name) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("name",name));
        List<Tournament> list = criteria.list();
        if (list != null && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class getDomainClass() {
        return Tournament.class;
    }
}
