package com.dreamgames.backendengineeringcasestudy.tournament.service;

import static com.cronutils.model.CronType.QUARTZ;

import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentDTO;
import com.dreamgames.backendengineeringcasestudy.exceptions.EntityNotFoundException;
import jakarta.annotation.PostConstruct;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


/**
 * Service class for managing tournament schedules. This service is responsible for initializing and
 * managing the tournament schedules. It provides methods to start and end tournaments, and to check
 * if a tournament is active.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {

  private final TournamentService tournamentService;

  private final RewardService rewardService;

  private final LeaderboardService leaderboardService;

  private final GroupPoolService groupPoolService;
  private final CronParser parser = new CronParser(
      CronDefinitionBuilder.instanceDefinitionFor(QUARTZ));

  @Getter
  private TournamentDTO currentTournament;

  @Getter
  public boolean isTournamentActive = false;

  @Value("${business.tournament.schedule.start-cron}")
  private String tournamentStartCron;

  @Value("${business.tournament.schedule.end-cron}")
  private String tournamentEndCron;

  @Value("${business.tournament.timezone}")
  private String timezone;

  /**
   * Initializes the tournament service and determines if a tournament is already active. It
   * calculates the start and end times of the tournament based on the cron expressions and checks
   * if the current time is within this range.
   */
  @PostConstruct
  private void init() {
    log.info("Tournament service initialized");
    ZonedDateTime now = ZonedDateTime.now(ZoneId.of(timezone));
    ZonedDateTime start = findNextExecutionTime(tournamentStartCron);
    ZonedDateTime end = findNextExecutionTime(tournamentEndCron);
    log.info("Tournament start time: {}", start);
    log.info("Tournament end time: {}", end);
    log.info("Current time: {}", now);
    if (isAlreadyStarted(now, start, end)) {
      log.info("Tournament is already started");
      isTournamentActive = true;
      try {
        currentTournament = tournamentService.getActiveTournament();
      } catch (EntityNotFoundException e) {
        log.info("Active tournament not found in the database, creating a new one");
        ZonedDateTime previousStart = findPreviousExecutionTime(tournamentStartCron);

        log.info("Creating new tournament with start time: {} and end time: {}", previousStart,
            end);
        currentTournament = tournamentService.createTournament(
            previousStart,
            end);
        groupPoolService.cleanupGroupPool();
        leaderboardService.cleanUpLeaderboards();
      }
    } else {
      long seconds = secondsDiff(start, now);
      log.info("Tournament is not started yet: {} hours {} minutes {} seconds left", seconds / 3600,
          (seconds % 3600) / 60, seconds % 60);
    }
  }

  /**
   * Calculates the difference in seconds between two LocalDateTime objects.
   *
   * @param start the start time
   * @param now   the current time
   * @return the difference in seconds
   */
  private long secondsDiff(ZonedDateTime start, ZonedDateTime now) {
    return start.toEpochSecond() - now.toEpochSecond();
  }

  /**
   * Checks if a tournament has already started based on the current time, start time, and end
   * time.
   *
   * @param now   the current time
   * @param start the start time of the tournament
   * @param end   the end time of the tournament
   * @return true if the tournament has already started, false otherwise
   */
  private boolean isAlreadyStarted(ZonedDateTime now, ZonedDateTime start, ZonedDateTime end) {
    if (start.isBefore(end)) {
      return now.isAfter(start) && now.isBefore(end);
    } else {
      return now.isAfter(start) || now.isBefore(end);
    }
  }

  /**
   * Finds the next execution time of a cron expression.
   *
   * @param cronExpression the cron expression
   * @return the next execution time as a LocalDateTime object
   */
  private ZonedDateTime findNextExecutionTime(String cronExpression) {
    ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of(timezone));
    Optional<ZonedDateTime> nextExecution = ExecutionTime.forCron(parser.parse(cronExpression))
        .nextExecution(dateTime);
    return nextExecution.orElse(null);
  }

  /**
   * Finds the previous execution time of a cron expression.
   *
   * @param cronExpression the cron expression
   * @return the previous execution time as a ZonedDateTime object, or null if no previous execution
   * time is found
   */
  private ZonedDateTime findPreviousExecutionTime(String cronExpression) {
    ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of(timezone));
    Optional<ZonedDateTime> lastExecution = ExecutionTime.forCron(parser.parse(cronExpression))
        .lastExecution(dateTime);
    return lastExecution.orElse(null);
  }

  /**
   * Starts a new tournament. This method is scheduled to run at the time specified by the
   * start-cron property. It sets the isTournamentActive flag to true, logs the start of the
   * tournament, and creates a new tournament with the start and end times calculated based on the
   * cron expressions.
   */
  @Scheduled(cron = "${business.tournament.schedule.start-cron}", zone = "${tournament.timezone}")
  private void startTournament() {
    isTournamentActive = true;
    log.info("Tournament started");
    ZonedDateTime startDate = ZonedDateTime.now(ZoneId.of(timezone));
    ZonedDateTime endDate = findNextExecutionTime(tournamentEndCron);
    currentTournament = tournamentService.createTournament(startDate, endDate);
    leaderboardService.cleanUpLeaderboards();
    groupPoolService.cleanupGroupPool();
  }

  /**
   * Ends the current tournament. This method is scheduled to run at the time specified by the
   * end-cron property. It checks if a tournament is active, completes the active tournament,
   * assigns rewards, cleans up the leaderboards, and sets the isTournamentActive flag to false.
   */
  @Scheduled(cron = "${business.tournament.schedule.end-cron}", zone = "${tournament.timezone}")
  private void endTournament() {
    if (!isTournamentActive) {
      log.info("Tournament is not active");
      return;
    }

    if (currentTournament == null) {
      try {
        currentTournament = tournamentService.getActiveTournament();
      } catch (EntityNotFoundException e) {
        log.info("No active tournament found for ending");
        isTournamentActive = false;
        return;
      }
    }
    tournamentService.completeTournament(currentTournament.getId());
    isTournamentActive = false;
    rewardService
        .assignRewards(currentTournament, leaderboardService.getGroupLeaderboards());
    leaderboardService.cleanUpLeaderboards();
    groupPoolService.cleanupGroupPool();
    currentTournament = null;
    log.info("Tournament ended");
  }
}
