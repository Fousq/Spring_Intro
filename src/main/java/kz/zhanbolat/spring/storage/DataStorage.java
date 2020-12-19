package kz.zhanbolat.spring.storage;

import kz.zhanbolat.spring.entity.Event;
import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.entity.User;
import kz.zhanbolat.spring.exception.MappingException;
import kz.zhanbolat.spring.mapper.Mapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Storage
public class DataStorage {
    private static final Logger logger = LogManager.getLogger(DataStorage.class);
    private Mapper<User> userMapper;
    private Mapper<Event> eventMapper;
    private Mapper<Ticket> ticketMapper;
    private Map<String, List<Object>> data;

    public DataStorage() {
        data = new ConcurrentHashMap<>();
    }

    // will be called in Bean Post Processor
    public void init(Properties properties) {
        properties.entrySet().forEach(entrySet -> {
            if (entrySet.getKey() instanceof String) {
                String key = (String) entrySet.getKey();
                if (key.startsWith(EntityNamespace.USER.getNamespace())) {
                    try {
                        User user = userMapper.map((String) entrySet.getValue());
                        addObject(EntityNamespace.USER.getNamespace(), user);
                    } catch (MappingException e) {
                        logger.error("Got the exception during mapping.", e);
                    }
                } else if (key.startsWith(EntityNamespace.EVENT.getNamespace())) {
                    try {
                        Event event = eventMapper.map((String) entrySet.getValue());
                        addObject(EntityNamespace.EVENT.getNamespace(), event);
                    } catch (MappingException e) {
                        logger.error("Got the exception during mapping.", e);
                    }
                } else if (key.startsWith(EntityNamespace.TICKET.getNamespace())) {
                    try {
                        Ticket ticket = ticketMapper.map((String) entrySet.getValue());
                        addObject(EntityNamespace.TICKET.getNamespace(), ticket);
                    } catch (MappingException e) {
                        logger.error("Got the exception during mapping.", e);
                    }
                }
            }
        });
    }

    private void addObject(String key, Object object) {
        if (data.containsKey(key)) {
            List<Object> objects = data.get(key);
            objects.add(object);
        } else {
            List<Object> objects = new ArrayList<>();
            objects.add(object);
            data.put(key, objects);
        }
    }

    public List<Object> get(String key) {
        return data.get(key);
    }

    public void put(String key, Object value) {
        if (!data.containsKey(key)) {
            throw new IllegalArgumentException("Such key does not exist.");
        }
        List<Object> objects = data.get(key);
        if (Objects.isNull(objects)) {
            objects = new ArrayList<>();
        }
        objects.add(value);
    }

    public void setUserMapper(Mapper<User> userMapper) {
        this.userMapper = userMapper;
    }

    public void setEventMapper(Mapper<Event> eventMapper) {
        this.eventMapper = eventMapper;
    }

    public void setTicketMapper(Mapper<Ticket> ticketMapper) {
        this.ticketMapper = ticketMapper;
    }
}
