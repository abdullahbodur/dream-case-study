package com.dreamgames.backendengineeringcasestudy.dto.request;

/**
 * This record is used to transfer data when creating a new User record.
 *
 * @param email           This is the email of the user being created.
 * @param nickname        This is the nickname of the user being created.
 * @param password        This is the password of the user being created.
 * @param passwordConfirm This is the password confirmation for the user being created.
 */
public record CreateUserDTO(
    String email,
    String nickname,
    String password,
    String passwordConfirm
) {

}