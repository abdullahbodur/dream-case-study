package com.dreamgames.backendengineeringcasestudy.api.dto.response.utils;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentGroupDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.utils.GroupDTOMapper;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.TournamentGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupDTOMapperTest {

  private GroupDTOMapper groupDTOMapper;

  @BeforeEach
  public void setUp() {
    groupDTOMapper = new GroupDTOMapper();
  }

  @DisplayName("Map TournamentGroup to TournamentGroupDTO successfully")
  @Test
  public void mapTournamentGroupToTournamentGroupDTOSuccessfully() {
    Tournament tournament = new Tournament();
    tournament.setId(1L);

    TournamentGroup tournamentGroup = new TournamentGroup();
    tournamentGroup.setId(2L);
    tournamentGroup.setTournament(tournament);
    tournamentGroup.setReady(true);

    TournamentGroupDTO expected = new TournamentGroupDTO(2L, 1L, true);

    TournamentGroupDTO actual = groupDTOMapper.apply(tournamentGroup);

    assertEquals(expected.getGroupId(), actual.getGroupId());
    assertEquals(expected.getTournamentId(), actual.getTournamentId());
    assertEquals(expected.isReady(), actual.isReady());
  }
}