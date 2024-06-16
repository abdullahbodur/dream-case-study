package com.dreamgames.backendengineeringcasestudy.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) for the request to claim a reward. This DTO includes the user ID and
 * the tournament ID. Both fields are validated to ensure they are not null and greater than 0.
 */
public record ClaimRewardRequestDTO(
    @NotNull(
        message = "User ID must not be null"
    )
    @Min(value = 1,
        message = "User ID must be greater than 0"
    ) Long userId,
    @NotNull(
        message = "Tournament ID must not be null"
    ) @Min(value = 1,
        message = "Tournament ID must be greater than 0"
    ) Long tournamentId
) {

}
