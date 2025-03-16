package msu.ru.webprac;

import msu.ru.webprac.dao.CourseDAO;
import msu.ru.webprac.dao.LectureDAO;
import msu.ru.webprac.dao.ProfessorDAO;
import msu.ru.webprac.db.Lectures;
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
import java.sql.Time;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class LectureDAOTest extends TestWithFiller {

    @Autowired
    private LectureDAO lectureDAO;
    @Autowired
    private ProfessorDAO professorsDAO;
    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testGet() {
        Lectures elem = lectureDAO.getById(6L);
        assertNotNull(elem);
        assertEquals(6L, elem.getId());

        List<Lectures> elems = lectureDAO.getAll();
        assertEquals(84, elems.size());

        Lectures elem2 = lectureDAO.getById(60000L);
        assertNull(elem2);
    }

    @Test
    void testUpdate() {
        Long newdur = 180L;
        Lectures elem = lectureDAO.getById(6L);
        elem.setDuration(newdur);
        lectureDAO.update(6L, elem);
        Lectures elem2 = lectureDAO.getById(6L);
        assertEquals(newdur, elem2.getDuration());
    }

    @Test
    void testInsert() {
        Long newdur = 180L;
        Lectures elem = new Lectures(108L, courseDAO.getById(1L), professorsDAO.getById(1L), 1000L, new Time(15, 0, 0), 90L);
        elem.setDuration(newdur);
        lectureDAO.insert(elem);
        Lectures elem2 = lectureDAO.getById(108L);
        assertEquals(newdur, elem2.getDuration());
    }

    @Test
    void testDelete() {
        lectureDAO.delete(6L);
        Lectures elem = lectureDAO.getById(6L);
        assertNull(elem);
    }

    @BeforeEach
    @AfterAll
    void fill() {
        filler(sessionFactory, "postgresql/insert_courses.sql");
        filler(sessionFactory, "postgresql/insert_universities.sql");
        filler(sessionFactory, "postgresql/insert_professors.sql");
        filler(sessionFactory, "postgresql/insert_lecture.sql");
    }

    @BeforeAll
    @AfterEach
    void reset() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE lecture RESTART IDENTITY CASCADE;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}