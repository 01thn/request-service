package com.reqserv.requestservice.service;

import com.reqserv.requestservice.dto.UserResponseDTO;
import com.reqserv.requestservice.dto.mapper.UserMapper;
import com.reqserv.requestservice.model.Role;
import com.reqserv.requestservice.model.User;
import com.reqserv.requestservice.repository.UserRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable).map(userMapper::userToResponseDTO);
  }

  public Optional<UserResponseDTO> getUserById(UUID id) {
    return userRepository.findById(id).map(userMapper::userToResponseDTO);
  }

  public Optional<UserResponseDTO> updateUserRoles(UUID userId, Set<Role> roles) {
    return userRepository.updateUserRolesById(userId, roles).map(userMapper::userToResponseDTO);
  }

  private User getByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  public UserDetailsService userDetailsService() {
    return this::getByUsername;
  }

  public User getCurrentUser() {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    return getByUsername(username);
  }

}
