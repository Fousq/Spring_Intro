package kz.zhanbolat.spring.service.impl;

import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.repository.TicketRepository;
import kz.zhanbolat.spring.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public boolean createTicket(Ticket ticket) {
        if (Objects.isNull(ticket)) {
            throw new IllegalArgumentException("The ticket object cannot be null.");
        }
        return ticketRepository.createTicket(ticket);
    }

    @Override
    public List<Ticket> getUnbookedTicketsForEvent(int eventId) {
        if (eventId < 1) {
            throw new IllegalArgumentException("The event id cannot be below 1.");
        }
        return ticketRepository.getUnbookedTicketsForEvent(eventId);
    }

    @Override
    public boolean updateTicket(Ticket ticket) {
        if (Objects.isNull(ticket)) {
            throw new IllegalArgumentException("The ticket object cannot be null.");
        }
        return ticketRepository.updateTicket(ticket);
    }
}
