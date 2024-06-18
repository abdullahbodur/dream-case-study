package com.dreamgames.backendengineeringcasestudy.dto.request;


/**
 * Data Transfer Object (DTO) for the request to enter a tournament. This DTO includes the user ID
 */
public record EnterTournamentRequestDTO(
    Long userId
) {

}
