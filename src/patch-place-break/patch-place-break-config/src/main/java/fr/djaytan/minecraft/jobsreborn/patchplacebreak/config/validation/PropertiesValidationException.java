package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation;

import jakarta.validation.ConstraintViolation;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.StandardException;

@StandardException(access = AccessLevel.PROTECTED)
public class PropertiesValidationException extends RuntimeException {

  private static final String CONFIG_CONSTRAINT_VIOLATIONS =
      "Detected config constraint violations: %s";

  public static <T> @NonNull PropertiesValidationException constraintViolations(
      @NonNull Set<ConstraintViolation<T>> constraintViolations) {
    String message = String.format(CONFIG_CONSTRAINT_VIOLATIONS, constraintViolations);
    return new PropertiesValidationException(message);
  }
}
