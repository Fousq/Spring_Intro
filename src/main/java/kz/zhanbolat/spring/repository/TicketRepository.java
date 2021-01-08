package kz.zhanbolat.spring.repository;

import kz.zhanbolat.spring.entity.Ticket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    @Query("select ticket from Ticket ticket inner join ticket.user user where user.id = ?1 and ticket.isBooked = true")
    List<Ticket> findAllByUserIdAndBookedTrue(Long userId);
}
