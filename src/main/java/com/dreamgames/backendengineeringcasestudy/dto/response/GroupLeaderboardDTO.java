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

  /**
   * Overrides the toString method to provide a custom string representation of the
   * GroupLeaderboardDTO object. The string representation includes the group ID and the top 5 users
   * in the group.
   *
   * @return a string representation of the GroupLeaderboardDTO object.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Group ID: ").append(groupId).append("\n");
    sb.append("Rank\tUser ID\tNickname\tCountry\tScore\n");
    for (int i = 0; i < leaderboard.size() && i < 5; i++) {
      GroupLeaderboardUserDTO user = leaderboard.get(i);
      sb.append(i + 1).append("\t") // Rank
          .append(user.getUserId()).append("\t")
          .append(user.getNickname()).append("\t")
          .append(user.getCountry()).append("\t")
          .append(user.getTournamentScore()).append("\n");
    }
    sb.append("............................\n");
    return sb.toString();
  }
}
