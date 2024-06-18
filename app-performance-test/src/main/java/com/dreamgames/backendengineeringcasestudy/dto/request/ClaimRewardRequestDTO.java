package com.dreamgames.backendengineeringcasestudy.dto.request;


/**
 * Data Transfer Object (DTO) for the request to claim a reward. This DTO includes the user ID and
 * the tournament ID. Both fields are validated to ensure they are not null and greater than 0.
 */
public record ClaimRewardRequestDTO(
   Long userId,
   Long tournamentId
) {

}
