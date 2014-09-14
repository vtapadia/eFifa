package com.vtapadia.fifa.dao;

import com.vtapadia.fifa.domain.Team;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TeamDAO extends AbstractDAO {

    @Autowired
    private EntityManager entityManager;

    public List<Team> getAllTeams() {
        Query query = entityManager.createNativeQuery("select * from ef_team order by team_group, " +
                "points desc, (goals_for-goals_against) desc, goals_for desc", Team.class);
        return query.getResultList();
    }

    public Team getTeamByName(String name) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.like("name",name));
        List<Team> teams = criteria.list();
        if (teams.size() == 1) {
            return teams.get(0);
        } else {
            throw new RuntimeException("Ideally not possible, team not found");
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    protected Class getDomainClass() {
        return Team.class;
    }
}
