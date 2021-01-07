package kz.zhanbolat.spring.repository.impl;

import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.repository.EventRepository;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;


@Repository
public class EventRepositoryImpl implements EventRepository {
    private static final Logger logger = LoggerFactory.getLogger(EventRepositoryImpl.class);
    private static final String SELECT_EVENT_BY_ID_QUERY = "from Event event where event.id = :eventId";
    private static final String SELECT_EVENT = "from Event event";
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean createEvent(Event event) {
        try {
            sessionFactory.getCurrentSession()
                    .createNativeQuery("INSERT INTO event(id, name, ticket_price) values (?, ?, ?)")
                    .setParameter(1, event.getId())
                    .setParameter(2, event.getName())
                    .setParameter(3, event.getTicketPrice())
                    .executeUpdate();
        } catch (Exception e) {
            logger.error("Got error on create event: " + event, e);
            return false;
        }
        return true;
    }

    @Override
    public Optional<Event> getEvent(Long id) {
        Event event;
        try {
            event = sessionFactory.openSession().createQuery(SELECT_EVENT_BY_ID_QUERY, Event.class)
                    .setParameter("eventId", id)
                    .getSingleResult();
        } catch (PersistenceException e) {
            logger.error("Got error on getting the event with id: " + id, e);
            return Optional.empty();
        }
        return Optional.of(event);
    }

    @Override
    public List<Event> getEvents() {
        return sessionFactory.openSession().createQuery(SELECT_EVENT, Event.class).getResultList();
    }
}
