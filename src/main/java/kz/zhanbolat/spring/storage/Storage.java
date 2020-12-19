package kz.zhanbolat.spring.storage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// todo: think about splitting storage to types, because BPP depends on implementation of the data storage class
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Storage {
}
