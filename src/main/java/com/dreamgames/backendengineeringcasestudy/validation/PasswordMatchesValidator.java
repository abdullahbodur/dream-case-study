package com.dreamgames.backendengineeringcasestudy.validation;

import com.dreamgames.backendengineeringcasestudy.dto.request.CreateUserDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

  @Override
  public void initialize(PasswordMatches constraintAnnotation) {
  }

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context) {
    CreateUserDTO user = (CreateUserDTO) obj;
    return user.password().equals(user.passwordConfirm());
  }
}