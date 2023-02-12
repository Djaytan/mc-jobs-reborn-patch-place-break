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

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.TestResourcesHelper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization.ConfigSerializationException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.ConfigSerializerTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.ValidatorTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.ConnectionPoolProperties;
import jakarta.validation.ConstraintViolation;
import lombok.NonNull;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class ConnectionPoolValidatingPropertiesTest {

  @Test
  @DisplayName("When calling equals() & hashCode()")
  void whenCallingEqualsAndHashcode_shouldMetContracts() {
    EqualsVerifier.forClass(ConnectionPoolValidatingProperties.class).withRedefinedSuperclass()
        .suppress(Warning.NONFINAL_FIELDS).verify();
  }

  @Test
  @DisplayName("When calling toString()")
  void whenCallingToString_shouldMetContracts() {
    ToStringVerifier.forClass(ConnectionPoolValidatingProperties.class)
        .withClassName(NameStyle.SIMPLE_NAME).verify();
  }

  @Nested
  @DisplayName("When instantiating")
  class WhenInstantiating {

    @Test
    @DisplayName("With no args constructor")
    void withNoArgsConstructor_shouldMatchDefaultValues() {
      // Given

      // When
      ConnectionPoolValidatingProperties connectionPoolValidatingProperties =
          new ConnectionPoolValidatingProperties();

      // Then
      assertAll(
          () -> assertThat(connectionPoolValidatingProperties.getConnectionTimeout())
              .isEqualTo(60000),
          () -> assertThat(connectionPoolValidatingProperties.getPoolSize()).isEqualTo(10),
          () -> assertThat(connectionPoolValidatingProperties.isValidated()).isFalse());
    }

    @Test
    @DisplayName("With all args constructor")
    void withAllArgsConstructor_shouldMatchGivenArguments() {
      // Given
      long connectionTimeout = 30000;
      int poolSize = 10;

      // When
      ConnectionPoolValidatingProperties connectionPoolValidatingProperties =
          ConnectionPoolValidatingProperties.of(connectionTimeout, poolSize);

      // Then
      assertAll(
          () -> assertThat(connectionPoolValidatingProperties.getConnectionTimeout())
              .isEqualTo(connectionTimeout),
          () -> assertThat(connectionPoolValidatingProperties.getPoolSize()).isEqualTo(poolSize),
          () -> assertThat(connectionPoolValidatingProperties.isValidated()).isFalse());
    }
  }

  @Nested
  @DisplayName("When converting")
  class WhenConverting {

    @Test
    @DisplayName("With properties marked as validated")
    void withPropertiesMarkedAsValidated_shouldConvertSuccessfully() {
      // Given
      ConnectionPoolValidatingProperties connectionPoolValidatingProperties =
          ConnectionPoolValidatingProperties.of(60000, 10);
      connectionPoolValidatingProperties.markAsValidated();

      // When
      ConnectionPoolProperties connectionPoolProperties =
          connectionPoolValidatingProperties.convert();

      // Then
      assertAll(() -> assertThat(connectionPoolValidatingProperties.isValidated()).isTrue(),
          () -> assertThat(connectionPoolProperties.getConnectionTimeout()).isEqualTo(60000),
          () -> assertThat(connectionPoolProperties.getPoolSize()).isEqualTo(10));
    }

    @Test
    @DisplayName("With properties not marked as validated")
    void withPropertiesNotMarkedAsValidated_shouldThrowException() {
      // Given
      ConnectionPoolValidatingProperties connectionPoolValidatingProperties =
          ConnectionPoolValidatingProperties.of(60000, 10);

      // When
      ThrowingCallable throwingCallable = connectionPoolValidatingProperties::convert;

      // Then
      assertAll(() -> assertThat(connectionPoolValidatingProperties.isValidated()).isFalse(),
          () -> assertThatIllegalStateException().isThrownBy(throwingCallable)
              .withMessage("Properties must be validated before being converted"));
    }
  }

  @Nested
  @DisplayName("When validating")
  class WhenValidating {

    @Test
    @DisplayName("With default values")
    void withDefaultValues_shouldNotGenerateConstraintViolations() {
      // Given
      ConnectionPoolValidatingProperties connectionPoolValidatingProperties =
          new ConnectionPoolValidatingProperties();

      // When
      Set<ConstraintViolation<ConnectionPoolValidatingProperties>> constraintViolations =
          ValidatorTestWrapper.validate(connectionPoolValidatingProperties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    @DisplayName("With only valid values")
    void withOnlyValidValues_shouldNotGenerateConstraintViolations() {
      // Given
      ConnectionPoolValidatingProperties connectionPoolValidatingProperties =
          ConnectionPoolValidatingProperties.of(30000, 10);

      // When
      Set<ConstraintViolation<ConnectionPoolValidatingProperties>> constraintViolations =
          ValidatorTestWrapper.validate(connectionPoolValidatingProperties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    @DisplayName("With only invalid values")
    void withOnlyInvalidValues_shouldGenerateConstraintViolations() {
      // Given
      ConnectionPoolValidatingProperties connectionPoolValidatingProperties =
          ConnectionPoolValidatingProperties.of(-1, -1);

      // When
      Set<ConstraintViolation<ConnectionPoolValidatingProperties>> constraintViolations =
          ValidatorTestWrapper.validate(connectionPoolValidatingProperties);

      // Then
      assertThat(constraintViolations).hasSize(2);
    }

    @Nested
    @DisplayName("'connectionTimeout' field")
    @TestInstance(Lifecycle.PER_CLASS)
    class ConnectionTimeoutField {

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With valid values")
      void withValidValues_shouldNotGenerateConstraintViolations(long validConnectionTimeout) {
        // Given
        ConnectionPoolValidatingProperties connectionPoolValidatingProperties =
            ConnectionPoolValidatingProperties.of(validConnectionTimeout, 10);

        // When
        Set<ConstraintViolation<ConnectionPoolValidatingProperties>> constraintViolations =
            ValidatorTestWrapper.validate(connectionPoolValidatingProperties);

        // Then
        assertThat(constraintViolations).isEmpty();
      }

      private @NonNull Stream<Arguments> withValidValues_shouldNotGenerateConstraintViolations() {
        return Stream.of(Arguments.of(Named.of("Highest allowed value", 600000)),
            Arguments.of(Named.of("Lowest allowed value", 1)));
      }

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With invalid values")
      void withInvalidValues_shouldGenerateConstraintViolations(long invalidConnectionTimeout) {
        // Given
        ConnectionPoolValidatingProperties connectionPoolValidatingProperties =
            ConnectionPoolValidatingProperties.of(invalidConnectionTimeout, 10);

        // When
        Set<ConstraintViolation<ConnectionPoolValidatingProperties>> constraintViolations =
            ValidatorTestWrapper.validate(connectionPoolValidatingProperties);

        // Then
        assertThat(constraintViolations).hasSize(1).element(0)
            .matches(constraintViolation -> constraintViolation
                .getRootBeanClass() == ConnectionPoolValidatingProperties.class)
            .matches(constraintViolation -> Objects.equals(constraintViolation.getInvalidValue(),
                invalidConnectionTimeout))
            .matches(constraintViolation -> constraintViolation.getPropertyPath().toString()
                .equals("connectionTimeout"));
      }

      private @NonNull Stream<Arguments> withInvalidValues_shouldGenerateConstraintViolations() {
        return Stream.of(Arguments.of(Named.of("Null value", 0)),
            Arguments.of(Named.of("Too high value", 600001)));
      }
    }

    @Nested
    @DisplayName("'poolSize' field")
    @TestInstance(Lifecycle.PER_CLASS)
    class PoolSizeField {

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With valid values")
      void withValidValues_shouldNotGenerateConstraintViolations(int validPoolSize) {
        // Given
        ConnectionPoolValidatingProperties connectionPoolValidatingProperties =
            ConnectionPoolValidatingProperties.of(60000, validPoolSize);

        // When
        Set<ConstraintViolation<ConnectionPoolValidatingProperties>> constraintViolations =
            ValidatorTestWrapper.validate(connectionPoolValidatingProperties);

        // Then
        assertThat(constraintViolations).isEmpty();
      }

      private @NonNull Stream<Arguments> withValidValues_shouldNotGenerateConstraintViolations() {
        return Stream.of(Arguments.of(Named.of("Highest allowed value", 100)),
            Arguments.of(Named.of("Lowest allowed value", 1)));
      }

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With invalid values")
      void withInvalidValues_shouldGenerateConstraintViolations(int invalidPoolSize) {
        // Given
        ConnectionPoolValidatingProperties connectionPoolValidatingProperties =
            ConnectionPoolValidatingProperties.of(60000, invalidPoolSize);

        // When
        Set<ConstraintViolation<ConnectionPoolValidatingProperties>> constraintViolations =
            ValidatorTestWrapper.validate(connectionPoolValidatingProperties);

        // Then
        assertThat(constraintViolations).hasSize(1).element(0)
            .matches(constraintViolation -> constraintViolation
                .getRootBeanClass() == ConnectionPoolValidatingProperties.class)
            .matches(constraintViolation -> Objects.equals(constraintViolation.getInvalidValue(),
                invalidPoolSize))
            .matches(constraintViolation -> constraintViolation.getPropertyPath().toString()
                .equals("poolSize"));
      }

      private @NonNull Stream<Arguments> withInvalidValues_shouldGenerateConstraintViolations() {
        return Stream.of(Arguments.of(Named.of("Null value", 0)),
            Arguments.of(Named.of("Too high value", 101)));
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
    void withValidContent_shouldMatchExpectedValue(@NonNull String yamlFileName,
        @NonNull ConnectionPoolValidatingProperties expectedValue) {
      // Given
      Path yamlFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      Optional<ConnectionPoolValidatingProperties> optionalConnectionPoolValidatingProperties =
          ConfigSerializerTestWrapper.deserialize(yamlFile,
              ConnectionPoolValidatingProperties.class);

      // Then
      assertThat(optionalConnectionPoolValidatingProperties).isPresent().get()
          .isEqualTo(expectedValue);
    }

    private @NonNull Stream<Arguments> withValidContent_shouldMatchExpectedValue() {
      return Stream.of(
          Arguments.of(Named.of("With valid values", "whenDeserializing_withValidValues.conf"),
              ConnectionPoolValidatingProperties.of(60000, 10)),
          Arguments.of(
              Named.of("With unexpected field", "whenDeserializing_withUnexpectedField.conf"),
              ConnectionPoolValidatingProperties.of(60000, 10)),
          Arguments.of(
              Named.of("With 'isValidated' field", "whenDeserializing_withIsValidatedField.conf"),
              ConnectionPoolValidatingProperties.of(60000, 10)));
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With invalid content")
    void withInvalidContent_shouldThrowException(@NonNull String yamlFileName) {
      // Given
      Path yamlFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      ThrowingCallable throwingCallable = () -> ConfigSerializerTestWrapper.deserialize(yamlFile,
          ConnectionPoolValidatingProperties.class);

      // Then
      assertThatThrownBy(throwingCallable).isInstanceOf(ConfigSerializationException.class)
          .hasCauseExactlyInstanceOf(SerializationException.class);
    }

    private @NonNull Stream<Arguments> withInvalidContent_shouldThrowException() {
      return Stream.of(
          Arguments.of(Named.of("With missing 'connectionTimeout' field",
              "whenDeserializing_withMissingConnectionTimeoutField.conf")),
          Arguments.of(Named.of("With missing 'poolSize' field",
              "whenDeserializing_withMissingPoolSizeField.conf")));
    }

    @Test
    @DisplayName("With empty content")
    void withEmptyContent_shouldGenerateNullValue() {
      // Given
      String yamlFileName = "whenDeserializing_withEmptyContent.conf";
      Path yamlFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      Optional<ConnectionPoolValidatingProperties> connectionPoolValidatingProperties =
          ConfigSerializerTestWrapper.deserialize(yamlFile,
              ConnectionPoolValidatingProperties.class);

      // Then
      assertThat(connectionPoolValidatingProperties).isNotPresent();
    }
  }
}
