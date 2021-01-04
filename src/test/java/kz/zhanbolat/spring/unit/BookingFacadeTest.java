package kz.zhanbolat.spring.unit;

import kz.zhanbolat.spring.entity.Event;
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
import static org.mockito.ArgumentMatchers.anyLong;
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
        final Ticket ticket = Ticket.builder().setId(1L).setEvent(new Event(1L)).build();
        when(userService.getUser(anyLong())).thenReturn(Optional.of(new User(1L, "test")));
        when(ticketService.getUnbookedTicketsForEvent(anyLong())).thenReturn(Collections.singletonList(ticket));
        when(ticketService.updateTicket(ticket)).thenReturn(true);

        boolean isTicketBooked = bookingFacade.bookTicket(1L, ticket);

        assertTrue(isTicketBooked);
    }

    @Test
    public void shouldReturnFalse_whenPassNullParameters() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.bookTicket(0L, Ticket.builder().build()));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.bookTicket(-1L, Ticket.builder().build()));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.bookTicket(1L, null));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.bookTicket(0L, null));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.bookTicket(-1L, null));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.cancelBooking(0L, Ticket.builder().build()));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.cancelBooking(-1L, Ticket.builder().build()));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.cancelBooking(1L, null));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.cancelBooking(0L, null));
            assertThrows(IllegalArgumentException.class, () -> bookingFacade.cancelBooking(-1L, null));
        });
    }

    @Test
    public void shouldReturnFalse_whenPassUserAndBookedTicket() {
        when(userService.getUser(anyLong())).thenReturn(Optional.of(new User(1L, "test")));
        when(ticketService.getUnbookedTicketsForEvent(anyLong())).thenReturn(Collections.singletonList(Ticket.builder().setId(2L).build()));

        boolean isTicketBooked = bookingFacade.bookTicket(1L, Ticket.builder().setId(1L).setEvent(new Event(1L)).build());

        assertFalse(isTicketBooked);
    }

    @Test
    public void shouldReturnTrue_whenPassBookedTicketAndUserWhoBookedTicket() {
        final Ticket ticket = Ticket.builder().setId(1L).setEvent(new Event(1L)).build();
        when(userService.getUser(anyLong())).thenReturn(Optional.of(new User(1L, "test")));
        when(ticketService.getUnbookedTicketsForEvent(anyLong())).thenReturn(Collections.singletonList(Ticket.builder().setId(2L).build()));
        when(userService.getUserByTicketId(anyLong())).thenReturn(Optional.of(new User(1L, "test")));
        when(ticketService.updateTicket(ticket)).thenReturn(true);

        boolean isBookingCancel = bookingFacade.cancelBooking(1L, ticket);

        assertTrue(isBookingCancel);
    }

    @Test
    public void shouldReturnFalse_whenPassUnBookedTicketAndUser() {
        when(userService.getUser(anyLong())).thenReturn(Optional.of(new User(1L, "test")));
        when(ticketService.getUnbookedTicketsForEvent(anyLong())).thenReturn(Collections.singletonList(Ticket.builder().setId(1L).build()));

        boolean isBookingCancel = bookingFacade.cancelBooking(1L, Ticket.builder().setId(1L).setEvent(new Event(1L)).build());

        assertFalse(isBookingCancel);
    }

    @Test
    public void shouldReturnFalse_whenPassBookedTicketAndUserWhoDidNotBookTicket() {
        when(userService.getUser(anyLong())).thenReturn(Optional.of(new User(1L, "test")));
        when(ticketService.getUnbookedTicketsForEvent(anyLong())).thenReturn(Collections.singletonList(Ticket.builder().setId(1L).build()));
        when(userService.getUserByTicketId(anyLong())).thenReturn(Optional.of(new User(2L, "test")));

        boolean isBookingCancel = bookingFacade.cancelBooking(1L, Ticket.builder().setId(1L).build());

        assertFalse(isBookingCancel);
    }
}
