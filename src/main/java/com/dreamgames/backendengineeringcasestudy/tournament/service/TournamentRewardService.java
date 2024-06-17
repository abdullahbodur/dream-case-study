package com.dreamgames.backendengineeringcasestudy.tournament.service;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardUserDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.RewardDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.utils.RewardDTOMapper;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.Reward;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.TournamentGroup;
import com.dreamgames.backendengineeringcasestudy.user.entity.UserProgress;
import com.dreamgames.backendengineeringcasestudy.exceptions.EntityNotFoundException;
import com.dreamgames.backendengineeringcasestudy.tournament.repository.RewardRepository;
import com.dreamgames.backendengineeringcasestudy.user.service.UserProgressService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service class for handling operations related to Tournament Rewards. This includes assigning
 * rewards to users based on their tournament performance, allowing users to claim their rewards,
 * and retrieving reward information.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TournamentRewardService {

  private final RewardRepository rewardRepository;
  private final RewardDTOMapper rewardDTOMapper;
  private final UserProgressService progressService;
  @Value("${business.tournament.rewards.first-place}")
  private int firstPlaceRewardAmount;
  @Value("${business.tournament.rewards.second-place}")
  private int secondPlaceRewardAmount;

  /**
   * Assigns rewards to the first and second place users in each group of a tournament.
   *
   * @param tournamentDTO     The tournament for which to assign rewards.
   * @param groupLeaderboards The leaderboards of each group in the tournament.
   */
  public void assignRewards(TournamentDTO tournamentDTO,
      List<GroupLeaderboardDTO> groupLeaderboards) {
    groupLeaderboards.forEach(
        groupLeaderboardDTO -> {
          GroupLeaderboardUserDTO firstPlaceUser = groupLeaderboardDTO.getLeaderboard()
              .get(0);
          GroupLeaderboardUserDTO secondPlaceUser = groupLeaderboardDTO.getLeaderboard()
              .get(1);
          initReward(firstPlaceUser, tournamentDTO, groupLeaderboardDTO.getGroupId(), 1,
              firstPlaceRewardAmount);
          initReward(secondPlaceUser, tournamentDTO, groupLeaderboardDTO.getGroupId(), 2,
              secondPlaceRewardAmount);
        }
    );
  }

  /**
   * Initializes a reward for a user based on their rank in a tournament group.
   *
   * @param groupLeaderboardUser The user to reward.
   * @param tournamentDTO        The tournament the user participated in.
   * @param groupId              The ID of the group the user was in.
   * @param rank                 The user's rank in the group.
   * @param rewardAmount         The amount of the reward.
   */
  void initReward(GroupLeaderboardUserDTO groupLeaderboardUser,
      TournamentDTO tournamentDTO,
      Long groupId,
      int rank,
      int rewardAmount) {
    Tournament tournament = new Tournament();
    tournament.setId(tournamentDTO.getId());

    TournamentGroup tournamentGroup = new TournamentGroup();
    tournamentGroup.setId(groupId);

    UserProgress userProgress = new UserProgress();
    userProgress.setId(groupLeaderboardUser.getUserId());
    // Assign rewards to user
    Reward reward = new Reward();
    reward.setAmount(rewardAmount);
    reward.setTournament(tournament);
    reward.setGroup(tournamentGroup);
    reward.setCurrentRank(rank);
    reward.setUser(userProgress);
    reward.setClaimed(false);
    log.info("User {} has won {} reward", userProgress.getId(), rewardAmount);
    rewardRepository.save(reward);
  }


  /**
   * Allows a user to claim their reward for a tournament.
   *
   * @param userId       The ID of the user claiming the reward.
   * @param tournamentId The ID of the tournament the reward is for.
   * @return The DTO of the claimed reward.
   * @throws EntityNotFoundException If no unclaimed reward is found for the user in the
   *                                 tournament.
   */
  public UserProgressDTO claimReward(Long userId, Long tournamentId) {
    Reward reward = rewardRepository.findByTournamentIdAndUserId(tournamentId, userId)
        .orElseThrow(() -> new EntityNotFoundException(
            "Reward not found for user " + userId + " in tournament " + tournamentId));
    if (reward.isClaimed()) {
      throw new EntityNotFoundException(
          "Reward already claimed for user " + userId + " in tournament " + tournamentId);
    }
    reward.setClaimed(true);
    rewardRepository.save(reward);
    return progressService.depositCoins(userId, reward.getAmount());
  }

  /**
   * Retrieves the reward for a user in a tournament.
   *
   * @param userId       The ID of the user.
   * @param tournamentId The ID of the tournament.
   * @return The DTO of the reward.
   * @throws EntityNotFoundException If no reward is found for the user in the tournament.
   */
  public RewardDTO getReward(Long userId, Long tournamentId) {
    return rewardRepository.findByTournamentIdAndUserId(tournamentId, userId)
        .map(rewardDTOMapper)
        .orElseThrow(() -> new EntityNotFoundException(
            "Reward not found for user " + userId + " in tournament " + tournamentId));
  }

  /**
   * Finds an unclaimed reward for a user.
   *
   * @param userId The ID of the user.
   * @return The DTO of the unclaimed reward, or null if no unclaimed reward is found.
   */
  public RewardDTO findUnclaimedReward(Long userId) {
    return rewardRepository.findAllByUserIdAndClaimed(userId, false)
        .map(rewardDTOMapper)
        .orElse(null);
  }
}

