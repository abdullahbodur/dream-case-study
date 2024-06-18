package com.dreamgames.backendengineeringcasestudy.tournament.repository;

import com.dreamgames.backendengineeringcasestudy.tournament.entity.Reward;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The RewardRepository interface extends JpaRepository and provides methods to interact with the Reward entities in the database.
 * It includes custom queries to find Reward entities based on their tournament id, user id, and claim status.
 */
public interface RewardRepository extends JpaRepository<Reward, Long> {

  /**
   * This method retrieves a Reward entity that matches the provided tournament id and user id.
   *
   * @param tournamentId The tournament id to match.
   * @param userId The user id to match.
   * @return An Optional containing the Reward entity that matches the provided tournament id and user id, or an empty Optional if no match is found.
   */
  @Query("SELECT r FROM Reward r WHERE r.tournament.id = ?1 AND r.user.id = ?2")
  Optional<Reward> findByTournamentIdAndUserId(Long tournamentId, Long userId);

  /**
   * This method retrieves all Reward entities that match the provided user id and claim status.
   *
   * @param userId The user id to match.
   * @param isClaimed The claim status to match.
   * @return An Optional containing the Reward entities that match the provided user id and claim status, or an empty Optional if no match is found.
   */
  @Query("SELECT r FROM Reward r WHERE r.user.id = ?1 AND r.isClaimed = ?2")
  Optional<Reward> findAllByUserIdAndClaimed(Long userId, boolean isClaimed);
}