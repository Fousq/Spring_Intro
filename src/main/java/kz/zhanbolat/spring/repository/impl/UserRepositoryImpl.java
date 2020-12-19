package kz.zhanbolat.spring.repository.impl;

import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.repository.UserRepository;
import kz.zhanbolat.spring.storage.DataStorage;
import kz.zhanbolat.spring.storage.EntityNamespace;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private DataStorage dataStorage;

    public void setDataStorage(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public boolean createUser(User user) {
        List<Object> users = dataStorage.get(EntityNamespace.USER.getNamespace());
        if (users.stream().anyMatch(createdUser -> user.getId() == ((User) createdUser).getId())) {
            return false;
        }
        dataStorage.put(EntityNamespace.USER.getNamespace(), user);
        return true;
    }

    @Override
    public Optional<User> getUserByTicketId(int ticketId) {
        List<Object> tickets = dataStorage.get(EntityNamespace.TICKET.getNamespace());
        int userId = 0;
        for (Object ticket : tickets) {
            if (ticketId == ((Ticket) ticket).getId()) {
                userId = ((Ticket) ticket).getUserId();
                break;
            }
        }
        return getUser(userId);
    }

    @Override
    public Optional<User> getUser(int id) {
        List<Object> users = dataStorage.get(EntityNamespace.USER.getNamespace());
        for (Object user : users) {
            if (id == ((User) user).getId()) {
                return Optional.of((User) user);
            }
        }
        return Optional.empty();
    }
}
