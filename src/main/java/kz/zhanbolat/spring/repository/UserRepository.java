package kz.zhanbolat.spring.repository;

import kz.zhanbolat.spring.entity.User;

import java.util.Optional;

public interface UserRepository {
    boolean createUser(User user);
    Optional<User> getUserByTicketId(int ticketId);
    Optional<User> getUser(int id);
}
