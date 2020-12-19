package kz.zhanbolat.spring.unit;

import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.exception.MappingException;
import kz.zhanbolat.spring.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    public void shouldReturnUser_whenPassStringWith2Fields() throws MappingException {
        final User user = userMapper.map("1;username");

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("username", user.getUsername());
    }

    @Test
    public void shouldThrowException_whenPassStringWithNoExceptedFieldsAmount() {
        assertAll(() -> {
            assertThrows(MappingException.class, () -> userMapper.map("1"));
            assertThrows(MappingException.class, () -> userMapper.map("1;username;u"));
        });
    }

    @Test
    public void shouldThrowException_whenPassStringWithNotIntegerFirstField() {
        assertAll(() -> {
            assertThrows(MappingException.class, () -> userMapper.map("id;username"));
            assertThrows(MappingException.class, () -> userMapper.map("1.5;username"));
        });
    }
}
