package kz.zhanbolat.spring.unit;

import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.service.EventService;
import kz.zhanbolat.spring.service.TicketService;
import kz.zhanbolat.spring.service.UserService;
import kz.zhanbolat.spring.service.impl.BookingFacadeImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for class <class>BookingFacadeImpl</class>
 */
public class BookingFacadeTest {
    private UserService userService;
    private EventService eventService;
    private TicketService ticketService;
    private BookingFacadeImpl bookingFacade;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        eventService = mock(EventService.class);
        ticketService = mock(TicketService.class);
        bookingFacade = new BookingFacadeImpl(userService, ticketService, eventService);
    }

    @Test
    public void shouldReturnTrue_whenPassUserAndUnbookedTicket() {
        final Ticket ticket = Ticket.builder().setId(1).setEventId(1).build();
        when(userService.getUser(anyInt())).thenReturn(Optional.of(new User(1, "test")));
        when(ticketService.getTicket(anyInt())).thenReturn(Optional.of(ticket));
        when(ticketService.updateTicket(ticket)).thenReturn(true);

        boolean isTicketBooked = bookingFacade.bookTicket(1, 1);

        assertTrue(isTicketBooked);
    }

    @Test
    public void shouldReturnFalse_whenPassNullParameters() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.bookTicket(0, 1));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.bookTicket(-1, 1));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.bookTicket(1, 0));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.bookTicket(0, 0));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.bookTicket(-1, -1));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.cancelBooking(0, 1));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.cancelBooking(-1, 1));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.cancelBooking(1, 0));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.cancelBooking(0, -1));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.cancelBooking(-1, 0));
        });
    }

    @Test
    public void shouldReturnFalse_whenPassUserAndBookedTicket() {
        when(userService.getUser(anyInt())).thenReturn(Optional.of(new User(1, "test")));
        when(ticketService.getTicket(anyInt())).thenReturn(Optional.of(Ticket.builder().setId(2).setBooked(true).build()));

        boolean isTicketBooked = bookingFacade.bookTicket(1, 2);

        assertFalse(isTicketBooked);
    }

    @Test
    public void shouldReturnTrue_whenPassBookedTicketAndUserWhoBookedTicket() {
        final Ticket ticket = Ticket.builder().setId(1).setEventId(1).setUserId(1).setBooked(true).build();
        final Optional<User> user = Optional.of(new User(1, "test"));
        when(userService.getUser(anyInt())).thenReturn(user);
        when(ticketService.getTicket(anyInt())).thenReturn(Optional.of(ticket));
        when(userService.getUserByTicketId(anyInt())).thenReturn(user);
        when(ticketService.updateTicket(Ticket.builder().setId(1).setEventId(1).build())).thenReturn(true);

        boolean isBookingCancel = bookingFacade.cancelBooking(1, 1);

        assertTrue(isBookingCancel);
    }

    @Test
    public void shouldReturnFalse_whenPassUnBookedTicketAndUser() {
        when(userService.getUser(anyInt())).thenReturn(Optional.of(new User(1, "test")));
        when(ticketService.getTicket(anyInt())).thenReturn(Optional.of(Ticket.builder().setId(1).build()));

        boolean isBookingCancel = bookingFacade.cancelBooking(1, 1);

        assertFalse(isBookingCancel);
    }

    @Test
    public void shouldReturnFalse_whenPassBookedTicketAndUserWhoDidNotBookTicket() {
        when(userService.getUser(anyInt())).thenReturn(Optional.of(new User(1, "test")));
        when(ticketService.getTicket(anyInt())).thenReturn(Optional.of(Ticket.builder().setId(1).build()));
        when(userService.getUserByTicketId(anyInt())).thenReturn(Optional.of(new User(2, "test")));

        boolean isBookingCancel = bookingFacade.cancelBooking(1, 1);

        assertFalse(isBookingCancel);
    }
}
