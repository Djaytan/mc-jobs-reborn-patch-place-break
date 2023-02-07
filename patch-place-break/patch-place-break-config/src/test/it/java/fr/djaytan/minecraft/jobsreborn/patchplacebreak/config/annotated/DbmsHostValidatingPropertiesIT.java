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
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.deserialization.YamlDeserializationException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.ValidatorTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.YamlDeserializerTestWrapper;
import jakarta.validation.ConstraintViolation;

class DbmsHostValidatingPropertiesIT {

  @Nested
  @DisplayName("When validating")
  class WhenValidating {

    @Test
    @DisplayName("With only valid values")
    void withOnlyValidValues_shouldNotGenerateConstraintViolations() {
      // Given
      DbmsHostValidatingProperties dbmsHostValidatingProperties =
          DbmsHostValidatingProperties.of("example.com", 1234, true);

      // When
      Set<ConstraintViolation<DbmsHostValidatingProperties>> constraintViolations =
          ValidatorTestWrapper.validate(dbmsHostValidatingProperties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    /*
     * Note: isSslEnabled is always valid.
     */
    @Test
    @DisplayName("With only invalid values")
    void withOnlyInvalidValues_shouldGenerateConstraintViolations() {
      // Given
      DbmsHostValidatingProperties dbmsHostValidatingProperties =
          DbmsHostValidatingProperties.of("", -1, false);

      // When
      Set<ConstraintViolation<DbmsHostValidatingProperties>> constraintViolations =
          ValidatorTestWrapper.validate(dbmsHostValidatingProperties);

      // Then
      assertThat(constraintViolations).hasSize(2);
    }

    @Nested
    @DisplayName("'hostname' field")
    @TestInstance(Lifecycle.PER_CLASS)
    class HostnameField {

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With valid values")
      void withValidValues_shouldNotGenerateConstraintViolations(String validHostname) {
        // Given
        DbmsHostValidatingProperties dbmsHostValidatingProperties =
            DbmsHostValidatingProperties.of(validHostname, 1234, true);

        // When
        Set<ConstraintViolation<DbmsHostValidatingProperties>> constraintViolations =
            ValidatorTestWrapper.validate(dbmsHostValidatingProperties);

        // Then
        assertThat(constraintViolations).isEmpty();
      }

      /*
       * We accept most invalid values here. More details given
       * in the DbmsHostValidatingProperties class.
       */
      private Stream<Arguments> withValidValues_shouldNotGenerateConstraintViolations() {
        return Stream.of(Arguments.of(Named.of("Nominal IPv4 address", "92.0.2.146")),
            Arguments
                .of(Named.of("Nominal IPv6 address", "2001:db8:3333:4444:5555:6666:7777:8888")),
            Arguments.of(Named.of("Domain name address", "my.example.com")),
            Arguments.of(Named.of("Longest valid value", Strings.repeat("s", 255))),
            Arguments.of(Named.of("Shortest valid value", "s")),
            Arguments.of(Named.of("Invalid IPv4 address", "-1.-1.-1.-1")), Arguments
                .of(Named.of("Invalid IPv6 address", "ZZZZ:ZZZZ:ZZZZ:ZZZZ:ZZZZ:ZZZZ:ZZZZ:ZZZZ")));
      }

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With invalid values")
      void withInvalidValues_shouldGenerateConstraintViolations(String invalidHostname) {
        // Given
        DbmsHostValidatingProperties dbmsHostValidatingProperties =
            DbmsHostValidatingProperties.of(invalidHostname, 1234, true);

        // When
        Set<ConstraintViolation<DbmsHostValidatingProperties>> constraintViolations =
            ValidatorTestWrapper.validate(dbmsHostValidatingProperties);

        // Then
        assertThat(constraintViolations).hasSize(1).element(0)
            .matches(constraintViolation -> constraintViolation
                .getRootBeanClass() == DbmsHostValidatingProperties.class)
            .matches(constraintViolation -> Objects.equals(constraintViolation.getInvalidValue(),
                invalidHostname))
            .matches(constraintViolation -> constraintViolation.getPropertyPath().toString()
                .equals("hostname"));
      }

      private Stream<Arguments> withInvalidValues_shouldGenerateConstraintViolations() {
        return Stream.of(Arguments.of(Named.of("Null value", null)),
            Arguments.of(Named.of("Empty value", "")), Arguments.of(Named.of("Blank value", " ")),
            Arguments.of(Named.of("Too long value", Strings.repeat("s", 256))));
      }
    }

    @Nested
    @DisplayName("'port' field")
    @TestInstance(Lifecycle.PER_CLASS)
    class PortField {

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With valid values")
      void withValidValues_shouldNotGenerateConstraintViolations(int validPort) {
        // Given
        DbmsHostValidatingProperties dbmsHostValidatingProperties =
            DbmsHostValidatingProperties.of("example.com", validPort, true);

        // When
        Set<ConstraintViolation<DbmsHostValidatingProperties>> constraintViolations =
            ValidatorTestWrapper.validate(dbmsHostValidatingProperties);

        // Then
        assertThat(constraintViolations).isEmpty();
      }

      private Stream<Arguments> withValidValues_shouldNotGenerateConstraintViolations() {
        return Stream.of(Arguments.of(Named.of("Highest possible value", 65535)),
            Arguments.of(Named.of("Lowest possible value", 1)));
      }

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With invalid values")
      void withInvalidValues_shouldGenerateConstraintViolations(int invalidPort) {
        // Given
        DbmsHostValidatingProperties dbmsHostValidatingProperties =
            DbmsHostValidatingProperties.of("example.com", invalidPort, true);

        // When
        Set<ConstraintViolation<DbmsHostValidatingProperties>> constraintViolations =
            ValidatorTestWrapper.validate(dbmsHostValidatingProperties);

        // Then
        assertThat(constraintViolations).hasSize(1).element(0)
            .matches(constraintViolation -> constraintViolation
                .getRootBeanClass() == DbmsHostValidatingProperties.class)
            .matches(constraintViolation -> Objects.equals(constraintViolation.getInvalidValue(),
                invalidPort))
            .matches(constraintViolation -> constraintViolation.getPropertyPath().toString()
                .equals("port"));
      }

      private Stream<Arguments> withInvalidValues_shouldGenerateConstraintViolations() {
        return Stream.of(Arguments.of(Named.of("Port n°0", 0)),
            Arguments.of(Named.of("Too high port", 65536)),
            Arguments.of(Named.of("Too low port", -1)));
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
        DbmsHostValidatingProperties expectedValue) {
      // Given
      Path yamlFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      Optional<DbmsHostValidatingProperties> optionalHostValidatingProperties =
          YamlDeserializerTestWrapper.deserialize(yamlFile, DbmsHostValidatingProperties.class);

      // Then
      assertThat(optionalHostValidatingProperties).isPresent().get().isEqualTo(expectedValue);
    }

    private Stream<Arguments> withValidContent_shouldMatchExpectedValue() {
      return Stream.of(
          Arguments.of(Named.of("With valid values", "whenDeserializing_withValidValues.yml"),
              DbmsHostValidatingProperties.of("example.com", 1234, true)),
          Arguments.of(
              Named.of("With unexpected field", "whenDeserializing_withUnexpectedField.yml"),
              DbmsHostValidatingProperties.of("example.com", 1234, true)),
          Arguments.of(
              Named.of("With 'isValidated' field", "whenDeserializing_withIsValidatedField.yml"),
              DbmsHostValidatingProperties.of("example.com", 1234, true)));
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With invalid content")
    void withInvalidContent_shouldThrowYamlDeserializationException(String yamlFileName) {
      // Given
      Path yamlFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      ThrowingCallable throwingCallable = () -> YamlDeserializerTestWrapper.deserialize(yamlFile,
          DbmsHostValidatingProperties.class);

      // Then
      assertThatThrownBy(throwingCallable).isInstanceOf(YamlDeserializationException.class)
          .hasCauseExactlyInstanceOf(SerializationException.class);
    }

    private Stream<Arguments> withInvalidContent_shouldThrowYamlDeserializationException() {
      return Stream.of(
          Arguments.of(Named.of("With missing 'hostname' field",
              "whenDeserializing_withMissingHostnameField.yml")),
          Arguments.of(
              Named.of("With missing 'port' field", "whenDeserializing_withMissingPortField.yml")),
          Arguments.of(Named.of("With missing 'isSslEnabled' field",
              "whenDeserializing_withMissingIsSslEnabledField.yml")));
    }

    @Test
    @DisplayName("With empty content")
    void withEmptyContent_shouldGenerateNullValue() {
      // Given
      String yamlFileName = "whenDeserializing_withEmptyContent.yml";
      Path yamlFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      Optional<DbmsHostValidatingProperties> dbmsHostValidatingProperties =
          YamlDeserializerTestWrapper.deserialize(yamlFile, DbmsHostValidatingProperties.class);

      // Then
      assertThat(dbmsHostValidatingProperties).isNotPresent();
    }
  }
}
