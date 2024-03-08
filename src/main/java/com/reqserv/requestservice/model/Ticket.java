package com.reqserv.requestservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;
  private String title;
  private String description;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private Status status;

  @ManyToOne
  @JoinColumn(name = "operator_id")
  private User operator;

  private ZonedDateTime createdAt;

  private ZonedDateTime updatedAt;

}
