package org.suht.moh.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import org.suht.moh.config.HibernateUtil;
import org.suht.moh.dao.CTFMainDao;
import org.suht.moh.model.CTFMain;

public class CTFMainDaoImpl extends AbstractGenericDao<CTFMain> implements CTFMainDao {
    
    public CTFMainDaoImpl(){
        
    }
    public Long getMinCtfMainId() {
        
        Transaction tx = null;
        Long maxId = 0L;
        try {
            tx = getSession().beginTransaction();
            Criteria criteria = getSession().createCriteria(CTFMain.class)
                .setProjection(Projections.max("id"));
            maxId = (Long) criteria.uniqueResult();
            tx.commit();
        } catch (HibernateException he) {
            tx.rollback();
            he.printStackTrace();
        }
        return maxId;
                       
    }
}
