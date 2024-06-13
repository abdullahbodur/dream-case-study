package com.dreamgames.backendengineeringcasestudy.dto.response.utils;

import com.dreamgames.backendengineeringcasestudy.dto.response.UserDTO;
import com.dreamgames.backendengineeringcasestudy.entity.User;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {

  @Override
  public UserDTO apply(User user) {
    return new UserDTO(
        user.getId(),
        user.getUsername(),
        user.getEmail()
    );
  }

}
