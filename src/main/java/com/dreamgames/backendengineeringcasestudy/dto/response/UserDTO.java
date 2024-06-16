package com.dreamgames.backendengineeringcasestudy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) class for a user. It is used to send user data over the network or to
 * save user data in the database.
 */
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

  /**
   * unique identifier for a user.
   */
  private Long id;


  /**
   * email of the user.
   */
  private String email;
}