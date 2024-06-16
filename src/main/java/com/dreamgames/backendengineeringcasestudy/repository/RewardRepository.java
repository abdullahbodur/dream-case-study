package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.entity.Reward;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RewardRepository extends JpaRepository<Reward, Long> {

  @Query("SELECT r FROM Reward r WHERE r.tournament.id = ?1 AND r.user.id = ?2")
  Optional<Reward> findByTournamentIdAndUserId(Long tournamentId, Long userId);

  @Query("SELECT r FROM Reward r WHERE r.user.id = ?1 AND r.isClaimed = ?2")
  Optional<Reward> findAllByUserIdAndClaimed(Long userId, boolean isClaimed);
}

