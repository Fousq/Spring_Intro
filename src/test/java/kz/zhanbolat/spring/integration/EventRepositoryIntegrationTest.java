package kz.zhanbolat.spring.integration;

import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.mapper.EventMapper;
import kz.zhanbolat.spring.repository.impl.EventRepositoryImpl;
import kz.zhanbolat.spring.storage.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class EventRepositoryIntegrationTest {
    private EventRepositoryImpl eventRepository;

    @BeforeEach
    public void setUp() {
        final DataStorage dataStorage = new DataStorage();
        dataStorage.setEventMapper(new EventMapper());
        Properties properties = new Properties();
        properties.put("event.event1", "1;event1");
        dataStorage.init(properties);

        eventRepository = new EventRepositoryImpl();
        eventRepository.setDataStorage(dataStorage);
    }

    @Test
    public void shouldReturnEvent_given1AsId() {
        final Optional<Event> event = eventRepository.getEvent(1);

        assertTrue(event.isPresent());
        assertEquals(new Event(1, "event1"), event.get());
    }

    @Test
    public void shouldReturnEmpty_whenEventWithSuchIdDoesNotExist() {
        final Optional<Event> event = eventRepository.getEvent(100);

        assertFalse(event.isPresent());
    }

    @Test
    public void shouldReturnTrue_whenCreateEvent_givenNewEvent() {
        final boolean isCreated = eventRepository.createEvent(new Event(2, "event2"));

        assertTrue(isCreated);
    }

    @Test
    public void shouldReturnFalse_whenCreateEvent_givenEventWithExistingId() {
        final boolean isCreated = eventRepository.createEvent(new Event(1, "event2"));

        assertFalse(isCreated);
    }

    @Test
    public void shouldReturnList_whenGetEvents() {
        final List<Event> events = eventRepository.getEvents();

        assertFalse(events.isEmpty());
    }
}
