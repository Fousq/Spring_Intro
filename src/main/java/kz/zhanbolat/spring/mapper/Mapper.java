package kz.zhanbolat.spring.mapper;

import kz.zhanbolat.spring.entity.Entity;
import kz.zhanbolat.spring.exception.MappingException;

public interface Mapper<T extends Entity> {
    T map(String string) throws MappingException;
}
