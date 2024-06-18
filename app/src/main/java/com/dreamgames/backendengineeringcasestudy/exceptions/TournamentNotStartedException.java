package com.dreamgames.backendengineeringcasestudy.exceptions;

/**
 * The TournamentNotStartedException class is a custom exception that extends RuntimeException.
 * It is thrown when a tournament has not started yet but an operation that requires the tournament to be started is performed.
 * It contains a message and an array of arguments that can be used to format the message.
 */
public class TournamentNotStartedException extends RuntimeException {

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
  public TournamentNotStartedException(String message) {
    super(message);
    this.message = message;
  }
}