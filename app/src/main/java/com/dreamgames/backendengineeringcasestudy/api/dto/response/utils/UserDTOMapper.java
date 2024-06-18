package com.dreamgames.backendengineeringcasestudy.api.dto.response.utils;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.UserDTO;
import com.dreamgames.backendengineeringcasestudy.user.entity.User;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 * service class that maps a User entity to a UserDTO. It implements the Function
 * interface and overrides the apply method to perform the mapping.
 */
@Service
public class UserDTOMapper implements Function<User, UserDTO> {

  /**
   * method is used to map a User entity to a UserDTO. It takes a User object as input and
   * returns a UserDTO object.
   *
   * @param user User entity that is to be mapped to a UserDTO.
   * @return UserDTO returns a UserDTO object that contains the user's details.
   */
  @Override
  public UserDTO apply(User user) {
    return new UserDTO(
        user.getId(),
        user.getEmail()
    );
  }

}