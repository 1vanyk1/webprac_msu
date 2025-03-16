package msu.ru.webprac;

import msu.ru.webprac.dao.UniversityDAO;
import msu.ru.webprac.db.Universities;
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

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class UniversityDAOTest extends TestWithFiller {

    @Autowired
    private UniversityDAO universityDAO;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testGet() {
        Universities elem = universityDAO.getById(6L);
        assertNotNull(elem);
        assertEquals(6L, elem.getId());

        List<Universities> elems = universityDAO.getAll();
        assertEquals(6, elems.size());

        Universities elem2 = universityDAO.getById(60000L);
        assertNull(elem2);
    }

    @Test
    void testUpdate() {
        String newname = "ГИ им. Волкова";
        Universities elem = universityDAO.getById(6L);
        elem.setName(newname);
        universityDAO.update(6L, elem);
        Universities elem2 = universityDAO.getById(6L);
        assertArrayEquals(newname.getBytes(), elem2.getName().getBytes());
    }

    @Test
    void testInsert() {
        String newname = "ВМВФГМУ им. Сокращалкина";
        Universities elem = new Universities(8L, newname, "ул. Уж, д. 127");
        elem.setName(newname);
        universityDAO.insert(elem);
        Universities elem2 = universityDAO.getById(8L);
        assertArrayEquals(newname.getBytes(), elem2.getName().getBytes());
    }

    @Test
    void testDelete() {
        universityDAO.delete(6L);
        Universities elem = universityDAO.getById(6L);
        assertNull(elem);
    }

    @BeforeEach
    @AfterAll
    void fill() {
        filler(sessionFactory, "postgresql/insert_universities.sql");
    }

    @BeforeAll
    @AfterEach
    void reset() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE universities RESTART IDENTITY CASCADE;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}