package com.dreamgames.backendengineeringcasestudy;

import static java.lang.Integer.parseInt;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class PerformanceTest {

  private int counter;

  public PerformanceTest() {
  }

  public PerformanceTest(String counter) {
    this.counter = parseInt(counter);
  }

  @Before
  public void setUp() {
    counter = 0;
  }

  @After
  public void tearDown() {
    log.info("Total number of users created: {}", counter);
  }

  @Test
  public void createUserTest() {
    log.info("Creating {} users", counter);
  }
}
