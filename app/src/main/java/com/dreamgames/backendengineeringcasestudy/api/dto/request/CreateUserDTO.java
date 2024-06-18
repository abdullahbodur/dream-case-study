package com.dreamgames.backendengineeringcasestudy.api.dto.request;


import com.dreamgames.backendengineeringcasestudy.common.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * record is used to transfer data when creating a new User record.
 *
 * @param email           email of the user being created.
 * @param nickname        nickname of the user being created.
 * @param password        password of the user being created.
 * @param passwordConfirm password confirmation for the user being created.
 */
@PasswordMatches(message = "Passwords do not match.")
public record CreateUserDTO(
    @Email @NotBlank String email,
    @NotBlank @Size(min = 3, max = 36, message = "Nickname must be between 3 and 36 characters.") String nickname,
    @NotBlank @Size(min = 8, max = 40, message = "Password must be between 8 and 40 characters.") String password,
    @NotBlank @Size(min = 8, max = 40, message = "Password must be between 8 and 40 characters.") String passwordConfirm
) {

}