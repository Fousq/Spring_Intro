package kz.zhanbolat.spring.repository.impl;

import kz.zhanbolat.spring.entity.Ticket;
import kz.zhanbolat.spring.repository.TicketRepository;
import kz.zhanbolat.spring.storage.DataStorage;
import kz.zhanbolat.spring.storage.EntityNamespace;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketRepositoryImpl implements TicketRepository {
    private DataStorage dataStorage;

    public void setDataStorage(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public boolean createTicket(Ticket ticket) {
        List<Object> tickets = dataStorage.get(EntityNamespace.TICKET.getNamespace());
        if (tickets.stream().anyMatch(createdTicket -> ticket.getId() == ((Ticket) createdTicket).getId())) {
            return false;
        }
        dataStorage.put(EntityNamespace.TICKET.getNamespace(), ticket);
        return true;
    }

    @Override
    public List<Ticket> getUnbookedTicketsForEvent(int eventId) {
        List<Object> tickets = dataStorage.get(EntityNamespace.TICKET.getNamespace());
        return tickets.stream()
                .map(ticket -> (Ticket) ticket)
                .filter(ticket -> !ticket.isBooked() && ticket.getEventId() == eventId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateTicket(Ticket ticket) {
        final List<Object> tickets = dataStorage.get(EntityNamespace.TICKET.getNamespace());
        for (int i = 0; i < tickets.size(); i++) {
            Ticket createdTicket = (Ticket) tickets.get(i);
            if (createdTicket.getId() == ticket.getId()) {
                tickets.set(i, ticket);
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<Ticket> getTicket(int id) {
        List<Object> tickets = dataStorage.get(EntityNamespace.TICKET.getNamespace());
        for (Object ticket : tickets) {
            if (((Ticket) ticket).getId() == id) {
                return Optional.of((Ticket) ticket);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Ticket> getBookedTicketsByUserId(int userId) {
        return dataStorage.get(EntityNamespace.TICKET.getNamespace())
                .stream()
                .map(ticket -> (Ticket) ticket)
                .filter(ticket -> ticket.isBooked() && ticket.getUserId() == userId)
                .collect(Collectors.toList());
    }
}
