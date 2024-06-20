package com.dreamgames.backendengineeringcasestudy.tournament.service;

import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import java.util.List;
import java.util.Set;
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
public class GroupPoolService {

  private final RedisTemplate<String, Number> groupPool;

  /**
   * Cleans up the group pool by setting an empty stack for each country.
   */
  public void cleanupGroupPool() {
    List<Country> allCountries = List.of(Country.values());
    allCountries.forEach(country -> groupPool.delete("groupPool:" + country.name() + ":*"));
  }

  /**
   * Gets an available group for a user based on their country. The group is removed from the pool
   * for that country.
   *
   * @param newUserCountry the country of the new user
   * @return the ID of the available group, or null if no group is available
   */
  public Long getAvailableGroup(Country newUserCountry) {
    Set<String> keys = groupPool.keys("groupPool:" + newUserCountry.name() + ":*");
    if (keys == null || keys.isEmpty()) {
      return null;
    }
    Long groupId = pickRandomGroup(keys);
    groupPool.delete("groupPool:" + newUserCountry.name() + ":" + groupId);
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
    remainingCountrySlots.forEach(country -> groupPool.opsForValue()
        .set("groupPool:" + country.name() + ":" + groupId,
            groupId));
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

  /**
   * Picks a random group from the given set of keys.
   *
   * @param keys the keys of the group pool entries
   * @return the ID of a random group from the given set of keys
   */
  private Long pickRandomGroup(Set<String> keys) {
    List<String> keyList = new java.util.ArrayList<>(keys);
    int randomIndex = new java.util.Random().nextInt(keyList.size());
    return getGroupNameFromKey(keyList.get(randomIndex));
  }

  /**
   * Gets the group ID from the key of a group pool entry.
   *
   * @param key the key of the group pool entry
   * @return the group ID from the key
   */
  private Long getGroupNameFromKey(String key) {
    return Long.parseLong(key.split(":")[2]);
  }
}
