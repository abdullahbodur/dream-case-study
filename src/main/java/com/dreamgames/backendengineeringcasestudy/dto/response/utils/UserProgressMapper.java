package com.dreamgames.backendengineeringcasestudy.dto.response.utils;

import com.dreamgames.backendengineeringcasestudy.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.entity.UserProgress;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class UserProgressMapper implements Function<UserProgress, UserProgressDTO> {

  @Override
  public UserProgressDTO apply(UserProgress entity) {
    return new UserProgressDTO(
        entity.getId(),
        entity.getCoinBalance(),
        entity.getLevel(),
        entity.getCountry()
    );
  }


}
