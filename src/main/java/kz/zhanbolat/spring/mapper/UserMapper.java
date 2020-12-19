package kz.zhanbolat.spring.mapper;

import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.exception.MappingException;

public class UserMapper extends AbstractMapper implements Mapper<User> {

    @Override
    public User map(String string) throws MappingException {
        final String[] fields = string.trim().split(";");
        if (fields.length != 2) {
            throw new MappingException("Cannot map the string: \"" + string + "\" to Object User.");
        }
        return new User(parseInt(fields[0]), fields[1]);
    }
}
