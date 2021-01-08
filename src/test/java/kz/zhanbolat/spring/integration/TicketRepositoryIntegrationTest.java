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
    @Transactional
    public void shouldReturnTrue_whenCreateTicket_givenNewTicket() {
        final Ticket ticket = Ticket.builder().setId(6L).setEvent(new Event(1L, "event1")).build();
        Ticket savedTicket = ticketRepository.save(ticket);

        assertEquals(ticket, savedTicket);
    }

    @Test
    @Transactional
    public void shouldReturnTrue_whenUpdateTicket_givenTicketWithExistingId() {
        final Ticket ticket = Ticket.builder().setId(1L).setEvent(new Event(1L, "event1")).build();
        Ticket savedTicket = ticketRepository.save(ticket);

        assertEquals(ticket, savedTicket);
    }

    @Test
    public void shouldReturnTicket_whenPassExistingTicketId() {
        Optional<Ticket> ticket = ticketRepository.findById(1L);

        assertTrue(ticket.isPresent());
    }

    @Test
    public void shouldReturnEmpty_whenPassNotExistingTicketId() {
        Optional<Ticket> ticket = ticketRepository.findById(100L);

        assertFalse(ticket.isPresent());
    }

    @Test
    public void shouldReturnList_givenUserId_whenGetBookedTickets() {
        final List<Ticket> tickets = ticketRepository.findAllByUserIdAndBookedTrue(1L);

        assertEquals(1, tickets.size());
    }

    @Test
    public void shouldReturnEmptyList_whenGetBookedTickets() {
        final List<Ticket> tickets = ticketRepository.findAllByUserIdAndBookedTrue(100L);

        assertEquals(Collections.emptyList(), tickets);
    }
}
