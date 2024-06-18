package com.dreamgames.backendengineeringcasestudy.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The RestException class is a custom exception that extends RuntimeException.
 * It is used to represent exceptions that occur during RESTful operations.
 * It contains a message and an array of arguments that can be used to format the message.
 */
@Getter
@AllArgsConstructor
public class RestException extends RuntimeException {

  /**
   * The message that describes the details of the exception.
   */
  private String message;

  /**
   * The arguments that can be used to format the message.
   */
  private Object[] args;
}