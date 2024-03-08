package com.reqserv.requestservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDTO {

  private String username;
  private String password;
  private String phone;
  private String email;
  private String firstName;
  private String lastName;

}
