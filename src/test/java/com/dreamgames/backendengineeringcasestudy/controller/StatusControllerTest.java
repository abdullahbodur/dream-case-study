package com.dreamgames.backendengineeringcasestudy.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class StatusControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testStatus() throws Exception {
    mockMvc.perform(get("/status"))
        .andExpect(status().isOk())
        .andExpect(content().string("Server is up!"));
  }
}