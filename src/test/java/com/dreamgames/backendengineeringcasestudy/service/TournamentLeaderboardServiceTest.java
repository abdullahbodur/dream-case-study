package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.dto.response.CountryLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.GroupLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.GroupLeaderboardUserDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.GroupLeaderboardUserRankDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.ParticipationDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.RewardDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import java.util.ArrayList;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TournamentLeaderboardServiceTest {

  @InjectMocks
  private TournamentLeaderboardService tournamentLeaderboardService;

  @MockBean
  private RedisTemplate<String, List<CountryLeaderboardDTO>> countryLeaderboard;

  @MockBean
  private TournamentRewardService tournamentRewardService;

  @MockBean
  private UserProgressService userProgressService;

  @MockBean
  private ParticipationService participationService;

  @MockBean
  private RedisTemplate<String, List<GroupLeaderboardUserDTO>> groupLeaderboardPool;

  @MockBean
  private RedisTemplate<String, Number> userGroupPool;

  @MockBean
  private ValueOperations<String, List<GroupLeaderboardUserDTO>> groupLeaderboardOps;

  @MockBean
  private ValueOperations<String, Number> userGroupOps;

  @MockBean
  private ValueOperations<String, List<CountryLeaderboardDTO>> countryLeaderboardOps;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    ReflectionTestUtils.setField(tournamentLeaderboardService, "groupLeaderboardPool",
        groupLeaderboardPool);
    ReflectionTestUtils.setField(tournamentLeaderboardService, "userGroupPool", userGroupPool);
    ReflectionTestUtils.setField(tournamentLeaderboardService, "countryLeaderboard",
        countryLeaderboard);
    ReflectionTestUtils.setField(tournamentLeaderboardService, "tournamentRewardService",
        tournamentRewardService);
    ReflectionTestUtils.setField(tournamentLeaderboardService, "userProgressService",
        userProgressService);
    ReflectionTestUtils.setField(tournamentLeaderboardService, "participationService",
        participationService);
    when(countryLeaderboard.opsForValue()).thenReturn(countryLeaderboardOps);
    when(groupLeaderboardPool.opsForValue()).thenReturn(groupLeaderboardOps);
    when(userGroupPool.opsForValue()).thenReturn(userGroupOps);
  }

  @DisplayName("Add user to group successfully")
  @Test
  public void addUserToGroupSuccessfully() {
    Long groupId = 1L;
    UserProgressDTO userProgressDTO = new UserProgressDTO(99L, 10000, 10, "test",
        Country.UNITED_STATES);
    List<GroupLeaderboardUserDTO> groupLeaderboardUserDTOList = new ArrayList<>();
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(
            1L,
            "test",
            Country.UNITED_STATES,
            100));
    when(groupLeaderboardOps.get(
        "groupLeaderboardPool:group:" + groupId
    )).thenReturn(groupLeaderboardUserDTOList);
    when(userGroupOps.get(
        "userGroupPool:" + userProgressDTO.getId()
    )).thenReturn(null);
    tournamentLeaderboardService.addUserToGroup(userProgressDTO, groupId);
    verify(groupLeaderboardOps, times(1)).set(
        eq("groupLeaderboardPool:group:" + groupId),
        argThat(arg -> arg.size() == 2 && arg.get(1).getUserId().equals(userProgressDTO.getId())
            && arg.get(1).getNickname().equals(userProgressDTO.getNickname())
            && arg.get(1).getCountry().equals(userProgressDTO.getCountry())));
    verify(userGroupOps, times(1)).set(anyString(), any(Number.class));
  }


  @DisplayName("Add user to group if group is newly created")
  @Test
  public void addUserToGroupIfGroupIsNewlyCreated() {
    Long groupId = 1L;
    UserProgressDTO userProgressDTO = new UserProgressDTO(99L, 10000, 10, "test",
        Country.UNITED_STATES);
    List<GroupLeaderboardUserDTO> groupLeaderboardUserDTOList = new ArrayList<>();
    when(groupLeaderboardOps.get(
        "groupLeaderboardPool:group:" + groupId
    )).thenReturn(null);
    when(userGroupOps.get(
        "userGroupPool:" + userProgressDTO.getId()
    )).thenReturn(null);
    tournamentLeaderboardService.addUserToGroup(userProgressDTO, groupId);
    verify(groupLeaderboardOps, times(1)).set(
        eq("groupLeaderboardPool:group:" + groupId),
        argThat(arg -> arg.size() == 1 && arg.get(0).getUserId().equals(userProgressDTO.getId())
            && arg.get(0).getNickname().equals(userProgressDTO.getNickname())
            && arg.get(0).getCountry().equals(userProgressDTO.getCountry())));
    verify(userGroupOps, times(1)).set(anyString(), any(Number.class));
  }

  @DisplayName("Clean up Leaderboards successfully")
  @Test
  public void cleanUpLeaderboardsSuccessfully() {
    // Mock the keys method to return a set of keys
    when(groupLeaderboardPool.keys(anyString())).thenReturn(Set.of("groupLeaderboardPool:group:1"));
    when(userGroupPool.keys(anyString())).thenReturn(Set.of("userGroupPool:1"));
    tournamentLeaderboardService.cleanUpLeaderboards();
    verify(countryLeaderboardOps, times(1)).set(
        eq("countryLeaderboardPool"),
        argThat(arg -> arg.size() == 5 && arg.get(0).getTotalScore() == 0
            && arg.get(1).getTotalScore() == 0 &&
            arg.get(2).getTotalScore() == 0 && arg.get(3).getTotalScore() == 0
            && arg.get(4).getTotalScore() == 0));

    // Verify the delete method is called with the correct keys
    verify(groupLeaderboardPool, times(1)).delete(Set.of("groupLeaderboardPool:group:1"));
    verify(userGroupPool, times(1)).delete(Set.of("userGroupPool:1"));
  }

  @DisplayName("Get group leaderboard successfully")
  @Test
  public void getGroupLeaderboardSuccessfully() {
    Long groupId = 1L;
    List<GroupLeaderboardUserDTO> groupLeaderboardUserDTOList = new ArrayList<>();
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(
            1L,
            "test",
            Country.UNITED_STATES,
            100));
    when(groupLeaderboardOps.get(
        "groupLeaderboardPool:group:" + groupId
    )).thenReturn(groupLeaderboardUserDTOList);
    GroupLeaderboardDTO result = tournamentLeaderboardService.getGroupLeaderboard(groupId);
    verify(groupLeaderboardOps, times(1)).get("groupLeaderboardPool:group:" + groupId);
    assertEquals(groupId, result.getGroupId());
    assertEquals(1, result.getLeaderboard().size());
    assertEquals(1L, result.getLeaderboard().get(0).getUserId());
    assertEquals("test", result.getLeaderboard().get(0).getNickname());
    assertEquals(Country.UNITED_STATES, result.getLeaderboard().get(0).getCountry());
    assertEquals(100, result.getLeaderboard().get(0).getTournamentScore());
  }

  @DisplayName("Get all group leaderboards successfully")
  @Test
  public void getAllGroupLeaderboardsSuccessfully() {
    // Mock the keys method to return a set of keys
    when(groupLeaderboardPool.keys(anyString())).thenReturn(Set.of("groupLeaderboardPool:group:1"
    ));
    when(groupLeaderboardOps.get(
        "groupLeaderboardPool:group:1"
    )).thenReturn(List.of(new GroupLeaderboardUserDTO(1L, "test", Country.UNITED_STATES, 100)
    ));
    List<GroupLeaderboardDTO> result = tournamentLeaderboardService.getGroupLeaderboards();
    verify(groupLeaderboardPool, times(1)).keys("*");
    assertEquals(1, result.size());
    // order might be different
    assertEquals(1L, result.get(0).getGroupId());
    assertEquals(1, result.get(0).getLeaderboard().size());
    assertEquals(1L, result.get(0).getLeaderboard().get(0).getUserId());
    assertEquals("test", result.get(0).getLeaderboard().get(0).getNickname());
    assertEquals(Country.UNITED_STATES, result.get(0).getLeaderboard().get(0).getCountry());
  }

  @DisplayName("Get current tournament group user rank successfully")
  @Test
  public void getCurrentTournamentGroupUserRankSuccessfully() {
    Long groupId = 1L;
    Long userId = 1L;
    List<GroupLeaderboardUserDTO> groupLeaderboardUserDTOList = new ArrayList<>();
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(
            2L,
            "test2",
            Country.UNITED_STATES,
            110
        )
    );
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(
            1L,
            "test",
            Country.UNITED_STATES,
            100));
    when(groupLeaderboardOps.get(
        "groupLeaderboardPool:group:" + groupId
    )).thenReturn(groupLeaderboardUserDTOList);
    GroupLeaderboardUserRankDTO result = tournamentLeaderboardService.getCurrentTournamentGroupUserRank(
        groupId, userId);
    verify(groupLeaderboardOps, times(1)).get("groupLeaderboardPool:group:" + groupId);
    assertEquals(2, result.getRank());
    assertEquals(1L, result.getUser().getUserId());
    assertEquals("test", result.getUser().getNickname());
    assertEquals(Country.UNITED_STATES, result.getUser().getCountry());
    assertEquals(100, result.getUser().getTournamentScore());
  }

  @DisplayName("Get current tournament group user is not found in group leaderboard")
  @Test
  public void getCurrentTournamentGroupUserIsNotFoundInGroupLeaderboard() {
    Long groupId = 1L;
    Long userId = 99L;
    List<GroupLeaderboardUserDTO> groupLeaderboardUserDTOList = new ArrayList<>();
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(
            1L,
            "test",
            Country.UNITED_STATES,
            100));
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(
            2L,
            "test2",
            Country.UNITED_STATES,
            110
        )
    );
    when(groupLeaderboardOps.get(
        "groupLeaderboardPool:group:" + groupId
    )).thenReturn(groupLeaderboardUserDTOList);
    GroupLeaderboardUserRankDTO result = tournamentLeaderboardService.getCurrentTournamentGroupUserRank(
        groupId, userId);
    verify(groupLeaderboardOps, times(1)).get("groupLeaderboardPool:group:" + groupId);
    assertNull(result);
  }

  @DisplayName("Get current tournament group user rank with null group leaderboard")
  @Test
  public void getCurrentTournamentGroupUserRankWithNullGroupLeaderboard() {
    Long groupId = 1L;
    Long userId = 1L;
    when(groupLeaderboardOps.get(
        "groupLeaderboardPool:group:" + groupId
    )).thenReturn(null);
    GroupLeaderboardUserRankDTO result = tournamentLeaderboardService.getCurrentTournamentGroupUserRank(
        groupId, userId);
    verify(groupLeaderboardOps, times(1)).get("groupLeaderboardPool:group:" + groupId);
    assertEquals(null, result);
  }

  @DisplayName("Add score for user in group leaderboard successfully")
  @Test
  public void addScoreForUserInGroupLeaderboardSuccessfully() {
    Long userId = 1L;
    int score = 100;
    List<GroupLeaderboardUserDTO> groupLeaderboardUserDTOList = new ArrayList<>();
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(
            1L,
            "test",
            Country.UNITED_STATES,
            100));
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(
            2L,
            "test2",
            Country.UNITED_STATES,
            110
        )
    );
    when(groupLeaderboardOps.get(
        "groupLeaderboardPool:group:" + userId
    )).thenReturn(groupLeaderboardUserDTOList);
    when(userGroupOps.get(
        "userGroupPool:" + userId
    )).thenReturn(1);
    tournamentLeaderboardService.addScoreForGroupLeaderboard(userId, score);
    verify(groupLeaderboardOps, times(1)).set(
        eq("groupLeaderboardPool:group:" + userId),
        argThat(arg -> arg.size() == 2 && arg.get(0).getUserId().equals(userId)
            && arg.get(0).getNickname().equals("test")
            && arg.get(0).getCountry().equals(Country.UNITED_STATES)
            && arg.get(0).getTournamentScore() == 200
            && arg.get(1).getUserId().equals(2L)
            && arg.get(1).getNickname().equals("test2")
            && arg.get(1).getCountry().equals(Country.UNITED_STATES)
            && arg.get(1).getTournamentScore() == 110));
  }

  @DisplayName("Add score for user in group leaderboard with null group leaderboard")
  @Test
  public void addScoreForUserInGroupLeaderboardWithNullGroupLeaderboard() {
    Long userId = 1L;
    int score = 100;
    when(userGroupOps.get(
        "userGroupPool:" + userId
    )).thenReturn(1);
    when(groupLeaderboardOps.get(
        "groupLeaderboardPool:group:" + userId
    )).thenReturn(null);
    assertThrows(IllegalArgumentException.class, () -> {
      tournamentLeaderboardService.addScoreForGroupLeaderboard(userId, score);
    });
  }

  @DisplayName("Add score for user in group when user is not found in any group")
  @Test
  public void addScoreForUserInGroupWhenUserIsNotFoundInAnyGroup() {
    Long userId = 1L;
    int score = 100;
    when(userGroupOps.get(
        "userGroupPool:" + userId
    )).thenReturn(null);
    assertThrows(IllegalArgumentException.class, () -> {
      tournamentLeaderboardService.addScoreForGroupLeaderboard(userId, score);
    });
  }

  @DisplayName("Add score for country in country leaderboard successfully")
  @Test
  public void addScoreForCountryLeaderboardSuccessfully() {
    Country country = Country.UNITED_STATES;
    int score = 100;
    List<CountryLeaderboardDTO> countryLeaderboardDTOList = new ArrayList<>();
    countryLeaderboardDTOList.add(
        new CountryLeaderboardDTO(
            Country.UNITED_STATES,
            100));
    countryLeaderboardDTOList.add(
        new CountryLeaderboardDTO(
            Country.TURKEY,
            110
        )
    );
    when(countryLeaderboardOps.get(
        "countryLeaderboardPool"
    )).thenReturn(countryLeaderboardDTOList);
    tournamentLeaderboardService.addScoreForCountryLeaderboard(country, score);
    verify(countryLeaderboardOps, times(1)).set(
        eq("countryLeaderboardPool"),
        argThat(arg -> arg.size() == 2 && arg.get(0).getCountry().equals(Country.UNITED_STATES)
            && arg.get(0).getTotalScore() == 200
            && arg.get(1).getCountry().equals(Country.TURKEY)
            && arg.get(1).getTotalScore() == 110));
  }

  @DisplayName("Get historical group leaderboard successfully")
  @Test
  public void getHistoricalGroupLeaderboardSuccessfully() {
    Long groupId = 1L;
    Long userId = 99L;
    Long tournamentId = 22L;
    RewardDTO rewardDTO = new RewardDTO(
        1L,
        tournamentId,
        groupId,
        userId,
        2,
        false
    );
    ParticipationDTO participationDTO = new ParticipationDTO(
        11L,
        userId,
        groupId,
        100
    );
    when(tournamentRewardService.getReward(userId, tournamentId)).thenReturn(rewardDTO);
    when(userProgressService.getUserProgress(userId)).thenReturn(
        new UserProgressDTO(99L, 10000, 10, "test99",
            Country.UNITED_STATES));
    when(participationService.getParticipation(userId, groupId)).thenReturn(participationDTO);
    GroupLeaderboardUserRankDTO result = tournamentLeaderboardService.getHistoricalTournamentUserRank(
        tournamentId, userId);
    assertEquals(2, result.getRank());
    assertEquals(99L, result.getUser().getUserId());
    assertEquals("test99", result.getUser().getNickname());
    assertEquals(Country.UNITED_STATES, result.getUser().getCountry());
    assertEquals(100, result.getUser().getTournamentScore());
  }

  @DisplayName("Get country leaderboard successfully")
  @Test
  public void getCountryLeaderboardSuccessfully() {
    List<CountryLeaderboardDTO> countryLeaderboardDTOList = new ArrayList<>();
    countryLeaderboardDTOList.add(
        new CountryLeaderboardDTO(
            Country.UNITED_STATES,
            100));
    countryLeaderboardDTOList.add(
        new CountryLeaderboardDTO(
            Country.TURKEY,
            110
        )
    );
    when(countryLeaderboardOps.get(
        "countryLeaderboardPool"
    )).thenReturn(countryLeaderboardDTOList);
    List<CountryLeaderboardDTO> result = tournamentLeaderboardService.getCountryLeaderboard();
    verify(countryLeaderboardOps, times(1)).get("countryLeaderboardPool");
    assertEquals(2, result.size());
    assertEquals(Country.UNITED_STATES, result.get(0).getCountry());
    assertEquals(100, result.get(0).getTotalScore());
    assertEquals(Country.TURKEY, result.get(1).getCountry());
    assertEquals(110, result.get(1).getTotalScore());
  }

  @DisplayName("Get groupId from userGroupPool")
  @Test
  public void getGroupIdFromUserGroupPool() {
    Long userId = 1L;
    when(userGroupOps.get(
        "userGroupPool:" + userId
    )).thenReturn(1);
    Long result = tournamentLeaderboardService.getGroupIdForUser(userId);
    assertEquals(1L, result);
  }

  @DisplayName("Get group is ready to tournament when is not ready")
  @Test
  public void getGroupIsReadyToTournament() {
    Long groupId = 1L;
    List<GroupLeaderboardUserDTO> groupLeaderboardUserDTOList = new ArrayList<>();
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(
            1L,
            "test",
            Country.UNITED_STATES,
            100));
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(
            2L,
            "test2",
            Country.UNITED_STATES,
            110
        )
    );
    when(groupLeaderboardOps.get(
        "groupLeaderboardPool:group:" + groupId
    )).thenReturn(groupLeaderboardUserDTOList);
    boolean result = tournamentLeaderboardService.isUserGroupReady(groupId);
    assertEquals(false, result);
  }

  @DisplayName("Get group is ready to tournament when is ready")
  @Test
  public void getGroupIsReadyToTournamentWhenIsReady() {
    Long groupId = 1L;
    List<GroupLeaderboardUserDTO> groupLeaderboardUserDTOList = new ArrayList<>();
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(
            1L,
            "test",
            Country.UNITED_STATES,
            100));
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(
            2L,
            "test2",
            Country.UNITED_STATES,
            110
        )
    );
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(
            3L,
            "test3",
            Country.UNITED_STATES,
            120
        )
    );
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(
            4L,
            "test4",
            Country.UNITED_STATES,
            130
        )
    );
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(
            5L,
            "test5",
            Country.UNITED_STATES,
            140
        )
    );
    when(groupLeaderboardOps.get(
        "groupLeaderboardPool:group:" + groupId
    )).thenReturn(groupLeaderboardUserDTOList);
    when(userGroupOps.get(
        "userGroupPool:" + 1L
    )).thenReturn(1);
    when(userGroupOps.get(
        "userGroupPool:" + 2L
    )).thenReturn(1);
    boolean result = tournamentLeaderboardService.isUserGroupReady(groupId);
    assertTrue(result);
  }


  @DisplayName("Get group is ready when leaderboard is null")
  @Test
  public void getGroupIsReadyWhenLeaderboardIsNull() {
    Long groupId = 1L;
    when(groupLeaderboardOps.get(
        "groupLeaderboardPool:group:" + groupId
    )).thenReturn(null);
    boolean result = tournamentLeaderboardService.isUserGroupReady(groupId);
    assertFalse(result);
  }


}