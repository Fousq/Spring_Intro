package kz.zhanbolat.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created to use the beans from xml in annotation-based beans
 */
@Configuration
@ImportResource("classpath:spring-context.xml")
public class ServiceConfig {
}
