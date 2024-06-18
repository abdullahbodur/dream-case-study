package com.dreamgames.backendengineeringcasestudy.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The PasswordMatches annotation is used to validate that two password fields match.
 * It is used at the class level and is validated by the PasswordMatchesValidator class.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
public @interface PasswordMatches {

  /**
   * The default error message that is used if the validation fails.
   */
  String message() default "Passwords do not match";

  /**
   * The groups the constraint belongs to.
   * Default is an empty array.
   */
  Class<?>[] groups() default {};

  /**
   * The payload that can be associated with a constraint.
   * It's typically used to associate metadata with a constraint declaration.
   * Default is an empty array.
   */
  Class<? extends Payload>[] payload() default {};
}