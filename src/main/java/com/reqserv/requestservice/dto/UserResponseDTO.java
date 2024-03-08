package com.reqserv.requestservice.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {

  private UUID id;
  private String username;
  private String phone;
  private String email;
  private String firstName;
  private String lastName;

}
