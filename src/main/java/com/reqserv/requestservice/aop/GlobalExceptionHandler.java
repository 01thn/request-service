package com.reqserv.requestservice.aop;

import com.reqserv.requestservice.exception.BadTicketStatusException;
import com.reqserv.requestservice.exception.NoSuchTicketException;
import com.reqserv.requestservice.exception.UserAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadTicketStatusException.class)
  public ResponseEntity<String> handleBadTicketStatusException(BadTicketStatusException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(NoSuchTicketException.class)
  public ResponseEntity<String> handleNoSuchTicketException(NoSuchTicketException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(UserAlreadyExists.class)
  public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExists ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
}
