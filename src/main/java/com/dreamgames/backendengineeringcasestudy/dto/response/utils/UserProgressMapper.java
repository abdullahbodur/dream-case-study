package com.dreamgames.backendengineeringcasestudy.dto.response.utils;

import com.dreamgames.backendengineeringcasestudy.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.entity.UserProgress;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 * service class that maps a UserProgress entity to a UserProgressDTO. It implements the
 * Function interface and overrides the apply method to perform the mapping.
 */
@Service
public class UserProgressMapper implements Function<UserProgress, UserProgressDTO> {

  /**
   * method is used to map a UserProgress entity to a UserProgressDTO. It takes a UserProgress
   * object as input and returns a UserProgressDTO object.
   *
   * @param entity UserProgress entity that is to be mapped to a UserProgressDTO.
   * @return UserProgressDTO returns a UserProgressDTO object that contains the user's progress
   * details.
   */
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
