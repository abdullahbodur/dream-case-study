package com.dreamgames.backendengineeringcasestudy.controller;

import com.dreamgames.backendengineeringcasestudy.dto.request.ClaimRewardRequestDTO;
import com.dreamgames.backendengineeringcasestudy.dto.request.EnterTournamentRequestDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.CountryLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.GroupLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.GroupLeaderboardUserRankDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.RewardDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.service.TournamentGroupService;
import com.dreamgames.backendengineeringcasestudy.service.TournamentLeaderboardService;
import com.dreamgames.backendengineeringcasestudy.service.TournamentRewardService;
import com.dreamgames.backendengineeringcasestudy.service.TournamentScheduleService;
import com.dreamgames.backendengineeringcasestudy.service.UserProgressService;
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

  private final TournamentLeaderboardService leaderboardService;
  private final TournamentGroupService groupService;
  private final UserProgressService progressService;
  private final TournamentRewardService rewardService;
  private final TournamentScheduleService scheduleService;

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
    return leaderboardService.getGroupLeaderboard(groupId);
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
      return leaderboardService.getCurrentTournamentGroupUserRank(tournamentId, userId);
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
  public RewardDTO claimReward(@RequestBody @Validated ClaimRewardRequestDTO requestDTO) {
    return rewardService.claimReward(requestDTO.userId(), requestDTO.tournamentId());
  }
}
