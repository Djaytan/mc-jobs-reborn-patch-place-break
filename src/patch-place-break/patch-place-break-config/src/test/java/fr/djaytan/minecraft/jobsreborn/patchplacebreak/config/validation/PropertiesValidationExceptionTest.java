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

import static org.assertj.core.api.Assertions.assertThat;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.ExceptionBaseTest;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import jakarta.validation.ConstraintViolation;
import java.util.Collections;
import java.util.Set;
import lombok.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("java:S2187")
class PropertiesValidationExceptionTest extends ExceptionBaseTest {

  @Override
  protected @NonNull Exception getException() {
    return new PropertiesValidationException();
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message) {
    return new PropertiesValidationException(message);
  }

  @Override
  protected @NonNull Exception getException(Throwable cause) {
    return new PropertiesValidationException(cause);
  }

  @Override
  protected @NonNull Exception getException(@NonNull String message, Throwable cause) {
    return new PropertiesValidationException(message, cause);
  }

  @Test
  @DisplayName("When instantiating constraint violations exception")
  void whenInstantiatingConstraintViolationsException(
      @Mock ConstraintViolation<DataSourceProperties> constraintViolation) {
    // Given
    Set<ConstraintViolation<DataSourceProperties>> constraintViolations =
        Collections.singleton(constraintViolation);

    // When
    PropertiesValidationException propertiesValidationException =
        PropertiesValidationException.constraintViolations(constraintViolations);

    // Then
    assertThat(propertiesValidationException)
        .hasMessageMatching("Detected config constraint violations: .*");
  }
}
