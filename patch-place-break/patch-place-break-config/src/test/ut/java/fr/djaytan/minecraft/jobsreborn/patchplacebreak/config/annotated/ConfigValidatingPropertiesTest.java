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
import static org.junit.jupiter.api.Assertions.assertAll;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.ConfigProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.ConnectionPoolProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.CredentialsProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DbmsHostProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.properties.DbmsServerProperties;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class ConfigValidatingPropertiesTest {

  @Test
  @DisplayName("When calling equals() & hashCode()")
  void whenCallingEqualsAndHashcode_shouldMetContracts() {
    EqualsVerifier.forClass(ConfigValidatingProperties.class).withRedefinedSuperclass()
        .suppress(Warning.NONFINAL_FIELDS).verify();
  }

  @Test
  @DisplayName("When calling toString()")
  void whenCallingToString_shouldMetContracts() {
    ToStringVerifier.forClass(ConfigValidatingProperties.class).withClassName(NameStyle.SIMPLE_NAME)
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
      ConfigValidatingProperties configValidatingProperties = new ConfigValidatingProperties();

      // Then
      assertAll(() -> assertThat(configValidatingProperties.getDataSource()).isNull(),
          () -> assertThat(configValidatingProperties.isValidated()).isFalse());
    }

    @Test
    @DisplayName("With all args constructor")
    void withAllArgsConstructor_shouldMatchGivenArguments() {
      // Given
      DataSourceValidatingProperties dataSourceValidatingProperties =
          DataSourceValidatingProperties.of(DataSourceType.MYSQL, "patch_place_break",
              DbmsServerValidatingProperties.of(
                  DbmsHostValidatingProperties.of("example.com", 1234, true),
                  CredentialsValidatingProperties.of("foo", "bar"), "patch_database"),
              ConnectionPoolValidatingProperties.of(60000, 10));

      // When
      ConfigValidatingProperties configValidatingProperties =
          ConfigValidatingProperties.of(dataSourceValidatingProperties);

      // Then
      assertAll(
          () -> assertThat(configValidatingProperties.getDataSource())
              .isEqualTo(dataSourceValidatingProperties),
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
      ConfigValidatingProperties configValidatingProperties = ConfigValidatingProperties
          .of(DataSourceValidatingProperties.of(DataSourceType.MYSQL, "patch_place_break",
              DbmsServerValidatingProperties.of(
                  DbmsHostValidatingProperties.of("example.com", 1234, true),
                  CredentialsValidatingProperties.of("foo", "bar"), "patch_database"),
              ConnectionPoolValidatingProperties.of(60000, 10)));
      configValidatingProperties.markAsValidated();

      // When
      ConfigProperties configProperties = configValidatingProperties.convert();

      // Then
      assertAll(() -> assertThat(configValidatingProperties.isValidated()).isTrue(),
          () -> assertThat(configProperties.getDataSource())
              .isEqualTo(DataSourceProperties.of(DataSourceType.MYSQL, "patch_place_break",
                  DbmsServerProperties.of(DbmsHostProperties.of("example.com", 1234, true),
                      CredentialsProperties.of("foo", "bar"), "patch_database"),
                  ConnectionPoolProperties.of(60000, 10))));
    }

    @Test
    @DisplayName("With properties not marked as validated")
    void withPropertiesNotMarkedAsValidated_shouldThrowIllegalStateException() {
      // Given
      ConfigValidatingProperties configValidatingProperties = ConfigValidatingProperties
          .of(DataSourceValidatingProperties.of(DataSourceType.MYSQL, "patch_place_break",
              DbmsServerValidatingProperties.of(
                  DbmsHostValidatingProperties.of("example.com", 1234, true),
                  CredentialsValidatingProperties.of("foo", "bar"), "patch_database"),
              ConnectionPoolValidatingProperties.of(60000, 10)));

      // When
      ThrowingCallable throwingCallable = configValidatingProperties::convert;

      // Then
      assertAll(() -> assertThat(configValidatingProperties.isValidated()).isFalse(),
          () -> assertThatIllegalStateException().isThrownBy(throwingCallable)
              .withMessage("Properties must be validated before being converted"));
    }
  }
}
