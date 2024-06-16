package com.dreamgames.backendengineeringcasestudy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TournamentGroupDTO {

  private Long groupId;
  private Long tournamentId;
  private boolean isReady;
}
