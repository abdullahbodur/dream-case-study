package com.dreamgames.backendengineeringcasestudy.tournament.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cronutils.model.time.ExecutionTime;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentDTO;
import com.dreamgames.backendengineeringcasestudy.exceptions.EntityNotFoundException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

  @InjectMocks
  private ScheduleService scheduleService;

  @Mock
  private TournamentService tournamentService;

  @Mock
  private RewardService rewardService;

  @Mock
  private LeaderboardService leaderboardService;

  @Mock
  private GroupPoolService groupPoolService;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(scheduleService, "tournamentStartCron", "0 0 0 * * ?");
    ReflectionTestUtils.setField(scheduleService, "tournamentEndCron", "0 0 20 * * ?");
    ReflectionTestUtils.setField(scheduleService, "timezone", "UTC");
    ReflectionTestUtils.setField(scheduleService, "tournamentService", tournamentService);
    ReflectionTestUtils.setField(scheduleService, "rewardService",
        rewardService);
    ReflectionTestUtils.setField(scheduleService, "leaderboardService",
        leaderboardService);
    ReflectionTestUtils.setField(scheduleService, "groupPoolService", groupPoolService);
  }

  @DisplayName("Start tournament successfully when active is existing")
  @Test
  public void startTournamentSuccessfully() {
    ZonedDateTime now = ZonedDateTime.parse("2024-06-17T00:00:00Z");
    ZonedDateTime nextEnd = ZonedDateTime.parse("2024-06-17T20:00:00Z");

    TournamentDTO tournamentDTO = new TournamentDTO(1L, now, nextEnd, false);
    try (MockedStatic<ZonedDateTime> zonedDateTimeMockedStatic = mockStatic(ZonedDateTime.class)
    ) {
      try (MockedStatic<ExecutionTime> executionTimeMockedStatic = mockStatic(ExecutionTime.class)
      ) {
        zonedDateTimeMockedStatic.when(() -> ZonedDateTime.now(ZoneId.of("UTC")))
            .thenReturn(now, now);
        ExecutionTime executionTime = mock(ExecutionTime.class);
        executionTimeMockedStatic.when(() -> ExecutionTime.forCron(any()))
            .thenReturn(executionTime);
        executionTimeMockedStatic.when(() -> executionTime.nextExecution(any()))
            .thenReturn(Optional.of(nextEnd), Optional.of(nextEnd));
        ReflectionTestUtils.invokeMethod(scheduleService, "startTournament");

      }
    }
    verify(tournamentService, times(1)).createTournament(now, nextEnd);
    verify(leaderboardService, times(1)).cleanUpLeaderboards();
    verify(groupPoolService, times(1)).cleanupGroupPool();
  }

  @DisplayName("End tournament successfully when active is existing")
  @Test
  public void endTournamentSuccessfully() {
    ZonedDateTime now = ZonedDateTime.parse("2024-06-17T20:00:00Z");
    TournamentDTO tournamentDTO = new TournamentDTO(1L, now.minusHours(20), now, false);
    ReflectionTestUtils.setField(scheduleService, "currentTournament", tournamentDTO);
    ReflectionTestUtils.setField(scheduleService, "isTournamentActive", true);

    List<GroupLeaderboardDTO> groupLeaderboardDTOList = List.of(
        new GroupLeaderboardDTO(1L, List.of()));
    when(leaderboardService.getReadyGroupLeaderboards()).thenReturn(groupLeaderboardDTOList);
    try (MockedStatic<ZonedDateTime> zonedDateTimeMockedStatic = mockStatic(ZonedDateTime.class)
    ) {
      zonedDateTimeMockedStatic.when(() -> ZonedDateTime.now(ZoneId.of("UTC")))
          .thenReturn(now);
      ReflectionTestUtils.invokeMethod(scheduleService, "endTournament");
    }

    assertEquals(false,
        ReflectionTestUtils.getField(scheduleService, "isTournamentActive"));
    verify(tournamentService, times(1)).completeTournament(1L);
    verify(leaderboardService, times(1)).cleanUpLeaderboards();
    verify(rewardService, times(1)).assignRewards(tournamentDTO, groupLeaderboardDTOList);
    verify(groupPoolService, times(1)).cleanupGroupPool();
  }

  @DisplayName("End tournament successfully when active is not existing")
  @Test
  public void endTournamentSuccessfullyWhenActiveNotExisting() {
    ZonedDateTime now = ZonedDateTime.parse("2024-06-17T20:00:00Z");
    ReflectionTestUtils.setField(scheduleService, "isTournamentActive", true);
    when(tournamentService.getActiveTournament()).thenThrow(new EntityNotFoundException(""));
    try (MockedStatic<ZonedDateTime> zonedDateTimeMockedStatic = mockStatic(ZonedDateTime.class)
    ) {
      zonedDateTimeMockedStatic.when(() -> ZonedDateTime.now(ZoneId.of("UTC")))
          .thenReturn(now);
      ReflectionTestUtils.invokeMethod(scheduleService, "endTournament");
    }

    assertEquals(false,
        ReflectionTestUtils.getField(scheduleService, "isTournamentActive"));
    verify(tournamentService, times(0)).completeTournament(1L);
    verify(leaderboardService, times(0)).cleanUpLeaderboards();
    verify(rewardService, times(0)).assignRewards(any(), anyList());
    verify(groupPoolService, times(0)).cleanupGroupPool();
  }

  @DisplayName("End tournament successfully when active is not assigned but retrieved")
  @Test
  public void endTournamentSuccessfullyWhenActiveNotAssignedButRetrieved() {
    ZonedDateTime now = ZonedDateTime.parse("2024-06-17T20:00:00Z");
    TournamentDTO tournamentDTO = new TournamentDTO(1L, now.minusHours(20), now, false);
    ReflectionTestUtils.setField(scheduleService, "isTournamentActive", true);
    ReflectionTestUtils.setField(scheduleService, "currentTournament", null);
    when(tournamentService.getActiveTournament()).thenReturn(tournamentDTO);
    List<GroupLeaderboardDTO> groupLeaderboardDTOList = List.of(
        new GroupLeaderboardDTO(1L, List.of()));
    when(leaderboardService.getReadyGroupLeaderboards()).thenReturn(groupLeaderboardDTOList);
    try (MockedStatic<ZonedDateTime> zonedDateTimeMockedStatic = mockStatic(ZonedDateTime.class)
    ) {
      zonedDateTimeMockedStatic.when(() -> ZonedDateTime.now(ZoneId.of("UTC")))
          .thenReturn(now);
      ReflectionTestUtils.invokeMethod(scheduleService, "endTournament");
    }

    assertEquals(false,
        ReflectionTestUtils.getField(scheduleService, "isTournamentActive"));
    verify(tournamentService, times(1)).completeTournament(1L);
    verify(leaderboardService, times(1)).cleanUpLeaderboards();
    verify(rewardService, times(1)).assignRewards(tournamentDTO, groupLeaderboardDTOList);
    verify(groupPoolService, times(1)).cleanupGroupPool();
  }

  @DisplayName("End tournament with failure if tournament is not active")
  @Test
  public void endTournamentWithFailureIfTournamentNotActive() {
    ReflectionTestUtils.setField(scheduleService, "isTournamentActive", false);
    ReflectionTestUtils.setField(scheduleService, "currentTournament", null);
    ReflectionTestUtils.invokeMethod(scheduleService, "endTournament");
    verify(tournamentService, times(0)).completeTournament(anyLong());
    verify(leaderboardService, times(0)).cleanUpLeaderboards();
    verify(rewardService, times(0)).assignRewards(any(), anyList());
    verify(groupPoolService, times(0)).cleanupGroupPool();
  }

  @DisplayName("Initialize tournament on post construct when tournament is already started")
  @Test
  public void initializeTournamentScheduleService() {
    ZonedDateTime now = ZonedDateTime.parse("2024-06-17T02:00:00Z");
    ZonedDateTime nextEnd = ZonedDateTime.parse("2024-06-17T20:00:00Z");
    ZonedDateTime nextStart = ZonedDateTime.parse("2024-06-18T00:00:00Z");
    ZonedDateTime previousStart = ZonedDateTime.parse("2024-06-17T00:00:00Z");
    TournamentDTO tournamentDTO = new TournamentDTO(1L, previousStart, nextEnd, false);

    try (MockedStatic<ZonedDateTime> zonedDateTimeMockedStatic = mockStatic(ZonedDateTime.class)
    ) {
      try (MockedStatic<ExecutionTime> executionTimeMockedStatic = mockStatic(ExecutionTime.class)
      ) {
        zonedDateTimeMockedStatic.when(() -> ZonedDateTime.now(ZoneId.of("UTC")))
            .thenReturn(now, now);
        ExecutionTime executionTime = mock(ExecutionTime.class);
        executionTimeMockedStatic.when(() -> ExecutionTime.forCron(any()))
            .thenReturn(executionTime);
        executionTimeMockedStatic.when(() -> executionTime.nextExecution(any()))
            .thenReturn(Optional.of(nextStart), Optional.of(nextEnd));
        when(tournamentService.getActiveTournament()).thenReturn(tournamentDTO);
        ReflectionTestUtils.invokeMethod(scheduleService, "init");
      }
    }

    verify(tournamentService, times(1)).getActiveTournament();
    assertEquals(true,
        ReflectionTestUtils.getField(scheduleService, "isTournamentActive"));
    assertEquals(tournamentDTO,
        ReflectionTestUtils.getField(scheduleService, "currentTournament"));
  }

  @DisplayName("Initialize tournament on post construct when tournament is already started and entity is not saved database")
  @Test
  public void initializeTournamentScheduleServiceWhenTournamentNotSaved() {
    ZonedDateTime now = ZonedDateTime.parse("2024-06-17T02:00:00Z");
    ZonedDateTime nextEnd = ZonedDateTime.parse("2024-06-17T20:00:00Z");
    ZonedDateTime nextStart = ZonedDateTime.parse("2024-06-18T00:00:00Z");
    ZonedDateTime previousStart = ZonedDateTime.parse("2024-06-17T00:00:00Z");
    TournamentDTO tournamentDTO = new TournamentDTO(1L, previousStart, nextEnd, false);

    try (MockedStatic<ZonedDateTime> zonedDateTimeMockedStatic = mockStatic(ZonedDateTime.class)
    ) {
      try (MockedStatic<ExecutionTime> executionTimeMockedStatic = mockStatic(ExecutionTime.class)
      ) {
        zonedDateTimeMockedStatic.when(() -> ZonedDateTime.now(ZoneId.of("UTC")))
            .thenReturn(now, now);
        ExecutionTime executionTime = mock(ExecutionTime.class);
        executionTimeMockedStatic.when(() -> ExecutionTime.forCron(any()))
            .thenReturn(executionTime);
        executionTimeMockedStatic.when(() -> executionTime.nextExecution(any()))
            .thenReturn(Optional.of(nextStart), Optional.of(nextEnd));
        executionTimeMockedStatic.when(() -> executionTime.lastExecution(any()))
            .thenReturn(Optional.of(previousStart));
        when(tournamentService.getActiveTournament()).thenThrow(new EntityNotFoundException(""));
        when(tournamentService.createTournament(any(), any())).thenReturn(tournamentDTO);
        ReflectionTestUtils.invokeMethod(scheduleService, "init");
      }
    }

    verify(tournamentService, times(1)).getActiveTournament();
    assertEquals(true,
        ReflectionTestUtils.getField(scheduleService, "isTournamentActive"));
    assertEquals(tournamentDTO,
        ReflectionTestUtils.getField(scheduleService, "currentTournament"));
    verify(tournamentService, times(1)).createTournament(previousStart, nextEnd);
    verify(leaderboardService, times(1)).cleanUpLeaderboards();
    verify(groupPoolService, times(1)).cleanupGroupPool();
  }

  @DisplayName("Initialize tournament on post construct when tournament is not started yet")
  @Test
  public void initializeTournamentScheduleServiceWhenTournamentNotStarted() {
    ZonedDateTime now = ZonedDateTime.parse("2024-06-17T21:00:00Z");
    ZonedDateTime nextEnd = ZonedDateTime.parse("2024-06-18T20:00:00Z");
    ZonedDateTime nextStart = ZonedDateTime.parse("2024-06-18T00:00:00Z");
    ZonedDateTime previousStart = ZonedDateTime.parse("2024-06-17T00:00:00Z");

    try (MockedStatic<ZonedDateTime> zonedDateTimeMockedStatic = mockStatic(ZonedDateTime.class)
    ) {
      try (MockedStatic<ExecutionTime> executionTimeMockedStatic = mockStatic(ExecutionTime.class)
      ) {
        zonedDateTimeMockedStatic.when(() -> ZonedDateTime.now(ZoneId.of("UTC")))
            .thenReturn(now, now);
        ExecutionTime executionTime = mock(ExecutionTime.class);
        executionTimeMockedStatic.when(() -> ExecutionTime.forCron(any()))
            .thenReturn(executionTime);
        executionTimeMockedStatic.when(() -> executionTime.nextExecution(any()))
            .thenReturn(Optional.of(nextStart), Optional.of(nextEnd));
        ReflectionTestUtils.invokeMethod(scheduleService, "init");
      }
    }

    verify(tournamentService, times(0)).getActiveTournament();
    assertEquals(false,
        ReflectionTestUtils.getField(scheduleService, "isTournamentActive"));
    assertEquals(null,
        ReflectionTestUtils.getField(scheduleService, "currentTournament"));
    verify(tournamentService, times(0)).createTournament(previousStart, nextEnd);
    verify(leaderboardService, times(0)).cleanUpLeaderboards();
    verify(groupPoolService, times(0)).cleanupGroupPool();
  }
}