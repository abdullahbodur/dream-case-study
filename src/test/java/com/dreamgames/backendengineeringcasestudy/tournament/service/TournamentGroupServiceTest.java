package com.dreamgames.backendengineeringcasestudy.tournament.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.RewardDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentGroupDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.utils.GroupDTOMapper;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.TournamentGroup;
import com.dreamgames.backendengineeringcasestudy.user.entity.UserProgress;
import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import com.dreamgames.backendengineeringcasestudy.exceptions.EntityNotFoundException;
import com.dreamgames.backendengineeringcasestudy.exceptions.TournamentNotStartedException;
import com.dreamgames.backendengineeringcasestudy.exceptions.UserNotReadyForNewTournamentException;
import com.dreamgames.backendengineeringcasestudy.tournament.repository.GroupRepository;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TournamentGroupServiceTest {

  @InjectMocks
  private TournamentGroupService tournamentGroupService;

  @Mock
  private TournamentGroupPoolService groupPoolService;

  @Mock
  private GroupRepository groupRepository;

  @Mock
  private GroupDTOMapper groupDTOMapper;

  @Mock
  private ParticipationService participationService;

  @Mock
  private TournamentRewardService rewardService;

  @Mock
  private TournamentLeaderboardService leaderboardService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void tearDown() {
    tournamentGroupService = null;
  }

  @DisplayName("User enters tournament successfully with creating new group")
  @Test
  public void userEntersTournamentSuccessfully() {
    UserProgressDTO userProgressDTO = new UserProgressDTO(1L, 10000, 20, "nickname",
        Country.UNITED_STATES);

    TournamentDTO tournamentDTO = new TournamentDTO(1L, ZonedDateTime.now(),
        ZonedDateTime.now().plusHours(1), false);

    Tournament tournament = new Tournament();
    tournament.setId(1L);
    TournamentGroup tournamentGroup = new TournamentGroup();
    tournamentGroup.setId(1L);
    tournamentGroup.setTournament(tournament);
    tournamentGroup.setReady(false);

    TournamentGroupDTO tournamentGroupDTO = new TournamentGroupDTO(
        1L,
        99L,
        false);

    when(rewardService.findUnclaimedReward(userProgressDTO.getId())).thenReturn(null);
    when(leaderboardService.getGroupIdForUser(userProgressDTO.getId())).thenReturn(null);
    when(groupPoolService.getAvailableGroup(userProgressDTO.getCountry())).thenReturn(null);
    when(groupRepository.save(any(TournamentGroup.class))).thenReturn(tournamentGroup);
    when(groupDTOMapper.apply(any(TournamentGroup.class))).thenReturn(tournamentGroupDTO);

    tournamentGroupService.enterTournament(userProgressDTO, tournamentDTO);

    verify(participationService, times(1)).createParticipation(any(UserProgress.class),
        any(TournamentGroupDTO.class));
    verify(groupPoolService, times(1)).addGroupToPool(any(Long.class), anyList());
    verify(groupRepository, times(1)).save(argThat(
        arg -> arg.getTournament().getId().equals(tournament.getId()) && !arg.isReady()));
    verify(groupDTOMapper, times(1)).apply(argThat(
        arg -> arg.getTournament().getId().equals(tournament.getId()) && !arg.isReady()));
  }

  @Test
  @DisplayName("User enters tournament successfully with existing group")
  public void userEntersTournamentSuccessfullyWithExistingGroup() {
    UserProgressDTO userProgressDTO = new UserProgressDTO(1L, 10000, 20, "nickname",
        Country.UNITED_STATES);
    TournamentDTO tournamentDTO = new TournamentDTO(1L, ZonedDateTime.now(),
        ZonedDateTime.now().plusHours(1), false);
    TournamentGroup tournamentGroup = new TournamentGroup();
    tournamentGroup.setId(1L);
    tournamentGroup.setTournament(new Tournament());
    tournamentGroup.setReady(false);
    TournamentGroupDTO tournamentGroupDTO = new TournamentGroupDTO(1L, 99L, false);

    when(rewardService.findUnclaimedReward(userProgressDTO.getId())).thenReturn(null);
    when(leaderboardService.getGroupIdForUser(userProgressDTO.getId())).thenReturn(null);
    when(groupRepository.findById(1L)).thenReturn(java.util.Optional.of(tournamentGroup));
    when(groupPoolService.getAvailableGroup(userProgressDTO.getCountry())).thenReturn(1L);
    when(groupDTOMapper.apply(tournamentGroup)).thenReturn(tournamentGroupDTO);

    tournamentGroupService.enterTournament(userProgressDTO, tournamentDTO);

    verify(participationService, times(1)).createParticipation(any(UserProgress.class),
        any(TournamentGroupDTO.class));
    verify(groupPoolService, times(0)).addGroupToPool(any(Long.class), anyList());
    verify(groupRepository, times(0)).save(any(TournamentGroup.class));
    verify(groupDTOMapper, times(1)).apply(tournamentGroup);
  }

  @Test
  @DisplayName("User fails to enter tournament due to group not found")
  public void userFailsToEnterTournamentDueToGroupNotFound() {
    UserProgressDTO userProgressDTO = new UserProgressDTO(1L, 10000, 20, "nickname",
        Country.UNITED_STATES);
    TournamentDTO tournamentDTO = new TournamentDTO(1L, ZonedDateTime.now(),
        ZonedDateTime.now().plusHours(1), false);
    TournamentGroup tournamentGroup = new TournamentGroup();
    tournamentGroup.setId(1L);
    tournamentGroup.setTournament(new Tournament());
    tournamentGroup.setReady(false);
    TournamentGroupDTO tournamentGroupDTO = new TournamentGroupDTO(1L, 99L, false);

    when(rewardService.findUnclaimedReward(userProgressDTO.getId())).thenReturn(null);
    when(leaderboardService.getGroupIdForUser(userProgressDTO.getId())).thenReturn(null);
    when(groupRepository.findById(1L)).thenReturn(java.util.Optional.empty());
    when(groupPoolService.getAvailableGroup(userProgressDTO.getCountry())).thenReturn(1L);
    when(groupDTOMapper.apply(tournamentGroup)).thenReturn(tournamentGroupDTO);


    assertThrows(EntityNotFoundException.class,
        () -> tournamentGroupService.enterTournament(userProgressDTO, tournamentDTO));
  }

  @DisplayName("User fails to enter tournament due to unclaimed reward")
  @Test
  public void userFailsToEnterTournamentDueToUnclaimedReward() {
    UserProgressDTO userProgressDTO = new UserProgressDTO(1L, 10000, 20, "nickname",
        Country.UNITED_STATES);
    TournamentDTO tournamentDTO = new TournamentDTO(99L, ZonedDateTime.now(),
        ZonedDateTime.now().plusHours(1), false);
    RewardDTO unClaimedReward = new RewardDTO(1L, 99L, 3L, 1L, 2, false);

    when(rewardService.findUnclaimedReward(userProgressDTO.getId())).thenReturn(unClaimedReward);

    assertThrows(UserNotReadyForNewTournamentException.class,
        () -> tournamentGroupService.enterTournament(userProgressDTO, tournamentDTO));
  }

  @DisplayName("User fails to enter tournament due to already in a group")
  @Test
  public void userFailsToEnterTournamentDueToAlreadyInGroup() {
    UserProgressDTO userProgressDTO = new UserProgressDTO(1L, 10000, 20, "nickname",
        Country.UNITED_STATES);
    TournamentDTO tournamentDTO = new TournamentDTO(99L,
        ZonedDateTime.now(), ZonedDateTime.now().plusHours(1), false);

    when(rewardService.findUnclaimedReward(userProgressDTO.getId())).thenReturn(null);

    when(leaderboardService.getGroupIdForUser(userProgressDTO.getId())).thenReturn(1L);

    assertThrows(UserNotReadyForNewTournamentException.class,
        () -> tournamentGroupService.enterTournament(userProgressDTO, tournamentDTO));
  }

  @DisplayName("User fails to enter tournament due to tournament not active")
  @Test
  public void userFailsToEnterTournamentDueToTournamentNotActive() {
    UserProgressDTO userProgressDTO = new UserProgressDTO(1L, 10000, 20, "nickname",
        Country.UNITED_STATES);
    assertThrows(TournamentNotStartedException.class,
        () -> tournamentGroupService.enterTournament(userProgressDTO, null));
  }
}