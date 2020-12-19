package kz.zhanbolat.spring.service.impl;

import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.repository.UserRepository;
import kz.zhanbolat.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean createUser(User user) {
        return userRepository.createUser(user);
    }

    @Override
    public Optional<User> getUserByTicketId(int ticketId) {
        if (ticketId < 1) {
            throw new IllegalArgumentException("The ticket id cannot be below 1.");
        }
        return userRepository.getUserByTicketId(ticketId);
    }

    @Override
    public Optional<User> getUser(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("The id cannot be below 1.");
        }
        return userRepository.getUser(id);
    }
}
