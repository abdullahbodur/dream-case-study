package com.dreamgames.backendengineeringcasestudy.exceptions;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.stream.Collectors;
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
  public ResponseEntity<RestMessage> handleIllegalArgument(RestException ex) {
    return new ResponseEntity<>(new RestMessage(ENTITY_NOT_FOUND,
        List.of(ex.getMessage())
    ), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles EntityNotFoundException.
   *
   * @param ex the exception
   * @return a ResponseEntity with a custom message and a NOT_FOUND status
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<RestMessage> handleEntityNotFound(EntityNotFoundException ex) {
    return new ResponseEntity<>(new RestMessage(ENTITY_NOT_FOUND, List.of(ex.getMessage())),
        NOT_FOUND);
  }

  /**
   * Handles TournamentNotStartedException.
   *
   * @param ex the exception
   * @return a ResponseEntity with a custom message and a BAD_REQUEST status
   */
  @ExceptionHandler(TournamentNotStartedException.class)
  public ResponseEntity<RestMessage> handleTournamentNotStarted(TournamentNotStartedException ex) {
    return new ResponseEntity<>(new RestMessage(TOURNAMENT_NOT_STARTED, List.of(ex.getMessage())),
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
      UserNotReadyForNewTournamentException ex) {
    return new ResponseEntity<>(
        new RestMessage(USER_NOT_READY_FOR_TOURNAMENT, List.of(ex.getMessage())),
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

    List<String> errors = ex.getBindingResult().getAllErrors().stream()
        .map(ObjectError::getDefaultMessage).
        collect(Collectors.toList());
    return new ResponseEntity<>(new RestMessage(VALIDATION_ERROR, errors),
        new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles all other exceptions.
   *
   * @param ex the exception
   * @return a ResponseEntity with a custom message and an INTERNAL_SERVER_ERROR status
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<RestMessage> handleExceptions(Exception ex) {
    ex.printStackTrace();
    return new ResponseEntity<>(new RestMessage(UNEXPECTED_ERROR, List.of(ex.getMessage())),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
