package com.reqserv.requestservice.model;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  ROLE_USER("ROLE_USER"),
  ROLE_OPERATOR("ROLE_OPERATOR"),
  ROLE_ADMIN("ROLE_ADMIN");

  private final String description;

  Role(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String getAuthority() {
    return name();
  }
}
