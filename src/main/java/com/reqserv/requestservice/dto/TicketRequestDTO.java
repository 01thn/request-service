package com.reqserv.requestservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketRequestDTO {

  private String title;
  private String description;
  private String status;

}
