package com.dreamgames.backendengineeringcasestudy.tournament.service;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.ParticipationDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.TournamentGroupDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.utils.ParticipationDTOMapper;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.Participation;
import com.dreamgames.backendengineeringcasestudy.tournament.entity.TournamentGroup;
import com.dreamgames.backendengineeringcasestudy.user.entity.UserProgress;
import com.dreamgames.backendengineeringcasestudy.exceptions.EntityNotFoundException;
import com.dreamgames.backendengineeringcasestudy.tournament.repository.ParticipationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * Service class for handling operations related to Participation.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ParticipationService {

  private final ParticipationRepository participationRepository;
  private final ParticipationDTOMapper participationDTOMapper;

  /**
   * Creates a new Participation record.
   *
   * @param userProgress       The UserProgress entity for the user participating in the
   *                           tournament.
   * @param tournamentGroupDTO The DTO of the TournamentGroup the user is participating in.
   */
  public void createParticipation(UserProgress userProgress,
      TournamentGroupDTO tournamentGroupDTO) {
    Participation participation = new Participation();
    participation.setUser(userProgress);
    TournamentGroup tournamentGroup = new TournamentGroup();
    tournamentGroup.setId(tournamentGroupDTO.getGroupId());
    participation.setGroup(tournamentGroup);
    participation.setScore(0);
    participationRepository.save(participation);
  }

  /**
   * Retrieves a ParticipationDTO for a specific user in a specific group.
   *
   * @param userId  The ID of the user.
   * @param groupId The ID of the group.
   * @return The ParticipationDTO for the user in the group.
   * @throws EntityNotFoundException If no Participation record is found for the user in the group.
   */
  public ParticipationDTO getParticipation(Long userId, Long groupId) {
    return participationRepository.findByGroupIdAndUserId(groupId, userId)
        .map(participationDTOMapper)
        .orElseThrow(() -> new EntityNotFoundException(
            "Participation not found for user " + userId + " in group " + groupId));
  }
}
