package com.reqserv.requestservice.dto;

import com.reqserv.requestservice.model.Status;
import java.time.ZonedDateTime;
import java.util.UUID;

public record TicketResponseDTO(

    UUID id,
    String title,
    String description,
    Status status,
    UserResponseDTO author,
    UserResponseDTO operator,
    ZonedDateTime createdAt,
    ZonedDateTime updatedAt

) {

}
