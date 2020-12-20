package kz.zhanbolat.spring.service;

import kz.zhanbolat.spring.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    boolean createTicket(Ticket ticket);
    List<Ticket> getUnbookedTicketsForEvent(int eventId);
    boolean updateTicket(Ticket ticket);
    Optional<Ticket> getTicket(int id);
}
