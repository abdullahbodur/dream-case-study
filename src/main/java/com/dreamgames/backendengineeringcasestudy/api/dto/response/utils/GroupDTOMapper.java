package com.dreamgames.backendengineeringcasestudy.api.dto.response.utils;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentGroupDTO;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.TournamentGroup;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class GroupDTOMapper implements Function<TournamentGroup, TournamentGroupDTO> {

  @Override
  public TournamentGroupDTO apply(TournamentGroup tournamentGroup) {
    return new TournamentGroupDTO(
        tournamentGroup.getId(),
        tournamentGroup.getTournament().getId(),
        tournamentGroup.isReady()
    );
  }
}
