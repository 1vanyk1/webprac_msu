package msu.ru.webprac;

//import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class HibernateDatabaseConfig {
    @Value("${url}")
    private String DB_URL;
    @Value("${username}")
    private String DB_USERNAME;
    @Value("${password}")
    private String DB_PASSWORD;

    @Bean(name="entityManagerFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(getDataSource());
        sessionFactory.setPackagesToScan("msu.ru.webprac.db");

        return sessionFactory;
    }

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

//        dataSource.setDriverClassName(DB_DRIVER);
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);

//        PGPoolingDataSource dataSource = new PGPoolingDataSource();
//        dataSource.setDataSourceName("A Data Source");
//        dataSource.setServerName("localhost");
//        dataSource.setPortNumber(5432);
//        dataSource.setDatabaseName("webprac");
//        dataSource.setUser("postgres");
//        dataSource.setPassword("postgres");
//        dataSource.setMaxConnections(10);

        return dataSource;
    }
}