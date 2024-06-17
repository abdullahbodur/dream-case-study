package com.dreamgames.backendengineeringcasestudy.tournament.service;

import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import java.util.List;
import java.util.Stack;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Service class for managing tournament group pools. This service is responsible for initializing
 * and managing the group pool in Redis. It provides methods to get an available group for a user
 * based on their country, and to add a group to the pool for available countries.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TournamentGroupPoolService {

  private final RedisTemplate<String, Stack<Number>> groupPool;

  /**
   * Cleans up the group pool by setting an empty stack for each country.
   */
  public void cleanupGroupPool() {
    List<Country> allCountries = List.of(Country.values());
    allCountries.forEach(country -> groupPool.opsForValue().set(country.name(), new Stack<>()));
  }

  /**
   * Gets an available group for a user based on their country. The group is removed from the pool
   * for that country.
   *
   * @param newUserCountry the country of the new user
   * @return the ID of the available group, or null if no group is available
   */
  public Long getAvailableGroup(Country newUserCountry) {
    Stack<Number> groupStack = groupPool.opsForValue().get(newUserCountry.name());
    if (groupStack == null || groupStack.isEmpty()) {
      return null;
    }
    Long groupId = groupStack.pop().longValue();
    groupPool.opsForValue().set(newUserCountry.name(), groupStack);
    return groupId;
  }

  /**
   * Adds a group to the pool for the given list of countries. The group ID is added to the stack
   * for each country in the list.
   *
   * @param groupId              the ID of the group to add to the pool
   * @param reservedCountrySlots the countries of the users in the group
   */
  public void addGroupToPool(Long groupId, List<Country> reservedCountrySlots) {
    List<Country> remainingCountrySlots = getRemainingCountrySlots(reservedCountrySlots);
    for (Country country : remainingCountrySlots) {
      Stack<Number> groupStack = groupPool.opsForValue().get(country.name());
      if (groupStack == null) {
        groupStack = new Stack<>();
      }
      groupStack.push(groupId);
      groupPool.opsForValue().set(country.name(), groupStack);
    }
  }

  /**
   * Determines the list of countries for which a group is available. This is done by removing the
   * countries of the users in the group from the list of all countries.
   *
   * @param countries the countries of the users in the group
   * @return the list of countries for which the group is available
   */

  public List<Country> getRemainingCountrySlots(List<Country> countries) {
    List<Country> allCountries = new java.util.ArrayList<>(List.of(Country.values()));
    allCountries.removeAll(countries);
    return allCountries;
  }
}
