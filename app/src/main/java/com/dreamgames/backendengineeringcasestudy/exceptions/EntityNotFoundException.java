package com.dreamgames.backendengineeringcasestudy.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The EntityNotFoundException class is a custom exception that extends RuntimeException.
 * It is thrown when an entity is not found in the database.
 * It contains a message and an array of arguments that can be used to format the message.
 */
@Getter
@AllArgsConstructor
public class EntityNotFoundException extends RuntimeException {

  /**
   * The message that describes the details of the exception.
   */
  private String message;

  /**
   * The arguments that can be used to format the message.
   */
  private Object[] args;

  /**
   * Constructor that initializes the message field.
   *
   * @param message The message that describes the details of the exception.
   */
  public EntityNotFoundException(String message) {
    super(message);
    this.message = message;
  }
}