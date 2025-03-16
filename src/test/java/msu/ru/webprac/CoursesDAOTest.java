package msu.ru.webprac;

import msu.ru.webprac.dao.CourseDAO;
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
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class CoursesDAOTest extends TestWithFiller {

    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testGet() {
        Courses elem = courseDAO.getById(6L);
        assertNotNull(elem);
        assertEquals(6L, elem.getId());

        List<Courses> elems = courseDAO.getAll();
        assertEquals(7, elems.size());

        Courses elem2 = courseDAO.getById(60000L);
        assertNull(elem2);
    }

    @Test
    void testUpdate() {
        String newname = "Физика Нейтрино";
        Courses elem = courseDAO.getById(6L);
        elem.setName(newname);
        courseDAO.update(6L, elem);
        Courses elem2 = courseDAO.getById(6L);
        assertArrayEquals(newname.getBytes(), elem2.getName().getBytes());
    }

    @Test
    void testInsert() {
        String newname = "Особенности культуры народов Дальнего Востока";
        Courses elem = new Courses(18L, 18L, newname, "", false);
        elem.setName(newname);
        courseDAO.insert(elem);
        Courses elem2 = courseDAO.getById(18L);
        assertArrayEquals(newname.getBytes(), elem2.getName().getBytes());
    }

    @Test
    void testDelete() {
        courseDAO.delete(6L);
        Courses elem = courseDAO.getById(6L);
        assertNull(elem);
    }

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
    void testRemoveCourse() {
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

    @BeforeEach
    @AfterAll
    void fill() {
        filler(sessionFactory, "postgresql/insert_courses.sql");
    }

    @BeforeAll
    @AfterEach
    void reset() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE courses RESTART IDENTITY CASCADE;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}