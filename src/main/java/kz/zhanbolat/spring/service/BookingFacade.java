package kz.zhanbolat.spring.service;

import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.entity.User;

import java.util.List;
import java.util.Optional;

public interface BookingFacade {
    boolean createUser(User user);
    boolean createEvent(Event event);
    boolean createTicket(Ticket ticket);
    Optional<User> getUser(int id);
    List<Ticket> getUnbookedTicketsForEvent(int eventId);
    List<Event> getEvents();
    Optional<Event> getEvent(int id);
    boolean bookTicket(int userId, Ticket ticket);
    boolean cancelBooking(int userId, Ticket ticket);
}
