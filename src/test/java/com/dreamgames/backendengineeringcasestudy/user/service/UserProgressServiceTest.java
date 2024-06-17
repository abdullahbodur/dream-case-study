package com.dreamgames.backendengineeringcasestudy.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.utils.UserProgressMapper;
import com.dreamgames.backendengineeringcasestudy.user.entity.UserProgress;
import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import com.dreamgames.backendengineeringcasestudy.exceptions.EntityNotFoundException;
import com.dreamgames.backendengineeringcasestudy.exceptions.UserNotReadyForNewTournamentException;
import com.dreamgames.backendengineeringcasestudy.user.repository.UserProgressRepository;
import com.dreamgames.backendengineeringcasestudy.common.utils.EnumRandomPicker;
import com.dreamgames.backendengineeringcasestudy.common.utils.NicknameGenerator;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

public class UserProgressServiceTest {

  @InjectMocks
  private UserProgressService userProgressService;
  @Mock
  private UserProgressRepository userProgressRepository;
  @Mock
  private UserProgressMapper userProgressMapper;
  @Mock
  private EnumRandomPicker enumRandomPicker;
  @Mock
  private NicknameGenerator nicknameGenerator;

  @BeforeEach
  public void setUp() {
    openMocks(this);
    ReflectionTestUtils.setField(userProgressService, "defaultCoinAmount", 5000);
    ReflectionTestUtils.setField(userProgressService, "levelUpReward", 25);
  }

  @AfterEach
  public void tearDown() {
    userProgressService = null;
  }

  @Test
  @DisplayName("Create user successfully")
  public void testCreateUser() {
    Long userId = 1L;
    UserProgress userProgress = new UserProgress();
    userProgress.setId(userId);
    userProgress.setLevel(1);
    userProgress.setCoinBalance(5000);
    userProgress.setCountry(Country.UNITED_STATES);

    UserProgressDTO userProgressDTO = new UserProgressDTO(
        userId,
        5000,
        1,
        "nickname_1",
        Country.UNITED_STATES
    );

    when(enumRandomPicker.getRandomEnum(Country.class)).thenReturn(Country.UNITED_STATES);
    when(userProgressRepository.save(any(UserProgress.class))).thenReturn(userProgress);
    when(userProgressMapper.apply(any(UserProgress.class))).thenReturn(userProgressDTO);
    when(nicknameGenerator.makeNicknameUnique(anyString())).thenReturn("nickname_1");

    UserProgressDTO result = userProgressService.createUser(userId, "nickname");

    assertEquals(result.getId(), userId);
    assertEquals(result.getLevel(), 1);
    assertEquals(result.getCoinBalance(), 5000);
    assertEquals(result.getCountry(), Country.UNITED_STATES);

    verify(userProgressRepository, times(1)).save(argThat(
        argument -> argument.getId().equals(userId) &&
            argument.getLevel() == 1 &&
            argument.getNickname().equals("nickname_1") &&
            argument.getCoinBalance() == 5000 &&
            argument.getCountry().equals(Country.UNITED_STATES)
    ));
    verify(userProgressMapper, times(1)).apply(any(UserProgress.class));
    verify(nicknameGenerator, times(1)).makeNicknameUnique("nickname");
  }

  @Test
  @DisplayName("Update level successfully")
  public void testUpdateLevel() {
    Long userId = 1L;
    UserProgress userProgress = new UserProgress();
    userProgress.setId(userId);
    userProgress.setLevel(1);
    userProgress.setCoinBalance(5000);
    userProgress.setCountry(Country.UNITED_STATES);

    UserProgressDTO userProgressDTO = new UserProgressDTO(
        userId,
        5000 + 25,
        2,
        "nickname_1",
        Country.UNITED_STATES
    );

    when(userProgressRepository.findById(anyLong())).thenReturn(Optional.of(userProgress));
    when(userProgressRepository.save(any(UserProgress.class))).thenReturn(userProgress);
    when(userProgressMapper.apply(any(UserProgress.class))).thenReturn(userProgressDTO);
    when(enumRandomPicker.getRandomEnum(Country.class)).thenReturn(Country.UNITED_STATES);
    UserProgressDTO result = userProgressService.updateLevel(userId);

    assertEquals(result.getId(), userId);
    assertEquals(result.getLevel(), 2);
    assertEquals(result.getCoinBalance(), 5000 + 25);
    assertEquals(result.getCountry(), Country.UNITED_STATES);
    assertEquals(result.getNickname(), "nickname_1");

    verify(userProgressRepository, times(1)).findById(anyLong());
    verify(userProgressRepository, times(1)).save(any(UserProgress.class));
    verify(userProgressMapper, times(1)).apply(any(UserProgress.class));
  }

  @Test
  @DisplayName("Get user progress successfully")
  public void testGetUserProgress() {
    Long userId = 1L;
    UserProgress userProgress = new UserProgress();
    userProgress.setId(userId);
    userProgress.setLevel(1);
    userProgress.setCoinBalance(5000);
    userProgress.setCountry(Country.UNITED_STATES);

    UserProgressDTO userProgressDTO = new UserProgressDTO(
        userId,
        5000,
        1,
        "nickname_1",
        Country.UNITED_STATES
    );

    when(userProgressRepository.findById(anyLong())).thenReturn(Optional.of(userProgress));
    when(userProgressMapper.apply(any(UserProgress.class))).thenReturn(userProgressDTO);

    UserProgressDTO result = userProgressService.getUserProgress(userId);

    assertEquals(result.getId(), userId);
    assertEquals(result.getLevel(), 1);
    assertEquals(result.getCoinBalance(), 5000);
    assertEquals(result.getCountry(), Country.UNITED_STATES);
    assertEquals(result.getNickname(), "nickname_1");

    verify(userProgressRepository, times(1)).findById(anyLong());
    verify(userProgressMapper, times(1)).apply(any(UserProgress.class));
  }

