package com.dreamgames.backendengineeringcasestudy.tournament.repository;

import com.dreamgames.backendengineeringcasestudy.tournament.entity.Participation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

  @Query("SELECT p FROM Participation p WHERE p.group.id = ?1 AND p.user.id = ?2")
  Optional<Participation> findByGroupIdAndUserId(Long groupId, Long userId);

  @Query("SELECT p FROM Participation p WHERE p.group.id = ?1")
  List<Participation> findAllByGroupId(Long groupId);
}
