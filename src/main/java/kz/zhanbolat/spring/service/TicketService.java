package kz.zhanbolat.spring.service;

import kz.zhanbolat.spring.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    boolean createTicket(Ticket ticket);
    List<Ticket> getUnbookedTicketsForEvent(Long eventId);
    boolean updateTicket(Ticket ticket);
    Optional<Ticket> getTicket(Long id);
    List<Ticket> getBookedTicketsByUserId(Long userId);
}
