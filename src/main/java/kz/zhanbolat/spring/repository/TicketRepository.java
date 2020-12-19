package kz.zhanbolat.spring.repository;

import kz.zhanbolat.spring.entity.Ticket;

import java.util.List;

public interface TicketRepository {
    boolean createTicket(Ticket ticket);
    List<Ticket> getUnbookedTicketsForEvent(int eventId);
    boolean updateTicket(Ticket ticket);
}
