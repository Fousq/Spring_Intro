package kz.zhanbolat.spring.processor;

import kz.zhanbolat.spring.storage.DataStorage;
import kz.zhanbolat.spring.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class DataInitializeBeanPostProcessor implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializeBeanPostProcessor.class);
    private static final String DATA_STORAGE_PLACEHOLDER_NAME = "dataStorage";
    private Map<String, String> filePath;

    public DataInitializeBeanPostProcessor(Map<String, String> filePath) {
        this.filePath = filePath;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Storage annotation = AnnotationUtils.findAnnotation(bean.getClass(), Storage.class);
        if (Objects.nonNull(annotation)) {
            if (filePath.containsKey(DATA_STORAGE_PLACEHOLDER_NAME)) {
                Path path;
                try {
                    path = Paths.get(getClass().getClassLoader().getResource(filePath.get(DATA_STORAGE_PLACEHOLDER_NAME)).toURI());
                } catch (URISyntaxException e) {
                    logger.error("Cannot convert path to URI.", e);
                    throw new IllegalStateException("Cannot convert path to URI.", e);
                }
                if (Files.exists(path)) {
                    Properties properties = new Properties();
                    try {
                        properties.load(new FileInputStream(path.toFile()));
                    } catch (Exception e) {
                        logger.error("Cannot load properties from file", e);
                        throw new IllegalStateException("Cannot load properties from file", e);
                    }
                    ((DataStorage) bean).init(properties);
                }
            }
        }
        return bean;
    }
}
