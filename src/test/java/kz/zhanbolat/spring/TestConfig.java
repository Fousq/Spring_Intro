package kz.zhanbolat.spring;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ImportResource("classpath:spring-test-context.xml")
@ComponentScan(basePackages = "kz.zhanbolat.spring.repository.impl")
@EnableTransactionManagement
@EnableJpaRepositories
// todo: think about profiling
public class TestConfig {

    // doesn't work with wsl2
    @Bean(initMethod = "start", destroyMethod = "stop")
    public PostgreSQLContainer postgreSQLContainer() {
        PostgreSQLContainer postgresql = new PostgreSQLContainer(DockerImageName.parse("postgres:11"))
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("0");
        postgresql.addExposedPort(5432);
        postgresql.waitingFor(Wait.forListeningPort());
        return postgresql;
    }

    @Bean
    public DataSource containerDataSource(PostgreSQLContainer postgreSQLContainer) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        config.setUsername(postgreSQLContainer.getUsername());
        config.setPassword(postgreSQLContainer.getPassword());
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(20);

        final HikariDataSource dataSource = new HikariDataSource(config);

        final Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration", "classpath:test-migration-data")
                .load();
        flyway.migrate();

        return dataSource;
    }

    // use if have an issues to boot tests with testcontainers
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

//    @Bean
//    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
//        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
//        sessionFactoryBean.setDataSource(dataSource);
//        sessionFactoryBean.setHibernateProperties(hibernateProperties());
//        sessionFactoryBean.setPackagesToScan("kz.zhanbolat.spring.entity");
//        return sessionFactoryBean;
//    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("kz.zhanbolat.spring.entity");
        factory.setDataSource(dataSource);
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }

//    @Bean
//    public TransactionManager transactionManager(LocalSessionFactoryBean sessionFactory) {
//        return new HibernateTransactionManager(sessionFactory.getObject());
//    }
}
