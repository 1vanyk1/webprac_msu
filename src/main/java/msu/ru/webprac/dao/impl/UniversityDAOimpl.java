package msu.ru.webprac.dao.impl;

import msu.ru.webprac.dao.UniversityDAO;
import msu.ru.webprac.db.Courses;
import msu.ru.webprac.db.Universities;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UniversityDAOimpl extends AbstractDAO<Universities, Long> implements UniversityDAO {
    public UniversityDAOimpl() {
        super(Universities.class);
    }

    @Override
    public void insert(Universities elem) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Query query = session.createNativeQuery("BEGIN; INSERT INTO universities VALUES (:val1, :val2, :val3); END;");
            query.setParameter("val1", elem.getId());
            query.setParameter("val2", elem.getName());
            query.setParameter("val3", elem.getAddress());
            query.executeUpdate();
            tx.commit();
        }
    }
}
