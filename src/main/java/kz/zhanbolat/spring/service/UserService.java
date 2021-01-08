package kz.zhanbolat.spring.service;

import kz.zhanbolat.spring.entity.User;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserByTicketId(Long ticketId);
    Optional<User> getUser(Long id);
    void refillAccount(Long id, BigDecimal amount);
    User saveUser(User user);
}
