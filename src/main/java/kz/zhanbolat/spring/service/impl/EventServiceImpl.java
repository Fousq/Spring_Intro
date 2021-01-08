package kz.zhanbolat.spring.service.impl;

import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.repository.EventRepository;
import kz.zhanbolat.spring.service.EventService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EventServiceImpl implements EventService {
    private EventRepository eventRepository;

    @Override
    @Transactional
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Optional<Event> getEvent(Long id) {
        if (id < 1) {
            throw new IllegalArgumentException("The id cannot be below 1.");
        }
        return eventRepository.findById(id);
    }

    @Override
    public List<Event> getEvents() {
        return StreamSupport.stream(eventRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
}
