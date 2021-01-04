package kz.zhanbolat.spring.repository;

import kz.zhanbolat.spring.entity.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository {
    boolean createEvent(Event event);
    Optional<Event> getEvent(Long id);
    List<Event> getEvents();
}
