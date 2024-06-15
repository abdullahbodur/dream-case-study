package com.dreamgames.backendengineeringcasestudy.exceptions;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RequiredArgsConstructor
@ControllerAdvice
@Slf4j
@Order(HIGHEST_PRECEDENCE)
public class ControllerAdvices extends ResponseEntityExceptionHandler {

  private static final String UNEXPECTED_ERROR = "Unexpected error occurred.";
  private static final String ENTITY_NOT_FOUND = "Given data is not found.";
  /**
   * message source
   */
  private final MessageSource messageSource;

  @ExceptionHandler(RestException.class)
  public ResponseEntity<RestMessage> handleIllegalArgument(RestException ex) {
    String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(),
        Locale.getDefault());
    return new ResponseEntity<>(new RestMessage(errorMessage), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<RestMessage> handleEntityNotFound() {
    String errorMessage = messageSource.getMessage(ENTITY_NOT_FOUND, null, Locale.getDefault());
    return new ResponseEntity<>(new RestMessage(errorMessage), NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<RestMessage> handleExceptions(Exception ex) {
    String errorMessage = messageSource.getMessage(UNEXPECTED_ERROR, null, Locale.getDefault());
    ex.printStackTrace();
    return new ResponseEntity<>(new RestMessage(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
