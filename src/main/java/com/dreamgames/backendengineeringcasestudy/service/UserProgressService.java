package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.utils.UserProgressMapper;
import com.dreamgames.backendengineeringcasestudy.entity.UserProgress;
import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import com.dreamgames.backendengineeringcasestudy.exceptions.EntityNotFoundException;
import com.dreamgames.backendengineeringcasestudy.exceptions.UserNotReadyForNewTournamentException;
import com.dreamgames.backendengineeringcasestudy.repository.UserProgressRepository;
import com.dreamgames.backendengineeringcasestudy.utils.EnumRandomPicker;
import com.dreamgames.backendengineeringcasestudy.utils.NicknameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user progress. This service is responsible for creating, updating, and
 * retrieving user progress records. It also checks if a user meets the minimum requirements for a
 * new tournament, and handles the withdrawal and deposit of coins for a user.
 */
@Service
@RequiredArgsConstructor
public class UserProgressService {

  private final UserProgressRepository userProgressRepository;

  private final UserProgressMapper userProgressMapper;

  private final EnumRandomPicker enumRandomPicker;

  private final NicknameGenerator nicknameGenerator;

  @Value("${business.user.default.coinAmount}")
  private double defaultCoinAmount;

  @Value("${business.user.onLevelUp.coinAmount}")
  private double levelUpReward;

  @Value("${business.tournament.requirements.minimumLevel}")
  private int minimumLevel;

  @Value("${business.tournament.requirements.minimumCoinBalance}")
  private double minimumCoinBalance;


  /**
   * method is used to create a new UserProgress record.
   *
   * @param userId ID of the user for whom the progress record is being created.
   * @return UserProgressDTO returns a DTO object that contains the user's progress details.
   */
  public UserProgressDTO createUser(Long userId, String nickname) {
    UserProgress userProgress = new UserProgress();
    userProgress.setId(userId);
    userProgress.setNickname(nicknameGenerator.makeNicknameUnique(nickname));
    userProgress.setLevel(1);
    userProgress.setCoinBalance(defaultCoinAmount);
    userProgress.setCountry(enumRandomPicker.getRandomEnum(Country.class));
    userProgressRepository.save(userProgress);
    return userProgressMapper.apply(userProgress);
  }

  /**
   * method is used to update the level of a user.
   *
   * @param userId ID of the user whose level is to be updated.
   * @return UserProgressDTO returns a DTO object that contains the user's progress details.
   */
  public UserProgressDTO updateLevel(Long userId) {
    UserProgress userProgress = userProgressRepository.findById(userId).orElseThrow(
        () -> new EntityNotFoundException("User progress record not found for user ID: " + userId)
    );
    userProgress.setLevel(userProgress.getLevel() + 1);
    userProgress.setCoinBalance(userProgress.getCoinBalance() + levelUpReward);
    userProgressRepository.save(userProgress);
    return userProgressMapper.apply(userProgress);
  }

  /**
   * method is used to retrieve a UserProgress record by its ID.
   *
   * @param id ID of the UserProgress record.
   * @return UserProgressDTO returns a DTO object that contains the user's progress details.
   */
  public UserProgressDTO getUserProgress(Long id) {
    UserProgress userProgress = userProgressRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("User progress record not found for user ID: " + id)
    );
    return userProgressMapper.apply(userProgress);
  }

  /**
   * method is used to check if a user meets the minimum requirements for a new tournament.
   *
   * @param progressDTO the user's progress details
   */
  public void checkUserFitsMinRequirements(UserProgressDTO progressDTO) {
    if (
        progressDTO.getLevel() < minimumLevel ||
            progressDTO.getCoinBalance() < minimumCoinBalance
    ) {
      throw new UserNotReadyForNewTournamentException(
          "User: " + progressDTO.getId()
              + " does not meet the minimum requirements for a new tournament");
    }
  }

  /**
   * Withdraws the entry ticket for a tournament from a user's coin balance.
   *
   * @param userId ID of the user from whom the entry ticket is to be withdrawn.
   */
  public void withdrawTournamentEntryTicket(Long userId) {
    UserProgress userProgress = userProgressRepository.findById(userId).orElseThrow(
        () -> new EntityNotFoundException("User progress record not found for user ID: " + userId)
    );
    userProgress.setCoinBalance(userProgress.getCoinBalance() - minimumCoinBalance);
    userProgressRepository.save(userProgress);
    userProgressMapper.apply(userProgress);
  }

  /**
   * Deposits a certain amount of coins into a user's coin balance.
   *
   * @param userId ID of the user to whom the coins are to be deposited.
   * @param amount the amount of coins to be deposited.
   */
  public void depositCoins(Long userId, double amount) {
    UserProgress userProgress = userProgressRepository.findById(userId).orElseThrow(
        () -> new EntityNotFoundException("User progress record not found for user ID: " + userId)
    );
    userProgress.setCoinBalance(userProgress.getCoinBalance() + amount);
    userProgressRepository.save(userProgress);
  }
}
