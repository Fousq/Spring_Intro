package kz.zhanbolat.spring.entity;

import java.util.Objects;

public class Ticket implements Entity {
    private int id;
    private int userId;
    private int eventId;
    private boolean isBooked;

    public Ticket(Builder builder) {
        id = builder.id;
        userId = builder.userId;
        eventId = builder.eventId;
        isBooked = builder.isBooked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

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
        private int id;
        private int userId;
        private int eventId;
        private boolean isBooked;

        private Builder() {
        }

        public Builder setId(int id) {
            this.id = id;

            return this;
        }

        public Builder setUserId(int userId) {
            this.userId = userId;

            return this;
        }

        public Builder setEventId(int eventId) {
            this.eventId = eventId;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id && userId == ticket.userId && eventId == ticket.eventId && isBooked == ticket.isBooked;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, eventId, isBooked);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", userId=" + userId +
                ", eventId=" + eventId +
                ", isBooked=" + isBooked +
                '}';
    }
}
