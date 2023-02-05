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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.annotated;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.spongepowered.configurate.serialize.SerializationException;

import com.google.common.base.Strings;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.TestResourcesHelper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.testutils.ValidatorTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.testutils.YamlDeserializerTestWrapper;
import jakarta.validation.ConstraintViolation;

class CredentialsValidatingPropertiesIT {

  @Nested
  @DisplayName("When validating")
  class WhenValidating {

    @Test
    @DisplayName("With only valid values")
    void withOnlyValidValues_shouldNotGenerateConstraintViolations() {
      // Given
      CredentialsValidatingProperties credentialsValidatingProperties =
          CredentialsValidatingProperties.of("foo", "bar");

      // When
      Set<ConstraintViolation<CredentialsValidatingProperties>> constraintViolations =
          ValidatorTestWrapper.validate(credentialsValidatingProperties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    @DisplayName("With only invalid values")
    void withOnlyInvalidValues_shouldGenerateConstraintViolations() {
      // Given
      CredentialsValidatingProperties credentialsValidatingProperties =
          CredentialsValidatingProperties.of("", null);

      // When
      Set<ConstraintViolation<CredentialsValidatingProperties>> constraintViolations =
          ValidatorTestWrapper.validate(credentialsValidatingProperties);

      // Then
      assertThat(constraintViolations).hasSize(2);
    }

    @Nested
    @DisplayName("'username' field")
    @TestInstance(Lifecycle.PER_CLASS)
    class UsernameField {

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With valid values")
      void withValidValues_shouldNotGenerateConstraintViolations(String validUsername) {
        // Given
        CredentialsValidatingProperties credentialsValidatingProperties =
            CredentialsValidatingProperties.of(validUsername, "bar");

        // When
        Set<ConstraintViolation<CredentialsValidatingProperties>> constraintViolations =
            ValidatorTestWrapper.validate(credentialsValidatingProperties);

        // Then
        assertThat(constraintViolations).isEmpty();
      }

      private Stream<Arguments> withValidValues_shouldNotGenerateConstraintViolations() {
        return Stream.of(Arguments.of(Named.of("Longest allowed value", Strings.repeat("s", 32))),
            Arguments.of(Named.of("Shortest allowed value", "s")));
      }

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With invalid values")
      void withInvalidValues_shouldGenerateConstraintViolations(String invalidUsername) {
        // Given
        CredentialsValidatingProperties credentialsValidatingProperties =
            CredentialsValidatingProperties.of(invalidUsername, "bar");

        // When
        Set<ConstraintViolation<CredentialsValidatingProperties>> constraintViolations =
            ValidatorTestWrapper.validate(credentialsValidatingProperties);

        // Then
        assertThat(constraintViolations).hasSize(1).element(0)
            .matches(constraintViolation -> constraintViolation
                .getRootBeanClass() == CredentialsValidatingProperties.class)
            .matches(constraintViolation -> Objects.equals(constraintViolation.getInvalidValue(),
                invalidUsername))
            .matches(constraintViolation -> constraintViolation.getPropertyPath().toString()
                .equals("username"));
      }

      private Stream<Arguments> withInvalidValues_shouldGenerateConstraintViolations() {
        return Stream.of(Arguments.of(Named.of("Null value", null)),
            Arguments.of(Named.of("Empty value", "")), Arguments.of(Named.of("Blank value", " ")),
            Arguments.of(Named.of("Too long value", Strings.repeat("s", 33))));
      }
    }

    @Nested
    @DisplayName("'password' field")
    @TestInstance(Lifecycle.PER_CLASS)
    class PasswordField {

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With valid values")
      void withValidValues_shouldNotGenerateConstraintViolations(String validPassword) {
        // Given
        CredentialsValidatingProperties credentialsValidatingProperties =
            CredentialsValidatingProperties.of("for", validPassword);

        // When
        Set<ConstraintViolation<CredentialsValidatingProperties>> constraintViolations =
            ValidatorTestWrapper.validate(credentialsValidatingProperties);

        // Then
        assertThat(constraintViolations).isEmpty();
      }

      /*
       * Note: No check are done for blank values.
       */
      private Stream<Arguments> withValidValues_shouldNotGenerateConstraintViolations() {
        return Stream.of(Arguments.of(Named.of("Longest allowed value", Strings.repeat("s", 128))),
            Arguments.of(Named.of("Shortest allowed value", "")),
            Arguments.of(Named.of("Blank value", " ")));
      }

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With invalid values")
      void withInvalidValues_shouldGenerateConstraintViolations(String invalidPassword) {
        // Given
        CredentialsValidatingProperties credentialsValidatingProperties =
            CredentialsValidatingProperties.of("foo", invalidPassword);

        // When
        Set<ConstraintViolation<CredentialsValidatingProperties>> constraintViolations =
            ValidatorTestWrapper.validate(credentialsValidatingProperties);

        // Then
        assertThat(constraintViolations).hasSize(1).element(0)
            .matches(constraintViolation -> constraintViolation
                .getRootBeanClass() == CredentialsValidatingProperties.class)
            .matches(constraintViolation -> Objects.equals(constraintViolation.getInvalidValue(),
                invalidPassword))
            .matches(constraintViolation -> constraintViolation.getPropertyPath().toString()
                .equals("password"));
      }

      private Stream<Arguments> withInvalidValues_shouldGenerateConstraintViolations() {
        return Stream.of(Arguments.of(Named.of("Null value", null)),
            Arguments.of(Named.of("Too long value", Strings.repeat("s", 129))));
      }
    }
  }

  @Nested
  @DisplayName("When deserializing from YAML")
  @TestInstance(Lifecycle.PER_CLASS)
  class WhenDeserializingFromYaml {

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With valid content")
    void withValidContent_shouldMatchExpectedValue(String yamlFileName,
        CredentialsValidatingProperties expectedValue) {
      // Given
      Path yamlFile = TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      Optional<CredentialsValidatingProperties> optionalCredentialsValidatingProperties =
          YamlDeserializerTestWrapper.deserialize(yamlFile, CredentialsValidatingProperties.class);

      // Then
      assertThat(optionalCredentialsValidatingProperties).isPresent().get()
          .isEqualTo(expectedValue);
    }

    private Stream<Arguments> withValidContent_shouldMatchExpectedValue() {
      return Stream.of(
          Arguments.of(Named.of("With valid values", "whenDeserializing_withValidValues.yml"),
              CredentialsValidatingProperties.of("foo", "bar")),
          Arguments.of(
              Named.of("With unexpected field", "whenDeserializing_withUnexpectedField.yml"),
              CredentialsValidatingProperties.of("foo", "bar")),
          Arguments.of(
              Named.of("With 'isValidated' field", "whenDeserializing_withIsValidatedField.yml"),
              CredentialsValidatingProperties.of("foo", "bar")));
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With invalid content")
    void withInvalidContent_shouldThrowSerializationException(String yamlFileName) {
      // Given
      Path yamlFile = TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      ThrowingCallable throwingCallable = () -> YamlDeserializerTestWrapper.deserialize(yamlFile,
          CredentialsValidatingProperties.class);

      // Then
      assertThatThrownBy(throwingCallable).isInstanceOf(SerializationException.class);
    }

    private Stream<Arguments> withInvalidContent_shouldThrowSerializationException() {
      return Stream.of(
          Arguments.of(Named.of("With missing 'username' field",
              "whenDeserializing_withMissingUsernameField.yml")),
          Arguments.of(Named.of("With missing 'password' field",
              "whenDeserializing_withMissingPasswordField.yml")));
    }

    @Test
    @DisplayName("With empty content")
    void withEmptyContent_shouldGenerateNullValue() {
      // Given
      String yamlFileName = "whenDeserializing_withEmptyContent.yml";
      Path yamlFile = TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      Optional<CredentialsValidatingProperties> credentialsValidatingProperties =
          YamlDeserializerTestWrapper.deserialize(yamlFile, CredentialsValidatingProperties.class);

      // Then
      assertThat(credentialsValidatingProperties).isNotPresent();
    }
  }
}
