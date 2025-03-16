package msu.ru.webprac;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class TestWithFiller {
    void filler(SessionFactory sessionFactory, String string) {
        try (Session session = sessionFactory.openSession()) {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classloader.getResourceAsStream(string);
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder linebuilder = new StringBuilder();
            for (String line; (line = reader.readLine()) != null;) {
                linebuilder.append(" ").append(line);
            }
            String line = linebuilder.toString();

            session.beginTransaction();
            session.createNativeQuery(line).executeUpdate();
            session.getTransaction().commit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
