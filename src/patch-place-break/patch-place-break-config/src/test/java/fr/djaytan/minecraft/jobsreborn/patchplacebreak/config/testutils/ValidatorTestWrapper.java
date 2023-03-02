package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import lombok.NonNull;

/**
 * A validator wrapper for test purposes.
 *
 * <p>Simply call {@link #validate(Object)} to validate a bean. When the validator is needed as a
 * dependency the method {@link #getValidator()} should be used.
 */
public class ValidatorTestWrapper {

  private static final ValidatorTestWrapper INSTANCE = new ValidatorTestWrapper();

  private final ValidatorFactory validatorFactory;

  private ValidatorTestWrapper() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
  }

  /**
   * @see jakarta.validation.Validator#validate(Object, Class[])
   */
  public static @NonNull <T> Set<ConstraintViolation<T>> validate(@NonNull T object) {
    return INSTANCE.validatorFactory.getValidator().validate(object);
  }

  /**
   * Returns a validator for test purposes.
   *
   * @return A validator for test purposes.
   */
  public static @NonNull Validator getValidator() {
    return INSTANCE.validatorFactory.getValidator();
  }
}
