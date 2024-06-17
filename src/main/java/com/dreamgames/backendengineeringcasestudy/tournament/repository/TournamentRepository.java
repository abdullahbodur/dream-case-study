package com.dreamgames.backendengineeringcasestudy.tournament.repository;

import com.dreamgames.backendengineeringcasestudy.tournament.entity.Tournament;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {

  @Query("SELECT t FROM Tournament t WHERE t.isCompleted = false AND t.startTime <= CURRENT_TIMESTAMP")
  Optional<Tournament> findActiveTournament();
}
