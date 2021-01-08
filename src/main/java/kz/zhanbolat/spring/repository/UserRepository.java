package kz.zhanbolat.spring.repository;

import kz.zhanbolat.spring.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("select ticket.user from Ticket ticket where ticket.id = ?1")
    Optional<User> findByTicketId(Long ticketId);
}
