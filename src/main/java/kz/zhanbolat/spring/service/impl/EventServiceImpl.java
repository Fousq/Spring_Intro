package kz.zhanbolat.spring.service.impl;

import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.repository.EventRepository;
import kz.zhanbolat.spring.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;

    @Override
    public boolean createEvent(Event event) {
        return eventRepository.createEvent(event);
    }

    @Override
    public Optional<Event> getEvent(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("The id cannot be below 1.");
        }
        return eventRepository.getEvent(id);
    }

    @Override
    public List<Event> getEvents() {
        return eventRepository.getEvents();
    }
}
