package com.dreamgames.backendengineeringcasestudy.common.validation;

import com.dreamgames.backendengineeringcasestudy.api.dto.request.CreateUserDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The PasswordMatchesValidator class implements the ConstraintValidator interface for the PasswordMatches annotation.
 * It validates that the password and password confirmation fields in the CreateUserDTO object are the same.
 */
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

  /**
   * This method is used to initialize the constraint annotation.
   * In this case, no initialization is needed, so the method is empty.
   */
  @Override
  public void initialize(PasswordMatches constraintAnnotation) {
  }

  /**
   * This method checks if the password and password confirmation fields in the CreateUserDTO object are the same.
   * If they are the same, it returns true. Otherwise, it returns false.
   *
   * @param obj The object that is being validated. In this case, it's an instance of CreateUserDTO.
   * @param context The context in which the constraint is evaluated.
   * @return A boolean indicating whether the password and password confirmation fields are the same.
   */
  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context) {
    CreateUserDTO user = (CreateUserDTO) obj;
    return user.password().equals(user.passwordConfirm());
  }
}