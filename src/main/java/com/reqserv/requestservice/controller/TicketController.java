package com.reqserv.requestservice.controller;

import com.reqserv.requestservice.controller.helpers.SortOrder;
import com.reqserv.requestservice.dto.TicketRequestDTO;
import com.reqserv.requestservice.dto.TicketResponseDTO;
import com.reqserv.requestservice.exception.BadTicketStatusException;
import com.reqserv.requestservice.exception.NoSuchTicketException;
import com.reqserv.requestservice.model.Status;
import com.reqserv.requestservice.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Ticket", description = "Ticket API")
public class TicketController {

  private final TicketService ticketService;

  private static final Integer DEFAULT_PAGE_SIZE = 5;

  @Operation(summary = "Get all tickets with pagination", description = "Returns all tickets")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully found"),
      @ApiResponse(responseCode = "400", description = "Something went wrong"),
      @ApiResponse(responseCode = "403", description = "Access denied"),
      @ApiResponse(responseCode = "404", description = "Not found"),
      @ApiResponse(responseCode = "500", description = "Server exception")
  })
  @GetMapping
  public ResponseEntity<Page<TicketResponseDTO>> getAllTickets(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam SortOrder sortingOrder) {
    PageRequest pageRequest = PageRequest.of(page, DEFAULT_PAGE_SIZE);
    if (SortOrder.ASC.equals(sortingOrder)) {
      pageRequest = pageRequest.withSort(Sort.by("updatedAt").ascending());
    } else if (SortOrder.DESC.equals(sortingOrder)) {
      pageRequest = pageRequest.withSort(Sort.by("updatedAt").descending());
    }
    return ResponseEntity.ok(ticketService.getAllTickets(pageRequest));
  }

  @Operation(summary = "Get ticket by id", description = "Returns ticket")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully found"),
      @ApiResponse(responseCode = "400", description = "Something went wrong"),
      @ApiResponse(responseCode = "403", description = "Access denied"),
      @ApiResponse(responseCode = "404", description = "Not found"),
      @ApiResponse(responseCode = "500", description = "Server exception")
  })
  @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
  @GetMapping("/{id}")
  public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable UUID id) {
    Optional<TicketResponseDTO> ticket = ticketService.getTicketById(id);
    return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Save new ticket", description = "Creates a new ticket")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successfully saved"),
      @ApiResponse(responseCode = "400", description = "Something went wrong"),
      @ApiResponse(responseCode = "403", description = "Access denied"),
      @ApiResponse(responseCode = "404", description = "Not found"),
      @ApiResponse(responseCode = "500", description = "Server exception")
  })
  @PreAuthorize("hasRole('ROLE_USER')")
  @PostMapping
  public ResponseEntity<TicketResponseDTO> saveTicket(@RequestBody TicketRequestDTO ticket) {
    return new ResponseEntity<>(ticketService.saveTicket(ticket), HttpStatus.CREATED);
  }

  @Operation(summary = "Delete ticket by id", description = "Deletes ticket")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successfully deleted"),
      @ApiResponse(responseCode = "400", description = "Something went wrong"),
      @ApiResponse(responseCode = "403", description = "Access denied"),
      @ApiResponse(responseCode = "404", description = "Not found"),
      @ApiResponse(responseCode = "500", description = "Server exception")
  })
  @PreAuthorize("hasRole('ROLE_USER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTicket(@PathVariable UUID id)
      throws IllegalAccessException, NoSuchTicketException {
    ticketService.deleteTicket(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Change ticket status", description = "Changes ticket status")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successfully changed"),
      @ApiResponse(responseCode = "400", description = "Something went wrong"),
      @ApiResponse(responseCode = "403", description = "Access denied"),
      @ApiResponse(responseCode = "404", description = "Not found"),
      @ApiResponse(responseCode = "500", description = "Server exception")
  })
  @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_USER')")
  @PutMapping("/{id}/status")
  public ResponseEntity<TicketResponseDTO> updateTicketStatus(@PathVariable UUID id,
      @RequestParam Status status)
      throws IllegalAccessException, NoSuchTicketException {
    return ResponseEntity.ok(ticketService.updateTicketStatus(id, status));
  }

  @Operation(summary = "Update ticket", description = "Updates ticket")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successfully changed"),
      @ApiResponse(responseCode = "400", description = "Something went wrong"),
      @ApiResponse(responseCode = "403", description = "Access denied"),
      @ApiResponse(responseCode = "404", description = "Not found"),
      @ApiResponse(responseCode = "500", description = "Server exception")
  })
  @PreAuthorize("hasRole('ROLE_USER')")
  @PutMapping("/{id}")
  public ResponseEntity<TicketResponseDTO> updateTicket(@PathVariable UUID id,
      @RequestBody TicketRequestDTO ticketRequest)
      throws NoSuchTicketException, BadTicketStatusException, IllegalAccessException {
    return ResponseEntity.ok(ticketService.updateTicket(id, ticketRequest));

  }

  @Operation(summary = "Get sent tickets by username with pagination", description = "Returns sent tickets by username")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully found"),
      @ApiResponse(responseCode = "400", description = "Something went wrong"),
      @ApiResponse(responseCode = "403", description = "Access denied"),
      @ApiResponse(responseCode = "404", description = "Not found"),
      @ApiResponse(responseCode = "500", description = "Server exception")
  })
  @PreAuthorize("hasRole('ROLE_OPERATOR')")
  @GetMapping("/by-username")
  public ResponseEntity<Page<TicketResponseDTO>> getSentTicketsByUsername(
      @RequestParam(defaultValue = "0") int page,
      String username,
      @RequestParam SortOrder sortingOrder) {
    PageRequest pageRequest = PageRequest.of(page, DEFAULT_PAGE_SIZE);
    if (SortOrder.ASC.equals(sortingOrder)) {
      pageRequest = pageRequest.withSort(Sort.by("updatedAt").ascending());
    } else if (SortOrder.DESC.equals(sortingOrder)) {
      pageRequest = pageRequest.withSort(Sort.by("updatedAt").descending());
    }
    return ResponseEntity.ok(
        ticketService.getSentTicketsByAuthorUsernameContains(pageRequest, username));
  }

}
