package com.reqserv.requestservice.dto;

public record UserRequestDTO(

    String username,
    String password,
    String phone,
    String email,
    String firstName,
    String lastName

) {

}
