package com.dreamgames.backendengineeringcasestudy.tournament.controller;

import com.dreamgames.backendengineeringcasestudy.api.dto.request.ClaimRewardRequestDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.request.EnterTournamentRequestDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.CountryLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardUserRankDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.tournament.service.GroupService;
import com.dreamgames.backendengineeringcasestudy.tournament.service.LeaderboardService;
import com.dreamgames.backendengineeringcasestudy.tournament.service.RewardService;
import com.dreamgames.backendengineeringcasestudy.tournament.service.ScheduleService;
import com.dreamgames.backendengineeringcasestudy.user.service.UserProgressService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller class for manging tournaments. It handles HTTP requests related to tournaments.
 */
@RestController
@RequestMapping("/api/v1/tournament")
@RequiredArgsConstructor
public class TournamentController {

  private final LeaderboardService leaderboardService;
  private final GroupService groupService;
  private final UserProgressService progressService;
  private final RewardService rewardService;
  private final ScheduleService scheduleService;

  /**
   * Endpoint for getting the leaderboard of countries.
   *
   * @return List of CountryLeaderboardDTO objects representing the leaderboard of countries.
   */
  @GetMapping("/countryLeaderboard")
  public List<CountryLeaderboardDTO> getCountryLeaderboard() {
    return leaderboardService.getCountryLeaderboard();
  }

  /**
   * Endpoint for getting the leaderboard of a specific group.
   *
   * @param groupId ID of the group.
   * @return GroupLeaderboardDTO object representing the leaderboard of the group.
   */
  @GetMapping("/groupLeaderboard/{groupId}")
  public GroupLeaderboardDTO getGroupLeaderboard(@PathVariable Long groupId) {
    return leaderboardService.getReadyGroupLeaderboard(groupId);
  }

  /**
   * Endpoint for getting the rank of a user in a specific tournament.
   *
   * @param userId       ID of the user.
   * @param tournamentId ID of the tournament.
   * @return GroupLeaderboardUserRankDTO object representing the user's rank in the tournament.
   */
  @GetMapping("/rank/{userId}/{tournamentId}")
  public GroupLeaderboardUserRankDTO getRank(@PathVariable Long userId,
      @PathVariable Long tournamentId) {
    if (scheduleService.getCurrentTournament().getId().equals(tournamentId)) {
      Long groupId = leaderboardService.getGroupIdForUser(userId);
      return leaderboardService.getCurrentTournamentGroupUserRank(groupId, userId);
    } else {
      return leaderboardService.getHistoricalTournamentUserRank(tournamentId, userId);
    }
  }

  /**
   * Endpoint for a user to enter a tournament.
   *
   * @param requestDTO Request body containing the user's ID.
   * @return GroupLeaderboardDTO object representing the leaderboard of the group the user entered.
   */
  @PostMapping("/enterTournament")
  public GroupLeaderboardDTO enterTournament(
      @RequestBody @Validated EnterTournamentRequestDTO requestDTO) {
    UserProgressDTO progressDTO = progressService.getUserProgress(requestDTO.userId());
    progressService.checkUserFitsMinRequirements(progressDTO);
    Long groupId = groupService.enterTournament(progressDTO,
        scheduleService.getCurrentTournament());
    leaderboardService.addUserToGroup(progressDTO, groupId);
    progressService.withdrawTournamentEntryTicket(progressDTO.getId());
    return leaderboardService.getGroupLeaderboard(groupId);
  }

  /**
   * Endpoint for a user to claim their reward for a tournament.
   *
   * @param requestDTO Request body containing the user's ID and the tournament's ID.
   * @return RewardDTO object representing the claimed reward.
   */
  @PostMapping("/claimReward")
  public UserProgressDTO claimReward(@RequestBody @Validated ClaimRewardRequestDTO requestDTO) {
    return rewardService.claimReward(requestDTO.userId(), requestDTO.tournamentId());
  }
}
