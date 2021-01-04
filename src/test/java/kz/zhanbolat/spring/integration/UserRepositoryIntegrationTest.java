package kz.zhanbolat.spring.integration;

import kz.zhanbolat.spring.TestConfig;
import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(TestConfig.class)
public class UserRepositoryIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldReturnUser_given1AsId() {
        final Optional<User> user = userRepository.getUser(1L);

        assertTrue(user.isPresent());
    }

    @Test
    public void shouldReturnEmpty_givenNotExistingId() {
        final Optional<User> user = userRepository.getUser(100L);

        assertFalse(user.isPresent());
    }

    @Test
    @Transactional
    public void shouldReturnTrue_whenCreateUser_givenNewUser() {
        final boolean isCreated = userRepository.createUser(new User(5L, "user5"));

        assertTrue(isCreated);
    }

    @Test
    @Transactional
    public void shouldReturnFalse_whenCreateUser_givenUserWithExistingId() {
        final boolean isCreated = userRepository.createUser(new User(1L, "user2"));

        assertFalse(isCreated);
    }

    @Test
    public void shouldReturnUser_givenExistingTicketId() {
        final Optional<User> user = userRepository.getUserByTicketId(2L);

        assertTrue(user.isPresent());
    }

    @Test
    public void shouldReturnEmpty_givenNotExistingTicketId() {
        final Optional<User> user = userRepository.getUserByTicketId(100L);

        assertFalse(user.isPresent());
    }
}
