package com.dreamgames.backendengineeringcasestudy.tournament.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
class LeaderboardServiceTest {

  @InjectMocks
  private LeaderboardService leaderboardService;

  @MockBean
  private RedisTemplate<String, List<CountryLeaderboardDTO>> countryLeaderboard;

  @MockBean
  private RewardService rewardService;

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
    ReflectionTestUtils.setField(leaderboardService, "groupLeaderboardPool",
        groupLeaderboardPool);
    ReflectionTestUtils.setField(leaderboardService, "userGroupPool", userGroupPool);
    ReflectionTestUtils.setField(leaderboardService, "countryLeaderboard",
        countryLeaderboard);
    ReflectionTestUtils.setField(leaderboardService, "rewardService",
        rewardService);
    ReflectionTestUtils.setField(leaderboardService, "userProgressService",
        userProgressService);
    ReflectionTestUtils.setField(leaderboardService, "participationService",
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
    leaderboardService.addUserToGroup(userProgressDTO, groupId);
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
    leaderboardService.addUserToGroup(userProgressDTO, groupId);
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
    leaderboardService.cleanUpLeaderboards();
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
    GroupLeaderboardDTO result = leaderboardService.getGroupLeaderboard(groupId);
    verify(groupLeaderboardOps, times(1)).get("groupLeaderboardPool:group:" + groupId);
    assertEquals(groupId, result.getGroupId());
    assertEquals(1, result.getLeaderboard().size());
    assertEquals(1L, result.getLeaderboard().get(0).getUserId());
    assertEquals("test", result.getLeaderboard().get(0).getNickname());
    assertEquals(Country.UNITED_STATES, result.getLeaderboard().get(0).getCountry());
    assertEquals(100, result.getLeaderboard().get(0).getTournamentScore());
  }

  @DisplayName("Get all ready group leaderboards successfully")
  @Test
  public void getAllGroupLeaderboardsSuccessfully() {
    // Mock the keys method to return a set of keys
    when(groupLeaderboardPool.keys(anyString())).thenReturn(Set.of("groupLeaderboardPool:group:1"
    ));
    when(groupLeaderboardOps.get(
        "groupLeaderboardPool:group:1"
    )).thenReturn(List.of(
        new GroupLeaderboardUserDTO(5L, "test5", Country.GERMANY, 140),
        new GroupLeaderboardUserDTO(4L, "test4", Country.UNITED_KINGDOM, 130),
        new GroupLeaderboardUserDTO(3L, "test3", Country.FRANCE, 120),
        new GroupLeaderboardUserDTO(2L, "test2", Country.TURKEY, 110),
        new GroupLeaderboardUserDTO(1L, "test", Country.UNITED_STATES, 100)
    ));
    List<GroupLeaderboardDTO> result = leaderboardService.getReadyGroupLeaderboards();
    verify(groupLeaderboardPool, times(1)).keys("groupLeaderboardPool:group:*");
    assertEquals(1, result.size());
    // order might be different
    assertEquals(1L, result.get(0).getGroupId());
    assertEquals(5, result.get(0).getLeaderboard().size());
    assertEquals(5L, result.get(0).getLeaderboard().get(0).getUserId());
    assertEquals("test5", result.get(0).getLeaderboard().get(0).getNickname());
    assertEquals(140, result.get(0).getLeaderboard().get(0).getTournamentScore());
    assertEquals(Country.GERMANY, result.get(0).getLeaderboard().get(0).getCountry());
    assertEquals(4L, result.get(0).getLeaderboard().get(1).getUserId());
    assertEquals("test4", result.get(0).getLeaderboard().get(1).getNickname());
    assertEquals(130, result.get(0).getLeaderboard().get(1).getTournamentScore());
    assertEquals(Country.UNITED_KINGDOM, result.get(0).getLeaderboard().get(1).getCountry());
    assertEquals(3L, result.get(0).getLeaderboard().get(2).getUserId());
    assertEquals("test3", result.get(0).getLeaderboard().get(2).getNickname());
    assertEquals(120, result.get(0).getLeaderboard().get(2).getTournamentScore());
    assertEquals(Country.FRANCE, result.get(0).getLeaderboard().get(2).getCountry());
    assertEquals(2L, result.get(0).getLeaderboard().get(3).getUserId());
    assertEquals("test2", result.get(0).getLeaderboard().get(3).getNickname());
    assertEquals(110, result.get(0).getLeaderboard().get(3).getTournamentScore());
    assertEquals(Country.TURKEY, result.get(0).getLeaderboard().get(3).getCountry());
    assertEquals(1L, result.get(0).getLeaderboard().get(4).getUserId());
    assertEquals("test", result.get(0).getLeaderboard().get(4).getNickname());
    assertEquals(100, result.get(0).getLeaderboard().get(4).getTournamentScore());
    assertEquals(Country.UNITED_STATES, result.get(0).getLeaderboard().get(4).getCountry());
  }

