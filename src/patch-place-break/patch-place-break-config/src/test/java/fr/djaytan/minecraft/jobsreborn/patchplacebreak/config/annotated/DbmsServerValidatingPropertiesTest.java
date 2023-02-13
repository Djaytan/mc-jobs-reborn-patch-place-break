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

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.commons.test.TestResourcesHelper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.serialization.ConfigSerializationException;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.ConfigSerializerTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.ValidatorTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.CredentialsProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DbmsHostProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DbmsServerProperties;
import jakarta.validation.ConstraintViolation;
import lombok.NonNull;
import lombok.SneakyThrows;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class DbmsServerValidatingPropertiesTest {

  private FileSystem imfs;

  @BeforeEach
  void beforeEach() {
    imfs = Jimfs.newFileSystem(Configuration.unix());
  }

  @AfterEach
  @SneakyThrows
  void afterEach() {
    imfs.close();
  }

  @Test
  @DisplayName("When calling equals() & hashCode()")
  void whenCallingEqualsAndHashcode_shouldMetContracts() {
    EqualsVerifier.forClass(DbmsServerValidatingProperties.class).withRedefinedSuperclass()
        .suppress(Warning.NONFINAL_FIELDS).verify();
  }

  @Test
  @DisplayName("When calling toString()")
  void whenCallingToString_shouldMetContracts() {
    ToStringVerifier.forClass(DbmsServerValidatingProperties.class)
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
      DbmsServerValidatingProperties dbmsServerValidatingProperties =
          DbmsServerValidatingProperties.ofDefault();

      // Then
      assertAll(
          () -> assertThat(dbmsServerValidatingProperties.getHost())
              .isEqualTo(DbmsHostValidatingProperties.of("localhost", 3306, true)),
          () -> assertThat(dbmsServerValidatingProperties.getCredentials())
              .isEqualTo(CredentialsValidatingProperties.of("username", "password")),
          () -> assertThat(dbmsServerValidatingProperties.getDatabase()).isEqualTo("database"),
          () -> assertThat(dbmsServerValidatingProperties.isValidated()).isFalse());
    }

    @Test
    @DisplayName("With all args constructor")
    void withAllArgsConstructor_shouldMatchGivenArguments() {
      // Given
      DbmsHostValidatingProperties host =
          DbmsHostValidatingProperties.of("example.com", 1234, true);
      CredentialsValidatingProperties credentials =
          CredentialsValidatingProperties.of("foo", "bar");
      String database = "patch_database";

      // When
      DbmsServerValidatingProperties dbmsServerValidatingProperties =
          DbmsServerValidatingProperties.of(host, credentials, database);

      // Then
      assertAll(() -> assertThat(dbmsServerValidatingProperties.getHost()).isEqualTo(host),
          () -> assertThat(dbmsServerValidatingProperties.getCredentials()).isEqualTo(credentials),
          () -> assertThat(dbmsServerValidatingProperties.getDatabase()).isEqualTo(database),
          () -> assertThat(dbmsServerValidatingProperties.isValidated()).isFalse());
    }
  }

  @Nested
  @DisplayName("When converting")
  class WhenConverting {

    @Test
    @DisplayName("With properties marked as validated")
    void withPropertiesMarkedAsValidated_shouldConvertSuccessfully() {
      // Given
      DbmsServerValidatingProperties dbmsServerValidatingProperties = DbmsServerValidatingProperties
          .of(DbmsHostValidatingProperties.of("example.com", 1234, true),
              CredentialsValidatingProperties.of("foo", "bar"), "patch_database");
      dbmsServerValidatingProperties.markAsValidated();

      // When
      DbmsServerProperties dbmsServerProperties = dbmsServerValidatingProperties.convert();

      // Then
      assertAll(() -> assertThat(dbmsServerValidatingProperties.isValidated()).isTrue(),
          () -> assertThat(dbmsServerProperties.getHost())
              .isEqualTo(DbmsHostProperties.of("example.com", 1234, true)),
          () -> assertThat(dbmsServerProperties.getCredentials())
              .isEqualTo(CredentialsProperties.of("foo", "bar")),
          () -> assertThat(dbmsServerProperties.getDatabase()).isEqualTo("patch_database"));
    }

    @Test
    @DisplayName("With properties not marked as validated")
    void withPropertiesNotMarkedAsValidated_shouldThrowException() {
      // Given
      DbmsServerValidatingProperties dbmsServerValidatingProperties = DbmsServerValidatingProperties
          .of(DbmsHostValidatingProperties.of("example.com", 1234, true),
              CredentialsValidatingProperties.of("foo", "bar"), "patch_database");

      // When
      ThrowingCallable throwingCallable = dbmsServerValidatingProperties::convert;

      // Then
      assertAll(() -> assertThat(dbmsServerValidatingProperties.isValidated()).isFalse(),
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
      DbmsServerValidatingProperties dbmsServerValidatingProperties =
          DbmsServerValidatingProperties.ofDefault();

      // When
      Set<ConstraintViolation<DbmsServerValidatingProperties>> constraintViolations =
          ValidatorTestWrapper.validate(dbmsServerValidatingProperties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    @DisplayName("With only valid values")
    void withOnlyValidValues_shouldNotGenerateConstraintViolations() {
      // Given
      DbmsServerValidatingProperties dbmsServerValidatingProperties = DbmsServerValidatingProperties
          .of(DbmsHostValidatingProperties.of("example.com", 1234, true),
              CredentialsValidatingProperties.of("foo", "bar"), "patch_database");

      // When
      Set<ConstraintViolation<DbmsServerValidatingProperties>> constraintViolations =
          ValidatorTestWrapper.validate(dbmsServerValidatingProperties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    @DisplayName("With only shallow invalid values")
    void withOnlyShallowInvalidValues_shouldGenerateConstraintViolations() {
      // Given
      DbmsServerValidatingProperties dbmsServerValidatingProperties =
          DbmsServerValidatingProperties.of(null, null, " ");

      // When
      Set<ConstraintViolation<DbmsServerValidatingProperties>> constraintViolations =
          ValidatorTestWrapper.validate(dbmsServerValidatingProperties);

      // Then
      assertThat(constraintViolations).hasSize(3);
    }

    @Test
    @DisplayName("With only deep invalid values")
    void withOnlyDeepInvalidValues_shouldGenerateConstraintViolations() {
      // Given
      DbmsHostValidatingProperties invalidHost = DbmsHostValidatingProperties.of(" ", 0, false);
      CredentialsValidatingProperties invalidCredentials =
          CredentialsValidatingProperties.of("", null);
      DbmsServerValidatingProperties dbmsServerValidatingProperties =
          DbmsServerValidatingProperties.of(invalidHost, invalidCredentials, " ");

      // When
      Set<ConstraintViolation<DbmsServerValidatingProperties>> constraintViolations =
          ValidatorTestWrapper.validate(dbmsServerValidatingProperties);

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
      void withValidValues_shouldNotGenerateConstraintViolations(@NonNull String validDatabase) {
        // Given
        DbmsServerValidatingProperties dbmsServerValidatingProperties =
            DbmsServerValidatingProperties.of(
                DbmsHostValidatingProperties.of("example.com", 1234, true),
                CredentialsValidatingProperties.of("foo", "bar"), validDatabase);

        // When
        Set<ConstraintViolation<DbmsServerValidatingProperties>> constraintViolations =
            ValidatorTestWrapper.validate(dbmsServerValidatingProperties);

        // Then
        assertThat(constraintViolations).isEmpty();
      }

      private @NonNull Stream<Arguments> withValidValues_shouldNotGenerateConstraintViolations() {
        return Stream.of(Arguments.of(Named.of("Longest allowed value", Strings.repeat("s", 128))),
            Arguments.of(Named.of("Shortest allowed value", "s")));
      }

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With invalid values")
      void withInvalidValues_shouldGenerateConstraintViolations(String invalidDatabase) {
        // Given
        DbmsServerValidatingProperties dbmsServerValidatingProperties =
            DbmsServerValidatingProperties.of(
                DbmsHostValidatingProperties.of("example.com", 1234, true),
                CredentialsValidatingProperties.of("foo", "bar"), invalidDatabase);

        // When
        Set<ConstraintViolation<DbmsServerValidatingProperties>> constraintViolations =
            ValidatorTestWrapper.validate(dbmsServerValidatingProperties);

        // Then
        assertThat(constraintViolations).hasSize(1).element(0)
            .matches(constraintViolation -> constraintViolation
                .getRootBeanClass() == DbmsServerValidatingProperties.class)
            .matches(constraintViolation -> Objects.equals(constraintViolation.getInvalidValue(),
                invalidDatabase))
            .matches(constraintViolation -> constraintViolation.getPropertyPath().toString()
                .equals("database"));
      }

      private @NonNull Stream<Arguments> withInvalidValues_shouldGenerateConstraintViolations() {
        return Stream.of(Arguments.of(Named.of("Null value", null)),
            Arguments.of(Named.of("Too long value", Strings.repeat("s", 129))),
            Arguments.of(Named.of("Empty and too short value", "")),
            Arguments.of(Named.of("Blank value", " ")));
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
    @SneakyThrows
    void withValidValues_shouldMatchExpectedYamlContent(
        @NonNull DbmsServerValidatingProperties givenValue, @NonNull String expectedYamlFileName) {
      // Given
      Path imDestFile = imfs.getPath("test.conf");

      // When
      ConfigSerializerTestWrapper.serialize(imDestFile, givenValue);

      // Then
      String actualYaml = new String(Files.readAllBytes(imDestFile));
      String expectedYaml = TestResourcesHelper.getClassResourceAsString(this.getClass(),
          expectedYamlFileName, false);
      assertThat(actualYaml).isEqualToIgnoringNewLines(expectedYaml);
    }

    private @NonNull Stream<Arguments> withValidValues_shouldMatchExpectedYamlContent() {
      return Stream.of(
          Arguments.of(Named.of("With default values", DbmsServerValidatingProperties.ofDefault()),
              "whenSerializing_withDefaultValues.conf"),
          Arguments.of(
              Named.of("With custom values",
                  DbmsServerValidatingProperties.of(
                      DbmsHostValidatingProperties.of("example.com", 1234, true),
                      CredentialsValidatingProperties.of("foo", "bar"), "patch_database")),
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
    void withValidContent_shouldMatchExpectedValue(@NonNull String confFileName,
        @NonNull DbmsServerValidatingProperties expectedValue) {
      // Given
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Optional<DbmsServerValidatingProperties> optionalDbmsServerValidatingProperties =
          ConfigSerializerTestWrapper.deserialize(confFile, DbmsServerValidatingProperties.class);

      // Then
      assertThat(optionalDbmsServerValidatingProperties).isPresent().get().isEqualTo(expectedValue);
    }

    private @NonNull Stream<Arguments> withValidContent_shouldMatchExpectedValue() {
      return Stream.of(
          Arguments.of(Named.of("With valid values", "whenDeserializing_withValidValues.conf"),
              DbmsServerValidatingProperties.of(
                  DbmsHostValidatingProperties.of("example.com", 1234, true),
                  CredentialsValidatingProperties.of("foo", "bar"), "patch_database")),
          Arguments.of(
              Named.of("With unexpected field", "whenDeserializing_withUnexpectedField.conf"),
              DbmsServerValidatingProperties.of(
                  DbmsHostValidatingProperties.of("example.com", 1234, true),
                  CredentialsValidatingProperties.of("foo", "bar"), "patch_database")),
          Arguments.of(
              Named.of("With 'isValidated' field", "whenDeserializing_withIsValidatedField.conf"),
              DbmsServerValidatingProperties.of(
                  DbmsHostValidatingProperties.of("example.com", 1234, true),
                  CredentialsValidatingProperties.of("foo", "bar"), "patch_database")));
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With invalid content")
    void withInvalidContent_shouldThrowException(@NonNull String confFileName) {
      // Given
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      ThrowingCallable throwingCallable = () -> ConfigSerializerTestWrapper.deserialize(confFile,
          DbmsServerValidatingProperties.class);

      // Then
      assertThatThrownBy(throwingCallable).isInstanceOf(ConfigSerializationException.class)
          .hasCauseExactlyInstanceOf(SerializationException.class);
    }

    private @NonNull Stream<Arguments> withInvalidContent_shouldThrowException() {
      return Stream.of(
          Arguments.of(
              Named.of("With missing 'host' field", "whenDeserializing_withMissingHostField.conf")),
          Arguments.of(Named.of("With missing 'credentials' field",
              "whenDeserializing_withMissingCredentialsField.conf")),
          Arguments.of(Named.of("With missing 'database' field",
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
      Optional<DbmsServerValidatingProperties> dbmsServerValidatingProperties =
          ConfigSerializerTestWrapper.deserialize(confFile, DbmsServerValidatingProperties.class);

      // Then
      assertThat(dbmsServerValidatingProperties).isNotPresent();
    }
  }
}
