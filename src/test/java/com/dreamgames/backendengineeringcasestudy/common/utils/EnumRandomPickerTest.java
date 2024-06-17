package com.dreamgames.backendengineeringcasestudy.common.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EnumRandomPickerTest {

  private EnumRandomPicker enumRandomPicker;

  @BeforeEach
  public void setUp() {
    enumRandomPicker = new EnumRandomPicker();
  }

  @DisplayName("Get random enum value successfully")
  @Test
  public void getRandomEnumValueSuccessfully() {
    Country randomEnumValue = enumRandomPicker.getRandomEnum(Country.class);
    assertNotNull(randomEnumValue);
  }

  @DisplayName("Get all enum values over multiple calls")
  @Test
  public void getAllEnumValuesOverMultipleCalls() {
    Set<Country> enumValues = new HashSet<>();
    for (int i = 0; i < 1000; i++) {
      enumValues.add(enumRandomPicker.getRandomEnum(Country.class));
    }
    assertTrue(enumValues.containsAll(Set.of(Country.values())));
  }
}