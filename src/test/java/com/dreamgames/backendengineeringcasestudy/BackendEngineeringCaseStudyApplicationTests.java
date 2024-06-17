package com.dreamgames.backendengineeringcasestudy;

import com.dreamgames.backendengineeringcasestudy.service.TournamentScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
class BackendEngineeringCaseStudyApplicationTests {

  @MockBean
  private TournamentScheduleService tournamentScheduleService;

  @Test
  void contextLoads() {
  }

}
