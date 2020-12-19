package kz.zhanbolat.spring.integration;

import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.mapper.TicketMapper;
import kz.zhanbolat.spring.repository.impl.TicketRepositoryImpl;
import kz.zhanbolat.spring.storage.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TicketRepositoryIntegrationTest {
    private TicketRepositoryImpl ticketRepository;

    @BeforeEach
    public void setUp() {
        final DataStorage dataStorage = new DataStorage();
        dataStorage.setTicketMapper(new TicketMapper());
        Properties properties = new Properties();
        properties.put("ticket.ticket1", "1;1");
        properties.put("ticket.ticket2", "1;1;1;true");
        dataStorage.init(properties);

        ticketRepository = new TicketRepositoryImpl();
        ticketRepository.setDataStorage(dataStorage);
    }

    @Test
    public void shouldReturnList_given1AsEventId() {
        final List<Ticket> tickets = ticketRepository.getUnbookedTicketsForEvent(1);

        assertFalse(tickets.isEmpty());
    }

    @Test
    public void shouldReturnEmptyList_givenNotExistingEventId() {
        final List<Ticket> tickets = ticketRepository.getUnbookedTicketsForEvent(2);

        assertTrue(tickets.isEmpty());
    }

    @Test
    public void shouldReturnTrue_whenCreateTicket_givenNewTicket() {
        final boolean isCreated = ticketRepository.createTicket(Ticket.builder().setId(4).setEventId(1).build());

        assertTrue(isCreated);
    }

    @Test
    public void shouldReturnFalse_whenCreateTicket_givenTicketWithExistingId() {
        final boolean isCreated = ticketRepository.createTicket(Ticket.builder().setId(1).setEventId(1).build());

        assertFalse(isCreated);
    }

    @Test
    public void shouldReturnTrue_whenUpdateTicket_givenTicketWithExistingId() {
        final boolean isUpdated = ticketRepository.updateTicket(Ticket.builder().setId(1).setEventId(1).build());

        assertTrue(isUpdated);
    }

    @Test
    public void shouldReturnFalse_whenUpdateTicket_givenTicketWithNotExistingId() {
        final boolean isUpdated = ticketRepository.updateTicket(Ticket.builder().setId(100).setEventId(1).build());

        assertFalse(isUpdated);
    }
}
