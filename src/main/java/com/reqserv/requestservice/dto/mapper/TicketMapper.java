package com.reqserv.requestservice.dto.mapper;

import com.reqserv.requestservice.dto.TicketRequestDTO;
import com.reqserv.requestservice.dto.TicketResponseDTO;
import com.reqserv.requestservice.model.Ticket;
import org.mapstruct.Mapper;

@Mapper
public interface TicketMapper {

  TicketResponseDTO ticketToResponseDTO(Ticket ticket);

  Ticket requestDTOToTicket(TicketRequestDTO ticketRequestDTO);

}
