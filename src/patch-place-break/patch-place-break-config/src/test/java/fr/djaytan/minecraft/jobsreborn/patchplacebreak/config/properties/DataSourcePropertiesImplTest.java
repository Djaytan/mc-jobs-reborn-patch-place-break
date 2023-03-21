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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceType;
import jakarta.validation.ConstraintViolation;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
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

class DataSourcePropertiesImplTest {

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
    EqualsVerifier.forClass(DataSourcePropertiesImpl.class).verify();
  }

  @Test
  @DisplayName("When calling toString()")
  void whenCallingToString_shouldMetContracts() {
    ToStringVerifier.forClass(DataSourcePropertiesImpl.class)
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
      DataSourcePropertiesImpl dataSourcePropertiesImpl = new DataSourcePropertiesImpl();

      // Then
      assertAll(
          () -> assertThat(dataSourcePropertiesImpl.getType()).isEqualTo(DataSourceType.SQLITE),
          () -> assertThat(dataSourcePropertiesImpl.getTable()).isEqualTo("patch_place_break_tag"),
          () ->
              assertThat(dataSourcePropertiesImpl.getDbmsServer())
                  .isEqualTo(
                      new DbmsServerPropertiesImpl(
                          new DbmsHostPropertiesImpl("localhost", 3306, true),
                          new DbmsCredentialsPropertiesImpl("username", "password"),
                          "database")),
          () ->
              assertThat(dataSourcePropertiesImpl.getConnectionPool())
                  .isEqualTo(new ConnectionPoolPropertiesImpl(30000, 10)));
    }

    @Test
    @DisplayName("With all args constructor")
    void withAllArgsConstructor_shouldMatchGivenArguments() {
      // Given
      DataSourceType dataSourceType = DataSourceType.MYSQL;
      String table = "patch_place_break";
      DbmsServerPropertiesImpl dbmsServerPropertiesImpl =
          new DbmsServerPropertiesImpl(
              new DbmsHostPropertiesImpl("example.com", 1234, true),
              new DbmsCredentialsPropertiesImpl("foo", "bar"),
              "patch_database");
      ConnectionPoolPropertiesImpl connectionPoolPropertiesImpl =
          new ConnectionPoolPropertiesImpl(60000, 10);

      // When
      DataSourcePropertiesImpl dataSourcePropertiesImpl =
          new DataSourcePropertiesImpl(
              dataSourceType, table, dbmsServerPropertiesImpl, connectionPoolPropertiesImpl);

      // Then
      assertAll(
          () -> assertThat(dataSourcePropertiesImpl.getType()).isEqualTo(dataSourceType),
          () -> assertThat(dataSourcePropertiesImpl.getTable()).isEqualTo(table),
          () ->
              assertThat(dataSourcePropertiesImpl.getDbmsServer())
                  .isEqualTo(dbmsServerPropertiesImpl),
          () ->
              assertThat(dataSourcePropertiesImpl.getConnectionPool())
                  .isEqualTo(connectionPoolPropertiesImpl));
    }
  }

  @Nested
  @DisplayName("When validating")
  class WhenValidating {

    @Test
    @DisplayName("With default values")
    void withDefaultValues_shouldNotGenerateConstraintViolations() {
      // Given
      DataSourcePropertiesImpl dataSourcePropertiesImpl = new DataSourcePropertiesImpl();

      // When
      Set<ConstraintViolation<DataSourcePropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(dataSourcePropertiesImpl);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    @DisplayName("With only valid values")
    void withOnlyValidValues_shouldNotGenerateConstraintViolations() {
      // Given
      DataSourcePropertiesImpl dataSourcePropertiesImpl =
          new DataSourcePropertiesImpl(
              DataSourceType.MYSQL,
              "patch_place_break",
              new DbmsServerPropertiesImpl(
                  new DbmsHostPropertiesImpl("example.com", 1234, true),
                  new DbmsCredentialsPropertiesImpl("foo", "bar"),
                  "patch_database"),
              new ConnectionPoolPropertiesImpl(60000, 10));

      // When
      Set<ConstraintViolation<DataSourcePropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(dataSourcePropertiesImpl);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    @DisplayName("With only shallow invalid values")
    void withOnlyShallowInvalidValues_shouldGenerateConstraintViolations() {
      // Given
      DataSourcePropertiesImpl dataSourcePropertiesImpl =
          new DataSourcePropertiesImpl(null, null, null, null);

      // When
      Set<ConstraintViolation<DataSourcePropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(dataSourcePropertiesImpl);

      // Then
      assertThat(constraintViolations).hasSize(4);
    }

    @Test
    @DisplayName("With only deep invalid values")
    void withOnlyDeepInvalidValues_shouldGenerateConstraintViolations() {
      // Given
      DataSourcePropertiesImpl dataSourcePropertiesImpl =
          new DataSourcePropertiesImpl(
              null,
              null,
              new DbmsServerPropertiesImpl(
                  new DbmsHostPropertiesImpl(null, -1, false),
                  new DbmsCredentialsPropertiesImpl("", null),
                  null),
              new ConnectionPoolPropertiesImpl(-1, -1));

      // When
      Set<ConstraintViolation<DataSourcePropertiesImpl>> constraintViolations =
          ValidatorTestWrapper.validate(dataSourcePropertiesImpl);

      // Then
      assertThat(constraintViolations).hasSize(9);
    }

    @Nested
    @DisplayName("'table' field")
    @TestInstance(Lifecycle.PER_CLASS)
    class TableField {

      @ParameterizedTest(name = "{index} - {0}")
      @MethodSource
      @DisplayName("With valid values")
      void withValidValues_shouldNotGenerateConstraintViolations(@NotNull String validTable) {
        // Given
        DataSourcePropertiesImpl dataSourcePropertiesImpl =
            new DataSourcePropertiesImpl(
                DataSourceType.MYSQL,
                validTable,
                new DbmsServerPropertiesImpl(
                    new DbmsHostPropertiesImpl("example.com", 1234, true),
                    new DbmsCredentialsPropertiesImpl("foo", "bar"),
                    "patch_database"),
                new ConnectionPoolPropertiesImpl(60000, 10));

        // When
        Set<ConstraintViolation<DataSourcePropertiesImpl>> constraintViolations =
            ValidatorTestWrapper.validate(dataSourcePropertiesImpl);

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
      void withInvalidValues_shouldGenerateConstraintViolations(@Nullable String invalidTable) {
        // Given
        DataSourcePropertiesImpl dataSourcePropertiesImpl =
            new DataSourcePropertiesImpl(
                DataSourceType.MYSQL,
                invalidTable,
                new DbmsServerPropertiesImpl(
                    new DbmsHostPropertiesImpl("example.com", 1234, true),
                    new DbmsCredentialsPropertiesImpl("foo", "bar"),
                    "patch_database"),
                new ConnectionPoolPropertiesImpl(60000, 10));

        // When
        Set<ConstraintViolation<DataSourcePropertiesImpl>> constraintViolations =
            ValidatorTestWrapper.validate(dataSourcePropertiesImpl);

        // Then
        assertThat(constraintViolations)
            .hasSize(1)
            .element(0)
            .matches(
                constraintViolation ->
                    constraintViolation.getRootBeanClass() == DataSourcePropertiesImpl.class)
            .matches(
                constraintViolation ->
                    Objects.equals(constraintViolation.getInvalidValue(), invalidTable))
            .matches(
                constraintViolation ->
                    constraintViolation.getPropertyPath().toString().equals("table"));
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
    @SneakyThrows
    void withValidValues_shouldMatchExpectedYamlContent(
        @NotNull DataSourcePropertiesImpl givenValue, @NotNull String expectedYamlFileName) {
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
              named("With default values", new DataSourcePropertiesImpl()),
              "whenSerializing_withDefaultValues.conf"),
          arguments(
              named(
                  "With custom values",
                  new DataSourcePropertiesImpl(
                      DataSourceType.MYSQL,
                      "patch_place_break",
                      new DbmsServerPropertiesImpl(
                          new DbmsHostPropertiesImpl("example.com", 1234, true),
                          new DbmsCredentialsPropertiesImpl("foo", "bar"),
                          "patch_database"),
                      new ConnectionPoolPropertiesImpl(60000, 10))),
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
        @NotNull String confFileName, @NotNull DataSourcePropertiesImpl expectedValue) {
      // Given
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Optional<DataSourcePropertiesImpl> optionalDataSourceValidatingProperties =
          ConfigSerializerTestWrapper.deserialize(confFile, DataSourcePropertiesImpl.class);

      // Then
      assertThat(optionalDataSourceValidatingProperties).isPresent().get().isEqualTo(expectedValue);
    }

    private @NotNull Stream<Arguments> withValidContent_shouldMatchExpectedValue() {
      return Stream.of(
          arguments(
              named("With valid values", "whenDeserializing_withValidValues.conf"),
              new DataSourcePropertiesImpl(
                  DataSourceType.MYSQL,
                  "patch_place_break",
                  new DbmsServerPropertiesImpl(
                      new DbmsHostPropertiesImpl("example.com", 1234, true),
                      new DbmsCredentialsPropertiesImpl("foo", "bar"),
                      "patch_database"),
                  new ConnectionPoolPropertiesImpl(60000, 10))),
          arguments(
              named("With unexpected field", "whenDeserializing_withUnexpectedField.conf"),
              new DataSourcePropertiesImpl(
                  DataSourceType.MYSQL,
                  "patch_place_break",
                  new DbmsServerPropertiesImpl(
                      new DbmsHostPropertiesImpl("example.com", 1234, true),
                      new DbmsCredentialsPropertiesImpl("foo", "bar"),
                      "patch_database"),
                  new ConnectionPoolPropertiesImpl(60000, 10))),
          arguments(
              named("With 'isValidated' field", "whenDeserializing_withIsValidatedField.conf"),
              new DataSourcePropertiesImpl(
                  DataSourceType.MYSQL,
                  "patch_place_break",
                  new DbmsServerPropertiesImpl(
                      new DbmsHostPropertiesImpl("example.com", 1234, true),
                      new DbmsCredentialsPropertiesImpl("foo", "bar"),
                      "patch_database"),
                  new ConnectionPoolPropertiesImpl(60000, 10))));
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With invalid content")
    void withInvalidContent_shouldThrowException(@NotNull String confFileName) {
      // Given
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      ThrowingCallable throwingCallable =
          () -> ConfigSerializerTestWrapper.deserialize(confFile, DataSourcePropertiesImpl.class);

      // Then
      assertThatThrownBy(throwingCallable)
          .isInstanceOf(ConfigSerializationException.class)
          .hasCauseExactlyInstanceOf(SerializationException.class);
    }

    private @NotNull Stream<Arguments> withInvalidContent_shouldThrowException() {
      return Stream.of(
          arguments(
              named("With missing 'type' field", "whenDeserializing_withMissingTypeField.conf")),
          arguments(
              named("With missing 'table' field", "whenDeserializing_withMissingTableField.conf")),
          arguments(
              named(
                  "With missing 'dbmsServer' field",
                  "whenDeserializing_withMissingDbmsServerField.conf")),
          arguments(
              named(
                  "With missing 'connectionPool' field",
                  "whenDeserializing_withMissingConnectionPoolField.conf")));
    }

    @Test
    @DisplayName("With empty content")
    void withEmptyContent_shouldGenerateNullValue() {
      // Given
      String confFileName = "whenDeserializing_withEmptyContent.conf";
      Path confFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), confFileName);

      // When
      Optional<DataSourcePropertiesImpl> dataSourceValidatingProperties =
          ConfigSerializerTestWrapper.deserialize(confFile, DataSourcePropertiesImpl.class);

      // Then
      assertThat(dataSourceValidatingProperties).isNotPresent();
    }
  }
}
