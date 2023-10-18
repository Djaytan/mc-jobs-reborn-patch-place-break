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
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.TestResourcesHelper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization.ConfigSerializationException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.ConfigSerializerTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.ValidatorTestWrapper;
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
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

class DbmsServerPropertiesImplTest {

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
    EqualsVerifier.forClass(DbmsServerPropertiesImpl.class).verify();
  }

  @Test
  @DisplayName("When calling toString()")
  void whenCallingToString_shouldMetContracts() {
    ToStringVerifier.forClass(DbmsServerPropertiesImpl.class)
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
      DbmsServerPropertiesImpl properties = new DbmsServerPropertiesImpl();

      // Then
      assertAll(
          () ->
              assertThat(properties.getHost())
                  .isEqualTo(new DbmsHostPropertiesImpl("localhost", 3306, true)),
          () ->
              assertThat(properties.getCredentials())
                  .isEqualTo(new DbmsCredentialsPropertiesImpl("username", "password")),
          () -> assertThat(properties.getDatabase()).isEqualTo("database"));
    }

    @Test
    @DisplayName("With all args constructor")
    void withAllArgsConstructor_shouldMatchGivenArguments() {
      // Given
      DbmsHostPropertiesImpl host = new DbmsHostPropertiesImpl("example.com", 1234, true);
      DbmsCredentialsPropertiesImpl credentials = new DbmsCredentialsPropertiesImpl("foo", "bar");
      String database = "patch_database";

      // When
      DbmsServerPropertiesImpl properties =
          new DbmsServerPropertiesImpl(host, credentials, database);

      // Then
      assertAll(
          () -> assertThat(properties.getHost()).isEqualTo(host),
          () -> assertThat(properties.getCredentials()).isEqualTo(credentials),
          () -> assertThat(properties.getDatabase()).isEqualTo(database));
    }
  }

  @Nested
  @DisplayName("When validating")
  class WhenValidating {

    @Test
    @DisplayName("With default values")
    void withDefaultValues_shouldNotGenerateConstraintViolations() {
      // Given
      DbmsServerPropertiesImpl properties = new DbmsServerPropertiesImpl();

      // When
      Set<ConstraintViolation<DbmsServerPropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(properties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    @DisplayName("With only valid values")
    void withOnlyValidValues_shouldNotGenerateConstraintViolations() {
      // Given
      DbmsServerPropertiesImpl properties =
          new DbmsServerPropertiesImpl(
              new DbmsHostPropertiesImpl("example.com", 1234, true),
              new DbmsCredentialsPropertiesImpl("foo", "bar"),
              "patch_database");

      // When
      Set<ConstraintViolation<DbmsServerPropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(properties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    @DisplayName("With only shallow invalid values")
    void withOnlyShallowInvalidValues_shouldGenerateConstraintViolations() {
      // Given
      DbmsServerPropertiesImpl properties = new DbmsServerPropertiesImpl(null, null, " ");

      // When
      Set<ConstraintViolation<DbmsServerPropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(properties);

      // Then
      assertThat(constraintViolations).hasSize(3);
    }

    @Test
    @DisplayName("With only deep invalid values")
    void withOnlyDeepInvalidValues_shouldGenerateConstraintViolations() {
      // Given
      DbmsHostPropertiesImpl invalidHost = new DbmsHostPropertiesImpl(" ", 0, false);
      DbmsCredentialsPropertiesImpl invalidCredentials =
          new DbmsCredentialsPropertiesImpl("", null);
      DbmsServerPropertiesImpl properties =
          new DbmsServerPropertiesImpl(invalidHost, invalidCredentials, " ");

      // When
      Set<ConstraintViolation<DbmsServerPropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(properties);

      // Then
      assertThat(constraintViolations).hasSize(5);
    }

    @Nested
    @DisplayName("'database' field")
    @TestInstance(Lifecycle.PER_CLASS)
    class DatabaseField {

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With valid values")
      void withValidValues_shouldNotGenerateConstraintViolations(@NotNull String validDatabase) {
        // Given
        DbmsServerPropertiesImpl properties =
            new DbmsServerPropertiesImpl(
                new DbmsHostPropertiesImpl("example.com", 1234, true),
                new DbmsCredentialsPropertiesImpl("foo", "bar"),
                validDatabase);

        // When
        Set<ConstraintViolation<DbmsServerPropertiesImpl>> constraintViolations =
            ValidatorTestWrapper.validate(properties);

        // Then
        assertThat(constraintViolations).isEmpty();
      }

      private @NotNull Stream<Arguments> withValidValues_shouldNotGenerateConstraintViolations() {
        return Stream.of(
            arguments(named("Longest allowed value", StringUtils.repeat("s", 128))),
            arguments(named("Shortest allowed value", "s")));
      }

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With invalid values")
      void withInvalidValues_shouldGenerateConstraintViolations(@Nullable String invalidDatabase) {
        // Given
        DbmsServerPropertiesImpl properties =
            new DbmsServerPropertiesImpl(
                new DbmsHostPropertiesImpl("example.com", 1234, true),
                new DbmsCredentialsPropertiesImpl("foo", "bar"),
                invalidDatabase);

        // When
        Set<ConstraintViolation<DbmsServerPropertiesImpl>> constraintViolations =
            ValidatorTestWrapper.validate(properties);

        // Then
        assertThat(constraintViolations)
            .hasSize(1)
            .element(0)
            .matches(
                constraintViolation ->
                    constraintViolation.getRootBeanClass() == DbmsServerPropertiesImpl.class)
            .matches(
                constraintViolation ->
                    Objects.equals(constraintViolation.getInvalidValue(), invalidDatabase))
            .matches(
                constraintViolation ->
                    constraintViolation.getPropertyPath().toString().equals("database"));
      }

      private @NotNull Stream<Arguments> withInvalidValues_shouldGenerateConstraintViolations() {
        return Stream.of(
            arguments(named("Null value", null)),
            arguments(named("Too long value", StringUtils.repeat("s", 129))),
            arguments(named("Empty and too short value", "")),
            arguments(named("Blank value", " ")));
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
        @NotNull DbmsServerPropertiesImpl givenValue, @NotNull String expectedYamlFileName)
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
              named("With default values", new DbmsServerPropertiesImpl()),
              "whenSerializing_withDefaultValues.conf"),
          arguments(
              named(
                  "With custom values",
                  new DbmsServerPropertiesImpl(
                      new DbmsHostPropertiesImpl("example.com", 1234, true),
                      new DbmsCredentialsPropertiesImpl("foo", "bar"),
                      "patch_database")),
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
        @NotNull String confFileName, @NotNull DbmsServerPropertiesImpl expectedValue) {
      // Given
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Optional<DbmsServerPropertiesImpl> optionalDbmsServerValidatingProperties =
          ConfigSerializerTestWrapper.deserialize(confFile, DbmsServerPropertiesImpl.class);

      // Then
      assertThat(optionalDbmsServerValidatingProperties).hasValue(expectedValue);
    }

    private @NotNull Stream<Arguments> withValidContent_shouldMatchExpectedValue() {
      return Stream.of(
          arguments(
              named("With valid values", "whenDeserializing_withValidValues.conf"),
              new DbmsServerPropertiesImpl(
                  new DbmsHostPropertiesImpl("example.com", 1234, true),
                  new DbmsCredentialsPropertiesImpl("foo", "bar"),
                  "patch_database")),
          arguments(
              named("With unexpected field", "whenDeserializing_withUnexpectedField.conf"),
              new DbmsServerPropertiesImpl(
                  new DbmsHostPropertiesImpl("example.com", 1234, true),
                  new DbmsCredentialsPropertiesImpl("foo", "bar"),
                  "patch_database")),
          arguments(
              named("With 'isValidated' field", "whenDeserializing_withIsValidatedField.conf"),
              new DbmsServerPropertiesImpl(
                  new DbmsHostPropertiesImpl("example.com", 1234, true),
                  new DbmsCredentialsPropertiesImpl("foo", "bar"),
                  "patch_database")));
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
                      confFile, DbmsServerPropertiesImpl.class));

      // Then
      assertThat(exception)
          .isInstanceOf(ConfigSerializationException.class)
          .hasCauseExactlyInstanceOf(SerializationException.class);
    }

    private @NotNull Stream<Arguments> withInvalidContent_shouldThrowException() {
      return Stream.of(
          arguments(
              named("With missing 'host' field", "whenDeserializing_withMissingHostField.conf")),
          arguments(
              named(
                  "With missing 'credentials' field",
                  "whenDeserializing_withMissingCredentialsField.conf")),
          arguments(
              named(
                  "With missing 'database' field",
                  "whenDeserializing_withMissingDatabaseField.conf")));
    }

    @Test
    @DisplayName("With empty content")
    void withEmptyContent_shouldGenerateNullValue() {
      // Given
      String confFileName = "whenDeserializing_withEmptyContent.conf";
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Optional<DbmsServerPropertiesImpl> dbmsServerValidatingProperties =
          ConfigSerializerTestWrapper.deserialize(confFile, DbmsServerPropertiesImpl.class);

      // Then
      assertThat(dbmsServerValidatingProperties).isNotPresent();
    }
  }
}
