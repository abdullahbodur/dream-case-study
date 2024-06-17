package com.dreamgames.backendengineeringcasestudy.api.dto.response;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardUserDTO;
import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupLeaderboardDTOTest {

  private GroupLeaderboardDTO groupLeaderboardDTO;

  @BeforeEach
  public void setUp() {
    List<GroupLeaderboardUserDTO> leaderboard = Arrays.asList(
        new GroupLeaderboardUserDTO(1L, "user1", Country.UNITED_STATES, 100),
        new GroupLeaderboardUserDTO(2L, "user2", Country.UNITED_STATES, 200)
    );
    groupLeaderboardDTO = new GroupLeaderboardDTO(1L, leaderboard);
  }

  @DisplayName("GroupLeaderboardDTO toString method returns expected format")
  @Test
  public void toStringReturnsExpectedFormat() {
    String expected = """
        Group ID: 1
        Rank\tUser ID\tNickname\tCountry\tScore
        1\t1\tuser1\tUNITED_STATES\t100
        2\t2\tuser2\tUNITED_STATES\t200
        ............................
        """;
    assertEquals(expected, groupLeaderboardDTO.toString());
  }

  @DisplayName("GroupLeaderboardDTO toString method handles empty leaderboard")
  @Test
  public void toStringHandlesEmptyLeaderboard() {
    groupLeaderboardDTO.setLeaderboard(List.of());
    String expected = """
        Group ID: 1
        Rank\tUser ID\tNickname\tCountry\tScore
        ............................
        """;
    assertEquals(expected, groupLeaderboardDTO.toString());
  }
}