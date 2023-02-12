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
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.ConnectionPoolProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.CredentialsProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DbmsHostProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DbmsServerProperties;
import jakarta.validation.ConstraintViolation;
import lombok.NonNull;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class DataSourceValidatingPropertiesTest {

  @Test
  @DisplayName("When calling equals() & hashCode()")
  void whenCallingEqualsAndHashcode_shouldMetContracts() {
    EqualsVerifier.forClass(DataSourceValidatingProperties.class).withRedefinedSuperclass()
        .suppress(Warning.NONFINAL_FIELDS).verify();
  }

  @Test
  @DisplayName("When calling toString()")
  void whenCallingToString_shouldMetContracts() {
    ToStringVerifier.forClass(DataSourceValidatingProperties.class)
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
      DataSourceValidatingProperties dataSourceValidatingProperties =
          new DataSourceValidatingProperties();

      // Then
      assertAll(() -> assertThat(dataSourceValidatingProperties.getType()).isNull(),
          () -> assertThat(dataSourceValidatingProperties.getTable()).isNull(),
          () -> assertThat(dataSourceValidatingProperties.getDbmsServer()).isNull(),
          () -> assertThat(dataSourceValidatingProperties.getConnectionPool()).isNull(),
          () -> assertThat(dataSourceValidatingProperties.isValidated()).isFalse());
    }

    @Test
    @DisplayName("With all args constructor")
    void withAllArgsConstructor_shouldMatchGivenArguments() {
      // Given
      DataSourceType dataSourceType = DataSourceType.MYSQL;
      String table = "patch_place_break";
      DbmsServerValidatingProperties dbmsServerValidatingProperties = DbmsServerValidatingProperties
          .of(DbmsHostValidatingProperties.of("example.com", 1234, true),
              CredentialsValidatingProperties.of("foo", "bar"), "patch_database");
      ConnectionPoolValidatingProperties connectionPoolValidatingProperties =
          ConnectionPoolValidatingProperties.of(60000, 10);

      // When
      DataSourceValidatingProperties dataSourceValidatingProperties =
          DataSourceValidatingProperties.of(dataSourceType, table, dbmsServerValidatingProperties,
              connectionPoolValidatingProperties);

      // Then
      assertAll(
          () -> assertThat(dataSourceValidatingProperties.getType()).isEqualTo(dataSourceType),
          () -> assertThat(dataSourceValidatingProperties.getTable()).isEqualTo(table),
          () -> assertThat(dataSourceValidatingProperties.getDbmsServer())
              .isEqualTo(dbmsServerValidatingProperties),
          () -> assertThat(dataSourceValidatingProperties.getConnectionPool())
              .isEqualTo(connectionPoolValidatingProperties),
          () -> assertThat(dataSourceValidatingProperties.isValidated()).isFalse());
    }
  }

  @Nested
  @DisplayName("When converting")
  class WhenConverting {

    @Test
    @DisplayName("With properties marked as validated")
    void withPropertiesMarkedAsValidated_shouldConvertSuccessfully() {
      // Given
      DataSourceValidatingProperties dataSourceValidatingProperties =
          DataSourceValidatingProperties.of(DataSourceType.MYSQL, "patch_place_break",
              DbmsServerValidatingProperties.of(
                  DbmsHostValidatingProperties.of("example.com", 1234, true),
                  CredentialsValidatingProperties.of("foo", "bar"), "patch_database"),
              ConnectionPoolValidatingProperties.of(60000, 10));
      dataSourceValidatingProperties.markAsValidated();

      // When
      DataSourceProperties dataSourceProperties = dataSourceValidatingProperties.convert();

      // Then
      assertAll(() -> assertThat(dataSourceValidatingProperties.isValidated()).isTrue(),
          () -> assertThat(dataSourceProperties.getType()).isEqualTo(DataSourceType.MYSQL),
          () -> assertThat(dataSourceProperties.getTable()).isEqualTo("patch_place_break"),
          () -> assertThat(dataSourceProperties.getDbmsServer())
              .isEqualTo(DbmsServerProperties.of(DbmsHostProperties.of("example.com", 1234, true),
                  CredentialsProperties.of("foo", "bar"), "patch_database")),
          () -> assertThat(dataSourceProperties.getConnectionPool())
              .isEqualTo(ConnectionPoolProperties.of(60000, 10)));
    }

    @Test
    @DisplayName("With properties not marked as validated")
    void withPropertiesNotMarkedAsValidated_shouldThrowException() {
      // Given
      DataSourceValidatingProperties dataSourceValidatingProperties =
          DataSourceValidatingProperties.of(DataSourceType.MYSQL, "patch_place_break",
              DbmsServerValidatingProperties.of(
                  DbmsHostValidatingProperties.of("example.com", 1234, true),
                  CredentialsValidatingProperties.of("foo", "bar"), "patch_database"),
              ConnectionPoolValidatingProperties.of(60000, 10));

      // When
      ThrowingCallable throwingCallable = dataSourceValidatingProperties::convert;

      // Then
      assertAll(() -> assertThat(dataSourceValidatingProperties.isValidated()).isFalse(),
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
      DataSourceValidatingProperties dataSourceValidatingProperties =
          DataSourceValidatingProperties.of(DataSourceType.MYSQL, "patch_place_break",
              DbmsServerValidatingProperties.of(
                  DbmsHostValidatingProperties.of("example.com", 1234, true),
                  CredentialsValidatingProperties.of("foo", "bar"), "patch_database"),
              ConnectionPoolValidatingProperties.of(60000, 10));

      // When
      Set<ConstraintViolation<DataSourceValidatingProperties>> constraintViolations =
          ValidatorTestWrapper.validate(dataSourceValidatingProperties);

      // Then
      assertThat(constraintViolations).isEmpty();
    }

    @Test
    @DisplayName("With only shallow invalid values")
    void withOnlyShallowInvalidValues_shouldGenerateConstraintViolations() {
      // Given
      DataSourceValidatingProperties dataSourceValidatingProperties =
          DataSourceValidatingProperties.of(null, null, null, null);

      // When
      Set<ConstraintViolation<DataSourceValidatingProperties>> constraintViolations =
          ValidatorTestWrapper.validate(dataSourceValidatingProperties);

      // Then
      assertThat(constraintViolations).hasSize(4);
    }

    @Test
    @DisplayName("With only deep invalid values")
    void withOnlyDeepInvalidValues_shouldGenerateConstraintViolations() {
      // Given
      DataSourceValidatingProperties dataSourceValidatingProperties =
          DataSourceValidatingProperties.of(null, null,
              DbmsServerValidatingProperties.of(DbmsHostValidatingProperties.of(null, -1, false),
                  CredentialsValidatingProperties.of("", null), null),
              ConnectionPoolValidatingProperties.of(-1, -1));

      // When
      Set<ConstraintViolation<DataSourceValidatingProperties>> constraintViolations =
          ValidatorTestWrapper.validate(dataSourceValidatingProperties);

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
      void withValidValues_shouldNotGenerateConstraintViolations(@NonNull String validTable) {
        // Given
        DataSourceValidatingProperties dataSourceValidatingProperties =
            DataSourceValidatingProperties.of(DataSourceType.MYSQL, validTable,
                DbmsServerValidatingProperties.of(
                    DbmsHostValidatingProperties.of("example.com", 1234, true),
                    CredentialsValidatingProperties.of("foo", "bar"), "patch_database"),
                ConnectionPoolValidatingProperties.of(60000, 10));

        // When
        Set<ConstraintViolation<DataSourceValidatingProperties>> constraintViolations =
            ValidatorTestWrapper.validate(dataSourceValidatingProperties);

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
      void withInvalidValues_shouldGenerateConstraintViolations(String invalidTable) {
        // Given
        DataSourceValidatingProperties dataSourceValidatingProperties =
            DataSourceValidatingProperties.of(DataSourceType.MYSQL, invalidTable,
                DbmsServerValidatingProperties.of(
                    DbmsHostValidatingProperties.of("example.com", 1234, true),
                    CredentialsValidatingProperties.of("foo", "bar"), "patch_database"),
                ConnectionPoolValidatingProperties.of(60000, 10));

        // When
        Set<ConstraintViolation<DataSourceValidatingProperties>> constraintViolations =
            ValidatorTestWrapper.validate(dataSourceValidatingProperties);

        // Then
        assertThat(constraintViolations).hasSize(1).element(0)
            .matches(constraintViolation -> constraintViolation
                .getRootBeanClass() == DataSourceValidatingProperties.class)
            .matches(constraintViolation -> Objects.equals(constraintViolation.getInvalidValue(),
                invalidTable))
            .matches(constraintViolation -> constraintViolation.getPropertyPath().toString()
                .equals("table"));
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
  @DisplayName("When deserializing from YAML")
  @TestInstance(Lifecycle.PER_CLASS)
  class WhenDeserializingFromYaml {

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With valid content")
    void withValidContent_shouldMatchExpectedValue(@NonNull String yamlFileName,
        @NonNull DataSourceValidatingProperties expectedValue) {
      // Given
      Path yamlFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      Optional<DataSourceValidatingProperties> optionalDataSourceValidatingProperties =
          ConfigSerializerTestWrapper.deserialize(yamlFile, DataSourceValidatingProperties.class);

      // Then
      assertThat(optionalDataSourceValidatingProperties).isPresent().get().isEqualTo(expectedValue);
    }

    private @NonNull Stream<Arguments> withValidContent_shouldMatchExpectedValue() {
      return Stream.of(
          Arguments.of(Named.of("With valid values", "whenDeserializing_withValidValues.conf"),
              DataSourceValidatingProperties.of(DataSourceType.MYSQL, "patch_place_break",
                  DbmsServerValidatingProperties.of(
                      DbmsHostValidatingProperties.of("example.com", 1234, true),
                      CredentialsValidatingProperties.of("foo", "bar"), "patch_database"),
                  ConnectionPoolValidatingProperties.of(60000, 10))),
          Arguments.of(
              Named.of("With unexpected field", "whenDeserializing_withUnexpectedField.conf"),
              DataSourceValidatingProperties.of(DataSourceType.MYSQL, "patch_place_break",
                  DbmsServerValidatingProperties.of(
                      DbmsHostValidatingProperties.of("example.com", 1234, true),
                      CredentialsValidatingProperties.of("foo", "bar"), "patch_database"),
                  ConnectionPoolValidatingProperties.of(60000, 10))),
          Arguments.of(
              Named.of("With 'isValidated' field", "whenDeserializing_withIsValidatedField.conf"),
              DataSourceValidatingProperties.of(DataSourceType.MYSQL, "patch_place_break",
                  DbmsServerValidatingProperties.of(
                      DbmsHostValidatingProperties.of("example.com", 1234, true),
                      CredentialsValidatingProperties.of("foo", "bar"), "patch_database"),
                  ConnectionPoolValidatingProperties.of(60000, 10))));
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
          DataSourceValidatingProperties.class);

      // Then
      assertThatThrownBy(throwingCallable).isInstanceOf(ConfigSerializationException.class)
          .hasCauseExactlyInstanceOf(SerializationException.class);
    }

    private @NonNull Stream<Arguments> withInvalidContent_shouldThrowException() {
      return Stream.of(
          Arguments.of(
              Named.of("With missing 'type' field", "whenDeserializing_withMissingTypeField.conf")),
          Arguments.of(Named.of("With missing 'table' field",
              "whenDeserializing_withMissingTableField.conf")),
          Arguments.of(Named.of("With missing 'dbmsServer' field",
              "whenDeserializing_withMissingDbmsServerField.conf")),
          Arguments.of(Named.of("With missing 'connectionPool' field",
              "whenDeserializing_withMissingConnectionPoolField.conf")));
    }

    @Test
    @DisplayName("With empty content")
    void withEmptyContent_shouldGenerateNullValue() {
      // Given
      String yamlFileName = "whenDeserializing_withEmptyContent.conf";
      Path yamlFile =
          TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      Optional<DataSourceValidatingProperties> dataSourceValidatingProperties =
          ConfigSerializerTestWrapper.deserialize(yamlFile, DataSourceValidatingProperties.class);

      // Then
      assertThat(dataSourceValidatingProperties).isNotPresent();
    }
  }
}
