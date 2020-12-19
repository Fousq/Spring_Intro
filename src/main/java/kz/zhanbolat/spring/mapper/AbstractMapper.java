package kz.zhanbolat.spring.mapper;

import kz.zhanbolat.spring.exception.MappingException;

public abstract class AbstractMapper {

    protected int parseInt(String s) throws MappingException {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new MappingException("Cannot parse the string \"" + s + "\" to integer.", e);
        }
    }
}
