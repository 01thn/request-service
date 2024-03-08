package com.reqserv.requestservice.repository;

import com.reqserv.requestservice.model.Role;
import com.reqserv.requestservice.model.Status;
import com.reqserv.requestservice.model.Ticket;
import com.reqserv.requestservice.model.User;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

  default Optional<Ticket> updateTicketStatusById(UUID id, Status status) {
    Optional<Ticket> optionalUser = findById(id);
    optionalUser.ifPresent(ticket -> {
      ticket.setStatus(status);
      save(ticket);
    });
    return optionalUser;
  }

}
