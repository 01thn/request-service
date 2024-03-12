package com.reqserv.requestservice.dto;

import java.util.UUID;

public record TicketResponseDTO(

    UUID id,
    String title,
    String description,
    String status

) {

}
