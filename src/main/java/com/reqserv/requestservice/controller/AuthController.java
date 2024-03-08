package com.reqserv.requestservice.controller;

import com.reqserv.requestservice.dto.UserRequestDTO;
import com.reqserv.requestservice.dto.UserResponseDTO;
import com.reqserv.requestservice.exception.UserAlreadyExists;
import com.reqserv.requestservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private UserService userService;

  @PostMapping("/register")
  public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO user)
      throws UserAlreadyExists {

    return ResponseEntity.ok(userService.registerUser(user));
  }

  @PostMapping("/login")
  public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) throws Exception {
    String token = userService.loginUser(username, password);
    return ResponseEntity.ok(token);
  }
}
