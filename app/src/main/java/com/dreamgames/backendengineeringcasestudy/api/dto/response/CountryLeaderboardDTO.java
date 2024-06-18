package com.dreamgames.backendengineeringcasestudy.api.dto.response;

import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CountryLeaderboardDTO {

  private Country country;
  private int totalScore;
}
