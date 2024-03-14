package com.reqserv.requestservice.controller;

import com.reqserv.requestservice.controller.helpers.SortOrder;
import com.reqserv.requestservice.dto.UserResponseDTO;
import com.reqserv.requestservice.model.Role;
import com.reqserv.requestservice.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User", description = "User API")
public class UserController {

  private final UserService userService;

  private static final Integer DEFAULT_PAGE_SIZE = 5;

  @GetMapping
  public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam SortOrder sortingOrder) {
    PageRequest pageRequest = PageRequest.of(page, DEFAULT_PAGE_SIZE);
    if (SortOrder.ASC.equals(sortingOrder)) {
      pageRequest = pageRequest.withSort(Sort.by("updatedAt").ascending());
    } else if (SortOrder.DESC.equals(sortingOrder)) {
      pageRequest = pageRequest.withSort(Sort.by("updatedAt").descending());
    }
    return ResponseEntity.ok(userService.getAllUsers(pageRequest));
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
