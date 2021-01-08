package kz.zhanbolat.spring.integration;

import kz.zhanbolat.spring.TestConfig;
import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringJUnitConfig(TestConfig.class)
public class UserRepositoryIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldReturnUser_given1AsId() {
        final Optional<User> user = userRepository.findById(1L);

        assertTrue(user.isPresent());
    }

    @Test
    public void shouldReturnEmpty_givenNotExistingId() {
        final Optional<User> user = userRepository.findById(100L);

        assertFalse(user.isPresent());
    }

    @Test
    @Transactional
    public void shouldReturnSavedUser_whenSaveUser_givenNewUser() {
        final User user = new User(5L, "user5", new BigDecimal(1_000));
        final User savedUser = userRepository.save(user);

        assertEquals(user, savedUser);
    }

    @Test
    @Transactional
    public void shouldReturnSavedUser_whenSaveUser_givenExistingUser() {
        final User user = new User(1L, "user5", new BigDecimal(1_000));
        final User savedUser = userRepository.save(user);

        assertEquals(user, savedUser);
    }

    @Test
    public void shouldReturnUser_givenExistingTicketId() {
        final Optional<User> user = userRepository.findByTicketId(2L);

        assertTrue(user.isPresent());
    }

    @Test
    public void shouldReturnEmpty_givenNotExistingTicketId() {
        final Optional<User> user = userRepository.findByTicketId(100L);

        assertFalse(user.isPresent());
    }
}
