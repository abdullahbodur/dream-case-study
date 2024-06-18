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
class TournamentGroupPoolServiceTest {

  @InjectMocks
  private TournamentGroupPoolService tournamentGroupPoolService;

  @MockBean
  private RedisTemplate<String, Stack<Number>> groupPool;

  @MockBean
  private ValueOperations<String, Stack<Number>> valueOperations;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(tournamentGroupPoolService, "groupPool", groupPool);
    when(groupPool.opsForValue()).thenReturn(valueOperations);
  }

  @DisplayName("Cleanup group pool successfully")
  @Test
  public void cleanupGroupPoolSuccessfully() {
    tournamentGroupPoolService.cleanupGroupPool();
    verify(groupPool, times(Country.values().length)).opsForValue();
    verify(valueOperations, times(1)).set(eq(Country.TURKEY.name()), argThat(
        Vector::isEmpty
    ));
    verify(valueOperations, times(1)).set(eq(Country.GERMANY.name()), argThat(
        Vector::isEmpty
    ));
    verify(valueOperations, times(1)).set(eq(Country.FRANCE.name()), argThat(
        Vector::isEmpty
    ));
    verify(valueOperations, times(1)).set(eq(Country.UNITED_STATES.name()), argThat(
        Vector::isEmpty
    ));
    verify(valueOperations, times(1)).set(eq(Country.UNITED_KINGDOM.name()), argThat(
        Vector::isEmpty
    ));
  }

  @DisplayName("Get available group successfully")
  @Test
  public void getAvailableGroupSuccessfully() {
    Stack<Number> groupStack = new Stack<>();
    groupStack.push(1);
    when(valueOperations.get(Country.TURKEY.name())).thenReturn(groupStack);
    tournamentGroupPoolService.getAvailableGroup(Country.TURKEY);
    verify(groupPool, times(2)).opsForValue();
  }

  @DisplayName("Get available group when no group is available")
  @Test
  public void getAvailableGroupWhenNoGroupIsAvailable() {
    Stack<Number> groupStack = new Stack<>();
    when(valueOperations.get(anyString())).thenReturn(groupStack);
    tournamentGroupPoolService.getAvailableGroup(Country.GERMANY);
    verify(groupPool, times(1)).opsForValue();
  }

  @DisplayName("Get available group when group stack is null")
  @Test
  public void getAvailableGroupWhenGroupStackIsNull() {
    when(valueOperations.get(anyString())).thenReturn(null);
    tournamentGroupPoolService.getAvailableGroup(Country.GERMANY);
    verify(groupPool, times(1)).opsForValue();
  }

  @DisplayName("Add group to pool successfully when all country stacks are empty")
  @Test
  public void addGroupToPoolSuccessfully() {
    when(valueOperations.get(anyString())).thenReturn(null);
    tournamentGroupPoolService.addGroupToPool(1L, List.of(Country.GERMANY));
    verify(valueOperations, times(1)).set(eq(Country.TURKEY.name()), argThat(
        stack -> stack.size() == 1 && stack.peek().equals(1L)
    ));
    verify(valueOperations, times(1)).set(eq(Country.FRANCE.name()), argThat(
        stack -> stack.size() == 1 && stack.peek().equals(1L)
    ));
    verify(valueOperations, times(1)).set(eq(Country.UNITED_STATES.name()), argThat(
        stack -> stack.size() == 1 && stack.peek().equals(1L)
    ));
    verify(valueOperations, times(1)).set(eq(Country.UNITED_KINGDOM.name()), argThat(
        stack -> stack.size() == 1 && stack.peek().equals(1L)
    ));
    verify(valueOperations, times(1)).set(eq(Country.TURKEY.name()), argThat(
        stack -> stack.size() == 1 && stack.peek().equals(1L)
    ));
    verify(valueOperations, times(1)).set(eq(Country.FRANCE.name()), argThat(
        stack -> stack.size() == 1 && stack.peek().equals(1L)
    ));
    verify(valueOperations, times(1)).set(eq(Country.UNITED_STATES.name()), argThat(
        stack -> stack.size() == 1 && stack.peek().equals(1L)
    ));
    verify(valueOperations, times(1)).set(eq(Country.UNITED_KINGDOM.name()), argThat(
        stack -> stack.size() == 1 && stack.peek().equals(1L)
    ));
  }

  @DisplayName("Add group to pool when some country stacks are not empty")
  @Test
  public void addGroupToPoolWhenSomeCountryStacksAreNotEmpty() {
    Stack<Number> groupStackTR = new Stack<>();
    groupStackTR.push(2);
    Stack<Number> groupStackFR = new Stack<>();
    groupStackFR.push(3);
    when(valueOperations.get(Country.TURKEY.name())).thenReturn(groupStackTR);
    when(valueOperations.get(Country.FRANCE.name())).thenReturn(groupStackFR);
    when(valueOperations.get(Country.UNITED_STATES.name())).thenReturn(null);
    when(valueOperations.get(Country.UNITED_KINGDOM.name())).thenReturn(null);
    tournamentGroupPoolService.addGroupToPool(1L, List.of(Country.GERMANY));
    verify(valueOperations, times(1)).get(eq(Country.TURKEY.name()));
    verify(valueOperations, times(1)).get(eq(Country.FRANCE.name()));
    verify(valueOperations, times(1)).get(eq(Country.UNITED_STATES.name()));
    verify(valueOperations, times(1)).get(eq(Country.UNITED_KINGDOM.name()));
    verify(valueOperations, times(1)).set(eq(Country.TURKEY.name()), argThat(
        stack -> stack.size() == 2 && stack.peek().equals(1L)
    ));
    verify(valueOperations, times(1)).set(eq(Country.FRANCE.name()), argThat(
        stack -> stack.size() == 2 && stack.peek().equals(1L)
    ));
    verify(valueOperations, times(1)).set(eq(Country.UNITED_STATES.name()), argThat(
        stack -> stack.size() == 1 && stack.peek().equals(1L)
    ));
    verify(valueOperations, times(1)).set(eq(Country.UNITED_KINGDOM.name()), argThat(
        stack -> stack.size() == 1 && stack.peek().equals(1L)
    ));
  }

  @DisplayName("Get remaining country slots successfully")
  @Test
  public void getRemainingCountrySlotsSuccessfully() {
    List<Country> remainingCountrySlots = tournamentGroupPoolService.getRemainingCountrySlots(
        List.of(Country.GERMANY));
    assertEquals(Country.values().length - 1, remainingCountrySlots.size());
  }
}

