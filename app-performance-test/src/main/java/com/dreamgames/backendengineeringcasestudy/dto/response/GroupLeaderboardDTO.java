package com.dreamgames.backendengineeringcasestudy.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for the response of a group leaderboard. This DTO includes the group
 * ID and a list of GroupLeaderboardUserDTO objects representing the users in the group.
 */
@Setter
@Getter
@AllArgsConstructor
public class GroupLeaderboardDTO {

  private Long groupId;
  private List<GroupLeaderboardUserDTO> leaderboard;
}
