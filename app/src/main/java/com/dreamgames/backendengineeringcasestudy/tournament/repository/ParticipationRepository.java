package com.dreamgames.backendengineeringcasestudy.tournament.repository;

import com.dreamgames.backendengineeringcasestudy.tournament.entity.Participation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The ParticipationRepository interface extends JpaRepository and provides methods to interact with the Participation entities in the database.
 * It includes custom queries to find Participation entities based on their group id and user id.
 */
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

  /**
   * This method retrieves a Participation entity that matches the provided group id and user id.
   *
   * @param groupId The group id to match.
   * @param userId The user id to match.
   * @return An Optional containing the Participation entity that matches the provided group id and user id, or an empty Optional if no match is found.
   */
  @Query("SELECT p FROM Participation p WHERE p.group.id = ?1 AND p.user.id = ?2")
  Optional<Participation> findByGroupIdAndUserId(Long groupId, Long userId);

}