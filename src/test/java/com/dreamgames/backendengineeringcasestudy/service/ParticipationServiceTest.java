package com.dreamgames.backendengineeringcasestudy.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dreamgames.backendengineeringcasestudy.dto.response.ParticipationDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.TournamentGroupDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.utils.ParticipationDTOMapper;
import com.dreamgames.backendengineeringcasestudy.entity.Participation;
import com.dreamgames.backendengineeringcasestudy.entity.UserProgress;
import com.dreamgames.backendengineeringcasestudy.exceptions.EntityNotFoundException;
import com.dreamgames.backendengineeringcasestudy.repository.ParticipationRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ParticipationServiceTest {

  @InjectMocks
  private ParticipationService participationService;

  @Mock
  private ParticipationRepository participationRepository;

  @Mock
  private ParticipationDTOMapper participationDTOMapper;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void tearDown() {
    participationService = null;
  }


  @DisplayName("Create participation successfully")
  @Test
  public void createParticipationSuccessfully() {
    UserProgress userProgress = new UserProgress();
    TournamentGroupDTO tournamentGroupDTO = new TournamentGroupDTO(1L, 100L, false);
    tournamentGroupDTO.setGroupId(1L);

    participationService.createParticipation(userProgress, tournamentGroupDTO);
    verify(participationRepository, times(1)).save(argThat(
        participation -> participation.getUser().equals(userProgress)
            && participation.getGroup().getId().equals(tournamentGroupDTO.getGroupId())
            && participation.getScore() == 0

    ));
  }

  @DisplayName("Get participation successfully")
  @Test
  public void getParticipationSuccessfully() {
    Participation participation = new Participation();
    ParticipationDTO participationDTO = new ParticipationDTO(
        1L, 10L, 3L, 0);
    when(participationRepository.findByGroupIdAndUserId(anyLong(), anyLong())).thenReturn(
        Optional.of(participation));
    when(participationDTOMapper.apply(any(Participation.class))).thenReturn(
        participationDTO
    );
    participationService.getParticipation(10L, 3L);

    verify(participationRepository, times(1)).findByGroupIdAndUserId(3L, 10L);
    verify(participationDTOMapper, times(1)).apply(participation);
  }

  @DisplayName("Get participation throws EntityNotFoundException")
  @Test
  public void getParticipationThrowsEntityNotFoundException() {
    when(participationRepository.findByGroupIdAndUserId(anyLong(), anyLong())).thenReturn(
        Optional.empty());
    when(participationDTOMapper.apply(any(Participation.class))).thenReturn(null);

    assertThrows(
        EntityNotFoundException.class, () -> participationService.getParticipation(10L, 3L));

    verify(participationRepository, times(1)).findByGroupIdAndUserId(3L, 10L);
  }
}