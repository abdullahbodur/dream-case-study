package com.dreamgames.backendengineeringcasestudy.api.dto.response.utils;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.RewardDTO;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.Reward;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 * The RewardDTOMapper class is a service that implements the Function interface to map Reward entities to RewardDTO objects.
 * It overrides the apply method from the Function interface to perform the mapping.
 */
@Service
public class RewardDTOMapper implements Function<Reward, RewardDTO> {

  /**
   * This method takes a Reward entity as input and maps it to a RewardDTO object.
   * The mapping involves copying the id, tournament id, group id, user id, current rank, and claimed status from the Reward entity to the RewardDTO object.
   *
   * @param reward The Reward entity to map.
   * @return A RewardDTO object that represents the mapped Reward entity.
   */
  @Override
  public RewardDTO apply(Reward reward) {
    return new RewardDTO(
        reward.getId(),
        reward.getTournament().getId(),
        reward.getGroup().getId(),
        reward.getUser().getId(),
        reward.getCurrentRank(),
        reward.isClaimed()
    );
  }
}