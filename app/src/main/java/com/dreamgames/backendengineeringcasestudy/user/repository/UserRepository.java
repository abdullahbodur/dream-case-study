package com.dreamgames.backendengineeringcasestudy.user.repository;

import com.dreamgames.backendengineeringcasestudy.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The UserRepository interface extends JpaRepository and provides methods to interact with the User entities in the database.
 * It includes a custom query to find a User entity based on their email.
 */
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * This method retrieves a User entity that matches the provided email.
   *
   * @param email The email to match.
   * @return An Optional containing the User entity that matches the provided email, or an empty Optional if no match is found.
   */
  @Query("SELECT u FROM User u WHERE u.email = ?1")
  Optional<User> findByEmail(String email);
}