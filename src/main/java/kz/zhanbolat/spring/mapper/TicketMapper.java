package kz.zhanbolat.spring.mapper;

import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.exception.MappingException;

public class TicketMapper extends AbstractMapper implements Mapper<Ticket> {

    @Override
    public Ticket map(String string) throws MappingException {
        final String[] fields = string.trim().split(";");
        if (fields.length < 2 || fields.length > 4) {
            throw new MappingException("Cannot map the string: \"" + string + "\" to Object Event.");
        }
        if (fields.length == 2) {
            return Ticket.builder().setId(parseInt(fields[0])).setEventId(parseInt(fields[1])).build();
        } else if (fields.length == 3) {
            return Ticket.builder()
                    .setId(parseInt(fields[0]))
                    .setUserId(parseInt(fields[1]))
                    .setEventId(parseInt(fields[2]))
                    .build();
        } else {
            if (!("true".equalsIgnoreCase(fields[3]) || "false".equalsIgnoreCase(fields[3]))) {
                throw new MappingException("Cannot map the string: \"" + fields[3] + "\" to boolean.");
            }
            return Ticket.builder()
                    .setId(parseInt(fields[0]))
                    .setUserId(parseInt(fields[1]))
                    .setEventId(parseInt(fields[2]))
                    .setBooked(Boolean.parseBoolean(fields[3]))
                    .build();
        }
    }
}
