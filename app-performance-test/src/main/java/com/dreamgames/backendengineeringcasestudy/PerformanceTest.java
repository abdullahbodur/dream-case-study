package com.dreamgames.backendengineeringcasestudy;

import com.dreamgames.backendengineeringcasestudy.dto.request.CreateUserDTO;
import com.dreamgames.backendengineeringcasestudy.client.AppClient;
import com.dreamgames.backendengineeringcasestudy.configuration.TestConfiguration;
import com.dreamgames.backendengineeringcasestudy.dto.request.EnterTournamentRequestDTO;
import com.dreamgames.backendengineeringcasestudy.dto.request.UpdateLevelRequest;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class PerformanceTest {

  private int index;
  private UUID uuid;

  private AppClient appClient;

  public PerformanceTest() {
  }

  public PerformanceTest(String index) {
    this.index = Integer.parseInt(index);
  }

  @Before
  public void setUp() {
    appClient = new AppClient(TestConfiguration.BASE_URL);
    this.uuid = UUID.randomUUID();
    log.info("Index: {}", this.index);
  }

  @Test
  public void createUserTest() {
    CreateUserDTO createUserDTO = new CreateUserDTO(
        "example-" + uuid.toString() + "@test.com",
        uuid.toString(),
        "password",
        "password"
    );

    log.info("Creating user: {}", createUserDTO);
    try {

      JsonNode progressDTO = appClient.createUser(createUserDTO);
      log.info("User created: {}", progressDTO);
      Assert.assertNotNull(progressDTO);
    } catch (Exception e) {
      log.error("Error creating user: {}", e);
      Assert.fail();
    }
  }


  @Test
  public void updateLevelTest() {
    UpdateLevelRequest updateLevelRequest = new UpdateLevelRequest(
        (long) this.index
    );
    log.info("Updating level: {}", updateLevelRequest);
    try {
      JsonNode progressDTO = appClient.updateLevel(updateLevelRequest);
      log.info("Level updated: {}", progressDTO);
      Assert.assertNotNull(progressDTO);
    } catch (Exception e) {
      log.error("Error updating level: {}", e);
      Assert.fail();
    }
  }

  @Test
  public void enterTournamentTest() {
    log.info("Entering tournament");
    try {
      EnterTournamentRequestDTO requestDTO = new EnterTournamentRequestDTO(
          (long) this.index
      );
      JsonNode progressDTO = appClient.enterTournament(requestDTO);
      log.info("Tournament entered: {}", progressDTO);
      Assert.assertNotNull(progressDTO);
    } catch (Exception e) {
      log.error("Error entering tournament: {}", e);
      Assert.fail();
    }
  }

  @Test
  public void getCountryLeaderboardTest() {
    log.info("Getting country leaderboard");
    try {
      JsonNode leaderboard = appClient.getCountryLeaderboard();
      log.info("Country leaderboard: {}", leaderboard);
      Assert.assertNotNull(leaderboard);
    } catch (Exception e) {
      log.error("Error getting country leaderboard: {}", e);
      Assert.fail();
    }
  }

  @Test
  public void getGroupLeaderboardTest() {
    log.info("Getting group leaderboard");
    try {
      JsonNode leaderboard = appClient.getGroupLeaderboard(1L);
      log.info("Group leaderboard: {}", leaderboard);
      Assert.assertNotNull(leaderboard);
    } catch (Exception e) {
      log.error("Error getting group leaderboard: {}", e);
      Assert.fail();
    }
  }
}
