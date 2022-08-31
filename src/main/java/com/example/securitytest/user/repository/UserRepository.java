package com.example.securitytest.user.repository;

import com.example.securitytest.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  @EntityGraph(attributePaths = {"authorities"})
  Optional<User> findByName(String name);

}
