package com.dreamgames.backendengineeringcasestudy.common.utils;

import org.springframework.stereotype.Component;

/**
 * Utility class for picking a random value from an Enum. It provides a method for getting a random
 * Enum value given the Enum class.
 */
@Component
public class EnumRandomPicker {

  /**
   * @param clazz Enum class from which a random value is to be picked.
   * @param <T>   Type of the Enum.
   * @return A random Enum value from the given Enum class.
   */
  public <T extends Enum<?>> T getRandomEnum(Class<T> clazz) {
    int x = (int) (Math.random() * clazz.getEnumConstants().length);
    return clazz.getEnumConstants()[x];
  }
}
