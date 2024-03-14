package com.reqserv.requestservice.service;

import com.reqserv.requestservice.dto.TicketRequestDTO;
import com.reqserv.requestservice.dto.TicketResponseDTO;
import com.reqserv.requestservice.dto.UserResponseDTO;
import com.reqserv.requestservice.dto.mapper.TicketMapper;
import com.reqserv.requestservice.exception.BadTicketStatusException;
import com.reqserv.requestservice.exception.NoSuchTicketException;
import com.reqserv.requestservice.model.Role;
import com.reqserv.requestservice.model.Status;
import com.reqserv.requestservice.model.Ticket;
import com.reqserv.requestservice.model.User;
import com.reqserv.requestservice.repository.TicketRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository ticketRepository;

  private final UserService userService;

  private final TicketMapper ticketMapper;

  public Page<TicketResponseDTO> getAllTickets(Pageable pageable) {
    User currentUser = userService.getCurrentUser();
    if (currentUser.getRoles().contains(Role.OPERATOR) || currentUser.getRoles()
        .contains(Role.ADMIN)) {
      return ticketRepository.findAll(pageable).map(ticketMapper::ticketToResponseDTO);
    }
    return ticketRepository.findAllByAuthor(pageable, currentUser)
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

  public void deleteTicket(UUID id) throws IllegalAccessException, NoSuchTicketException {
    Optional<TicketResponseDTO> ticketById = getTicketById(id);
    User currentUser = userService.getCurrentUser();
    if (ticketById.isEmpty()){
      throw new NoSuchTicketException("Ticket not found");
    }
    if (!checkSameUserAuthor(ticketById.get().author(), currentUser)){
      throw new IllegalAccessException();
    }
    ticketById.map(TicketResponseDTO::status).filter(it -> it.equals(Status.DRAFT))
        .orElseThrow(() -> new IllegalAccessException("Cannot delete not draft ticket"));
    ticketRepository.deleteById(id);
  }

  public Optional<TicketResponseDTO> updateTicketStatus(UUID ticketId, Status status)
      throws IllegalAccessException, NoSuchTicketException {
    Optional<TicketResponseDTO> ticketById = getTicketById(ticketId);

    if (ticketById.isEmpty()) {
      throw new NoSuchTicketException("Ticket not found");
    }

    User currentUser = userService.getCurrentUser();
    if (!canUpdateStatusByRole(currentUser, status, ticketById.get())) {
      throw new IllegalAccessException("Cannot change status");
    }

    if (currentUser.getRoles().contains(Role.OPERATOR)) {
      return ticketRepository.updateTicketStatusAndOperatorById(ticketId, status, currentUser)
          .map(ticketMapper::ticketToResponseDTO);
    } else {
      return ticketRepository.updateTicketStatusById(ticketId, status)
          .map(ticketMapper::ticketToResponseDTO);
    }
  }

  private boolean canUpdateStatusByRole(User user, Status status,
      TicketResponseDTO originalTicket) {
    Set<Role> roles = user.getRoles();
    if (roles.contains(Role.OPERATOR) || roles.contains(Role.ADMIN)) {
      return true;
    }
    return (Status.SENT.equals(status) || Status.DRAFT.equals(status)) && checkSameUserAuthor(
        originalTicket.author(), user);
  }

  public Optional<TicketResponseDTO> updateTicket(UUID ticketId, TicketRequestDTO ticketRequest)
      throws BadTicketStatusException, NoSuchTicketException, IllegalAccessException {
    Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
    User currentUser = userService.getCurrentUser();
    if (optionalTicket.isPresent()) {
      Ticket ticket = optionalTicket.get();
      if (!checkSameUserAuthor(ticket.getAuthor(), currentUser)){
        throw new IllegalAccessException();
      }
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

  private boolean checkSameUserAuthor(UserResponseDTO originalTicketAuthor, User editor) {
    return originalTicketAuthor.username().equals(editor.getUsername());
  }

  private boolean checkSameUserAuthor(User originalTicketAuthor, User editor) {
    return originalTicketAuthor.getUsername().equals(editor.getUsername());
  }

}