  @DisplayName("Get all ready group leaderboards with no ready groups")
  @Test
  public void getAllGroupLeaderboardsWithNoReadyGroups() {
    // Mock the keys method to return a set of keys
    when(groupLeaderboardPool.keys(anyString())).thenReturn(Set.of("groupLeaderboardPool:group:1"
    ));
    when(groupLeaderboardOps.get(
        "groupLeaderboardPool:group:1"
    )).thenReturn(List.of(
        new GroupLeaderboardUserDTO(5L, "test5", Country.GERMANY, 0),
        new GroupLeaderboardUserDTO(4L, "test4", Country.UNITED_KINGDOM, 0),
        new GroupLeaderboardUserDTO(3L, "test3", Country.FRANCE, 0),
        new GroupLeaderboardUserDTO(2L, "test2", Country.TURKEY, 0)
    ));
    List<GroupLeaderboardDTO> result = leaderboardService.getReadyGroupLeaderboards();
    verify(groupLeaderboardPool, times(1)).keys("groupLeaderboardPool:group:*");
    assertEquals(0, result.size());
  }

  @DisplayName("Get all ready group leaderboards with no groups")
  @Test
  public void getAllGroupLeaderboardsWithNoGroups() {
    // Mock the keys method to return a set of keys
    when(groupLeaderboardPool.keys(anyString())).thenReturn(Set.of());
    List<GroupLeaderboardDTO> result = leaderboardService.getReadyGroupLeaderboards();
    verify(groupLeaderboardPool, times(1)).keys("groupLeaderboardPool:group:*");
    assertEquals(0, result.size());
  }

  @DisplayName("Get ready group leaderboard when group is not ready")
  @Test
  public void getReadyGroupLeaderboardSuccessfully() {
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

    assertThrows(EntityNotFoundException.class, () -> {
      leaderboardService.getReadyGroupLeaderboard(groupId);
    });
    verify(groupLeaderboardOps, times(1)).get("groupLeaderboardPool:group:" + groupId);
  }


