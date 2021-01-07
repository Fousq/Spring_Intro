package kz.zhanbolat.spring.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@javax.persistence.Entity
@Table(name = "ticket")
@XmlRootElement(name = "ticket")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Ticket {
    @Id
    private Long id;
    @ManyToOne(targetEntity = User.class)
    private User user;
    @ManyToOne(targetEntity = Event.class)
    private Event event;
    @Column(name = "booked")
    private boolean isBooked;

    public Ticket() {
    }

    public Ticket(Builder builder) {
        id = builder.id;
        user = builder.user;
        event = builder.event;
        isBooked = builder.isBooked;
    }

    @XmlAttribute(name = "id", required = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserId(Long userId) {
        if (Objects.isNull(user)) {
            user = new User();
        }
        user.setId(userId);
    }

    @XmlAttribute(name = "userId")
    public Long getUserId() {
        return Objects.nonNull(user) ? user.getId() : null;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setEventId(Long eventId) {
        if (Objects.isNull(event)) {
            event = new Event();
        }
        event.setId(eventId);
    }

    @XmlAttribute(name = "eventId", required = true)
    public Long getEventId() {
        return Objects.nonNull(event) ? event.getId() : null;
    }

    @XmlAttribute(name = "booked")
    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private User user;
        private Event event;
        private boolean isBooked;

        private Builder() {
        }

        public Builder setId(Long id) {
            this.id = id;

            return this;
        }

        public Builder setUser(User user) {
            this.user = user;

            return this;
        }

        public Builder setEvent(Event event) {
            this.event = event;

            return this;
        }

        public Builder setBooked(boolean isBooked) {
            this.isBooked = isBooked;

            return this;
        }

        public Ticket build() {
            return new Ticket(this);
        }
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", user=" + user +
                ", event=" + event +
                ", isBooked=" + isBooked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return isBooked == ticket.isBooked && Objects.equals(id, ticket.id) && Objects.equals(user, ticket.user) && Objects.equals(event, ticket.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, event, isBooked);
    }
}
