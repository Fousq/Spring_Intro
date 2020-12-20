package kz.zhanbolat.spring.service.impl;

import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.repository.EventRepository;
import kz.zhanbolat.spring.service.EventService;

import java.util.List;
import java.util.Optional;

public class EventServiceImpl implements EventService {
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

    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
}
