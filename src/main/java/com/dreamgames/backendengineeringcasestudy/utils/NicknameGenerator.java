package com.dreamgames.backendengineeringcasestudy.utils;

import org.springframework.stereotype.Component;

@Component
public class NicknameGenerator {

  public String makeNicknameUnique(String nickname) {
    return nickname + "_" + System.currentTimeMillis();
  }
}
