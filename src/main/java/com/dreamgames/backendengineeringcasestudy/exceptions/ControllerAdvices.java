package com.dreamgames.backendengineeringcasestudy.exceptions;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RequiredArgsConstructor
@ControllerAdvice
@Slf4j
@Order(HIGHEST_PRECEDENCE)
public class ControllerAdvices extends ResponseEntityExceptionHandler {

  private static final String UNEXPECTED_ERROR = "Unexpected error occurred";
  private static final String ENTITY_NOT_FOUND = "Given data is not found";
  private static final String VALIDATION_ERROR = "Validation error occurred";
  private static final String TOURNAMENT_NOT_STARTED = "Tournament is not started yet";
  private static final String USER_NOT_READY_FOR_TOURNAMENT = "User is not ready for the tournament";

  /**
   * Handles RestException.
   *
   * @param ex the exception
   * @return a ResponseEntity with a custom message and a BAD_REQUEST status
   */
  @ExceptionHandler(RestException.class)
  public ResponseEntity<RestMessage> handleIllegalArgument(RestException ex, ServletWebRequest request) {
    return new ResponseEntity<>(new RestMessage(
        HttpStatus.BAD_REQUEST.value(),
        ex.getMessage(),
        request.getRequest().getServletPath(),
        ENTITY_NOT_FOUND
    ), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles EntityNotFoundException.
   *
   * @param ex the exception
   * @return a ResponseEntity with a custom message and a NOT_FOUND status
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<RestMessage> handleEntityNotFound(EntityNotFoundException ex,
      ServletWebRequest request) {
    return new ResponseEntity<>(new RestMessage(
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage(),
        request.getRequest().getServletPath(),
        ENTITY_NOT_FOUND
    ),
        NOT_FOUND);
  }

  /**
   * Handles TournamentNotStartedException.
   *
   * @param ex the exception
   * @return a ResponseEntity with a custom message and a BAD_REQUEST status
   */
  @ExceptionHandler(TournamentNotStartedException.class)
  public ResponseEntity<RestMessage> handleTournamentNotStarted(TournamentNotStartedException ex,
      ServletWebRequest request) {
    return new ResponseEntity<>(new RestMessage(
        HttpStatus.BAD_REQUEST.value(),
        ex.getMessage(),
        request.getRequest().getServletPath(),
        TOURNAMENT_NOT_STARTED
    ),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles UserNotReadyForNewTournamentException.
   *
   * @param ex the exception
   * @return a ResponseEntity with a custom message and a BAD_REQUEST status
   */
  @ExceptionHandler(UserNotReadyForNewTournamentException.class)
  public ResponseEntity<RestMessage> handleUserNotReadyForTournament(
      UserNotReadyForNewTournamentException ex, ServletWebRequest request) {
    return new ResponseEntity<>(new RestMessage(
        HttpStatus.BAD_REQUEST.value(),
        ex.getMessage(),
        request.getRequest().getServletPath(),
        USER_NOT_READY_FOR_TOURNAMENT
    ),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Overrides the handleMethodArgumentNotValid method from ResponseEntityExceptionHandler to handle
   * validation errors.
   *
   * @param ex      the exception
   * @param headers the headers for the response
   * @param status  the status for the response
   * @param request the current request
   * @return a ResponseEntity with a custom message and a BAD_REQUEST status
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    List<String> messages = ex.getBindingResult().getAllErrors().stream()
        .map(ObjectError::getDefaultMessage)
        .toList();
    String message = messages.isEmpty() ? "Validation error occurred" : messages.get(0);
    return new ResponseEntity<>(new RestMessage(
        HttpStatus.BAD_REQUEST.value(),
        message,
        ((ServletWebRequest) request).getRequest().getServletPath(),
        VALIDATION_ERROR
    ),
        new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles all other exceptions.
   *
   * @param ex the exception
   * @return a ResponseEntity with a custom message and an INTERNAL_SERVER_ERROR status
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<RestMessage> handleExceptions(Exception ex, ServletWebRequest request) {
    ex.printStackTrace();
    return new ResponseEntity<>(new RestMessage(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        ex.getMessage(),
        request.getRequest().getServletPath(),
        UNEXPECTED_ERROR
    ),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
