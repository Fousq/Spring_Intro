package kz.zhanbolat.spring.integration;

import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.mapper.EventMapper;
import kz.zhanbolat.spring.mapper.TicketMapper;
import kz.zhanbolat.spring.mapper.UserMapper;
import kz.zhanbolat.spring.storage.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataStorageIntegrationTest {
    private DataStorage dataStorage;

    @BeforeEach
    public void setUp() {
        dataStorage = new DataStorage();
        dataStorage.setUserMapper(new UserMapper());
        dataStorage.setTicketMapper(new TicketMapper());
        dataStorage.setEventMapper(new EventMapper());
    }

    @Test
    public void shouldReturnValueForValidKeys() {
        Properties properties = new Properties();
        properties.put("user.user1", "1;user1");
        properties.put("event.event1", "1;event1");
        properties.put("ticket.ticket1", "1;1");
        dataStorage.init(properties);

        assertAll(() -> {
            assertEquals(Collections.singletonList(new User(1, "user1")), dataStorage.get("user"));
            assertEquals(Collections.singletonList(new Event(1, "event1")), dataStorage.get("event"));
            assertEquals(Collections.singletonList(Ticket.builder().setId(1).setEventId(1).build()), dataStorage.get("ticket"));
        });
    }
}
