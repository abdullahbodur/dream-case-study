package com.dreamgames.backendengineeringcasestudy.dto.response;

import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CountryLeaderboardDTO {

  private Country country;
  private int totalScore;
}
