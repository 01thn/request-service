package com.reqserv.requestservice.repository;

import com.reqserv.requestservice.model.Role;
import com.reqserv.requestservice.model.User;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, UUID> {

  @Query("SELECT * FROM users LIMIT :limit OFFSET :offset")
  Flux<User> findUsersWithPagination(@Param("limit") int limit, @Param("offset") int offset);

  @Modifying
  @Query("UPDATE users SET roles = :roles WHERE id = :id")
  Mono<Void> updateUserRolesById(@Param("id") UUID id, @Param("roles") Set<Role> roles);

}
