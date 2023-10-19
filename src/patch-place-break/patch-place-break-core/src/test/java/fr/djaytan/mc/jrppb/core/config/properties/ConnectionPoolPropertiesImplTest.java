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
package fr.djaytan.mc.jrppb.core.config.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import fr.djaytan.mc.jrppb.commons.test.TestResourcesHelper;
import fr.djaytan.mc.jrppb.core.config.serialization.ConfigSerializationException;
import fr.djaytan.mc.jrppb.core.config.testutils.ConfigSerializerTestWrapper;
import fr.djaytan.mc.jrppb.core.config.testutils.ValidatorTestWrapper;
import jakarta.validation.ConstraintViolation;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.spongepowered.configurate.serialize.SerializationException;

class ConnectionPoolPropertiesImplTest {

  private FileSystem imfs;

  @BeforeEach
  void beforeEach() {
    imfs = Jimfs.newFileSystem(Configuration.unix());
  }

  @AfterEach
  void afterEach() throws IOException {
    imfs.close();
  }

  @Test
  @DisplayName("When calling equals() & hashCode()")
  void whenCallingEqualsAndHashcode_shouldMetContracts() {
    EqualsVerifier.forClass(ConnectionPoolPropertiesImpl.class).verify();
  }

  @Test
  @DisplayName("When calling toString()")
  void whenCallingToString_shouldMetContracts() {
    ToStringVerifier.forClass(ConnectionPoolPropertiesImpl.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }

  @Nested
  @DisplayName("When instantiating")
  class WhenInstantiating {

    @Test
    @DisplayName("With no args constructor")
    void withNoArgsConstructor_shouldMatchDefaultValues() {
      // Given

      // When
      ConnectionPoolPropertiesImpl properties = new ConnectionPoolPropertiesImpl();

      // Then
      assertAll(
          () -> assertThat(properties.getConnectionTimeout()).isEqualTo(30000),
          () -> assertThat(properties.getPoolSize()).isEqualTo(10));
    }

    @Test
    @DisplayName("With all args constructor")
    void withAllArgsConstructor_shouldMatchGivenArguments() {
      // Given
      long connectionTimeout = 30000;
      int poolSize = 10;

      // When
      ConnectionPoolPropertiesImpl properties =
          new ConnectionPoolPropertiesImpl(connectionTimeout, poolSize);

      // Then
      assertAll(
          () -> assertThat(properties.getConnectionTimeout()).isEqualTo(connectionTimeout),
          () -> assertThat(properties.getPoolSize()).isEqualTo(poolSize));
    }
  }

  @Nested
  @DisplayName("When validating")
  class WhenValidating {

    @Test
    @DisplayName("With default values")
    void withDefaultValues_shouldNotGenerateConstraintViolations() {
      // Given
      ConnectionPoolPropertiesImpl properties = new ConnectionPoolPropertiesImpl();

      // When
      Set<ConstraintViolation<ConnectionPoolPropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(properties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    @DisplayName("With only valid values")
    void withOnlyValidValues_shouldNotGenerateConstraintViolations() {
      // Given
      ConnectionPoolPropertiesImpl properties = new ConnectionPoolPropertiesImpl(30000, 10);

      // When
      Set<ConstraintViolation<ConnectionPoolPropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(properties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    @DisplayName("With only invalid values")
    void withOnlyInvalidValues_shouldGenerateConstraintViolations() {
      // Given
      ConnectionPoolPropertiesImpl properties = new ConnectionPoolPropertiesImpl(-1, -1);

      // When
      Set<ConstraintViolation<ConnectionPoolPropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(properties);

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
        ConnectionPoolPropertiesImpl properties =
            new ConnectionPoolPropertiesImpl(validConnectionTimeout, 10);

        // When
        Set<ConstraintViolation<ConnectionPoolPropertiesImpl>> constraintViolations =
            ValidatorTestWrapper.validate(properties);

        // Then
        assertThat(constraintViolations).isEmpty();
      }

      private @NotNull Stream<Arguments> withValidValues_shouldNotGenerateConstraintViolations() {
        return Stream.of(
            arguments(named("Highest allowed value", 600000)),
            arguments(named("Lowest allowed value", 1)));
      }

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With invalid values")
      void withInvalidValues_shouldGenerateConstraintViolations(long invalidConnectionTimeout) {
        // Given
        ConnectionPoolPropertiesImpl properties =
            new ConnectionPoolPropertiesImpl(invalidConnectionTimeout, 10);

        // When
        Set<ConstraintViolation<ConnectionPoolPropertiesImpl>> constraintViolations =
            ValidatorTestWrapper.validate(properties);

        // Then
        assertThat(constraintViolations)
            .hasSize(1)
            .element(0)
            .matches(
                constraintViolation ->
                    constraintViolation.getRootBeanClass() == ConnectionPoolPropertiesImpl.class)
            .matches(
                constraintViolation ->
                    Objects.equals(constraintViolation.getInvalidValue(), invalidConnectionTimeout))
            .matches(
                constraintViolation ->
                    constraintViolation.getPropertyPath().toString().equals("connectionTimeout"));
      }

      private @NotNull Stream<Arguments> withInvalidValues_shouldGenerateConstraintViolations() {
        return Stream.of(
            arguments(named("Null value", 0)), arguments(named("Too high value", 600001)));
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
        ConnectionPoolPropertiesImpl properties =
            new ConnectionPoolPropertiesImpl(60000, validPoolSize);

        // When
        Set<ConstraintViolation<ConnectionPoolPropertiesImpl>> constraintViolations =
            ValidatorTestWrapper.validate(properties);

        // Then
        assertThat(constraintViolations).isEmpty();
      }

      private @NotNull Stream<Arguments> withValidValues_shouldNotGenerateConstraintViolations() {
        return Stream.of(
            arguments(named("Highest allowed value", 100)),
            arguments(named("Lowest allowed value", 1)));
      }

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With invalid values")
      void withInvalidValues_shouldGenerateConstraintViolations(int invalidPoolSize) {
        // Given
        ConnectionPoolPropertiesImpl properties =
            new ConnectionPoolPropertiesImpl(60000, invalidPoolSize);

        // When
        Set<ConstraintViolation<ConnectionPoolPropertiesImpl>> constraintViolations =
            ValidatorTestWrapper.validate(properties);

        // Then
        assertThat(constraintViolations)
            .hasSize(1)
            .element(0)
            .matches(
                constraintViolation ->
                    constraintViolation.getRootBeanClass() == ConnectionPoolPropertiesImpl.class)
            .matches(
                constraintViolation ->
                    Objects.equals(constraintViolation.getInvalidValue(), invalidPoolSize))
            .matches(
                constraintViolation ->
                    constraintViolation.getPropertyPath().toString().equals("poolSize"));
      }

      private @NotNull Stream<Arguments> withInvalidValues_shouldGenerateConstraintViolations() {
        return Stream.of(
            arguments(named("Null value", 0)), arguments(named("Too high value", 101)));
      }
    }
  }

  @Nested
  @DisplayName("When serializing to YAML")
  @TestInstance(Lifecycle.PER_CLASS)
  class WhenSerializingToYaml {

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With valid values")
    void withValidValues_shouldMatchExpectedYamlContent(
        @NotNull ConnectionPoolPropertiesImpl givenValue, @NotNull String expectedYamlFileName)
        throws IOException {
      // Given
      Path imDestFile = imfs.getPath("test.conf");

      // When
      ConfigSerializerTestWrapper.serialize(imDestFile, givenValue);

      // Then
      String actualYaml = new String(Files.readAllBytes(imDestFile));
      String expectedYaml =
          TestResourcesHelper.getClassResourceAsString(
              this.getClass(), expectedYamlFileName, false);
      assertThat(actualYaml).containsIgnoringNewLines(expectedYaml);
    }

    private @NotNull Stream<Arguments> withValidValues_shouldMatchExpectedYamlContent() {
      return Stream.of(
          arguments(
              named("With default values", new ConnectionPoolPropertiesImpl()),
              "whenSerializing_withDefaultValues.conf"),
          arguments(
              named("With custom values", new ConnectionPoolPropertiesImpl(30000, 5)),
              "whenSerializing_withCustomValues.conf"));
    }
  }

  @Nested
  @DisplayName("When deserializing from YAML")
  @TestInstance(Lifecycle.PER_CLASS)
  class WhenDeserializingFromYaml {

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With valid content")
    void withValidContent_shouldMatchExpectedValue(
        @NotNull String confFileName, @NotNull ConnectionPoolPropertiesImpl expectedValue) {
      // Given
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Optional<ConnectionPoolPropertiesImpl> actualValue =
          ConfigSerializerTestWrapper.deserialize(confFile, ConnectionPoolPropertiesImpl.class);

      // Then
      assertThat(actualValue).hasValue(expectedValue);
    }

    private @NotNull Stream<Arguments> withValidContent_shouldMatchExpectedValue() {
      return Stream.of(
          arguments(
              named("With valid values", "whenDeserializing_withValidValues.conf"),
              new ConnectionPoolPropertiesImpl(60000, 10)),
          arguments(
              named("With unexpected field", "whenDeserializing_withUnexpectedField.conf"),
              new ConnectionPoolPropertiesImpl(60000, 10)),
          arguments(
              named("With 'isValidated' field", "whenDeserializing_withIsValidatedField.conf"),
              new ConnectionPoolPropertiesImpl(60000, 10)));
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With invalid content")
    void withInvalidContent_shouldThrowException(@NotNull String confFileName) {
      // Given
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Exception exception =
          catchException(
              () ->
                  ConfigSerializerTestWrapper.deserialize(
                      confFile, ConnectionPoolPropertiesImpl.class));

      // Then
      assertThat(exception)
          .isInstanceOf(ConfigSerializationException.class)
          .hasCauseExactlyInstanceOf(SerializationException.class);
    }

    private @NotNull Stream<Arguments> withInvalidContent_shouldThrowException() {
      return Stream.of(
          arguments(
              named(
                  "With missing 'connectionTimeout' field",
                  "whenDeserializing_withMissingConnectionTimeoutField.conf")),
          arguments(
              named(
                  "With missing 'poolSize' field",
                  "whenDeserializing_withMissingPoolSizeField.conf")));
    }

    @Test
    @DisplayName("With empty content")
    void withEmptyContent_shouldGenerateNullValue() {
      // Given
      String confFileName = "whenDeserializing_withEmptyContent.conf";
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Optional<ConnectionPoolPropertiesImpl> actualValue =
          ConfigSerializerTestWrapper.deserialize(confFile, ConnectionPoolPropertiesImpl.class);

      // Then
      assertThat(actualValue).isNotPresent();
    }
  }
}
