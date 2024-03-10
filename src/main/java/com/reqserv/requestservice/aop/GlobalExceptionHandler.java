package com.reqserv.requestservice.aop;

import com.reqserv.requestservice.exception.BadTicketStatusException;
import com.reqserv.requestservice.exception.NoSuchTicketException;
import com.reqserv.requestservice.exception.UserAlreadyExists;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
@Order(-2)
public class GlobalExceptionHandler {

  @ExceptionHandler(BadTicketStatusException.class)
  public Mono<ResponseEntity<String>> handleBadTicketStatusException(BadTicketStatusException ex) {
    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
  }

  @ExceptionHandler(NoSuchTicketException.class)
  public Mono<ResponseEntity<String>> handleNoSuchTicketException(NoSuchTicketException ex) {
    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
  }

  @ExceptionHandler(UserAlreadyExists.class)
  public Mono<ResponseEntity<String>> handleUserAlreadyExists(UserAlreadyExists ex) {
    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
  }

}
