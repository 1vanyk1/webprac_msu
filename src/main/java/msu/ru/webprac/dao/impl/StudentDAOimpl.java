package msu.ru.webprac.dao.impl;

import msu.ru.webprac.dao.StudentDAO;
import msu.ru.webprac.db.Courses;
import msu.ru.webprac.db.Students;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class StudentDAOimpl extends AbstractDAO<Students, Long> implements StudentDAO {
    public StudentDAOimpl() {
        super(Students.class);
    }

    @Override
    public void insert(Students elem) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Query query = session.createNativeQuery("BEGIN; INSERT INTO students VALUES (:val1, :val2, :val3, :val4, :val5); END;");
            query.setParameter("val1", elem.getId());
            query.setParameter("val2", elem.getName());
            query.setParameter("val3", elem.getSurname());
            query.setParameter("val4", elem.getPatronymic());
            query.setParameter("val5", elem.getDate());
            query.executeUpdate();
            tx.commit();
        }
    }

    @Override
    public Set<Courses> getCourses(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return new HashSet<>(session.get(Students.class, id).getCourses());
        }
    }

    @Override
    public void addCourse(Long id, Long course_id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Students student = session.get(Students.class, id);
            Courses course = session.get(Courses.class, course_id);
            student.getCourses().add(course);
            session.persist(student);
            tx.commit();
        }
    }

    @Override
    public void deleteCourse(Long id, Long course_id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Students student = session.get(Students.class, id);
            Courses course = session.get(Courses.class, course_id);
            student.getCourses().remove(course);
            session.persist(student);
            tx.commit();
        }
    }
}
