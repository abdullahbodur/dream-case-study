package com.dreamgames.backendengineeringcasestudy.tournament.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardUserDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.RewardDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.utils.RewardDTOMapper;
import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import com.dreamgames.backendengineeringcasestudy.exceptions.EntityNotFoundException;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.Reward;
import com.dreamgames.backendengineeringcasestudy.tournament.repository.RewardRepository;
import com.dreamgames.backendengineeringcasestudy.user.service.UserProgressService;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

  @InjectMocks
  private RewardService rewardService;

  @Mock
  private RewardRepository rewardRepository;
  @Mock
  private RewardDTOMapper rewardDTOMapper;

  @Mock
  private UserProgressService progressService;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(rewardService, "firstPlaceRewardAmount", 100);
    ReflectionTestUtils.setField(rewardService, "secondPlaceRewardAmount", 50);
  }

  @DisplayName("Assign rewards to users in tournament groups")
  @Test
  public void assignRewards() {
    TournamentDTO tournamentDTO = new TournamentDTO(44L, ZonedDateTime.now(),
        ZonedDateTime.now().plusHours(1), false);

    List<GroupLeaderboardDTO> groupLeaderboards = List.of(
        new GroupLeaderboardDTO(1L, List.of(new GroupLeaderboardUserDTO(10L, "test",
                Country.GERMANY,
                100),
            new GroupLeaderboardUserDTO(11L, "test1",
                Country.UNITED_STATES,
                110),
            new GroupLeaderboardUserDTO(12L, "test2",
                Country.UNITED_KINGDOM,
                120),
            new GroupLeaderboardUserDTO(13L, "test3",
                Country.TURKEY,
                130),
            new GroupLeaderboardUserDTO(14L, "test4",
                Country.FRANCE,
                140))));
    rewardService.assignRewards(tournamentDTO, groupLeaderboards);

    verify(rewardRepository, times(2)).save(any(Reward.class));
  }


  @DisplayName("Initialize reward for user")
  @Test
  public void initReward() {

    GroupLeaderboardUserDTO groupLeaderboardUser = new GroupLeaderboardUserDTO(10L, "test",
        Country.GERMANY,
        100);

    TournamentDTO tournamentDTO = new TournamentDTO(44L, ZonedDateTime.now(),
        ZonedDateTime.now().plusHours(1), false);

    Long groupId = 1L;
    int rank = 1;

    rewardService.initReward(groupLeaderboardUser, tournamentDTO, groupId, rank, 10000);

    verify(rewardRepository, times(1)).save(any(Reward.class));
  }

  @DisplayName("Claim reward for a tournament")
  @Test
  public void claimReward() {
    Long userId = 10L;
    Long tournamentId = 1L;
    Reward reward = new Reward();
    reward.setAmount(10000);
    reward.setClaimed(false);
    UserProgressDTO userProgressDTO = new UserProgressDTO(10L, 10000, 10, "test",
        Country.UNITED_STATES);

    when(rewardRepository.findByTournamentIdAndUserId(anyLong(), anyLong())).thenReturn(
        Optional.of(reward));
    when(progressService.depositCoins(userId, 10000)).thenReturn(userProgressDTO);
    UserProgressDTO result = rewardService.claimReward(userId, tournamentId);
    assertEquals(userProgressDTO, result);
    verify(rewardRepository, times(1)).save(reward);
  }

  @DisplayName("Claim reward throws EntityNotFoundException when reward is already claimed")
  @Test
  public void claimRewardThrowsEntityNotFoundExceptionWhenRewardIsAlreadyClaimed() {
    Long userId = 10L;
    Long tournamentId = 1L;
    Reward reward = new Reward();
    reward.setClaimed(true);
    when(rewardRepository.findByTournamentIdAndUserId(anyLong(), anyLong())).thenReturn(
        Optional.of(reward));

    assertThrows(EntityNotFoundException.class,
        () -> rewardService.claimReward(userId, tournamentId));
  }

  @DisplayName("Get reward for a user in a tournament")
  @Test
  public void getReward() {
    Long userId = 10L;
    Long tournamentId = 1L;
    Long groupId = 33L;
    Reward reward = new Reward();
    RewardDTO rewardDTO = new RewardDTO(1L, tournamentId, groupId, userId, 1, false);
    when(rewardRepository.findByTournamentIdAndUserId(anyLong(), anyLong())).thenReturn(
        Optional.of(reward));
    when(rewardDTOMapper.apply(any(Reward.class))).thenReturn(rewardDTO);

    RewardDTO result = rewardService.getReward(userId, tournamentId);

    assertEquals(rewardDTO, result);
  }

  @DisplayName("Get reward throws EntityNotFoundException when reward is not found")
  @Test
  public void getRewardThrowsEntityNotFoundExceptionWhenRewardIsNotFound() {
    Long userId = 10L;
    Long tournamentId = 1L;
    when(rewardRepository.findByTournamentIdAndUserId(anyLong(), anyLong())).thenReturn(
        Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> rewardService.getReward(userId, tournamentId));
  }

  @DisplayName("Find unclaimed reward for a user")
  @Test
  public void findUnclaimedReward() {
    Long userId = 10L;
    Long tournamentId = 1L;
    Long groupId = 33L;
    Reward reward = new Reward();
    RewardDTO rewardDTO = new RewardDTO(1L, tournamentId, groupId, userId, 1, false);
    when(rewardRepository.findAllByUserIdAndClaimed(anyLong(), anyBoolean())).thenReturn(
        Optional.of(reward));
    when(rewardDTOMapper.apply(any(Reward.class))).thenReturn(rewardDTO);
    RewardDTO result = rewardService.findUnclaimedReward(userId);
    assertEquals(rewardDTO, result);
  }

  @DisplayName("Find unclaimed reward returns null when no unclaimed reward is found")
  @Test
  public void findUnclaimedRewardReturnsNullWhenNoUnclaimedRewardIsFound() {
    Long userId = 10L;
    when(rewardRepository.findAllByUserIdAndClaimed(anyLong(), anyBoolean())).thenReturn(
        Optional.empty());

    RewardDTO result = rewardService.findUnclaimedReward(userId);

    assertNull(result);
  }
}