package com.vtapadia.fifa.dao;

import com.vtapadia.fifa.domain.Roles;
import com.vtapadia.fifa.domain.Subscription;
import com.vtapadia.fifa.domain.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Repository
public class UserDAO extends AbstractDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("user_id", authentication.getName()));
        List<User> users = criteria.list();
        if (users.size()>1) {
            throw new RuntimeException("multiple returned, only one should be possible " + authentication.getName());
        }
        User user = null;
        if (users.size()==1) {
            user = users.get(0);
        }
        return user;
    }

    public User getUser(String userId) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("user_id", userId));
        User user = null;
        try {
            user = (User) criteria.list().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean save(User user) {
        if (user.getId() == 0) {
            Roles roles = new Roles();
            roles.setUserId(user.getUser_id());
            roles.setRole(Roles.DefinedRole.USER);
            entityManager.persist(roles);
        }
        user.setLastUpdated(new Date());
        entityManager.persist(user);
        return true;
    }

    public boolean save(List<User> users) {
        for (User user : users) {
            entityManager.persist(user);
        }
        return true;
    }
    public List<User> getAllUsers() {
        Criteria criteria = getCriteria();
        criteria.addOrder(Order.desc("name"));
        return criteria.list();
    }

    public List<User> gettop(int max) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("subscription", Subscription.FULL));
        criteria.addOrder(Order.desc("points"));
//        Query query = entityManager.createNativeQuery("select * from ef_user where points in (select * from ( " +
//                "select distinct points from ef_user order by points desc " +
//                ") where rownum <= " + max + ") order by points desc,id asc", User.class);
        return criteria.list();
    }

    public List<User> getOrderedUsers() {
//        Criteria criteria = getCriteria();
//        criteria.add(Restrictions.eq("subscription", Subscription.FULL));
//        criteria.addOrder(Order.desc("points + globalTeamPoints + globalGoalPoints"));
        Query query = entityManager.createNativeQuery("select * from ef_user where subscription='FULL' order by points+global_team_points+global_goal_points desc", User.class);
        return query.getResultList();
    }

    public List<BigInteger> getDistinctPoints() {
        Query query = entityManager.createNativeQuery("select distinct (points+global_team_points+global_goal_points) as totalPoints from ef_user order by totalPoints desc");
        return (List<BigInteger>) query.getResultList();
    }

    public int getRegisteredUserCount() {
        Query query = entityManager.createNativeQuery("select count(*) from ef_user");
        return Integer.parseInt(query.getSingleResult().toString());
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class getDomainClass() {
        return User.class;
    }
}
