package com.reqserv.requestservice.repository;

import com.reqserv.requestservice.model.Status;
import com.reqserv.requestservice.model.Ticket;
import com.reqserv.requestservice.model.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

  Page<Ticket> findAllByAuthor(Pageable pageable, User author);

  Page<Ticket> findAllByStatusIn(Pageable pageable, Set<Status> status);

  Page<Ticket> findByAuthorUsernameContainingIgnoreCaseAndStatus(Pageable page, String username, Status status);

  default Optional<Ticket> updateTicketStatusById(UUID id, Status status) {
    Optional<Ticket> optionalUser = findById(id);
    optionalUser.ifPresent(ticket -> {
      ticket.setStatus(status);
      save(ticket);
    });
    return optionalUser;
  }

  default Optional<Ticket> updateTicketStatusAndOperatorById(UUID id, Status status,
      User operator) {
    Optional<Ticket> optionalUser = findById(id);
    optionalUser.ifPresent(ticket -> {
      ticket.setStatus(status);
      ticket.setOperator(operator);
      save(ticket);
    });
    return optionalUser;
  }

}
