package com.dreamgames.backendengineeringcasestudy.api.dto.response.utils;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentDTO;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.Tournament;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class TournamentDTOMapper implements Function<Tournament, TournamentDTO> {

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
