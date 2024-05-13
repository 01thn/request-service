package com.reqserv.requestservice.service;

import com.reqserv.requestservice.annotation.LogExecutionTime;
import com.reqserv.requestservice.dto.TicketRequestDTO;
import com.reqserv.requestservice.dto.TicketResponseDTO;
import com.reqserv.requestservice.dto.mapper.TicketMapper;
import com.reqserv.requestservice.exception.BadTicketStatusException;
import com.reqserv.requestservice.exception.NoSuchTicketException;
import com.reqserv.requestservice.model.Role;
import com.reqserv.requestservice.model.Status;
import com.reqserv.requestservice.model.Ticket;
import com.reqserv.requestservice.model.User;
import com.reqserv.requestservice.repository.TicketRepository;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@LogExecutionTime
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository ticketRepository;

  private final UserService userService;

  private final TicketMapper ticketMapper;

  public Page<TicketResponseDTO> getAllTickets(Pageable pageable) {
    User currentUser = userService.getCurrentUser();
    Set<Status> allowedStatuses;

    if (currentUser.getRoles().contains(Role.ROLE_ADMIN)) {
      allowedStatuses = EnumSet.of(Status.SENT, Status.ACCEPTED, Status.REJECTED);
    } else if (currentUser.getRoles().contains(Role.ROLE_OPERATOR)) {
      allowedStatuses = EnumSet.of(Status.SENT);
    } else {
      return ticketRepository.findAllByAuthor(pageable, currentUser)
          .map(ticketMapper::ticketToResponseDTO);
    }

    return ticketRepository.findAllByStatusIn(pageable, allowedStatuses)
        .map(ticketMapper::ticketToResponseDTO);
  }

  public Optional<TicketResponseDTO> getTicketById(UUID id) {
    return ticketRepository.findById(id).map(ticketMapper::ticketToResponseDTO);
  }

  public TicketResponseDTO saveTicket(TicketRequestDTO ticket) {
    User currentUser = userService.getCurrentUser();
    var ticketFromRequest = ticketMapper.requestDTOToTicket(ticket);
    ticketFromRequest.setAuthor(currentUser);
    ticketFromRequest.setStatus(Status.DRAFT);
    return ticketMapper.ticketToResponseDTO(ticketRepository.save(ticketFromRequest));
  }

  public void deleteTicket(UUID id) throws NoSuchTicketException, IllegalAccessException {
    Ticket ticket = ticketRepository.findById(id)
        .orElseThrow(() -> new NoSuchTicketException("Ticket not found"));

    User currentUser = userService.getCurrentUser();
    if (!checkNotSameUserAuthor(ticket.getAuthor(), currentUser)) {
      throw new IllegalAccessException();
    }

    if (ticket.getStatus() != Status.DRAFT) {
      throw new IllegalAccessException("Cannot delete non-draft ticket");
    }

    ticketRepository.deleteById(id);
  }

  public TicketResponseDTO updateTicketStatus(UUID ticketId, Status status)
      throws NoSuchTicketException, IllegalAccessException {
    Ticket ticket = ticketRepository.findById(ticketId)
        .orElseThrow(() -> new NoSuchTicketException("Ticket not found"));

    User currentUser = userService.getCurrentUser();
    if (!canUpdateStatusByRole(currentUser, status, ticket)) {
      throw new IllegalAccessException("Cannot change status");
    }

    if (currentUser.getRoles().contains(Role.ROLE_OPERATOR)) {
      ticket = ticketRepository.updateTicketStatusAndOperatorById(ticketId, status, currentUser)
          .orElseThrow(() -> new NoSuchTicketException("Ticket not found"));
    } else {
      ticket = ticketRepository.updateTicketStatusById(ticketId, status)
          .orElseThrow(() -> new NoSuchTicketException("Ticket not found"));
    }

    return ticketMapper.ticketToResponseDTO(ticket);
  }


  public TicketResponseDTO updateTicket(UUID ticketId, TicketRequestDTO ticketRequest)
      throws BadTicketStatusException, NoSuchTicketException, IllegalAccessException {
    Ticket ticket = ticketRepository.findById(ticketId)
        .orElseThrow(() -> new NoSuchTicketException("Ticket not found"));

    User currentUser = userService.getCurrentUser();
    if (checkNotSameUserAuthor(ticket.getAuthor(), currentUser)) {
      throw new IllegalAccessException();
    }

    if (ticket.getStatus() != Status.DRAFT) {
      throw new BadTicketStatusException("Ticket was already sent");
    }

    ticket.setTitle(ticketRequest.title());
    ticket.setDescription(ticketRequest.description());

    Ticket updatedTicket = ticketRepository.save(ticket);
    return ticketMapper.ticketToResponseDTO(updatedTicket);
  }

  public Page<TicketResponseDTO> getSentTicketsByAuthorUsernameContains(Pageable pageable,
      String username) {
    return ticketRepository.findByAuthorUsernameContainingIgnoreCaseAndStatus(pageable,
        username, Status.SENT).map(
        ticketMapper::ticketToResponseDTO);
  }

  private boolean canUpdateStatusByRole(User user, Status status,
      Ticket originalTicket) {
    Set<Role> roles = user.getRoles();
    if (roles.contains(Role.ROLE_ADMIN)) {
      return true;
    }
    if (roles.contains(Role.ROLE_OPERATOR)) {
      return (Status.ACCEPTED.equals(status) || Status.REJECTED.equals(status));
    }
    return (Status.SENT.equals(status) || Status.DRAFT.equals(status)) && !checkNotSameUserAuthor(
        originalTicket.getAuthor(), user);
  }

  private boolean checkNotSameUserAuthor(User originalTicketAuthor, User editor) {
    return !originalTicketAuthor.getUsername().equals(editor.getUsername());
  }

}
