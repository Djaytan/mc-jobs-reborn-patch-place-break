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
package fr.djaytan.mc.jrppb.core.config.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

import fr.djaytan.mc.jrppb.commons.test.TestResourcesHelper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConstraintViolationFormatterTest {

  @Nested
  class WhenFormatting {

    @Test
    void withoutAnyConstraintViolation_shouldThrowException() {
      // Given
      Collection<ConstraintViolation<Object>> constraintViolations = Collections.emptySet();

      // When
      Exception exception =
          catchException(() -> ConstraintViolationFormatter.format(constraintViolations));

      // Then
      assertThat(exception).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withOneConstraintViolation_shouldMatchExpectations(
        @Mock @NotNull ConstraintViolation<Object> constraintViolationMocked,
        @Mock @NotNull Path propertyPathMocked) {
      // Given
      given(propertyPathMocked.toString()).willReturn("theImpactedProperty");
      given(constraintViolationMocked.getPropertyPath()).willReturn(propertyPathMocked);
      given(constraintViolationMocked.getMessage()).willReturn("A message");
      given(constraintViolationMocked.getInvalidValue()).willReturn("An invalid value");

      Collection<ConstraintViolation<Object>> constraintViolations =
          Collections.singleton(constraintViolationMocked);

      // When
      String actualFormattedText = ConstraintViolationFormatter.format(constraintViolations);

      // Then
      String fileName = "whenFormatting_withOneConstraintViolation.txt";
      String expectedFormattedText =
          TestResourcesHelper.getClassResourceAsString(this.getClass(), fileName, true);
      assertThat(actualFormattedText).isEqualTo(expectedFormattedText);
    }

    @Test
    void withSeveralConstraintViolations_shouldMatchExpectations(
        @Mock @NotNull ConstraintViolation<Object> constraintViolationMocked1,
        @Mock @NotNull Path propertyPathMocked1,
        @Mock @NotNull ConstraintViolation<Object> constraintViolationMocked2,
        @Mock @NotNull Path propertyPathMocked2) {
      // Given
      given(propertyPathMocked1.toString()).willReturn("theImpactedProperty1");
      given(constraintViolationMocked1.getPropertyPath()).willReturn(propertyPathMocked1);
      given(constraintViolationMocked1.getMessage()).willReturn("A message");
      given(constraintViolationMocked1.getInvalidValue()).willReturn("An invalid value");

      given(propertyPathMocked2.toString()).willReturn("theImpactedProperty2");
      given(constraintViolationMocked2.getPropertyPath()).willReturn(propertyPathMocked2);
      given(constraintViolationMocked2.getMessage()).willReturn("Another message");
      given(constraintViolationMocked2.getInvalidValue()).willReturn("Another invalid value");

      Collection<ConstraintViolation<Object>> constraintViolations =
          Arrays.asList(constraintViolationMocked1, constraintViolationMocked2);

      // When
      String actualFormattedText = ConstraintViolationFormatter.format(constraintViolations);

      // Then
      String fileName = "whenFormatting_withSeveralConstraintViolations.txt";
      String expectedFormattedText =
          TestResourcesHelper.getClassResourceAsString(this.getClass(), fileName, true);
      assertThat(actualFormattedText).isEqualToIgnoringNewLines(expectedFormattedText);
    }
  }
}
