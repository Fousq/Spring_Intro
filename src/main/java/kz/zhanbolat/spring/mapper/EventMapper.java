package kz.zhanbolat.spring.mapper;

import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.exception.MappingException;

public class EventMapper extends AbstractMapper implements Mapper<Event> {
    @Override
    public Event map(String string) throws MappingException {
        final String[] fields = string.trim().split(";");
        if (fields.length != 2) {
            throw new MappingException("Cannot map the string: \"" + string + "\" to Object Event.");
        }
        return new Event(parseInt(fields[0]), fields[1]);
    }
}
