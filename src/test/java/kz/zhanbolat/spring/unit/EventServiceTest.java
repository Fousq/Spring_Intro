package kz.zhanbolat.spring.unit;

import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.repository.EventRepository;
import kz.zhanbolat.spring.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    public void shouldReturnEvent_whenPassId() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(new Event(1L, "test")));

        final Optional<Event> event = eventService.getEvent(1L);

        assertTrue(event.isPresent());
        assertEquals(1, event.get().getId());
        assertEquals("test", event.get().getName());
    }

    @Test
    public void shouldReturnEmpty_whenPassNonExistedId() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        final Optional<Event> event = eventService.getEvent(1L);

        assertFalse(event.isPresent());
    }

    @Test
    public void shouldThrowExceptions_whenPassNegativeOrZeroId() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> eventService.getEvent(0L));
            assertThrows(IllegalArgumentException.class, () -> eventService.getEvent(-1L));
        });
    }
}
