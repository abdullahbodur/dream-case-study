package com.dreamgames.backendengineeringcasestudy.tournament.service;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.CountryLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardUserDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardUserRankDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.ParticipationDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.RewardDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import com.dreamgames.backendengineeringcasestudy.exceptions.EntityNotFoundException;
import com.dreamgames.backendengineeringcasestudy.user.service.UserProgressService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Service class for managing tournament leaderboards. This service is responsible for initializing
 * and managing the leaderboards in Redis. It provides methods to get and update the leaderboards,
 * add scores, and add users to groups.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LeaderboardService {

  private final RedisTemplate<String, List<CountryLeaderboardDTO>> countryLeaderboard;

  private final RewardService rewardService;

  private final UserProgressService userProgressService;

  private final ParticipationService participationService;

  private final RedisTemplate<String, List<GroupLeaderboardUserDTO>> groupLeaderboardPool;

  private final RedisTemplate<String, Number> userGroupPool;

  /**
   * Cleans up all leaderboards by deleting all keys in Redis.
   */
  public void cleanUpLeaderboards() {
    initCountryLeaderboard();
    groupLeaderboardPool.delete(Objects.requireNonNull(groupLeaderboardPool.keys("*")));
    userGroupPool.delete(Objects.requireNonNull(userGroupPool.keys("*")));
  }

  /**
   * Initializes the country leaderboard.
   */
  private List<CountryLeaderboardDTO> initCountryLeaderboard() {
    List<CountryLeaderboardDTO> countryLeaderboardDTOList = new ArrayList<>();
    for (Country country : Country.values()) {
      countryLeaderboardDTOList.add(new CountryLeaderboardDTO(country, 0));
    }
    countryLeaderboard.opsForValue().set("countryLeaderboardPool", countryLeaderboardDTOList);
    return countryLeaderboardDTOList;
  }

  /**
   * Gets the leaderboard for a specific group.
   *
   * @param groupId the ID of the group
   * @return the leaderboard for the group
   */
  public GroupLeaderboardDTO getGroupLeaderboard(Long groupId) {
    return new GroupLeaderboardDTO(groupId,
        groupLeaderboardPool.opsForValue().get("groupLeaderboardPool:group:" + groupId));
  }

  /**
   * Gets the leaderboard for a specific group if it is ready.
   *
   * @param groupId the ID of the group
   * @return the leaderboard for the group
   */
  public GroupLeaderboardDTO getReadyGroupLeaderboard(Long groupId) {
    List<GroupLeaderboardUserDTO> groupLeaderboard = groupLeaderboardPool.opsForValue()
        .get("groupLeaderboardPool:group:" + groupId);
    if (groupLeaderboard == null) {
      throw new EntityNotFoundException("Group leaderboard not found for group: " + groupId);
    }
    if (groupLeaderboard.size() == List.of(Country.values()).size()) {
      return new GroupLeaderboardDTO(groupId, groupLeaderboard);
    }
    throw new EntityNotFoundException("Group is not ready yet");
  }

  /**
   * Gets all group leaderboards.
   *
   * @return a list of all group leaderboards
   */
  public List<GroupLeaderboardDTO> getReadyGroupLeaderboards() {
    return Objects.requireNonNull(groupLeaderboardPool.keys(
            "groupLeaderboardPool:group:*")).stream()
        .map(key -> new GroupLeaderboardDTO(Long.parseLong(key.split(":")[2]),
            groupLeaderboardPool.opsForValue().get(key)))
        .filter(groupLeaderboard -> isGroupReadyByLeaderboard(groupLeaderboard.getLeaderboard()))
        .toList();
  }

  /**
   * Checks if a group is ready based on the leaderboard.
   *
   * @param groupLeaderboard the group leaderboard
   * @return true if the group is ready, false otherwise
   */
  private boolean isGroupReadyByLeaderboard(List<GroupLeaderboardUserDTO> groupLeaderboard) {
    return groupLeaderboard != null && groupLeaderboard.size() == List.of(Country.values()).size();
  }

  /**
   * Gets the current rank of a user in a specific tournament group.
   *
   * @param groupId the ID of the group
   * @param userId  the ID of the user
   * @return the current rank of the user in the group
   */
  public GroupLeaderboardUserRankDTO getCurrentTournamentGroupUserRank(Long groupId, Long userId) {
    List<GroupLeaderboardUserDTO> groupLeaderboard = groupLeaderboardPool.opsForValue()
        .get("groupLeaderboardPool:group:" + groupId);
    if (groupLeaderboard == null) {
      return null;
    }
    for (int i = 0; i < groupLeaderboard.size(); i++) {
      if (groupLeaderboard.get(i).getUserId().equals(userId)) {
        return new GroupLeaderboardUserRankDTO(i + 1, groupLeaderboard.get(i));
      }
    }
    return null;
  }

  /**
   * Adds a score for a user in a group leaderboard.
   *
   * @param userId the ID of the user
   * @param score  the score to add
   */
  public void addScoreForGroupLeaderboard(Long userId, int score) {
    Number groupId = userGroupPool.opsForValue().get("userGroupPool:" + userId);
    if (groupId == null) {
      throw new IllegalArgumentException("User not found in any group");
    }
    List<GroupLeaderboardUserDTO> groupLeaderboard = groupLeaderboardPool.opsForValue()
        .get("groupLeaderboardPool:group:" + groupId.longValue());
    if (groupLeaderboard == null) {
      throw new IllegalArgumentException(
          "Group leaderboard not found for group: " + groupId.longValue());
    }
    for (GroupLeaderboardUserDTO user : groupLeaderboard) {
      if (user.getUserId().equals(userId)) {
        user.setTournamentScore(user.getTournamentScore() + score);
        break;
      }
    }
    // sort the leaderboard descending
    groupLeaderboard.sort((a, b) -> b.getTournamentScore() - a.getTournamentScore());
    groupLeaderboardPool.opsForValue().set(
        "groupLeaderboardPool:group:" + groupId.longValue(), groupLeaderboard);
  }

  /**
   * Adds a score for a country in the country leaderboard.
   *
   * @param country the country
   * @param score   the score to add
   */
  public void addScoreForCountryLeaderboard(Country country, int score) {
    List<CountryLeaderboardDTO> countryLeaderBoard = countryLeaderboard.opsForValue()
        .get("countryLeaderboardPool");
    if (countryLeaderBoard == null) {
      countryLeaderBoard = initCountryLeaderboard();
    }
    for (CountryLeaderboardDTO leaderboard : countryLeaderBoard) {
      if (leaderboard.getCountry().equals(country)) {
        leaderboard.setTotalScore(leaderboard.getTotalScore() + score);
        break;
      }
    }
    // sort the leaderboard descending
    countryLeaderBoard.sort((a, b) -> b.getTotalScore() - a.getTotalScore());
    countryLeaderboard.opsForValue().set("countryLeaderboardPool", countryLeaderBoard);
  }

  /**
   * Adds a user to a group.
   *
   * @param userProgress the user's progress
   * @param groupId      the ID of the group
   */
  public void addUserToGroup(UserProgressDTO userProgress, Long groupId) {
    // add to  groupLeaderboardPool:group:groupId list
    List<GroupLeaderboardUserDTO> groupLeaderboard = groupLeaderboardPool.opsForValue()
        .get("groupLeaderboardPool:group:" + groupId);
    if (groupLeaderboard == null) {
      groupLeaderboard = new ArrayList<>();
    }
    groupLeaderboard.add(new GroupLeaderboardUserDTO(
        userProgress.getId(),
        userProgress.getNickname(),
        userProgress.getCountry(),
        0
    ));
    groupLeaderboardPool.opsForValue().set(
        "groupLeaderboardPool:group:" + groupId, groupLeaderboard);
    userGroupPool.opsForValue().set("userGroupPool:" + userProgress.getId(), groupId);

  }

  /**
   * Gets the historical rank of a user in a specific tournament.
   *
   * @param tournamentId the ID of the tournament
   * @param userId       the ID of the user
   * @return the historical rank of the user in the tournament
   */
  public GroupLeaderboardUserRankDTO getHistoricalTournamentUserRank(Long tournamentId,
      Long userId) {
    RewardDTO reward = rewardService.getReward(userId, tournamentId);
    UserProgressDTO progressDTO = userProgressService.getUserProgress(userId);
    ParticipationDTO participationDTO = participationService.getParticipation(userId,
        reward.getGroupId());
    return new GroupLeaderboardUserRankDTO(
        reward.getCurrentRank(),
        new GroupLeaderboardUserDTO(
            userId,
            progressDTO.getNickname(),
            progressDTO.getCountry(),
            participationDTO.getScore()
        )
    );
  }

  /**
   * Gets the country leaderboard.
   *
   * @return the country leaderboard
   */
  public List<CountryLeaderboardDTO> getCountryLeaderboard() {
    List<CountryLeaderboardDTO> leaderboardDTO = countryLeaderboard.opsForValue()
        .get("countryLeaderboardPool");
    if (leaderboardDTO == null) {
      leaderboardDTO = initCountryLeaderboard();
    }
    return leaderboardDTO;
  }

  /**
   * Initializes the country leaderboard.
   *
   * @param userId the ID of the user
   * @return the ID of the group for the user
   */
  public Long getGroupIdForUser(Long userId) {
    Number groupId = userGroupPool.opsForValue().get("userGroupPool:" + userId);
    return groupId != null ? groupId.longValue() : null;
  }

  /**
   * retrieves the group ready status.
   *
   * @param userId the ID of the user
   * @return true if the group is full and ready, false otherwise
   */
  public boolean isUserGroupReady(Long userId) {
    Number groupId = userGroupPool.opsForValue().get("userGroupPool:" + userId);
    if (groupId == null) {
      return false;
    }
    List<GroupLeaderboardUserDTO> groupLeaderboard = groupLeaderboardPool.opsForValue()
        .get("groupLeaderboardPool:group:" + groupId);
    return groupLeaderboard != null && groupLeaderboard.size() == List.of(Country.values()).size();
  }
}
