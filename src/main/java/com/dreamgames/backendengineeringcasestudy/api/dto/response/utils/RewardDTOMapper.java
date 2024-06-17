package com.dreamgames.backendengineeringcasestudy.api.dto.response.utils;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.RewardDTO;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.Reward;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class RewardDTOMapper implements Function<Reward, RewardDTO> {

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
