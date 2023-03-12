/*
 * The MIT License
 * Copyright © 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.properties.Properties;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

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
   * Validates the specified properties.
   *
   * @param properties The properties to validate.
   * @throws PropertiesValidationException If {@link ConstraintViolation}s are detected.
   */
  public void validate(@NonNull Properties properties) {
    String propertiesTypeName = properties.getClass().getSimpleName();
    log.atInfo().log("Validating properties of type '{}'...", propertiesTypeName);

    Set<ConstraintViolation<Properties>> constraintViolations = validator.validate(properties);

    if (!constraintViolations.isEmpty()) {
      String formatted = ConstraintViolationFormatter.format(constraintViolations);
      log.atError().log("Detected constraint violations:{}{}", LINE_SEPARATOR, formatted);
      throw PropertiesValidationException.constraintViolations(constraintViolations);
    }

    log.atInfo().log("Properties of type '{}' validated.", propertiesTypeName);
  }
}
