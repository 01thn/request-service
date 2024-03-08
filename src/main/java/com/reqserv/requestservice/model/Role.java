package com.reqserv.requestservice.model;


public enum Role {
  USER("User"),
  OPERATOR("Operator"),
  ADMIN("Admin");

  private final String displayName;

  Role(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