  @DisplayName("Get ready group leaderboard when group is ready")
  @Test
  public void getReadyGroupLeaderboardWhenGroupIsReady() {
    Long groupId = 1L;
    List<GroupLeaderboardUserDTO> groupLeaderboardUserDTOList = new ArrayList<>();
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(5L, "test5", Country.GERMANY, 140)
    );
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(4L, "test4", Country.UNITED_KINGDOM, 130)
    );
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(3L, "test3", Country.FRANCE, 120)
    );
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(2L, "test2", Country.TURKEY, 110)
    );
    groupLeaderboardUserDTOList.add(
        new GroupLeaderboardUserDTO(1L, "test", Country.UNITED_STATES, 100)
    );
    when(groupLeaderboardOps.get(
        "groupLeaderboardPool:group:" + groupId
    )).thenReturn(groupLeaderboardUserDTOList);
    when(userGroupOps.get(
        "userGroupPool:" + 1L
    )).thenReturn(1);
    GroupLeaderboardDTO result = leaderboardService.getReadyGroupLeaderboard(groupId);
    verify(groupLeaderboardOps, times(1)).get("groupLeaderboardPool:group:" + groupId);
    assertEquals(groupId, result.getGroupId());
    assertEquals(5, result.getLeaderboard().size());
    assertEquals(5L, result.getLeaderboard().get(0).getUserId());
    assertEquals("test5", result.getLeaderboard().get(0).getNickname());
    assertEquals(Country.GERMANY, result.getLeaderboard().get(0).getCountry());
    assertEquals(140, result.getLeaderboard().get(0).getTournamentScore());
    assertEquals(4L, result.getLeaderboard().get(1).getUserId());
    assertEquals("test4", result.getLeaderboard().get(1).getNickname());
    assertEquals(Country.UNITED_KINGDOM, result.getLeaderboard().get(1).getCountry());
    assertEquals(130, result.getLeaderboard().get(1).getTournamentScore());
    assertEquals(3L, result.getLeaderboard().get(2).getUserId());
    assertEquals("test3", result.getLeaderboard().get(2).getNickname());
    assertEquals(Country.FRANCE, result.getLeaderboard().get(2).getCountry());
    assertEquals(120, result.getLeaderboard().get(2).getTournamentScore());
    assertEquals(2L, result.getLeaderboard().get(3).getUserId());
    assertEquals("test2", result.getLeaderboard().get(3).getNickname());
    assertEquals(Country.TURKEY, result.getLeaderboard().get(3).getCountry());
    assertEquals(110, result.getLeaderboard().get(3).getTournamentScore());
    assertEquals(1L, result.getLeaderboard().get(4).getUserId());
    assertEquals("test", result.getLeaderboard().get(4).getNickname());
    assertEquals(Country.UNITED_STATES, result.getLeaderboard().get(4).getCountry());
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
    GroupLeaderboardUserRankDTO result = leaderboardService.getCurrentTournamentGroupUserRank(
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
    GroupLeaderboardUserRankDTO result = leaderboardService.getCurrentTournamentGroupUserRank(
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
    GroupLeaderboardUserRankDTO result = leaderboardService.getCurrentTournamentGroupUserRank(
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
    leaderboardService.addScoreForGroupLeaderboard(userId, score);
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
      leaderboardService.addScoreForGroupLeaderboard(userId, score);
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
      leaderboardService.addScoreForGroupLeaderboard(userId, score);
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
    leaderboardService.addScoreForCountryLeaderboard(country, score);
    verify(countryLeaderboardOps, times(1)).set(
        eq("countryLeaderboardPool"),
        argThat(arg -> arg.size() == 2 && arg.get(0).getCountry().equals(Country.UNITED_STATES)
            && arg.get(0).getTotalScore() == 200
            && arg.get(1).getCountry().equals(Country.TURKEY)
            && arg.get(1).getTotalScore() == 110));
  }

  @DisplayName("Add score for country in country leaderboard with null country leaderboard")
  @Test
  public void addScoreForCountryLeaderboardWithNullCountryLeaderboard() {
    Country country = Country.UNITED_STATES;
    int score = 100;
    when(countryLeaderboardOps.get(
        "countryLeaderboardPool"
    )).thenReturn(null);
    leaderboardService.addScoreForCountryLeaderboard(country, score);
    verify(countryLeaderboardOps, times(2)).set(
        eq("countryLeaderboardPool"), argThat(arg -> arg.size() == 5));
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
    when(rewardService.getReward(userId, tournamentId)).thenReturn(rewardDTO);
    when(userProgressService.getUserProgress(userId)).thenReturn(
        new UserProgressDTO(99L, 10000, 10, "test99",
            Country.UNITED_STATES));
    when(participationService.getParticipation(userId, groupId)).thenReturn(participationDTO);
    GroupLeaderboardUserRankDTO result = leaderboardService.getHistoricalTournamentUserRank(
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
    List<CountryLeaderboardDTO> result = leaderboardService.getCountryLeaderboard();
    verify(countryLeaderboardOps, times(1)).get("countryLeaderboardPool");
    assertEquals(2, result.size());
    assertEquals(Country.UNITED_STATES, result.get(0).getCountry());
    assertEquals(100, result.get(0).getTotalScore());
    assertEquals(Country.TURKEY, result.get(1).getCountry());
    assertEquals(110, result.get(1).getTotalScore());
  }

  @DisplayName("Get country leaderboard with null country leaderboard")
  @Test
  public void getCountryLeaderboardWithNullCountryLeaderboard() {
    when(countryLeaderboardOps.get(
        "countryLeaderboardPool"
    )).thenReturn(null);
    List<CountryLeaderboardDTO> result = leaderboardService.getCountryLeaderboard();
    verify(countryLeaderboardOps, times(1)).get("countryLeaderboardPool");
    assertEquals(List.of(Country.values()).size(), result.size());
  }

  @DisplayName("Get groupId from userGroupPool")
  @Test
  public void getGroupIdFromUserGroupPool() {
    Long userId = 1L;
    when(userGroupOps.get(
        "userGroupPool:" + userId
    )).thenReturn(1);
    Long result = leaderboardService.getGroupIdForUser(userId);
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
    boolean result = leaderboardService.isUserGroupReady(groupId);
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
    boolean result = leaderboardService.isUserGroupReady(groupId);
    assertTrue(result);
  }


  @DisplayName("Get group is ready when leaderboard is null")
  @Test
  public void getGroupIsReadyWhenLeaderboardIsNull() {
    Long groupId = 1L;
    when(groupLeaderboardOps.get(
        "groupLeaderboardPool:group:" + groupId
    )).thenReturn(null);
    boolean result = leaderboardService.isUserGroupReady(groupId);
    assertFalse(result);
  }


}