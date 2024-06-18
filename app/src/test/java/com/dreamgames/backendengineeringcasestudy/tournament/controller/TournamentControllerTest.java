package com.dreamgames.backendengineeringcasestudy.tournament.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dreamgames.backendengineeringcasestudy.api.dto.request.ClaimRewardRequestDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.request.EnterTournamentRequestDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.CountryLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardUserDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardUserRankDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import com.dreamgames.backendengineeringcasestudy.exceptions.EntityNotFoundException;
import com.dreamgames.backendengineeringcasestudy.exceptions.UserNotReadyForNewTournamentException;
import com.dreamgames.backendengineeringcasestudy.testutils.JsonReader;
import com.dreamgames.backendengineeringcasestudy.tournament.service.GroupService;
import com.dreamgames.backendengineeringcasestudy.tournament.service.LeaderboardService;
import com.dreamgames.backendengineeringcasestudy.tournament.service.RewardService;
import com.dreamgames.backendengineeringcasestudy.tournament.service.ScheduleService;
import com.dreamgames.backendengineeringcasestudy.user.service.UserProgressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TournamentControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private LeaderboardService leaderboardService;

  @MockBean
  private GroupService groupService;

  @MockBean
  private UserProgressService progressService;

  @MockBean
  private RewardService rewardService;

  @MockBean
  private ScheduleService scheduleService;

  @Autowired
  private MockMvc mockMvc;

  @DisplayName("Test getCountryLeaderboard method")
  @ParameterizedTest
  @MethodSource("getCountryLeaderboard")
  public void testGetCountryLeaderboard(List<CountryLeaderboardDTO> leaderboard) throws Exception {
    when(leaderboardService.getCountryLeaderboard()).thenReturn(leaderboard);
    mockMvc.perform(get("/api/v1/tournament/countryLeaderboard"))
        .andExpect(status().isOk());
    verify(leaderboardService, times(1)).getCountryLeaderboard();
  }

  @ParameterizedTest
  @DisplayName("Test getGroupLeaderboard method")
  @MethodSource("getGroupLeaderboard")
  public void testGetGroupLeaderboard(
      GroupLeaderboardDTO groupLeaderboardDTO
  ) throws Exception {
    when(leaderboardService.getGroupLeaderboard(1L)).thenReturn(groupLeaderboardDTO);
    mockMvc.perform(get("/api/v1/tournament/groupLeaderboard/1"))
        .andExpect(status().isOk());
    verify(leaderboardService, times(1)).getReadyGroupLeaderboard(1L);
  }

  @Test
  @DisplayName("Test getRank method with current tournament")
  public void testGetRank() throws Exception {
    GroupLeaderboardUserRankDTO requestDTO = new GroupLeaderboardUserRankDTO(
        1, new GroupLeaderboardUserDTO(10L, "test",
        Country.GERMANY,
        100)
    );
    TournamentDTO tournamentDTO = new TournamentDTO(1L, null, null, false);
    when(scheduleService.getCurrentTournament()).thenReturn(tournamentDTO);
    when(leaderboardService.getGroupIdForUser(10L)).thenReturn(1L);
    when(leaderboardService.getCurrentTournamentGroupUserRank(1L, 10L)).thenReturn(requestDTO);
    mockMvc.perform(get("/api/v1/tournament/rank/10/1"))
        .andExpect(status().isOk());
    verify(leaderboardService, times(1)).getCurrentTournamentGroupUserRank(1L, 10L);
    verify(leaderboardService, times(1)).getGroupIdForUser(10L);
    verify(scheduleService, times(1)).getCurrentTournament();
  }

  @Test
  @DisplayName("Test getRank method with historical tournament")
  public void testGetRankWithHistoricalTournament() throws Exception {
    GroupLeaderboardUserRankDTO requestDTO = new GroupLeaderboardUserRankDTO(
        1, new GroupLeaderboardUserDTO(10L, "test",
        Country.GERMANY,
        100)
    );
    TournamentDTO tournamentDTO = new TournamentDTO(1L, null, null, true);
    when(scheduleService.getCurrentTournament()).thenReturn(tournamentDTO);
    when(leaderboardService.getHistoricalTournamentUserRank(1L, 10L)).thenReturn(requestDTO);
    mockMvc.perform(get("/api/v1/tournament/rank/10/5"))
        .andExpect(status().isOk());
    verify(leaderboardService, times(1)).getHistoricalTournamentUserRank(5L, 10L);
  }

  @DisplayName("User enters tournament successfully")
  @Test
  public void userEntersTournamentSuccessfully() throws Exception {
    EnterTournamentRequestDTO requestDTO = new EnterTournamentRequestDTO(1L);
    UserProgressDTO progressDTO = new UserProgressDTO(1L, 100, 10, "test", Country.GERMANY);
    GroupLeaderboardDTO groupLeaderboardDTO = new GroupLeaderboardDTO(1L, new ArrayList<>());

    when(progressService.getUserProgress(requestDTO.userId())).thenReturn(progressDTO);
    when(groupService.enterTournament(progressDTO,
        scheduleService.getCurrentTournament())).thenReturn(1L);
    when(leaderboardService.getGroupLeaderboard(1L)).thenReturn(groupLeaderboardDTO);

    mockMvc.perform(post("/api/v1/tournament/enterTournament")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.groupId", is(1)));
  }

  @DisplayName("User fails to enter tournament due to failed tournament entry requirements")
  @Test
  public void userFailsToEnterTournamentDueToInsufficientProgress() throws Exception {
    EnterTournamentRequestDTO requestDTO = new EnterTournamentRequestDTO(1L);
    UserProgressDTO progressDTO = new UserProgressDTO(1L, 50, 5, "test", Country.GERMANY);

    when(progressService.getUserProgress(requestDTO.userId())).thenReturn(progressDTO);
    doThrow(new UserNotReadyForNewTournamentException(
        "User: 1 does not meet the minimum requirements for a new tournament")
    ).when(progressService).checkUserFitsMinRequirements(progressDTO);

    mockMvc.perform(post("/api/v1/tournament/enterTournament")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().isBadRequest());
  }


  @DisplayName("User claims reward successfully")
  @Test
  public void userClaimsRewardSuccessfully() throws Exception {
    ClaimRewardRequestDTO requestDTO = new ClaimRewardRequestDTO(3L, 5L);
    UserProgressDTO userProgressDTO = new UserProgressDTO(3L, 100, 10, "test", Country.GERMANY);

    when(rewardService.claimReward(requestDTO.userId(), requestDTO.tournamentId()))
        .thenReturn(userProgressDTO);

    mockMvc.perform(post("/api/v1/tournament/claimReward")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(3)))
        .andExpect(jsonPath("$.coinBalance", is(100.0)))
        .andExpect(jsonPath("$.level", is(10)))
        .andExpect(jsonPath("$.nickname", is("test")))
        .andExpect(jsonPath("$.country", is("GERMANY")));
  }

  @DisplayName("User fails to claim reward due to non-existent tournament")
  @Test
  public void userFailsToClaimRewardDueToNonExistentTournament() throws Exception {
    ClaimRewardRequestDTO requestDTO = new ClaimRewardRequestDTO(1L, 1L);

    when(rewardService.claimReward(requestDTO.userId(), requestDTO.tournamentId())).thenThrow(
        new EntityNotFoundException("Tournament does not exist"));

    mockMvc.perform(post("/api/v1/tournament/claimReward")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().isNotFound());
  }

  static List<List<CountryLeaderboardDTO>> getCountryLeaderboard() {
    return List.of(
        List.of(new CountryLeaderboardDTO(Country.TURKEY, 100),
            new CountryLeaderboardDTO(Country.GERMANY, 200),
            new CountryLeaderboardDTO(Country.FRANCE, 300),
            new CountryLeaderboardDTO(Country.UNITED_STATES, 400),
            new CountryLeaderboardDTO(Country.UNITED_KINGDOM, 500)));
  }

  static List<GroupLeaderboardDTO> getGroupLeaderboard() {
    GroupLeaderboardDTO groupLeaderboardDTO = JsonReader.readJson(
        "src/test/resources/group-leaderboard.json", GroupLeaderboardDTO.class);
    return Collections.singletonList(
        groupLeaderboardDTO
    );
  }

}