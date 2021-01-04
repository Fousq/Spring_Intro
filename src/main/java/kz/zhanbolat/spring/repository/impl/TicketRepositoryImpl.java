package kz.zhanbolat.spring.repository.impl;

import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.repository.TicketRepository;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TicketRepositoryImpl implements TicketRepository {
    private static final Logger logger = LoggerFactory.getLogger(TicketRepositoryImpl.class);
    private static final String SELECT_UNBOOKED_TICKETS_BY_EVENT_ID_QUERY = "select ticket from Ticket ticket " +
            "inner join ticket.event event where ticket.isBooked = false and event.id = :eventId";
    private static final String SELECT_TICKET_BY_ID_QUERY = "from Ticket ticket where ticket.id = :ticketId";
    private static final String SELECT_BOOKED_TICKETS_BY_USER_ID_QUERY = "select ticket from Ticket ticket inner join ticket.user user " +
            "where user.id = :userId and ticket.isBooked = true";
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean createTicket(Ticket ticket) {
        try {
            Query query;
            if (Objects.nonNull(ticket.getUser())) {
                 query = sessionFactory.getCurrentSession()
                        .createNativeQuery("INSERT INTO ticket(id, user_id, event_id, booked) VALUES (?, ?, ?, ?)")
                        .setParameter(1, ticket.getId())
                        .setParameter(2, Objects.nonNull(ticket.getUser()) ? ticket.getUserId() : ticket.getUser())
                        .setParameter(3, ticket.getEventId())
                        .setParameter(4, ticket.isBooked());
            } else {
                query = sessionFactory.getCurrentSession()
                        .createNativeQuery("INSERT INTO ticket(id, event_id, booked) VALUES (?, ?, ?)")
                        .setParameter(1, ticket.getId())
                        .setParameter(2, ticket.getEventId())
                        .setParameter(3, ticket.isBooked());
            }
            query.executeUpdate();
        } catch (Exception e) {
            logger.error("Got error on creating the ticket: " + ticket, e);
            return false;
        }
        return true;
    }

    @Override
    public List<Ticket> getUnbookedTicketsForEvent(Long eventId) {
        return sessionFactory.openSession().createQuery(SELECT_UNBOOKED_TICKETS_BY_EVENT_ID_QUERY, Ticket.class)
                .setParameter("eventId", eventId).getResultList();
    }

    @Override
    public boolean updateTicket(Ticket ticket) {
        try {
            Query query;
            if (Objects.nonNull(ticket.getUser())) {
                query = sessionFactory.getCurrentSession().createNativeQuery("UPDATE ticket SET event_id = :eventId, " +
                        "user_id = :userId, booked = :isBooked WHERE id = :ticketId")
                        .setParameter("eventId", ticket.getEventId())
                        .setParameter("userId", ticket.getUserId())
                        .setParameter("isBooked", ticket.isBooked())
                        .setParameter("ticketId", ticket.getId());
            } else {
                query = sessionFactory.getCurrentSession().createNativeQuery("UPDATE ticket SET event_id = :eventId, " +
                        "booked = :isBooked WHERE id = :ticketId")
                        .setParameter("eventId", ticket.getEventId())
                        .setParameter("isBooked", ticket.isBooked())
                        .setParameter("ticketId", ticket.getId());
            }
            return query.executeUpdate() != 0;
        } catch (Exception e) {
            logger.error("Got error on updating the ticket: " + ticket, e);
            return false;
        }
    }

    @Override
    public Optional<Ticket> getTicket(Long id) {
        Ticket ticket;
        try {
            ticket = sessionFactory.openSession().createQuery(SELECT_TICKET_BY_ID_QUERY, Ticket.class)
                    .setParameter("ticketId", id).getSingleResult();
        } catch (PersistenceException e) {
            logger.error("Got error on getting the ticket with id: " + id, e);
            return Optional.empty();
        }
        return Optional.of(ticket);
    }

    @Override
    public List<Ticket> getBookedTicketsByUserId(Long userId) {
        return sessionFactory.openSession().createQuery(SELECT_BOOKED_TICKETS_BY_USER_ID_QUERY, Ticket.class)
                .setParameter("userId", userId).getResultList();
    }
}
