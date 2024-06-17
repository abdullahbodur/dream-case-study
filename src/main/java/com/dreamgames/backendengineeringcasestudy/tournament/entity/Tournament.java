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

@Entity
@Getter
@Setter
@Table(name = "Tournament", schema = "public")
public class Tournament {

  /**
   * unique identifier for a user.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * name of the tournament.
   */
  private ZonedDateTime startTime;

  /**
   * email of the user.
   */
  private ZonedDateTime endTime;

  private boolean isCompleted;

  @OneToMany(mappedBy = "tournament")
  private List<TournamentGroup> groups;
}
