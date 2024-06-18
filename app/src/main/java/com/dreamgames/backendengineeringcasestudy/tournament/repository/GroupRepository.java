package com.dreamgames.backendengineeringcasestudy.tournament.repository;

import com.dreamgames.backendengineeringcasestudy.tournament.entity.TournamentGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The GroupRepository interface extends JpaRepository and provides methods to interact with the TournamentGroup entities in the database.
 * It includes custom queries to find TournamentGroup entities based on their readiness and tournament id.
 */
public interface GroupRepository extends JpaRepository<TournamentGroup, Long> {

  /**
   * This method retrieves all TournamentGroup entities that match the provided readiness status.
   *
   * @param isReady The readiness status to match.
   * @return A list of TournamentGroup entities that match the provided readiness status.
   */
  @Query("SELECT g FROM TournamentGroup g WHERE g.isReady = :isReady")
  List<TournamentGroup> findAllByReady(boolean isReady);

  /**
   * This method retrieves all TournamentGroup entities that match the provided tournament id and readiness status.
   *
   * @param tournamentId The tournament id to match.
   * @param isReady The readiness status to match.
   * @return A list of TournamentGroup entities that match the provided tournament id and readiness status.
   */
  @Query("SELECT g FROM TournamentGroup g WHERE g.tournament.id = :tournamentId AND g.isReady = :isReady")
  List<TournamentGroup> findAllByTournamentIdAndReady(Long tournamentId, boolean isReady);
}