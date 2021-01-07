package kz.zhanbolat.spring.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@javax.persistence.Entity
@Table(name = "event")
public class Event {
    @Id
    private Long id;
    private String name;
    @Column(name = "ticket_price", nullable = false)
    private BigDecimal ticketPrice;

    @OneToMany(targetEntity = Ticket.class, mappedBy = "event")
    private List<Ticket> tickets = new ArrayList<>();

    public Event(Long id, String name, BigDecimal ticketPrice) {
        this.id = id;
        this.name = name;
        this.ticketPrice = ticketPrice;
    }

    public Event(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Event(Long id) {
        this.id = id;
    }

    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(name, event.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
