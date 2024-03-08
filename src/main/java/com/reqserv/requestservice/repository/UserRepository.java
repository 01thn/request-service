package com.reqserv.requestservice.repository;

import com.reqserv.requestservice.model.Role;
import com.reqserv.requestservice.model.User;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUsername(String username);

  default Optional<User> updateUserRolesById(UUID id, Set<Role> roles) {
    Optional<User> optionalUser = findById(id);
    optionalUser.ifPresent(user -> {
      user.setRoles(roles);
      save(user);
    });
    return optionalUser;
  }

}
