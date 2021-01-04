package kz.zhanbolat.spring.service;

import kz.zhanbolat.spring.entity.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {
    boolean createEvent(Event event);
    Optional<Event> getEvent(Long id);
    List<Event> getEvents();
}
