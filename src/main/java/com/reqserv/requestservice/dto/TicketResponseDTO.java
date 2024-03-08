package com.reqserv.requestservice.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketResponseDTO {

  private UUID id;
  private String title;
  private String description;
  private String status;

}
