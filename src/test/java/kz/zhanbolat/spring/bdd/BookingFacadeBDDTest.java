package kz.zhanbolat.spring.bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import kz.zhanbolat.spring.TestConfig;
import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.service.BookingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringJUnitConfig(TestConfig.class)
public class BookingFacadeBDDTest {
    @Autowired
    private BookingFacade bookingFacade;
    private User user;
    private Event event;
    private Ticket ticket;
    private Event savedEvent;
    private User savedUser;
    private Ticket savedTicket;
    private boolean isCreated;
    private boolean isBooked;
    private boolean isCanceled;

    @Given("Provide {string} and {string}")
    public void provideAnd(String id, String username) {
        user = new User(Long.parseLong(id), username, new BigDecimal(100));
    }

    @When("Need to create user")
    public void needToCreateUser() {
        savedUser = bookingFacade.saveUser(user);
    }

    @Then("Check saved user")
    public void createUserWithProvidedUser() {
        assertEquals(user, savedUser);
    }

    @Given("Provide event {string} and event {string}")
    public void provideEventAndEvent(String id, String name) {
        event = new Event(Long.parseLong(id), name, BigDecimal.TEN);
    }

    @When("Need to create event")
    public void needToCreateEvent() {
        savedEvent = bookingFacade.saveEvent(event);
    }

    @Then("Check saved event")
    public void createEventWithProvidedIdAndName() {
        assertEquals(event, savedEvent);
    }

    @Given("Provide ticket {string} and event with id {string} and name {string}")
    public void provideIdAndEventSId(String ticketId, String eventId, String eventName) {
        ticket = Ticket.builder()
                .setId(Long.parseLong(ticketId))
                .setEvent(new Event(Long.parseLong(eventId), eventName))
                .build();
    }

    @When("Need to create ticket")
    public void needToCreateTicket() {
        savedTicket = bookingFacade.saveTicket(ticket);
    }

    @Then("Check saved ticket")
    public void createTicketWithProvidedIdAndEventSId() {
        assertEquals(ticket, savedTicket);
    }

    @Given("Provide user {string} and ticket {string}")
    public void provideUserIdAndTicket(String userId, String ticketId) {
        user = bookingFacade.getUser(Long.parseLong(userId)).orElseThrow(() -> new IllegalArgumentException("No user with id: " + userId));
        ticket = bookingFacade.getTicket(Long.parseLong(ticketId)).orElseThrow(() -> new IllegalArgumentException("No ticket with id: " + ticketId));
    }

    @When("Need to book ticket")
    public void needToBookTicket() {
        isBooked = bookingFacade.bookTicket(user.getId(), ticket.getId());
    }

    @Then("Book the ticket {string} successfully")
    public void bookTheTicketSuccessfully(String ticketId) {
        assertTrue(isBooked);
        final Optional<Ticket> bookedTicket = bookingFacade.getTicket(Long.parseLong(ticketId));
        assertTrue(bookedTicket.isPresent());
        assertTrue(bookedTicket.get().isBooked());
        assertEquals(user.getId(), bookedTicket.get().getUserId());
    }

    @Given("Provide user {string} and booked ticket {string} for provided user")
    public void provideUserIdAndBookedTicketForProvidedUser(String userId, String ticketId) {
        user = bookingFacade.getUser(Long.parseLong(userId)).orElseThrow(() -> new IllegalArgumentException("No user with id: " + userId));
        ticket = bookingFacade.getTicket(Long.parseLong(ticketId)).orElseThrow(() -> new IllegalArgumentException("No ticket with id: " + ticketId));
    }

    @When("Need to cancel the booking")
    public void needToCancelTheBooking() {
        isCanceled = bookingFacade.cancelBooking(user.getId(), ticket.getId());
    }

    @Then("Cancel the booking of ticket {string} successfully")
    public void cancelTheBookingOfTicketSuccessfully(String ticketId) {
        assertTrue(isCanceled);
        final Optional<Ticket> bookedTicket = bookingFacade.getTicket(Long.parseLong(ticketId));
        assertTrue(bookedTicket.isPresent());
        assertFalse(bookedTicket.get().isBooked());
        assertNotEquals(user.getId(), bookedTicket.get().getUserId());
    }
}
