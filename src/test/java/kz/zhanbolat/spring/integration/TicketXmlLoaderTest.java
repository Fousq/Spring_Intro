package kz.zhanbolat.spring.integration;

import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.loader.TicketXmlLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class TicketXmlLoaderTest {
    private GenericXmlApplicationContext applicationContext;
    private TicketXmlLoader ticketXmlLoader;

    @BeforeEach
    public void setUp() {
        applicationContext = new GenericXmlApplicationContext("spring-test-context.xml");
        ticketXmlLoader = applicationContext.getBean(TicketXmlLoader.class);
    }

    @AfterEach
    public void tearDown() {
        applicationContext.close();
    }

    @Test
    public void shouldReturnNotEmptyList_whenLoadFromXmlFile() throws URISyntaxException {
        final String path = Objects.requireNonNull(getClass().getClassLoader().getResource("test-xml/test-tickets.xml")).toURI().getPath();
        final List<Ticket> tickets = ticketXmlLoader.apply(path);

        assertFalse(tickets.isEmpty());
        assertEquals(Ticket.builder().setId(1).setUserId(1).setEventId(1).setBooked(true).build(), tickets.get(0));
        assertEquals(Ticket.builder().setId(2).setEventId(2).build(), tickets.get(1));
    }

    @Test
    public void shouldReturnEmpty_List_whenLoadFromEmptyXmlFile() {
        final String path = Objects.requireNonNull(getClass().getClassLoader().getResource("test-xml/test-empty-tickets.xml")).getPath();
        final List<Ticket> tickets = ticketXmlLoader.apply(path);

        assertTrue(tickets.isEmpty());
    }
}
