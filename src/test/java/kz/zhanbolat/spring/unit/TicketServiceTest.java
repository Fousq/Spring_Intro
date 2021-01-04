package kz.zhanbolat.spring.unit;

import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.repository.TicketRepository;
import kz.zhanbolat.spring.service.impl.TicketServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
    @Mock
    private TicketRepository ticketRepository;
    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    public void shouldThrowsException_whenPassNull() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> ticketService.getUnbookedTicketsForEvent(0L));
            assertThrows(IllegalArgumentException.class, () -> ticketService.getUnbookedTicketsForEvent(-1L));
        });

    }

    @Test
    public void shouldReturnUnbookedTicketsList_whenPassEvent() {
        when(ticketRepository.getUnbookedTicketsForEvent(anyLong())).thenReturn(Collections.singletonList(Ticket.builder().build()));

        List<Ticket> tickets = ticketService.getUnbookedTicketsForEvent(1L);

        assertFalse(tickets.isEmpty());
    }
}
