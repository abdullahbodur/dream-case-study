package com.dreamgames.backendengineeringcasestudy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ParticipationDTO {

  private Long id;
  private Long userId;
  private Long groupId;
  private int score;
}