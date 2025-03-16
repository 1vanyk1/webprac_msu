package msu.ru.webprac;

import msu.ru.webprac.dao.CourseDAO;
import msu.ru.webprac.dao.StudentDAO;
import msu.ru.webprac.db.Courses;
import msu.ru.webprac.db.Students;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class StudentsCoursesDAOTest extends TestWithFiller {
    @Autowired
    private StudentDAO studentsDAO;
    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testAddStudent() {
        courseDAO.addStudent(1L, 100L);
        boolean bo = false;
        Set<Students> students = courseDAO.getStudents(1L);
        for (Students student : students) {
            if (student.getId() == 100L) {
                bo = true;
                break;
            }
        }
        assertTrue(bo);
    }

    @Test
    void testRemoveStudent() {
        courseDAO.deleteStudent(1L, 100L);
        boolean bo = false;
        Set<Students> students = courseDAO.getStudents(1L);
        for (Students student : students) {
            if (student.getId() == 100L) {
                bo = true;
                break;
            }
        }
        assertFalse(bo);
    }

    @Test
    void testAddCourse() {
        studentsDAO.addCourse(100L, 1L);
        boolean bo = false;
        Set<Courses> courses = studentsDAO.getCourses(100L);
        for (Courses course : courses) {
            if (course.getId() == 1L) {
                bo = true;
                break;
            }
        }
        assertTrue(bo);
    }

    @Test
    void testRemoveCourse() {
        studentsDAO.deleteCourse(100L, 7L);
        boolean bo = false;
        Set<Courses> courses = studentsDAO.getCourses(100L);
        for (Courses course : courses) {
            if (course.getId() == 1L) {
                bo = true;
                break;
            }
        }
        assertFalse(bo);
    }

    @BeforeEach
    @AfterAll
    void fill() {
        filler(sessionFactory, "postgresql/insert_courses.sql");
        filler(sessionFactory, "postgresql/insert_students.sql");
        filler(sessionFactory, "postgresql/insert_students_courses.sql");
    }

    @BeforeAll
    @AfterEach
    void reset() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE students_courses RESTART IDENTITY CASCADE;").executeUpdate();
            session.getTransaction().commit();
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE courses RESTART IDENTITY CASCADE;").executeUpdate();
            session.getTransaction().commit();
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE students RESTART IDENTITY CASCADE;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}