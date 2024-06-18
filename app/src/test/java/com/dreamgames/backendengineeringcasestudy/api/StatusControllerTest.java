package com.dreamgames.backendengineeringcasestudy.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dreamgames.backendengineeringcasestudy.tournament.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StatusControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ScheduleService scheduleService;

  @Test
  public void testStatus() throws Exception {
    mockMvc.perform(get("/status"))
        .andExpect(status().isOk())
        .andExpect(content().string("Server is up!"));
  }
}