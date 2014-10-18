package com.vtapadia.fifa.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;
import org.hibernate.transform.DistinctRootEntityResultTransformer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class AbstractDAO<T> {

    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return entityManager;
    };

    protected abstract Class getDomainClass();

    public boolean save(T t) {
        getEntityManager().persist(t);
        return true;
    }

    public boolean save(List<T> tList) {
        for (T t : tList) {
            save(t);
        }
        return true;
    }

    protected Criteria getCriteria() {
        Criteria criteria = getSession().createCriteria(getDomainClass());
        criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
        return criteria;
    }

    protected Session getSession() {
        HibernateEntityManager hem = getEntityManager().unwrap(HibernateEntityManager.class);
        Session session = hem.getSession();
        return session;
    }
}
