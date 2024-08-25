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
import fr.djaytan.mc.jrppb.core.config.testutils.ConfigSerializerTestWrapper;
import fr.djaytan.mc.jrppb.core.config.testutils.ValidatorTestWrapper;
import jakarta.validation.ConstraintViolation;
import java.io.IOException;
import java.io.UncheckedIOException;
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
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.spongepowered.configurate.serialize.SerializationException;

class DbmsHostPropertiesImplTest {

  @AutoClose private final FileSystem imfs = Jimfs.newFileSystem(Configuration.unix());

  @Test
  void whenCallingEqualsAndHashcode_shouldMetContracts() {
    EqualsVerifier.forClass(DbmsHostPropertiesImpl.class).verify();
  }

  @Test
  void whenCallingToString_shouldMetContracts() {
    ToStringVerifier.forClass(DbmsHostPropertiesImpl.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }

  @Nested
  class WhenInstantiating {

    @Test
    void withNoArgsConstructor_shouldMatchDefaultValues() {
      // Given

      // When
      DbmsHostPropertiesImpl properties = new DbmsHostPropertiesImpl();

      // Then
      assertAll(
          () -> assertThat(properties.getHostname()).isEqualTo("localhost"),
          () -> assertThat(properties.getPort()).isEqualTo(3306),
          () -> assertThat(properties.isSslEnabled()).isTrue());
    }

    @Test
    void withAllArgsConstructor_shouldMatchGivenArguments() {
      // Given
      String hostname = "example.com";
      int port = 1234;
      boolean isSslEnabled = true;

      // When
      DbmsHostPropertiesImpl properties = new DbmsHostPropertiesImpl(hostname, port, isSslEnabled);

      // Then
      assertAll(
          () -> assertThat(properties.getHostname()).isEqualTo("example.com"),
          () -> assertThat(properties.getPort()).isEqualTo(1234),
          () -> assertThat(properties.isSslEnabled()).isTrue());
    }
  }

  @Nested
  class WhenValidating {

    @Test
    void withDefaultValues_shouldNotGenerateConstraintViolations() {
      // Given
      DbmsHostPropertiesImpl properties = new DbmsHostPropertiesImpl();

      // When
      Set<ConstraintViolation<DbmsHostPropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(properties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    void withOnlyValidValues_shouldNotGenerateConstraintViolations() {
      // Given
      DbmsHostPropertiesImpl properties = new DbmsHostPropertiesImpl("example.com", 1234, true);

      // When
      Set<ConstraintViolation<DbmsHostPropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(properties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    /*
     * Note: isSslEnabled is always valid.
     */
    @Test
    void withOnlyInvalidValues_shouldGenerateConstraintViolations() {
      // Given
      DbmsHostPropertiesImpl properties = new DbmsHostPropertiesImpl("", -1, false);

      // When
      Set<ConstraintViolation<DbmsHostPropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(properties);

      // Then
      assertThat(constraintViolations).hasSize(2);
    }

    @Nested
    class HostnameField {

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      void withValidValues_shouldNotGenerateConstraintViolations(@NotNull String validHostname) {
        // Given
        DbmsHostPropertiesImpl properties = new DbmsHostPropertiesImpl(validHostname, 1234, true);

        // When
        Set<ConstraintViolation<DbmsHostPropertiesImpl>> constraintViolations =
            ValidatorTestWrapper.validate(properties);

        // Then
        assertThat(constraintViolations).isEmpty();
      }

      /*
       * We accept most of the invalid values here.
       * More details given in the DbmsHostValidatingProperties class.
       */
      private static @NotNull Stream<Arguments>
          withValidValues_shouldNotGenerateConstraintViolations() {
        return Stream.of(
            arguments(named("Nominal IPv4 address", "92.0.2.146")),
            arguments(named("Nominal IPv6 address", "2001:db8:3333:4444:5555:6666:7777:8888")),
            arguments(named("Domain name address", "my.example.com")),
            arguments(named("Longest valid value", StringUtils.repeat("s", 255))),
            arguments(named("Shortest valid value", "s")),
            arguments(named("Invalid IPv4 address", "-1.-1.-1.-1")),
            arguments(named("Invalid IPv6 address", "ZZZZ:ZZZZ:ZZZZ:ZZZZ:ZZZZ:ZZZZ:ZZZZ:ZZZZ")));
      }

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      void withInvalidValues_shouldGenerateConstraintViolations(@Nullable String invalidHostname) {
        // Given
        DbmsHostPropertiesImpl properties = new DbmsHostPropertiesImpl(invalidHostname, 1234, true);

        // When
        Set<ConstraintViolation<DbmsHostPropertiesImpl>> constraintViolations =
            ValidatorTestWrapper.validate(properties);

        // Then
        assertThat(constraintViolations)
            .hasSize(1)
            .element(0)
            .matches(
                constraintViolation ->
                    constraintViolation.getRootBeanClass() == DbmsHostPropertiesImpl.class)
            .matches(
                constraintViolation ->
                    Objects.equals(constraintViolation.getInvalidValue(), invalidHostname))
            .matches(
                constraintViolation ->
                    constraintViolation.getPropertyPath().toString().equals("hostname"));
      }

      private static @NotNull Stream<Arguments>
          withInvalidValues_shouldGenerateConstraintViolations() {
        return Stream.of(
            arguments(named("Null value", null)),
            arguments(named("Empty value", "")),
            arguments(named("Blank value", " ")),
            arguments(named("Too long value", StringUtils.repeat("s", 256))));
      }
    }

    @Nested
    class PortField {

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      void withValidValues_shouldNotGenerateConstraintViolations(int validPort) {
        // Given
        DbmsHostPropertiesImpl properties =
            new DbmsHostPropertiesImpl("example.com", validPort, true);

        // When
        Set<ConstraintViolation<DbmsHostPropertiesImpl>> constraintViolations =
            ValidatorTestWrapper.validate(properties);

        // Then
        assertThat(constraintViolations).isEmpty();
      }

      private static @NotNull Stream<Arguments>
          withValidValues_shouldNotGenerateConstraintViolations() {
        return Stream.of(
            arguments(named("Highest possible value", 65535)),
            arguments(named("Lowest possible value", 1)));
      }

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      void withInvalidValues_shouldGenerateConstraintViolations(int invalidPort) {
        // Given
        DbmsHostPropertiesImpl properties =
            new DbmsHostPropertiesImpl("example.com", invalidPort, true);

        // When
        Set<ConstraintViolation<DbmsHostPropertiesImpl>> constraintViolations =
            ValidatorTestWrapper.validate(properties);

        // Then
        assertThat(constraintViolations)
            .hasSize(1)
            .element(0)
            .matches(
                constraintViolation ->
                    constraintViolation.getRootBeanClass() == DbmsHostPropertiesImpl.class)
            .matches(
                constraintViolation ->
                    Objects.equals(constraintViolation.getInvalidValue(), invalidPort))
            .matches(
                constraintViolation ->
                    constraintViolation.getPropertyPath().toString().equals("port"));
      }

      private static @NotNull Stream<Arguments>
          withInvalidValues_shouldGenerateConstraintViolations() {
        return Stream.of(
            arguments(named("Port n°0", 0)),
            arguments(named("Too high port", 65536)),
            arguments(named("Too low port", -1)));
      }
    }
  }

  @Nested
  class WhenSerializingToYaml {

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    void withValidValues_shouldMatchExpectedYamlContent(
        @NotNull DbmsHostPropertiesImpl givenValue, @NotNull String expectedYamlFileName)
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
              named("With default values", new DbmsHostPropertiesImpl()),
              "whenSerializing_withDefaultValues.conf"),
          arguments(
              named("With custom values", new DbmsHostPropertiesImpl("example.com", 1234, true)),
              "whenSerializing_withCustomValues.conf"));
    }
  }

  @Nested
  class WhenDeserializingFromYaml {

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    void withValidContent_shouldMatchExpectedValue(
        @NotNull String confFileName, @NotNull DbmsHostPropertiesImpl expectedValue) {
      // Given
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Optional<DbmsHostPropertiesImpl> optionalHostValidatingProperties =
          ConfigSerializerTestWrapper.deserialize(confFile, DbmsHostPropertiesImpl.class);

      // Then
      assertThat(optionalHostValidatingProperties).hasValue(expectedValue);
    }

    private static @NotNull Stream<Arguments> withValidContent_shouldMatchExpectedValue() {
      return Stream.of(
          arguments(
              named("With valid values", "whenDeserializing_withValidValues.conf"),
              new DbmsHostPropertiesImpl("example.com", 1234, true)),
          arguments(
              named("With unexpected field", "whenDeserializing_withUnexpectedField.conf"),
              new DbmsHostPropertiesImpl("example.com", 1234, true)),
          arguments(
              named("With 'isValidated' field", "whenDeserializing_withIsValidatedField.conf"),
              new DbmsHostPropertiesImpl("example.com", 1234, true)));
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
                  ConfigSerializerTestWrapper.deserialize(confFile, DbmsHostPropertiesImpl.class));

      // Then
      assertThat(exception)
          .isExactlyInstanceOf(UncheckedIOException.class)
          .hasMessageStartingWith("Fail to deserialize config properties")
          .hasMessageContainingAll(
              "fr.djaytan.mc.jrppb.core.config.properties.DbmsHostPropertiesImpl", confFileName)
          .hasRootCauseExactlyInstanceOf(SerializationException.class);
    }

    private static @NotNull Stream<Arguments> withInvalidContent_shouldThrowException() {
      return Stream.of(
          arguments(
              named(
                  "With missing 'hostname' field",
                  "whenDeserializing_withMissingHostnameField.conf")),
          arguments(
              named("With missing 'port' field", "whenDeserializing_withMissingPortField.conf")),
          arguments(
              named(
                  "With missing 'isSslEnabled' field",
                  "whenDeserializing_withMissingIsSslEnabledField.conf")));
    }

    @Test
    void withEmptyContent_shouldGenerateNullValue() {
      // Given
      String confFileName = "whenDeserializing_withEmptyContent.conf";
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Optional<DbmsHostPropertiesImpl> dbmsHostValidatingProperties =
          ConfigSerializerTestWrapper.deserialize(confFile, DbmsHostPropertiesImpl.class);

      // Then
      assertThat(dbmsHostValidatingProperties).isNotPresent();
    }
  }
}
