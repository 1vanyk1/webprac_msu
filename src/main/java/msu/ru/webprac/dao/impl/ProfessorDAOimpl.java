package msu.ru.webprac.dao.impl;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import msu.ru.webprac.dao.ProfessorDAO;
import msu.ru.webprac.db.Lectures;
import msu.ru.webprac.db.Professors;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<Lectures> getLecturesForProfessor(Long id) {
        List<Lectures> res;
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<Lectures> query = session.getCriteriaBuilder().createQuery(Lectures.class);
            Root<Lectures> root = query.from(Lectures.class);
            query.where(session.getCriteriaBuilder().equal(root.get("professor").get("id"), id));
            res = session.createQuery(query).getResultList();
        }
        return res;
    }
}
