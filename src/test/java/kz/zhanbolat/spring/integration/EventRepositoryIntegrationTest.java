package kz.zhanbolat.spring.integration;

import kz.zhanbolat.spring.TestConfig;
import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {TestConfig.class})
public class EventRepositoryIntegrationTest {
    @Autowired
    private EventRepository eventRepository;

    @Test
    public void shouldReturnEvent_given1AsId() {
        final Optional<Event> event = eventRepository.findById(1L);

        assertTrue(event.isPresent());
        assertEquals(new Event(1L, "event1"), event.get());
    }

    @Test
    public void shouldReturnEmpty_whenEventWithSuchIdDoesNotExist() {
        final Optional<Event> event = eventRepository.findById(100L);

        assertFalse(event.isPresent());
    }

    @Test
    @Transactional
    public void shouldReturnEvent_whenCreateEvent_givenNewEvent() {
        final Event event = new Event(2L, "event2", new BigDecimal(10));
        Event createdEvent = eventRepository.save(event);

        assertEquals(event, createdEvent);
    }

    @Test
    public void shouldReturnList_whenGetEvents() {
        final Iterable<Event> events = eventRepository.findAll();

        assertTrue(events.iterator().hasNext());
    }
}
