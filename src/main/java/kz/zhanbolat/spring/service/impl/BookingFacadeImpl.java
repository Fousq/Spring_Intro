package kz.zhanbolat.spring.service.impl;

import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.exception.BatchActionException;
import kz.zhanbolat.spring.service.BookingFacade;
import kz.zhanbolat.spring.service.EventService;
import kz.zhanbolat.spring.service.TicketService;
import kz.zhanbolat.spring.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class BookingFacadeImpl implements BookingFacade {
    private UserService userService;
    private TicketService ticketService;
    private EventService eventService;
    private Function<String, List<Ticket>> ticketLoader;

    public BookingFacadeImpl(UserService userService, TicketService ticketService, EventService eventService) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.eventService = eventService;
    }

    public void setTicketLoader(Function<String, List<Ticket>> ticketLoader) {
        this.ticketLoader = ticketLoader;
    }

    @Override
    public User saveUser(User user) {
        return userService.saveUser(user);
    }

    @Override
    public Event saveEvent(Event event) {
        return eventService.saveEvent(event);
    }

    @Override
    public Ticket saveTicket(Ticket ticket) {
        return ticketService.saveTicket(ticket);
    }

    @Override
    public Optional<User> getUser(Long id) {
        return userService.getUser(id);
    }

    @Override
    public Optional<Event> getEvent(Long id) {
        return eventService.getEvent(id);
    }

    @Override
    public Optional<Ticket> getTicket(Long id) {
        return ticketService.getTicket(id);
    }

    @Override
    @Transactional
    public boolean bookTicket(Long userId, Long ticketId) {
        if (userId < 1 || ticketId < 1) {
            throw new IllegalArgumentException("User id or ticket object are not suitable. " +
                    "User id must be above 0, and ticket object should not be null.");
        }
        Optional<User> user = userService.getUser(userId);
        if (!user.isPresent()) {
            return false;
        }
        final Optional<Ticket> ticket = ticketService.getTicket(ticketId);
        if (ticket.get().isBooked()) {
            return false;
        }
        final Optional<Event> event = eventService.getEvent(ticket.get().getEventId());
        if (event.get().getTicketPrice().compareTo(user.get().getBalance()) == 1) {
            return false;
        }
        ticket.get().setUser(user.get());
        ticket.get().setBooked(true);
        ticketService.saveTicket(ticket.get());
        user.get().setBalance(user.get().getBalance().subtract(event.get().getTicketPrice()));
        userService.saveUser(user.get());
        return true;
    }

    @Override
    @Transactional
    public boolean cancelBooking(Long userId, Long ticketId) {
        if (userId < 1 || ticketId < 1) {
            throw new IllegalArgumentException("User id or ticket object are not suitable. " +
                    "User id must be above 0, and ticket object should not be null.");
        }
        Optional<User> user = userService.getUser(userId);
        if (!user.isPresent()) {
            return false;
        }
        final Optional<Ticket> ticket = ticketService.getTicket(ticketId);
        if (!ticket.get().isBooked()) {
            return false;
        }
        user = userService.getUserByTicketId(ticketId);
        if (user.isPresent() && user.get().getId() == userId) {
            ticket.get().setUser(null);
            ticket.get().setBooked(false);
            ticketService.saveTicket(ticket.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        return ticketService.getBookedTicketsByUserId(user.getId());
    }

    @Override
    public void preloadTickets(String filePath) {
        final List<Ticket> tickets = ticketLoader.apply(filePath);
        tickets.forEach(ticket -> {
            Optional<Ticket> duplicateTicket = ticketService.getTicket(ticket.getId());
            if (duplicateTicket.isPresent()) {
                throw new BatchActionException("Cannot load tickets, because ticket - " + ticket + " is already loaded");
            }
        });
        tickets.forEach(ticket -> ticketService.saveTicket(ticket));
    }

    @Override
    public void refillAccount(Long id, BigDecimal amount) {
        userService.refillAccount(id, amount);
    }
}
