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
 * The Participation class represents a user's participation in a tournament group.
 * It contains information about the user, the group they are participating in, and their score.
 */
@Entity
@Getter
@Setter
@Table(name = "Participation", schema = "public")
public class Participation {

  /**
   * Unique identifier for a participation instance.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The user who is participating in the tournament group.
   * This is a many-to-one relationship as a user can participate in multiple groups.
   */
  @ManyToOne
  @JoinColumn(name = "userId")
  private UserProgress user;

  /**
   * The tournament group in which the user is participating.
   * This is a many-to-one relationship as a group can have multiple participants.
   */
  @ManyToOne
  @JoinColumn(name = "groupId")
  private TournamentGroup group;

  /**
   * The score of the user in the tournament group.
   */
  private int score;
}
