package kz.zhanbolat.spring.entity;

import java.util.Objects;

public class Event implements Entity {
    private int id;
    private String name;

    public Event(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Event() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return id == event.id && Objects.equals(name, event.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
