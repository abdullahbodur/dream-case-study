package com.dreamgames.backendengineeringcasestudy.tournament.entity;

import com.dreamgames.backendengineeringcasestudy.user.entity.UserProgress;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Reward", schema = "public")
public class Reward {

  /**
   * unique identifier for a user.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "tournamentId")
  private Tournament tournament;

  @ManyToOne
  @JoinColumn(name = "groupId")
  private TournamentGroup group;

  @ManyToOne
  @JoinColumn(name = "userId")
  private UserProgress user;

  private int currentRank;

  private int amount;

  private boolean isClaimed;
}
