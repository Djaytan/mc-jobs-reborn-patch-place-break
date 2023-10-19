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
package fr.djaytan.mc.jrppb.core.config.testutils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

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
  public static <T> @NotNull Set<ConstraintViolation<T>> validate(@NotNull T object) {
    return INSTANCE.validatorFactory.getValidator().validate(object);
  }

  /**
   * Returns a validator for test purposes.
   *
   * @return A validator for test purposes.
   */
  public static @NotNull Validator getValidator() {
    return INSTANCE.validatorFactory.getValidator();
  }
}
