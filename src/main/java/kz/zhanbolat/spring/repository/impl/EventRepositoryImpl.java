package kz.zhanbolat.spring.repository.impl;

import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.repository.EventRepository;
import kz.zhanbolat.spring.storage.DataStorage;
import kz.zhanbolat.spring.storage.EntityNamespace;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventRepositoryImpl implements EventRepository {
    private DataStorage dataStorage;

    public void setDataStorage(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public boolean createEvent(Event event) {
        List<Object> events = dataStorage.get(EntityNamespace.EVENT.getNamespace());
        if (events.stream().anyMatch(createdEvent -> ((Event) createdEvent).getId() == event.getId())) {
            return false;
        }
        dataStorage.put(EntityNamespace.EVENT.getNamespace(), event);
        return true;
    }

    @Override
    public Optional<Event> getEvent(int id) {
        List<Object> events = dataStorage.get(EntityNamespace.EVENT.getNamespace());
        for (Object event : events) {
            if (((Event) event).getId() == id) {
                return Optional.of((Event) event);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Event> getEvents() {
        return dataStorage.get(EntityNamespace.EVENT.getNamespace())
                .stream()
                .map(event -> (Event) event)
                .collect(Collectors.toList());
    }
}
