package com.reqserv.requestservice.dto;

import java.util.UUID;

public record UserResponseDTO(

    UUID id,
    String username,
    String phone,
    String email,
    String firstName,
    String lastName
) {

}
