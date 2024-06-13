package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.utils.UserProgressMapper;
import com.dreamgames.backendengineeringcasestudy.entity.UserProgress;
import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import com.dreamgames.backendengineeringcasestudy.repository.UserProgressRepository;
import com.dreamgames.backendengineeringcasestudy.utils.EnumRandomPicker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProgressService {

  private final UserProgressRepository userProgressRepository;

  private final UserProgressMapper userProgressMapper;

  private final EnumRandomPicker enumRandomPicker;

  @Value("${business.user.default.coinAmount}")
  private double defaultCoinAmount;

  @Value("${business.user.onLevelUp.coinAmount}")
  private double levelUpReward;

  /**
   * method is used to create a new UserProgress record.
   *
   * @param userId ID of the user for whom the progress record is being created.
   * @return UserProgressDTO returns a DTO object that contains the user's progress details.
   */
  public UserProgressDTO createUser(Long userId) {
    UserProgress userProgress = new UserProgress();
    userProgress.setId(userId);
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
    UserProgress userProgress = userProgressRepository.findById(userId).orElseThrow();
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
    UserProgress userProgress = userProgressRepository.findById(id).orElseThrow();
    return userProgressMapper.apply(userProgress);
  }


}
