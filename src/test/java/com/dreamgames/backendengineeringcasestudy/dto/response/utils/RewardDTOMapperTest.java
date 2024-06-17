package com.dreamgames.backendengineeringcasestudy.dto.response.utils;

import com.dreamgames.backendengineeringcasestudy.dto.response.RewardDTO;
import com.dreamgames.backendengineeringcasestudy.entity.Reward;
import com.dreamgames.backendengineeringcasestudy.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.entity.TournamentGroup;
import com.dreamgames.backendengineeringcasestudy.entity.UserProgress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RewardDTOMapperTest {

  private RewardDTOMapper rewardDTOMapper;

  @BeforeEach
  public void setUp() {
    rewardDTOMapper = new RewardDTOMapper();
  }

  @DisplayName("Map Reward to RewardDTO successfully")
  @Test
  public void mapRewardToRewardDTOSuccessfully() {
    UserProgress user = new UserProgress();
    user.setId(1L);

    TournamentGroup group = new TournamentGroup();
    group.setId(2L);

    Tournament tournament = new Tournament();
    tournament.setId(3L);

    Reward reward = new Reward();
    reward.setId(4L);
    reward.setUser(user);
    reward.setGroup(group);
    reward.setTournament(tournament);
    reward.setCurrentRank(1);
    reward.setClaimed(true);

    RewardDTO expected = new RewardDTO(4L, 3L, 2L, 1L, 1, true);

    RewardDTO actual = rewardDTOMapper.apply(reward);

    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getTournamentId(), actual.getTournamentId());
    assertEquals(expected.getGroupId(), actual.getGroupId());
    assertEquals(expected.getUserId(), actual.getUserId());
    assertEquals(expected.getCurrentRank(), actual.getCurrentRank());
    assertEquals(expected.isClaimed(), actual.isClaimed());
  }
}