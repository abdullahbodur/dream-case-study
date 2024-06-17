package com.dreamgames.backendengineeringcasestudy.tournament.service;


import com.dreamgames.backendengineeringcasestudy.api.dto.response.RewardDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentGroupDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.utils.GroupDTOMapper;
import com.dreamgames.backendengineeringcasestudy.exceptions.EntityNotFoundException;
import com.dreamgames.backendengineeringcasestudy.exceptions.TournamentNotStartedException;
import com.dreamgames.backendengineeringcasestudy.exceptions.UserNotReadyForNewTournamentException;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.TournamentGroup;
import com.dreamgames.backendengineeringcasestudy.tournament.repository.GroupRepository;
import com.dreamgames.backendengineeringcasestudy.user.entity.UserProgress;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * Service class for handling operations related to Tournament Groups.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TournamentGroupService {

  private final TournamentGroupPoolService groupPoolService;
  private final GroupRepository groupRepository;
  private final GroupDTOMapper groupDTOMapper;
  private final ParticipationService participationService;
  private final TournamentRewardService rewardService;
  private final TournamentLeaderboardService leaderboardService;

  /**
   * Allows a user to enter a tournament.
   *
   * @param userProgressDTO The DTO of the user's progress.
   * @param tournamentDTO   The DTO of the tournament the user wants to enter.
   * @return The ID of the group the user is assigned to.
   * @throws TournamentNotStartedException         If the tournament is not active.
   * @throws UserNotReadyForNewTournamentException If the user has an unclaimed reward or is already
   *                                               in a group.
   * @throws EntityNotFoundException               If the group with the given ID is not found.
   */
  public Long enterTournament(UserProgressDTO userProgressDTO, TournamentDTO tournamentDTO) {
    if (tournamentDTO == null) {
      throw new TournamentNotStartedException("Tournament is not active");
    }
    RewardDTO unClaimedReward = rewardService.findUnclaimedReward(userProgressDTO.getId());
    if (unClaimedReward != null) {
      throw new UserNotReadyForNewTournamentException(
          "User: " + userProgressDTO.getId() + " has unclaimed reward: " + unClaimedReward.getId());
    }
    Long groupIdAlreadyIn = leaderboardService.getGroupIdForUser(userProgressDTO.getId());
    if (groupIdAlreadyIn != null) {
      throw new UserNotReadyForNewTournamentException(
          "User: " + userProgressDTO.getId() + " is already in a group: " + groupIdAlreadyIn);
    }
    Long groupId = groupPoolService.getAvailableGroup(userProgressDTO.getCountry());
    TournamentGroupDTO tournamentGroupDTO;
    UserProgress userProgress = new UserProgress();
    userProgress.setId(userProgressDTO.getId());
    if (groupId == null) {
      Tournament tournament = new Tournament();
      tournament.setId(tournamentDTO.getId());
      tournamentGroupDTO = initTournamentGroup(tournament, userProgress);
      groupPoolService.addGroupToPool(tournamentGroupDTO.getGroupId(),
          List.of(userProgressDTO.getCountry()));
    } else {
      tournamentGroupDTO = groupDTOMapper.apply(groupRepository.findById(groupId)
          .orElseThrow(
              () -> new EntityNotFoundException("Group with id " + groupId + " not found")
          ));
    }
    participationService.createParticipation(userProgress, tournamentGroupDTO);
    return tournamentGroupDTO.getGroupId();
  }

  /**
   * Initializes a new Tournament Group.
   *
   * @param tournament   The Tournament entity the group is part of.
   * @param userProgress The UserProgress entity of the user entering the tournament.
   * @return The DTO of the newly created Tournament Group.
   */
  private TournamentGroupDTO initTournamentGroup(Tournament tournament, UserProgress userProgress) {
    TournamentGroupDTO tournamentGroupDTO;
    TournamentGroup tournamentGroup = new TournamentGroup();
    tournamentGroup.setTournament(tournament);
    tournamentGroup.setReady(false);
    tournamentGroupDTO = groupDTOMapper.apply(groupRepository.save(tournamentGroup));
    return tournamentGroupDTO;
  }
}
