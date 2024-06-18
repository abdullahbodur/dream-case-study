package com.dreamgames.backendengineeringcasestudy.dto.request;

public record CreateUserDTO(
    String email,
    String nickname,
    String password,
    String passwordConfirm
) {

}