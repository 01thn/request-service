package com.reqserv.requestservice.model;

import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  @Column("id")
  private UUID id;

  @Column("username")
  private String username;

  @Column("password")
  private String password;

  @Column("first_name")
  private String firstName;

  @Column("last_name")
  private String lastName;

  @Column("phone")
  private String phone;

  @Column("email")
  private String email;

  @Column("roles")
  private Set<Role> roles;

}
