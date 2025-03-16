package msu.ru.webprac;

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
import java.sql.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class StudentsDAOTest extends TestWithFiller {

    @Autowired
    private StudentDAO studentsDAO;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testGet() {
        Students elem = studentsDAO.getById(100L);
        assertNotNull(elem);
        assertEquals(100L, elem.getId());

        List<Students> elems = studentsDAO.getAll();
        assertEquals(100, elems.size());

        Students elem2 = studentsDAO.getById(60000L);
        assertNull(elem2);
    }

    @Test
    void testUpdate() {
        String newname = "Бел";
        Students elem = studentsDAO.getById(6L);
        elem.setName(newname);
        studentsDAO.update(6L, elem);
        Students elem2 = studentsDAO.getById(6L);
        assertArrayEquals(newname.getBytes(), elem2.getName().getBytes());
    }

    @Test
    void testInsert() {
        String newname = "Джон";
        Students elem = new Students(128L, newname, "Иванов", "Кириловивич", new Date(2005, 04, 04));
        elem.setName(newname);
        studentsDAO.insert(elem);
        Students elem2 = studentsDAO.getById(128L);
        assertArrayEquals(newname.getBytes(), elem2.getName().getBytes());
    }

    @BeforeEach
    @AfterAll
    void fill() {
        filler(sessionFactory, "postgresql/insert_students.sql");
    }

    @BeforeAll
    @AfterEach
    void reset() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE students_courses RESTART IDENTITY CASCADE;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}