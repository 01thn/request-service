package com.reqserv.requestservice.service;

import com.reqserv.requestservice.dto.UserRequestDTO;
import com.reqserv.requestservice.dto.UserResponseDTO;
import com.reqserv.requestservice.dto.mapper.UserMapper;
import com.reqserv.requestservice.exception.UserAlreadyExists;
import com.reqserv.requestservice.model.Role;
import com.reqserv.requestservice.model.User;
import com.reqserv.requestservice.repository.UserRepository;
import com.reqserv.requestservice.security.JwtUtil;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  private final PasswordEncoder passwordEncoder;

  private final JwtUtil jwtUtil;

  public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable).map(userMapper::userToResponseDTO);
  }

  public Optional<UserResponseDTO> getUserById(UUID id) {
    return userRepository.findById(id).map(userMapper::userToResponseDTO);
  }

  public void deleteUser(UUID id) {
    userRepository.deleteById(id);
  }

  public Optional<UserResponseDTO> updateUserRoles(UUID userId, Set<Role> roles) {
    return userRepository.updateUserRolesById(userId, roles).map(userMapper::userToResponseDTO);
  }

  public UserResponseDTO registerUser(UserRequestDTO user) throws UserAlreadyExists {
    Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
    if (byUsername.isPresent()) {
      throw new UserAlreadyExists("User with such username already exists");
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userMapper.userToResponseDTO(userRepository.save(userMapper.requestDTOToUser(user)));
  }

  public String loginUser(String username, String password) throws Exception {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new Exception("Invalid username or password");
    }

    return jwtUtil.generateToken(username);
  }

}
