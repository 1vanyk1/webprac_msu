package msu.ru.webprac.dao.impl;

import jakarta.persistence.criteria.CriteriaQuery;
import msu.ru.webprac.dao.BasicDAO;
import msu.ru.webprac.db.ListBase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public abstract class AbstractDAO<T extends ListBase<T, I>, I extends Serializable> implements BasicDAO<T, I> {
    protected SessionFactory sessionFactory;

    protected Class<T> thisClass;

    public AbstractDAO(Class<T> class_) {
        this.thisClass = class_;
    }

    @Autowired
    public void setSessionFactory(LocalSessionFactoryBean sessionFactory) {
        this.sessionFactory = sessionFactory.getObject();
    }

    @Override
    public T getById(I id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(thisClass, id);
        }
    }

    @Override
    public List<T> getAll() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<T> query = session.getCriteriaBuilder().createQuery(thisClass);
            query.from(thisClass);
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public abstract void insert(T elem);

    @Override
    public void delete(I id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            T elem = session.find(thisClass, id);
            session.remove(elem);
            tx.commit();
            session.close();
        }
    }

    @Override
    public void update(I id, T elem) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            T elemUpdated = session.find(thisClass, id);
            session.evict(elemUpdated);
            elemUpdated.replace(elem);
            session.merge(elemUpdated);
            tx.commit();
            session.close();
        }
    }
}
