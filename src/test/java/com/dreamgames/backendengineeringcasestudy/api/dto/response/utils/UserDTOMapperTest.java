package com.dreamgames.backendengineeringcasestudy.api.dto.response.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.UserDTO;
import com.dreamgames.backendengineeringcasestudy.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserDTOMapperTest {

  private UserDTOMapper userDTOMapper;

  @BeforeEach
  public void setUp() {
    userDTOMapper = new UserDTOMapper();
  }

  @DisplayName("Map User to UserDTO successfully")
  @Test
  public void mapUserToUserDTOSuccessfully() {
    User user = new User();
    user.setId(1L);
    user.setEmail("test@example.com");

    UserDTO expected = new UserDTO(1L, "test@example.com");

    UserDTO actual = userDTOMapper.apply(user);

    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getEmail(), actual.getEmail());
  }
}