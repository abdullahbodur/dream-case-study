package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT u FROM User u WHERE u.nickname = ?1")
  Optional<User> findByUsername(String username);

  @Query("SELECT u FROM User u WHERE u.email = ?1")
  Optional<User> findByEmail(String email);
}
