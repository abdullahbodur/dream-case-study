package com.dreamgames.backendengineeringcasestudy.api.dto.response.utils;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.ParticipationDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.utils.ParticipationDTOMapper;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.Participation;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.TournamentGroup;
import com.dreamgames.backendengineeringcasestudy.user.entity.UserProgress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParticipationDTOMapperTest {

  private ParticipationDTOMapper participationDTOMapper;

  @BeforeEach
  public void setUp() {
    participationDTOMapper = new ParticipationDTOMapper();
  }

  @DisplayName("Map Participation to ParticipationDTO successfully")
  @Test
  public void mapParticipationToParticipationDTOSuccessfully() {
    UserProgress user = new UserProgress();
    user.setId(1L);

    TournamentGroup group = new TournamentGroup();
    group.setId(2L);

    Participation participation = new Participation();
    participation.setId(3L);
    participation.setUser(user);
    participation.setGroup(group);
    participation.setScore(100);

    ParticipationDTO expected = new ParticipationDTO(3L, 1L, 2L, 100);

    ParticipationDTO actual = participationDTOMapper.apply(participation);

    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getUserId(), actual.getUserId());
    assertEquals(expected.getGroupId(), actual.getGroupId());
    assertEquals(expected.getScore(), actual.getScore());
  }
}