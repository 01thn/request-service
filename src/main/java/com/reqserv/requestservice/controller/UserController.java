package com.reqserv.requestservice.controller;

import com.reqserv.requestservice.dto.UserResponseDTO;
import com.reqserv.requestservice.model.Role;
import com.reqserv.requestservice.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController {

  private final UserService userService;

  @GetMapping
  public Flux<UserResponseDTO> getAllUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "4") int size) {
    return userService.getAllUsers(page, size);
  }

  @GetMapping("/{id}")
  public Mono<UserResponseDTO> getUserById(@PathVariable UUID id) {
    return userService.getUserById(id);
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteUser(@PathVariable UUID id) {
    return userService.deleteUser(id);
  }

  @PutMapping("/{id}/roles")
  public Mono<Void> updateUserRoles(
      @PathVariable UUID id,
      @RequestBody Set<Role> roles) {
    return userService.updateUserRoles(id, roles);
  }

}
