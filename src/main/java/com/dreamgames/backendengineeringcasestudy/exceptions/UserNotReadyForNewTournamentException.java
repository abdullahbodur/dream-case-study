package com.dreamgames.backendengineeringcasestudy.exceptions;

public class UserNotReadyForNewTournamentException extends RuntimeException {

  private final String message;
  private Object[] args;

  public UserNotReadyForNewTournamentException(String message) {
    super(message);
    this.message = message;
  }

}
