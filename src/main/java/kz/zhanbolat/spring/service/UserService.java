package kz.zhanbolat.spring.service;

import kz.zhanbolat.spring.entity.User;

import java.util.Optional;

public interface UserService {
    boolean createUser(User user);
    Optional<User> getUserByTicketId(int ticketId);
    Optional<User> getUser(int id);
}
