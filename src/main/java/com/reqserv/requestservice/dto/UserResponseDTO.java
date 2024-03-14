package com.reqserv.requestservice.dto;

import com.reqserv.requestservice.model.Role;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponseDTO(

    UUID id,
    String username,
    String phone,
    String email,
    String firstName,
    String lastName,
    Set<Role> roles,
    ZonedDateTime registeredAt

) {

}
