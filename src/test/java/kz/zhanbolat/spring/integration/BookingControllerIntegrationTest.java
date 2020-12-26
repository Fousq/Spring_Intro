package kz.zhanbolat.spring.integration;

import kz.zhanbolat.spring.config.ServiceConfig;
import kz.zhanbolat.spring.config.WebConfig;
import kz.zhanbolat.spring.controller.BookingController;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("Slow-tests")
@SpringJUnitWebConfig(classes = {WebConfig.class, ServiceConfig.class})
public class BookingControllerIntegrationTest {
    @InjectMocks
    private BookingController bookingController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void shouldCreateUser_andRedirectToUserPage() throws Exception {
        mockMvc.perform(post("/user/create")
                .requestAttr("userId", 4)
                .requestAttr("username", "username"))
                .andExpect(forwardedUrl("/user/4"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void shouldNotCreateUser_andRedirectToUserCreatePage() throws Exception {
        mockMvc.perform(post("/user/create")
                .requestAttr("userId", 1)
                .requestAttr("username", "username"))
                .andExpect(forwardedUrl("/user/create"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreateEvent_andRedirectToEventPage() throws Exception {
        mockMvc.perform(post("/event/create")
                .requestAttr("eventId", 5)
                .requestAttr("eventName", "event name"))
                .andExpect(forwardedUrl("/event/5"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotCreateEvent_andRedirectToEventCreatePage() throws Exception {
        mockMvc.perform(post("/event/create")
                .requestAttr("eventId", 1)
                .requestAttr("eventName", "event name"))
                .andExpect(forwardedUrl("/event/create"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreateTicket_andRedirectToTicketPage() throws Exception {
        mockMvc.perform(post("/ticket/create")
                .requestAttr("ticketId", 8)
                .requestAttr("eventId", 1))
                .andExpect(forwardedUrl("/ticket/8"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotCreateTicket_andRedirectToTicketCreatePage() throws Exception {
        mockMvc.perform(post("/ticket/create")
                .requestAttr("ticketId", 1)
                .requestAttr("eventId", 1))
                .andExpect(forwardedUrl("/ticket/create"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnUser_givenUserId() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andReturn();

        Document content = Jsoup.parse(mvcResult.getResponse().getContentAsString());
        final Elements elements = content.getElementsByTag("h2");
        assertEquals(2, elements.size());
        assertEquals("Id: 1", elements.get(0).text());
        assertEquals("Username: user1", elements.get(1).text());
    }

    @Test
    public void shouldReturnEvent_givenEventId() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get("/event/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("event"))
                .andReturn();

        Document content = Jsoup.parse(mvcResult.getResponse().getContentAsString());
        final Elements elements = content.getElementsByTag("h2");
        assertEquals(2, elements.size());
        assertEquals("Id: 1", elements.get(0).text());
        assertEquals("Name: event1", elements.get(1).text());
    }

    @Test
    public void shouldReturnUnbookedTicket_givenTicketId() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get("/ticket/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ticket"))
                .andReturn();

        Document content = Jsoup.parse(mvcResult.getResponse().getContentAsString());
        final Elements elements = content.getElementsByTag("h2");
        assertEquals(3, elements.size());
        assertEquals("Id: 1", elements.get(0).text());
        assertEquals("Event id: 1", elements.get(1).text());
        assertEquals("Booked: No", elements.get(2).text());
    }

    @Test
    public void shouldReturnBookedTicket_givenTicketId() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get("/ticket/2"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ticket"))
                .andReturn();

        Document content = Jsoup.parse(mvcResult.getResponse().getContentAsString());
        final Elements elements = content.getElementsByTag("h2");
        assertEquals(4, elements.size());
        assertEquals("Id: 2", elements.get(0).text());
        assertEquals("User id: 2", elements.get(1).text());
        assertEquals("Event id: 1", elements.get(2).text());
        assertEquals("Booked: Yes", elements.get(3).text());
    }

    @Test
    public void shouldBookTicketSuccessfully() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(post("/book")
                .requestAttr("userId", 1)
                .requestAttr("ticketId", 1)
                .requestAttr("eventId", 1))
                .andExpect(status().isOk())
                .andReturn();

        Document content = Jsoup.parse(mvcResult.getResponse().getContentAsString());
        final Elements elements = content.getElementsByTag("h1");
        assertEquals(1, elements.size());
        assertEquals("Booking of ticket with id - 1, event id - 1 has been performed successfully for user with id - 1", elements.get(0).text());
    }

    @Test
    public void shouldBookTicketWithFailure() throws Exception {
        mockMvc.perform(post("/book")
                .requestAttr("userId", 1)
                .requestAttr("ticketId", 3)
                .requestAttr("eventId", 1))
                .andExpect(forwardedUrl("/book"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCancelBookingSuccessfully() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(post("/cancel")
                .requestAttr("userId", 2)
                .requestAttr("ticketId", 2)
                .requestAttr("eventId", 1))
                .andExpect(status().isOk())
                .andReturn();

        Document content = Jsoup.parse(mvcResult.getResponse().getContentAsString());
        final Elements elements = content.getElementsByTag("h1");
        assertEquals(1, elements.size());
        assertEquals("Canceling of ticket booking with id - 2, event id - 1 has been performed successfully for user with id - 2", elements.get(0).text());
    }

    @Test
    public void shouldCancelBookingWithFailure() throws Exception {
        mockMvc.perform(post("/cancel")
                .requestAttr("userId", 1)
                .requestAttr("ticketId", 4)
                .requestAttr("eventId", 1))
                .andExpect(forwardedUrl("/cancel"))
                .andExpect(status().isOk());
    }
}