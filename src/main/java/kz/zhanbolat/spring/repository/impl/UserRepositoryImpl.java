package kz.zhanbolat.spring.repository.impl;

import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.repository.UserRepository;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private static final String SELECT_USER_BY_TICKET_ID_QUERY = "select ticket.user from Ticket ticket where ticket.id = :ticketId";
    private static final String SELECT_USER_BY_ID_QUERY = "from User user where user.id = :userId";
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean createUser(User user) {
        try {
            sessionFactory.getCurrentSession().createNativeQuery("INSERT INTO user_account(id, username) VALUES (?, ?)")
                    .setParameter(1, user.getId())
                    .setParameter(2, user.getUsername())
                    .executeUpdate();
        } catch (Exception e) {
            logger.error("Got error on creating the user: " + user, e);
            return false;
        }
        return true;
    }

    @Override
    public Optional<User> getUserByTicketId(Long ticketId) {
        User user;
        try {
            user = sessionFactory.openSession().createQuery(SELECT_USER_BY_TICKET_ID_QUERY, User.class)
                    .setParameter("ticketId", ticketId).getSingleResult();
        } catch (PersistenceException e) {
            logger.error("Got error on getting the user by ticket id: " + ticketId, e);
            return Optional.empty();
        }
        return Optional.of(user);
    }

    @Override
    public Optional<User> getUser(Long id) {
        User user;
        try {
            user = sessionFactory.openSession().createQuery(SELECT_USER_BY_ID_QUERY, User.class)
                    .setParameter("userId", id).getSingleResult();
        } catch (PersistenceException e) {
            logger.error("Got error on getting the user by user id: " + id, e);
            return Optional.empty();
        }
        return Optional.of(user);
    }
}
