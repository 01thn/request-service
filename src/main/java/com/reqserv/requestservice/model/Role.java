package com.reqserv.requestservice.model;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  ROLE_USER,
  ROLE_OPERATOR,
  ROLE_ADMIN;

  @Override
  public String getAuthority() {
    return name();
  }

}
