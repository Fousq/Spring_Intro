package kz.zhanbolat.spring.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created to use the beans from xml in annotation-based beans
 */
@Configuration
@ImportResource("classpath:spring-context.xml")
@ComponentScan(basePackages = "kz.zhanbolat.spring.repository")
@EnableTransactionManagement
public class ServiceConfig {

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource(new HikariConfig("jdbc.properties"));
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
