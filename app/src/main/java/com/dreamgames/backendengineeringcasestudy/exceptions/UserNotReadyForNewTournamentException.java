package com.dreamgames.backendengineeringcasestudy.exceptions;

/**
 * The UserNotReadyForNewTournamentException class is a custom exception that extends RuntimeException.
 * It is thrown when a user is not ready for a new tournament but an operation that requires the user to be ready is performed.
 * It contains a message and an array of arguments that can be used to format the message.
 */
public class UserNotReadyForNewTournamentException extends RuntimeException {

  /**
   * The message that describes the details of the exception.
   */
  private final String message;

  /**
   * The arguments that can be used to format the message.
   */
  private Object[] args;

  /**
   * Constructor that initializes the message field.
   *
   * @param message The message that describes the details of the exception.
   */
  public UserNotReadyForNewTournamentException(String message) {
    super(message);
    this.message = message;
  }
}