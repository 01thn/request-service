package com.reqserv.requestservice.model;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "tickets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

  @Id
  @Column("id")
  private UUID id;

  @Column("title")
  private String title;

  @Column("description")
  private String description;

  @Column("author_id")
  private UUID authorId;

  @Column("status")
  private Status status;

  @Column("operator_id")
  private UUID operatorId;

  @Column("created_at")
  private ZonedDateTime createdAt;

  @Column("updated_at")
  private ZonedDateTime updatedAt;

}
