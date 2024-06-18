package com.dreamgames.backendengineeringcasestudy.common.utils;

import org.springframework.stereotype.Component;


/**
 * The NicknameGenerator class provides a utility method for generating unique nicknames.
 * It is annotated with @Component, meaning that Spring will automatically create an instance of this class.
 */
@Component
public class NicknameGenerator {

  /**
   * This method makes a given nickname unique by appending the current time in milliseconds.
   *
   * @param nickname The original nickname.
   * @return The unique nickname.
   */
  public String makeNicknameUnique(String nickname) {
    return nickname + "_" + System.currentTimeMillis();
  }
}