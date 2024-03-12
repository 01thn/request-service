package com.reqserv.requestservice.service;

import com.reqserv.requestservice.dto.TicketRequestDTO;
import com.reqserv.requestservice.dto.TicketResponseDTO;
import com.reqserv.requestservice.dto.mapper.TicketMapper;
import com.reqserv.requestservice.exception.BadTicketStatusException;
import com.reqserv.requestservice.exception.NoSuchTicketException;
import com.reqserv.requestservice.model.Status;
import com.reqserv.requestservice.model.Ticket;
import com.reqserv.requestservice.repository.TicketRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository ticketRepository;

  private final TicketMapper ticketMapper;

  public Page<TicketResponseDTO> getAllTickets(Pageable pageable) {
    return ticketRepository.findAll(pageable).map(ticketMapper::ticketToResponseDTO);
  }

  public Optional<TicketResponseDTO> getTicketById(UUID id) {
    return ticketRepository.findById(id).map(ticketMapper::ticketToResponseDTO);
  }

  public TicketResponseDTO saveTicket(TicketRequestDTO ticket) {
    return ticketMapper.ticketToResponseDTO(ticketRepository.save(ticketMapper.requestDTOToTicket(ticket)));
  }

  public void deleteTicket(UUID id) {
    ticketRepository.deleteById(id);
  }

  public Optional<TicketResponseDTO> updateTicketStatus(UUID ticketId, Status status) {
    return ticketRepository.updateTicketStatusById(ticketId, status).map(ticketMapper::ticketToResponseDTO);
  }

  public Optional<TicketResponseDTO> updateTicket(UUID ticketId, TicketRequestDTO ticketRequest)
      throws BadTicketStatusException, NoSuchTicketException {
    Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
    if (optionalTicket.isPresent()) {
      Ticket ticket = optionalTicket.get();
      if (ticket.getStatus() == Status.DRAFT) {
        ticket.setTitle(ticketRequest.title());
        ticket.setDescription(ticketRequest.description());
        Ticket updatedTicket = ticketRepository.save(ticket);
        return Optional.of(ticketMapper.ticketToResponseDTO(updatedTicket));
      } else {
        throw new BadTicketStatusException("Ticket was already sent");
      }
    } else {
      throw new NoSuchTicketException("Ticket not found");
    }
  }
}
