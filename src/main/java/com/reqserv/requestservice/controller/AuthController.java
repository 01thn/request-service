package com.reqserv.requestservice.controller;

import com.reqserv.requestservice.dto.LoginResponseDTO;
import com.reqserv.requestservice.dto.SignInRequestDTO;
import com.reqserv.requestservice.dto.SignUpRequestDTO;
import com.reqserv.requestservice.exception.UserAlreadyExists;
import com.reqserv.requestservice.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth API")
public class AuthController {

  private final AuthenticationService authenticationService;

  @Operation(summary = "Sign up for new user")
  @PostMapping("/sign-up")
  public LoginResponseDTO signUp(@RequestBody @Valid SignUpRequestDTO request)
      throws UserAlreadyExists {
    return authenticationService.signUp(request);
  }

  @Operation(summary = "Sign in for exisiting user")
  @PostMapping("/sign-in")
  public LoginResponseDTO signIn(@RequestBody @Valid SignInRequestDTO request) {
    return authenticationService.signIn(request);
  }

}
