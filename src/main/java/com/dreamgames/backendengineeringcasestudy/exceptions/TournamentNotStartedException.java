package com.dreamgames.backendengineeringcasestudy.exceptions;

public class TournamentNotStartedException extends RuntimeException {
  private final String message;
  private Object[] args;

  public TournamentNotStartedException(String message) {
    super(message);
    this.message = message;
  }
}
