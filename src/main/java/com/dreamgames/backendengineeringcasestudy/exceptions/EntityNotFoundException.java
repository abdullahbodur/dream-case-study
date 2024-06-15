package com.dreamgames.backendengineeringcasestudy.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EntityNotFoundException extends RuntimeException {

  private String message;
  private Object[] args;

  public EntityNotFoundException(String message) {
    super(message);
    this.message = message;
  }
}