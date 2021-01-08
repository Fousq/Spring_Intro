package kz.zhanbolat.spring.controller;

import kz.zhanbolat.spring.config.view.PdfView;
import kz.zhanbolat.spring.controller.model.BookUnbookDto;
import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.exception.EntityNotFoundException;
import kz.zhanbolat.spring.service.BookingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
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
    public String createUserPage(Model model) {
        model.addAttribute("user", new User());
        return "create_user";
    }

    @GetMapping("/event/create")
    public String createEventPage(Model model) {
        model.addAttribute("event", new Event());
        return "create_event";
    }

    @GetMapping("/ticket/create")
    public String createTicketPage(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "create_ticket";
    }

    @PostMapping("/user/create")
    public String createUser(@ModelAttribute("user") User user, Model model) {
        boolean isCreated = bookingFacade.createUser(user);
        if (!isCreated) {
            model.addAttribute("errorMsg",
                    "Cannot create user with id - " + user.getId() + ", username - " + user.getUsername());
            return "redirect:/user/create";
        }
        return "redirect:/user/" + user.getId();
    }

    @PostMapping("/event/create")
    public String createEvent(@ModelAttribute("event") Event event, Model model) {
        boolean isCreated = bookingFacade.createEvent(event);
        if (!isCreated) {
            model.addAttribute("errorMsg",
                    "Cannot create event with id - " + event.getId() + ", name - " + event.getName());
            return "redirect:/event/create";
        }
        return "redirect:/event/" + event.getId();
    }

    @PostMapping("/ticket/create")
    public String createTicket(@ModelAttribute("ticket") Ticket ticket, Model model) {
        boolean isCreated = bookingFacade.createTicket(ticket);
        if (!isCreated) {
            model.addAttribute("errorMsg",
                    "Cannot create ticket with id - " + ticket.getId() + ", event id - " + ticket.getEventId());
            return "redirect:/ticket/create";
        }
        return "redirect:/ticket/" + ticket.getId();
    }

    @GetMapping("/user/{userId}")
    public String getUser(@PathVariable("userId") Long userId, Model model) {
        final Optional<User> user = bookingFacade.getUser(userId);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("The user cannot be found.");
        }
        model.addAttribute("user", user.get());
        return "user";
    }

    @GetMapping("/event/{eventId}")
    public String getEvent(@PathVariable("eventId") Long eventId, Model model) {
        final Optional<Event> event = bookingFacade.getEvent(eventId);
        if (!event.isPresent()) {
            throw new EntityNotFoundException("The event cannot be found.");
        }
        model.addAttribute("event", event.get());
        return "event";
    }

    @GetMapping("/ticket/{ticketId}")
    public String getTicket(@PathVariable("ticketId") Long ticketId, Model model) {
        final Optional<Ticket> ticket = bookingFacade.getTicket(ticketId);
        if (!ticket.isPresent()) {
            throw new EntityNotFoundException("The ticket cannot be found.");
        }
        model.addAttribute("ticket", ticket.get());
        return "ticket";
    }

    @GetMapping("/book")
    public String bookTicketPage(Model model) {
        model.addAttribute("bookModel", new BookUnbookDto());
        return "bookTicket";
    }

    @PostMapping("/book")
    public String bookTicket(@ModelAttribute("bookModel") BookUnbookDto bookUnbookDto, Model model) {
        final boolean isBooked = bookingFacade.bookTicket(bookUnbookDto.getUserId(), bookUnbookDto.getTicketId());
        if (!isBooked) {
            model.addAttribute("errorMsg", "Ticket cannot be booked with id - "
                    + bookUnbookDto.getTicketId() + " by user with id - " + bookUnbookDto.getUserId());
            return "redirect:/book";
        }
        model.addAttribute("successMsg", "Booking of ticket with id - "
                + bookUnbookDto.getTicketId() + " has been performed successfully for user with id - "
                + bookUnbookDto.getUserId());
        return "success";
    }

    @GetMapping("/cancel")
    public String cancelBookingPage(Model model) {
        model.addAttribute("unbookModel", new BookUnbookDto());
        return "cancelBooking";
    }

    @PostMapping("/cancel")
    public String cancelBooking(@ModelAttribute("unbookModel") BookUnbookDto bookUnbookDto, Model model) {
        final boolean isCanceled = bookingFacade.cancelBooking(bookUnbookDto.getUserId(), bookUnbookDto.getTicketId());
        if (!isCanceled) {
            model.addAttribute("errorMsg",
                    "Booking of ticket cannot be canceled with id - " + bookUnbookDto.getTicketId()
                            +" by user with id - " + bookUnbookDto.getUserId());
            return "redirect:/cancel";
        }
        model.addAttribute("successMsg",
                "Canceling of ticket booking with id - " + bookUnbookDto.getTicketId()
                        + " has been performed successfully for user with id - " + bookUnbookDto.getUserId());
        return "success";
    }

    @GetMapping(value = "/ticket/list/booked/{userId}", produces = "application/pdf")
    public ModelAndView bookedTickets(@PathVariable("userId") Long userId,
                                      @RequestParam(value = "pageSize", defaultValue = "400") Integer pageSize,
                                      @RequestParam(value = "pageNum", defaultValue = "10") Integer pageNum) {
        Map<String, Object> modelMap = new HashMap<>();

        modelMap.put("tickets", bookingFacade.getBookedTickets(new User(userId), pageSize, pageNum));
        modelMap.put("pageSize", pageSize);
        modelMap.put("pageNum", pageNum);

        return new ModelAndView(new PdfView(), modelMap);
    }

    @ExceptionHandler
    public String handleControllerExceptions(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "unpredicted_error_page";
    }
}
