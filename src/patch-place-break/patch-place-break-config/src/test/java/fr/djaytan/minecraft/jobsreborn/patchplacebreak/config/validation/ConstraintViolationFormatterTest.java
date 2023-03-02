package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.TestResourcesHelper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConstraintViolationFormatterTest {

  @Nested
  @DisplayName("When formatting")
  class WhenFormatting {

    @Test
    @DisplayName("Without any constraint violation")
    void withoutAnyConstraintViolation_shouldThrowException() {
      // Given
      Collection<ConstraintViolation<Object>> constraintViolations = Collections.emptySet();

      // When
      ThrowingCallable throwingCallable =
          () -> ConstraintViolationFormatter.format(constraintViolations);

      // Then
      assertThatThrownBy(throwingCallable).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("With one constraint violation")
    void withOneConstraintViolation_shouldMatchExpectations(
        @Mock ConstraintViolation<Object> constraintViolationMocked,
        @Mock Path propertyPathMocked) {
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
    @DisplayName("With several constraint violations")
    void withSeveralConstraintViolations_shouldMatchExpectations(
        @Mock ConstraintViolation<Object> constraintViolationMocked1,
        @Mock Path propertyPathMocked1,
        @Mock ConstraintViolation<Object> constraintViolationMocked2,
        @Mock Path propertyPathMocked2) {
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
