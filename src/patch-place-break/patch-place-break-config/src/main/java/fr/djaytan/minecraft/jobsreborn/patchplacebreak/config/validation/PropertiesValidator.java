package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/** Validator of any {@link ValidatingConvertibleProperties}. */
@Singleton
@Slf4j
public final class PropertiesValidator {

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  private final Validator validator;

  @Inject
  public PropertiesValidator(Validator validator) {
    this.validator = validator;
  }

  /**
   * Validates the specified {@link ValidatingConvertibleProperties} and convert it to the
   * corresponding final data structure.
   *
   * @param validatingConvertibleProperties The properties to validate and then convert to the final
   *     data structure.
   * @param <T> The targeted type for the conversion.
   * @return The validated config converted to the final data structure.
   * @throws PropertiesValidationException If {@link ConstraintViolation}s are detected.
   */
  public <T> @NonNull T validate(
      @NonNull ValidatingConvertibleProperties<T> validatingConvertibleProperties) {
    String propertiesTypeName = validatingConvertibleProperties.getClass().getSimpleName();
    log.atInfo().log("Validating properties of type '{}'...", propertiesTypeName);

    Set<ConstraintViolation<ValidatingConvertibleProperties<T>>> constraintViolations =
        validator.validate(validatingConvertibleProperties);

    if (!constraintViolations.isEmpty()) {
      String formatted = ConstraintViolationFormatter.format(constraintViolations);
      log.atError().log("Detected constraint violations:{}{}", LINE_SEPARATOR, formatted);
      throw PropertiesValidationException.constraintViolations(constraintViolations);
    }

    validatingConvertibleProperties.markAsValidated();
    T validatedProperties = validatingConvertibleProperties.convert();
    log.atInfo().log("Properties of type '{}' validated.", propertiesTypeName);
    return validatedProperties;
  }
}
