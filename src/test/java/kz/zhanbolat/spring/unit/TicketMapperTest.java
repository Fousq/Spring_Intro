package kz.zhanbolat.spring.unit;

import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.exception.MappingException;
import kz.zhanbolat.spring.mapper.TicketMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TicketMapperTest {
    private TicketMapper ticketMapper;

    @BeforeEach
    public void setUp() {
        ticketMapper = new TicketMapper();
    }

    @Test
    public void shouldReturnTicket_whenPassCorrectString() {
        assertAll(() -> {
            assertEquals(Ticket.builder().setId(1).setEventId(2).build(), ticketMapper.map("1;2"));
            assertEquals(Ticket.builder().setId(1).setUserId(2).setEventId(3).build(), ticketMapper.map("1;2;3"));
            assertEquals(Ticket.builder().setId(1).setUserId(2).setEventId(3).setBooked(true).build(), ticketMapper.map("1;2;3;true"));
            assertEquals(Ticket.builder().setId(1).setUserId(2).setEventId(3).setBooked(false).build(), ticketMapper.map("1;2;3;false"));
        });
    }

    @Test
    public void shouldThrowException_whenPassStringWithNoCorrectFieldsAmount() {
        assertAll(() -> {
            assertThrows(MappingException.class, () -> ticketMapper.map("1"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1;1;2;4;5"));
        });
    }

    @Test
    public void shouldThrowException_whenPassStringWithNotMatchingTypesFields() {
        assertAll(() -> {
            assertThrows(MappingException.class, () -> ticketMapper.map("id;2"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1.6;2"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1;id"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1;2.6"));
            assertThrows(MappingException.class, () -> ticketMapper.map("id;id"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1.6;2.6"));
            assertThrows(MappingException.class, () -> ticketMapper.map("id;2;3"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1.6;2;3"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1;id;3"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1;2.6;3"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1;2;id"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1;2;3.2"));
            assertThrows(MappingException.class, () -> ticketMapper.map("id;id;3"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1.6;2.5;3"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1;id;id"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1;2.5;3.3"));
            assertThrows(MappingException.class, () -> ticketMapper.map("id;2;id"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1.6;2;3.3"));
            assertThrows(MappingException.class, () -> ticketMapper.map("id;id;id"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1.6;2.5;3.3"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1;2;3;4"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1;2;3;boolean"));
            assertThrows(MappingException.class, () -> ticketMapper.map("1;2;3;4.5"));
        });
    }
}
