package com.reqserv.requestservice.controller;

import com.reqserv.requestservice.dto.TicketRequestDTO;
import com.reqserv.requestservice.dto.TicketResponseDTO;
import com.reqserv.requestservice.exception.BadTicketStatusException;
import com.reqserv.requestservice.exception.NoSuchTicketException;
import com.reqserv.requestservice.model.Status;
import com.reqserv.requestservice.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
@Tag(name = "Ticket", description = "Ticket API")
public class TicketController {

  private final TicketService ticketService;


  @GetMapping
  public ResponseEntity<Page<TicketResponseDTO>> getAllTickets(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "4") int size) {
    return ResponseEntity.ok(ticketService.getAllTickets(PageRequest.of(page, size)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable UUID id) {
    Optional<TicketResponseDTO> ticket = ticketService.getTicketById(id);
    return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<TicketResponseDTO> saveTicket(@RequestBody TicketRequestDTO ticket) {
    return new ResponseEntity<>(ticketService.saveTicket(ticket), HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTicket(@PathVariable UUID id) {
    ticketService.deleteTicket(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<TicketResponseDTO> updateTicketStatus(@PathVariable UUID id,
      @RequestBody Status status) {
    Optional<TicketResponseDTO> updatedTicket = ticketService.updateTicketStatus(id, status);
    return updatedTicket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<TicketResponseDTO> updateTicket(@PathVariable UUID id,
      @RequestBody TicketRequestDTO ticketRequest)
      throws NoSuchTicketException, BadTicketStatusException {
    Optional<TicketResponseDTO> updatedTicket = ticketService.updateTicket(id, ticketRequest);
    return updatedTicket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

  }

}
