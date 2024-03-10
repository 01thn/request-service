package com.reqserv.requestservice.repository;

import com.reqserv.requestservice.model.Status;
import com.reqserv.requestservice.model.Ticket;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TicketRepository extends ReactiveCrudRepository<Ticket, UUID> {

  @Query("SELECT * FROM ticket LIMIT :limit OFFSET :offset")
  Flux<Ticket> findTicketsWithPagination(@Param("limit") int limit, @Param("offset") int offset);

  @Modifying
  @Query("UPDATE tickets SET status = :status WHERE id = :id")
  Mono<Ticket> updateTicketStatusById(@Param("id") UUID id, @Param("status") Status status);

}
