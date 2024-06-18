package com.dreamgames.backendengineeringcasestudy.tournament.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * The TournamentGroup class represents a group within a tournament.
 * It contains information about the tournament it belongs to, whether the group is ready,
 * and the scores of the users in the group.
 */
@Entity
@Getter
@Setter
@Table(name = "TournamentGroup", schema = "public")
public class TournamentGroup {

  /**
   * Unique identifier for a tournament group.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The tournament to which the group belongs.
   * This is a many-to-one relationship as a tournament can have multiple groups.
   */
  @ManyToOne
  @JoinColumn(name = "tournamentId")
  private Tournament tournament;

  /**
   * A flag indicating whether the group is ready.
   */
  private boolean isReady;

  /**
   * The scores of the users in the group.
   * This is an element collection, as it represents a collection of basic values (integers) associated with the entity.
   */
  @ElementCollection
  private List<Integer> scores;

}