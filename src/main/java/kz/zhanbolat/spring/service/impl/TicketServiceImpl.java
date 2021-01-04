package kz.zhanbolat.spring.service.impl;

import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.repository.TicketRepository;
import kz.zhanbolat.spring.service.TicketService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TicketServiceImpl implements TicketService {
    private TicketRepository ticketRepository;

    @Override
    @Transactional
    public boolean createTicket(Ticket ticket) {
        if (Objects.isNull(ticket)) {
            throw new IllegalArgumentException("The ticket object cannot be null.");
        }
        return ticketRepository.createTicket(ticket);
    }

    @Override
    public List<Ticket> getUnbookedTicketsForEvent(Long eventId) {
        if (eventId < 1) {
            throw new IllegalArgumentException("The event id cannot be below 1.");
        }
        return ticketRepository.getUnbookedTicketsForEvent(eventId);
    }

    @Override
    @Transactional
    public boolean updateTicket(Ticket ticket) {
        if (Objects.isNull(ticket)) {
            throw new IllegalArgumentException("The ticket object cannot be null.");
        }
        return ticketRepository.updateTicket(ticket);
    }

    @Override
    public Optional<Ticket> getTicket(Long id) {
        if (id < 1) {
            throw new IllegalArgumentException("The id cannot be below 1.");
        }
        return ticketRepository.getTicket(id);
    }

    @Override
    public List<Ticket> getBookedTicketsByUserId(Long userId) {
        if (userId < 1) {
            throw new IllegalArgumentException("The user id cannot be below 1.");
        }
        return ticketRepository.getBookedTicketsByUserId(userId);
    }

    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
}
