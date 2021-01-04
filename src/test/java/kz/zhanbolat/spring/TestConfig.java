package kz.zhanbolat.spring;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ImportResource("classpath:spring-test-context.xml")
@ComponentScan(basePackages = "kz.zhanbolat.spring.repository.impl")
@EnableTransactionManagement
// todo: think about profiling
public class TestConfig {

    // todo: should be tested later(doesn't work with wsl2)
    public PostgreSQLContainer postgreSQLContainer() {
        PostgreSQLContainer postgresql = new PostgreSQLContainer(DockerImageName.parse("postgres:11"))
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("0");
        postgresql.withExposedPorts(5432);

        Flyway flyway = Flyway.configure()
                .dataSource(postgresql.getJdbcUrl(), postgresql.getUsername(), postgresql.getPassword())
                .locations("classpath:db/migration", "classpath:db/test-migration-data")
                .load();
        flyway.migrate();

        return postgresql;
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource(new HikariConfig(getClass().getClassLoader().getResource("jdbc-test.properties").getPath()));

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration", "classpath:test-migration-data")
                .load();
        flyway.migrate();

        return dataSource;
    }

    @Bean
    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
        return properties;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setHibernateProperties(hibernateProperties());
        sessionFactoryBean.setPackagesToScan("kz.zhanbolat.spring.entity");
        return sessionFactoryBean;
    }

    @Bean
    public TransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory().getObject());
    }
}
