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
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.spongepowered.configurate.serialize.SerializationException;

class DbmsCredentialsPropertiesImplTest {

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
  void whenCallingEqualsAndHashcode_shouldMetContracts() {
    EqualsVerifier.forClass(DbmsCredentialsPropertiesImpl.class)
        .withIgnoredFields("password")
        .verify();
  }

  @Test
  void whenCallingToString_shouldMetContracts() {
    ToStringVerifier.forClass(DbmsCredentialsPropertiesImpl.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .withIgnoredFields("password")
        .withFailOnExcludedFields(true)
        .verify();
  }

  @Nested
  class WhenInstantiating {

    @Test
    void withNoArgsConstructor_shouldMatchDefaultValues() {
      // Given

      // When
      DbmsCredentialsPropertiesImpl properties = new DbmsCredentialsPropertiesImpl();

      // Then
      assertAll(
          () -> assertThat(properties.getUsername()).isEqualTo("username"),
          () -> assertThat(properties.getPassword()).isEqualTo("password"));
    }

    @Test
    void withAllArgsConstructor_shouldMatchGivenArguments() {
      // Given
      String username = "foo";
      String password = "bar";

      // When
      DbmsCredentialsPropertiesImpl properties =
          new DbmsCredentialsPropertiesImpl(username, password);

      // Then
      assertAll(
          () -> assertThat(properties.getUsername()).isEqualTo("foo"),
          () -> assertThat(properties.getPassword()).isEqualTo("bar"));
    }
  }

  @Nested
  class WhenValidating {

    @Test
    void withDefaultValues_shouldNotGenerateConstraintViolations() {
      // Given
      DbmsCredentialsPropertiesImpl properties = new DbmsCredentialsPropertiesImpl();

      // When
      Set<ConstraintViolation<DbmsCredentialsPropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(properties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    void withOnlyValidValues_shouldNotGenerateConstraintViolations() {
      // Given
      DbmsCredentialsPropertiesImpl properties = new DbmsCredentialsPropertiesImpl("foo", "bar");

      // When
      Set<ConstraintViolation<DbmsCredentialsPropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(properties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    void withOnlyInvalidValues_shouldGenerateConstraintViolations() {
      // Given
      DbmsCredentialsPropertiesImpl properties = new DbmsCredentialsPropertiesImpl("", null);

      // When
      Set<ConstraintViolation<DbmsCredentialsPropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(properties);

      // Then
      assertThat(constraintViolations).hasSize(2);
    }

    @Nested
    class UsernameField {

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      void withValidValues_shouldNotGenerateConstraintViolations(@NotNull String validUsername) {
        // Given
        DbmsCredentialsPropertiesImpl properties =
            new DbmsCredentialsPropertiesImpl(validUsername, "bar");

        // When
        Set<ConstraintViolation<DbmsCredentialsPropertiesImpl>> constraintViolations =
            ValidatorTestWrapper.validate(properties);

        // Then
        assertThat(constraintViolations).isEmpty();
      }

      private static @NotNull Stream<Arguments>
          withValidValues_shouldNotGenerateConstraintViolations() {
        return Stream.of(
            arguments(named("Longest allowed value", StringUtils.repeat("s", 32))),
            arguments(named("Shortest allowed value", "s")));
      }

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      void withInvalidValues_shouldGenerateConstraintViolations(@Nullable String invalidUsername) {
        // Given
        DbmsCredentialsPropertiesImpl properties =
            new DbmsCredentialsPropertiesImpl(invalidUsername, "bar");

        // When
        Set<ConstraintViolation<DbmsCredentialsPropertiesImpl>> constraintViolations =
            ValidatorTestWrapper.validate(properties);

        // Then
        assertThat(constraintViolations)
            .hasSize(1)
            .element(0)
            .matches(
                constraintViolation ->
                    constraintViolation.getRootBeanClass() == DbmsCredentialsPropertiesImpl.class)
            .matches(
                constraintViolation ->
                    Objects.equals(constraintViolation.getInvalidValue(), invalidUsername))
            .matches(
                constraintViolation ->
                    constraintViolation.getPropertyPath().toString().equals("username"));
      }

      private static @NotNull Stream<Arguments>
          withInvalidValues_shouldGenerateConstraintViolations() {
        return Stream.of(
            arguments(named("Null value", null)),
            arguments(named("Empty value", "")),
            arguments(named("Blank value", " ")),
            arguments(named("Too long value", StringUtils.repeat("s", 33))));
      }
    }

    @Nested
    class PasswordField {

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      void withValidValues_shouldNotGenerateConstraintViolations(@NotNull String validPassword) {
        // Given
        DbmsCredentialsPropertiesImpl properties =
            new DbmsCredentialsPropertiesImpl("for", validPassword);

        // When
        Set<ConstraintViolation<DbmsCredentialsPropertiesImpl>> constraintViolations =
            ValidatorTestWrapper.validate(properties);

        // Then
        assertThat(constraintViolations).isEmpty();
      }

      private static @NotNull Stream<Arguments>
          withValidValues_shouldNotGenerateConstraintViolations() {
        return Stream.of(
            arguments(named("Longest allowed value", StringUtils.repeat("s", 128))),
            arguments(named("Shortest allowed value", "")),
            arguments(named("Blank value", " ")));
      }

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      void withInvalidValues_shouldGenerateConstraintViolations(@Nullable String invalidPassword) {
        // Given
        DbmsCredentialsPropertiesImpl properties =
            new DbmsCredentialsPropertiesImpl("foo", invalidPassword);

        // When
        Set<ConstraintViolation<DbmsCredentialsPropertiesImpl>> constraintViolations =
            ValidatorTestWrapper.validate(properties);

        // Then
        assertThat(constraintViolations)
            .hasSize(1)
            .element(0)
            .matches(
                constraintViolation ->
                    constraintViolation.getRootBeanClass() == DbmsCredentialsPropertiesImpl.class)
            .matches(
                constraintViolation ->
                    Objects.equals(constraintViolation.getInvalidValue(), invalidPassword))
            .matches(
                constraintViolation ->
                    constraintViolation.getPropertyPath().toString().equals("password"));
      }

      private static @NotNull Stream<Arguments>
          withInvalidValues_shouldGenerateConstraintViolations() {
        return Stream.of(
            arguments(named("Null value", null)),
            arguments(named("Too long value", StringUtils.repeat("s", 129))));
      }
    }
  }

  @Nested
  class WhenSerializingToYaml {

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    void withValidValues_shouldMatchExpectedYamlContent(
        @NotNull DbmsCredentialsPropertiesImpl givenValue, @NotNull String expectedYamlFileName)
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

    private static @NotNull Stream<Arguments> withValidValues_shouldMatchExpectedYamlContent() {
      return Stream.of(
          arguments(
              named("With default values", new DbmsCredentialsPropertiesImpl()),
              "whenSerializing_withDefaultValues.conf"),
          arguments(
              named("With custom values", new DbmsCredentialsPropertiesImpl("foo", "bar")),
              "whenSerializing_withCustomValues.conf"));
    }
  }

  @Nested
  class WhenDeserializingFromYaml {

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    void withValidContent_shouldMatchExpectedValue(
        @NotNull String confFileName, @NotNull DbmsCredentialsPropertiesImpl expectedValue) {
      // Given
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Optional<DbmsCredentialsPropertiesImpl> optionalCredentialsValidatingProperties =
          ConfigSerializerTestWrapper.deserialize(confFile, DbmsCredentialsPropertiesImpl.class);

      // Then
      assertThat(optionalCredentialsValidatingProperties).hasValue(expectedValue);
    }

    private static @NotNull Stream<Arguments> withValidContent_shouldMatchExpectedValue() {
      return Stream.of(
          arguments(
              named("With valid values", "whenDeserializing_withValidValues.conf"),
              new DbmsCredentialsPropertiesImpl("foo", "bar")),
          arguments(
              named("With unexpected field", "whenDeserializing_withUnexpectedField.conf"),
              new DbmsCredentialsPropertiesImpl("foo", "bar")),
          arguments(
              named("With 'isValidated' field", "whenDeserializing_withIsValidatedField.conf"),
              new DbmsCredentialsPropertiesImpl("foo", "bar")));
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    void withInvalidContent_shouldThrowException(@NotNull String confFileName) {
      // Given
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Exception exception =
          catchException(
              () ->
                  ConfigSerializerTestWrapper.deserialize(
                      confFile, DbmsCredentialsPropertiesImpl.class));

      // Then
      assertThat(exception)
          .isInstanceOf(ConfigSerializationException.class)
          .hasCauseExactlyInstanceOf(SerializationException.class);
    }

    private static @NotNull Stream<Arguments> withInvalidContent_shouldThrowException() {
      return Stream.of(
          arguments(
              named(
                  "With missing 'username' field",
                  "whenDeserializing_withMissingUsernameField.conf")),
          arguments(
              named(
                  "With missing 'password' field",
                  "whenDeserializing_withMissingPasswordField.conf")));
    }

    @Test
    void withEmptyContent_shouldGenerateNullValue() {
      // Given
      String confFileName = "whenDeserializing_withEmptyContent.conf";
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Optional<DbmsCredentialsPropertiesImpl> credentialsValidatingProperties =
          ConfigSerializerTestWrapper.deserialize(confFile, DbmsCredentialsPropertiesImpl.class);

      // Then
      assertThat(credentialsValidatingProperties).isNotPresent();
    }
  }
}
