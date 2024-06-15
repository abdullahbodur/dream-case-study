package com.dreamgames.backendengineeringcasestudy.dto.request;


import com.dreamgames.backendengineeringcasestudy.validation.PasswordMatches;
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
@PasswordMatches
public record CreateUserDTO(
    @Email @NotBlank String email,
    @NotBlank @Size(min = 3, max = 20) String nickname,
    @NotBlank @Size(min = 8, max = 40) String password,
    @NotBlank @Size(min = 8, max = 40) String passwordConfirm
) {

}