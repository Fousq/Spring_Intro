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
        when(ticketService.getUnbookedTicketsForEvent(anyInt())).thenReturn(Collections.singletonList(ticket));
        when(ticketService.updateTicket(ticket)).thenReturn(true);

        boolean isTicketBooked = bookingFacade.bookTicket(1, ticket);

        assertTrue(isTicketBooked);
    }

    @Test
    public void shouldReturnFalse_whenPassNullParameters() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.bookTicket(0, Ticket.builder().build()));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.bookTicket(-1, Ticket.builder().build()));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.bookTicket(1, null));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.bookTicket(0, null));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.bookTicket(-1, null));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.cancelBooking(0, Ticket.builder().build()));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.cancelBooking(-1, Ticket.builder().build()));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.cancelBooking(1, null));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.cancelBooking(0, null));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.cancelBooking(-1, null));
        });
    }

    @Test
    public void shouldReturnFalse_whenPassUserAndBookedTicket() {
        when(userService.getUser(anyInt())).thenReturn(Optional.of(new User(1, "test")));
        when(ticketService.getUnbookedTicketsForEvent(anyInt())).thenReturn(Collections.singletonList(Ticket.builder().setId(2).build()));

        boolean isTicketBooked = bookingFacade.bookTicket(1, Ticket.builder().setId(1).setEventId(1).build());

        assertFalse(isTicketBooked);
    }

    @Test
    public void shouldReturnTrue_whenPassBookedTicketAndUserWhoBookedTicket() {
        final Ticket ticket = Ticket.builder().setId(1).setEventId(1).build();
        when(userService.getUser(anyInt())).thenReturn(Optional.of(new User(1, "test")));
        when(ticketService.getUnbookedTicketsForEvent(anyInt())).thenReturn(Collections.singletonList(Ticket.builder().setId(2).build()));
        when(userService.getUserByTicketId(anyInt())).thenReturn(Optional.of(new User(1, "test")));
        when(ticketService.updateTicket(ticket)).thenReturn(true);

        boolean isBookingCancel = bookingFacade.cancelBooking(1, ticket);

        assertTrue(isBookingCancel);
    }

    @Test
    public void shouldReturnFalse_whenPassUnBookedTicketAndUser() {
        when(userService.getUser(anyInt())).thenReturn(Optional.of(new User(1, "test")));
        when(ticketService.getUnbookedTicketsForEvent(anyInt())).thenReturn(Collections.singletonList(Ticket.builder().setId(1).build()));

        boolean isBookingCancel = bookingFacade.cancelBooking(1, Ticket.builder().setId(1).setEventId(1).build());

        assertFalse(isBookingCancel);
    }

    @Test
    public void shouldReturnFalse_whenPassBookedTicketAndUserWhoDidNotBookTicket() {
        when(userService.getUser(anyInt())).thenReturn(Optional.of(new User(1, "test")));
        when(ticketService.getUnbookedTicketsForEvent(anyInt())).thenReturn(Collections.singletonList(Ticket.builder().setId(1).build()));
        when(userService.getUserByTicketId(anyInt())).thenReturn(Optional.of(new User(2, "test")));

        boolean isBookingCancel = bookingFacade.cancelBooking(1, Ticket.builder().setId(1).build());

        assertFalse(isBookingCancel);
    }
}
