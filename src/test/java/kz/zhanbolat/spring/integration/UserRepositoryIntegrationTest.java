package kz.zhanbolat.spring.integration;

import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.mapper.TicketMapper;
import kz.zhanbolat.spring.mapper.UserMapper;
import kz.zhanbolat.spring.repository.impl.UserRepositoryImpl;
import kz.zhanbolat.spring.storage.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRepositoryIntegrationTest {
    private UserRepositoryImpl userRepository;

    @BeforeEach
    public void setUp() {
        final DataStorage dataStorage = new DataStorage();
        dataStorage.setUserMapper(new UserMapper());
        dataStorage.setTicketMapper(new TicketMapper());
        Properties properties = new Properties();
        properties.put("user.user1", "1;user1");
        properties.put("ticket.ticket1", "1;1;1;true");
        dataStorage.init(properties);

        userRepository = new UserRepositoryImpl();
        userRepository.setDataStorage(dataStorage);
    }

    @Test
    public void shouldReturnUser_given1AsId() {
        final Optional<User> user = userRepository.getUser(1);

        assertTrue(user.isPresent());
    }

    @Test
    public void shouldReturnEmpty_givenNotExistingId() {
        final Optional<User> user = userRepository.getUser(100);

        assertFalse(user.isPresent());
    }

    @Test
    public void shouldReturnTrue_whenCreateUser_givenNewUser() {
        final boolean isCreated = userRepository.createUser(new User(2, "user2"));

        assertTrue(isCreated);
    }

    @Test
    public void shouldReturnFalse_whenCreateUser_givenUserWithExistingId() {
        final boolean isCreated = userRepository.createUser(new User(1, "user2"));

        assertFalse(isCreated);
    }

    @Test
    public void shouldReturnUser_givenExistingTicketId() {
        final Optional<User> user = userRepository.getUserByTicketId(1);

        assertTrue(user.isPresent());
    }

    @Test
    public void shouldReturnEmpty_givenNotExistingTicketId() {
        final Optional<User> user = userRepository.getUserByTicketId(100);

        assertFalse(user.isPresent());
    }
}
