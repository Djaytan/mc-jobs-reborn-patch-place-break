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
package fr.djaytan.mc.jrppb.config.validation;

import jakarta.validation.ConstraintViolation;
import java.util.Collection;
import java.util.StringJoiner;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

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
  static <T> @NotNull String format(
      @NotNull Collection<ConstraintViolation<T>> constraintViolations) {
    Validate.notEmpty(constraintViolations);

    StringJoiner stringJoiner = new StringJoiner(LINE_SEPARATOR);

    for (ConstraintViolation<T> constraintViolation : constraintViolations) {
      String formatted = format(constraintViolation);
      stringJoiner.add(formatted);
    }
    return stringJoiner.toString();
  }

  private static <T> @NotNull String format(@NotNull ConstraintViolation<T> constraintViolation) {
    return String.format(
        FORMAT_TEMPLATE,
        constraintViolation.getPropertyPath(),
        constraintViolation.getMessage(),
        constraintViolation.getInvalidValue());
  }
}
