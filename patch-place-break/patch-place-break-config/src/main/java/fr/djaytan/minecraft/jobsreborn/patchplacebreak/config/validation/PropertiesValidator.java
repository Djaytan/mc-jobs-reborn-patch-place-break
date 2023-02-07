/*
 * MIT License
 *
 * Copyright (c) 2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Validator of any {@link ValidatingConvertibleProperties}.
 */
@Singleton
@Slf4j
public class PropertiesValidator {

  private final Validator validator;

  @Inject
  public PropertiesValidator(Validator validator) {
    this.validator = validator;
  }

  /**
   * Validates the specified {@link ValidatingConvertibleProperties} and convert it
   * to the corresponding final data structure.
   *
   * @param validatingConvertibleProperties The properties to validate and then convert
   *                                        to the final data structure.
   * @param <T> The targeted type for the conversion.
   * @return The validated config converted to the final data structure.
   * @throws PropertiesValidationException If {@link ConstraintViolation}s are detected.
   */
  public <T> @NonNull T validate(
      @NonNull ValidatingConvertibleProperties<T> validatingConvertibleProperties)
      throws PropertiesValidationException {
    String propertiesTypeName = validatingConvertibleProperties.getClass().getSimpleName();
    log.atInfo().log("Validating properties of type '{}'...", propertiesTypeName);

    Set<ConstraintViolation<ValidatingConvertibleProperties<T>>> constraintViolations =
        validator.validate(validatingConvertibleProperties);

    if (!constraintViolations.isEmpty()) {
      String formatted = ConstraintViolationFormatter.format(constraintViolations);
      log.atError().log("Detected constraint violations:\n{}", formatted);
      throw PropertiesValidationException.constraintViolations(constraintViolations);
    }

    validatingConvertibleProperties.markAsValidated();
    T validatedProperties = validatingConvertibleProperties.convert();
    log.atInfo().log("Properties of type '{}' validated.", propertiesTypeName);
    return validatedProperties;
  }
}
