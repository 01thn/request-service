package com.reqserv.requestservice.controller;

import com.reqserv.requestservice.dto.TicketRequestDTO;
import com.reqserv.requestservice.dto.TicketResponseDTO;
import com.reqserv.requestservice.model.Status;
import com.reqserv.requestservice.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket", description = "Ticket API")
public class TicketController {

  private final TicketService ticketService;


  @GetMapping
  public Flux<TicketResponseDTO> getAllTickets(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "4") int size) {
    return ticketService.getAllTickets(page, size);
  }

  @GetMapping("/{id}")
  public Mono<TicketResponseDTO> getTicketById(@PathVariable UUID id) {
    return ticketService.getTicketById(id);
  }

  @PostMapping
  public Mono<TicketResponseDTO> saveTicket(@RequestBody TicketRequestDTO ticket) {
    return ticketService.saveTicket(ticket);
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteTicket(@PathVariable UUID id) {
    return ticketService.deleteTicket(id);
  }

  @PutMapping("/{id}/status")
  public Mono<TicketResponseDTO> updateTicketStatus(@PathVariable UUID id,
      @RequestBody Status status) {
    return ticketService.updateTicketStatus(id, status);
  }

  @PutMapping("/{id}")
  public Mono<TicketResponseDTO> updateTicket(@PathVariable UUID id,
      @RequestBody TicketRequestDTO ticketRequest) {
    return ticketService.updateTicket(id, ticketRequest);

  }

}
