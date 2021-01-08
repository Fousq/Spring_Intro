package kz.zhanbolat.spring.service;

import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BookingFacade {
    boolean createUser(User user);
    boolean createEvent(Event event);
    boolean createTicket(Ticket ticket);
    Optional<User> getUser(Long id);
    Optional<Event> getEvent(Long id);
    Optional<Ticket> getTicket(Long id);
    boolean bookTicket(Long userId, Long ticketId);
    boolean cancelBooking(Long userId, Long ticketId);
    List<Ticket> getBookedTickets(User user, int pageSize, int pageNum);
    void preloadTickets(String filePath);
    void refillAccount(Long id, BigDecimal amount);
}
