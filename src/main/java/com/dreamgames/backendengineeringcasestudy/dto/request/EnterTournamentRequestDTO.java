package com.dreamgames.backendengineeringcasestudy.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) for the request to enter a tournament. This DTO includes the user ID
 * which is validated to ensure it is not null and greater than 0.
 */
public record EnterTournamentRequestDTO(
    @NotNull(
        message = "userId must not be null"
    )
    @Min(
        value = 1,
        message = "userId must be greater than or equal to 1"
    )
    Long userId
) {

}
