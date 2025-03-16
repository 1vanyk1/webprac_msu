package msu.ru.webprac.dao.impl;

import msu.ru.webprac.dao.ProfessorDAO;
import msu.ru.webprac.db.Professors;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ProfessorDAOimpl extends AbstractDAO<Professors, Long> implements ProfessorDAO {
    public ProfessorDAOimpl() {
        super(Professors.class);
    }

    @Override
    public void insert(Professors elem) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Query query = session.createNativeQuery("BEGIN; INSERT INTO professors VALUES (:val1, :val2, :val3, :val4, :val5, :val6); END;");
            query.setParameter("val1", elem.getId());
            query.setParameter("val2", elem.getUniversity().getId());
            query.setParameter("val3", elem.getName());
            query.setParameter("val4", elem.getSurname());
            query.setParameter("val5", elem.getPatronymic());
            query.setParameter("val6", elem.getDate());
            query.executeUpdate();
            tx.commit();
        }
    }
}
