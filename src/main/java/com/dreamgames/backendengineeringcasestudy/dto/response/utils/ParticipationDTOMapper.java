package com.dreamgames.backendengineeringcasestudy.dto.response.utils;

import com.dreamgames.backendengineeringcasestudy.dto.response.ParticipationDTO;
import com.dreamgames.backendengineeringcasestudy.entity.Participation;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class ParticipationDTOMapper implements Function<Participation, ParticipationDTO> {

  @Override
  public ParticipationDTO apply(Participation participation) {
    return new ParticipationDTO(
        participation.getId(),
        participation.getUser().getId(),
        participation.getGroup().getId(),
        participation.getScore()
    );
  }
}
