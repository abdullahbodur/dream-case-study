package com.dreamgames.backendengineeringcasestudy.dto.response;

import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object (DTO) class for user progress. It is used to send user progress
 * data over the network or to save user progress data in the database.
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserProgressDTO {

  /**
   * unique identifier for a user's progress.
   */
  private Long id;

  /**
   * coin balance of the user.
   */
  private double coinBalance;

  /**
   * level of the user.
   */
  private int level;

  private String nickname;

  /**
   * country of the user.
   */
  private Country country;
}