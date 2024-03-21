package com.reqserv.requestservice.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInRequestDTO(

    @NotBlank(message = "Имя пользователя не может быть пустыми")
    String username,

    @NotBlank(message = "Пароль не может быть пустыми")
    String password

) {

}
