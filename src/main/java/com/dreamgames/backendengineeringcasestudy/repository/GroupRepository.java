package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.entity.TournamentGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GroupRepository extends JpaRepository<TournamentGroup, Long> {
  @Query("SELECT g FROM TournamentGroup g WHERE g.isReady = :isReady")
  List<TournamentGroup> findAllByReady(boolean isReady);

  @Query("SELECT g FROM TournamentGroup g WHERE g.tournament.id = :tournamentId AND g.isReady = :isReady")
  List<TournamentGroup> findAllByTournamentIdAndReady(Long tournamentId, boolean isReady);
}
