package com.dreamgames.backendengineeringcasestudy.tournament.service;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.utils.TournamentDTOMapper;
import com.dreamgames.backendengineeringcasestudy.exceptions.EntityNotFoundException;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.tournament.repository.TournamentRepository;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service class for handling operations related to Tournaments. This includes creating a new
 * tournament, retrieving the active tournament, retrieving a specific tournament by its ID, and
 * marking a tournament as completed.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TournamentService {

  private final TournamentRepository tournamentRepository;
  private final TournamentDTOMapper tournamentDTOMapper;

  /**
   * Creates a new Tournament with the given start and end times.
   *
   * @param startTime The start time of the new Tournament.
   * @param endTime   The end time of the new Tournament.
   * @return The DTO of the newly created Tournament.
   */
  public TournamentDTO createTournament(ZonedDateTime startTime, ZonedDateTime endTime) {
    Tournament tournament = new Tournament();
    tournament.setStartTime(startTime);
    tournament.setEndTime(endTime);
    tournament.setCompleted(false);
    tournamentRepository.save(tournament);
    return tournamentDTOMapper.apply(tournament);
  }

  /**
   * Retrieves the currently active Tournament.
   *
   * @return The DTO of the active Tournament.
   * @throws EntityNotFoundException If no active Tournament is found.
   */
  public TournamentDTO getActiveTournament() {
    return tournamentRepository.findActiveTournament()
        .map(tournamentDTOMapper)
        .orElseThrow(
            () -> new EntityNotFoundException("Active tournament not found")
        );
  }

  /**
   * Retrieves a specific Tournament by its ID.
   *
   * @param tournamentId The ID of the Tournament to retrieve.
   * @return The DTO of the retrieved Tournament.
   * @throws EntityNotFoundException If no Tournament with the given ID is found.
   */
  public TournamentDTO getTournament(Long tournamentId) {
    return tournamentRepository.findById(tournamentId)
        .map(tournamentDTOMapper)
        .orElseThrow(
            () -> new EntityNotFoundException("Tournament with id " + tournamentId + " not found")
        );
  }

  /**
   * Marks a specific Tournament as completed.
   *
   * @param tournamentId The ID of the Tournament to mark as completed.
   * @throws EntityNotFoundException If no Tournament with the given ID is found.
   */
  public void completeTournament(Long tournamentId) {
    Tournament tournament = tournamentRepository.findById(tournamentId)
        .orElseThrow(
            () -> new EntityNotFoundException("Tournament with id " + tournamentId + " not found")
        );
    tournament.setCompleted(true);
    tournamentRepository.save(tournament);
  }

}
