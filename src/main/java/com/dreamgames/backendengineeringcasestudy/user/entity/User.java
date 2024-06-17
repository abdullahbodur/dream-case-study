package com.dreamgames.backendengineeringcasestudy.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class for a user. It is used to map the User table in the database to a Java
 * object.
 */
@Entity
@Getter
@Setter
@Table(name = "User", schema = "public")
public class User {

  /**
   * unique identifier for a user.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * email of the user.
   */
  private String email;

  /**
   * password of the user.
   */
  private String password;
}
