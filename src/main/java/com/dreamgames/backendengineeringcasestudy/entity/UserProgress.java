package com.dreamgames.backendengineeringcasestudy.entity;

import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class for user progress. It is used to map the UserProgress table in the database to a
 * Java object.
 */
@Entity
@Getter
@Setter
@Table(name = "UserProgress", schema = "public")
public class UserProgress {

  /**
   * unique identifier for a user's progress.
   */
  @Id
  private Long id;

  /**
   * coin balance of the user.
   */
  private double coinBalance;

  /**
   * level of the user.
   */
  private int level;

  /**
   * country of the user.
   */
  @Enumerated(EnumType.STRING)
  private Country country;
}
