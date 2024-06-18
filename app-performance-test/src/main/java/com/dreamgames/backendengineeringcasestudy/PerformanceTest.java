package com.dreamgames.backendengineeringcasestudy;

import com.dreamgames.backendengineeringcasestudy.dto.request.CreateUserDTO;
import com.dreamgames.backendengineeringcasestudy.client.AppClient;
import com.dreamgames.backendengineeringcasestudy.configuration.TestConfiguration;
import com.dreamgames.backendengineeringcasestudy.dto.request.UpdateLevelRequest;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class PerformanceTest {

  private int threadId;
  private int counter;
  private int index;
  private UUID uuid;

  private AppClient appClient;

  public PerformanceTest() {
  }

  public PerformanceTest(String text) {
    String[] split = text.split("-");
    this.threadId = Integer.parseInt(split[0]);
    int loop = Integer.parseInt(split[1]);
    this.index = (this.threadId - 1) * loop;
    log.info("Thread id: {}", this.threadId);
    log.info("Index: {}", this.index);
  }

  @Before
  public void setUp() {
    appClient = new AppClient(TestConfiguration.BASE_URL);
    this.uuid = UUID.randomUUID();
    this.index += 1;
    log.info("Index: {}", this.index);
  }

  @After
  public void tearDown() {
  }

  @Test
  public void createUserTest() throws Exception {
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
}
