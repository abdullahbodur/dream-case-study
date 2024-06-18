package com.dreamgames.backendengineeringcasestudy.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RewardDTO {

  private Long id;
  private Long tournamentId;
  private Long groupId;
  private Long userId;
  private int currentRank;
  private boolean isClaimed;
}
