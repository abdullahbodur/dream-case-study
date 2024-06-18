package com.dreamgames.backendengineeringcasestudy.tournament.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * The Tournament class represents a tournament in the system.
 * It contains information about the start and end times of the tournament,
 * whether the tournament is completed, and the groups that are part of the tournament.
 */
@Entity
@Getter
@Setter
@Table(name = "Tournament", schema = "public")
public class Tournament {

  /**
   * Unique identifier for a tournament.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The start time of the tournament.
   */
  private ZonedDateTime startTime;

  /**
   * The end time of the tournament.
   */
  private ZonedDateTime endTime;

  /**
   * A flag indicating whether the tournament is completed.
   */
  private boolean isCompleted;

  /**
   * The groups that are part of the tournament.
   * This is a one-to-many relationship as a tournament can have multiple groups.
   */
  @OneToMany(mappedBy = "tournament")
  private List<TournamentGroup> groups;
}