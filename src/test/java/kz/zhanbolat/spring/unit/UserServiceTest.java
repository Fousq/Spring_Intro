package kz.zhanbolat.spring.unit;

import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.repository.UserRepository;
import kz.zhanbolat.spring.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldReturnUser_whenPassExistedUserId() {
        when(userRepository.getUser(anyInt())).thenReturn(Optional.of(new User(1, "test")));
        final Optional<User> user = userService.getUser(1);

        assertTrue(user.isPresent());
        assertEquals(1, user.get().getId());
        assertEquals("test", user.get().getUsername());
    }

    @Test
    public void shouldReturnEmpty_whenPassNonExistedUserId() {
        when(userRepository.getUser(anyInt())).thenReturn(Optional.empty());
        final Optional<User> user = userService.getUser(1);

        assertFalse(user.isPresent());
    }

    @Test
    public void shouldThrowExceptions_whenPassNegativeOrZeroValue() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> userService.getUser(0));
            assertThrows(IllegalArgumentException.class, () -> userService.getUser(-1));
        });
    }

    @Test
    public void shouldReturnUser_whenPassTicketId() {
        when(userRepository.getUserByTicketId(anyInt())).thenReturn(Optional.of(new User(1, "test")));

        final Optional<User> user = userService.getUserByTicketId(1);

        assertTrue(user.isPresent());
        assertEquals(1, user.get().getId());
        assertEquals("test", user.get().getUsername());
    }

    @Test
    public void shouldReturnEmpty_whenPassNonExistedTicketId() {
        when(userRepository.getUserByTicketId(anyInt())).thenReturn(Optional.empty());

        final Optional<User> user = userService.getUserByTicketId(1);

        assertFalse(user.isPresent());
    }

    @Test
    public void shouldThrowExceptions_whenPassNegativeOrZeroTicketId() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> userService.getUserByTicketId(0));
            assertThrows(IllegalArgumentException.class, () -> userService.getUserByTicketId(-1));
        });
    }
}
