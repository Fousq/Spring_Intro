package kz.zhanbolat.spring.unit;

import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.repository.UserRepository;
import kz.zhanbolat.spring.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldReturnUser_whenPassExistedUserId() {
        when(userRepository.getUser(anyLong())).thenReturn(Optional.of(new User(1L, "test")));
        final Optional<User> user = userService.getUser(1L);

        assertTrue(user.isPresent());
        assertEquals(1, user.get().getId());
        assertEquals("test", user.get().getUsername());
    }

    @Test
    public void shouldReturnEmpty_whenPassNonExistedUserId() {
        when(userRepository.getUser(anyLong())).thenReturn(Optional.empty());
        final Optional<User> user = userService.getUser(1L);

        assertFalse(user.isPresent());
    }

    @Test
    public void shouldThrowExceptions_whenPassNegativeOrZeroValue() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> userService.getUser(0L));
            assertThrows(IllegalArgumentException.class, () -> userService.getUser(-1L));
        });
    }

    @Test
    public void shouldReturnUser_whenPassTicketId() {
        when(userRepository.getUserByTicketId(anyLong())).thenReturn(Optional.of(new User(1L, "test")));

        final Optional<User> user = userService.getUserByTicketId(1L);

        assertTrue(user.isPresent());
        assertEquals(1, user.get().getId());
        assertEquals("test", user.get().getUsername());
    }

    @Test
    public void shouldReturnEmpty_whenPassNonExistedTicketId() {
        when(userRepository.getUserByTicketId(anyLong())).thenReturn(Optional.empty());

        final Optional<User> user = userService.getUserByTicketId(1L);

        assertFalse(user.isPresent());
    }

    @Test
    public void shouldThrowExceptions_whenPassNegativeOrZeroTicketId() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> userService.getUserByTicketId(0L));
            assertThrows(IllegalArgumentException.class, () -> userService.getUserByTicketId(-1L));
        });
    }

    @Test
    public void shouldRefillAccount_givenExistingUserAndNotZeroOrNegativeAmount() {
        BigDecimal amount = new BigDecimal(100);
        final User user = new User(1L, "test", new BigDecimal(100));
        User updatedUser = new User(user.getId(), user.getUsername(), user.getBalance().add(amount));
        when(userRepository.getUser(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.updateUser(updatedUser)).thenReturn(true);

        userService.refillAccount(1L, amount);
    }

    @Test
    public void shouldThrowExceptions_whenPassNegativeOrZeroAmount() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> userService.refillAccount(1L, new BigDecimal(0)));
            assertThrows(IllegalArgumentException.class, () -> userService.refillAccount(1L, new BigDecimal(-1)));
        });
    }

    @Test
    public void shouldThrowException_whenNotFindUserWithGivenId() {
        when(userRepository.getUser(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.refillAccount(1L, new BigDecimal(1)));
    }
}
