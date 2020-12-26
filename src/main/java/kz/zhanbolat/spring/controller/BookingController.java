package kz.zhanbolat.spring.controller;

import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.exception.EntityNotFoundException;
import kz.zhanbolat.spring.service.BookingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class BookingController {
    @Autowired
    private BookingFacade bookingFacade;

    // index page
    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/user/create")
    public String createUserPage() {
        return "create_user";
    }

    @GetMapping("/event/create")
    public String createEventPage() {
        return "create_event";
    }

    @GetMapping("/ticket/create")
    public String createTicketPage() {
        return "create_ticket";
    }

    @PostMapping("/user/create")
    public String createUser(@RequestAttribute Integer userId, @RequestAttribute String username, Model model) {
        boolean isCreated = bookingFacade.createUser(new User(userId, username));
        if (!isCreated) {
            model.addAttribute("errorMsg",
                    "Cannot create user with id - " + userId + ", username - " + username);
            return "forward:/user/create";
        }
        return "forward:/user/" + userId;
    }

    @PostMapping("/event/create")
    public String createEvent(@RequestAttribute Integer eventId, @RequestAttribute String eventName, Model model) {
        boolean isCreated = bookingFacade.createEvent(new Event(eventId, eventName));
        if (!isCreated) {
            model.addAttribute("errorMsg",
                    "Cannot create event with id - " + eventId + ", name - " + eventName);
            return "forward:/event/create";
        }
        return "forward:/event/" + eventId;
    }

    @PostMapping("/ticket/create")
    public String createTicket(@RequestAttribute Integer ticketId, @RequestAttribute Integer eventId, Model model) {
        boolean isCreated = bookingFacade.createTicket(Ticket.builder().setId(ticketId).setEventId(eventId).build());
        if (!isCreated) {
            model.addAttribute("errorMsg",
                    "Cannot create ticket with id - " + ticketId + ", event id - " + eventId);
            return "forward:/ticket/create";
        }
        return "forward:/ticket/" + ticketId;
    }

    @GetMapping("/user/{userId}")
    public String getUser(@PathVariable("userId") Integer userId, Model model) {
        final Optional<User> user = bookingFacade.getUser(userId);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("The user cannot be found.");
        }
        model.addAttribute("user", user.get());
        return "user";
    }

    @GetMapping("/event/{eventId}")
    public String getEvent(@PathVariable("eventId") Integer eventId, Model model) {
        final Optional<Event> event = bookingFacade.getEvent(eventId);
        if (!event.isPresent()) {
            throw new EntityNotFoundException("The event cannot be found.");
        }
        model.addAttribute("event", event.get());
        return "event";
    }

    @GetMapping("/ticket/{ticketId}")
    public String getTicket(@PathVariable("ticketId") Integer ticketId, Model model) {
        final Optional<Ticket> ticket = bookingFacade.getTicket(ticketId);
        if (!ticket.isPresent()) {
            throw new EntityNotFoundException("The ticket cannot be found.");
        }
        model.addAttribute("ticket", ticket.get());
        return "ticket";
    }

    @GetMapping("/book")
    public String bookTicketPage() {
        return "bookTicket";
    }

    @PostMapping("/book")
    public String bookTicket(@RequestAttribute Integer userId, @RequestAttribute Integer ticketId,
                             @RequestAttribute Integer eventId, Model model) {
        final boolean isBooked = bookingFacade.bookTicket(userId, Ticket.builder().setId(ticketId).setEventId(eventId).build());
        if (!isBooked) {
            model.addAttribute("errorMsg",
                    "Ticket cannot be booked with id - " + ticketId + ", event id - " + eventId
                            + " by user with id - " + userId);
            return "forward:/book";
        }
        model.addAttribute("successMsg", "Booking of ticket with id - " + ticketId
                + ", event id - " + eventId + " has been performed successfully for user with id - " + userId);
        return "success";
    }

    @GetMapping("/cancel")
    public String cancelBookingPage() {
        return "cancelBooking";
    }

    @PostMapping("/cancel")
    public String cancelBooking(@RequestAttribute Integer userId, @RequestAttribute Integer ticketId,
                                @RequestAttribute Integer eventId, Model model) {
        final boolean isCanceled = bookingFacade.cancelBooking(userId,
                Ticket.builder().setId(ticketId).setEventId(eventId).setUserId(userId).build());
        if (!isCanceled) {
            model.addAttribute("errorMsg",
                    "Booking of ticket cannot be canceled with id - " + ticketId + ", event id - " + eventId
                            + " by user with id - " + userId);
            return "forward:/cancel";
        }
        model.addAttribute("successMsg", "Canceling of ticket booking with id - " + ticketId
                + ", event id - " + eventId + " has been performed successfully for user with id - " + userId);
        return "success";
    }

    @GetMapping("/ticket/list/booked/{userId}")
    public Object bookedTickets(@PathVariable("userId") Integer userId, @RequestParam("pageSize") Integer pageSize,
                                @RequestParam("pageNum") Integer pageNum) {

        return "";
    }
}
