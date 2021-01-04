package kz.zhanbolat.spring.integration;

import kz.zhanbolat.spring.TestConfig;
import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(TestConfig.class)
public class TicketRepositoryIntegrationTest {
    @Autowired
    private TicketRepository ticketRepository;

    @Test
    public void shouldReturnList_given1AsEventId() {
        final List<Ticket> tickets = ticketRepository.getUnbookedTicketsForEvent(1L);

        assertFalse(tickets.isEmpty());
    }

    @Test
    public void shouldReturnEmptyList_givenNotExistingEventId() {
        final List<Ticket> tickets = ticketRepository.getUnbookedTicketsForEvent(2L);

        assertTrue(tickets.isEmpty());
    }

    @Test
    @Transactional
    public void shouldReturnTrue_whenCreateTicket_givenNewTicket() {
        final boolean isCreated = ticketRepository.createTicket(Ticket.builder().setId(6L).setEvent(new Event(1L)).build());

        assertTrue(isCreated);
    }

    @Test
    @Transactional
    public void shouldReturnFalse_whenCreateTicket_givenTicketWithExistingId() {
        final boolean isCreated = ticketRepository.createTicket(Ticket.builder().setId(1L).setEvent(new Event(1L)).build());

        assertFalse(isCreated);
    }

    @Test
    @Transactional
    public void shouldReturnTrue_whenUpdateTicket_givenTicketWithExistingId() {
        final boolean isUpdated = ticketRepository.updateTicket(Ticket.builder().setId(1L).setEvent(new Event(1L)).build());

        assertTrue(isUpdated);
    }

    @Test
    @Transactional
    public void shouldReturnFalse_whenUpdateTicket_givenTicketWithNotExistingId() {
        final boolean isUpdated = ticketRepository.updateTicket(Ticket.builder().setId(100L).setEvent(new Event(1L)).build());

        assertFalse(isUpdated);
    }

    @Test
    public void shouldReturnTicket_whenPassExistingTicketId() {
        Optional<Ticket> ticket = ticketRepository.getTicket(1L);

        assertTrue(ticket.isPresent());
    }

    @Test
    public void shouldReturnEmpty_whenPassNotExistingTicketId() {
        Optional<Ticket> ticket = ticketRepository.getTicket(100L);

        assertFalse(ticket.isPresent());
    }

    @Test
    public void shouldReturnList_givenUserId_whenGetBookedTickets() {
        final List<Ticket> tickets = ticketRepository.getBookedTicketsByUserId(1L);

        assertEquals(1, tickets.size());
    }

    @Test
    public void shouldReturnEmptyList_whenGetBookedTickets() {
        final List<Ticket> tickets = ticketRepository.getBookedTicketsByUserId(100L);

        assertEquals(Collections.emptyList(), tickets);
    }
}
