package msu.ru.webprac.dao.impl;

import msu.ru.webprac.dao.CourseDAO;
import msu.ru.webprac.db.Courses;
import msu.ru.webprac.db.Students;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class CourseDAOimpl extends AbstractDAO<Courses, Long> implements CourseDAO {
    public CourseDAOimpl() {
        super(Courses.class);
    }

    @Override
    public void insert(Courses elem) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Query query = session.createNativeQuery("BEGIN; INSERT INTO courses VALUES (:val1, :val2, :val3, :val4, :val5); END;");
            query.setParameter("val1", elem.getId());
            query.setParameter("val2", elem.getDuration());
            query.setParameter("val3", elem.getName());
            query.setParameter("val4", elem.getDescription());
            query.setParameter("val5", elem.getEnded());
            query.executeUpdate();
            tx.commit();
        }
    }

    @Override
    public Set<Students> getStudents(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return new HashSet<>(session.get(Courses.class, id).getStudents());
        }
    }

    @Override
    public void addStudent(Long id, Long student_id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Courses course = session.get(Courses.class, id);
            Students student = session.get(Students.class, student_id);
            course.getStudents().add(student);
            session.persist(course);
            tx.commit();
        }
    }

    @Override
    public void deleteStudent(Long id, Long student_id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Courses course = session.get(Courses.class, id);
            Students student = session.get(Students.class, student_id);
            course.getStudents().remove(student);
            session.persist(course);
            tx.commit();
        }
    }
}
