package com.reqserv.requestservice.service;

import com.reqserv.requestservice.dto.UserResponseDTO;
import com.reqserv.requestservice.dto.mapper.UserMapper;
import com.reqserv.requestservice.model.Role;
import com.reqserv.requestservice.repository.UserRepository;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

//  private final PasswordEncoder passwordEncoder;

//  private final JwtUtil jwtUtil;

  public Flux<UserResponseDTO> getAllUsers(int limit, int offset) {
    return userRepository.findUsersWithPagination(limit, offset).map(userMapper::userToResponseDTO);
  }

  public Mono<UserResponseDTO> getUserById(UUID id) {
    return userRepository.findById(id).map(userMapper::userToResponseDTO);
  }

  public Mono<Void> deleteUser(UUID id) {
    return userRepository.deleteById(id);
  }

  public Mono<Void> updateUserRoles(UUID userId, Set<Role> roles) {
    return userRepository.updateUserRolesById(userId, roles);
  }

//  public UserResponseDTO registerUser(UserRequestDTO user) throws UserAlreadyExists {
//    Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
//    if (byUsername.isPresent()) {
//      throw new UserAlreadyExists("User with such username already exists");
//    }
//    user.setPassword(passwordEncoder.encode(user.getPassword()));
//    return userMapper.userToResponseDTO(userRepository.save(userMapper.requestDTOToUser(user)));
//  }
//
//  public String loginUser(String username, String password) throws Exception {
//    User user = userRepository.findByUsername(username)
//        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//    if (!passwordEncoder.matches(password, user.getPassword())) {
//      throw new Exception("Invalid username or password");
//    }
//
//    return jwtUtil.generateToken(username);
//  }

}
