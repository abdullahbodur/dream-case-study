package com.dreamgames.backendengineeringcasestudy.tournament.service;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.utils.TournamentDTOMapper;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.exceptions.EntityNotFoundException;
import com.dreamgames.backendengineeringcasestudy.tournament.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TournamentServiceTest {

  @InjectMocks
  private TournamentService tournamentService;

  @Mock
  private TournamentRepository tournamentRepository;

  @Mock
  private TournamentDTOMapper tournamentDTOMapper;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @DisplayName("Create tournament successfully")
  @Test
  public void createTournamentSuccessfully() {
    ZonedDateTime startTime = ZonedDateTime.now();
    ZonedDateTime endTime = startTime.plusHours(1);
    Tournament tournament = new Tournament();
    tournament.setStartTime(startTime);
    tournament.setEndTime(endTime);
    tournament.setCompleted(false);

    TournamentDTO tournamentDTO = new TournamentDTO(
        1L,
        startTime,
        endTime,
        false
    );

    when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);
    when(tournamentDTOMapper.apply(any(Tournament.class))).thenReturn(tournamentDTO);
    TournamentDTO result = tournamentService.createTournament(startTime, endTime);
    // assert equals
    assertEquals(startTime, result.getStartTime());
    assertEquals(endTime, result.getEndTime());
    assertFalse(result.isCompleted());
    verify(tournamentRepository, times(1)).save(
        argThat(
            arg -> arg.getStartTime().equals(startTime) && arg.getEndTime().equals(endTime)
        ));
    verify(tournamentDTOMapper, times(1)).apply(any(Tournament.class));
  }

  @DisplayName("Get active tournament successfully")
  @Test
  public void getActiveTournamentSuccessfully() {
    Tournament tournament = new Tournament();
    TournamentDTO tournamentDTO = new TournamentDTO(
        1L,
        ZonedDateTime.now(),
        ZonedDateTime.now().plusHours(1),
        false
    );
    when(tournamentDTOMapper.apply(any(Tournament.class))).thenReturn(tournamentDTO);
    when(tournamentRepository.findActiveTournament()).thenReturn(Optional.of(tournament));

    tournamentService.getActiveTournament();

    verify(tournamentRepository, times(1)).findActiveTournament();
    verify(tournamentDTOMapper, times(1)).apply(any(Tournament.class));
  }

  @DisplayName("Get active tournament throws EntityNotFoundException")
  @Test
  public void getActiveTournamentThrowsEntityNotFoundException() {
    when(tournamentRepository.findActiveTournament()).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> tournamentService.getActiveTournament());

    verify(tournamentRepository, times(1)).findActiveTournament();
  }

  @DisplayName("Get tournament by id successfully")
  @Test
  public void getTournamentSuccessfully() {
    Tournament tournament = new Tournament();
    TournamentDTO tournamentDTO = new TournamentDTO(
        1L,
        ZonedDateTime.now(),
        ZonedDateTime.now().plusHours(1),
        false
    );
    when(tournamentDTOMapper.apply(any(Tournament.class))).thenReturn(tournamentDTO);
    when(tournamentRepository.findById(anyLong())).thenReturn(Optional.of(tournament));

    tournamentService.getTournament(1L);
    verify(tournamentRepository, times(1)).findById(1L);
    verify(tournamentDTOMapper, times(1)).apply(any(Tournament.class));
  }

  @DisplayName("Get tournament by id throws EntityNotFoundException")
  @Test
  public void getTournamentThrowsEntityNotFoundException() {
    when(tournamentRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> tournamentService.getTournament(1L));

    verify(tournamentRepository, times(1)).findById(1L);
  }

  @DisplayName("Complete tournament successfully")
  @Test
  public void completeTournamentSuccessfully() {
    Tournament tournament = new Tournament();
    when(tournamentRepository.findById(anyLong())).thenReturn(Optional.of(tournament));

    tournamentService.completeTournament(1L);

    verify(tournamentRepository, times(1)).findById(anyLong());
    verify(tournamentRepository, times(1)).save(any(Tournament.class));
  }

  @DisplayName("Complete tournament throws EntityNotFoundException")
  @Test
  public void completeTournamentThrowsEntityNotFoundException() {
    when(tournamentRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> tournamentService.completeTournament(1L));

    verify(tournamentRepository, times(1)).findById(anyLong());
  }
}