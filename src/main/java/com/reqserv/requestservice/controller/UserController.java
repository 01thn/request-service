package com.reqserv.requestservice.controller;

import com.reqserv.requestservice.dto.UserRequestDTO;
import com.reqserv.requestservice.dto.UserResponseDTO;
import com.reqserv.requestservice.model.Role;
import com.reqserv.requestservice.service.UserService;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "4") int size) {
    return ResponseEntity.ok(userService.getAllUsers(PageRequest.of(page, size)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
    Optional<UserResponseDTO> user = userService.getUserById(id);
    return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}/roles")
  public ResponseEntity<UserResponseDTO> updateUserRoles(
      @PathVariable UUID id,
      @RequestBody Set<Role> roles) {
    Optional<UserResponseDTO> updatedUser = userService.updateUserRoles(id, roles);
    return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

}