  @Test
  @DisplayName("Get user progress when user not found")
  public void testGetUserProgressNotFound() {
    Long userId = 1L;
    when(userProgressRepository.findById(userId)).thenReturn(Optional.empty());
    assertThrows(
        EntityNotFoundException.class,
        () -> userProgressService.getUserProgress(userId)
    );
  }

  @Test
  @DisplayName("Update level when user not found")
  public void testUpdateLevelNotFound() {
    Long userId = 1L;
    when(userProgressRepository.findById(userId)).thenReturn(Optional.empty());
    assertThrows(
        EntityNotFoundException.class,
        () -> userProgressService.updateLevel(userId)
    );
  }

  @Test
  @DisplayName("Check user fits min requirements successfully")
  public void checkUserFitsMinRequirements() {
    UserProgressDTO userProgressDTO = new UserProgressDTO(
        1L,
        5000,
        1,
        "nickname_1",
        Country.UNITED_STATES
    );

    ReflectionTestUtils.setField(userProgressService, "minimumLevel", 2);
    ReflectionTestUtils.setField(userProgressService, "minimumCoinBalance", 6000);

    assertThrows(
        UserNotReadyForNewTournamentException.class,
        () -> userProgressService.checkUserFitsMinRequirements(userProgressDTO)
    );
  }

  @Test
  @DisplayName("Check user fits min requirements successfully")
  public void withdrawTournamentEntryTicket() {
    Long userId = 1L;
    UserProgress userProgress = new UserProgress();
    userProgress.setId(userId);
    userProgress.setLevel(1);
    userProgress.setCoinBalance(5000);
    userProgress.setCountry(Country.UNITED_STATES);

    when(userProgressRepository.findById(anyLong())).thenReturn(Optional.of(userProgress));
    when(userProgressRepository.save(any(UserProgress.class))).thenReturn(userProgress);

    userProgressService.withdrawTournamentEntryTicket(userId);

    verify(userProgressRepository, times(1)).findById(anyLong());
    verify(userProgressRepository, times(1)).save(any(UserProgress.class));
  }

  @Test
  @DisplayName("Check user fits min requirements when user does not meet the requirements for level")
  public void checkUserFitsMinRequirementsWhenUserDoesNotMeetRequirements() {
    UserProgressDTO userProgressDTO = new UserProgressDTO(
        1L,
        5000,
        1,
        "nickname_1",
        Country.UNITED_STATES
    );

    ReflectionTestUtils.setField(userProgressService, "minimumLevel", 10);
    ReflectionTestUtils.setField(userProgressService, "minimumCoinBalance", 5000);

    assertThrows(
        UserNotReadyForNewTournamentException.class,
        () -> userProgressService.checkUserFitsMinRequirements(userProgressDTO)
    );
  }

  @Test
  @DisplayName("Check user fits min requirements when user does not meet the requirements for coin balance")
  public void checkUserFitsMinRequirementsWhenUserDoesNotMeetRequirementsForCoinBalance() {
    UserProgressDTO userProgressDTO = new UserProgressDTO(
        1L,
        5000,
        1,
        "nickname_1",
        Country.UNITED_STATES
    );

    ReflectionTestUtils.setField(userProgressService, "minimumLevel", 1);
    ReflectionTestUtils.setField(userProgressService, "minimumCoinBalance", 6000);

    assertThrows(
        UserNotReadyForNewTournamentException.class,
        () -> userProgressService.checkUserFitsMinRequirements(userProgressDTO)
    );
  }

  @Test
  @DisplayName("Deposit coins successfully")
  public void depositCoins() {
    Long userId = 1L;
    UserProgress userProgress = new UserProgress();
    userProgress.setId(userId);
    userProgress.setLevel(1);
    userProgress.setCoinBalance(5000);
    userProgress.setCountry(Country.UNITED_STATES);

    UserProgressDTO userProgressDTO = new UserProgressDTO(
        userId,
        5000 + 1000,
        1,
        "nickname_1",
        Country.UNITED_STATES
    );

    when(userProgressRepository.findById(anyLong())).thenReturn(Optional.of(userProgress));
    when(userProgressRepository.save(any(UserProgress.class))).thenReturn(userProgress);
    when(userProgressMapper.apply(any(UserProgress.class))).thenReturn(userProgressDTO);

    UserProgressDTO result = userProgressService.depositCoins(userId, 1000);

    assertEquals(result.getCoinBalance(), 5000 + 1000);

    verify(userProgressRepository, times(1)).findById(anyLong());
    verify(userProgressRepository, times(1)).save(any(UserProgress.class));
    verify(userProgressMapper, times(1)).apply(any(UserProgress.class));
  }

  @Test
  @DisplayName("Deposit coins when user not found")
  public void depositCoinsNotFound() {
    Long userId = 1L;
    when(userProgressRepository.findById(userId)).thenReturn(Optional.empty());
    assertThrows(
        EntityNotFoundException.class,
        () -> userProgressService.depositCoins(userId, 1000)
    );
  }

  @Test
  @DisplayName("Withdraw coins when user not found")
  public void withdrawCoinsNotFound() {
    Long userId = 1L;
    when(userProgressRepository.findById(userId)).thenReturn(Optional.empty());
    assertThrows(
        EntityNotFoundException.class,
        () -> userProgressService.withdrawTournamentEntryTicket(userId)
    );
  }

}