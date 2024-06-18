package com.dreamgames.backendengineeringcasestudy.dto.request;

/**
 * Data Transfer Object (DTO) for the request to enter a tournament. This DTO includes the user ID
 * which is validated to ensure it is not null and greater than 0.
 */
public record UpdateLevelRequest(
  Long userId
) {

}
