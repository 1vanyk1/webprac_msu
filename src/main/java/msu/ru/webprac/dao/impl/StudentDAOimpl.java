package msu.ru.webprac.dao.impl;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import msu.ru.webprac.dao.StudentDAO;
import msu.ru.webprac.db.Courses;
import msu.ru.webprac.db.Lectures;
import msu.ru.webprac.db.Students;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

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

    public List<Lectures> getLectures(Long id) {
        List<Lectures> res = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            for (Courses course: session.get(Students.class, id).getCourses()) {
                CriteriaQuery<Lectures> query = session.getCriteriaBuilder().createQuery(Lectures.class);
                Root<Lectures> root = query.from(Lectures.class);
                query.where(session.getCriteriaBuilder().equal(root.get("course").get("id"), course.getId()));
                res.addAll(session.createQuery(query).getResultList());
            }
        }
        Collections.sort(res);
        return res;
    }
}
