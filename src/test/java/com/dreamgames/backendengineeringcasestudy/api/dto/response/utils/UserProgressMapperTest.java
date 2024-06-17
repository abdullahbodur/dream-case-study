package com.dreamgames.backendengineeringcasestudy.api.dto.response.utils;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.utils.UserProgressMapper;
import com.dreamgames.backendengineeringcasestudy.user.entity.UserProgress;
import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserProgressMapperTest {

  private UserProgressMapper userProgressMapper;

  @BeforeEach
  public void setUp() {
    userProgressMapper = new UserProgressMapper();
  }

  @DisplayName("Map UserProgress to UserProgressDTO successfully")
  @Test
  public void mapUserProgressToUserProgressDTOSuccessfully() {
    UserProgress userProgress = new UserProgress();
    userProgress.setId(1L);
    userProgress.setCoinBalance(100);
    userProgress.setLevel(2);
    userProgress.setNickname("test");
    userProgress.setCountry(Country.UNITED_STATES);

    UserProgressDTO expected = new UserProgressDTO(1L, 100, 2, "test", Country.UNITED_STATES);

    UserProgressDTO actual = userProgressMapper.apply(userProgress);

    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getCoinBalance(), actual.getCoinBalance());
    assertEquals(expected.getLevel(), actual.getLevel());
    assertEquals(expected.getNickname(), actual.getNickname());
    assertEquals(expected.getCountry(), actual.getCountry());
  }
}