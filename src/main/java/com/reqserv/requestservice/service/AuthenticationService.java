package com.reqserv.requestservice.service;

import com.reqserv.requestservice.dto.LoginResponseDTO;
import com.reqserv.requestservice.dto.SignInRequestDTO;
import com.reqserv.requestservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserService userService;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public LoginResponseDTO signIn(SignInRequestDTO request) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        request.username(),
        request.password()
    ));

    var user = userService
        .userDetailsService()
        .loadUserByUsername(request.username());

    var jwt = jwtService.generateToken(user);
    return new LoginResponseDTO(jwt);
  }

}
