package com.dreamgames.backendengineeringcasestudy.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DigestUtilsTest {

  private DigestUtils digestUtils;

  @BeforeEach
  public void setUp() {
    digestUtils = new DigestUtils();
  }

  @DisplayName("Hashing a string successfully")
  @Test
  public void hashStringSuccessfully() {
    String data = "test";
    String expected = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";

    String actual = digestUtils.hash(data);

    assertEquals(expected, actual);
  }

  @DisplayName("Hashing an empty string")
  @Test
  public void hashEmptyString() {
    String data = "";
    String expected = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

    String actual = digestUtils.hash(data);

    assertEquals(expected, actual);
  }
}