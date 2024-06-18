package com.dreamgames.backendengineeringcasestudy.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) for the request to enter a tournament. This DTO includes the user ID
 * which is validated to ensure it is not null and greater than 0.
 */
public record UpdateLevelRequest(
    @NotNull(
        message = "User ID must not be null"
    )
    @Min(
        value = 1,
        message = "User ID must be greater than 0"
    ) Long userId
) {

}
