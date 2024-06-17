package com.dreamgames.backendengineeringcasestudy.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NicknameGeneratorTest {

  private NicknameGenerator nicknameGenerator;

  @BeforeEach
  public void setUp() {
    nicknameGenerator = new NicknameGenerator();
  }

  @DisplayName("Make nickname unique successfully")
  @Test
  public void makeNicknameUniqueSuccessfully() {
    String nickname = "test";
    String uniqueNickname = nicknameGenerator.makeNicknameUnique(nickname);

    assertTrue(uniqueNickname.startsWith(nickname));
    assertTrue(uniqueNickname.length() > nickname.length());
  }
}