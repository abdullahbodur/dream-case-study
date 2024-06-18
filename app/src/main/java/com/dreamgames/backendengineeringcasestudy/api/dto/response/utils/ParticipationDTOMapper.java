package com.dreamgames.backendengineeringcasestudy.api.dto.response.utils;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.ParticipationDTO;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.Participation;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 * The ParticipationDTOMapper class is a service that implements the Function interface to map Participation entities to ParticipationDTO objects.
 * It overrides the apply method from the Function interface to perform the mapping.
 */
@Service
public class ParticipationDTOMapper implements Function<Participation, ParticipationDTO> {

  /**
   * This method takes a Participation entity as input and maps it to a ParticipationDTO object.
   * The mapping involves copying the id, user id, group id, and score from the Participation entity to the ParticipationDTO object.
   *
   * @param participation The Participation entity to map.
   * @return A ParticipationDTO object that represents the mapped Participation entity.
   */
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