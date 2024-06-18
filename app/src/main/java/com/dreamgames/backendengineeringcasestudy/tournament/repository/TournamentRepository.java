package com.dreamgames.backendengineeringcasestudy.tournament.repository;

import com.dreamgames.backendengineeringcasestudy.tournament.entity.Tournament;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The TournamentRepository interface extends JpaRepository and provides methods to interact with the Tournament entities in the database.
 * It includes a custom query to find an active Tournament entity.
 */
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

  /**
   * This method retrieves an active Tournament entity.
   * A tournament is considered active if it is not completed and its start time is less than or equal to the current timestamp.
   *
   * @return An Optional containing the active Tournament entity, or an empty Optional if no active tournament is found.
   */
  @Query("SELECT t FROM Tournament t WHERE t.isCompleted = false AND t.startTime <= CURRENT_TIMESTAMP")
  Optional<Tournament> findActiveTournament();
}