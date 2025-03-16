package msu.ru.webprac;

import msu.ru.webprac.dao.ProfessorDAO;
import msu.ru.webprac.dao.UniversityDAO;
import msu.ru.webprac.db.Professors;
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

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class ProfessorsDAOTest extends TestWithFiller {

    @Autowired
    private ProfessorDAO professorsDAO;
    @Autowired
    private UniversityDAO universityDAO;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testGet() {
        Professors elem = professorsDAO.getById(10L);
        assertNotNull(elem);
        assertEquals(10L, elem.getId());

        List<Professors> elems = professorsDAO.getAll();
        assertEquals(10, elems.size());

        Professors elem2 = professorsDAO.getById(60000L);
        assertNull(elem2);
    }

    @Test
    void testUpdate() {
        String newname = "Бел";
        Professors elem = professorsDAO.getById(6L);
        elem.setName(newname);
        professorsDAO.update(6L, elem);
        Professors elem2 = professorsDAO.getById(6L);
        assertArrayEquals(newname.getBytes(), elem2.getName().getBytes());
    }

    @Test
    void testInsert() {
        String newname = "Джон";
        Professors elem = new Professors(128L, universityDAO.getById(1L), newname, "Иванов", "Кириловивич", new Date(2005, 04, 04));
        elem.setName(newname);
        professorsDAO.insert(elem);
        Professors elem2 = professorsDAO.getById(128L);
        assertArrayEquals(newname.getBytes(), elem2.getName().getBytes());
    }

    @Test
    void testDelete() {
        professorsDAO.delete(6L);
        Professors elem = professorsDAO.getById(6L);
        assertNull(elem);
    }

    @BeforeEach
    @AfterAll
    void fill() {
        filler(sessionFactory, "postgresql/insert_universities.sql");
        filler(sessionFactory, "postgresql/insert_professors.sql");
    }

    @BeforeAll
    @AfterEach
    void reset() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE professors RESTART IDENTITY CASCADE;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}