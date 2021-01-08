package kz.zhanbolat.spring.bdd;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.service.BookingFacade;
import kz.zhanbolat.spring.service.impl.BookingFacadeImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BookingFacadeBDDTest {
    private ClassPathXmlApplicationContext context;
    private BookingFacade bookingFacade;
    private User user;
    private Event event;
    private Ticket ticket;
    private boolean isCreated;
    private boolean isBooked;
    private boolean isCanceled;

    @Before
    public void setUp() {
        context = new ClassPathXmlApplicationContext("spring-test-context.xml");
        bookingFacade = context.getBean("bookingFacade", BookingFacadeImpl.class);
    }

    @After
    public void tearDown() {
        context.close();
    }

    @Given("Provide {string} and {string}")
    public void provideAnd(String id, String username) {
        user = new User(Integer.parseInt(id), username);
    }

    @When("Need to create user")
    public void needToCreateUser() {
        isCreated = bookingFacade.createUser(user);
    }

    @Then("Create user with provided user {string}")
    public void createUserWithProvidedUser(String id) {
        assertTrue(isCreated);
        Optional<User> createdUser = bookingFacade.getUser(Integer.parseInt(id));
        assertTrue(createdUser.isPresent());
        assertEquals(user, createdUser.get());
    }

    @Given("Provide event {string} and event {string}")
    public void provideEventAndEvent(String id, String name) {
        event = new Event(Integer.parseInt(id), name);
    }

    @When("Need to create event")
    public void needToCreateEvent() {
        isCreated = bookingFacade.createEvent(event);
    }

    @Then("Create event with provided {string}")
    public void createEventWithProvidedIdAndName(String id) {
        assertTrue(isCreated);
        Optional<Event> createdEvent = bookingFacade.getEvent(Integer.parseInt(id));
        assertTrue(createdEvent.isPresent());
        assertEquals(event, createdEvent.get());
    }

    @Given("Provide ticket {string} and event {string}")
    public void provideIdAndEventSId(String ticketId, String eventId) {
        ticket = Ticket.builder()
                .setId(Integer.parseInt(ticketId))
                .setEventId(Integer.parseInt(eventId))
                .build();
    }

    @When("Need to create ticket")
    public void needToCreateTicket() {
        isCreated = bookingFacade.createTicket(ticket);
    }

    @Then("Create ticket with provided {string}")
    public void createTicketWithProvidedIdAndEventSId(String ticketId) {
        assertTrue(isCreated);
        final Optional<Ticket> createdTicket = bookingFacade.getTicket(Integer.parseInt(ticketId));
        assertTrue(createdTicket.isPresent());
        assertEquals(ticket, createdTicket.get());
    }

    @Given("Provide user {string} and ticket {string}")
    public void provideUserIdAndTicket(String userId, String ticketId) {
        user = bookingFacade.getUser(Integer.parseInt(userId)).orElseThrow(() -> new IllegalArgumentException("No user with id: " + userId));
        ticket = bookingFacade.getTicket(Integer.parseInt(ticketId)).orElseThrow(() -> new IllegalArgumentException("No ticket with id: " + ticketId));
    }

    @When("Need to book ticket")
    public void needToBookTicket() {
        isBooked = bookingFacade.bookTicket(user.getId(), ticket.getId());
    }

    @Then("Book the ticket {string} successfully")
    public void bookTheTicketSuccessfully(String ticketId) {
        assertTrue(isBooked);
        final Optional<Ticket> bookedTicket = bookingFacade.getTicket(Integer.parseInt(ticketId));
        assertTrue(bookedTicket.isPresent());
        assertTrue(bookedTicket.get().isBooked());
        assertEquals(user.getId(), bookedTicket.get().getUserId());
    }

    @Given("Provide user {string} and booked ticket {string} for provided user")
    public void provideUserIdAndBookedTicketForProvidedUser(String userId, String ticketId) {
        user = bookingFacade.getUser(Integer.parseInt(userId)).orElseThrow(() -> new IllegalArgumentException("No user with id: " + userId));
        ticket = bookingFacade.getTicket(Integer.parseInt(ticketId)).orElseThrow(() -> new IllegalArgumentException("No ticket with id: " + ticketId));
    }

    @When("Need to cancel the booking")
    public void needToCancelTheBooking() {
        isCanceled = bookingFacade.cancelBooking(user.getId(), ticket.getId());
    }

    @Then("Cancel the booking of ticket {string} successfully")
    public void cancelTheBookingOfTicketSuccessfully(String ticketId) {
        assertTrue(isCanceled);
        final Optional<Ticket> bookedTicket = bookingFacade.getTicket(Integer.parseInt(ticketId));
        assertTrue(bookedTicket.isPresent());
        assertFalse(bookedTicket.get().isBooked());
        assertNotEquals(user.getId(), bookedTicket.get().getUserId());
    }
}
