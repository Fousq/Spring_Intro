package kz.zhanbolat.spring.unit;

import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.exception.MappingException;
import kz.zhanbolat.spring.mapper.EventMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventMapperTest {
    private EventMapper eventMapper;

    @BeforeEach
    public void setUp() {
        eventMapper = new EventMapper();
    }

    @Test
    public void shouldReturnEvent_whenPassCorrectString() throws MappingException {
        final Event event = eventMapper.map("1;event name");

        assertNotNull(event);
        assertEquals(1, event.getId());
        assertEquals("event name", event.getName());
    }

    @Test
    public void shouldThrowException_whenPassStringWithNotCorrectFieldsAmount() {
        assertAll(() -> {
            assertThrows(MappingException.class, () -> eventMapper.map("1"));
            assertThrows(MappingException.class, () -> eventMapper.map("1;event;event"));
        });
    }

    @Test
    public void shouldThrowException_whenPassStringWithNotIntegerFirstField() {
        assertAll(() -> {
            assertThrows(MappingException.class, () -> eventMapper.map("id;event"));
            assertThrows(MappingException.class, () -> eventMapper.map("1.5;event"));
        });
    }
}
