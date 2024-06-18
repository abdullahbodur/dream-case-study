package com.dreamgames.backendengineeringcasestudy.api.dto.response;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TournamentDTO {

  private Long id;
  private ZonedDateTime startTime;
  private ZonedDateTime endTime;
  private boolean isCompleted;
}
