package com.dreamgames.backendengineeringcasestudy.tournament.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
class GroupPoolServiceTest {

  @InjectMocks
  private GroupPoolService groupPoolService;

  @MockBean
  private RedisTemplate<String, Number> groupPool;

  @MockBean
  private ValueOperations<String, Number> valueOperations;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(groupPoolService, "groupPool", groupPool);
    when(groupPool.opsForValue()).thenReturn(valueOperations);
  }

  @DisplayName("Cleanup group pool successfully")
  @Test
  public void cleanupGroupPoolSuccessfully() {
    groupPoolService.cleanupGroupPool();
    for (Country country : Country.values()) {
      verify(groupPool, times(1)).delete("groupPool:" + country.name() + ":*");
    }
  }

  @DisplayName("Get available group successfully")
  @Test
  public void getAvailableGroupSuccessfully() {
    Set<String> keys = Set.of("groupPool:" + Country.TURKEY.name() + ":1");
    when(groupPool.keys("groupPool:" + Country.TURKEY.name() + ":*")).thenReturn(keys);
    when(valueOperations.get(anyString())).thenReturn(1L);
    groupPoolService.getAvailableGroup(Country.TURKEY);
    verify(groupPool, times(1)).keys("groupPool:" + Country.TURKEY.name() + ":*");
    verify(groupPool, times(1)).delete("groupPool:" + Country.TURKEY.name() + ":1");
  }

  @DisplayName("Get available group when no group is available")
  @Test
  public void getAvailableGroupWhenNoGroupIsAvailable() {
    when(groupPool.keys("groupPool:" + Country.GERMANY.name() + ":*")).thenReturn(null);
    groupPoolService.getAvailableGroup(Country.GERMANY);
    verify(groupPool, times(1)).keys("groupPool:" + Country.GERMANY.name() + ":*");
    verify(groupPool, times(0)).delete(anyString());
  }

  @DisplayName("Get available group when key set is empty")
  @Test
  public void getAvailableGroupWhenGroupStackIsNull() {
    when(groupPool.keys("groupPool:" + Country.GERMANY.name() + ":*")).thenReturn(Set.of());
    groupPoolService.getAvailableGroup(Country.GERMANY);
    verify(groupPool, times(1)).keys("groupPool:" + Country.GERMANY.name() + ":*");
    verify(groupPool, times(0)).delete(anyString());
  }

  @DisplayName("Add group to pool successfully when all country stacks are empty")
  @Test
  public void addGroupToPoolSuccessfully() {
    groupPoolService.addGroupToPool(1L, List.of(Country.GERMANY));
    for (Country country : List.of(Country.FRANCE, Country.TURKEY, Country.UNITED_KINGDOM,
        Country.UNITED_STATES)) {
      verify(valueOperations, times(1)).set(eq("groupPool:" + country.name() + ":1"), eq(1L));
    }
  }

  @DisplayName("Get remaining country slots successfully")
  @Test
  public void getRemainingCountrySlotsSuccessfully() {
    List<Country> remainingCountrySlots = groupPoolService.getRemainingCountrySlots(
        List.of(Country.GERMANY));
    assertEquals(Country.values().length - 1, remainingCountrySlots.size());
  }
}

