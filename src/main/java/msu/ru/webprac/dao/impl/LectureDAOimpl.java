package msu.ru.webprac.dao.impl;

import msu.ru.webprac.dao.LectureDAO;
import msu.ru.webprac.db.Lectures;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class LectureDAOimpl extends AbstractDAO<Lectures, Long> implements LectureDAO {
    public LectureDAOimpl() {
        super(Lectures.class);
    }

    @Override
    public void insert(Lectures elem) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Query query = session.createNativeQuery("BEGIN; INSERT INTO lecture VALUES (:val1, :val2, :val3, :val4, :val5, :val6); END;");
            query.setParameter("val1", elem.getId());
            query.setParameter("val2", elem.getCourse().getId());
            query.setParameter("val3", elem.getProfessor().getId());
            query.setParameter("val4", elem.getDay());
            query.setParameter("val5", elem.getTime());
            query.setParameter("val6", elem.getDuration());
            query.executeUpdate();
            tx.commit();
        }
    }
}
