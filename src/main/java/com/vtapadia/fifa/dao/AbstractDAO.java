package com.vtapadia.fifa.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;

import javax.persistence.EntityManager;

public abstract class AbstractDAO {
    protected abstract EntityManager getEntityManager();
    protected abstract Class getDomainClass();

    protected Criteria getCriteria() {
        Criteria criteria = getSession().createCriteria(getDomainClass());
        return criteria;
    }

    protected Session getSession() {
        HibernateEntityManager hem = getEntityManager().unwrap(HibernateEntityManager.class);
        Session session = hem.getSession();
        return session;
    }
}
