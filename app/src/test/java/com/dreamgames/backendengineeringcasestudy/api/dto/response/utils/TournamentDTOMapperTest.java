package com.dreamgames.backendengineeringcasestudy.api.dto.response.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentDTO;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.Tournament;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TournamentDTOMapperTest {

  private TournamentDTOMapper tournamentDTOMapper;

  @BeforeEach
  public void setUp() {
    tournamentDTOMapper = new TournamentDTOMapper();
  }

  @DisplayName("Map Tournament to TournamentDTO successfully")
  @Test
  public void mapTournamentToTournamentDTOSuccessfully() {
    ZonedDateTime startTime = ZonedDateTime.now();
    ZonedDateTime endTime = startTime.plusHours(1);

    Tournament tournament = new Tournament();
    tournament.setId(1L);
    tournament.setStartTime(startTime);
    tournament.setEndTime(endTime);
    tournament.setCompleted(true);

    TournamentDTO expected = new TournamentDTO(1L, startTime, endTime, true);

    TournamentDTO actual = tournamentDTOMapper.apply(tournament);

    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getStartTime(), actual.getStartTime());
    assertEquals(expected.getEndTime(), actual.getEndTime());
    assertEquals(expected.isCompleted(), actual.isCompleted());
  }
}