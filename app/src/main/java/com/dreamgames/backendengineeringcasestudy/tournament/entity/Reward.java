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

/**
 * The Reward class represents a reward that a user can receive in a tournament. It contains
 * information about the tournament, the group, the user who received the reward, the current rank
 * of the user, the amount of the reward, and whether the reward has been claimed.
 */
@Entity
@Getter
@Setter
@Table(name = "Reward", schema = "public")
public class Reward {


  /**
   * Unique identifier for a reward instance.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The tournament in which the reward is given.
   * This is a many-to-one relationship as a tournament can have multiple rewards.
   */
  @ManyToOne
  @JoinColumn(name = "tournamentId")
  private Tournament tournament;

  /**
   * The group in which the reward is given.
   * This is a many-to-one relationship as a group can have multiple rewards.
   */
  @ManyToOne
  @JoinColumn(name = "groupId")
  private TournamentGroup group;

  /**
   * The user who received the reward.
   * This is a many-to-one relationship as a user can receive multiple rewards.
   */
  @ManyToOne
  @JoinColumn(name = "userId")
  private UserProgress user;

  /**
   * The current rank of the user in the tournament.
   */
  private int currentRank;

  /**
   * The amount of the reward.
   */
  private int amount;

  /**
   * A flag indicating whether the reward has been claimed by the user.
   */
  private boolean isClaimed;
}
