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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.config.annotated;

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
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.testutils.ValidatorTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.core.testutils.YamlDeserializerTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;
import jakarta.validation.ConstraintViolation;

class DataSourceValidatingPropertiesIT {

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
      void withValidValues_shouldNotGenerateConstraintViolations(String validTable) {
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

      private Stream<Arguments> withValidValues_shouldNotGenerateConstraintViolations() {
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

      private Stream<Arguments> withInvalidValues_shouldGenerateConstraintViolations() {
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
    void withValidContent_shouldMatchExpectedValue(String yamlFileName,
        DataSourceValidatingProperties expectedValue) {
      // Given
      Path yamlFile = TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      Optional<DataSourceValidatingProperties> optionalDataSourceValidatingProperties =
          YamlDeserializerTestWrapper.deserialize(yamlFile, DataSourceValidatingProperties.class);

      // Then
      assertThat(optionalDataSourceValidatingProperties).isPresent().get().isEqualTo(expectedValue);
    }

    private Stream<Arguments> withValidContent_shouldMatchExpectedValue() {
      return Stream.of(
          Arguments.of(Named.of("With valid values", "whenDeserializing_withValidValues.yml"),
              DataSourceValidatingProperties.of(DataSourceType.MYSQL, "patch_place_break",
                  DbmsServerValidatingProperties.of(
                      DbmsHostValidatingProperties.of("example.com", 1234, true),
                      CredentialsValidatingProperties.of("foo", "bar"), "patch_database"),
                  ConnectionPoolValidatingProperties.of(60000, 10))),
          Arguments.of(
              Named.of("With unexpected field", "whenDeserializing_withUnexpectedField.yml"),
              DataSourceValidatingProperties.of(DataSourceType.MYSQL, "patch_place_break",
                  DbmsServerValidatingProperties.of(
                      DbmsHostValidatingProperties.of("example.com", 1234, true),
                      CredentialsValidatingProperties.of("foo", "bar"), "patch_database"),
                  ConnectionPoolValidatingProperties.of(60000, 10))),
          Arguments.of(
              Named.of("With 'isValidated' field", "whenDeserializing_withIsValidatedField.yml"),
              DataSourceValidatingProperties.of(DataSourceType.MYSQL, "patch_place_break",
                  DbmsServerValidatingProperties.of(
                      DbmsHostValidatingProperties.of("example.com", 1234, true),
                      CredentialsValidatingProperties.of("foo", "bar"), "patch_database"),
                  ConnectionPoolValidatingProperties.of(60000, 10))));
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource
    @DisplayName("With invalid content")
    void withInvalidContent_shouldThrowSerializationException(String yamlFileName) {
      // Given
      Path yamlFile = TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      ThrowingCallable throwingCallable = () -> YamlDeserializerTestWrapper.deserialize(yamlFile,
          DataSourceValidatingProperties.class);

      // Then
      assertThatThrownBy(throwingCallable).isInstanceOf(SerializationException.class);
    }

    private Stream<Arguments> withInvalidContent_shouldThrowSerializationException() {
      return Stream.of(
          Arguments.of(
              Named.of("With missing 'type' field", "whenDeserializing_withMissingTypeField.yml")),
          Arguments.of(Named.of("With missing 'table' field",
              "whenDeserializing_withMissingTableField.yml")),
          Arguments.of(Named.of("With missing 'dbmsServer' field",
              "whenDeserializing_withMissingDbmsServerField.yml")),
          Arguments.of(Named.of("With missing 'connectionPool' field",
              "whenDeserializing_withMissingConnectionPoolField.yml")));
    }

    @Test
    @DisplayName("With empty content")
    void withEmptyContent_shouldGenerateNullValue() {
      // Given
      String yamlFileName = "whenDeserializing_withEmptyContent.yml";
      Path yamlFile = TestResourcesHelper.getClassResourceAsAbsolutePath(this.getClass(), yamlFileName);

      // When
      Optional<DataSourceValidatingProperties> dataSourceValidatingProperties =
          YamlDeserializerTestWrapper.deserialize(yamlFile, DataSourceValidatingProperties.class);

      // Then
      assertThat(dataSourceValidatingProperties).isNotPresent();
    }
  }
}
