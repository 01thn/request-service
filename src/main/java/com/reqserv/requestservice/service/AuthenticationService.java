package com.reqserv.requestservice.service;

import com.reqserv.requestservice.dto.LoginResponseDTO;
import com.reqserv.requestservice.dto.SignInRequestDTO;
import com.reqserv.requestservice.dto.SignUpRequestDTO;
import com.reqserv.requestservice.exception.UserAlreadyExists;
import com.reqserv.requestservice.model.Role;
import com.reqserv.requestservice.model.User;
import com.reqserv.requestservice.security.JwtService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserService userService;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  public LoginResponseDTO signUp(SignUpRequestDTO request) throws UserAlreadyExists {

    var user = User.builder()
        .username(request.username().toLowerCase())
        .email(request.email())
        .password(passwordEncoder.encode(request.password()))
        .firstName(request.firstName())
        .lastName(request.lastName())
        .phone(request.phone())
        .roles(Set.of(Role.ROLE_USER))
        .build();

    userService.create(user);

    var jwt = jwtService.generateToken(user);
    return new LoginResponseDTO(jwt);
  }

  public LoginResponseDTO signIn(SignInRequestDTO request) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        request.username().toLowerCase(),
        request.password()
    ));

    var user = userService
        .userDetailsService()
        .loadUserByUsername(request.username().toLowerCase());

    var jwt = jwtService.generateToken(user);
    return new LoginResponseDTO(jwt);
  }

}
