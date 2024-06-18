package com.dreamgames.backendengineeringcasestudy.api.dto.response.utils;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentGroupDTO;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.TournamentGroup;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 * The GroupDTOMapper class is a service that implements the Function interface to map TournamentGroup entities to TournamentGroupDTO objects.
 * It overrides the apply method from the Function interface to perform the mapping.
 */
@Service
public class GroupDTOMapper implements Function<TournamentGroup, TournamentGroupDTO> {

  /**
   * This method takes a TournamentGroup entity as input and maps it to a TournamentGroupDTO object.
   * The mapping involves copying the id, tournament id, and readiness status from the TournamentGroup entity to the TournamentGroupDTO object.
   *
   * @param tournamentGroup The TournamentGroup entity to map.
   * @return A TournamentGroupDTO object that represents the mapped TournamentGroup entity.
   */
  @Override
  public TournamentGroupDTO apply(TournamentGroup tournamentGroup) {
    return new TournamentGroupDTO(
        tournamentGroup.getId(),
        tournamentGroup.getTournament().getId(),
        tournamentGroup.isReady()
    );
  }
}