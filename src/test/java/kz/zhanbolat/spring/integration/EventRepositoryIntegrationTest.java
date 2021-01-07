package kz.zhanbolat.spring.integration;

import kz.zhanbolat.spring.TestConfig;
import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.repository.impl.EventRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {TestConfig.class})
public class EventRepositoryIntegrationTest {
    @Autowired
    private EventRepositoryImpl eventRepository;

    @Test
    public void shouldReturnEvent_given1AsId() {
        final Optional<Event> event = eventRepository.getEvent(1L);

        assertTrue(event.isPresent());
        assertEquals(new Event(1L, "event1"), event.get());
    }

    @Test
    public void shouldReturnEmpty_whenEventWithSuchIdDoesNotExist() {
        final Optional<Event> event = eventRepository.getEvent(100L);

        assertFalse(event.isPresent());
    }

    @Test
    @Transactional
    public void shouldReturnTrue_whenCreateEvent_givenNewEvent() {
        final boolean isCreated = eventRepository.createEvent(new Event(2L, "event2", new BigDecimal(10)));

        assertTrue(isCreated);
    }

    @Test
    @Transactional
    public void shouldReturnFalse_whenCreateEvent_givenEventWithExistingId() {
        final boolean isCreated = eventRepository.createEvent(new Event(1L, "event2", new BigDecimal(10)));

        assertFalse(isCreated);
    }

    @Test
    public void shouldReturnList_whenGetEvents() {
        final List<Event> events = eventRepository.getEvents();

        assertFalse(events.isEmpty());
    }
}
