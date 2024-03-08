package com.reqserv.requestservice.model;

public enum Status {
  DRAFT("Draft"),
  SENT("Sent"),
  ACCEPTED("Accepted"),
  REJECTED("Rejected");

  private final String displayName;

  Status(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
