package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation;

import jakarta.validation.ConstraintViolation;
import java.util.Collection;
import java.util.StringJoiner;
import lombok.NonNull;
import org.apache.commons.lang3.Validate;

/** Formatter of a {@link ConstraintViolation} to improve readability. */
final class ConstraintViolationFormatter {

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  private static final String FORMAT_TEMPLATE = " * Property '%s' - %s (invalid value: '%s')";

  private ConstraintViolationFormatter() {
    // Static class
  }

  /**
   * Formats the constraint violations into a more readable text.
   *
   * @param constraintViolations The constraint violations to format.
   * @return The constraint violations formatted into a more readable text.
   */
  static <T> @NonNull String format(
      @NonNull Collection<ConstraintViolation<T>> constraintViolations) {
    Validate.notEmpty(constraintViolations);

    StringJoiner stringJoiner = new StringJoiner(LINE_SEPARATOR);

    for (ConstraintViolation<T> constraintViolation : constraintViolations) {
      String formatted = format(constraintViolation);
      stringJoiner.add(formatted);
    }
    return stringJoiner.toString();
  }

  private static <T> @NonNull String format(@NonNull ConstraintViolation<T> constraintViolation) {
    return String.format(
        FORMAT_TEMPLATE,
        constraintViolation.getPropertyPath(),
        constraintViolation.getMessage(),
        constraintViolation.getInvalidValue());
  }
}
