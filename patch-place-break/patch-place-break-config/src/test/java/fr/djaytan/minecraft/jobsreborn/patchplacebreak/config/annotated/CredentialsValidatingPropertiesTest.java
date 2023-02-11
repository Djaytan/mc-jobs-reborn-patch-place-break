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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.TestResourcesHelper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization.ConfigSerializationException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.ConfigSerializerTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.ValidatorTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.CredentialsProperties;
import jakarta.validation.ConstraintViolation;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class CredentialsValidatingPropertiesTest {

  @Test
  @DisplayName("When calling equals() & hashCode()")
  void whenCallingEqualsAndHashcode_shouldMetContracts() {
    EqualsVerifier.forClass(CredentialsValidatingProperties.class).withRedefinedSuperclass()
        .suppress(Warning.NONFINAL_FIELDS).verify();
  }

  @Test
  @DisplayName("When calling toString()")
  void whenCallingToString_shouldMetContracts() {
    ToStringVerifier.forClass(CredentialsValidatingProperties.class)
        .withClassName(NameStyle.SIMPLE_NAME).withIgnoredFields("password")
        .withFailOnExcludedFields(true).verify();
  }

  @Nested
  @DisplayName("When instantiating")
  class WhenInstantiating {

    @Test
    @DisplayName("With no args constructor")
    void withNoArgsConstructor_shouldMatchDefaultValues() {
      // Given

      // When
      CredentialsValidatingProperties credentialsValidatingProperties =
          new CredentialsValidatingProperties();

      // Then
      assertAll(() -> assertThat(credentialsValidatingProperties.getUsername()).isNull(),
          () -> assertThat(credentialsValidatingProperties.getPassword()).isNull(),
          () -> assertThat(credentialsValidatingProperties.isValidated()).isFalse());
    }

    @Test
    @DisplayName("With all args constructor")
    void withAllArgsConstructor_shouldMatchGivenArguments() {
      // Given
      String username = "foo";
      String password = "bar";

      // When
      CredentialsValidatingProperties credentialsValidatingProperties =
          CredentialsValidatingProperties.of(username, password);

      // Then
      assertAll(() -> assertThat(credentialsValidatingProperties.getUsername()).isEqualTo("foo"),
          () -> assertThat(credentialsValidatingProperties.getPassword()).isEqualTo("bar"),
          () -> assertThat(credentialsValidatingProperties.isValidated()).isFalse());
    }
  }

  @Nested
  @DisplayName("When converting")
  class WhenConverting {

    @Test
    @DisplayName("With properties marked as validated")
    void withPropertiesMarkedAsValidated_shouldConvertSuccessfully() {
      // Given
      CredentialsValidatingProperties credentialsValidatingProperties =
          CredentialsValidatingProperties.of("foo", "bar");
      credentialsValidatingProperties.markAsValidated();

      // When
      CredentialsProperties credentialsProperties = credentialsValidatingProperties.convert();

      // Then
      assertAll(() -> assertThat(credentialsValidatingProperties.isValidated()).isTrue(),
          () -> assertThat(credentialsProperties.getUsername()).isEqualTo("foo"),
          () -> assertThat(credentialsProperties.getPassword()).isEqualTo("bar"));
    }

    @Test
    @DisplayName("With properties not marked as validated")
    void withPropertiesNotMarkedAsValidated_shouldThrowIllegalStateException() {
      // Given
      CredentialsValidatingProperties credentialsValidatingProperties =
          CredentialsValidatingProperties.of("foo", "bar");

      // When
      ThrowingCallable throwingCallable = credentialsValidatingProperties::convert;

      // Then
      assertAll(() -> assertThat(credentialsValidatingProperties.isValidated()).isFalse(),
          () -> assertThatIllegalStateException().isThrownBy(throwingCallable)
              .withMessage("Properties must be validated before being converted"));
    }
  }

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
      Path yamlFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      Optional<CredentialsValidatingProperties> optionalCredentialsValidatingProperties =
          ConfigSerializerTestWrapper.deserialize(yamlFile, CredentialsValidatingProperties.class);

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
    void withInvalidContent_shouldThrowYamlDeserializationException(String yamlFileName) {
      // Given
      Path yamlFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      ThrowingCallable throwingCallable = () -> ConfigSerializerTestWrapper.deserialize(yamlFile,
          CredentialsValidatingProperties.class);

      // Then
      assertThatThrownBy(throwingCallable).isInstanceOf(ConfigSerializationException.class)
          .hasCauseExactlyInstanceOf(SerializationException.class);
    }

    private Stream<Arguments> withInvalidContent_shouldThrowYamlDeserializationException() {
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
      Path yamlFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      Optional<CredentialsValidatingProperties> credentialsValidatingProperties =
          ConfigSerializerTestWrapper.deserialize(yamlFile, CredentialsValidatingProperties.class);

      // Then
      assertThat(credentialsValidatingProperties).isNotPresent();
    }
  }
}
