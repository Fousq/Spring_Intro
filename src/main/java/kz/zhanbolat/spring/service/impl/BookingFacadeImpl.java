package kz.zhanbolat.spring.service.impl;

import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.exception.BatchActionException;
import kz.zhanbolat.spring.service.BookingFacade;
import kz.zhanbolat.spring.service.EventService;
import kz.zhanbolat.spring.service.TicketService;
import kz.zhanbolat.spring.service.UserService;

import java.util.List;
import java.util.Objects;
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
    public boolean createUser(User user) {
        return userService.createUser(user);
    }

    @Override
    public boolean createEvent(Event event) {
        return eventService.createEvent(event);
    }

    @Override
    public boolean createTicket(Ticket ticket) {
        return ticketService.createTicket(ticket);
    }

    @Override
    public Optional<User> getUser(int id) {
        return userService.getUser(id);
    }

    @Override
    public Optional<Event> getEvent(int id) {
        return eventService.getEvent(id);
    }

    @Override
    public Optional<Ticket> getTicket(int id) {
        return ticketService.getTicket(id);
    }

    @Override
    public boolean bookTicket(int userId, Ticket ticket) {
        if (userId < 1 || Objects.isNull(ticket)) {
            throw new IllegalArgumentException("User id or ticket object are not suitable. " +
                    "User id must be above 0, and ticket object should not be null.");
        }
        Optional<User> foundedUser = userService.getUser(userId);
        if (!foundedUser.isPresent()) {
            return false;
        }
        List<Ticket> unbookedTickets = ticketService.getUnbookedTicketsForEvent(ticket.getEventId());
        if (unbookedTickets.stream().anyMatch(unbookedTicket -> unbookedTicket.getId() == ticket.getId())) {
            ticket.setUserId(userId);
            ticket.setBooked(true);
            return ticketService.updateTicket(ticket);
        }
        return false;
    }

    @Override
    public boolean cancelBooking(int userId, Ticket ticket) {
        if (userId < 1 || Objects.isNull(ticket)) {
            throw new IllegalArgumentException("User id or ticket object are not suitable. " +
                    "User id must be above 0, and ticket object should not be null.");
        }
        Optional<User> user = userService.getUser(userId);
        if (!user.isPresent()) {
            return false;
        }
        final List<Ticket> unbookedTickets = ticketService.getUnbookedTicketsForEvent(ticket.getEventId());
        for (Ticket unbookedTicket : unbookedTickets) {
            if (unbookedTicket.getId() == ticket.getId()) {
                return false;
            }
        }
        user = userService.getUserByTicketId(ticket.getId());
        if (user.isPresent() && user.get().getId() == userId) {
            ticket.setUserId(0);
            ticket.setBooked(false);
            return ticketService.updateTicket(ticket);
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
        tickets.forEach(ticket -> ticketService.createTicket(ticket));
    }
}
