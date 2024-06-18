package com.dreamgames.backendengineeringcasestudy.api.dto.response.utils;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentDTO;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.Tournament;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 * The TournamentDTOMapper class is a service that implements the Function interface to map Tournament entities to TournamentDTO objects.
 * It overrides the apply method from the Function interface to perform the mapping.
 */
@Service
public class TournamentDTOMapper implements Function<Tournament, TournamentDTO> {

  /**
   * This method takes a Tournament entity as input and maps it to a TournamentDTO object.
   * The mapping involves copying the id, start time, end time, and completion status from the Tournament entity to the TournamentDTO object.
   *
   * @param tournament The Tournament entity to map.
   * @return A TournamentDTO object that represents the mapped Tournament entity.
   */
  @Override
  public TournamentDTO apply(Tournament tournament) {
    return new TournamentDTO(
        tournament.getId(),
        tournament.getStartTime(),
        tournament.getEndTime(),
        tournament.isCompleted()
    );
  }
}