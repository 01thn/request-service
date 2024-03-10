package com.reqserv.requestservice.service;

import com.reqserv.requestservice.dto.TicketRequestDTO;
import com.reqserv.requestservice.dto.TicketResponseDTO;
import com.reqserv.requestservice.dto.mapper.TicketMapper;
import com.reqserv.requestservice.exception.BadTicketStatusException;
import com.reqserv.requestservice.exception.NoSuchTicketException;
import com.reqserv.requestservice.model.Status;
import com.reqserv.requestservice.repository.TicketRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository ticketRepository;

  private final TicketMapper ticketMapper;

  public Flux<TicketResponseDTO> getAllTickets(int limit, int offset) {
    return ticketRepository.findTicketsWithPagination(limit, offset).map(
        ticketMapper::ticketToResponseDTO);
  }

  public Mono<TicketResponseDTO> getTicketById(UUID id) {
    return ticketRepository.findById(id)
        .map(ticketMapper::ticketToResponseDTO);
  }

  public Mono<TicketResponseDTO> saveTicket(TicketRequestDTO ticket) {
    return ticketRepository.save(ticketMapper.requestDTOToTicket(ticket))
        .map(ticketMapper::ticketToResponseDTO);
  }

  public Mono<Void> deleteTicket(UUID id) {
    return ticketRepository.deleteById(id);
  }


  public Mono<TicketResponseDTO> updateTicketStatus(UUID ticketId, Status status) {
    return ticketRepository.updateTicketStatusById(ticketId, status)
        .map(ticketMapper::ticketToResponseDTO);
  }

  public Mono<TicketResponseDTO> updateTicket(UUID ticketId, TicketRequestDTO ticketRequest) {
    return ticketRepository.findById(ticketId)
        .flatMap(ticket -> {
          if (ticket.getStatus() == Status.DRAFT) {
            ticket.setTitle(ticketRequest.getTitle());
            ticket.setDescription(ticketRequest.getDescription());
            return ticketRepository.save(ticket);
          } else {
            return Mono.error(new BadTicketStatusException("Ticket was already sent"));
          }
        })
        .map(ticketMapper::ticketToResponseDTO)
        .switchIfEmpty(Mono.error(new NoSuchTicketException("Ticket not found")));
  }
}
