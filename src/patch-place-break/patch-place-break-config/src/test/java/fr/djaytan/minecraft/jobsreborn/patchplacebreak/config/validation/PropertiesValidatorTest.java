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

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated.ConnectionPoolValidatingProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated.CredentialsValidatingProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated.DataSourceValidatingProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated.DbmsHostValidatingProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.annotated.DbmsServerValidatingProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.config.testutils.ValidatorTestWrapper;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.ConnectionPoolProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.CredentialsProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceType;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DbmsHostProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DbmsServerProperties;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PropertiesValidatorTest {

  private PropertiesValidator propertiesValidator;

  @BeforeEach
  void beforeEach() {
    propertiesValidator = new PropertiesValidator(ValidatorTestWrapper.getValidator());
  }

  @Test
  @DisplayName("When validating with valid values")
  void whenValidating_withValidValues_shouldSuccess() {
    // Given
    DataSourceValidatingProperties dataSourceValidatingProperties =
        DataSourceValidatingProperties.of(
            DataSourceType.MYSQL,
            "patch_place_break",
            DbmsServerValidatingProperties.of(
                DbmsHostValidatingProperties.of("example.com", 1234, true),
                CredentialsValidatingProperties.of("foo", "bar"),
                "patch_database"),
            ConnectionPoolValidatingProperties.of(60000, 10));

    // When
    DataSourceProperties dataSourceProperties =
        propertiesValidator.validate(dataSourceValidatingProperties);

    // Then
    assertAll(
        () -> assertThat(dataSourceValidatingProperties.isValidated()).isTrue(),
        () ->
            assertThat(dataSourceProperties)
                .isEqualTo(
                    DataSourceProperties.of(
                        DataSourceType.MYSQL,
                        "patch_place_break",
                        DbmsServerProperties.of(
                            DbmsHostProperties.of("example.com", 1234, true),
                            CredentialsProperties.of("foo", "bar"),
                            "patch_database"),
                        ConnectionPoolProperties.of(60000, 10))));
  }

  @Test
  @DisplayName("When validating with invalid values")
  void whenValidating_withInvalidValues_shouldThrowException() {
    // Given
    DataSourceValidatingProperties dataSourceValidatingProperties =
        DataSourceValidatingProperties.of(
            DataSourceType.MYSQL,
            "patch_place_break",
            DbmsServerValidatingProperties.of(
                DbmsHostValidatingProperties.of("example.com", -1, true),
                CredentialsValidatingProperties.of("foo", "bar"),
                "patch_database"),
            ConnectionPoolValidatingProperties.of(60000, 10));

    // When
    ThrowingCallable throwingCallable =
        () -> propertiesValidator.validate(dataSourceValidatingProperties);

    // Then
    assertThatThrownBy(throwingCallable).isExactlyInstanceOf(PropertiesValidationException.class);
  }
}
