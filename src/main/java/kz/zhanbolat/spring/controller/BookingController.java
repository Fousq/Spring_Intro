package kz.zhanbolat.spring.controller;

import kz.zhanbolat.spring.controller.util.AttributeWrapper;
import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.exception.EntityNotFoundException;
import kz.zhanbolat.spring.service.BookingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class BookingController {
    @Autowired
    private BookingFacade bookingFacade;
    @Autowired
    private AttributeWrapper attributeWrapper;

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
    public String createUser(Model model) {
        Integer userId = attributeWrapper.getInteger(model, "userId", "User id cannot be null.");
        String username = attributeWrapper.getString(model, "username", "Username cannot be null or empty.");
        boolean isCreated = bookingFacade.createUser(new User(userId, username));
        if (!isCreated) {
            return "redirect:/user/create";
        }
        return "redirect:/user";
    }

    @PostMapping("/event/create")
    public String createEvent(Model model) {
        Integer eventId = attributeWrapper.getInteger(model, "eventId", "Event id cannot be null.");
        String eventName = attributeWrapper.getString(model, "eventName", "Event name cannot be null or empty.");
        boolean isCreated = bookingFacade.createEvent(new Event(eventId, eventName));
        if (!isCreated) {
            return "redirect:/event/create";
        }
        return "redirect:/event";
    }

    @PostMapping("/ticket/create")
    public String createTicket(Model model) {
        Integer ticketId = attributeWrapper.getInteger(model, "ticketId", "Ticket id cannot be null.");
        Integer eventId = attributeWrapper.getInteger(model, "eventId", "Event id cannot be null");
        boolean isCreated = bookingFacade.createTicket(Ticket.builder().setId(ticketId).setEventId(eventId).build());
        if (!isCreated) {
            return "redirect:/ticket/create";
        }
        return "redirect:/ticket";
    }

    @GetMapping("/user")
    public ModelAndView getUser(Model model) {
        Integer userId = attributeWrapper.getInteger(model, "userId", "User id cannot be null.");
        final Optional<User> user = bookingFacade.getUser(userId);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("The user cannot be found.");
        }
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("user", user.get());
        return new ModelAndView("user", modelMap);
    }

    @GetMapping("/event")
    public ModelAndView getEvent(Model model) {
        Integer eventId = attributeWrapper.getInteger(model, "eventId", "Event id cannot be null.");
        final Optional<Event> event = bookingFacade.getEvent(eventId);
        if (!event.isPresent()) {
            throw new EntityNotFoundException("The event cannot be found.");
        }
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("event", event.get());
        return new ModelAndView("event", modelMap);
    }

    @GetMapping("/ticket")
    public ModelAndView getTicket(Model model) {
        Integer ticketId = attributeWrapper.getInteger(model, "ticketId", "Ticket id cannot be null.");
        final Optional<Ticket> ticket = bookingFacade.getTicket(ticketId);
        if (!ticket.isPresent()) {
            throw new EntityNotFoundException("The ticket cannot be found.");
        }
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("ticket", ticket.get());
        return new ModelAndView("ticket", modelMap);
    }

    @PostMapping("/book")
    public String bookTicket(Model model) {

        return "";
    }

    @PostMapping("/cancel")
    public String cancelBooking(Model model) {
        return "";
    }
}
