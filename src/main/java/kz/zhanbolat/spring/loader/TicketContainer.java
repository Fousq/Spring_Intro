package kz.zhanbolat.spring.loader;

import kz.zhanbolat.spring.entity.Ticket;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "tickets")
public class TicketContainer {
    private List<Ticket> tickets;

    @XmlElement(name = "ticket")
    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
