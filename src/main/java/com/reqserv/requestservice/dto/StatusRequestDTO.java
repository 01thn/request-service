package com.reqserv.requestservice.dto;

public enum StatusRequestDTO {
  DRAFT("Draft"),
  SENT("Sent");

  private final String displayName;

  StatusRequestDTO(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
