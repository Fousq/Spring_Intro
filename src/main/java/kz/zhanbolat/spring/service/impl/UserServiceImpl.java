package kz.zhanbolat.spring.service.impl;

import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.repository.UserRepository;
import kz.zhanbolat.spring.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    @Transactional
    public boolean createUser(User user) {
        return userRepository.createUser(user);
    }

    @Override
    public Optional<User> getUserByTicketId(Long ticketId) {
        if (ticketId < 1) {
            throw new IllegalArgumentException("The ticket id cannot be below 1.");
        }
        return userRepository.getUserByTicketId(ticketId);
    }

    @Override
    public Optional<User> getUser(Long id) {
        if (id < 1) {
            throw new IllegalArgumentException("The id cannot be below 1.");
        }
        return userRepository.getUser(id);
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
