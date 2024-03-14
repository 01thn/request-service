package com.reqserv.requestservice.dto;

import com.reqserv.requestservice.model.Status;
import java.util.UUID;

public record TicketResponseDTO(

    UUID id,
    String title,
    String description,
    Status status,

    UserResponseDTO author,

    UserResponseDTO operator

) {

}
