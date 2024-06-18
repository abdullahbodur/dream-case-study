package com.dreamgames.backendengineeringcasestudy.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupLeaderboardUserRankDTO {

  private int rank;
  private GroupLeaderboardUserDTO user;
}
